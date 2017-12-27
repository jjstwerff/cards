package cards.record;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.betterbe.memorydb.structure.Store;
import com.betterbe.memorydb.file.Write;

/**
 * Automatically generated record class for table Connect
 */
public class Connect {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 39;

	public Connect(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Connect(Store store, int rec) {
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

	public int getNr() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 8);
	}

	public enum Type {
		DOOR, CLIMB, LINE
	};

	public Connect.Type getType() {
		int data = rec == 0 ? 0 : store.getShort(rec, 12);
		if (data <= 0)
			return null;
		return Type.values()[data - 1];
	}

	public ChecksArray getChecks() {
		return new ChecksArray();
	}

	public class ChecksArray implements Iterable<ChecksArray>, Iterator<ChecksArray>{
		int idx = -1;

		public int getSize() {
			return store.getInt(rec, 14);
		}

		public ChecksArray add() {
			int p = store.getInt(rec, 18);
			idx = getSize();
			if (p == 0 || idx * 4 >= store.getInt(p, 0)) {
				store.setInt(p, 0, idx * 4);
				store.setInt(rec, 18, store.resize(p));
			}
			store.setInt(rec, 14, idx + 1);
			return this;
		}

		@Override
		public Iterator<ChecksArray> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return idx + 1 < getSize();
		}

		@Override
		public ChecksArray next() {
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

	public void getUpRecord(Room value) {
		value.setRec(store.getInt(rec, 31));
	}

	public Room getUpRecord() {
		return new Room(store, rec == 0 ? 0 : store.getInt(rec, 31));
	}

	public void getTo(Room value) {
		value.setRec(store.getInt(rec, 35));
	}

	public Room getTo() {
		return new Room(store, rec == 0 ? 0 : store.getInt(rec, 35));
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("nr", getNr(), true);
		write.field("type", getType(), false);
		for (ChecksArray sub: getChecks())
			sub.output(write, iterate - 1);
		write.field("to", "{" + getTo().toKeyString() + "}", false);
	}

	public String toKeyString() {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Room").append("={").append(getUpRecord().toKeyString()).append("}");
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
}
