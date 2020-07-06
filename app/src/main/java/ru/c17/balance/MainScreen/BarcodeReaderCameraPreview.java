package ru.c17.balance.MainScreen;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BarcodeReaderCameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    public BarcodeReaderCameraPreview(Context context) {
        super(context);
    }

    public BarcodeReaderCameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarcodeReaderCameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BarcodeReaderCameraPreview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
