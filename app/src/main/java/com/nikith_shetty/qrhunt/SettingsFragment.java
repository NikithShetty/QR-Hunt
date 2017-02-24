package com.nikith_shetty.qrhunt;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private SettingsFragmentInteraction settingsFragmentInteraction;
    public static final String KEY_PREF_WIDTH = "pref_width";

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_settings);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        Preference codePref = findPreference(KEY_PREF_WIDTH);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String enteredValue = sharedPreferences.getString(KEY_PREF_WIDTH, "");
        codePref.setSummary(this.getActivity().getResources().getString(R.string.pref_width_summary) +
                ": " + enteredValue);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final Activity activity = getActivity();
        if (activity instanceof SettingsFragmentInteraction) {
            settingsFragmentInteraction = (SettingsFragmentInteraction) activity;
            //KEY_PREF_CODE = getActivity().getResources().getString(R.string.editPreference_code_id);
        } else {
            throw new IllegalArgumentException("Activity must implement SettingsFragmentInteraction");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(KEY_PREF_WIDTH)){
            Preference codePref = findPreference(KEY_PREF_WIDTH);
            //set summary to the value entered by the user after checking for correctness of the value entered
            String enteredValue = sharedPreferences.getString(KEY_PREF_WIDTH, "");
            if(!enteredValue.equals("") &&
                    Pattern.matches("\\d\\d\\d+", enteredValue) &&
                    Integer.parseInt(enteredValue)>=500 &&
                    Integer.parseInt(enteredValue)<=999){
                codePref.setSummary(this.getActivity().getResources().getString(R.string.pref_width_summary) +
                        ": " + enteredValue);
            }else{
                codePref.setDefaultValue("500");
                Toast.makeText(this.getActivity(), "Enter a valid width value between 500 and 9999", Toast.LENGTH_SHORT).show();
            }
            
        }
    }

    public interface SettingsFragmentInteraction{
        void onFragmentInteraction(Uri uri);
    }

}
