package hex;



public class HexaMap {
	/**Spara hexagon i varje element
	 * 
	 * 
	 * 
	 * 
	 * 
	 **/
	
	public Hexagon[][] HexaMap;
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
				HexaMap[HexaMap.length-i-1][HexaMap.length-j-1] = null;
			}
			k--;
		}
		//S�tter in player
		for(int i =0; i < PlayerAmount;i++){

			double angle = i*2*Math.PI/((double)PlayerAmount); ;

			
			int x =  Math.toIntExact((long)(((size-1))*((Math.cos(angle)))));
			int y =  Math.toIntExact((long)(((size-1))*((Math.sin(angle)))));
			HexaMap[size-1-x][size-1-y] = new Hexagon(player[i].getId(),100);
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
