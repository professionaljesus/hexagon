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
	private Hexagon[][] TrueMap;

	public BibidipapadoBot(int id, int size, Color c, String name) {
		super(id, size, c, name);
		this.size = size;
		turn = 0;
		InitializeMaps();

	}

	public int[] algo(HashSet<Hexagon> H) {
		return null;
	}

	// Prepare Data and summerice Data - Knowledge Base

	// Model with moves - Model

	
	
	
	
	
	
	
	/**End of turn before turning in the move. Cleans up for the next round.
	 * 
	 */
	private void Clean(){
		
	}
	
	
	
	private void KnowledgeBaseUpdate(int[] lastmove){
		//Update turn
		//Save lastmove
		//Update Heatmap
		//Update TrueMap
		
		
	}
	
	private void KnowledgeCalc(){
		//Check my info
		//Check what apponent did
		//Check what info don't know.
		//Calculate risk
		//Calculate a model what opponent x do
	}
	
	
	
	
	
	
	
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Initialize just the HeatMap.
	 */
	private void InitializeHeatMap() {
		HeatMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
		for (int x = 0; x < HeatMap.length; x++) {
			for (int y = 0; y < HeatMap[x].length; y++) {
				if (x + y >= size - 1 && x + y <= size * 3 - 3) {
					HeatMap[x][y] = new Hexagon(x, y,size);
				}
			}
		}
	}

	/**
	 * Initialize the Action
	 */
	private void InitializeMaps() {
		InitializeHeatMap();
		Actionlist = new HashSet<Integer[]>();
		TrueMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
	}

}
