package com.meishe.sdkdemo.capture;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
//import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.meishe.sdkdemo.R;


public class BeautyShapeAdapter extends RecyclerView.Adapter<BeautyShapeAdapter.ViewHolder> {

    private ArrayList<BeautyShapeDataItem> mDataList;
    private int mSelectedPos = Integer.MAX_VALUE;
    private Context mContext;
    private OnItemClickListener mClickListener;
    private boolean mIsEnable = true;

    public static final int POS_BEAUTY_STRENGTH_0 = 0;
    public static final int POS_BEAUTY_WHITING_1 = 1;
    public static final int POS_BEAUTY_REDDING_2 = 2;
    public static final int POS_BEAUTY_ADJUSTCOLOR_3 = 3;
    public static final int POS_BEAUTY_SHARPEN_4 = 4;

    public BeautyShapeAdapter(Context context, ArrayList dataList) {
        mContext = context;
        mDataList = dataList;
    }

    public void setEnable(boolean enable) {
        mIsEnable = enable;
        notifyDataSetChanged();
    }

    public void setSelectPos(int pos) {
        mSelectedPos = pos;
        notifyDataSetChanged();
    }

    public int getSelectPos() {
        return mSelectedPos;
    }

    public BeautyShapeDataItem getSelectItem() {
        if (mDataList != null && mSelectedPos >= 0 && mSelectedPos < mDataList.size()) {
            return mDataList.get(mSelectedPos);
        }
        return null;
    }

    public void setWittenName(int pos, String newName) {
        if (mDataList != null && pos >= 0 && pos < mDataList.size()) {
            BeautyShapeDataItem item = mDataList.get(pos);
            if(item == null) {
                return;
            }
            item.name = newName;
            notifyItemChanged(pos);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void updateDataList(ArrayList dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beauty_shape_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BeautyShapeDataItem item = mDataList.get(position);
        holder.shape_icon.setImageResource(item.resId);
        holder.shape_name.setText(item.name);

        if (mIsEnable) {
            holder.shape_name.setTextColor(Color.WHITE);
        } else {
            holder.shape_name.setTextColor(mContext.getResources().getColor(R.color.ms_disable_color));
        }

        if (mIsEnable && mSelectedPos == position) {
            holder.shape_name.setTextColor(Color.parseColor("#CC4A90E2"));
            holder.shape_icon_layout.setAlpha(1.0f);
            holder.shape_name.setAlpha(1.0f);
            GradientDrawable background = (GradientDrawable) holder.shape_icon_layout.getBackground();
            background.setColor(mContext.getResources().getColor(R.color.selcet_bg_blue));
        } else {
            if (mIsEnable && mSelectedPos != position) {
                GradientDrawable background = (GradientDrawable) holder.shape_icon_layout.getBackground();
                background.setColor(mContext.getResources().getColor(R.color.ms_disable_color));
                holder.shape_name.setTextColor(Color.WHITE);
                holder.shape_icon_layout.setAlpha(1.0f);
                holder.shape_name.setAlpha(1.0f);

            } else if (!mIsEnable) {
                GradientDrawable background = (GradientDrawable) holder.shape_icon_layout.getBackground();
                background.setColor(mContext.getResources().getColor(R.color.ms_disable_color));
                holder.shape_name.setTextColor(Color.WHITE);
                holder.shape_icon_layout.setAlpha(0.5f);
                holder.shape_name.setAlpha(0.5f);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsEnable) {
                    return;
                }
                if (mClickListener != null) {
                    notifyItemChanged(mSelectedPos);
                    mSelectedPos = position;
                    notifyItemChanged(mSelectedPos);
                    mClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout shape_icon_layout;
        private ImageView shape_icon;
        private TextView shape_name;


        public ViewHolder(View view) {
            super(view);
            shape_icon_layout = (RelativeLayout) view.findViewById(R.id.shape_icon_layout);
            shape_icon = (ImageView) view.findViewById(R.id.shape_icon);
            shape_name = (TextView) view.findViewById(R.id.shape_txt);
        }
    }

}
