package com.example.perek27;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class stockAdapter extends RecyclerView.Adapter<stockAdapter.stockViewHolder>
{

    private Context context;
    private ArrayList<Stock> arrayList;

    private OnItemClickListener listener;

    public stockAdapter(Context newContext, ArrayList<Stock> newArrayList, OnItemClickListener newListener)
    {
        this.context = newContext;
        this.arrayList = newArrayList;
        this.listener = newListener;
    }

    @NonNull
    @Override
    public stockAdapter.stockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rowindiscover,null);
        return new stockAdapter.stockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull stockAdapter.stockViewHolder holder, int position)
    {
        Stock stock = arrayList.get(position);

        holder.amountOfMoney.setText(stock.getValue() + "");
        holder.stockInvested.setText(stock.getTypeOfStock());

        holder.itemView.setOnClickListener(view -> listener.onItemClick(stock));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class stockViewHolder extends RecyclerView.ViewHolder
    {
            TextView stockInvested, amountOfMoney;

        public stockViewHolder(@NonNull View itemView) {
            super(itemView);
            amountOfMoney = itemView.findViewById(R.id.amountOfMoney);
            stockInvested = itemView.findViewById(R.id.nameOfStock);




        }
    }
}
