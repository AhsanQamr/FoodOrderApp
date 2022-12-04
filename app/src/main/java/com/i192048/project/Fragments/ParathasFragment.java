package com.i192048.project.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.i192048.project.Adapters.ShwarmaAdapter;
import com.i192048.project.Modals.FoodModal;
import com.i192048.project.R;

import java.util.ArrayList;
import java.util.List;


public class ParathasFragment extends Fragment {

    RecyclerView paratha_rv;
    List<FoodModal> list;

    public ParathasFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_parathas, container, false);
        paratha_rv = (RecyclerView) view.findViewById(R.id.parathas_recycler_view);
        list = new ArrayList<>();
        list.add(new FoodModal("Bihari Roll Paratha","$400","best","hi"));
        list.add(new FoodModal("Bihari Roll Paratha","$400","best","hi"));
        list.add(new FoodModal("Bihari Roll Paratha","$400","best","hi"));
        list.add(new FoodModal("Bihari Roll Paratha","$400","best","hi"));
        list.add(new FoodModal("Bihari Roll Paratha","$400","best","hi"));


        ShwarmaAdapter adapter = new ShwarmaAdapter(list,getContext());
        paratha_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        paratha_rv.setAdapter(adapter);

        return view;
    }
}