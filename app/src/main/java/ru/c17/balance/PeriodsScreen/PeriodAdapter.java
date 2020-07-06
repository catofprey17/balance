package ru.c17.balance.PeriodsScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

import ru.c17.balance.R;
import ru.c17.balance.Utils.ChartUtils;
import ru.c17.balance.data.Period;

public class PeriodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderInterface{

    private ChartUtils mChartUtils;
    private List<PeriodRecyclerViewElement> mData;
    private MonthClickListener mListener;
    private WeakReference<Context> mContextWeakReference;
//    private ExecutorService mChartExecutor = Executors.newSingleThreadExecutor();

    public PeriodAdapter(Context context, MonthClickListener listener) {
        mListener = listener;
        mContextWeakReference = new WeakReference<>(context);
        mChartUtils = new ChartUtils();
    }

    public void setData(List<PeriodRecyclerViewElement> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();

        if (mData.get(viewType) instanceof PeriodHeader) {
            view = LayoutInflater.from(context).inflate(R.layout.item_period_header,parent,false);
            return new HeaderVH(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_period_data, parent, false);
            return new PeriodVH(view);
        }

//        switch (viewType) {
//
//            case PeriodRecyclerViewElement.HEADER:
//                view = LayoutInflater.from(context).inflate(R.layout.item_period_header,parent,false);
//                return new HeaderVH(view);
//
//            default:
//                view = LayoutInflater.from(context).inflate(R.layout.item_period_data, parent, false);
//                return new PeriodVH(view);
//        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        holder.setIsRecyclable(false);
        if (mData.get(position) instanceof PeriodHeader) {
            ((HeaderVH) holder).bind(position);
        } else {
//            holder.setIsRecyclable(false);
            ((PeriodVH) holder).bind(position);
        }


//        switch (getItemViewType(position)) {
//            case PeriodRecyclerViewElement.PERIOD:
//                ((PeriodVH) holder).setIsRecyclable(false);
//                ((PeriodVH) holder).bind(position);
//                break;
//            case PeriodRecyclerViewElement.HEADER:
//                ((HeaderVH) holder).bind(position);
//                break;
//        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        else {
            return mData.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (mData.get(position) instanceof PeriodHeader) {
//            return PeriodRecyclerViewElement.HEADER;
//        } else {
//            return PeriodRecyclerViewElement.PERIOD;
//        }
        return position;
    }

    public void onDestroy() {
        if (mChartUtils != null)
            mChartUtils.onDestroy();
    }

    @Override
    public int getHeaderPositionForItem(int itemPos) {
        int result = 0;
        do {
            if (isHeader(itemPos)) {
                result = itemPos;
                break;
            }
            itemPos -= 1;
        } while (itemPos >= 0);
        return result;
    }

    @Override
    public int getHeaderLayout(int headerPos) {
        return R.layout.item_period_header;
    }

    @Override
    public void bindHeaderData(View header, int headerPos) {
        TextView year = header.findViewById(R.id.text_year);
        PeriodHeader periodHeaderData = (PeriodHeader) mData.get(headerPos);
        year.setText(periodHeaderData.getYear());
    }

    @Override
    public boolean isHeader(int itemPos) {
        return mData.get(itemPos) instanceof PeriodHeader;
    }


    public interface MonthClickListener {
        void onMonthClicked(Period period);
    }

    class PeriodVH extends RecyclerView.ViewHolder {

        TextView mTextMonth;
        TextView mTotal;
        CardView mCardView;
//        PieChart mPieChart;

        public PeriodVH(@NonNull View itemView) {
            super(itemView);

            mTextMonth = itemView.findViewById(R.id.text_month);
            mTotal = itemView.findViewById(R.id.text_sum);
            mCardView = itemView.findViewById(R.id.item_period_card);
//            mPieChart = itemView.findViewById(R.id.pie_chart);

            itemView.setOnClickListener(v -> {
                PeriodWithSum periodWithSum = (PeriodWithSum) mData.get(getAdapterPosition());
                mListener.onMonthClicked(periodWithSum.getPeriod());
            });
        }

        void bind(int position) {
            PeriodWithSum periodWithSum = (PeriodWithSum) mData.get(position);
            mTextMonth.setText(periodWithSum.getStringIdForMonth());
            String total = periodWithSum.getSum().toString();
            if (total.indexOf('.') < total.length() - 3) {
                total = total.substring(0, total.indexOf('.') + 2);
            }
            if (total.indexOf('.') == total.length() - 2) {
                total += "0";
            }

            total += "\u20BD";
            mTotal.setText(total);

            Context context = mContextWeakReference.get();
            if (context != null) {
                @ColorInt int color = 0;
                switch (((PeriodWithSum) mData.get(position)).getPeriod().getMonth()) {
                    case 11:
                    case 0:
                    case 1:
                        color = context.getColor(R.color.colorPeriodWinter);
                        break;

                    case 2:
                    case 3:
                    case 4:
                        color = context.getColor(R.color.colorPeriodSpring);
                        break;

                    case 5:
                    case 6:
                    case 7:
                        color = context.getColor(R.color.colorPeriodSummer);
                        break;

                    case 8:
                    case 9:
                    case 10:
                        color = context.getColor(R.color.colorPeriodAutumn);
                        break;
                }

                // TODO Fix color
//                mCardView.setBackgroundColor(color);
                mCardView.setCardBackgroundColor(color);
            }




//            mChartUtils.fillChartAsync(mContextWeakReference.get(), mPieChart, periodWithSum.getPeriod().getId());
//            ChartUtils.fillChart(mContextWeakReference.get(), mPieChart, periodWithSum.getPeriod().getId());
        }
    }

    class HeaderVH extends RecyclerView.ViewHolder {

        TextView mTextYear;

        public HeaderVH(@NonNull View itemView) {
            super(itemView);

            mTextYear = itemView.findViewById(R.id.text_year);
        }

        void bind(int position) {
            PeriodHeader periodHeader = (PeriodHeader) mData.get(position);
            mTextYear.setText(periodHeader.getYear());
        }
    }
}

