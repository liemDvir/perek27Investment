package com.example.perek27;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StockModel extends Application {

    private StockControllerService mStockControllerService;

    private UserData mUserData = null;
    boolean isStockControllerBind = false;
    private static StockModel mStockModel;
    private List<Observer> mObservers = Collections.synchronizedList(new ArrayList<Observer>());

    private List<Stock> mStocksInvestedList =  Collections.synchronizedList(new ArrayList<Stock>());
    private final Observer mModelObserver = new ModelObserver();
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

        synchronized (mObservers){
            if (!mObservers.contains(observer)) {
                mObservers.add(observer);
            }
        }
    }

    public void unregister(final Observer observer) {
        //mStockControllerService.unregister(observer);
        synchronized (mObservers){
            if (!mObservers.contains(observer)) {
                mObservers.remove(observer);
            }
        }
    }

    public void SignInWithEmailAndPassword(String email, String password){
        if(mStockControllerService == null)
            return;

        mStockControllerService.SignInWithEmailAndPassword(email, password);
    }

    public void SetTransaction(Transaction transaction){
        if(mStockControllerService == null)
            return;

        mStockControllerService.SetTransaction(transaction);
    }

    public void GetAllStocksInvested()
    {
        synchronized (mStocksInvestedList){
            if(!mStocksInvestedList.isEmpty()){
                mModelObserver.GetAllStocksInvested(mStocksInvestedList);
            }
        }

        if(mStockControllerService == null)
            return;

        mStockControllerService.GetAllStocksInvested();
    }

    public void GetTransactionHistory(){
        if(mStockControllerService == null)
            return;

        mStockControllerService.GetTransactionHistory();
    }

    public void GetAllStocksInMarket(){
        //TODO - need to implement it
        //ArrayList<Stock> stocksStack =  new ArrayList<>();
        //Date currentDay = new Date(System.currentTimeMillis());
        /*Stock s1 = new Stock("AAPL",1);
        Stock s2 = new Stock("NETFLIX",3);
        Stock s3 = new Stock("META",2);
        Stock s4 = new Stock("GME",4);

        stocksStack.add(s1);
        stocksStack.add(s2);
        stocksStack.add(s3);
        stocksStack.add(s4);
        return stocksStack;*/

        if(mStockControllerService == null)
            return;

        mStockControllerService.GetAllStocksInMarket();
    }
    public float GetSumStocksInvested(){
        ///TODO - need to implement it
        return 1200;
    }
    public void GetAllUserData()
    {
        if(mStockControllerService == null)
            return;

        if(mUserData!= null){
            mModelObserver.GetAllUserData(mUserData);
            return;
        }

        //SetTransaction(new Transaction(120,"NETFLIX",2, Calendar.getInstance().getTime()));
        mStockControllerService.GetAllUserData();
    }
    public float GetSumOfAllMoney()
    {
       /* float sum = GetSumStocksInvested() + GetAllCash();
        return sum;*/
        return 0;
    }

    public void GetAllCash()
    {
        if(mStockControllerService == null)
            return ;

        mStockControllerService.getALLCash();
    }
    public void BuyStock(Stock stock, float amountMoneyToBuy)
    {
        if(mStockControllerService == null)
            return ;

        //mStockControllerService.SetTransaction();

    }

    public void SellStock(Stock stock)
    {


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
        /*ArrayList<Stock> stockArrayList = GetAllStocksInMarket();
        Stock curentStock;
        int index = 0;
        while(!stockArrayList.isEmpty())
        {
            curentStock = stockArrayList.get(index);
            if (curentStock.getStockName().equals(stockName))
            {
                return curentStock;
            }
            index++;
        }*/
        return new Stock("AAPL", 0);

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StockControllerService.InteractionService interactionService = (StockControllerService.InteractionService) service;
            mStockControllerService = interactionService.getService();
            isStockControllerBind = true;

            if(mStockControllerService == null){
                Log.e("StockModel", "mStockControllerService == null");
                return;
            }
            mStockControllerService.register(mModelObserver);
           /* for (final Observer observer : mObservers) {
                register(observer);
            }*/
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isStockControllerBind = false;
        }
    };

    public class ModelObserver implements Observer{

        @Override
        public void SignInWithEmailAndPasswordCompleate(@NonNull Task<AuthResult> task) {
            synchronized (mObservers){
                for (final Observer observer : mObservers) {
                    observer.SignInWithEmailAndPasswordCompleate(task);
                }
            }
        }

        @Override
        public void GetAllStocksInMarket(List<Stock> stocksList) {
            synchronized (mObservers){
                for (final Observer observer : mObservers) {
                    observer.GetAllStocksInMarket(stocksList);
                }
            }
        }

        @Override
        public void GetTransactionHistory(ArrayList<Transaction> transactionsList) {

        }

        @Override
        public void GetAllStocksInvested(List<Stock> stockInvested) {
            synchronized (mStocksInvestedList){
                if(!mStocksInvestedList.isEmpty()){
                    mStocksInvestedList.clear();
                }
                mStocksInvestedList.addAll(stockInvested);
            }
            synchronized (mObservers){
                for (final Observer observer : mObservers) {
                    observer.GetAllStocksInvested(stockInvested);
                }
            }
        }

        @Override
        public void getALLCash(float cash) {
            synchronized (mObservers){
                for (final Observer observer : mObservers) {
                    observer.getALLCash(cash);
                }
            }
        }

        @Override
        public void GetAllUserData(UserData userDate) {
            if(mUserData == null){
                mUserData = userDate;
            }
            synchronized (mObservers){
                for (final Observer observer : mObservers) {
                    observer.GetAllUserData(userDate);
                }
            }

        }

    }
}
