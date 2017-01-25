// Copyright (c) 2017 Magicleap. All right reserved.

package com.uvdev.myapplication;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *
 */
public class ExperienceClient extends WebViewClient {

    private Bundle mSavedInstanceState;

    public void setSavedInstanceState(Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (mSavedInstanceState == null) {
            view.loadUrl("javascript:onExperienceCreated('');");
            return;
        }
        JsonObject jsonState = new JsonObject();
        for (String key : mSavedInstanceState.keySet()) {
            Object value = mSavedInstanceState.get(key);
            if (value instanceof String) {
                jsonState.addProperty(key, (String) value);
            } else if (value instanceof Number) {
                jsonState.addProperty(key, (Number) value);
            } else if (value instanceof Boolean) {
                jsonState.addProperty(key, (Boolean) value);
            } else if (value instanceof Character) {
                jsonState.addProperty(key, (Character) value);
            } else {
                throw new IllegalArgumentException("Invalid type in state: "
                        + value.getClass().getName());
            }
        }
        view.loadUrl("javascript:onExperienceCreated('" + new Gson().toJson(jsonState) + "');");
    }
}
