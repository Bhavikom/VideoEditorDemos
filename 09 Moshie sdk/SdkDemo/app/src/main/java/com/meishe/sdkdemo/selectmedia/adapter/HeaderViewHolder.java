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

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.meishe.sdkdemo.MSApplication;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.selectmedia.interfaces.OnItemClick;
import com.meishe.sdkdemo.utils.MediaConstant;

/**
 * Created by tomas on 01/06/15.
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView titleText = null;
    private TextView select_all;
    private int clickType;
    private int mLimitMediaCount;
    public HeaderViewHolder(View itemView, @IdRes int titleID, int type, int limitMediaCount) {
        super(itemView);
        titleText = (TextView) itemView.findViewById(titleID);
        select_all = (TextView) itemView.findViewById(R.id.meida_head_selectAll);
        clickType = type;
        mLimitMediaCount = limitMediaCount;
    }

    public void render(String title, boolean state){
        titleText.setText(title);
        if (clickType == MediaConstant.TYPE_ITEMCLICK_MULTIPLE){
            if(mLimitMediaCount >= 0)
                select_all.setText("");
            else{
                String checkAll = MSApplication.getmContext().getResources().getString(R.string.checkAll);
                String cancleCheckAll = MSApplication.getmContext().getResources().getString(R.string.cancelCheckAll);
                select_all.setText(state? cancleCheckAll : checkAll);
            }

        }else if (clickType == MediaConstant.TYPE_ITEMCLICK_SINGLE){
            select_all.setText("");
        }
    }
    public void onClick(final int  section, final OnItemClick onItemClick){
        select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnHeadClick(itemView,section);
            }
        });
    }
}
