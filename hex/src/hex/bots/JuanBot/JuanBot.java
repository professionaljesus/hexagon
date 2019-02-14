package hex.bots.JuanBot;

import hex.Hexagon;
import hex.Player;

import java.awt.*;
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
        ArrayList<Hexagon> hAsList = new ArrayList<>(H);
        // Categorise hexes
        ArrayList<HexPair> notOwnedHexPairs = getHexPairs(new ArrayList<>(hAsList), HexType.NOTOWNED);
        ArrayList<HexPair> ownedHexPairs = getHexPairs(new ArrayList<>(hAsList), HexType.OWNED);

        int[] instruction = null;
        // If we can expand without competition, DO IT!
        if (notOwnedHexPairs.size()>0) {
            HexPair selectedHexPair = notOwnedHexPairs.get(rand.nextInt(notOwnedHexPairs.size()));
            Hexagon parent = selectedHexPair.getParent();
            Hexagon target = selectCloseHex(selectedHexPair.getChildren());

            instruction = createInstruction(1, parent, target);
        }
        return instruction;
    }

    /**
     * Method to select non-owned hexagon with the most owned neighbours.
     * @param hexagons list of hexagons.
     * @return hexagon with most owned neighbours.
     */
    private Hexagon selectCloseHex(ArrayList<Hexagon> hexagons) {
        Hexagon lowest = null;
        int highestNeighbourCount = 0; // can not be more than 6?
        for (Hexagon hex: hexagons) {
            int ownedNeighbours = getHexPairs(new ArrayList<>(Arrays.asList(hex.getNeighbours())), HexType.OWNED).size();
            if (ownedNeighbours > highestNeighbourCount) {
                highestNeighbourCount = ownedNeighbours;
                lowest = hex;
            }
        }
        return lowest;
    }

    /**
     * Method to create instruction from res, parent and target.
     * @param res resource to move.
     * @param parent, parent hexagon.
     * @param target, target hexagon.
     * @return instruction for game.
     */
    private int[] createInstruction(int res, Hexagon parent, Hexagon target) {
        return new int[]{id, res, parent.getX(), parent.getY(), target.getX(), target.getY()};
    }

    /**
     * Get hex pairs depending on HexType.
     * @param hexMap, map of game.
     * @param type, type of hexes to return.
     * @return parent and neighbours of HexType type.
     */
    private ArrayList<HexPair> getHexPairs(ArrayList<Hexagon> hexMap, HexType type) {
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
        int selection = rand.nextInt(hexMap.size());
        return hexMap.get(selection);
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
