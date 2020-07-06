package ru.c17.balance.data.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import ru.c17.balance.data.Period;

import java.util.List;

@Dao
public interface PeriodDao {

    @Query("SELECT * FROM periods ORDER BY year DESC, month DESC")
    LiveData<List<Period>> getAllLiveData();

    @Query("SELECT * FROM periods ORDER BY year DESC, month ASC")
    List<Period> getAll();

    @Query("SELECT * FROM periods WHERE id =:id")
    LiveData<Period> getPeriodById(long id);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Period period);

    @Delete
    void delete(Period period);

    @Update
    void update(Period period);

    @Query("DELETE FROM periods")
    void dropTable();
}
