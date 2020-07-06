package ru.c17.balance.helpers;

public class CategorySummary {

    private ReceiptCategory mCategory;
    private double sum;


    CategorySummary(ReceiptCategory category, double sum) {
        mCategory = category;
        this.sum = sum;
    }

    public ReceiptCategory getCategory() {
        return mCategory;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    void addSum(double sum) {
        this.sum += sum;
    }
}
