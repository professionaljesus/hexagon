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
	 * S�tter ut start player & fyller Mapen Hexagon med empty hexagon samt null f�r odef
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
	
	
	/*Direction 0 = �st
	 * Direction 1 = Syd�st
	 * Direction 2 = SydV�st
	 * Direction 3 = V�st
	 * Direction 4 = NorrV�st
	 * Direction 5 = Norr�st
	 */
	public Hexagon GetNeighbour(int Direction){
		
		
		return null;
	}
	
}
