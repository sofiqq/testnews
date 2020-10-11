package com.sof.testnews;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    final static String FILE_NAME = "preferences";

    final static String PREF_NEWS = "news";

    private SharedPreferences preferences;

    public Preferences(Context context) {
        preferences = context.getSharedPreferences(FILE_NAME, 0);
    }

    private SharedPreferences.Editor getEditor() {
        return preferences.edit();
    }

    public void setSavedNews(String json) {
        getEditor().putString(PREF_NEWS, json).commit();
    }

    public String getSavedNews() {
        return preferences.getString(PREF_NEWS, "");
    }



}
