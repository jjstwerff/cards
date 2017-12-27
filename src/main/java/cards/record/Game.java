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
 * Automatically generated record class for table Game
 */
public class Game {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 25;

	public Game(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Game(Store store, int rec) {
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
		return rec == 0 ? null : store.getString(store.getInt(rec, 4));
	}

	public IndexAreas getAreas() {
		return new IndexAreas(new Area(store));
	}

	public class IndexAreas extends RedBlackTree implements Iterable<Area>, Iterator<Area> {
		Key key = null;
		Area record;
		int nextRec;

		public IndexAreas(Area record) {
			this.record = record;
			record.store = store;
			this.key = null;
			nextRec = first();
		}

		public IndexAreas(Area record, String key1) {
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
		public Iterator<Area> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Area next() {
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
			return (store.getByte(recNr, 20) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert(store.validate(recNr));
			store.setByte(recNr, 20, (store.getByte(rec, 20) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert(store.validate(recNr));
			return store.getInt(recNr, 25);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert(store.validate(recNr));
			store.setInt(recNr, 25, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert(store.validate(recNr));
			return store.getInt(recNr, 29);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert(store.validate(recNr));
			store.setInt(recNr, 29, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(store.getInt(record.getRec(), 37), 36);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(store.getInt(record.getRec(), 37), 36, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Area recA = new Area(store, a);
			Area recB = new Area(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return 0;
			return 0;
		}

		@Override
		protected String toString(int rec) {
			return new Game(store, rec).toString();
		}
	}

	public void getRules(Rules value) {
		value.setRec(store.getInt(rec, 12));
	}

	public Rules getRules() {
		return new Rules(store, rec == 0 ? 0 : store.getInt(rec, 12));
	}

	public class IndexGameName extends RedBlackTree implements Iterable<Game>, Iterator<Game> {
		Key key = null;
		int nextRec;

		public IndexGameName() {
			this.key = null;
			nextRec = first();
		}

		public IndexGameName(String key1) {
			this.key = new Key() {
				@Override
				public int compareTo(int recNr) {
					if (recNr < 0)
						return -1;
					assert(store.validate(recNr));
					setRec(recNr);
					int o = 0;
					o = RedBlackTree.compare(key1, Game.this.getName());
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
		public Iterator<Game> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Game next() {
			int n = nextRec;
			nextRec = next(nextRec);
			if (key != null) {
				while (nextRec != 0 && key.compareTo(nextRec) != 0) {
					nextRec = next(nextRec);
				}
			}
			setRec(n);
			return Game.this;
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
			return store.getInt(0, 40);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(0, 40, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Game recA = new Game(store, a);
			Game recB = new Game(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return 0;
			return o;
		}

		@Override
		protected String toString(int rec) {
			return new Game(store, rec).toString();
		}
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		for (Area sub: getAreas())
			sub.output(write, iterate - 1);
		write.field("rules", "{" + getRules().toKeyString() + "}", false);
	}

	public String toKeyString() {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
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
