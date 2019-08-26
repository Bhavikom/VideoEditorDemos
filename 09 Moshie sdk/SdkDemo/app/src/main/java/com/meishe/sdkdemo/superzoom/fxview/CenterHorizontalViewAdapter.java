package com.meishe.sdkdemo.superzoom.fxview;

import android.content.Context;
import android.icu.text.UFormat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.view.CircleProgressBar;
import com.meishe.sdkdemo.utils.asset.NvAsset;

import java.util.List;


public class CenterHorizontalViewAdapter extends RecyclerView.Adapter<CenterHorizontalViewAdapter.FxViewHolder> implements CenterHorizontalView.IHorizontalView {
    private Context mContext;
    private View mView;
    private List<NvAsset> mDatas;
    private int circle;

    private ItemClickCallBack mItemClickCallBack;
    private ItemTouchCallBack mItemTouchCallBack;

    private int[] selectedRes = {
            R.mipmap.selected_dramatization,
            R.mipmap.selected_spring,
            R.mipmap.selected_cartoon,
            R.mipmap.selected_daily,
            R.mipmap.selected_dogpacks,
            R.mipmap.selected_fire,
            R.mipmap.selected_horror,
            R.mipmap.selected_love,
            R.mipmap.selected_no,
            R.mipmap.selected_rhythm,
//            R.mipmap.selected_rotate,
            R.mipmap.selected_shake,
            R.mipmap.selected_tragedy,
            R.mipmap.selected_tv,
            R.mipmap.selected_wasted
    };
    private int[] unSelectedRes = {
            R.mipmap.noselected_dramatization,
            R.mipmap.noselected_spring,
            R.mipmap.noselected_cartoon,
            R.mipmap.noselected_daily,
            R.mipmap.noselected_dogpacks,
            R.mipmap.noselected_fire,
            R.mipmap.noselected_horror,
            R.mipmap.noselected_love,
            R.mipmap.noselected_no,
            R.mipmap.noselected_rhythm,
//            R.mipmap.noselected_rotate,
            R.mipmap.noselected_shake,
            R.mipmap.noselected_tragedy,
            R.mipmap.noselected_tv,
            R.mipmap.noselected_wasted
    };
    private int[] fxTexts = {
            R.string.super_zoom_dramatization,
            R.string.super_zoom_spring,
            R.string.super_zoom_cartoon,
            R.string.super_zoom_daily,
            R.string.super_zoom_Dogpacks,
            R.string.super_zoom_fire,
            R.string.super_zoom_horror,
            R.string.super_zoom_love,
            R.string.super_zoom_no,
            R.string.super_zoom_rhythm,
//            R.string.super_zoom_rotate,
            R.string.super_zoom_shake,
            R.string.super_zoom_tragedy,
            R.string.super_zoom_TV,
            R.string.super_zoom_wasted
    };

    public CenterHorizontalViewAdapter(Context context, List<NvAsset> datas, int circle) {
        this.mContext = context;
        this.mDatas = datas;
        this.circle = circle;
    }

    @Override
    public FxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.super_zoom_rv_item, parent, false);
        return new FxViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final FxViewHolder holder, final int position) {
        int size = mDatas.size();
        holder.mName.setText(fxTexts[position % size]);
        holder.mWholeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击进行切换
                if (mItemClickCallBack != null) {
                    mItemClickCallBack.itemclicked(holder, position);
                }
            }
        });
        // 解决下载刷新和点击冲突问题
        holder.mWholeItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mItemTouchCallBack != null) {
                    mItemTouchCallBack.itemTouched(v, event);
                }
                return false;
            }
        });
        if (TextUtils.isEmpty(mDatas.get(position).localDirPath) && TextUtils.isEmpty(mDatas.get(position).bundledLocalDirPath)) {
            //滑进该item时候，显示下载进度
            //未下载更新进度
            if (mDatas.get(position).downloadStatus == NvAsset.DownloadStatusNone) {
                (holder).mNeedDownloadIcon.setVisibility(View.VISIBLE);
                (holder).mCircleProgressBar.setVisibility(View.INVISIBLE);
            } else {
                (holder).mNeedDownloadIcon.setVisibility(View.INVISIBLE);
                (holder).mCircleProgressBar.setVisibility(View.VISIBLE);
            }
            // 更新下载进度
            holder.mCircleProgressBar.drawProgress(mDatas.get(position).downloadProgress);
        } else {
            // 已经完成下载
            (holder).mNeedDownloadIcon.setVisibility(View.INVISIBLE);
            (holder).mCircleProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public interface ItemClickCallBack {
        void itemclicked(FxViewHolder holder, int position);
    }

    public void setItemClickListener(ItemClickCallBack itemClickCallBack) {
        mItemClickCallBack = itemClickCallBack;
    }

    public interface ItemTouchCallBack {
        void itemTouched(View v, MotionEvent event);
    }

    public void setItemTouchListener(ItemTouchCallBack itemTouchCallBack) {
        mItemTouchCallBack = itemTouchCallBack;
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size() * circle;
    }

    @Override
    public View getItemView() {
        return mView;
    }

    public List<NvAsset> getData() {
        return mDatas;
    }

    @Override
    public void onViewSelected(boolean isSelected, int pos, RecyclerView.ViewHolder holder, int itemWidth) {
        ((FxViewHolder) holder).mSelectedBg.setBackgroundResource(selectedRes[pos]);
        ((FxViewHolder) holder).unSelectedBg.setBackgroundResource(unSelectedRes[pos]);
        if (isSelected) {
            ((FxViewHolder) holder).mSelectedBg.setVisibility(View.VISIBLE);
            ((FxViewHolder) holder).unSelectedBg.setVisibility(View.INVISIBLE);
            ((FxViewHolder) holder).mName.setVisibility(View.VISIBLE);
        } else {
            ((FxViewHolder) holder).mSelectedBg.setVisibility(View.INVISIBLE);
            ((FxViewHolder) holder).unSelectedBg.setVisibility(View.VISIBLE);
            ((FxViewHolder) holder).mName.setVisibility(View.INVISIBLE);
        }
    }

    public static class FxViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        ImageView mNeedDownloadIcon;
        CircleProgressBar mCircleProgressBar;
        ImageView unSelectedBg;
        ImageView mSelectedBg;
        LinearLayout mWholeItem;


        FxViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.super_zoom_item_text_tv);
            mNeedDownloadIcon = (ImageView) itemView.findViewById(R.id.need_download_icon_iv);
            mCircleProgressBar = (CircleProgressBar) itemView.findViewById(R.id.super_zoom_circle_progressbar);
            unSelectedBg = (ImageView) itemView.findViewById(R.id.super_zoom_unselected_bg_iv);
            mSelectedBg = (ImageView) itemView.findViewById(R.id.super_zoom_item_selected_bg_iv);
            mWholeItem = (LinearLayout) itemView.findViewById(R.id.super_zoom_fx_item_ll);
        }
    }
}
