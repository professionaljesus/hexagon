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
	
	public Panel() throws IOException {
		super();
		setPreferredSize(new Dimension(1280,720));
        requestFocus();
 
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
	
	public void test(){
		System.out.println("testing");
	}
	

	

}
