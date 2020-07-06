package ru.c17.balance.DetailScreen;

import androidx.lifecycle.ViewModel;

import ru.c17.balance.App;
import ru.c17.balance.data.ReceiptRepository;

class ReceiptViewModel extends ViewModel {

    private ReceiptRepository mReceiptRepository;

    public ReceiptViewModel() {
        super();

        mReceiptRepository = App.getReceiptRepository();
    }

}
