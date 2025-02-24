package com.example.perek27;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class stockAdapter extends RecyclerView.Adapter<postAdapter.postViewHolder>
{

    private Context context;
    private ArrayList<stock> arrayList;

    public stockAdapter(Context newContext, ArrayList<stock> newArrayList)
    {
        this.context = newContext;
        this.arrayList = newArrayList;
    }
    public  stockAdapter()
    {

    }

    @NonNull
    @Override
    public postAdapter.postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rowinstockhistory,null);
        return new postAdapter.postViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postAdapter.postViewHolder holder, int position)
    {
        stock stock = arrayList.get(position);

        holder.typeOfStock.setText(stock.getTypeOfStock());
        holder.amountOfStock.setText(stock.getMoneyInvested()+ "");


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class stockViewHolder extends RecyclerView.ViewHolder
    {
            TextView stockInvested, amountOfMoney;
        public stockViewHolder(@NonNull View itemView) {
            super(itemView);
            stockInvested = itemView.findViewById(R.id.amountOfMoney);
            amountOfMoney = itemView.findViewById(R.id.nameOfStock);


        }
    }
}
