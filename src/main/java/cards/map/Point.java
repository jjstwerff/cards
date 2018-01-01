package cards.map;

import cards.record.Map.DArray;

public class Point {
	final DArray a;
	final int l;
	final int x;
	final int y;

	public Point(DArray a, int l, int x, int y) {
		this.a = a;
		this.l = l;
		this.x = x;
		this.y = y;
	}

	public Point(DArray a, int l, int x, int y, int d) {
		int px = -1;
		int py = -1;
		switch (d) {
		case 0:
			px = x * 2 + 2;
			py = y + (x + 1) % 2;
			break;
		case 1:
			px = x * 2 + 1;
			py = y;
			break;
		case 2:
			px = x * 2;
			py = y;
			break;
		case 3:
			px = x * 2 - 1;
			py = y + (x + 1) % 2;
			break;
		case 4:
			px = x * 2;
			py = y + 1;
			break;
		case 5:
			px = x * 2;
			py = y + 1;
			break;
		}
		this.a = a;
		this.l = l;
		this.x = px;
		this.y = py;
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

	public Point step(int d) {
		if (x % 2 == (d + 1) % 2)
			throw new RuntimeException("Incorrect direction");
		int px = x;
		int py = y;
		switch (d) {
		case 0:
			px += 1;
			break;
		case 1:
			px += 1;
			py -= (x / 2) % 2;
			break;
		case 2:
			px -= 1;
			py -= (x / 2) % 2;
			break;
		case 3:
			px -= 1;
			break;
		case 4:
			px -= 1;
			py += (x / 2 + 1) % 2;
			break;
		case 5:
			px += 1;
			py += (x / 2 + 1) % 2;
			break;
		}
		return new Point(a, l, px, py);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
