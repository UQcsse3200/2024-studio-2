package com.csse3200.game.components.combat;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Manages the turn-based combat loop and handles attacks
 */
public class CombatManager extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CombatManager.class);

    private enum Turn { INIT, PLAYER, ENEMY }
    private Turn currentTurn;
    private final Entity player;
    private final Entity enemy;
    private final CombatStatsComponent playerStats;
    private final CombatStatsComponent enemyStats;
    private boolean isCombatEnd = false;

    public CombatManager(Entity player, Entity enemy) {
        this.player = player;
        this.enemy = enemy;

        this.playerStats = player.getComponent(CombatStatsComponent.class);
        this.enemyStats = enemy.getComponent(CombatStatsComponent.class);

        this.currentTurn = Turn.INIT;
    }

    /**
     * Player has clicked attack button to select 'attack' as their action.
     */
    public void onAttackSelected()
    {
        checkTurn();
        if (currentTurn == Turn.PLAYER) {
            enemyStats.hit(playerStats);
        } else {
            playerStats.hit(enemyStats);
        }

        checkCombatEnd();
        if (!isCombatEnd) {
            switchTurn();
        }
    }

    /**
     * Sets the current turn to whoever currently has the fastest speed stat.
     */
    private void checkTurn() {
        if (playerStats.getSpeed() >= enemyStats.getSpeed()) {
            this.currentTurn = Turn.PLAYER;
        } else {
            this.currentTurn = Turn.ENEMY;
        }
    }

    /**
     * Switches currentTurn to opposite of current currentTurn.
     */
    private void switchTurn() {
        if (currentTurn == Turn.PLAYER) {
            currentTurn = Turn.ENEMY;
        } else {
            currentTurn = Turn.PLAYER;
        }
    }

    /**
     * Checks if either the player or enemy's health is at 0, end changes isCombatEnd to true if so
     */
    private void checkCombatEnd() {
        if (playerStats.getHealth() <= 0 || enemyStats.getHealth() <= 0) {
            isCombatEnd = true;
            logger.info("Combat ended");
        }
    }

    /**
     * Returns the player's Entity
     * @return Entity of the player
     */
    public Entity getPlayer() {
        return player;
    }

    /**
     * Returns the enemy's entity
     * @return Entity of the enemy
     */
    public Entity getEnemy() {
        return enemy;
    }

    /**
     * Returns the stats component of the player
     * @return CombatStatsComponent of player
     */
    public CombatStatsComponent getPlayerStats() {
        return playerStats;
    }

    /**
     * Returns the stats component of the enemy
     * @return CombatStatsComponent of the enemy
     */
    public CombatStatsComponent getEnemyStats() {
        return enemyStats;
    }
}
