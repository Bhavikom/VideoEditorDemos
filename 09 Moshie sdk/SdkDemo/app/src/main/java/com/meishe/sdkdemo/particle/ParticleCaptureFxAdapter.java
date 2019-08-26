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
import com.meicam.sdk.NvsAssetPackageParticleDescParser;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.data.FilterItem;
import com.meishe.sdkdemo.edit.view.CircleProgressBar;
import com.meishe.sdkdemo.edit.view.RoundImageView;
import com.meishe.sdkdemo.utils.asset.NvAsset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyj on 2017/12/19 0019.
 */

public class ParticleCaptureFxAdapter extends RecyclerView.Adapter<ParticleCaptureFxAdapter.ViewHolder> {
    private Context mContext;
    private OnItemClickListener mClickListener;
    private List<FilterItem> mFilterDataList = new ArrayList<>();
    private ParticleItemDownloadListener mParticleItemDownloadListener;

    private int mSelectPos = 0;

    public void setParticleItemDownloadListener(ParticleItemDownloadListener particleItemDownloadListener) {
        this.mParticleItemDownloadListener = particleItemDownloadListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onSameItemClick();
    }

    public ParticleCaptureFxAdapter(Context context) {
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void setFilterDataList(List<FilterItem> filterDataList) {
        this.mFilterDataList = filterDataList;
        notifyDataSetChanged();
    }

    public void setSelectPos(int pos) {
        this.mSelectPos = pos;
    }

    public interface ParticleItemDownloadListener {
        void onItemDownload(View view, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // 展示
        private RoundImageView assetImageRoundImageView;
        private TextView assetNameTextView;
        private RelativeLayout assetSelectedBackgroundRelativeLayout;
        private ImageView assetParamAdjustmentImageView;
        // 下载
        private ImageView assetDownloadIconImageView;
        private CircleProgressBar assetDownloadCircleProgressBar;

        public ViewHolder(View view) {
            super(view);
            assetNameTextView = (TextView) view.findViewById(R.id.assetNameTextView);
            assetImageRoundImageView = (RoundImageView) view.findViewById(R.id.assetImageRoundImageView);
            assetSelectedBackgroundRelativeLayout = (RelativeLayout) view.findViewById(R.id.assetSelectedBackgroundRelativeLayout);
            assetParamAdjustmentImageView = (ImageView) view.findViewById(R.id.assetParamAdjustmentImageView);

            assetDownloadIconImageView = (ImageView) view.findViewById(R.id.assetDownloadIconImageView);
            assetDownloadCircleProgressBar = (CircleProgressBar) view.findViewById(R.id.assetDownloadCircleProgressBar);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.particle_item_capture_fx, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FilterItem itemData = mFilterDataList.get(position);
        if (itemData == null)
            return;

        String name = itemData.getFilterName();
        if (name != null) {
            holder.assetNameTextView.setText(name);
        }
        int filterMode = itemData.getFilterMode();
        if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
            int imageId = itemData.getImageId();
            if (imageId != 0)
                holder.assetImageRoundImageView.setImageResource(imageId);
        } else {
            String imageUrl = itemData.getImageUrl();
            if (imageUrl != null) {
                //加载图片
                RequestOptions options = new RequestOptions();
                options.centerCrop();
                options.placeholder(R.mipmap.default_filter);
                Glide.with(mContext)
                        .asBitmap()
                        .load(imageUrl)
                        .apply(options)
                        .into(holder.assetImageRoundImageView);
            }
        }
        if (position != 0 && itemData.getParticleType() == NvsAssetPackageParticleDescParser.PARTICLE_TYPE_NORMAL) {
            holder.assetParamAdjustmentImageView.setVisibility(View.VISIBLE);
        } else {
            holder.assetParamAdjustmentImageView.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(itemData.getAssetDescription()) && position != 0) {
            if (itemData.downloadStatus == NvAsset.DownloadStatusInProgress) {
                holder.assetDownloadIconImageView.setVisibility(View.GONE);
                holder.assetDownloadCircleProgressBar.setVisibility(View.VISIBLE);
                holder.assetDownloadCircleProgressBar.drawProgress(itemData.downloadProgress);
            } else if (itemData.downloadStatus == NvAsset.DownloadStatusFinished) {
                holder.assetDownloadIconImageView.setVisibility(View.GONE);
                holder.assetDownloadCircleProgressBar.setVisibility(View.GONE);
            } else {
                holder.assetDownloadIconImageView.setVisibility(View.VISIBLE);
                holder.assetDownloadCircleProgressBar.setVisibility(View.GONE);
            }
        } else {
            holder.assetDownloadIconImageView.setVisibility(View.GONE);
            holder.assetDownloadCircleProgressBar.setVisibility(View.GONE);
        }
        if (mSelectPos == position) {
            holder.assetSelectedBackgroundRelativeLayout.setVisibility(View.VISIBLE);
            holder.assetNameTextView.setTextColor(Color.parseColor("#994a90e2"));
        } else {
            holder.assetSelectedBackgroundRelativeLayout.setVisibility(View.GONE);
            holder.assetNameTextView.setTextColor(Color.parseColor("#CCffffff"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 没有资源
                if (TextUtils.isEmpty(itemData.getAssetDescription()) && position != 0) {
                    mParticleItemDownloadListener.onItemDownload(holder.itemView, position);
                } else {
                    if (mSelectPos == position) {
                        if (mClickListener != null) {
                            mClickListener.onSameItemClick();
                        }
                        return;
                    }

                    mSelectPos = position;
                    notifyDataSetChanged();
                    if (mClickListener != null) {
                        mClickListener.onItemClick(view, position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilterDataList.size();
    }
}
