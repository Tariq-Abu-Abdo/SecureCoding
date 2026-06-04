package Final;

import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class RegisterUserTest {

	    private AuthManager auth;
	    private String existingUser;
	    private String existingId;

	    @BeforeEach
	    public void setUp() {
	        SecurityConfig testConfig = new SecurityConfig();
	        auth = new AuthManager(testConfig);
	        
	        // Use UUID for mathematically guaranteed uniqueness, grabbing the first 8 characters
	        existingUser = "user_" + UUID.randomUUID().toString().substring(0, 8);
	        existingId = "ID_" + UUID.randomUUID().toString().substring(0, 8);
	        
	        // Extremely strong password to bypass any strict default config policies
	        auth.registerUser(existingUser, "SuperStrongPass123!@#", "Customer", "John Doe", existingId, "0790000000");
	    }

	    @Test
	    public void testSuccessfulRegistration() {
	        String newUser = "new_" + UUID.randomUUID().toString().substring(0, 8);
	        String newId = "ID_" + UUID.randomUUID().toString().substring(0, 8);
	        
	        boolean result = auth.registerUser(newUser, "SuperValidPass123!@#", "Customer", "Jane Doe", newId, "0791111111");
	        assertTrue(result, "Registration should succeed with valid, unique inputs.");
	    }

	    @Test
	    public void testDuplicateUsername() {
	        String newId = "ID_" + UUID.randomUUID().toString().substring(0, 8);
	        boolean result = auth.registerUser(existingUser, "AnotherPass1!@#", "Delivery", "Mark", newId, "0792222222");
	        assertFalse(result, "Registration should fail when the username already exists.");
	    }

	    @Test
	    public void testDuplicateIdNumberSameRole() {
	        String newUser = "another_" + UUID.randomUUID().toString().substring(0, 8);
	        boolean result = auth.registerUser(newUser, "SuperValidPass123!@#", "Customer", "Jake", existingId, "0793333333");
	        assertFalse(result, "Registration should fail when the ID number is already registered to the same role.");
	    }

	    @Test
	    public void testWeakPassword() {
	        String weakUser = "weak_" + UUID.randomUUID().toString().substring(0, 8);
	        String newId = "ID_" + UUID.randomUUID().toString().substring(0, 8);
	        boolean result = auth.registerUser(weakUser, "weak", "Customer", "Sarah", newId, "0794444444");
	        assertFalse(result, "Registration should fail if the password does not meet security requirements.");
	    }
	}