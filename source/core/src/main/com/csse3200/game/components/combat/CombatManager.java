package com.csse3200.game.components.combat;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.combat.move.CombatMoveComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.overlays.CombatAnimationDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.Random;
import java.util.ArrayList;
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

    private final Entity player;
    private final Entity enemy;
    private CombatStatsComponent copyPlayerStats;
    private CombatStatsComponent copyEnemyStats;
    private final CombatStatsComponent playerStats;
    private final CombatStatsComponent enemyStats;
    private Action playerAction;
    private Action enemyAction;
    private final CombatMoveComponent playerMove;
    private final CombatMoveComponent enemyMove;
    private InputListener dialogueBoxCombatListener;
    private TextButton contButton;
    private AbstractItem playerItem;
    private int playerItemIndex;
    private ItemUsageContext playerItemContext;

    private int statusEffectDuration;
    private Boolean moveChangedByConfusion;


    // HashMap stores information on enemies when attack
    static Map<String,ArrayList> EnemyMoveStore;

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

        initStatsCopies();

        this.playerAction = null;
        this.enemyAction = null;

        this.playerMove = player.getComponent(CombatMoveComponent.class);
        this.enemyMove = enemy.getComponent(CombatMoveComponent.class);

        this.moveChangedByConfusion = false;
       // enemies = new HashMap<String,Entity>();
        EnemyMoveStore = new LinkedHashMap<>();
    }

    /**
     * Initialises the event listeners.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("itemConfirmed", this::usePlayerItem);
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
     * Initialises copies of the CombatStatsComponents of the player and enemy for DialogueBox use
     */
    private void initStatsCopies() {
        this.copyPlayerStats = new CombatStatsComponent(playerStats.getMaxHealth(), playerStats.getMaxHunger(),
                playerStats.getStrength(), playerStats.getDefense(), playerStats.getSpeed(),
                playerStats.getMaxExperience(), playerStats.getMaxStamina(), playerStats.isPlayer(),
                playerStats.isBoss(), playerStats.getLevel());
        copyPlayerStats.setHealth(playerStats.getHealth());
        copyPlayerStats.setExperience(playerStats.getExperience());
        copyPlayerStats.setHunger(playerStats.getHunger());
        copyPlayerStats.setStamina(playerStats.getStamina());
        copyPlayerStats.setLevel(playerStats.getLevel());

        this.copyEnemyStats = new CombatStatsComponent(enemyStats.getMaxHealth(), enemyStats.getMaxHunger(),
                enemyStats.getStrength(), enemyStats.getDefense(), enemyStats.getSpeed(),
                enemyStats.getMaxExperience(), enemyStats.getMaxStamina(), enemyStats.isPlayer(), enemyStats.isBoss(), enemyStats.getLevel());
        copyEnemyStats.setHealth(enemyStats.getHealth());
        copyEnemyStats.setExperience(enemyStats.getExperience());
        copyEnemyStats.setHunger(enemyStats.getHunger());
        copyEnemyStats.setStamina(enemyStats.getStamina());
        copyEnemyStats.setLevel(enemyStats.getLevel());

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

        logger.info("(BEFORE) PLAYER {}: health {}, stamina {}", playerAction, playerStats.getHealth(), playerStats.getStamina());
        logger.info("(BEFORE) ENEMY {}: health {}, stamina {}", enemyAction, enemyStats.getHealth(), enemyStats.getStamina());

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
            playerAction = actions.get((int) (Math.random() * actions.size()));
            moveChangedByConfusion = true;
        }
    }

    /**
     * Process Special Move status effects on the Player by reducing Player health and/or stamina.
     * Updates the statusEffectDuration and removes expired effects. Confusion only lasts 1 round and is always removed.
     */
    public void handleStatusEffects() {
        if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.CONFUSED)) {
            if (moveChangedByConfusion) {
                playerStats.removeStatusEffect(CombatStatsComponent.StatusEffect.CONFUSED);
                moveChangedByConfusion = false;
            }
        }
        if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING)) {
            if (statusEffectDuration == 0) {
                statusEffectDuration = playerStats.getStatusEffectDuration(CombatStatsComponent.StatusEffect.BLEEDING);
            } else {
                // Bleeding reduces health and stamina by 9%, 6%, and 3% respectively each round.
                double reductionMultiplier = (double) (-3 * statusEffectDuration) / 100;
                playerStats.addHealth((int) (reductionMultiplier * playerStats.getMaxHealth()));
                playerStats.addStamina((int) (reductionMultiplier * playerStats.getMaxStamina()));
                if (--statusEffectDuration <= 0) {
                    playerStats.removeStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING);
                }
            }
        } else if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.POISONED)) {
            if (statusEffectDuration == 0) {
                statusEffectDuration = playerStats.getStatusEffectDuration(CombatStatsComponent.StatusEffect.POISONED);
            } else {
                // Poison reduces stamina by 30% each round.
                playerStats.addStamina((int) (-0.3 * playerStats.getMaxStamina()));
                if (--statusEffectDuration <= 0) {
                    playerStats.removeStatusEffect(CombatStatsComponent.StatusEffect.POISONED);
                }
            }
        } else if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.SHOCKED)) {
            if (statusEffectDuration == 0) {
                statusEffectDuration = playerStats.getStatusEffectDuration(CombatStatsComponent.StatusEffect.SHOCKED);
            } else {
                // Shock reduces health by 15% each round.
                playerStats.addHealth((int) (-0.15 * playerStats.getMaxHealth()));
                if (--statusEffectDuration <= 0) {
                    playerStats.removeStatusEffect(CombatStatsComponent.StatusEffect.SHOCKED);
                }
            }
        }
    }

    /**
     * Randomly selects an enemy move from ATTACK, GUARD, SLEEP, or SPECIAL.
     *
     * @return the selected action for the enemy.
     */
    private Action selectEnemyMove() {
        Action enemyAction;

        if (enemyStats.getStamina() < 25) {
            UpdateEnemyMoveStore(Action.SLEEP);
            return Action.SLEEP;
        }

        int rand = (enemyMove.hasSpecialMove() && !playerStats.hasStatusEffect()) ?
                (int) (Math.random() * 4) : (int) (Math.random() * 3);
        enemyAction = switch (rand) {
            case 0 -> Action.ATTACK;
            case 1 -> Action.GUARD;
            case 2 -> Action.SLEEP;
            case 3 -> Action.SPECIAL;
            default -> null;
        };

        //stores enemyAction
        UpdateEnemyMoveStore(enemyAction);
        return enemyAction;
    }
    /**
     * Updates the stored sequence of enemy moves for a specific enemy type.
     * This is used to determine if a special move can be executed after a certain move combination.
     *
     * @param value The enemy action to store.
     */

    private void UpdateEnemyMoveStore(Action value) {
        ArrayList itemsList = EnemyMoveStore.get(enemy.getEnemyType().toString());

        if (itemsList == null)
        {
            itemsList = new ArrayList();
            logger.info("empty hashmap :: Updating special move list");
        }
        else
        //particular enemy has already made a move
        {
            logger.info("removing existing record in hashmap");
            itemsList = EnemyMoveStore.remove(enemy.getEnemyType().toString());
        }


        itemsList.add(value);
        EnemyMoveStore.put(enemy.getEnemyType().toString(), itemsList);
        if(itemsList.size()>2)
        {
            logger.info("1- item list size: {}", itemsList.size());
            CheckSpecialMoveCombination();
        }

    }
    /**
     * Verifies if the last three moves in the enemy's sequence match a predefined special move combination.
     * Triggers special effects if the combination is achieved, otherwise removes outdated moves.
     */

    private void CheckSpecialMoveCombination()
    {
        boolean NoSpecialMoveComboFlag= false;
        ArrayList itemsList = EnemyMoveStore.get(enemy.getEnemyType().toString());
        logger.info("Checking special move combination");
        for (Map.Entry<String, ArrayList> entry : EnemyMoveStore.entrySet()) {

    logger.info("Map<String,ArrayList> ::  " +entry.getKey()+ " :: "+entry.getValue() );
}
        String enemyMoves = "";

        // compare enemy move seq to last 3 enemy moves)
        switch (enemy.getEnemyType().toString())
        {
            case "FROG" -> {
                for (Map.Entry<String, ArrayList> entry : EnemyMoveStore.entrySet()){

                    enemyMoves += entry.getValue().toString();
                    enemyMoves += ", ";

                }
                enemyMoves+="";
                logger.info("enemy move combination" +enemyMoves);
                if (enemyMoves=="[ATTACK, ATTACK, ATTACK, ]"){
                    logger.info("special move combination achieved");
                    //special effect
                }
            }
            case "CHICKEN" -> {
                for (Map.Entry<String, ArrayList> entry : EnemyMoveStore.entrySet()){

                    enemyMoves += entry.getValue().toString();
                    enemyMoves += ", ";

                }
                enemyMoves+="";
                if (enemyMoves=="[ATTACK, ATTACK, GUARD, ]"){
                }

            }
            case "MONKEY" -> {
                for (Map.Entry<String, ArrayList> entry : EnemyMoveStore.entrySet()){

                    enemyMoves += entry.getValue().toString();
                    enemyMoves += ", ";

                }
                enemyMoves+="";
                if (enemyMoves=="[ATTACK, GUARD, ATTACK, ]"){
                }

            }
            case "BEAR" -> {
                for (Map.Entry<String, ArrayList> entry : EnemyMoveStore.entrySet()){

                    enemyMoves += entry.getValue().toString();
                    enemyMoves += ", ";

                }
                enemyMoves+="";
                if (enemyMoves=="[GUARD, ATTACK, ATTACK, ]"){
                }

            }
            default -> {
                NoSpecialMoveComboFlag = true;
            }

        }

        if(NoSpecialMoveComboFlag)
        {
            //remove outdated enemy action
            itemsList.remove(0);
        }
        else
        {
            //reset enemy move for next special move set
            itemsList.clear();
            //call special effect
        }
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

        switch (playerAction) {
            case ATTACK -> {
                combatAnimationDisplay.initiateAnimation(Action.ATTACK);
                switch (enemyAction) {
                    case ATTACK -> {
                        if (getFasterEntity() == player) {
                            playerMove.executeMove(playerAction, enemyStats);
                            enemyMove.executeMove(enemyAction, playerStats);
                        } else {
                            enemyMove.executeMove(enemyAction, playerStats);
                            playerMove.executeMove(playerAction, enemyStats);
                        }
                    }
                    case GUARD -> {
                        enemyMove.executeMove(enemyAction);
                        playerMove.executeMove(playerAction, enemyStats, true);
                    }
                    case SLEEP -> {
                        enemyMove.executeMove(enemyAction);
                        playerMove.executeMove(playerAction, enemyStats, false, getEnemyMultiHitsLanded());
                    }
                    case SPECIAL -> {
                        enemyMove.executeMove(enemyAction, playerStats, false);
                        playerMove.executeMove(playerAction, enemyStats);
                    }
                }
            }
            case GUARD -> {
                combatAnimationDisplay.initiateAnimation(Action.GUARD);
                switch(enemyAction) {
                    case ATTACK, SPECIAL -> {
                        playerMove.executeMove(playerAction);
                        enemyMove.executeMove(enemyAction, playerStats, true);
                    }
                    case GUARD, SLEEP -> {
                        playerMove.executeMove(playerAction);
                        enemyMove.executeMove(enemyAction);
                    }
                }
            }
            case SLEEP -> {
                combatAnimationDisplay.initiateAnimation(Action.SLEEP);
                switch(enemyAction) {
                    case ATTACK -> {
                        playerMove.executeMove(playerAction);
                        enemyMove.executeMove(enemyAction, playerStats, false, getEnemyMultiHitsLanded());
                    }
                    case GUARD, SLEEP -> {
                        playerMove.executeMove(playerAction);
                        enemyMove.executeMove(enemyAction);
                    }
                    case SPECIAL -> {
                        playerMove.executeMove(playerAction);
                        enemyMove.executeMove(enemyAction, playerStats, false);
                    }
                }
            }
            case ITEM -> {
                // Player's move is using an item in the CombatInventoryDisplay.
                entity.getEvents().trigger("itemMove", playerItem, playerItemIndex, playerItemContext);
                enemyMove.executeMove(enemyAction);
            }
        }

        logger.info("(AFTER) PLAYER: health {}, stamina {}", playerStats.getHealth(), playerStats.getStamina());
        logger.info("(AFTER) ENEMY: health {}, stamina {}", enemyStats.getHealth(), enemyStats.getStamina());
        displayCombatResults();
        initStatsCopies();
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
                this.getEntity().getEvents().trigger("combatLossBoss");
                GameState.resetState();
                SaveHandler.delete(GameState.class, "saves", FileLoader.Location.LOCAL);
            } else {
                this.getEntity().getEvents().trigger("combatLoss");
                //Clear inventory/other normal death events
            }
            // nullifyCombatDialogueListener(); // remove the listener added for animation syncing
        } else if (enemyStats.getHealth() <= 0) {
            if (enemy.getEnemyType() == Entity.EnemyType.KANGAROO) {
                this.getEntity().getEvents().trigger("landBossDefeated");
            } else if (enemy.getEnemyType() == Entity.EnemyType.WATER_BOSS) {
                this.getEntity().getEvents().trigger("waterBossDefeated");
            } else if (enemy.getEnemyType() == Entity.EnemyType.AIR_BOSS) {
                this.getEntity().getEvents().trigger("airBossDefeated");
            } else {
                this.getEntity().getEvents().trigger("combatWin", enemy);
            }
            // nullifyCombatDialogueListener(); // remove the listener added for animation syncing
        }
    }

    final Random random = new Random();

    /**
     * Simulates a multi-hit attack by the enemy.
     * The number of hits is based on the enemy's speed and is calculated using a Bernoulli distribution.
     *
     * @return the number of successful hits landed by the enemy in a multi-hit attack.
     */
    private int getEnemyMultiHitsLanded() {
        double successProbability = Math.exp(enemyStats.getSpeed() / 250.0) - 0.5;
        int successfulHits = 0;
        for (int i = 0; i < 4; i++) {
            successfulHits += (random.nextDouble() < successProbability) ? 1 : 0;
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
     * A function used to calculate and construct the strings describing player and enemy changes
     * @return A string array containing the stat change details of the player and enemy
     */
    private String[] calculateStatChanges () {
        int arraySize = 2;
        String[] statChanges = new String[arraySize];
        String playerStatsDetails = "";
        String enemyStatsDetails = "";

        if (playerStats.getHealth() > copyPlayerStats.getHealth()) {
            playerStatsDetails += String.format("You gained %dHP. ", playerStats.getHealth() - copyPlayerStats.getHealth());
        } else if (playerStats.getHealth() < copyPlayerStats.getHealth()) {
            playerStatsDetails += String.format("You lost %dHP. ", copyPlayerStats.getHealth() - playerStats.getHealth());
        }

        if (playerStats.getStamina() > copyPlayerStats.getStamina()) {
            playerStatsDetails += String.format("You gained %d stamina. ", playerStats.getStamina() -
                    copyPlayerStats.getStamina());
        } else if (playerStats.getStamina() < copyPlayerStats.getStamina()) {
            playerStatsDetails += String.format("You lost %d stamina. ", copyPlayerStats.getStamina() -
                    playerStats.getStamina());
        }

        if (playerStats.getStrength() > copyPlayerStats.getStrength()) {
            playerStatsDetails += String.format("You gained %d strength. ", playerStats.getStrength() -
                    copyPlayerStats.getStrength());
        }

        if (playerStats.getDefense() > copyPlayerStats.getDefense()) {
            playerStatsDetails += String.format("You gained %d defense. ", playerStats.getDefense() -
                    copyPlayerStats.getDefense());
        }

        if (enemyStats.getHealth() > copyEnemyStats.getHealth()) {
            enemyStatsDetails += String.format("The enemy gained %dHP. ", enemyStats.getHealth() - copyEnemyStats.getHealth());
        } else if (enemyStats.getHealth() < copyEnemyStats.getHealth()) {
            enemyStatsDetails += String.format("The enemy lost %dHP. ", copyEnemyStats.getHealth() - enemyStats.getHealth());
        }

        statChanges[0] = playerStatsDetails;
        statChanges[1] = enemyStatsDetails;

        return statChanges;
    }

    /**
     * A function used to construct the strings describing Status Effects applied to the Player
     * @return A string array containing the details of status effects (Bleeding, Shocked, or Poisoned).
     */
    private String playerStatusEffects() {
        String effectDetails = "";
        if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING) && statusEffectDuration == 0) {
            effectDetails += "You're bleeding! Your GUARDs will be less effective.";
        } else if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.POISONED)
                && statusEffectDuration == 0) {
            effectDetails += "You've been poisoned! SLEEPing won't heal you.";
        } else if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.SHOCKED)
                && statusEffectDuration == 0) {
            effectDetails += "You've been shocked! Your ATTACKs will be weakened.";
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
        boolean playerStatChange = false;
        boolean enemyStatChange = false;

        String[] entityStatChanges = calculateStatChanges();

        if (!entityStatChanges[0].isEmpty()) {
            playerStatChange = true;
        }
        if (!entityStatChanges[1].isEmpty()) {
            enemyStatChange = true;
        }
        logger.info(entityStatChanges[1]);
        logger.info(String.format("The enemyStat change value is %b", enemyStatChange));


        if (moveChangedByConfusion) {
            moveTextList.add(String.format("The enemy confused you into %sing!", playerMoveDetails));
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

        if (playerStatChange) {
            moveTextList.add(entityStatChanges[0]);
        }
        if (enemyStatChange) {
            moveTextList.add(entityStatChanges[1]);
        }

        String statusEffects = playerStatusEffects();

        if (!statusEffects.isEmpty()) {
            moveTextList.add(statusEffects);
        }

        // Convert the ArrayList to a 2D array for updateText
        String[][] moveText = new String[1][moveTextList.size()];
        moveText[0] = moveTextList.toArray(new String[0]);

        ServiceLocator.getDialogueBoxService().updateText(moveText);

        // Add the listener to initiate enemy animations when enemy move indicated on dialogue box:
        addDialogueBoxListener();

        entity.getEvents().trigger("displayCombatResults");
    }

    /**
     * Add listener to continue button in dialogue box in combat to allow syncing of
     * enemy animations after player animations and when continue button is pressed as enemy attack is
     * described in dialogue box
     */
    public void addDialogueBoxListener() {

        // Get the continue button for the dialogue box
        contButton = ServiceLocator.getDialogueBoxService().getCurrentOverlay().getForwardButton();

        dialogueBoxCombatListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                combatAnimationDisplay.dispose();

                Label db = ServiceLocator.getDialogueBoxService().getCurrentOverlay().getLabel();
                String currentText = String.valueOf(db.getText());

                //            TODO: replace Label code with code below due in next PR
                //            int index = ServiceLocator.getDialogueBoxService().getCurrentOverlay().getCurrentHint();
                //            int index2 = ServiceLocator.getDialogueBoxService().getCurrentOverlay().getCurrentHintLine();
                //            String[][] fullText = (ServiceLocator.getDialogueBoxService().getHints());
                //            String currentText = String.valueOf(fullText[index][index2]);

                if (currentText.equals("The enemy decided to ATTACK")){
                    combatAnimationDisplay.initiateEnemyAnimation(Action.ATTACK);
                } else if (currentText.equals("The enemy decided to SLEEP")){
                    combatAnimationDisplay.initiateEnemyAnimation(Action.SLEEP);
                } else if (currentText.equals("The enemy decided to GUARD")){
                    combatAnimationDisplay.initiateEnemyAnimation(Action.GUARD);
                }

                return true;
            }
        };

        contButton.addListener(dialogueBoxCombatListener); // add the listener to the button for the duration of combat
    }

    /**
     * Remove the input listener for the continue button of the dialogue box used to
     * sync the animations of enemy players with when continue button was clicked
     */
    private void nullifyCombatDialogueListener(){
        if (dialogueBoxCombatListener != null) {
            contButton.removeListener(dialogueBoxCombatListener);
            dialogueBoxCombatListener = null;
        }
    }


}
