package ru.c17.balance.DetailScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.WriterException;

import java.util.List;

import ru.c17.balance.App;
import ru.c17.balance.EditReceiptScreen.EditReceiptDialogFragment;
import ru.c17.balance.EmptyCredentialsDialogFragment;
import ru.c17.balance.R;
import ru.c17.balance.SettingsScreen.SignInDialogFragment;
import ru.c17.balance.Utils.BarcodeUtils;
import ru.c17.balance.Utils.NetworkUtils;
import ru.c17.balance.data.Item;
import ru.c17.balance.data.Receipt;
import ru.c17.balance.data.ReceiptRepository;
import ru.c17.balance.helpers.NalogApiHelper;
import ru.c17.balance.helpers.OkvedHelper;
import ru.c17.balance.helpers.ReceiptCategory;

// TODO Concat ViewModels


// TODO Add removing receipt
// TODO Add sharing


public class DetailActivity extends AppCompatActivity implements NetworkUtils.DownloadingHandler, SignInDialogFragment.SignInDialogListener {

    public static final String EXTRA_RECEIPT = "extra-receipt";

    Receipt mReceipt;
    ItemsViewModel mViewModel;

    RecyclerView mRecyclerView;
    ItemAdapter mAdapter;
    BarcodeDialog mBarcodeDialog;

    TextView mTextStoreName;
    TextView mTextDate;
    TextView mTextSum;
    TextView mTextCategoryEmoji;

    TextView mTextInn;
    TextView mTextAddress;
    TextView mTextFn;
    TextView mTextFd;
    TextView mTextFp;
    ImageView mImageBarcode;
    ConstraintLayout mLayoutDetails;

    CoordinatorLayout mRootLayout;

    FloatingActionButton mFAB;
    ProgressBar mProgressBar;

    ReceiptRepository mRepository;

    List<Item> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        BottomAppBar appBar = findViewById(R.id.bottom_appbar);
        setSupportActionBar(appBar);

        mTextStoreName = findViewById(R.id.text_store_name);
        mTextDate = findViewById(R.id.text_date);
        mTextSum  = findViewById(R.id.text_sum);
        mTextCategoryEmoji = findViewById(R.id.text_category_emoji);
        mTextInn = findViewById(R.id.text_inn);
        mTextAddress = findViewById(R.id.text_address);
        mTextFn = findViewById(R.id.text_fn);
        mTextFd = findViewById(R.id.text_fd);
        mTextFp = findViewById(R.id.text_fp);
        mImageBarcode = findViewById(R.id.image_barcode);
        mLayoutDetails = findViewById(R.id.layout_details);
        mFAB = findViewById(R.id.fab_download);
        mProgressBar = findViewById(R.id.progressBar);
        mRootLayout = findViewById(R.id.root_layout);

        mRecyclerView = findViewById(R.id.recycler_items);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ItemAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRepository = App.getReceiptRepository();

//        LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadReceiver, new IntentFilter(NetworkUtils.ACTION_DOWNLOAD_COMPLETED));

        mImageBarcode.setOnClickListener(v -> {
            //TODO Fix tag
            if (mBarcodeDialog != null)
                mBarcodeDialog.show(getSupportFragmentManager(),"TAG");
        });


