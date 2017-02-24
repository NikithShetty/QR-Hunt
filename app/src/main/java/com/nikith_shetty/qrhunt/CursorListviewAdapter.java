package com.nikith_shetty.qrhunt;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Nikith_Shetty on 07/01/2017.
 */

public class CursorListviewAdapter extends CursorAdapter {


    public CursorListviewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_history, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView title = (TextView) view.findViewById(R.id.row_title);
        TextView timeStamp  = (TextView) view.findViewById(R.id.time_stamp);
        TextView decodeKey  = (TextView) view.findViewById(R.id.decode_key);
        Button delBtn = (Button) view.findViewById(R.id.delete_row_btn);

        final String rowId = cursor.getString(0);
        String timeText = cursor.getString(1);
        String keyText = cursor.getString(2);
        final String titleText = cursor.getString(3);

        title.setText(titleText);
        timeStamp.setText("Time Stamp : " + timeText);

        if(!(keyText==null || keyText=="")){
            decodeKey.setText("Key used for decrypting : " + keyText);
            decodeKey.setVisibility(View.VISIBLE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Scanned : " + titleText, Toast.LENGTH_SHORT).show();
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new QRDatabase(context).deleteData(rowId);
                cursor.requery();
                notifyDataSetChanged();
            }
        });
    }
}
