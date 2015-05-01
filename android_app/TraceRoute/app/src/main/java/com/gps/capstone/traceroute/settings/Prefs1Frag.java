package com.gps.capstone.traceroute.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gps.capstone.traceroute.GLFiles.OpenGLActivity;
import com.gps.capstone.traceroute.debugConsole.DebugConsole;
import com.gps.capstone.traceroute.R;

public class Prefs1Frag extends PreferenceFragment implements OnSharedPreferenceChangeListener, OnPreferenceClickListener {
    // Tag for logging
    private final String TAG = getClass().getSimpleName();
    // Preference that we can disable
    private Preference mUseCube;
    // Preference that we can disable
    private SwitchPreference mUseRender;

    public Prefs1Frag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make sure default values are applied.  In a real app, you would
        // want this in a shared function that is used to retrieve the
        // SharedPreferences wherever they are needed.
//        PreferenceManager.setDefaultValues(getActivity(),
//                R.xml.advanced_preferences, false);

        // TODO sean: Currently can't get the menu to show for some bizarre reason
        setHasOptionsMenu(true);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.fragmented_preferences);
        mUseCube = findPreference(getString(R.string.pref_key_use_cube));
        mUseRender = (SwitchPreference) findPreference(getString(R.string.pref_key_render_shape));
    }


    @Override
    public void onResume() {
        super.onResume();
        // If the user does not want to render shape lets not give them the option
        mUseRender.setOnPreferenceClickListener(this);
        determineSwitch(mUseRender);
        // Make it so when we change any preferences we can see it here
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                            .registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        // Don't forget to unregister the listener when we are done
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                            .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e(TAG, "MENU");
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_debug_console, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // Depending on what the user clicks lets start that activity
        Intent i = null;
        switch (id) {
            case R.id.open_gl_view:
                i = new Intent(getActivity(), OpenGLActivity.class);
                break;
            case R.id.debug_console:
                i = new Intent(getActivity(), DebugConsole.class);
                break;
        }
        // TODO sean: Figure out to fix the back stack
        // Need to figure out how to fix the back stack so we aren't
        // having a giant back stack
        if (i != null) {
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_key_height_ft)) || key.equals(getString(R.string.pref_key_height_in))) {
            int height = 12 * Integer.valueOf(sharedPreferences.getString(getString(R.string.pref_key_height_ft), "" + 0));
            height += Integer.valueOf(sharedPreferences.getString(getString(R.string.pref_key_height_in), ""+0));
            // Put the total height in the pref
            sharedPreferences.edit().putInt(getString(R.string.pref_key_total_height_in), height).apply();
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        determineSwitch(preference);
        return false;
    }

    private void determineSwitch(Preference preference) {
        if (!preference.getSharedPreferences().getBoolean(preference.getKey(), true)) {
            mUseCube.setEnabled(false);
        } else {
            mUseCube.setEnabled(true);
        }
    }
}
