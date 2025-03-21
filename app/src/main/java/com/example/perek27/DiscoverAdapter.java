package com.example.perek27;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.stockViewHolder>
{

    private Context context;
    private ArrayList<Stock> arrayList;

    private OnItemClickListener listener;

    public DiscoverAdapter(Context newContext, ArrayList<Stock> newArrayList, OnItemClickListener newListener)
    {
        this.context = newContext;
        this.arrayList = newArrayList;
        this.listener = newListener;
    }

    @NonNull
    @Override
    public DiscoverAdapter.stockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_in_discover,null);
        return new DiscoverAdapter.stockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverAdapter.stockViewHolder holder, int position)
    {
        Stock stock = arrayList.get(position);

        //holder.amountOfMoney.setText(stock.getCurrentValue() + "");
        holder.amountOfMoney.setText(stock.getAmountOfStock()+"");
        holder.stockInvested.setText(stock.getStockName());

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
