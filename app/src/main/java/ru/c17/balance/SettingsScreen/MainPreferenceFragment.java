package ru.c17.balance.SettingsScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import ru.c17.balance.AboutActivity;
import ru.c17.balance.App;
import ru.c17.balance.R;
import ru.c17.balance.data.ReceiptRepository;


public class MainPreferenceFragment extends PreferenceFragmentCompat implements SignInDialogFragment.SignInDialogListener {

    private SharedPreferences mSharedPreferences;
    private ReceiptRepository mRepository;
    private DropDownPreference mThemePreference;
    private SwitchPreference mAutoDownloadPreference;

    private Context mContext;

    //TODO Fix auth


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v != null && getContext() != null)
            v.setBackgroundColor(getContext().getColor(R.color.colorFocusBack));
        return v;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        mRepository = App.getReceiptRepository();
        setPreferencesFromResource(R.xml.main_preferences, rootKey);

        // TODO Complete
//        if (getArguments() != null && getArguments().containsKey(SettingsActivity.EXTRA_PREF)) {
//            int prefResId = getArguments().getInt(SettingsActivity.EXTRA_PREF);
//            if (prefResId != -1) {
//                Preference preference = this.findPreference(getString(prefResId));
//            }
//        }

        // Auth preference
        Preference signInPref = this.findPreference(getString(R.string.pref_nalog_api_auth_key));
        if (signInPref != null) {
            if (App.isAuthorized()) {
                signInPref.setSummary(mSharedPreferences.getString(getString(R.string.pref_nalog_api_phone_key),""));
            }

            signInPref.setOnPreferenceClickListener(preference -> {
                new SignInDialogFragment(MainPreferenceFragment.this).show(getParentFragmentManager(),getString(R.string.pref_nalog_api_auth_key));
                return true;
            });
        }

        // Auto Download preference
        mAutoDownloadPreference = this.findPreference(getString(R.string.pref_nalog_api_autodownload_key));
        if (mAutoDownloadPreference != null) {
            mAutoDownloadPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean value = (boolean) newValue;
                mSharedPreferences.edit().putBoolean(getString(R.string.pref_nalog_api_autodownload_key), value).apply();
                return true;
            });
            checkAuthorization();
        }

        // Dark Theme preference
        mThemePreference = this.findPreference(getString(R.string.pref_theme_key));
        if (mThemePreference != null) {
            mThemePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                String value = (String) newValue;
                if (value.equals(getString(R.string.theme_value_dark))) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else if (value.equals(getString(R.string.theme_value_light))) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (value.equals(getString(R.string.theme_value_default))) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
                mSharedPreferences.edit().putString(getString(R.string.pref_theme_key), value).apply();
                return true;
            });
        }

        // Drop Receipts preference
        Preference dropReceiptsPreference = findPreference(getString(R.string.pref_storage_drop_key));
        if (dropReceiptsPreference != null) {
            dropReceiptsPreference.setOnPreferenceClickListener(preference -> {

                //TODO Fix colors Night
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.dialog_drop_title)
                        .setMessage(R.string.dialog_drop_message)
                        .setPositiveButton(R.string.dialog_drop_yes, (dialog, which) -> {
                            mRepository.deleteAll();
                            if (getView() != null) {
                                Snackbar.make(getView(), R.string.dialog_drop_done, BaseTransientBottomBar.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.dialog_drop_no, (dialog, which) -> {})
                        .create()
                        .show();
                return true;
            });
        }


        // About preference
        Preference aboutPreference = findPreference(getString(R.string.pref_about_key));
        if (aboutPreference != null) {
            aboutPreference.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                return true;
            });
        }
    }


    private void checkAuthorization() {
        if (!App.isAuthorized()) {
            mAutoDownloadPreference.setEnabled(false);
        }
    }


    @Override
    public void onResume() {
        super.onResume(); 

        mThemePreference.setValue(mSharedPreferences.getString(getString(R.string.pref_theme_key), getString(R.string.theme_value_default)));
        mAutoDownloadPreference.setChecked(mSharedPreferences.getBoolean(getString(R.string.pref_nalog_api_autodownload_key), false));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(getString(R.string.shared_preferences_main), Context.MODE_PRIVATE);
    }

    @Override
    public void dialogDone(boolean result) {
        if (result) {
            App.setIsAuthorized(true);
        }
        checkAuthorization();
    }
}
