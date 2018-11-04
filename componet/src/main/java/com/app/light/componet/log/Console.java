package com.app.light.componet.log;

import android.util.Log;

import com.szy.bridge.javamodule.NativeMethod;
import com.szy.bridge.javamodule.NativeModule;

/**
 * Created by tical on 2018/7/28.
 */

public class Console extends NativeModule {

    @NativeMethod
    public void log(String param) {
        Log.d("console", param);
    }

    @Override
    public String getName() {
        return "Console";
    }
}
