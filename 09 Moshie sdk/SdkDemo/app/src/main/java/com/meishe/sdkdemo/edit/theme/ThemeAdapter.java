package com.meishe.sdkdemo.edit.theme;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
import com.meishe.sdkdemo.edit.data.FilterItem;

import java.util.ArrayList;

/**
 * Created by yyj on 2017/12/19 0019.
 */

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {
    private Context mContext;
    private OnItemClickListener mClickListener;
    private ArrayList<FilterItem> mThemeDataList = new ArrayList<>();
    private int mSelectPos = 0;
    RequestOptions mOptions = new RequestOptions();

    public interface OnItemClickListener {
        void onItemClick(View view,int position);
    }
    public ThemeAdapter(Context context) {
        mContext = context;
        mOptions.centerCrop();
        mOptions.skipMemoryCache(false);
        mOptions.placeholder(R.mipmap.default_filter);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }
    public void setThemeDataList(ArrayList<FilterItem> themeDataList) {
        this.mThemeDataList = themeDataList;
    }
    public void setSelectPos(int pos) {
        this.mSelectPos = pos;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout item_assetLayout;
        private ImageView item_assetImage;
        private TextView item_assetName;
        public ViewHolder(View view) {
            super(view);
            item_assetLayout = (RelativeLayout) view.findViewById(R.id.layoutAsset);
            item_assetName = (TextView) view.findViewById(R.id.nameAsset);
            item_assetImage = (ImageView) view.findViewById(R.id.imageAsset);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FilterItem itemData = mThemeDataList.get(position);
        holder.item_assetName.setText(itemData.getFilterName());
        int filterMode = itemData.getFilterMode();
        if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
            int imageId = itemData.getImageId();
            if (imageId != 0)
                holder.item_assetImage.setImageResource(imageId);
        }else {
            String imageUrl = itemData.getImageUrl();
            if (imageUrl != null) {
                //加载图片
                Glide.with(mContext)
                        .asBitmap()
                        .load(imageUrl)
                        .apply(mOptions)
                        .into(holder.item_assetImage);
            }
        }

        if(mSelectPos == position) {
            holder.item_assetLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.fx_item_radius_shape_select));
            holder.item_assetName.setTextColor(Color.parseColor("#994a90e2"));
        } else {
            holder.item_assetLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.fx_item_radius_shape_unselect));
            holder.item_assetName.setTextColor(Color.parseColor("#CCffffff"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener != null) {
                    mClickListener.onItemClick(view,position);
                }
                if(mSelectPos == position)
                    return;
                notifyItemChanged(mSelectPos);
                mSelectPos = position;
                notifyItemChanged(mSelectPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mThemeDataList.size();
    }
}
