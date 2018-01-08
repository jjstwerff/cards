package cards.record;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for encounter
 */

public class EncounterArray implements Iterable<EncounterArray>, Iterator<EncounterArray>{
	private final Store store;
	private final int rec;
	private int idx;
	private int alloc;
	private int size;

	public EncounterArray(Area record) {
		this.store = record.store;
		this.rec = record.rec;
		this.idx = -1;
		this.alloc = store.getInt(rec, 16);
		this.size = store.getInt(rec, 12);
	}

	public int getSize() {
		return size;
	}

	public int getIndex() {
		return this.idx;
	}

	public void setIndex(int index) {
		this.idx = index;
	}

	public EncounterArray add() {
		idx = size;
		if (alloc == 0)
			alloc = store.allocate(3);
		else
			alloc = store.resize(alloc, (11 + (idx + 1) * 4) / 8);
		store.setInt(rec, 16, alloc);
		size = idx + 1;
		store.setInt(rec, 12, size);
		return this;
	}

	@Override
	public Iterator<EncounterArray> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return idx + 1 < size;
	}

	@Override
	public EncounterArray next() {
		idx++;
		return this;
	}

	public void getCard(Card value) {
		value.setRec(store.getInt(rec, 0));
	}

	public Card getCard() {
		return new Card(store, alloc == 0 || idx < 0 || idx >= size ? 0 : store.getInt(alloc, idx * 4 + 4));
	}

	public void setCard(Card value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setInt(alloc, idx * 4 + 4, value == null ? 0 : value.getRec());
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.strField("card", "{" + getCard().keys() + "}", true);
		write.endRecord();
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
		EncounterArray record = this;
		record.setCard(null);
		parser.getRelation("card", (int recNr) -> {
			Card rec = new Card(store);
			boolean found = rec.parseKey(parser, null);
			idx=recNr;
			record.setCard(rec);
			return found;
		}, idx);
	}
}