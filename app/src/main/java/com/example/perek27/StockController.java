package com.example.perek27;

import com.google.firebase.Firebase;
import com.google.firebase.database.FirebaseDatabase;

public class StockController {
    private static StockController mStockController;
    private static FirebaseDatabase mFirebaseDatabase;

    private final String FIREBASE_URL = "https://insvestment-85820-default-rtdb.firebaseio.com/";

    public static StockController GetInstance(){
        if(mStockController == null){
            mStockController = new StockController();
        }
        return mStockController;
    }

    public Boolean Init(){
        if(mFirebaseDatabase == null){
            mFirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
        }
        return true;
    }


}
