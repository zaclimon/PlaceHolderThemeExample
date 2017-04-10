package com.zaclimon.placeholderthemesexample;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.id;

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
            case R.id.lightThemeRadioButton:
                editor.putBoolean(MainActivity.DARK_THEME_KEY, false);
                break;
            case R.id.darkThemeRadioButton:
                editor.putBoolean(MainActivity.DARK_THEME_KEY, true);
                break;
        }

        editor.apply();

    }

    private String getCurrentTheme() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.PREFERENCES_FILE, Context.MODE_PRIVATE);
        return (sharedPreferences.getBoolean(MainActivity.DARK_THEME_KEY, false) ? getString(R.string.dark_theme_placeholder) : getString(R.string.light_theme_placeholder));
    }

}
