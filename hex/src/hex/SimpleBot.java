package hex;

public class SimpleBot extends Player {

	int turnorder;
	int own;
	int generating;
	Hexagon[][] heatmap;

	public SimpleBot(int id, int size) {
		super(id, size);
		heatmap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
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

	public int[] algo(HexaMap H) {
		super.getMap(H);
		for (int x = 0; x < super.myMap.length; x++) {
			for (int y = 0; y < super.myMap.length; y++) {
				if (super.myMap[x][y].getOwner() == super.getId()) {
					own++;
					if (super.myMap[x][y].getResources() < 100) {
						generating++;
					}
				}
				if (super.myMap[x][y].getOwner() != super.getId() && super.myMap[x][y].getOwner() != 0) {
					heatmap[x][y].setOwner(super.myMap[x][y].getOwner());
					if (heatmap[x][y].getResources() != super.myMap[x][y].getResources()) {
						heatmap[x][y].setResources(super.myMap[x][y].getResources());
					}
				}

			}
		}

		int[] moves = new int[5];
		moves[0] = super.getId();
		moves[1] = 0;
		moves[3] = 0;
		moves[4] = 3;
		moves[2] = super.myMap[0][3].getResources() / 10;
		// this.getMap(H);
		return moves;
		// g

	}

}
