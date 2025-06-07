package com.example.perek27;

import android.content.Context;

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

    public ArrayList<StockInfo> GetAllStocksInMarket(){

        //Cursor cursor = stockDB.rawQuery(StockDatabaseHelper.GET_ALL_STOCKS_SQL, null);
        return dbHelper.GetAllStocksInMarket();
    }

    public void DeleteAllDatabase(){
        //stockDB.delete(StockDatabaseHelper.TABLE_NAME,null,null);
    }

    public ArrayList<StockInfo> GetStocksByName(String stockName){
        return dbHelper.GetStocksByName(stockName);
    }


    public StockInfo GetStocksBySymbol(String stockSymbol){
        return dbHelper.GetStocksBySymbol(stockSymbol);
    }

    public void UpdateStockInfo(StockInfo stock){
        dbHelper.UpdateStockInfo(stock);
    }

    public void InsertAllStockInMarket(ArrayList<StockInfo> stocksInMarket){

       dbHelper.InsertAllStockInMarket(stocksInMarket);
    }
}
