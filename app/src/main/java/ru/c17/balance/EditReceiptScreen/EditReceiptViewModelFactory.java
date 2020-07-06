package ru.c17.balance.EditReceiptScreen;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditReceiptViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final long receiptId;

    public EditReceiptViewModelFactory(long receiptId) {
        super();
        this.receiptId = receiptId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == EditReceiptViewModel.class) {
            return (T) new EditReceiptViewModel(receiptId);
        }
        return null;
    }
}
