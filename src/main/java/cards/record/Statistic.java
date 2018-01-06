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
 * Automatically generated record class for table Statistic
 */
public class Statistic implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 30;

	public Statistic(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Statistic(Store store, int rec) {
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

	public int getNr() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 8);
	}

	public class IndexStatisticName extends RedBlackTree implements Iterable<Statistic>, Iterator<Statistic> {
		Key key = null;
		int nextRec;

		public IndexStatisticName() {
			this.key = null;
			nextRec = first();
		}

		public IndexStatisticName(String key1) {
			this.key = new Key() {
				@Override
				public int compareTo(int recNr) {
					if (recNr < 0)
						return -1;
					assert store.validate(recNr);
					setRec(recNr);
					int o = 0;
					o = RedBlackTree.compare(key1, Statistic.this.getName());
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
		public Iterator<Statistic> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Statistic next() {
			int n = nextRec;
			nextRec = next(nextRec);
			if (key != null) {
				while (nextRec != 0 && key.compareTo(nextRec) != 0) {
					nextRec = next(nextRec);
				}
			}
			setRec(n);
			return Statistic.this;
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
			return store.getInt(0, 4);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(0, 4, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Statistic recA = new Statistic(store, a);
			Statistic recB = new Statistic(store, b);
			int o = 0;
			o = compare(recA.getName(), recB.getName());
			if (o == 0)
				return 0;
			return o;
		}

		@Override
		protected String toString(int rec) {
			return new Statistic(store, rec).toString();
		}
	}

	public class IndexStatisticNr extends RedBlackTree implements Iterable<Statistic>, Iterator<Statistic> {
		Key key = null;
		int nextRec;

		public IndexStatisticNr() {
			this.key = null;
			nextRec = first();
		}

		public IndexStatisticNr(int key1) {
			this.key = new Key() {
				@Override
				public int compareTo(int recNr) {
					if (recNr < 0)
						return -1;
					assert store.validate(recNr);
					setRec(recNr);
					int o = 0;
					o = RedBlackTree.compare(key1, Statistic.this.getNr());
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
		public Iterator<Statistic> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Statistic next() {
			int n = nextRec;
			nextRec = next(nextRec);
			if (key != null) {
				while (nextRec != 0 && key.compareTo(nextRec) != 0) {
					nextRec = next(nextRec);
				}
			}
			setRec(n);
			return Statistic.this;
		}

		@Override
		protected boolean readRed(int recNr) {
			assert store.validate(recNr);
			return (store.getByte(recNr, 21) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 21, (store.getByte(rec, 21) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 22);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 22, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 26);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 26, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(0, 8);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(0, 8, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Statistic recA = new Statistic(store, a);
			Statistic recB = new Statistic(store, b);
			int o = 0;
			o = compare(recA.getNr(), recB.getNr());
			if (o == 0)
				return 0;
			return o;
		}

		@Override
		protected String toString(int rec) {
			return new Statistic(store, rec).toString();
		}
	}

	@Override
	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		write.field("nr", getNr(), false);
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
			IndexStatisticName idx = new IndexStatisticName(name);
			if (idx.nextRec == 0) {
				try (ChangeStatistic record = new ChangeStatistic(store)) {
					record.setName(name);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeStatistic record = new ChangeStatistic(this)) {
					parseFields(parser, record);
				}
			}
		}
	}

	public boolean parseKey(Parser parser) {
		String name = parser.getString("name");
		IndexStatisticName idx = new IndexStatisticName(name);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeStatistic record) {
		record.setNr(parser.getInt("nr"));
	}
}
