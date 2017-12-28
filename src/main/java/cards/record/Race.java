package cards.record;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Race
 */
public class Race {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 33;

	public Race(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Race(Store store, int rec) {
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

	public CardsArray getCards() {
		return new CardsArray();
	}

	public class CardsArray implements Iterable<CardsArray>, Iterator<CardsArray>{
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

		public void parse(Parser parser) {
			CardsArray record = this;
		parser.getRelation("card)", () -> {
			Card rec = new Card(store);
			boolean found = rec.parseKey(parser);
			record.setCard(rec);
			return found;
		});
		}
	}

	public void getUpRecord(Rules value) {
		value.setRec(store.getInt(rec, 29));
	}

	public Rules getUpRecord() {
		return new Rules(store, rec == 0 ? 0 : store.getInt(rec, 29));
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		for (CardsArray sub: getCards())
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
			Rules.IndexRaces idx = parent.new IndexRaces(this, name);
			if (idx.nextRec == 0) {
				try (ChangeRace record = new ChangeRace(parent)) {
					record.setName(name);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeRace record = new ChangeRace(this)) {
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
		Rules.IndexRaces idx = parent.new IndexRaces(this, name);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeRace record) {
		if (parser.hasSub("cards"))
			while (parser.getSub()) {
				CardsArray sub = new CardsArray();
				sub.add();
				sub.parse(parser);
			}
	}
}
