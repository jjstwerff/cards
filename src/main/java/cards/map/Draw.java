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
	static final byte[] match = new byte[32];
	// max 8 steps in 2 directions
	//  2 bytes per step: 0=direction 1=0:end 1:same 2:other 3:cross

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

	public String show(Point p, int wall) {
		match(p, wall);
		StringBuilder bld = new StringBuilder();
		Dir: for (int d = 0; d < 32; d += 16) {
			for (int i = 1; i < 14; i += 2) {
				switch (match[i + d]) {
				case 0:
					bld.append(".!");
					continue Dir;
				case 1:
					bld.append("s");
					break;
				case 2:
					bld.append("o");
					break;
				case 3:
					bld.append("c!");
					continue Dir;
				}
			}
			bld.append("!");
		}
		return bld.toString();
	}

	private int match(Point p, int wall) {
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
			step(p, 0, wall, d0, (3 + d1) % 6);
			step(p, 16, wall, d1, (3 + d0) % 6);
		}
		return c;
	}

	private void step(Point p, int i, int wall, int curD, int lastD) {
		Point n = p.step(curD);
		if (i % 16 == 14)
			return;
		int c = 0;
		int d0 = -1;
		for (int d = n.x % 2; d < 6; d += 2) {
			if (d == (curD + 3) % 6) // is this direction back the last move?
				continue;
			if (n.match(wall, d)) {
				c++;
				if (d0 == -1)
					d0 = d;
			}
		}
		match[i] = (byte) curD;
		if (c == 0) {
			match[i + 1] = (byte) 0;
		} else if (c == 1) {
			match[i + 1] = (byte) (d0 == lastD ? 1 : 2);
			step(n, i + 2, wall, d0, curD);
		} else if (c == 2)
			match[i + 1] = (byte) 3;
	}

	public P moveDir(Point p, int wall) {
		int c = match(p, wall);
		if (c == 2) {
			if (match[1] == 1 && match[3] == 1 && ((match[17] == 2 && match[19] == 2) || match[17] == 3))
				match[17] = 1;
			if (match[17] == 1 && match[19] == 1 && ((match[1] == 2 && match[3] == 2) || match[1] == 3))
				match[1] = 1;
			if ((match[1] == 1 || match[1] == 2) && (match[17] == 1 || match[17] == 2)) {
				int rd = calcDir(match[0], match[1] == 1, match[16], match[17] == 1);
				lastMove = rd;
				return new P(p.rx() + MX[rd], p.ry() + MY[rd]);
			}
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
		lastMove = -1;
		return new P(p.rx(), p.ry());
	}

	public P moveDirOld(Point p, int wall) {
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
			Point st0 = p.step(d0);
			lastMove = d0;
			Point st1 = next(st0, wall);
			int s0 = lastMove;
			boolean s0s = s0 == (3 + d1) % 6;
			next(st1, wall);
			int s1 = lastMove;
			boolean s1s = s1 == d0;

			Point nt0 = p.step(d1);
			lastMove = d1;
			Point nt1 = next(nt0, wall);
			int n0 = lastMove;
			boolean n0s = n0 == (3 + d0) % 6;
			next(nt1, wall);
			int n1 = lastMove;
			boolean n1s = n1 == d1;

			if (s0s && s1s && ((!n0s && !n1s) || n0 == -2))
				n0s = true;
			if (n0s && n1s && ((!s0s && !s1s) || s0 == -2))
				s0s = true;
			rd = calcDir(d0, s0s, d1, n0s);
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

	/**
	 * lastMove becomes:
	 * -2 : encountered cross
	 * -1 : encountered end
	 * ?? : direction of move
	 * return: next step Point
	 */
	private Point next(Point p, int wall) {
		int c = 0;
		int d0 = -1;
		for (int d = p.x % 2; d < 6; d += 2) {
			if (lastMove >= 0 && d == (lastMove + 3) % 6) // is this direction back the last move?
				continue;
			if (p.match(wall, d)) {
				c++;
				if (d0 == -1)
					d0 = d;
			}
		}
		if (c == 1) {
			lastMove = d0;
			return p.step(d0);
		}
		if (c == 2)
			lastMove = -2;
		else
			lastMove = -1;
		return p;
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
