package com.codepeaker.hora.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private SharedPref() {
    }

    private static SharedPref sharedPref;

    public static synchronized SharedPref getInstance() {
        if (sharedPref == null) {
            sharedPref = new SharedPref();
        }
        return sharedPref;
    }

    public void setSharedprefrences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.HORA_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setSharedprefrences(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.HORA_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanpreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.HORA_SP, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public String getStringpreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.HORA_SP, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

}
