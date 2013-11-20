package googleDrive;
import abstractConnector.Connector;


import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.activation.MimetypesFileTypeMap;


public class DriveConnector extends Connector {

  private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
  
  private HttpTransport httpTransport;
  private JsonFactory jsonFactory;
  private GoogleAuthorizationCodeFlow flow;
  private Drive service;
    
  public DriveConnector() {
	super("1063495183399.apps.googleusercontent.com","ic6lWswoGoXrROroCDylUlQ8");
  }
  
  public void init(){
	     httpTransport = new NetHttpTransport();
	     jsonFactory = new JacksonFactory();	   
	     flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, jsonFactory, APP_KEY, APP_SECRET, Arrays.asList(DriveScopes.DRIVE))
	        .setAccessType("online")
	        .setApprovalPrompt("auto").build();	  
  }
  
  public void connect() {
	  String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();	  	
	  showMessageDialog(url);

      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	  String code;
	  try {
			code = br.readLine();
			GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
		    GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
		    service = new Drive.Builder(httpTransport, jsonFactory, credential).build();	 
	  } catch (IOException e) {
			e.printStackTrace();
	  }
  }
 
 /*
  public String upload() {
	    File body = new File();
	    body.setTitle("My document");
	    body.setDescription("A test document");
	    body.setMimeType("text/plain");
	    
	    java.io.File fileContent = new java.io.File("document.txt");
	    FileContent mediaContent = new FileContent("text/plain", fileContent);

	    File file;
		try {
			file = service.files().insert(body, mediaContent).execute();
		    String shareLink = "https://docs.google.com/file/d/"+file.getId()+"/edit?usp=drivesdk";
		    return setLastUpp(shareLink);	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
  }
*/
  
  public String uploadFile(java.io.File img) throws IOException{    		
  		File body = new File();
	    body.setTitle(createtFileName());	    
	    FileContent mediaContent = new FileContent(new MimetypesFileTypeMap().getContentType(img), img);
	    File file = service.files().insert(body, mediaContent).execute();
		return "https://docs.google.com/file/d/"+file.getId()+"/edit?usp=drivesdk";	
  }  


}