package cards.record;

import java.io.IOException;
import java.io.StringWriter;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.RecordInterface;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Goal
 */
public class Goal implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 29;

	public Goal(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Goal(Store store, int rec) {
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

	public enum Type {
		KNOWLEDGE, WEAPON, WEARABLE, STATUS, IMPLANT
	};

	public Goal.Type getType() {
		int data = rec == 0 ? 0 : store.getShort(rec, 8);
		if (data <= 0)
			return null;
		return Type.values()[data - 1];
	}

	public int getXP() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 10);
	}

	public enum Gained {
		STASH, OVERHEAR, REWARD
	};

	public Goal.Gained getGained() {
		int data = rec == 0 ? 0 : store.getShort(rec, 14);
		if (data <= 0)
			return null;
		return Gained.values()[data - 1];
	}

	public void getUpRecord(Area value) {
		value.setRec(store.getInt(rec, 25));
	}

	public Area getUpRecord() {
		return new Area(store, rec == 0 ? 0 : store.getInt(rec, 25));
	}

	@Override
	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		write.field("type", getType(), false);
		write.field("XP", getXP(), false);
		write.field("gained", getGained(), false);
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
			Area.IndexGoal idx = parent.new IndexGoal(this, name);
			if (idx.nextRec == 0) {
				try (ChangeGoal record = new ChangeGoal(parent)) {
					record.setName(name);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeGoal record = new ChangeGoal(this)) {
					parseFields(parser, record);
				}
			}
		}
	}

	public boolean parseKey(Parser parser, Area parentRec) {
		Area parent = parentRec == null ? new Area(store) : parentRec;
		parser.getRelation("Area", () -> {
			parent.parseKey(parser, null);
			return true;
		});
		String name = parser.getString("name");
		Area.IndexGoal idx = parent.new IndexGoal(this, name);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeGoal record) {
		record.setType(Type.valueOf(parser.getString("type")));
		record.setXP(parser.getInt("XP"));
		record.setGained(Gained.valueOf(parser.getString("gained")));
	}
}
