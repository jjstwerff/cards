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
 * Automatically generated record class for table Room
 */
public class Room implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 41;

	public Room(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Room(Store store, int rec) {
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

	public OpponentArray getOpponent() {
		return new OpponentArray(this);
	}

	public ItemsArray getItems() {
		return new ItemsArray(this);
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
			return (store.getByte(recNr, 18) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 18, (store.getByte(rec, 18) & 254) + (value ? 1 : 0));
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
			return store.getInt(Room.this.getRec(), 24);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(Room.this.getRec(), 24, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Connect recA = new Connect(store, a);
			Connect recB = new Connect(store, b);
			int o = 0;
			o = compare(recA.getNr(), recB.getNr());
			if (o == 0)
				return o;
			return o;
		}

		@Override
		protected String toString(int rec) {
			return new Room(store, rec).toString();
		}
	}

	public void getUpRecord(Area value) {
		value.setRec(store.getInt(rec, 37));
	}

	public Area getUpRecord() {
		return new Area(store, rec == 0 ? 0 : store.getInt(rec, 37));
	}

	@Override
	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		write.sub("opponent", false);
		for (OpponentArray sub: getOpponent())
			sub.output(write, iterate - 1);
		write.endSub();
		write.sub("items", false);
		for (ItemsArray sub: getItems())
			sub.output(write, iterate - 1);
		write.endSub();
		write.sub("connection", false);
		for (Connect sub : getConnection())
			sub.output(write, iterate - 1);
		write.endSub();
		write.endRecord();
	}

	@Override
	public String keys() throws IOException {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Area").append("{").append(getUpRecord().keys()).append("}");
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
		if (parser.hasSub("opponent")) {
			OpponentArray sub = new OpponentArray(record);
			while (parser.getSub()) {
				sub.add();
				sub.parse(parser);
			}
		}
		if (parser.hasSub("items")) {
			ItemsArray sub = new ItemsArray(record);
			while (parser.getSub()) {
				sub.add();
				sub.parse(parser);
			}
		}
		if (parser.hasSub("connection"))
			new Connect(store).parse(parser, record);
	}
}
