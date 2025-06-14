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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    StockModel mStockModel;
    RecyclerView recyclerView;

    TextView showAmountOfStock, showDate ,showStock ;
    ArrayList<Transaction> transactionArr;

    private Transaction currentStock2;
    Button summaryBtn, discoverBtn, settingBtn;

    private Observer mTransactionHistoryActivityObserver = new MainActivityObserver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mStockModel = StockModel.GetInstance();
        mStockModel.Init();
        mStockModel.register(mTransactionHistoryActivityObserver);


        summaryBtn = (Button)findViewById(R.id.summaryButton);
        summaryBtn.setOnClickListener(this);

        discoverBtn =(Button)findViewById(R.id.discoverButton);
        discoverBtn.setOnClickListener(this);

        settingBtn = (Button)findViewById(R.id.settingButton);
        settingBtn.setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStockModel.GetTransactionHistory();

        /*firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("push").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactionArr = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Transaction p = dataSnapshot.getValue(Transaction.class);
                    transactionArr.add(p);
                }


                TransactionHistoryAdapter adapter = new TransactionHistoryAdapter(TransactionHistoryActivity.this, transactionArr);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

*/

    }


    @Override
    public void onClick(View view) {
        if (view == summaryBtn)
        {
            Intent intent = new Intent(TransactionHistoryActivity.this, SummaryActivity.class);
            startActivity(intent);
        } else if (view == discoverBtn)
        {
            Intent intent = new Intent(TransactionHistoryActivity.this, DiscoverActivity.class);
            startActivity(intent);
        } else if (view == settingBtn)
        {
            Intent intent =new Intent(TransactionHistoryActivity.this, ProfileActivity.class);
            startActivity(intent);
        }


    }
    public class MainActivityObserver implements Observer{

        @Override
        public void SignInWithEmailAndPasswordCompleate(@NonNull Task<AuthResult> task) {

        }

        @Override
        public void GetAllStocksInvested(List<StockInfo> stockInvested) {

        }

        @Override
        public void GetTransactionHistory(ArrayList<Transaction> transactionsList) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    TransactionHistoryAdapter transactionHistoryAdapter = new TransactionHistoryAdapter(TransactionHistoryActivity.this ,new ArrayList<>(transactionsList), item -> {

                    });
                    recyclerView.setAdapter(transactionHistoryAdapter);
                }

            });

        }

        @Override
        public void GetDailyReportOfSymbolResults(List<StockInfo> stocksList) {

        }

        @Override
        public void GetAllStocksInMarket(List<StockInfo> stocksList) {

        }

        @Override
        public void GetAllCash(float cash) {

        }

        @Override
        public void GetAllUserData(UserData userData) {

        }

        @Override
        public void OnStockInfoUpdate(StockInfo stockInf) {

        }

        @Override
        public void OnBuyStockCompleted(Boolean success, String reason) {

        }

        @Override
        public void OnSellStockCompleted(Boolean success, String reason) {

        }

        @Override
        public void OnUpdateCashCompleted(Boolean success) {

        }
    }
}