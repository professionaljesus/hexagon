package hex.bots;

import java.awt.Color;
import java.util.HashSet;

import hex.Hexagon;
import hex.Player;

public class BibidipapadoBot extends Player {

	private Hexagon[][] HeatMap;
	private int size;
	private int turn;
	private HashSet<Integer[]> Actionlist;

	
	public BibidipapadoBot(int id, int size, Color c, String name) {
		super(id, size, c, name);
		this.size = size;
		turn = 0;
		InitializeHeatMap();
		InitializeActionMap();
	}

	public int[] algo(HashSet<Hexagon> H) {
		return null;
	}

	// Prepare Data and summerice Data - Knowledge Base

	// Model with moves - Model

	
	
	
	
	/**Initialize just the HeatMap.
	 */
	private void InitializeHeatMap() {
		HeatMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
		for (int x = 0; x < HeatMap.length; x++) {
			for (int y = 0; y < HeatMap[x].length; y++) {
				if (x + y >= size - 1 && x + y <= size * 3 - 3) {
					HeatMap[x][y] = new Hexagon(x, y);
				}
			}
		}
	}
	
	private void InitializeActionMap(){
		Actionlist = new HashSet<Integer[]>();
	}
	
}
