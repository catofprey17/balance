package ru.c17.balance.EditReceiptScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.c17.balance.App;
import ru.c17.balance.data.Receipt;
import ru.c17.balance.data.ReceiptRepository;

public class EditReceiptViewModel extends ViewModel {

    private LiveData<Receipt> mReceiptLiveData;
    private ReceiptRepository repository;

    public EditReceiptViewModel(long receiptId) {
        repository = App.getReceiptRepository();
        mReceiptLiveData = repository.getReceiptLiveDataById(receiptId);
    }

    LiveData<Receipt> getReceipt() {
        return mReceiptLiveData;
    }
}
