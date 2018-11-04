package com.app.light.componet.init;

import com.szy.bridge.jsmodule.JsModule;

/**
 * Created by tical on 2018/7/28.
 */

/**
 * js application info
 */
public interface AppInfo extends JsModule {
    void init();
    String getPages();
    String getWindowStyle();
}
