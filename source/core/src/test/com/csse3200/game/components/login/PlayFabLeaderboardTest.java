package com.csse3200.game.components.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.extensions.GameExtension;
import com.playfab.PlayFabClientAPI;
import com.playfab.PlayFabClientModels.RegisterPlayFabUserRequest;
import com.playfab.PlayFabClientModels.RegisterPlayFabUserResult;
import com.playfab.PlayFabErrors.PlayFabResult;
import com.playfab.PlayFabClientModels.*;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

@ExtendWith(GameExtension.class)
class PlayFabLeaderboardTest {
    private PlayFab playFab;
    MockedStatic<PlayFabClientAPI> mockedAPI;
    private PlayFabResult<GetLeaderboardResult> mockSnakeLeaderboardResult;
    private PlayFabResult<GetLeaderboardResult> mockBirdLeaderboardResult;
    private PlayFabResult<GetLeaderboardResult> mockFishLeaderboardResult;

    String newUser = "NewUser";
    @BeforeEach
    public void setUpUpdateLeaderboard() {
        MockitoAnnotations.openMocks(this);
        playFab = new PlayFab("DBB26");
        mockSnakeLeaderboardResult = new PlayFabResult<>();
        mockBirdLeaderboardResult = new PlayFabResult<>();
        mockFishLeaderboardResult = new PlayFabResult<>();

        GetLeaderboardResult leaderboardResult = new GetLeaderboardResult();
        leaderboardResult.Leaderboard = new ArrayList<>();

        // Mocking three leaderboard entries
        ArrayList<PlayerLeaderboardEntry> entries = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            PlayerLeaderboardEntry entry = new PlayerLeaderboardEntry();
            entry.DisplayName = "User" + i;
            entry.StatValue = 10 - i;
            entries.add(entry);
        }

        //Snake leaderboard
        leaderboardResult.Leaderboard.add(entries.get(0));
        leaderboardResult.Leaderboard.add(entries.get(1));
        leaderboardResult.Leaderboard.add(entries.get(2));
        mockSnakeLeaderboardResult.Result = leaderboardResult;

        //Bird leaderboard
        leaderboardResult = new GetLeaderboardResult(); // Reinitialize to avoid reference issues
        leaderboardResult.Leaderboard = new ArrayList<>();
        leaderboardResult.Leaderboard.add(entries.get(3));
        leaderboardResult.Leaderboard.add(entries.get(4));
        leaderboardResult.Leaderboard.add(entries.get(5));
        mockBirdLeaderboardResult.Result = leaderboardResult;

        //Fish leaderboard
        leaderboardResult = new GetLeaderboardResult(); // Reinitialize to avoid reference issues
        leaderboardResult.Leaderboard = new ArrayList<>();
        leaderboardResult.Leaderboard.add(entries.get(6));
        leaderboardResult.Leaderboard.add(entries.get(7));
        mockFishLeaderboardResult.Result = leaderboardResult;

        // Set up the mocked PlayFabClientAPI
        mockedAPI = mockStatic(PlayFabClientAPI.class);
        mockedAPI.when(() -> PlayFabClientAPI.GetLeaderboard(any(GetLeaderboardRequest.class)))
                .thenAnswer(invocation -> {
                    String leaderboardName = ((GetLeaderboardRequest) invocation.getArgument(0)).StatisticName;
                    switch (leaderboardName) {
                        case "Snake":
                            return mockSnakeLeaderboardResult;
                        case "Bird":
                            return mockBirdLeaderboardResult;
                        case "Fish":
                            return mockFishLeaderboardResult;
                        default:
                            throw new IllegalArgumentException("Unknown leaderboard: " + leaderboardName);
                    }
                });
        PlayFabResult<UpdatePlayerStatisticsResult> mockSuccessResult = new PlayFabResult<>();
        mockSuccessResult.Result = new UpdatePlayerStatisticsResult();

