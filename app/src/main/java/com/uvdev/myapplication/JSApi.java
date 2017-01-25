// Copyright (c) 2017 Magicleap. All right reserved.

package com.uvdev.myapplication;

import android.os.SystemClock;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *
 */
public class JSApi {

    private static final String SELF = new Gson().fromJson("{"
            + "\"username\": \"Yuval\","
            + "\"icon_url\": \"https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_120x44dp.png\""
            + "}", JsonObject.class).toString();

    @JavascriptInterface
    public String loadSelf() {
        Log.d("JSApi", "loadSelf()");
        SystemClock.sleep(500);
        Log.d("JSApi", "  returning " + SELF);
        return SELF;
    }
}
