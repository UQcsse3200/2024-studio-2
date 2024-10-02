package com.csse3200.game.minigames.snake;

import com.csse3200.game.minigames.snake.controller.Events;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventsTest {

    @Test
    public void testEnumValues() {
        // To verify whether the event enum constants are defined correctly
        // To verify there are only three constants NONE, RESTART and EXIT_TO_MENU
        assertEquals(3, Events.values().length);

        // each Enum constant is retrieved by its name
        assertEquals(Events.NONE, Events.valueOf("NONE"));
        assertEquals(Events.RESTART, Events.valueOf("RESTART"));
        assertEquals(Events.EXIT_TO_MENU, Events.valueOf("EXIT_TO_MENU"));
    }

    @Test
    public void testToString() {
        // To verify the string representation of each event enum constant and it matches the name
        assertEquals("NONE", Events.NONE.toString());
        assertEquals("RESTART", Events.RESTART.toString());
        assertEquals("EXIT_TO_MENU", Events.EXIT_TO_MENU.toString());
    }
}
