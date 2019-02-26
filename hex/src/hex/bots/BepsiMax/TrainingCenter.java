package hex.bots.BepsiMax;

import com.Boi;
import hex.HexaMap;
import hex.Hexagon;
import hex.Player;
import hex.bots.CrazyBot;

import java.awt.*;
import java.util.HashSet;

public class TrainingCenter {

    private Thread thread;
    private HexaMap H;
    private Player[] player;
    public int turn;
    private int mapsize;

    private final int width = 1280;
    private final int height = 720;
    public int winner;



    public TrainingCenter(double[][] chromosomes) {

        mapsize = 4;
        player = new Player[3];

        for (int i = 0; i<3; i++){
            player[i] = new Bepster(i+1,4,Color.RED,"test",chromosomes[i]);
        }


    }

    public void initGame() {
        H = new HexaMap(mapsize,width,height,player);
        turn = 0;
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
                index = i;
            }
        }
        return index;

    }



    public void gamerun() {
        HashSet<Hexagon> send = new HashSet<Hexagon>();
        for(Player p: player) {
            send.clear();
            for(Hexagon a: H.getClonedPhex().get(p.getId() - 1)) {
                send.add(a);
            }
            H.move(p.algo(send));
        }
        H.endTurn();
        turn++;
    }
}
