package com.example.growbro.ui.greenhousetab;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.growbro.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GreenhouseTabFragment extends Fragment {

    private GreenhouseTabViewModel mViewModel;

    public static GreenhouseTabFragment newInstance() {
        return new GreenhouseTabFragment();
    }

    GreenhouseFragmentStateAdapter greenhouseFragmentStateAdapter;
    ViewPager2 viewPager;
    private final String[] titles = new String[]{"Greenhouse", "Statistics"};

    private int selectedGreenhouseId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedGreenhouseId = getArguments().getInt(getString(R.string.selectedGreenhouseId));
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
        mViewModel = new ViewModelProvider(this).get(GreenhouseTabViewModel.class);
        // TODO: Use the ViewModel
    }

}