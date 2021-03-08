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
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRICE + " DOUBLE, " + COLUMN_PERCENT_CHANGE_HOUR + " DOBLE, " + COLUMN_PERCENT_CHANGE_DAY + " DOUBLE, " + COLUMN_PERCENT_CHANGE_WEEK + " DOUBLE, " + COLUMN_NAME + " TEXT, " + COLUMN_SYMBOL + " TEXT, " + COLUMN_IMAGE_URL + " TEXT )";


        db.execSQL(createTableStatement);
    }

    //version number changed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean deleteOne(){
        //mikhad ?
        return true;
    }

    public boolean addOne(Coin coin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRICE, coin.getPrice());
        cv.put(COLUMN_PERCENT_CHANGE_HOUR, coin.getPercentChangeHour());
        cv.put(COLUMN_PERCENT_CHANGE_DAY, coin.getPercentChangeDay());
        cv.put(COLUMN_PERCENT_CHANGE_WEEK, coin.getPercentChangeWeek());
        cv.put(COLUMN_NAME, coin.getName());
        cv.put(COLUMN_SYMBOL, coin.getSymbol());
        cv.put(COLUMN_IMAGE_URL, coin.getImageUrl());

        long insert = db.insert(COIN_TABLE, null, cv);
        return insert != 1;
    }

    public List<Coin> getAll(){
        List<Coin> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + COIN_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {

            do{
                int coinID = cursor.getInt(0);
                String coinName = cursor.getString(5);
            }while (cursor.moveToNext());

        }else {
            return null;
        }
        cursor.close();
        return returnList;
    }
}