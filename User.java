package Final;

public class User{
	    private String username;
	    private String passwordHash;
	    private String role; 
	    private int failedAttempts;
	    private boolean isLocked;
	    private String fullName;
	    private String idNumber;
	    private String contactNumber;

	    public User(String username, String passwordHash, String role, int failedAttempts, boolean isLocked, String fullName, String idNumber, String contactNumber) {
	        this.username = username;
	        this.passwordHash = passwordHash;
	        this.role = role;
	        this.failedAttempts = failedAttempts;
	        this.isLocked = isLocked;
	        this.fullName = fullName;
	        this.idNumber = idNumber;
	        this.contactNumber = contactNumber;
	    }

	    public String getUsername() { return username; }
	    public String getPasswordHash() { return passwordHash; }
	    public String getRole() { return role; }
	    public int getFailedAttempts() { return failedAttempts; }
	    public void setFailedAttempts(int attempts) { this.failedAttempts = attempts; }
	    public boolean isLocked() { return isLocked; }
	    public void setLocked(boolean locked) { this.isLocked = locked; }
	    
	    // NEW SETTERS
	    public String getFullName() { return fullName; }
	    public void setFullName(String fullName) { this.fullName = fullName; }
	    public String getIdNumber() { return idNumber; }
	    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
	    public String getContactNumber() { return contactNumber; }
	    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

	    @Override
	    public String toString() {
	        return username + "," + passwordHash + "," + role + "," + failedAttempts + "," + isLocked + "," + fullName + "," + idNumber + "," + contactNumber;
	    }
	}