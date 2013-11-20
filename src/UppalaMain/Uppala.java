package UppalaMain;

import java.awt.AWTException;
import java.io.IOException;

import systemTray.SystemTrayDemo;


public class Uppala {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String OS = System.getProperty("os.name").toLowerCase();
		System.out.println(OS);
		String current = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Current dir:"+current);
 String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" +currentDir);

	}

}
