package cards.map;

import cards.record.Map.DArray;

public class Point {
	DArray a;
	int l;
	int x;
	int y;

	public Point(DArray a, int l, int x, int y) {
		this.a = a;
		this.l = l;
		this.x = x;
		this.y = y;
	}

	public Point(Point o) {
		this.a = o.a;
		this.l = o.l;
		this.x = o.x;
		this.y = o.y;
	}

	public int rx() {
		return Draw.SX - 2 * Draw.PX + (x * 3 + x % 2) * Draw.PX;
	}

	public int ry() {
		return Draw.SY - 2 * Draw.PY + (y * 2 - (x / 2) % 2) * 2 * Draw.PY;
	}

	public boolean match(int wall, int d) {
		if (x % 2 == d % 2)
			switch (d) {
			case 0:
				a.setIndex(x / 2 + y * l);
				return a.getT() == wall;
			case 1:
				a.setIndex(x / 2 + 1 + (y - (x / 2) % 2) * l);
				return a.getL() == wall;
			case 2:
				a.setIndex(x / 2 - 1 + (y - (x / 2) % 2) * l);
				return a.getR() == wall;
			case 3:
				a.setIndex(x / 2 + y * l);
				return a.getT() == wall;
			case 4:
				a.setIndex(x / 2 + y * l);
				return a.getL() == wall;
			case 5:
				a.setIndex(x / 2 + y * l);
				return a.getR() == wall;
			}
		return false;
	}

	public void step(int d) {
		if (x % 2 == (d + 1) % 2)
			throw new RuntimeException("Incorrect direction");
		switch (d) {
		case 0:
			x += 1;
			break;
		case 1:
			y -= (x / 2) % 2;
			x += 1;
			break;
		case 2:
			y -= (x / 2) % 2;
			x -= 1;
			break;
		case 3:
			x -= 1;
			break;
		case 4:
			y += (x / 2 + 1) % 2;
			x -= 1;
			break;
		case 5:
			y += (x / 2 + 1) % 2;
			x += 1;
			break;
		}
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
