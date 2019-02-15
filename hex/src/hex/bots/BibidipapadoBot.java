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

	// Prepare Data and summarize Data - Knowledge Base

	// Model with moves - Model

	
	
	/**Primitive move, will always generate a better board
	 * 0 : Take empty
	 * 1 : Take enemy
	 * 2 : Transfer
	 * 3 : Get under 100
	 * 4 : Move Best Hexagon to frontline
	 */
	private void PrimMove(int move, int res, Hexagon A, Hexagon B){
		
	}
	
	/**Strategy for when PrimMove is not good 
	 * 
	 */
	private void Strategy(){
		
	}
	
	/**End of turn before turning in the move. Cleans up for the next round.
	 * 
	 */
	private void Clean(){
		Actionlist.clear();
	}
	
	/**
	 * Generate Integer[7]
	 * @param res
	 * @param A
	 * @param B
	 * @param value
	 * @return
	 */
	private Integer[] generateMove(int res, Hexagon A, Hexagon B,int value){
		Integer[] temp = new Integer[7];
		temp[0] = super.getId();
		temp[1] = res;
		temp[2] = A.getX();
		temp[3] = A.getY();
		temp[4] = B.getX();
		temp[5] = B.getY();
		temp[6] = value;
		return temp;
	}
	
	
	/** Get the closets enemy prio lowest res
	 * 
	 * @param A
	 * @return
	 */
	private Hexagon ClosestEnemy(Hexagon A){
		return null;
	}
	
	
	private void KnowledgeBaseUpdate(int[] lastmove){
		//Update turn
		//Save lastmove
		//Update Heatmap
		//Update TrueMap
		
		
	}
	
	private void KnowledgeCalc(){
		//Check my info
		//Check what opponent did
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
					HeatMap[x][y] = new Hexagon(x, y);
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
