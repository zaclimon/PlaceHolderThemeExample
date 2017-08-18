package com.zaclimon.placeholderthemesexample.fragments;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zaclimon.placeholderthemesexample.BuildConfig;
import com.zaclimon.placeholderthemesexample.activities.MainActivity;
import com.zaclimon.placeholderthemesexample.R;
import com.zaclimon.placeholderthemesexample.activities.placeholders.PlaceHolderActivity;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        final RadioGroup radioGroup = (RadioGroup) getActivity().findViewById(R.id.radioButtonGroup);
        final Button applyButton = (Button) getActivity().findViewById(R.id.applyButton);
        TextView currentThemeTextView = (TextView) getActivity().findViewById(R.id.currentThemeTextView);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = radioGroup.getCheckedRadioButtonId();

                if (id != 0) {
                    changeTheme(id);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                String currentTheme = getCurrentTheme();

                if (checkedId == R.id.lightThemeRadioButton && currentTheme.equals(getString(R.string.dark_theme_placeholder)) ||
                        checkedId == R.id.darkThemeRadioButton && currentTheme.equals(getString(R.string.light_theme_placeholder))) {
                    applyButton.setEnabled(true);
                } else {
                    applyButton.setEnabled(false);
                }
            }
        });

        currentThemeTextView.setText(getString(R.string.current_theme, getCurrentTheme()));

    }

    private void changeTheme(int id) {

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MainActivity.PREFERENCES_FILE, Context.MODE_PRIVATE).edit();

        switch (id) {
            case R.id.darkThemeRadioButton:
                editor.putBoolean(MainActivity.DARK_THEME_KEY, true);
                switchActivities(true);
                break;
            case R.id.lightThemeRadioButton:
                editor.putBoolean(MainActivity.DARK_THEME_KEY, false);
                switchActivities(false);
                break;
        }
        editor.apply();
    }

    private void switchActivities(boolean isDark) {

        final String APPLICATION_NAME = BuildConfig.APPLICATION_ID;
        PackageManager packageManager = getActivity().getPackageManager();
        List<ComponentName> componentNames = new ArrayList<>();

        ComponentName lightThemeComponentName = new ComponentName(APPLICATION_NAME, APPLICATION_NAME + PlaceHolderActivity.LIGHT_THEME_PLACEHOLDER_COMPONENT_NAME);
        ComponentName darkThemeComponentName = new ComponentName(APPLICATION_NAME, APPLICATION_NAME + PlaceHolderActivity.DARK_THEME_PLACEHOLDER_COMPONENT_NAME);

        componentNames.add(lightThemeComponentName);
        componentNames.add(darkThemeComponentName);

        for (ComponentName tempComp : componentNames) {
            packageManager.setComponentEnabledSetting(tempComp, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
        }

        // Since the light theme is the default one, we only change the alias if the theme is dark.
        if (isDark) {
            packageManager.setComponentEnabledSetting(lightThemeComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            packageManager.setComponentEnabledSetting(darkThemeComponentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        }

         /*
          There is a weird issue starting from Nougat where a recreated Activity might not initialize
          itself completely. In that case, recreate the Activity using the old fashioned method.
          */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent = getActivity().getIntent();
            startActivity(intent);
            getActivity().finish();
        } else {
            getActivity().recreate();
        }
    }

    private String getCurrentTheme() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.PREFERENCES_FILE, Context.MODE_PRIVATE);
        return (sharedPreferences.getBoolean(MainActivity.DARK_THEME_KEY, false) ? getString(R.string.dark_theme_placeholder) : getString(R.string.light_theme_placeholder));
    }

}
