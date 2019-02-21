package com;

import com.evo.NEAT.Environment;
import com.evo.NEAT.Genome;
import com.evo.NEAT.Pool;

import hex.Panel;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by vishnughosh on 05/03/17.
 */
public class XOR implements Environment {
    @Override
    public void evaluateFitness(ArrayList<Genome> population) {
    	Panel p = null;

        for (int i = 0; i < population.size(); i += 3) {
        	System.out.println(i);
        	
            Genome[] gens = new Genome[] {population.get(i), population.get(i + 1), population.get(i + 2)};
            try {
				p = new Panel(gens);
	            while(!p.end()) {
	            	
	            }
	            int[] winners = p.getWinners();
	            
	            int win = 0;
	            for(int j = 0; j < winners.length; j++) {
	            	if(winners[j] == 0)
	            		population.get(i + j).setFitness(10);
	            	else
	            		population.get(i + j).setFitness(0);
	            }
	            	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


        }

    }

    public static void main(String arg0[]){
        XOR xor = new XOR();

        Pool pool = new Pool();
        pool.initializePool();

        Genome topGenome = new Genome();
        int generation = 0;
        while(true){
            //pool.evaluateFitness();
            pool.evaluateFitness(xor);
            topGenome = pool.getTopGenome();
            System.out.println("TopFitness : " + topGenome.getPoints());

            if(topGenome.getPoints()>15){
                break;
            }
//            System.out.println("Population : " + pool.getCurrentPopulation() );
            System.out.println("Generation : " + generation );
            //           System.out.println("Total number of matches played : "+TicTacToe.matches);
            //           pool.calculateGenomeAdjustedFitness();

            pool.breedNewGeneration();
            generation++;

        }
        System.out.println(topGenome.evaluateNetwork(new float[]{1,0})[0]);
    }
}
