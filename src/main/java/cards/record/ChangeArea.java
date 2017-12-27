package cards.record;

/**
 * Automatically generated record class for table Area
 */
public class ChangeArea extends Area implements AutoCloseable {
	private final Game parent;

	public ChangeArea(Game parent) {
		super(parent.store, parent.store.allocate(Area.SIZE));
		this.parent = parent;
		setName(null);
		store.setInt(rec, 12, 0);
		store.setInt(rec, 16, 0);
		store.setInt(rec, 20, 0);
		store.setInt(rec, 24, 0);
		setUpRecord(null);
		setUpRecord(parent);
	}

	public ChangeArea(Area current) {
		super(current.store, current.getRec());
		this.parent = getUpRecord();
		parent.new IndexAreas(this).remove(getRec());
	}

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 8));
	}

	public void setName(String value) {
		store.setInt(rec, 8, store.putString(value));
	}

	public IndexRooms getRooms() {
		return new IndexRooms(new Room(store));
	}

	public EncounterArray getEncounter() {
		return new EncounterArray();
	}

	public IndexGoal getGoal() {
		return new IndexGoal(new Goal(store));
	}

	public void getUpRecord(Game value) {
		value.setRec(store.getInt(rec, 37));
	}

	public Game getUpRecord() {
		return new Game(store, rec == 0 ? 0 : store.getInt(rec, 37));
	}

	public void setUpRecord(Game value) {
		store.setInt(rec, 37, value == null ? 0 : value.getRec());
	}

	@Override
	public void close() throws Exception {
		parent.new IndexAreas(this).insert(getRec());
	}
}