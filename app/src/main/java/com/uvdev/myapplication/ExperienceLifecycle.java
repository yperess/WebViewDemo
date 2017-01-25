// Copyright (c) 2017 Magicleap. All right reserved.

package com.uvdev.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *
 */
public class ExperienceLifecycle {

    private static final String TAG = "ExpLife";

    private final WebView mView;
    private final JavascriptResultInterface mResultInterface;
    private int mState = 0;
    private boolean mPendingResume = false;

    public ExperienceLifecycle(@NonNull WebView view,
            @NonNull JavascriptResultInterface resultInterface) {
        mView = view;
        mResultInterface = resultInterface;
    }

    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        mState = isValidTransition(1, 0);
        String savedInstanceStateJson = "";
        if (savedInstanceState != null) {
            JsonObject jsonState = new JsonObject();
            for (String key : savedInstanceState.keySet()) {
                Object value = savedInstanceState.get(key);
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
            savedInstanceStateJson = new Gson().toJson(jsonState);
        }
        // Call void onExperienceCreated(stateJsonString)
        mView.loadUrl("javascript:onExperienceCreated('" + savedInstanceStateJson + "');");
        if (mPendingResume) {
            onResume();
        }
    }

    public void onResume() {
        Log.d(TAG, "onResume()");
        if (mState == 0) {
            // Not finished creating page.
            mPendingResume = true;
        } else {
            mState = isValidTransition(2, 1, 3);
            // Call void onExperienceResumed()
            mView.loadUrl("javascript:onExperienceResumed();");
            mPendingResume = false;
        }
    }

    public void onPause() {
        Log.d(TAG, "onPause()");
        mState = isValidTransition(3, 0, 2);
        // Call void onExperiencePaused()
        mView.loadUrl("javascript:onExperiencePaused();");
    }

    public void saveInstanceState(Bundle outState) {
        Log.d(TAG, "saveInstanceState()");
        // Call stateJsonString saveExperienceState()
        mView.loadUrl("javascript:HostResult.postResult(saveExperienceState());");
        if (!TextUtils.isEmpty(mResultInterface.result)) {
            outState.putString("state", mResultInterface.result);
        }
    }

    private int isValidTransition(int to, int... from) {
        for (int i = 0; i < from.length; ++i) {
            if (mState == from[i]) {
                return to;
            }
        }
        throw new IllegalStateException("Invalid transition from " + mState + " to " + to);
    }
}
