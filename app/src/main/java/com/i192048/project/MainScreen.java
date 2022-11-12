package com.i192048.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.i192048.project.Adapters.MainScreenAdapter;
import com.i192048.project.Fragments.AddonsFragment;
import com.i192048.project.Fragments.BurgerFragment;
import com.i192048.project.Fragments.DealsFragment;
import com.i192048.project.Fragments.ParathasFragment;
import com.i192048.project.Fragments.PastasFragment;
import com.i192048.project.Fragments.PizzaFragment;
import com.i192048.project.Fragments.ShwarmaFragment;

public class MainScreen extends AppCompatActivity {

    ImageView menu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    TabLayout tabLayout;
    ViewPager viewPager;


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

        /* Hooks End */


       toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.bringToFront();

        setViewPagerWithTab();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainScreen.this,"clicked",Toast.LENGTH_SHORT).show();
                if(drawerLayout.isDrawerOpen(Gravity.LEFT))
                    drawerLayout.closeDrawer(Gravity.LEFT);
                else
                    drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                int itemId = item.getItemId();

                if(itemId==R.id.edit_profile){
                    Toast.makeText(MainScreen.this,"edit profile clicked",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });



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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}