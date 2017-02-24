package com.nikith_shetty.qrhunt;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button scanBtn, genBtn, histBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = (Button) findViewById(R.id.scanBtn);
        genBtn = (Button) findViewById(R.id.generateBtn);
        histBtn = (Button) findViewById(R.id.historyBtn);
        scanBtn.setOnClickListener(this);
        genBtn.setOnClickListener(this);
        histBtn.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if(view.equals(scanBtn)){
            startActivity(new Intent(this, ScannerActivity.class));
        }else if(view.equals(genBtn)){
            startActivity(new Intent(this, GeneratorActivity.class));
        }else if(view.equals(histBtn)){
            startActivity(new Intent(this, HistoryActivity.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
