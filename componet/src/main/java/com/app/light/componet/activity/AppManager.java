package com.app.light.componet.activity;

import android.app.Activity;

/**
 * Created by tical on 2018/8/9.
 */

public class AppManager {
    private static Activity currentActivity;

    public static void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }
}
