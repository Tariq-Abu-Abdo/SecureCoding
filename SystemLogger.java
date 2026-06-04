package Final;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SystemLogger{

	    private static final Logger logger = Logger.getLogger("ShipTrackLogger");
	    private static FileHandler fileHandler;

	    // Static block initializes the logger the very first time this class is called
	    static {
	        try {
	            // SECURITY: Prevent logs from also printing to the console (Attack Surface Reduction)
	            // We don't want hackers reading stack traces on the screen if an error happens.
	            logger.setUseParentHandlers(false);

	            // True means it will "append" to the file rather than overwriting it every time
	            fileHandler = new FileHandler("system_audit.log", true);
	            fileHandler.setFormatter(new SimpleFormatter());
	            logger.addHandler(fileHandler);
	            logger.setLevel(Level.INFO);

	        } catch (SecurityException | IOException e) {
	            // Fail Securely: If the logger breaks, print a warning, but don't crash the app.
	            System.err.println("[WARNING] Security Logger failed to initialize. Audit logging is disabled.");
	        }
	    }

	    // Centralized method to write to the file
	    public static void logAction(String action) {
	        if (fileHandler != null) {
	            logger.info(action);
	        }
	    }}