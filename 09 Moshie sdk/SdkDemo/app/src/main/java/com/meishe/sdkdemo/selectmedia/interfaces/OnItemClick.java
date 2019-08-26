package com.meishe.sdkdemo.selectmedia.interfaces;

import android.view.View;

/**
 * Created by CaoZhiChao on 2018/6/4 18:22
 */
public interface OnItemClick {
    void OnHeadClick(View v, int position);
    void OnItemClick(View v, int headListPosition, int childListPosition);
}
