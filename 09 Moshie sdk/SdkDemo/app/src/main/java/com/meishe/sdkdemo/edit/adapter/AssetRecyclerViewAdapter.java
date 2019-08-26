package com.meishe.sdkdemo.edit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.data.AssetInfoDescription;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by admin on 2018/5/25.
 */

public class AssetRecyclerViewAdapter extends RecyclerView.Adapter<AssetRecyclerViewAdapter.ViewHolder>  {
    private ArrayList<AssetInfoDescription> m_assetInfolist;
    private Context m_mContext;
    private OnItemClickListener m_onItemClickListener = null;

    public AssetRecyclerViewAdapter(Context context) {
        m_mContext = context;
    }

    public void updateData(ArrayList<AssetInfoDescription> assetInfoList) {
        m_assetInfolist = assetInfoList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(m_mContext).inflate(R.layout.item_asset, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mImageAsset.setImageResource(m_assetInfolist.get(position).mImageId);
        holder.mAssetName.setText(m_assetInfolist.get(position).mAssetName);
        if(m_onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String[] assetName = m_mContext.getResources().getStringArray(R.array.videoEdit);
                    v.setTag(assetName[position]);
                    m_onItemClickListener.onItemClick(v,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return m_assetInfolist.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.m_onItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageAsset;
        TextView mAssetName;
        public ViewHolder(View itemView) {
            super(itemView);
            mImageAsset = (ImageView) itemView.findViewById(R.id.imageAsset);
            mAssetName = (TextView) itemView.findViewById(R.id.nameAsset);
        }
    }
}
