package ru.c17.balance.MainScreen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import ru.c17.balance.CameraSource;
import ru.c17.balance.R;
import ru.c17.balance.ReceiptList.ReceiptViewModel;
import ru.c17.balance.animation.AnimationUtils;
import ru.c17.balance.animation.RevealAnimationSetting;
import ru.c17.balance.data.Receipt;

//import com.google.android.gms.vision.CameraSource;

public class BarcodeReaderFragment extends Fragment implements AnimationUtils.Dismissible {

    static final String ARG_REVEAL_SETTINGS = "ARG_REVEAL_SETTINGS";
    static final String TAG = BarcodeReaderFragment.class.getSimpleName();

    public interface ReaderListener {
        void onReadingFinish();
    }

    private CameraSource mCameraSource;
    private BarcodeReaderCameraPreview mCameraPreview;
    private ReaderListener mListener;
    private TextView mTextAlert;
    private ImageView mFlashImage;

    private ReceiptViewModel mViewModel;
    private BarcodeDetector mDetector;

    private RevealAnimationSetting mAnimationSetting;

    private Context mContext;

    private boolean crazyFlag = false;

    private volatile boolean readingDone = false;
    private ExecutorService checkQRExecutor;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barcode_reader, container, false);

        checkQRExecutor = Executors.newSingleThreadExecutor();

        mCameraPreview = view.findViewById(R.id.surface_camera);
        mListener = (ReaderListener) getActivity();
        mTextAlert = view.findViewById(R.id.text_alert);

        SwitchMaterial switchFlash = view.findViewById(R.id.switch_flashlight);
        mFlashImage = view.findViewById(R.id.image_flash);

        switchFlash.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mFlashImage.setImageResource(R.drawable.ic_flash_on);
                mCameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            } else {
                mFlashImage.setImageResource(R.drawable.ic_flash_off);
                mCameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }
        });

        mViewModel = new ViewModelProvider(this).get(ReceiptViewModel.class);

        if (getContext() instanceof ReaderListener) {
            mListener = (ReaderListener) getContext();
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            mAnimationSetting = getArguments().getParcelable(ARG_REVEAL_SETTINGS);

            AnimationUtils.openCircularRevealAnimation(
                    getContext(),
                    view,
                    mAnimationSetting,
                    () -> {
                        if (crazyFlag) {
                            initCamera();

                        } else {
                            crazyFlag = true;
                        }
                    });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (crazyFlag) {
            initCamera();
        } else {
            crazyFlag = true;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onPause() {
        super.onPause();
        crazyFlag = false;
    }

    @Override
    public void dismiss(final OnDismissedListener listener) {

        AnimationUtils.closeCircularRevealAnimation(getView(),
                mAnimationSetting,
                () -> {
                    listener.onDismissed();
                    if (mCameraSource != null) {
                        mCameraSource.stop();
                    }
                });
    }


    private void initCamera() {
        mDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();

        // TODO Add flashlight on/off
        this.mCameraSource = new CameraSource.Builder(mContext, mDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(30.0f)
                .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
//                .setAutoFocusEnabled(true)
                .build();

        // TODO Optimize
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mCameraSource.start(mCameraPreview.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mDetector.setProcessor(new Detector.Processor<Barcode>() {

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size()>0) {
                    final Pattern pattern = Pattern.compile("^t=\\d{8}T\\d{4,6}&s=\\d+\\.\\d{1,2}&fn=\\d{16}&i=\\d{1,10}&fp=\\d{1,10}&n=\\d$");
                    final String qrValue = qrCodes.valueAt(0).displayValue;
                    if (pattern.matcher(qrValue).find()) {
                        checkQRExecutor.submit(() -> {
                            if (!readingDone) {
                                Receipt receipt = Receipt.parseQR(qrValue);
                                if (mViewModel.checkAndWriteReceipt(receipt)) {
                                    // TODO Close fragment
                                    readingDone = true;
                                    mDetector.release();
                                } else {
                                    // TODO Notify that already exist
                                    showAlreadyExistAlert();
                                }
                            }
                        });
                    }

                }
            }



            @Override
            public void release() {
                Activity activity = (Activity) mContext;
                activity.runOnUiThread(() -> mListener.onReadingFinish());
            }
        });







    }

    private void showAlreadyExistAlert() {
        if (mTextAlert.getVisibility() != View.VISIBLE) {
            mTextAlert.post(() -> mTextAlert.setVisibility(View.VISIBLE));
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(2000);
                        mTextAlert.post(() -> mTextAlert.setVisibility(View.INVISIBLE));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

}
