package cards.map;

import cards.record.Area;
import cards.record.Map;
import cards.record.Map.DArray;

public class Draw {
	private final Area area;
	static final int PX = 15; // sixth of distance between each line
	static final int PY = 26; // better 25,98 distance between half rows
	static final int SX = 60; // start of x to prevent negative numbers
	static final int SY = 104; // start of y to prevent negative numbers
	static final int[] DX = new int[] { PX * 4, PX * 2, -PX * 2, -PX * 4, -PX * 2, PX * 2, 0 };
	static final int[] DY = new int[] { 0, -PY * 2, -PY * 2, 0, PY * 2, PY * 2, 0 };

	public Draw(Area area) {
		this.area = area;
	}

	public Area getArea() {
		return area;
	}

	private int px(int x, int y, int d) {
		return SX + x * 6 * PX + DX[d];
	}

	private int py(int x, int y, int d) {
		return SY + (y * 2 - x % 2) * PY * 2 + DY[d];
	}

	int rx(int x, int y) {
		return SX + x * 6 * PX;
	}

	int ry(int x, int y) {
		return SY + (y * 2 - x % 2) * PY * 2;
	}

	static class Point {
		DArray a;
		int l;
		int x;
		int y;

		Point(DArray a, int l, int x, int y) {
			this.a = a;
			this.l = l;
			this.x = x;
			this.y = y;
		}

		public int rx() {
			return SX - 2 * PX + (x * 3 + x % 2) * PX;
		}

		public int ry() {
			return SY - 2 * PY + (y * 2 - (x / 2) % 2) * 2 * PY;
		}

		public int match(int wall) {
			int r = 0;
			int mx = x / 2;
			int my = y;
			if (x % 2 == 0) {
				a.setIndex(mx + my * l);
				if (a.getT() == wall)
					r += 1;
				if (a.getL() == wall)
					r += 16;
				a.setIndex(mx - 1 + (my - mx % 2) * l);
				if (a.getR() == wall)
					r += 4;
			} else {
				a.setIndex(mx + my * l);
				if (a.getT() == wall)
					r += 8;
				if (a.getR() == wall)
					r += 32;
				a.setIndex(mx + 1 + (my - mx % 2) * l);
				if (a.getL() == wall)
					r += 2;
			}
			return r;
		}

		public void step(int d) {
			if (x % 2 == (d + 1) % 2)
				throw new RuntimeException("Incorrect direction");
			switch (d) {
			case 0:
				x += 1;
				break;
			case 1:
				x += 1;
				y -= (x / 2) % 2;
				break;
			case 2:
				x -= 1;
				y -= (x / 2) % 2;
				break;
			case 3:
				x -= 1;
				break;
			case 4:
				x -= 1;
				y += (x / 2 + 1) % 2;
				break;
			case 5:
				x += 1;
				y += (x / 2 + 1) % 2;
				break;
			}
		}
	}

	public String dump() {
		StringBuilder bld = new StringBuilder();
		bld.append("<!DOCTYPE html>\n");
		bld.append("<html>\n<head>\n  <title>" + area.getName() + "</title>\n</head>\n<body>\n");
		bld.append("<canvas id=\"area\" width=\"1280\" height=\"1024\" style=\"border:1px solid #d3d3d3;\"></canvas>\n");
		bld.append("<script>\n");
		bld.append("var a = document.getElementById(\"area\");\n");
		bld.append("var c = a.getContext(\"2d\");\n");
		bld.append("c.lineWidth=0.3;\n");
		for (Map map : area.getMaps()) {
			int sx = map.getX();
			int sy = map.getY();
			int l = map.getL();
			int x = 0;
			int y = 0;
			for (DArray el : map.getD()) {
				if (el.getR() == 2)
					bld.append(line(sx + x, sy + y, 0, 1));
				if (el.getT() == 2)
					bld.append(line(sx + x, sy + y, 1, 2));
				if (el.getL() == 2)
					bld.append(line(sx + x, sy + y, 2, 3));
				x += 1;
				if (x >= l) {
					x = 0;
					y += 1;
				}
			}
		}
		bld.append("c.lineWidth=1;\n");
		for (Map map : area.getMaps()) {
			int sx = map.getX();
			int sy = map.getY();
			int l = map.getL();
			int x = 0;
			int y = 0;
			for (DArray el : map.getD()) {
				if (el.getR() == 2)
					bld.append(modLine(sx + x, sy + y, 0, 1));
				if (el.getT() == 2)
					bld.append(modLine(sx + x, sy + y, 1, 2));
				if (el.getL() == 2)
					bld.append(modLine(sx + x, sy + y, 2, 3));
				x += 1;
				if (x >= l) {
					x = 0;
					y += 1;
				}
			}
		}
		bld.append("</script>\n");
		bld.append("</body>\n</html>\n");
		return bld.toString();
	}

	private String line(int x, int y, int sd, int ed) {
		return "c.beginPath(); c.moveTo(" + px(x, y, sd) + ", " + py(x, y, sd) + "); c.lineTo(" + px(x, y, ed) + ", " + py(x, y, ed) + "); c.stroke();\n";
	}

	private String modLine(int x, int y, int sd, int ed) {
		return "c.beginPath(); c.moveTo(" + px(x, y, sd) + ", " + py(x, y, sd) + "); c.lineTo(" + px(x, y, ed) + ", " + py(x, y, ed) + "); c.stroke();\n";
	}
}
