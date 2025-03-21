package com.example.perek27;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {

    private Context context;
    private ArrayList<Stock> arrayList;

    private OnItemClickListener listener;

    public SummaryAdapter(Context newContext, ArrayList<Stock> newArrayList, OnItemClickListener newListener)
    {
        this.context = newContext;
        this.arrayList = newArrayList;
        this.listener = newListener;
    }


    @NonNull
    @Override
    public SummaryAdapter.SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_in_summary,null);
        return new SummaryAdapter.SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryAdapter.SummaryViewHolder holder, int position) {
        Stock stock = arrayList.get(position);

        holder.nameOfStock.setText(stock.getStockName());
        //holder.priceOfStock.setText(String.valueOf(stock.getCurrentValue()));
        holder.priceOfStock.setText(String.valueOf(500));
        holder.AmountOfSHares.setText(String.valueOf(stock.getAmountOfStock()));

        holder.itemView.setOnClickListener(view -> listener.onItemClick(stock));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  class SummaryViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameOfStock, priceOfStock, AmountOfSHares;
        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);

            nameOfStock = (TextView)itemView.findViewById(R.id.nameOfStockXml);
            priceOfStock = (TextView)itemView.findViewById(R.id.amountOfMoneyXml);
            AmountOfSHares = (TextView)itemView.findViewById(R.id.numOfSharesXml);
        }
    }
}

