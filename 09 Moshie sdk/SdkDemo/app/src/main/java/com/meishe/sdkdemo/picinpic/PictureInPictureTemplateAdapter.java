package com.meishe.sdkdemo.picinpic;

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
import com.meishe.sdkdemo.picinpic.data.PicInPicTemplateAsset;
import java.util.ArrayList;

/**
 * Created by czl on 2017/12/19 0019.
 */

public class PictureInPictureTemplateAdapter extends RecyclerView.Adapter<PictureInPictureTemplateAdapter.ViewHolder> {
    private Context mContext;
    private OnItemClickListener mClickListener;
    private ArrayList<PicInPicTemplateAsset> mPicInPicDataList = new ArrayList<>();
    private int mSelectPos = 0;
    public interface OnItemClickListener {
        void onItemClick(View view,int position);
    }
    public PictureInPictureTemplateAdapter(Context context) {
        mContext = context;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }
    public void setPicInPicDataList(ArrayList<PicInPicTemplateAsset> picInPicDataList) {
        this.mPicInPicDataList = picInPicDataList;
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
        PicInPicTemplateAsset itemData = mPicInPicDataList.get(position);
        holder.item_assetName.setText(itemData.getTemplateName());
        //加载图片
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.mipmap.default_theme);
        Glide.with(mContext)
                .asBitmap()
                .load(itemData.getTemplateCover())
                .apply(options)
                .into(holder.item_assetImage);

        if(mSelectPos == position) {
            holder.item_assetLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.fx_item_radius_shape_select));
            holder.item_assetName.setTextColor(Color.parseColor("#4a90e2"));
        } else {
            holder.item_assetLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.fx_item_radius_shape_unselect));
            holder.item_assetName.setTextColor(Color.parseColor("#ffffff"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectPos == position) {
                    return;
                }

                mSelectPos = position;
                notifyDataSetChanged();
                if(mClickListener != null) {
                    mClickListener.onItemClick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPicInPicDataList.size();
    }
}
