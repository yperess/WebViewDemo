// Copyright (c) 2017 Magicleap. All right reserved.

package com.uvdev.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 */
public class ExperienceWebView extends WebView {

    private final ExperienceLifecycle mLifecycle;
    private final ExperienceClient mExperienceClient;
    private UnzipTask mUnzipTask;
    private final JavascriptResultInterface mResultInterface = new JavascriptResultInterface();

    public ExperienceWebView(Context context) {
        this(context, null /* attrs */);
    }

    public ExperienceWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0 /* defStyleAttr */);
    }

    public ExperienceWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLifecycle = new ExperienceLifecycle(this, mResultInterface);
        mExperienceClient = new ExperienceClient(mLifecycle);
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        setWebViewClient(mExperienceClient);
        setWebChromeClient(new ExperienceChromeClient());
        addJavascriptInterface(mResultInterface, "HostResult");
        addJavascriptInterface(new JSApi(), "AppUser");
    }

    public void loadExperience(@NonNull InputStream experienceZip, Bundle savedInstanceState) {
        if (mUnzipTask != null) {
            mUnzipTask.cancel(true);
        }
        getFilesDir().delete();
        mUnzipTask = new UnzipTask();
        mExperienceClient.setSavedInstanceState(savedInstanceState);
        mUnzipTask.execute(experienceZip);
    }

    public void saveInstanceState(Bundle outState) {
        mLifecycle.saveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLifecycle.onResume();
    }

    public void onPause() {
//        evaluateJavascript("", null);
        mLifecycle.onPause();
    }

    public void onExperienceUnzipped(boolean success) {
        if (!success) {
            throw new RuntimeException("Failed to unzip experience");
        }

        String json = loadStringFromFile(new File(getFilesDir(), "manifest.json"));
        Log.d("Test", json);
        setManifest(new Gson().fromJson(json, JsonObject.class));
    }

    private File getFilesDir() {
        return new File(getContext().getFilesDir(), "experience");
    }

    private void setManifest(JsonObject json) {
        String startingPage = json.get("initial_page").getAsString();
        File file = new File(getFilesDir(), startingPage);
        loadUrl(Uri.fromFile(file).toString());
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

    private final class UnzipTask extends AsyncTask<InputStream, Void, Boolean> {
        @Override
        protected Boolean doInBackground(InputStream... params) {
            try {
                return unzip(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            onExperienceUnzipped(result);
        }

        private boolean unzip(InputStream zipFile) throws IOException {
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(zipFile));
            try {
                ZipEntry zipEntry;
                int count;
                byte[] buffer = new byte[8192];

                while ((zipEntry = zis.getNextEntry()) != null) {
                    File file = new File(getFilesDir(), zipEntry.getName());
                    File dir = zipEntry.isDirectory() ? file : file.getParentFile();
                    if (!dir.isDirectory() && !dir.mkdirs())
                        throw new FileNotFoundException("Failed to ensure directory: " +
                                dir.getAbsolutePath());
                    if (zipEntry.isDirectory())
                        continue;
                    FileOutputStream fout = new FileOutputStream(file);
                    try {
                        while ((count = zis.read(buffer)) != -1)
                            fout.write(buffer, 0, count);
                    } finally {
                        fout.close();
                    }
                }
            } finally {
                zis.close();
            }
            return true;
        }
    }
}
