package hex.bots.BepsiMax;

import com.Boi;
import hex.Panel;
import hex.Player;
import hex.bots.CrazyBot;
import hex.bots.SimpleBot;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class RunTrainingSimulations {

    public static void main(String[] args) throws IOException {
        double chromosomes[][] = createRandomGeneration();
        int mapsize = 4;

        JFrame frame = new JFrame("FrameDemo");
        Player player[] = new Player[3];
        player[0] = new Bepster(1,mapsize, Color.GREEN, "BEPPNATION",chromosomes[0]);
        player[1] = new Bepster(2,mapsize, Color.RED, "GURRA",chromosomes[1]);
        player[2] = new Bepster(3,mapsize, Color.CYAN, "WILDCARD",chromosomes[2]);
        Panel tc = new Panel(player,mapsize);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Panel(player,mapsize));
        frame.pack();
        frame.setVisible(true);

        //TrainingCenter tc = new TrainingCenter(chromosomes);
//        tc.initGame();
//        while(!tc.end() && tc.turn < 100){
//            tc.gamerun();
//        }
//        System.out.println("The winner is: "+ tc.winner);
    }

    public static double[][] createRandomGeneration(){
        Random rand = new Random();
        double[][] chromosomes = new double[50][11];
        for(int i=0; i<50; i++){
            for(int j = 0; j<11; j++){
                chromosomes[i][j] = rand.nextDouble()*2-1;
            }
        }
        return chromosomes;
    }

    public static void readGeneration(String filename){
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

    }

//    public static void saveGeneration(String filename) {
//        BufferedWriter writer;
//        String write = "";
//        try {
//            writer = new BufferedWriter(new FileWriter(filename));
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
//            writer.close();
//        } catch (IOException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//    }
}
