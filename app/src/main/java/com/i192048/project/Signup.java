package com.i192048.project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.i192048.project.Modals.User;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Signup extends AppCompatActivity {

    MaterialButton Signup;
    TextView toLogin;
    EditText email,password,name,username,phone,address;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;
    DocumentReference reference;
    ImageView userImage;
    Uri imageUri;
    String userID;


    @SuppressLint("WrongViewCast")
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
        userImage = findViewById(R.id.user_image);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Please wait while we create your account");

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
            }
        });

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

                                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
                                ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageUrl = uri.toString();
                                                Log.d(TAG, "onSuccess: "+ imageUrl);

                                                // add ip address to okhttp client


                                                System.out.println(4);
                                                OkHttpClient client = new OkHttpClient();
                                                System.out.println(3);
                                                RequestBody body = new FormBody.Builder()
                                                        .add("email",email_field)
                                                        .add("image",imageUrl)
                                                        .build();
                                                System.out.println(1);
                                                Request request = new Request.Builder()
                                                        .url("http://192.168.18.65:5000/postImage").post(body).build();
                                                System.out.println(2);
                                                client.newCall(request).enqueue(new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        Log.d(TAG, "onFailure: "+e.getMessage());
                                                        e.printStackTrace();
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        if(response.isSuccessful()){
                                                            String resp = response.body().string();
                                                            Log.d(TAG, "onResponse: "+response.body().string());


                                                            if(resp.contains("success")){
                                                                Log.d("Response", resp);
                                                                progressDialog.dismiss();
                                                                User user = new User(name_field,username_field,phone_field,address_field,email_field,pass_field,userID);
                                                                firestore.collection("Users").document(userID).set(user);
                                                                Log.d(TAG, "onComplete: User added to user class");
                                                                response.close();
                                                                //Toast.makeText(Signup.this, "User registered", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(Signup.this, MainScreen.class));
                                                            }
                                                            else{
                                                                Log.d("Response", resp);
                                                                progressDialog.dismiss();
                                                                response.close();
                                                                runOnUiThread(()-> Toast.makeText(Signup.this, "Some error Occurred", Toast.LENGTH_SHORT).show());
                                                            }
                                                        }




                                                    }
                                                });
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Signup.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                    }
                                });



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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            userImage.setImageURI(imageUri);
        }
    }




    private String getImageUrl(){
        final String[] imageUrl = {""};

        return imageUrl[0];
    }

}