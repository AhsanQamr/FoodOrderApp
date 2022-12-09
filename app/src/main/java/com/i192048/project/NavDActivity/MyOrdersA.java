package com.i192048.project.NavDActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.i192048.project.Adapters.OrderAdapter;
import com.i192048.project.Modals.Order;
import com.i192048.project.NavBActivity.CartActivity;
import com.i192048.project.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyOrdersA extends AppCompatActivity {

    ImageView back;
    RecyclerView order_recycler;
    List<Order> list;
    FirebaseFirestore db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        back = findViewById(R.id.back);
        order_recycler = findViewById(R.id.my_orders_recycler);
        order_recycler.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();

        fetchOrderFromCart();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void fetchOrderFromCart(){
        list = new ArrayList<>();
        db.collection("Orders").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .collection("UserOrders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                        Order order = documentSnapshot.toObject(Order.class);
                        list.add(order);
                    }
                    order_recycler.setAdapter(new OrderAdapter(list, MyOrdersA.this));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyOrdersA.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}