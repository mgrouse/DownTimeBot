package service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.github.mgrouse.downtimebot.DownTimeBot;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;


public class ServiceFactory
{
    private static final String APPLICATION_NAME = "DownTimeBot";
    // private static final String TOKENS_DIRECTORY_PATH = "./tokens";

    private static final String CREDENTIALS_FILE_NAME = "downtimebot.json";
    // "DiscordDevCredentials.json";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    /**
     * Global instance of the scopes required. If modifying these scopes, delete
     * your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);


    private static Sheets m_service = null;


    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
//    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException
//    {
//	// Load client secrets.
//	ClassLoader loader = DownTimeBot.class.getClassLoader();
//
//	// get RESOURCE file as a BufferedReader
//	InputStream stream = loader.getResourceAsStream(CREDENTIALS_FILE_NAME);
//	InputStreamReader in = new InputStreamReader(stream);
//
//	GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, in);
//
//	// Build flow and trigger user authorization request.
//	Builder builder = new Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES);
//
//	builder.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)));
//
//	builder.setAccessType("offline");
//
//	builder.setApprovalPrompt("force").build();
//
//	GoogleAuthorizationCodeFlow flow = builder.build();
//
//	LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//
//	AuthorizationCodeInstalledApp app = new AuthorizationCodeInstalledApp(flow, receiver);
//
//	return app.authorize("user");
//    }


//    public static Sheets createSheetsService()
//    {
//
//	try
//	{
//	    // Build a new authorized API client service.
//	    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//
//	    Credential credential = getCredentials(HTTP_TRANSPORT);
//
//	    Sheets.Builder builder = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential);
//	    builder.setApplicationName(APPLICATION_NAME);
//
//	    m_service = builder.build();
//	}
//	catch (Exception e)
//	{
//	    e.printStackTrace();
//	}
//	return m_service;
//    }


    private static void createSheetsService()
    {
	// Load client secrets.
	ClassLoader loader = DownTimeBot.class.getClassLoader();

	// get RESOURCE file as a BufferedReader
	InputStream stream = loader.getResourceAsStream(CREDENTIALS_FILE_NAME);

	GoogleCredential credential;
	try
	{
	    credential = GoogleCredential.fromStream(stream)
		    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
	    // Build a new authorized API client service.
	    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

	    m_service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();

	}
	catch (Exception e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }


    public static Sheets getSheetsService()
    {
	if (null == m_service)
	{
	    createSheetsService();
	}

	return m_service;
    }

}
