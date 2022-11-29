package com.i192048.project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.i192048.project.Modals.User;

import java.util.Objects;

public class Signup extends AppCompatActivity {

    MaterialButton Signup;
    TextView toLogin;
    EditText email,password,name,username,phone,address;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;
    DocumentReference reference;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Signup = findViewById(R.id.signup);
        toLogin = findViewById(R.id.sign_in_text);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        username = findViewById(R.id.user_name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        firestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Please wait while we create your account");

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this,Login.class);
                startActivity(intent);
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
                //addUserToDB();
                //addDataToUserClass();
            }
        });

    }

    private void createUser() {
        String email_field = email.getText().toString();
        String pass_field = password.getText().toString();
        String name_field = name.getText().toString();
        String username_field = username.getText().toString();
        String phone_field = phone.getText().toString();
        String address_field = address.getText().toString();

        if(TextUtils.isEmpty(email_field)){
            email.setError("Please provide email");
            email.requestFocus();
        }       else if(TextUtils.isEmpty(pass_field)){
            password.setError("Add a password");
            password.requestFocus();
        }       else if(TextUtils.isEmpty(name_field)){
            name.setError("Please provide name");
            name.requestFocus();
        }        else if(TextUtils.isEmpty(username_field)){
            username.setError("Please provide username");
            username.requestFocus();
        }        else if(TextUtils.isEmpty(phone_field)){
            phone.setError("Please provide phone");
            phone.requestFocus();
        }        else if(TextUtils.isEmpty(address_field)){
            address.setError("Please provide address");
            address.requestFocus();
        }
        else{
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email_field, pass_field)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                Log.d(TAG,"creation: "+userID.toString());
                                User user = new User(name_field,username_field,phone_field,address_field,email_field,pass_field,userID);
                                firestore.collection("Users").document(userID).set(user);
                                Log.d(TAG, "onComplete: User added to user class");
                                progressDialog.dismiss();
                                Toast.makeText(Signup.this, "User registered", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Signup.this, MainScreen.class));
                            } else {
                                progressDialog.dismiss();
                                if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                    Toast.makeText(Signup.this, "User already registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup.this, "Error registering", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

}