/*
package com.csse3200.game.components.login;


import com.csse3200.game.extensions.GameExtension;
import com.playfab.PlayFabClientAPI;
import com.playfab.PlayFabClientModels.*;
import com.playfab.PlayFabErrors.PlayFabResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(GameExtension.class)
class PlayFabLoginRegisterTest {
    private PlayFab playFab;
    @BeforeEach
    public void setUp () {
        playFab = new PlayFab("DBB26");
    }

    // Since sprint 3, this test has created 2k5 new users, so we decided to mark this test as a comment.

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


    // RequireBothUsernameAndEmail is set to true, and not include email field.
    // Total 4 cases.
    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is set to true
     * - Existing username is used.
     * - Password is valid.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testExistingUsernameValidPasswordRequireBothTrue() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        request.Password = "123456";
        request.RequireBothUsernameAndEmail = true;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Both username and email are required unless specified with RequireBothUsernameAndEmail.", result.Error.errorMessage);
    }


    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is set to true
     * - New username is used
     * - Password is valid
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterNewUsernameValidPasswordRequireBothTrue() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test00000";
        request.Password = "123456";
        request.RequireBothUsernameAndEmail = true;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Both username and email are required unless specified with RequireBothUsernameAndEmail.", result.Error.errorMessage);
    }

    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is set to true
     * - New username is used
     * - Password is invalid.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterNewUsernameInvalidPasswordRequireBothTrue() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test00000";
        request.Password = "123";
        request.RequireBothUsernameAndEmail = true;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }


    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is set to true
     * - Existing username is used
     * - Password is invalid.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterExistingUsernameInvalidPasswordRequireBothTrue() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        request.Password = "123";
        request.RequireBothUsernameAndEmail = true;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }


    // RequireBothUsernameAndEmail is set to false, and not include email field.
    // Only 3 cases, as forth case, which is new username and valid password, will create a new user.
    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is set to false
     * - Existing username is used
     * - Password is valid.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterExistingUsernameValidPasswordRequireBothFalse() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        request.Password = "123456";
        request.RequireBothUsernameAndEmail = false;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Username not available", result.Error.errorMessage);
    }


    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is set to false
     * - Existing username is used
     * - Password is invalid.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterExistingUsernameInvalidPasswordRequireBothFalse() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        request.Password = "123";
        request.RequireBothUsernameAndEmail = false;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }


    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is set to false
     * - New username is used
     * - Password is invalid.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterNewUsernameInvalidPasswordRequireBothFalse() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test0000";
        request.Password = "123";
        request.RequireBothUsernameAndEmail = false;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }

    // RequireBothUsernameAndEmail is used and include email field.
    // Total 6 cases
    /**
     * This test verifies that when:
     * - New username is used.
     * - Existing email is used.
     * - Password is invalid.
     * - RequireBothUsernameAndEmail is set to false.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterNewUsernameExistingEmailInvalidPasswordNotRequireBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test0000";
        request.Email = "test123@gmail.com";
        request.Password = "1234";
        request.RequireBothUsernameAndEmail = false;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }



    /**
     * This test verifies that when:
     * - Existing username is used.
     * - New email is used.
     * - Password is invalid.
     * - RequireBothUsernameAndEmail is set to false.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterExistingUsernameNewEmailInvalidPasswordNotRequireBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test000";
        request.Email = "test000@gmail.com";
        request.Password = "1234";
        request.RequireBothUsernameAndEmail = false;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }



    /**
     * This test verifies that when:
     * - Existing username is used.
     * - Existing email is used.
     * - Password is invalid.
     * - RequireBothUsernameAndEmail is set to false.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterExistingUsernameExistingEmailInvalidPasswordNotRequireBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test000";
        request.Email = "test000@gmail.com";
        request.Password = "1234";
        request.RequireBothUsernameAndEmail = false;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }


    /**
     * This test verifies that when:
     * - New username is used.
     * - Existing email is used.
     * - Password is valid.
     * - RequireBothUsernameAndEmail is set to false.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterNewUsernameExistingEmailValidPasswordNotRequireBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test0000";
        request.Email = "test123@gmail.com";
        request.Password = "123456";
        request.RequireBothUsernameAndEmail = false;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Email address not available", result.Error.errorMessage);
    }

    /**
     * This test verifies that when:
     * - Existing username is used.
     * - New email is used.
     * - Password is valid.
     * - RequireBothUsernameAndEmail is set to false.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterExistingUsernameNewEmailValidPasswordNotRequireBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        request.Email = "test000@gmail.com";
        request.Password = "123456";
        request.RequireBothUsernameAndEmail = false;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Username not available", result.Error.errorMessage);
    }


    /**
     * This test verifies that when:
     * - Existing username is used.
     * - Existing email is used.
     * - Password is valid.
     * - RequireBothUsernameAndEmail is set to false.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterExistingUsernameExistingEmailValidPasswordNotRequireBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        request.Email = "test123@gmail.com";
        request.Password = "123456";
        request.RequireBothUsernameAndEmail = false;
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Email address not available", result.Error.errorMessage);
    }


    // Not declare RequireBothUsernameAndEmail, and not include email field.
    // Total 4 cases
    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is not declared.
     * - Existing username is used
     * - Password is valid
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterExistingUsernameValidPasswordNotRequireBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        request.Password = "123456";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Both username and email are required unless specified with RequireBothUsernameAndEmail.", result.Error.errorMessage);
    }

    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is not declared.
     * - Existing username is used
     * - Password is invalid
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterExistingUsernameInvalidPasswordNotRequireBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        request.Password = "123";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }


    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is not declared.
     * - New username is used
     * - Password is valid
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterNewUsernameValidPasswordNotRequireBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test0000";
        request.Password = "123456";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Both username and email are required unless specified with RequireBothUsernameAndEmail.", result.Error.errorMessage);
    }


    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is not declared.
     * - New username is used
     * - Password is invalid
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterNewUsernameInvalidPasswordNotRequireBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test0000";
        request.Password = "123";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }



    // Not declare RequireBothUsernameAndEmail, but include email.
    // Total 6 cases
    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is not declared.
     * - Existing username is used.
     * - Existing email is used.
     * - Password is valid.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterExistingUsernameExistingEmailValidPasswordNotDeclareBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        request.Email = "test123@gmail.com";
        request.Password = "123456";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Email address not available", result.Error.errorMessage);
    }


    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is not declared.
     * - Existing username is used.
     * - New email is used.
     * - Password is valid.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterExistingUsernameNewEmailValidPasswordNotDeclareBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        request.Email = "test000@gmail.com";
        request.Password = "123456";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Username not available", result.Error.errorMessage);
    }


    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is not declared.
     * - New username is used.
     * - Existing email is used.
     * - Password is valid.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterNewUsernameExistingEmailValidPasswordNotDeclareBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test0000";
        request.Email = "test123@gmail.com";
        request.Password = "123456";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Email address not available", result.Error.errorMessage);
    }


    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is not declared.
     * - New username is used.
     * - Existing email is used.
     * - Password is invalid.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterNewUsernameExistingEmailInvalidPasswordNotDeclareBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test0000";
        request.Email = "test123@gmail.com";
        request.Password = "1234";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }



    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is not declared.
     * - Existing username is used.
     * - New email is used.
     * - Password is invalid.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterExistingUsernameNewEmailInvalidPasswordNotDeclareBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        request.Email = "test000@gmail.com";
        request.Password = "1234";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }

    /**
     * This test verifies that when:
     * - RequireBothUsernameAndEmail is not declared.
     * - Existing username is used.
     * - Existing email is used.
     * - Password is invalid.
     * The registration fails and returns an appropriate error message.</p>
      
    @Test
    void testRegisterExistingUsernameExistingEmailInvalidPasswordNotDeclareBoth() {
        RegisterPlayFabUserRequest request = new RegisterPlayFabUserRequest();
        request.Username = "test123";
        request.Email = "test123@gmail.com";
        request.Password = "1234";
        PlayFabResult<RegisterPlayFabUserResult> result = PlayFabClientAPI.RegisterPlayFabUser(request);

        // Assert that result.Result is not null
        assertNotNull(result.Error, "Result should not be null");
        assertEquals("Invalid input parameters", result.Error.errorMessage);
    }

    // The login test could be implemented. H
    // However, it will cause throttling when we run the test multiple times within a short period of time.

}
*/
