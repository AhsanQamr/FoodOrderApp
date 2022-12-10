package com.i192048.project.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.i192048.project.Modals.Favorites;
import com.i192048.project.Modals.FoodModal;
import com.i192048.project.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private List<Favorites> favoritesList;
    private Context context;

    public FavoritesAdapter(List<Favorites> favoritesList, Context context) {
        this.favoritesList = favoritesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Favorites favorites = favoritesList.get(position);
        holder.name.setText(favorites.getName());
        holder.price.setText(favorites.getPrice());
        Picasso.get().load(favorites.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, price;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.fav_name);
            price = itemView.findViewById(R.id.fav_price);
            image = itemView.findViewById(R.id.fav_image);
        }
    }
}

