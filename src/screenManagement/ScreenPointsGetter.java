package screenManagement;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

import org.apache.log4j.Logger;



public class ScreenPointsGetter {
	protected static final Logger logger = Logger.getLogger(ScreenPointsGetter.class);
    
	Rectangle captureRect;

    ScreenPointsGetter(final BufferedImage screen) {
        final BufferedImage screenCopy = new BufferedImage(screen.getWidth()+50,screen.getHeight()-50,screen.getType());
        final JLabel screenLabel = new JLabel(new ImageIcon(screenCopy));

        JPanel panel = new JPanel();       
        panel.add(screenLabel, BorderLayout.CENTER);
        repaint(screen, screenCopy);
        screenLabel.repaint();
        
        screenLabel.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				screenLabel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));			
			}

			@Override
			public void mouseExited(MouseEvent arg0) {				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				try {
					// Simulo il tasto ENTER
					new Robot().keyPress(KeyEvent.VK_ENTER);
					logger.debug("selecting screenshot area: done");
				} catch (AWTException e) {
					logger.error("problem selecting area");
					e.printStackTrace();
				}				
			}
        	
        });
        
        screenLabel.addMouseMotionListener(new MouseMotionAdapter() {

            Point start = new Point();

            @Override
            public void mouseMoved(MouseEvent me) {
                start = me.getPoint();
                repaint(screen, screenCopy);
                screenLabel.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent me) {
                Point end = me.getPoint();
                Point negativeStart;
                
                int deltaX = end.x-start.x,
                	deltaY = end.y-start.y,
                
                    AbsDeltaX = Math.abs(deltaX),
                	AbsDeltaY = Math.abs(deltaY);
                
                if(deltaX<0||deltaY<0){
                	if(deltaX<0){
                		if(deltaY<0){ // X, Y < 0
	                		negativeStart = new Point(end.x, end.y);
		                	captureRect = new Rectangle(negativeStart,
	                            new Dimension(AbsDeltaX, AbsDeltaY) );
	                	}else{ // X < 0, Y > 0
	                		negativeStart = new Point(end.x, start.y);
	                		captureRect = new Rectangle(negativeStart,
	                				new Dimension(AbsDeltaX, deltaY) );	 
	                	}
	                }else{	 // X > 0, Y < 0    
	                	negativeStart = new Point(start.x, end.y);
            			captureRect = new Rectangle(negativeStart,
            					new Dimension(deltaX, AbsDeltaY) );
	                }         
                }else{
                captureRect = new Rectangle(start,
                        new Dimension(deltaX, deltaY));            
                }
                repaint(screen, screenCopy);
                screenLabel.repaint();
            }   
        });             
        JOptionPane.showMessageDialog(null, panel);        
    }

    public void repaint(BufferedImage orig, BufferedImage copy) {

    	Graphics2D g = copy.createGraphics();
    	g.drawImage(orig,0,0, null);
        if (captureRect!=null) {
            g.setColor(Color.BLUE);
            g.draw(captureRect);
            g.setColor(new Color(255,255,255,0));
            g.fill(captureRect);
        }
        g.dispose();
    }
    
    public Rectangle getRect(){
    	return captureRect;
    }
}