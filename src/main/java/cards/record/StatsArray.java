package cards.record;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for stats
 */

public class StatsArray implements Iterable<StatsArray>, Iterator<StatsArray>{
	private final Store store;
	private final int rec;
	private int idx;
	private int alloc;
	private int size;

	public StatsArray(Card record) {
		this.store = record.store;
		this.rec = record.rec;
		this.idx = -1;
		this.alloc = store.getInt(rec, 14);
		this.size = store.getInt(rec, 10);
	}

	public StatsArray(StatsArray other) {
		this.store = other.store;
		this.rec = other.rec;
		this.idx = other.idx;
		this.alloc = other.alloc;
		this.size = other.size;
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

	public StatsArray add() {
		idx = size;
		if (alloc == 0)
			alloc = store.allocate(4);
		else
			alloc = store.resize(alloc, (11 + (idx + 1) * 5) / 8);
		store.setInt(rec, 14, alloc);
		size = idx + 1;
		store.setInt(rec, 10, size);
		return this;
	}

	@Override
	public Iterator<StatsArray> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return idx + 1 < size;
	}

	@Override
	public StatsArray next() {
		idx++;
		return new StatsArray(this);
	}

	public void getStatistic(Statistic value) {
		value.setRec(store.getInt(rec, 0));
	}

	public Statistic getStatistic() {
		return new Statistic(store, alloc == 0 || idx < 0 || idx >= size ? 0 : store.getInt(alloc, idx * 5 + 4));
	}

	public void setStatistic(Statistic value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setInt(alloc, idx * 5 + 4, value == null ? 0 : value.getRec());
	}

	public byte getValue() {
		return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 5 + 8);
	}

	public void setValue(byte value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 5 + 8, value);
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.strField("statistic", "{" + getStatistic().keys() + "}", true);
		write.field("value", getValue(), false);
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
		StatsArray record = this;
		record.setStatistic(null);
		record.setValue((byte)0);
		parser.getRelation("statistic", (int recNr) -> {
			Statistic rec = new Statistic(store);
			boolean found = rec.parseKey(parser);
			idx=recNr;
			record.setStatistic(rec);
			return found;
		}, idx);
		record.setValue((byte) parser.getInt("value"));
	}
}