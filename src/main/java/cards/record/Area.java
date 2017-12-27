package cards.record;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.betterbe.memorydb.structure.Store;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.IndexOperation;
import com.betterbe.memorydb.structure.Key;
import com.betterbe.memorydb.structure.RedBlackTree;

/**
 * Automatically generated record class for table Area
 */
public class Area {
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

	public int getRec() {
		return rec;
	}

	public void setRec(int rec) {
		assert(store.validate(rec));
		this.rec = rec;
	}

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 8));
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
					assert(store.validate(recNr));
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
			assert(store.validate(recNr));
			return (store.getByte(recNr, 24) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert(store.validate(recNr));
			store.setByte(recNr, 24, (store.getByte(rec, 24) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert(store.validate(recNr));
			return store.getInt(recNr, 29);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert(store.validate(recNr));
			store.setInt(recNr, 29, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert(store.validate(recNr));
			return store.getInt(recNr, 33);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert(store.validate(recNr));
			store.setInt(recNr, 33, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(store.getInt(record.getRec(), 41), 24);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(store.getInt(record.getRec(), 41), 24, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Room recA = new Room(store, a);
			Room recB = new Room(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return 0;
			return 0;
		}

		@Override
		protected String toString(int rec) {
			return new Area(store, rec).toString();
		}
	}

	public EncounterArray getEncounter() {
		return new EncounterArray();
	}

	public class EncounterArray implements Iterable<EncounterArray>, Iterator<EncounterArray>{
		int idx = -1;

		public int getSize() {
			return store.getInt(rec, 16);
		}

		public EncounterArray add() {
			int p = store.getInt(rec, 20);
			idx = getSize();
			if (p == 0 || idx * 4 >= store.getInt(p, 0)) {
				store.setInt(p, 0, idx * 4);
				store.setInt(rec, 20, store.resize(p));
			}
			store.setInt(rec, 16, idx + 1);
			return this;
		}

		@Override
		public Iterator<EncounterArray> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return idx + 1 < getSize();
		}

		@Override
		public EncounterArray next() {
			idx++;
			return this;
		}


		public void getCard(Card value) {
			value.setRec(store.getInt(rec, 0));
		}

		public Card getCard() {
			return new Card(store, rec == 0 || idx < 0 ? 0 : store.getInt(rec, idx * 4 + 0));
		}

		public void setCard(Card value) {
			store.setInt(rec, idx * 4 + 0, value == null ? 0 : value.getRec());
		}

		public void output(Write write, int iterate) throws IOException {
			if (rec == 0 || iterate <= 0)
				return;
			write.field("card", "{" + getCard().toKeyString() + "}", true);
		}
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
					assert(store.validate(recNr));
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
			assert(store.validate(recNr));
			return (store.getByte(recNr, 12) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert(store.validate(recNr));
			store.setByte(recNr, 12, (store.getByte(rec, 12) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert(store.validate(recNr));
			return store.getInt(recNr, 17);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert(store.validate(recNr));
			store.setInt(recNr, 17, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert(store.validate(recNr));
			return store.getInt(recNr, 21);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert(store.validate(recNr));
			store.setInt(recNr, 21, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(store.getInt(record.getRec(), 29), 28);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(store.getInt(record.getRec(), 29), 28, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Goal recA = new Goal(store, a);
			Goal recB = new Goal(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return 0;
			return 0;
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

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		for (Room sub: getRooms())
			sub.output(write, iterate - 1);
		for (EncounterArray sub: getEncounter())
			sub.output(write, iterate - 1);
		for (Goal sub: getGoal())
			sub.output(write, iterate - 1);
	}

	public String toKeyString() {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Game").append("={").append(getUpRecord().toKeyString()).append("}");
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
}
