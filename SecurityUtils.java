package Final;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityUtils {
 // Generates a SHA-256 hash using MessageDigest
	    public static String hashPassword(String password) {
	        try {
	            MessageDigest digest=MessageDigest.getInstance("SHA-256");
	            byte[] encodedhash=digest.digest(password.getBytes());
	            return Base64.getEncoder().encodeToString(encodedhash);
	        } catch (NoSuchAlgorithmException e) {
	            // Applying "Fail Securely": log the error and halt rather than continuing insecurely
	            throw new RuntimeException("Critical security failure: Hashing algorithm not found.", e);
	        }
	    }

	    // Validates password against Admin-defined strength policies
	    public static boolean isPasswordStrong(String password) {
	        if (password.length() < 8) return false;
	        if (!password.matches(".*[A-Z].*")) return false; // Uppercase
	        if (!password.matches(".*[a-z].*")) return false; // Lowercase
	        if (!password.matches(".*\\d.*")) return false;   // Digits (0-9)
	        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) return false; // Special Char
	        return true;
	    }
	}