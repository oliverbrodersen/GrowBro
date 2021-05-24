package com.example.growbro.ui.greenhousetab.statistics;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.growbro.R;
import com.example.growbro.ui.greenhousetab.greenhouse.GreenhouseViewModel;

public class StatisticsFragment extends Fragment {

    public static final String ARG_SELECTED_GREENHOUSE_ID = "selectedGreenhouseId";
    private int selectedGreenhouseId;

    private GreenhouseViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        selectedGreenhouseId = args.getInt(ARG_SELECTED_GREENHOUSE_ID);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.greenhouse_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GreenhouseViewModel.class);
        // TODO: Use the ViewModel
    }

}