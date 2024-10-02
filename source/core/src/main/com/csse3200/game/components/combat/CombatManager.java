package com.csse3200.game.components.combat;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.combat.move.CombatMoveComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;


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

        // Apply confusion effect if it exists.
        checkForConfusion(playerStats);

        enemyAction = selectEnemyMove();
        logger.info("(BEFORE) PLAYER {}: health {}, stamina {}", playerAction, playerStats.getHealth(), playerStats.getStamina());
        logger.info("(BEFORE) ENEMY {}: health {}, stamina {}", enemyAction, enemyStats.getHealth(), enemyStats.getStamina());

        // Execute the selected moves for both player and enemy.
        executeMoveCombination(playerAction, enemyAction);

        // Process any status effects after the actions are taken.
        processStatusEffects(playerStats);
        processStatusEffects(enemyStats);

        checkCombatEnd();
    }

    /**
     * Checks if the player is confused and randomly selects a new action for them if they are.
     *
     * @param playerStats the player's combat statistics, which include status effects.
     */
    public void checkForConfusion(CombatStatsComponent playerStats) {
        if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.CONFUSION)) {
            logger.info("PLAYER is CONFUSED");

            int rand = (int) (Math.random() * 3);
            playerAction = switch (rand) {
                case 0 -> Action.ATTACK;
                case 1 -> Action.GUARD;
                case 2 -> Action.SLEEP;
                default -> null;
            };
        }
    }

    /**
     * Processes status effects such as BLEEDING, which inflicts damage each round.
     *
     * @param entityStats the combat statistics of the entity to process status effects on.
     */
    public void processStatusEffects(CombatStatsComponent entityStats) {
        if (entityStats.hasStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING)) {
            logger.info("{} is BLEEDING", entityStats.isPlayer() ? "PLAYER" : "ENEMY");
            entityStats.setHealth(entityStats.getHealth() - 5);
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
            return Action.SLEEP;
        }

        int rand = enemyMove.hasSpecialMove() ? (int) (Math.random() * 4) : (int) (Math.random() * 3);
        enemyAction = switch (rand) {
            case 0 -> Action.ATTACK;
            case 1 -> Action.GUARD;
            case 2 -> Action.SLEEP;
            case 3 -> Action.SPECIAL;
            default -> null;
        };

        return enemyAction;
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
     * Displays the results of the combat moves in that turn on the game screen in a DialogueBox
     */
    private void displayCombatResults() {
        String[][] moveText;
        String playerMoveDetails = playerAction.name();
        String enemyMoveDetails = enemyAction.name();
        Boolean playerStatChange = false;
        Boolean enemyStatChange = false;

        String[] entityStatChanges = calculateStatChanges();

        if (!entityStatChanges[0].isEmpty()) {
            playerStatChange = true;
        }
        if (!entityStatChanges[1].isEmpty()) {
            enemyStatChange = true;
        }
        logger.info(entityStatChanges[1]);
        logger.info(String.format("The enemyStat change value is %b", enemyStatChange));
        if (playerStatChange && enemyStatChange) {
            logger.info("THERE'S STATS CHANGES FOR PLAYER AND ENEMY");
            moveText = new String[][]{{String.format("You decided to %s", playerMoveDetails),
                    String.format("The enemy decided to %s", enemyMoveDetails), entityStatChanges[0],
                    entityStatChanges[1]}};
        } else if (playerStatChange) {
            moveText = new String[][]{{String.format("You decided to %s", playerMoveDetails),
                    String.format("The enemy decided to %s", enemyMoveDetails), entityStatChanges[0]}};
        } else if (enemyStatChange) {
            moveText = new String[][]{{String.format("You decided to %s", playerMoveDetails),
                    String.format("The enemy decided to %s", enemyMoveDetails), entityStatChanges[1]}};
        } else {
            moveText = new String[][]{{String.format("You decided to %s", playerMoveDetails),
                    String.format("The enemy decided to %s", enemyMoveDetails),
                    "No stats were changed, try again!"}};
        }

        ServiceLocator.getDialogueBoxService().updateText(moveText, DialogueBoxService.DialoguePriority.BATTLE);
        entity.getEvents().trigger("displayCombatResults");
    }
}
