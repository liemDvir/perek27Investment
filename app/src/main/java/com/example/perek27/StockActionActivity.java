package com.example.perek27;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StockActionActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase firebaseDatabase;
    DiscoverActivity discoverActivity;

    DatabaseReference ref;
    EditText  amountOfStock;
     TextView typeOfStock;
    Button buyBtn, sellBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseDatabase = FirebaseDatabase.getInstance("https://insvestment-85820-default-rtdb.firebaseio.com/");

        Intent intent = getIntent();
        String stockName = intent.getStringExtra("StockName");

        typeOfStock = (TextView)findViewById(R.id.typeOfStock);
        typeOfStock.setOnClickListener(this);
        typeOfStock.setText(stockName);

        amountOfStock = (EditText)findViewById(R.id.amountMoney);
        amountOfStock.setOnClickListener(this);

        buyBtn = (Button)findViewById(R.id.buyButton);
        buyBtn.setOnClickListener(this);

        sellBtn = (Button)findViewById(R.id.sellButton);
        sellBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
         if (view == buyBtn)
         {
             if (!typeOfStock.getText().toString().isEmpty() && 0 < Integer.parseInt(amountOfStock.getText().toString()) && !amountOfStock.getText().toString().isEmpty()) {
                 String strType = typeOfStock.getText().toString();
                 int intAmount = Integer.parseInt(amountOfStock.getText().toString());
                 TransactionHistory transactionHistory1 = new TransactionHistory(intAmount, strType,true);// means true because buy is positive
                 StockModel.GetInstance().SetTransaction(transactionHistory1);
                 Toast.makeText(StockActionActivity.this, "Successfully bought", Toast.LENGTH_LONG).show();
                 Intent intent = new Intent(StockActionActivity.this,UserInfoActivity.class);
                 finish();
             }

         }
         else
        {
            Toast.makeText(StockActionActivity.this, "Error", Toast.LENGTH_LONG).show();
        }

    }
}