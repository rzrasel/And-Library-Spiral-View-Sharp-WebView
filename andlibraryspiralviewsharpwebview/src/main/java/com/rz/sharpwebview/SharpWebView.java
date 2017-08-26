package com.rz.sharpwebview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import com.rz.logger.LogWriter;

import java.lang.reflect.Method;

/**
 * Created by Rz Rasel on 2017-08-23.
 */

public class SharpWebView extends WebView {
    private Context context;
    //UTF-8 Charset
    public static final String CHARSET_UTF8 = "UTF-8";
    //Path to webview html template
    public static final String HTML_TEMPLATE_ERROR = "html/template_webview_error.html";
    //Substring where the error message will be placed
    public static final String HTML_TEMPLATE_CONTENT = "##CONTENT##";
    private ZoomButtonsController zoomButtons;
    //|----|
    private boolean isAlreadyLoaded = false;
    //|----|
    private boolean isOnResumeReload = true;
    private boolean isJavaScriptEnabled = false;
    private boolean displayZoomControls = false;
    private boolean isGestureZoomable = false;
    private boolean isCacheEnabled = false;
    //|----|

    public SharpWebView(Context argContext) {
        super(argContext.getApplicationContext());
        context = argContext;
        onInit();
        enablePlugins(false);
    }

    public SharpWebView(Context argContext, AttributeSet argAttrs) {
        super(argContext.getApplicationContext(), argAttrs);
        context = argContext;
        TypedArray typedArray = argContext.obtainStyledAttributes(argAttrs, R.styleable.RzSharpWebView, 0, 0);
        isJavaScriptEnabled = typedArray.getBoolean(R.styleable.RzSharpWebView_isJavaScriptEnabled, false);
        displayZoomControls = typedArray.getBoolean(R.styleable.RzSharpWebView_displayZoomControls, false);
        isGestureZoomable = typedArray.getBoolean(R.styleable.RzSharpWebView_isGestureZoomable, false);
        isOnResumeReload = typedArray.getBoolean(R.styleable.RzSharpWebView_isOnResumeReload, true);
        isCacheEnabled = typedArray.getBoolean(R.styleable.RzSharpWebView_isCacheEnabled, false);
        typedArray.recycle();
        onInit();
        enablePlugins(false);
    }

    public SharpWebView(Context argContext, AttributeSet argAttrs, int argDefStyle) {
        super(argContext.getApplicationContext(), argAttrs, argDefStyle);
        context = argContext;
        onInit();
        enablePlugins(false);
    }

    private void onInit() {
        setWebChromeClient(new SharpWebChromeClient());
        setWebViewClient(new SharpWebViewClient(isOnResumeReload, isAlreadyLoaded));
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(isJavaScriptEnabled);
        //webSettings.setBuiltInZoomControls(true);
        /*getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getSettings().setDisplayZoomControls(false);
        } else {
            try {
                Method method = getClass().getMethod("getZoomButtonsController");
                zoomButtons = (ZoomButtonsController) method.invoke(this);
            } catch (Exception e) {
                // pass
            }
        }*/
        getSettings().setDisplayZoomControls(displayZoomControls);
        getSettings().setBuiltInZoomControls(isGestureZoomable);
        if (isCacheEnabled) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            webSettings.setAppCacheEnabled(true);
        }
        LogWriter.Log("URL: " + getUrl());
    }

    @Override
    public boolean onTouchEvent(MotionEvent argMotionEvent) {
        boolean result = super.onTouchEvent(argMotionEvent);
        if (zoomButtons != null) {
            zoomButtons.setVisible(false);
            zoomButtons.getZoomControls().setVisibility(View.GONE);
        }
        return result;
    }

    public void loadErrorPage(String argError) {
        try {
            // Load HTML template from assets
            String htmlContent = "hhhhhhhhhhhhhh faaaaaaaaaaaaaaaa baaaaaaaaaaaaaaaa";
            /*InputStream is = getContext().getAssets().open(HTML_TEMPLATE_ERROR);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                htmlContent += line;
            }
            br.close();*/

            htmlContent = htmlContent.replace(HTML_TEMPLATE_CONTENT, argError);

            loadData(htmlContent, "text/html; charset=" + CHARSET_UTF8, CHARSET_UTF8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion <= Build.VERSION_CODES.JELLY_BEAN) {
            invalidate();
        }
        super.onDraw(canvas);
    }

    protected void enablePlugins(final boolean argEnabled) {
        // Android 4.3 and above has no concept of plugin states
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return;
        }

        //if (SupportVersion.Froyo()) {
        if (currentApiVersion >= Build.VERSION_CODES.FROYO) {
            // Note: this is needed to compile against api level 18.
            try {
                Class<Enum> pluginStateClass = (Class<Enum>) Class.forName("android.webkit.WebSettings$PluginState");

                Class<?>[] parameters = {pluginStateClass};
                Method method = getSettings().getClass().getDeclaredMethod("setPluginState", parameters);

                Object pluginState = Enum.valueOf(pluginStateClass, argEnabled ? "ON" : "OFF");
                method.invoke(getSettings(), pluginState);
            } catch (Exception e) {
                System.out.println("Unable to modify WebView plugin state.");
            }
        } else {
            try {
                Method method = Class.forName("android.webkit.WebSettings").getDeclaredMethod("setPluginsEnabled", boolean.class);
                method.invoke(getSettings(), argEnabled);
            } catch (Exception e) {
                System.out.println("Unable to " + (argEnabled ? "enable" : "disable")
                        + "WebSettings plugins for BaseWebView.");
            }
        }
    }

    public void onBackPressed() {
    }

    @Override
    public boolean onKeyDown(int argKeyCode, KeyEvent argKeyEvent) {
        //LogWriter.Log(argKeyCode + " - " + argKeyEvent);
        if ((argKeyCode == KeyEvent.KEYCODE_BACK) && canGoBack()) {
            goBack();
            return true;
        } else {
            ((Activity) context).finish();
        }
        return super.onKeyDown(argKeyCode, argKeyEvent);
    }

    public void onLoadURL(String argURL) {
        this.loadUrl(argURL);
    }
}