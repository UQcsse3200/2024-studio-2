package com.csse3200.game.entities.configs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputService;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseFriendlyEntityConfigTest {
    private BaseFriendlyEntityConfig entityConfig;

    @BeforeEach
    void setup() {
        entityConfig = new BaseFriendlyEntityConfig();
    }

    @Test
    void testDefaultValues() {
        assertEquals(0, entityConfig.getBaseAttack());
        assertEquals(0, entityConfig.getStrength());
        assertEquals(100, entityConfig.getStamina());
        assertEquals(1, entityConfig.getLevel());
        assertFalse(entityConfig.isBoss());
        assertEquals(100, entityConfig.getHealth());
        assertEquals(100, entityConfig.getHunger());
        assertEquals(0.0f, entityConfig.getItemProbability());
        assertEquals("", entityConfig.getAnimalName());
        assertEquals(0.1f, entityConfig.getAnimationSpeed());
        assertNull(entityConfig.getBaseHint());
        assertNull(entityConfig.getSoundPath());
        assertNull(entityConfig.getSpritePath());
    }

    @Test
    void testSettersAndGetters() {
        entityConfig.setBaseAttack(10);
        assertEquals(10, entityConfig.getBaseAttack());

        entityConfig.setStrength(5);
        assertEquals(5, entityConfig.getStrength());

        entityConfig.setStamina(80);
        assertEquals(80, entityConfig.getStamina());

        entityConfig.setLevel(2);
        assertEquals(2, entityConfig.getLevel());

        entityConfig.setHealth(150);
        assertEquals(150, entityConfig.getHealth());

        entityConfig.setHunger(50);
        assertEquals(50, entityConfig.getHunger());

        entityConfig.setSpritePath("path/to/sprite");
        assertEquals("path/to/sprite", entityConfig.getSpritePath());

        String[] soundPaths = {"sound1.mp3", "sound2.mp3"};
        entityConfig.setSoundPath(soundPaths);
        assertArrayEquals(soundPaths, entityConfig.getSoundPath());

        entityConfig.setItemProbability(0.5f);
        assertEquals(0.5f, entityConfig.getItemProbability());

        entityConfig.setStrength(10);
        assertEquals(10, entityConfig.getStrength());

        entityConfig.setDefense(10);
        assertEquals(10, entityConfig.getDefense());

        entityConfig.setSpeed(5);
        assertEquals(5, entityConfig.getSpeed());

        entityConfig.setExperience(100);
        assertEquals(100, entityConfig.getExperience());

        entityConfig.setStamina(90);
        assertEquals(90, entityConfig.getStamina());

        entityConfig.setLevel(3);
        assertEquals(3, entityConfig.getLevel());
    }

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
    @Test
    void testIsBoss() {
        assertFalse(entityConfig.isBoss());
        entityConfig.setIsBoss(true);
        assertTrue(entityConfig.isBoss());
    }

}