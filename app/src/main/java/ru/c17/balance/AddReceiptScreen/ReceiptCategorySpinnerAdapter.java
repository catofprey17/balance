package ru.c17.balance.AddReceiptScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.c17.balance.R;

import ru.c17.balance.helpers.ReceiptCategory;

import java.util.List;

public class ReceiptCategorySpinnerAdapter extends ArrayAdapter<ReceiptCategory> {

    private enum Type {VIEW, DROPDOWN_VIEW}

    private Context mContext;

    public ReceiptCategorySpinnerAdapter(@NonNull Context context, int resource, @NonNull List<ReceiptCategory> list) {
        super(context, resource, list);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent, Type.VIEW);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent, Type.DROPDOWN_VIEW);
    }

    private View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent, Type type) {

        int resId;
        if (type == Type.VIEW) {
            resId = R.layout.item_category_view;
        } else {
            resId = R.layout.item_category_dropdown;
        }

        if (convertView == null ) {
            convertView = LayoutInflater.from(mContext).inflate(resId, parent, false);
        }


        TextView textName = convertView.findViewById(R.id.text_category_name);
        TextView textEmoji = convertView.findViewById(R.id.text_category_emoji);

        ReceiptCategory category = getItem(position);
        if (category != null) {
            textName.setText(mContext.getString(category.getCategoryStringResId()));
            textEmoji.setText(category.getEmoji());
        }

        return convertView;
    }


}
