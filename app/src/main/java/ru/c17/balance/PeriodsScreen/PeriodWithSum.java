package ru.c17.balance.PeriodsScreen;

import ru.c17.balance.data.Period;

public class PeriodWithSum implements PeriodRecyclerViewElement {

    private Period period;
    private Float sum;

    public PeriodWithSum() {

    }

    public PeriodWithSum(Period period, Float sum) {
        this.period = period;
        this.sum = sum;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Float getSum() {
        return sum;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }

    public int getStringIdForMonth() {

        return PeriodConverter.getMonthStringId(period);
    }


    @Override
    public int getType() {
        return PERIOD;
    }
}
