package com.app.light.componet.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import com.app.light.componet.R;
import com.app.light.componet.dom.Page;
import com.app.light.componet.dom.VirtualDom;
import com.app.light.componet.init.AppInfo;
import com.app.light.componet.init.Navigator;
import com.app.light.componet.log.Console;
import com.app.light.componet.view.Toast;
import com.app.light.componet.view.RootView;
import com.szy.bridge.bridge.JsExecutor;
import com.szy.bridge.javamodule.NativeModule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BaseLiteActivity extends FragmentActivity implements Navigator.PageListener {

    public static final String TAG = "page";
    public static final String BUNDLE_URI = "bundle_uri";
    public static final String DO_FINISH = "do_finish";
    private List<String> pageList = new ArrayList<>();
    private Navigator navigator;
    private RootView rootView;

    public String getAppUri() {
        Intent intent = getIntent();
        String name = intent.getStringExtra(BUNDLE_URI);
        Log.d(TAG, "name = " + name);
        return name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate " + this.getClass().getSimpleName());
        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lite_app);

        rootView = (RootView) findViewById(R.id.container);

        if (pageList.size() > navigator.getIndex()) {
            buildPage(navigator.getIndex());
        }
    }

    private void init() {
        //init native modules
        ArrayList<NativeModule> list = new ArrayList<>();
        list.add(new Console());
        list.add(new Toast());
        navigator = new Navigator(this);
        list.add(navigator);

        //init js modules
        JsExecutor.getInstance().init(this.getApplicationContext(), getBundle(this), list);

        //init app info
        JsExecutor.getInstance().getJsModule(AppInfo.class).init();

        //init pages
        String pages = JsExecutor.getInstance().getJsModule(AppInfo.class).getPages();

        //init styles
        String window = JsExecutor.getInstance().getJsModule(AppInfo.class).getWindowStyle();
        try {
            JSONObject attr = new JSONObject(window);
            Object obj = attr.opt(VirtualDom.Window.backgroundTextStyle.name());
            if (obj != null) {
                String theme = (String) obj;
                if (VirtualDom.Theme.light.name().equals(theme)) {
                    this.setTheme(android.R.style.Theme_Light);
                } else {
                    this.setTheme(android.R.style.Theme_Black);
                }
            }

            obj = attr.opt(VirtualDom.Window.navigationBarTextStyle.name());
            if (obj != null) {
                this.setTitleColor(Color.parseColor((String) obj));
            }

            JSONArray array = new JSONArray(pages);
            for (int i = 0; i < array.length(); i++) {
                if (array.get(i) != null) {
                    pageList.add((String) array.get(i));
                }
            }

            navigator.setPageList(pageList);

            if (pageList.size() > navigator.getIndex()) {
                this.setTitle(attr.getString(VirtualDom.Window.navigationBarTitleText.name()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getBundle(Context c) {
        InputStream in = null;
        try {
            in = c.getResources().getAssets().open(getAppUri());
            int length = in.available();
            byte[]  buffer = new byte[length];
            in.read(buffer);
            String str = new String(buffer, "utf-8");

            return str;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        AppManager.setCurrentActivity(this);
        JsExecutor.getInstance().getJsModule(App.class).onLaunch();

        if (pageList.size() > navigator.getIndex()) {
            JsExecutor.getInstance().getJsModule(Page.class).onShow(navigator.getIndex());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        AppManager.setCurrentActivity(null);
        JsExecutor.getInstance().getJsModule(App.class).onHide();
        JsExecutor.getInstance().getJsModule(Page.class).onHide(navigator.getIndex());
    }

    @Override
    public void toNext(int index) {
        if (rootView != null) {
            rootView.removeAllViews();
            buildPage(index);
        }
    }

    @Override
    public void toBack(int index) {
        if (navigator != null && rootView != null) {
            rootView.removeAllViews();
            buildPage(index);
        }
    }

    private void buildPage(int index) {
        View view = VirtualDom.buildView(this, index);
        if (view != null) {
            rootView.addView(view);
        }
        JsExecutor.getInstance().getJsModule(Page.class).onReady(index);
    }

    @Override
    public void onBackPressed() {
        Intent intent = this.getPackageManager().getLaunchIntentForPackage("sample.szy.com.lite");
        this.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        if (intent.getBooleanExtra(DO_FINISH, false)) {
            Log.d(TAG, "onNewIntent need finish");
            finish();
            Process.killProcess(Process.myPid());
        }
    }
}
