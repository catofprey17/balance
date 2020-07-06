package ru.c17.balance.AddReceiptScreen;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;

import ru.c17.balance.App;
import ru.c17.balance.MainScreen.MainActivity;
import ru.c17.balance.R;
import ru.c17.balance.ReceiptCategoryMaterialSpinnerAdapter;
import ru.c17.balance.Utils.ColorUtils;
import ru.c17.balance.data.Receipt;
import ru.c17.balance.data.ReceiptRepository;
import ru.c17.balance.helpers.OkvedHelper;
import ru.c17.balance.helpers.ReceiptCategory;



/**
 * A simple {@link Fragment} subclass.
 */
public class AddReceiptFragment extends Fragment {

    private Context context;

    private Calendar mCalendar;
    private ReceiptRepository mRepository;
    private long time;
    private List<ReceiptCategory> mCategories;

    private TextInputLayout mSumLayout;


    private TextInputEditText mEditDate;
    private TextInputEditText mEditSum;
    private TextInputEditText mEditStore;
    private MaterialAutoCompleteTextView mCategoryEdit;

    public AddReceiptFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_receipt, container, false);

        mEditDate = view.findViewById(R.id.date_edit_text);
        mEditSum = view.findViewById(R.id.sum_edit_text);
        mEditStore = view.findViewById(R.id.store_name_edit_text);
        mCategoryEdit = view.findViewById(R.id.category_edit_text);
        mSumLayout = view.findViewById(R.id.sum_input);

        mRepository = App.getReceiptRepository();


        mCalendar = Calendar.getInstance();
        time = mCalendar.getTimeInMillis();

        // Set up sum input
        mEditSum.addTextChangedListener(new SumTextWatcher(mEditSum));
        mEditSum.setOnKeyListener((v, keyCode, event) -> {
            mSumLayout.setError(null);
            return false;
        });

        // Set up category input
        mCategories = OkvedHelper.getInstance().getCategories();

        mCategoryEdit.setAdapter(new ReceiptCategoryMaterialSpinnerAdapter(context, R.layout.item_category_dropdown, mCategories));
        mCategoryEdit.setDropDownWidth(getResources().getDisplayMetrics().widthPixels - 50);
        mCategoryEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                mCategoryEdit.showDropDown();

        });
        mCategoryEdit.setOnTouchListener((v, event) -> {
            mCategoryEdit.showDropDown();
            return false;
        });
        mCategoryEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ReceiptCategory category = getCategoryFromEmoji(view, s.toString());
                if (category != null) {
                    @ColorInt int color = ColorUtils.adjustAlpha(context.getColor(category.getColorResId()), 0.5f);
                    ConstraintLayout storeView = view.findViewById(R.id.view_store);
                    storeView.setBackgroundColor(color);
                    MaterialCardView card = view.findViewById(R.id.receipt_card);
                    card.setStrokeColor(color);
                }
            }
        });
        mCategoryEdit.setText(OkvedHelper.getInstance().getReceiptCategoryById(0).getEmoji());

        // Set up date input
        mEditDate.setText(DateUtils.formatDateTime(context,
                time,
                DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_YEAR));

        final DatePickerDialog.OnDateSetListener listener = (v, year, month, dayOfMonth) -> {
            mCalendar.set(year,month,dayOfMonth);
            time = mCalendar.getTimeInMillis();
            mEditDate.setText(DateUtils.formatDateTime(context,
                    time,
                    DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_YEAR));

        };

        mEditDate.setOnClickListener(v1 -> new DatePickerDialog(context,
                listener, mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show());

        // Set up Add button
        FloatingActionButton addButton = ((MainActivity) context).findViewById(R.id.fab_add);

        addButton.setOnClickListener(v -> {
            if (mEditSum.getText() != null && !mEditSum.getText().toString().equals("")) {
                if (mEditStore.getText() != null && mEditStore.getText().toString().equals("")) {
                    mEditStore.setText(getString(R.string.store));
                }
                Receipt receipt = new Receipt();
                if (mEditStore.getText() != null) {
                    receipt.setStoreName(mEditStore.getText().toString());
                    ReceiptCategory receiptCategory = getCategoryFromEmoji(view, mCategoryEdit.getText().toString());
                    if (receiptCategory != null) {
                        receipt.setCategoryId(receiptCategory.getCategoryId());
                        receipt.setTime(time);
                        receipt.setSum(Float.valueOf(mEditSum.getText().toString()));
                        receipt.setDownloaded(true);
                        receipt.setAddedManually(true);
                        mRepository.insertReceiptWithPeriod(receipt);
                        ((MainActivity) context).closeAddReceiptFragment();
                    }
                }
            } else {
                mSumLayout.setError(getString(R.string.error_sum_empty));
            }
        });






        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity;
        if (context instanceof MainActivity) {
            this.context = context;
            activity = (MainActivity) context;
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            activity.setFabMode(MainActivity.MODE.ADD_RECEIPT);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity activity;
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
            activity.setFabMode(MainActivity.MODE.MAIN);
        }
    }

    private ReceiptCategory getCategoryFromEmoji(View v, String emoji) {
        for (ReceiptCategory category : mCategories) {
            if (emoji.equals(category.getEmoji())) {
                @ColorInt int color = ColorUtils.adjustAlpha(context.getColor(category.getColorResId()), 0.5f);
                ConstraintLayout storeView = v.findViewById(R.id.view_store);
                storeView.setBackgroundColor(color);
                MaterialCardView card = v.findViewById(R.id.receipt_card);
                card.setStrokeColor(color);
                return category;
            }
        }
        return null;
    }


}
