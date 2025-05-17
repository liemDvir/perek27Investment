package com.example.perek27;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class StockDatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "Stocks.db";

    private SQLiteDatabase stockDB = null;
    private static final int DATABASE_VERSION = 1;

    // Table Name and Columns
    public static final String TABLE_NAME = "StocksInMarket";
    private static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SYMBOL = "symbol";
    // Create Table SQL query
    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_SYMBOL + " TEXT" +
                    ");";

    // SQL query to select values starting with 'df'
    public static final String GET_STOCK_BY_NAME_SQL =
            "SELECT * FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_NAME + " LIKE '";//'df%'";

    public static final String GET_ALL_STOCKS_SQL =
            "SELECT * FROM " + TABLE_NAME;
    public StockDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating the table
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop the old table and recreate it if database version changes
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Stock> GetStocksByName(String stockName){

        if(stockDB == null) {
            stockDB = this.getWritableDatabase();
        }

        Cursor cursor = stockDB.rawQuery(StockDatabaseHelper.GET_STOCK_BY_NAME_SQL + stockName + "%'", null);

        ArrayList<Stock> stocksList =  new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") String symbol = cursor.getString(cursor.getColumnIndex(COLUMN_SYMBOL));

                stocksList.add(new Stock(name,symbol,0));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return stocksList;
    }

    public ArrayList<Stock> GetAllStocksInMarket(){

        if(stockDB == null) {
            stockDB = this.getWritableDatabase();
        }

        Cursor cursor = stockDB.rawQuery(StockDatabaseHelper.GET_ALL_STOCKS_SQL, null);
        ArrayList<Stock> stocksInMarket =  new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") String symbol = cursor.getString(cursor.getColumnIndex(COLUMN_SYMBOL));

                stocksInMarket.add(new Stock(name,symbol,0));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return stocksInMarket;
    }

    public void InsertAllStockInMarket(ArrayList<Stock> stocksInMarket){

        if(stockDB == null) {
            stockDB = this.getWritableDatabase();
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Code to run on a background thread
                stockDB.beginTransaction();
                try {
                    for(Stock stock : stocksInMarket){
                        ContentValues values = new ContentValues();
                        values.put(StockDatabaseHelper.COLUMN_NAME, stock.getStockName());
                        values.put(StockDatabaseHelper.COLUMN_SYMBOL, stock.getStockSymbol());
                        long newRowId = stockDB.insert(StockDatabaseHelper.TABLE_NAME, null, values);
                    }
                    stockDB.setTransactionSuccessful();
                } catch (Exception e) {

                } finally {
                    stockDB.endTransaction(); // End the transaction
                    //stockDB.close(); // Close the database
                }
            }
        });

        thread.start();  // Start the thread
    }

}
