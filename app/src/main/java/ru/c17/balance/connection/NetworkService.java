package ru.c17.balance.connection;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Credentials;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.c17.balance.App;
import ru.c17.balance.R;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String NALOG_BASE_URL = "https://proverkacheka.nalog.ru:9999";
    private static final String OKVED_BASE_URL = "https://suggestions.dadata.ru";
    private Retrofit mNalogRetrofit;
    private Retrofit mOkvedRetrofit;

    private NetworkService() {
        mNalogRetrofit = new Retrofit.Builder()
                .baseUrl(NALOG_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mOkvedRetrofit = new Retrofit.Builder()
                .baseUrl(OKVED_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public NalogAPI getNalogApi() {
        return mNalogRetrofit.create(NalogAPI.class);
    }

    public OkvedAPI getOkvedApi() {
        return mOkvedRetrofit.create(OkvedAPI.class);
    }
}


