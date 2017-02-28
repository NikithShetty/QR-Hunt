package com.nikith_shetty.qrhunt;

import com.pushbots.push.Pushbots;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.regex.Pattern;

public class StartActivity extends AppCompatActivity {

    int PLAY_SERVICE_RESOLUTION_REQUEST = 9000;
    int SPLASH_DISPLAY_LENGTH = 1500;
    int MY_PERMISSIONS_REQUEST_ACCOUNTS = 52142;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Pushbots.sharedInstance().init(this);
        setPushbotsAlias();

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

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],
                                           int[] grantResults){
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCOUNTS){
            if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Permission granted
                setPushbotsAlias();
            } else {
                //Permission Denied
                //Go back to main screen
                Toast.makeText(this, "No permission to access ACCOUNT", Toast.LENGTH_SHORT).show();
            }
        }
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

    private void setPushbotsAlias(){
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS},
                    MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return;
        }
        Account[] accounts = AccountManager.get(getBaseContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                Pushbots.sharedInstance().setAlias(possibleEmail);
            }
        }
    }
}
