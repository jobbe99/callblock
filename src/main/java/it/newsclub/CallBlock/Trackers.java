package it.newsclub.CallBlock;


import it.newsclub.CallBlock.R;

import java.util.HashMap;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import android.app.Application;

public class Trackers extends Application
{
    public enum TrackerName
    {
        APP_TRACKER, // Tracker used only in this
                        // app.
        GLOBAL_TRACKER, // Tracker used by all the
                        // apps from a company.
                        // eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all
                            // ecommerce
                            // transactions from a
                            // company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId)
    {
        if (!mTrackers.containsKey(trackerId))
        {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            if( trackerId == TrackerName.GLOBAL_TRACKER )
            {
                mTrackers.put(trackerId, analytics.newTracker(R.xml.global_tracker));
            }
        }
        return mTrackers.get(trackerId);
    }
}
