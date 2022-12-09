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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.i192048.project.DetailsActivity;
import com.i192048.project.Modals.FoodModal;
import com.i192048.project.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BurgerAdapter extends RecyclerView.Adapter<BurgerAdapter.BurgerViewHolder> {
    List<FoodModal> ls;
    Context context;
    boolean liked = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> data = new HashMap<>();
    public BurgerAdapter(List<FoodModal> ls, Context context) {
        this.ls = ls;
        this.context = context;
    }

    @NonNull
    @Override
    public BurgerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.foodview,parent,false);
        return new BurgerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BurgerAdapter.BurgerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.food_name.setText(ls.get(position).getF_name());
        holder.food_price.setText(ls.get(position).getF_price());
        //holder.food_description.setText(ls.get(position).getB_description());
        Glide.with(context).load(ls.get(position).getF_image()).into(holder.food_image);

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
                if(liked == false){
                    holder.likeOnCard.setImageResource(R.drawable.favourite);
                    liked = true;
                }
                else{
                    holder.likeOnCard.setImageResource(R.drawable.emptyfav);
                    liked = false;
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class BurgerViewHolder extends RecyclerView.ViewHolder {
        ImageView food_image;
        TextView food_name,food_price;
        ImageView likeOnCard;
        CardView cardView;
        public BurgerViewHolder(@NonNull View itemView) {
            super(itemView);
            food_image = itemView.findViewById(R.id.food_image);
            food_name = itemView.findViewById(R.id.food_name);
            food_price = itemView.findViewById(R.id.food_price);
            likeOnCard = itemView.findViewById(R.id.like_on_card);
            cardView = itemView.findViewById(R.id.food_card);

        }
    }

    private String fetchFavorite(){
        // fetch data from maps stored in documents and add them to list
        final String[] favorite = new String[1];
        db.collection("Foods").document("Burgers").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        data = document.getData();
                        assert data != null;
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            Map<String, Object> map = (Map<String, Object>) entry.getValue();
                            favorite[0] = (String) map.get("Favorite");
                            System.out.println(favorite[0]);
                            //list.add(new FoodModal(map.get("Name").toString(), map.get("Price").toString(),map.get("Description").toString() ,map.get("Image").toString()));
                        }

                    }
                }
            }
        });
        return favorite[0];
    }


}

