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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;
import java.util.Stack;

public class discoverActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase firebaseDatabase;

    RecyclerView recyclerView;
    Button summaryBtn, historyBtn,settingBtn;
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
        ArrayList<stock> stocksStack =  new ArrayList<>();
        Date currentDay = new Date(System.currentTimeMillis());
        stock s1 = new stock("AAPL", 500,currentDay);
        stock s2 = new stock("NETFLIX", 200,currentDay);
        stock s3 = new stock("META", 130,currentDay);

        stocksStack.add(s1);
        stocksStack.add(s2);
        stocksStack.add(s3);


        summaryBtn = (Button)findViewById(R.id.summaryButton);
        summaryBtn.setOnClickListener(this);

        historyBtn = (Button)findViewById(R.id.historyButton);
        historyBtn.setOnClickListener(this);

        settingBtn = (Button)findViewById(R.id.settingButton);
        settingBtn.setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.recycleDiscover);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        stockAdapter stockAdapter = new stockAdapter(discoverActivity.this,stocksStack);
        recyclerView.setAdapter(stockAdapter);

    }

    @Override
    public void onClick(View view) {
        if (view == summaryBtn)
        {
            Intent intent = new Intent(discoverActivity.this,UserInfoActivity.class);
            startActivity(intent);
        } else if (view == historyBtn)
        {
            Intent intent = new Intent(discoverActivity.this,postListActivity.class);
            startActivity(intent);
        } else if (view == settingBtn)
        {
            Intent intent = new Intent(discoverActivity.this,settingsActivity.class);
            startActivity(intent);
        }

    }
}