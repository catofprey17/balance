package ru.c17.balance.EditReceiptScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.transition.Transition;
import android.transition.TransitionListenerAdapter;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ru.c17.balance.AddReceiptScreen.ReceiptCategorySpinnerAdapter;
import ru.c17.balance.AddReceiptScreen.SumTextWatcher;
import ru.c17.balance.App;
import ru.c17.balance.R;
import ru.c17.balance.data.Receipt;
import ru.c17.balance.data.ReceiptRepository;
import ru.c17.balance.helpers.OkvedHelper;
import ru.c17.balance.helpers.ReceiptCategory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class EditReceiptActivity extends AppCompatActivity {

    public static final String EXTRA_RECEIPT = "extra-receipt";

    private Calendar mCalendar;
    private EditText editStore;
    private TextView textDate;
    private EditText editSum;
    private Spinner spinnerCategory;
    private FloatingActionButton mButtonEdit;
    private boolean isAddedManually;
    private Receipt mReceipt;
    private long id;

    private long time;
    private int receiptCategoryId;

    private ReceiptRepository mRepository;

    private EditReceiptViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_receipt);

        editStore = findViewById(R.id.edit_name);
        textDate = findViewById(R.id.text_date);
        editSum = findViewById(R.id.edit_sum);
        spinnerCategory = findViewById(R.id.spinner_category);
        mButtonEdit = findViewById(R.id.fab_edit);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRepository = App.getReceiptRepository();

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_RECEIPT)) {
            mReceipt = intent.getParcelableExtra(EXTRA_RECEIPT);
            if (mReceipt != null) {
                editStore.setText(mReceipt.getStoreNameForDisplay(this));
                textDate.setText(mReceipt.getConvertedTimeForDisplay());
                editSum.setText(String.valueOf(mReceipt.getSum()));
                isAddedManually = mReceipt.isAddedManually();
                receiptCategoryId = mReceipt.getCategoryId();
                id = mReceipt.getId();
            } else {
                // TODO write else
            }
            // TODO Fix spinner
        } else {
            finish();
        }


        mCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(year,month,dayOfMonth);
                textDate.setText(DateUtils.formatDateTime(EditReceiptActivity.this,
                        mCalendar.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_YEAR));
                time = mCalendar.getTimeInMillis();
            }
        };
        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditReceiptActivity.this,
                        listener,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        editSum.addTextChangedListener(new SumTextWatcher(editSum));

        ReceiptCategorySpinnerAdapter adapter = new ReceiptCategorySpinnerAdapter(this, R.layout.item_category_dropdown, OkvedHelper.getInstance().getCategories());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setSelection(receiptCategoryId);



        mViewModel = new ViewModelProvider(this, new EditReceiptViewModelFactory(id)).get(EditReceiptViewModel.class);
        mViewModel.getReceipt().observe(this, new Observer<Receipt>() {
            @Override
            public void onChanged(Receipt receipt) {
                mReceipt = receipt;

                // Set date
                mCalendar.setTimeInMillis(receipt.getTime());
                textDate.setText(DateUtils.formatDateTime(EditReceiptActivity.this,
                        mCalendar.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_YEAR));
            }
        });

        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editStore.getText().toString().equals("") && !textDate.getText().equals("") && !editSum.getText().toString().equals("")) {
                    mReceipt.setStoreName(editStore.getText().toString());
                    ReceiptCategory receiptCategory = (ReceiptCategory) spinnerCategory.getSelectedItem();
                    mReceipt.setCategoryId(receiptCategory.getCategoryId());

                    if (isAddedManually) {

                        mReceipt.setTime(time);
                        mReceipt.setSum(Float.valueOf(editSum.getText().toString()));

                        mRepository.rewriteReceipt(mReceipt);
                    } else {
                        mRepository.updateReceipt(mReceipt);
                    }
                    finish();
                } else {
                    Toast.makeText(EditReceiptActivity.this, R.string.toast_fill_info, Toast.LENGTH_SHORT).show();
                }
            }
        });



        // TODO Remove listener
        if (!isAddedManually) {
            editSum.setEnabled(false);
            textDate.setEnabled(false);
        }







        // TODO Check is it necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getSharedElementEnterTransition().addListener(new TransitionListenerAdapter() {
                @Override
                public void onTransitionStart(Transition transition) {
                    super.onTransitionStart(transition);
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    super.onTransitionEnd(transition);
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    super.onTransitionCancel(transition);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    super.onTransitionPause(transition);
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    super.onTransitionResume(transition);
                }
            });
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
