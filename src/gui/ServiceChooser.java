package gui;

import java.awt.Frame;

import javax.swing.JOptionPane;

public class ServiceChooser {

	Object[] possibilities = {"Dropbox", "Google Drive"};
    String cloudService;

	public ServiceChooser(){
		setCloudService();
	}

	public void setCloudService(){
		cloudService = (String)JOptionPane.showInputDialog(
	        		new Frame(),
	                    	"Choose your cloud service",
	                    	"Customized Dialog",
	                    	JOptionPane.PLAIN_MESSAGE,
	                    	null,
	                    	possibilities,
	                    	"Dropbox");
	}
	
	public String getCloudService(){
	//If a string was returned, say so.
		if ((cloudService != null) && (cloudService.length() > 0)) {
	    		if(cloudService.equals("Dropbox"))
	    			return "D";
	    		else
	    			return  "G";
		}else{
			return null;
		}

	}
}
