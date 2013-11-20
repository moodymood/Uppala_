package screenManagement;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;


public class ScreenShotter {
	
	protected static final Logger logger = Logger.getLogger(ScreenShotter.class);
	private static int count;
	private Robot robot;
	private Rectangle captureSize;
	private BufferedImage bufferedImage;
	private File outputfile;
	
	public ScreenShotter(){	
		try {
			robot = new Robot();
			logger.debug("cam created");
		} catch (AWTException e) { 
			logger.error("error during cam creation");
			e.printStackTrace(); 
		}
	}
	
	public File getTotalShoot() {     
		logger.debug("calling getTotalShoot()");  
		captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		return shoot();
	}

	public File getPartialShoot() {     
		logger.debug("calling getPartialShoot().");
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final BufferedImage screen = robot.createScreenCapture(new Rectangle(screenSize));
       
        Rectangle rect = (new ScreenPointsGetter(screen)).getRect();    
        if(rect!=null){
        	captureSize = new Rectangle((int) rect.getMinX(), (int) rect.getMinY(), rect.width,rect.height);
        	return shoot();
        }	
        logger.error("attention, no area selected");
    	return null;
	}
        
	public File shoot(){
		bufferedImage = robot.createScreenCapture(captureSize);
		outputfile = new File("img"+ count + ".jpg"); 
		try {
			ImageIO.write(bufferedImage, "jpg", outputfile);
			logger.debug("screenshot taken![" + count++ + "]"); 
		} catch (IOException e) { 
			logger.error("error while shooting");
			e.printStackTrace(); 
		}	  
    	return outputfile;
	}
        
	public void setScreenshotSize(int x, int y){
		captureSize.setSize(x, y);
	}
		
	public String getTotalScreenshot(){
		return "Total shoots: " + count;
	}
	
	public String fileToString(){
		return outputfile.getAbsolutePath();
	}
}
