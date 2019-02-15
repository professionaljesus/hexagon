package hex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JPanel;

import hex.bots.BeppeBot;
import hex.bots.CrazyBot;
import hex.bots.GustafBot;
import hex.bots.GustafBot2;
import hex.bots.SimpleBot;
import hex.bots.Snake;
import hex.bots.JuanBot.JuanBot;

public class Panel extends JPanel implements Runnable, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Thread thread;
	private HexaMap H;
	private Player[] player;
	private final int MAX_TURN = 500;
	private int turn, mapsize;

	private final int width = 1280;
	private final int height = 720;
	private Random rand;
	private double[] weights;
	private int gurraturn;
	private boolean write;
	
	public Panel() throws IOException {
		super();
		setPreferredSize(new Dimension(width,height));
		setFocusable(true);

        requestFocus();
        


<<<<<<< HEAD
        mapsize = 6;
=======
        mapsize = 4;
>>>>>>> branch 'master' of https://github.com/professionaljesus/hexagon.git
        write = false;



    	player = new Player[4];
    	rand = new Random();
<<<<<<< HEAD


    	player[0] = new SimpleBot(1,mapsize, Color.CYAN, "WILDCARD");
		player[1] = new BeppeBot(2,mapsize, Color.GREEN, "BEPPNATION");
		player[2] = new JuanBot(3,mapsize, Color.RED, "JUAN");
		player[3] = new GustafBot2(4,mapsize, Color.YELLOW, "GURRA");
		H = new HexaMap(mapsize,width,height,player);
		

		//crazyTest();
=======
    	initGame();
>>>>>>> branch 'master' of https://github.com/professionaljesus/hexagon.git
	}


	
	private void initGame() {
		double safe = 0.00001;
		weights = new double[] {rand.nextDouble() + safe, rand.nextDouble() + safe, -(rand.nextDouble() + safe), rand.nextDouble() + safe,
				-(rand.nextDouble() + safe)};
		
		player[0] = new BeppeBot(1,mapsize, Color.GREEN, "BEPPNATION");
		player[1] = new SimpleBot(2,mapsize, Color.CYAN, "WILDCARD");
		player[2] = new CrazyBot(3,mapsize, Color.RED, "GURRA", weights);

		H = new HexaMap(mapsize,width,height,player);
		turn = 0;
		gurraturn = 0;
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
            
       //    gamerun();
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
	
	
	private void gamerun() {
		if(!end() && turn < MAX_TURN) {
			long t;
			HashSet<Hexagon> send = new HashSet<Hexagon>();

			for(Player p: player) {
				send.clear();
				for(Hexagon a: H.getPhex().get(p.getId() - 1)) {
					send.add(a.clone());
				}
				t = System.currentTimeMillis();
				H.move(p.algo(send));
				//System.out.println("Player " + p.getId() + " " + (System.currentTimeMillis() - t) + " ms");
			}
			H.endTurn();
			//System.out.println("Turn: " + turn);
			turn++;
			if(H.getPhex().get(2).size() > 0)
				gurraturn++;
		}else {
			int winrar = 0;
			for(int i = 0; i < H.getPhex().size(); i++) {
				if(H.getPhex().get(i).size() > 0) {
					winrar = i;
				}
			}
			System.out.println("Winner: " + player[winrar].getName());
			if(write) {

				 BufferedWriter writer;
				 String s = String.valueOf(gurraturn);
				 for(double w : weights)
					 s += "," + w;
				 
				try {
				writer = new BufferedWriter(new FileWriter("boi.csv",true));	
				 writer.write(s);
				 writer.newLine();
				 writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			initGame();
		   
		}
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
