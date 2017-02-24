package com.nikith_shetty.qrhunt;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * A simple {@link Fragment} subclass.
 */
public class BarcodeInputDetailsFragment extends Fragment implements View.OnClickListener, Runnable {

    public static int WIDTH = 500;
    final static String TAG = "BarcodeInputFragment";

    FragmentInterface fragmentInterface;
    Button genBtn;
    EditText msg, key;
    TextView enterKeyText;
    CheckBox encryptCheck;

    ProgressDialog progressDialog;

    public BarcodeInputDetailsFragment() {
        // Required empty public constructor
    }

    public static BarcodeInputDetailsFragment newInstance(){
        return new BarcodeInputDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_barcode_input_details, container, false);

        msg = (EditText) view.findViewById(R.id.text_msg);
        enterKeyText = (TextView) view.findViewById(R.id.enter_key_text);
        key = (EditText) view.findViewById(R.id.key_input);
        encryptCheck = (CheckBox) view.findViewById(R.id.to_encrypt);
        encryptCheck.setOnClickListener(this);
        genBtn = (Button) view.findViewById(R.id.startGenBtn);
        genBtn.setOnClickListener(this);

        String pref_width_string = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(SettingsFragment.KEY_PREF_WIDTH, "");
        if(!pref_width_string.equals("")) {
            Log.e("WIDTH", "" + pref_width_string);
            WIDTH = Integer.parseInt(pref_width_string);
        }
        return view;
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? ContextCompat.getColor(getActivity(), R.color.black):ContextCompat.getColor(getActivity(), R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    private String message = null;


    @Override
    public void onClick(View view) {
        if(view.equals(genBtn)){
            if(!"".equals(msg.getText().toString()) &&
                    Pattern.matches("^[a-zA-Z0-9_ ~!@#$%&*()/\\\"?<>,':;+-=^.]*$", msg.getText().toString())){
                new Thread(this).start();
            }else{
                Toast.makeText(getContext(), "Enter some valid text", Toast.LENGTH_SHORT).show();
            }
        }else if(view.equals(encryptCheck)){
            if(encryptCheck.isChecked()){
                enterKeyText.setVisibility(View.VISIBLE);
                key.setVisibility(View.VISIBLE);
            }else{
                enterKeyText.setVisibility(View.GONE);
                key.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void run() {
        //check whether Checkbox selected
        if(encryptCheck.isChecked()){
            //check if KEY input is not empty and has 4 digits in it
            if(!key.getText().toString().equals("") &&
                    Pattern.matches("\\d{4}", key.getText().toString())){
                //encrypt the message
                try {
                    message = CryptoEngine.encrypt(Integer.parseInt(key.getText().toString()),
                            msg.getText().toString());
                    Message msg = Message.obtain();
                    msg.setTarget(startProgressDialog);
                    msg.sendToTarget();
                    Log.e(TAG, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Enter a valid 4 digit number", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
        }else{
            message = msg.getText().toString();
            Message msg = Message.obtain();
            msg.setTarget(startProgressDialog);
            msg.sendToTarget();
        }

        Bitmap bitmap;
        if(message!=null) {
            try {
                bitmap = encodeAsBitmap(message);
                Bundle data = new Bundle();
                data.putParcelable("bitmap", bitmap);
                sendResult(data);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    Handler startProgressDialog = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Generating QR Code");
            progressDialog.show();
        }
    };

    private void sendResult(Bundle data){
        if( data != null)
            fragmentInterface.onChangeFragment(GenBarcodeDisplayFragment.newInstance(data));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final Activity activity = getActivity();
        Log.e("getActivity", "activity must be got " + activity.toString());
        if(activity instanceof FragmentInterface) {
            fragmentInterface = (FragmentInterface) activity;
        }else{
            throw new IllegalArgumentException("Activity should implement FragmentInterface");
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
