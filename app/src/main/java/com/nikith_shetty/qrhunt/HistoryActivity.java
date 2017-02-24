package com.nikith_shetty.qrhunt;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {


    TextView noScannedText;
    CursorListviewAdapter adapter;
    ListView listView;
    Cursor cursor;
    QRDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noScannedText = (TextView) findViewById(R.id.no_QR_Scanned_text);
        listView = (ListView) findViewById(R.id.listview_display);
        listView.setEmptyView(noScannedText);
        db = new QRDatabase(this);
        cursor = db.readAllData();

        adapter = new CursorListviewAdapter(this, cursor, 0);
        listView.setAdapter(adapter);
    }
}
