package ru.c17.balance.helpers;

// TODO Add emoji compatability

import ru.c17.balance.data.Receipt;

import java.util.ArrayList;
import java.util.List;

public class ReceiptCategory {

    private int categoryId;
    private int categoryStringResId;
    private String emoji;
    private int colorResId;

    ReceiptCategory(int categoryId, int categoryStringResId, String emoji, int colorResId) {
        this.categoryId = categoryId;
        this.categoryStringResId = categoryStringResId;
        this.emoji = emoji;
        this.colorResId = colorResId;

    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getCategoryStringResId() {
        return categoryStringResId;
    }

    public String getEmoji() {
        return emoji;
    }

    public int getColorResId() {
        return colorResId;
    }

    public static List<CategorySummary> getSummaryForReceipts(List<Receipt> receipts) {
        List<CategorySummary> result = new ArrayList<>();
        for (Receipt receipt: receipts) {
            ReceiptCategory category = OkvedHelper.getInstance().getReceiptCategoryById(receipt.getCategoryId());
            boolean flag = false;
            for (int i=0; i < result.size(); i++) {
                if (result.get(i).getCategory().getCategoryId() == category.getCategoryId()) {
                    result.get(i).addSum(Double.valueOf(receipt.getSum()));
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                result.add(new CategorySummary(category, Double.valueOf(receipt.getSum())));
            }

        }
        return result;
    }
}
