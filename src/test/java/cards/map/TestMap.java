package cards.map;

import org.junit.Test;

import com.betterbe.memorydb.file.DBParser;
import com.betterbe.memorydb.structure.Store;

import cards.record.Game;

public class TestMap {
	@Test
	public void testMap() {
		Store store = new Store(20);
		Game game = new Game(store);
		game.parse(new DBParser("testMap.txt"));
	}
}
