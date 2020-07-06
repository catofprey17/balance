package ru.c17.balance.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// TODO Replace TAG

public class DownloadReceiptBroadcast extends BroadcastReceiver {

    public static final String RESULT_EXTRA = "result-extra";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(RESULT_EXTRA)) {
            switch (intent.getIntExtra(RESULT_EXTRA, -1)) {
                case NalogApiHelper.OK:

            }
        }
    }
}













//    public static final String ACTION_DOWNLOADING_DONE = "ru.c17.balance.DownloadReceiptReceiver.ACTION_DOWNLOADING_DONE";
//
//    public static final String EXTRA_RESULT_KEY = "extra-result-key";
////    public static final int RESULT_ERROR_UNKNOWN = -1;
////    public static final int RESULT_OK = 0;
////    public static final int RESULT_WRONG_CRED = 1;
////    public static final int RESULT_EMPTY_CRED = 2;
////    public static final int RESULT_NO_INTERNET = 3;
////    public static final int RESULT_IO_ERROR = 4;
////    public static final int RESULT_RECEIPT_NOT_EXISTS = 5;
////
////    private WeakReference<AppCompatActivity> mActivity;
////
////    public DownloadReceiptBroadcast(AppCompatActivity activity) {
////        mActivity = new WeakReference<>(activity);
////    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
////        if (!intent.hasExtra(EXTRA_RESULT_KEY)) {
////            return;
////        }
////        int result = intent.getIntExtra(EXTRA_RESULT_KEY, -1);
////
////        switch (result) {
////            case RESULT_OK:
////                break;
////
////            case RESULT_WRONG_CRED:
////                if (mActivity.get() != null) {
////                    new WrongCredentialsDialogFragment().show(mActivity.get().getSupportFragmentManager(),"TAG");
////                }
////                break;
////
////            case RESULT_EMPTY_CRED:
////                if (mActivity.get() != null) {
////                    new EmptyCredentialsDialogFragment().show(mActivity.get().getSupportFragmentManager(),"TAG");
////                }
////                break;
////
////            case RESULT_NO_INTERNET:
////                Toast.makeText(context, R.string.toast_no_internet, Toast.LENGTH_SHORT).show();
////                break;
////        }
////
////        onDownloadingDone();
//
//    }
