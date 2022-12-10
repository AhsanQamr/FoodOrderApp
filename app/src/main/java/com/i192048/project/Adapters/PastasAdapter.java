package com.i192048.project.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.i192048.project.DetailsActivity;
import com.i192048.project.Modals.FoodModal;
import com.i192048.project.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PastasAdapter extends RecyclerView.Adapter<PastasAdapter.PastasViewHolder> {
    List<FoodModal> ls;
    Context context;
    boolean liked = false;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> data = new HashMap<>();

    public PastasAdapter(List<FoodModal> ls, Context context) {
        this.ls = ls;
        this.context = context;
    }

    @NonNull
    @Override
    public PastasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.foodview,parent,false);
        return new PastasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastasAdapter.PastasViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.food_name.setText(ls.get(position).getF_name());
        holder.food_price.setText(ls.get(position).getF_price());
        //holder.food_description.setText(ls.get(position).getB_description());
        Glide.with(context).load(ls.get(position).getF_image()).into(holder.food_image);

        db.collection("Favorites").document(mAuth.getCurrentUser().getUid()).collection("UserFavorites").whereEqualTo("f_name", ls.get(position).getF_name()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        holder.likeOnCard.setImageResource(R.drawable.favourite);
                    } else {
                        holder.likeOnCard.setImageResource(R.drawable.emptyfav);
                    }
                }
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("name",ls.get(position).getF_name());
                intent.putExtra("price",ls.get(position).getF_price());
                intent.putExtra("image",ls.get(position).getF_image());
                intent.putExtra("description",ls.get(position).getF_description());
                intent.putExtra("price2",ls.get(position).getF_price2());
                intent.putExtra("price3",ls.get(position).getF_price3());
                context.startActivity(intent);
            }
        });

        holder.likeOnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Favorites").document(mAuth.getCurrentUser().getUid()).collection("UserFavorites").whereEqualTo("f_name", ls.get(position).getF_name()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                holder.likeOnCard.setImageResource(R.drawable.favourite);
                                addToFavorites(ls.get(position).getF_name(), ls.get(position).getF_price(), ls.get(position).getF_image());
                            } else {
                                holder.likeOnCard.setImageResource(R.drawable.emptyfav);
                                removeFromFavorites(ls.get(position).getF_name());
                            }
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class PastasViewHolder extends RecyclerView.ViewHolder {
        ImageView food_image;
        TextView food_name,food_price;
        CardView cardView;
        ImageView likeOnCard;
        public PastasViewHolder(@NonNull View itemView) {
            super(itemView);
            food_image = itemView.findViewById(R.id.food_image);
            food_name = itemView.findViewById(R.id.food_name);
            food_price = itemView.findViewById(R.id.food_price);
            cardView = itemView.findViewById(R.id.food_card);
            likeOnCard = itemView.findViewById(R.id.like_on_card);
        }
    }

    private void addToFavorites(String foodName,String foodPrice,String foodImage) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        data.put("f_name",foodName );
        data.put("f_price", foodPrice);
        data.put("f_image", foodImage);

        db.collection("Favorites").document(mAuth.getCurrentUser().getUid()).collection("UserFavorites").document(foodName).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to add to Favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFromFavorites(String foodName) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db.collection("Favorites").document(mAuth.getCurrentUser().getUid()).collection("UserFavorites").document(foodName).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to remove from Favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

