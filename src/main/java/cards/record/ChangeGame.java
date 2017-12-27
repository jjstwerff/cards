package cards.record;

import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Game
 */
public class ChangeGame extends Game implements AutoCloseable {
	public ChangeGame(Store store) {
		super(store, store.allocate(Game.SIZE));
		setName(null);
		store.setInt(rec, 8, 0);
		setRules(null);
	}

	public ChangeGame(Game current) {
		super(current.store, current.getRec());
		new IndexGameName().remove(getRec());
	}

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 4));
	}

	public void setName(String value) {
		store.setInt(rec, 4, store.putString(value));
	}

	public IndexAreas getAreas() {
		return new IndexAreas(new Area(store));
	}

	public void getRules(Rules value) {
		value.setRec(store.getInt(rec, 12));
	}

	public Rules getRules() {
		return new Rules(store, rec == 0 ? 0 : store.getInt(rec, 12));
	}

	public void setRules(Rules value) {
		store.setInt(rec, 12, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() throws Exception {
		new IndexGameName().insert(getRec());
	}
}