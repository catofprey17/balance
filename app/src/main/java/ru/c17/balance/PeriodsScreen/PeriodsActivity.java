package ru.c17.balance.PeriodsScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomappbar.BottomAppBar;

import ru.c17.balance.data.Period;
import ru.c17.balance.MonthScreen.MonthActivity;
import ru.c17.balance.R;

import java.util.ArrayList;
import java.util.List;

public class PeriodsActivity extends AppCompatActivity implements PeriodAdapter.MonthClickListener {

//    ProgressBar mProgressBar;
    PeriodAdapter mAdapter;
    List<PeriodRecyclerViewElement> data;
    PeriodsViewModel mViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periods);

//        mProgressBar = findViewById(R.id.progressBar);

//        ConstraintLayout root = findViewById(R.id.root_layout);
        BottomAppBar appBar = findViewById(R.id.bottom_appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_periods);
//        nestedScrollView.setNestedScrollingEnabled(false);

        mAdapter = new PeriodAdapter(this, this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (data.get(position) instanceof PeriodHeader) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new PeriodRecyclerViewItemDecoration(recyclerView, mAdapter));



        mViewModel = new ViewModelProvider(this).get(PeriodsViewModel.class);
        mViewModel.getPeriodsWithSums().observe(this, periodWithSums -> {
            List<PeriodRecyclerViewElement> result = new ArrayList<>();

            int tempYear = 0;
            for (PeriodWithSum element : periodWithSums) {
                if (element.getPeriod().getYear() == tempYear) {
                    result.add(element);
                } else {
                    tempYear = element.getPeriod().getYear();
                    result.add(new PeriodHeader(tempYear));
                    result.add(element);
                }
            }
            data = result;
            mAdapter.setData(data);
//            mProgressBar.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    public void onMonthClicked(Period period) {
        Intent intent = new Intent(this, MonthActivity.class);
        intent.putExtra(MonthActivity.PERIOD_ID_EXTRA_KEY, period.getId());
        String date = getString(PeriodConverter.getMonthStringId(period)) + " " + period.getYear();
        intent.putExtra(MonthActivity.PERIOD_TITLE_EXTRA_KEY, date);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null)
            mAdapter.onDestroy();
    }
}
