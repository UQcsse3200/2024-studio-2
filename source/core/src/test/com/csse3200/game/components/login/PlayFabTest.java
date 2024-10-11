package com.csse3200.game.components.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.extensions.GameExtension;
import com.playfab.PlayFabClientAPI;
import com.playfab.PlayFabClientModels.RegisterPlayFabUserRequest;
import com.playfab.PlayFabClientModels.RegisterPlayFabUserResult;
import com.playfab.PlayFabErrors.PlayFabResult;

@ExtendWith(GameExtension.class)
class PlayFabTest {
    private PlayFab playFab;

    @BeforeEach
    public void setup() {
        // Initialize PlayFab with your Title ID
        playFab = new PlayFab("DBB26");
    }

    /*
    @Test
    void testRegisterUserSuccess() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        int random = ThreadLocalRandom.current().nextInt(10000000, 99999999);
        request.Username = "test" + random;
        System.out.println(request.Username);
        request.Email = "test" + random + "@gmail.com";
        request.Password = "123456";
        request.DisplayName = "test" + random;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Result, "Result should not be null");

        assertEquals("test" + random, result.Result.Username);
    }
    */

    @Test
    void testRegisterUserPasswordFail() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        System.out.println(request.Username);
        request.Email = "test123@gmail.com";
        request.Password = "1234";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }


    @Test
    void testRegisterUserEmailFail() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        System.out.println(request.Username);
        request.Email = "test";
        request.Password = "123456";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }

    @Test
    void testRegisterUserPasswordAndEmailFail() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        System.out.println(request.Username);
        request.Email = "test";
        request.Password = "1234";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }

    @Test
    void testRegisterUserEmailExistFail() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        System.out.println(request.Username);
        request.Email = "long11221@gmail.com";
        request.Password = "123456";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Email address not available", result.Error.errorMessage);
    }

    @Test
    void testRegisterUserEmailExistAndPasswordFail() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        System.out.println(request.Username);
        request.Email = "long11221@gmail.com";
        request.Password = "123456";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Email address not available", result.Error.errorMessage);
    }

    // Login Test
    // Importing Login Unit Testing will cause throttling, so we won't import it.
}
