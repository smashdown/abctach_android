package com.abctech.blogtalking.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class BTPreferences {
    public static final String SORT_BY = "SORT_BY";

    private static BTPreferences     instance;
    protected      SharedPreferences mSharedPreferences;

    public BTPreferences(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static BTPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new BTPreferences(context);
        }
        return instance;
    }

    public void clear() {
        mSharedPreferences.edit().clear().commit();
    }

    public void dump() {
        for (String key : mSharedPreferences.getAll().keySet()) {
            Log.d("JJY", key + " = " + mSharedPreferences.getAll().get(key));
        }
    }

    public String getSortBy() {
        return mSharedPreferences.getString(SORT_BY, "createdDate");
    }

    public void setSortBy(String value) {
        mSharedPreferences.edit().putString(SORT_BY, value).apply();
    }
}
