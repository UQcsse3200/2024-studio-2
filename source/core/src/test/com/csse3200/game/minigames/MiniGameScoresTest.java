package com.csse3200.game.minigames;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class MiniGameScoresTest {
    private MiniGamesScores miniGamesScores;
    private Entity mockPlayer;
    private GdxGame mockGame;
    @BeforeEach
    public void setUp() {
        mockGame = mock(GdxGame.class);

        miniGamesScores = new MiniGamesScores();
    }

    /**
     * Test initial medal and score set up
     */
    @Test
    void testInitialScoresAndMedals() {
        assertEquals(0, miniGamesScores.getSnakeHighScore());
        assertEquals(MiniGameMedals.FAIL, miniGamesScores.getSnakeMedal());

        assertEquals(0, miniGamesScores.getBirdHighScore());
        assertEquals(MiniGameMedals.FAIL, miniGamesScores.getBirdMedal());

        assertEquals(0, miniGamesScores.getMazeHighScore());
        assertEquals(MiniGameMedals.FAIL, miniGamesScores.getMazeMedal());
    }

    /**
     * Test functionality as the score increases
     */
    @Test
    void testSnakeHighScoreAndMedal() {
        miniGamesScores.checkAndSetSnakeScoreMedal(MiniGameConstants.SNAKE_BRONZE_THRESHOLD - 1);
        assertEquals(MiniGameConstants.SNAKE_BRONZE_THRESHOLD - 1, miniGamesScores.getSnakeHighScore());
        assertEquals(MiniGameMedals.FAIL, miniGamesScores.getSnakeMedal());

        miniGamesScores.checkAndSetSnakeScoreMedal(MiniGameConstants.SNAKE_BRONZE_THRESHOLD - 2);
        assertEquals(MiniGameConstants.SNAKE_BRONZE_THRESHOLD - 1, miniGamesScores.getSnakeHighScore());
        assertEquals(MiniGameMedals.FAIL, miniGamesScores.getSnakeMedal());

        miniGamesScores.checkAndSetSnakeScoreMedal(MiniGameConstants.SNAKE_SILVER_THRESHOLD - 1);
        assertEquals(MiniGameConstants.SNAKE_SILVER_THRESHOLD - 1, miniGamesScores.getSnakeHighScore());
        assertEquals(MiniGameMedals.BRONZE, miniGamesScores.getSnakeMedal());

        miniGamesScores.checkAndSetSnakeScoreMedal(MiniGameConstants.SNAKE_SILVER_THRESHOLD - 2);
        assertEquals(MiniGameConstants.SNAKE_SILVER_THRESHOLD - 1, miniGamesScores.getSnakeHighScore());
        assertEquals(MiniGameMedals.BRONZE, miniGamesScores.getSnakeMedal());

        miniGamesScores.checkAndSetSnakeScoreMedal(MiniGameConstants.SNAKE_GOLD_THRESHOLD - 1);
        assertEquals(MiniGameConstants.SNAKE_GOLD_THRESHOLD - 1, miniGamesScores.getSnakeHighScore());
        assertEquals(MiniGameMedals.SILVER, miniGamesScores.getSnakeMedal());

        miniGamesScores.checkAndSetSnakeScoreMedal(MiniGameConstants.SNAKE_GOLD_THRESHOLD);
        assertEquals(MiniGameConstants.SNAKE_GOLD_THRESHOLD, miniGamesScores.getSnakeHighScore());
        assertEquals(MiniGameMedals.GOLD, miniGamesScores.getSnakeMedal());
    }

    /**
     * to be properly implemented
     * Test functionality as the score increases
     */
    @Test
    void testFlappyBirdHighScoreAndMedal() {
        // add when flappy bord is implemented in sprint 2
    }

    /**
     * to be properly implemented
     * Test functionality as the score increases
     */
    @Test
    void testMazeHighScoreAndMedal() {
        // Add when maze is implemented in sprint 3
    }
}
