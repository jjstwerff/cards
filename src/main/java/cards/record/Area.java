package cards.record;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.IndexOperation;
import com.betterbe.memorydb.structure.Key;
import com.betterbe.memorydb.structure.RedBlackTree;
import com.betterbe.memorydb.structure.RecordInterface;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Area
 */
public class Area implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 41;

	public Area(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Area(Store store, int rec) {
		rec = store.correct(rec);
		this.store = store;
		this.rec = rec;
	}

	@Override
	public int getRec() {
		return rec;
	}

	public void setRec(int rec) {
		assert store.validate(rec);
		this.rec = rec;
	}

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 4));
	}


	public IndexRooms getRooms() {
		return new IndexRooms(new Room(store));
	}


	public class IndexRooms extends RedBlackTree implements Iterable<Room>, Iterator<Room> {
		Key key = null;
		Room record;
		int nextRec;

		public IndexRooms(Room record) {
			this.record = record;
			record.store = store;
			this.key = null;
			nextRec = first();
		}

		public IndexRooms(Room record, String key1) {
			this.record = record;
			record.store = store;
			this.key = new Key() {
				@Override
				public int compareTo(int recNr) {
					if (recNr < 0)
						return -1;
					assert store.validate(recNr);
					record.setRec(recNr);
					int o = 0;
					o = RedBlackTree.compare(key1, record.getName());
					return o;
				}

				@Override
				public IndexOperation oper() {
					return IndexOperation.EQ;
				}
			};
			nextRec = find(this.key);
		}

		@Override
		public Iterator<Room> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Room next() {
			int n = nextRec;
			nextRec = next(nextRec);
			if (key != null) {
				while (nextRec != 0 && key.compareTo(nextRec) != 0) {
					nextRec = next(nextRec);
				}
			}
			record.setRec(n);
			return record;
		}

		@Override
		protected boolean readRed(int recNr) {
			assert store.validate(recNr);
			return (store.getByte(recNr, 28) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 28, (store.getByte(rec, 28) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 29);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 29, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 33);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 33, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(Area.this.getRec(), 8);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(Area.this.getRec(), 8, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Room recA = new Room(store, a);
			Room recB = new Room(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return o;
			return o;
		}

		@Override
		protected String toString(int rec) {
			return new Area(store, rec).toString();
		}
	}

	public EncounterArray getEncounter() {
		return new EncounterArray(this);
	}


	public IndexGoal getGoal() {
		return new IndexGoal(new Goal(store));
	}


	public class IndexGoal extends RedBlackTree implements Iterable<Goal>, Iterator<Goal> {
		Key key = null;
		Goal record;
		int nextRec;

		public IndexGoal(Goal record) {
			this.record = record;
			record.store = store;
			this.key = null;
			nextRec = first();
		}

		public IndexGoal(Goal record, String key1) {
			this.record = record;
			record.store = store;
			this.key = new Key() {
				@Override
				public int compareTo(int recNr) {
					if (recNr < 0)
						return -1;
					assert store.validate(recNr);
					record.setRec(recNr);
					int o = 0;
					o = RedBlackTree.compare(key1, record.getName());
					return o;
				}

				@Override
				public IndexOperation oper() {
					return IndexOperation.EQ;
				}
			};
			nextRec = find(this.key);
		}

		@Override
		public Iterator<Goal> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Goal next() {
			int n = nextRec;
			nextRec = next(nextRec);
			if (key != null) {
				while (nextRec != 0 && key.compareTo(nextRec) != 0) {
					nextRec = next(nextRec);
				}
			}
			record.setRec(n);
			return record;
		}

		@Override
		protected boolean readRed(int recNr) {
			assert store.validate(recNr);
			return (store.getByte(recNr, 16) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 16, (store.getByte(rec, 16) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 17);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 17, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 21);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 21, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(Area.this.getRec(), 20);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(Area.this.getRec(), 20, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Goal recA = new Goal(store, a);
			Goal recB = new Goal(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return o;
			return o;
		}

		@Override
		protected String toString(int rec) {
			return new Area(store, rec).toString();
		}
	}

	public IndexMaps getMaps() {
		return new IndexMaps(new Map(store));
	}


	public class IndexMaps extends RedBlackTree implements Iterable<Map>, Iterator<Map> {
		Key key = null;
		Map record;
		int nextRec;

		public IndexMaps(Map record) {
			this.record = record;
			record.store = store;
			this.key = null;
			nextRec = first();
		}

		public IndexMaps(Map record, int key1) {
			this.record = record;
			record.store = store;
			this.key = new Key() {
				@Override
				public int compareTo(int recNr) {
					if (recNr < 0)
						return -1;
					assert store.validate(recNr);
					record.setRec(recNr);
					int o = 0;
					o = RedBlackTree.compare(key1, record.getX());
					return o;
				}

				@Override
				public IndexOperation oper() {
					return IndexOperation.EQ;
				}
			};
			nextRec = find(this.key);
		}

		public IndexMaps(Map record, int key1, int key2) {
			this.record = record;
			record.store = store;
			this.key = new Key() {
				@Override
				public int compareTo(int recNr) {
					if (recNr < 0)
						return -1;
					assert store.validate(recNr);
					record.setRec(recNr);
					int o = 0;
					o = RedBlackTree.compare(key1, record.getX());
					if (o != 0)
						return o;
					o = RedBlackTree.compare(key2, record.getY());
					return o;
				}

				@Override
				public IndexOperation oper() {
					return IndexOperation.EQ;
				}
			};
			nextRec = find(this.key);
		}

		public IndexMaps(Map record, int key1, int key2, int key3) {
			this.record = record;
			record.store = store;
			this.key = new Key() {
				@Override
				public int compareTo(int recNr) {
					if (recNr < 0)
						return -1;
					assert store.validate(recNr);
					record.setRec(recNr);
					int o = 0;
					o = RedBlackTree.compare(key1, record.getX());
					if (o != 0)
						return o;
					o = RedBlackTree.compare(key2, record.getY());
					if (o != 0)
						return o;
					o = RedBlackTree.compare(key3, record.getZ());
					return o;
				}

				@Override
				public IndexOperation oper() {
					return IndexOperation.EQ;
				}
			};
			nextRec = find(this.key);
		}

		@Override
		public Iterator<Map> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Map next() {
			int n = nextRec;
			nextRec = next(nextRec);
			if (key != null) {
				while (nextRec != 0 && key.compareTo(nextRec) != 0) {
					nextRec = next(nextRec);
				}
			}
			record.setRec(n);
			return record;
		}

		@Override
		protected boolean readRed(int recNr) {
			assert store.validate(recNr);
			return (store.getByte(recNr, 28) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 28, (store.getByte(rec, 28) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 29);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 29, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 33);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 33, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(Area.this.getRec(), 24);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(Area.this.getRec(), 24, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Map recA = new Map(store, a);
			Map recB = new Map(store, b);
			int o = 0;
			o = compare(recA.getX(), recB.getX());
			if (o == 0)
				return o;
			o = compare(recA.getY(), recB.getY());
			if (o == 0)
				return o;
			o = compare(recA.getZ(), recB.getZ());
			if (o == 0)
				return o;
			return o;
		}

		@Override
		protected String toString(int rec) {
			return new Area(store, rec).toString();
		}
	}

	public void getUpRecord(Game value) {
		value.setRec(store.getInt(rec, 37));
	}

	public Game getUpRecord() {
		return new Game(store, rec == 0 ? 0 : store.getInt(rec, 37));
	}


	@Override
	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		write.sub("rooms", false);
		for (Room sub : getRooms())
			sub.output(write, iterate - 1);
		write.endSub();
		write.sub("encounter", false);
		for (EncounterArray sub: getEncounter())
			sub.output(write, iterate - 1);
		write.endSub();
		write.sub("goal", false);
		for (Goal sub : getGoal())
			sub.output(write, iterate - 1);
		write.endSub();
		write.sub("maps", false);
		for (Map sub : getMaps())
			sub.output(write, iterate - 1);
		write.endSub();
		write.endRecord();
	}

	@Override
	public String keys() throws IOException {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Game").append("{").append(getUpRecord().keys()).append("}");
		res.append(", ");
		res.append("Name").append("=").append(getName());
		return res.toString();
	}

	@Override
	public String toString() {
		Write write = new Write(new StringWriter());
		try {
			output(write, 4);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return write.toString();
	}

	public void parse(Parser parser, Game parent) {
		while (parser.getSub()) {
			String name = parser.getString("name");
			Game.IndexAreas idx = parent.new IndexAreas(this, name);
			if (idx.nextRec == 0) {
				try (ChangeArea record = new ChangeArea(parent)) {
					record.setName(name);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeArea record = new ChangeArea(this)) {
					parseFields(parser, record);
				}
			}
		}
	}

	public boolean parseKey(Parser parser) {
		Game parent = new Game(store);
		parser.getRelation("Game", () -> {
			parent.parseKey(parser);
			return true;
		});
		String name = parser.getString("name");
		Game.IndexAreas idx = parent.new IndexAreas(this, name);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeArea record) {
		if (parser.hasSub("rooms"))
			new Room(store).parse(parser, record);
		if (parser.hasSub("encounter")) {
			EncounterArray sub = new EncounterArray(record);
			while (parser.getSub()) {
				sub.add();
				sub.parse(parser);
			}
		}
		if (parser.hasSub("goal"))
			new Goal(store).parse(parser, record);
		if (parser.hasSub("maps"))
			new Map(store).parse(parser, record);
	}
}
