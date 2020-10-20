import javax.swing.JFrame; 
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;

public class Application extends JFrame implements MouseListener, KeyListener, MouseWheelListener {
    private final int SCREEN_WIDTH = 1280;
    private final int SCREEN_HEIGHT = 920;
    private BufferedImage image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
    private double zoom = 0.008;
    private double xPos = 0;
    private double yPos = 0;
    private double zx, zy, cX, cY, tmp;
    private int maxIterations = 80;
    private int xMouseStart = 0;
    private int xMouseEnd = 0;
    private int yMouseStart = 0;
    private int yMouseEnd = 0;
    

    public static void main(String[] args) throws Exception {
        new Application();
    }

    public Application() {
        super("Mandelbrot");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setVisible(true);
        setResizable(true);
        addMouseListener(this);
        addKeyListener(this);
        addMouseWheelListener(this);
        // Method to compute
        compute();
        
    }

    private void compute() {
    	image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
    	int modHeight = SCREEN_HEIGHT >> 1; //Bit Shifting. Changes the available use area
    	int modWidth = SCREEN_WIDTH >> 1;
    	
    	for(int y = 0; y < SCREEN_HEIGHT; y++) {
    		for(int x = 0; x < SCREEN_WIDTH; x++) {
    		zx = zy = 0;
    		cX = xPos + (x-modWidth) * zoom; //canvas
    		cY = yPos + (y-modHeight) * zoom; //canvas
    		
    		int iteration;
    		for(iteration = 0; iteration < maxIterations && zx*zx+zy*zy < 15; iteration++) {
    			tmp = zx * zx - zy * zy + cX;
    			zy = 2 * zx * zy + cY;
    			zx = tmp;
    		}   
    		if(iteration == maxIterations) {
    			//Do nothing
    		}else {
    			

    			double r = iteration | (iteration << 3);
    			while(r > 255) {
    				r -= 255;
    			}
    			
    			double g = iteration | (iteration << 5);
    			while(g > 255) {
    				g -= 255;
    			}
    			
    			double b = iteration | (iteration << 3);
    			while(b > 255) {
    				b -= 255;
    			}
    			
    			Color color = new Color((int) r, (int) g, (int) b);
    			image.setRGB(x, y, color.getRGB());
    		}
    	}	
    		
    		
    	}
    	
    	repaint();
    }
    
    public void paint(Graphics g) {
    	g.drawImage(image, 0, 0, this); //tells it to use entire space
    	
    }
    

    // MouseListener
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

   }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    	//System.out.println(e.getX());
    	if(e.getButton() == 1) {
    		xMouseStart = e.getX();
    		yMouseStart = e.getY();
    	}

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    	if(e.getButton() == 1) {
    		xMouseEnd = e.getX();
    		yMouseEnd = e.getY();
    		double xMove = xMouseEnd-xMouseStart;
    		double yMove = yMouseEnd-yMouseStart;
    		//System.out.println("xMove: " + xMove); //how much cursor moved while pressing mouse button
    		//System.out.println("yMove: " + yMove);
    		xPos = xPos - (xMove * zoom);   //moves canvas around
    		yPos = yPos - (yMove * zoom);
    		compute();
    	}

    }

    // KeyListener
    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ADD || e.getKeyCode() == KeyEvent.VK_EQUALS) {
            //System.out.println("you pressed the plus sign"); //test to make sure it works
        	zoom *= 0.9;
        	compute();
        }
        if (e.getKeyCode() == KeyEvent.VK_MINUS || e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
        	zoom *= 1.1;
        	compute();
        }
        if(e.getKeyCode()== KeyEvent.VK_CONTROL){
        		maxIterations += 5;
        		compute();
        }
        if(e.getKeyCode()== KeyEvent.VK_SHIFT){
        		maxIterations -= 5;
        		compute();
        }
        if(e.getKeyCode()== KeyEvent.VK_LEFT){
        	xPos += 12 * zoom;
    		compute();
        }
        if(e.getKeyCode()== KeyEvent.VK_RIGHT){
        	xPos -= 12 * zoom;
    		compute();
        }
        if(e.getKeyCode()== KeyEvent.VK_UP){
        	yPos += 12 * zoom;
        	compute();
        }
        if(e.getKeyCode()== KeyEvent.VK_DOWN){
        	yPos -= 12 * zoom;
        	compute();
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    	int notches =e.getWheelRotation();
    	if(notches < 0) {
    		//System.out.println("Mouse wheel moved up " + -notches + " notches.");
    		zoom *= 0.95;
    	} else {
    		//System.out.println("Mouse wheel moved down " + notches + " notches.");
    		zoom *= 1.04;
    	}
    	compute();

    }

}