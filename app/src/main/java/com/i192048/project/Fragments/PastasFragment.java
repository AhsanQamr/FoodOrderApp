package com.i192048.project.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.i192048.project.Adapters.DealsAdapter;
import com.i192048.project.Adapters.PastasAdapter;
import com.i192048.project.Modals.FoodModal;
import com.i192048.project.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PastasFragment extends Fragment {

    RecyclerView pastas_rv;
    List<FoodModal> list;
    Map<String, Object> data = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PastasFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pastas, container, false);
        pastas_rv = (RecyclerView) view.findViewById(R.id.pastas_recycler_view);
        list = new ArrayList<>();

        pastas_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchData();

        return view;
    }

    private void fetchData(){
        // fetch data from maps stored in documents and add them to list
        db.collection("Foods").document("Pastas").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                            String image = (String) map.get("Image");
                            String description = (String) map.get("Description");
                            //list.add(new FoodModal(map.get("Name").toString(), map.get("Price").toString(),map.get("Description").toString() ,map.get("Image").toString()));
                            list.add(new FoodModal(name, price, description, image));
                        }
                    }
                    System.out.println("l" + list);
                    System.out.println("data" + data);

                    PastasAdapter adapter = new PastasAdapter(list,getContext());
                    pastas_rv.setAdapter(adapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}