        // Mock the UpdatePlayerStatistics API response
        mockedAPI.when(() -> PlayFabClientAPI.UpdatePlayerStatistics(any(UpdatePlayerStatisticsRequest.class)))
                .thenAnswer(invocation -> {
                    UpdatePlayerStatisticsRequest request = invocation.getArgument(0);
                    int score = request.Statistics.get(0).Value;
                    String leaderboardName = request.Statistics.get(0).StatisticName; // Get the leaderboard type from the request

                    GetLeaderboardResult result;
                    switch (leaderboardName) {
                        case "Snake":
                            result = mockSnakeLeaderboardResult.Result;
                            break;
                        case "Bird":
                            result = mockBirdLeaderboardResult.Result;
                            break;
                        case "Fish":
                            result = mockFishLeaderboardResult.Result;
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown leaderboard: " + leaderboardName);
                    }
                    // Check if the new user already exists in the leaderboard
                    PlayerLeaderboardEntry existingEntry = null;
                    for (PlayerLeaderboardEntry entry : result.Leaderboard) {
                        if (newUser.equals(entry.DisplayName)) {
                            existingEntry = entry;
                            break;
                        }
                    }

                    // If the user does not exist, then add them to the leaderboard
                    if (existingEntry == null) {
                        PlayerLeaderboardEntry newEntry = new PlayerLeaderboardEntry();
                        newEntry.DisplayName = newUser;
                        newEntry.StatValue = score;
                        result.Leaderboard.add(newEntry);
                    }
                    return mockSuccessResult;
                });
    }
    @AfterEach
    public void closeMockedAPI() {
        if (mockedAPI != null) {
            mockedAPI.close();
        }
    }

    @Test
    public void testUpdateSnakeLeaderboard() {
        // Act
        PlayFab.updateLeaderboard("Snake");
        // Assert
        assertEquals(3, playFab.getUsernames().size());
        assertEquals(3, playFab.getHighscores().size());
        assertEquals("User0", playFab.getUsernames().get(0));
        assertEquals("10", playFab.getHighscores().get(0));
        assertEquals("User1", playFab.getUsernames().get(1));
        assertEquals("9", playFab.getHighscores().get(1));
        assertEquals("User2", playFab.getUsernames().get(2));
        assertEquals("8", playFab.getHighscores().get(2));

    }


    @Test
    public void testUpdateBirdLeaderboard() {
        // Act
        PlayFab.updateLeaderboard("Bird");
        // Assert
        assertEquals(3, playFab.getUsernames().size());
        assertEquals(3, playFab.getHighscores().size());
        assertEquals("User3", playFab.getUsernames().get(0));
        assertEquals("7", playFab.getHighscores().get(0));
        assertEquals("User4", playFab.getUsernames().get(1));
        assertEquals("6", playFab.getHighscores().get(1));
        assertEquals("User5", playFab.getUsernames().get(2));
        assertEquals("5", playFab.getHighscores().get(2));
    }


    @Test
    public void testUpdateFishLeaderboard() {
        // Act
        PlayFab.updateLeaderboard("Fish");
        // Assert
        assertEquals(2, playFab.getUsernames().size());
        assertEquals(2, playFab.getHighscores().size());
        assertEquals("User6", playFab.getUsernames().get(0));
        assertEquals("4", playFab.getHighscores().get(0));
        assertEquals("User7", playFab.getUsernames().get(1));
        assertEquals("3", playFab.getHighscores().get(1));
    }

    @Test
    public void testSubmitSnakeScoreNewUser() {
        PlayFab.isLogin = true;
        // Act
        PlayFab.submitScore("Snake", 20);
        PlayFab.updateLeaderboard("Snake");
        // Assert
        assertEquals(4, mockSnakeLeaderboardResult.Result.Leaderboard.size()); // New user added
        assertEquals(newUser, playFab.getUsernames().get(3));
        assertEquals("20", playFab.getHighscores().get(3));
    }


    @Test
    public void testSubmitBirdScoreNewUser() {
        PlayFab.isLogin = true;
        // Act
        PlayFab.submitScore("Bird", 20);
        PlayFab.updateLeaderboard("Bird");
        // Assert
        assertEquals(4, mockBirdLeaderboardResult.Result.Leaderboard.size()); // New user added
        assertEquals(newUser, playFab.getUsernames().get(3));
        assertEquals("20", playFab.getHighscores().get(3));
    }


    @Test
    public void testSubmitFishScoreNewUser() {
        PlayFab.isLogin = true;
        // Act
        PlayFab.submitScore("Fish", 20);
        PlayFab.updateLeaderboard("Fish");
        // Assert
        assertEquals(3, mockFishLeaderboardResult.Result.Leaderboard.size()); // New user added
        assertEquals(newUser, playFab.getUsernames().get(2));
        assertEquals("20", playFab.getHighscores().get(2));
    }
}
