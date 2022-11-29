package com.i192048.project.NavDActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.i192048.project.Modals.User;
import com.i192048.project.R;

import java.util.HashMap;
import java.util.Map;

public class EditProfileA extends AppCompatActivity {

    ImageView back;
    EditText updateEmail,updateName,updateAddress,updateContact;
    MaterialButton updateProfile;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        back = findViewById(R.id.back);
        updateEmail = findViewById(R.id.update_email);
        updateAddress = findViewById(R.id.update_address);
        updateName = findViewById(R.id.update_name);
        updateContact = findViewById(R.id.update_phone);
        updateProfile = findViewById(R.id.update_profile);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile();
            }
        });


    }

    private void updateUserProfile(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null) {
            userID = firebaseUser.getUid();

            String update_email = updateEmail.getText().toString();
            String update_address = updateAddress.getText().toString();
            String update_name = updateName.getText().toString();
            String update_contact = updateContact.getText().toString();





            Map<String, Object> userMap = new HashMap<>();
            userMap.put("email", update_email);
            userMap.put("address", update_address);
            userMap.put("full_name", update_name);
            userMap.put("phone_num", update_contact);

            User user = User.getInstance();
            user.setEmail(update_email);
            user.setAddress(update_address);
            user.setFull_name(update_name);
            user.setPhone_num(update_contact);

            firestore.collection("Users").document(userID).update(userMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(EditProfileA.this, "User Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }
}