        // TODO If null??
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_RECEIPT)) {
            mReceipt = intent.getParcelableExtra(EXTRA_RECEIPT);
            if (mReceipt != null) {
                fillView(mReceipt);
            } else {
                finish();
            }
        } else {
            finish();
        }

        mViewModel = new ViewModelProvider(this, new ItemViewModelFactory(mReceipt.getId())).get(ItemsViewModel.class);

        mViewModel.getItems().observe(this, items -> {
            mItems = items;
            mAdapter.setData(mItems);
        });

        mViewModel.getReceipt().observe(this, receipt -> {
            mReceipt = receipt;
            fillView(receipt);
        });

        //temp
        findViewById(R.id.fab_download).setOnClickListener(v -> {
            mFAB.hide();
            mProgressBar.setVisibility(View.VISIBLE);
            NetworkUtils.checkAndDownloadReceipt(mReceipt, this);
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_details_menu_item:
                StringBuilder sharingStr = new StringBuilder();
                sharingStr = sharingStr.append(mReceipt.getStoreNameForDisplay(this)).append("\n")
                        .append(mReceipt.getConvertedTimeForDisplay()).append("\n")
                        .append("***").append("\n");

                if (mItems != null) {
                    for(Item receiptItem: mItems) {
                        sharingStr = sharingStr.append(receiptItem.getName()).append(" ")
                            .append(receiptItem.getQuantityAndPriceForDisplay()).append("\n");
                    }
                    sharingStr = sharingStr.append("***").append("\n");
                }

                sharingStr = sharingStr.append(mReceipt.getSumForDisplay());

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sharingStr.toString());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, getResources().getString(R.string.share_intent_title));
                startActivity(shareIntent);



                return true;

            case R.id.edit_details_menu_item:
                EditReceiptDialogFragment editReceiptDialog = new EditReceiptDialogFragment(mReceipt);
                editReceiptDialog.show(getSupportFragmentManager(), "EDIT");
                return true;

            case R.id.remove_details_menu_item:
                mRepository.deleteReceipt(mReceipt);
                finish();
                return true;

            default:
                return false;
        }
    }

    private void fillView(Receipt receipt) {
        if (receipt == null) {
            return;
        }
        mTextStoreName.setText(receipt.getStoreNameForDisplay(this));
        mTextDate.setText(receipt.getConvertedTimeForDisplay());
        mTextSum.setText(receipt.getSumForDisplay());
        ReceiptCategory category = OkvedHelper.getInstance().getReceiptCategoryById(receipt.getCategoryId());
        mTextCategoryEmoji.setText(category.getEmoji());

        if (receipt.isAddedManually()) {
            mLayoutDetails.setVisibility(View.GONE);
        } else {
            mTextFn.setText(receipt.getFn());
            mTextFd.setText(receipt.getFd());
            mTextFp.setText(receipt.getFp());

            try {
                mImageBarcode.setImageBitmap(BarcodeUtils.getQrCodeFromString(mReceipt.getQrString(),mImageBarcode.getWidth()));
                if (mBarcodeDialog == null)
                    mBarcodeDialog = new BarcodeDialog(receipt.getQrString());
            } catch (WriterException e) {
                e.printStackTrace();
            }


            if (receipt.isDownloaded()) {
                mFAB.hide();
                mTextInn.setText(receipt.getInn());
                mTextAddress.setText(receipt.getAddress());

            } else {
                mFAB.show();
            }
        }
    }


    @Override
    public void onDownloaded(int result) {
        switch (result) {
            case NalogApiHelper.OK:
                mProgressBar.setVisibility(View.GONE);
                break;

            case NalogApiHelper.NO_INTERNET_CONNECTION:
                // TODO Fix hardcoded string
                Snackbar.make(mRootLayout, R.string.error_no_internet, BaseTransientBottomBar.LENGTH_SHORT)
                        .setAnchorView(R.id.bottom_appbar)
                        .show();
                resumeFab();
                break;

            case NalogApiHelper.EMPTY_CREDENTIALS:
                Snackbar.make(mRootLayout, R.string.error_wrong_cred, BaseTransientBottomBar.LENGTH_SHORT)
                        .setAnchorView(R.id.bottom_appbar)
                        .setAction(R.string.snack_action_sign, v -> {
                            new EmptyCredentialsDialogFragment(this).show(getSupportFragmentManager(), "EMPTY_CRED");
                        }).show();
                resumeFab();
                break;


            case NalogApiHelper.RECEIPT_NOT_EXISTS:
                Snackbar.make(mRootLayout, R.string.error_receipt_not_exists, BaseTransientBottomBar.LENGTH_SHORT)
                        .setAnchorView(R.id.bottom_appbar)
                        .show();
                resumeFab();
                break;

            case NalogApiHelper.IO_ERROR:
            case NalogApiHelper.TOO_MANY_ATTEMPTS:
                Snackbar.make(mRootLayout, R.string.error_too_many_attempts, BaseTransientBottomBar.LENGTH_SHORT)
                        .setAnchorView(R.id.bottom_appbar)
                        .show();
                resumeFab();
                break;

            case NalogApiHelper.ALREADY_DOWNLOADING:
                Snackbar.make(mRootLayout, R.string.error_already_downloading, BaseTransientBottomBar.LENGTH_SHORT)
                        .setAnchorView(R.id.bottom_appbar)
                        .show();
                resumeFab();
                break;

            case NalogApiHelper.WRONG_CREDENTIALS:
                // TODO Run auth form
                Toast.makeText(this, R.string.error_wrong_cred, Toast.LENGTH_SHORT).show();
                Snackbar.make(mRootLayout, R.string.error_wrong_cred, BaseTransientBottomBar.LENGTH_SHORT)
                        .setAnchorView(R.id.bottom_appbar)
                        .setAction(R.string.snack_action_update_cred, v -> {
                            new EmptyCredentialsDialogFragment(this).show(getSupportFragmentManager(), "WRONG_CRED");
                        }).show();
                resumeFab();
                break;

            default:
                resumeFab();
                break;
        }
    }

    private void resumeFab() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mFAB.show();
    }

    @Override
    public void dialogDone(boolean result) {
        if (result) {
            mFAB.hide();
            mProgressBar.setVisibility(View.VISIBLE);
            NetworkUtils.checkAndDownloadReceipt(mReceipt, this);
        }
    }
}
