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

	public void setNr(int value) {
		store.setInt(rec, 8, value);
	}

	public void setType(Connect.Type value) {
		if (value == null)
				store.setShort(rec, 12, 0);
			else
				store.setShort(rec, 12, 1 + value.ordinal());
	}

	public void setUpRecord(Room value) {
		store.setInt(rec, 31, value == null ? 0 : value.getRec());
	}

	public void setTo(Room value) {
		store.setInt(rec, 35, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() {
		parent.new IndexConnection(this).insert(getRec());
	}
}