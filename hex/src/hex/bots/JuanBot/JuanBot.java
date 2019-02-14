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
        ArrayList<Hexagon> hexKingdom = new ArrayList<>(H);
        // Categorise hexes
        ArrayList<HexPair> notOwnedHexPairs = getHexPairs(new ArrayList<>(hexKingdom), HexType.NOTOWNED);

        int[] instruction = null;
        // If we can expand without competition, DO IT!
        if (notOwnedHexPairs.size()>0) {
            HexPair selectedHexPair = notOwnedHexPairs.get(rand.nextInt(notOwnedHexPairs.size()));
            Hexagon parent = selectedHexPair.getParent();
            Hexagon target = selectCloseHex(selectedHexPair.getChildren());

            instruction = createInstruction(1, parent, target);
        } else {
            // try to get a sneaky risk free invade, literally can not go wrong.
            // TODO: DO I NEED TO DEFEND?
            instruction = freeInvade(hexKingdom);
            // TODO: PLAN INVADE
        }
        return instruction;
    }

    /**
     * Invade risk free hexagons.
     */
    private int[] freeInvade(ArrayList<Hexagon> hexKingdom) {
        ArrayList<HexPair> enemies = getHexPairs(new ArrayList<>(hexKingdom), HexType.ENEMY);
        //System.out.println(enemies);
        // Get risk free hexagons
        for (HexPair hp: enemies) {
            Hexagon parentHex = hp.getParent();

            int maxResource = 0;
            Hexagon maxNeighbour = null;
            int minResource = 100;
            Hexagon minNeighbour = null;
            for (Hexagon neighbour: hp.getChildren()) {
                int neighbourResource = neighbour.getResources();
                if (neighbourResource < minResource) {
                    minResource = neighbourResource;
                    minNeighbour = neighbour;
                } else if (neighbourResource > maxResource) {
                    maxResource = neighbourResource;
                    maxNeighbour = neighbour;
                }
            }
            if ((parentHex.getResources()-1) - minResource > maxResource) {
                return createInstruction(minResource+1, parentHex, minNeighbour);
            }
        }
        return null;
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
            ArrayList<Hexagon> hexNeighbours = getHexByType(new ArrayList<>(Arrays.asList(neighbours)), type);
            if (hexNeighbours.size() > 0) hpList.add(new HexPair(hex, hexNeighbours));
        }
        return hpList;
    }

    /**
     * Method to get hexes of a specific type.
     * @param hexMap, map of hexes.
     * @return hexes with the type type.
     */
    private ArrayList<Hexagon> getHexByType(ArrayList<Hexagon> hexMap, HexType type) {
        ArrayList<Hexagon> hexes = new ArrayList<>();
        for (Hexagon hex: hexMap) {
            switch(type) {
                case OWNED:
                    if (hex.getOwner() == id) hexes.add(hex);
                    break;
                case NOTOWNED:
                    if (hex.getOwner() == 0) hexes.add(hex);
                    break;
                case ENEMY:
                    if (hex.getOwner() != id && hex.getOwner() != 0) hexes.add(hex);
                    break;
            }
        }
        return hexes;
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

        @Override
        public String toString() {
            return "Parent:" + parent + " == " + children.toString();
        }
    }

    enum HexType {
        OWNED,
        NOTOWNED,
        ENEMY
    }
}
