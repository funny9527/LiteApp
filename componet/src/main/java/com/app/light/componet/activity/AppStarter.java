package com.app.light.componet.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tical on 2018/4/22.
 */

public class AppStarter {
    private static final String TAG = "start";

    private static class AppItem {
        public AppItem(Class cls, long time) {
            this.name = cls;
            this.time = time;
        }
        Class name;
        long time;
    }

    private static AppItem[] SLOT = new AppItem[] {
            new AppItem(LiteActivity1.class, 0),
            new AppItem(LiteActivity2.class, 0),
            new AppItem(LiteActivity3.class,0),
            new AppItem(LiteActivity4.class,0),
            new AppItem(LiteActivity5.class,0)
    };
    private Map<Integer, String> processNameList = new HashMap<>(SLOT.length);
    private Map<Integer, AppItem> appList = new LinkedHashMap<>(SLOT.length);

    private static AppStarter instance = new AppStarter();

    private AppStarter() {
        processNameList.put(SLOT[0].name.hashCode(), "appbrand1");
        processNameList.put(SLOT[1].name.hashCode(), "appbrand2");
        processNameList.put(SLOT[2].name.hashCode(), "appbrand3");
        processNameList.put(SLOT[3].name.hashCode(), "appbrand4");
        processNameList.put(SLOT[4].name.hashCode(), "appbrand5");
    }

    public static AppStarter getInstance() {
        return instance;
    }

    public void start(final Context context, final String uri) {
        Log.d(TAG, "start " + uri);
        int index = uri.hashCode();
        Class cls = null;
        boolean needRecycle = false;

        if (appList.get(index) != null) {
            AppItem item = appList.get(index);
            cls = item.name;
            item.time = System.currentTimeMillis();
        } else {
            long min = Long.MAX_VALUE;
            int pos = -1;
            for (int i = 0; i < SLOT.length; i++) {
                AppItem item = SLOT[i];
                if (item.time < min) {
                    min = item.time;
                    pos = i;
                }
            }

            if (pos >= 0) {
                AppItem item = SLOT[pos];
                item.time = System.currentTimeMillis();
                cls = item.name;

                Iterator<Map.Entry<Integer, AppItem>> iterator = appList.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, AppItem> next = iterator.next();
                    if (next != null && next.getValue() != null && next.getValue().name == cls) {
                        needRecycle = true;
                        iterator.remove();
                        Log.d(TAG, "remove " + cls.getSimpleName());
                        Intent intent = new Intent(context, cls);
                        intent.putExtra(BaseLiteActivity.DO_FINISH, true);
                        context.startActivity(intent);
                        Log.d(TAG, "finish ... " + cls);
                        break;
                    }
                }

                appList.put(index, item);
            }
        }

        if (cls != null) {
            Log.d(TAG, "finally start " + cls.getSimpleName() + "  needRecycle = " + needRecycle);
            if (needRecycle) {
                new StartTask(context, cls, processNameList, uri).execute();
            } else {
                Intent intent = new Intent(context, cls);
                intent.putExtra(BaseLiteActivity.BUNDLE_URI, uri);
                context.startActivity(intent);
            }
        }
    }

    private static class StartTask extends AsyncTask<Integer, String, Boolean> {

        private Context mContext;
        private Class aClass;
        private Map<Integer, String> map;
        private String uri;

        public StartTask(Context context, Class cls, Map<Integer, String> map, String uri) {
            mContext = context;
            aClass = cls;
            this.map = map;
            this.uri = uri;
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            boolean stopped = false;
            while (!stopped) {
                boolean status = getPid(mContext, aClass);
                if (status) {
                    try {
                        Thread.sleep(300);
                    } catch (Exception e) {}
                } else {
                    stopped = true;
                }
            }
            return stopped;
        }

        @Override
        public void onPostExecute(Boolean died) {
            if (died) {
                Intent intent = new Intent(mContext, aClass);
                intent.putExtra(BaseLiteActivity.BUNDLE_URI, uri);
                mContext.startActivity(intent);
            }
        }

        private boolean getPid(final Context context, Class cls) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (am != null) {
                List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo process : lists) {
                    if (process.processName != null && process.processName.endsWith(map.get(cls.hashCode()))) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
