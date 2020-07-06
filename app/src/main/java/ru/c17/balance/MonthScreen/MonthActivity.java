package ru.c17.balance.MonthScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.c17.balance.PeriodsScreen.PeriodConverter;
import ru.c17.balance.R;
import ru.c17.balance.ReceiptList.ReceiptAdapter;
import ru.c17.balance.data.Receipt;
import ru.c17.balance.helpers.CategorySummary;
import ru.c17.balance.helpers.ReceiptCategory;


// TODO Fix chart
// TODO (optionally) add switcher between val and perc.

public class MonthActivity extends AppCompatActivity {

    public static final String PERIOD_ID_EXTRA_KEY = "period-id";
    public static final String PERIOD_TITLE_EXTRA_KEY = "period-title";

    ProgressBar mProgressBar;

    RecyclerView mRecyclerView;
    List<Receipt> mData;
    ReceiptAdapter mAdapter;

    MonthViewModel mViewModel;

    List<CategorySummary> mCategorySummaries;

    PieChart mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

//        HashMap
//
//
        mProgressBar = findViewById(R.id.progressBar);


        long periodId = 0;

        Intent intent= getIntent();
        if ( intent != null &&
                intent.hasExtra(PERIOD_ID_EXTRA_KEY) &&
                intent.hasExtra(PERIOD_TITLE_EXTRA_KEY)) {
            periodId = intent.getLongExtra(PERIOD_ID_EXTRA_KEY, 0);
            ((TextView) findViewById(R.id.text_period)).setText(intent.getStringExtra(PERIOD_TITLE_EXTRA_KEY));

        } else {
            finish();
        }

        // TODO Change total style

        mPieChart = findViewById(R.id.pie_chart);
        mPieChart.setHoleRadius(25f);
        mPieChart.getLegend().setEnabled(false);
        mPieChart.getDescription().setEnabled(false);
        //mPieChart.setExtraOffsets(30f,30f,30f,30f);


        mPieChart.setBackgroundColor(getColor(R.color.colorFocusBack));







        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new ReceiptAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        mViewModel = new ViewModelProvider(this, new MonthViewModelFactory(periodId)).get(MonthViewModel.class);
        mViewModel.getReceipts().observe(this, receipts -> {
            mData = receipts;
            mCategorySummaries = ReceiptCategory.getSummaryForReceipts(mData);
            mAdapter.setData(mData);

            ArrayList<PieEntry> entries = new ArrayList<>();
            ArrayList<Integer> colors = new ArrayList<>();
            for (CategorySummary categorySummary : mCategorySummaries) {
//                    PieEntry entry = new PieEntry((float) categorySummary.getSum(),
//                            getResources().getString(categorySummary.getCategory().getCategoryStringResId()));
                PieEntry entry = new PieEntry((float) categorySummary.getSum(),
                        categorySummary.getCategory().getEmoji());
                entries.add(entry);
                colors.add(getColor(categorySummary.getCategory().getColorResId()));
            }

            PieDataSet dataSet = new PieDataSet(entries, "Test");

            dataSet.setValueLinePart1Length(0.8f);
            dataSet.setValueLinePart2Length(0.1f);
            dataSet.setColors(colors);
            //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setValueTextSize(16f);
            dataSet.setValueTextColor(getColor(android.R.color.black));

            dataSet.setValueFormatter(new PieChartValueFormatter());
            mPieChart.setEntryLabelColor(getColor(android.R.color.black));

            float sum = 0;
            for (Receipt receipt : mData) {
                sum += receipt.getSum();
            }

            mPieChart.setCenterText(String.format(Locale.getDefault(), "%.2f", sum) + "₽");

            PieData data = new PieData(dataSet);
            mPieChart.setData(data);
            mPieChart.setRotationEnabled(false);
            mPieChart.setTransparentCircleRadius(0f);
            mPieChart.setTouchEnabled(false);
            mPieChart.setEntryLabelTextSize(16f);
            mPieChart.invalidate();


            mProgressBar.setVisibility(View.INVISIBLE);
        });

        mViewModel.getPeriod().observe(this, period -> {
            String month = getString(PeriodConverter.getMonthStringId(period));
            String year = String.valueOf(period.getYear());
            setTitle(month + " " + year);
        });
    }
}
