package com.nikith_shetty.qrhunt;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity implements OnClickListener{

    TextView myEmail, nikEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        myEmail = (TextView) findViewById(R.id.myEmail);
        myEmail.setText(Html.fromHtml(getString(R.string.myEmail)));
        myEmail.setOnClickListener(this);
        nikEmail = (TextView) findViewById(R.id.nikhilEmail);
        nikEmail.setText(Html.fromHtml(getString(R.string.nikhilEmail)));
        nikEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(myEmail)){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:shettynikith3@gmail.com"));
            startActivity(emailIntent);
        }else if(view.equals(nikEmail)){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:nikhilnagaraju96@gmail.com"));
            startActivity(emailIntent);
        }
    }
}
