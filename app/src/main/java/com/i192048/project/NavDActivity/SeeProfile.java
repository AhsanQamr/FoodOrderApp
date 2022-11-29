package com.i192048.project.NavDActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.i192048.project.MainScreen;
import com.i192048.project.Modals.User;
import com.i192048.project.R;

public class SeeProfile extends AppCompatActivity {

    TextView username,email,address,phone,full_name;
    User user;
    ImageView back;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_profile);

        user = User.getInstance();
        back = findViewById(R.id.back);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.contact);
        full_name = findViewById(R.id.fullname);

        username.setText(user.getUsername());
        email.setText(user.getEmail());
        address.setText(user.getAddress());
        phone.setText(user.getPhone_num());
        full_name.setText(user.getFull_name());



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}