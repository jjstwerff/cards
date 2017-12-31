package cards.record;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.IndexOperation;
import com.betterbe.memorydb.structure.Key;
import com.betterbe.memorydb.structure.RedBlackTree;
import com.betterbe.memorydb.structure.RecordInterface;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Character
 */
public class Character implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 25;

	public Character(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Character(Store store, int rec) {
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

	public IndexSkills getSkills() {
		return new IndexSkills(new Skill(store));
	}

	public class IndexSkills extends RedBlackTree implements Iterable<Skill>, Iterator<Skill> {
		Key key = null;
		Skill record;
		int nextRec;

		public IndexSkills(Skill record) {
			this.record = record;
			record.store = store;
			this.key = null;
			nextRec = first();
		}

		public IndexSkills(Skill record, String key1) {
			this.record = record;
			record.store = store;
			this.key = new Key() {
				@Override
				public int compareTo(int recNr) {
					if (recNr < 0)
						return -1;
					assert store.validate(recNr);
					record.setRec(recNr);
					int o = 0;
					o = RedBlackTree.compare(key1, record.getCard().getName());
					return o;
				}

				@Override
				public IndexOperation oper() {
					return IndexOperation.EQ;
				}
			};
			nextRec = find(this.key);
		}

		@Override
		public Iterator<Skill> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return nextRec != 0;
		}

		@Override
		public Skill next() {
			int n = nextRec;
			nextRec = next(nextRec);
			if (key != null) {
				while (nextRec != 0 && key.compareTo(nextRec) != 0) {
					nextRec = next(nextRec);
				}
			}
			record.setRec(n);
			return record;
		}

		@Override
		protected boolean readRed(int recNr) {
			assert store.validate(recNr);
			return (store.getByte(recNr, 10) & 1) > 0;
		}

		@Override
		protected void changeRed(int recNr, boolean value) {
			assert store.validate(recNr);
			store.setByte(recNr, 10, (store.getByte(rec, 10) & 254) + (value ? 1 : 0));
		}

		@Override
		protected int readLeft(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 11);
		}

		@Override
		protected void changeLeft(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 11, value);
		}

		@Override
		protected int readRight(int recNr) {
			assert store.validate(recNr);
			return store.getInt(recNr, 15);
		}

		@Override
		protected void changeRight(int recNr, int value) {
			assert store.validate(recNr);
			store.setInt(recNr, 15, value);
		}

		@Override
		protected int readTop() {
			return store.getInt(Character.this.getRec(), 8);
		}

		@Override
		protected void changeTop(int value) {
			store.setInt(Character.this.getRec(), 8, value);
		}

		@Override
		protected int compareTo(int a, int b) {
			Skill recA = new Skill(store, a);
			Skill recB = new Skill(store, b);
			int o = 0;
			o = compare(recA.getCard().getName(), recB.getCard().getName());
			if (o == 0)
				return o;
			return o;
		}

		@Override
		protected String toString(int rec) {
			return new Character(store, rec).toString();
		}
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
		write.sub("skills", false);
		for (Skill sub : getSkills())
			sub.output(write, iterate - 1);
		write.endSub();
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
			Game.IndexCharacters idx = parent.new IndexCharacters(this, name);
			if (idx.nextRec == 0) {
				try (ChangeCharacter record = new ChangeCharacter(parent)) {
					record.setName(name);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeCharacter record = new ChangeCharacter(this)) {
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
		Game.IndexCharacters idx = parent.new IndexCharacters(this, name);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeCharacter record) {
		if (parser.hasSub("skills"))
			new Skill(store).parse(parser, record);
	}
}
