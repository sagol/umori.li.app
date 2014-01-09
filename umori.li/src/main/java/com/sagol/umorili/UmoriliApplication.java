package com.sagol.umorili;

import android.app.Application;
import android.content.Context;

public class UmoriliApplication extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        UmoriliApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return UmoriliApplication.context;
    }
}
