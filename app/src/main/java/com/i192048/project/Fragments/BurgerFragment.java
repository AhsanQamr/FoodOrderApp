package com.i192048.project.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.i192048.project.Adapters.AddOnsAdapter;
import com.i192048.project.Adapters.BurgerAdapter;
import com.i192048.project.DetailsActivity;
import com.i192048.project.Modals.FoodModal;
import com.i192048.project.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BurgerFragment extends Fragment {


    List<FoodModal> list;
    FirebaseFirestore db;
    RecyclerView burger_rv;
    Map<String, Object> data = new HashMap<>();

    public BurgerFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_burger, container, false);
        list = new ArrayList<>();
        burger_rv = (RecyclerView) view.findViewById(R.id.burger_recycler_view);
        db = FirebaseFirestore.getInstance();
        burger_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchData();

        return view;
    }


    private void fetchData(){
        // fetch data from maps stored in documents and add them to list
        db.collection("Foods").document("Burgers").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        data = document.getData();
                        assert data != null;
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            Map<String, Object> map = (Map<String, Object>) entry.getValue();
                            String name = (String) map.get("Name");
                            String price = (String) map.get("Price");
                            String price2 = (String) map.get("price2");
                            String price3 = (String) map.get("price3");
                            String image = (String) map.get("Image");
                            String description = (String) map.get("Description");

                            list.add(new FoodModal(name, price, description, image, price2, price3));
                            //list.add(new FoodModal(map.get("Name").toString(), map.get("Price").toString(),map.get("Description").toString() ,map.get("Image").toString()));
                        }

                    }

                    BurgerAdapter adapter = new BurgerAdapter(list,getContext());
                    burger_rv.setAdapter(adapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchItem(){

    }


}