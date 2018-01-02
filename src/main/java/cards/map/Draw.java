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
	static final double[] MX = new double[] { PX, HX, 0.5 * PX, 0, -0.5 * PX, -HX, -PX, -HX, -0.5 * PX, 0, 0.5 * PX, HX };
	static final double[] MY = new double[] { 0, -HY, -0.5 * PY, -PY, -0.5 * PY, -HY, 0, HY, 0.5 * PY, PY, 0.5 * PY, HY };
	private final Area area;
	int lastMove;

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

	public P moveDir(Point p, int wall) {
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
			int s0 = next(p.step(d0), wall, (3 + d0) % 6, (3 + d1) % 6);
			int s1 = next(p.step(d1), wall, (3 + d1) % 6, (3 + d0) % 6);
			if (s0 == 1 && s1 == 2 && next(p.step(d0).step((3 + d1) % 6), wall, d1, d0) == 1)
				s1 = 1;
			if (s1 == 1 && s0 == 2 && next(p.step(d1).step((3 + d0) % 6), wall, d0, d1) == 1)
				s0 = 1;
			rd = calcDir(d0, s0 == 1, d1, s1 == 1);
		} else if (c == 3) { // move 3 direction towards the loose end
			for (int d = p.x % 2; d < 6; d += 2) {
				Point n = p.step(d);
				c = 0;
				for (int nd = n.x % 2; nd < 6; nd += 2)
					if (n.match(wall, nd))
						c++;
				if (c == 1) {
					lastMove = 2 * d;
					return new P(p.rx() + 2 * MX[2 * d], p.ry() + 2 * MY[2 * d]);
				}
			}
		} else if (c == 1) { // discard loose ends
			lastMove = -2;
			return null;
		}
		lastMove = rd;
		if (rd == -1)
			return new P(p.rx(), p.ry());
		return new P(p.rx() + MX[rd], p.ry() + MY[rd]);
	}

	private int calcDir(int d0, boolean s0, int d1, boolean s1) {
		int ca = d0 + d1;
		if (d0 == 0 && d1 == 4)
			ca = 8; // TODO inconsistent.. should have been 10
		if (d0 == 1 && d1 == 5)
			ca = 12;
		return (ca + (s1 ? s0 ? 0 : 1 : -1)) % 12;
	}

	private int next(Point p, int wall, int dwalk, int doth) {
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
		if (c == 2)
			return 2;
		return c == 1 && d0 == doth ? 1 : 0;
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
			steps(p.step(d0), s, wall, (3 + d0) % 6, (3 + d1) % 6, 0);
			if (s.charAt(0) != 's') {
				same = false;
				s.setLength(0);
			}
			int at = s.length();
			steps(p.step(d1), s, wall, (3 + d1) % 6, (3 + d0) % 6, 0);
			rd = calcDir(d0, same, d1, s.charAt(at) == 's');
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
			s.append("c");
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
		P mf = moveDir(f, 2);
		int df = lastMove;
		P mt = moveDir(t, 2);
		int dt = lastMove;
		if (mf == null || mt == null)
			return "";
		return "c.beginPath(); c.moveTo(" + mf.x + ", " + mf.y + "); c.lineTo(" + mt.x + ", " + mt.y + "); c.stroke();\n" //
				+ "c.beginPath(); c.arc(" + mf.x + ", " + mf.y + ", 5, 0, 2 * Math.PI); c.stroke();\n" //
				+ "c.beginPath(); c.arc(" + mt.x + ", " + mt.y + ", 5, 0, 2 * Math.PI); c.stroke();\n" //
				+ "c.fillText(\"" + df + "\", " + (mf.x + 3) + ", " + (mf.y - 5) + ");\n" //
				+ "c.fillText(\"" + dt + "\", " + (mt.x + 3) + ", " + (mt.y - 5) + ");\n";
	}

	public int getLastMove() {
		return lastMove;
	}
}
