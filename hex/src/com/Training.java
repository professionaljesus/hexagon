package com;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;

import javax.swing.JPanel;

import com.evo.NEAT.Genome;

import hex.HexaMap;
import hex.Hexagon;
import hex.Player;
import hex.bots.CrazyBot;


public class Training{

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
	private double[] weights;
	private boolean training = false;
	private int[] winners;
	
	

	public Training(Genome[] gen) {
		
        mapsize = 4;        
    	player = new Player[3];
        winners = new int[player.length];

    	player[0] = new CrazyBot(1,mapsize, Color.GREEN, "Crazy1", gen[0]);
		player[1] = new CrazyBot(2,mapsize, Color.RED, "Crazy2", gen[1]);
		player[2] = new CrazyBot(3,mapsize, Color.CYAN, "Crazy3", gen[2]);

		H = new HexaMap(mapsize,width,height,player);
		turn = 0;

	}


	public int[] getWinners() {
		return winners;
	}
	
	/**
	 * Om det bara finns en spelare kvar p� mappen
	 * @return True om bara en kvar, false annars
	 */
	public boolean end() {
		int boi = 0;
		ArrayList<HashSet<Hexagon>> phex = HexaMap.getPhex();
		for(int i = 0; i < phex.size(); i++) {
			if(phex.get(i).isEmpty()) {
				boi++;
				if(winners[i] == 0)
					winners[i] = turn;
			}
		}

		
		return boi + 1 >= player.length;
	}


	
	public void gamerun() {
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
			System.out.println("Turn: " + turn);
			turn++;
		}
	}
	
	
	//System.out.println("Player: " + p.getId() + " X Move :" + p.algo(H.getPhex().get(p.getId() - 1))[2] + " Y Move :" + p.algo(H.getPhex().get(p.getId() - 1))[3]+ " XTarget Move :" + p.algo(H.getPhex().get(p.getId() - 1))[4] + " YTarget Move :" + p.algo(H.getPhex().get(p.getId() - 1))[5]+" Res " + p.algo(H.getPhex().get(p.getId() - 1))[1]);
	

}
