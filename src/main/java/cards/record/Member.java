package cards.record;

import java.io.IOException;
import java.io.StringWriter;

import com.betterbe.memorydb.file.Parser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.RecordInterface;
import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Member
 */
public class Member implements RecordInterface {
	/* package private */ Store store;
	protected int rec;
	/* package private */ static int SIZE = 27;

	public Member(Store store) {
		this.store = store;
		this.rec = 0;
	}

	public Member(Store store, int rec) {
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

	public void getGame(Game value) {
		value.setRec(store.getInt(rec, 4));
	}

	public Game getGame() {
		return new Game(store, rec == 0 ? 0 : store.getInt(rec, 4));
	}

	public enum Role {
		PLAYER, MASTER, OBSERVER
	};

	public Member.Role getRole() {
		int data = rec == 0 ? 0 : store.getShort(rec, 8);
		if (data <= 0)
			return null;
		return Role.values()[data - 1];
	}

	public int getXp() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 10);
	}

	public void getUpRecord(Player value) {
		value.setRec(store.getInt(rec, 23));
	}

	public Player getUpRecord() {
		return new Player(store, rec == 0 ? 0 : store.getInt(rec, 23));
	}

	@Override
	public void output(Write write, int iterate) throws IOException {
		if (rec == 0 || iterate <= 0)
			return;
		write.field("game", getGame(), true);
		write.field("role", getRole(), false);
		write.field("xp", getXp(), false);
		write.endRecord();
	}

	@Override
	public String keys() throws IOException {
		StringBuilder res = new StringBuilder();
		if (rec == 0)
			return "";
		res.append("Player").append("{").append(getUpRecord().keys()).append("}");
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

	public void parse(Parser parser, Player parent) {
		while (parser.getSub()) {
			Game game = new Game(store);
			parser.getRelation("game", () -> {
				game.parseKey(parser);
				return true;
			});
			Player.IndexMember idx = parent.new IndexMember(this, game.getName());
			if (idx.nextRec == 0) {
				try (ChangeMember record = new ChangeMember(parent)) {
					record.setGame(game);
					parseFields(parser, record);
				}
			} else {
				rec = idx.nextRec;
				try (ChangeMember record = new ChangeMember(this)) {
					parseFields(parser, record);
				}
			}
		}
	}

	public boolean parseKey(Parser parser, Player parentRec) {
		Player parent = parentRec == null ? new Player(store) : parentRec;
		parser.getRelation("Player", () -> {
			parent.parseKey(parser);
			return true;
		});
		Game game = new Game(store);
		parser.getRelation("game", () -> {
			game.parseKey(parser);
			return true;
		});
		Player.IndexMember idx = parent.new IndexMember(this, game.getName());
		parser.finishRelation();
		rec = idx.nextRec;
		return idx.nextRec != 0;
	}

	private void parseFields(Parser parser, ChangeMember record) {
		record.setRole(Role.valueOf(parser.getString("role")));
		record.setXp(parser.getInt("xp"));
	}
}
