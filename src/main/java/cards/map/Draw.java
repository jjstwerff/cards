package cards.map;

import cards.record.Area;
import cards.record.Map;
import cards.record.Map.DArray;

public class Draw {
	private final Area area;
	static int px = 15; // sixth of distance between each line
	static int py = 26; // better 25,98 distance between half rows
	static int[] Dx = new int[] { 4, 3, 2, 0, -2, -3, -4, -3, -2, 0, 2, 3 }; // directions x
	static int[] Dy = new int[] { 0, -1, -2, -2, -2, -1, 0, 1, 2, 2, 2, 1 }; // directions y
	static int[] dx = new int[] { px * 4, px * 2, -px * 2, -px * 4, -px * 2, px * 2, 0 };
	static int[] dy = new int[] { 0, -py * 2, -py * 2, 0, py * 2, py * 2, 0 };

	public Draw(Area area) {
		this.area = area;
	}

	public Area getArea() {
		return area;
	}

	private int px(int x, int y, int d) {
		return 60 + x * 6 * px + dx[d];
	}

	private int py(int x, int y, int d) {
		return 104 + (y * 2 - x % 2) * py * 2 + dy[d];
	}

	private int px(int v) {
		return 60 + v * px;
	}

	/** Find the default Y position of a hex tile */
	private int py(int v) {
		return 104 + v * py;
	}

	/** Find the wall material on a tile */
	public int wall(DArray a, int l, int x, int y) {
		int ax = (x + (y % 4 == 1 ? 6 : 0)) / 6;
		int ay = (y + 2 + 2 * ((x / 6) % 2)) / 4;
		int mx = ax * 6;
		int my = ay * 4 - (ax % 2) * 2;
		int dx = x - mx;
		int dy = y - my;
		System.out.println("(" + x + "," + y + ") a=(" + ax + "," + ay + ") m=(" + mx + "," + my + ") d=(" + dx + "," + dy + ")");
		/*
		boolean R = x - mx == 
		boolean T = (x + 6) % 6 == 0 && (y + 2) % 4 == 0;
		boolean L = (x + 6) % 6 == 3 && (y + 2) % 4 == 1;
		
		switch (d) {
		case 0:
			a.setIndex(y * l + x);
			return a.getR();
		case 1:
			a.setIndex(y * l + x);
			return a.getT();
		case 2:
			a.setIndex(y * l + x);
			return a.getL();
		case 3:
			if (x > 0) {
				a.setIndex((x % 2 == 0 ? y + 1 : y) * l + x - 1);
				return a.getR();
			}
		case 4:
			a.setIndex((y + 1) * l + x);
			return a.getT();
		case 5:
			a.setIndex((x % 2 == 0 ? y + 1 : y) * l + x + 1);
			return a.getL();
		}*/
		return 0;
	}

	/*
	private int directions(DArray a, int l, int x, int y, int d) {
		System.out.println("directions (" + x + "," + y + ":" + d + ")\n");
		int r = 0;
		if (wall(a, l, x, y, d) == 2) {
			System.out.println("first " + (d + 2) % 6 + "\n");
		}
		if (wall(a, l, x, y, (d + 5) % 6) == 2) {
			System.out.println("first " + (d + 4) % 6 + "\n");
		}
		if (wall(a, l, x, y, (d + 5) % 6) == 2) {
			System.out.println("first " + (d + 4) % 6 + "\n");
		}
		return r;
	}*/

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
