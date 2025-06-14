package com.example.perek27;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;


public class StockActionActivity extends AppCompatActivity implements View.OnClickListener {

    StockModel mStockModel;
    DiscoverActivity discoverActivity;

    EditText  amountOfStock;
    TextView stockSymbolTV,stockCurrentValueTV,stockPrecTV,stockNameTV,stockOwmByUserTV,amountOfMoneyInvestedTV ;
    Button buyBtn, sellBtn, goBackBtn,oneDayBtn,oneWeekBtn,threeMonthsBtn,sixMonthsBtn,oneYearBtn;
    ArrayList<Stock> currentStockArrList;
    Stock currentStock;

    String StockSymbol;

    Dialog d;

    Button dialogPositiveBtn, dialogNegativeBtn;


    String amountOfStockOwnByUser;

    private Observer mStockActionActivityObserver = new StockActionActivityObserver();

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
        mStockModel.register(mStockActionActivityObserver);

        Intent intent = getIntent();
        StockSymbol = intent.getStringExtra("StockSymbol");
        String stockName = intent.getStringExtra("StockName");
        amountOfStockOwnByUser = intent.getStringExtra("amountOfShares") +"";

        goBackBtn = (Button)findViewById(R.id.goBackXml);
        goBackBtn.setOnClickListener(this);

        stockCurrentValueTV = (TextView)findViewById(R.id.stockValueTextView);
        stockCurrentValueTV.setOnClickListener(this);

        stockNameTV = (TextView)findViewById(R.id.typeOfStockTextView);
        stockNameTV.setOnClickListener(this);
        stockNameTV.setText(stockName);


        stockPrecTV = (TextView)findViewById(R.id.stockPrecTextView);
        stockPrecTV.setOnClickListener(this);


        stockSymbolTV = (TextView)findViewById(R.id.stockSymbolTextView);
        stockSymbolTV.setOnClickListener(this);
        stockSymbolTV.setText(StockSymbol);

        amountOfStock = (EditText)findViewById(R.id.amountMoney);
        amountOfStock.setOnClickListener(this);

        buyBtn = (Button)findViewById(R.id.buyButton);
        buyBtn.setOnClickListener(this);

        sellBtn = (Button)findViewById(R.id.sellButton);
        sellBtn.setOnClickListener(this);

        stockOwmByUserTV = (TextView)findViewById(R.id.currentAmountOfSharexml);
        stockOwmByUserTV.setOnClickListener(this);

        stockSymbolTV = (TextView)findViewById(R.id.stockSymbolTextView);
        stockSymbolTV.setOnClickListener(this);

        amountOfMoneyInvestedTV = (TextView)findViewById(R.id.currentAmountMoneyInSharesXml);
        amountOfMoneyInvestedTV.setOnClickListener(this);



