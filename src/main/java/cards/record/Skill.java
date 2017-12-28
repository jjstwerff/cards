package cards.record;

import java.io.IOException;
import java.io.StringWriter;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Skill
 */
public class Skill {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 27;

	public Skill(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Skill(Store store, int rec) {
		rec = store.correct(rec);
		this.store = store;
		this.rec = rec;
	}

	public int getRec() {
		return rec;
	}

	public void setRec(int rec) {
		assert store.validate(rec);
		this.rec = rec;
	}

	public void getCard(Card value) {
		value.setRec(store.getInt(rec, 8));
	}

	public Card getCard() {
		return new Card(store, rec == 0 ? 0 : store.getInt(rec, 8));
	}

	public enum State {
		STASHED, PARTY, ACTIVE
	};

	public Skill.State getState() {
		int data = rec == 0 ? 0 : store.getShort(rec, 12);
		if (data <= 0)
			return null;
		return State.values()[data - 1];
	}

	public void getUpRecord(Character value) {
		value.setRec(store.getInt(rec, 23));
	}

	public Character getUpRecord() {
		return new Character(store, rec == 0 ? 0 : store.getInt(rec, 23));
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("card", "{" + getCard().toKeyString() + "}", true);
		write.field("state", getState(), false);
	}

	public String toKeyString() {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Character").append("={").append(getUpRecord().toKeyString()).append("}");
		res.append(", ");
		res.append("CardName").append("=").append(getCard().getName());
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

	public void parse(Parser parser, Character parent) {
		while (parser.getSub()) {
			Card card = new Card(store);
			parser.getRelation("card", () -> {
				card.parseKey(parser);
				return true;
			});
			Character.IndexSkills idx = parent.new IndexSkills(this, card);
			if (idx.nextRec == 0) {
				try (ChangeSkill record = new ChangeSkill(parent)) {
					record.setCard(card);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeSkill record = new ChangeSkill(this)) {
					parseFields(parser, record);
				}
			}
		}
	}

	public boolean parseKey(Parser parser) {
		Character parent = new Character(store);
		parser.getRelation("Character", () -> {
			parent.parseKey(parser);
			return true;
		});
		Card card = new Card(store);
		parser.getRelation("card", () -> {
			card.parseKey(parser);
			return true;
		});
		Character.IndexSkills idx = parent.new IndexSkills(this, card);
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeSkill record) {
		record.setState(State.valueOf(parser.getString("state")));
	}
}
