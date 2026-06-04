package Final;
import java.io.*;

public class SecurityConfig {
	    private static final String CONFIG_FILE = "config.txt";
	    public int minLength = 8;
	    public int minUpper = 1;
	    public int minLower = 1;
	    public int minDigit = 1;
	    public int minSpecial = 1;
	    public int maxAttempts = 3;

	    public SecurityConfig() {
	        loadConfig();
	    }

	    public void loadConfig() {
	        File file = new File(CONFIG_FILE);
	        if (!file.exists()) {
	            saveConfig(); // Create default if missing
	            return;
	        }
	        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	            String line = br.readLine();
	            if (line != null) {
	                String[] parts = line.split(",");
	                minLength = Integer.parseInt(parts[0]);
	                minUpper = Integer.parseInt(parts[1]);
	                minLower = Integer.parseInt(parts[2]);
	                minDigit = Integer.parseInt(parts[3]);
	                minSpecial = Integer.parseInt(parts[4]);
	                maxAttempts = Integer.parseInt(parts[5]);
	            }
	        } catch (Exception e) {
	            System.err.println("Error loading config, using defaults.");
	        }
	    }

	    public void saveConfig() {
	        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
	            bw.write(minLength + "," + minUpper + "," + minLower + "," + minDigit + "," + minSpecial + "," + maxAttempts);
	        } catch (IOException e) {
	            System.err.println("Error saving config.");
	        }
	    }

	    public boolean isPasswordStrong(String password) {
	        if (password.length() < minLength) return false;
	        if (password.replaceAll("[^A-Z]", "").length() < minUpper) return false;
	        if (password.replaceAll("[^a-z]", "").length() < minLower) return false;
	        if (password.replaceAll("[^0-9]", "").length() < minDigit) return false;
	        if (password.replaceAll("[a-zA-Z0-9]", "").length() < minSpecial) return false;
	        return true;
	    }
	}