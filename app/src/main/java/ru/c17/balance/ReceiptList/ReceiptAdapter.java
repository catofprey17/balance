package ru.c17.balance.ReceiptList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextClassifierEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import ru.c17.balance.App;
import ru.c17.balance.Utils.ColorUtils;
import ru.c17.balance.DetailScreen.DetailActivity;
import ru.c17.balance.data.Item;
import ru.c17.balance.data.Receipt;
import ru.c17.balance.data.ReceiptRepository;
import ru.c17.balance.helpers.OkvedHelper;
import ru.c17.balance.helpers.ReceiptCategory;

import ru.c17.balance.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    private List<Receipt> mData;
    private Context mContext;

    private Receipt mReceiptBackup;
    private int mPositionBackup;
    private List<Item> mItemsBackup;

    private ReceiptRepository mRepository;

    public ReceiptAdapter(Context context) {
        mContext = context;
        mRepository = App.getReceiptRepository();
    }

    public void setData(List<Receipt> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View view = inflater.inflate(R.layout.item_receipt, parent, false);
        View view = inflater.inflate(R.layout.item_receipt, parent, false);
        return new ReceiptViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final ReceiptViewHolder holder, final int position) {

        // TODO Fix anarchy in this method
        Receipt receipt = mData.get(position);

        holder.mTextSum.setText(receipt.getSumForDisplay());
        holder.mTextStore.setText(receipt.getStoreNameForDisplay(mContext));
        holder.mTextDate.setText(receipt.getConvertedTimeForDisplay());


        ReceiptCategory category = OkvedHelper.getInstance().getReceiptCategoryById(receipt.getCategoryId());
        holder.mTextEmoji.setText(category.getEmoji());

        int color = ColorUtils.adjustAlpha(mContext.getColor(category.getColorResId()), 0.5f);
        holder.mRoot.setStrokeColor(color);

        int nightmode = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightmode == Configuration.UI_MODE_NIGHT_YES) {
            holder.mSeparator.setBackgroundColor(mContext.getColor(R.color.colorFocusBack));
            holder.mStoreView.setBackgroundColor(mContext.getColor(R.color.colorFocusBack));
        } else {

            holder.mSeparator.setBackgroundColor(color);
            holder.mStoreView.setBackgroundColor(color);
        }
//        holder.mRoot.invalidate();



    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        } else {
            return mData.size();
        }
    }


    void deleteReceipt(int position) {
        final Receipt receipt = mData.get(position);
        mReceiptBackup = receipt;
        mPositionBackup = position;

        new Thread(new Runnable() {
            @Override
            public void run() {
                mItemsBackup = mRepository.getItemsForReceiptOld(receipt.getId());
                mRepository.deleteReceipt(receipt);
            }
        }).start();

        mData.remove(position);
        notifyItemRemoved(position);
        Snackbar snackbar = Snackbar.make(((Activity) mContext).findViewById(android.R.id.content),
                R.string.snack_receipt_removed_string,
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.snack_undo_string, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreReceipt();
            }
        });

        snackbar.setAnchorView(R.id.bottom_appbar);
        snackbar.show();
    }

    private void restoreReceipt() {
        mData.add(mPositionBackup,mReceiptBackup);
        notifyItemInserted(mPositionBackup);
        Log.d("OLD ID", String.valueOf(mReceiptBackup.getId()));
        mRepository.restoreReceipt(mReceiptBackup, mItemsBackup);
    }


    class ReceiptViewHolder extends RecyclerView.ViewHolder {

        TextView mTextStore;
        TextView mTextDate;
        TextView mTextSum;
        TextView mTextEmoji;
        View mSeparator;
        ConstraintLayout mStoreView;

        MaterialCardView mRoot;



        ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);

//            mRoot = itemView.findViewById(R.id.root_layout);
//            mTextDate = itemView.findViewById(R.id.text_month);
//            mTextStore = itemView.findViewById(R.id.text_name);
//            mTextSum = itemView.findViewById(R.id.edit_sum);
//            mTextEmoji = itemView.findViewById(R.id.spinner_category);

            mRoot = itemView.findViewById(R.id.root_layout);
            mTextDate = itemView.findViewById(R.id.text_date);
            mTextStore = itemView.findViewById(R.id.text_store_name);
            mTextSum = itemView.findViewById(R.id.text_sum);
            mTextEmoji = itemView.findViewById(R.id.text_category_emoji);
            mSeparator = itemView.findViewById(R.id.separator);
            mStoreView = itemView.findViewById(R.id.view_store);



            mRoot.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_RECEIPT, mData.get(getAdapterPosition()));



//                ActivityOptionsCompat options = ActivityOptionsCompat
//                        .makeSceneTransitionAnimation((Activity) mContext,
//                                Pair.<View, String>create(mRoot,"receipt_card_transition")
////                                   , Pair.<View, String>create(((Activity) mContext).findViewById(android.R.id.statusBarBackground), Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
//                        );
//                mContext.startActivity(intent, options.toBundle());
//                    notifyItemChanged(getAdapterPosition());

                mContext.startActivity(intent);
            });
        }
    }
}
