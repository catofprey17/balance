package ru.c17.balance.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.IntDef;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import ru.c17.balance.App;
import ru.c17.balance.R;
import ru.c17.balance.Utils.NetworkUtils;
import ru.c17.balance.connection.NetworkService;
import ru.c17.balance.connection.pojo.nalog.Item;
import ru.c17.balance.connection.pojo.nalog.ReceiptResp;
import ru.c17.balance.connection.pojo.okved.OkvedResp;
import ru.c17.balance.data.Receipt;
import ru.c17.balance.data.ReceiptRepository;


// TODO Check Internet connection
// TODO Check Internet permissions

public class NalogApiHelper {

    @IntDef(value = {
            OK,
            WRONG_CREDENTIALS,
            EMPTY_CREDENTIALS,
            NO_INTERNET_CONNECTION,
            IO_ERROR,
            RECEIPT_NOT_EXISTS,
            ALREADY_DOWNLOADING,
            TOO_MANY_ATTEMPTS
    })

    @Retention(RetentionPolicy.SOURCE)
    public @interface Result {}

    public static final int OK = 1;
    public static final int WRONG_CREDENTIALS = 2;
    public static final int EMPTY_CREDENTIALS = 3;
    public static final int NO_INTERNET_CONNECTION = 4;
    public static final int IO_ERROR = 5;
    public static final int RECEIPT_NOT_EXISTS = 6;
    public static final int ALREADY_DOWNLOADING = 7;
    public static final int TOO_MANY_ATTEMPTS = 8;


    private final static String LOG_TAG = NalogApiHelper.class.getSimpleName();

//    private static Executor netExecutor = Executors.newSingleThreadExecutor();

    private static List<Long> checkingIds = new ArrayList<>();

    @Result
    public static int checkAndDownload(final Receipt receipt) {

        if (checkingIds.contains(receipt.getId())) {
            return ALREADY_DOWNLOADING;
        }

        checkingIds.add(receipt.getId());

        Context context = App.getContext();
//            Intent intent = new Intent(DownloadReceiptReceiver.ACTION_DOWNLOADING_DONE);

        // Check the Internet connection
        if (!NetworkUtils.isOnline()) {
//                intent.putExtra(DownloadReceiptReceiver.EXTRA_RESULT_KEY, DownloadReceiptReceiver.RESULT_NO_INTERNET);
//                context.sendBroadcast(intent);
            checkingIds.remove(receipt.getId());
            return NO_INTERNET_CONNECTION;
        }


        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_main), Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(context.getString(R.string.pref_nalog_api_phone_key),"");
        String password = sharedPreferences.getString(context.getString(R.string.pref_nalog_api_password_key),"");

        // Check are credentials empty
        if (username.equals("") || password.equals("")) {
//                intent.putExtra(DownloadReceiptReceiver.EXTRA_RESULT_KEY, DownloadReceiptReceiver.RESULT_EMPTY_CRED);
//                context.sendBroadcast(intent);
            checkingIds.remove(receipt.getId());
            return EMPTY_CREDENTIALS;
        }
        String temp = username + ":" + password;
        String credentials;
        credentials = "Basic " + Base64.encodeToString(temp.getBytes(StandardCharsets.UTF_8),Base64.NO_WRAP);


        // Login to proverkacheka.nalog.ru
        Response<Void> responseLogin;
        try {
            responseLogin = NetworkService.getInstance().getNalogApi().signIn(credentials).execute();
        } catch (IOException e) {
            e.printStackTrace();
//                intent.putExtra(DownloadReceiptReceiver.EXTRA_RESULT_KEY, DownloadReceiptReceiver.RESULT_IO_ERROR);
//                context.sendBroadcast(intent);
            checkingIds.remove(receipt.getId());
            return IO_ERROR;
        }
        if (responseLogin.code() != 200) {
//                intent.putExtra(DownloadReceiptReceiver.EXTRA_RESULT_KEY, DownloadReceiptReceiver.RESULT_WRONG_CRED);
//                context.sendBroadcast(intent);
            checkingIds.remove(receipt.getId());
            return WRONG_CREDENTIALS;
        }


