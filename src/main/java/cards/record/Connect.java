package cards.record;

import java.io.IOException;
import java.io.StringWriter;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.RecordInterface;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Connect
 */
public class Connect implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 35;

	public Connect(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Connect(Store store, int rec) {
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

	public int getNr() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 4);
	}

	public enum Type {
		DOOR, CLIMB, LINE
	};

	public Connect.Type getType() {
		int data = rec == 0 ? 0 : store.getShort(rec, 8);
		if (data <= 0)
			return null;
		return Type.values()[data - 1];
	}

	public ChecksArray getChecks() {
		return new ChecksArray(this);
	}

	public void getUpRecord(Room value) {
		value.setRec(store.getInt(rec, 27));
	}

	public Room getUpRecord() {
		return new Room(store, rec == 0 ? 0 : store.getInt(rec, 27));
	}

	public void getTo(Room value) {
		value.setRec(store.getInt(rec, 31));
	}

	public Room getTo() {
		return new Room(store, rec == 0 ? 0 : store.getInt(rec, 31));
	}

	@Override
	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("nr", getNr(), true);
		write.field("type", getType(), false);
		write.sub("checks", false);
		for (ChecksArray sub: getChecks())
			sub.output(write, iterate - 1);
		write.endSub();
		write.field("to", getTo(), false);
		write.endRecord();
	}

	@Override
	public String keys() throws IOException {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Room").append("{").append(getUpRecord().keys()).append("}");
		res.append(", ");
		res.append("Nr").append("=").append(getNr());
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

	public void parse(Parser parser, Room parent) {
		while (parser.getSub()) {
			int nr = parser.getInt("nr");
			Room.IndexConnection idx = parent.new IndexConnection(this, nr);
			if (idx.nextRec == 0) {
				try (ChangeConnect record = new ChangeConnect(parent)) {
					record.setNr(nr);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeConnect record = new ChangeConnect(this)) {
					parseFields(parser, record);
				}
			}
		}
	}

	public boolean parseKey(Parser parser, Room parentRec) {
		Room parent = parentRec == null ? new Room(store) : parentRec;
		parser.getRelation("Room", () -> {
			parent.parseKey(parser, null);
			return true;
		});
		int nr = parser.getInt("nr");
		Room.IndexConnection idx = parent.new IndexConnection(this, nr);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeConnect record) {
		record.setType(Type.valueOf(parser.getString("type")));
		if (parser.hasSub("checks")) {
			ChecksArray sub = new ChecksArray(record);
			while (parser.getSub()) {
				sub.add();
				sub.parse(parser);
			}
		}
		parser.getRelation("to", () -> {
			Room rec = new Room(store);
			boolean found = rec.parseKey(parser, null);
			record.setTo(rec);
			return found;
		});
	}
}
