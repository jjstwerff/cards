package cards.record;

/**
 * Automatically generated record class for table Skill
 */
public class ChangeSkill extends Skill implements AutoCloseable {
	private final Character parent;

	public ChangeSkill(Character parent) {
		super(parent.store, parent.store.allocate(Skill.SIZE));
		this.parent = parent;
		setCard(null);
		setState(null);
		setUpRecord(null);
		setUpRecord(parent);
	}

	public ChangeSkill(Skill current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexSkills(this).remove(getRec());
	}

	public void setCard(Card value) {
		store.setInt(rec, 8, value == null ? 0 : value.getRec());
	}

	public void setState(Skill.State value) {
		if (value == null)
				store.setShort(rec, 12, 0);
			else
				store.setShort(rec, 12, 1 + value.ordinal());
	}

	public void setUpRecord(Character value) {
		store.setInt(rec, 23, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() throws Exception {
		parent.new IndexSkills(this).insert(getRec());
	}
}