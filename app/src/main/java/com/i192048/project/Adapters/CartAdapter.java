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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.i192048.project.Modals.Cart;
import com.i192048.project.NavBActivity.CartActivity;
import com.i192048.project.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Cart> cartList;
    private Context context;


    public CartAdapter(List<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cart cart = cartList.get(position);
        holder.name.setText(cart.getName());
        holder.price.setText(cart.getPrice());
        holder.quantity.setText(cart.getQuantity());
        holder.size.setText(cart.getSize());
        //Picasso.get().load(cart.getImage()).into(holder.image);

        int count = getItemCount();
        int sum =0;
        for(int i=0;i<count;++i){
            sum += Integer.parseInt(cartList.get(i).getPrice().replaceAll("\\s",""));
        }
        System.out.println("Sum: " + sum);


        Intent intent = new Intent("MyData");
        intent.putExtra("sum", String.valueOf(sum));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartList.remove(position);
                deleteCartFromFirebase(cart.getName());
                notifyDataSetChanged();
                //LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity, size;
        ImageView image;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.food_name);
            price = itemView.findViewById(R.id.food_price);
            quantity = itemView.findViewById(R.id.food_quantity);
            size = itemView.findViewById(R.id.food_size);
            image = itemView.findViewById(R.id.food_image);
            delete = itemView.findViewById(R.id.remove_card);

        }
    }

    public void deleteCartFromFirebase(String name){
        FirebaseFirestore db;
        FirebaseAuth auth;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        // delete sub document from cart;

        db.collection("Cart").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("UserCart").document(name).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
