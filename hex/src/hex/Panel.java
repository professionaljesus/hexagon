package hex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Thread thread;
	private HexaMap H;
	private Player[] player;
	private final int MAX_TURN = 100;
	private int turn;

	private final int width = 1280;
	private final int height = 720;
	
	public Panel() throws IOException {
		super();
		setPreferredSize(new Dimension(width,height));
		setFocusable(true);

        requestFocus();
    	player = new Player[3];

		player[0] = new SimpleBot(1,4);
		player[1] = new SimpleBot(2,4);
		player[2] = new SimpleBot(3,4);
		
		H = new HexaMap(4,width,height);
		H.GetNeighbour(6, 3, 5);
		H.startMap(3, player);
		turn = 0;
	}
	
	public void addNotify() {
        super.addNotify();
        if(thread == null) {
            thread = new Thread(this);
			addKeyListener(this);
            thread.start();
        }
    }
	
	
	@Override
	  protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    H.draw(g);
	}
	
	/**
	 * Om det bara finns en spelare kvar på mappen
	 * @return True om bara en kvar, false annars
	 */
	private boolean end() {
		int owner = 0;
		for (Hexagon[] u: H.getHexaMap()) {
		    for (Hexagon elem: u) {
		    	if(elem != null) {
			    	if(owner == 0)
			    		owner = elem.getOwner();
			    	else if(owner != elem.getOwner() && elem.getOwner() != 0)
			    			return false;
		    	}
		    }
		}
		return true;
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

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(!end() && turn < MAX_TURN) {
			for(Player p: player) {
				p.algo(H);
			}
			
			
			
			System.out.println("Turn: " + turn);
			turn++;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
		
	}
	
	
	
	
	

}
