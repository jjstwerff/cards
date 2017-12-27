package cards.record;

/**
 * Automatically generated record class for table Room
 */
public class ChangeRoom extends Room implements AutoCloseable {
	private final Area parent;

	public ChangeRoom(Area parent) {
		super(parent.store, parent.store.allocate(Room.SIZE));
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

	public ChangeRoom(Room current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexRooms(this).remove(getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 8, store.putString(value));
	}

	public void setUpRecord(Area value) {
		store.setInt(rec, 41, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() throws Exception {
		parent.new IndexRooms(this).insert(getRec());
	}
}