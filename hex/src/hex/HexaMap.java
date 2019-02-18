package hex;

import java.awt.Color;

import java.awt.Graphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Stack;

public class HexaMap {
    /**
     * Spara hexagon i en HexaMap[][] Anv�nder Axial coordinates
     **/
    private Hexagon[][] HexaMap;
    private int size;

    public static final int HEXAGON_HEIGHT = 68;
    public static final int HEXAGON_WIDTH = (int) (HEXAGON_HEIGHT * Math.cos(Math.PI / 6));
    public static final boolean wrapAround = false;

    private Player[] player;
    private static ArrayList<HashSet<Hexagon>> phex;
    private static ArrayList<HashSet<Hexagon>> playerHexesAndNeighbours;
    private int width;
    private int height;
    private Stack<int[]> stacken;
    private ArrayList<String> turnInfo;

    /**
     * Size resulterar i sum(0,size) 6*n; HexagonMap blir size*2-1
     **/
    public HexaMap(int size, int width, int height, Player[] player) {
        this.size = size;
        this.width = width;
        this.height = height;

        phex = new ArrayList<HashSet<Hexagon>>();
        for (int i = 0; i < player.length; i++){
            phex.add(new HashSet<Hexagon>());
            playerHexesAndNeighbours.add(new HashSet<Hexagon>());
        }


        this.player = player;

        HexaMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
        stacken = new Stack<int[]>();

        // Detta borde g�ra samma
        for (int x = 0; x < HexaMap.length; x++) {
            for (int y = 0; y < HexaMap[x].length; y++) {
                if (x + y >= size - 1 && x + y <= size * 3 - 3) {
                    HexaMap[x][y] = new Hexagon(x, y);
                }
            }
        }

        for (Hexagon[] uu : HexaMap) {
            for (Hexagon u : uu) {
                if (u != null) {
                    Hexagon[] n = new Hexagon[6];
                    for (int i = 0; i < 6; i++) {
                        n[i] = getNeighbour(u.getX(), u.getY(), i);
                    }
                    u.setNeighbours(n);
                }
            }
        }

        // Placering av spelar positioner
        switch (player.length) {
            case 1:

                HexaMap[0][size - 1].setOwner(player[0].getId());
                HexaMap[0][size - 1].setResources(10);
                phex.get(0).add(HexaMap[0][size - 1]);
                break;
            case 2:

                HexaMap[0][size - 1].setOwner(player[0].getId());
                HexaMap[0][size - 1].setResources(10);
                phex.get(0).add(HexaMap[0][size - 1]);
                HexaMap[size - 1][size - 1].setOwner(player[1].getId());
                HexaMap[size - 1][size - 1].setResources(10);
                phex.get(1).add(HexaMap[size - 1][size - 1]);
                break;
            case 3:
                HexaMap[0][size - 1].setOwner(player[0].getId());
                HexaMap[0][size - 1].setResources(10);
                phex.get(0).add(HexaMap[0][size - 1]);

                HexaMap[size - 1][size - 1].setOwner(player[1].getId());
                HexaMap[size - 1][size - 1].setResources(10);
                phex.get(1).add(HexaMap[size - 1][size - 1]);

                HexaMap[size * 2 - 2][size - 1].setOwner(player[2].getId());
                HexaMap[size * 2 - 2][size - 1].setResources(10);
                phex.get(2).add(HexaMap[size * 2 - 2][size - 1]);

                break;
            case 4:
                HexaMap[size - 1][size / 2].setOwner(player[0].getId());
                HexaMap[size - 1][size / 2].setResources(10);
                phex.get(0).add(HexaMap[size - 1][size / 2]);

                HexaMap[2 * size - 2 - size / 2][size / 2].setOwner(player[1].getId());
                HexaMap[2 * size - 2 - size / 2][size / 2].setResources(10);
                phex.get(1).add(HexaMap[2 * size - 2 - size / 2][size / 2]);

                HexaMap[size / 2][2 * size - 2 - size / 2].setOwner(player[2].getId());
                HexaMap[size / 2][2 * size - 2 - size / 2].setResources(10);
                phex.get(2).add(HexaMap[size / 2][2 * size - 2 - size / 2]);

                HexaMap[size - 1][2 * size - 2 - size / 2].setOwner(player[3].getId());
                HexaMap[size - 1][2 * size - 2 - size / 2].setResources(10);
                phex.get(3).add(HexaMap[size - 1][2 * size - 2 - size / 2]);

                break;
        }

        updatePlayerHexesAndNeighbours();
    }

