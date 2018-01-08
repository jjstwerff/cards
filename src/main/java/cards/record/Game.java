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
 * Automatically generated record class for table Game
 */
public class Game implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 53;

	public Game(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Game(Store store, int rec) {
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
			return store.getInt(Game.this.getRec(), 8);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(Game.this.getRec(), 8, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Area recA = new Area(store, a);
			Area recB = new Area(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return o;
			return o;
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
			return (store.getByte(recNr, 12) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 12, (store.getByte(rec, 12) & 254) + (value ? 1 : 0));
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
			return store.getInt(Game.this.getRec(), 16);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(Game.this.getRec(), 16, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Character recA = new Character(store, a);
			Character recB = new Character(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return o;
			return o;
		}

		@Override
		protected String toString(int rec) {
			return new Game(store, rec).toString();
		}
	}

	public WallsArray getWalls() {
		return new WallsArray(this);
	}

	public FloorsArray getFloors() {
		return new FloorsArray(this);
	}

	public IndexItems getItems() {
		return new IndexItems(new Item(store));
	}

	public class IndexItems extends RedBlackTree implements Iterable<Item>, Iterator<Item> {
		Key key = null;
		Item record;
		int nextRec;

		public IndexItems(Item record) {
			this.record = record;
			record.store = store;
			this.key = null;
			nextRec = first();
		}

		public IndexItems(Item record, String key1) {
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
		public Iterator<Item> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Item next() {
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
			return (store.getByte(recNr, 12) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 12, (store.getByte(rec, 12) & 254) + (value ? 1 : 0));
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
			return store.getInt(Game.this.getRec(), 36);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(Game.this.getRec(), 36, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Item recA = new Item(store, a);
			Item recB = new Item(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return o;
			return o;
		}

		@Override
		protected String toString(int rec) {
			return new Game(store, rec).toString();
		}
	}

	public IndexMaterials getMaterials() {
		return new IndexMaterials(new Material(store));
	}

	public class IndexMaterials extends RedBlackTree implements Iterable<Material>, Iterator<Material> {
		Key key = null;
		Material record;
		int nextRec;

		public IndexMaterials(Material record) {
			this.record = record;
			record.store = store;
			this.key = null;
			nextRec = first();
		}

		public IndexMaterials(Material record, String key1) {
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
		public Iterator<Material> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Material next() {
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
			return (store.getByte(recNr, 12) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 12, (store.getByte(rec, 12) & 254) + (value ? 1 : 0));
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
			return store.getInt(Game.this.getRec(), 40);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(Game.this.getRec(), 40, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Material recA = new Material(store, a);
			Material recB = new Material(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return o;
			return o;
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
			return (store.getByte(recNr, 44) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 44, (store.getByte(rec, 44) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 45);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 45, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 49);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 49, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(0, 20);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(0, 20, value);
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

	@Override
	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		write.sub("areas", false);
		for (Area sub : getAreas())
			sub.output(write, iterate - 1);
		write.endSub();
		write.strField("rules", "{" + getRules().keys() + "}", false);
		write.sub("characters", false);
		for (Character sub : getCharacters())
			sub.output(write, iterate - 1);
		write.endSub();
		write.sub("walls", false);
		for (WallsArray sub: getWalls())
			sub.output(write, iterate - 1);
		write.endSub();
		write.sub("floors", false);
		for (FloorsArray sub: getFloors())
			sub.output(write, iterate - 1);
		write.endSub();
		write.sub("items", false);
		for (Item sub : getItems())
			sub.output(write, iterate - 1);
		write.endSub();
		write.sub("materials", false);
		for (Material sub : getMaterials())
			sub.output(write, iterate - 1);
		write.endSub();
		write.endRecord();
	}

	@Override
	public String keys() throws IOException {
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
		String name = parser.getRelationString("name");
		IndexGameName idx = new IndexGameName(name);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeGame record) {
		if (parser.hasSub("areas"))
			new Area(store).parse(parser, record);
		parser.getRelation("rules", (int recNr) -> {
			Rules rec = new Rules(store);
			boolean found = rec.parseKey(parser);
			record.setRec(recNr);
			record.setRules(rec);
			return found;
		}, getRec());
		if (parser.hasSub("characters"))
			new Character(store).parse(parser, record);
		if (parser.hasSub("walls")) {
			WallsArray sub = new WallsArray(record);
			while (parser.getSub()) {
				sub.add();
				sub.parse(parser);
			}
		}
		if (parser.hasSub("floors")) {
			FloorsArray sub = new FloorsArray(record);
			while (parser.getSub()) {
				sub.add();
				sub.parse(parser);
			}
		}
		if (parser.hasSub("items"))
			new Item(store).parse(parser, record);
		if (parser.hasSub("materials"))
			new Material(store).parse(parser, record);
	}
}
