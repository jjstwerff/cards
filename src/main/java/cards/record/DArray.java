package cards.record;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for d
 */

public class DArray implements Iterable<DArray>, Iterator<DArray>{
	private final Store store;
	private final int rec;
	private int idx;
	private int alloc;
	private int size;

	public DArray(Map record) {
		this.store = record.store;
		this.rec = record.rec;
		this.idx = -1;
		this.alloc = store.getInt(rec, 24);
		this.size = store.getInt(rec, 20);
	}

	public DArray(DArray other) {
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

	public DArray add() {
		idx = size;
		if (alloc == 0)
			alloc = store.allocate(6);
		else
			alloc = store.resize(alloc, (11 + (idx + 1) * 8) / 8);
		store.setInt(rec, 24, alloc);
		size = idx + 1;
		store.setInt(rec, 20, size);
		return this;
	}

	@Override
	public Iterator<DArray> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return idx + 1 < size;
	}

	@Override
	public DArray next() {
		idx++;
		return new DArray(this);
	}

	public byte getL() {
		return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 8 + 4);
	}

	public void setL(byte value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 8 + 4, value);
	}

	public byte getT() {
		return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 8 + 5);
	}

	public void setT(byte value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 8 + 5, value);
	}

	public byte getR() {
		return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 8 + 6);
	}

	public void setR(byte value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 8 + 6, value);
	}

	public byte getF() {
		return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 8 + 7);
	}

	public void setF(byte value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 8 + 7, value);
	}

	public byte getI() {
		return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 8 + 8);
	}

	public void setI(byte value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 8 + 8, value);
	}

	public byte getD() {
		return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 8 + 9);
	}

	public void setD(byte value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 8 + 9, value);
	}

	public short getH() {
		return alloc == 0 || idx < 0 || idx >= size ? Short.MIN_VALUE : store.getShort(alloc, idx * 8 + 10);
	}

	public void setH(short value) {
		if (alloc != 0 && idx >= 0 && idx < size)
				store.setShort(alloc, idx * 8 + 10, value);
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("l", getL(), true);
		write.field("t", getT(), false);
		write.field("r", getR(), false);
		write.field("f", getF(), false);
		write.field("i", getI(), false);
		write.field("d", getD(), false);
		write.field("h", getH(), false);
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
		DArray record = this;
		record.setL((byte)0);
		record.setT((byte)0);
		record.setR((byte)0);
		record.setF((byte)0);
		record.setI((byte)0);
		record.setD((byte)0);
		record.setH((short)0);
		record.setL((byte) parser.getInt("l"));
		record.setT((byte) parser.getInt("t"));
		record.setR((byte) parser.getInt("r"));
		record.setF((byte) parser.getInt("f"));
		record.setI((byte) parser.getInt("i"));
		record.setD((byte) parser.getInt("d"));
		record.setH((short) parser.getInt("h"));
	}
}