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

    private enum Turn { PLAYER, ENEMY }
    private Turn currentTurn;
    private final Entity player;
    private final Entity enemy;
    private final CombatStatsComponent playerStats;
    private final CombatStatsComponent enemyStats;
    private int playerHealth;
    private int enemyHealth;
    private boolean isCombatEnd = false;

    public CombatManager(Entity player, Entity enemy) {
        this.player = player;
        this.enemy = enemy;

        this.playerStats = player.getComponent(CombatStatsComponent.class);
        this.enemyStats = enemy.getComponent(CombatStatsComponent.class);

        this.playerHealth = player.getComponent(CombatStatsComponent.class).getHealth();
        this.enemyHealth = enemy.getComponent(CombatStatsComponent.class).getHealth();

        this.currentTurn = Turn.PLAYER;
    }

    /**
     * Player has clicked attack button to select 'attack' as their action.
     */
    public void onAttackSelected() {
        if (currentTurn == Turn.PLAYER && !isCombatEnd) {
            handlePlayerTurn();
            checkCombatEnd();
            if (!isCombatEnd) {
                switchToEnemyTurn();
            }
            // Update UI.
        }
    }

    private void switchToEnemyTurn() {
        currentTurn = Turn.ENEMY;
        logger.info("Switching to Enemy Turn");
        handleEnemyTurn();
        checkCombatEnd();
        if (!isCombatEnd) {
            currentTurn = Turn.PLAYER;
            logger.info("Switching to Player Turn");
        }
    }

    private void handlePlayerTurn() {
        enemyStats.hit(playerStats);
    }

    private void handleEnemyTurn() {
        playerStats.hit(enemyStats);
    }

    private void checkCombatEnd() {
        if (playerHealth <= 0 || enemyHealth <= 0) {
            isCombatEnd = true;
            logger.info("Combat ended");
            player.getComponent(CombatStatsComponent.class).setHealth(playerHealth);
            enemy.getComponent(CombatStatsComponent.class).setHealth(enemyHealth);
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
