package com.zaclimon.placeholderthemeexample.fragments

import android.app.Fragment
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import com.zaclimon.placeholderthemeexample.BuildConfig
import com.zaclimon.placeholderthemeexample.R
import com.zaclimon.placeholderthemeexample.activities.MainActivity
import com.zaclimon.placeholderthemeexample.activities.placeholders.PlaceHolderActivity

/**
 * Created by isaac on 17-08-18.
 */
class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return (inflater!!.inflate(R.layout.fragment_main, container, false))
    }

    override fun onStart() {
        super.onStart()

        val radioGroup = activity.findViewById<RadioGroup>(R.id.radioButtonGroup)
        val applyButton = activity.findViewById<Button>(R.id.applyButton)
        val currentThemeTextView = activity.findViewById<TextView>(R.id.currentThemeTextView)

        applyButton.setOnClickListener {
            var id = radioGroup.checkedRadioButtonId

            if (id != 0) {
               changeTheme(id)
            }
        }

        radioGroup.setOnCheckedChangeListener { group, i ->
            var currentTheme = getCurrentTheme()
            var shouldButtonBeActivated = i == R.id.lightThemeRadioButton && currentTheme == getString(R.string.dark_theme_placeholder)
                    || i == R.id.darkThemeRadioButton && currentTheme == getString(R.string.light_theme_placeholder)

            applyButton.isEnabled = shouldButtonBeActivated
        }
        currentThemeTextView.text = getString(R.string.current_theme, getCurrentTheme())

    }

    fun changeTheme(id: Int): Unit {

        val editor: SharedPreferences.Editor = activity.getSharedPreferences(MainActivity.PREFERENCES_KEY, Context.MODE_PRIVATE).edit()

        when (id) {
            R.id.darkThemeRadioButton -> {
                editor.putBoolean(MainActivity.DARK_THEME_KEY, true)
                switchActivities(true)
            }
            R.id.lightThemeRadioButton -> {
                editor.putBoolean(MainActivity.DARK_THEME_KEY, false)
                switchActivities(false)
            }
        }

        editor.apply()

    }

    private fun getCurrentTheme(): String {
        val sharedPreferences = activity.getSharedPreferences(MainActivity.PREFERENCES_KEY, Context.MODE_PRIVATE)
        return if (sharedPreferences.getBoolean(MainActivity.DARK_THEME_KEY, false)) getString(R.string.dark_theme_placeholder) else getString(R.string.light_theme_placeholder)
    }

    private fun switchActivities(isDark: Boolean) : Unit {

        val APPLICATION_NAME = BuildConfig.APPLICATION_ID
        var packageManager = activity.packageManager
        var componentNames : MutableList<ComponentName> = mutableListOf()

        var lightThemeComponentName = ComponentName(APPLICATION_NAME, APPLICATION_NAME + PlaceHolderActivity.LIGHT_THEME_PLACEHOLDER_COMPONENT_NAME)
        var darkThemeComponentName = ComponentName(APPLICATION_NAME, APPLICATION_NAME + PlaceHolderActivity.DARK_THEME_PLACEHOLDER_COMPONENT_NAME)

        componentNames.add(lightThemeComponentName)
        componentNames.add(darkThemeComponentName)

        for (tempComp in componentNames) {
            packageManager.setComponentEnabledSetting(tempComp, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP)
        }

        // Since the light theme is the default one, we only change the alias if the theme is dark.
        if (isDark) {
            packageManager.setComponentEnabledSetting(lightThemeComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
            packageManager.setComponentEnabledSetting(darkThemeComponentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        }

        activity.recreate()
    }

}