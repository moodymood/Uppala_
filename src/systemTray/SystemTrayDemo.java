package systemTray;

import screenManagement.ScreenShotter;
import abstractConnector.Connector;
import dropbox.DropboxConnector;
import googleDrive.DriveConnector;
import gui.ServiceChooser;























































































































































































































































































































































































































































































































































































































































































































































































































































































































import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.ImageIcon;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;



public class SystemTrayDemo {
	
	protected static final Logger logger = Logger.getLogger(SystemTrayDemo.class);
	
	private TrayIcon trayIcon;
	private JPopupMenu popupMenu;
	
	private Connector dbConn;
	private ScreenShotter cam;
	
	public SystemTrayDemo(String cloudType){
		super();
		logger.info(System.getProperty("java.vendor") + " " + System.getProperty("java.vendor.url"));
		logger.info(System.getProperty("java.version"));	
		
		if(cloudType.equals("D") || cloudType.equals("d")){
			dbConn = new DropboxConnector();
			logger.info("Welcome to Dropbox");
		}else if(cloudType.equals("G") || cloudType.equals("g")){
			dbConn = new DriveConnector();
			logger.info("Welcome to Drive Cloud");
		}
		cam = new ScreenShotter();
		create(); 
	}

	private void create() {
		try {
			if (SystemTray.isSupported()) {
				popupMenu = createPopupMenu();
				logger.debug("popMenu successfully created");
				SystemTray systemTray = SystemTray.getSystemTray();
				Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/cam.png"));
				TrayIcon trayIcon = new TrayIcon(img);
				systemTray.add(trayIcon);
				trayIcon.addMouseListener(new SystemTrayMouseListener());
				logger.debug("SystemTray supported: mouseListener added to the sistem tray");
			}
		} catch (AWTException ex) {
			logger.debug("SystemTray create() failed");
			ex.printStackTrace();
		}
	}


	private JPopupMenu createPopupMenu() {
		JPopupMenu p = new JPopupMenu();

		JMenuItem selectAll = new JMenuItem("Select All", new ImageIcon(getClass().getResource("/img/selectAll.png")));
		JMenuItem selectArea = new JMenuItem("Select Area", new ImageIcon(getClass().getResource("/img/selectArea.png")));
		JMenuItem hide = new JMenuItem("Hide", new ImageIcon(getClass().getResource("/img/invisible.png")));
		JMenuItem quit = new JMenuItem("Quit", new ImageIcon(getClass().getResource("/img/exit.png")));
		
		selectAll.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) { 
				logger.debug("calling \"selectAll()\"");
				popupMenu.setVisible(false);
				try {		        
					dbConn.tryUploadFile(cam.getTotalShoot());	        
					dbConn.shareFile();			    
				} catch (Exception e1) {
					e1.printStackTrace();
				}		
			}
		});
    
		selectArea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.debug("calling \"selectArea()\"");
				popupMenu.setVisible(false);
				try {
					if(dbConn.tryUploadFile(cam.getPartialShoot()))	        
						dbConn.shareFile();	      
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
 
		hide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.debug("calling \"hide()\"");
				popupMenu.setVisible(false);				
			}
		});

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
					logger.debug("calling \"quit()\"");
					SystemTray.getSystemTray().remove(trayIcon);
					logger.info("See you soon :)");
					System.exit(0);      
			}
   	 	});

		p.add(selectArea);
		logger.debug("adedd selectArea option to popMenu");
		p.add(selectAll);
		logger.debug("adedd selectAll option to popMenu");
		p.addSeparator();
		p.add(hide);
		logger.debug("adedd hide option to popMenu");
		p.add(quit);
		logger.debug("adedd exit option to popMenu");
		return p;
	}

	private class SystemTrayMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			//Pulsante sinistro del mouse  
			if (e.getButton() == 1 || e.getButton() == 3) { 	  
				if (popupMenu.isVisible()) 
					popupMenu.setVisible(false);
				else 
					popupMenu.show(e.getComponent(), e.getX(), e.getY());						
	        } 
		}  
	}

	public static void main(String[] args) {
		logger.info("Uppala STARTED");
		String cloudService = (new ServiceChooser()).getCloudService();
		if(cloudService!=null)
			new SystemTrayDemo(cloudService);
		else
			System.out.println("Quitting..");
	
	}

}