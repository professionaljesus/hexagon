package hex;

import java.io.IOException;

import javax.swing.JFrame;

import hex.Panel;

public class Main {

	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame("FrameDemo");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new Panel());
		frame.pack();

		frame.setVisible(true);
		Player[] player = new Player[3];
		player[0] = new Player(1);
		player[1] = new Player(2);
		player[2] = new Player(3);
		HexaMap H = new HexaMap(4);
		H.startMap(3, player);
		
		System.out.println();
	}

}

