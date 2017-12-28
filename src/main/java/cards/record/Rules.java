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
 * Automatically generated record class for table Rules
 */
public class Rules {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 29;

	public Rules(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Rules(Store store, int rec) {
		rec = store.correct(rec);
		this.store = store;
		this.rec = rec;
	}

	public int getRec() {
		return rec;
	}

	public void setRec(int rec) {
		assert (store.validate(rec));
		this.rec = rec;
	}

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 4));
	}

	public IndexRaces getRaces() {
		return new IndexRaces(new Race(store));
	}

	public class IndexRaces extends RedBlackTree implements Iterable<Race>, Iterator<Race> {
		Key key = null;
		Race record;
		int nextRec;

		public IndexRaces(Race record) {
			this.record = record;
			record.store = store;
			this.key = null;
			nextRec = first();
		}

		public IndexRaces(Race record, String key1) {
			this.record = record;
			record.store = store;
			this.key = new Key() {
				@Override
				public int compareTo(int recNr) {
					if (recNr < 0)
						return -1;
					assert (store.validate(recNr));
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
		public Iterator<Race> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Race next() {
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
			assert (store.validate(recNr));
			return (store.getByte(recNr, 12) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert (store.validate(recNr));
			store.setByte(recNr, 12, (store.getByte(rec, 12) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert (store.validate(recNr));
			return store.getInt(recNr, 17);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert (store.validate(recNr));
			store.setInt(recNr, 17, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert (store.validate(recNr));
			return store.getInt(recNr, 21);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert (store.validate(recNr));
			store.setInt(recNr, 21, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(store.getInt(record.getRec(), 29), 12);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(store.getInt(record.getRec(), 29), 12, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Race recA = new Race(store, a);
			Race recB = new Race(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return 0;
			return 0;
		}

		@Override
		protected String toString(int rec) {
			return new Rules(store, rec).toString();
		}
	}

	public CardsArray getCards() {
		return new CardsArray();
	}

	public class CardsArray implements Iterable<CardsArray>, Iterator<CardsArray> {
		int idx = -1;

		public int getSize() {
			return store.getInt(rec, 12);
		}

		public CardsArray add() {
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
		public Iterator<CardsArray> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return idx + 1 < getSize();
		}

		@Override
		public CardsArray next() {
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

	public class IndexRulesName extends RedBlackTree implements Iterable<Rules>, Iterator<Rules> {
		Key key = null;
		int nextRec;

		public IndexRulesName() {
			this.key = null;
			nextRec = first();
		}

		public IndexRulesName(String key1) {
			this.key = new Key() {
				@Override
				public int compareTo(int recNr) {
					if (recNr < 0)
						return -1;
					assert (store.validate(recNr));
					setRec(recNr);
					int o = 0;
					o = RedBlackTree.compare(key1, Rules.this.getName());
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
		public Iterator<Rules> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Rules next() {
			int n = nextRec;
			nextRec = next(nextRec);
			if (key != null) {
				while (nextRec != 0 && key.compareTo(nextRec) != 0) {
					nextRec = next(nextRec);
				}
			}
			setRec(n);
			return Rules.this;
		}

		@Override
		protected boolean readRed(int recNr) {
			assert (store.validate(recNr));
			return (store.getByte(recNr, 16) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert (store.validate(recNr));
			store.setByte(recNr, 16, (store.getByte(rec, 16) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert (store.validate(recNr));
			return store.getInt(recNr, 21);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert (store.validate(recNr));
			store.setInt(recNr, 21, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert (store.validate(recNr));
			return store.getInt(recNr, 25);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert (store.validate(recNr));
			store.setInt(recNr, 25, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(0, 16);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(0, 16, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Rules recA = new Rules(store, a);
			Rules recB = new Rules(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return 0;
			return o;
		}

		@Override
		protected String toString(int rec) {
			return new Rules(store, rec).toString();
		}
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		for (Race sub : getRaces())
			sub.output(write, iterate - 1);
		for (CardsArray sub : getCards())
			sub.output(write, iterate - 1);
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

	public void parser(Parser parser, Game game) {

	}

	public boolean parseKey(Parser parser) {
		return null;
	}
}
