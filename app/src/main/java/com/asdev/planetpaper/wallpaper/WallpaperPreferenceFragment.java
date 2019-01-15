package com.asdev.planetpaper.wallpaper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class WallpaperPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);

        findPreference(getString(R.string.key_reset_advanced)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                editor.putString(getString(R.string.key_over_draw), "150");
                editor.putString(getString(R.string.key_offset_y), "600");
                editor.apply();

                Toast.makeText(getActivity(), "Advanced settings reset", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
