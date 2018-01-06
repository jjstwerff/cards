package cards.record;

import java.io.IOException;
import java.io.StringWriter;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.RecordInterface;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Race
 */
public class Race implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 29;

	public Race(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Race(Store store, int rec) {
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


	public CardsArray getCards() {
		return new CardsArray(this);
	}


	public void getUpRecord(Rules value) {
		value.setRec(store.getInt(rec, 25));
	}

	public Rules getUpRecord() {
		return new Rules(store, rec == 0 ? 0 : store.getInt(rec, 25));
	}


	@Override
	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		write.sub("cards", false);
		for (CardsArray sub: getCards())
			sub.output(write, iterate - 1);
		write.endSub();
		write.endRecord();
	}

	@Override
	public String keys() throws IOException {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Rules").append("{").append(getUpRecord().keys()).append("}");
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
		if (parser.hasSub("cards")) {
			CardsArray sub = new CardsArray(record);
			while (parser.getSub()) {
				sub.add();
				sub.parse(parser);
			}
		}
	}
}
