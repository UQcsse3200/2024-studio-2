package com.csse3200.game.components.login;

import com.playfab.PlayFabErrors.*;
import com.playfab.PlayFabClientModels.*;
import com.playfab.PlayFabClientAPI;
import com.playfab.PlayFabSettings;

import java.util.*;
import java.util.stream.Collectors;
public class PlayFab {
    // Constructor to initialize the PlayFab settings
    public PlayFab(String titleId) {
        PlayFabSettings.TitleId = titleId; // Set your PlayFab Title ID here
    }
    public static void main(String[] args) {
        // Initialize PlayFab with your Title ID
        PlayFab playFab = new PlayFab("DBB26");

        // Call the registration method
        playFab.registerUser("long1221", "long11221@gmail.com", "password125");

    }
    // Method to register a new user
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
            Collection<List<String>> errors = result.Error.errorDetails.values();
            List<String> errorList = errors.stream().flatMap(List::stream).toList();
            for (String error : errorList) {
                System.out.println(error);
            }
            return new Response(errorList.getFirst(), false);
        }
        //
    }
    // Method to login a new user
    public static void loginUser(String username, String password) {
        LoginWithPlayFabRequest request = new LoginWithPlayFabRequest();
        request.Username = username;
        request.Password = password;

        PlayFabClientAPI.LoginWithPlayFab(request);
        //
    }

    public static class Response {
        private String result;
        private Boolean isSucceed;

        public Response(String result, Boolean isSucceed) {
            this.result = result;
            this.isSucceed = isSucceed;
        }

        public String getResult() {
            return result;
        }

        public Boolean getIsSucceed() {
            return isSucceed;
        }
    }
}

