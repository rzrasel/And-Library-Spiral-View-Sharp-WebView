package com.rz.usagesexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rz.sharpwebview.SharpWebView;

public class ActSplash extends AppCompatActivity {
    private SharpWebView sysSharpWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        sysSharpWebView = (SharpWebView) findViewById(R.id.sysSharpWebView);
        sysSharpWebView.onLoadURL("http://jagoron24.com/");
    }
}
