package com.musicentertainment.countrymusic;

import android.app.Activity;
import android.app.Application;
import android.os.StrictMode;

import com.musicentertainment.utils.AppOpenManager;
import com.musicentertainment.utils.Constant;
import com.musicentertainment.utils.DBHelper;
import com.musicentertainment.utils.SharedPref;
import com.facebook.FacebookSdk;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OneSignal;

import androidx.appcompat.app.AppCompatDelegate;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class MyApplication extends Application {

    SharedPref sharedPref;
    private static AppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPref = new SharedPref(getApplicationContext());

        FirebaseAnalytics.getInstance(getApplicationContext());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/poppins_reg.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        OneSignal.initWithContext(this);
        OneSignal.setAppId("93307a8f-02c2-444f-996c-9b51f509fb74");

        DBHelper dbHelper = new DBHelper( getApplicationContext());
        dbHelper.onCreate(dbHelper.getWritableDatabase());
        dbHelper.getAbout();

        MobileAds.initialize(getApplicationContext());

        FacebookSdk.sdkInitialize(getApplicationContext());

        String mode = sharedPref.getDarkMode();
        switch (mode) {
            case Constant.DARK_MODE_SYSTEM:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case Constant.DARK_MODE_OFF:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case Constant.DARK_MODE_ON:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }

        appOpenManager = new AppOpenManager(this);
    }
}