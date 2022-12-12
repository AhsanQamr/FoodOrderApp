package com.i192048.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.i192048.project.Adapters.BurgerAdapter;
import com.i192048.project.Fragments.BurgerFragment;
import com.i192048.project.Modals.Cart;
import com.i192048.project.Modals.FoodModal;
import com.i192048.project.NavBActivity.CartActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    ImageView min,max;
    TextView quantity;
    TextView price;
    String burgerSize;
    int q=0;
    ImageView back;
    String b_name,b_price,b_image,b_description,b_price2,b_price3;
    TextView name,description;
    ImageView image;
    MaterialButton add_to_cart;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        spinner = findViewById(R.id.spinner);
        min = findViewById(R.id.min);
        max = findViewById(R.id.max);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        back = findViewById(R.id.back);
        name = findViewById(R.id.food_name);
        description = findViewById(R.id.food_description);
        image = findViewById(R.id.food_image);
        add_to_cart = findViewById(R.id.add_to_cart);

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  //addDataToCart();

                addToCart();

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getData();
        setData();

        spinner.setOnItemSelectedListener(this);
        String[] size = getResources().getStringArray(R.array.selectSize);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,size);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        q = Integer.parseInt(quantity.getText().toString());
        int p = 0;


        int finalP = p;
        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(q>1){
                    q--;
                    quantity.setText(String.valueOf(q));
                    if(spinner.getSelectedItem().toString().equals("S")){
                        int myTotalPrice = Integer.parseInt(b_price) * q;
                        price.setText("Rs. "+myTotalPrice);
                    }
                    else if(spinner.getSelectedItem().toString().equals("M")){
                        int myTotalPrice = Integer.parseInt(b_price2) * q;
                        price.setText("Rs. "+myTotalPrice);
                    }
                    else if(spinner.getSelectedItem().toString().equals("L")){
                        int myTotalPrice = Integer.parseInt(b_price3) * q;
                        price.setText("Rs. "+myTotalPrice);
                    }

                    //removePrice();
                }

            }
        });

        max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(q<10){
                    q++;
                    quantity.setText(String.valueOf(q));
                    if(spinner.getSelectedItem().toString().equals("S")){
                        int myTotalPrice = Integer.parseInt(b_price) * q;
                        price.setText("Rs. "+myTotalPrice);
                    }
                    else if(spinner.getSelectedItem().toString().equals("M")){
                        int myTotalPrice = Integer.parseInt(b_price2) * q;
                        price.setText("Rs. "+myTotalPrice);
                    }
                    else if(spinner.getSelectedItem().toString().equals("L")){
                        int myTotalPrice = Integer.parseInt(b_price3) * q;
                        price.setText("Rs. "+myTotalPrice);
                    }
                    //addPrice();
                }

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinner){
            burgerSize = adapterView.getItemAtPosition(i).toString();
            if(burgerSize.equals("S")){
                price.setText("Rs "+b_price);
            }
            else if(burgerSize.equals("M")){
                price.setText("Rs "+b_price2);
            }
            else if(burgerSize.equals("L")){
                price.setText("Rs "+b_price3);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getData(){
        Intent intent = getIntent();
        b_name = intent.getStringExtra("name");
        b_image = intent.getStringExtra("image");
        b_description = intent.getStringExtra("description");
        b_price = intent.getStringExtra("price");
        b_price2 = intent.getStringExtra("price2");
        b_price3 = intent.getStringExtra("price3");

    }

    private void setData(){
        //image.setImageResource(b_image);
        Picasso.get().load(b_image).into(image);
        name.setText(b_name);
        description.setText(b_description);
        price.setText(b_price);
    }


    private void addToCart(){
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        String myPrice = price.getText().toString().substring(3);

        if(myPrice.equals("null")){
            Toast.makeText(this, "Please Select Valid Item", Toast.LENGTH_SHORT).show();
            return;
        }
        Snackbar.make(add_to_cart,"Added to Cart",Snackbar.LENGTH_SHORT).show();
        //Snackbar.make(view, "Added to Cart", Snackbar.LENGTH_LONG).show();
        Map<String,Object> cart = new HashMap<>();
        cart.put("name",b_name);
        cart.put("price",myPrice);
        cart.put("quantity",quantity.getText().toString());
        cart.put("image",b_image);
        cart.put("description",b_description);
        cart.put("size",spinner.getSelectedItem().toString());

        db.collection("Cart").document(uid).collection("UserCart").document(b_name).set(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(DetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailsActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }




}