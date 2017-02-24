package com.nikith_shetty.qrhunt;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScannedResultFragment extends Fragment implements View.OnClickListener{

    private static String TAG = "ScannedResultFragment";

    private FragmentInterface listener;
    QRDatabase db;
    String barcodeData;
    TextView resultDisplay;
    Button scanMore;

    //Decode panel elements
    SlidingUpPanelLayout panelLayout;
    EditText decodeKey;
    Button panelUpBtn, decodeBtn;
    TextView decodeResultText, decodeResultDisplay;

    public ScannedResultFragment() {
        // Required empty public constructor
    }

    public static ScannedResultFragment newInstance(Bundle data){
        ScannedResultFragment n = new ScannedResultFragment();
        n.setArguments(data);
        return n;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scanned_result, container, false);
        resultDisplay = (TextView) view.findViewById(R.id.resultDisplay);
        scanMore = (Button) view.findViewById(R.id.scanMore);
        scanMore.setOnClickListener(this);

        barcodeData = getArguments().getString("barcodeData");
        if( barcodeData != null ){
            resultDisplay.setText(barcodeData);
            db = new QRDatabase(getContext());

            db.insertData(null, barcodeData);
        }

        panelLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_decode_panel);
        panelLayout.addPanelSlideListener(onPanelSlide());
        decodeKey = (EditText) view.findViewById(R.id.decode_key_input);
        panelUpBtn = (Button) view.findViewById(R.id.decodePanelUpBtn);
        panelUpBtn.setOnClickListener(this);
        decodeBtn = (Button) view.findViewById(R.id.decodeBtn);
        decodeBtn.setOnClickListener(this);
        decodeResultText = (TextView) view.findViewById(R.id.decodeResultText);
        decodeResultDisplay = (TextView) view.findViewById(R.id.decodedResultDisplay);

        return view;
    }

    private SlidingUpPanelLayout.PanelSlideListener onPanelSlide() {
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Decode");
                }else if(newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
                }
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final Activity activity = getActivity();
        if(activity instanceof FragmentInterface){
            listener = (FragmentInterface) activity;
        }else{
            throw new IllegalArgumentException("Activity should implement ScannedResultFragmentListener");
        }
    }

    @Override
    public void onClick(View view) {
        if(view.equals(scanMore)){
            listener.onChangeFragment(ScannerFragment.newInstance());
        }else if(view.equals(panelUpBtn)){
            panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }else if(view.equals(decodeBtn)){
            if(!decodeKey.getText().toString().equals("") && barcodeData!=null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String plainText = "";
                        try {
                            plainText = CryptoEngine.decrypt(Integer.parseInt(decodeKey.getText().toString()),
                                    barcodeData);
                            Log.e(TAG, "plainText Recieved : " + plainText);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message msg = Message.obtain();
                        msg.obj = plainText;
                        msg.setTarget(setResultDisplay);
                        msg.sendToTarget();
                    }
                }).start();
            }
        }
    }

    Handler setResultDisplay = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            decodeResultDisplay.setText((String)msg.obj);
            decodeResultText.setVisibility(View.VISIBLE);
            decodeResultDisplay.setVisibility(View.VISIBLE);

            //save decoded value to DB
            db = new QRDatabase(getContext());
            db.insertData(decodeKey.getText().toString(), (String)msg.obj);
        }
    };
}
