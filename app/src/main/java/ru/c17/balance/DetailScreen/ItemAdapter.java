package ru.c17.balance.DetailScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.c17.balance.R;

import ru.c17.balance.data.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> mData;


    void setData(List<Item> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.mTextLabel.setText(mData.get(position).getName());
        holder.mTextTotal.setText(mData.get(position).getSumForDisplay());
        holder.mTextQuantityPrice.setText(mData.get(position).getQuantityAndPriceForDisplay());
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        } else {
            return mData.size();
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView mTextLabel;
        TextView mTextQuantityPrice;
        TextView mTextTotal;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextLabel = itemView.findViewById(R.id.text_label);
            mTextQuantityPrice = itemView.findViewById(R.id.text_quantity_price);
            mTextTotal = itemView.findViewById(R.id.text_total);

        }
    }
}
