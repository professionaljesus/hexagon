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

public class TrainGen {
	private ArrayList<ArrayList<Boi>> spec;
	private final int inputs = 3, population = 40;
	private int species;
	
	public TrainGen() {
		species = (int) Math.round(Math.pow(2.0, (inputs - 1)));
		spec = new ArrayList<ArrayList<Boi>>();
		for(int i = 0; i < species; i++)
			spec.add(new ArrayList<Boi>());
		
		for(int i = 0; i < population; i++) {
			placeInSpecies(new Boi(inputs));
		}
	}
	
	public TrainGen(String filename) {
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
		
		double[] w = new double[inputs];
 		for(int i = 0; i < read.size(); i++) {
			for(int j = 0; j < read.get(i).length; j++)
				w[j] = Double.parseDouble(read.get(i)[j]);
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
							write += "," + d;
						writer.write(write.substring(1));
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
	
	private void placeInSpecies(Boi b) {
		double[] w = b.getWeights();
		double index = 0;
		for(int i = 0; i < w.length - 1; i++)
			index += (w[i] >= w[i + 1] ? 1.0:0.0) * Math.pow(2,i);
		
		spec.get((int) index).add(b);
		
	}
	
	public void runSimulations() {
		TrainingCenter t;
		Boi[] fighters = new Boi[2];
		for(ArrayList<Boi> yas: spec) {
			Collections.shuffle(yas);
			for(int i = 0; i < yas.size(); i++) {
				System.out.println(i);

				for(int j = i + 1; j < yas.size(); j++) {
					fighters[0] = yas.get(i);
					fighters[1] = yas.get(j);
					t = new TrainingCenter(fighters);
					
					while(!t.end() && t.turn < 200)
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
		
		TrainingCenter t;
		Boi[] fighters = new Boi[2];
		
		for(int tour = 0; tour < tournament; tour++) {
			Collections.shuffle(topfighters);

			for(int i = 1; i < topfighters.size(); i++) {
				System.out.println(i);

				for(int j = i + 1; j < topfighters.size(); j++) {
					fighters[0] = topfighters.get(i);
					fighters[1] = topfighters.get(j);
					t = new TrainingCenter(fighters);
					
					while(!t.end() && t.turn < 200)
						t.gamerun();
					
					fighters[t.getWinner()].setFitness(fighters[t.getWinner()].getFitness() + 1/((double)t.turn));
				}
			}
		}
		
		Collections.sort(topfighters);
		
		return topfighters;
		
		
	}
	
	public void interSpeciesBreeding() {
		
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

		
		for(int s = 0; s < species; s++) {
			Collections.sort(spec.get(s));
			
			int pop = spec.get(s).size();
	
			double[] mean = new double[inputs];
			double[] sd = new double[inputs];
			double[][] all = new double[pop/5][inputs];
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
					divider = highest((int)max);
					
					for(int j = 0; j < inputs; j++) {
						w[j] = all[i][j]/divider;
						mean[j] += w[j];
					}
					spec.get(s).get(i).setWeights(w);
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
			System.out.println(spec.get(s));

			for(int i = 0; i < spec.get(s).size() && i < 3; i++) {
				spec.get(s).get(i).setFitness(0);
				topbois.add(spec.get(s).get(i));
				System.out.println(Arrays.toString(spec.get(s).get(i).getWeights()));
			}
			
			spec.get(s).clear();
			
			spec.get(s).addAll(topbois);
						
			my[s] = mean;
			sigma[s] = sd;
			
			

		}
				
		for(int s = 0; s < species; s++) {
			int part = population/spec.size();
			for(int i = spec.get(s).size(); i < part; i++) {
				if(i < 2*(part/3))
					placeInSpecies(new Boi(inputs,my[s],sigma[s]));
				else
					placeInSpecies(new Boi(inputs));
			}
		}

		
	}
	
	
	
    public static void main(String arg0[]){
    	
    	TrainGen t = new TrainGen("data.csv");
    	/*for(int i = 0; i < 10; i++) {
    		t.runSimulations();
    		if(i < 9)
    			t.breedTop();
    		
    		t.saveGeneration("data.csv");
    	}*/
    	
    	ArrayList<Boi> top = t.runGlobalSimulation(2);
    	for(Boi b : top)
    		System.out.println(Arrays.toString(b.getWeights()));
    	
    	
    }
}
