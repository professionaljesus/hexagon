package hex.bots.BepsiMax;

import hex.Player;
import hex.Panel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class RunTrainingSimulations {

    public static int nrChromosomes = 16;
    public static int nrWeights = 10;

    public static void main(String[] args) throws IOException {
        //double chromosomes[][] = createRandomGeneration();
        double chromosomes[][] = readGeneration("BeppesHemligaData.csv");
        int mapsize = 4;

        JFrame frame = new JFrame("FrameDemo");
        Player player[] = new Player[3];
        player[0] = new Bepster(1,mapsize, Color.GREEN, "BEPPNATION",chromosomes[0]);
        player[1] = new Bepster(2,mapsize, Color.RED, "GURRA",chromosomes[1]);
        player[2] = new Bepster(3,mapsize, Color.CYAN, "WILDCARD",chromosomes[2]);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Panel(player,mapsize));
        frame.pack();
        frame.setVisible(true);

//        TrainingCenter tc = new TrainingCenter(chromosomes,10);
//        saveGeneration("BeppesHemligaData.csv", chromosomes);
    }

    public static double[][] createRandomGeneration(){
        Random rand = new Random();
        double[][] chromosomes = new double[nrChromosomes][nrWeights+1];
        for(int i=0; i<nrChromosomes; i++){
            chromosomes[i][0] = 1000;
            for(int j = 1; j<nrWeights+1; j++){
                chromosomes[i][j] = 1-rand.nextDouble()*2;
            }
        }
        return chromosomes;
    }

    public static double[][] readGeneration(String filename){
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

        double[][] chromosomes = new double[read.size()][read.get(0).length];

        for (int i = 0; i<read.size();i++){
            for (int j = 0; j<read.get(0).length; j++){
                chromosomes[i][j] = Double.parseDouble(read.get(i)[j]);
            }
        }
        return chromosomes;
    }

    public static void saveGeneration(String filename, double[][] chromosomes) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            for(double[] c: chromosomes){
                String line = "";
                for(double d: c){
                    line += d + ",";
                }
                writer.write(line);
                writer.flush();
                writer.newLine();
            }


//            for(ArrayList<Boi> yas : spec) {
//                for(Boi b : yas) {
//                    for(double d:b.getWeights())
//                        write += d + ",";
//
//                    write += b.getFitness();
//                    writer.write(write);
//                    writer.flush();
//                    writer.newLine();
//                    write = "";
//                }
//            }
            writer.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
