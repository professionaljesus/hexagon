package com;

import java.util.ArrayList;
import java.util.Random;

public class NeatBoi implements Comparable<NeatBoi>{
	private double[] weights;
	private Random rand;
	private double fitness;
	public static int innovation = 0;
	private Node[] nodes;
	public ArrayList<Connection> conns;
	private static final int size = 200;
	
	public NeatBoi(double[] w) {
		weights = w;
	}
	
	public NeatBoi(int b){
		nodes = new Node[size];
		conns = new ArrayList<Connection>();

		rand = new Random();
		weights = new double[b];
		fitness = 0;
		for(int i = 0; i < b; i++) {
			nodes[i] = new Node();
			conns.add(new Connection(i,size - 1,i));
		}
		nodes[size - 1] = new Node();
		
		
	}

	
	public NeatBoi(NeatBoi mom, NeatBoi dad) {
		int best = mom.compareTo(dad);
		ArrayList<Connection> dc = dad.conns, mc = mom.conns, newconn = new ArrayList<Connection>();
		int len = Math.max(dc.get(dc.size() - 1).innov, mc.get(mc.size() - 1).innov);
		
		for(int i = 0; i <= len; i++) {

			
			//Om mommas i allel != null eller och hon har inga mer eller innovationsnummret är för lågt
			if(mc.size() <= i || (mc.get(i) != null && mc.get(i).innov > i)) {
				mom.conns.add(i, null);
				if(dc.size() <= i || dc.get(i) == null)
					newconn.add(null);
				else
					newconn.add(dc.get(i));
			}
				
			if(dc.size() <= i || (dc.get(i) != null && dc.get(i).innov > i)) {
				dc.add(i, null);
				if(mc.size() <= i || mc.get(i) == null)
					newconn.add(null);
				else
					newconn.add(mc.get(i));
			}
			
			if(mc.get(i) != null && dc.get(i) != null) {
				switch(best){
				case -1:
					newconn.add(mc.get(i));
					break;
				case 0:
					if(rand.nextBoolean())
						newconn.add(mc.get(i));
					else
						newconn.add(dc.get(i));
					break;
				case 1:
					newconn.add(dc.get(i));
				}

			}
		
			
		}
		System.out.println(mc);
		System.out.println(dc);
		System.out.println(newconn.size());
		System.out.println(newconn);
		this.conns = newconn;
		this.nodes = mom.nodes;
		

	}
	
	
	public void setFitness(double f) {
		fitness = f;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public double evaluateNetwork(double[] inputs) {
		for(int i = 0; i < inputs.length; i++)
			nodes[i].value = inputs[i];
		
		for(Connection c : conns) {
			if(c != null && c.enabled)
				nodes[c.out].addValue(nodes[c.in].value*c.weight);
		}
		return nodes[size - 1].value;
	}
	
	public void setWeights(double[] w) {
		weights = w;
	}
	
	public ArrayList<Connection> getWeights() {
		return conns;
	}
	
	@Override
	public String toString() {
		String ret = this.fitness + "";
		//for(int i = 0; i < weights.length; i++)
		//	ret += "," + weights[i];
		
		return ret;
	}

	@Override
	public int compareTo(NeatBoi b) {
		if(this.fitness - b.getFitness() == 0)
			return 0;
		else if(this.fitness - b.getFitness() > 0)
			return -1;
		else
			return 1;
	}
	
	class Node{
		double value;
		ArrayList<Connection> conns;
		int outs;
		
		public Node() {
			value = 0;
			conns = new ArrayList<Connection>();
			outs = 0;
		}
		
		public void addValue(double v) {
			value += v;
		}
		
		public void incOuts() {
			outs++;
		}
		
		private double sigmoid(double input) {
			return 1/(1 + Math.exp(-input));
			
		}

	}
	
	class Connection{
		private int in, out, innov;
		private double weight;
		private boolean enabled;
		
		public Connection(int in, int out) {
			this.in = in;
			this.out = out;
			this.weight = rand.nextDouble() - 0.5;
			this.enabled = true;
			this.innov = innovation;
			innovation++;

			}
		
		public Connection(int in, int out, int innov) {
			this.in = in;
			this.out = out;
			this.weight = rand.nextDouble() - 0.5;
			this.enabled = true;
			this.innov = innov;
			innovation = innov + 1;
			
		}
		
		@Override
		public String toString() {
			return "" + innov;
		}
		

		
	}
}
