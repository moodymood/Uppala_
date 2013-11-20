

































package abstractConnector;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.net.URL;
import java.util.Date;
import org.apache.log4j.Logger;

import javax.swing.JOptionPane;

public abstract class Connector {
	
	protected static String APP_KEY;
	protected static String APP_SECRET;
	private String appName;
	protected String lastUpp;	 
	protected static final Logger logger = Logger.getLogger(Connector.class);
	
	public Connector(String key, String secret) {
		appName = this.getClass().getCanonicalName();
		APP_KEY = key;
		APP_SECRET = secret;
			
		init();
		initOK();
		
		connect();
		connectionOK();
	}
	  
	public abstract void init();
	  
	public abstract void connect();
    
	public void showMessageDialog(String url){
		JOptionPane.showMessageDialog(null, "Press ok to continue once you have authenticated.");
	    try {
	  	  Desktop.getDesktop().browse(new URL(url).toURI());
	    } catch (Exception e1) {
	  	  e1.printStackTrace();
	    } 
	}    
	
    public boolean tryUploadFile(File file) {
    	if(file != null){	        	       
			try {
				setLastUpp(uploadFile(file));
				uploadOK();
		        return true;
			} catch (Exception e) {
				e.printStackTrace();
				uploadKO();
				return false;
			} 
    	}
		return false;
    }    
    
    public abstract String uploadFile(File file) throws Exception;
	
	public void shareFile() { 
	  	StringSelection selection = new StringSelection(getLastUpp());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);		
		Toolkit.getDefaultToolkit().beep();   
		clipboardOK();
	}
	
	@SuppressWarnings("deprecation")
	public String createtFileName(){
		Date date =  new Date();
		String fileName = date.toLocaleString().replace(" ", "_") +".jpg";
		fileNameOK();
		return fileName;
	}
	
    public String setLastUpp(String newLastUpp){
    	loggerDebug("setting lastUpp var (" + lastUpp + ")");
		return lastUpp = newLastUpp;
	}
		  
	public String getLastUpp(){
		return lastUpp;
	}
	
	public String getAppName(){
		return appName;
	}
	
	public void initOK(){
		loggerDebug("initialization done");
	}
	
	public void connectionOK(){
		loggerInfo("connected");
	}
	
	public void uploadOK(){
		loggerDebug("upload completed");
	}
	
	public void uploadKO(){
		loggerError("failed during upload");
	}
	
	public void uploadFileKO(){
		loggerError("problem getting the file to upload (null) ");
	}
	
	public void clipboardOK(){
		loggerInfo("Clipboard now ready (" + getLastUpp() + ")");
	}
	
	public void fileNameOK(){
		loggerDebug("screenshot file name created");
	}
		
	public void loggerInfo(String msg){
		logger.info(getAppName() + ": " + msg);
	}
	
	public void loggerDebug(String msg){
		logger.debug(getAppName() + ": " + msg);
	}
	
	public void loggerError(String msg){
		logger.error(getAppName() + ": " + msg);
	}
}
