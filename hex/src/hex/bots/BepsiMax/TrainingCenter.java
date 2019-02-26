package hex.bots.BepsiMax;

import hex.HexaMap;
import hex.Hexagon;
import hex.Player;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class TrainingCenter {

    private Thread thread;
    private HexaMap H;
    private Player[] players;
    public int turn;
    private int mapsize;

    private final int width = 1280;
    private final int height = 720;
    public int winner;



    public TrainingCenter(double[][] chromosomes,int genSteps) {
       mapsize = 4;

       for(int k = 0; k<genSteps; k++){
           System.out.println("Working on generation " + k);
           for(int i = 0; i<chromosomes.length; i++){
               for(int j = i+1; j<chromosomes.length;j++){
                   initGame(chromosomes[i],chromosomes[j]);
               }
           }
           Arrays.sort(chromosomes, (a, b) -> (int) b[0] - (int) a[0]);
           breed(chromosomes);
       }


    }

    public void initGame() {
        H = new HexaMap(mapsize,width,height, players);
        turn = 0;
        while(!end() && turn < 100){
            gamerun();
        }
        //System.out.println("The winner is: "+ getWinner());
    }

    public void initGame(double[] chromosomeA,double[] chromosomeB) {
        players = new Player[2];
        players[0] = new Bepster(1,4,Color.RED,"test",chromosomeA);
        players[1] = new Bepster(2,4,Color.RED,"test",chromosomeB);

        H = new HexaMap(mapsize,width,height,players);
        turn = 0;
        while(!end() && turn < 100){
            gamerun();
        }
        //System.out.println("The winner is: "+ getWinner());
        //System.out.println("Before: " + (int) chromosomeA[0] + "---" + (int) chromosomeB[0]);
        if(getWinner()==1){
            double temp = EarnedRating(1,expectedScore(chromosomeA[0],chromosomeB[0]));
            chromosomeA[0] += temp;
            chromosomeB[0] -= temp;
        }else if(getWinner() == 2){
            double temp = EarnedRating(1,expectedScore(chromosomeB[0],chromosomeA[0]));
            chromosomeB[0] += temp;
            chromosomeA[0] -= temp;
        }
        if (getWinner() != 1 && getWinner()!= 2) {
            System.out.println("Somethings fishy here");
        }
        //System.out.println("After: " + (int) chromosomeA[0] + "---" + (int) chromosomeB[0]);
    }

    public void breed(double[][] chromosomes){
        Random rand = new Random();
        for(int i = 0; i <chromosomes.length/2; i+=2){
            double[] child = new double[chromosomes[i].length];
            double[] child2 = new double[chromosomes[i].length];
            child[0] = 1000;
            child2[0] = 1000;
            for(int j = 1; j < chromosomes[i].length; j++){
                if(rand.nextBoolean()){
                    if(rand.nextDouble()>0.04){
                        child[j] = chromosomes[i][j];
                        child2[j] = chromosomes[i+1][j];
                    }else{
                        if(rand.nextBoolean()){
                            child[j] = chromosomes[i][j]*(3/2) + chromosomes[i][j]*(1/2);
                            if(child[j]>1){
                                child[j] = 2 - child[j];
                            }
                            if(child[j]<-1){
                                child[j] = -2+child[j];
                            }
                        }else{
                            child2[j] = chromosomes[i][j]*(3/2) + chromosomes[i][j]*(1/2);
                            if(child2[j]>1){
                                child2[j] = 2 - child2[j];
                            }
                            if(child2[j]<-1){
                                child2[j] = -2+child2[j];
                            }
                        }
                    }
                }else{
                    if(rand.nextDouble()>0.04){
                        child[j] = chromosomes[i+1][j];
                        child2[j] = chromosomes[i][j];
                    }else{
                        if(rand.nextBoolean()){
                            child[j] = chromosomes[i][j]*(3/2) + chromosomes[i][j]*(1/2);
                            if(child[j]>1){
                                child[j] = 2 - child[j];
                            }
                            if(child[j]<-1){
                                child[j] = -2+child[j];
                            }
                        }else{
                            child2[j] = chromosomes[i][j]*(3/2) + chromosomes[i][j]*(1/2);
                            if(child2[j]>1){
                                child2[j] = 2 - child2[j];
                            }
                            if(child2[j]<-1){
                                child2[j] = -2+child2[j];
                            }
                        }
                    }
                }
            }
            chromosomes[i+chromosomes.length/2] = child;
            chromosomes[i+1+chromosomes.length/2] = child;
        }
    }



    /**
     * Om det bara finns en spelare kvar p� mappen
     * @return True om bara en kvar, false annars
     */
    public boolean end() {
        int left = 0;

        for(HashSet<Hexagon> a : H.getPhex()) {
            if(!a.isEmpty()) {
                left++;
            }
        }

        return left < 2;
    }

    public int getWinner() {
        int max = 0;
        int index = 0;
        for(int i = 0; i < H.getPhex().size(); i++) {
            if(H.getPhex().get(i).size() > max) {
                max = H.getPhex().get(i).size();
                index = i+1;
            }
        }
        return index;

    }

    public void gamerun() {
        HashSet<Hexagon> send = new HashSet<Hexagon>();
        for(Player p: players) {
            send.clear();
            for(Hexagon a: H.getClonedPhex().get(p.getId() - 1)) {
                send.add(a);
            }
            H.move(p.algo(send));
        }
        H.endTurn();
        turn++;
    }

    public double expectedScore(double rA, double rB){
        return 1/(1 + Math.pow(10,(rB-rA)/400));
    }

    public int EarnedRating(double score, double es){
        return (int) (32*(score-es));
    }
}
