package com.meishe.sdkdemo.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meishe.sdkdemo.R;

import java.util.List;

/**
 * Created by CaoZhiChao on 2018/11/14 20:26
 */
public class MainViewPagerFragmentAdapter extends RecyclerView.Adapter<MainViewPagerFragmentAdapter.MainViewPagerFragmentViewHolder> {
    private List<MainViewPagerFragmentData> dataList;
    private Context mContext;
    private OnItemClickListener itemClickListener;
    public MainViewPagerFragmentAdapter(Context context, List<MainViewPagerFragmentData> list, OnItemClickListener onItemClickListener) {
        this.dataList = list;
        this.mContext = context;
        itemClickListener = onItemClickListener;
    }

    @Override
    public MainViewPagerFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewPagerFragmentViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.fragment_main_viewpager_recycleview_item, parent,
                false));
    }

    @Override
    public void onBindViewHolder(MainViewPagerFragmentViewHolder holder, int position) {
        holder.textView.setText(dataList.get(position).getName());
        holder.imageView.setTag(dataList.get(position).getName());
        holder.imageView.setBackground(mContext.getResources().getDrawable(dataList.get(position).getBackGroundId()));
        holder.imageView.setImageDrawable(mContext.getResources().getDrawable(dataList.get(position).getImageId()));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MainViewPagerFragmentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        MainViewPagerFragmentViewHolder(View view) {
            super(view);
            imageView = (ImageView)view.findViewById(R.id.main_fragment_recycle_iv);
            textView = (TextView)view.findViewById(R.id.main_fragment_recycle_tv);
        }
    }
}
