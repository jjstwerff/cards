package cards.record;

import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Game
 */
public class ChangeGame extends Game implements AutoCloseable {
	public ChangeGame(Store store) {
		super(store, store.allocate(Game.SIZE));
		setName(null);
		store.setInt(rec, 8, 0); // SET areaName
		store.setInt(rec, 12, 0); // ARRAY areas
		store.setInt(rec, 16, 0);
		setRules(null);
		store.setInt(rec, 24, 0); // SET characters
		store.setInt(rec, 28, 0); // ARRAY walls
		store.setInt(rec, 32, 0);
		store.setInt(rec, 36, 0); // ARRAY floors
		store.setInt(rec, 40, 0);
		store.setInt(rec, 44, 0); // SET items
		store.setInt(rec, 48, 0); // SET materials
	}

	public ChangeGame(Game current) {
		super(current.store, current.getRec());
		new IndexGameName().remove(getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 4, store.putString(value));
	}

	public void setRules(Rules value) {
		store.setInt(rec, 20, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() {
		new IndexGameName().insert(getRec());
	}
}