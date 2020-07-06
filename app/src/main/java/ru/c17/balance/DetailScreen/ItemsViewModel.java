package ru.c17.balance.DetailScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.c17.balance.App;
import ru.c17.balance.data.Item;
import ru.c17.balance.data.Receipt;
import ru.c17.balance.data.ReceiptRepository;
import ru.c17.balance.data.dao.ItemDao;
import ru.c17.balance.data.dao.ReceiptDao;

import java.util.List;

public class ItemsViewModel extends ViewModel {

    private ItemDao itemDao;



    private LiveData<Receipt> receipt;

    private ReceiptRepository repository;
    private ReceiptDao receiptDao;
    private LiveData<List<Item>> items;


    public ItemsViewModel(long receiptId) {
        super();

        repository = App.getReceiptRepository();
        items = repository.getItemsForReceipt(receiptId);
        receipt = repository.getReceiptLiveDataById(receiptId);
    }

    LiveData<List<Item>> getItems() {
        return items;
    }

    LiveData<Receipt> getReceipt() {
        return receipt;
    }

    Receipt getReceiptById(long id) {
        return repository.getReceiptById(id);
    }


}
