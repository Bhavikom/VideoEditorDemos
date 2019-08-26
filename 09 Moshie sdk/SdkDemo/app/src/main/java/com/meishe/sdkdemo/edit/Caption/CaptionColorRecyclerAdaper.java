package com.meishe.sdkdemo.edit.Caption;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.data.CaptionColorInfo;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.edit.view.RoundColorView;

import java.util.ArrayList;

/**
 * Created by admin on 2018/5/25.
 */

public class CaptionColorRecyclerAdaper extends RecyclerView.Adapter<CaptionColorRecyclerAdaper.ViewHolder>  {
    private ArrayList<CaptionColorInfo> mCaptionColorList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;
    public CaptionColorRecyclerAdaper(Context context) {
        mContext = context;
    }

    public void setCaptionColorList(ArrayList<CaptionColorInfo> captionColorList) {
        this.mCaptionColorList = captionColorList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_asset_caption_color, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CaptionColorInfo colorInfo = mCaptionColorList.get(position);
        holder.mCaptionColor.setColor(Color.parseColor(colorInfo.mColorValue));
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
        return mCaptionColorList.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundColorView mCaptionColor;
        View mSelecteItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mCaptionColor = (RoundColorView)itemView.findViewById(R.id.captionColor);
            mSelecteItem = itemView.findViewById(R.id.selectedItem);
        }
    }
}
