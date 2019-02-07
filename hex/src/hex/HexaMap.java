package hex;



public class HexaMap {
	/**Spara hexagon i varje element
	 * 
	 * 
	 * 
	 * 
	 * 
	 **/
	
	Hexagon[][] HexaMap;
	private int size;
	
	
	/**Size resulterar i sum(0,size) 6*n;
	 * 
	 * 
	 **/
	public HexaMap(int size){
		this.size = size;
		HexaMap = new Hexagon[1+(size-1)*2][1+(size-1)*2];
	}
	
	/**Kallas vid start.
	 * S�tter ut start player & fyller Mapen Hexagon med empty hexagon samt null f�r odef
	 * 
	 * 
	 **/
	public void startMap(int PlayerAmount,Player[] player){
		
		//S�tter ut hela mapen som nya hexagon
		for(int i =0; i < HexaMap.length;i++){
			for(int j =0; j < HexaMap.length;j++){
				HexaMap[i][j]=new Hexagon();
			}
		}
		
		//S�tter de som inte anv�nds till null
		int k = size-1;
		for(int i=0;i<size-1;i++){
			for(int j=0;j<k;j++){
				HexaMap[i][j] = null;
				k--;
			}
		}
		//S�tter in player
		for(int i =0; i < PlayerAmount;i++){
			double angle = 0;
			if(i !=0){
				angle = Math.PI/((double) i*PlayerAmount); ;
			}
			int x = Math.round((float) (Math.cos(Math.PI/2 + angle)));
			int y = Math.round((float) (Math.sin(Math.PI/2 + angle)));
			HexaMap[size-1+x][size-1+y] = new Hexagon(player[i].getId(),100);
		}
		
		
	}
	
	
	/**Direction 0 = �st
	 * Direction 1 = Syd�st
	 * Direction 2 = SydV�st
	 * Direction 3 = V�st
	 * Direction 4 = NorrV�st
	 * Direction 5 = Norr�st
	 **/
	public Hexagon GetNeighbour(int Direction){
		
		
		return null;
	}
	
}
