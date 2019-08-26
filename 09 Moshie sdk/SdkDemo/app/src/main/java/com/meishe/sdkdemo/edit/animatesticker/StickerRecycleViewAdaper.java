package com.meishe.sdkdemo.edit.animatesticker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;

import java.util.ArrayList;

/**
 * Created by admin on 2018/5/25.
 */

public class StickerRecycleViewAdaper extends RecyclerView.Adapter<StickerRecycleViewAdaper.ViewHolder>  {
    private ArrayList<NvAsset> mAssetList = new ArrayList<>();
    private ArrayList<NvAssetManager.NvCustomStickerInfo> mCustomStickerAssetList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;

    private int mSelectedPos = -1;
    private boolean mIsCutomStickerAsset = false;
    private boolean mIsStickerInPlay = false;//true 是播放状态，false是停止状态

    public void setIsStickerInPlay(boolean isStickerInPlay) {
        this.mIsStickerInPlay = isStickerInPlay;
    }
    public StickerRecycleViewAdaper(Context context) {
        mContext = context;
    }
    public void setAssetList(ArrayList<NvAsset> assetList) {
        this.mAssetList = assetList;
    }
    public void setCustomStickerAssetList(ArrayList<NvAssetManager.NvCustomStickerInfo> assetList) {
        this.mCustomStickerAssetList = assetList;
    }
    public void setIsCutomStickerAsset(boolean isCutomStickerAsset) {
        mIsCutomStickerAsset = isCutomStickerAsset;
    }
    public void setSelectedPos(int selectedPos) {
        this.mSelectedPos = selectedPos;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_asset_animatesticker, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String assetCoverUrl =  mIsCutomStickerAsset ? mCustomStickerAssetList.get(position).imagePath : mAssetList.get(position).coverUrl;
        //加载图片
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.mipmap.default_sticker);
        Glide.with(mContext)
                .asBitmap()
                .load(assetCoverUrl)
                .apply(options)
                .into(holder.mStickerAssetCover);
        if(mSelectedPos == position){
            holder.mStickerPlayButton.setImageResource( mIsStickerInPlay ? R.mipmap.icon_edit_pause : R.mipmap.icon_edit_play);
        }else {
            holder.mStickerPlayButton.setImageResource(R.mipmap.icon_edit_pause);
        }

        holder.mStickerPlayButton.setVisibility(mSelectedPos == position ? View.VISIBLE : View.GONE);
        holder.mSelecteItem.setVisibility(mSelectedPos == position ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v,position);
                if(mSelectedPos >= 0 && mSelectedPos == position)
                    return;
                notifyItemChanged(mSelectedPos);
                mSelectedPos = position;
                notifyItemChanged(mSelectedPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIsCutomStickerAsset ? mCustomStickerAssetList.size() : mAssetList.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mStickerAssetCover;
        ImageView mStickerPlayButton;
        View mSelecteItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mStickerAssetCover = (ImageView) itemView.findViewById(R.id.stickerAssetCover);
            mStickerPlayButton = (ImageView) itemView.findViewById(R.id.stickerPlayButton);
            mSelecteItem = itemView.findViewById(R.id.selectedItem);
        }
    }
}
