package cards.record;

/**
 * Automatically generated record class for table Area
 */
public class ChangeArea extends Area implements AutoCloseable {
	private final Game parent;

	public ChangeArea(Game parent) {
		super(parent.store, parent.store.allocate(Area.SIZE));
		this.parent = parent;
		setName(null);
		store.setInt(rec, 8, 0); // SET rooms
		store.setInt(rec, 12, 0); // ARRAY encounter
		store.setInt(rec, 16, 0);
		store.setInt(rec, 20, 0); // SET goal
		store.setInt(rec, 24, 0); // SET maps
		setUpRecord(null);
		setUpRecord(parent);
	}

	public ChangeArea(Area current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexAreas(this).remove(getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 4, store.putString(value));
	}

	public void setUpRecord(Game value) {
		store.setInt(rec, 37, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() {
		parent.new IndexAreas(this).insert(getRec());
	}
}