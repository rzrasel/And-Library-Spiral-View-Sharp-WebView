package com.rz.sharpwebview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.rz.logger.LogWriter;

/**
 * Created by Rz Rasel on 2017-08-27.
 */

public class SharpWebChromeClient  extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView argView, int argNewProgress) {
        /*if (argNewProgress >= 100) {
            mProgressbar.setVisibility(GONE);
        } else {
            if (mProgressbar.getVisibility() == GONE)
                mProgressbar.setVisibility(VISIBLE);
            mProgressbar.setProgress(newProgress);
        }*/
        LogWriter.Log("Progress: " + argNewProgress);
        super.onProgressChanged(argView, argNewProgress);
    }
}