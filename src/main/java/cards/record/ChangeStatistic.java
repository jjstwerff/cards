package cards.record;

import com.betterbe.memorydb.structure.Store;

/**
 * Automatically generated record class for table Statistic
 */
public class ChangeStatistic extends Statistic implements AutoCloseable {
	public ChangeStatistic(Store store) {
		super(store, store.allocate(Statistic.SIZE));
		setName(null);
		setNr(0);
	}

	public ChangeStatistic(Statistic current) {
		super(current.store, current.getRec());
		new IndexStatisticName().remove(getRec());
		new IndexStatisticNr().remove(getRec());
	}

	public void setName(String value) {
		store.setInt(rec, 4, store.putString(value));
	}

	public void setNr(int value) {
		store.setInt(rec, 8, value);
	}

	@Override
	public void close() throws Exception {
		new IndexStatisticName().insert(getRec());
		new IndexStatisticNr().insert(getRec());
	}
}