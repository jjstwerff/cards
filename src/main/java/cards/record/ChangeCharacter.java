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

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 0));
	}

	public void setName(String value) {
		store.setInt(rec, 0, store.putString(value));
	}

	public IndexSkills getSkills() {
		return new IndexSkills(new Skill(store));
	}

	@Override
	public void close() throws Exception {
	}
}