package hex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.IOException;
import java.util.HashSet;

import javax.swing.JPanel;

import hex.bots.BeppeBot;
import hex.bots.GustafBot2;
import hex.bots.SimpleBot;
import hex.bots.Snake;

public class Panel extends JPanel implements Runnable, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Thread thread;
	private HexaMap H;
	private Player[] player;
	private final int MAX_TURN = 2000;
	private int turn, mapsize;

	private final int width = 1280;
	private final int height = 720;
	
	public Panel() throws IOException {
		super();
		setPreferredSize(new Dimension(width,height));
		setFocusable(true);

        requestFocus();
        
        mapsize = 6;

    	player = new Player[3];


		player[0] = new BeppeBot(1,mapsize, Color.GREEN, "BEPPNATION");
		player[1] = new BeppeBot(2,mapsize, Color.BLUE, "WILDCARD");
		player[2] = new GustafBot2(3,mapsize, Color.RED, "GURRA");

		H = new HexaMap(mapsize,width,height,player);

	
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
	 * Om det bara finns en spelare kvar p� mappen
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
			long t;
			for(Player p: player) {
				t = System.currentTimeMillis();
				H.move(p.algo(H.getPhex().get(p.getId() - 1)));
				System.out.println("Player " + p.getId() + " " + (System.currentTimeMillis() - t) + " ms");
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
	
	
	
	//System.out.println("Player: " + p.getId() + " X Move :" + p.algo(H.getPhex().get(p.getId() - 1))[2] + " Y Move :" + p.algo(H.getPhex().get(p.getId() - 1))[3]+ " XTarget Move :" + p.algo(H.getPhex().get(p.getId() - 1))[4] + " YTarget Move :" + p.algo(H.getPhex().get(p.getId() - 1))[5]+" Res " + p.algo(H.getPhex().get(p.getId() - 1))[1]);
	

}
