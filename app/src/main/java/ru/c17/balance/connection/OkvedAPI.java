package ru.c17.balance.connection;

import ru.c17.balance.connection.pojo.okved.OkvedResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface OkvedAPI {

    // TODO move out token from headers to header as a parameter

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
            "Authorization: Token 431e6310392c1335e8fe166bdafb47152b302d36"
    })
    @GET("suggestions/api/4_1/rs/suggest/party")
    Call<OkvedResp> getOkved(@Query("query") String inn);
}
