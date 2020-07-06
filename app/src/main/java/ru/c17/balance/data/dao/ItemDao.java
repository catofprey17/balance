package ru.c17.balance.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import ru.c17.balance.data.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM items WHERE receipt_id = :receiptId")
    LiveData<List<Item>> getItemsLiveDataByReceiptId(long receiptId);

    @Query("SELECT * FROM items WHERE receipt_id = :receiptId")
    List<Item> getItemsListByReceiptId(long receiptId);

    @Insert
    void insertItem(Item item);

    @Query("DELETE FROM items")
    void dropTable();
}
