package cards.map;

import cards.record.Area;
import cards.record.Map;
import cards.record.Map.DArray;

public class Draw {
	public static final int PX = 15; // sixth of distance between each line
	public static final int PY = 26; // better 25,98 distance between half rows
	public static final double HX = 22.5167;
	public static final int HY = 13;
	public static final int SX = 60; // start of x to prevent negative numbers
	public static final int SY = 104; // start of y to prevent negative numbers
	static final int[] DX = new int[] { PX * 4, PX * 2, -PX * 2, -PX * 4, -PX * 2, PX * 2, 0 };
	static final int[] DY = new int[] { 0, -PY * 2, -PY * 2, 0, PY * 2, PY * 2, 0 };
	static final double[] MX = new double[] { 0, PX, HX, 0.5 * PX, 0, -0.5 * PX, -HX, -PX, -HX, -0.5 * PX, 0, 0.5 * PX, HX };
	static final double[] MY = new double[] { 0, 0, -HY, -0.5 * PY, -PY, -0.5 * PY, -HY, 0, HY, 0.5 * PY, PY, 0.5 * PY, HY };
	private final Area area;
	private final StringBuilder bld;

	public Draw(Area area) {
		this.area = area;
		this.bld = new StringBuilder();
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
	public int moveDir(Point p, int wall) {
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
		if (c == 2) {
			boolean s0 = next(p.step(d0), wall, (3 + d0) % 6, (3 + d1) % 6);
			boolean s1 = next(p.step(d1), wall, (3 + d1) % 6, (3 + d0) % 6);
			int ca = d0 + d1;
			if (d0 == 0 && d1 == 4)
				ca = 8;
			if (d0 == 1 && d1 == 5)
				ca = 0;
			rd = ca + (s1 ? s0 ? 0 : 1 : -1);
		} else {
			// nothing yet on 3 directions
		}
		return rd;
	}

	private boolean next(Point p, int wall, int dwalk, int doth) {
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
		return c == 1 && d0 == doth;
	}

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
			steps(p.step(d0), s, wall, (3 + d0) % 6, (3 + d1) % 6, 0);
			if (s.charAt(0) != 's') {
				same = false;
				s.setLength(0);
			}
			int at = s.length();
			steps(p.step(d1), s, wall, (3 + d1) % 6, (3 + d0) % 6, 0);
			int ca = d0 + d1;
			if (d1 == 4 && d0 == 0)
				ca = 8;
			rd = ca + (s.charAt(at) == 's' ? same ? 0 : 1 : -1);
			if (!same)
				steps(p.step(d0), s, wall, (3 + d0) % 6, (3 + d1) % 6, 0);
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
			steps(p.step(d0), s, wall, (3 + d0) % 6, (3 + dwalk) % 6, step + 1);
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
		Point f = new Point(map.getD(), map.getL(), x, y, sd);
		Point t = new Point(map.getD(), map.getL(), x, y, ed);
		bld.setLength(0);
		int df = moveDir(f, 2) + 1;
		bld.setLength(0);
		int dt = moveDir(t, 2) + 1;
		return "c.beginPath(); c.moveTo(" + (f.rx() + MX[df]) + ", " + (f.ry() + MY[df]) + "); c.lineTo(" + (t.rx() + MX[dt]) + ", " + (t.ry() + MY[dt]) + "); c.stroke();\n" //
				+ "c.beginPath(); c.arc(" + (f.rx() + MX[df]) + ", " + (f.ry() + MY[df]) + ", 5, 0, 2 * Math.PI); c.stroke();\n" //
				+ "c.beginPath(); c.arc(" + (t.rx() + MX[dt]) + ", " + (t.ry() + MY[dt]) + ", 5, 0, 2 * Math.PI); c.stroke();\n";
	}
}
