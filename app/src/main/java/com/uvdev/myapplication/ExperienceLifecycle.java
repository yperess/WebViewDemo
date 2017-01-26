// Copyright (c) 2017 Magicleap. All right reserved.

package com.uvdev.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

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
            savedInstanceStateJson = savedInstanceState.getString("state", "");
        }
        Log.d(TAG, "    '" + savedInstanceStateJson + "'");
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
        Log.d(TAG, "    '" + mResultInterface.result + "'");
        String result = mResultInterface.waitForResult();
        if (!TextUtils.isEmpty(result)) {
            outState.putString("state", result);
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
