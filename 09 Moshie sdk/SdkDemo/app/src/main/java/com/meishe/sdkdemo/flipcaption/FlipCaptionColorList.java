package com.meishe.sdkdemo.flipcaption;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.adapter.SpaceItemDecoration;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by admin on 2018/12/25.
 */

public class FlipCaptionColorList extends RelativeLayout {
    private RecyclerView mColorRecyclerList;
    private FlipCaptionColorAdaper mFlipCaptionColorAdapter;
    private OnFlipCaptionColorListener mCaptionColorListener;
    public interface OnFlipCaptionColorListener{
        void onCaptionColor(int pos);
    }

    public void setCaptionColorListener(OnFlipCaptionColorListener captionColorListener) {
        this.mCaptionColorListener = captionColorListener;
    }
    public FlipCaptionColorList(Context context){
        this(context,null);
    }

    public FlipCaptionColorList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initColorRecyclerList(context);
    }
    public void setCaptionColorInfolist(ArrayList<String> captionColorlist) {
        if(mFlipCaptionColorAdapter != null)
            mFlipCaptionColorAdapter.setCaptionColorList(captionColorlist);
    }

    public void notifyDataSetChanged(){
        if(mFlipCaptionColorAdapter != null)
            mFlipCaptionColorAdapter.notifyDataSetChanged();
    }

    public void setSelectedPos(int selectedPos) {
        if(mFlipCaptionColorAdapter != null)
            mFlipCaptionColorAdapter.setSelectedPos(selectedPos);
    }
    private void initColorRecyclerList(Context context){
        View rootView = LayoutInflater.from(context).inflate(R.layout.flip_caption_color_list, this);
        mColorRecyclerList = (RecyclerView)rootView.findViewById(R.id.colorRecyclerList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mColorRecyclerList.setLayoutManager(layoutManager);
        mFlipCaptionColorAdapter = new FlipCaptionColorAdaper(context);
        mColorRecyclerList.setAdapter(mFlipCaptionColorAdapter);
        mColorRecyclerList.addItemDecoration(new SpaceItemDecoration(0, ScreenUtils.dip2px(context,29)));
        mFlipCaptionColorAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if(mCaptionColorListener != null){
                    mCaptionColorListener.onCaptionColor(pos);
                }
            }
        });
    }
}
