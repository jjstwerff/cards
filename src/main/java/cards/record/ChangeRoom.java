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

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 8));
	}

	public void setName(String value) {
		store.setInt(rec, 8, store.putString(value));
	}

	public OpponentArray getOpponent() {
		return new OpponentArray();
	}

	public ItemsArray getItems() {
		return new ItemsArray();
	}

	public IndexConnection getConnection() {
		return new IndexConnection(new Connect(store));
	}

	public void getUpRecord(Area value) {
		value.setRec(store.getInt(rec, 41));
	}

	public Area getUpRecord() {
		return new Area(store, rec == 0 ? 0 : store.getInt(rec, 41));
	}

	public void setUpRecord(Area value) {
		store.setInt(rec, 41, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() throws Exception {
		parent.new IndexRooms(this).insert(getRec());
	}
}