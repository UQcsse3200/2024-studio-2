package com.csse3200.game.components.login;

import com.playfab.PlayFabErrors.*;
import com.playfab.PlayFabClientModels.*;
import com.playfab.PlayFabClientAPI;
import com.playfab.PlayFabSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * The PlayFab class handles user registration and login via PlayFab's client API.
 * It allows initializing the PlayFab client with a Title ID and provides methods
 * for user registration and login.
 */
public class PlayFab {
    private static final Logger logger = LoggerFactory.getLogger(PlayFab.class);
    private static ArrayList<String> minigameUsernames;
    private static ArrayList<String> minigameHighscores;
    private static String playerName;
    public static boolean isLogin = false;

    /**
     * Constructor to initialize PlayFab with the given Title ID.
     *
     * @param titleId The PlayFab Title ID to be used for authentication.
     */
    public PlayFab(String titleId) {
        PlayFabSettings.TitleId = titleId; // Set your PlayFab Title ID here

        minigameUsernames = new ArrayList<>();
        minigameHighscores = new ArrayList<>();
        playerName = "";
    }

    public static void main(String[] args) {
        new PlayFab("DBB26");
        registerUser("test123", "000");
        //loginUser("GameMaster", "123456");
        //submitScore("Bird", 100000);
    }

    /**
     * Registers a new user in PlayFab with the provided credentials.
     *
     * @param username The desired username for the user.
     * @param password The desired password for the user.
     * @return A {@link Response} object containing a success or failure message.
     */
    public static Response registerUser(String username, String password) {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = username;
        request.Password = password;
        request.DisplayName = username;
        request.RequireBothUsernameAndEmail = false;


        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        if (result.Result != null) {
            String succeedMsg = result.Result.Username + " has successfully registered.";
            return new Response(succeedMsg, true);
        } else {
            String errorMsg = result.Error.errorMessage;
            logger.debug(errorMsg);
            return new Response(errorMsg, false);
        }
        //
    }

    /**
     * Logs in a user via PlayFab with the provided username and password.
     *
     * @param username The username of the registered account.
     * @param password The password of the registered account.
     * @return A {@link Response} object containing a success or failure message.
     */
    public static Response loginUser(String username, String password) {
        LoginWithPlayFabRequest request = new LoginWithPlayFabRequest();
        request.Username = username;
        request.Password = password;

        PlayFabResult<LoginResult> result = PlayFabClientAPI.LoginWithPlayFab(request);

        if (result.Result != null) {
            String succeedMsg = "Welcome " + request.Username + ".";
            playerName = username;
            isLogin = true;

            if (!minigameUsernames.contains(username)) {
                minigameUsernames.add(username);
            }

            logger.debug(succeedMsg);
            return new Response(succeedMsg, true);
        } else {
            String errorMsg = result.Error.errorMessage;
            logger.debug(result.Error.errorMessage);
            return new Response(errorMsg, false);
        }
    }

    public static String getPlayerName() {
        if (isLogin) {
            return playerName;
        } else {
            return "Guest"; // Return "Guest" if no user is logged in or list is empty
        }
    }

    public static void updateLeaderboard(String gameName){
        GetLeaderboardRequest request = new GetLeaderboardRequest();
        request.StatisticName = gameName;
        request.MaxResultsCount = 10;
        PlayFabResult<GetLeaderboardResult> result = PlayFabClientAPI.GetLeaderboard(request);
        // Clean the arrays before updating them
        minigameUsernames.clear();
        minigameHighscores.clear();

        if (result.Result != null) {
            // Update UI with leaderboard data
            List<PlayerLeaderboardEntry> leaderboard = result.Result.Leaderboard;
            for (int i = 0; i < leaderboard.size(); i++) {
                minigameUsernames.add(leaderboard.get(i).DisplayName);
                minigameHighscores.add(String.valueOf(leaderboard.get(i).StatValue));
                System.out.println("User " + i + ": " + minigameUsernames.get(i) + " - " + minigameHighscores.get(i));
            }
        } else {
            logger.error("Failed to retrieve leaderboard: " + result.Error.errorMessage);
        }
    }

    public static void submitScore(String gameName, int score) {
        if (!isLogin) {
            logger.info("You need to login to put your score to the leaderboard.");
            return;
        }
        UpdatePlayerStatisticsRequest request = new UpdatePlayerStatisticsRequest();

        StatisticUpdate statUpdate = new StatisticUpdate();
        statUpdate.StatisticName = gameName;
        statUpdate.Value = score;

        ArrayList<StatisticUpdate> statisticUpdates = new ArrayList<>();
        statisticUpdates.add(statUpdate);

        request.Statistics = statisticUpdates;

        PlayFabResult<UpdatePlayerStatisticsResult> result = PlayFabClientAPI.UpdatePlayerStatistics(request);

        if (result.Result != null) {
            logger.info("Player score submitted successfully.");
        } else {
            logger.error("Failed to submit score: " + result.Error.errorMessage);
        }
    }

    public ArrayList<String> getUsernames() {
        return minigameUsernames;
    }

    public ArrayList<String> getHighscores() {
        return minigameHighscores;
    }


    /**
     * A class used to return the result of a user action (registration/login).
     */
    public static class Response {
        private String result;
        private Boolean isSucceed;

        /**
         * Constructor for the Response class.
         *
         * @param result The result message.
         * @param isSucceed Indicates whether the operation was successful.
         */
        public Response(String result, Boolean isSucceed) {
            this.result = result;
            this.isSucceed = isSucceed;
        }

        /**
         * Gets the result message.
         *
         * @return The result message.
         */
        public String getResult() {
            return result;
        }

        /**
         * Gets whether the operation succeeded.
         *
         * @return true if the operation succeeded, false otherwise.
         */
        public Boolean getIsSucceed() {
            return isSucceed;
        }
    }
}

