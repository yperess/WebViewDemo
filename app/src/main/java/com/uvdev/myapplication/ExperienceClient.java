// Copyright (c) 2017 Magicleap. All right reserved.

package com.uvdev.myapplication;

import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class ExperienceClient extends WebViewClient {

    private final ExperienceLifecycle mLifecycle;
    private Bundle mSavedInstanceState = null;

    public ExperienceClient(ExperienceLifecycle lifecycle) {
        mLifecycle = lifecycle;
    }

    public void setSavedInstanceState(Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        injectApi(view);
        mLifecycle.onCreate(mSavedInstanceState);
    }

    private void injectApi(WebView view) {
        InputStream input;
        try {
            input = view.getContext().getAssets().open("api.js");
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            // String-ify the script byte-array using BASE64 encoding !!!
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            view.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
