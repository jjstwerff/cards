package cards.record;

import java.io.IOException;
import java.io.StringWriter;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.RecordInterface;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Item
 */
public class Item implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 25;

	public Item(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Item(Store store, int rec) {
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


	public void getMaterial(Material value) {
		value.setRec(store.getInt(rec, 8));
	}

	public Material getMaterial() {
		return new Material(store, rec == 0 ? 0 : store.getInt(rec, 8));
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
		write.field("material", getMaterial(), false);
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
			Game.IndexItems idx = parent.new IndexItems(this, name);
			if (idx.nextRec == 0) {
				try (ChangeItem record = new ChangeItem(parent)) {
					record.setName(name);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeItem record = new ChangeItem(this)) {
					parseFields(parser, record);
				}
			}
		}
	}

	public boolean parseKey(Parser parser) {
		Game parent = new Game(store);
		parser.getRelation("Game", () -> {
			parent.parseKey(parser);
			return true;
		});
		String name = parser.getString("name");
		Game.IndexItems idx = parent.new IndexItems(this, name);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeItem record) {
		parser.getRelation("material", () -> {
			Material rec = new Material(store);
			boolean found = rec.parseKey(parser);
			record.setMaterial(rec);
			return found;
		});
	}
}
