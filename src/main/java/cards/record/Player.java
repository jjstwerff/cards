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
import com.betterbe.memorydb.structure.DateTime;
import java.time.LocalDateTime;

/**
 * Automatically generated record class for table Player
 */
public class Player {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 37;

	public Player(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Player(Store store, int rec) {
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

	public LocalDateTime getCreation() {
		return rec == 0 ? null : DateTime.of(store.getLong(rec, 4));
	}

	public LocalDateTime getLast() {
		return rec == 0 ? null : DateTime.of(store.getLong(rec, 12));
	}

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 20));
	}

	public IndexMember getMember() {
		return new IndexMember(new Member(store));
	}

	public class IndexMember extends RedBlackTree implements Iterable<Member>, Iterator<Member> {
		Key key = null;
		Member record;
		int nextRec;

		public IndexMember(Member record) {
			this.record = record;
			record.store = store;
			this.key = null;
			nextRec = first();
		}

		public IndexMember(Member record, String key1) {
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
					o = RedBlackTree.compare(key1, record.getGame().getName());
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
		public Iterator<Member> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Member next() {
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
			return (store.getByte(recNr, 10) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 10, (store.getByte(rec, 10) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 15);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 15, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 19);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 19, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(store.getInt(record.getRec(), 27), 56);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(store.getInt(record.getRec(), 27), 56, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Member recA = new Member(store, a);
			Member recB = new Member(store, b);
			int o = 0;
			o = compare(recA.getGame().getName(), recB.getGame().getName());
			if (o == 0)
				return 0;
			return 0;
		}

		@Override
		protected String toString(int rec) {
			return new Player(store, rec).toString();
		}
	}

	public class IndexPlayerName extends RedBlackTree implements Iterable<Player>, Iterator<Player> {
		Key key = null;
		int nextRec;

		public IndexPlayerName() {
			this.key = null;
			nextRec = first();
		}

		public IndexPlayerName(String key1) {
			this.key = new Key() {
				@Override
				public int compareTo(int recNr) {
					if (recNr < 0)
						return -1;
					assert store.validate(recNr);
					setRec(recNr);
					int o = 0;
					o = RedBlackTree.compare(key1, Player.this.getName());
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
		public Iterator<Player> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Player next() {
			int n = nextRec;
			nextRec = next(nextRec);
			if (key != null) {
				while (nextRec != 0 && key.compareTo(nextRec) != 0) {
					nextRec = next(nextRec);
				}
			}
			setRec(n);
			return Player.this;
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
			return store.getInt(0, 60);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(0, 60, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Player recA = new Player(store, a);
			Player recB = new Player(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return 0;
			return o;
		}

		@Override
		protected String toString(int rec) {
			return new Player(store, rec).toString();
		}
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("creation", getCreation(), true);
		write.field("last", getLast(), false);
		write.field("name", getName(), false);
		write.sub("member", false);
		for (Member sub : getMember())
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
			IndexPlayerName idx = new IndexPlayerName(name);
			if (idx.nextRec == 0) {
				try (ChangePlayer record = new ChangePlayer(store)) {
					record.setName(name);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangePlayer record = new ChangePlayer(this)) {
					parseFields(parser, record);
				}
			}
		}
	}

	public boolean parseKey(Parser parser) {
		String name = parser.getString("name");
		IndexPlayerName idx = new IndexPlayerName(name);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangePlayer record) {
		record.setCreation(DateTime.of(parser.getString("creation")));
		record.setLast(DateTime.of(parser.getString("last")));
		if (parser.hasSub("member"))
			while (parser.getSub()) {
				Member sub = new Member(store);
				sub.parse(parser, record);
			}
	}
}
