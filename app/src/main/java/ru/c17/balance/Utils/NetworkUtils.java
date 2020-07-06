package ru.c17.balance.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.c17.balance.App;
import ru.c17.balance.data.Receipt;
import ru.c17.balance.helpers.DownloadReceiptBroadcast;
import ru.c17.balance.helpers.NalogApiHelper;

public class NetworkUtils {

    private interface DownloadedRunnable {
        void run(@NalogApiHelper.Result int result);
    }

    public interface DownloadingHandler {
        void onDownloaded(@NalogApiHelper.Result int result);
    }


    private static ExecutorService netExecutor;

    public static final String ACTION_DOWNLOAD_COMPLETED = "ru.c17.balance.ACTION_RECEIPT_DOWNLOADED";
    public static final String RESULT = "result";
    public static final String RECEIPT_ID = "receipt-id";

    public static boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        if (networkCapabilities == null) {
            return false;
        }
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
    }



    public static void checkAndDownloadReceipt(Receipt receipt, DownloadingHandler activity) {
        WeakReference<DownloadingHandler> activityWeakReference = new WeakReference<>(activity);
        checkAndDownloadReceipt(receipt, (DownloadedRunnable) result -> {
            DownloadingHandler handler = activityWeakReference.get();
            if (handler != null) {
                ((Activity) handler).runOnUiThread(() -> {
                    handler.onDownloaded(result);
                });
            }
        });
    }

    public static void checkAndDownloadReceipt(Receipt receipt) {
        DownloadedRunnable runnable = result -> {
            Intent intent = new Intent(ACTION_DOWNLOAD_COMPLETED);
            intent.putExtra(DownloadReceiptBroadcast.RESULT_EXTRA, result);
            App.getContext().sendBroadcast(intent);
        };

        checkAndDownloadReceipt(receipt, runnable);
    }




    private static void checkAndDownloadReceipt(Receipt receipt, DownloadedRunnable runnable) {

        if (netExecutor == null) {
            netExecutor = Executors.newSingleThreadExecutor();
        }

        netExecutor.execute(() -> {
            int result = NalogApiHelper.checkAndDownload(receipt);

            if (result != NalogApiHelper.ALREADY_DOWNLOADING) {
                if (runnable != null)
                    runnable.run(result);
            }
        });


    }
}
