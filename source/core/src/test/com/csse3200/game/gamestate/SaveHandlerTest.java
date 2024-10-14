package com.csse3200.game.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.csse3200.game.components.stats.Stat;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class SaveHandlerTest {
    private final String testDirectory = "test/saves";
    @Test
    void shouldSaveLoadDeleteValues() {
        GameState.clearState();

        GameStateTest.env1.test = 3;
        GameStateTest.env2.test2 = "test";
        SaveHandler.getInstance().save(GameStateTest.class, testDirectory, FileLoader.Location.LOCAL);

        GameStateTest.env1.test = 2;
        GameStateTest.env1.test2 = "five";
        SaveHandler.getInstance().load(GameStateTest.class, testDirectory, FileLoader.Location.LOCAL);

        assertEquals(3, GameStateTest.env1.test);
        assertEquals("set", GameStateTest.env1.test2);
        assertEquals(1, GameStateTest.env2.test);
        assertEquals("test", GameStateTest.env2.test2);

        SaveHandler.getInstance().delete(GameStateTest.class, testDirectory, FileLoader.Location.LOCAL);

        FileHandle handle = Gdx.files.local(testDirectory);
        assertFalse(handle.exists());
    }

    @Test
    void shouldResetState() {

        GameState.resetState();

        assertNotEquals(0, GameState.quests.quests.size());
    }

    @Test
    void shouldClearState() {
        GameState.resetState();
        
        GameState.stats.stats.add(new Stat());

        GameState.clearState();

        assertEquals(0, GameState.stats.stats.size);

        GameState.clearState();
    }

    @Test
    void shouldCheckState() {
        GameState.clearState();

        GameState.quests = null;

        assertTrue(GameState.checkState());

        GameState.resetState();

        assertFalse(GameState.checkState());
    }
}
