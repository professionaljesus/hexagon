package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;


public class Q {
	
	private float[][][][] matrix;
	private boolean[] x,y,z;
	private final int dx = 200,dy = 400,dz = 200, actions = 4;
	public Q() {
		matrix = new float[dx][dy][dz][actions];
	}
	
	public float[][][][] getMap(){
		return matrix;
	}
	
	/*
	public Q(String filename, int id) {

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
		
		matrix = new HashMap<State,float[]>();

		
		for(int i = 0; i < read.size(); i += 2) {
			float[] ins = new float[read.get(i).length];
			float[] act = new float[read.get(i + 1).length];

			for(int j = 0; j < ins.length; j++)
				ins[j] = Float.parseFloat(read.get(i)[j]);
			
			for(int j = 0; j < act.length; j++)
				act[j] = Float.parseFloat(read.get(i + 1)[j]);
			
			matrix.put(new State(ins,id), act);
				
		}
			
	}
	

	
	public void save(String filename) {
		BufferedWriter writer;
		String write;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
			for(Entry<State, float[]> e: matrix.entrySet()) {
				
				write = e.getKey().toString();
				writer.write(write);
				writer.newLine();
				writer.flush();
				write = "";
				for(float d : e.getValue())
					write += d + ",";
				
				write = write.substring(0,write.length() - 1);
				writer.write(write);
				writer.newLine();
				writer.flush();
			}
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	@Override
	public String toString() {
		String ret = "------------------------";
		for(Entry<State, float[]> e: matrix.entrySet()) {
			ret += "("+ e.getKey().toString() + ")"+ "|" + Arrays.toString(e.getValue()) + "\n";
		}
		ret += "------------------------";
		return ret;
	}
	*/
}
