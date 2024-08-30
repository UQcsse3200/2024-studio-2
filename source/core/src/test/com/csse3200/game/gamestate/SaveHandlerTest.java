package com.csse3200.game.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class SaveHandlerTest {
    private final String testDirectory = "test/saves";
    @Test
    void shouldSaveLoadDeleteValues() {
        GameStateTest.env1.test = 3;
        GameStateTest.env2.test2 = "test";
        SaveHandler.save(GameStateTest.class, testDirectory);

        GameStateTest.env1.test = 2;
        GameStateTest.env1.test2 = "five";
        SaveHandler.load(GameStateTest.class, testDirectory);

        assertEquals(3, GameStateTest.env1.test);
        assertEquals("set", GameStateTest.env1.test2);
        assertEquals(1, GameStateTest.env2.test);
        assertEquals("test", GameStateTest.env2.test2);

        SaveHandler.delete(GameStateTest.class, testDirectory);

        FileHandle handle = Gdx.files.local(testDirectory);
        assertFalse(handle.exists());
    }
}
