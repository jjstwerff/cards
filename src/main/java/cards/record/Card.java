package cards.record;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Card
 */
public class Card {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 35;

	public Card(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Card(Store store, int rec) {
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

	public enum Set {
		RACIAL, BACKGROUND, MOVE, WEAPON, WEARING, IMPLANT, ENCOUNTER, ROOM, OPPONENT, COMBAT
	};

	public Card.Set getSet() {
		int data = rec == 0 ? 0 : store.getShort(rec, 12);
		if (data <= 0)
			return null;
		return Set.values()[data - 1];
	}

	public StatsArray getStats() {
		return new StatsArray();
	}

	public class StatsArray implements Iterable<StatsArray>, Iterator<StatsArray>{
		int idx = -1;

		public int getSize() {
			return store.getInt(rec, 14);
		}

		public StatsArray add() {
			int p = store.getInt(rec, 18);
			idx = getSize();
			if (p == 0 || idx * 5 >= store.getInt(p, 0)) {
				store.setInt(p, 0, idx * 5);
				store.setInt(rec, 18, store.resize(p));
			}
			store.setInt(rec, 14, idx + 1);
			return this;
		}

		@Override
		public Iterator<StatsArray> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return idx + 1 < getSize();
		}

		@Override
		public StatsArray next() {
			idx++;
			return this;
		}


		public void getStatistic(Statistic value) {
			value.setRec(store.getInt(rec, 0));
		}

		public Statistic getStatistic() {
			return new Statistic(store, rec == 0 || idx < 0 ? 0 : store.getInt(rec, idx * 5 + 0));
		}

		public void setStatistic(Statistic value) {
			store.setInt(rec, idx * 5 + 0, value == null ? 0 : value.getRec());
		}

		public byte getValue() {
			return rec == 0 ? 0 : store.getByte(rec, idx * 5 + 4);
		}

		public void setValue(byte value) {
			store.setByte(rec, idx * 5 + 4, value);
		}

		public void output(Write write, int iterate) throws IOException {
			if (rec == 0 || iterate <= 0)
				return;
			write.field("statistic", "{" + getStatistic().toKeyString() + "}", true);
			write.field("value", getValue(), false);
		}

		public void parse(Parser parser) {
			StatsArray record = this;
			parser.getRelation("statistic)", () -> {
				Statistic rec = new Statistic(store);
				boolean found = rec.parseKey(parser);
				record.setStatistic(rec);
				return found;
			});
			record.setValue((byte) parser.getInt("value"));
		}
	}

	public void getUpRecord(Rules value) {
		value.setRec(store.getInt(rec, 31));
	}

	public Rules getUpRecord() {
		return new Rules(store, rec == 0 ? 0 : store.getInt(rec, 31));
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		write.field("set", getSet(), false);
		for (StatsArray sub: getStats())
			sub.output(write, iterate - 1);
	}

	public String toKeyString() {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Rules").append("={").append(getUpRecord().toKeyString()).append("}");
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

	public void parse(Parser parser, Rules parent) {
		while (parser.getSub()) {
			String name = parser.getString("name");
			Rules.IndexCards idx = parent.new IndexCards(this, name);
			if (idx.nextRec == 0) {
				try (ChangeCard record = new ChangeCard(parent)) {
					record.setName(name);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeCard record = new ChangeCard(this)) {
					parseFields(parser, record);
				}
			}
		}
	}

	public boolean parseKey(Parser parser) {
		Rules parent = new Rules(store);
		parser.getRelation("Rules", () -> {
			parent.parseKey(parser);
			return true;
		});
		String name = parser.getString("name");
		Rules.IndexCards idx = parent.new IndexCards(this, name);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeCard record) {
		record.setSet(Set.valueOf(parser.getString("set")));
		if (parser.hasSub("stats"))
			while (parser.getSub()) {
				StatsArray sub = new StatsArray();
				sub.add();
				sub.parse(parser);
			}
	}
}
