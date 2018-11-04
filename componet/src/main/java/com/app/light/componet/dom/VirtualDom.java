package com.app.light.componet.dom;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.light.componet.R;
import com.szy.bridge.bridge.JsExecutor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by tical on 2018/8/8.
 */

public class VirtualDom {
    public static final String TAG = "dom";

    public static View buildView(Context context, final int index) {
        String dom = JsExecutor.getInstance().getJsModule(Page.class).onLoad("", index);
        Log.d(TAG, dom);
        if (!TextUtils.isEmpty(dom)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(new ByteArrayInputStream(dom.getBytes()));
                Element root = document.getDocumentElement();
                NodeList list = root.getChildNodes();
                Log.d(TAG, "root node " + root.getTagName());
                LinearLayout rootView = (LinearLayout) parseView(context, root, index);
                for (int i = 0; i < list.getLength(); i++) {
                    Element node = (Element) list.item(i);
                    Log.d(TAG, "node " + node.getTagName());
                    rootView.addView(parseView(context, node, index));
                }

                return rootView;
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                Log.e(TAG, "", e);
            } catch (SAXException e) {
                e.printStackTrace();
                Log.e(TAG, "", e);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "", e);
            }
        }

        return null;
    }

    public static View parseView(Context context, final Element element, final int index) {
        if (Dom.view.name().equals(element.getTagName())) {
            LinearLayout layout = new LinearLayout(context);
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            if (element.hasAttribute(Attr.width.name())) {
                width = Integer.parseInt(element.getAttribute(Attr.width.name()).replace(Attr.px.name(), ""));
            }
            if (element.hasAttribute(Attr.height.name())) {
                height = Integer.parseInt(element.getAttribute(Attr.height.name()).replace(Attr.px.name(), ""));
            }
            if (element.hasAttribute(Attr.color.name())) {
                layout.setBackgroundColor(Color.parseColor(element.getAttribute(Attr.color.name())));
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(layoutParams);
            return layout;
        } else if (Dom.button.name().equals(element.getTagName())) {
            View view = new Button(context);
            if (element.hasAttribute(Attr.bindtap.name())) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JsExecutor.getInstance().getJsModule(Page.class).onTabItemTap(index, element.getAttribute(Attr.bindtap.name()));
                    }
                });
            }

            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            if (element.hasAttribute(Attr.width.name())) {
                width = Integer.parseInt(element.getAttribute(Attr.width.name()).replace(Attr.px.name(), ""));
            }
            if (element.hasAttribute(Attr.height.name())) {
                height = Integer.parseInt(element.getAttribute(Attr.height.name()).replace(Attr.px.name(), ""));
            }
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, height);

            view.setLayoutParams(lp);
            ((Button) view).setText(element.getTextContent());
            return view;
        }
        return null;
    }

    public enum Dom {
        view,
        button,
    }

    public enum Window {
        backgroundTextStyle,
        navigationBarTextStyle,
        navigationBarTitleText
    }

    public enum Theme {
        light,
    }

    public enum Attr {
        width,
        height,
        color,
        bindtap,
        px
    }
}
