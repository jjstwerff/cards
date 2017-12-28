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
		store.setInt(rec, 12, 0);
		store.setInt(rec, 16, 0);
		store.setInt(rec, 20, 0);
		store.setInt(rec, 24, 0);
		store.setInt(rec, 28, 0);
		setUpRecord(null);
		setUpRecord(parent);
	}

	public ChangeArea(Area current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexAreas(this).remove(getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 8, store.putString(value));
	}

	public void setUpRecord(Game value) {
		store.setInt(rec, 41, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() {
		parent.new IndexAreas(this).insert(getRec());
	}
}