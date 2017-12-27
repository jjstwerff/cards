package cards.record;

import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Rules
 */
public class ChangeRules extends Rules implements AutoCloseable {
	public ChangeRules(Store store) {
		super(store, store.allocate(Rules.SIZE));
		setName(null);
		store.setInt(rec, 8, 0);
		store.setInt(rec, 12, 0);
		store.setInt(rec, 16, 0);
	}

	public ChangeRules(Rules current) {
		super(current.store, current.getRec());
		new IndexRulesName().remove(getRec());
	}

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 4));
	}

	public void setName(String value) {
		store.setInt(rec, 4, store.putString(value));
	}

	public IndexRaces getRaces() {
		return new IndexRaces(new Race(store));
	}

	public CardsArray getCards() {
		return new CardsArray();
	}

	@Override
	public void close() throws Exception {
		new IndexRulesName().insert(getRec());
	}
}