package com;

import java.util.ArrayList;
import java.util.Random;

public class NeatBoi implements Comparable<NeatBoi>{
	private double[] weights;
	private Random rand;
	private double fitness;
	public static int innovation = 0;
	private Node[] input;
	public ArrayList<Connection> conns;
	private Node output;
	private ArrayList<Node> hidden;
	
	public NeatBoi(double[] w) {
		weights = w;
	}
	
	public NeatBoi(int b){
		input = new Node[b];
		conns = new ArrayList<Connection>();
		hidden = new ArrayList<Node>();
		output = new Node();

		rand = new Random();
		weights = new double[b];
		fitness = 0;
		for(int i = 0; i < b; i++) {
			input[i] = new Node();
			conns.add(new Connection(input[i],output,i));
		}
		innovation = b;
		
		for(Connection c : conns)
			c.out.addIn(c.in);
		
		
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
				if(!mc.get(i).enabled) {
					newconn.add(mc.get(i));
					break;
				}
					
				if(!dc.get(i).enabled) {
					newconn.add(dc.get(i));
					break;
				}
				
				
				
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
					break;
				}

			}
		
			
		}
		System.out.println(mc);
		System.out.println(dc);
		System.out.println(newconn.size());
		System.out.println(newconn);
		this.conns = newconn;
		

	}
	
	private void connectionMutation() {
		this.conns.add(null);
	}
	
	private void nodeMutation() {
		int which = rand.nextInt(conns.size());
		Connection c1, c2;
		Node n = new Node();
		
	}
	
	
	public void setFitness(double f) {
		fitness = f;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public double evaluateNetwork(double[] inputs) {
		for(int i = 0; i < inputs.length; i++)
			this.input[i].value = inputs[i];
			
		return eval(output);
	}
	
	private double eval(Node n) {
		if(n.in.size() == 0)
			return n.value;
		ArrayList<Node> list = n.in;
		double val = 0;
		for(int i = 0; i < list.size(); i++)
			 val += eval(n.in.get(i))*n.we.get(i);
		return val;
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
		ArrayList<Node> in;
		ArrayList<Double> we;
		
		public Node() {
			value = 0;
			in = new ArrayList<Node>();
			we = new ArrayList<Double>();

		}
		
		public double evalNode() {
			if(in.size() == 0)
				return value;
			value = 0;
			for(int i = 0; i < we.size(); i++)
				value += we.get(i) * in.get(i).evalNode();
			
			return value;
		}
		
		
		public void addIn(Node n, double w) {
			in.add(n);
			we.add(w);
		}


		@Override
		public Node clone() {
			Node n = new Node();
			return n;
		}
		
		
		private double sigmoid(double input) {
			return 1/(1 + Math.exp(-input));
			
		}

	}

	
	class Connection{
		private int innov;
		private Node in, out;
		private double weight;
		private boolean enabled;
		
		public Connection(Node in, Node out) {
			this.in = in;
			this.out = out;
			this.weight = rand.nextDouble() - 0.5;
			this.out.addIn(in,weight);
			this.enabled = true;
			this.innov = innovation;
			innovation++;

			}
		
		public Connection(Node in, Node out, int innov) {
			this.in = in;
			this.out = out;
			this.weight = rand.nextDouble() - 0.5;
			this.out.addIn(in,weight);
			this.enabled = true;
			this.innov = innov;
			
		}
		
		@Override
		public Connection clone() {
			Connection c = new Connection(this.in.clone(), this.out.clone(), this.innov);
			c.weight = this.weight;
			c.enabled = this.enabled;
			
			return c;
		}
		
		@Override
		public String toString() {
			return "" + innov;
		}
		

		
	}
}
