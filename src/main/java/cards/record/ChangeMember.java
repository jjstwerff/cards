package cards.record;

/**
 * Automatically generated record class for table Member
 */
public class ChangeMember extends Member implements AutoCloseable {
	private final Player parent;

	public ChangeMember(Player parent) {
		super(parent.store, parent.store.allocate(Member.SIZE));
		this.parent = parent;
		setGame(null);
		setRole(null);
		setXp(0);
		setUpRecord(null);
		setUpRecord(parent);
	}

	public ChangeMember(Member current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexMember(this).remove(getRec());
	}

	public void setGame(Game value) {
		store.setInt(rec, 8, value == null ? 0 : value.getRec());
	}

	public void setRole(Member.Role value) {
		if (value == null)
				store.setShort(rec, 12, 0);
			else
				store.setShort(rec, 12, 1 + value.ordinal());
	}

	public void setXp(int value) {
		store.setInt(rec, 14, value);
	}

	public void setUpRecord(Player value) {
		store.setInt(rec, 27, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() {
		parent.new IndexMember(this).insert(getRec());
	}
}