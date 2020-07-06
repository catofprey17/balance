package ru.c17.balance;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import ru.c17.balance.data.Receipt;
import ru.c17.balance.data.ReceiptRepository;
import ru.c17.balance.helpers.OkvedHelper;
import ru.c17.balance.helpers.ReceiptCategory;

public class FakeFiller {

    public static void fillData() {

        List<ReceiptCategory> categories = OkvedHelper.getInstance().getCategories();
        Random random = new Random();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 1,1);
        long year2017 = calendar.getTimeInMillis();
        calendar.set(2020,1,1);
        long year2020 = calendar.getTimeInMillis();
        ReceiptRepository repository = App.getReceiptRepository();


        for (int i = 0; i < 200; i++) {
            Receipt receipt = new Receipt();
            receipt.setStoreName("Store");
            ReceiptCategory receiptCategory = categories.get(random.nextInt(categories.size() - 1));
            receipt.setCategoryId(receiptCategory.getCategoryId());
            long time =  year2017 + (long) (Math.random() * (year2020 - year2017));
            receipt.setTime(time);
            receipt.setSum((float) random.nextInt(5000));
            receipt.setDownloaded(true);
            receipt.setAddedManually(true);
            repository.insertReceiptWithPeriod(receipt);
        }

    }
}
