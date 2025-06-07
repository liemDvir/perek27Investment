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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StockControllerService extends Service {

    private final String STOCK_CONTROLLER_SERVICE_NAME = "StockControllerService";
    private final String FIREBASE_URL = "https://insvestment-85820-default-rtdb.firebaseio.com/";

    private final String HISTORY_TABLE_NAME = "history";
    private final String STOCK_TABLE_NAME = "stock";
    private final String USER_DATA_TABLE_ROW = "user";
    private static FirebaseDatabase mFirebaseDatabase;
    private static FirebaseAuth mFirebaseAuth;

    private static String mUID;

    private UserData currentUserData = null;

    private final List<Observer> mObservers = Collections.synchronizedList(new ArrayList<Observer>());
    private final IBinder binder = new InteractionService();

    private DBManager stockDBManager = null;
    
    private static final String ALPHA_VANTAGE_API_KEY = "2PJN7DA1JVZIZ7G9";

    //private static final String FINNHUB_API_KEY = "cvfj3h9r01qtu9s5564gcvfj3h9r01qtu9s55650";
    private static final String ALPHA_VANTAGE_KEY = "202VJPX1LEX8M4MA";//"QVKNZ7YQGN1U0PTZ";
    private static final String ALPHA_VANTAGE_BASE_URL = "https://www.alphavantage.co/";

    private static String ALPHA_VANTAGE_LISTING_URL = "https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=";

    private static String FINNHUB_LISTING_URL = "https://finnhub.io/api/v1/stock/symbol?exchange=US&token=";
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private static String FINNHUB_API_KEY = "cvfj3h9r01qtu9s5564gcvfj3h9r01qtu9s55650";
    private static final String IEX_API_URL = "https://cloud.iexapis.com/stable/";

    private static final String IEX_API_KEY = "https://sandbox.iexapis.com/";
    private static Retrofit mRetrofit;
    public StockControllerService() {
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        init();
        return binder;
    }

    public static Retrofit initRetrofit() {
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

    private void getAllStockInMarketDomainBySymbol(ArrayList<Stock> stocksInMarket){

        Object waitobj = new Object();
        Map<String, String> stockMap = new HashMap<>();
        OkHttpClient client = new OkHttpClient();
        for(Stock stock : stocksInMarket ){

            String symbol = stock.getStockSymbol();
            String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" + symbol + "&token=" + FINNHUB_API_KEY;
            Request request = new Request.Builder().url(url).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    synchronized (waitobj){
                        waitobj.notify();
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    try {
                        JSONObject obj = new JSONObject(json);
                        String webUrl = obj.getString("weburl");
                        String ticker = obj.getString("ticker");

                        // Extract domain
                        URI uri = new URI(webUrl);
                        String domain = uri.getHost().replace("www.", ""); // e.g., "apple.com"

                        Log.d("CompanyDomain", "Domain: " + domain);

                        // Optional: Get logo from Clearbit
                        String logoUrl = "https://logo.clearbit.com/" + domain;

                        stockMap.put(ticker,webUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    synchronized (waitobj){
                        waitobj.notifyAll();
                    }
                }
            });

            try {
                synchronized (waitobj){
                    waitobj.wait(10000);
                }

            } catch (InterruptedException e) {

            }
        }

        Log.d("tag", "tag");
    }

    public void GetStocksByName(String stockName){
        ArrayList<StockInfo> stocksList = stockDBManager.GetStocksByName(stockName);
        synchronized (mObservers){
            for (final Observer observer : mObservers) {
                observer.GetAllStocksInMarket(stocksList);
            }
        }
    }
    public void GetTransactionHistory(){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.getReference(HISTORY_TABLE_NAME).child(mUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("onDataChange", snapshot.toString());
                ArrayList<Transaction> transactionList =  new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    float amountOfStock = dataSnapshot.child("amountOfStock").getValue(float.class);
                    int moneyInvested = dataSnapshot.child("moneyInvested").getValue(int.class);
                    String stockName = dataSnapshot.child("stockName").getValue(String.class);
                    Date d = dataSnapshot.child("transactionTime").getValue(Date.class);
                    Transaction t = new Transaction(moneyInvested,stockName,amountOfStock,d);
                    transactionList.add(t);
                }

                synchronized (mObservers){
                    for (final Observer observer : mObservers) {
                        observer.GetTransactionHistory(transactionList);
                    }
                }
                Log.d("onDataChange", "ended");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void AddNewStockInvested(StockInfo stock){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference objRef = mFirebaseDatabase.getReference(STOCK_TABLE_NAME).child(mUID);
        String stockId = objRef.push().getKey();
        stock.setStockID(stockId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("amountOfStock", stock.getAmountOfStock());
        updates.put("stockSymbol", stock.getStockSymbol());

        //mFirebaseDatabase.getReference(STOCK_TABLE_NAME).child(mUID).child(stock.getStockID()).updateChildren(updates,);

        mFirebaseDatabase.getReference(STOCK_TABLE_NAME).child(mUID).child(stock.getStockID()).updateChildren(updates,new DatabaseReference.CompletionListener(){
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Log.d(STOCK_CONTROLLER_SERVICE_NAME,"AddNewStockInvested complete");
            }
        });
        //UpdateAmountOfStockInvested(stock);
    }

    public void UpdateAmountOfStockInvested(StockInfo stock){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        String id = stock.getStockID();
        mFirebaseDatabase.getReference(STOCK_TABLE_NAME).child(mUID).child(stock.getStockID()).child("amountOfStock").setValue(stock.getAmountOfStock(),new DatabaseReference.CompletionListener(){
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Log.e("FIREBASE", "Failed to set value: " + error.getMessage());
                } else {
                    Log.d("FIREBASE", "Value set successfully " );
                }
            }
        });
    }
    public void GetAllStocksInvested(){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.getReference(STOCK_TABLE_NAME).child(mUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("onDataChange", snapshot.toString());
                //transactionHistoryArr = new ArrayList<>();
                ArrayList<StockInfo> stocksStack =  new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    String stockID = dataSnapshot.getKey();
                    float amountOfStock = dataSnapshot.child("amountOfStock").getValue(float.class);
                    //String typeOfStock = dataSnapshot.child("typeOfStock").getValue(String.class);
                    String stockSymbol = dataSnapshot.child("stockSymbol").getValue(String.class);
                    //Date d = dataSnapshot.child("lastUpdate").getValue(Date.class);
                    StockInfo s = new StockInfo("", stockSymbol,amountOfStock, stockID);
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

   /* public static class RetrofitClient {
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
    }*/

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

        if(stockDBManager == null){
            stockDBManager = new DBManager(this);
        }
        if(mFirebaseDatabase == null){
            mFirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
        }

        if(mFirebaseAuth == null){
            mFirebaseAuth = FirebaseAuth.getInstance();
        }

        //initRetrofit();

        //IexApi api = mRetrofit.create(IexApi.class);

        //GetAllStocksInMarket();

        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[]{}; }
                }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            Log.e(STOCK_CONTROLLER_SERVICE_NAME, e.getMessage());
        }
        try {
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            Log.e(STOCK_CONTROLLER_SERVICE_NAME, e.getMessage());
        }

        mOkHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .build();


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
                //Notify observers
                synchronized (mObservers){
                    for (final Observer observer : mObservers) {
                        observer.SignInWithEmailAndPasswordCompleate(task);
                    }
                }
            }

        });

    }

    public void UpdateStocksInvested(Stock stock){
        Log.d(STOCK_CONTROLLER_SERVICE_NAME,"start UpdateStocksInvested");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.getReference(STOCK_TABLE_NAME).child(mUID).child(stock.getStockID()).setValue(stock.getAmountOfStock()).addOnCompleteListener(task -> {

        });

    }

    public void UpdateCash(float newValue){
        Log.d(STOCK_CONTROLLER_SERVICE_NAME,"start getAllCash");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.getReference(USER_DATA_TABLE_ROW).child(mUID).child("cash").setValue(newValue).addOnCompleteListener(task -> {
            synchronized (mObservers){
                for (final Observer observer : mObservers) {
                    observer.OnUpdateCashCompleted(task.isSuccessful());
                }
            }
        });
    }
    public void GetAllCash()
    {
        Log.d(STOCK_CONTROLLER_SERVICE_NAME,"start getAllCash");
        if(currentUserData != null){
            synchronized (mObservers){
                for (final Observer observer : mObservers) {
                    observer.GetAllCash(currentUserData.getCash());
                }
            }
            return;
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.getReference(USER_DATA_TABLE_ROW).child(mUID).addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserData = snapshot.getValue(UserData.class);
                if (currentUserData!= null)
                {
                    synchronized (mObservers){
                        for (final Observer observer : mObservers) {
                            observer.GetAllCash(currentUserData.getCash());
                        }
                    }
                    Log.d("onDataChange", "ended get all cash");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetAllUserData()
    {
        Log.d("GetAllUserData()","start get all user data");
        if(currentUserData != null){
            synchronized (mObservers){
                for (final Observer observer : mObservers) {
                    observer.GetAllUserData(currentUserData);
                }
            }
            return;
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.getReference(USER_DATA_TABLE_ROW).child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserData = snapshot.getValue(UserData.class);
                if (currentUserData != null)
                {
                    synchronized (mObservers){
                        for (final Observer observer : mObservers) {
                            observer.GetAllUserData(currentUserData);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d("onDataChange", "ended get all user data");

    }

    public void SetTransaction(Transaction transaction){

        DatabaseReference transRef = mFirebaseDatabase.getReference(HISTORY_TABLE_NAME).child(mUID);
        String transID = transRef.push().getKey();
        transRef.child(transID).setValue(transaction).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(STOCK_CONTROLLER_SERVICE_NAME,"SetTransaction() - successful");
            } else {
                Log.d(STOCK_CONTROLLER_SERVICE_NAME,"SetTransaction() - failed");
            }
        });
    }

    public void GetStockInfo(String stockSymbol){

        /*"01. symbol": "AAPL",
        "02. open": "186.1000",
        "03. high": "199.5400",
        "04. low": "186.0600",
        "05. price": "198.1500",
        "06. volume": "87435915",
        "07. latest trading day": "2025-04-11",
        "08. previous close": "190.4200",
        "09. change": "7.7300",
        "10. change percent": "4.0594%"*/

        //{"c":697.71,"d":13.09,"dp":1.912,"h":702.806,"l":691.87,"o":696.17,"pc":684.62,"t":1749240000}

        Log.d(STOCK_CONTROLLER_SERVICE_NAME, "GetStockInfo");

        if(stockSymbol == null){
            Log.e(STOCK_CONTROLLER_SERVICE_NAME, "GetStockInfo() - stockSymbol == null");
            return;
        }
        //OkHttpClient client = new OkHttpClient();

        /*String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE"
                + "&symbol=" + stockSymbol
                + "&apikey=" + ALPHA_VANTAGE_API_KEY;*/

        String url = "https://finnhub.io/api/v1/quote?symbol="
                + stockSymbol
                + "&token=" + FINNHUB_API_KEY;

        Request request = new Request.Builder().url(url).build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(STOCK_CONTROLLER_SERVICE_NAME, "GetStockInfo() - " + e.getMessage());
                //Notify Observers
                synchronized (mObservers){
                    for (final Observer observer : mObservers) {
                        observer.OnStockInfoUpdate(null);
                    }
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try{
                        String json = response.body().string();
                        //JSONObject obj = new JSONObject(json).getJSONObject("Global Quote");
                        JSONObject obj = new JSONObject(json);

                        StockInfo stockInf = new StockInfo();
                        /*String symbol = obj.getString("01. symbol");
                        String price = obj.getString("05. price");
                        String changePercent = obj.getString("10. change percent");
*/
                        String symbol = stockSymbol;
                        double price = obj.getDouble("c");
                        String changePercent = obj.getString("dp");

                        /*String symbol = stockSymbol;
                        String price = "198.1500";
                        String changePercent = "4.0594%";*/

                        stockInf.setStockSymbol(symbol);
                        stockInf.setPrice((float)price);
                        stockInf.setChange_percent(changePercent);

                        //Update Database
                        stockDBManager.UpdateStockInfo(stockInf);
                        String stockName = stockDBManager.GetStocksBySymbol(symbol).getStockName();
                        if(stockName != null){
                            stockInf.setStockName(stockName);
                        }

                        //Notify Observers
                        synchronized (mObservers){
                            for (final Observer observer : mObservers) {
                                observer.OnStockInfoUpdate(stockInf);
                            }
                        }
                        /*System.out.println("Current Price: $" + price);
                        System.out.println("Change Percent: " + changePercent);*/
                    } catch (Exception e) {
                        Log.d("Log", e.getMessage());
                        //Notify Observers
                        synchronized (mObservers){
                            for (final Observer observer : mObservers) {
                                observer.OnStockInfoUpdate(null);
                            }
                        }
                    }
                }
            }
        });
    }

    public void GetAllStocksInMarket(){

        ArrayList<StockInfo> stocksInMarket = stockDBManager.GetAllStocksInMarket();
        if(!stocksInMarket.isEmpty()){
            synchronized (mObservers){
                for (final Observer observer : mObservers) {
                    observer.GetAllStocksInMarket(stocksInMarket);
                }
            }
            return;
        }


        OkHttpClient client = new OkHttpClient();

        //String url = FINNHUB_LISTING_URL + FINNHUB_API_KEY;

        String url = ALPHA_VANTAGE_LISTING_URL + ALPHA_VANTAGE_KEY;

        url = "https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=demo";// + ALPHA_VANTAGE_API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("AlphaVantage", "Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    //String jsonResponse = response.body().string();
                    //BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));

                    String csvData = response.body().string();
                    BufferedReader reader = new BufferedReader(new StringReader(csvData));

                    String line;
                    boolean isFirstLine = true;

                    ArrayList<StockInfo> stocksInMarket =  new ArrayList<>();

                    while ((line = reader.readLine()) != null) {
                        if (isFirstLine) {
                            isFirstLine = false; // Skip the header
                            continue;
                        }

                        String[] tokens = line.split(",");
                        if (tokens.length >= 4) {
                            String symbol = tokens[0];
                            String name = tokens[1];
                            String exchange = tokens[2];
                            String assetType = tokens[3];

                            stocksInMarket.add(new StockInfo(name,symbol,0, "0"));

                            Log.d("StockSymbol", "Symbol: " + symbol + ", Name: " + name + ", Exchange: " + exchange);
                        }
                    }

                    //getAllStockInMarketDomainBySymbol(stocksInMarket);
                    stockDBManager.InsertAllStockInMarket(stocksInMarket);

                    synchronized (mObservers){
                        for (final Observer observer : mObservers) {
                            observer.GetAllStocksInMarket(stocksInMarket);
                        }
                    }

                } else {
                    Log.e("AlphaVantage", "Unsuccessful response: " + response.code());
                }
            }
        });
    }
}