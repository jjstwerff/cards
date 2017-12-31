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
		store.setInt(rec, 8, 0); // ARRAY opponent
		store.setInt(rec, 12, 0);
		store.setInt(rec, 16, 0); // ARRAY items
		store.setInt(rec, 20, 0);
		store.setInt(rec, 24, 0); // SET connection
		setUpRecord(null);
		setUpRecord(parent);
	}

	public ChangeRoom(Room current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexRooms(this).remove(getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 4, store.putString(value));
	}

	public void setUpRecord(Area value) {
		store.setInt(rec, 37, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() {
		parent.new IndexRooms(this).insert(getRec());
	}
}