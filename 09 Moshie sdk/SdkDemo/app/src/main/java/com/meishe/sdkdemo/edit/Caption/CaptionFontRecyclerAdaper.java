package com.meishe.sdkdemo.edit.Caption;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.data.AssetItem;
import com.meishe.sdkdemo.edit.view.CircleProgressBar;
import com.meishe.sdkdemo.utils.asset.NvAsset;

import java.util.ArrayList;

/**
 * Created by admin on 2018/5/25.
 */

public class CaptionFontRecyclerAdaper extends RecyclerView.Adapter<CaptionFontRecyclerAdaper.ViewHolder> {
    private ArrayList<AssetItem> mAssetInfoList = new ArrayList<>();
    private Context mContext;
    private OnFontItemClickListener mOnItemClickListener = null;
    RequestOptions mOptions = new RequestOptions();
    private int mSelectedPos = 0;
    public interface OnFontItemClickListener{
        void onItemDownload(View view,int position);
        void onItemClick(View view,int position);
    }
    public CaptionFontRecyclerAdaper(Context context) {
        mContext = context;
        mOptions.fitCenter();
        mOptions.skipMemoryCache(false);
        mOptions.placeholder(R.mipmap.default_filter);
    }

    public void setSelectedPos(int selectedPos) {
        this.mSelectedPos = selectedPos;
    }

    public void setAssetInfoList(ArrayList<AssetItem> assetInfoList) {
        this.mAssetInfoList = assetInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_asset_caption_font, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AssetItem assetItemInfo = mAssetInfoList.get(position);
        if (assetItemInfo.getAssetMode() == AssetItem.ASSET_NONE){
            holder.mCaptinFontCover.setImageResource(assetItemInfo.getImageRes());
            setViewVisible(holder,View.GONE,View.GONE,View.GONE);
        }else {
            NvAsset asset = assetItemInfo.getAsset();
            String imageUrl = asset.coverUrl;
            if (!TextUtils.isEmpty(imageUrl)) {
                //加载图片
                Glide.with(mContext)
                        .asBitmap()
                        .load(imageUrl)
                        .apply(mOptions)
                        .into(holder.mCaptinFontCover);
            }
            if(asset.isUsable()){
                setViewVisible(holder,View.GONE,View.GONE,View.GONE);
            }else {
                if(asset.downloadStatus == NvAsset.DownloadStatusFinished){
                    setViewVisible(holder,View.GONE,View.GONE,View.GONE);
                }else if(asset.downloadStatus == NvAsset.DownloadStatusInProgress){
                    setViewVisible(holder,View.VISIBLE,View.GONE,View.VISIBLE);
                    holder.mCircleProgressBar.drawProgress(asset.downloadProgress);
                }else {
                    setViewVisible(holder,View.VISIBLE,View.VISIBLE,View.GONE);
                }
            }
        }
        holder.mSelecteItem.setVisibility(mSelectedPos == position ? View.VISIBLE : View.GONE);
        holder.mDownloadAssetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemDownload(v, position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(assetItemInfo.getAssetMode() != AssetItem.ASSET_NONE && !assetItemInfo.getAsset().isUsable()){
                    holder.mDownloadAssetButton.callOnClick();
                    return;//未下载点击执行下载操作，不选中
                }

                if(mSelectedPos == position)
                    return;
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v, position);
                notifyItemChanged(mSelectedPos);
                mSelectedPos = position;
                notifyItemChanged(mSelectedPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAssetInfoList.size();
    }

    private void setViewVisible(final ViewHolder holder,
                                int shadowVisibility,
                                int downloadAssetVisibility,
                                int progressBarVisibility){
        holder.mDownloadShadow.setVisibility(shadowVisibility);
        holder.mDownloadAssetButton.setVisibility(downloadAssetVisibility);
        holder.mCircleProgressBar.setVisibility(progressBarVisibility);
    }
    public void setOnItemClickListener(OnFontItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mCaptinFontCover;
        View mSelecteItem;
        View mDownloadShadow;
        ImageView mDownloadAssetButton;
        CircleProgressBar mCircleProgressBar;
        public ViewHolder(View itemView) {
            super(itemView);
            mCaptinFontCover = (ImageView) itemView.findViewById(R.id.captionFontAssetCover);
            mSelecteItem = itemView.findViewById(R.id.selectedItem);
            mDownloadShadow = itemView.findViewById(R.id.downloadShadow);
            mDownloadAssetButton = (ImageView)itemView.findViewById(R.id.downloadAssetButton);
            mCircleProgressBar = (CircleProgressBar)itemView.findViewById(R.id.circleProgressBar);
        }
    }
}
