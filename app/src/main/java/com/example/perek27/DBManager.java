package com.example.perek27;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {

    private StockDatabaseHelper dbHelper = null;
    private SQLiteDatabase stockDB = null;
    public DBManager(Context ctx) {

        if(dbHelper == null){
            dbHelper = new StockDatabaseHelper(ctx);
        }

        if(stockDB == null){
            stockDB = dbHelper.getWritableDatabase();
        }
    }

    public void InsertAllStockInMarket(ArrayList<Stock> stocksInMarket){
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
            stockDB.close(); // Close the database
        }
    }
}
