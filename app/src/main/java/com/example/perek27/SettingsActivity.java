package com.example.perek27;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Button summaryBtn, discoverBtn, historyBtn, logoutBtn;

    TextView firstNameTxt, lastNameTxt, emailTxt, dateTxt;
    ImageView pictureView;

    StockModel mStockModel;

    private Observer mProfileObserver = new SettingsActivityObserver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mStockModel = StockModel.GetInstance();
        mStockModel.Init();
        mStockModel.register(mProfileObserver);

        firstNameTxt = (TextView)findViewById(R.id.firstNameTextView);
        firstNameTxt.setOnClickListener(this);

        lastNameTxt = (TextView)findViewById(R.id.lastNameTextView);
        lastNameTxt.setOnClickListener(this);

        emailTxt = (TextView)findViewById(R.id.emailTextView);
        emailTxt.setOnClickListener(this);

        dateTxt = (TextView)findViewById(R.id.dateTextView);
        dateTxt.setOnClickListener(this);

        pictureView = (ImageView)findViewById(R.id.imageView);
        pictureView.setOnClickListener(this);



        summaryBtn = (Button)findViewById(R.id.summaryButton);
        summaryBtn.setOnClickListener(this);

        discoverBtn = (Button)findViewById(R.id.discoverButton);
        discoverBtn.setOnClickListener(this);

        historyBtn = (Button)findViewById(R.id.historyButton);
        historyBtn.setOnClickListener(this);

        logoutBtn = (Button)findViewById(R.id.logoutButton);
        logoutBtn.setOnClickListener(this);

        mStockModel.GetAllUserData();


    }

    @Override
    public void onClick(View view) {
        if (view == summaryBtn)
        {
            Intent intent = new Intent(SettingsActivity.this, SummaryActivity.class);
            startActivity(intent);
        } else if (view == discoverBtn)
        {
            Intent intent = new Intent(SettingsActivity.this, DiscoverActivity.class);
            startActivity(intent);
        } else if (view == historyBtn)
        {
            Intent intent = new Intent(SettingsActivity.this, TransactionHistoryActivity.class);
            startActivity(intent);
        } else if (view == logoutBtn)
        {
            firebaseAuth.signOut();
            finish();

        }

    }
    public class SettingsActivityObserver implements Observer {

        @Override
        public void SignInWithEmailAndPasswordCompleate(@NonNull Task<AuthResult> task) {

        }

        @Override
        public void GetAllStocksInvested(List<Stock> stockInvested) {

        }

        @Override
        public void GetAllStocksInMarket(List<Stock> stocksList) {

        }

        @Override
        public void GetTransactionHistory(ArrayList<Transaction> transactionsList) {

        }

        @Override
        public void getALLCash(float cash) {

        }

        @Override
        public void GetAllUserData(UserData userData) {

            firstNameTxt.setText(userData.getFirstName());
            lastNameTxt.setText(userData.getLastName());
            emailTxt.setText(userData.getEmail());
            Date currentDay = new Date(System.currentTimeMillis());
            if (userData.getDate() != null)
            {
                currentDay = userData.getDate();
            }
            String date = currentDay.getDate() +"/" + currentDay.getMonth()+1 + "/" + currentDay.getYear();
            dateTxt.setText(date);
        }

        @Override
        public void OnStockInfoUpdate(StockInfo stockInf) {

        }
    }
}