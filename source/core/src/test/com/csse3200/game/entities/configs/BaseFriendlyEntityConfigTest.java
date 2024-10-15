package com.csse3200.game.entities.configs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BaseFriendlyEntityConfigTest {
    private BaseFriendlyEntityConfig entityConfig;

    @BeforeEach
    void setup() {
        entityConfig = new BaseFriendlyEntityConfig();
    }

    /**
     * Tests that the base friendly entity has the correct default values.
     */
    @Test
    void testDefaultValues() {
        // Expected Base Attack should be 0.
        assertEquals(0, entityConfig.getBaseAttack());
        // Expected Strength should be 0.
        assertEquals(0, entityConfig.getStrength());
        // Expected Stamina should be 100.
        assertEquals(100, entityConfig.getStamina());
        // Expected Level should be 1.
        assertEquals(1, entityConfig.getLevel());
        // Expected to return false when checking if entity is a boss.
        assertFalse(entityConfig.isBoss());
        // Expected health should be 100.
        assertEquals(100, entityConfig.getHealth());
        // Expected hunger should be 100.
        assertEquals(100, entityConfig.getHunger());
        // Expected Item Probability should be 0.0f.
        assertEquals(0.0f, entityConfig.getItemProbability());
        // Expected to not have a name as they are defined in NPCs.json.
        assertEquals("", entityConfig.getAnimalName());
        // Expected Animation Speed should be 0.1f.
        assertEquals(0.1f, entityConfig.getAnimationSpeed());
        // Expected to have null base hint as hints are defined in NPCs.json.
        assertNull(entityConfig.getBaseHint());
        // Expected to have null sound path as it is defined in NPCs.json.
        assertNull(entityConfig.getSoundPath());
        // Expected to have null sprite path as it is defined in NPCs.json.
        assertNull(entityConfig.getSpritePath());
    }

    /**
     * Tests that the get and set methods of each attribute in the config.
     */
    @Test
    void testSettersAndGetters() {
        // Should set base attack to 10 and return 10 when the getter is called.
        entityConfig.setBaseAttack(10);
        assertEquals(10, entityConfig.getBaseAttack());
        // Should set strength to 10 and return 10 when the getter is called.
        entityConfig.setStrength(10);
        assertEquals(10, entityConfig.getStrength());
        // Should set stamina to 10 and return 10 when the getter is called.
        entityConfig.setStamina(10);
        assertEquals(10, entityConfig.getStamina());
        // Should set level to 10 and return 10 when the getter is called.
        entityConfig.setLevel(10);
        assertEquals(10, entityConfig.getLevel());
        // Should set health to 10 and return 10 when the getter is called.
        entityConfig.setHealth(10);
        assertEquals(10, entityConfig.getHealth());
        // Should set hunger to 10 and return 10 when the getter is called.
        entityConfig.setHunger(10);
        assertEquals(10, entityConfig.getHunger());
        // Should set sprite path to test/spritePath and return accordingly when the getter is called.
        entityConfig.setSpritePath("test/spitePath");
        assertEquals("test/spritePath", entityConfig.getSpritePath());
        // Should set sound paths to test sounds and return accordingly when the getter is called.
        String[] soundPaths = {"sound1.mp3", "sound2.mp3"};
        entityConfig.setSoundPath(soundPaths);
        assertArrayEquals(soundPaths, entityConfig.getSoundPath());
        // Should set item probability to 0.5f and return 0.5f when the getter is called.
        entityConfig.setItemProbability(0.5f);
        assertEquals(0.5f, entityConfig.getItemProbability());
        // Should set defense to 10 and return 10 when the getter is called.
        entityConfig.setDefense(10);
        assertEquals(10, entityConfig.getDefense());
        // Should set speed to 10 and return 10 when the getter is called.
        entityConfig.setSpeed(10);
        assertEquals(10, entityConfig.getSpeed());
        // Should set experience to 10 and return 10 when the getter is called.
        entityConfig.setExperience(10);
        assertEquals(10, entityConfig.getExperience());
    }

    /**
     * Tests that the hint system is functional and restart current hint also works.
     */
    @Test
    void testRestartCurrentHint() {
        entityConfig.restartCurrentHint();
        assertEquals(0, entityConfig.currentHint);

        Map<Integer, String[]> hints = entityConfig.getHints();
        assertNull(hints);
        Map<Integer, String[]> newHints = new HashMap<>();
        newHints.put(1, new String[]{"Hint1, Hint2"});
        entityConfig.setHints(newHints);
        assertNotNull(entityConfig.getHints());

        entityConfig.restartCurrentHint();
        assertEquals(0, entityConfig.currentHint);
    }

    /**
     * Tests that setting an entity to a boss functions as intended.
     */
    @Test
    void testIsBoss() {
        assertFalse(entityConfig.isBoss());
        entityConfig.setIsBoss(true);
        assertTrue(entityConfig.isBoss());
    }

}