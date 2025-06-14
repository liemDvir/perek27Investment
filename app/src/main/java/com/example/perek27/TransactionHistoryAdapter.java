package com.example.perek27;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.postViewHolder> {
    private Context context;
    private OnItemClickListener listener;
    private ArrayList<Transaction> arrayList;

    public TransactionHistoryAdapter(Context context, ArrayList<Transaction> arrayList, OnItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    // קונסטרקטור בלי Listener
    public TransactionHistoryAdapter(Context context, ArrayList<Transaction> arrayList) {
        this(context, arrayList, null);
    }
    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_in_transaction_history,null);
        return new postViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull postViewHolder holder, int position)
    {
        Transaction transaction = arrayList.get(position);
        holder.stockSymbol.setText(transaction.getStockName());
        if (transaction.getMoneyInvested() < 0)
        {
            holder.amountOfStock.setTextColor(Color.RED);
            holder.amountOfStock.setText("-" + transaction.getMoneyInvested() );

        }else if(transaction.getMoneyInvested() > 0)
        {
            holder.amountOfStock.setTextColor(Color.GREEN);
            holder.amountOfStock.setText("+" + transaction.getMoneyInvested());
        }else {

            holder.amountOfStock.setTextColor(Color.WHITE);
            holder.amountOfStock.setText("" +transaction.getMoneyInvested());
        }

        holder.dateOfTransaction.setText(transaction.getTransactionTime()+"");


        holder.itemView.setOnClickListener(null);



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class postViewHolder extends RecyclerView.ViewHolder
    {
        TextView stockSymbol, amountOfStock, dateOfTransaction;
        public postViewHolder(@NonNull View itemView) {
            super(itemView);
            stockSymbol = itemView.findViewById(R.id.stockxml);
            amountOfStock = itemView.findViewById(R.id.pricexml);
            dateOfTransaction = itemView.findViewById(R.id.datexml);

        }

    }

}

