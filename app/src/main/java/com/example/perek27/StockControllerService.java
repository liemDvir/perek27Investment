package com.example.perek27;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.Callback;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StockControllerService extends Service {

    private final String STOCK_CONTROLLER_SERVICE_NAME = "StockControllerService";
    private final String FIREBASE_URL = "https://insvestment-85820-default-rtdb.firebaseio.com/";

    private final String STOCK_HISTORY_REFERENCE_NAME = "push";

    private final String STOCK_TABLE_NAME = "stock";
    private static FirebaseDatabase mFirebaseDatabase;
    private static FirebaseAuth mFirebaseAuth;

    private static String mUID;

    private final List<Observer> mObservers = Collections.synchronizedList(new ArrayList<Observer>());
    private final IBinder binder = new InteractionService();

    private static final String ALPHA_VANTAGE_KEY = "QVKNZ7YQGN1U0PTZ";
    private static final String ALPHA_VANTAGE_BASE_URL = "https://www.alphavantage.co/";
    private static Retrofit mRetrofit;
    public StockControllerService() {
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        init();
        return binder;
    }

    public static Retrofit getApiClient() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(ALPHA_VANTAGE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public static void fetchStockData(String symbol, Callback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                String url = ALPHA_VANTAGE_BASE_URL + "function=TIME_SERIES_INTRADAY&symbol=" + symbol +
                        "&interval=5min&apikey=" + ALPHA_VANTAGE_KEY;

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful() && response.body() != null) {
                        return response.body().string();
                    }
                } catch (IOException e) {
                    Log.e("AlphaVantageAPI", "Error fetching data", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                /*if (result != null) {
                    callback.onResponse(result);
                } else {
                    callback.onError("Failed to fetch data");
                }*/
                Log.d("onPostExecute", result);

            }
        }.execute();
    }

    public class InteractionService extends Binder {

        StockControllerService getService() {
            return StockControllerService.this;
        }
    }

    public void GetAllStocksInvested(){


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.getReference(STOCK_TABLE_NAME).child(mUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("onDataChange", snapshot.toString());
                //transactionHistoryArr = new ArrayList<>();
                ArrayList<Stock> stocksStack =  new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    float amountOfStock = dataSnapshot.child("amountOfStock").getValue(float.class);
                    String typeOfStock = dataSnapshot.child("typeOfStock").getValue(String.class);
                    Date d = dataSnapshot.child("lastUpdate").getValue(Date.class);
                    Stock s = new Stock(typeOfStock,amountOfStock,d);
                    stocksStack.add(s);
                }

                synchronized (mObservers){
                    for (final Observer observer : mObservers) {
                        observer.GetAllStocksInvested(stocksStack);
                    }
                }
                Log.d("onDataChange", "ended");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static class RetrofitClient {
        private static Retrofit retrofit = null;

        public static Retrofit getClient() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl("https://www.alphavantage.co/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }

    public void register(final Observer observer) {
        synchronized (mObservers){
            if (!mObservers.contains(observer)) {
                mObservers.add(observer);
            }
        }


        /*AlphaVantageApi api = RetrofitClient.getClient().create(AlphaVantageApi.class);
        Call<StockResponse> call = api.getStockData("GLOBAL_QUOTE", "AAPL", ALPHA_VANTAGE_KEY);

        call.enqueue(new retrofit2.Callback<StockResponse>() {
            @Override
            public void onResponse(Call<StockResponse> call, retrofit2.Response<StockResponse> response) {
                Log.d("onResponse","");
            }

            @Override
            public void onFailure(Call<StockResponse> call, Throwable throwable) {
                Log.d("onFailure","");
            }

            
        });*/
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
                mUID = mFirebaseAuth.getUid();
                /*mFirebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = mFirebaseDatabase.getReference("stock");
                Stock s1 = new Stock("META", 111, Calendar.getInstance().getTime());
                Stock s2 = new Stock("AAPL", 222, Calendar.getInstance().getTime());
                ArrayList<Stock> stocksStack =  new ArrayList<>();
                stocksStack.add(s1);
                stocksStack.add(s2);
                myRef.child(mUID).setValue(stocksStack);*/
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
    public float getALLCash()
    {
        Log.d(STOCK_CONTROLLER_SERVICE_NAME,"start getAllCash");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        final float[] currentCash = {0};
        mFirebaseDatabase.getReference("user").child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData currentUser = snapshot.getValue(UserData.class);
                if (currentUser!= null)
                {
                    currentCash[0] = currentUser.getCash();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return currentCash[0];


    }

    public void SetTransaction(TransactionHistory tranHistory){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(STOCK_HISTORY_REFERENCE_NAME).push();
        ref.setValue(tranHistory);
    }

    public void GetSockList(){

    }
}