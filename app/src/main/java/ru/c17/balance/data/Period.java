package ru.c17.balance.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity (   tableName = "periods",
            indices = {@Index(value = {"year", "month"}, unique = true)})
public class Period {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "month")
    private int month;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setMonthAndYearInMills(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
    }


}
