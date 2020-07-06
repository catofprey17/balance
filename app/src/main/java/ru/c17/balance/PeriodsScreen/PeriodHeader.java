package ru.c17.balance.PeriodsScreen;

public class PeriodHeader implements PeriodRecyclerViewElement {

    private String year;



    public PeriodHeader() {
    }

    public PeriodHeader(String year) {
        this.year = year;
    }

    public PeriodHeader(int year) {
        this.year = String.valueOf(year);
    }



    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public int getType() {
        return HEADER;
    }
}
