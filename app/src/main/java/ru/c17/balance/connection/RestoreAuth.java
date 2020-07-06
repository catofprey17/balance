package ru.c17.balance.connection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestoreAuth {

    @SerializedName("phone")
    @Expose
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
