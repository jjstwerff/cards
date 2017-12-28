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

	public void setName(String value) {
		store.setInt(rec, 8, store.putString(value));
	}

	public void setType(Goal.Type value) {
		if (value == null)
				store.setShort(rec, 12, 0);
			else
				store.setShort(rec, 12, 1 + value.ordinal());
	}

	public void setXP(int value) {
		store.setInt(rec, 14, value);
	}

	public void setGained(Goal.Gained value) {
		if (value == null)
				store.setShort(rec, 18, 0);
			else
				store.setShort(rec, 18, 1 + value.ordinal());
	}

	public void setUpRecord(Area value) {
		store.setInt(rec, 29, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() {
		parent.new IndexGoal(this).insert(getRec());
	}
}