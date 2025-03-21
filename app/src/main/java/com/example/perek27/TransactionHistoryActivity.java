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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransactionHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    ArrayList<Transaction> transactionArr;

    Button summaryBtn, discoverBtn, settingBtn;

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

        summaryBtn = (Button)findViewById(R.id.summaryButton);
        summaryBtn.setOnClickListener(this);

        discoverBtn =(Button)findViewById(R.id.discoverButton);
        discoverBtn.setOnClickListener(this);

        settingBtn = (Button)findViewById(R.id.settingButton);
        settingBtn.setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        firebaseDatabase = FirebaseDatabase.getInstance();
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
            Intent intent =new Intent(TransactionHistoryActivity.this, SettingsActivity.class);
            startActivity(intent);
        }


    }
}