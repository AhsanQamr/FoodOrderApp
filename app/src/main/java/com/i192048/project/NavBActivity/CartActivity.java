package com.i192048.project.NavBActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.i192048.project.Adapters.CartAdapter;
import com.i192048.project.Final;
import com.i192048.project.Modals.Cart;
import com.i192048.project.Modals.Order;
import com.i192048.project.R;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CartActivity extends AppCompatActivity {

    private static final String ONESIGNAL_APP_ID = "d4b5463b-e6c5-4546-899c-f8157cab5ff3";

    CardView cardView;
    ImageView back;
    TextView itemTotal, total;
    RecyclerView cart_recycler;
    List<Cart> list = new ArrayList<>();;
    FirebaseFirestore db;
    String sum = "0";
    MaterialButton checkout;
    SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //cardView = findViewById(R.id.just_touch);
        cart_recycler = findViewById(R.id.cart_recycler);
        back = findViewById(R.id.back);
        db = FirebaseFirestore.getInstance();
        itemTotal = findViewById(R.id.item_total);
        total = findViewById(R.id.total);
        checkout = findViewById(R.id.checkout);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                startActivity(getIntent());
            }
        });


        OneSignalSetup();


        cart_recycler.setLayoutManager(new LinearLayoutManager(this));

        fetchDataFromCart();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("MyData"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(total.getText().toString().equals("0") || list.size()==0) {
                    Toast.makeText(CartActivity.this, "Cart is empty, Please refresh in case of glitch ", Toast.LENGTH_SHORT).show();
                    return;
                }
                String deviceID = OneSignal.getDeviceState().getUserId();
                try{
                    OneSignal.postNotification(new JSONObject("{'contents': {'en':'Your Order has been confirmed\n 45 min's Delivery time'}, 'include_player_ids': ['" + deviceID + "']}"), null);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                addToOrders();
                CartAdapter adapter = new CartAdapter(list, CartActivity.this);
                for(int i=0;i<list.size();i++){
                    adapter.deleteCartFromFirebase(list.get(i).getName());
                }
                finish();
                startActivity(new Intent(CartActivity.this, Final.class));
            }
        });



    }



    private void fetchDataFromCart(){
        // fetch data from subdocument of cart
        db.collection("Cart").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).collection("UserCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    list = task.getResult().toObjects(Cart.class);
                    CartAdapter adapter = new CartAdapter(list, CartActivity.this);
                    cart_recycler.setAdapter(adapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CartActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sum = intent.getStringExtra("sum");
            System.out.println("Sum received: "+sum);
            itemTotal.setText("Rs: "+sum);
            calculateTotal(sum);

        }
    };

    private void calculateTotal(String value){
        int total = 0;
        int deliveryCharge = 30;
        int gst = 17;
        if(Integer.parseInt(value) == 0 ){
            total = 0;
        }
        else
            total += Integer.parseInt(value) + deliveryCharge + (gst * Integer.parseInt(value)/100);

        this.total.setText("Rs: "+total);


    }



    private void addToOrders(){
        Map<String,Object> orders = new HashMap<>();
        String id = generateRandom();
        String date = java.text.DateFormat.getDateTimeInstance().format(java.util.Calendar.getInstance().getTime());
        String status = "Pending";
        orders.put("id",id);
        orders.put("total",total.getText().toString());
        orders.put("status",status);
        orders.put("date", date);
        db.collection("Orders").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).collection("UserOrders").document(id).set(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CartActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CartActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateRandom(){
        String random = UUID.randomUUID().toString();
        String id = random.substring(0,4);
        return id;
    }

    private void OneSignalSetup(){
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        //OneSignal.setNotificationOpenedHandler(new MyNotificationOpenedHandler(this));
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.promptForPushNotifications();

    }

    public class MyNotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {

        private Application application;
        public MyNotificationOpenedHandler(Application application) {
            this.application = application;
        }

        @Override
        public void notificationOpened(OSNotificationOpenedResult result) {
            JSONObject data = result.getNotification().getAdditionalData();
            if (data != null) {
                Intent intent = new Intent(application, CartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                application.startActivity(intent);
            }
        }
    }




}