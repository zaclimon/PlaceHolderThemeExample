package com.zaclimon.placeholderthemeexample.activities

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zaclimon.placeholderthemeexample.R
import com.zaclimon.placeholderthemeexample.fragments.MainFragment

/**
 * Created by isaac on 17-08-18.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {

        if (isDarkTheme()) {
            setTheme(R.style.AppTheme)
        } else {
            setTheme(R.style.AppThemeLight)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentPlaceholder, MainFragment())
        fragmentTransaction.commit()

    }

    private fun isDarkTheme() : Boolean {
        var sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
        return (sharedPreferences.getBoolean(DARK_THEME_KEY, false))
    }

    companion object {
        val PREFERENCES_KEY : String = "example_prefs"
        val DARK_THEME_KEY : String = "dark_key"
    }

}