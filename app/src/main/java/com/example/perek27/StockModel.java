package com.example.perek27;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockModel extends Application {

    private StockControllerService mStockControllerService;
    boolean isStockControllerBind = false;
    private static StockModel mStockModel;

    private final List<Observer> mObservers = new ArrayList<Observer>();
    public static StockModel GetInstance(){
        if(mStockModel == null){
            mStockModel = new StockModel();
        }
        return mStockModel;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mStockModel = this;
    }

    public void Init(){
        if(isStockControllerBind){
            return;
        }
        Intent serviceIntent = new Intent(this.getApplicationContext(),StockControllerService.class);
        this.getApplicationContext().bindService(serviceIntent,serviceConnection,BIND_AUTO_CREATE);
    }

    public void register(final Observer observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }

        if(mStockControllerService == null){
            Log.e("StockModel", "mStockControllerService == null");
            return;
        }
        mStockControllerService.register(observer);
    }

    public void unregister(final Observer observer) {
        mStockControllerService.unregister(observer);
    }

    public void SignInWithEmailAndPassword(String email, String password){
        if(mStockControllerService == null)
            return;

        mStockControllerService.SignInWithEmailAndPassword(email, password);
    }

    public void SetTransaction(TransactionHistory tranHistory){
        if(mStockControllerService == null)
            return;

        mStockControllerService.SetTransaction(tranHistory);
    }

    public ArrayList<Stock> GetAllStocksInvested()
    {
        if(mStockControllerService == null)
            return null;

        ArrayList<Stock> stocksList = mStockControllerService.GetAllStocksInvested();
        //TODO - need to implement it
        ArrayList<Stock> stocksStack =  new ArrayList<>();
        Date currentDay = new Date(System.currentTimeMillis());
        Stock s1 = new Stock("AAPL",1/*,500*/,currentDay);
        Stock s3 = new Stock("META",2,/*130,*/currentDay);

        stocksStack.add(s1);
        stocksStack.add(s3);
        return stocksStack;
    }

    public ArrayList<Stock> GetAllStocksInMarket(){
        //TODO - need to implement it
        ArrayList<Stock> stocksStack =  new ArrayList<>();
        Date currentDay = new Date(System.currentTimeMillis());
        Stock s1 = new Stock("AAPL",1/*,500*/,currentDay);
        Stock s2 = new Stock("NETFLIX",0 /*,200*/,currentDay);
        Stock s3 = new Stock("META",2/*,130*/,currentDay);
        Stock s4 = new Stock("GME",0/*,50*/,currentDay);

        stocksStack.add(s1);
        stocksStack.add(s2);
        stocksStack.add(s3);
        stocksStack.add(s4);
        return stocksStack;
    }
    public float GetSumStocksInvested(){
        ///TODO - need to implement it
        return 1200;
    }
    public float GetSumOfAllMoney()
    {
        float sum = GetSumStocksInvested() + GetAllCash();
        return sum;
    }

    public float GetAllCash()
    {
        ///TODO - need to implement it
        return 5000;
    }
    public boolean BuyStock(Stock stock, float amountMoneyToBuy)
    {
        ///TODO- need to implement it
        return true;
    }
    public boolean SellStock(Stock stock, float amountMoneyToSell)
    {
        ///TODO - need to implement it
        return true;
    }
    public LineGraphSeries<DataPoint> GetCurrentStockGraphViewSeries(Stock stock)
    {
        ///TODO- need to implement it
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
                new DataPoint(5, 7),
                new DataPoint(6, 10),
                new DataPoint(7, 3),
                new DataPoint(8, 12),
                new DataPoint(9, 14),
                new DataPoint(10, 16),
                new DataPoint(11, 15),
                new DataPoint(12, 13),
                new DataPoint(13, 15),
                new DataPoint(14, 16),
        });



        return series;
    }
    public Stock getStockByName(String stockName)
    {
        ArrayList<Stock> stockArrayList = GetAllStocksInMarket();
        Stock curentStock;
        int index = 0;
        while(!stockArrayList.isEmpty())
        {
            curentStock = stockArrayList.get(index);
            if (curentStock.getTypeOfStock().equals(stockName))
            {
                return curentStock;
            }
            index++;
        }
        return null;

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StockControllerService.InteractionService interactionService = (StockControllerService.InteractionService) service;
            mStockControllerService = interactionService.getService();
            isStockControllerBind = true;

            for (final Observer observer : mObservers) {
                register(observer);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isStockControllerBind = false;
        }
    };
}
