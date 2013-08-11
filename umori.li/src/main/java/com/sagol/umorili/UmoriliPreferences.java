package com.sagol.umorili;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UmoriliPreferences {

    private SharedPreferences sharedPreferences;

    public UmoriliPreferences (Context ctx) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public String getString (String pref) {
       if (sharedPreferences.contains(pref)) {
           return sharedPreferences.getString(pref, null);
       } else {
           return null;
       }
    }

    public Integer getInt (String pref) {
        if (sharedPreferences.contains(pref)) {
            return sharedPreferences.getInt(pref, -1);
        } else {
            return -1;
        }
    }

    public Boolean getBoolean (String pref) {
        if (sharedPreferences.contains(pref)) {
            return sharedPreferences.getBoolean(pref, false);
        } else {
            return false;
        }
    }

    public void setString (String pref, String param) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(pref, param);
        editor.commit();
    }

    public void setInt (String pref, Integer param) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(pref, param);
        editor.commit();
    }

    public void setBoolean (String pref, Boolean param) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(pref, param);
        editor.commit();
    }

}
