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

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    private enum Turn { PLAYER, ENEMY }
    private Turn currentTurn;
    private final Entity player;
    private final Entity enemy;
    int playerHealth;
    int enemyHealth;
    private boolean isCombatEnd = false;

    public CombatManager(Entity player, Entity enemy) {
        this.player = player;
        this.playerHealth = player.getComponent(CombatStatsComponent.class).getHealth();
        this.enemy = enemy;
        this.enemyHealth = enemy.getComponent(CombatStatsComponent.class).getHealth();
        this.currentTurn = Turn.PLAYER;
    }

    /** This function will be called from clicking combat buttons */
    public void onPlayerTurnCompleted() {
        if (currentTurn == Turn.PLAYER && !isCombatEnd) {
            handlePlayerTurn();
            checkCombatEnd();
            if (!isCombatEnd) {
                switchToEnemyTurn();
            }
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
        enemyHealth -= 10;
        logger.info("Enemy health after player turn: {}", enemyHealth);
    }

    private void handleEnemyTurn() {
        playerHealth -= 10;
        logger.info("Player health after enemy turn: {}", playerHealth);
    }

    private void checkCombatEnd() {
        if (playerHealth <= 0 || enemyHealth <= 0) {
            isCombatEnd = true;
            logger.info("Combat ended");
            player.getComponent(CombatStatsComponent.class).setHealth(playerHealth);
            enemy.getComponent(CombatStatsComponent.class).setHealth(enemyHealth);
        }
    }
}
