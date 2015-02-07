package com.sagol.umorili;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.GoogleAnalytics;

import java.util.HashMap;

public class UmoriliApplication extends Application {

    private static Context context;
    private static boolean first_cache;
    private static UmoriliDataContent udc_sources;

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     *
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
                    : analytics.newTracker(R.xml.global_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

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
