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
    public synchronized void postResult(String result) {
        Log.d("JS", "result = '" + result + "'");
        this.result = result;
        notify();
    }

    public synchronized String waitForResult() {
        Log.d("JS", "waiting for result...");
        try {
            wait();
        } catch (InterruptedException e) {}
        return result;
    }
}
