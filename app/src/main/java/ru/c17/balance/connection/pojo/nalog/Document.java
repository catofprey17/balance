package ru.c17.balance.connection.pojo.nalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Document {

    @SerializedName("receipt")
    @Expose
    private ReceiptObj mReceiptObj;

    public ReceiptObj getReceiptObj() {
        return mReceiptObj;
    }

    public void setReceiptObj(ReceiptObj receiptObj) {
        this.mReceiptObj = receiptObj;
    }
}
