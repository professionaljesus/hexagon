package hex;



public class Map {
	/*Spara hexagon i varje element
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	Hexagon[][] Map;
	private int size;
	
	
	/*Size resulterar i sum(0,size) 6*n;
	 * 
	 * 
	 */
	public Map(int size){
		this.size = size;
		Map = new Hexagon[1+(size-1)*2][1+(size-1)*2];
	}
	
	/*Kallas vid start.
	 * Sätter ut start player & fyller Mapen Hexagon med empty hexagon samt null för odef
	 * 
	 * 
	 */
	public void startMap(int PlayerAmount){
		for(int i =0; i < Map.length;i++){
			for(int j =0; j < Map.length;j++){
				Map[i][j]=new Hexagon();
			}
		}
		int k = size-1;
		for(int i=0;i<size-1;i++){
			for(int j=0;j<k;j++){
				Map[i][j] = null;
				k--;
			}
		}

		
	}
	
	
	/*Direction 0 = Öst
	 * Direction 1 = SydÖst
	 * Direction 2 = SydVäst
	 * Direction 3 = Väst
	 * Direction 4 = NorrVäst
	 * Direction 5 = NorrÖst
	 */
	public Hexagon GetNeighbour(int Direction){
		
		
		return null;
	}
	
}
