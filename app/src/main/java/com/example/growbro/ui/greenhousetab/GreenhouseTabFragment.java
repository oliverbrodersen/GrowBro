package com.example.growbro.ui.greenhousetab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.growbro.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GreenhouseTabFragment extends Fragment {

    GreenhouseFragmentStateAdapter greenhouseFragmentStateAdapter;
    ViewPager2 viewPager;
    private final String[] titles = new String[]{"Greenhouse", "Statistics"};

    private String selectedGreenhouseId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedGreenhouseId = getArguments().getString(getString(R.string.selectedGreenhouseId));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.greenhouse_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        greenhouseFragmentStateAdapter = new GreenhouseFragmentStateAdapter(this, selectedGreenhouseId);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(greenhouseFragmentStateAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])
        ).attach();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}