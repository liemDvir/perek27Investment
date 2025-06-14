package com.example.perek27;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.List;
import android.text.Editable;
import android.text.TextWatcher;

public class DiscoverActivity extends AppCompatActivity implements View.OnClickListener {

    StockModel mStockModel;
    RecyclerView recyclerView;

    private DiscoverAdapter discoverAdapter;

    private Stock currentStock;
    Button summaryBtn, historyBtn,settingBtn;

    EditText searchET;

    private Observer discoverActivityObserver = new DiscoverActivityObserver();
    public DiscoverActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_discover);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchET = (EditText)findViewById(R.id.searchEditText);
        searchET.setOnClickListener(this);


        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // לא צריך כאן כלום
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    mStockModel.GetStocksByName(query);
                } else {
                    mStockModel.GetAllStocksInMarket();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        summaryBtn = (Button)findViewById(R.id.summaryButton);
        summaryBtn.setOnClickListener(this);

        historyBtn = (Button)findViewById(R.id.historyButton);
        historyBtn.setOnClickListener(this);

        settingBtn = (Button)findViewById(R.id.settingButton);
        settingBtn.setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.recycleDiscover);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStockModel = StockModel.GetInstance();
        mStockModel.Init();
        mStockModel.register(discoverActivityObserver);
        mStockModel.GetAllStocksInMarket();

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                if (layoutManager == null) return;
//
//                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
//
//                if (discoverAdapter == null) return;
//
//                for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
//                    if (i >= 0 && i < discoverAdapter.arrayList.size()) {
//                        StockInfo stockInfoCurrent = discoverAdapter.arrayList.get(i);
//                        mStockModel.GetStockInfo(stockInfoCurrent.getStockSymbol());
//                    }
//                }
//            }
//        });




    }
    public Stock getCurrentStock()
    {
        return currentStock;
    }


    @Override
    public void onClick(View view) {

        if (view == summaryBtn)
        {
            Intent intent = new Intent(DiscoverActivity.this, SummaryActivity.class);
            startActivity(intent);
        } else if (view == historyBtn)
        {
            Intent intent = new Intent(DiscoverActivity.this, TransactionHistoryActivity.class);
            startActivity(intent);
        } else if (view == settingBtn)
        {
            Intent intent = new Intent(DiscoverActivity.this, ProfileActivity.class);
            startActivity(intent);
        }else if(view == searchET){

        }
    }

    public class DiscoverActivityObserver implements Observer{
        @Override
        public void SignInWithEmailAndPasswordCompleate(@NonNull Task<AuthResult> task) {

        }

        @Override
        public void GetAllStocksInvested(List<StockInfo> stockInvested) {

        }

        @Override
        public void GetTransactionHistory(ArrayList<Transaction> transactionsList) {

        }

        @Override
        public void GetDailyReportOfSymbolResults(List<StockInfo> stocksList)
        {


        }

        @Override
        public void GetAllStocksInMarket(List<StockInfo> stocksList) {
            runOnUiThread(() -> {
                if (discoverAdapter == null) {
                    discoverAdapter = new DiscoverAdapter(DiscoverActivity.this, new ArrayList<>(stocksList), item -> {
                        currentStock = item;
                        Intent tmpIntent = new Intent(DiscoverActivity.this, StockActionActivity.class);
                        tmpIntent.putExtra("StockName", item.getStockName());
                        tmpIntent.putExtra("StockSymbol", item.getStockSymbol());
                        tmpIntent.putExtra("amountOfShares", item.getAmountOfStock() + "");
                        startActivity(tmpIntent);
                    });
                    recyclerView.setAdapter(discoverAdapter);
                } else {
                    discoverAdapter.arrayList.clear();
                    discoverAdapter.arrayList.addAll(stocksList);
                    discoverAdapter.notifyDataSetChanged();
                }

                // קריאה ל-GetStockInfo ל-50 מניות ראשונות
//                for (int i = 0; i < Math.min(50, stocksList.size()); i++) {
//                    StockInfo stockInfoCurrent = stocksList.get(i);
//                    mStockModel.GetStockInfo(stockInfoCurrent.getStockSymbol());
//                }
            });
        }

        @Override
        public void GetAllCash(float cash) {

        }

        @Override
        public void GetAllUserData(UserData userData) {

        }
        @Override
        public void OnStockInfoUpdate(StockInfo stockInf) {

            runOnUiThread(new Runnable() {
                public void run() {
                    // ✅ UI operations here (e.g., update TextView, Toast)

                    DiscoverAdapter discoverAdapter = (DiscoverAdapter) recyclerView.getAdapter();
                    if (discoverAdapter == null) return;

                    // למצוא את המניה המבוקשת לפי הסימבול ולעדכן אותה
                    for (int i = 0; i < discoverAdapter.arrayList.size(); i++) {
                        StockInfo stockInList = discoverAdapter.arrayList.get(i);

                        if (stockInf.getStockSymbol().equals(stockInList.getStockSymbol())) {
                            stockInList.setPrice(stockInf.getPrice());
                            stockInList.setChange_percent(stockInf.getChange_percent());

                            // לעדכן את ה-ViewHolder הספציפי
                            discoverAdapter.notifyItemChanged(i);
                            break;
                    }    }
                }
            });
        }

        @Override
        public void OnBuyStockCompleted(Boolean success, String reason) {

        }

        @Override
        public void OnSellStockCompleted(Boolean seccess, String reason) {

        }

        @Override
        public void OnUpdateCashCompleted(Boolean success) {

        }

    }

    }


