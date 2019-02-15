package hex.bots.JuanBot;

import com.google.gson.Gson;
import hex.Hexagon;
import hex.Player;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class JuanMLBot extends Player {
    private Server server;

    public JuanMLBot(int id, int size, Color c, String name) {
        super(id, size, c, name);
        server = new Server(8989);
        try {
            server.run(size, "training");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[] algo(HashSet<Hexagon> H) {
        ArrayList<Hexagon> hexKingdom = new ArrayList<>(H);
        Gson gson = new Gson();

        ArrayList<SimpleHexagon> simplexKingdom = new ArrayList<>();
        for (Hexagon hex: hexKingdom) {
            simplexKingdom.add(new SimpleHexagon(hex.getOwner(), hex.getX(), hex.getY(), hex.getResources()));
        }

        String json = gson.toJson(simplexKingdom);
        server.send(json);
        //String response = server.receive();
        return null;
    }
}
