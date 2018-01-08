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
		try (DBParser parser = new DBParser(resource(this, "testMap.txt"))) {
			game.parse(parser);
		}
		game = game.new IndexGameName().iterator().next();
		Assert.assertEquals(content(this, "testMap2.txt"), game.toString());

		Draw draw = new Draw(game.getAreas().next());

		Area area = game.getAreas().next();
		Map map = area.getMaps().next();
		Assert.assertEquals("ossssoo!osososo!2", show(draw, map, 11, 8));
		Assert.assertEquals("osososo!ossssoo!8", show(draw, map, 2, 5));
		Assert.assertEquals("oososos!ssssoos!0", show(draw, map, 1, 5));
		Assert.assertEquals("c!c!4", show(draw, map, 2, 1));
		Assert.assertEquals("c!ssc!0", show(draw, map, 1, 1));
		Assert.assertEquals("ssoosos!ssoosos!0", show(draw, map, 1, 6));
		Assert.assertEquals("ssc!c!0", show(draw, map, 1, 2));
		Assert.assertEquals("osososc!sc!9", show(draw, map, 4, 0));
		Assert.assertEquals("ososc!sosc!3", show(draw, map, 6, 1));
		Assert.assertEquals("sc!sc!6", show(draw, map, 2, 2));
		Assert.assertEquals("!!-1", show(draw, map, 3, 2));
		Assert.assertEquals("c!.!-2", show(draw, map, 1, 0));
		Assert.assertEquals("sososos!c!3", show(draw, map, 3, 1));
		Assert.assertEquals("c!sososos!3", show(draw, map, 10, 1));

		Assert.assertEquals(2, moveDir(draw, map, 11, 8));
		Assert.assertEquals(8, moveDir(draw, map, 2, 5));
		Assert.assertEquals(0, moveDir(draw, map, 1, 5));
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

	private String show(Draw draw, Map map, int x, int y) {
		return draw.show(new Point(map.getD(), map.getL(), x, y), 2);
	}

	private int moveDir(Draw draw, Map map, int x, int y) {
		draw.moveDir(new Point(map.getD(), map.getL(), x, y), 2);
		return draw.getLastMove();
	}
}
