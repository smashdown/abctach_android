package com.abctech.blogtalking.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.abctech.blogtalking.api.BTRestService;

import net.danlew.android.joda.JodaTimeAndroid;

import butterknife.ButterKnife;

public abstract class BaseApp extends MultiDexApplication {
    public static final int PAGE_ITEM_COUNT = 20;

    public static final String LOG_TAG = BaseApp.class.getSimpleName();

    protected static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppContext = getApplicationContext();

        // JodaTime
        JodaTimeAndroid.init(this);

        ButterKnife.setDebug(true);
    }

    public static Context getAppContext() {
        if (mAppContext == null)
            mAppContext = getAppContext();

        return mAppContext;
    }

    public static String makeImageUrl(String imageId) {
        return BTRestService.BASE_URL + "images/" + imageId;
    }
}