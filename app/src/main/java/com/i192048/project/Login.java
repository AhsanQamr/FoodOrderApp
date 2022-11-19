package com.i192048.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    MaterialButton Login;
    TextView toSignup;
    EditText email,password;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login = (MaterialButton) findViewById(R.id.login);
        toSignup = (TextView) findViewById(R.id.sign_up_text);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging in");
        //progressDialog.setMessage("Please wait while we create your account");

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        toSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Signup.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(){
        String email_field = email.getText().toString();
        String pass_field = password.getText().toString();
        if(TextUtils.isEmpty(email_field)){
            email.setError("email cannot be empty");
            email.requestFocus();
        }
        else if(TextUtils.isEmpty(pass_field)){
            password.setError("Add a password");
            password.requestFocus();
        }
        else{
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email_field,pass_field)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(Login.this,"Successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this,MainScreen.class));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this,"Credentials does not match",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}