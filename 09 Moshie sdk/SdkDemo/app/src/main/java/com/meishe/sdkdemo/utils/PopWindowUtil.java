package com.meishe.sdkdemo.utils;

import android.app.Activity;
import android.view.Gravity;

import com.meishe.sdkdemo.selectmedia.view.CustomPopWindow;

/**
 * Created by CaoZhiChao on 2018/11/13 09:48
 */
public class PopWindowUtil {
    private static final PopWindowUtil ourInstance = new PopWindowUtil();

    public static PopWindowUtil getInstance() {
        return ourInstance;
    }

    private PopWindowUtil() {
    }
    public void show(Activity activity, int layoutId, CustomPopWindow.OnViewClickListener listener){
        new CustomPopWindow.PopupWindowBuilder(activity)
                .setView(layoutId)
                .setViewClickListener(listener)
                .create()
                .showAtLocation(activity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }
}
