package ru.c17.balance.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (   tableName = "items",
            indices = @Index(value = "receipt_id"),
            foreignKeys = @ForeignKey(entity = Receipt.class, parentColumns = "id", childColumns = "receipt_id", onDelete = ForeignKey.CASCADE))
public class Item {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "sum")
    private long sum;

    @ColumnInfo(name = "price")
    private long price;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "quantity")
    private double quantity;

    @ColumnInfo(name = "receipt_id")
    private long receiptId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(long receiptId) {
        this.receiptId = receiptId;
    }


    public String getSumForDisplay() {
        String result = String.valueOf(sum);
        result = result.substring(0, result.length()-2) + "." + result.substring(result.length()-2);
        result += " \u20BD";
        return result;
    }

    public String getQuantityAndPriceForDisplay() {
        String p = String.valueOf(price);
        p = p.substring(0, p.length()-2) + "." + p.substring(p.length()-2);
        p+= " \u20BD";

        String q = String.valueOf(quantity);
        if (q.substring(q.length()-2).equals(".0")) {
           q = q.substring(0, q.length()-2);
        }

        return  q + " x " + p;
    }
}
