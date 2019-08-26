package com.meishe.sdkdemo.edit.animatesticker.customsticker;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.utils.asset.NvAsset;

import java.util.ArrayList;

/**
 * Created by admin on 2018/5/25.
 */

public class CustomStickerRecycleViewAdaper extends RecyclerView.Adapter<CustomStickerRecycleViewAdaper.ViewHolder>  {
    private ArrayList<NvAsset> mAssetList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;

    private int mSelectedPos = -1;

    public CustomStickerRecycleViewAdaper(Context context) {
        mContext = context;
    }
    public void setAssetList(ArrayList<NvAsset> assetList) {
        this.mAssetList = assetList;
    }
    public void setSelectedPos(int selectedPos) {
        this.mSelectedPos = selectedPos;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_asset_customsticker, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        NvAsset asset = mAssetList.get(position);
        //加载图片
        RequestOptions options = new RequestOptions();
        options.centerCrop().placeholder(R.mipmap.default_sticker);
        Glide.with(mContext)
                .load(asset.coverUrl)
                .apply(options)
                .into(holder.mCustomStickerEffectCover);

        holder.mCustomStickerSelected.setBackgroundResource(mSelectedPos == position ? R.drawable.shape_border_custom_animatesticker_selected : R.drawable.shape_border_custom_animatesticker);
        holder.mCustomStickerEffectName.setText(asset.name);
        holder.mCustomStickerEffectName.setTextColor(mSelectedPos == position ? Color.parseColor("#994a90e2") : Color.parseColor("#CCffffff"));
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v,position);
                if(mSelectedPos>= 0 && mSelectedPos == position)
                    return;
                mSelectedPos = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAssetList.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mCustomStickerEffectCover;
        RelativeLayout mCustomStickerSelected;
        TextView mCustomStickerEffectName;
        public ViewHolder(View itemView) {
            super(itemView);
            mCustomStickerSelected = (RelativeLayout)itemView.findViewById(R.id.customStickerSelected);
            mCustomStickerEffectCover = (ImageView) itemView.findViewById(R.id.customStickerEffectCover);
            mCustomStickerEffectName = (TextView)itemView.findViewById(R.id.customStickerEffectName);
        }
    }
}
