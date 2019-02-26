package com;

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
import java.util.Map.Entry;
import java.util.Random;

public class TrainGen {
	private ArrayList<ArrayList<Boi>> spec;
	private final int inputs = 3, population = 70;
	private int species;
	private Random rand;
	private final double diff = 1.5;
	
	public TrainGen() {
		rand = new Random();
		species = (int) Math.round(Math.pow(2.0, (inputs - 1)));
		spec = new ArrayList<ArrayList<Boi>>();
		for(int i = 0; i < species; i++) {
			spec.add(new ArrayList<Boi>());
			for(int j = 0; j < population/species; j++)
				spec.get(i).add(new Boi(correctWeights(i)));
		}
		
		for(int i = 0; i  < population - (population/species) * species; i++)
			placeInSpecies(new Boi(inputs));
		
	}
	
	public TrainGen(String filename) {
		rand = new Random();
		species = (int) Math.round(Math.pow(2.0, (inputs - 1)));
		spec = new ArrayList<ArrayList<Boi>>();

		for(int i = 0; i < species; i++)
			spec.add(new ArrayList<Boi>());
		
		ArrayList<String[]> read = new ArrayList<String[]>();

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));

			String line = null;
			while ((line = reader.readLine()) != null) {
				read.add(line.split(","));
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
 		for(int i = 0; i < read.size(); i++) {
 			double[] w = new double[inputs];
			for(int j = 0; j < inputs; j++)
				w[j] = Double.parseDouble(read.get(i)[j]);
			
			Boi b = new Boi(w);
			b.setFitness(Double.parseDouble(read.get(i)[inputs]));
			placeInSpecies(new Boi(w));
		}

	}
	
	public void saveGeneration(String filename) {
		BufferedWriter writer;
		String write = "";
		try {
			writer = new BufferedWriter(new FileWriter(filename));
			for(ArrayList<Boi> yas : spec) {
				for(Boi b : yas) {
						for(double d:b.getWeights())
							write += d + ",";

						write += b.getFitness();
						writer.write(write);
						writer.flush();
						writer.newLine();
						write = "";
				}
			}
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	

	
	public void runSimulations() {
		TrainingCenter t = null;
		Boi[] fighters = new Boi[2];
		for(ArrayList<Boi> yas: spec) {
			Collections.shuffle(yas);
			for(int i = 0; i < yas.size(); i++) {
				System.out.println(i);

				for(int j = i + 1; j < yas.size(); j++) {
					fighters[0] = yas.get(i);
					fighters[1] = yas.get(j);
					if(t == null)
						t = new TrainingCenter(fighters);
					else
						t.initGame(fighters);
					
					while(!t.end() && t.turn < 100)
						t.gamerun();
					
					fighters[t.getWinner()].setFitness(fighters[t.getWinner()].getFitness() + 100.0/((double)t.turn));
				
				}
			}
		}
	}
	
	public ArrayList<Boi> runGlobalSimulation(int tournament) {
		System.out.println("COMMENCING GLOBAL TOURNAMENT");
		ArrayList<Boi> topfighters = new ArrayList<Boi>();
		
		for(ArrayList<Boi> yas: spec) {
			Collections.sort(yas);
			for(int i = 0; i < yas.size() && i < 5; i++) {	
				yas.get(i).setFitness(0);
				topfighters.add(yas.get(i));
			}
		}
		
		
		TrainingCenter t = null;
		Boi[] fighters = new Boi[2];
		
		for(int tour = 0; tour < tournament; tour++) {
			Collections.shuffle(topfighters);

			for(int i = 0; i < topfighters.size(); i++) {
				System.out.println(i);

				for(int j = i + 1; j < topfighters.size(); j++) {
					fighters[0] = topfighters.get(i);
					fighters[1] = topfighters.get(j);
					if(t == null)
						t = new TrainingCenter(fighters);
					else
						t.initGame(fighters);
					
					
					while(!t.end() && t.turn < 200)
						t.gamerun();
					
					fighters[t.getWinner()].setFitness(fighters[t.getWinner()].getFitness() + 100.0/((double)t.turn));
				}
			}
		}
		
		Collections.sort(topfighters);
		
		return topfighters;
		
		
	}
	
	
	private static int highest(int a) {
		int highest = 10;
		while(true) {
			if(a % highest == a)
				return highest/10;
			else
				highest*=10;
		}
			
	}
	

	
	public void breedTop() {
		double[][] my = new double[species][inputs];
		double[][] sigma = new double[species][inputs];
		int currentpop = 0;
		
		for(int s = 0; s < species; s++) {
			Collections.sort(spec.get(s));
			
			int pop = spec.get(s).size();
	
			double[] mean = new double[inputs];
			double[] sd = new double[inputs];
			double[][] all = new double[pop/5 + 1][inputs];
			double max = 0;
			double divider = 1;
			double[] w = new double[inputs];
			
			for(int i = 0; i < all.length; i++) {
				all[i] = spec.get(s).get(i).getWeights();
				for(int j = 0; j < inputs; j++) {
					if(Math.abs(all[i][j]) > max)
						max = Math.abs(all[i][j]);
				}
				if(max > 10) {
					System.out.println("Max: " + max);
					System.out.println(Arrays.toString(all[i]));
					divider = highest((int)max);
					
					for(int j = 0; j < inputs; j++) {
						w[j] = all[i][j]/divider;
						mean[j] += w[j];
					}
					spec.get(s).get(i).setWeights(w);
					System.out.println(Arrays.toString(w));

					max = 0;
				}else {
					for(int j = 0; j < inputs; j++)
						mean[j] += all[i][j];
				}
			}
			
			for(int j = 0; j < inputs; j++) {
				mean[j] = mean[j]/((double)mean.length);
				for(int i = 0; i < all.length; i++) {
					sd[j] += Math.pow(all[i][j] - mean[j], 2);
				}
				sd[j] = Math.sqrt(sd[j]/((double)all.length));
			}
			
			ArrayList<Boi> topbois = new ArrayList<Boi>();
			System.out.println(s + " - " + spec.get(s));

			for(int i = 0; i < pop && i < 2; i++) {
				spec.get(s).get(i).setFitness(0);
				topbois.add(spec.get(s).get(i));
				System.out.println(Arrays.toString(spec.get(s).get(i).getWeights()));
				currentpop++;
			}
			
			spec.get(s).clear();
			
			spec.get(s).addAll(topbois);
						
			my[s] = mean;
			sigma[s] = sd;
			

		}
		
		int part = population/species;
		for(int s = 0; s < species; s++) {
			System.out.println("Current Population: " + currentpop);
			System.out.println(s + " -SD- " +Arrays.toString(sigma[s]));

			for(int i = currentpop; i < currentpop + part/2; i++) {
				placeInSpecies(new Boi(inputs,my[s],sigma[s]));
			}
			currentpop += part/2;

		}
		System.out.println("Current Population: " + currentpop);

		for(int i = currentpop; i < population; i++) {
			placeInSpecies(new Boi(inputs));
			currentpop++;
		}
		System.out.println("Current Population: " + currentpop);

		
	}
	
	private void placeInSpecies(Boi b) {
		double[] w = b.getWeights();
		double index = 0;
		for(int i = 0; i < w.length - 1; i++)
			index += (w[i] >= w[i + 1] ? 1.0:0.0) * Math.pow(2,i);
		
		this.spec.get((int) index).add(b);
		
	}
	
	public double[] correctWeights(int index) {
		String bin = Integer.toBinaryString(index);
		while(bin.length() < inputs - 1)
			bin = "0" + bin;
		double[] w = new double[inputs];
		w[0] = rand.nextDouble();
		for(int i = bin.length() - 1; i >= 0; i--) {
			if(bin.charAt(i) == '1')
				w[bin.length() - i] = rand.nextDouble()*w[bin.length() - 1 - i]; 
			else
				w[bin.length() - i] = w[bin.length() - 1 - i] + rand.nextDouble()*(1 - w[bin.length() - 1 - i]);
		}	
		return w;
	}
	
	
	public void breeding() {
		ArrayList<Boi> nextgen = new ArrayList<Boi>();
		int j = 0;
		for(int s = 0; s < species; s++) {
			for(int i = 0; i < spec.get(s).size() && i < 3; i++) {
				j = i + 1;
				while(j < i + 3 && spec.get(s).size() > j && diff > spec.get(s).get(i).getFitness() - spec.get(s).get(j).getFitness()) {
					nextgen.add(new Boi(spec.get(s).get(i),spec.get(s).get(j),0));
					nextgen.add(new Boi(spec.get(s).get(i),spec.get(s).get(j),1));
					nextgen.add(new Boi(spec.get(s).get(i),spec.get(s).get(j),2));
					j++;
				}
				
				spec.get(s).get(i).setFitness(0);
				nextgen.add(spec.get(s).get(i));

			}
			
			for(int i = 0; i < population/species - spec.get(s).size(); i++)
				nextgen.add(new Boi(correctWeights(s)));
			
			spec.get(s).clear();
		}
		while(nextgen.size() < population)
			nextgen.add(new Boi(inputs));
		
		System.out.println("Nextgen size:" + nextgen.size());
		
		for(Boi b : nextgen)
			placeInSpecies(b);
		
	}
	
	
	public HashMap<Boi, int[]> vsKompisLaget(ArrayList<Boi> top, int tournament) {
		System.out.println("Mot Kompisarna");
		
		VsKompisarna t = null;
		
		HashMap<Boi, int[]> m = new HashMap<Boi,int[]>();
		
		for(Boi b : top) {
			
			if(t == null)
				t = new VsKompisarna(b);

			int[] winner = new int[3];
			
			for(int i = 0; i < tournament; i++) {
				System.out.println(i);
				t.initGame(b);
								
				while(!t.end()) 
					t.gamerun();
				
								
				winner[t.getWinner()]++;
			}
			
			m.put(b, winner);
			
		}
		
		return m;
		
	}
	
	
	
	
    public static void main(String arg0[]){
    	
    	TrainGen t = new TrainGen();

    	t.runSimulations();

    	for(int i = 0; i < 7; i++) {
			t.breeding();

    		t.runSimulations();
    		
    		t.saveGeneration("data.csv");
    		System.out.println("Generation: "+i);
    	}
    	
    	//TrainGen t = new TrainGen("data.csv");
    	
    	ArrayList<Boi> top = t.runGlobalSimulation(2);
    	
    	HashMap<Boi,int[]> m = t.vsKompisLaget(top, 10);
    	
    	for(Entry<Boi,int[]> e: m.entrySet()) {
    		System.out.println(Arrays.toString(e.getValue()) + " " + Arrays.toString(e.getKey().getWeights()));
    	}
    	
    	
    }
}
