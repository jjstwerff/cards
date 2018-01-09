package cards.record;

import java.io.IOException;
import java.io.StringWriter;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.RecordInterface;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Material
 */
public class Material implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 25;

	public Material(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Material(Store store, int rec) {
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

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 4));
	}

	public int getColor() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 8);
	}

	public void getUpRecord(Game value) {
		value.setRec(store.getInt(rec, 21));
	}

	public Game getUpRecord() {
		return new Game(store, rec == 0 ? 0 : store.getInt(rec, 21));
	}

	@Override
	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("name", getName(), true);
		write.field("color", getColor(), false);
		write.endRecord();
	}

	@Override
	public String keys() throws IOException {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Game").append("{").append(getUpRecord().keys()).append("}");
		res.append(", ");
		res.append("Name").append("=").append(getName());
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

	public void parse(Parser parser, Game parent) {
		while (parser.getSub()) {
			String name = parser.getString("name");
			Game.IndexMaterials idx = parent.new IndexMaterials(this, name);
			if (parser.isDelete(idx.nextRec)) {
				try (ChangeMaterial record = new ChangeMaterial(this)) {
					store.toFree(record.getRec());
					record.setRec(0);
				}
				continue;
			}
			if (idx.nextRec == 0) {
				try (ChangeMaterial record = new ChangeMaterial(parent)) {
					record.setName(name);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeMaterial record = new ChangeMaterial(this)) {
					parseFields(parser, record);
				}
			}
		}
	}

	public boolean parseKey(Parser parser, Game parentRec) {
		Game parent = parentRec == null ? new Game(store) : parentRec;
		parser.getRelation("Game", (int recNr) -> {
			parent.setRec(recNr);
			parent.parseKey(parser);
			return true;
		}, getRec());
		String name = parser.getRelationString("name");
		Game.IndexMaterials idx = parent.new IndexMaterials(this, name);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeMaterial record) {
		record.setColor(parser.getInt("color"));
	}
}
