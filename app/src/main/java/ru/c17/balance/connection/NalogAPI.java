package ru.c17.balance.connection;

import ru.c17.balance.connection.pojo.nalog.ReceiptResp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NalogAPI {

    @POST("v1/mobile/users/signup")
    Call<Void> signUp(@Body Auth auth);

    @GET("v1/mobile/users/login")
    Call<Void> signIn(@Header("Authorization") String credentials);

    @POST("v1/mobile/users/restore")
    Call<Void> restore(@Body RestoreAuth restoreAuth);

    @Headers({
            "Device-Id: ",
            "Device-Os: "
    })
    @GET("v1/inns/*/kkts/*/fss/{fn}/tickets/{fd}?&sendToEmail=no")
    Call<ReceiptResp> requestReceipt(@Path("fn") String fn,
                                     @Path("fd") String fd,
                                     @Query("fiscalSign") String fp,
                                     @Header("Authorization") String credentials);


    @Headers({
            "Device-Id: ",
            "Device-Os: "
    })
    @GET("v1/ofds/*/inns/*/fss/{fn}/operations/1/tickets/{fd}")
    Call<Void> checkReceipt(@Path("fn") String fn,
                            @Path("fd") String fd,
                            @Query("fiscalSign") String fp,
                            @Query("date") String date,
                            @Query("sum") String sum,
                            @Header("Authorization") String credentials);
}
