package ru.c17.balance;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.c17.balance.connection.NetworkService;
import ru.c17.balance.data.ReceiptRepository;
import ru.c17.balance.helpers.DownloadReceiptBroadcast;

// TODO Add search by category
// TODO Add start tutorial
// TODO Fix digits input in Nalog auth
// TODO Set Coordinator layout as root in activity_main.xml
// TODO Replace swipe to delete w/ long press
// TODO Fix preference summary color

public class App extends Application {

    private static App mContext;
    private static ReceiptRepository mRepository;
    private static boolean mIsAuthorized = false;
    private DownloadReceiptBroadcast mBroadcast;

    @Override
    public void onCreate() {
        super.onCreate();
        mBroadcast = new DownloadReceiptBroadcast();
        registerReceiver(mBroadcast, new IntentFilter("ru.c17.balance.ACTION_RECEIPT_DOWNLOADED"));
        mRepository = new ReceiptRepository(this);
        mContext = this;
        checkAuthorization();

        setupTheme();

    }

    private void setupTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_main), MODE_PRIVATE);
        String theme = sharedPreferences.getString(getString(R.string.pref_theme_key), getString(R.string.theme_value_default));
        if (theme.equals(getString(R.string.theme_value_dark))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (theme.equals(getString(R.string.theme_value_light))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (theme.equals(getString(R.string.theme_value_default))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }


    public static Context getContext() {
        return mContext;
    }

    public static boolean isAuthorized() {
        Log.d(App.class.getSimpleName(), "User authorization " + mIsAuthorized);
        return mIsAuthorized;
    }

    public static void setIsAuthorized(boolean mIsAuthorized) {
        App.mIsAuthorized = mIsAuthorized;
    }

    public static ReceiptRepository getReceiptRepository() {
        return mRepository;
    }

    // TODO move to helper
    public void checkAuthorization() {

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_main), Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(getString(R.string.pref_nalog_api_phone_key),"");
        String password = sharedPreferences.getString(getString(R.string.pref_nalog_api_password_key),"");
        NetworkService.getInstance().getNalogApi().signIn(Credentials.basic(username, password)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mIsAuthorized = response.code() == 200;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mIsAuthorized = false;
            }
        });
    }
}

