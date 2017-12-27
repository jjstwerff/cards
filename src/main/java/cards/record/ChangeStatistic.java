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

	public String getName() {
		return rec == 0 ? null : store.getString(store.getInt(rec, 4));
	}

	public void setName(String value) {
		store.setInt(rec, 4, store.putString(value));
	}

	public int getNr() {
		return rec == 0 ? Integer.MIN_VALUE : store.getInt(rec, 8);
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