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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;

import javax.swing.JPanel;


import hex.HexaMap;
import hex.Hexagon;
import hex.Player;
import hex.bots.CrazyBot;


public class TrainingCenter{

	/**
	 * 
	 */
	private Thread thread;
	private HexaMap H;
	private Player[] player;
	public int turn;
	private int mapsize;

	private final int width = 1280;
	private final int height = 720;
	public int winner;
	
	

	public TrainingCenter(Boi[] gen) {
		
        mapsize = 4;        
    	player = new Player[gen.length];

    	player[0] = new CrazyBot(1,mapsize, Color.GREEN, "Crazy1", gen[0]);
		player[1] = new CrazyBot(2,mapsize, Color.RED, "Crazy2", gen[1]);

		H = new HexaMap(mapsize,width,height,player);
		turn = 0;

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
	
	public int getWinner() {
		int max = 0;
		int index = 0;
		for(int i = 0; i < H.getPhex().size(); i++) {
			if(H.getPhex().get(i).size() > max) {
				max = H.getPhex().get(i).size();
				index = i;
			}
		}
		return index;
		
	}
	
	
	
	public void gamerun() {
		HashSet<Hexagon> send = new HashSet<Hexagon>();
		for(Player p: player) {
			send.clear();
			for(Hexagon a: H.getClonedPhex().get(p.getId() - 1)) {
				send.add(a);
			}
			H.move(p.algo(send));
		}
		H.endTurn();
		turn++;
	}
	
		

}
