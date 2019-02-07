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
		
	}

}

