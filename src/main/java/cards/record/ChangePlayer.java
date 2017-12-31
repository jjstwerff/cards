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
		store.setInt(rec, 24, 0); // SET member
	}

	public ChangePlayer(Player current) {
		super(current.store, current.getRec());
		new IndexPlayerName().remove(getRec());
	}

	public void setCreation(LocalDateTime value) {
		store.setLong(rec, 4, value == null ? 0 : DateTime.getLong(value));
	}

	public void setLast(LocalDateTime value) {
		store.setLong(rec, 12, value == null ? 0 : DateTime.getLong(value));
	}

	public void setName(String value) {
		store.setInt(rec, 20, store.putString(value));
	}

	@Override
	public void close() {
		new IndexPlayerName().insert(getRec());
	}
}