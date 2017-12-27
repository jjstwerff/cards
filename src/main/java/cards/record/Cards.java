package cards.record;

import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated project class for Cards
 */
public class Cards {
	private final Store store;

	public Cards(Store store) {
		this.store = store;
	}

	@Override
	public String toString() { 
		return store.toString();
	}
}
