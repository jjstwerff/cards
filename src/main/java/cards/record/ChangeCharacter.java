package cards.record;

import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Character
 */
public class ChangeCharacter extends Character implements AutoCloseable {
	public ChangeCharacter(Store store) {
		super(store, store.allocate(Character.SIZE));
		setName(null);
		store.setInt(rec, 4, 0);
	}

	public ChangeCharacter(Character current) {
		super(current.store, current.getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 0, store.putString(value));
	}

	@Override
	public void close() {
	}
}