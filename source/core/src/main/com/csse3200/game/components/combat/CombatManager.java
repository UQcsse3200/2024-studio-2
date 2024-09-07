package com.csse3200.game.components.combat;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.combat.move.CombatMoveComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Manages the turn-based combat loop and handles attacks
 */
public class CombatManager extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CombatManager.class);

    public enum Action { START_MOVE, ATTACK, GUARD, COUNTER, SPECIAL };
    private final Entity player;
    private final Entity enemy;
    private final CombatStatsComponent playerStats;
    private final CombatStatsComponent enemyStats;
    private final CombatStatsDisplay statsDisplay;
    private Action playerAction;
    private Action enemyAction;
    private final CombatMoveComponent playerMove;
    private final CombatMoveComponent enemyMove;
    private boolean isCombatEnd = false;

    public CombatManager(Entity player, Entity enemy, CombatStatsDisplay statsDisplay) {
        this.player = player;
        this.enemy = enemy;
        this.statsDisplay = statsDisplay;

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
     * @param playerActionStr
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

        logger.debug("Player action = {}, enemy action = {}", playerAction, enemyAction);

        processActions();

        statsDisplay.updateHealthUI(25, playerStats.getMaxHealth(), true);
        //statsDisplay.updateHealthUI(enemyStats.getHealth(), enemyStats.getMaxHealth(), false);
    }

    private Action selectEnemyMove()
    {
        Action enemyAction;

        int rand = (int) (Math.random() * 4);
        switch (rand) {
            case 0:
                enemyAction = Action.ATTACK;
                break;
            case 1:
                enemyAction = Action.GUARD;
                break;
            case 2:
                enemyAction = Action.COUNTER;
                break;
            case 3:
                enemyAction = Action.SPECIAL;
                break;
            default:
                enemyAction = null;
        }

        return enemyAction;
    }

    private void executeMove(Action playerAction, Action enemyAction) {
        if (playerMove == null) {
            logger.error("Player does not have a CombatMoveComponent.");
            return;
        }
        if (enemyMove == null) {
            logger.error("Enemy does not have a CombatMoveComponent.");
            return;
        }

        if (playerStats.getSpeed() >= enemyStats.getSpeed()) {
            playerMove.executeMove(playerAction, enemy);
            enemyMove.executeMove(enemyAction, player);
        } else {
            enemyMove.executeMove(enemyAction, player);
            playerMove.executeMove(playerAction, enemy);
        }
    }

    private void processActions()
    {
        if (playerAction == null || enemyAction == null) {
            logger.error("Both player and enemy actions must be determined.");
            return;
        }
        executeMove(playerAction, enemyAction);
    }

    private void checkCombatEnd() {
        if (playerStats.getHealth() <= 0 || enemyStats.getHealth() <= 0) {
            isCombatEnd = true;
            logger.info("Combat ended");
        }
    }

    public Entity getPlayer() {
        return player;
    }

    public Entity getEnemy() {
        return enemy;
    }
}
