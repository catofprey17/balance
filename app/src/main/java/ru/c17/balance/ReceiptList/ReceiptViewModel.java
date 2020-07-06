package ru.c17.balance.ReceiptList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.c17.balance.App;
import ru.c17.balance.data.Receipt;
import ru.c17.balance.data.ReceiptRepository;
import ru.c17.balance.data.dao.ReceiptDao;

import java.util.List;

public class ReceiptViewModel extends ViewModel {

    private ReceiptDao mReceiptDao;
    private LiveData<List<Receipt>> mAllReceipts;

    private ReceiptRepository mReceiptRepository;

    public ReceiptViewModel() {
        super();

        mReceiptRepository = App.getReceiptRepository();
        mAllReceipts = mReceiptRepository.getAllReceipts();
        mReceiptDao = mReceiptRepository.getReceiptDao();

        mAllReceipts = mReceiptDao.getAll();
    }

    LiveData<List<Receipt>> getAllReceipts() {
        return mAllReceipts;
    }

    public void addReceipt(final Receipt receipt) {
        mReceiptRepository.insertReceiptWithPeriod(receipt);
    }

    public boolean checkAndWriteReceipt(final Receipt receipt) {
        Receipt temp = mReceiptDao.getByFnFdFp(receipt.getFn(), receipt.getFd(), receipt.getFp());
        if (temp == null) {
            receipt.setAddedManually(false);
            addReceipt(receipt);
            return true;
        } else {
            return false;
        }
    }
}
