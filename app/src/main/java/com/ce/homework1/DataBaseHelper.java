package com.ce.homework1;

import android.content.ContentValues;
import android.content.Context;
import android.content.SyncAdapterType;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public static final String COLUMN_ROW = "ROW_DATABSE";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "coin.db", null, 1);
    }

    //create new table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + COIN_TABLE + " (" +
                COLUMN_ID + " INTEGER," +
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
        Cursor res = getData(coin.getId());
        if(res.getCount()!=0){  //coin already exist
            updateCoin(coin);
            return true;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, coin.getId());
        cv.put(COLUMN_PRICE, coin.getPrice());
        cv.put(COLUMN_PERCENT_CHANGE_HOUR, coin.getPercentChangeHour());
        cv.put(COLUMN_PERCENT_CHANGE_DAY, coin.getPercentChangeDay());
        cv.put(COLUMN_PERCENT_CHANGE_WEEK, coin.getPercentChangeWeek());
        cv.put(COLUMN_NAME, coin.getName());
        cv.put(COLUMN_SYMBOL, coin.getSymbol());
        cv.put(COLUMN_IMAGE_URL, coin.getImageUrl());
        db.insert(COIN_TABLE, null, cv);
        return true;

    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM COIN_TABLE WHERE ID=" + id + "", null);
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

    public ArrayList<Coin> getAllCoins(int lastFetchedCoin,int limit) {
        ArrayList<Coin> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from COIN_TABLE LIMIT ?,?", new String[]{Integer.toString(lastFetchedCoin),Integer.toString(limit)} );
        res.moveToFirst();
        while(!res.isAfterLast()){
            String symbol = res.getString(res.getColumnIndex(COLUMN_SYMBOL));
            String name = res.getString(res.getColumnIndex(COLUMN_NAME));
            Integer id = Integer.parseInt(res.getString(res.getColumnIndex(COLUMN_ID)));
            Double price = Double.parseDouble(res.getString(res.getColumnIndex(COLUMN_PRICE)));
            Double percentChangeHour = Double.parseDouble(res.getString(res.getColumnIndex(COLUMN_PERCENT_CHANGE_HOUR)));
            Double percentChangeDay = Double.parseDouble(res.getString(res.getColumnIndex(COLUMN_PERCENT_CHANGE_DAY)));
            Double percentChangeWeek = Double.parseDouble(res.getString(res.getColumnIndex(COLUMN_PERCENT_CHANGE_WEEK)));
            String imageUrl = res.getString(res.getColumnIndex(COLUMN_IMAGE_URL));
            array_list.add(new Coin(symbol,name,id,price,percentChangeHour,percentChangeDay,percentChangeWeek));
            res.moveToNext();
        }
        return array_list;
    }

    public void deleteDataBase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ COIN_TABLE);
    }

    public int getRowCount(){
            SQLiteDatabase db = this.getReadableDatabase();
            long count = DatabaseUtils.queryNumEntries(db, COIN_TABLE);
            db.close();
            return (int) count;
    }
}
