package cards.record;

import com.betterbe.memorydb.structure.Store;
import com.betterbe.memorydb.structure.DateTime;
import java.time.LocalDateTime;

/**
 * Automatically generated record class for table Player
 */
public class ChangePlayer extends Player implements AutoCloseable {
	public ChangePlayer(Store store) {
		super(store, store.allocate(Player.SIZE));
		setCreation(null);
		setLast(null);
		setName(null);
		store.setInt(rec, 24, 0);
	}

	public ChangePlayer(Player current) {
		super(current.store, current.getRec());
		new IndexPlayerName().remove(getRec());
	}

	public LocalDateTime getCreation() {
		return rec == 0 ? null : DateTime.of(store.getLong(rec, 4));
	}

	public void setCreation(LocalDateTime value) {
		store.setLong(rec, 4, value == null ? 0 : DateTime.getLong(value));
	}

	public LocalDateTime getLast() {
		return rec == 0 ? null : DateTime.of(store.getLong(rec, 12));
	}

	public void setLast(LocalDateTime value) {
		store.setLong(rec, 12, value == null ? 0 : DateTime.getLong(value));
	}

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 20));
	}

	public void setName(String value) {
		store.setInt(rec, 20, store.putString(value));
	}

	public IndexMember getMember() {
		return new IndexMember(new Member(store));
	}

	@Override
	public void close() throws Exception {
		new IndexPlayerName().insert(getRec());
	}
}