package ru.c17.balance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ru.c17.balance.helpers.ReceiptCategory;

public class ReceiptCategoryMaterialSpinnerAdapter extends ArrayAdapter<ReceiptCategory> {

    private Filter mFilter;
    private List<ReceiptCategory> mData;
    private Context mContext;

    public ReceiptCategoryMaterialSpinnerAdapter(@NonNull Context context, int resource, @NonNull final List<ReceiptCategory> objects) {
        super(context, resource, objects);
        mData = objects;
        mContext = context;
        mFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((ReceiptCategory)resultValue).getEmoji();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                results.values = objects;
                results.count = objects.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return mFilter;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getDropDownView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        int resId = R.layout.item_category_dropdown;

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

//
//        return super.getDropDownView(position, convertView, parent);
    }
}
