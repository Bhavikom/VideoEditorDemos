package com.meishe.sdkdemo.edit.Caption;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.data.CaptionColorInfo;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.edit.view.RoundColorView;


import java.util.ArrayList;

/**
 * Created by admin on 2018/5/25.
 */

public class CaptionOutlineRecyclerAdaper extends RecyclerView.Adapter<CaptionOutlineRecyclerAdaper.ViewHolder>  {
    private ArrayList<CaptionColorInfo> mCaptionOutlineColorList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;
    public CaptionOutlineRecyclerAdaper(Context context) {
        mContext = context;
    }
    public void setCaptionOutlineColorList(ArrayList<CaptionColorInfo> captionOutlineColorList) {
        this.mCaptionOutlineColorList = captionOutlineColorList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_asset_caption_outline, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CaptionColorInfo colorInfo = mCaptionOutlineColorList.get(position);
        if(0 == position){
            holder.mCaptionOutlineNoColor.setVisibility(View.VISIBLE);
            holder.mCaptionOutlineColor.setVisibility(View.GONE);
        }else {
            holder.mCaptionOutlineNoColor.setVisibility(View.GONE);
            holder.mCaptionOutlineColor.setVisibility(View.VISIBLE);
            holder.mCaptionOutlineColor.setColor(Color.parseColor(colorInfo.mColorValue));
        }
        if(colorInfo.mSelected){
            holder.mSelecteItem.setVisibility(View.VISIBLE);
        }else {
            holder.mSelecteItem.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCaptionOutlineColorList.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mCaptionOutlineNoColor;
        RoundColorView mCaptionOutlineColor;
        View mSelecteItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mCaptionOutlineNoColor = (ImageView)itemView.findViewById(R.id.captionOutlineNoColor);
            mCaptionOutlineColor = (RoundColorView)itemView.findViewById(R.id.captionOutlineColor);
            mSelecteItem = itemView.findViewById(R.id.selectedItem);
        }
    }
}
