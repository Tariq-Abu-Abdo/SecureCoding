package Final;
import java.io.*;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
public class AuthManager {

	    private static final String CREDENTIALS_FILE = "shiptrack_users.txt";
	    private Map<String, User> userDatabase = new HashMap<>();
	    private SecurityConfig config;

	    public AuthManager(SecurityConfig config) {
	        this.config = config;
	        loadUsersFromFile();
	    }

	    public boolean hasUsers() { return !userDatabase.isEmpty(); }

	    private void loadUsersFromFile() {
	        File file = new File(CREDENTIALS_FILE);
	        if (!file.exists()) return;
	        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                if (line.trim().isEmpty()) continue; 
	                String[] parts = line.split(",");
	                if (parts.length == 8) {
	                    User user = new User(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Boolean.parseBoolean(parts[4]), parts[5], parts[6], parts[7]);
	                    userDatabase.put(user.getUsername(), user);
	                } else {
	                    System.err.println("[WARNING] Data mismatch. Skipping: " + line);
	                }
	            }
	        } catch (Exception e) { System.err.println("DB load error."); }
	    }

	    public void saveUsersToFile() {
	        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
	            for (User user : userDatabase.values()) {
	                bw.write(user.toString()); bw.newLine();
	            }
	        } catch (IOException e) { System.err.println("DB save error."); }
	    }

	    private String hashPassword(String password) {
	        try {
	            MessageDigest digest = MessageDigest.getInstance("SHA-256");
	            return Base64.getEncoder().encodeToString(digest.digest(password.getBytes()));
	        } catch (Exception e) { throw new RuntimeException("Hashing error", e); }
	    }

	    
	    
	    
	    public boolean registerUser(String username, String password, String role, String name, String id, String contact) {
	        // 1. Check if username is taken
	        if (userDatabase.containsKey(username)) { 
	            System.out.println("Registration failed: Username already exists."); 
	            return false;}
	        
	        for (User existingUser : userDatabase.values()) {
	            if (existingUser.getRole().equals(role) && existingUser.getIdNumber().equals(id)) {
	                System.out.println("Registration failed: ID Number '" + id + "' is already registered for a " + role + ".");
	                return false;
	            }}
	        if (!config.isPasswordStrong(password)) { 
	            System.out.println("Registration failed: Password does not meet security requirements."); 
	            return false; 
	        }
	        
	        //Save the user
	        userDatabase.put(username, new User(username, hashPassword(password), role, 0, false, name, id, contact));
	        saveUsersToFile();
	        SystemLogger.logAction("ACCOUNT CREATED: Role [" + role + "] Username [" + username + "]");
	        System.out.println(role + " registered successfully."); 
	        return true;
	    }

	    // ===LOGIC FOR FAILED ATTEMPTS ===
	    public User authenticate(String username, String password) {
	        User user = userDatabase.get(username);
	        if (user == null) {
	            System.out.println("Login failed.");
	            return null;
	        }
	        if (user.isLocked()) {
	            System.out.println("Account locked. Contact System Admin.");
	            return null;
	        }
	        if (user.getPasswordHash().equals(hashPassword(password))) {
	            user.setFailedAttempts(0); // Reset on success
	            saveUsersToFile();
	            SystemLogger.logAction("LOGIN SUCCESS: User [" + username + "]");
	            return user;
	        } else {
	            user.setFailedAttempts(user.getFailedAttempts() + 1);
	            int attemptsRemaining = config.maxAttempts - user.getFailedAttempts();
	            
	            if (user.getFailedAttempts() >= config.maxAttempts) {
	                user.setLocked(true);
	                System.out.println("SECURITY ALERT: Account locked due to reaching maximum failed attempts (" + config.maxAttempts + ").");
	                SystemLogger.logAction("SECURITY LOCKOUT: User [" + username + "] exceeded maximum attempts.");
	            } else {
	                System.out.println("Invalid password. Attempts remaining before lockout: " + attemptsRemaining);
	                SystemLogger.logAction("LOGIN FAILED: Invalid password for [" + username + "].");
	            }
	            saveUsersToFile();
	            return null;
	        }
	    }

	    public void removeUser(String username) {
	        if (userDatabase.remove(username) != null) { 
	        	saveUsersToFile(); 
	        	SystemLogger.logAction("ADMIN ACTION: Admin permanently removed user [" + username + "] from the system.");
	        System.out.println("User removed."); } 
	        else { System.out.println("User not found."); }
	    }

	    public void lockUnlockUser(String username, boolean lockStatus) {
	        User user = userDatabase.get(username);
	        if (user != null) {
	            user.setLocked(lockStatus);
	            if(!lockStatus) user.setFailedAttempts(0);
	            saveUsersToFile();
	            SystemLogger.logAction("ADMIN ACTION: Admin changed lock status of [" + username + "] to " + lockStatus);
	            System.out.println("User " + username + " lock status set to: " + lockStatus);
	        } else { System.out.println("User not found."); }
	    }
	}