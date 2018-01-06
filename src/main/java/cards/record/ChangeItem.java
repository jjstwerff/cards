package cards.record;

/**
 * Automatically generated record class for table Item
 */
public class ChangeItem extends Item implements AutoCloseable {
	private final Game parent;

	public ChangeItem(Game parent) {
		super(parent.store, parent.store.allocate(Item.SIZE));
		this.parent = parent;
		setName(null);
		setMaterial(null);
		setUpRecord(null);
		setUpRecord(parent);
	}

	public ChangeItem(Item current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexItems(this).remove(getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 4, store.putString(value));
	}

	public void setMaterial(Material value) {
		store.setInt(rec, 8, value == null ? 0 : value.getRec());
	}

	public void setUpRecord(Game value) {
		store.setInt(rec, 21, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() {
		parent.new IndexItems(this).insert(getRec());
	}
}