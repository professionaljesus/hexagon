package hex;
import java.awt.Color;
import java.util.HashSet;
public class SimpleBot extends Player {
	int turnorder;
	int own;
	int generating;
	Hexagon[][] heatmap;

	public SimpleBot(int id, int size,Color color) {
		super(id, size,color);
		heatmap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
		for (int x = 0; x < heatmap.length; x++) {
			for (int y = 0; y < heatmap[x].length; y++) {
				if (x + y >= size - 1 && x + y <= size * 3 - 3) {
					heatmap[x][y] = new Hexagon(x, y);
				}
			}
		}
		switch (id) {
		case 1:
			heatmap[size - 1][size * 2 - 2].setOwner(2);
			heatmap[size - 1][size * 2 - 2].setResources(100);
			heatmap[size * 2 - 2][0].setOwner(3);
			heatmap[size * 2 - 2][0].setResources(100);

			heatmap[0][size - 1].setOwner(1);
			heatmap[0][size - 1].setResources(100);
			break;
		case 2:
			heatmap[size * 2 - 2][0].setOwner(3);
			heatmap[size * 2 - 2][0].setResources(100);
			heatmap[0][size - 1].setOwner(1);
			heatmap[0][size - 1].setResources(100);

			heatmap[size - 1][size * 2 - 2].setOwner(2);
			heatmap[size - 1][size * 2 - 2].setResources(100);
			break;
		case 3:
			heatmap[size - 1][size * 2 - 2].setOwner(1);
			heatmap[size - 1][size * 2 - 2].setResources(100);
			heatmap[0][size - 1].setOwner(2);
			heatmap[0][size - 1].setResources(100);

			heatmap[size * 2 - 2][0].setOwner(3);
			heatmap[size * 2 - 2][0].setResources(100);
			break;
		}
		own = 1;
		generating = 1;
		turnorder = id;

	}
		
	public int[] algo(HashSet<Hexagon> H) {
		return null;
//		super.getMap(H);
//		for (int x = 0; x < super.myMap.length; x++) {
//			for (int y = 0; y < super.myMap.length; y++) {
//				if (super.myMap[x][y].getOwner() == super.getId()) {
//					own++;
//					if (super.myMap[x][y].getResources() < 100) {
//						generating++;
//					}
//				}
//				if (super.myMap[x][y].getOwner() != super.getId() && super.myMap[x][y].getOwner() != 0) {
//					heatmap[x][y].setOwner(super.myMap[x][y].getOwner());
//					if (heatmap[x][y].getResources() != super.myMap[x][y].getResources()) {
//						heatmap[x][y].setResources(super.myMap[x][y].getResources());
//					}
//				}
//			}
//		}
	}


}
