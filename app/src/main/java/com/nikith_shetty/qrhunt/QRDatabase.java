package com.nikith_shetty.qrhunt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikith_Shetty on 04/12/2016.
 */

public class QRDatabase extends SQLiteOpenHelper {

    private static String DB_NAME = "QR_DB";
    private static String TABLE_NAME = "QR_INFO";
    private static int DB_VERSION = 1;
    private static String ID_COL = "_id";
    private static String TIME_STAMP_COL = "time_stamp";
    private static String CODE_COL = "code";
    private static String DATA_COL = "data";

    public QRDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "( "
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TIME_STAMP_COL + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + CODE_COL + " TEXT, "
                + DATA_COL + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    long insertData(String code, String data){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CODE_COL, code);
        cv.put(DATA_COL, data);
        long rowID = db.insert(TABLE_NAME, null, cv);
        db.close();
        return rowID;
    }

    int deleteData(String id_col){
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_NAME, ID_COL + "=?", new String[]{id_col});
        db.close();
        return result;
    }

    Cursor readAllData(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.query(TABLE_NAME,
                 null,
                 null, null, null, null, null);
        return result;
    }

    Cursor queryById(String _id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.query(TABLE_NAME,
                new String[]{TIME_STAMP_COL, CODE_COL, DATA_COL},
                "id=?",
                new String[]{_id},
                null, null, null);
        db.close();
        return result;
    }
}
