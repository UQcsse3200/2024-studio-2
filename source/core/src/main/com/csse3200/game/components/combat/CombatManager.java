package com.csse3200.game.components.combat;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.combat.move.CombatMoveComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
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

        this.playerAction = null;
        this.enemyAction = null;

        this.playerMove = player.getComponent(CombatMoveComponent.class);
        this.enemyMove = enemy.getComponent(CombatMoveComponent.class);
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

        checkCombatEnd();
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
            entity.getEvents().trigger("combatLoss");
        } else if (enemyStats.getHealth() <= 0) {
            entity.getEvents().trigger("combatWin");
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
}
