package hex;

import java.awt.*;
import java.io.IOException;

import javax.swing.JFrame;

import com.Boi;
import hex.Panel;
import hex.bots.BepsiMax.BeppeBot;
import hex.bots.CrazyBot;
import hex.bots.SimpleBot;

public class Main {

	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame("FrameDemo");
		int mapsize = 4;
		Player player[] = new Player[3];
		player[0] = new BeppeBot(1,mapsize, Color.GREEN, "BEPPNATION");
		player[1] = new CrazyBot(2,mapsize, Color.RED, "GURRA",new Boi(new double[]{0.28840757649341964, 0.14188537681292093, 0.01269229409707147}));
		player[2] = new SimpleBot(3,mapsize, Color.CYAN, "WILDCARD");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new Panel(player,mapsize));
		frame.pack();
		frame.setVisible(true);

	}
	
}