    public static ArrayList<HashSet<Hexagon>> getPhex() {
        return phex;
    }

    public static ArrayList<HashSet<Hexagon>> getPlayerHexesAndNeighbours() {
        return playerHexesAndNeighbours;
    }

    private void updatePlayerHexesAndNeighbours(){
        for(int i = 0; i<phex.size();i++){
            for(Hexagon hex: phex.get(i)){
                for(Hexagon nei: hex.getNeighbours()){
                    playerHexesAndNeighbours.get(i).add(nei);
                }
            }
        }
    }

    /**
     * Get's called at the endofTurn t[0] user t[1] resource t[2] x t[3] y t[4]
     * targetX t[5] targetY
     * <p>
     * Om dir > 5 s� g�r den p� targetX och Y
     */
    public void endTurn() {
        Collections.shuffle(stacken);
        turnInfo = new ArrayList<String>();
        while (!stacken.isEmpty()) {
            int[] t = stacken.pop();
            String s = "";
            if (t == null) {
                // System.out.println("Ingen respons");
                s += "Ingen Respons";
            } else {
                int id = t[0];
                int res = t[1];
                int x = t[2];
                int y = t[3];
                int targetX = t[4];
                int targetY = t[5];

                // Om man angett en hexagon som inte är ens egen
                if (HexaMap[x][y].getOwner() != id) {
                    s += "Player: " + id + " Wrong owner";
                    continue;
                }

                // Om man har angett mindre res än vad man har
                if (HexaMap[x][y].getResources() < res)
                    continue;

                boolean illegal = true;

                //Om target är ens egen ruta, dvs en boost
                if (HexaMap[targetX][targetY].getOwner() == id) {

                    for (Hexagon n : HexaMap[targetX][targetY].getNeighbours()) {
                        if (n.getOwner() == id) {
                            illegal = false;
                            break;
                        }
                    }
                    if (illegal)
                        continue;

                    HexaMap[targetX][targetY].setResources(HexaMap[targetX][targetY].getResources() + res);
                    HexaMap[x][y].setResources(HexaMap[x][y].getResources() - res);
                    if (HexaMap[x][y].getResources() == 0) {
                        HexaMap[x][y].setOwner(0);
                        phex.get(id - 1).remove(HexaMap[x][y]);
                    }
                } else { //En attack
                    for (Hexagon n : HexaMap[targetX][targetY].getNeighbours()) {
                        //Kollar om source hexagonen är granne med target hexagonen
                        if (n.equals(HexaMap[x][y]))
                            illegal = false;
                    }
                    if (illegal)
                        continue;

                    //Om man attackerar en tomm ruta
                    if (HexaMap[targetX][targetY].getOwner() == 0) {
                        HexaMap[targetX][targetY].setResources(HexaMap[targetX][targetY].getResources() + res);
                        HexaMap[x][y].setResources(HexaMap[x][y].getResources() - res);
                        HexaMap[targetX][targetY].setOwner(id);
                        phex.get(id - 1).add(HexaMap[targetX][targetY]);
                    } else { //Om man attackerar en fiende hexagon

                        //Om man anfaller med mindre trupper en fienden har
                        if (res < HexaMap[targetX][targetY].getResources())
                            HexaMap[targetX][targetY].setResources(HexaMap[targetX][targetY].getResources() - res);
                        else if(res == HexaMap[targetX][targetY].getResources()) { //Om man anfaller med lika många
                            HexaMap[targetX][targetY].setResources(0);
                            phex.get(HexaMap[targetX][targetY].getOwner() - 1).remove(HexaMap[targetX][targetY]);
                            HexaMap[targetX][targetY].setOwner(0);
                        } else { //Om man har mindre
                            HexaMap[targetX][targetY].setResources(res - HexaMap[targetX][targetY].getResources());
                            HexaMap[x][y].setResources(HexaMap[x][y].getResources() - res);
                            phex.get(HexaMap[targetX][targetY].getOwner() - 1).remove(HexaMap[targetX][targetY]);
                            HexaMap[targetX][targetY].setOwner(id);
                            phex.get(id - 1).add(HexaMap[targetX][targetY]);

                        }

                    }

                    // Du sl�sa alla
                    if (HexaMap[x][y].getResources() == 0) {
                        HexaMap[x][y].setOwner(0);
                        phex.get(id - 1).remove(HexaMap[x][y]);
                    }
                }
            }
            if (t != null) {
                s += "Player: " + t[0] + " ( " + t[2] + " , " + t[3] + " ) ----> " + " ( " + t[4] + " , " + t[5] + " ) "
                        + " Res: " + t[1];
            }
            turnInfo.add(s);

        }

        for (HashSet<Hexagon> uu : phex) {
            for (Hexagon u : uu) {
                if (u.getResources() < 100)
                    u.setResources(u.getResources() + 1);
            }
        }

        updatePlayerHexesAndNeighbours();
    }

