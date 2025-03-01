package com.example.perek27;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class DiscoverActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase firebaseDatabase;

    RecyclerView recyclerView;
    private Stock currentStock;
    Button summaryBtn, historyBtn,settingBtn;
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
        ArrayList<Stock> stocksStack =  new ArrayList<>();
        Date currentDay = new Date(System.currentTimeMillis());
        Stock s1 = new Stock("AAPL", 500,currentDay);
        Stock s2 = new Stock("NETFLIX", 200,currentDay);
        Stock s3 = new Stock("META", 130,currentDay);

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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("stock").push();
        ref.setValue(stocksStack);

        stockAdapter stockAdapter = new stockAdapter(DiscoverActivity.this,stocksStack, item -> {
            currentStock = item;
            Intent tmpIntent = new Intent(DiscoverActivity.this, StockActionActivity.class);
            tmpIntent.putExtra("StockName", item.getTypeOfStock());
            startActivity(tmpIntent);

        });
        recyclerView.setAdapter(stockAdapter);

    }
    public Stock getCurrentStock()
    {
        return currentStock;
    }


    @Override
    public void onClick(View view) {
        if (view == summaryBtn)
        {
            Intent intent = new Intent(DiscoverActivity.this,UserInfoActivity.class);
            startActivity(intent);
        } else if (view == historyBtn)
        {
            Intent intent = new Intent(DiscoverActivity.this,postListActivity.class);
            startActivity(intent);
        } else if (view == settingBtn)
        {
            Intent intent = new Intent(DiscoverActivity.this,settingsActivity.class);
            startActivity(intent);
        }

    }

}
