package com.csse3200.game.components.gamestate;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
public class SaveHandlerTest {
    @Test
    void shouldSaveLoadValues() {
        GameStateTest.env1.test = 3;
        GameStateTest.env2.test2 = "test";
        SaveHandler.save("test", GameStateTest.class);

        GameStateTest.env1.test = 2;
        GameStateTest.env2.test2 = "test2";
        GameStateTest.env1.test2 = "five";
        SaveHandler.loadAll("test", GameStateTest.class);

        assertEquals(GameStateTest.env1.test, 3);
        assertEquals(GameStateTest.env1.test2, "set");
        assertEquals(GameStateTest.env2.test, 1);
        assertEquals(GameStateTest.env2.test2, "test");

        SaveHandler.clearAll("test", GameStateTest.class);
    }
}
