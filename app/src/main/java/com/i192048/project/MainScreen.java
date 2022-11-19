package com.i192048.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.i192048.project.Adapters.MainScreenAdapter;
import com.i192048.project.Fragments.AddonsFragment;
import com.i192048.project.Fragments.BurgerFragment;
import com.i192048.project.Fragments.DealsFragment;
import com.i192048.project.Fragments.ParathasFragment;
import com.i192048.project.Fragments.PastasFragment;
import com.i192048.project.Fragments.PizzaFragment;
import com.i192048.project.Fragments.ShwarmaFragment;
import com.i192048.project.NavBActivity.CartActivity;
import com.i192048.project.NavBActivity.FavoritesActivity;
import com.i192048.project.NavBActivity.SearchActivity;
import com.i192048.project.NavDActivity.ChangePasswordA;
import com.i192048.project.NavDActivity.ContactUsA;
import com.i192048.project.NavDActivity.EditProfileA;
import com.i192048.project.NavDActivity.MyOrdersA;

public class MainScreen extends AppCompatActivity {

    ImageView menu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    TabLayout tabLayout;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;


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
        /* Hooks End */


        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.bringToFront();

        setViewPagerWithTab();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(Gravity.LEFT))
                    drawerLayout.closeDrawer(Gravity.LEFT);
                else
                    drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        setBottomNavView();
        setDrawerNavView();
    }

    private void setViewPagerWithTab(){
        tabLayout.setupWithViewPager(viewPager);
        MainScreenAdapter mainScreenAdapter = new MainScreenAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mainScreenAdapter.addFragment(new BurgerFragment(),"Burgers");
        mainScreenAdapter.addFragment(new PizzaFragment(),"Pizzas");
        mainScreenAdapter.addFragment(new PastasFragment(),"Pastas");
        mainScreenAdapter.addFragment(new ParathasFragment(),"Parathas");
        mainScreenAdapter.addFragment(new ShwarmaFragment(),"Shwarma");
        mainScreenAdapter.addFragment(new DealsFragment(),"Deals");
        mainScreenAdapter.addFragment(new AddonsFragment(),"Addons");

        viewPager.setAdapter(mainScreenAdapter);
    }


    private void setDrawerNavView(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                int itemId = item.getItemId();

                switch (item.getItemId()){

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

    private void setBottomNavView(){
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void logoutFunctionality(){
        FirebaseAuth.getInstance().signOut();
        onBackPressed();
        finish();
    }


}