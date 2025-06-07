package com.example.perek27;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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

        Log.d("StockModel", "BuyStock - stockSymbol:" + stockSymbol);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(mUserData == null){
                    mStockControllerService.GetAllUserData();
                    synchronized (lockObject){
                        try {
                            if(mUserData == null) {
                                lockObject.wait(10000);
                            }
                        } catch (InterruptedException e) {
                            mModelObserver.OnBuyStockCompleted(false, "Timeout");
                            return;
                        }
                    }
                }

                if(mUserData.getCash() < amountOfMoneyToBuy){
                    Log.e("BuyStock()", "Not enough money");
                    mModelObserver.OnBuyStockCompleted(false, "Not enough money");
                    return;
                }

                //Update cash on datebase
                mStockControllerService.UpdateCash(mUserData.getCash() - amountOfMoneyToBuy);
                synchronized (lockObject){
                    try {
                        lockObject.wait(10000);
                    } catch (InterruptedException e) {
                        mModelObserver.OnBuyStockCompleted(false, "Connection timeout");
                        return;
                    }
                }

                if(!bUpdateCashSuccess){
                    synchronized (lockObject){
                        mModelObserver.OnBuyStockCompleted(false, "Update failure");
                    }
                    return;
                }

                mUserData.setCash(mUserData.getCash() - amountOfMoneyToBuy);
                //Update cash on GUI
                mModelObserver.GetAllCash(mUserData.getCash());

                float amoutOfStockToBuy = 0;
                StockInfo newStockInfoList = null;
                Boolean bIsNewStock = false;

                //Update stocks invested
                synchronized (mStocksInvestedList) {
                    for (StockInfo stockInfFromList : mStocksInvestedList) {
                        if (!stockInfFromList.getStockSymbol().equals(stockSymbol)) {
                            continue;
                        }
                        newStockInfoList = stockInfFromList;
                        break;
                        /*float stockPrice = stockInfFromList.getPrice();
                        if(stockPrice == 0){
                            Log.e("StockModel", "BuyStock: stockPrice == 0");
                            break;
                        }
                        amoutOfStockToBuy = amountOfMoneyToBuy/stockPrice;
                        float currentAmountOfStocks = stockInfFromList.getAmountOfStock();
                        stockInfFromList.setAmountOfStock(amoutOfStockToBuy + currentAmountOfStocks);
                        mStockControllerService.UpdateAmountOfStockInvested(stockInfFromList);
                        break;*/
                    }
                }
                if((newStockInfoList == null) || (newStockInfoList.getPrice() == (float)0)){
                    //A new invested stock
                    bIsNewStock = true;
                    newStockInfoList = new StockInfo("",stockSymbol,0,"0");
                    synchronized (mStocksInvestedList){
                        mStocksInvestedList.add(newStockInfoList);
                    }
                    mStockControllerService.GetStockInfo(stockSymbol);
                    synchronized (lockObject){
                        try {
                                lockObject.wait(10000);
                        } catch (InterruptedException e) {
                            mModelObserver.OnBuyStockCompleted(false, "Timeout");
                            return;
                        }
                    }
                }

                if(newStockInfoList.getPrice() == 0){
                    Log.e("StockModel", "BuyStock: stockPrice == 0");
                    mModelObserver.OnBuyStockCompleted(false, "Timeout");
                    return;
                }
                amoutOfStockToBuy = amountOfMoneyToBuy/newStockInfoList.getPrice();

                if(!bIsNewStock) {
                    amoutOfStockToBuy = newStockInfoList.getAmountOfStock() + amoutOfStockToBuy;
                }
                newStockInfoList.setAmountOfStock(amoutOfStockToBuy);
                if(bIsNewStock){
                    mStockControllerService.AddNewStockInvested(newStockInfoList);
                }
                else{
                    mStockControllerService.UpdateAmountOfStockInvested(newStockInfoList);
                }


                synchronized (lockObject){
                    for (final Observer observer : mObservers) {
                        observer.GetAllStocksInvested(mStocksInvestedList);
                    }
                }

                //Update transaction history
                SetTransaction(new Transaction((int)amountOfMoneyToBuy,stockSymbol,((-1)*amoutOfStockToBuy), Date.from(Instant.now())));

                synchronized (lockObject){
                    mModelObserver.OnBuyStockCompleted(true, "Action completed");
                }

            }
        });

        thread.start();
    }

    public void SellStock(String stockSymbol, float amountOfMoneyToSell)
    {
        if(mStockControllerService == null)
            return ;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(mUserData == null){
                    mStockControllerService.GetAllUserData();
                    synchronized (lockObject){
                        try {
                            if(mUserData == null) {
                                lockObject.wait(10000);
                            }
                        } catch (InterruptedException e) {
                            mModelObserver.OnSellStockCompleted(false, "Timeout");
                            return;
                        }
                    }
                }

                StockInfo stockToSell = null;
                synchronized (mStocksInvestedList){
                    for(StockInfo stockInfFromList : mStocksInvestedList){
                        if(!stockInfFromList.getStockSymbol().equals(stockSymbol)){
                            continue;
                        }
                        stockToSell = stockInfFromList;
                        float stockPrice = stockInfFromList.getPrice();
                        float amountOfStock = stockInfFromList.getAmountOfStock();
                        if(amountOfMoneyToSell > (amountOfStock * stockPrice)){
                            mModelObserver.OnSellStockCompleted(false, "Not enough stocks to sell");
                            return;
                        }
                    }
                }


                //Update cash on datebase
                mStockControllerService.UpdateCash(mUserData.getCash() + amountOfMoneyToSell);
                synchronized (lockObject){
                    try {
                        lockObject.wait(10000);
                    } catch (InterruptedException e) {
                        mModelObserver.OnSellStockCompleted(false, "Connection timeout");
                        return;
                    }
                }

                if(!bUpdateCashSuccess){
                    synchronized (lockObject){
                        mModelObserver.OnSellStockCompleted(false, "Update failure");
                    }
                    return;
                }

                mUserData.setCash(mUserData.getCash() + amountOfMoneyToSell);
                //Update cash on GUI
                mModelObserver.GetAllCash(mUserData.getCash());


                float stockPrice = stockToSell.getPrice();
                float amoutOfStockToSell = amountOfMoneyToSell/stockPrice;
                float currentAmountOfStocks = stockToSell.getAmountOfStock();

                //Update stocks invested
                synchronized (mStocksInvestedList){
                    stockToSell.setAmountOfStock(currentAmountOfStocks - amoutOfStockToSell);
                    mStockControllerService.UpdateAmountOfStockInvested(stockToSell);
                }

                synchronized (lockObject){
                    for (final Observer observer : mObservers) {
                        observer.GetAllStocksInvested(mStocksInvestedList);
                    }
                }

                //Update transaction
                //amoutOfStockToSell = (amoutOfStockToSell * (-1));
                SetTransaction(new Transaction((int)amoutOfStockToSell,stockToSell.getStockSymbol(),amountOfMoneyToSell, Date.from(Instant.now())));

                synchronized (lockObject){
                    mModelObserver.OnSellStockCompleted(true, "Action completed");
                }

            }
        });

        thread.start();
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

        Log.d("StockModel", "GetStockInfo: " + stockSymbol);
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

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 56);
            calendar.set(Calendar.SECOND, 0);

            setExactAlarm(getApplicationContext(), calendar);
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
            synchronized (lockObject){
                for (final Observer observer : mObservers) {
                    observer.GetTransactionHistory(transactionsList);
                }
            }
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
                lockObject.notifyAll();
            }

        }
        @Override
        public void OnStockInfoUpdate(StockInfo stockInf) {
            if(stockInf == null){
                Log.e("StockModel", "OnStockInfoUpdate complete with error");
                return;
            }
            Log.d("StockModel", "OnStockInfoUpdate complete. price:" + String.valueOf(stockInf.getPrice()));
            synchronized (mStocksInvestedList){
                for(StockInfo stockInfFromList : mStocksInvestedList){
                    if(stockInfFromList.getStockSymbol().equals(stockInf.getStockSymbol())){
                        stockInfFromList.setPrice(stockInf.getPrice());
                        stockInfFromList.setChange_percent(stockInf.getChange_percent());
                        if((stockInf.getStockName() != null) && (!stockInf.getStockName().isEmpty())){
                            stockInfFromList.setStockName(stockInf.getStockName());
                        }
                    }
                }
            }

            synchronized (lockObject){
                Log.d("StockModel", "OnStockInfoUpdate() - notify observers");
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
        public void OnSellStockCompleted(Boolean success, String reason) {
            synchronized (lockObject){
                for (final Observer observer : mObservers) {
                    observer.OnSellStockCompleted(success,reason);
                }
            }
        }

        @Override
        public void OnUpdateCashCompleted(Boolean success) {
            bUpdateCashSuccess = success;
            synchronized (lockObject){
                lockObject.notifyAll();
            }
        }
    }

    public static class AlarmReceiver  extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("OnReceive", "time completed");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "alarm_channel";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Alarm Notification", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Alarm")
                    .setContentText("Your scheduled alarm went off!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            notificationManager.notify(1, builder.build());
        }
    }

    public void setExactAlarm(Context context,Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
