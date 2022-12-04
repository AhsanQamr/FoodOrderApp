package com.i192048.project.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.i192048.project.Adapters.PastasAdapter;
import com.i192048.project.Modals.FoodModal;
import com.i192048.project.R;

import java.util.ArrayList;
import java.util.List;


public class PastasFragment extends Fragment {

    RecyclerView pastas_rv;
    List<FoodModal> list;

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
        list.add(new FoodModal("chicken alfredo pasta","$400","best","hi"));
        list.add(new FoodModal("chicken alfredo pasta","$400","best","hi"));
        list.add(new FoodModal("chicken alfredo pasta","$400","best","hi"));
        list.add(new FoodModal("chicken alfredo pasta","$400","best","hi"));
        list.add(new FoodModal("chicken alfredo pasta","$400","best","hi"));
        list.add(new FoodModal("chicken alfredo pasta","$400","best","hi"));

        PastasAdapter adapter = new PastasAdapter(list,getContext());
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BurgerFragment.this.getContext());
        pastas_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        //burger_rv.setLayoutManager(layoutManager);
        pastas_rv.setAdapter(adapter);

        return view;
    }
}