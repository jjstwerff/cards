package cards.record;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for areas
 */

public class AreasArray implements Iterable<AreasArray>, Iterator<AreasArray>{
	private final Store store;
	private final int rec;
	private int idx;
	private int alloc;
	private int size;

	public AreasArray(Game record) {
		this.store = record.store;
		this.rec = record.rec;
		this.idx = -1;
		this.alloc = store.getInt(rec, 16);
		this.size = store.getInt(rec, 12);
	}

	public AreasArray(AreasArray other) {
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

	public AreasArray add() {
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
	public Iterator<AreasArray> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return idx + 1 < size;
	}

	@Override
	public AreasArray next() {
		idx++;
		return new AreasArray(this);
	}

	public void getArea(Area value) {
		value.setRec(store.getInt(rec, 0));
	}

	public Area getArea() {
		return new Area(store, alloc == 0 || idx < 0 || idx >= size ? 0 : store.getInt(alloc, idx * 4 + 4));
	}

	public void setArea(Area value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setInt(alloc, idx * 4 + 4, value == null ? 0 : value.getRec());
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.strField("area", "{" + getArea().keys() + "}", true);
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
		AreasArray record = this;
		record.setArea(null);
		parser.getRelation("area", (int recNr) -> {
			Area rec = new Area(store);
			boolean found = rec.parseKey(parser, new Game(store, this.rec));
			idx=recNr;
			record.setArea(rec);
			return found;
		}, idx);
	}
}