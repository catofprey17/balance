package ru.c17.balance.DetailScreen;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.WriterException;

import ru.c17.balance.Utils.BarcodeUtils;
import ru.c17.balance.R;

public class BarcodeDialog extends DialogFragment {
    private String mBarcodeString;

    public BarcodeDialog() {
    }

    public BarcodeDialog(String barcodeString) {
        mBarcodeString = barcodeString;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_barcode, container, false);
        ImageView barcodeImageView = v.findViewById(R.id.image_barcode);
        try {
            int size = barcodeImageView.getLayoutParams().width;

            Bitmap bitmap = BarcodeUtils.getQrCodeFromString(mBarcodeString, size);
            barcodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            dismiss();
        }
        return v;
    }
}
