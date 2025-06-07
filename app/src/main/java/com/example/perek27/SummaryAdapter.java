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

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {

    private Context context;
    public ArrayList<StockInfo> arrayList;

    public SummaryAdapter.SummaryViewHolder mViewHolder;

    private OnItemClickListener listener;

    public SummaryAdapter(Context newContext, ArrayList<StockInfo> newArrayList, OnItemClickListener newListener)
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
        mViewHolder = new SummaryAdapter.SummaryViewHolder(view);
        return  mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryAdapter.SummaryViewHolder holder, int position) {
        StockInfo stock = arrayList.get(position);
        holder.nameOfStock.setText(stock.getStockName());
        holder.AmountOfSHaresAndSymbol.setText(stock.getAmountOfStock()+ " " + stock.getStockSymbol());

        if (stock.getChange_percent() != null)
        {
            String currentChangePrecString = stock.getChange_percent().substring(0,stock.getChange_percent().length()-1);
            float currentChangePrec =Float.parseFloat(currentChangePrecString);
            if (currentChangePrec < 0)
            {
                holder.changePrec.setTextColor(Color.RED);
                holder.changePrec.setText("-" + stock.getChange());

            }else if(currentChangePrec > 0)
            {
                holder.changePrec.setTextColor(Color.GREEN);
                holder.changePrec.setText("+" + stock.getChange());
            }else {

                holder.changePrec.setTextColor(Color.WHITE);
                holder.changePrec.setText("" +stock.getChange());
            }

        } else  {
            holder.changePrec.setText("  ");
        }
        holder.totalValue.setText(String.valueOf(stock.getAmountOfStock()*stock.getPrice()));


        holder.itemView.setOnClickListener(view -> listener.onItemClick(stock));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  class SummaryViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameOfStock, totalValue, AmountOfSHaresAndSymbol,changePrec;
        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);

            nameOfStock = itemView.findViewById(R.id.stockNameTV);
            totalValue = itemView.findViewById(R.id.totalValueTV);
            AmountOfSHaresAndSymbol = itemView.findViewById(R.id.stockAmountSymbolTV);
            changePrec = itemView.findViewById(R.id.changePercentTV);



        }
    }
}

