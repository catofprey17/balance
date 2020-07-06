package ru.c17.balance.Utils;

import android.content.Context;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.c17.balance.App;
import ru.c17.balance.data.Receipt;
import ru.c17.balance.data.ReceiptRepository;
import ru.c17.balance.helpers.CategorySummary;
import ru.c17.balance.helpers.ReceiptCategory;

public class ChartUtils {

    private ExecutorService mExecutor;

    public ChartUtils() {
        mExecutor = Executors.newSingleThreadExecutor();
    }

    public static void fillChart(Context context, PieChart pieChart, long periodId) {



        ReceiptRepository repository = App.getReceiptRepository();

        repository.getAllByPeriod(periodId, receipts -> {

//            pieChart.setHoleRadius(25f);
            pieChart.getLegend().setEnabled(false);
            pieChart.getDescription().setEnabled(false);
            pieChart.setDrawEntryLabels(false);
            List<CategorySummary> categorySummaries = ReceiptCategory.getSummaryForReceipts(receipts);

            ArrayList<PieEntry> entries = new ArrayList<>();
            ArrayList<Integer> colors = new ArrayList<>();
            for (CategorySummary categorySummary : categorySummaries) {
                PieEntry entry = new PieEntry((float) categorySummary.getSum(),
                        categorySummary.getCategory().getEmoji());

                entries.add(entry);
                colors.add(context.getColor(categorySummary.getCategory().getColorResId()));
            }

            PieDataSet dataSet = new PieDataSet(entries, "Test");
            dataSet.setColors(colors);
            dataSet.setDrawValues(false);
            dataSet.setDrawIcons(false);

            PieData data = new PieData();
            data.setDataSet(dataSet);

//            pieChart.setRenderer(new PieChartRenderer(pieChart, pieChart.getAnimator(), pieChart.getViewPortHandler()) {
//                @Override
//                protected void drawDataSet(Canvas c, IPieDataSet dataSet) {
//                    try {
//                        super.drawDataSet(c, dataSet);
//                    } catch (Exception ignored) {
//
//                    }
//                }
//            });

            pieChart.setData(data);
            pieChart.setRotationEnabled(false);
            pieChart.setTransparentCircleRadius(0f);
            pieChart.setTouchEnabled(false);
            pieChart.invalidate();
        });

    }

    public void fillChartAsync(Context context, PieChart pieChart, long periodId) {

        mExecutor.execute(() -> {
            ReceiptRepository repository = App.getReceiptRepository();

            List<Receipt> receipts = repository.getAllByPeriodSync(periodId);

            pieChart.setHoleRadius(25f);
            pieChart.getLegend().setEnabled(false);
            pieChart.getDescription().setEnabled(false);
            pieChart.setDrawEntryLabels(false);
            List<CategorySummary> categorySummaries = ReceiptCategory.getSummaryForReceipts(receipts);

            ArrayList<PieEntry> entries = new ArrayList<>();
            ArrayList<Integer> colors = new ArrayList<>();
            for (CategorySummary categorySummary : categorySummaries) {
                PieEntry entry = new PieEntry((float) categorySummary.getSum(),
                        categorySummary.getCategory().getEmoji());

                entries.add(entry);
                colors.add(context.getColor(categorySummary.getCategory().getColorResId()));
            }

            PieDataSet dataSet = new PieDataSet(entries, "Test");
            dataSet.setColors(colors);
            dataSet.setDrawValues(false);
            dataSet.setDrawIcons(false);

            PieData data = new PieData();
            data.setDataSet(dataSet);

            pieChart.setData(data);
            pieChart.setRotationEnabled(false);
            pieChart.setTransparentCircleRadius(0f);
            pieChart.setTouchEnabled(false);
            pieChart.invalidate();
        });


        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        mExecutor.shutdown();
    }
}
