package hex;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Thread thread;
	private HexaMap H;
	private Player[] player;
	private int width = 1280;
	private int height = 720;
	
	public Panel() throws IOException {
		super();
		setPreferredSize(new Dimension(width,height));
        requestFocus();
    	player = new Player[4];
		player[0] = new Player(1);
		player[1] = new Player(2);
		player[2] = new Player(3);
		player[3] = new Player(4);
		H = new HexaMap(4,width,height);
		H.startMap(4, player);
 
	}
	
	public void addNotify() {
        super.addNotify();
        if(thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
	
	
	@Override
	  protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    H.draw(g);
	}



	@Override
	public void run() {
		long start;
        long elapsed;
        long wait;
         
         
        //game loop
        while(true) {
             
            start = System.nanoTime();
             
            elapsed = System.nanoTime() - start;
            
            repaint();
             
            wait = 1000/60 - elapsed / 1000000;
            if (wait < 0) wait  = 5;
             
             
            try {
                Thread.sleep(wait);
            }
            catch(Exception e) {
                e.printStackTrace();
                 
            }
             
             
        }
		
	}
	
	
	
	
	

}
