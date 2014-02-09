package com.sagol.umorili;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class UmoriliApplication extends Application {

    private static Context context;
    private static boolean first_cache;
    private static UmoriliDataContent udc_sources;

    public void onCreate(){
        super.onCreate();
        UmoriliApplication.context     = getApplicationContext();
        UmoriliApplication.first_cache = false;
        UmoriliApplication.udc_sources = null;
    }

    public static Context getAppContext() {
        return UmoriliApplication.context;
    }

    public static boolean getFirstCache() {
        return UmoriliApplication.first_cache;
    }
    public static void setFirstCache(boolean flag) {
        UmoriliApplication.first_cache = flag;
        return;
    }
    public static UmoriliDataContent getUDCSources() {
        return UmoriliApplication.udc_sources;
    }
    public static void setUDCSources(UmoriliDataContent udc) {
        UmoriliApplication.udc_sources = udc;
        return;
    }

}
