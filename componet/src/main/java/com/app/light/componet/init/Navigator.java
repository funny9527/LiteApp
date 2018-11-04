package com.app.light.componet.init;

import android.util.Log;

import com.szy.bridge.javamodule.NativeMethod;
import com.szy.bridge.javamodule.NativeModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by tical on 2018/8/9.
 */

public class Navigator extends NativeModule {
    private static final String TAG = "Navigator";
    private int index = 0;
    private List<String> pageList = new ArrayList<>();
    private Stack<Integer> indexStack = new Stack();
    private PageListener pageListener;

    public Navigator(PageListener listener) {
        pageListener = listener;
    }

    @Override
    public String getName() {
        return "Navigator";
    }

    @NativeMethod
    public void toNext(String page) {
        if (pageList.contains(page)) {
            int id = Collections.binarySearch(pageList, page);

            if (id == index) {
                return;
            }

            indexStack.push(id);
            if (pageListener != null) {
                pageListener.toNext(id);
            }
        }
    }

    @NativeMethod
    public void toBack() {
        if (indexStack == null || indexStack.empty()) {
            return;
        }

        Log.d(TAG, "toBack .");
        int id = indexStack.pop();
        Log.d(TAG, "toBack .. " + id + " " + pageListener);
        if (pageListener != null && id > 0) {
            pageListener.toBack(id - 1);
        }
    }

    public void setIndex(int i) {
        index = i;
    }

    public int getIndex() {
        return index;
    }

    public void setPageList(List<String> list) {
        pageList = list;
    }

    public interface PageListener {
        void toNext(int index);
        void toBack(int index);
    }
}
