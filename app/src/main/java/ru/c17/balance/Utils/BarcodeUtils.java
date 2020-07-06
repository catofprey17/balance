package ru.c17.balance.Utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class BarcodeUtils {
    public static Bitmap getQrCodeFromString(String qrString, int size) throws WriterException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(qrString, BarcodeFormat.QR_CODE, size, size);
        BarcodeEncoder encoder = new BarcodeEncoder();
        return encoder.createBitmap(bitMatrix);
    }
}
