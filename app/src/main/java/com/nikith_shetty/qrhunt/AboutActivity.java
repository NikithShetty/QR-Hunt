package com.nikith_shetty.qrhunt;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity implements OnClickListener{

    TextView myEmail, nikEmail;
    ImageButton github;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        github=(ImageButton)findViewById(R.id.github);
        github.setOnClickListener(this);

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
        }if(view.equals(nikEmail)){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:nikhilnagaraju96@gmail.com"));
            startActivity(emailIntent);
        }if(view.equals(github)){
            //github link of project
            Uri uri = Uri.parse(getString(R.string.gitlink));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
