package Final;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthenticateTest {

	    private AuthManager auth;
	    private String testUser; // Dynamic username so tests don't overlap

	    @BeforeEach
	    public void setUp() {
	        SecurityConfig testConfig = new SecurityConfig();
	        auth = new AuthManager(testConfig);
	        
	        // Generate a unique username and ID for every single test run
	        testUser = "validUser_" + System.currentTimeMillis();
	        String testId = "ID_" + System.currentTimeMillis();
	        
	        // Register a fresh, unlocked user to test against
	        auth.registerUser(testUser, "SecurePass1!", "Customer", "John", testId, "0795555555");
	    }

	    @Test
	    public void testSuccessfulLogin() {
	        User result = auth.authenticate(testUser, "SecurePass1!");
	        assertNotNull(result, "Authentication should return a User object on valid credentials.");
	        assertEquals(testUser, result.getUsername(), "The returned user should match the login username.");
	    }

	    @Test
	    public void testInvalidUsername() {
	        User result = auth.authenticate("ghostUser", "SecurePass1!");
	        assertNull(result, "Authentication should return null for a non-existent username.");
	    }

	    @Test
	    public void testInvalidPassword() {
	        User result = auth.authenticate(testUser, "WrongPassword!");
	        assertNull(result, "Authentication should return null for an incorrect password.");
	    }

	    @Test
	    public void testAccountLockout() {
	        // Since we cannot read the private config file, we will simulate a brute-force attack.
	        // We will purposely fail the login 10 times to guarantee we hit the maximum lockout limit.
	        for (int i = 0; i < 10; i++) {
	            auth.authenticate(testUser, "WrongPassword!");
	        }
	        
	        // Now, we test if the account is truly locked by attempting to log in with the CORRECT password.
	        // If the system is secure, it should return null (deny access).
	        User lockedResult = auth.authenticate(testUser, "SecurePass1!");
	        assertNull(lockedResult, "Authentication should return null if the account is locked, even with correct password.");
	    }
}
