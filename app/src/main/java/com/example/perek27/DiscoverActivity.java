package com.example.perek27;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class DiscoverActivity extends AppCompatActivity implements View.OnClickListener {

    StockModel mStockModel;
    RecyclerView recyclerView;
    private Stock currentStock;
    Button summaryBtn, historyBtn,settingBtn;

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
        /*ArrayList<Stock> allStocks = */mStockModel.GetAllStocksInMarket();

        /*DiscoverAdapter DiscoverAdapter = new DiscoverAdapter(DiscoverActivity.this,allStocks, item -> {
            currentStock = item;
            Intent tmpIntent = new Intent(DiscoverActivity.this, StockActionActivity.class);
            tmpIntent.putExtra("StockName", item.getStockName());
            startActivity(tmpIntent);

        });
        recyclerView.setAdapter(DiscoverAdapter);*/

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
            Intent intent = new Intent(DiscoverActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

    }

    public class DiscoverActivityObserver implements Observer{
        @Override
        public void SignInWithEmailAndPasswordCompleate(@NonNull Task<AuthResult> task) {

        }

        @Override
        public void GetAllStocksInvested(List<Stock> stockInvested) {

        }

        @Override
        public void GetTransactionHistory(ArrayList<Transaction> transactionsList) {

        }

        @Override
        public void GetAllStocksInMarket(List<Stock> stocksList) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // ✅ UI operations here (e.g., update TextView, Toast)
                    DiscoverAdapter DiscoverAdapter = new DiscoverAdapter(DiscoverActivity.this,new ArrayList<>(stocksList), item -> {
                        currentStock = item;
                        Intent tmpIntent = new Intent(DiscoverActivity.this, StockActionActivity.class);
                        tmpIntent.putExtra("StockName", item.getStockName());
                        startActivity(tmpIntent);

                    });
                    recyclerView.setAdapter(DiscoverAdapter);
                }
            });

            /*DiscoverAdapter DiscoverAdapter = new DiscoverAdapter(DiscoverActivity.this,new ArrayList<>(stocksList), item -> {
            currentStock = item;
            Intent tmpIntent = new Intent(DiscoverActivity.this, StockActionActivity.class);
            tmpIntent.putExtra("StockName", item.getStockName());
            startActivity(tmpIntent);

            });
            recyclerView.setAdapter(DiscoverAdapter);*/
        }

        @Override
        public void getALLCash(float cash) {

        }

        @Override
        public void GetAllUserData(UserData userData) {

        }
    }

}
