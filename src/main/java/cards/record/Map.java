package cards.record;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.RecordInterface;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Map
 */
public class Map implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 41;

	public Map(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Map(Store store, int rec) {
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

	public int getX() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 4);
	}

	public int getY() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 8);
	}

	public int getZ() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 12);
	}

	public int getL() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 16);
	}

	public DArray getD() {
		return new DArray();
	}

	public class DArray implements Iterable<DArray>, Iterator<DArray>{
		int idx = -1;
		int alloc = store.getInt(rec, 24);
		int size = store.getInt(rec, 20);

		public int getSize() {
			return size;
		}

		public DArray add() {
			idx = size;
			if (alloc == 0)
				alloc = store.allocate(5);
			else
				alloc = store.resize(alloc, 1 + idx * 8 / 8);
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
			return this;
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

		public void parse(Parser parser) {
			DArray record = this;
			record.setL((byte) parser.getInt("l"));
			record.setT((byte) parser.getInt("t"));
			record.setR((byte) parser.getInt("r"));
			record.setF((byte) parser.getInt("f"));
			record.setI((byte) parser.getInt("i"));
			record.setD((byte) parser.getInt("d"));
			record.setH((short) parser.getInt("h"));
		}
	}

	public void getUpRecord(Area value) {
		value.setRec(store.getInt(rec, 37));
	}

	public Area getUpRecord() {
		return new Area(store, rec == 0 ? 0 : store.getInt(rec, 37));
	}

	@Override
	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("x", getX(), true);
		write.field("y", getY(), false);
		write.field("z", getZ(), false);
		write.field("l", getL(), false);
		write.sub("d", false);
		for (DArray sub: getD())
			sub.output(write, iterate - 1);
		write.endSub();
		write.endRecord();
	}

	@Override
	public String keys() throws IOException {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Area").append("{").append(getUpRecord().keys()).append("}");
		res.append(", ");
		res.append("X").append("=").append(getX());
		res.append(", ");
		res.append("Y").append("=").append(getY());
		res.append(", ");
		res.append("Z").append("=").append(getZ());
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

	public void parse(Parser parser, Area parent) {
		while (parser.getSub()) {
			int x = parser.getInt("x");
			int y = parser.getInt("y");
			int z = parser.getInt("z");
			Area.IndexMaps idx = parent.new IndexMaps(this, x, y, z);
			if (idx.nextRec == 0) {
				try (ChangeMap record = new ChangeMap(parent)) {
					record.setX(x);
					record.setY(y);
					record.setZ(z);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeMap record = new ChangeMap(this)) {
					parseFields(parser, record);
				}
			}
		}
	}

	public boolean parseKey(Parser parser) {
		Area parent = new Area(store);
		parser.getRelation("Area", () -> {
			parent.parseKey(parser);
			return true;
		});
		int x = parser.getInt("x");
		int y = parser.getInt("y");
		int z = parser.getInt("z");
		Area.IndexMaps idx = parent.new IndexMaps(this, x, y, z);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeMap record) {
		record.setL(parser.getInt("l"));
		if (parser.hasSub("d")) {
			DArray sub = record.new DArray();
			while (parser.getSub()) {
				sub.add();
				sub.parse(parser);
			}
		}
	}
}
