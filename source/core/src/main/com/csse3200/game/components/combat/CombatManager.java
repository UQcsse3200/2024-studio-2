package com.csse3200.game.components.combat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.combat.move.CombatMove;
import com.csse3200.game.components.combat.move.CombatMoveComponent;
import com.csse3200.game.components.combat.quicktimeevent.CombatMoveAudio;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.overlays.CombatAnimationDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The CombatManager class is responsible for managing the turn-based combat loop between two entities (player and enemy).
 * It handles the selection of moves, the sequence in which they are performed, and applies the effects of moves such as
 * attacks, guards, and special actions. The class also checks for any status effects (like confusion or bleeding) and determines
 * when combat has ended based on the health of the player and the enemy.
 */
public class CombatManager extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CombatManager.class);

    /**
     * Enum representing the possible actions in combat: ATTACK, GUARD, SLEEP, SPECIAL, or ITEM.
     */
    public enum Action { ATTACK, GUARD, SLEEP, SPECIAL, ITEM }
    private final CombatAnimationDisplay combatAnimationDisplay = new CombatAnimationDisplay();
    private final CombatMoveAudio combatMoveAudio = new CombatMoveAudio();

    private final Entity player;
    private final Entity enemy;
    private final CombatStatsComponent playerStats;
    private final CombatStatsComponent enemyStats;
    private Action playerAction;
    private Action enemyAction;
    private final CombatMoveComponent playerMove;
    private final CombatMoveComponent enemyMove;

    private AbstractItem playerItem;
    private int playerItemIndex;
    private ItemUsageContext playerItemContext;

    private int statusEffectDuration;
    private boolean moveChangedByConfusion;

    /**
     * Creates a CombatManager that handles the combat sequence between the player and enemy.
     * It initializes player and enemy stats, their moves, and actions.
     *
     * @param player the player entity involved in combat.
     * @param enemy the enemy entity involved in combat.
     */
    public CombatManager(Entity player, Entity enemy) {
        this.player = player;
        this.enemy = enemy;

        this.playerStats = player.getComponent(CombatStatsComponent.class);
        this.enemyStats = enemy.getComponent(CombatStatsComponent.class);

        this.playerAction = null;
        this.enemyAction = null;

        this.playerMove = player.getComponent(CombatMoveComponent.class);
        this.enemyMove = enemy.getComponent(CombatMoveComponent.class);

        this.moveChangedByConfusion = false;
    }

    /**
     * Initialises the event listeners.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("itemConfirmed", this::usePlayerItem);
        entity.addComponent(new CombatStatsChangePopup());
        entity.getComponent(CombatStatsChangePopup.class).create();
    }

    /**
     * Sets player's item as the one passed into this function, then calls for moves to be completed with the
     * player's move being ITEM.
     * @param item to be used.
     * @param index of the item in the original inventory.
     * @param context of the item.
     */
    public void usePlayerItem(AbstractItem item, int index, ItemUsageContext context) {
        logger.debug("Item was confirmed. Using item now.");
        this.playerItem = item;
        this.playerItemIndex = index;
        this.playerItemContext = context;

        onPlayerActionSelected("ITEM");
    }

    /**
     * Sets the player's action based on input and triggers enemy action selection.
     * The move combination is then executed, and status effects are processed at the end of the turn.
     *
     * @param playerActionStr the action chosen by the player as a string.
     */
    public void onPlayerActionSelected(String playerActionStr) {
        try {
            playerAction = Action.valueOf(playerActionStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid player action: {}", playerActionStr);
            return;
        }

        enemyAction = selectEnemyMove();

        handlePlayerConfusion();

        logger.info("(BEFORE) PLAYER {}: health {}, hunger {}", playerAction, playerStats.getHealth(), playerStats.getHunger());
        logger.info("(BEFORE) ENEMY {}: health {}, hunger {}", enemyAction, enemyStats.getHealth(), enemyStats.getHunger());

        // Execute the selected moves for both player and enemy.
        executeMoveCombination(playerAction, enemyAction);

        handleStatusEffects();

        checkCombatEnd();
    }

    /**
     * Randomly select a move to replace the player's selected move if the player has the Confusion status effect
     */
    public void handlePlayerConfusion() {
        if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.CONFUSED)) {
            logger.info("PLAYER is CONFUSED");
            ArrayList<Action> actions = new ArrayList<>(List.of(Action.ATTACK, Action.GUARD, Action.SLEEP));
            actions.remove(playerAction);
            playerAction = actions.get((int) (MathUtils.random() * actions.size()));
            moveChangedByConfusion = true;
        }
    }

    /**
     * Process Special Move status effects on the Player by reducing Player health and/or hunger.
     * Updates the statusEffectDuration and removes expired effects. Confusion only lasts 1 round and is always removed.
     */
    public void handleStatusEffects() {
        // Don't have a status effect, can skip the rest
        if (!playerStats.hasStatusEffect()) return;
        
        //Player has been confused
        if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.CONFUSED) && moveChangedByConfusion) {
                playerStats.removeStatusEffect(CombatStatsComponent.StatusEffect.CONFUSED);
                moveChangedByConfusion = false;
            }
        
        //check if player has been affected by other status effects, handle appropriately
        //note current implementation means if a player is both poisoned and bleeding, they will only be affected by bleed
        if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING)) {
            handleBleed();
        } else if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.POISONED)) {
            handlePoisoned();
        } else if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.SHOCKED)) {
            handleShocked();
        }
    }
    
    private void handleBleed() {
        if (statusEffectDuration == 0) {
            statusEffectDuration = playerStats.getStatusEffectDuration(CombatStatsComponent.StatusEffect.BLEEDING);
        } else {
            // Bleeding reduces health and hunger by 9%, 6%, and 3% respectively each round.
            double reductionMultiplier = (double) (3 * statusEffectDuration) / 100;

            int healthReduction = (int) Math.min(reductionMultiplier * playerStats.getMaxHealth(), playerStats.getHealth());
            int hungerReduction = (int) Math.min(reductionMultiplier * playerStats.getMaxHunger(), playerStats.getHunger());

            playerStats.addHealth(-healthReduction);
            playerStats.addHunger(-hungerReduction);

            entity.getEvents().trigger("statusEffectStatsChangePopup", -healthReduction, -hungerReduction, playerStats);

            if (--statusEffectDuration <= 0) {
                playerStats.removeStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING);
            }
        }
    }
    
    private void handlePoisoned() {
        if (statusEffectDuration == 0) {
            statusEffectDuration = playerStats.getStatusEffectDuration(CombatStatsComponent.StatusEffect.POISONED);
        } else {
            // Poison reduces hunger by 30% each round.
            int hungerReduction = (int) Math.min(0.3 * playerStats.getMaxHunger(), playerStats.getHunger());
            playerStats.addHunger(-hungerReduction);

            entity.getEvents().trigger("statusEffectStatsChangePopup", 0, -hungerReduction, playerStats);

            if (--statusEffectDuration <= 0) {
                playerStats.removeStatusEffect(CombatStatsComponent.StatusEffect.POISONED);
            }
        }
    }
    
    private void handleShocked() {
        if (statusEffectDuration == 0) {
            statusEffectDuration = playerStats.getStatusEffectDuration(CombatStatsComponent.StatusEffect.SHOCKED);
        } else {
            // Shock reduces health by 15% each round.
            int healthReduction = (int) Math.min(0.15 * playerStats.getMaxHealth(), playerStats.getHealth());
            playerStats.addHealth(-healthReduction);

            entity.getEvents().trigger("statusEffectStatsChangePopup", -healthReduction, 0, playerStats);

            if (--statusEffectDuration <= 0) {
                playerStats.removeStatusEffect(CombatStatsComponent.StatusEffect.SHOCKED);
            }
        }
    }

    /**
     * Selects an enemy move based on stats and randomisation.
     *
     * @return the selected action for the enemy.
     */
    private Action selectEnemyMove() {
        // Initialise moves with default probabilities
        HashMap<Action, Double> actionProbabilities = new HashMap<>();
        actionProbabilities.put(Action.ATTACK, 0.3);
        actionProbabilities.put(Action.GUARD, 0.2);
        actionProbabilities.put(Action.SLEEP, 0.1);

        // Add special move if available, if the player does not have an active status effect, and if the player did not use an item
        if (enemyMove.hasSpecialMove() && !playerStats.hasStatusEffect() && playerAction != Action.ITEM) {
            actionProbabilities.put(Action.SPECIAL, 0.2); // Default probability
        }

        // Don't sleep if both health and hunger are maxed out
        if (enemyStats.getHunger() == enemyStats.getMaxHunger() && enemyStats.getHealth() == enemyStats.getMaxHealth()) {
            actionProbabilities.remove(Action.SLEEP);
        }

        // Adjust probabilities based on stats:
        // Higher chance of sleeping or guarding when health is low
        if (enemyStats.getHealth() < enemyStats.getMaxHealth() * 0.3 ||
                enemyStats.getHunger() < enemyStats.getMaxHunger() * 0.3) {
            actionProbabilities.put(Action.GUARD, 0.4);
            if (actionProbabilities.containsKey(Action.SLEEP)) {
                actionProbabilities.put(Action.SLEEP, 0.6);
            }
        }
        // Higher chance to attack when player health is low
        if (playerStats.getHealth() < playerStats.getMaxHealth() * 0.3) {
            actionProbabilities.put(Action.ATTACK, 0.7);
        }

        // Normalize probabilities to sum up to 1
        double totalProbability = actionProbabilities.values().stream().mapToDouble(Double::doubleValue).sum();
        HashMap<Action, Double> normalizedProbabilities = new HashMap<>();
        for (Action action : actionProbabilities.keySet()) {
            normalizedProbabilities.put(action, actionProbabilities.get(action) / totalProbability);
        }

        // Select an action based on weighted probabilities
        double rand = MathUtils.random();
        double cumulativeProbability = 0.0;
        for (Action action : normalizedProbabilities.keySet()) {
            cumulativeProbability += normalizedProbabilities.get(action);
            if (rand <= cumulativeProbability) {
                return action;
            }
        }

        // Fallback to random action if no choice was made
        List<Action> availableActions = new ArrayList<>(normalizedProbabilities.keySet());
        int randomIndex = (int) (MathUtils.random() * availableActions.size());
        return availableActions.get(randomIndex);
    }

    /**
     * Executes the player's and enemy's selected moves in combination based on their respective actions.
     * This method handles different move interactions and applies combat logic accordingly.
     *
     * @param playerAction the player's selected action.
     * @param enemyAction the enemy's selected action.
     */
    private void executeMoveCombination(Action playerAction, Action enemyAction) {
        if (playerAction == null || enemyAction == null) {
            logger.error("Both player and enemy actions must be determined.");
            return;
        }
        if (playerMove == null) {
            logger.error("Player does not have a CombatMoveComponent.");
            return;
        }
        if (enemyMove == null) {
            logger.error("Enemy does not have a CombatMoveComponent.");
            return;
        }

        combatMoveAudio.playCombatSound(playerAction, enemyAction);
        combatAnimationDisplay.animateCombat(playerAction, enemyAction, getFasterEntity() == player);

        CombatMove.StatsChange[] playerStatsChanges;
        CombatMove.StatsChange[] enemyStatsChanges;

        switch (playerAction) {
            case ATTACK -> {
                switch (enemyAction) {
                    case ATTACK -> {
                        if (getFasterEntity() == player) {
                            playerStatsChanges = playerMove.executeMove(playerAction, enemyStats);
                            enemyStatsChanges = enemyMove.executeMove(enemyAction, playerStats);
                        } else {
                            enemyStatsChanges = enemyMove.executeMove(enemyAction, playerStats);
                            playerStatsChanges = playerMove.executeMove(playerAction, enemyStats);
                        }
                    }
                    case GUARD -> {
                        enemyStatsChanges = enemyMove.executeMove(enemyAction);
                        playerStatsChanges = playerMove.executeMove(playerAction, enemyStats, true);
                    }
                    case SLEEP -> {
                        enemyStatsChanges = enemyMove.executeMove(enemyAction);
                        playerStatsChanges = playerMove.executeMove(playerAction, enemyStats, false, getMultiHitsLanded());
                    }
                    case SPECIAL -> {
                        enemyStatsChanges = enemyMove.executeMove(enemyAction, playerStats, false);
                        playerStatsChanges = playerMove.executeMove(playerAction, enemyStats);
                    }
                    default -> throw new GdxRuntimeException("Unknown enemy action: " + enemyAction);
                }
            }
            case GUARD -> {
                switch(enemyAction) {
                    case ATTACK, SPECIAL -> {
                        playerStatsChanges = playerMove.executeMove(playerAction);
                        enemyStatsChanges = enemyMove.executeMove(enemyAction, playerStats, true);
                    }
                    case GUARD, SLEEP -> {
                        playerStatsChanges = playerMove.executeMove(playerAction);
                        enemyStatsChanges = enemyMove.executeMove(enemyAction);
                    }
                    default -> throw new GdxRuntimeException("Unknown enemy action: " + enemyAction);
                }
            }
            case SLEEP -> {
                switch(enemyAction) {
                    case ATTACK -> {
                        playerStatsChanges = playerMove.executeMove(playerAction);
                        enemyStatsChanges = enemyMove.executeMove(enemyAction, playerStats, false, getMultiHitsLanded());
                    }
                    case GUARD, SLEEP -> {
                        playerStatsChanges = playerMove.executeMove(playerAction);
                        enemyStatsChanges = enemyMove.executeMove(enemyAction);
                    }
                    case SPECIAL -> {
                        playerStatsChanges = playerMove.executeMove(playerAction);
                        enemyStatsChanges = enemyMove.executeMove(enemyAction, playerStats, false);
                    }
                    default -> throw new GdxRuntimeException("Unknown enemy action: " + enemyAction);
                }
            }
            case ITEM -> {
                int initialHealth = playerStats.getHealth();
                int initialHunger = playerStats.getHunger();
                // Player's move is using an item in the CombatInventoryDisplay.
                entity.getEvents().trigger("itemUsedInCombat", playerItem, playerItemContext, playerItemIndex);
                enemyStatsChanges = enemyMove.executeMove(enemyAction);
                entity.getEvents().trigger("useItem", playerStats, enemyStats);
                playerStatsChanges = new CombatMove.StatsChange[]{new CombatMove.StatsChange(playerStats.getHealth()
                        - initialHealth, playerStats.getHunger() - initialHunger)};
            }
            default -> throw new GdxRuntimeException("Unknown player action: " + playerAction);
        }

        logger.info("(AFTER) PLAYER: health {}, hunger {}", playerStats.getHealth(), playerStats.getHunger());
        logger.info("(AFTER) ENEMY: health {}, hunger {}", enemyStats.getHealth(), enemyStats.getHunger());
        displayCombatResults();

        // Trigger stats change popup animations
        for (CombatMove.StatsChange statsChange : enemyStatsChanges) {
            entity.getEvents().trigger("enemyHungerStatsChangePopup", statsChange.getHungerChange());
            entity.getEvents().trigger("enemyHealthStatsChangePopup", statsChange.getHealthChange());
        }
        for (CombatMove.StatsChange statsChange : playerStatsChanges) {
            entity.getEvents().trigger("playerHungerStatsChangePopup", statsChange.getHungerChange());
            entity.getEvents().trigger("playerHealthStatsChangePopup", statsChange.getHealthChange());
        }
    }

    /**
     * Determines the faster entity based on their speed stat.
     *
     * @return the entity with the higher speed stat.
     */
    private Entity getFasterEntity() {
        return playerStats.getSpeed() >= enemyStats.getSpeed() ? player : enemy;
    }

    /**
     * Checks if combat has ended by evaluating the health of both the player and the enemy.
     * Triggers events based on whether the player has won or lost the combat.
     */
    private void checkCombatEnd() {
        if (playerStats.getHealth() <= 0) {
            if (enemy.getComponent(CombatStatsComponent.class).isBoss()) {
                this.getEntity().getEvents().trigger("bossCombatLoss", enemy);
                GameState.resetState();
                SaveHandler.delete(GameState.class, "saves", FileLoader.Location.LOCAL);
            } else {
                this.getEntity().getEvents().trigger("combatLoss", enemy);
                //Clear inventory/other normal death events
            }
            // nullifyCombatDialogueListener(); // remove the listener added for animation syncing
        } else if (enemyStats.getHealth() <= 0) {
            if (enemy.getComponent(CombatStatsComponent.class).isBoss()) {
                entity.getEvents().trigger("bossCombatWin", enemy);
            } else {
                this.getEntity().getEvents().trigger("combatWin", enemy);
            }
            // nullifyCombatDialogueListener(); // remove the listener added for animation syncing
        }
    }

    /**
     * Simulates a multi-hit attack.
     * The number of hits is based on the entity's speed and is calculated using a Bernoulli distribution.
     *
     * @return the number of successful hits landed in a multi-hit attack.
     */
    private int getMultiHitsLanded() {
        double successProbability = Math.exp(enemyStats.getSpeed() / 250.0) - 0.5;
        int successfulHits = 0;
        for (int i = 0; i < 4; i++) {
            successfulHits += (MathUtils.random() < successProbability) ? 1 : 0;
        }
        return successfulHits;
    }

    /**
     * @return the player entity.
     */
    public Entity getPlayer() {
        return player;
    }

    /**
     * @return the enemy entity.
     */
    public Entity getEnemy() {
        return enemy;
    }

    /**
     * @return the stats component of the player.
     */
    public CombatStatsComponent getPlayerStats() {
        return playerStats;
    }

    /**
     * @return the stats component of the enemy.
     */
    public CombatStatsComponent getEnemyStats() {
        return enemyStats;
    }

    /**
     * A function used to construct the strings describing Status Effects applied to the Player
     * @return A string array containing the details of status effects (Bleeding, Shocked, or Poisoned).
     */
    private String playerStatusEffects() {
        String effectDetails = "";
        if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING) && statusEffectDuration == 0) {
            effectDetails += "The Kanga's claws have left their mark. Watch your step...";
        } else if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.POISONED)
                && statusEffectDuration == 0) {
            effectDetails += "The Leviathan's venom runs deep. Rest offers no reprieve.";
        } else if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.SHOCKED)
                && statusEffectDuration == 0) {
            effectDetails += "A jolt from the Griffin lingers, sparking faint tremors within.";
        }
        return effectDetails;
    }

    /**
     * Displays the results of the combat moves in that turn on the game screen in a DialogueBox
     */
    private void displayCombatResults() {
        List<String> moveTextList = new ArrayList<>();
        String playerMoveDetails = playerAction.name();
        String enemyMoveDetails = enemyAction.name();

        if (moveChangedByConfusion) {
            moveTextList.add(String.format("Your mind is foggy, and you find yourself %sing.", playerMoveDetails));
        } else if (playerMoveDetails.equals("ITEM")) {
            moveTextList.add(String.format("You decided to use an %s.", playerMoveDetails));
        } else {
            moveTextList.add(String.format("You decided to %s.", playerMoveDetails));
        }
        if (enemyMoveDetails.equals("SLEEP") || enemyMoveDetails.equals("GUARD")) {
            moveTextList.add(String.format("The enemy decided to %s!", enemyMoveDetails));
        } else {
            moveTextList.add(String.format("The enemy used their %s!", enemyMoveDetails));
        }

        String statusEffects = playerStatusEffects();

        if (!statusEffects.isEmpty()) {
            moveTextList.add(statusEffects);
        }

        // Convert the ArrayList to a 2D array for updateText
        String[][] moveText = new String[1][moveTextList.size()];
        moveText[0] = moveTextList.toArray(new String[0]);

        ServiceLocator.getDialogueBoxService().updateText(moveText, DialogueBoxService.DialoguePriority.BATTLE);

        entity.getEvents().trigger("displayCombatResults");
    }
}
