package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import com.github.mgrouse.downtimebot.DownTimeBot;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.Builder;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;


public class ServiceFactory
{
    private static final String APPLICATION_NAME = "DownTimeBot";
    private static final String TOKENS_DIRECTORY_PATH = "./tokens";

    private static final String CREDENTIALS_FILE_NAME = "DiscordDevCredentials.json";

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
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException
    {
	// Load client secrets.
	ClassLoader loader = DownTimeBot.class.getClassLoader();

	// get RESOURCE file as a BufferedReader
	InputStream stream = loader.getResourceAsStream(CREDENTIALS_FILE_NAME);
	InputStreamReader in = new InputStreamReader(stream);

	GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, in);

	// Build flow and trigger user authorization request.
	Builder builder = new Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES);

	builder.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)));

	builder.setAccessType("offline");

	GoogleAuthorizationCodeFlow flow = builder.build();

	LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

	AuthorizationCodeInstalledApp app = new AuthorizationCodeInstalledApp(flow, receiver);

	return app.authorize("user");
    }


    public static Sheets createSheetsService()
    {

	try
	{
	    // Build a new authorized API client service.
	    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

	    Credential credential = getCredentials(HTTP_TRANSPORT);

	    Sheets.Builder builder = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential);
	    builder.setApplicationName(APPLICATION_NAME);

	    m_service = builder.build();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return m_service;
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
