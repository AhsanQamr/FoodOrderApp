package com.i192048.project.NavBActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.i192048.project.Adapters.FavoritesAdapter;
import com.i192048.project.Modals.Favorites;
import com.i192048.project.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FavoritesActivity extends AppCompatActivity {

    ImageView back;
    FirebaseFirestore db;
    FirebaseAuth auth;
    RecyclerView favorites_rv;
    List<Favorites> list = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        back = findViewById(R.id.back);
        favorites_rv = findViewById(R.id.favorites_recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_favorite);

        favorites_rv.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                startActivity(getIntent());
            }
        });


        fetchFavorites();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }

    private void fetchFavorites(){


        db.collection("Favorites").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("UserFavorites").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot documentSnapshot : task.getResult()){
                                Map<String, Object> map = documentSnapshot.getData();
                                assert map != null;
                                list.add(new Favorites(map.get("f_name").toString(), map.get("f_price").toString(), map.get("f_image").toString()));
                            }
                            FavoritesAdapter adapter = new FavoritesAdapter(list, FavoritesActivity.this);
                            favorites_rv.setAdapter(adapter);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FavoritesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}