package com.csse3200.game.components.login;

import com.playfab.PlayFabErrors.*;
import com.playfab.PlayFabClientModels.*;
import com.playfab.PlayFabClientAPI;
import com.playfab.PlayFabSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * The PlayFab class handles user registration and login via PlayFab's client API.
 * It allows initializing the PlayFab client with a Title ID and provides methods
 * for user registration and login.
 */
public class PlayFab {
    private static final Logger logger = LoggerFactory.getLogger(PlayFab.class);

    /**
     * Constructor to initialize PlayFab with the given Title ID.
     *
     * @param titleId The PlayFab Title ID to be used for authentication.
     */
    public PlayFab(String titleId) {
        PlayFabSettings.TitleId = titleId; // Set your PlayFab Title ID here
    }

    /**
     * Main method to test user registration functionality.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Initialize PlayFab with your Title ID
        PlayFab playFab = new PlayFab("DBB26");

        // Call the registration method
        playFab.registerUser("test12345", "long","123456");

    }
    /**
     * Registers a new user in PlayFab with the provided credentials.
     *
     * @param username The desired username for the user.
     * @param email The email address associated with the user.
     * @param password The desired password for the user.
     * @return A {@link Response} object containing a success or failure message.
     */
    public static Response registerUser(String username, String email, String password) {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = username;
        request.Email = email;
        request.Password = password;
        request.DisplayName = username;


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
            logger.debug(succeedMsg);
            return new Response(succeedMsg, true);
        } else {
            String errorMsg = result.Error.errorMessage;
            logger.debug(result.Error.errorMessage);
            return new Response(errorMsg, false);
        }
    }

    public static Response saveData(Map<String, String> data) {
        UpdateUserDataRequest request = new UpdateUserDataRequest();
        request.Data = data;
        PlayFabResult<UpdateUserDataResult> result = PlayFabClientAPI.UpdateUserData(request);

        if (result.Result != null) {
            String succeedMsg = "Data is successfully saved.";
            System.out.println(succeedMsg);
            return new Response(succeedMsg, true);
        } else {
            String errorMsg = result.Error.errorMessage;
            System.out.println(errorMsg);
            return new Response(errorMsg, false);
        }
    }

    public static Response loadData() {
        GetUserDataRequest request = new GetUserDataRequest();
        PlayFabResult<GetUserDataResult> result = PlayFabClientAPI.GetUserData(request);

        if (result.Result != null && result.Result.Data != null) {
            Map<String, UserDataRecord> userData = result.Result.Data;
            String succeedMsg = "Data is successfully loaded.";
            System.out.println(succeedMsg);
            return new Response(userData.toString(), true);
        } else {
            String errorMsg = result.Error.errorMessage;
            System.out.println(errorMsg);
            return new Response(errorMsg, false);
        }
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

