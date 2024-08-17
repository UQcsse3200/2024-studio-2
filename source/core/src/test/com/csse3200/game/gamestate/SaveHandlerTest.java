package com.csse3200.game.gamestate;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(GameExtension.class)
public class SaveHandlerTest {
    @Test
    void shouldSaveLoadValues() {
        GameStateTest.env1.test = 3;
        GameStateTest.env2.test2 = "test";
        SaveHandler.save(GameStateTest.class, "test");

        GameStateTest.env1.test = 2;
        GameStateTest.env2.test2 = "test2";
        GameStateTest.env1.test2 = "five";
        SaveHandler.loadAll(GameStateTest.class, "test");

        assertEquals(GameStateTest.env1.test, 3);
        assertEquals(GameStateTest.env1.test2, "set");
        assertEquals(GameStateTest.env2.test, 1);
        assertEquals(GameStateTest.env2.test2, "test");

        SaveHandler.clearAll(GameStateTest.class, "test");
    }
}
