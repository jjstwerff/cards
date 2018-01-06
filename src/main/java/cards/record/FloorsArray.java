package cards.record;

import java.io.IOException;
import java.util.Iterator;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.Store;
import org.apache.commons.lang.StringUtils;

/**
 * Automatically generated record class for floors
 */

public class FloorsArray implements Iterable<FloorsArray>, Iterator<FloorsArray>{
	private final Store store;
	private final int rec;
	private int idx;
	private int alloc;
	private int size;

	public FloorsArray(Game record) {
		this.store = record.store;
		this.rec = record.rec;
		this.idx = -1;
		this.alloc = store.getInt(rec, 32);
		this.size = store.getInt(rec, 28);
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

	public FloorsArray add() {
		idx = size;
		if (alloc == 0)
			alloc = store.allocate(7);
		else
			alloc = store.resize(alloc, 1 + idx * 11 / 8);
		store.setInt(rec, 32, alloc);
		size = idx + 1;
		store.setInt(rec, 28, size);
		return this;
	}

	@Override
	public Iterator<FloorsArray> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return idx + 1 < size;
	}

	@Override
	public FloorsArray next() {
		idx++;
		return this;
	}

	public String getName() {
		return alloc == 0 || idx < 0 || idx >= size ? null : store.getString(store.getInt(alloc, idx * 11 + 4));
	}

	public void setName(String value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setInt(alloc, idx * 11 + 4, store.putString(value));
	}

	public enum Type {
		OPEN, FILLED, INSIDE
	};

	public Type getType() {
		int data = alloc == 0 || idx < 0 || idx >= size ? 0 : store.getShort(rec, idx * 11 + 4);
		if (data <= 0)
			return null;
		return Type.values()[data - 1];
	}

	public void setType(Type value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				if (value == null)
				store.setShort(rec, idx * 11 + 4, 0);
			else
				store.setShort(rec, idx * 11 + 4, 1 + value.ordinal());
	}

	public boolean isSloped() {
		return alloc == 0 || idx < 0 || idx >= size ? false : (store.getByte(alloc, idx * 11 + 10) & 1) > 0;
	}

	public void setSloped(boolean value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 11 + 10, (store.getByte(alloc, idx * 11 + 10) & 254) + (value ? 1 : 0));
	}

	public void getMaterial(Material value) {
		value.setRec(store.getInt(rec, 7));
	}

	public Material getMaterial() {
		return new Material(store, alloc == 0 || idx < 0 || idx >= size ? 0 : store.getInt(alloc, idx * 11 + 11));
	}

	public void setMaterial(Material value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setInt(alloc, idx * 11 + 11, value == null ? 0 : value.getRec());
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		write.field("type", getType(), false);
		write.field("sloped", isSloped(), false);
		write.field("material", "{" + getMaterial().keys() + "}", false);
		write.endRecord();
	}

	public void parse(Parser parser) {
		FloorsArray record = this;
			record.setName(parser.getString("name"));
			record.setType(Type.valueOf(parser.getString("type")));
			record.setSloped(StringUtils.equals(parser.getString("sloped"), "true"));
			parser.getRelation("material", () -> {
				Material rec = new Material(store);
				boolean found = rec.parseKey(parser);
				record.setMaterial(rec);
				return found;
			});
	}
}