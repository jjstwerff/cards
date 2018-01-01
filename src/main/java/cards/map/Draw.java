package cards.map;

import cards.record.Area;
import cards.record.Map;
import cards.record.Map.DArray;

public class Draw {
	private final Area area;
	public static final int PX = 15; // sixth of distance between each line
	public static final int PY = 26; // better 25,98 distance between half rows
	public static final int SX = 60; // start of x to prevent negative numbers
	public static final int SY = 104; // start of y to prevent negative numbers
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

	/*
	 * s: same as one step back
	 * o: other as one step back
	 * !: match
	 * indirect wobble: so!os!
	 * direct wobble: s!s!  or ss!ss!
	 * normalize.. always start with an s when possible
	 * return move direction: 0 - 11 in 30 degrees steps
	 */
	public int found(Point p, StringBuilder s, int wall) {
		int rd = -1;
		int c = 0;
		int d0 = -1;
		int d1 = -1;
		for (int d = p.x % 2; d < 6; d += 2) {
			if (p.match(wall, d)) {
				c++;
				if (d0 == -1)
					d0 = d;
				else if (d1 == -1)
					d1 = d;
			}
		}
		if (c == 0) {
			s.append("@");
		} else if (c == 1) {
			s.append(".");
		} else if (c == 2) {
			boolean same = true;
			Point f = new Point(p);
			f.step(d0);
			steps(f, s, wall, (3 + d0) % 6, (3 + d1) % 6, 0);
			if (s.charAt(0) != 's') {
				same = false;
				s.setLength(0);
			}
			int at = s.length();
			Point n = new Point(p);
			n.step(d1);
			steps(n, s, wall, (3 + d1) % 6, (3 + d0) % 6, 0);
			rd = (d1 + d0 + 12) % 12 + (s.charAt(at) == 's' ? same ? 0 : 1 : -1);
			if (!same) {
				f = new Point(p);
				f.step(d0);
				steps(f, s, wall, (3 + d0) % 6, (3 + d1) % 6, 0);
			}
		} else {
			// nothing yet on 3 directions
		}
		return rd;
	}

	private void steps(Point p, StringBuilder s, int wall, int dwalk, int doth, int step) {
		if (step == 3) {
			s.append("!");
			return;
		}
		int c = 0;
		int d0 = -1;
		for (int d = p.x % 2; d < 6; d += 2) {
			if (d == dwalk)
				continue;
			if (p.match(wall, d)) {
				c++;
				if (d0 == -1)
					d0 = d;
			}
		}
		if (c == 0)
			s.append(".");
		else if (c == 2)
			s.append("k");
		else {
			s.append(d0 == doth ? "s" : "o");
			Point n = new Point(p);
			n.step(d0);
			steps(n, s, wall, (3 + d0) % 6, (3 + dwalk) % 6, step + 1);
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
					bld.append(modLine(map, sx + x, sy + y, 0, 1));
				if (el.getT() == 2)
					bld.append(modLine(map, sx + x, sy + y, 1, 2));
				if (el.getL() == 2)
					bld.append(modLine(map, sx + x, sy + y, 2, 3));
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

	private String modLine(Map map, int x, int y, int sd, int ed) {
		return "c.beginPath(); c.moveTo(" + px(x, y, sd) + ", " + py(x, y, sd) + "); c.lineTo(" + px(x, y, ed) + ", " + py(x, y, ed) + "); c.stroke();\n";
	}
}
