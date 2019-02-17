package hex.bots.JuanBot;

import com.google.gson.Gson;
import hex.Hexagon;
import hex.Player;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class JuanMLBot extends Player {
    private Server server;
    private int MAP_SIZE;
    private int numberOfHexagons;

    public JuanMLBot(int id, int size, Color c, String name) {
        super(id, size, c, name);
        server = new Server(8989);
        MAP_SIZE = size;
        numberOfHexagons = (int) (1 + 6*(0.5*MAP_SIZE*(MAP_SIZE-1)));
        try {
            server.run(size, "training");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[] algo(HashSet<Hexagon> H) {
        Gson gson = new Gson();
        ArrayList<Hexagon> hexKingdom = new ArrayList<>(H);
        // send map
        String json = createMapJSON(hexKingdom);
        server.send(json);
        // send game info

        // read response
        String response = server.receive();
        return gson.fromJson(response, int[].class);
    }

    public String createMapJSON(ArrayList<Hexagon> kingdom) {
        Gson gson = new Gson();

        SimpleHexagon[] hexagonArray = new SimpleHexagon[numberOfHexagons];
        for (Hexagon hex: kingdom) {
            SimpleHexagon simpleHex = new SimpleHexagon(hex.getOwner(), hex.getX(), hex.getY(), hex.getResources());
            int arrayIndex = getMapArrayIndex(hex);
            hexagonArray[arrayIndex] = simpleHex;

            // add neighbours
            Hexagon[] neighbours = hex.getNeighbours();
            for (Hexagon h: neighbours) {
                SimpleHexagon simpleNeighbour = new SimpleHexagon(h.getOwner(), h.getX(), h.getY(), h.getResources());
                arrayIndex = getMapArrayIndex(h);
                hexagonArray[arrayIndex] = simpleNeighbour;
            }
        }
        return gson.toJson(hexagonArray);
    }

    private int getMapArrayIndex(Hexagon hex) {
        int row = hex.getX();
        if (row <= MAP_SIZE) {
            return hex.getY() - ((MAP_SIZE-row)-1) + hex.getX() * (MAP_SIZE + (MAP_SIZE-(row-1)));
        } else {
            return hex.getY() + numberOfHexagons/2;
        }
    }
}
