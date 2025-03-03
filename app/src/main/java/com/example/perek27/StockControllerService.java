package com.example.perek27;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StockControllerService extends Service {

    private final String STOCK_CONTROLLER_SERVICE_NAME = "StockControllerService";
    private final String FIREBASE_URL = "https://insvestment-85820-default-rtdb.firebaseio.com/";

    private final String STOCK_HISTORY_REFERENCE_NAME = "push";
    private static FirebaseDatabase mFirebaseDatabase;
    private static FirebaseAuth mFirebaseAuth;

    private final List<Observer> mObservers = Collections.synchronizedList(new ArrayList<Observer>());
    private final IBinder binder = new InteractionService();

    private final String ALPHA_VANTAGE_KEY = "QVKNZ7YQGN1U0PTZ";
    public StockControllerService() {
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        init();
        return binder;
    }

    public class InteractionService extends Binder {

        StockControllerService getService() {
            return StockControllerService.this;
        }
    }

    public void register(final Observer observer) {
        synchronized (mObservers){
            if (!mObservers.contains(observer)) {
                mObservers.add(observer);
            }
        }

    }

    public void unregister(final Observer observer) {
        synchronized (mObservers){
            mObservers.remove(observer);
        }

    }

    private void init(){

        if(mFirebaseDatabase == null){
            mFirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
        }

        if(mFirebaseAuth == null){
            mFirebaseAuth = FirebaseAuth.getInstance();
        }
    }

    public void SignInWithEmailAndPassword(String email, String password){
        Log.d(STOCK_CONTROLLER_SERVICE_NAME, "Start SignInWithEmailAndPassword - email: " + email + " password: " + password );
        mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(STOCK_CONTROLLER_SERVICE_NAME, "SignInWithEmailAndPassword - onComplete");
                /*if(task.isSuccessful()){
                    Log.d(STOCK_CONTROLLER_SERVICE_NAME, "SignInWithEmailAndPassword - successfully end");
                }*/
                //Notify observers
                synchronized (mObservers){
                    for (final Observer observer : mObservers) {
                        observer.SignInWithEmailAndPasswordCompleate(task);
                    }
                }
            }

        });

    }
    /// need to continue the action
    /*public float getALLCash()
    {
        Log.d(STOCK_CONTROLLER_SERVICE_NAME,"start getAllCash");

    }*/

    public void SetTransaction(TransactionHistory tranHistory){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(STOCK_HISTORY_REFERENCE_NAME).push();
        ref.setValue(tranHistory);
    }

    public void GetSockList(){

    }
}