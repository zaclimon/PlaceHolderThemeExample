package com.zaclimon.placeholderthemesexample.activities.placeholders;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.zaclimon.placeholderthemesexample.activities.MainActivity;

/**
 * Created by isaac on 17-04-12.
 */

public abstract class PlaceHolderActivity extends AppCompatActivity {

    /*
     In an attempt to leave a good user experience, don't just show the user a blank screen while
     the app is loading in a cold start. Let's base ourselves from the Material Design spec sheet
     and use a PlaceHolder UI since the app doesn't require that much time to start up.

     One of the techniques used is to modify android:windowBackground to make it show a static layout
     the time the view of the Activity get's inflated.

     That said the case with some dynamic theme applications is kinda special since most of the
     tactics available to get it running are only for apps that has only one main theme.

     Since there are 2 themes (Light and Dark) and there is only one theme at a time that can be
     defined in AndroidManifest.xml, let's do this by creating 3 placeholder Activities that will
     represent each theme. These themes will also have their own launchers event in the user's
     launcher since we want to change activities at will to show the represented theme.

     They all are based from this abstract PlaceHolderActivity since their only task is to create a
     new MainActivity.

     This has to be one of the ugliest way to get it done but there doesn't seem to be any other
     way to bypass AndroidManifest.xml's limitation at the moment so let's use this.

     Thanks to these two links which guided me toward doing this:
     http://saulmm.github.io/avoding-android-cold-starts
     https://www.reddit.com/r/androiddev/comments/3r7lvg/use_cold_start_time_to_make_your_app_look_much/cwml976/
     */

    public static final String LIGHT_THEME_PLACEHOLDER_COMPONENT_NAME = ".LightTheme.PlaceHolder";
    public static final String DARK_THEME_PLACEHOLDER_COMPONENT_NAME = ".DarkTheme.PlaceHolder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add some sleep to really see the placeholder
        SystemClock.sleep(1000);

        Intent baseActivityIntent = new Intent(this, MainActivity.class);
        startActivity(baseActivityIntent);
        finish();
    }

}
