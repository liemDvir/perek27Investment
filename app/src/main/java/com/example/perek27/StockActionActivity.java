package com.example.perek27;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    Dialog d;

    Button dialogPositiveBtn, dialogNegativeBtn;

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
             sellBtn.setEnabled(false);
             buyBtn.setEnabled(false);
             createDialog();
             if (view == dialogPositiveBtn)
             {
                 d.dismiss();
                if (!typeOfStock.getText().toString().isEmpty() && 0 < Integer.parseInt(amountOfStock.getText().toString()) && !amountOfStock.getText().toString().isEmpty()) {
                    String strType = typeOfStock.getText().toString();
                    int intAmount = Integer.parseInt(amountOfStock.getText().toString());
                    //TransactionHistory transactionHistory1 = new TransactionHistory(intAmount, strType,true);// means true because buy is positive
                    //StockModel.GetInstance().SetTransaction(transactionHistory1);
                    Toast.makeText(StockActionActivity.this, "Successfully bought", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(StockActionActivity.this, SummaryActivity.class);
                     finish();
                }
             }else {
                 sellBtn.setEnabled(true);
                 buyBtn.setEnabled(true);
                 d.dismiss();

             }

         } else if (view == sellBtn) {
             createDialog();
             sellBtn.setEnabled(false);
             buyBtn.setEnabled(false);
            /* if (view == dialogPositiveBtn )
             {
                 /// do the selling action
             }
             else if(view == dialogNegativeBtn){
                 d.dismiss();
                 sellBtn.setEnabled(true);
                 buyBtn.setEnabled(true);
             }*/



         } else if (view == goBackBtn)
         {
             finish();
         } else
        {
            Toast.makeText(StockActionActivity.this, "Error", Toast.LENGTH_LONG).show();
        }

    }
    public void createDialog()
    {
        Log.d("createDialog","Start createDialog");
        d = new Dialog(this);
        d.setContentView(R.layout.dialog_verification_action);
        d.setCancelable(true);
        dialogPositiveBtn = (Button)d.findViewById(R.id.yesButton);
        dialogPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dialogNegativeBtn = (Button)d.findViewById(R.id.noButton);
        dialogNegativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
                sellBtn.setEnabled(true);
                buyBtn.setEnabled(true);
            }
        });
        d.show();
        Log.d("createDialog","End createDialog");
    }
}