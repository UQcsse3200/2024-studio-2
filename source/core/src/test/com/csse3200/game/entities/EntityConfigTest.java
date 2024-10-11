package com.csse3200.game.entities;

import com.csse3200.game.entities.configs.BaseEnemyEntityConfig;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(GameExtension.class)
class EntityConfigTest {
    private static BaseEntityConfig entityConfig = new BaseEntityConfig(false, "Placeholder String",
            new String[] {"Placeholder String 1", "Placeholder String 2"});
    private static BaseEnemyEntityConfig enemyConfig = new BaseEnemyEntityConfig();
    
    /**
     * Tests getSpritePath method
     */
    @Test
    void testGetSpritePath() {
        assertEquals("Placeholder String", entityConfig.getSpritePath(), "Sprite path is incorrect0");
    }
    
    /**
     * Tests isFriendly and setFriendly methods
     */
    @Test
    void testFriendly() {
        assertFalse(entityConfig.isFriendly(), "Friendly should be false");
        entityConfig.setFriendly(true);
        assertTrue(entityConfig.isFriendly(), "Friendly should be true");
    }
    
    /**
     * Tests getAnimationSpeed method
     */
    @Test
    void testGetAnimationSpeed() {
        assertEquals(0.1f, entityConfig.getAnimationSpeed(), "Animation speed is incorrect");
    }
    
    /**
     * Tests getSoundPath method
     */
    @Test
    void testGetSoundPath() {
        assertArrayEquals(new String[]{"Placeholder String 1", "Placeholder String 2"},
                entityConfig.getSoundPath(), "Sound path is incorrect");
    }
    
    /**
     * Tests Health methods
     */
    @Test
    void testHealth() {
        assertEquals(100, enemyConfig.getHealth(), "Health is incorrect");
        enemyConfig.setHealth(50);
        assertEquals(50, enemyConfig.getHealth(), "Health is incorrect");
    }
    
    /**
     * Tests Hunger methods
     */
    @Test
    void testHunger() {
        assertEquals(100, enemyConfig.getHunger(), "Hunger is incorrect");
        enemyConfig.setHunger(50);
        assertEquals(50, enemyConfig.getHunger(), "Hunger is incorrect");
    }
    
    /**
     * Tests Attack methods
     */
    @Test
    void testAttack() {
        enemyConfig.setBaseAttack(100);
        assertEquals(100, enemyConfig.getBaseAttack(), "BaseAttack is incorrect");
    }

    /**
     * Tests Defence methods
     */
    @Test
    void testDefence() {
        assertEquals(0, enemyConfig.getDefense(), "Defence is incorrect");
        enemyConfig.setDefense(50);
        assertEquals(50, enemyConfig.getDefense(), "Defence is incorrect");
    }
    
    /**
     * Tests Speed methods
     */
    @Test
    void testSpeed() {
        assertEquals(1, enemyConfig.getSpeed(), "Speed is incorrect");
        enemyConfig.setSpeed(50);
        assertEquals(50, enemyConfig.getSpeed(), "Speed is incorrect");
    }
    
    /**
     * Tests Experience methods
     */
    @Test
    void testExperience() {
        assertEquals(10, enemyConfig.getExperience(), "Experience is incorrect");
        enemyConfig.setExperience(50);
        assertEquals(50, enemyConfig.getExperience(), "Experience is incorrect");
    }
}
