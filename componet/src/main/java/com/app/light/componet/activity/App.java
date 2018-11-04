package com.app.light.componet.activity;

import com.szy.bridge.jsmodule.JsModule;

/**
 * Created by tical on 2018/8/2.
 */

public interface App extends JsModule {
    void onLaunch();
    void onHide();
}
