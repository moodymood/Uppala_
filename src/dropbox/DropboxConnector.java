package dropbox;
import abstractConnector.Connector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.Session.AccessType;


public class DropboxConnector extends Connector {
	 
    private static final AccessType ACCESS_TYPE = AccessType.APP_FOLDER;
    private static DropboxAPI<WebAuthSession> mDBApi;
    private WebAuthSession session;
    private AppKeyPair appKeys;
  
    public DropboxConnector() {
    	super("6sdvrf7qr429r70","vq7xkeykwm6zg2s");
    }
    
    public void init(){
        appKeys = new AppKeyPair(APP_KEY, APP_SECRET);       
        session = new WebAuthSession(appKeys, ACCESS_TYPE, new AccessTokenPair("s2oetzrhnk30w17", "vkw9jy77nhdkhbr"));
        mDBApi = new DropboxAPI<WebAuthSession>(session);
    }
    
    public void connect(){   	

    }
       
    public String uploadFile(File file) throws DropboxException{        
	        ByteArrayInputStream inputStream = fileConverter(file);
	        if(inputStream!=null){
	        	conversionOK();
	        	Entry newEntry = mDBApi.putFile(createtFileName(), inputStream, file.length(), null, null);
	        	return (mDBApi.share(newEntry.path)).url;
	        }else{
	        	conversionKO();
	        	return null;
	        }
    }    
    
    public ByteArrayInputStream fileConverter(File screenshot) {
    	FileInputStream input;
    	ByteArrayOutputStream output = new ByteArrayOutputStream ();
    	byte [] data;
        byte [] buffer = new byte [65536];
        
        int l;
		try {
			input = new FileInputStream (screenshot);
	        while ((l = input.read (buffer)) > 0)
	            output.write (buffer, 0, l);
	        input.close ();
	        output.close ();
	        data = output.toByteArray ();	        
	        return new ByteArrayInputStream(data);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
    }
    
	public void conversionOK(){
		loggerDebug("screenshot conversion done");
	}
	
	public void conversionKO(){
		loggerDebug("conversion failed");
	}
}