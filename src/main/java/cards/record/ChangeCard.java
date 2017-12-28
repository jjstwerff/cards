package cards.record;

import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Card
 */
public class ChangeCard extends Card implements AutoCloseable {
	public ChangeCard(Store store) {
		super(store, store.allocate(Card.SIZE));
		setName(null);
		setSet(null);
		store.setInt(rec, 6, 0);
		store.setInt(rec, 10, 0);
	}

	public ChangeCard(Card current) {
		super(current.store, current.getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 0, store.putString(value));
	}

	public void setSet(Card.Set value) {
		if (value == null)
				store.setShort(rec, 4, 0);
			else
				store.setShort(rec, 4, 1 + value.ordinal());
	}

	@Override
	public void close() {
	}
}