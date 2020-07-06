package ru.c17.balance.SettingsScreen;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.c17.balance.App;
import ru.c17.balance.R;

import ru.c17.balance.connection.NetworkService;
import ru.c17.balance.connection.RestoreAuth;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInDialogFragment extends DialogFragment implements View.OnClickListener {

    public interface SignInDialogListener {
        void dialogDone(boolean result);
    }

    private  SignInDialogListener mListener;

    TextInputLayout mInputPhone;
    TextInputLayout mInputEmail;
    TextInputLayout mInputPass;

    TextInputEditText mEditPhone;
    TextInputEditText mEditEmail;
    TextInputEditText mEditPass;

    Button mSignUpButton;
    Button mSignInButton;
    Button mRestoreButton;
    SharedPreferences mSharedPreferences;


    Switch mSwitch;

    Context mContext;


    public SignInDialogFragment() {
    }

    public SignInDialogFragment(SignInDialogListener listener) {
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_sign_in_material, null);

        mInputPhone = v.findViewById(R.id.input_phone);
        mInputEmail = v.findViewById(R.id.input_email);
        mInputPass = v.findViewById(R.id.input_password);

        mEditPhone = v.findViewById(R.id.edit_phone);
        mEditEmail = v.findViewById(R.id.edit_email);
        mEditPass = v.findViewById(R.id.edit_password);

        mSignUpButton = v.findViewById(R.id.button_sign_up);
        mSignUpButton.setOnClickListener(this);
        mSignInButton = v.findViewById(R.id.button_sign_in);
        mSignInButton.setOnClickListener(this);
        mRestoreButton = v.findViewById(R.id.button_restore);
        mRestoreButton.setOnClickListener(this);

        mSwitch = v.findViewById(R.id.switch_sign);

        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                singUpMode();
            } else {
                signInMode();
            }
        });

        mSharedPreferences = mContext.getSharedPreferences(getString(R.string.shared_preferences_main),Context.MODE_PRIVATE);

        mEditPhone.setText(mSharedPreferences.getString(getString(R.string.pref_nalog_api_phone_key),""));
        mEditPass.setText(mSharedPreferences.getString(getString(R.string.pref_nalog_api_password_key),""));
        Log.d("CREADENTIALS/SIGNIN", mEditPhone.getText().toString() + ":" + mEditPass.getText().toString());

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = (int) (point.x * 0.8);
        int height = (int) (point.y * 0.6);
        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void signInMode() {
        mInputEmail.setVisibility(View.GONE);
        mSignUpButton.setVisibility(View.GONE);
        mSignInButton.setVisibility(View.VISIBLE);
        mRestoreButton.setVisibility(View.VISIBLE);
    }

    private void singUpMode() {
        mInputEmail.setVisibility(View.VISIBLE);
        mSignUpButton.setVisibility(View.VISIBLE);
        mSignInButton.setVisibility(View.GONE);
        mRestoreButton.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_up:
                break;

            case R.id.button_sign_in: {
                final String username = mEditPhone.getText().toString();
                final String password = mEditPass.getText().toString();
                NetworkService.getInstance().getNalogApi().signIn(Credentials.basic(username, password)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putString(getString(R.string.pref_nalog_api_phone_key), username);
                            editor.putString(getString(R.string.pref_nalog_api_password_key), password);
                            editor.apply();
                            Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();

                            // TODO Fix crash
                            App.setIsAuthorized(true);
                            if (mListener != null) {
                                mListener.dialogDone(true);
                            }
                            dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                break;
            }


            case R.id.button_restore: {
                final String username = mEditPhone.getText().toString();
                RestoreAuth restoreAuth = new RestoreAuth();
                restoreAuth.setPhone(username);
                NetworkService.getInstance().getNalogApi().restore(restoreAuth).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                break;
            }


            default:
                return;
        }
    }
}
