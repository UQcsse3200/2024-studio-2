package com.csse3200.game.components.login;

import com.playfab.PlayFabErrors.*;
import com.playfab.PlayFabClientModels.*;
import com.playfab.PlayFabClientAPI;
import com.playfab.PlayFabSettings;

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
        System.out.println(result.Result); //Continue work on this later.
        System.out.println(result.Error); //Continue work on this later.
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

