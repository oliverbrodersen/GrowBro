package com.example.growbro.ui.Settings;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.example.growbro.R;

public class MainSettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}