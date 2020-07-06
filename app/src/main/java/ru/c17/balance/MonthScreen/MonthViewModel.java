package ru.c17.balance.MonthScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.c17.balance.App;
import ru.c17.balance.data.Period;
import ru.c17.balance.data.Receipt;
import ru.c17.balance.data.ReceiptRepository;

import java.util.List;

public class MonthViewModel extends ViewModel {

    private ReceiptRepository repository;
    private LiveData<List<Receipt>> mReceipts;
    private LiveData<Period> mPeriod;

    public MonthViewModel(long periodId) {
        super();

        repository = App.getReceiptRepository();
        mReceipts = repository.getAllByPeriod(periodId);
        mPeriod = repository.getPeriodById(periodId);
    }

    LiveData<List<Receipt>> getReceipts() {
        return mReceipts;
    }

    LiveData<Period> getPeriod() {
        return mPeriod;
    }
}
