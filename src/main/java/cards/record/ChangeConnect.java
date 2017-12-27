package cards.record;

/**
 * Automatically generated record class for table Connect
 */
public class ChangeConnect extends Connect implements AutoCloseable {
	private final Room parent;

	public ChangeConnect(Room parent) {
		super(parent.store, parent.store.allocate(Connect.SIZE));
		this.parent = parent;
		setNr(0);
		setType(null);
		store.setInt(rec, 14, 0);
		store.setInt(rec, 18, 0);
		setUpRecord(null);
		setTo(null);
		setUpRecord(parent);
	}

	public ChangeConnect(Connect current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexConnection(this).remove(getRec());
	}

	public int getNr() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 8);
	}

	public void setNr(int value) {
		store.setInt(rec, 8, value);
	}

	public Connect.Type getType() {
		int data = rec == 0 ? 0 : store.getShort(rec, 12);
		if (data <= 0)
			return null;
		return Type.values()[data - 1];
	}

	public void setType(Connect.Type value) {
		if (value == null)
				store.setShort(rec, 12, 0);
			else
				store.setShort(rec, 12, 1 + value.ordinal());
	}

	public ChecksArray getChecks() {
		return new ChecksArray();
	}

	public void getUpRecord(Room value) {
		value.setRec(store.getInt(rec, 31));
	}

	public Room getUpRecord() {
		return new Room(store, rec == 0 ? 0 : store.getInt(rec, 31));
	}

	public void setUpRecord(Room value) {
		store.setInt(rec, 31, value == null ? 0 : value.getRec());
	}

	public void getTo(Room value) {
		value.setRec(store.getInt(rec, 35));
	}

	public Room getTo() {
		return new Room(store, rec == 0 ? 0 : store.getInt(rec, 35));
	}

	public void setTo(Room value) {
		store.setInt(rec, 35, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() throws Exception {
		parent.new IndexConnection(this).insert(getRec());
	}
}