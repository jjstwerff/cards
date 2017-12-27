package cards.record;

import java.io.IOException;
import java.io.StringWriter;

import com.betterbe.memorydb.structure.Store;
import com.betterbe.memorydb.file.Write;

/**
 * Automatically generated record class for table Member
 */
public class Member {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 31;

	public Member(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Member(Store store, int rec) {
		rec = store.correct(rec);
		this.store = store;
		this.rec = rec;
	}

	public int getRec() {
		return rec;
	}

	public void setRec(int rec) {
		assert(store.validate(rec));
		this.rec = rec;
	}

	public void getGame(Game value) {
		value.setRec(store.getInt(rec, 8));
	}

	public Game getGame() {
		return new Game(store, rec == 0 ? 0 : store.getInt(rec, 8));
	}

	public enum Role {
		PLAYER, MASTER, OBSERVER
	};

	public Member.Role getRole() {
		int data = rec == 0 ? 0 : store.getShort(rec, 12);
		if (data <= 0)
			return null;
		return Role.values()[data - 1];
	}

	public int getXp() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 14);
	}

	public void getUpRecord(Player value) {
		value.setRec(store.getInt(rec, 27));
	}

	public Player getUpRecord() {
		return new Player(store, rec == 0 ? 0 : store.getInt(rec, 27));
	}

	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("game", "{" + getGame().toKeyString() + "}", true);
		write.field("role", getRole(), false);
		write.field("xp", getXp(), false);
	}

	public String toKeyString() {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Player").append("={").append(getUpRecord().toKeyString()).append("}");
		res.append(", ");
		res.append("GameName").append("=").append(getGame().getName());
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
}
