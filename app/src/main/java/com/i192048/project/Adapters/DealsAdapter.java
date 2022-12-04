package com.i192048.project.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.i192048.project.Modals.FoodModal;
import com.i192048.project.R;

import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealsViewHolder> {
    List<FoodModal> ls;
    Context context;

    public DealsAdapter(List<FoodModal> ls, Context context) {
        this.ls = ls;
        this.context = context;
    }

    @NonNull
    @Override
    public DealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.foodview,parent,false);
        return new DealsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealsAdapter.DealsViewHolder holder, int position) {
        holder.food_name.setText(ls.get(position).getF_name());
        holder.food_price.setText(ls.get(position).getF_price());
        //holder.food_description.setText(ls.get(position).getB_description());
        Glide.with(context).load(ls.get(position).getF_image()).into(holder.food_image);
    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class DealsViewHolder extends RecyclerView.ViewHolder {
        ImageView food_image;
        TextView food_name,food_price;
        public DealsViewHolder(@NonNull View itemView) {
            super(itemView);
            food_image = itemView.findViewById(R.id.food_image);
            food_name = itemView.findViewById(R.id.food_name);
            food_price = itemView.findViewById(R.id.food_price);

        }
    }
}

