package hex.bots.BepsiMax;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import hex.Hexagon;
import hex.Player;

public class Bepster extends Player {

    HashSet<Hexagon> neighbours = new HashSet<Hexagon>();
    HashSet<Hexagon> outers = new HashSet<Hexagon>();
    HashSet<Hexagon> inners = new HashSet<Hexagon>();
    double[] chromosome;

    public Bepster(int id, int size, Color c, String name, double[] chromosome) {
        super(id, size, c, name);

        this.chromosome = chromosome;
    }

    // id, res, x, y, targetX, targetY
    @Override
    public int[] algo(HashSet<Hexagon> H) {
        neighbours.clear();
        outers.clear();
        inners.clear();
        // Creates hashsets with my neighbouring, outer and inner hexagons
        for (Hexagon hex : H) {
            Hexagon[] neighs = hex.getNeighbours(getId());
            boolean inner = true;
            for (int i = 0; i < 6; i++) {
                if (neighs[i] != null && neighs[i].getOwner() != getId()) {
                    neighbours.add(neighs[i]);
                    outers.add(hex);
                    inner = false;
                }
            }
            if (inner == true) {
                inners.add(hex);
            }
        }

//        System.out.println("Id: " + getId() );
//        System.out.println("-------Neighbours-------");
//        for(Hexagon hex: neighbours){
//            System.out.println("Coords: " + hex.getX() + "," + hex.getY() );
//        }
//        System.out.println("-------Outers-------");
//        for(Hexagon hex: outers){
//            System.out.println("Coords: " + hex.getX() + "," + hex.getY() );
//        }
//        System.out.println("-------Inners-------");
//        for(Hexagon hex: inners){
//            System.out.println("Coords: " + hex.getX() + "," + hex.getY() );
//        }

        double valueOfBestFoundCapture = -100;
        int[] bestCapture = null;

        //Loop over all possible captures
        for (Hexagon source : outers) {
            for (Hexagon target : source.getNeighbours(getId())) {
                //Skip friendlies
                if(target.getOwner()==getId()){
                    continue;
                }

                //If capture is possible
                if (source.getResources() + 1 > target.getResources()) {
                    int maxTransfer = source.getResources() - 1;
                    int minTransfer = target.getResources() + 1;
                    int trooploss = target.getResources();

                    for(int i = 0; i<2; i++){

                        //Test three amounts of transfers
                        int transfer = 0;
                        if(i == 0) {
                            transfer = maxTransfer;
                        }else if(i == 1){
                            transfer = minTransfer + (maxTransfer-minTransfer)*(2/3);
                        }else if(i == 2) {
                            transfer = minTransfer + (maxTransfer-minTransfer)*(1/3);
                        }
                        int troopsInTarget = transfer - target.getResources();
                        int troopsInSource = source.getResources() - transfer;
                        double averageDistToFriends = averageDistToFriendlies(target);
                        double valueOfThisCapture = valueOfCapture(trooploss,troopsInTarget,troopsInSource,averageDistToFriends);
                        if (valueOfThisCapture> valueOfBestFoundCapture) {
                            valueOfBestFoundCapture = valueOfThisCapture;
                            bestCapture = new int[]{getId(), transfer, source.getX(), source.getY(), target.getX(), target.getY()};
                        }

                    }

                }
            }
        }

        double valueOfBestFoundBoost = -100;
        int[] bestBoost = null;

        if(!inners.isEmpty()){
            Hexagon source = Collections.max(inners);
            int sourceRes = source.getResources();
            for(Hexagon target: outers){
                int targetRes = target.getResources();
                int largestEnemy = largestEnemy(target);
                int nrOfSurroundingEnemyHexes = nrEnemyHexes(target);

                double valueOfThisBoost = valueOfBoost(sourceRes, targetRes, largestEnemy, nrOfSurroundingEnemyHexes);
                if(valueOfThisBoost>valueOfBestFoundBoost){
                    valueOfBestFoundBoost = valueOfThisBoost;
                    bestBoost = new int[]{getId(),sourceRes-1,source.getX(),source.getY(),target.getX(),target.getY()};
                }
            }
        }


        valueOfBestFoundCapture = valueOfBestFoundCapture*chromosome[0];
        valueOfBestFoundBoost = valueOfBestFoundBoost*chromosome[1];

        if(bestCapture != null && (valueOfBestFoundCapture >= valueOfBestFoundBoost || bestBoost == null)){
            System.out.println("Capture: " + bestCapture[0]  + " "+ bestCapture[1]+ " "+ bestCapture[2]+ " "+ bestCapture[3]+ " "+ bestCapture[4]+ " "+ bestCapture[5]);
            return bestCapture;
        }else if(bestBoost != null && valueOfBestFoundBoost>valueOfBestFoundCapture){
            //System.out.println("returned boost");
            return bestBoost;

        }else{
            return null;
        }
    }

    private double valueOfCapture(int trooploss, int troopsInTarget, int troopsInSource, double averageDistToFriends) {
        double value = 0;
        value += chromosome[2] * trooploss;
        value += chromosome[3] * troopsInTarget;
        value += chromosome[4] * troopsInTarget;
        value += chromosome[5] * troopsInTarget;
        return value;
    }

    private double valueOfBoost(int sourceRes, int targetRes, int largestEnemy, int nrOfSurroundingEnemyHexes){
        double value = 0;
        value += chromosome[6] * sourceRes;
        value += chromosome[7] * targetRes;
        value += chromosome[8] * largestEnemy;
        value += chromosome[9] * nrOfSurroundingEnemyHexes;
        return value;
    }

    private double averageDistToFriendlies(Hexagon target) {
        int dist = 0;
        int nr = 0;
        for (Hexagon hex : outers) {
            dist += target.calculateDistanceTo(hex);
            nr++;
        }
        for (Hexagon hex : inners) {
            dist += target.calculateDistanceTo(hex);
            nr++;
        }
        return dist / nr;
    }

    private int largestEnemy(Hexagon target){
        int value = 0;
        for(Hexagon n: target.getNeighbours(getId())){
            if(n != null && n.getOwner() != getId() && n.getResources() > value){
                value = n.getResources();
            }
        }
        return value;
    }

    private int nrEnemyHexes(Hexagon target){
        int nr = 0;
        for(Hexagon n: target.getNeighbours(getId())){
            if((n != null) && n.getOwner() != getId() && n.getOwner() != 0){
                nr++;
            }
        }
        return nr;
    }

}
