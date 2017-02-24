package com.nikith_shetty.qrhunt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class StartActivity extends AppCompatActivity {

    int PLAY_SERVICE_RESOLUTION_REQUEST = 9000;
    int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        checkPlayServices();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                StartActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private boolean checkPlayServices(){
        GoogleApiAvailability  apiAvailability = GoogleApiAvailability.getInstance();
        int result = apiAvailability.isGooglePlayServicesAvailable(this);
        if(result!= ConnectionResult.SUCCESS){
            if(apiAvailability.isUserResolvableError(result)){
                apiAvailability.getErrorDialog(this, result, PLAY_SERVICE_RESOLUTION_REQUEST, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        System.exit(1);
                    }
                }).show();
            }else{
                Log.e("StartActivity", "Cannot resolve Google play services");
                finish();
            }
            return false;
        }
        return true;
    }
}
