package com.i192048.project.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i192048.project.Adapters.BurgerAdapter;
import com.i192048.project.Adapters.PizzaAdapter;
import com.i192048.project.Modals.FoodModal;
import com.i192048.project.R;

import java.util.ArrayList;
import java.util.List;


public class PizzaFragment extends Fragment {

    RecyclerView pizza_rv;
    List<FoodModal> list;

    public PizzaFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pizza, container, false);
        pizza_rv = (RecyclerView) view.findViewById(R.id.pizza_recycler_view);

        list = new ArrayList<>();
        list.add(new FoodModal("bbq chicken","$1000","best","hi"));
        list.add(new FoodModal("bbq chicken","$1000","best","hi"));
        list.add(new FoodModal("bbq chicken","$1000","best","hi"));
        list.add(new FoodModal("bbq chicken","$1000","best","hi"));
        list.add(new FoodModal("bbq chicken","$1000","best","hi"));
        list.add(new FoodModal("bbq chicken","$1000","best","hi"));
        list.add(new FoodModal("bbq chicken","$1000","best","hi"));
        list.add(new FoodModal("bbq chicken","$1000","best","hi"));
        list.add(new FoodModal("bbq chicken","$1000","best","hi"));


        PizzaAdapter adapter = new PizzaAdapter(list,getContext());
        pizza_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        pizza_rv.setAdapter(adapter);

        return view;
    }
}