package com.aware.phone.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.aware.phone.Aware_Client;
import com.aware.phone.R;
import com.aware.phone.ui.login.LoginActivity;

public class About extends Aware_Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.aware_about);
        SharedPreferences sp = getSharedPreferences("Login",0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
        Intent i =new Intent(About.this, LoginActivity.class);
        startActivity(i);
//        WebView about_us = (WebView) findViewById(R.id.about_us);
//        WebSettings settings = about_us.getSettings();
//        settings.setJavaScriptEnabled(true);
//        about_us.loadUrl("https://awareframework.com/team/");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
