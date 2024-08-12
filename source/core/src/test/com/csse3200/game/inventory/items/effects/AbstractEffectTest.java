package com.csse3200.game.inventory.items.effects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.effects.healing.HealEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class AbstractEffectTest {

    protected AbstractEffect effect;

    @BeforeEach
    public void setUp() {
        effect = new HealEffect(3);
    }

    @Test
    public void testGetDescription() {
        assertEquals("Heals the player by 3 points.", effect.getDescription(), "Effect description should match expected format.");
    }

    @Test
    public void testApply() {
        // Test the apply method
        effect.apply();
        // Verify the behavior once player model is created
    }
}