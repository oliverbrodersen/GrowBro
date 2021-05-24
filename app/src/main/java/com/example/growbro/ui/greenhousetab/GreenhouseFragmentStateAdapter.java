package com.example.growbro.ui.greenhousetab;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.growbro.ui.greenhousetab.greenhouse.GreenhouseFragment;
import com.example.growbro.ui.greenhousetab.statistics.StatisticsFragment;

import java.util.ArrayList;
import java.util.List;

public class GreenhouseFragmentStateAdapter extends FragmentStateAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private String selectedGreenhouseId;

    public GreenhouseFragmentStateAdapter(Fragment fragment, String selectedAreaId) {
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
        args.putString(GreenhouseFragment.ARG_SELECTED_GREENHOUSE_ID, selectedGreenhouseId);
        fragment.setArguments(args);
        Log.i("fragment", position + "");
        return fragment;
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}