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


public class DealsFragment extends Fragment {

    RecyclerView deals_rv;
    List<FoodModal> list;

    public DealsFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_deals, container, false);
        deals_rv = (RecyclerView) view.findViewById(R.id.deals_recycler_view);
        list = new ArrayList<>();
        list.add(new FoodModal("Deal for 2","$400","best","hi"));
        list.add(new FoodModal("Deal for 3","$600","best","hi"));
        list.add(new FoodModal("Deal for 4","$800","best","hi"));
        list.add(new FoodModal("Deal for 6","$900","best","hi"));



        ShwarmaAdapter adapter = new ShwarmaAdapter(list,getContext());
        deals_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        deals_rv.setAdapter(adapter);

        return view;
    }
}