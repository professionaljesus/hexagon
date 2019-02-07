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
		
		
		
		Player[] player = new Player[4];
		player[0] = new Player(1);
		player[1] = new Player(2);
		player[2] = new Player(3);
		player[3] = new Player(4);
		HexaMap H = new HexaMap(4);
		H.startMap(4, player);
		Hexagon test = H.GetNeighbour(3, 3, 0);
		System.out.println();
	}
	
	
	public void test(){
		System.out.println("test");
		//asdjhflkösdajhfasd
	}
	
}