        graphView = (GraphView)findViewById(R.id.graphViewXml);
        graphView.setOnClickListener(this);
        // maximum and minimum value y
        graphView.getViewport().setYAxisBoundsManual(true);
        // maximum and minimum value x
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);


        mStockModel.GetStockInfo(StockSymbol);

        mStockModel.GetDailyReportOfSymbol((String) stockSymbolTV.getText());


    }

    @Override
    public void onClick(View view) {
         if (view == buyBtn)
         {
             sellBtn.setEnabled(false);
             buyBtn.setEnabled(false);
             if(amountOfStock.getText().toString().isEmpty() && !amountOfStock.getText().toString().equals("enter the amount of money to invest"))
             {
                Toast.makeText(StockActionActivity.this, "please enter the amount of money to invest", Toast.LENGTH_LONG).show();
                 sellBtn.setEnabled(true);
                 buyBtn.setEnabled(true);
             }
             else if ( 0 >= Integer.parseInt(amountOfStock.getText().toString()))
             {
                Toast.makeText(StockActionActivity.this, "please enter the positive number", Toast.LENGTH_LONG).show();
                 sellBtn.setEnabled(true);
                 buyBtn.setEnabled(true);
             }
             else{
                 createDialog(buyBtn);
             }

         } else if (view == sellBtn) {
             sellBtn.setEnabled(false);
             buyBtn.setEnabled(false);
             if(amountOfStock.getText().toString().isEmpty() && !amountOfStock.getText().toString().equals("enter the amount of money to invest"))
             {
                 Toast.makeText(StockActionActivity.this, "please enter the amount of money to invest", Toast.LENGTH_LONG).show();
                 sellBtn.setEnabled(true);
                 buyBtn.setEnabled(true);
             }
             else if ( 0 >= Integer.parseInt(amountOfStock.getText().toString()))
             {
                 Toast.makeText(StockActionActivity.this, "please enter the positive number", Toast.LENGTH_LONG).show();
                 sellBtn.setEnabled(true);
                 buyBtn.setEnabled(true);
             }
             else{
                 createDialog(sellBtn);
             }

         } else if (view == goBackBtn)
         {
             finish();
         }

    }
    public void createDialog(View view)
    {
        Log.d("createDialog","Start createDialog");
        d = new Dialog(this);
        if(view ==buyBtn) {
            d.setContentView(R.layout.dialog_verification_action);
            d.setCancelable(true);
            dialogPositiveBtn = (Button) d.findViewById(R.id.yesButton);
            dialogPositiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    d.dismiss();
                    mStockModel.BuyStock(StockSymbol,Integer.valueOf(amountOfStock.getText().toString()));


                }
            });
        } else if (view == sellBtn) {
            d.setContentView(R.layout.dialog_verification_action);
            d.setCancelable(true);
            dialogPositiveBtn = (Button) d.findViewById(R.id.yesButton);
            dialogPositiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    d.dismiss();
                    mStockModel.SellStock(StockSymbol,Integer.valueOf(amountOfStock.getText().toString()));
                }
            });
        }
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

    public class StockActionActivityObserver implements Observer{

        @Override
        public void SignInWithEmailAndPasswordCompleate(@NonNull Task<AuthResult> task) {

        }

        @Override
        public void GetAllStocksInvested(List<StockInfo> stockInvested) {

        }

        @Override
        public void GetTransactionHistory(ArrayList<Transaction> transactionsList) {

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

        @SuppressLint("SetTextI18n")
        @Override
        public void OnStockInfoUpdate(StockInfo stockInf) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // ✅ UI operations here (e.g., update TextView, Toast)
                    StockInfo stock = stockInf;
                    stockNameTV.setText(stockInf.getStockName());

                    stockSymbolTV.setTextColor(Color.GRAY);
                    stockNameTV.setTextColor(Color.WHITE);

                    stockCurrentValueTV.setText((stockInf.getPrice())+"");

                    stockSymbolTV.setText("(" + stockInf.getStockSymbol()+ ")");

                    String currentChangePrecString = stockInf.getChange_percent();
                    if (currentChangePrecString != null)
                    {
                        try {
                            currentChangePrecString = currentChangePrecString.substring(0,currentChangePrecString.length()-1);

                            float currentChangePrec =Float.parseFloat(currentChangePrecString);
                            if(currentChangePrec>0)
                            {
                                stockPrecTV.setTextColor(Color.GREEN);
                                stockPrecTV.setText("+" + stockInf.getChange_percent());

                            }else if (currentChangePrec<0){
                                stockPrecTV.setTextColor(Color.RED);
                                stockPrecTV.setText(stockInf.getChange_percent());
                            }else {
                                stockPrecTV.setTextColor(Color.WHITE);
                                stockPrecTV.setText(stockInf.getChange_percent());
                            }
                        } catch (Exception e) {
                            Log.e("StockActionActivity", e.getMessage());
                        }

                    }else {
                        Log.d("error", "run:show error ");
                    }

                    //round number to 2 decimal after dot
                    stockCurrentValueTV.setText(String.format("%.2f", stockInf.getPrice()) + " USD");
                    if (amountOfStockOwnByUser != null)
                    {
                        stockOwmByUserTV.setText(amountOfStockOwnByUser);
                        try {
                            if (amountOfStockOwnByUser != null && !amountOfStockOwnByUser.trim().isEmpty()) {
                                double shares = Double.parseDouble(amountOfStockOwnByUser.trim());
                                double price = stockInf.getPrice();
                                double totalValue = shares * price;
                                amountOfMoneyInvestedTV.setText(String.format("%.2f USD", totalValue));
                            } else {
                                amountOfMoneyInvestedTV.setText("0.00 USD");
                            }
                        } catch (NumberFormatException e) {
                            amountOfMoneyInvestedTV.setText("Invalid share count");
                            Log.e("ERROR", "NumberFormatException: " + e.getMessage());
                        }

                    }


                }
            });

        }

        @Override
        public void GetDailyReportOfSymbolResults(List<StockInfo> stocksList) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                    for (int i = 0; i < stocksList.size(); i++) {
                        series.appendData(new DataPoint(i, stocksList.get(i).getPrice()), true, stocksList.size());
                    }
                    graphView.removeAllSeries();
                    graphView.addSeries(series);

                    series.setColor(Color.CYAN);
                    // Or another color to match your theme
                    series.setThickness(6);
                    series.setDrawBackground(true);
                    series.setBackgroundColor(Color.argb(50, 0, 255, 255));
                    series.setDrawDataPoints(true);
                    series.setDataPointsRadius(8);

                    graphView.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
                    graphView.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
                    graphView.getGridLabelRenderer().setGridColor(Color.DKGRAY);

                    graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                        @Override
                        public String formatLabel(double value, boolean isValueX) {
                            if (isValueX) {
                                return "Day " + ((int) value);
                            } else {
                                return String.format("%.2f", value);
                            }
                        }
                    });

                    graphView.getViewport().setMinX(0);
                    graphView.getViewport().setMaxX(stocksList.size() - 1);


                    int min = 0;
                    int max = 0;

                    for (StockInfo stock:stocksList)
                    {
                     if (stock.getPrice() > max) {
                         max = (int) stock.getPrice();
                     }
                     if ((min == 0) || (min > stock.getPrice()))
                     {
                         min =(int) stock.getPrice();
                     }

                    }

                    graphView.getViewport().setMaxY(max*1.1);
                    graphView.getViewport().setMinY(min*0.9);

                    graphView.setTitle("Daily Stock Price");
                    graphView.setTitleColor(Color.WHITE);
                    graphView.setTitleTextSize(48);


                }
            });
        }

        @Override
        public void OnBuyStockCompleted(Boolean success, String reason) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // ✅ UI operations here (e.g., update TextView, Toast)
                    Toast.makeText(StockActionActivity.this, reason, Toast.LENGTH_LONG).show();
                    sellBtn.setEnabled(true);
                    buyBtn.setEnabled(true);
                }
            });
        }

        @Override
        public void OnSellStockCompleted(Boolean success, String reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // ✅ UI operations here (e.g., update TextView, Toast)
                    Toast.makeText(StockActionActivity.this, reason, Toast.LENGTH_LONG).show();
                    sellBtn.setEnabled(true);
                    buyBtn.setEnabled(true);
                }
            });
        }

        @Override
        public void OnUpdateCashCompleted(Boolean success) {

        }
    }
}