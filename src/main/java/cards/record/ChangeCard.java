package cards.record;

/**
 * Automatically generated record class for table Card
 */
public class ChangeCard extends Card implements AutoCloseable {
	private final Rules parent;

	public ChangeCard(Rules parent) {
		super(parent.store, parent.store.allocate(Card.SIZE));
		this.parent = parent;
		setName(null);
		setSet(null);
		store.setInt(rec, 10, 0); // ARRAY stats
		store.setInt(rec, 14, 0);
		setUpRecord(null);
		setUpRecord(parent);
	}

	public ChangeCard(Card current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexCards(this).remove(getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 4, store.putString(value));
	}

	public void setSet(Card.Set value) {
		if (value == null)
				store.setShort(rec, 8, 0);
			else
				store.setShort(rec, 8, 1 + value.ordinal());
	}

	public void setUpRecord(Rules value) {
		store.setInt(rec, 27, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() {
		parent.new IndexCards(this).insert(getRec());
	}
}