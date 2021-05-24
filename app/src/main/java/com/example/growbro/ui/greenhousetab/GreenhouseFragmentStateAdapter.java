package com.example.growbro.ui.greenhousetab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.growbro.ui.greenhousetab.greenhouse.GreenhouseFragment;
import com.example.growbro.ui.greenhousetab.statistics.StatisticsFragment;

import java.util.ArrayList;
import java.util.List;

public class GreenhouseFragmentStateAdapter extends FragmentStateAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private int selectedGreenhouseId;

    public GreenhouseFragmentStateAdapter(Fragment fragment, int selectedAreaId) {
        super(fragment);

        this.selectedGreenhouseId = selectedAreaId;
        fragments.add(new GreenhouseFragment());
        fragments.add(new StatisticsFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = fragments.get(position);

        Bundle args = new Bundle();
        args.putInt(GreenhouseFragment.ARG_SELECTED_GREENHOUSE_ID, selectedGreenhouseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}