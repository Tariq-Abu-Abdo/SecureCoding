package Final;
import com.code_intelligence.jazzer.api.FuzzedDataProvider;
public class FuzzingTestTemplate {


		  
		    private static final SecurityConfig config = new SecurityConfig();
		    private static final AuthManager auth = new AuthManager(config);

		    public static void fuzzerTestOneInput(FuzzedDataProvider data) {
		        
		        String fuzzedUsername = data.consumeString(100);
		        String fuzzedPassword = data.consumeString(100);
		        
		        auth.authenticate(fuzzedUsername, fuzzedPassword);
		    }
	}
