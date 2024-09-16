package com.csse3200.game.components.login;

import com.playfab.PlayFabErrors.*;
import com.playfab.PlayFabClientModels.*;
import com.playfab.PlayFabClientAPI;
import com.playfab.PlayFabSettings;

import java.util.Collection;
import java.util.List;
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
        playFab.registerUser("long122133", "long11221@gmail.com", "password125");

    }
    // Method to register a new user
    public void registerUser(String username, String email, String password) {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = username;
        request.Email = email;
        request.Password = password;

        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);
        if (result.Result != null) {
            System.out.println(result.Result.Username + " has successfully registered."); //Continue work on this later.
        } else {
            Collection<List<String>> errors = result.Error.errorDetails.values();
            List<String> errorList = errors.stream().flatMap(List::stream).toList();
            for (String error : errorList) {
                System.out.println(error);
            }
        }

        //
    }
    // Method to login a new user
    public void loginUser(String username, String password) {
        LoginWithPlayFabRequest request = new LoginWithPlayFabRequest();
        request.Username = username;
        request.Password = password;

        PlayFabClientAPI.LoginWithPlayFab(request);
        //
    }
}

