package com.csse3200.game.inventory.items;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class ItemUsageContextTest {

    @Test
    public void testDefaultConstructor() { // Test ItemUsageContext with no inputs
        ItemUsageContext context = new ItemUsageContext();
        assertEquals(0, context.getNumInputs(), "Default constructor should have 0 inputs.");
    }
}