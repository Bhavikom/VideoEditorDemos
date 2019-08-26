package com.meishe.sdkdemo.edit.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by admin on 2018/5/25.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration{
    private int leftSpace;
    private int rightSpace;

    public SpaceItemDecoration(int left,int right) {
        this.leftSpace = left;
        this.rightSpace = right;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int pos = parent.getChildAdapterPosition(view);
        if(pos != 0) {
            outRect.left = leftSpace;
        } else {
            outRect.left = 0;
        }
        outRect.right = rightSpace;
    }

}