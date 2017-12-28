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
 * Automatically generated record class for table Room
 */
public class Room {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 45;

	public Room(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Room(Store store, int rec) {
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
		return rec == 0 ? null : store.getString(store.getInt(rec, 8));
	}

	public OpponentArray getOpponent() {
		return new OpponentArray();
	}

	public class OpponentArray implements Iterable<OpponentArray>, Iterator<OpponentArray>{
		int idx = -1;

		public int getSize() {
			return store.getInt(rec, 12);
		}

		public OpponentArray add() {
			int p = store.getInt(rec, 16);
			idx = getSize();
			if (p == 0 || idx * 4 >= store.getInt(p, 0)) {
				store.setInt(p, 0, idx * 4);
				store.setInt(rec, 16, store.resize(p));
			}
			store.setInt(rec, 12, idx + 1);
			return this;
		}

		@Override
		public Iterator<OpponentArray> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return idx + 1 < getSize();
		}

		@Override
		public OpponentArray next() {
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

		public void parse(Parser parser) {
			OpponentArray record = this;
		parser.getRelation("card)", () -> {
			Card rec = new Card(store);
			boolean found = rec.parseKey(parser);
			record.setCard(rec);
			return found;
		});
		}
	}

	public ItemsArray getItems() {
		return new ItemsArray();
	}

	public class ItemsArray implements Iterable<ItemsArray>, Iterator<ItemsArray>{
		int idx = -1;

		public int getSize() {
			return store.getInt(rec, 20);
		}

		public ItemsArray add() {
			int p = store.getInt(rec, 24);
			idx = getSize();
			if (p == 0 || idx * 4 >= store.getInt(p, 0)) {
				store.setInt(p, 0, idx * 4);
				store.setInt(rec, 24, store.resize(p));
			}
			store.setInt(rec, 20, idx + 1);
			return this;
		}

		@Override
		public Iterator<ItemsArray> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return idx + 1 < getSize();
		}

		@Override
		public ItemsArray next() {
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

		public void parse(Parser parser) {
			ItemsArray record = this;
		parser.getRelation("card)", () -> {
			Card rec = new Card(store);
			boolean found = rec.parseKey(parser);
			record.setCard(rec);
			return found;
		});
		}
	}

	public IndexConnection getConnection() {
		return new IndexConnection(new Connect(store));
	}

	public class IndexConnection extends RedBlackTree implements Iterable<Connect>, Iterator<Connect> {
		Key key = null;
		Connect record;
		int nextRec;

		public IndexConnection(Connect record) {
			this.record = record;
			record.store = store;
			this.key = null;
			nextRec = first();
		}

		public IndexConnection(Connect record, int key1) {
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
					o = RedBlackTree.compare(key1, record.getNr());
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
		public Iterator<Connect> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Connect next() {
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
			return (store.getByte(recNr, 14) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 14, (store.getByte(rec, 14) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 19);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 19, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 23);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 23, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(store.getInt(record.getRec(), 31), 20);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(store.getInt(record.getRec(), 31), 20, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Connect recA = new Connect(store, a);
			Connect recB = new Connect(store, b);
			int o = 0;
			o = compare(recA.getNr(), recB.getNr());
			if (o == 0)
				return 0;
			return 0;
		}

		@Override
		protected String toString(int rec) {
			return new Room(store, rec).toString();
		}
	}

	public void getUpRecord(Area value) {
		value.setRec(store.getInt(rec, 41));
	}

	public Area getUpRecord() {
		return new Area(store, rec == 0 ? 0 : store.getInt(rec, 41));
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		for (OpponentArray sub: getOpponent())
			sub.output(write, iterate - 1);
		for (ItemsArray sub: getItems())
			sub.output(write, iterate - 1);
		write.sub("connection", false);
		for (Connect sub : getConnection())
			sub.output(write, iterate - 1);
		write.endSub();
	}

	public String toKeyString() {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Area").append("={").append(getUpRecord().toKeyString()).append("}");
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

	public void parse(Parser parser, Area parent) {
		while (parser.getSub()) {
			String name = parser.getString("name");
			Area.IndexRooms idx = parent.new IndexRooms(this, name);
			if (idx.nextRec == 0) {
				try (ChangeRoom record = new ChangeRoom(parent)) {
					record.setName(name);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeRoom record = new ChangeRoom(this)) {
					parseFields(parser, record);
				}
			}
		}
	}

	public boolean parseKey(Parser parser) {
		Area parent = new Area(store);
		parser.getRelation("Area", () -> {
			parent.parseKey(parser);
			return true;
		});
		String name = parser.getString("name");
		Area.IndexRooms idx = parent.new IndexRooms(this, name);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeRoom record) {
		if (parser.hasSub("opponent"))
			while (parser.getSub()) {
				OpponentArray sub = new OpponentArray();
				sub.add();
				sub.parse(parser);
			}
		if (parser.hasSub("items"))
			while (parser.getSub()) {
				ItemsArray sub = new ItemsArray();
				sub.add();
				sub.parse(parser);
			}
		if (parser.hasSub("connection"))
			while (parser.getSub()) {
				Connect sub = new Connect(store);
				sub.parse(parser, record);
			}
	}
}
