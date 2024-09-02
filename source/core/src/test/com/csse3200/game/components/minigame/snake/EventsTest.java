package com.csse3200.game.components.minigame.snake;

import com.csse3200.game.components.minigame.snake.controller.Events;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventsTest {

    @Test
    public void testEnumValues() {
        // To verify whether the event enum constants are defined correctly
        assertEquals(3, Events.values().length);
        assertEquals(Events.NONE, Events.valueOf("NONE"));
        assertEquals(Events.RESTART, Events.valueOf("RESTART"));
        assertEquals(Events.EXIT_TO_MENU, Events.valueOf("EXIT_TO_MENU"));
    }

    @Test
    public void testToString() {
        // To verify the string representation of each event enum constant
        assertEquals("NONE", Events.NONE.toString());
        assertEquals("RESTART", Events.RESTART.toString());
        assertEquals("EXIT_TO_MENU", Events.EXIT_TO_MENU.toString());
    }
}
