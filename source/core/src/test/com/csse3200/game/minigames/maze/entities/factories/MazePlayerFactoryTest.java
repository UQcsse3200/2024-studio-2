package com.csse3200.game.minigames.maze.entities.factories;

import com.csse3200.game.files.FileLoader;
import com.csse3200.game.minigames.maze.entities.configs.MazePlayerConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.csse3200.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
class MazePlayerFactoryTest {

    private static MazePlayerConfig mockPlayerConfig;

    @BeforeAll
    static void setup() {

        mockPlayerConfig = new MazePlayerConfig();
        mockPlayerConfig.health = 100;

        mockStatic(FileLoader.class);

        when(FileLoader.readClass(MazePlayerConfig.class, "configs/minigames/maze/player.json"))
                .thenReturn(mockPlayerConfig);
    }



    @Test
    void testFactoryPrivateConstructor() {

        assertThrows(IllegalStateException.class, () -> {
            MazePlayerFactory factory = new MazePlayerFactory();
        }, "Expected to throw IllegalStateException when instantiating MazePlayerFactory");
    }
}
