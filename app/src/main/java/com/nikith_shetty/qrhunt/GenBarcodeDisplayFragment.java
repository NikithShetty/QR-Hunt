package com.nikith_shetty.qrhunt;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class GenBarcodeDisplayFragment extends Fragment implements View.OnClickListener{

    private static String TAG = GenBarcodeDisplayFragment.class.getName();

    ImageView barcodeImg;
    Button saveBtn;
    Bitmap barcodeImageData;
    EditText fileName;
    TextView fileNameText;
    LinearLayout hiddenFileInfo;


    private int MY_REQUEST_STORAGE_ID = 5658;
    private int MY_REQUEST_FILE_PATH_ID = 5622;

    public GenBarcodeDisplayFragment() {
        // Required empty public constructor
    }

    public static GenBarcodeDisplayFragment newInstance(Bundle data){
        GenBarcodeDisplayFragment n = new GenBarcodeDisplayFragment();
        n.setArguments(data);
        return n;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gen_barcode_display, container, false);

        barcodeImg = (ImageView) view.findViewById(R.id.disp_gen_barcode);
        saveBtn = (Button) view.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);
        fileName = (EditText) view.findViewById(R.id.fileName);
        fileNameText = (TextView) view.findViewById(R.id.fileNameText);
        hiddenFileInfo = (LinearLayout) view.findViewById(R.id.hiddenFileInfo);

        barcodeImageData = getArguments().getParcelable("bitmap");
        if(barcodeImageData!=null){
            barcodeImg.setImageBitmap(barcodeImageData);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.equals(saveBtn)){
            checkPermissionAndStartSaveToFile();
        }
    }

    private void checkPermissionAndStartSaveToFile(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_REQUEST_STORAGE_ID);
        }else {
            if (hiddenFileInfo.getVisibility() == View.VISIBLE &&
                    isExternalStorageWritable() &&
                    barcodeImageData != null) {
                if (fileName.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Enter a file name", Toast.LENGTH_SHORT).show();
                } else {
                    saveToFile(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath(),
                            fileName.getText().toString(), barcodeImageData);
                }
            } else {
                hiddenFileInfo.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],
                                          int[] grantResults){
        if(requestCode == MY_REQUEST_STORAGE_ID){
            if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Permission granted
                checkPermissionAndStartSaveToFile();
            } else {
                //Permission Denied
                //Go back to main screen
                getActivity().finish();
            }
        }
    }

    private void saveToFile(String filePath, String fileName, Bitmap barcodeImageData) {
        if(filePath == null){
            filePath = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).getAbsolutePath();
        }
        File imageFile = new File(filePath, fileName + ".jpeg");
        try {
            OutputStream stream = new FileOutputStream(imageFile);
            barcodeImageData.compress(Bitmap.CompressFormat.JPEG, 85, stream);
            stream.flush();
            stream.close();
            Toast.makeText(getContext(), "File save as " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
