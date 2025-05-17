package com.example.perek27;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {

    private StockDatabaseHelper dbHelper = null;
    //private SQLiteDatabase stockDB = null;
    public DBManager(Context ctx) {

        if(dbHelper == null){
            dbHelper = new StockDatabaseHelper(ctx);
        }

        /*if(stockDB == null){
            stockDB = dbHelper.getWritableDatabase();
        }*/
    }

    public ArrayList<Stock> GetAllStocksInMarket(){

        //Cursor cursor = stockDB.rawQuery(StockDatabaseHelper.GET_ALL_STOCKS_SQL, null);
        return dbHelper.GetAllStocksInMarket();
    }

    public void DeleteAllDatabase(){
        //stockDB.delete(StockDatabaseHelper.TABLE_NAME,null,null);
    }

    public ArrayList<Stock> GetStocksByName(String stockName){
        return dbHelper.GetStocksByName(stockName);
    }

    public void InsertAllStockInMarket(ArrayList<Stock> stocksInMarket){

       dbHelper.InsertAllStockInMarket(stocksInMarket);
    }
}
