package com.example.perek27;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase firebaseDatabase;
    EditText typeOfStock, amountOfStock;
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

        typeOfStock = (EditText)findViewById(R.id.typeOfStock);
        typeOfStock.setOnClickListener(this);

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
                 Post post1 = new Post(intAmount, strType,true);// means true because buy is positive
                 DatabaseReference ref = FirebaseDatabase.getInstance().getReference("push").push();
                 ref.setValue(post1);
                 Toast.makeText(AddPostActivity.this, "Successfully bought", Toast.LENGTH_LONG).show();
                 Intent intent = new Intent(AddPostActivity.this,UserInfoActivity.class);
                 finish();
             }

         }
         else
        {
            Toast.makeText(AddPostActivity.this, "Error", Toast.LENGTH_LONG).show();
        }

    }
}