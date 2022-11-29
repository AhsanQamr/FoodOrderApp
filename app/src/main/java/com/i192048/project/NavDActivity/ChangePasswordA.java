package com.i192048.project.NavDActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.i192048.project.R;

import java.util.Objects;

public class ChangePasswordA extends AppCompatActivity {

    ImageView back;
    EditText curr_field, update_field1,update_field2;
    FirebaseAuth mAuth;
    MaterialButton updatePassword;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        back = findViewById(R.id.back);
        updatePassword = findViewById(R.id.update_password);
        curr_field = findViewById(R.id.curr_password);
        update_field1 = findViewById(R.id.update_pass1);
        update_field2 = findViewById(R.id.update_pass2);
        mAuth = FirebaseAuth.getInstance();



        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCredentials();
            }
        });




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getCredentials(){

        String f1 = curr_field.getText().toString();
        String f2 = update_field1.getText().toString();
        String f3 = update_field2.getText().toString();

        if(TextUtils.isEmpty(f1)){
            curr_field.setError("Password field empty");
            curr_field.requestFocus();
        }
        else if(TextUtils.isEmpty(f2)){
            update_field1.setError("Password field empty");
            update_field2.requestFocus();
        }
        else if(TextUtils.isEmpty(f3)){
            update_field2.setError("Password field empty");
            update_field2.requestFocus();
        }
        else{
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), curr_field.getText().toString());
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ChangePasswordA.this, "user re-authenticated", Toast.LENGTH_SHORT).show();
                                updatePassword();
                            }
                            else
                                Toast.makeText(ChangePasswordA.this, "old password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    });

        }



    }

    private void updatePassword(){

        String password1 = update_field1.getText().toString();
        String password2 = update_field2.getText().toString();
        if(!password1.equals(password2)){
            Toast.makeText(ChangePasswordA.this, "both passwords does not match", Toast.LENGTH_SHORT).show();
        }
        else{
            user.updatePassword(password1)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("TAG",password2);
                            if (task.isSuccessful())
                                Toast.makeText(ChangePasswordA.this, "Password updated", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(ChangePasswordA.this, "Issues updating password, Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}