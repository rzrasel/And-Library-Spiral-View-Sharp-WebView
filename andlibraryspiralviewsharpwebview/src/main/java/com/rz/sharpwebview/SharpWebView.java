package com.rz.sharpwebview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import java.lang.reflect.Method;

/**
 * Created by Rz Rasel on 2017-08-23.
 */

public class SharpWebView extends WebView {
    //UTF-8 Charset
    public static final String CHARSET_UTF8 = "UTF-8";
    //Path to webview html template
    public static final String HTML_TEMPLATE_ERROR = "html/template_webview_error.html";
    //Substring where the error message will be placed
    public static final String HTML_TEMPLATE_CONTENT = "##CONTENT##";
    private ZoomButtonsController zoomButtons;

    public SharpWebView(Context argContext) {
        super(argContext.getApplicationContext());
        onInit();
        enablePlugins(false);
    }

    public SharpWebView(Context argContext, AttributeSet argAttrs) {
        super(argContext.getApplicationContext(), argAttrs);
        onInit();
        enablePlugins(false);
    }

    public SharpWebView(Context argContext, AttributeSet argAttrs, int argDefStyle) {
        super(argContext.getApplicationContext(), argAttrs, argDefStyle);
        onInit();
        enablePlugins(false);
    }

    private void onInit() {
        setWebChromeClient(new OnWebChromeClient());
        setWebViewClient(new OnWebViewClient());
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        //webSettings.setBuiltInZoomControls(true);
        /*getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getSettings().setDisplayZoomControls(false);
        } else {
            try {
                Method method = getClass()
                        .getMethod("getZoomButtonsController");
                zoomButtons = (ZoomButtonsController) method.invoke(this);
            } catch (Exception e) {
                // pass
            }
        }*/
        getSettings().setBuiltInZoomControls(true);
        getSettings().setDisplayZoomControls(false);
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

    private class OnWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView argView, String argURL, Bitmap argFavIcon) {
            super.onPageStarted(argView, argURL, argFavIcon);
            //System.out.println("your current url when webpage loading.." + url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView argView, String argURL) {
            /*final Uri uri = Uri.parse(url);
            return handleUri(uri);*/
            //System.out.println("when you click on any interlink on webview that time you got url :-" + url);
            //return super.shouldOverrideUrlLoading(view, url);
            argView.loadUrl(argURL);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView argView, WebResourceRequest argRequest) {
            /*final Uri uri = request.getUrl();
            return handleUri(uri);*/
            //System.out.println("when you click on any interlink on webview that time you got url :-" + url);
            argView.loadUrl(argRequest.getUrl().toString());
            return true;
        }

        //Show loader on url load
        public void onLoadResource(WebView argView, String argURL) {
            /*if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(ActSplash.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }*/
            super.onLoadResource(argView, argURL);
        }

        public void onPageFinished(WebView argView, String argURL) {
            /*try {
                /-*if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                progressDialog.dismiss();*-/
                amountOfCurProg = 100;
                sysPbProgress.setProgress(amountOfCurProg);
            } catch (Exception exception) {
                exception.printStackTrace();
            }*/
            //System.out.println("your current url when webpage loading.. finish" + url);
            super.onPageFinished(argView, argURL);
        }
    }

    private class OnWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView argView, int argNewProgress) {
            /*if (argNewProgress >= 100) {
                mProgressbar.setVisibility(GONE);
            } else {
                if (mProgressbar.getVisibility() == GONE)
                    mProgressbar.setVisibility(VISIBLE);
                mProgressbar.setProgress(newProgress);
            }*/
            System.out.println("Progress: " + argNewProgress);
            super.onProgressChanged(argView, argNewProgress);
        }
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

    public void onLoadURL(String argURL) {
        this.loadUrl(argURL);
    }
}