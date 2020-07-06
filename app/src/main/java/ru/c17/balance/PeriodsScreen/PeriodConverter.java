package ru.c17.balance.PeriodsScreen;

import ru.c17.balance.R;

import ru.c17.balance.data.Period;

public class PeriodConverter {

    public static int getMonthStringId(Period period) {
        switch (period.getMonth()) {
            case 0:
                return R.string.month_jan;
            case 1:
                return R.string.month_feb;
            case 2:
                return R.string.month_mar;
            case 3:
                return R.string.month_apr;
            case 4:
                return R.string.month_may;
            case 5:
                return R.string.month_jun;
            case 6:
                return R.string.month_jul;
            case 7:
                return R.string.month_aug;
            case 8:
                return R.string.month_sep;
            case 9:
                return R.string.month_oct;
            case 10:
                return R.string.month_nov;
            default:
                return R.string.month_dec;
        }
    }
}
