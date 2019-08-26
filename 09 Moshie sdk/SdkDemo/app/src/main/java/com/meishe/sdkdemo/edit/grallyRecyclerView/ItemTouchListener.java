package com.meishe.sdkdemo.edit.grallyRecyclerView;

/**
 * Created by CaoZhiChao on 2018/5/31 11:31
 */
public interface ItemTouchListener {
    /**
     * 上下拖拽时回调方法，adpater将两个位置的item调换位置
     * @param fromPosition
     * @param toPosition
     */
    void onItemMoved(int fromPosition, int toPosition);
    void onItemDismiss(int position);//左右删除item
    void removeAll();//清除所有的
}
