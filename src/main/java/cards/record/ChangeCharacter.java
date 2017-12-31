package cards.record;

/**
 * Automatically generated record class for table Character
 */
public class ChangeCharacter extends Character implements AutoCloseable {
	private final Game parent;

	public ChangeCharacter(Game parent) {
		super(parent.store, parent.store.allocate(Character.SIZE));
		this.parent = parent;
		setName(null);
		store.setInt(rec, 8, 0); // SET skills
		setUpRecord(null);
		setUpRecord(parent);
	}

	public ChangeCharacter(Character current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexCharacters(this).remove(getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 4, store.putString(value));
	}

	public void setUpRecord(Game value) {
		store.setInt(rec, 21, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() {
		parent.new IndexCharacters(this).insert(getRec());
	}
}