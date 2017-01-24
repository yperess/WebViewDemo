// Copyright (c) 2017 Magicleap. All right reserved.

package com.uvdev.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 */
public class MyWebView extends WebView {

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void start() {
        try {
            FileInputStream is =
                    new FileInputStream(new File(getContext().getFilesDir(), "manifest.json"));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.d("Test", json);
            setManifest(new JSONObject(json));
        } catch (IOException|JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void setManifest(JSONObject json) {

    }
}
