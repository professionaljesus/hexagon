package hex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.JPanel;

import com.Boi;
import hex.bots.BepsiMax.BeppeBot;
import hex.bots.CrazyBot;
import hex.bots.SimpleBot;

public class Panel extends JPanel implements Runnable, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Thread thread;
	private HexaMap H;
	private Player[] player;
	private final int MAX_TURN = 1000;
	private int turn, mapsize;

	private final int width = 1280;
	private final int height = 720;

	public Panel(Player[] player, int mapsize) throws IOException {
		super();
		setPreferredSize(new Dimension(width,height));
		setFocusable(true);

        requestFocus();
        
        this.mapsize = mapsize;
    	this.player = player;
    	initGame();

	}
	

	public void initGame() {
		//0.6015911100889212, 0.2711100296066967, 0.061405684209602795
		
		//0.8837214190122404, 0.46487856044682585, 0.05472276970794476
		/*0.979388220424738, 0.4237294463423128, 0.040420006959647536
		 * 0.2687604520880118, 0.14188537681292093, 0.01269229409707147
		 * 0.2687604520880118, 0.14188537681292093, 0.01269229409707147
		 * 0.28840757649341964, 0.14188537681292093, 0.01269229409707147
		 */

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
	public boolean end() {
		int left = 0;

		for(HashSet<Hexagon> a : H.getPhex()) {
			if(!a.isEmpty()) {
				left++;
			}
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
        	
        	repaint();

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
	
	
	private void gamerun() {
		if(!end() && turn < MAX_TURN) {
			long t;
			HashSet<Hexagon> send = new HashSet<Hexagon>();

			for(Player p: player) {
				send.clear();
				for(Hexagon a: H.getClonedPhex().get(p.getId() - 1)) {
					send.add(a);
				}
				t = System.currentTimeMillis();
				H.move(p.algo(send));
				//System.out.println("Player " + p.getId() + " " + (System.currentTimeMillis() - t) + " ms");
			}
			H.endTurn();
			//System.out.println("Turn: " + turn);
			turn++;
		}else
			initGame();
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
			gamerun();
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
