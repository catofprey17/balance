package ru.c17.balance.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import ru.c17.balance.data.Receipt;

import java.util.List;

@Dao
public interface ReceiptDao {

    @Query("SELECT * FROM receipts ORDER BY time DESC")
    LiveData<List<Receipt>> getAll();

    @Query("SELECT * FROM receipts WHERE id = :id")
    Receipt getById(long id);

    @Query("SELECT * FROM receipts WHERE id = :id")
    LiveData<Receipt> getLiveDataById(long id);

    @Query("SELECT * FROM receipts WHERE fn = :fn AND fd = :fd AND fp = :fp")
    Receipt getByFnFdFp(String fn, String fd, String fp);

//    @Query("SELECT DISTINCT year, month FROM receipts")
//    LiveData<List<Period>> getPeriods();

    @Query("SELECT * FROM receipts WHERE period_id = :periodId")
    LiveData<List<Receipt>> getAllByPeriod(long periodId);

    @Query("SELECT * FROM receipts WHERE period_id = :periodId")
    List<Receipt> getAllByPeriodOld(long periodId);

    @Query("DELETE FROM receipts WHERE id = :id")
    void deleteReceiptById(long id);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Receipt receipt);

    @Update
    void update(Receipt receipt);

    @Delete
    void delete(Receipt receipt);

    @Query("DELETE FROM receipts")
    void dropTable();


}
