package cards.record;

import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Game
 */
public class ChangeGame extends Game implements AutoCloseable {
	public ChangeGame(Store store) {
		super(store, store.allocate(Game.SIZE));
		setName(null);
		store.setInt(rec, 8, 0); // SET areas
		setRules(null);
		store.setInt(rec, 16, 0); // SET characters
	}

	public ChangeGame(Game current) {
		super(current.store, current.getRec());
		new IndexGameName().remove(getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 4, store.putString(value));
	}

	public void setRules(Rules value) {
		store.setInt(rec, 12, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() {
		new IndexGameName().insert(getRec());
	}
}