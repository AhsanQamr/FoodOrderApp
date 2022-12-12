package com.i192048.project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.i192048.project.Adapters.MainScreenAdapter;
import com.i192048.project.Fragments.AddonsFragment;
import com.i192048.project.Fragments.BurgerFragment;
import com.i192048.project.Fragments.DealsFragment;
import com.i192048.project.Fragments.ParathasFragment;
import com.i192048.project.Fragments.PastasFragment;
import com.i192048.project.Fragments.PizzaFragment;
import com.i192048.project.Fragments.ShwarmaFragment;
import com.i192048.project.Modals.User;
import com.i192048.project.NavBActivity.CartActivity;
import com.i192048.project.NavBActivity.FavoritesActivity;
import com.i192048.project.NavBActivity.SearchActivity;
import com.i192048.project.NavDActivity.ChangePasswordA;
import com.i192048.project.NavDActivity.ContactUsA;
import com.i192048.project.NavDActivity.EditProfileA;
import com.i192048.project.NavDActivity.MyOrdersA;
import com.i192048.project.NavDActivity.SeeProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Base64;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainScreen extends AppCompatActivity  {

    ImageView menu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    TabLayout tabLayout;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    TextView YourUserName, YourAddress;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String userID;
    View headerView;
    User user;

    ImageView userDP;

    String imageUri2 = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        /*  Hooks */
        menu = findViewById(R.id.menu_icon);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        YourAddress = findViewById(R.id.your_address);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        headerView = navigationView.getHeaderView(0);
        YourUserName = (TextView) headerView.findViewById(R.id.your_user_name);
        user = User.getInstance();
        userDP = findViewById(R.id.userImage);
        /* Hooks End */

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.bringToFront();

        readDataFromDB();
        setViewPagerWithTab();
        setBottomNavView();
        setDrawerNavView();
        setUserDP();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        userDP = (ImageView) headerview.findViewById(R.id.userImage);
        //Picasso.get().load(imageUri2).into(userDP);


        //Log.d("Shaheer", imageUri2);

        //Picasso.get().load(imageUri2).into(userDP);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                    drawerLayout.closeDrawer(Gravity.LEFT);
                else
                    drawerLayout.openDrawer(Gravity.LEFT);


                if (userDP!=null) {
                    Picasso.get().load(imageUri2).into(userDP);
                }

                else {
                    Log.d("Shaheer", "user dp is null");
                }
            }
        });

    }

    // Fetch and set user dp
    private void setUserDP() {
        String email1 = "shaheerasif13@gmail.com";
        Log.d("Shaheer", "Sending email to server: " + email1);
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("email",email1)
                .build();

        //Request request = new Request.Builder().url("http://192.168.10.5:5000/getImage?email=" + email1).addHeader("Connection", "close").build();
        Request request = new Request.Builder()
                .url("http://172.17.54.120:5000/getImage").post(body).build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "failed to execute request");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                Log.d("TAG", "Response : " + response.toString());

                if (response.isSuccessful()) {
                    System.out.println("Response is successful");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        imageUri2 = jsonObject.getString("image");
                        if (imageUri2 != null) {
//                            byte[] imageData = Base64.getDecoder().decode(imageUri2);
//                            byte[] imageData = Base64.getMimeDecoder().decode(imageUri2);
//                            Bitmap dppp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
//                            userDP.setImageBitmap(dppp);
//                            Picasso.get().load(imageUri2).into(userDP);

                            Log.d("Shaheer", imageUri2);
//                            Picasso.get().load(imageUri2).into(userDP);
                        }
                        response.close();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                else {
                    Log.d("Shaheer", "not successful");
                }
            }
        });
    }

    private void setViewPagerWithTab () {
        tabLayout.setupWithViewPager(viewPager);
        MainScreenAdapter mainScreenAdapter = new MainScreenAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mainScreenAdapter.addFragment(new BurgerFragment(), "Burgers");
        mainScreenAdapter.addFragment(new PizzaFragment(), "Pizzas");
        mainScreenAdapter.addFragment(new PastasFragment(), "Pastas");
        mainScreenAdapter.addFragment(new ParathasFragment(), "Parathas");
        mainScreenAdapter.addFragment(new ShwarmaFragment(), "Shwarma");
        mainScreenAdapter.addFragment(new DealsFragment(), "Deals");
        mainScreenAdapter.addFragment(new AddonsFragment(), "Addons");

        viewPager.setAdapter(mainScreenAdapter);
    }


    private void setDrawerNavView () {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                //int itemId = item.getItemId();

                switch (item.getItemId()) {
                    case R.id.see_profile:
                        startActivity(new Intent(MainScreen.this, SeeProfile.class));
                        return true;
                    case R.id.edit_profile:
                        startActivity(new Intent(MainScreen.this, EditProfileA.class));
                        return true;
                    case R.id.my_orders:
                        startActivity(new Intent(MainScreen.this, MyOrdersA.class));
                        return true;
                    case R.id.pswd_change:
                        startActivity(new Intent(MainScreen.this, ChangePasswordA.class));
                        return true;
                    case R.id.contact_us:
                        startActivity(new Intent(MainScreen.this, ContactUsA.class));
                        return true;
                    case R.id.logout:
                        logoutFunctionality();
                }
                return false;
            }
        });
    }

    private void setBottomNavView () {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.liked:
                        Intent intent3 = new Intent(MainScreen.this, FavoritesActivity.class);
                        startActivity(intent3);
                        return true;
                    case R.id.search:
                        Intent intent1 = new Intent(MainScreen.this, SearchActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.cart:
                        Intent intent2 = new Intent(MainScreen.this, CartActivity.class);
                        startActivity(intent2);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void logoutFunctionality () {
        FirebaseAuth.getInstance().signOut();
        onBackPressed();
        //startActivity(new Intent(MainScreen.this,Login.class));
        finish();
    }

    private void readDataFromDB () {
        userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firestore.collection("Users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                YourUserName.setText(value.getString("username"));
                YourAddress.setText(value.getString("address"));
                YourUserName.setText(value.getString("username"));
                User user = User.getInstance();
                user.setUsername(value.getString("username"));
                user.setFull_name(value.getString("full_name"));
                user.setEmail(value.getString("email"));
                user.setPhone_num(value.getString("phone_num"));
                user.setAddress(value.getString("address"));
            }
        });


    }

}