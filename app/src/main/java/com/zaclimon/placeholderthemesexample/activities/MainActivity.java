package com.zaclimon.placeholderthemesexample.activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zaclimon.placeholderthemesexample.R;
import com.zaclimon.placeholderthemesexample.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCES_FILE = "example_prefs";
    public static final String DARK_THEME_KEY = "dark_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (isThemeDark()) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppThemeLight);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentPlaceholder, new MainFragment());
        fragmentTransaction.commit();
    }

    private boolean isThemeDark() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return (sharedPreferences.getBoolean(DARK_THEME_KEY, false));
    }

}
