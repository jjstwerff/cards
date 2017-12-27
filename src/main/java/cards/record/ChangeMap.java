package cards.record;

/**
 * Automatically generated record class for table Map
 */
public class ChangeMap extends Map implements AutoCloseable {
	private final Area parent;

	public ChangeMap(Area parent) {
		super(parent.store, parent.store.allocate(Map.SIZE));
		this.parent = parent;
		setX(0);
		setY(0);
		setZ(0);
		store.setInt(rec, 20, 0);
		store.setInt(rec, 24, 0);
		setUpRecord(null);
		setUpRecord(parent);
	}

	public ChangeMap(Map current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexMaps(this).remove(getRec());
	}

	public void setX(int value) {
		store.setInt(rec, 8, value);
	}

	public void setY(int value) {
		store.setInt(rec, 12, value);
	}

	public void setZ(int value) {
		store.setInt(rec, 16, value);
	}

	public void setUpRecord(Area value) {
		store.setInt(rec, 37, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() throws Exception {
		parent.new IndexMaps(this).insert(getRec());
	}
}