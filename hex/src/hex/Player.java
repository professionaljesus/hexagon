package hex;

import java.awt.Color;
import java.util.HashSet;

public abstract class Player {
	private int id;
	private Color color;
	private String name;
	private int size;
	public Player(int id, int size, Color c, String name) {
		this.id = id;
		this.color = c;
		this.name = name;
		this.size = size;
	}
	
	public Color getColor() {
		return color;
	}

	//id, res, x, y, targetX, targetY
	abstract public int[] algo(HashSet<Hexagon> H);

	public int getId() {
		return id;
	}
	
	public String getName(){
		return name;
	}
}
