package com.example.perek27;

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
    private ArrayList<TransactionHistory> arrayList;

    public TransactionHistoryAdapter(Context newContext, ArrayList<TransactionHistory> newArrayList)
    {
        this.context = newContext;
        this.arrayList = newArrayList;
    }
    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_in_transaction_history,null);
        return new postViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postViewHolder holder, int position)
    {
        TransactionHistory transactionHistory = arrayList.get(position);

        if (transactionHistory.getIsBuy()) // if true means he sell the stock, else he bought
        {
            holder.amountOfStock.setText("+" + transactionHistory.getMoneyInvested());
            holder.amountOfStock.setTextColor(Color.parseColor("#4CAF50"));
            holder.typeOfStock.setTextColor(Color.parseColor("#4CAF50"));
        } else if (!transactionHistory.getIsBuy())
        {
            holder.amountOfStock.setText("-" + transactionHistory.getMoneyInvested());
            holder.amountOfStock.setTextColor(Color.parseColor("#871919"));
            holder.typeOfStock.setTextColor(Color.parseColor("#871919"));

         }
        holder.typeOfStock.setText(transactionHistory.getTypeOfStock());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class postViewHolder extends RecyclerView.ViewHolder
    {
        TextView typeOfStock, amountOfStock;
        public postViewHolder(@NonNull View itemView) {
            super(itemView);
            typeOfStock = itemView.findViewById(R.id.stockxml);
            amountOfStock = itemView.findViewById(R.id.pricexml);

        }

    }

}

