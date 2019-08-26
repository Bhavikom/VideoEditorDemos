package com.meishe.sdkdemo.edit.interfaces;

import android.view.View;

/**
 * Created by CaoZhiChao on 2018/5/28 14:44
 */
public interface OnGrallyItemClickListener {
    void onLeftItemClick(View view, int pos);
    void onRightItemClick(View view, int pos);
    void onItemMoved(int fromPosition, int toPosition);
    void onItemDismiss(int position);
    void removeall();
}
