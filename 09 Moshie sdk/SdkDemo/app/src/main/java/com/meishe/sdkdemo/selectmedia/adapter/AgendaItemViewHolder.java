/*
 * Copyright (C) 2015 Tomás Ruiz-López.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meishe.sdkdemo.selectmedia.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.sdkdemo.MSApplication;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.selectmedia.bean.MediaData;
import com.meishe.sdkdemo.selectmedia.interfaces.OnItemClick;
import com.meishe.sdkdemo.selectmedia.utils.TimeUtil;
import com.meishe.sdkdemo.utils.MediaConstant;
import com.meishe.sdkdemo.utils.ScreenUtils;

import java.io.File;

import static com.meishe.sdkdemo.selectmedia.fragment.MediaFragment.GRIDITEMCOUNT;

public class AgendaItemViewHolder extends RecyclerView.ViewHolder {

    TextView textView;
    ImageView iv_item_image;
    RelativeLayout item_media_hideLayout;
    TextView tv_selected_num;
    private int mClickType;

    public AgendaItemViewHolder(View itemView, int type) {
        super(itemView);
        this.mClickType = type;
        textView = (TextView) itemView.findViewById(R.id.tv_media_type);
        tv_selected_num = (TextView) itemView.findViewById(R.id.tv_selected_num);
        iv_item_image = (ImageView) itemView.findViewById(R.id.iv_item_image);
        item_media_hideLayout = (RelativeLayout) itemView.findViewById(R.id.item_media_hideLayout);
    }

    public void render(MediaData mediaData, final int se, final int position, final OnItemClick onItemClick) {
        //设置当前的图片为正方形
        int marginSizeLeftAndRight = (int) MSApplication.getmContext().getResources().getDimension(R.dimen.select_recycle_marginLeftAndRight);
        int width = ScreenUtils.getWindowWidth(MSApplication.getmContext()) - marginSizeLeftAndRight * 2;
        int marginSizeStart = (int) MSApplication.getmContext().getResources().getDimension(R.dimen.select_item_start_end);
        int marginSizeMiddle = (int) MSApplication.getmContext().getResources().getDimension(R.dimen.select_item_between);
        int itemWidth = (width - marginSizeStart * 2 - marginSizeMiddle * (GRIDITEMCOUNT - 1)) / GRIDITEMCOUNT;

        RecyclerView.LayoutParams param = new RecyclerView.LayoutParams(itemWidth, itemWidth);
        int columnMarginStartAndEnd = 0;
        int columnMarginMiddle = 0;
        int marginSizeTopAndEnd = ScreenUtils.dip2px(MSApplication.getmContext(), 4) / 2;
        if (position < GRIDITEMCOUNT) {
            marginSizeTopAndEnd = 0;
        }
        param.setMargins(columnMarginStartAndEnd, marginSizeTopAndEnd, columnMarginMiddle, marginSizeTopAndEnd);
        itemView.setLayoutParams(param);
        if (mediaData.getType() == MediaConstant.VIDEO) {
            textView.setVisibility(View.VISIBLE);
            textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            textView.getPaint().setAntiAlias(true);//抗锯齿
            textView.setText(TimeUtil.secToTime((int) (mediaData.getDuration() / 1000) < 1 ? 1 : (int) (mediaData.getDuration() / 1000)));
        } else {
            textView.setVisibility(View.GONE);
        }
        item_media_hideLayout.setVisibility(mediaData.isState() ? View.VISIBLE : View.GONE);
        if (mClickType == MediaConstant.TYPE_ITEMCLICK_SINGLE) {
            tv_selected_num.setVisibility(View.GONE);
        } else {
            tv_selected_num.setText(mediaData.getPosition() + "");
        }
        setImageByFile(mediaData.getPath(), itemWidth);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnItemClick(itemView, se, position);
            }
        });
    }

    private void setImageByFile(String iamgeFile, int width) {
        File file = new File(iamgeFile);
        RequestOptions options = new RequestOptions().centerCrop()
                .placeholder(R.drawable.bank_thumbnail_local)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .override(width, width);
        Glide.with(MSApplication.getmContext())
                .asBitmap()
                .load(file)
                .apply(options)
                .into(iv_item_image);
    }
}
