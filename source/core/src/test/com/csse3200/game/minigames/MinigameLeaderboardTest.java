/*package com.csse3200.game.minigames;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.login.PlayFab;
import com.csse3200.game.components.mainmenu.MainMenuDisplay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.csse3200.game.extensions.GameExtension;
import org.mockito.Mockito;

import java.util.ArrayList;

@ExtendWith(GameExtension.class)
public class MinigameLeaderboardTest {
    private MinigameLeaderboard minigameLeaderboard;
    private MainMenuDisplay mockMainMenuDisplay;
    private PlayFab mockPlayFab;
    @BeforeEach
    public void setup() {
        mockPlayFab = Mockito.mock(PlayFab.class);
        mockMainMenuDisplay  = Mockito.mock(MainMenuDisplay.class);
        minigameLeaderboard = new MinigameLeaderboard(mockMainMenuDisplay);
        minigameLeaderboard.playFab = mockPlayFab;
        minigameLeaderboard.makeLeaderboardTable();

        ArrayList<String> mockUsernames = new ArrayList<>();
        mockUsernames.add("player1");
        mockUsernames.add("test1234");
        mockUsernames.add("player3");

        ArrayList<String> mockHighscores = new ArrayList<>();
        mockHighscores.add("50");
        mockHighscores.add("68");
        mockHighscores.add("30");

        // Stub the PlayFab methods to return the mock data
        when(mockPlayFab.getUsernames()).thenReturn(mockUsernames);
        when(mockPlayFab.getHighscores()).thenReturn(mockHighscores);

        PlayFab.updateLeaderboard(anyString());

    }

    // Initialization test
    @Test
    public void testLeaderboardInitialization() {
        assertNotNull(minigameLeaderboard, "Leaderboard should be initialized");
    }
    @Test
    public void testLeaderboardIndexInitialization() {
        assertEquals(0, minigameLeaderboard.currentIdx);
    }

    @Test
    public void testGameNameInitialization() {
        assertTrue("Game array list should contain 'Snake'", minigameLeaderboard.gameName.contains("Snake"));
        assertTrue("Game array list should contain 'Bird'", minigameLeaderboard.gameName.contains("Bird"));
        assertTrue("Game array list should contain 'Fish'", minigameLeaderboard.gameName.contains("Fish"));
    }



    @Test
    public void testLoadTextures() {
        minigameLeaderboard.loadTextures();
        assertNotNull(minigameLeaderboard.backgroundTexture, "Background texture should be loaded");
        assertNotNull(minigameLeaderboard.closeButtonTexture, "Close button texture should be loaded");
    }

    @Test
    public void testMakeLeaderboardTable() {
        Table table = minigameLeaderboard.makeLeaderboardTable();
        assertNotNull(table, "Leaderboard table should not be null");
        assertTrue("Leaderboard table should be visible", table.isVisible());
    }

    //Functionality test
    @Test
    public void testRefreshWithCurrentParameter() {
        minigameLeaderboard.refreshLeaderboard("Current");
        assertEquals(minigameLeaderboard.currentIdx, 0);
    }


    @Test
    public void testCurrentIdxAfterRefreshWithSnakeParameter() {
        minigameLeaderboard.refreshLeaderboard("Snake");
        assertEquals(minigameLeaderboard.currentIdx, 0);
    }
    @Test
    public void testCurrentIdxAfterRefreshWithBirdParameter() {
        minigameLeaderboard.refreshLeaderboard("Bird");
        assertEquals(minigameLeaderboard.currentIdx, 1);
    }

    @Test
    public void testCurrentIdxAfterRefreshWithFishParameter() {
        minigameLeaderboard.refreshLeaderboard("Fish");
        assertEquals(minigameLeaderboard.currentIdx, 2);
    }

    @Test
    public void testUpdateLeaderboard() {
        minigameLeaderboard.updateLeaderboard(); //it will update the usernames and highscores.
        ArrayList<String> highscoreUsername = minigameLeaderboard.getUsernames();
        int userIndex = highscoreUsername.indexOf("test1234");
        int testScore = Integer.parseInt(String.valueOf(minigameLeaderboard.getHighscores().get(userIndex)));
        assertEquals(testScore, 68);
    }
}
*/