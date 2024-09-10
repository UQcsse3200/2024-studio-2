package com.csse3200.game.components.combat;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.combat.move.CombatMoveComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

/**
 * Manages the turn-based combat loop and handles attacks
 */
public class CombatManager extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CombatManager.class);

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
     * A combat manager which handles enemy move selection, player and enemy move combinations, move sequencing,
     * combat logic, and checking whether the combat sequence is over.
     * @param player player entity involved in combat.
     * @param enemy enemy entity involved in combat.
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
     * Both opponents' actions need to be determined before either action is enacted, as the sequencing of the moves
     * will be dependent on both of the choices.
     * @param playerActionStr selected move as per button press (from CombatActions).
     */
    public void onPlayerActionSelected(String playerActionStr)
    {
        // Map the string input to the corresponding enum value
        try {
            playerAction = Action.valueOf(playerActionStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid player action: {}", playerActionStr);
            return;
        }

        // The effect of confusion is applied at the start of the round after the player has selected their move.
        checkForConfusion(playerStats);

        enemyAction = selectEnemyMove();

        logger.info("Player action = {}, enemy action = {}", playerAction, enemyAction);

        executeMoveCombination(playerAction, enemyAction);

        // The effect of any status ailments that afflict the player/enemy are applied at the end of each round.
        processStatusEffects(playerStats);
        processStatusEffects(enemyStats);
    }

    /**
     * Check if the player has a CONFUSION status effect, which will cause them to use a random move instead
     */
    public void checkForConfusion(CombatStatsComponent playerStats) {
        if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.CONFUSION)) {
            logger.info("Entity is confused and will use a random move.");

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
     * Processes active status effects (to be called at the end of each turn).
     */
    public void processStatusEffects(CombatStatsComponent entityStats) {
        if (entityStats.hasStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING)) {
            logger.info("Entity is bleeding and takes damage.");
            entityStats.setHealth(entityStats.getHealth() - 5);
        }
    }

    private Action selectEnemyMove()
    {
        Action enemyAction;

        int rand = (int) (Math.random() * 4);
        enemyAction = switch (rand) {
            case 0 -> Action.ATTACK;
            case 1 -> Action.GUARD;
            case 2 -> Action.SLEEP;
            case 3 -> Action.SPECIAL;
            default -> null;
        };

        return enemyAction;
    }

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
            case Action.ATTACK:
                switch(enemyAction) {
                    case Action.ATTACK:
                    /* ATTACK - ATTACK
                     * Animal with the highest speed stat attacks first.
                     * Stamina decreases for both.
                     */
                        if (getFasterEntity() == player) {
                            playerMove.executeMove(playerAction, enemyStats);
                        } else {
                            enemyMove.executeMove(enemyAction, enemyStats);
                        }
                        checkCombatEnd();
                        if (getSlowerEntity() == player) {
                            playerMove.executeMove(playerAction, enemyStats);
                        } else {
                            enemyMove.executeMove(enemyAction, enemyStats);
                        }
                        checkCombatEnd();
                        break;
                    case Action.GUARD:
                    /* ATTACK - GUARD
                     * Enemy guards first, and player's attack damage is reduced by 50%.
                     * Stamina decreases for both.
                     */
                        enemyMove.executeMove(enemyAction);
                        playerMove.executeMove(playerAction, enemyStats, true);
                        checkCombatEnd();
                        break;
                    case Action.SLEEP:
                    /* ATTACK - SLEEP
                     * Enemy falls asleep, raising its stamina & health.
                     * Player performs multi-hit attack.
                     * Player stamina decreases.
                     */
                        enemyMove.executeMove(enemyAction);
                        // player.multiHitAttack()
                        checkCombatEnd();
                        break;
                    case Action.SPECIAL:
                    /* ATTACK - SPECIAL
                     * Enemy's special is activated.
                     * Player performs attack.
                     * Player stamina decreases.
                     */
                        // enemy.special()
                        playerMove.executeMove(playerAction, enemyStats);
                        checkCombatEnd();
                        break;
                }

            case Action.GUARD:
                switch(enemyAction) {
                    case Action.ATTACK:
                    /* GUARD - ATTACK
                     * Player guards first, and enemy’s attack damage is reduced by 50%.
                     * Stamina decreases for both.
                     */
                        playerMove.executeMove(playerAction);
                        enemyMove.executeMove(enemyAction, playerStats, true);
                        checkCombatEnd();
                        break;
                    case Action.GUARD:
                    /* GUARD - GUARD
                     * Both animals guard but nothing happens.
                     * Stamina decreases for both.
                     */
                        playerMove.executeMove(playerAction);
                        enemyMove.executeMove(enemyAction);
                        break;
                    case Action.SLEEP:
                    /* GUARD - SLEEP
                     * Player guards reducing stamina.
                     * Enemy sleeps increasing stamina and health.
                     */
                        playerMove.executeMove(playerAction);
                        enemyMove.executeMove(enemyAction);
                        break;
                    case Action.SPECIAL:
                    /* GUARD - SPECIAL
                     * Enemy’s special is activated.
                     * Negative specials are blocked by guard.
                     * Player stamina decreases.
                     */
                        playerMove.executeMove(playerAction);
                        // enemy.special(bool guarded = true)
                        checkCombatEnd();
                        break;
                }

            case Action.SLEEP:
                switch(enemyAction) {
                    case Action.ATTACK:
                    /* SLEEP - ATTACK
                     * Player falls asleep, raising stamina & health.
                     * Enemy performs multi-hit attack.
                     * Enemy stamina decreases.
                     */
                        playerMove.executeMove(playerAction);
                        enemyMove.executeMove(enemyAction, playerStats, false, getEnemyMultiHitsLanded());
                        checkCombatEnd();
                        break;
                    case Action.GUARD:
                    /* SLEEP - GUARD
                     * Player falls asleep, raising stamina & health.
                     * Enemy stamina decreases.
                     */
                        playerMove.executeMove(playerAction);
                        enemyMove.executeMove(enemyAction);
                        break;
                    case Action.SLEEP:
                    /* SLEEP - SLEEP
                     * Both animals fall asleep, raising stamina & health.
                     */
                        playerMove.executeMove(playerAction);
                        enemyMove.executeMove(enemyAction);
                        break;
                    case Action.SPECIAL:
                    /* SLEEP - SPECIAL
                     * Player falls asleep, raising stamina & health.
                     * Enemy’s special is activated.
                     */
                        playerMove.executeMove(playerAction);
                        // enemy.special()
                        checkCombatEnd();
                        break;
                }

            case Action.ITEM:
                // Always goes first.
                // player.useItem()
                // enemy.executeAction(enemyAction)
                // checkCombatEnd()
                break;
        }
        logger.info("PLAYER health {} stamina {}", playerStats.getHealth(), playerStats.getStamina());
        logger.info("ENEMY health {} stamina {}", enemyStats.getHealth(), enemyStats.getStamina());
    }

    /**
     * @return a pointer to the faster entity between the player and the enemy.
     */
    private Entity getFasterEntity() {
        Entity faster;

        if (playerStats.getSpeed() >= enemyStats.getSpeed()) {
            faster = player;
        } else {
            faster = enemy;
        }

        return faster;
    }

    /**
     * @return a pointer to the slower entity between the player and the enemy.
     */
    private Entity getSlowerEntity() {
        Entity slower;

        if (playerStats.getSpeed() < enemyStats.getSpeed()) {
            slower = player;
        } else {
            slower = enemy;
        }

        return slower;
    }

    private void checkCombatEnd() {
        logger.info("Checking combat end: player health = {}, enemy health = {}", playerStats.getHealth(), enemyStats.getHealth());
        if (playerStats.getHealth() <= 0) {
            entity.getEvents().trigger("combatLoss");
            logger.info("Combat lost in manager.");
        } else if (enemyStats.getHealth() <= 0) {
            entity.getEvents().trigger("combatWin");
            logger.info("Combat won in manager.");
        }
    }

    final Random random = new Random();

    /**
     * @return the number of successful hits in an enemy multi-hit attack (4 rolls).
     * The probability of success for each roll is exp(enemySpeed / 250) - 0.5
     * and each roll is simulated using a Bernoulli distribution
     */
    private int getEnemyMultiHitsLanded() {
        double successProbability = Math.exp(enemyStats.getSpeed() / 250.0) - 0.5;
        int successfulHits = 0;
        for (int i = 0; i < 4; i++) {
            successfulHits += (random.nextDouble() < successProbability) ? 1 : 0;
        }
        return successfulHits;
    }

    public Entity getPlayer() {
        return player;
    }

    public Entity getEnemy() {
        return enemy;
    }
}
