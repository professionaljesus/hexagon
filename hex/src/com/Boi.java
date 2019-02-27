package com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Boi implements Comparable<Boi>{
	private double[] weights;
	private Random rand;
	private double fitness;

	
	public Boi(double[] w) {
		weights = w;
		fitness = 0;
	}
	

	public Boi(int b){
		rand = new Random();
		weights = new double[b];
		fitness = 0;
		for(int i = 0; i < b; i++)
			weights[i] = rand.nextDouble();
		
		
	}
	
	public Boi(Boi mom, Boi dad, int childnbr) {
		double totfit = mom.fitness + dad.fitness;
		rand = new Random();
		weights = new double[mom.weights.length];
		fitness = 0;
		for(int i = 0; i < weights.length; i++) {
			switch (childnbr){
				case 0:
					weights[i] = 0.5*mom.weights[i] + 0.5*dad.weights[i]; 
					break;
				case 1:
					weights[i] = 1.5*mom.weights[i] - 0.5*dad.weights[i]; 
					break;
				case 2:
					weights[i] = -0.5*mom.weights[i] + 1.5*dad.weights[i]; 
					break;
				default:
					weights[i] = rand.nextDouble()*totfit < mom.fitness ? mom.weights[i]:dad.weights[i];
					break;
			
			}
		}
			 
			
		if(rand.nextDouble() < 0.1)
			weights[rand.nextInt(weights.length)] += rand.nextBoolean()?-0.2:0.2;
			
	}
	
	public Boi(int b, double[] my, double[] sigma) {
		rand = new Random();
		weights = new double[b];
		fitness = 0;
		
		for(int i = 0; i < b; i++)
			weights[i] = my[i] + rand.nextGaussian()*sigma[i];
	}
	
	
	public void setFitness(double f) {
		fitness = f;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public double evaluateNetwork(double[] inputs) {
		double j = 0;
		for(int i = 0; i < inputs.length; i++)
			j += weights[i]*inputs[i];
		
		return j;
	}
	
	public void setWeights(double[] w) {
		weights = w;
	}
	
	public double[] getWeights() {
		return weights;
	}
	
	@Override
	public boolean equals(Object o) {
		Boi b = (Boi) o;
		return Arrays.equals(this.weights, b.weights);
	}
	
	@Override
	public String toString() {
		String ret = this.fitness + "";
		//for(int i = 0; i < weights.length; i++)
		//	ret += "," + weights[i];
		
		return ret;
	}

	@Override
	public int compareTo(Boi b) {
		if(this.fitness - b.getFitness() == 0)
			return 0;
		else if(this.fitness - b.getFitness() > 0)
			return -1;
		else
			return 1;
	}
	

}
