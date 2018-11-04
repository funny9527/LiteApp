package com.app.light.componet.dom;

import com.szy.bridge.jsmodule.JsModule;

/**
 * Created by tical on 2018/8/7.
 */

public interface Page extends JsModule {
    String onLoad(String options, int index);
    void onReady(int index);
    void onShow(int index);
    void onHide(int index);
    void onUnload(int index);
    void onTabItemTap(int index, String id);
}
