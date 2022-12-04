package com.i192048.project.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i192048.project.Adapters.BurgerAdapter;
import com.i192048.project.Modals.FoodModal;
import com.i192048.project.R;

import java.util.ArrayList;
import java.util.List;

public class BurgerFragment extends Fragment {


    List<FoodModal> list;
    public BurgerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView burger_rv;
        View view = inflater.inflate(R.layout.fragment_burger, container, false);
        burger_rv = (RecyclerView) view.findViewById(R.id.burger_recycler_view);

        list = new ArrayList<>();
        list.add(new FoodModal("chicken jalapeno","500","good","hi"));
        list.add(new FoodModal("chicken jalapeno","500","good","hi"));
        list.add(new FoodModal("chicken jalapeno","500","good","hi"));
        list.add(new FoodModal("chicken jalapeno","500","good","hi"));
        list.add(new FoodModal("chicken jalapeno","500","good","hi"));
        list.add(new FoodModal("chicken jalapeno","500","good","hi"));
        list.add(new FoodModal("chicken jalapeno","500","good","hi"));

        BurgerAdapter adapter = new BurgerAdapter(list,getContext());
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BurgerFragment.this.getContext());
        burger_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        //burger_rv.setLayoutManager(layoutManager);
        burger_rv.setAdapter(adapter);

        return view;
    }


}