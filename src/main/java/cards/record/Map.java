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
	/* package private */ static int SIZE = 37;

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

	public DataArray getData() {
		return new DataArray();
	}

	public class DataArray implements Iterable<DataArray>, Iterator<DataArray>{
		int idx = -1;
		int alloc = store.getInt(rec, 20);
		int size = store.getInt(rec, 16);

		public int getSize() {
			return size;
		}

		public DataArray add() {
			idx = size;
			if (alloc == 0)
				alloc = store.allocate(5);
			else
				alloc = store.resize(alloc, 1 + idx * 8 / 8);
			store.setInt(rec, 20, alloc);
			size = idx + 1;
			store.setInt(rec, 16, size);
			return this;
		}

		@Override
		public Iterator<DataArray> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return idx + 1 < size;
		}

		@Override
		public DataArray next() {
			idx++;
			return this;
		}

		public byte getWallL() {
			return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 8 + 4);
		}

		public void setWallL(byte value) {
			if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 8 + 4, value);
		}

		public byte getWallT() {
			return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 8 + 5);
		}

		public void setWallT(byte value) {
			if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 8 + 5, value);
		}

		public byte getWallR() {
			return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 8 + 6);
		}

		public void setWallR(byte value) {
			if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 8 + 6, value);
		}

		public byte getFloor() {
			return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 8 + 7);
		}

		public void setFloor(byte value) {
			if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 8 + 7, value);
		}

		public byte getItem() {
			return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 8 + 8);
		}

		public void setItem(byte value) {
			if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 8 + 8, value);
		}

		public byte getRotation() {
			return alloc == 0 || idx < 0 || idx >= size ? 0 : store.getByte(alloc, idx * 8 + 9);
		}

		public void setRotation(byte value) {
			if (alloc != 0 && idx >= 0 && idx < size)
				store.setByte(alloc, idx * 8 + 9, value);
		}

		public short getHeight() {
			return alloc == 0 || idx < 0 || idx >= size ? Short.MIN_VALUE : store.getShort(alloc, idx * 8 + 10);
		}

		public void setHeight(short value) {
			if (alloc != 0 && idx >= 0 && idx < size)
				store.setShort(alloc, idx * 8 + 10, value);
		}

		public void output(Write write, int iterate) throws IOException {
			if (rec == 0 || iterate <= 0)
				return;
			write.field("wallL", getWallL(), true);
			write.field("wallT", getWallT(), false);
			write.field("wallR", getWallR(), false);
			write.field("floor", getFloor(), false);
			write.field("item", getItem(), false);
			write.field("rotation", getRotation(), false);
			write.field("height", getHeight(), false);
			write.endRecord();
		}

		public void parse(Parser parser) {
			DataArray record = this;
			record.setWallL((byte) parser.getInt("wallL"));
			record.setWallT((byte) parser.getInt("wallT"));
			record.setWallR((byte) parser.getInt("wallR"));
			record.setFloor((byte) parser.getInt("floor"));
			record.setItem((byte) parser.getInt("item"));
			record.setRotation((byte) parser.getInt("rotation"));
			record.setHeight((short) parser.getInt("height"));
		}
	}

	public void getUpRecord(Area value) {
		value.setRec(store.getInt(rec, 33));
	}

	public Area getUpRecord() {
		return new Area(store, rec == 0 ? 0 : store.getInt(rec, 33));
	}

	@Override
	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("X", getX(), true);
		write.field("Y", getY(), false);
		write.field("Z", getZ(), false);
		write.sub("data", false);
		for (DataArray sub: getData())
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
			int X = parser.getInt("X");
			int Y = parser.getInt("Y");
			int Z = parser.getInt("Z");
			Area.IndexMaps idx = parent.new IndexMaps(this, X, Y, Z);
			if (idx.nextRec == 0) {
				try (ChangeMap record = new ChangeMap(parent)) {
					record.setX(X);
					record.setY(Y);
					record.setZ(Z);
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
		int X = parser.getInt("X");
		int Y = parser.getInt("Y");
		int Z = parser.getInt("Z");
		Area.IndexMaps idx = parent.new IndexMaps(this, X, Y, Z);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeMap record) {
		if (parser.hasSub("data")) {
			DataArray sub = record.new DataArray();
			while (parser.getSub()) {
				sub.add();
				sub.parse(parser);
			}
		}
	}
}
