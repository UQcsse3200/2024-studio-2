package com.csse3200.game.components.combat;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Manages the turn-based combat loop and handles attacks
 */
public class CombatManager {
    private static final Logger logger = LoggerFactory.getLogger(CombatManager.class);

    public enum Action { START_MOVE, ATTACK, GUARD, COUNTER };
    private final Entity player;
    private final Entity enemy;
    private final CombatStatsComponent playerStats;
    private final CombatStatsComponent enemyStats;
    private final CombatStatsDisplay statsDisplay;
    private String playerAction;
    private String enemyAction;
    private boolean isCombatEnd = false;

    public CombatManager(Entity player, Entity enemy, CombatStatsDisplay statsDisplay) {
        this.player = player;
        this.enemy = enemy;
        this.statsDisplay = statsDisplay;

        this.playerStats = player.getComponent(CombatStatsComponent.class);
        this.enemyStats = enemy.getComponent(CombatStatsComponent.class);

        this.playerAction = "";
        this.enemyAction = "";
    }

    /**
     * Both opponents' actions need to be determined before either action is enacted, as the sequencing of the moves
     * will be dependent on both of the choices.
     * @param playerActionStr
     */
    public void onPlayerActionSelected(String playerActionStr)
    {
        playerAction = String.valueOf(playerActionStr);

        enemyAction = selectEnemyMove();

        logger.debug("Player action = {}, enemy action = {}", playerAction, enemyAction);

        processActions();

        statsDisplay.updateHealthUI(25, playerStats.getMaxHealth(), true);
        //statsDisplay.updateHealthUI(enemyStats.getHealth(), enemyStats.getMaxHealth(), false);
    }

    private String selectEnemyMove()
    {
        String enemyAction;

        int rand = (int) (Math.random() * 3);
        if (rand == 0) {
            enemyAction = "ATTACK";
        } else if (rand == 1) {
            enemyAction = "GUARD";
        } else {
            enemyAction = "COUNTER";
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// THIS IS FOR TESTING, GET RID OF THIS:
        enemyAction = "ATTACK";
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        return enemyAction;
    }

    private void processActions()
    {
        if (Objects.equals(playerAction, "ATTACK"))
        {
            if (Objects.equals(enemyAction, "ATTACK"))
            {
                // Fastest entity moves first.
                enemyStats.hit(playerStats);
                //playerStats.hit(enemyStats);
            }
            else if (Objects.equals(enemyAction, "GUARD"))
            {
                enemyStats.hit(playerStats);
            }
            else if (Objects.equals(enemyAction, "COUNTER"))
            {
                enemyStats.hit(playerStats);
            }
        }
        else if (Objects.equals(playerAction, "GUARD"))
        {
            if (Objects.equals(enemyAction, "ATTACK"))
            {

            }
            else if (Objects.equals(enemyAction, "GUARD"))
            {

            }
            else if (Objects.equals(enemyAction, "COUNTER"))
            {

            }
        }
        else if (Objects.equals(playerAction, "COUNTER"))
        {
            if (Objects.equals(enemyAction, "ATTACK"))
            {

            }
            else if (Objects.equals(enemyAction, "GUARD"))
            {

            }
            else if (Objects.equals(enemyAction, "COUNTER"))
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
}
