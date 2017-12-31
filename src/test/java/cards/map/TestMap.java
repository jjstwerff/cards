package cards.map;

import org.junit.Test;

import com.betterbe.memorydb.file.DBParser;
import com.betterbe.memorydb.meta.Tests;
import com.betterbe.memorydb.structure.Store;

import cards.record.Game;
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
}
