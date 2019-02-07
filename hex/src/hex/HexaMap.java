package hex;



public class HexaMap {
	/*Spara hexagon i varje element
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	Hexagon[][] HexaMap;
	private int size;
	
	
	/*Size resulterar i sum(0,size) 6*n;
	 * 
	 * 
	 */
	public HexaMap(int size){
		this.size = size;
		HexaMap = new Hexagon[1+(size-1)*2][1+(size-1)*2];
	}
	
	/*Kallas vid start.
	 * Sätter ut start player & fyller Mapen Hexagon med empty hexagon samt null för odef
	 * 
	 * 
	 */
	public void startMap(int PlayerAmount){
		
		//Sätter ut hela mapen som nya hexagon
		for(int i =0; i < HexaMap.length;i++){
			for(int j =0; j < HexaMap.length;j++){
				HexaMap[i][j]=new Hexagon();
			}
		}
		
		//Sätter de som inte används till null
		int k = size-1;
		for(int i=0;i<size-1;i++){
			for(int j=0;j<k;j++){
				HexaMap[i][j] = null;
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
