package cards.map;

import org.junit.Test;

import com.betterbe.memorydb.file.DBParser;
import com.betterbe.memorydb.meta.Tests;
import com.betterbe.memorydb.structure.Store;

import cards.record.Game;
import cards.record.Map;
import cards.record.Map.DArray;
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
	}

	@Test
	public void testConvert() {
		Store store = new Store(20);
		Game game = new Game(store);
		game.parse(new DBParser(resource(this, "testMap.txt")));
		game = game.new IndexGameName().iterator().next();
		Assert.assertEquals(content(this, "testMap2.txt"), game.toString());

		Draw draw = new Draw(game.getAreas().next());
		Map map = draw.getArea().getMaps().next();
		DArray d = map.getD();
		draw.wall(d, map.getL(), 3, 1);
		draw.wall(d, map.getL(), 6, 0);
		draw.wall(d, map.getL(), 3, 3);
		draw.wall(d, map.getL(), 0, 2);		
		draw.wall(d, map.getL(), 9, 1);
		draw.wall(d, map.getL(), 9, 3);
	}
}
