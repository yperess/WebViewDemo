// Copyright (c) 2017 Magicleap. All right reserved.

package com.uvdev.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
        String json = loadStringFromFile(new File(getContext().getFilesDir(), "manifest.json"));
        Log.d("Test", json);
        setManifest(new Gson().fromJson(json, JsonObject.class));
    }

    private void setManifest(JsonObject json) {
        String startingPage = json.get("initial_page").getAsString();
        loadData(loadStringFromFile(new File(getContext().getFilesDir(), startingPage)),
                "text/html", "UTF-8");
    }

    private String loadStringFromFile(File file) {
        try {
            FileInputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
