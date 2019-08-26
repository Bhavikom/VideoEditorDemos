package com.meishe.sdkdemo.edit.grallyRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseConstants;
import com.meishe.sdkdemo.edit.interfaces.OnGrallyItemClickListener;
import com.meishe.sdkdemo.utils.dataInfo.ClipInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by CaoZhiChao on 2018/5/29 15:06
 */
public class GrallyAdapter extends RecyclerView.Adapter<GrallyAdapter.ViewHolder> implements ItemTouchListener {
    private Context mContext;
    private ArrayList<ClipInfo> mClipInfoArray = new ArrayList<>();
    private GrallyScaleHelper mCardAdapterHelper = new GrallyScaleHelper();
    private OnGrallyItemClickListener m_onItemClickListener = null;
    private int mSelectPos = 0;

    public GrallyAdapter(Context context) {
        this.mContext = context;
    }

    public void setClipInfoArray(ArrayList<ClipInfo> clipInfoArray) {
        this.mClipInfoArray = clipInfoArray;
    }

    public void setOnItemSelectedListener(OnGrallyItemClickListener m_onItemClickListener) {
        this.m_onItemClickListener = m_onItemClickListener;
    }

    public void setSelectPos(int pos) {
        mSelectPos = pos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_grally, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        setBindViewHolder(holder, position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            setBindViewHolder(holder, position);
        }
    }

    private void setBindViewHolder(final ViewHolder holder, int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount(), holder.mImageView);
        if (mSelectPos != position) {
            holder.itemView.setScaleY(BaseConstants.EDITGRALLYSCALE);
            holder.line_left.setVisibility(View.GONE);
            holder.line_right.setVisibility(View.GONE);
            holder.iv_left.setVisibility(View.GONE);
            holder.iv_right.setVisibility(View.GONE);
        } else {
            holder.itemView.setScaleY(1);
            holder.line_left.setVisibility(View.VISIBLE);
            holder.line_right.setVisibility(View.VISIBLE);
            holder.iv_left.setVisibility(View.VISIBLE);
            holder.iv_right.setVisibility(View.VISIBLE);

            holder.line_left.setAlpha(1f);
            holder.line_right.setAlpha(1f);
            holder.iv_left.setAlpha(1f);
            holder.iv_right.setAlpha(1f);

            holder.line_left.setScaleX(1f);
            holder.line_right.setScaleX(1f);
            holder.iv_left.setScaleX(1f);
            holder.iv_right.setScaleX(1f);
            holder.iv_left.setScaleY(1f);
            holder.iv_right.setScaleY(1f);
        }
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.edit_clip_default_bg);
        String filePath = mClipInfoArray.get(position).getFilePath();
        Glide.with(mContext)
                .load(filePath)
                .apply(options)
                .into(holder.mImageView);
        holder.iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_onItemClickListener.onLeftItemClick(v, holder.getAdapterPosition());
            }
        });
        holder.iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_onItemClickListener.onRightItemClick(v, holder.getAdapterPosition() + 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mClipInfoArray.size();
    }

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        mSelectPos = toPosition;
        Collections.swap(mClipInfoArray, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        if (m_onItemClickListener != null)
            m_onItemClickListener.onItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mClipInfoArray.remove(position);
        notifyItemRemoved(position);
        if (m_onItemClickListener != null)
            m_onItemClickListener.onItemDismiss(position);
    }

    @Override
    public void removeAll() {
        mClipInfoArray.clear();
        if (m_onItemClickListener != null)
            m_onItemClickListener.removeall();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView, line_left, line_right, iv_left, iv_right;

        ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.edit_centerimage);
            line_left = (ImageView) itemView.findViewById(R.id.line_left);
            line_right = (ImageView) itemView.findViewById(R.id.line_right);
            iv_left = (ImageView) itemView.findViewById(R.id.addImage_left);
            iv_right = (ImageView) itemView.findViewById(R.id.addImage_right);
        }
    }
}