        // Check receipt at proverkacheka.nalog.ru
        Response<Void> responseCheck;
        try {
            responseCheck = NetworkService.getInstance().getNalogApi()
                    .checkReceipt(receipt.getFn(),
                            receipt.getFd(),
                            receipt.getFp(),
                            receipt.getConvertedTimeForRequest(),
                            receipt.getSumForRequest(),
                            credentials)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
//                intent.putExtra(DownloadReceiptReceiver.EXTRA_RESULT_KEY, DownloadReceiptReceiver.RESULT_IO_ERROR);
//                context.sendBroadcast(intent);
            checkingIds.remove(receipt.getId());
            return IO_ERROR;
        }
        if (responseCheck.code() != 204) {
//                intent.putExtra(DownloadReceiptReceiver.EXTRA_RESULT_KEY, DownloadReceiptReceiver.RESULT_RECEIPT_NOT_EXISTS);
//                context.sendBroadcast(intent);
            checkingIds.remove(receipt.getId());
            return RECEIPT_NOT_EXISTS;
        }



        // Download receipt info from proverkacheka.nalog.ru
        for (int i=0; i<5; i++) {
            Response<ReceiptResp> responseReceipt;
            try {
                responseReceipt = NetworkService.getInstance().getNalogApi()
                        .requestReceipt(receipt.getFn(),
                                receipt.getFd(),
                                receipt.getFp(),
                                credentials)
                        .execute();
            } catch (IOException e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                continue;
            }

            if (responseReceipt.code() == 202) {
//                    intent.putExtra(DownloadReceiptReceiver.EXTRA_RESULT_KEY, DownloadReceiptReceiver.RESULT_IO_ERROR);
//                    context.sendBroadcast(intent);
//                checkingIds.remove(receipt.getId());
//                return IO_ERROR;
                continue;
            }

            if (responseReceipt.code() == 406) {
                checkingIds.remove(receipt.getId());
                return RECEIPT_NOT_EXISTS;
            }

            if (responseReceipt.code() == 200 && responseReceipt.body() != null) {

                // Fill the store name
                String storeName = responseReceipt.body().getDocument().getReceiptObj().getUser();
                if (storeName == null) {
                    storeName = "";
                }
                receipt.setStoreName(storeName);

                // Fill the store INN
                String storeInn = responseReceipt.body().getDocument().getReceiptObj().getUserInn();
                if (storeInn == null) {
                    storeInn = "";
                }
                receipt.setInn(storeInn);

                String address = responseReceipt.body().getDocument().getReceiptObj().getRetailPlaceAddress();
                if (address == null) {
                    address = "";
                }
                receipt.setAddress(address);



                // Write items to db
                List<Item> itemList = responseReceipt.body().getDocument().getReceiptObj().getItems();
                writeItemsForReceiptToDb(itemList, receipt.getId());

                try {
                    receipt.setCategoryId(OkvedHelper.getInstance().getReceiptCategoryId(getOkvedFromWeb(receipt)));
                } catch (IOException e) {
//                        intent.putExtra(DownloadReceiptReceiver.EXTRA_RESULT_KEY, DownloadReceiptReceiver.RESULT_IO_ERROR);
//                        context.sendBroadcast(intent);
                    checkingIds.remove(receipt.getId());
                    return IO_ERROR;
                }
                updateReceipt(receipt);
//                    intent.putExtra(DownloadReceiptReceiver.EXTRA_RESULT_KEY, DownloadReceiptReceiver.RESULT_OK);
//                    context.sendBroadcast(intent);
                checkingIds.remove(receipt.getId());
                return OK;
            }
        }
        checkingIds.remove(receipt.getId());
        return TOO_MANY_ATTEMPTS;
    }

    private static void writeItemsForReceiptToDb(List<Item> itemList , long receiptId) {

        ReceiptRepository repository = App.getReceiptRepository();
        for (Item pojoItem : itemList) {
            ru.c17.balance.data.Item dataItem = new ru.c17.balance.data.Item();
            dataItem.setName(pojoItem.getName());
            dataItem.setPrice(pojoItem.getPrice());
            dataItem.setQuantity(pojoItem.getQuantity());
            dataItem.setSum(pojoItem.getSum());
            dataItem.setReceiptId(receiptId);


            repository.insetItem(dataItem);
        }
        Log.d(LOG_TAG, "Items written into db");
    }

    private static void updateReceipt(Receipt receipt) {
        receipt.setDownloaded(true);
        ReceiptRepository repository = App.getReceiptRepository();
        repository.updateReceipt(receipt);
        Log.d(LOG_TAG, "ReceiptObj updated in db");
    }

    // TODO Handle empty response
    private static String getOkvedFromWeb(Receipt receipt) throws IOException {
        Response<OkvedResp> response = NetworkService.getInstance().getOkvedApi().getOkved(receipt.getInn()).execute();
        if (response.body() != null) {
            return response.body().getSuggestions().get(0).getData().getOkved();
        } else return "";

    }
}
