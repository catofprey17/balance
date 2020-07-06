package ru.c17.balance.connection.pojo.okved;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Suggestions {

    @SerializedName("data")
    @Expose
    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
