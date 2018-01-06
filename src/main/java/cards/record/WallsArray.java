package cards.record;

import java.io.IOException;
import java.util.Iterator;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.Store;
import org.apache.commons.lang.StringUtils;

/**
 * Automatically generated record class for walls
 */

public class WallsArray implements Iterable<WallsArray>, Iterator<WallsArray>{
	private final Store store;
	private final int rec;
	private int idx;
	private int alloc;
	private int size;

	public WallsArray(Game record) {
		this.store = record.store;
		this.rec = record.rec;
		this.idx = -1;
		this.alloc = store.getInt(rec, 24);
		this.size = store.getInt(rec, 20);
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

	public WallsArray add() {
		idx = size;
		if (alloc == 0)
			alloc = store.allocate(10);
		else
			alloc = store.resize(alloc, 1 + idx * 16 / 8);
		store.setInt(rec, 24, alloc);
		size = idx + 1;
		store.setInt(rec, 20, size);
		return this;
	}

	@Override
	public Iterator<WallsArray> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return idx + 1 < size;
	}

	@Override
	public WallsArray next() {
		idx++;
		return this;
	}

	public String getName() {
		return alloc == 0 || idx < 0 || idx >= size ? null : store.getString(store.getInt(alloc, idx * 16 + 4));
	}

	public void setName(String value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setInt(alloc, idx * 16 + 4, store.putString(value));
	}

	public byte getThickness() {
		return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 16 + 8);
	}

	public void setThickness(byte value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 16 + 8, value);
	}

	public boolean isSloped() {
		return alloc == 0 || idx < 0 || idx >= size ? false : (store.getByte(alloc, idx * 16 + 9) & 1) > 0;
	}

	public void setSloped(boolean value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 16 + 9, (store.getByte(alloc, idx * 16 + 9) & 254) + (value ? 1 : 0));
	}

	public byte getCombineLevel() {
		return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 16 + 10);
	}

	public void setCombineLevel(byte value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 16 + 10, value);
	}

	public void getMaterial(Material value) {
		value.setRec(store.getInt(rec, 7));
	}

	public Material getMaterial() {
		return new Material(store, alloc == 0 || idx < 0 || idx >= size ? 0 : store.getInt(alloc, idx * 16 + 11));
	}

	public void setMaterial(Material value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setInt(alloc, idx * 16 + 11, value == null ? 0 : value.getRec());
	}

	public void getItem(Item value) {
		value.setRec(store.getInt(rec, 11));
	}

	public Item getItem() {
		return new Item(store, alloc == 0 || idx < 0 || idx >= size ? 0 : store.getInt(alloc, idx * 16 + 15));
	}

	public void setItem(Item value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setInt(alloc, idx * 16 + 15, value == null ? 0 : value.getRec());
	}

	public boolean isInwards() {
		return alloc == 0 || idx < 0 || idx >= size ? false : (store.getByte(alloc, idx * 16 + 19) & 1) > 0;
	}

	public void setInwards(boolean value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 16 + 19, (store.getByte(alloc, idx * 16 + 19) & 254) + (value ? 1 : 0));
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		write.field("thickness", getThickness(), false);
		write.field("sloped", isSloped(), false);
		write.field("combineLevel", getCombineLevel(), false);
		write.field("material", "{" + getMaterial().keys() + "}", false);
		write.field("item", "{" + getItem().keys() + "}", false);
		write.field("inwards", isInwards(), false);
		write.endRecord();
	}

	public void parse(Parser parser) {
		WallsArray record = this;
			record.setName(parser.getString("name"));
			record.setThickness((byte) parser.getInt("thickness"));
			record.setSloped(StringUtils.equals(parser.getString("sloped"), "true"));
			record.setCombineLevel((byte) parser.getInt("combineLevel"));
			parser.getRelation("material", () -> {
				Material rec = new Material(store);
				boolean found = rec.parseKey(parser);
				record.setMaterial(rec);
				return found;
			});
			parser.getRelation("item", () -> {
				Item rec = new Item(store);
				boolean found = rec.parseKey(parser);
				record.setItem(rec);
				return found;
			});
			record.setInwards(StringUtils.equals(parser.getString("inwards"), "true"));
	}
}