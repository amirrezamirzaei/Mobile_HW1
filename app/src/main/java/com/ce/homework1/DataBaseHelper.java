package com.ce.homework1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ce.homework1.model.Coin;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String COIN_TABLE = "COIN_TABLE";
    public static final String COLUMN_PRICE = "PRICE";
    public static final String COLUMN_PERCENT_CHANGE_HOUR = "PERCENT_CHANGE_HOUR";
    public static final String COLUMN_PERCENT_CHANGE_DAY = "PERCENT_CHANGE_DAY";
    public static final String COLUMN_PERCENT_CHANGE_WEEK = "PERCENT_CHANGE_WEEK";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_SYMBOL = "SYMBOL";
    public static final String COLUMN_IMAGE_URL = "IMAGE_URL";
    public static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "coin.db", null, 1);
    }

    //create new table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + COIN_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRICE + " DOUBLE, " +
                COLUMN_PERCENT_CHANGE_HOUR + " DOBLE, " +
                COLUMN_PERCENT_CHANGE_DAY + " DOUBLE, " +
                COLUMN_PERCENT_CHANGE_WEEK + " DOUBLE, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SYMBOL + " TEXT, "
                + COLUMN_IMAGE_URL + " TEXT )";


        db.execSQL(createTableStatement);
    }

    //version number changed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS coin");
        onCreate(db);
    }

    public boolean insertCoin(Coin coin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRICE, coin.getPrice());
        cv.put(COLUMN_PERCENT_CHANGE_HOUR, coin.getPercentChangeHour());
        cv.put(COLUMN_PERCENT_CHANGE_DAY, coin.getPercentChangeDay());
        cv.put(COLUMN_PERCENT_CHANGE_WEEK, coin.getPercentChangeWeek());
        cv.put(COLUMN_NAME, coin.getName());
        cv.put(COLUMN_SYMBOL, coin.getSymbol());
        cv.put(COLUMN_IMAGE_URL, coin.getImageUrl());

        db.insert(COIN_TABLE, null, cv);
        return true;

//        long insert = db.insert(COIN_TABLE, null, cv);
//        return insert != 1;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM COIN_TABLE WHERE ID=" + COLUMN_ID + "", null);
        return res;
    }

    public boolean updateCoin(Coin coin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRICE, coin.getPrice());
        cv.put(COLUMN_PERCENT_CHANGE_HOUR, coin.getPercentChangeHour());
        cv.put(COLUMN_PERCENT_CHANGE_DAY, coin.getPercentChangeDay());
        cv.put(COLUMN_PERCENT_CHANGE_WEEK, coin.getPercentChangeWeek());
        cv.put(COLUMN_NAME, coin.getName());
        cv.put(COLUMN_SYMBOL, coin.getSymbol());
        cv.put(COLUMN_IMAGE_URL, coin.getImageUrl());

        db.update(COIN_TABLE, cv, "id = ? ", new String[]{Integer.toString(coin.getId())});
        return true;
    }

    public ArrayList<String> getAllCoins() {
        ArrayList<String> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from COIN_TABLE", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}
