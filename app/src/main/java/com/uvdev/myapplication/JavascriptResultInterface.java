// Copyright (c) 2017 Magicleap. All right reserved.

package com.uvdev.myapplication;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 *
 */
public class JavascriptResultInterface {

    String result;

    @JavascriptInterface
    public void postResult(String result) {
        Log.d("JS", "result = '" + result + "'");
        this.result = result;
    }
}
