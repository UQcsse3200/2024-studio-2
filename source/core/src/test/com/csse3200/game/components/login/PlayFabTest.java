package com.csse3200.game.components.login;

import com.playfab.PlayFabErrors.*;
import com.playfab.PlayFabClientModels.*;
import com.playfab.PlayFabClientAPI;
import com.playfab.PlayFabSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
public class PlayFabTest {
    private PlayFab playFab;

    @BeforeEach
    public void setup() {
        // Initialize PlayFab with your Title ID
        playFab = new PlayFab("DBB26");
    }

    @Test
    public void testRegisterUserSuccess() {
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


    @Test
    public void testRegisterUserPasswordFail() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        System.out.println(request.Username);
        request.Email = "test123@gmail.com";
        request.Password = "1234";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");

        Collection<List<String>> errors = result.Error.errorDetails.values();
        List<String> errorList = errors.stream().flatMap(List::stream).toList();
        assertEquals("Password must be between 6 and 100 characters.", errorList.getFirst());
    }


    @Test
    public void testRegisterUserEmailFail() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        System.out.println(request.Username);
        request.Email = "test";
        request.Password = "123456";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");

        Collection<List<String>> errors = result.Error.errorDetails.values();
        List<String> errorList = errors.stream().flatMap(List::stream).toList();

        assertEquals("Email address is not valid.", errorList.getFirst());
    }

    @Test
    public void testRegisterUserPasswordAndEmailFail() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        System.out.println(request.Username);
        request.Email = "test";
        request.Password = "1234";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");

        Collection<List<String>> errors = result.Error.errorDetails.values();
        List<String> errorList = errors.stream().flatMap(List::stream).toList();

        assertEquals("Email address is not valid.", errorList.getFirst());
        assertEquals("Password must be between 6 and 100 characters.", errorList.get(1));
    }

    @Test
    public void testRegisterUserEmailExistFail() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        System.out.println(request.Username);
        request.Email = "long11221@gmail.com";
        request.Password = "123456";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");

        Collection<List<String>> errors = result.Error.errorDetails.values();
        List<String> errorList = errors.stream().flatMap(List::stream).toList();

        assertEquals("Email address already exists. ", errorList.getFirst());
    }

    @Test
    public void testRegisterUserEmailExistAndPasswordFail() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        System.out.println(request.Username);
        request.Email = "long11221@gmail.com";
        request.Password = "123456";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");

        Collection<List<String>> errors = result.Error.errorDetails.values();
        List<String> errorList = errors.stream().flatMap(List::stream).toList();

        assertEquals("Email address already exists. ", errorList.getFirst());
    }
}
