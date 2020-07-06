package ru.c17.balance.EditReceiptScreen;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.c17.balance.AddReceiptScreen.SumTextWatcher;
import ru.c17.balance.App;
import ru.c17.balance.R;
import ru.c17.balance.ReceiptCategoryMaterialSpinnerAdapter;
import ru.c17.balance.Utils.ColorUtils;
import ru.c17.balance.data.Receipt;
import ru.c17.balance.data.ReceiptRepository;
import ru.c17.balance.helpers.OkvedHelper;
import ru.c17.balance.helpers.ReceiptCategory;

public class EditReceiptDialogFragment extends DialogFragment {

    private Receipt mReceipt;
    private Context mContext;

    private TextInputEditText mEditDate;
    private TextInputEditText mEditSum;
    private TextInputEditText mEditStore;
    private MaterialAutoCompleteTextView mCategoryEdit;
    private FloatingActionButton mFabDone;

    private TextInputLayout mSumLayout;

    private List<ReceiptCategory> mCategories;
    ReceiptRepository mRepository;


    private Calendar mCalendar;

    public EditReceiptDialogFragment() {
    }

    public EditReceiptDialogFragment(Receipt receipt) {
        mReceipt = receipt;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_receipt, container, false);
        if (mReceipt == null) {
            dismiss();
        }

        mRepository = App.getReceiptRepository();

        mEditDate = view.findViewById(R.id.date_edit_text);
        mEditSum = view.findViewById(R.id.sum_edit_text);
        mEditStore = view.findViewById(R.id.store_name_edit_text);
        mCategoryEdit = view.findViewById(R.id.category_edit_text);
        mFabDone = view.findViewById(R.id.fab_done);

        mSumLayout = view.findViewById(R.id.sum_input);

        if (!mReceipt.isAddedManually()) {
            mEditDate.setEnabled(false);
            mEditSum.setEnabled(false);
        }






        // region Configure sum
        String editSumStr = String.format(Locale.getDefault(), "%.2f", mReceipt.getSum());
        mEditSum.setText(editSumStr.replaceAll(",","."));
        mEditSum.addTextChangedListener(new SumTextWatcher(mEditSum));
        mEditSum.setOnKeyListener((v13, keyCode, event) -> {
            mSumLayout.setError(null);
            return false;
        });
        // endregion

        // region Configure category input
        mCategories = OkvedHelper.getInstance().getCategories();
        mCategoryEdit.setText(OkvedHelper.getInstance().getReceiptCategoryById(mReceipt.getCategoryId()).getEmoji());
        mCategoryEdit.setAdapter(new ReceiptCategoryMaterialSpinnerAdapter(mContext, R.layout.item_category_dropdown, mCategories));
        mCategoryEdit.setDropDownWidth(getResources().getDisplayMetrics().widthPixels - 50);
        mCategoryEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                mCategoryEdit.showDropDown();

        });
        mCategoryEdit.setOnTouchListener((v12, event) -> {
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
                @ColorInt int color = ColorUtils.adjustAlpha(mContext.getColor(category.getColorResId()), 0.5f);
                ConstraintLayout storeView = view.findViewById(R.id.view_store);
                storeView.setBackgroundColor(color);
//                MaterialCardView card = view.findViewById(R.id.receipt_card);
//                card.setStrokeColor(color);
            }
        });
        mCategoryEdit.setText(OkvedHelper.getInstance().getReceiptCategoryById(mReceipt.getCategoryId()).getEmoji());
        // endregion

        // region Configure date input
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(mReceipt.getTime());
        mEditDate.setText(DateUtils.formatDateTime(mContext,
                mCalendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_YEAR));

        final DatePickerDialog.OnDateSetListener listener = (v, year, month, dayOfMonth) -> {
            mCalendar.set(year,month,dayOfMonth);
            mEditDate.setText(DateUtils.formatDateTime(mContext,
                    mCalendar.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_YEAR));

        };

        mEditDate.setOnClickListener(v -> new DatePickerDialog(mContext,
                        listener, mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show());
        //endregion

        //region Configure store name input
        mEditStore.setText(mReceipt.getStoreNameForDisplay(mContext));
        //endregion


        mFabDone.setOnClickListener(v -> {
            if (!mEditSum.getText().toString().equals("")) {
                if (mEditStore.getText().toString().equals("")) {
                    mEditStore.setText(getString(R.string.store));
                }
                mReceipt.setStoreName(mEditStore.getText().toString());
                ReceiptCategory receiptCategory = getCategoryFromEmoji(v, mCategoryEdit.getText().toString());
                mReceipt.setCategoryId(receiptCategory.getCategoryId());
                mReceipt.setTime(mCalendar.getTimeInMillis());
                mReceipt.setSum(Float.valueOf(mEditSum.getText().toString()));
                mRepository.updatePeriodWithPeriod(mReceipt);
                dismiss();
            } else {
                mSumLayout.setError(getString(R.string.error_sum_empty));
            }
        });











        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private ReceiptCategory getCategoryFromEmoji(View v, String emoji) {
        for (ReceiptCategory category : mCategories) {
            if (emoji.equals(category.getEmoji())) {
//                @ColorInt int color = ColorUtils.adjustAlpha(mContext.getColor(category.getColorResId()), 0.5f);
//                ConstraintLayout storeView = v.findViewById(R.id.view_store);
//                // TODO Fix null exception
//                storeView.setBackgroundColor(color);
//                MaterialCardView card = v.findViewById(R.id.receipt_card);
//                card.setStrokeColor(color);
                return category;
            }
        }
        return null;
    }
}
