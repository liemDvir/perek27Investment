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

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.stockViewHolder>
{

    private Context context;
    public ArrayList<StockInfo> arrayList;

    private OnItemClickListener listener;

    public DiscoverAdapter(Context newContext, ArrayList<StockInfo> newArrayList, OnItemClickListener newListener)
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
        StockInfo stock = arrayList.get(position);

        if (stock.getStockName().length() > 27)
        {
            String newName = stock.getStockName().substring(0,27);
            holder.stockInvested.setText(newName + "...");
        }else {
            holder.stockInvested.setText(stock.getStockName());
        }

        holder.stockSymbol.setText(stock.getStockSymbol());

        holder.stockValueXml.setText(stock.getPrice() +" USD");

        String currentChangePrecString = stock.getChange_percent();
        if(currentChangePrecString != null){
            currentChangePrecString = currentChangePrecString.substring(0,currentChangePrecString.length()-1);
            float currentChangePrec =Float.parseFloat(currentChangePrecString);
            if(currentChangePrec>0)
            {
                holder.stockPrec.setTextColor(Color.GREEN);
                holder.stockPrec.setText("+"+stock.getChange_percent());

            }else if (currentChangePrec<0){
                holder.stockPrec.setTextColor(Color.RED);
                holder.stockPrec.setText(stock.getChange_percent());
            }else {
                holder.stockPrec.setTextColor(Color.WHITE);
                holder.stockPrec.setText(stock.getChange_percent());
            }
        }

        holder.itemView.setOnClickListener(view -> listener.onItemClick(stock));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class stockViewHolder extends RecyclerView.ViewHolder
    {
            TextView stockInvested, stockSymbol, stockValueXml, stockPrec;

        public stockViewHolder(@NonNull View itemView) {
            super(itemView);
            stockSymbol = itemView.findViewById(R.id.stockSymbol);
            stockInvested = itemView.findViewById(R.id.nameOfStock);
            stockValueXml = itemView.findViewById(R.id.stockValue);
            stockPrec = itemView.findViewById(R.id.stockprecTxtView);





        }
    }
}
