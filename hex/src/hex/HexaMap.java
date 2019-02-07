package hex;

import java.awt.Graphics;

public class HexaMap {
	/**
	 * Spara hexagon i varje element
	 * 
	 **/

	public Hexagon[][] HexaMap;
	private int size;
	private int widht;
	private int height;

	/**
	 * Size resulterar i sum(0,size) 6*n;
	 * 
	 * 
	 **/
	public HexaMap(int size, int width, int height) {
		this.size = size;
		this.widht = width;
		this.height = height;
		
		HexaMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
	}

	/**
	 * Kallas vid start. Sätter ut start player & fyller Mapen Hexagon med empty
	 * hexagon samt null för odef
	 * 
	 * 
	 **/
	public void startMap(int PlayerAmount, Player[] player) {

		// Sätter ut hela mapen som nya hexagon
		for (int i = 0; i < HexaMap.length; i++) {
			for (int j = 0; j < HexaMap.length; j++) {
				HexaMap[i][j] = new Hexagon();
			}
		}

		// Sätter de som inte används till null
		int k = size - 1;
		for (int i = 0; i < size - 1; i++) {
			for (int j = 0; j < k; j++) {
				HexaMap[i][j] = null;
				HexaMap[HexaMap.length - i - 1][HexaMap.length - j - 1] = null;
			}
			k--;
		}

		
		//Placering av spelar positioner
		switch(PlayerAmount){
		case 3:
			HexaMap[0][size-1].setOwner(player[0].getId());
			HexaMap[size-1][size*2-2].setOwner(player[1].getId());
			HexaMap[size*2-2][0].setOwner(player[2].getId());
			break;
		}
		

	}

	/**
	 * Direction 0 = Öst Direction 1 = SydÖst Direction 2 = SydVäst Direction 3
	 * = Väst Direction 4 = NorrVäst Direction 5 = NorrÖst
	 **/
	public Hexagon GetNeighbour(int x, int y, int Direction) {
		int d1 = 0;
		int d2 = 0;
		switch (Direction) {
		case 0:
			d1 = 1;
			break;
		case 1:
			d2 = 1;
			break;
		case 2:
			d1 = -1;
			d2 = 1;
			break;
		case 3:
			d1 = -1;
			break;
		case 4:
			d2 = -1;
			break;
		case 5:
			d1 = 1;
			d2 = -1;
			break;
		}

		if (HexaMap[x][y] == null || (x + d1 > 1 + size * 2) || (y + d2 > 1 + size * 2)) {
			return null;
		}

		return HexaMap[x + d1][y + d2];
	}
	
	public void draw(Graphics g){
		for(int x = 0; x < HexaMap.length; x++) {
			for(int y = 0; y < HexaMap[x].length; y++) {
				if(x + y >= size-1 && x+y<= size*2 +1){
					double rad = 20;
					double originX = (widht/2) - rad*(size-1)+ x*rad*2;
					double originY = (height/2) - rad * Math.sin(30) * (size-1) + y*rad*2;
					drawHexagon(g, originX, originY,rad);
				}
				
			}
		}
	}
	
	public void drawHexagon(Graphics g, double originX, double originY, double rad){
		double base = rad * Math.sin(30);
		int[] px,py;
		
		
		py = new int[]{
				(int) (originY-(rad/2)),//top left
				(int) (originY+(rad/2)),//top right
				(int) (originY+rad),// right
				(int) (originY+(rad/2)),//bottom right
				(int) (originY-(rad/2)),//bottom left
				(int) (originY-rad),// left
		};
		
		px = new int[]{
				(int) (originX-base),//top left
				(int) (originX-base),//top right
				(int) (originX),// right
				(int) (originX+base),//bottom right
				(int) (originX+base),//bottom left
				(int) (originX)// left
		};
		
		
		g.drawPolygon(px,py,6);
		
		/*
		int width = 20;
		double side = width/Math.cos(Math.PI/6);
		double dy = width*Math.tan(Math.PI/6);
		
		int[] px, py;
		if(y%2 == 0) {
			px = new int[]{2*width*x, 2*width*x + width, 2*width*x + width, 2*width*x, 2*width*x - width, 2*width*x - width};
			g.drawString("(" + String.valueOf(x) + "," + String.valueOf(y) + ")", 2*width*x - width + xTranslation, (int)(y*(side + dy) + side + yTranslation));
		}else {
			px = new int[]{2*width*x + width, 2*width*x +2*width, 2*width*x + 2*width, 2*width*x + width, 2*width*x, 2*width*x};
			g.drawString("(" + String.valueOf(x) + "," + String.valueOf(y) + ")", 2*width*x + xTranslation, (int)(y*(side + dy) + side + yTranslation));
		}

		py = new int[] {(int)(y*(side + dy)), (int)(y*(side + dy) + dy), (int)(y*(side + dy) + dy + side), (int)(y*(side + dy) + 2*dy + side), (int)(y*(side + dy) + dy + side), (int)(y*(side + dy) + dy)};
		
		
		//Add the translations
		for(int i = 0; i<px.length; i++){
			px[i] +=  xTranslation;
		}
		for(int i = 0; i<py.length; i++){
			py[i] +=  yTranslation;
		}
		*/
	
	}
	
	 
	/*
	public void draw(Graphics g) {
		int width = 20;
		double side = width/Math.cos(Math.PI/6);
		double dy = width*Math.tan(Math.PI/6);
	
		for(int x = 0; x < HexaMap.length; x++) {
			for(int y = 0; y < HexaMap[x].length; y++) {
				if(HexaMap[x][y] != null) {
					int[] px, py;
					if(y%2 == 0) {
						
						px = new int[]{2*width*x, 2*width*x + width, 2*width*x + width, 2*width*x, 2*width*x - width, 2*width*x - width};
						g.drawString("(" + String.valueOf(x) + "," + String.valueOf(y) + ")", 2*width*x - width, (int)(y*(side + dy) + side));
					}else {
						px = new int[]{2*width*x + width, 2*width*x +2*width, 2*width*x + 2*width, 2*width*x + width, 2*width*x, 2*width*x};
						g.drawString("(" + String.valueOf(x) + "," + String.valueOf(y) + ")", 2*width*x, (int)(y*(side + dy) + side));
					}
					py = new int[] {(int)(y*(side + dy)), (int)(y*(side + dy) + dy), (int)(y*(side + dy) + dy + side), (int)(y*(side + dy) + 2*dy + side), (int)(y*(side + dy) + dy + side), (int)(y*(side + dy) + dy)};
					g.drawPolygon(px,py,6);
					
					
				}
			}
		}
	} 
	 */
	

}
