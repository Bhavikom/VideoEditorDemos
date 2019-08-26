package com.meishe.sdkdemo.musicLyrics;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.adapter.AssetRecyclerViewAdapter;
import com.meishe.sdkdemo.edit.data.AssetInfoDescription;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;

import java.util.ArrayList;

public class LrcCaptionRecyclerViewAdapter extends RecyclerView.Adapter<LrcCaptionRecyclerViewAdapter.ViewHolder>  {

    private ArrayList<AssetInfoDescription> m_captionInfolist;
    private Context m_mContext;
    private OnItemClickListener m_onItemClickListener = null;
    private int mSelectPos = 0;

    public void setSelectPos(int pos) {
        this.mSelectPos = pos;
    }

    public LrcCaptionRecyclerViewAdapter(Context context) {
        m_mContext = context;
    }

    public void updateData(ArrayList<AssetInfoDescription> captionInfoList) {
        m_captionInfolist = captionInfoList;
        notifyDataSetChanged();
    }

    @Override
    public LrcCaptionRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(m_mContext).inflate(R.layout.lrc_item, parent, false);
        LrcCaptionRecyclerViewAdapter.ViewHolder viewHolder = new LrcCaptionRecyclerViewAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LrcCaptionRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.mImageAsset.setImageResource(m_captionInfolist.get(position).mImageId);
        holder.mAssetName.setText(m_captionInfolist.get(position).mAssetName);

        if (mSelectPos == position) {
            holder.mselectedItem.setVisibility(View.VISIBLE);
            holder.mAssetName.setTextColor(ContextCompat.getColor(m_mContext, R.color.ms994a90e2));
        } else {
            holder.mselectedItem.setVisibility(View.GONE);
            holder.mAssetName.setTextColor(ContextCompat.getColor(m_mContext, R.color.ccffffff));
        }

        if(m_onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String[] assetName = m_mContext.getResources().getStringArray(R.array.videoEdit);
                    v.setTag(assetName[position]);
                    m_onItemClickListener.onItemClick(v,position);

                    if (mSelectPos == position)
                        return;

                    notifyItemChanged(mSelectPos);
                    mSelectPos = position;
                    notifyItemChanged(mSelectPos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return m_captionInfolist.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.m_onItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageAsset;
        TextView mAssetName;
        View mselectedItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mImageAsset = (ImageView) itemView.findViewById(R.id.imageAsset);
            mAssetName = (TextView) itemView.findViewById(R.id.nameAsset);
            mselectedItem = (View)itemView.findViewById(R.id.selectedItem);
        }
    }
}
