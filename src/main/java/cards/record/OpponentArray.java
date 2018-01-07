package cards.record;

import java.io.IOException;
import java.util.Iterator;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for opponent
 */

public class OpponentArray implements Iterable<OpponentArray>, Iterator<OpponentArray>{
	private final Store store;
	private final int rec;
	private int idx;
	private int alloc;
	private int size;

	public OpponentArray(Room record) {
		this.store = record.store;
		this.rec = record.rec;
		this.idx = -1;
		this.alloc = store.getInt(rec, 12);
		this.size = store.getInt(rec, 8);
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

	public OpponentArray add() {
		idx = size;
		if (alloc == 0)
			alloc = store.allocate(3);
		else
			alloc = store.resize(alloc, (11 + (idx + 1) * 4) / 8);
		store.setInt(rec, 12, alloc);
		size = idx + 1;
		store.setInt(rec, 8, size);
		return this;
	}

	@Override
	public Iterator<OpponentArray> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return idx + 1 < size;
	}

	@Override
	public OpponentArray next() {
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
		write.field("card", "{" + getCard().keys() + "}", true);
		write.endRecord();
	}

	public void parse(Parser parser) {
		OpponentArray record = this;
			parser.getRelation("card", () -> {
				Card rec = new Card(store);
				boolean found = rec.parseKey(parser, null);
				record.setCard(rec);
				return found;
			});
	}
}