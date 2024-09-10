package com.csse3200.game.components.combat;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.combat.move.CombatMoveComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        enemyAction = selectEnemyMove();

        logger.info("Player action = {}, enemy action = {}", playerAction, enemyAction);

        executeMoveCombination(playerAction, enemyAction);
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
            case ATTACK:
                switch(enemyAction) {
                    case ATTACK:
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
                    case GUARD:
                    /* ATTACK - GUARD
                     * Enemy guards first, and player's attack damage is reduced by 50%.
                     * Stamina decreases for both.
                     */
                        // enemy.guard() - Guard move should probably not actually do anything except reduce stamina.
                        playerMove.executeMove(playerAction, enemyStats, true);
                        // checkCombatEnd()
                        break;
                    case SLEEP:
                    /* ATTACK - SLEEP
                     * Enemy falls asleep, raising its stamina & health.
                     * Player performs multi-hit attack.
                     * Player stamina decreases.
                     */
                        // enemy.sleep()
                        // player.multiHitAttack()
                        // checkCombatEnd()
                        break;
                    case SPECIAL:
                    /* ATTACK - SPECIAL
                     * Enemy's special is activated.
                     * Player performs attack.
                     * Player stamina decreases.
                         */
                        // enemy.special()
                        // player.attack()
                        // checkCombatEnd()
                        break;
                }

            case GUARD:
                switch(enemyAction) {
                    case ATTACK:
                    /* GUARD - ATTACK
                     * Player guards first, and enemy’s attack damage is reduced by 50%.
                     * Stamina decreases for both.
                     */
                        // player.guard() - Guard move should probably not actually do anything except reduce stamina.
                        // enemy.attack(bool guarded = true)
                        // checkCombatEnd()
                        break;
                    case GUARD:
                    /* GUARD - GUARD
                     * Both animals guard but nothing happens.
                     * Stamina decreases for both.
                     */
                        Entity faster = getFasterEntity();
                        Entity slower = getSlowerEntity();
                        // faster.guard() - Guard move should probably not actually do anything except reduce stamina.
                        // slower.guard() - Guard move should probably not actually do anything except reduce stamina.
                        break;
                    case SLEEP:
                    /* GUARD - SLEEP
                     *
                     */
                        break;
                    case SPECIAL:
                    /* GUARD - SPECIAL
                     * Enemy’s special is activated.
                     * Negative specials are blocked by guard.
                     * Player stamina decreases.
                     */
                        // player.guard() - Guard move should probably not actually do anything except reduce stamina.
                        // enemy.special(bool guarded = true)
                        break;
                }

            case SLEEP:
                switch(enemyAction) {
                    case ATTACK:
                    /* SLEEP - ATTACK
                     * Player falls asleep, raising stamina & health.
                     * Enemy performs multi-hit attack.
                     * Enemy stamina decreases.
                     */
                        // player.sleep()
                        // enemy.multiHitAttack()
                        // checkCombatEnd()
                        break;
                    case GUARD:
                    /* SLEEP - GUARD
                     * Player falls asleep, raising stamina & health.
                     * Enemy stamina decreases.
                     */
                        // player.sleep()
                        // enemy.guard() - Guard move should probably not actually do anything except reduce stamina.
                        break;
                    case SLEEP:
                    /* SLEEP - SLEEP
                     * Both animals fall asleep, raising stamina & health.
                     */
                        Entity faster = getFasterEntity();
                        Entity slower = getSlowerEntity();
                        // faster.sleep()
                        // slower.sleep()
                        break;
                    case SPECIAL:
                    /* SLEEP - SPECIAL
                     * Player falls asleep, raising stamina & health.
                     * Enemy’s special is activated.
                     */
                        // player.sleep()
                        // enemy.special()
                        break;
                }

            case ITEM:
                // Always goes first.
                // player.useItem()
                // enemy.executeAction(enemyAction)
                // checkCombatEnd()
                break;
        }
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

    public Entity getPlayer() {
        return player;
    }

    public Entity getEnemy() {
        return enemy;
    }
}
