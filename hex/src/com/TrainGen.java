package com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TrainGen {
	private ArrayList<Boi> bois;
	private int inputs, population;
	
	public TrainGen() {
		inputs = 3;
		population = 100;
		bois = new ArrayList<Boi>();
		for(int i = 0; i < population; i++)
			bois.add(new Boi(inputs));
	}
	
	public void runSimulations() {
		TrainingCenter t;
		Boi[] fighters = new Boi[3];
		for(int i = 2; i < bois.size(); i += 3) {
			System.out.println(i);
			fighters[0] = bois.get(i - 2);
			fighters[1] = bois.get(i - 1);
			fighters[2] = bois.get(i);
			t = new TrainingCenter(fighters);
			
			while(!t.end() && t.turn < 300)
				t.gamerun();
			
			fighters[t.getWinner()].setFitness(1/((double)t.turn));
		}
	}
	
	public void breedTop() {
		ArrayList<Boi> nextgen = new ArrayList<Boi>();
		Collections.sort(bois);
		
		nextgen.add(bois.get(0));
		nextgen.add(bois.get(1));
		nextgen.add(bois.get(2));
		
		double[] mean = new double[inputs];
		double[] sd = new double[inputs];
		double[][] all = new double[population/5][inputs];
		
		for(int i = 0; i < all.length; i++) {
			all[i] = bois.get(i).getWeights();
			for(int j = 0; j < inputs; j++)
				mean[j] += all[i][j];
		}
		
		for(int j = 0; j < inputs; j++) {
			mean[j] = mean[j]/((double)mean.length);
			for(int i = 0; i < all.length; i++) {
				sd[j] += Math.pow(all[i][j] - mean[j], 2);
			}
			sd[j] = Math.sqrt(sd[j]/((double)all.length));
		}
		
		for(int i = 3; i < population/2; i++)
			nextgen.add(new Boi(inputs,mean,sd));
		
		for(int i = population/2; i < population; i++)
			nextgen.add(new Boi(inputs));
		
		bois = nextgen;
		
	}
	
	
	
    public static void main(String arg0[]){
    	
    	TrainGen t = new TrainGen();
    	for(int i = 0; i < 100; i++) {
	    	t.runSimulations();
	    	t.breedTop();
	    	System.out.println(t.bois);
	    	System.out.println(Arrays.toString(t.bois.get(0).getWeights()));

    	}
    	
    }
}
