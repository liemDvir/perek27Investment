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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity implements View.OnClickListener {

    StockModel mStockModel;

    RecyclerView recyclerView;
    Button logout, historyBtn, discoverBtn, settingBtn;

    TextView cashAmountOfMoney, sumAllMoney,sumAllMoneyInvested;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    private Stock currentStock;

    //private Observer mMainActivityObserver = new MainActivity.MainActivityObserver();

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
        //mStockModel.register(mMainActivityObserver);

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

        sumAllMoneyInvested.setText(String.valueOf(mStockModel.GetSumStocksInvested()));
        sumAllMoney.setText(String.valueOf(mStockModel.GetSumOfAllMoney()));
        cashAmountOfMoney.setText((String.valueOf(mStockModel.GetAllCash())));

        recyclerView = (RecyclerView)findViewById(R.id.recycleSummary);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Stock> allStocksInvestedArr = mStockModel.GetAllStocksInvested();
        //TODO - check if list is empty or null
        SummaryAdapter summaryAdapter = new SummaryAdapter(SummaryActivity.this, allStocksInvestedArr,item -> {
        currentStock = item;
        Intent tmpIntent = new Intent(SummaryActivity.this,StockActionActivity.class);
        tmpIntent.putExtra("StockName", currentStock.getTypeOfStock());
        startActivity(tmpIntent);
        });
        recyclerView.setAdapter(summaryAdapter);


        /// need to transform the code to the controller, the code is how to get data from the firebase
        /*if(firebaseAuth.getCurrentUser()!= null)
        {
            (firebaseDatabase.getReference("user").child(firebaseAuth.getCurrentUser().getUid())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserData user = snapshot.getValue(UserData.class);
                    if (user!=null)
                    {
                        String cash = String.valueOf(user.getCash());
                        cashAmountOfMoney.setText(cash);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }*/


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
            Intent intent = new Intent(SummaryActivity.this, SettingsActivity.class);
            this.startActivity(intent);
        }

    }

    public class MainActivityObserver implements Observer{

        @Override
        public void SignInWithEmailAndPasswordCompleate(@NonNull Task<AuthResult> task) {

        }
    }
}