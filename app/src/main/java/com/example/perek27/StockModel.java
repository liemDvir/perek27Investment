package com.example.perek27;

import static android.content.Context.BIND_AUTO_CREATE;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
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
