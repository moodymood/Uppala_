package UppalaMain;

import gui.ServiceChooser;


public class Uppala {
	
	public static void main(String[] args) {
		String cloudService = (new ServiceChooser()).getCloudService();
		if(cloudService!=null)
			new UppalaSystemTray(cloudService);
		else
			System.out.println("Quitting..");
	
	}
}


