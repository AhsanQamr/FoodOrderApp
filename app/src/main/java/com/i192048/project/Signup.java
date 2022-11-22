package com.i192048.project;

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
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.i192048.project.Modals.User;

public class Signup extends AppCompatActivity {

    MaterialButton Signup;
    TextView toLogin;
    EditText email,password,name,username,phone,address;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

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
                addUserToDB();
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

    private void addUserToDB(){
        UserDBHandler userDBHandler = new UserDBHandler(Signup.this);
        SQLiteDatabase database = userDBHandler.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(UserContract.Project._NAME,name.getText().toString());
        cv.put(UserContract.Project._USERNAME,username.getText().toString());
        cv.put(UserContract.Project._PHONE,phone.getText().toString());
        cv.put(UserContract.Project._ADDRESS,address.getText().toString());
        cv.put(UserContract.Project._EMAIL,email.getText().toString());
        cv.put(UserContract.Project._PASSWORD,password.getText().toString());

        double res = database.insert(UserContract.Project.TABLE_NAME,null,cv);
        if(res == 1){
            System.out.println("successful db");
        }
        else
            System.out.println("errors");
        database.close();
        userDBHandler.close();

    }
}