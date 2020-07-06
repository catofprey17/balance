package ru.c17.balance;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import ru.c17.balance.R;

import ru.c17.balance.SettingsScreen.SettingsActivity;
import ru.c17.balance.SettingsScreen.SignInDialogFragment;

public class EmptyCredentialsDialogFragment extends DialogFragment {

    private Context mContext;
    private SignInDialogFragment.SignInDialogListener mListener;

    public EmptyCredentialsDialogFragment() {
    }

    public EmptyCredentialsDialogFragment(SignInDialogFragment.SignInDialogListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.empty_cred_dialog_title);
        builder.setMessage(R.string.empty_cred_dialog_message);
        builder.setPositiveButton(R.string.dialog_button_cred_sign_in, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(mContext, SettingsActivity.class);
//
//                startActivity(intent);
                new SignInDialogFragment(mListener).show(getParentFragmentManager(), null);
            }
        });
        builder.setNegativeButton(R.string.dialog_button_cred_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
