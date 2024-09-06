package com.csse3200.game.components.combat;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Manages the turn-based combat loop and handles attacks
 */
public class CombatManager extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CombatManager.class);

    public enum Action { START_MOVE, ATTACK, GUARD, COUNTER };
    private final Entity player;
    private final Entity enemy;
    private final CombatStatsComponent playerStats;
    private final CombatStatsComponent enemyStats;
    private Action playerAction;
    private Action enemyAction;
    private boolean isCombatEnd = false;

    public CombatManager(Entity player, Entity enemy) {
        this.player = player;
        this.enemy = enemy;

        this.playerStats = player.getComponent(CombatStatsComponent.class);
        this.enemyStats = enemy.getComponent(CombatStatsComponent.class);

        this.playerAction = Action.START_MOVE;
        this.enemyAction = Action.START_MOVE;
    }

    /**
     * Both opponents' actions need to be determined before either action is enacted, as the sequencing of the moves
     * will be dependent on both of the choices.
     * @param playerActionStr
     */
    public void onPlayerActionSelected(String playerActionStr)
    {
        playerAction = Action.valueOf(playerActionStr);

        selectEnemyMove();

        processActions();
    }

    private Action selectEnemyMove()
    {
        Action enemyAction;

        int rand = (int) (Math.random() * 3);
        if (rand == 0) {
            enemyAction = Action.ATTACK;
        } else if (rand == 1) {
            enemyAction = Action.GUARD;
        } else {
            enemyAction = Action.COUNTER;
        }

        return enemyAction;
    }

    private void processActions()
    {
        if (playerAction == Action.ATTACK)
        {
            if (enemyAction == Action.ATTACK)
            {
                // Fastest entity moves first.
            }
            else if (enemyAction == Action.GUARD)
            {

            }
            else if (enemyAction == Action.COUNTER)
            {

            }
        }
        else if (playerAction == Action.GUARD)
        {
            if (enemyAction == Action.ATTACK)
            {

            }
            else if (enemyAction == Action.GUARD)
            {

            }
            else if (enemyAction == Action.COUNTER)
            {

            }
        }
        else if (playerAction == Action.COUNTER)
        {
            if (enemyAction == Action.ATTACK)
            {

            }
            else if (enemyAction == Action.GUARD)
            {

            }
            else if (enemyAction == Action.COUNTER)
            {

            }
        }
    }

    private void checkCombatEnd()
    {
        if (playerStats.getHealth() <= 0 || enemyStats.getHealth() <= 0) {
            isCombatEnd = true;
            logger.info("Combat ended");
        }
    }

    public Entity getPlayer() { return player; }
    public Entity getEnemy() { return enemy; }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

}
