package cards.record;

import java.io.IOException;
import java.io.StringWriter;

import com.betterbe.memorydb.structure.Store;
import com.betterbe.memorydb.file.Write;

/**
 * Automatically generated record class for table Goal
 */
public class Goal {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 33;

	public Goal(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Goal(Store store, int rec) {
		rec = store.correct(rec);
		this.store = store;
		this.rec = rec;
	}

	public int getRec() {
		return rec;
	}

	public void setRec(int rec) {
		assert(store.validate(rec));
		this.rec = rec;
	}

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 8));
	}

	public enum Type {
		KNOWLEDGE, WEAPON, WEARABLE, STATUS, IMPLANT
	};

	public Goal.Type getType() {
		int data = rec == 0 ? 0 : store.getShort(rec, 12);
		if (data <= 0)
			return null;
		return Type.values()[data - 1];
	}

	public int getXP() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 14);
	}

	public enum Gained {
		STASH, OVERHEAR, REWARD
	};

	public Goal.Gained getGained() {
		int data = rec == 0 ? 0 : store.getShort(rec, 18);
		if (data <= 0)
			return null;
		return Gained.values()[data - 1];
	}

	public void getUpRecord(Area value) {
		value.setRec(store.getInt(rec, 29));
	}

	public Area getUpRecord() {
		return new Area(store, rec == 0 ? 0 : store.getInt(rec, 29));
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		write.field("type", getType(), false);
		write.field("XP", getXP(), false);
		write.field("gained", getGained(), false);
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
}
