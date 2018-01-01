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
		Assert.assertEquals(content(this, "testMap.html"), draw.dump());

		Area area = game.getAreas().next();
		Map map = area.getMaps().next();
		Assert.assertEquals("sos!oso!3", found(draw, map, 6, 1));
		Assert.assertEquals("sksk6", found(draw, map, 2, 2));
		Assert.assertEquals("@", found(draw, map, 3, 2));
		Assert.assertEquals(".", found(draw, map, 1, 0));
		Assert.assertEquals("sos!k3", found(draw, map, 3, 1));
		Assert.assertEquals("sos!k3", found(draw, map, 10, 1));
	}

	private String found(Draw draw, Map map, int x, int y) {
		StringBuilder bld = new StringBuilder();
		Point p = new Point(map.getD(), map.getL(), x, y);
		int found = draw.found(p, bld, 2);
		if (found != -1)
			bld.append(Integer.toHexString(found));
		return bld.toString();
	}
}
