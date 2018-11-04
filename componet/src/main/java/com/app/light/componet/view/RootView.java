package com.app.light.componet.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by tical on 2018/7/28.
 */

public class RootView extends LinearLayout {

    public RootView(Context context) {
        super(context);
    }

    public RootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RootView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
