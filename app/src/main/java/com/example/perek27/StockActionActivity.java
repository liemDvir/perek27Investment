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

import com.jjoe64.graphview.GraphView;


public class StockActionActivity extends AppCompatActivity implements View.OnClickListener {

    StockModel mStockModel;
    DiscoverActivity discoverActivity;

    EditText  amountOfStock;
     TextView typeOfStock;
    Button buyBtn, sellBtn, goBackBtn;
    Stock currentStock;

    GraphView graphView;
    //private Observer mMainActivityObserver = new MainActivity.MainActivityObserver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stock_action);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mStockModel = StockModel.GetInstance();
        mStockModel.Init();
        //mStockModel.register(mMainActivityObserver);

        Intent intent = getIntent();
        String stockName = intent.getStringExtra("StockName");
        currentStock = mStockModel.getStockByName(stockName);

        goBackBtn = (Button)findViewById(R.id.goBackXml);
        goBackBtn.setOnClickListener(this);

        typeOfStock = (TextView)findViewById(R.id.typeOfStock);
        typeOfStock.setOnClickListener(this);
        typeOfStock.setText(stockName);

        amountOfStock = (EditText)findViewById(R.id.amountMoney);
        amountOfStock.setOnClickListener(this);

        buyBtn = (Button)findViewById(R.id.buyButton);
        buyBtn.setOnClickListener(this);

        sellBtn = (Button)findViewById(R.id.sellButton);
        sellBtn.setOnClickListener(this);

        graphView = (GraphView)findViewById(R.id.graphViewXml);
        graphView.setOnClickListener(this);
        graphView.addSeries(mStockModel.GetCurrentStockGraphViewSeries(currentStock));
        // maximum and minimum value y
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(-40);
        graphView.getViewport().setMaxY(40);
        // maximum and minimum value x
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(40);

        // enable scaling and scrolling
        //graphView.getViewport().setScrollable(true); // enables horizontal scrolling
        //graphView.getViewport().setScrollableY(true); // enables vertical scrolling
        graphView.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graphView.getViewport().setScalableY(true); // enables vertical zooming and scrolling

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
                 Intent intent = new Intent(StockActionActivity.this, SummaryActivity.class);
                 finish();
             }

         } else if (view == sellBtn) {


         } else if (view == goBackBtn)
         {
             finish();
         } else
        {
            Toast.makeText(StockActionActivity.this, "Error", Toast.LENGTH_LONG).show();
        }

    }
}