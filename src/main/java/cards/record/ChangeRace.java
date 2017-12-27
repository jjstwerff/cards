package cards.record;

/**
 * Automatically generated record class for table Race
 */
public class ChangeRace extends Race implements AutoCloseable {
	private final Rules parent;

	public ChangeRace(Rules parent) {
		super(parent.store, parent.store.allocate(Race.SIZE));
		this.parent = parent;
		setName(null);
		store.setInt(rec, 12, 0);
		store.setInt(rec, 16, 0);
		setUpRecord(null);
		setUpRecord(parent);
	}

	public ChangeRace(Race current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexRaces(this).remove(getRec());
	}

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 8));
	}

	public void setName(String value) {
		store.setInt(rec, 8, store.putString(value));
	}

	public CardsArray getCards() {
		return new CardsArray();
	}

	public void getUpRecord(Rules value) {
		value.setRec(store.getInt(rec, 29));
	}

	public Rules getUpRecord() {
		return new Rules(store, rec == 0 ? 0 : store.getInt(rec, 29));
	}

	public void setUpRecord(Rules value) {
		store.setInt(rec, 29, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() throws Exception {
		parent.new IndexRaces(this).insert(getRec());
	}
}