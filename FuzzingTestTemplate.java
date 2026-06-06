import Final.*;
import com.code_intelligence.jazzer.api.FuzzedDataProvider;
public class FuzzingTestTemplate {


	    private static final SecurityConfig config = new SecurityConfig();
	    private static final AuthManager auth = new AuthManager(config);

	    public static void fuzzerTestOneInput(FuzzedDataProvider data) {
	        
	       
	        String fuzzedUsername = data.consumeString(100);
	        String fuzzedPassword = data.consumeString(100);
	        auth.authenticate(fuzzedUsername, fuzzedPassword);
	        
	       
	        String regUser = data.consumeString(100);
	        String regPass = data.consumeString(100);
	        String regRole = data.consumeString(20);
	        String regName = data.consumeString(50);
	        String regId = data.consumeString(20);
	        
	        // consumeRemainingAsString() is a Jazzer best practice for the final string parameter
	        String regContact = data.consumeRemainingAsString(); 
	        
	        auth.registerUser(regUser, regPass, regRole, regName, regId, regContact);
	    }
	}
