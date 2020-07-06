package ru.c17.balance.connection.pojo.okved;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OkvedResp {

    @SerializedName("suggestions")
    @Expose
    private List<Suggestions> suggestions;

    public List<Suggestions> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Suggestions> suggestions) {
        this.suggestions = suggestions;
    }
}
