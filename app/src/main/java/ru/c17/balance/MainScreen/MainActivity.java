package ru.c17.balance.MainScreen;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.c17.balance.AddReceiptScreen.AddReceiptFragment;
import ru.c17.balance.FakeFiller;
import ru.c17.balance.PeriodsScreen.PeriodsActivity;
import ru.c17.balance.R;
import ru.c17.balance.ReceiptList.ReceiptListFragment;
import ru.c17.balance.SettingsScreen.SettingsActivity;
import ru.c17.balance.animation.AnimationUtils;
import ru.c17.balance.animation.RevealAnimationSetting;

public class MainActivity extends AppCompatActivity implements BarcodeReaderFragment.ReaderListener  {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 625;

    public enum MODE {MAIN, BARCODE_READER, ADD_RECEIPT}

    private Menu mMenu;

    FloatingActionButton mFAB;
    ConstraintLayout mRootLayout;
    Fragment fragmentBarcodeReader;
    ReceiptListFragment mReceiptListFragment;

    FragmentContainerView mContainer;
    FragmentContainerView mBarcodeReaderContainer;
    BottomAppBar mBottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomAppBar = findViewById(R.id.bottom_appbar);
        setSupportActionBar(findViewById(R.id.bottom_appbar));

//        setupTheme();

        mFAB = findViewById(R.id.fab_add);
        mRootLayout = findViewById(R.id.root_layout);
        mContainer = findViewById(R.id.fragment_container);
        mBarcodeReaderContainer = findViewById(R.id.barcode_fragment_container);

        mReceiptListFragment = new ReceiptListFragment();

        // Set up receipt list fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mReceiptListFragment)
                .commit();

        setFabMode(MODE.MAIN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.stats_main_menu_item:
                startActivity(new Intent(MainActivity.this, PeriodsActivity.class));
                return true;

            case R.id.settings_main_menu_item:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                runBarcodeReader();
            } else {
                Toast.makeText(MainActivity.this, "You need to grant permission to use camera", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(BarcodeReaderFragment.TAG) != null) {
            onReadingFinish();
        } else {
            super.onBackPressed();
        }
    }

    public void runBarcodeReader() {
//        if (fragmentBarcodeReader == null) {
            fragmentBarcodeReader = new BarcodeReaderFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(BarcodeReaderFragment.ARG_REVEAL_SETTINGS, constructRevealSettings());
            fragmentBarcodeReader.setArguments(bundle);

//        }

        // Replace ReceiptListFragment --> BarcodeReaderFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.barcode_fragment_container,
                        fragmentBarcodeReader,BarcodeReaderFragment.TAG)
                .commit();
        mContainer.setEnabled(false);
    }

    private RevealAnimationSetting constructRevealSettings() {
        return new RevealAnimationSetting(
                (int) (mFAB.getX() + mFAB.getWidth() / 2),
                (int) (mFAB.getY() + mFAB.getHeight() / 2),
                mContainer.getWidth(),
                mContainer.getHeight());
    }

    @Override
    public void onReadingFinish() {

        // Show appbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }

        setFabMode(MODE.MAIN);


        ((AnimationUtils.Dismissible) fragmentBarcodeReader).dismiss(() -> getSupportFragmentManager()
                .beginTransaction()
                .remove(fragmentBarcodeReader)
                .commitAllowingStateLoss());

        mContainer.setEnabled(true);
    }

/// Already moved to App.java
//    private void setupTheme() {
//        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_main), MODE_PRIVATE);
//        String theme = sharedPreferences.getString(getString(R.string.pref_theme_key), getString(R.string.theme_value_default));
//        if (theme.equals(getString(R.string.theme_value_dark))) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        } else if (theme.equals(getString(R.string.theme_value_light))) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        } else if (theme.equals(getString(R.string.theme_value_default))) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
//        }
//    }

    public void closeAddReceiptFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mReceiptListFragment)
                .commit();
    }


    public void setFabMode(MODE fabMode) {
        switch (fabMode) {
            case MAIN:
                mFAB.setOnClickListener(v -> {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        runBarcodeReader();
                        setFabMode(MODE.BARCODE_READER);
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                    }
                });

                mFAB.setOnLongClickListener(v -> {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new AddReceiptFragment())
                            .addToBackStack(null)
//                            .setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_open_exit)
                            .commit();
                    return true;
                });
//                mFAB.setOnLongClickListener(v -> {
//                    FakeFiller.fillData();
//                    return true;
//                });


                mFAB.setImageResource(R.drawable.ic_fab_barcode);
                setMenuItemsVisible(true);
                mBottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                mBottomAppBar.performShow();
                break;

            case BARCODE_READER:
                mFAB.setOnClickListener(v -> onReadingFinish());
                mFAB.setOnLongClickListener(null);

                mFAB.setImageResource(R.drawable.ic_fab_close);
                mBottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                mBottomAppBar.performHide();
                setMenuItemsVisible(false);
                break;

            case ADD_RECEIPT:
                mFAB.setOnClickListener(null);
                mFAB.setOnLongClickListener(null);

                mFAB.setImageResource(R.drawable.ic_fab_done);
                mBottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
                mBottomAppBar.performShow();
                setMenuItemsVisible(false);
                break;


        }
    }

    private void setMenuItemsVisible(boolean visibility) {
        if (mMenu == null) return;
        for (int i = 0; i < mMenu.size(); i++) {
            mMenu.getItem(i).setVisible(visibility);
        }
    }

}


