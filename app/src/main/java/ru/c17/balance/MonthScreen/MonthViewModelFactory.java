package ru.c17.balance.MonthScreen;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MonthViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private long id;

    public MonthViewModelFactory(long id) {
        super();
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == MonthViewModel.class) {
            return (T) new MonthViewModel(id);
        }
        return null;
    }
}
