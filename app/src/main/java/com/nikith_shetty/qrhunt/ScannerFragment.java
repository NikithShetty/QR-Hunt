package com.nikith_shetty.qrhunt;


import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScannerFragment extends Fragment {

    private FragmentInterface fragmentListener;
    private int MY_PERMISSIONS_REQUEST_READ_CAMERA = 5421;
    SurfaceView cameraPreview;

    public ScannerFragment() {
        // Required empty public constructor
    }

    public static ScannerFragment newInstance() {
        return new ScannerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);

        cameraPreview = (SurfaceView) view.findViewById(R.id.barcode_scanner);
        processCameraInput();

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],
                                           int[] grantResults){
        if(requestCode == MY_PERMISSIONS_REQUEST_READ_CAMERA){
            if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Permission granted
                processCameraInput();
            } else {
                //Permission Denied
                //Go back to main screen
                getActivity().finish();
            }
        }
    }

    private void processCameraInput() {

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getActivity()).build();
        final CameraSource cameraSource = new CameraSource.Builder(getActivity(), barcodeDetector)
                .setAutoFocusEnabled(true)
                .build();
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_READ_CAMERA);
                }else {
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size()>0){
                    Bundle data = new Bundle();
                    data.putString("barcodeData", barcodes.valueAt(0).displayValue);
                    sendResult(data);
                }
            }
        });

    }

    private void sendResult(Bundle data){
        if( data != null)
            fragmentListener.onChangeFragment(ScannedResultFragment.newInstance(data));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final Activity activity = getActivity();
        Log.e("getActivity", "activity must be got " + activity.toString());
        if(activity instanceof FragmentInterface) {
            fragmentListener = (FragmentInterface) activity;
            Log.e("fragmentListener", "must me defined as " + fragmentListener.toString());
        }else{
            throw new IllegalArgumentException("Activity should implement FragmentInterface");
        }
    }
}
