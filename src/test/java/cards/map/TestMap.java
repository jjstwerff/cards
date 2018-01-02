package cards.map;

import org.junit.Test;

import com.betterbe.memorydb.file.DBParser;
import com.betterbe.memorydb.meta.Tests;
import com.betterbe.memorydb.structure.Store;

import cards.record.Area;
import cards.record.Game;
import cards.record.Map;
import junit.framework.Assert;

public class TestMap extends Tests {
	@Test
	public void testMap() {
		Store store = new Store(20);
		Game game = new Game(store);
		game.parse(new DBParser(resource(this, "testMap.txt")));
		game = game.new IndexGameName().iterator().next();
		Assert.assertEquals(content(this, "testMap2.txt"), game.toString());

		Draw draw = new Draw(game.getAreas().next());

		Area area = game.getAreas().next();
		Map map = area.getMaps().next();
		Assert.assertEquals("", found(draw, map, 2, 1));
		Assert.assertEquals("sscc1", found(draw, map, 1, 1));
		Assert.assertEquals("sso!sso!0", found(draw, map, 1, 6));
		Assert.assertEquals("ssccb", found(draw, map, 1, 2));
		Assert.assertEquals("scoso!9", found(draw, map, 4, 0));
		Assert.assertEquals("sos!oso!3", found(draw, map, 6, 1));
		Assert.assertEquals("scsc6", found(draw, map, 2, 2));
		Assert.assertEquals("@", found(draw, map, 3, 2));
		Assert.assertEquals(".", found(draw, map, 1, 0));
		Assert.assertEquals("sos!c3", found(draw, map, 3, 1));
		Assert.assertEquals("sos!c3", found(draw, map, 10, 1));

		Assert.assertEquals(0, moveDir(draw, map, 1, 1));
		Assert.assertEquals(0, moveDir(draw, map, 1, 6));
		Assert.assertEquals(0, moveDir(draw, map, 1, 2));
		Assert.assertEquals(9, moveDir(draw, map, 4, 0));
		Assert.assertEquals(3, moveDir(draw, map, 6, 1));
		Assert.assertEquals(6, moveDir(draw, map, 2, 2));
		Assert.assertEquals(-1, moveDir(draw, map, 3, 2));
		Assert.assertEquals(-2, moveDir(draw, map, 1, 0));
		Assert.assertEquals(3, moveDir(draw, map, 3, 1));
		Assert.assertEquals(3, moveDir(draw, map, 10, 1));

		Assert.assertEquals(content(this, "testMap.html"), draw.dump());
	}

	private String found(Draw draw, Map map, int x, int y) {
		StringBuilder bld = new StringBuilder();
		Point p = new Point(map.getD(), map.getL(), x, y);
		int found = draw.found(p, bld, 2);
		if (found != -1)
			bld.append(Integer.toHexString(found));
		return bld.toString();
	}

	private int moveDir(Draw draw, Map map, int x, int y) {
		Point p = new Point(map.getD(), map.getL(), x, y);
		draw.moveDir(p, 2);
		return draw.getLastMove();
	}
}
