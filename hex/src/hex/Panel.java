package hex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.IOException;
import java.util.HashSet;

import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Thread thread;
	private HexaMap H;
	private Player[] player;
	private final int MAX_TURN = 1000;
	private int turn;

	private final int width = 1280;
	private final int height = 720;
	
	public Panel() throws IOException {
		super();
		setPreferredSize(new Dimension(width,height));
		setFocusable(true);

        requestFocus();

    	player = new Player[3];

		player[0] = new GustafBot(1,4, Color.GREEN);
		player[1] = new GustafBot(2,4, Color.BLUE);
		player[2] = new GustafBot(3,4, Color.RED);
 


		H = new HexaMap(4,width,height,player);

	
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
		int left = 0;
		for(HashSet<Hexagon> a : H.getPhex()) {
			if(!a.isEmpty())
				left++;
		}
		
		return left < 2;
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
				H.move(p.algo(H.getPhex().get(p.getId() - 1)));
			}
			H.endTurn();
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
