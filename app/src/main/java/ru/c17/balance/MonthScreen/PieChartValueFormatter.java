package ru.c17.balance.MonthScreen;

import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Locale;

public class PieChartValueFormatter extends ValueFormatter {


    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {
        return String.format(Locale.getDefault(), "%.2f", value) + "₽";
    }
}
