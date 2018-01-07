package cards.record;

import java.io.IOException;
import java.io.StringWriter;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.RecordInterface;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Card
 */
public class Card implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 31;

	public Card(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Card(Store store, int rec) {
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

	public enum Set {
		RACIAL, BACKGROUND, MOVE, WEAPON, WEARING, IMPLANT, ENCOUNTER, ROOM, OPPONENT, COMBAT
	};

	public Card.Set getSet() {
		int data = rec == 0 ? 0 : store.getShort(rec, 8);
		if (data <= 0)
			return null;
		return Set.values()[data - 1];
	}

	public StatsArray getStats() {
		return new StatsArray(this);
	}

	public void getUpRecord(Rules value) {
		value.setRec(store.getInt(rec, 27));
	}

	public Rules getUpRecord() {
		return new Rules(store, rec == 0 ? 0 : store.getInt(rec, 27));
	}

	@Override
	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		write.field("set", getSet(), false);
		write.sub("stats", false);
		for (StatsArray sub: getStats())
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

	public boolean parseKey(Parser parser, Rules parentRec) {
		Rules parent = parentRec == null ? new Rules(store) : parentRec;
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
		if (parser.hasSub("stats")) {
			StatsArray sub = new StatsArray(record);
			while (parser.getSub()) {
				sub.add();
				sub.parse(parser);
			}
		}
	}
}
