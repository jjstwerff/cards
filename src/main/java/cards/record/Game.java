package cards.record;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.IndexOperation;
import com.betterbe.memorydb.structure.Key;
import com.betterbe.memorydb.structure.RedBlackTree;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Game
 */
public class Game {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 29;

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
		assert store.validate(rec);
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
			assert store.validate(recNr);
			return (store.getByte(recNr, 24) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 24, (store.getByte(rec, 24) & 254) + (value ? 1 : 0));
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
			return store.getInt(store.getInt(record.getRec(), 41), 44);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(store.getInt(record.getRec(), 41), 44, value);
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

	public IndexCharacters getCharacters() {
		return new IndexCharacters(new Character(store));
	}

	public class IndexCharacters extends RedBlackTree implements Iterable<Character>, Iterator<Character> {
		Key key = null;
		Character record;
		int nextRec;

		public IndexCharacters(Character record) {
			this.record = record;
			record.store = store;
			this.key = null;
			nextRec = first();
		}

		public IndexCharacters(Character record, String key1) {
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
		public Iterator<Character> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Character next() {
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
			return (store.getByte(recNr, 8) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 8, (store.getByte(rec, 8) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 13);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 13, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 17);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 17, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(store.getInt(record.getRec(), 25), 48);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(store.getInt(record.getRec(), 25), 48, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Character recA = new Character(store, a);
			Character recB = new Character(store, b);
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
					assert store.validate(recNr);
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
			return store.getInt(recNr, 21);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 21, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 25);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 25, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(0, 52);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(0, 52, value);
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
		write.sub("areas", false);
		for (Area sub : getAreas())
			sub.output(write, iterate - 1);
		write.endSub();
		write.field("rules", "{" + getRules().toKeyString() + "}", false);
		write.sub("characters", false);
		for (Character sub : getCharacters())
			sub.output(write, iterate - 1);
		write.endSub();
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

	public void parse(Parser parser) {
		while (parser.getSub()) {
			String name = parser.getString("name");
			IndexGameName idx = new IndexGameName(name);
			if (idx.nextRec == 0) {
				try (ChangeGame record = new ChangeGame(store)) {
					record.setName(name);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeGame record = new ChangeGame(this)) {
					parseFields(parser, record);
				}
			}
		}
	}

	public boolean parseKey(Parser parser) {
		String name = parser.getString("name");
		IndexGameName idx = new IndexGameName(name);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeGame record) {
		if (parser.hasSub("areas"))
			while (parser.getSub()) {
				Area sub = new Area(store);
				sub.parse(parser, record);
			}
		parser.getRelation("rules)", () -> {
			Rules rec = new Rules(store);
			boolean found = rec.parseKey(parser);
			record.setRules(rec);
			return found;
		});
		if (parser.hasSub("characters"))
			while (parser.getSub()) {
				Character sub = new Character(store);
				sub.parse(parser, record);
			}
	}
}
