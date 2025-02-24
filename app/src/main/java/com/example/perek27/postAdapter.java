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

public class postAdapter extends RecyclerView.Adapter<postAdapter.postViewHolder> {
    private Context context;
    private ArrayList<Post> arrayList;

    public postAdapter(Context newContext, ArrayList<Post> newArrayList)
    {
        this.context = newContext;
        this.arrayList = newArrayList;
    }
    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rowinstockhistory,null);
        return new postViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postViewHolder holder, int position)
    {
        Post post = arrayList.get(position);

        if (post.getIsBuy()) // if true means he sell the stock, else he bought
        {
            holder.amountOfStock.setText("+" + post.getMoneyInvested());
            holder.amountOfStock.setTextColor(Color.parseColor("#4CAF50"));
            holder.typeOfStock.setTextColor(Color.parseColor("#4CAF50"));
        } else if (!post.getIsBuy())
        {
            int i = post.getMoneyInvested();
            holder.amountOfStock.setText("-" + post.getMoneyInvested());
            holder.amountOfStock.setTextColor(Color.parseColor("#871919"));
            holder.typeOfStock.setTextColor(Color.parseColor("#871919"));

         }
        holder.typeOfStock.setText(post.getTypeOfStock());


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

