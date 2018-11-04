package com.app.light.componet.view;

import com.app.light.componet.activity.AppManager;
import com.szy.bridge.javamodule.NativeMethod;
import com.szy.bridge.javamodule.NativeModule;

/**
 * Created by tical on 2018/8/8.
 */

public class Toast extends NativeModule {
    @Override
    public String getName() {
        return "Toast";
    }

    @NativeMethod
    public void show(String text, int time) {
        if (AppManager.getCurrentActivity() != null) {
            android.widget.Toast.makeText(AppManager.getCurrentActivity(), text, time).show();
        }
    }
}
