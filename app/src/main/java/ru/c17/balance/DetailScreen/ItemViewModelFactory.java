package ru.c17.balance.DetailScreen;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ItemViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final long receiptId;

    public ItemViewModelFactory(long receiptId) {
        super();
        this.receiptId = receiptId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == ItemsViewModel.class) {
            return (T) new ItemsViewModel(receiptId);
        }
        return null;
    }
}
