package cards.record;

import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Rules
 */
public class ChangeRules extends Rules implements AutoCloseable {
	public ChangeRules(Store store) {
		super(store, store.allocate(Rules.SIZE));
		setName(null);
		store.setInt(rec, 8, 0); // SET races
		store.setInt(rec, 12, 0); // SET cards
	}

	public ChangeRules(Rules current) {
		super(current.store, current.getRec());
		new IndexRulesName().remove(getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 4, store.putString(value));
	}

	@Override
	public void close() {
		new IndexRulesName().insert(getRec());
	}
}