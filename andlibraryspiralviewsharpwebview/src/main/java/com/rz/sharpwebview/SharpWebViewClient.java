package com.rz.sharpwebview;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rz.logger.LogWriter;

/**
 * Created by Rz Rasel on 2017-08-27.
 */

public class SharpWebViewClient extends WebViewClient {
    private boolean isOnResumeReload;
    private boolean isAlreadyLoaded;

    public SharpWebViewClient(boolean argIsOnResumeReload, boolean argIsAlreadyLoaded) {
        isOnResumeReload = argIsOnResumeReload;
        isAlreadyLoaded = argIsAlreadyLoaded;
    }

    @Override
    public void onPageStarted(WebView argView, String argURL, Bitmap argFavIcon) {
        if (!isOnResumeReload && isAlreadyLoaded)
            return;
        isAlreadyLoaded = true;
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
        LogWriter.Log("onLoadResource: " + argURL);
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
        LogWriter.Log("onPageFinished: " + argURL);
    }
}