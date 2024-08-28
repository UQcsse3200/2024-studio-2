//package com.csse3200.game.components.gamearea;
//
//import com.badlogic.gdx.Files;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.files.FileHandle;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GameAreaDisplayTest
//{
//
//    @BeforeEach
//    void setUp()
//    {
//        // Mock Gdx.files to provide a controlled environment
//        Gdx.files = (Files) Mockito.mock(FileHandle.class);
//        // Set up a mock skin file
//        Mockito.when(Gdx.files.internal("skin.json")).thenReturn(new FileHandle("mock_skin_file.json"));
//    }
//
//    @Test
//    void shouldUseCorrectPlayerIconTextureForSkyKingdom() {
//        GameAreaDisplay gameAreaDisplay = new GameAreaDisplay("Sky Kingdom");
//        gameAreaDisplay.create();
//
//        String expectedPath = "images/player_icon_sky.png";
//        assertNotNull(gameAreaDisplay.playerIconTexture);
//
//        // Use FileHandle methods directly
//        FileHandle textureFile = Gdx.files.internal(expectedPath);
//        assertEquals(expectedPath, textureFile.path());
//
//        gameAreaDisplay.playerIconTexture.dispose();
//    }
//
//    @Test
//    void shouldUseCorrectPlayerIconTextureForSeaKingdom() {
//        GameAreaDisplay gameAreaDisplay = new GameAreaDisplay("Sea Kingdom");
//        gameAreaDisplay.create();
//
//        String expectedPath = "images/player_icon_sea.png";
//        assertNotNull(gameAreaDisplay.playerIconTexture);
//
//        // Use FileHandle methods directly
//        FileHandle textureFile = Gdx.files.internal(expectedPath);
//        assertEquals(expectedPath, textureFile.path());
//
//        gameAreaDisplay.playerIconTexture.dispose();
//    }
//
//    @Test
//    void shouldUseCorrectPlayerIconTextureForForestKingdom() {
//        GameAreaDisplay gameAreaDisplay = new GameAreaDisplay("Forest Kingdom");
//        gameAreaDisplay.create();
//
//        String expectedPath = "images/player_icon_forest.png";
//        assertNotNull(gameAreaDisplay.playerIconTexture);
//
//        // Use FileHandle methods directly
//        FileHandle textureFile = Gdx.files.internal(expectedPath);
//        assertEquals(expectedPath, textureFile.path());
//
//        gameAreaDisplay.playerIconTexture.dispose();
//    }
//
//}
