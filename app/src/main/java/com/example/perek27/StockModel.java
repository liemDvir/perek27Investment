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

    private final Object lockObject = new Object();

    private List<StockInfo> mStocksInvestedList =  Collections.synchronizedList(new ArrayList<StockInfo>());
    private final Observer mModelObserver = new ModelObserver();

    private Boolean bUpdateCashSuccess = true;
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

        synchronized (lockObject){
            if (!mObservers.contains(observer)) {
                mObservers.add(observer);
            }
        }
    }

    public void unregister(final Observer observer) {
        //mStockControllerService.unregister(observer);
        synchronized (lockObject){
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
        synchronized (lockObject){
            if(!mStocksInvestedList.isEmpty()){
                mModelObserver.GetAllStocksInvested(mStocksInvestedList);
            }
        }

        if(mStockControllerService == null)
            return;

        mStockControllerService.GetAllStocksInvested();
    }

    public void GetSummaryOfUser(){
        GetAllStocksInvested();
        GetAllCash();
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

        if(mUserData == null){
            mStockControllerService.GetAllCash();
            return;
        }

        mModelObserver.GetAllCash(mUserData.getCash());
    }
    public void BuyStock(String stockSymbol, float amountOfMoneyToBuy)
    {
        if(mStockControllerService == null)
            return ;

        if(mUserData.getCash() < amountOfMoneyToBuy){
            Log.e("BuyStock()", "Not enough money");
            mModelObserver.OnBuyStockCompleted(false, "Not enough money");
            return;
        }

        //Update stock value
        GetStockInfo(stockSymbol);
        synchronized (lockObject){
            try {
                lockObject.wait(10000);
            } catch (InterruptedException e) {
                mModelObserver.OnBuyStockCompleted(false, "Timeout");
                return;
            }
        }

        //Update cash on datebase
        mStockControllerService.UpdateCash(mUserData.getCash() - amountOfMoneyToBuy);
        synchronized (lockObject){
            try {
                lockObject.wait(10000);
            } catch (InterruptedException e) {
                mModelObserver.OnBuyStockCompleted(false, "update");
                return;
            }
        }

        //Update transaction history
        //TODO

        //Update cash
        //TODO

        //Update stocks invested
        //TODO

    }

    public void SellStock(String stockSymbol, float amountOfMoneyToSell)
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

    public ArrayList<Stock> GetStocksByName(String stockName){
        ArrayList<Stock> stockList = new ArrayList<>();
        if(mStockControllerService == null)
            return null;

        mStockControllerService.GetStocksByName(stockName);

        return stockList;
    }

    public void GetStockInfo(String stockSymbol){
        if(mStockControllerService == null)
            return;

        mStockControllerService.GetStockInfo(stockSymbol);
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
            synchronized (lockObject){
                for (final Observer observer : mObservers) {
                    observer.SignInWithEmailAndPasswordCompleate(task);
                }
            }
        }

        @Override
        public void GetAllStocksInMarket(List<StockInfo> stocksList) {
            synchronized (lockObject){
                for (final Observer observer : mObservers) {
                    observer.GetAllStocksInMarket(stocksList);
                }
            }
        }

        @Override
        public void GetAllCash(float cash) {
            synchronized (lockObject){
                for (final Observer observer : mObservers) {
                    observer.GetAllCash(cash);
                }
            }
        }

        @Override
        public void GetTransactionHistory(ArrayList<Transaction> transactionsList) {

        }

        @Override
        public void GetAllStocksInvested(List<StockInfo> stockInvested) {
            synchronized (mStocksInvestedList){
                if(!mStocksInvestedList.isEmpty()){
                    mStocksInvestedList.clear();
                }
                mStocksInvestedList.addAll(stockInvested);
            }
            synchronized (lockObject){
                for (final Observer observer : mObservers) {
                    observer.GetAllStocksInvested(stockInvested);
                }
            }

            //GetStocksValue
            for(StockInfo stockInf : mStocksInvestedList){
                mStockControllerService.GetStockInfo(stockInf.getStockSymbol());
            }
        }

        @Override
        public void GetAllUserData(UserData userDate) {
            if(mUserData == null){
                mUserData = userDate;
            }

            synchronized (lockObject){
                for (final Observer observer : mObservers) {
                    observer.GetAllUserData(userDate);
                }
            }

        }
        @Override
        public void OnStockInfoUpdate(StockInfo stockInf) {
            synchronized (mStocksInvestedList){
                for(StockInfo stockInfFromList : mStocksInvestedList){
                    if(stockInfFromList.getStockSymbol().equals(stockInf.getStockSymbol())){
                        stockInfFromList.setPrice(stockInf.getPrice());
                        stockInfFromList.setChange_percent(stockInf.getChange_percent());
                    }
                }
            }

            synchronized (lockObject){
                for (final Observer observer : mObservers) {
                    observer.OnStockInfoUpdate(stockInf);
                }

                lockObject.notifyAll();
            }
        }

        @Override
        public void OnBuyStockCompleted(Boolean success, String reason) {
            synchronized (lockObject){
                for (final Observer observer : mObservers) {
                    observer.OnBuyStockCompleted(success,reason);
                }
            }
        }

        @Override
        public void OnUpdateCashCompleted(Boolean success) {

        }
    }
}
