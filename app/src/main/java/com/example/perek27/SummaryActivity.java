package com.example.perek27;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class SummaryActivity extends AppCompatActivity implements View.OnClickListener {

    StockModel mStockModel;

    RecyclerView recyclerView;
    Button  historyBtn, discoverBtn, settingBtn;

    TextView cashAmountOfMoney, sumAllMoney,sumAllMoneyInvested;
    private StockInfo currentStock;

    private Observer mSummayActivityObserver = new MainActivityObserver();

    float sumOfStockValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_summary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mStockModel = StockModel.GetInstance();
        mStockModel.Init();
        mStockModel.register(mSummayActivityObserver);

        setContentView(R.layout.activity_summary);

        historyBtn = (Button)findViewById(R.id.historyButton);
        historyBtn.setOnClickListener(this);

        discoverBtn =(Button)findViewById(R.id.discoverButton);
        discoverBtn.setOnClickListener(this);

        settingBtn = (Button)findViewById(R.id.settingButton);
        settingBtn.setOnClickListener(this);

        sumAllMoneyInvested = (TextView)findViewById(R.id.sumMoneyInvestedXml);
        sumAllMoneyInvested.setOnClickListener(this);

        sumAllMoney = (TextView)findViewById(R.id.sumMoneyXml);
        sumAllMoney.setOnClickListener(this);

        cashAmountOfMoney = (TextView)findViewById(R.id.cashAmountOfMoney);
        cashAmountOfMoney.setOnClickListener(this);


        recyclerView = (RecyclerView)findViewById(R.id.recycleSummary);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStockModel.GetAllCash();
        mStockModel.GetSummaryOfUser();
    }

    @Override
    public void onClick(View view) {

        if (view == historyBtn) {
            Intent intent = new Intent(SummaryActivity.this, TransactionHistoryActivity.class);
            this.startActivity(intent);

        } else if(view == discoverBtn)
        {
            Intent intent = new Intent(SummaryActivity.this, DiscoverActivity.class);
            this.startActivity(intent);
        } else if (view == settingBtn)
        {
            Intent intent = new Intent(SummaryActivity.this, ProfileActivity.class);
            this.startActivity(intent);
        }

    }

    public class MainActivityObserver implements Observer{

        @Override
        public void SignInWithEmailAndPasswordCompleate(@NonNull Task<AuthResult> task) {

        }

        @Override
        public void GetTransactionHistory(ArrayList<Transaction> transactionsList) {

        }

        @Override
        public void GetAllStocksInMarket(List<StockInfo> stocksList) {

        }

        @Override
        public void GetAllCash(float cash) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cashAmountOfMoney.setText((String.valueOf(cash)));
                }
            });
        }

        @Override
        public void GetAllStocksInvested(List<StockInfo> stockInvested) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // ✅ UI operations here (e.g., update TextView, Toast)
                    SummaryAdapter summaryAdapter = new SummaryAdapter(SummaryActivity.this, new ArrayList<>(stockInvested),item -> {
                        currentStock = (StockInfo) item;
                        Intent tmpIntent = new Intent(SummaryActivity.this,StockActionActivity.class);
                        tmpIntent.putExtra("StockName", currentStock.getStockName());
                        tmpIntent.putExtra("StockSymbol", currentStock.getStockSymbol());
                        startActivity(tmpIntent);
                    });
                    recyclerView.setAdapter(summaryAdapter);
                }
            });
        }

        @Override
        public void GetAllUserData(UserData userDate) {

        }

        @Override
        public void OnStockInfoUpdate(StockInfo stockInf) {



            runOnUiThread(new Runnable() {
                public void run() {
                    // ✅ UI operations here (e.g., update TextView, Toast)

                    SummaryAdapter summaryAdapter = (SummaryAdapter) recyclerView.getAdapter();

                    for(StockInfo stock : summaryAdapter.arrayList){

                        int position = summaryAdapter.arrayList.indexOf(stock);

                        if(stock.getStockSymbol().equals(stockInf.getStockSymbol())){

                            stock.setPrice(stockInf.getPrice());
                            stock.setChange_percent(stockInf.getChange_percent());

                            if (stock.getAmountOfStock() != 0)
                            {
                                sumOfStockValue = sumOfStockValue + stock.getAmountOfStock()*stock.getPrice();
                            }

                            summaryAdapter.notifyItemChanged(position);
                            break; // אם אין מניות עם אותו סמל פעמיים, כדאי לצאת מהלולאה
                        }
                    }
                    sumAllMoneyInvested.setText(String.valueOf(sumOfStockValue));
                    sumAllMoney.setText(String.valueOf((sumOfStockValue + Float.parseFloat(cashAmountOfMoney.getText().toString()))));



                }
            });
        }

        @Override
        public void OnBuyStockCompleted(Boolean success, String reason) {

        }

        @Override
        public void OnUpdateCashCompleted(Boolean success) {

        }
    }
}