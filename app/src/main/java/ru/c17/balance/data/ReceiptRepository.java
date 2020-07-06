package ru.c17.balance.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import ru.c17.balance.Utils.NetworkUtils;
import ru.c17.balance.data.dao.ItemDao;
import ru.c17.balance.data.dao.PeriodDao;
import ru.c17.balance.data.dao.ReceiptDao;
import ru.c17.balance.data.dao.ReceiptWithPeriodDao;
import ru.c17.balance.helpers.NalogApiHelper;
import ru.c17.balance.App;
import ru.c17.balance.PeriodsScreen.PeriodWithSum;
import ru.c17.balance.PeriodsScreen.PeriodsViewModel;
import ru.c17.balance.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReceiptRepository {

    private ReceiptDao mReceiptDao;
    private ReceiptWithPeriodDao mReceiptWithPeriodDao;
    private ItemDao mItemDao;
    private PeriodDao mPeriodDao;
    private Executor mDiskIO;

    public ReceiptRepository(Context context) {
        ReceiptsDatabase database = ReceiptsDatabase.getInstance(context);
        mReceiptDao = database.receiptDao();
        mItemDao = database.itemDao();
        mPeriodDao = database.periodDao();
        mReceiptWithPeriodDao = database.receiptWithPeriodDao();
        mDiskIO = Executors.newSingleThreadExecutor();
    }


    // Receipts methods

    public ReceiptDao getReceiptDao() {
        return mReceiptDao;
    }

    public LiveData<List<Receipt>> getAllReceipts() {
        return mReceiptDao.getAll();
    }

    public LiveData<List<Receipt>> getAllByPeriod(long periodId) {
        return mReceiptDao.getAllByPeriod(periodId);
    }

    public void getAllByPeriod(long periodId, ReceiptsLoadedCallback callback) {
        mDiskIO.execute(() -> {
            List<Receipt> receipts = mReceiptDao.getAllByPeriodOld(periodId);
            callback.onLoaded(receipts);
        });
    }

    public List<Receipt> getAllByPeriodSync(long periodId) {
        return mReceiptDao.getAllByPeriodOld(periodId);
    }

    public LiveData<Receipt> getReceiptLiveDataById(long id) {
        return mReceiptDao.getLiveDataById(id);
    }

    public Receipt getReceiptById(long id) {
        return mReceiptDao.getById(id);
    }


    public void updateReceipt(final Receipt receipt) {
        mDiskIO.execute(() -> mReceiptDao.update(receipt));
    }

    public void deleteReceipt(final Receipt receipt) {
        mDiskIO.execute(new Runnable() {
            @Override
            public void run() {
                mReceiptWithPeriodDao.deleteReceiptClean(receipt);
            }
        });
    }

    public void rewriteReceipt(final Receipt receipt) {
        mDiskIO.execute(new Runnable() {
            @Override
            public void run() {
                mReceiptDao.deleteReceiptById(receipt.getId());
                mReceiptWithPeriodDao.insertReceiptWithPeriod(receipt);
            }
        });
    }

    public void deleteAll() {
        mDiskIO.execute(new Runnable() {
            @Override
            public void run() {
                mReceiptDao.dropTable();
                mPeriodDao.dropTable();
                mItemDao.dropTable();
            }
        });
    }



    // Periods methods

    public PeriodDao getPeriodDao() {
        return mPeriodDao;
    }

    public LiveData<List<Period>> getAllPeriods() {
        return mPeriodDao.getAllLiveData();
    }

    public LiveData<Period> getPeriodById(long id) {
        return mPeriodDao.getPeriodById(id);
    }




    // Receipts with Periods methods

    public void insertReceiptWithPeriod(final Receipt receipt) {
        mDiskIO.execute(() -> {
            receipt.setId(mReceiptWithPeriodDao.insertReceiptWithPeriod(receipt));
            Context context = App.getContext();
            if (!receipt.isAddedManually()) {

                // TODO replace with broadcast
                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_main), Context.MODE_PRIVATE);
                if (sharedPreferences.getBoolean(context.getString(R.string.pref_nalog_api_autodownload_key), false)) {
                    NetworkUtils.checkAndDownloadReceipt(receipt);
                }
            }

        });
    }

    public void updatePeriodWithPeriod(final Receipt receipt) {
        mDiskIO.execute(() -> {
            mReceiptWithPeriodDao.updateReceiptWithPeriod(receipt);
        });
    }

    public void getPeriodsWithSums(final PeriodsViewModel.PeriodsViewModelCallback callback) {
        mDiskIO.execute(() -> {
            List<Period> periods = mPeriodDao.getAll();

            List<PeriodWithSum> periodsWithSums = new ArrayList<>();
            for (Period period : periods) {
                float sum = 0;
                List<Receipt> receipts = mReceiptDao.getAllByPeriodOld(period.getId());
                for (Receipt receipt : receipts) {
                    sum += receipt.getSum();
                }
                periodsWithSums.add(new PeriodWithSum(period, sum));
            }

            callback.onLoadFinished(periodsWithSums);
        });
    }





    // region ItemDao

    public LiveData<List<Item>> getItemsForReceipt(long  receiptId) {
        return mItemDao.getItemsLiveDataByReceiptId(receiptId);
    }

    public List<Item> getItemsForReceiptOld(long  receiptId) {
        return mItemDao.getItemsListByReceiptId(receiptId);
    }

    public void insetItem(final Item item) {
        mDiskIO.execute(new Runnable() {
            @Override
            public void run() {
                mItemDao.insertItem(item);
            }
        });
    }

    // endregion



    // Receipt with Items methods

    public void restoreReceipt(final Receipt receipt, final List<Item> items) {
        mDiskIO.execute(new Runnable() {
            @Override
            public void run() {
                mReceiptWithPeriodDao.insertReceiptWithPeriod(receipt);
                for (Item item : items) {
                    mItemDao.insertItem(item);
                }
            }
        });
    }

    public interface ReceiptsLoadedCallback {
        void onLoaded(List<Receipt> receipts);
    }
}
