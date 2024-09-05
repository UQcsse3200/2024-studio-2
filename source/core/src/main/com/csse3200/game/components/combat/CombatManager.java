package com.csse3200.game.components.combat;

import com.csse3200.game.components.Component;
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

    private enum Turn { INIT, PLAYER, ENEMY }
    private Turn currentTurn;
    public enum Action { ATTACK, GUARD, COUNTER };
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

        switchTurn();

        if (currentTurn == Turn.PLAYER) {
            enemyStats.hit(playerStats);
        } else {
            playerStats.hit(enemyStats);
        }

        checkCombatEnd();
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

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

}
