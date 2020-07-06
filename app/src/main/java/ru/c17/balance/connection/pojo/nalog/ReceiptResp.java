package ru.c17.balance.connection.pojo.nalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReceiptResp {

    @SerializedName("document")
    @Expose
    private Document document;

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
