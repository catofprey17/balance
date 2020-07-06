package ru.c17.balance.PeriodsScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.c17.balance.App;
import ru.c17.balance.data.Period;
import ru.c17.balance.data.ReceiptRepository;
import ru.c17.balance.data.dao.PeriodDao;

import java.util.List;

public class PeriodsViewModel extends ViewModel {

    public interface PeriodsViewModelCallback {
        void onLoadFinished(List<PeriodWithSum> result);
    }

    private ReceiptRepository repository;
    private LiveData<List<Period>> periods;

    private MutableLiveData<List<PeriodWithSum>> periodsWithSums;

    public PeriodsViewModel() {
        super();
        repository = App.getReceiptRepository();
        periods = repository.getAllPeriods();

    }

    LiveData<List<Period>> getAllPeriods() {
        return periods;
    }

    PeriodDao getPeriodDao() {
        return repository.getPeriodDao();
    }

    LiveData<List<PeriodWithSum>> getPeriodsWithSums() {
        if (periodsWithSums == null) {
            periodsWithSums = new MutableLiveData<>();
            repository.getPeriodsWithSums(result -> periodsWithSums.postValue(result));
        }
        return periodsWithSums;
    }



}