    /**
     * @param t playerid, dir, res, x, y, targetx, targety
     */
    public void move(int[] t) {
        stacken.push(t);
    }

    public Hexagon[][] getHexaMap() {
        return HexaMap;
    }

    public Hexagon getNeighbour(int x, int y, int direction) {
        int[] target = getNeighbourXY(x, y, direction);
        return HexaMap[target[0]][target[1]];
    }

    public int[] getNeighbourXY(int x, int y, int Direction) {
        int targetX = 0;
        int targetY = 0;
        int[] pos = new int[2];
        switch (Direction) {
            case 0: // H�ger
                if (x + y == size * 3 - 3) { // bottom right
                    targetX = x - (size - 1);
                    targetY = y - (size - 1);
                } else if (x == size * 2 - 2) { // top right
                    targetX = 0;
                    targetY = y + size;
                } else {
                    targetX = x + 1;
                    targetY = y;
                }
                break;
            case 1: // Ner�t H�ger
                if (y == size * 2 - 2) { // bottom
                    targetX = x + (size - 1);
                    targetY = 0;
                } else if (x + y == size * 3 - 3) { // bottom right
                    targetX = x - size;
                    targetY = y - (size - 2);
                } else {
                    targetX = x;
                    targetY = y + 1;
                }
                break;
            case 2: // Ner�t V�nster
                if (x == 0) { // bottom left
                    targetX = size * 2 - 2;
                    targetY = y - (size - 1);
                } else if (y == size * 2 - 2) { // bottom
                    targetX = x + (size - 2);
                    targetY = 0;
                } else {
                    targetX = x - 1;
                    targetY = y + 1;
                }
                break;
            case 3: // V�nster
                if (x + y == size - 1) { // top left
                    targetX = x + (size - 1);
                    targetY = y + (size - 1);
                } else if (x == 0) { // bottom left
                    targetX = size * 2 - 2;
                    targetY = y - size;
                } else {
                    targetX = x - 1;
                    targetY = y;
                }
                break;
            case 4: // Upp �t v�nster
                if (y == 0) {// top
                    targetX = x - (size - 1);
                    targetY = size * 2 - 2;
                } else if (x + y == size - 1) {// top left
                    targetX = x + size;
                    targetY = y + (size - 2);
                } else {
                    targetX = x;
                    targetY = y - 1;
                }
                break;
            case 5: // Upp �t H�ger
                if (x == size * 2 - 2) {// top right
                    targetX = 0;
                    targetY = y + (size - 1);
                } else if (y == 0) {// top
                    targetX = x - (size - 2);
                    targetY = size * 2 - 2;
                } else {
                    targetX = x + 1;
                    targetY = y - 1;
                }
                break;
        }
        pos[0] = targetX;
        pos[1] = targetY;
        return pos;
    }

