package cards.record;

/**
 * Automatically generated record class for table Goal
 */
public class ChangeGoal extends Goal implements AutoCloseable {
	private final Area parent;

	public ChangeGoal(Area parent) {
		super(parent.store, parent.store.allocate(Goal.SIZE));
		this.parent = parent;
		setName(null);
		setType(null);
		setXP(0);
		setGained(null);
		setUpRecord(null);
		setUpRecord(parent);
	}

	public ChangeGoal(Goal current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexGoal(this).remove(getRec());
	}

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 8));
	}

	public void setName(String value) {
		store.setInt(rec, 8, store.putString(value));
	}

	public Goal.Type getType() {
		int data = rec == 0 ? 0 : store.getShort(rec, 12);
		if (data <= 0)
			return null;
		return Type.values()[data - 1];
	}

	public void setType(Goal.Type value) {
		if (value == null)
				store.setShort(rec, 12, 0);
			else
				store.setShort(rec, 12, 1 + value.ordinal());
	}

	public int getXP() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 14);
	}

	public void setXP(int value) {
		store.setInt(rec, 14, value);
	}

	public Goal.Gained getGained() {
		int data = rec == 0 ? 0 : store.getShort(rec, 18);
		if (data <= 0)
			return null;
		return Gained.values()[data - 1];
	}

	public void setGained(Goal.Gained value) {
		if (value == null)
				store.setShort(rec, 18, 0);
			else
				store.setShort(rec, 18, 1 + value.ordinal());
	}

	public void getUpRecord(Area value) {
		value.setRec(store.getInt(rec, 29));
	}

	public Area getUpRecord() {
		return new Area(store, rec == 0 ? 0 : store.getInt(rec, 29));
	}

	public void setUpRecord(Area value) {
		store.setInt(rec, 29, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() throws Exception {
		parent.new IndexGoal(this).insert(getRec());
	}
}