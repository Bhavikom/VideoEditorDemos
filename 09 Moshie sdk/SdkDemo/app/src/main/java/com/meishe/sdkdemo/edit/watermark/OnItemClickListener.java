package com.meishe.sdkdemo.edit.watermark;

import android.support.v7.widget.RecyclerView;

/**
 * Created by CaoZhiChao on 2018/10/16 15:28
 */
public interface OnItemClickListener {
    void onClick(RecyclerView.ViewHolder holder, int position,int pictureType, String picturePath, int waterMarkType,String waterMarkPicture);
}
