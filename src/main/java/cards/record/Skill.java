package cards.record;

import java.io.IOException;
import java.io.StringWriter;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.RecordInterface;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Skill
 */
public class Skill implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 23;

	public Skill(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Skill(Store store, int rec) {
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

	public void getCard(Card value) {
		value.setRec(store.getInt(rec, 4));
	}

	public Card getCard() {
		return new Card(store, rec == 0 ? 0 : store.getInt(rec, 4));
	}

	public enum State {
		STASHED, PARTY, ACTIVE
	};

	public Skill.State getState() {
		int data = rec == 0 ? 0 : store.getShort(rec, 8);
		if (data <= 0)
			return null;
		return State.values()[data - 1];
	}

	public void getUpRecord(Character value) {
		value.setRec(store.getInt(rec, 19));
	}

	public Character getUpRecord() {
		return new Character(store, rec == 0 ? 0 : store.getInt(rec, 19));
	}

	@Override
	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("card", getCard(), true);
		write.field("state", getState(), false);
		write.endRecord();
	}

	@Override
	public String keys() throws IOException {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Character").append("{").append(getUpRecord().keys()).append("}");
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
			parser.getRelation("card", (int recNr) -> {
				card.parseKey(parser, null);
				return true;
			}, -1);
			Character.IndexSkills idx = parent.new IndexSkills(this, card.getName());
			if (parser.isDelete(idx.nextRec)) {
				try (ChangeSkill record = new ChangeSkill(this)) {
					store.free(record.getRec());
					record.setRec(0);
				}
				continue;
			}
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

	public boolean parseKey(Parser parser, Character parentRec) {
		Character parent = parentRec == null ? new Character(store) : parentRec;
		parser.getRelation("Character", (int recNr) -> {
			parent.setRec(recNr);
			parent.parseKey(parser, null);
			return true;
		}, getRec());
		Card card = new Card(store);
		parser.getRelation("card", (int recNr) -> {
			card.parseKey(parser, null);
			return true;
		}, -1);
		Character.IndexSkills idx = parent.new IndexSkills(this, card.getName());
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeSkill record) {
		record.setState(State.valueOf(parser.getString("state")));
	}
}
