package ru.c17.balance.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import ru.c17.balance.data.Period;
import ru.c17.balance.data.Receipt;

@Dao
public abstract class ReceiptWithPeriodDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract long insertPeriod(Period period);

    @Insert
    abstract long insertReceipt(Receipt receipt);

    @Update
    abstract void updateReceipt(Receipt receipt);

    @Delete
    abstract void deleteReceipt(Receipt receipt);

    @Query("DELETE FROM periods WHERE id = :periodId")
    abstract void deletePeriod(long periodId);

    @Query("SELECT * FROM periods where month = :month AND year = :year")
    abstract Period getPeriod(int year, int month);

    @Query("SELECT COUNT(id) FROM receipts WHERE period_id = :periodId")
    public abstract Integer getCount(long periodId);

    @Transaction
    public long insertReceiptWithPeriod(Receipt receipt) {
        Period period = getPeriod(receipt.getPeriod().getYear(), receipt.getPeriod().getMonth());
        long periodId;
        if (period != null) {
            periodId = period.getId();
        } else {
            periodId = insertPeriod(receipt.getPeriod());
        }
        receipt.setPeriodId(periodId);
        return insertReceipt(receipt);
    }

    public void updateReceiptWithPeriod(Receipt receipt) {
        Period period = getPeriod(receipt.getPeriod().getYear(), receipt.getPeriod().getMonth());
        long periodId;
        if (period != null) {
            receipt.setPeriodId(period.getId());
        } else {
            receipt.setPeriodId(insertPeriod(receipt.getPeriod()));
        }
        updateReceipt(receipt);
    }

    @Transaction
    public void deleteReceiptClean(Receipt receipt) {
        deleteReceipt(receipt);
        long periodId = receipt.getPeriodId();
        int count = getCount(periodId);
        if (count == 0) {
            deletePeriod(periodId);
        }
    }
}
