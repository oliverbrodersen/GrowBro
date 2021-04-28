package com.example.growbro.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.growbro.Models.Data.Data;
import com.example.growbro.R;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.tvCo2);
        final Button button = root.findViewById(R.id.bSendData);

        button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                    {
                        homeViewModel.waterNow(0,0);
                    }
            });

        homeViewModel.getLiveData(1).observe(getViewLifecycleOwner(), new Observer<List<Data>>() {
            @Override
            public void onChanged(List<Data> data) {

            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}