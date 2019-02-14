package hex.bots.JuanBot;

import hex.Hexagon;
import hex.Player;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;

public class JuanBot extends Player {
    private int MAP_SIZE;
    private int id;
    Random rand;

    public JuanBot(int id, int size, Color c, String name) {
        super(id, size, c, name);
        MAP_SIZE = size;
        this.id = id;
        rand = new Random();
    }

    @Override
    public int[] algo(HashSet<Hexagon> H) {
        // Categorise hexes
        ArrayList<HexPair> notOwnedHexPairs = getHexPairs(H, HexType.NOTOWNED);
        ArrayList<HexPair> ownedHexPairs = getHexPairs(H, HexType.OWNED);

        HexPair selectedHexPair = notOwnedHexPairs.get(rand.nextInt(notOwnedHexPairs.size()));
        Hexagon parent = selectedHexPair.getParent();
        Hexagon target = selectRandomHex(selectedHexPair.getChildren());

        int[] instruction = new int[6];
        instruction[0] = id;
        instruction[1] = 1;
        instruction[2] = parent.getX();
        instruction[3] = parent.getY();
        instruction[4] = target.getX();
        instruction[5] = target.getY();
        System.out.println(Arrays.toString(instruction));
        return instruction;
    }

    /**
     * Get hex pairs depending on HexType.
     * @param hexMap
     * @param type
     * @return parent and neighbours of HexType type.
     */
    private ArrayList<HexPair> getHexPairs(HashSet<Hexagon> hexMap, HexType type) {
        ArrayList<HexPair> hpList = new ArrayList<>();
        for (Hexagon hex: hexMap) {
            Hexagon[] neighbours = hex.getNeighbours();
            ArrayList<Hexagon> hexNeighbours = new ArrayList<>();
            switch(type) {
                case OWNED:
                    hexNeighbours = getOwnedHexes(new ArrayList<>(Arrays.asList(neighbours)));
                    break;
                case NOTOWNED:
                    hexNeighbours = getNotOwnedHexes(new ArrayList<>(Arrays.asList(neighbours)));
                    break;
            }
            if (hexNeighbours.size() > 0) hpList.add(new HexPair(hex, hexNeighbours));
        }
        return hpList;
    }

    /**
     * Method to get hexes not owned by any person.
     * @param hexMap, map of hexes.
     * @return hexes not owned by anyone.
     */
    private ArrayList<Hexagon> getNotOwnedHexes(ArrayList<Hexagon> hexMap) {
        ArrayList<Hexagon> notOwned = new ArrayList<>();
        for (Hexagon hex: hexMap) {
            if (hex.getOwner() == 0) notOwned.add(hex);
        }
        return notOwned;
    }

    /**
     * Method to get owned hexes.
     * @param hexMap, map of hexes.
     * @return hexes owned by id.
     */
    private ArrayList<Hexagon> getOwnedHexes(ArrayList<Hexagon> hexMap) {
        ArrayList<Hexagon> owned = new ArrayList<>();
        for (Hexagon hex: hexMap) {
            if (hex.getOwner() == id) owned.add(hex);
        }
        return owned;
    }

    /**
     * Select a random hex from a list of hexes.
     * @param hexMap, list of hexes.
     * @return random hex.
     */
    private Hexagon selectRandomHex(ArrayList<Hexagon> hexMap) {
        if (hexMap.size() > 0) {
            int selection = rand.nextInt(hexMap.size());
            return hexMap.get(selection);
        }
        return null;
    }

    /**
     * Class for storing hexpairs.
     */
    class HexPair {
        private Hexagon parent;
        private ArrayList<Hexagon> children;

        public HexPair(Hexagon parent, ArrayList<Hexagon> children) {
            this.parent = parent;
            this.children = children;
        }

        public Hexagon getParent() {
            return parent;
        }

        public ArrayList<Hexagon> getChildren() {
            return children;
        }
    }

    enum HexType {
        OWNED,
        NOTOWNED,
        ENEMY
    }
}