    public void draw(Graphics g) {
        int sum;

        int counter = 0;
        if (turnInfo != null) {
            for (String s : turnInfo) {
                g.drawString(s, (int) (width * 0.7), (int) (height * 0.5) + counter * 20);
                counter++;
            }
        }

        int tempX = (int) (width * 0.7);
        int tempY = (int) (height * 0.125);
        g.drawString("Hexagons   |   Resources", tempX + player[0].getName().length() * 15 - 50, tempY - 10);


        for (int i = 0; i < player.length; i++) {
            g.setColor(player[i].getColor());
            g.fillRect(tempX, tempY + 20 * i, 10, 10);
            g.setColor(Color.BLACK);
            sum = 0;
            for (Hexagon h : phex.get(i)) {
                sum += h.getResources();
            }
            g.drawString(player[i].getName(), tempX + 20, tempY + 20 * i + 10);
            g.drawString(phex.get(i).size() + " | " + sum, tempX + player[0].getName().length() * 15,
                    tempY + 20 * i + 10);
        }

        for (int x = 0; x < HexaMap.length; x++) {
            for (int y = 0; y < HexaMap[x].length; y++) {
                if (x + y >= size - 1 && x + y <= size * 3 - 3) {
                    double originX = (width / 3)// Center of the screen
                            - HEXAGON_WIDTH * (size - 1) + x * (HEXAGON_WIDTH) + (y - size + 1) * (HEXAGON_WIDTH / 2);// Shift
                    double originY = (height / 2) - HEXAGON_HEIGHT * (size - 1) * 0.75 + y * (HEXAGON_HEIGHT / 2) * 1.5;
                    drawHexagon(g, x, y, originX, originY);

                    if (wrapAround) {
                        drawHexagon(g, x, y, originX + HEXAGON_WIDTH * (0.5) * (size * 3 - 2),
                                originY - HEXAGON_HEIGHT * (0.75) * size);
                        drawHexagon(g, x, y, originX + HEXAGON_WIDTH * (0.5) * (size * 3 - 1),
                                originY + HEXAGON_HEIGHT * (0.75) * (size - 1));
                        drawHexagon(g, x, y, originX + HEXAGON_WIDTH * (0.5),
                                originY + HEXAGON_HEIGHT * (0.75) * (size * 2 - 1));

                        drawHexagon(g, x, y, originX - HEXAGON_WIDTH * (0.5) * (size * 3 - 2),
                                originY + HEXAGON_HEIGHT * (0.75) * size);
                        drawHexagon(g, x, y, originX - HEXAGON_WIDTH * (0.5) * (size * 3 - 1),
                                originY - HEXAGON_HEIGHT * (0.75) * (size - 1));
                        drawHexagon(g, x, y, originX - HEXAGON_WIDTH * (0.5),
                                originY - HEXAGON_HEIGHT * (0.75) * (size * 2 - 1));
                    }
                }
            }
        }
    }

    public void drawHexagon(Graphics g, int x, int y, double originX, double originY) {

        int[] px, py;

        px = new int[]{(int) (originX - (HEXAGON_WIDTH / 2)), // top left
                (int) (originX), // top
                (int) (originX + (HEXAGON_WIDTH / 2)), // top right
                (int) (originX + (HEXAGON_WIDTH / 2)), // top left
                (int) (originX), // top
                (int) (originX - (HEXAGON_WIDTH / 2)) // top right
        };

        py = new int[]{(int) (originY - (HEXAGON_HEIGHT / 4)), // top left
                (int) (originY - (HEXAGON_HEIGHT / 2)), // top
                (int) (originY - (HEXAGON_HEIGHT / 4)), // top right
                (int) (originY + (HEXAGON_HEIGHT / 4)), // top left
                (int) (originY + (HEXAGON_HEIGHT / 2)), // top
                (int) (originY + (HEXAGON_HEIGHT / 4)) // top right
        };

        if (HexaMap[x][y].getOwner() != 0) {
            g.setColor(player[HexaMap[x][y].getOwner() - 1].getColor());
            g.fillPolygon(px, py, 6);
        }

        g.setColor(Color.BLACK);

        g.drawString(Integer.toString(HexaMap[x][y].getResources()),
                (int) originX - g.getFontMetrics().stringWidth(HexaMap[x][y].toString()) / 2, (int) originY - 10);
        g.drawString(HexaMap[x][y].toString(),
                (int) originX - g.getFontMetrics().stringWidth(HexaMap[x][y].toString()) / 2, (int) originY + 8);

        g.drawPolygon(px, py, 6);
    }

}
