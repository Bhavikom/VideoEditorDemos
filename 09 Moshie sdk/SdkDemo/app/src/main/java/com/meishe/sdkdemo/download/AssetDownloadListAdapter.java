package com.meishe.sdkdemo.download;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseConstants;
import com.meishe.sdkdemo.utils.asset.NvAsset;

import java.io.File;
import java.util.ArrayList;

import static com.meishe.sdkdemo.utils.asset.NvAsset.ASSET_ANIMATED_STICKER;
import static com.meishe.sdkdemo.utils.asset.NvAsset.AspectRatio_16v9;
import static com.meishe.sdkdemo.utils.asset.NvAsset.AspectRatio_NoFitRatio;
import static com.meishe.sdkdemo.utils.asset.NvAsset.RatioArray;
import static com.meishe.sdkdemo.utils.asset.NvAsset.RatioStringArray;

public class AssetDownloadListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NvAsset> mAssetDataList = new ArrayList<>();
    private Context mContext;
    //view type
    private final int TYPE_ITEM = 1;
    private final int TYPE_FOOTER = 2;

    //加载状态：LOADING--正在加载；LOADING_COMPLETE--加载完成;LOADING_END -- 加载到底
    public static final int LOADING = 1;
    public static final int LOADING_COMPLETE = 2;
    public static final int LOADING_FAILED = 3;
    public static final int LOADING_END = 4;
    //当前状态,默认加载完成
    private int currentLoadState = LOADING_COMPLETE;
    private OnDownloadClickListener mDownloadClickerListener = null;
    private int curTimelineRatio = AspectRatio_NoFitRatio;
    private int mAssetType = 0;

    public class DownloadButtonInfo {
        int buttonBackgroud;
        String buttonText;
        String buttonTextColor;

        public DownloadButtonInfo() {

        }
    }

    public AssetDownloadListAdapter(Context context) {
        mContext = context;
    }

    public void setAssetType(int assetType) {
        this.mAssetType = assetType;
    }

    public void setCurTimelineRatio(int curTimelineRatio) {
        this.curTimelineRatio = curTimelineRatio;
    }

    public void setAssetDatalist(ArrayList<NvAsset> assetDataList) {
        this.mAssetDataList = assetDataList;
        Log.e("Datalist", "DataCount = " + mAssetDataList.size());
    }

    public void setDownloadClickerListener(OnDownloadClickListener downloadClickerListener) {
        this.mDownloadClickerListener = downloadClickerListener;
    }

    public interface OnDownloadClickListener {
        /**
         * @param holder
         * @param pos
         */
        void onItemDownloadClick(RecyclerViewHolder holder, int pos);
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 通过判断显示类型，来创建不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_asset_download, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_asset_download_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecyclerViewHolder) {
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            final NvAsset asset = mAssetDataList.get(position);
            //加载图片

            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.placeholder(R.drawable.bank_thumbnail_local);
            Glide.with(mContext)
                    .asBitmap()
                    .load(asset.coverUrl)
                    .apply(options)
                    .into(recyclerViewHolder.mAssetCover);

            recyclerViewHolder.mAssetName.setText(asset.name);

            if (asset.categoryId <= BaseConstants.PROP_IMAGES.length && asset.categoryId - 1 >= 0) {
                recyclerViewHolder.assetCover_type_image.setVisibility(View.VISIBLE);
                recyclerViewHolder.assetCover_type_image.setBackground(mContext.getResources().getDrawable(BaseConstants.PROP_IMAGES[asset.categoryId - 1]));
            } else {
                recyclerViewHolder.assetCover_type_image.setVisibility(View.INVISIBLE);
            }
            if (mAssetType == 4) {//mAssetType = 4是贴纸，贴纸无场景区分
                recyclerViewHolder.mAssetRatio.setText(R.string.asset_ratio);
            } else {
                recyclerViewHolder.mAssetRatio.setText(getAssetRatio(asset.aspectRatio));
            }

            recyclerViewHolder.mAssetSize.setText(getAssetSize(asset.remotePackageSize));
            DownloadButtonInfo buttonInfo = getDownloadButtonInfo(asset);
            recyclerViewHolder.mDownloadButton.setBackgroundResource(buttonInfo.buttonBackgroud);
            recyclerViewHolder.mDownloadButton.setText(buttonInfo.buttonText);
            recyclerViewHolder.mDownloadButton.setTextColor(Color.parseColor(buttonInfo.buttonTextColor));
            recyclerViewHolder.mDownloadProgressBar.setVisibility(View.GONE);
            recyclerViewHolder.mDownloadButton.setVisibility(View.VISIBLE);
            if (asset.downloadStatus == NvAsset.DownloadStatusFailed) {
                recyclerViewHolder.mDownloadButton.setText(R.string.retry);
                recyclerViewHolder.mDownloadButton.setTextColor(Color.parseColor("#ffffffff"));
                recyclerViewHolder.mDownloadButton.setBackgroundResource(R.drawable.download_button_shape_corner_retry);
                recyclerViewHolder.mDownloadProgressBar.setVisibility(View.GONE);
                recyclerViewHolder.mDownloadButton.setVisibility(View.VISIBLE);
            } else if (asset.downloadStatus == NvAsset.DownloadStatusFinished) {
                recyclerViewHolder.mDownloadProgressBar.setVisibility(View.GONE);
                recyclerViewHolder.mDownloadButton.setVisibility(View.VISIBLE);
            } else if (asset.downloadStatus == NvAsset.DownloadStatusInProgress) {
                recyclerViewHolder.mDownloadProgressBar.setVisibility(View.VISIBLE);
                recyclerViewHolder.mDownloadProgressBar.setProgress(asset.downloadProgress);
                recyclerViewHolder.mDownloadButton.setVisibility(View.GONE);
            }
            recyclerViewHolder.mDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (curTimelineRatio >= AspectRatio_16v9 && mAssetType != ASSET_ANIMATED_STICKER) {
                        if ((curTimelineRatio & asset.aspectRatio) == 0)
                            return;//时间线比例不适配，禁止下载
                    }

                    if (asset.isUsable() && !asset.hasUpdate())
                        return;
                    if (asset.isUsable() && asset.hasRemoteAsset() && asset.hasUpdate()) {
                        File file = new File(asset.localDirPath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    if (mDownloadClickerListener != null) {
                        mDownloadClickerListener.onItemDownloadClick(recyclerViewHolder, position);
                    }
                }
            });
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (currentLoadState) {
                case LOADING: // 正在加载
                    footViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                    footViewHolder.mLoadFailTips.setVisibility(View.GONE);
                    break;

                case LOADING_COMPLETE: // 加载完成
                    footViewHolder.mLoadLayout.setVisibility(View.INVISIBLE);
                    footViewHolder.mLoadFailTips.setVisibility(View.GONE);
                    break;
                case LOADING_FAILED:
                    footViewHolder.mLoadLayout.setVisibility(View.GONE);
                    footViewHolder.mLoadFailTips.setVisibility(View.VISIBLE);
                    break;
                case LOADING_END:
                    footViewHolder.mLoadLayout.setVisibility(View.GONE);
                    footViewHolder.mLoadFailTips.setVisibility(View.GONE);
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAssetDataList.size() + 1;
    }

    private String getAssetRatio(int aspectRatio) {
        String assetStrRatio = "";
        int length = RatioArray.length;
        for (int index = 0; index < length; ++index) {
            if ((aspectRatio & RatioArray[index]) != 0) {
                if (index == length - 1) {//通用类型
                    if (aspectRatio >= RatioArray[index])
                        assetStrRatio = mContext.getResources().getString(R.string.asset_ratio);//RatioStringArray[index]
                } else {//满足几种Ratio的素材
                    assetStrRatio += RatioStringArray[index];
                    assetStrRatio += " ";
                }
            }
        }
        return assetStrRatio;
    }

    private String getAssetSize(int assetSize) {
        int totalKbSize = assetSize / 1024;
        int mbSize = totalKbSize / 1024;
        int kbSize = totalKbSize % 1024;
        float tempSize = (float) (kbSize / 1024.0);
        String packageAssetSize;
        if (mbSize > 0) {
            tempSize = (mbSize + tempSize);
            packageAssetSize = String.format("%.1f", tempSize);
            packageAssetSize = packageAssetSize + "M";
        } else {
            packageAssetSize = String.format("%d", kbSize);
            packageAssetSize = packageAssetSize + "K";
        }
        return packageAssetSize;
    }

    private DownloadButtonInfo getDownloadButtonInfo(NvAsset asset) {
        DownloadButtonInfo buttonInfo = new DownloadButtonInfo();
        if (curTimelineRatio >= AspectRatio_16v9
                && mAssetType != ASSET_ANIMATED_STICKER
                && (curTimelineRatio & asset.aspectRatio) == 0) {
            buttonInfo.buttonBackgroud = R.drawable.download_button_shape_corner_finished;
            buttonInfo.buttonText = mContext.getResources().getString(R.string.asset_mismatch);
            buttonInfo.buttonTextColor = "#ff928c8c";
        } else if (!asset.isUsable() && asset.hasRemoteAsset()) {
            buttonInfo.buttonBackgroud = R.drawable.download_button_shape_corner_download;
            buttonInfo.buttonText = mContext.getResources().getString(R.string.asset_download);
            buttonInfo.buttonTextColor = "#ffffffff";
        } else if (asset.isUsable() && !asset.hasUpdate()) {
            buttonInfo.buttonBackgroud = R.drawable.download_button_shape_corner_finished;
            buttonInfo.buttonText = mContext.getResources().getString(R.string.asset_downloadfinished);
            buttonInfo.buttonTextColor = "#ff909293";
        } else if (asset.isUsable() && asset.hasRemoteAsset() && asset.hasUpdate()) {
            buttonInfo.buttonBackgroud = R.drawable.download_button_shape_corner_update;
            buttonInfo.buttonText = mContext.getResources().getString(R.string.asset_update);
            buttonInfo.buttonTextColor = "#ffffffff";
        }
        return buttonInfo;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView mAssetCover;
        ImageView assetCover_type_image;
        TextView mAssetName;
        TextView mAssetRatio;
        TextView mAssetSize;
        Button mDownloadButton;
        DownloadProgressBar mDownloadProgressBar;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            mAssetCover = (ImageView) itemView.findViewById(R.id.assetCover);
            assetCover_type_image = (ImageView) itemView.findViewById(R.id.assetCover_type_image);
            mAssetName = (TextView) itemView.findViewById(R.id.assetName);
            mAssetRatio = (TextView) itemView.findViewById(R.id.assetRatio);
            mAssetSize = (TextView) itemView.findViewById(R.id.assetSize);
            mDownloadButton = (Button) itemView.findViewById(R.id.download_button);
            mDownloadProgressBar = (DownloadProgressBar) itemView.findViewById(R.id.downloadProgressBar);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLoadLayout;
        FrameLayout mLoadFailTips;

        FootViewHolder(View itemView) {
            super(itemView);
            mLoadLayout = (LinearLayout) itemView.findViewById(R.id.loadLayout);
            mLoadFailTips = (FrameLayout) itemView.findViewById(R.id.loadFailTips);
        }
    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 1.正在加载 2.加载完成 3,加载结束
     */
    public void setLoadState(int loadState) {
        this.currentLoadState = loadState;
        notifyDataSetChanged();
    }

    public void updateDownloadItems() {
        notifyDataSetChanged();
    }
}
