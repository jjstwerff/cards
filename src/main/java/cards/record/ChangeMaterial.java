package cards.record;

/**
 * Automatically generated record class for table Material
 */
public class ChangeMaterial extends Material implements AutoCloseable {
	private final Game parent;

	public ChangeMaterial(Game parent) {
		super(parent.store, parent.store.allocate(Material.SIZE));
		this.parent = parent;
		setName(null);
		setColor(0);
		setUpRecord(null);
		setUpRecord(parent);
	}

	public ChangeMaterial(Material current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexMaterials(this).remove(getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 4, store.putString(value));
	}

	public void setColor(int value) {
		store.setInt(rec, 8, value);
	}

	public void setUpRecord(Game value) {
		store.setInt(rec, 21, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() {
		parent.new IndexMaterials(this).insert(getRec());
	}
}