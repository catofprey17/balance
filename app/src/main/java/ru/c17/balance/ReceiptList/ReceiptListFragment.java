package ru.c17.balance.ReceiptList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.c17.balance.MainScreen.MainActivity;
import ru.c17.balance.R;

import ru.c17.balance.data.Receipt;

import java.util.List;

public class ReceiptListFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mTextNoReceipts;

    private ReceiptViewModel mViewModel;
    private ReceiptAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_receipt, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mProgressBar = rootView.findViewById(R.id.progressBar);
        mTextNoReceipts = rootView.findViewById(R.id.textNoReceipts);

        mAdapter = new ReceiptAdapter(getActivity());
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        // TODO fix appbar hidden after deleting

        if (getActivity() != null) {
            if (getActivity() instanceof MainActivity) {
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add);
                        if (dy > 0) {
                            fab.hide();
                        } else {
                            fab.show();
                        }
                    }
                });
            }
        }

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
//        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mViewModel = new ViewModelProvider(getActivity()).get(ReceiptViewModel.class);
        mViewModel.getAllReceipts().observe(getActivity(), new Observer<List<Receipt>>() {
            @Override
            public void onChanged(List<Receipt> receipts) {
                mAdapter.setData(receipts);
                mProgressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
                if (receipts.size() == 0 ) {
                    mTextNoReceipts.setVisibility(View.VISIBLE);
                } else {
                    mTextNoReceipts.setVisibility(View.INVISIBLE);
                }
            }
        });



        return  rootView;
    }
}
