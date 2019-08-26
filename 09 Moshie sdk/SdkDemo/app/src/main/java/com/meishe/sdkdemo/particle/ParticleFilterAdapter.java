package com.meishe.sdkdemo.particle;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.data.AssetItem;
import com.meishe.sdkdemo.edit.view.CircleProgressBar;
import com.meishe.sdkdemo.edit.view.RoundImageView;
import com.meishe.sdkdemo.utils.asset.NvAsset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyj on 2017/12/19 0019.
 */

public class ParticleFilterAdapter extends RecyclerView.Adapter<ParticleFilterAdapter.ViewHolder> {
    private Context mContext;
    private OnItemClickListener mClickListener;
    private List<AssetItem> mAssetItemDataList = new ArrayList<>();

    private int mSelectPos = 0;
    RequestOptions mOptions = new RequestOptions();
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onSameItemClick();
        void onItemDownload(View view,int position);
    }
    public ParticleFilterAdapter(Context context) {
        mContext = context;
        mOptions.centerCrop();
        mOptions.placeholder(R.mipmap.default_filter);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }
    public void setAssetItemDataList(List<AssetItem> assetItemDataList) {
        this.mAssetItemDataList = assetItemDataList;
    }

    public void setSelectPos(int pos) {
        this.mSelectPos = pos;
    }

    private void setViewVisible(final ParticleFilterAdapter.ViewHolder holder,
                                int shadowVisibility,
                                int downloadAssetVisibility,
                                int progressBarVisibility){
        holder.mDownloadShadow.setVisibility(shadowVisibility);
        holder.mDownloadAssetButton.setVisibility(downloadAssetVisibility);
        holder.mCircleProgressBar.setVisibility(progressBarVisibility);
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        RoundImageView item_assetImage;
        TextView item_assetName;
        RelativeLayout item_select;
        ImageView item_select_image;

        View mDownloadShadow;
        ImageView mDownloadAssetButton;
        CircleProgressBar mCircleProgressBar;
        public ViewHolder(View view) {
            super(view);
            item_assetName = (TextView) view.findViewById(R.id.nameAsset);
            item_assetImage = (RoundImageView) view.findViewById(R.id.imageAsset);
            item_select = (RelativeLayout) view.findViewById(R.id.layoutAssetSelect);
            item_select_image = (ImageView) view.findViewById(R.id.imageAssetSelect);

            mDownloadShadow = itemView.findViewById(R.id.assetDownloadShadow);
            mDownloadAssetButton = (ImageView) itemView.findViewById(R.id.downloadAssetButton);
            mCircleProgressBar = (CircleProgressBar)itemView.findViewById(R.id.circleProgressBar);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.particle_item_fx, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        AssetItem assetItem = mAssetItemDataList.get(position);
        if (assetItem == null)
            return;

        final NvAsset asset = assetItem.getAsset();
        if(asset == null)
            return;
        if(!TextUtils.isEmpty(asset.name)) {
            holder.item_assetName.setText(asset.name);
        }

        if(assetItem.getAssetMode() == AssetItem.ASSET_NONE
                || assetItem.getAssetMode() == AssetItem.ASSET_BUILTIN){
            holder.item_assetImage.setImageResource(assetItem.getImageRes());
        }else {
            Glide.with(mContext)
                    .asBitmap()
                    .load(asset.coverUrl)
                    .apply(mOptions)
                    .into(holder.item_assetImage);
        }
        if(asset.isUsable()){
            setViewVisible(holder,View.GONE,View.GONE,View.GONE);
        }else {
            if(assetItem.getAssetMode() == AssetItem.ASSET_NONE ||
                    assetItem.getAssetMode() == AssetItem.ASSET_BUILTIN ||
                    asset.downloadStatus == NvAsset.DownloadStatusFinished){
                setViewVisible(holder,View.GONE,View.GONE,View.GONE);
            }else if(asset.downloadStatus == NvAsset.DownloadStatusInProgress){
                setViewVisible(holder,View.VISIBLE,View.GONE,View.VISIBLE);
                holder.mCircleProgressBar.drawProgress(asset.downloadProgress);
            }else {
                setViewVisible(holder,View.VISIBLE,View.VISIBLE,View.GONE);
            }
        }

        holder.mDownloadAssetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null)
                    mClickListener.onItemDownload(v, position);
            }
        });

        holder.mDownloadShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!asset.isUsable()){
                    holder.mDownloadAssetButton.callOnClick();
                }
            }
        });

        if(position != 0) {
            holder.item_select_image.setVisibility(View.VISIBLE);
        } else {
            holder.item_select_image.setVisibility(View.GONE);
        }

        if(mSelectPos == position) {
            holder.item_select.setVisibility(View.VISIBLE);
            holder.item_assetName.setTextColor(Color.parseColor("#994a90e2"));
        } else {
            holder.item_select.setVisibility(View.GONE);
            holder.item_assetName.setTextColor(Color.parseColor("#CCffffff"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectPos == position) {
                    if(mClickListener != null) {
                        mClickListener.onSameItemClick();
                    }
                    return;
                }
                notifyItemChanged(mSelectPos);
                mSelectPos = position;
                notifyItemChanged(mSelectPos);
                if(mClickListener != null) {
                    mClickListener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAssetItemDataList.size();
    }
}
