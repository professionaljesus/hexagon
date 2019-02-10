package hex;

import java.awt.Color;
import java.util.HashSet;

public abstract class Player {
	private int id;
	private Color color;

	public Player(int id, int size, Color c) {
		this.id = id;
		this.color = c;

	}
	
	public Color getColor() {
		return color;
	}

	
	abstract public int[] algo(HashSet<Hexagon> H);

	public int getId() {
		return id;
	}
}
