package com;

import java.util.Random;

public class Boi implements Comparable<Boi>{
	private double[] weights;
	private Random rand;
	private double fitness;
	
	public Boi(double[] w) {
		weights = w;
	}
	
	public Boi(int b){
		rand = new Random();
		weights = new double[b];
		fitness = 0;
		for(int i = 0; i < b; i++)
			weights[i] = rand.nextDouble();
		
		
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
