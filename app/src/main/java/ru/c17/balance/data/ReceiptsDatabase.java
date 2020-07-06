package ru.c17.balance.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.c17.balance.data.dao.ItemDao;
import ru.c17.balance.data.dao.PeriodDao;
import ru.c17.balance.data.dao.ReceiptDao;
import ru.c17.balance.data.dao.ReceiptWithPeriodDao;

@Database(entities = {Receipt.class, Period.class, Item.class}, version = 1)
public abstract class ReceiptsDatabase extends RoomDatabase {

    // TODO Handle storage limits

    private static ReceiptsDatabase instance;

    public static synchronized ReceiptsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ReceiptsDatabase.class, "database")
                    .build();
        }
        return instance;
    }

    public abstract ReceiptDao receiptDao();

    public abstract ReceiptWithPeriodDao receiptWithPeriodDao();

    public abstract PeriodDao periodDao();

    public abstract ItemDao itemDao();

}
