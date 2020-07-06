package ru.c17.balance.connection.pojo.nalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReceiptObj {

    @SerializedName("totalSum")
    @Expose
    private long totalSum;

    @SerializedName("userInn")
    @Expose
    private String userInn;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("items")
    @Expose
    private List<Item> items;

    @SerializedName("retailPlaceAddress")
    @Expose
    private String retailPlaceAddress;

    public long getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(long totalSum) {
        this.totalSum = totalSum;
    }

    public String getUserInn() {
        return userInn;
    }

    public void setUserInn(String userInn) {
        this.userInn = userInn;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getRetailPlaceAddress() {
        return retailPlaceAddress;
    }

    public void setRetailPlaceAddress(String retailPlaceAddress) {
        this.retailPlaceAddress = retailPlaceAddress;
    }
}
