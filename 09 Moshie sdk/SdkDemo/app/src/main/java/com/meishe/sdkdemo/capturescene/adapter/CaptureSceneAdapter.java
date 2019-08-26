package com.meishe.sdkdemo.capturescene.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.capturescene.data.CaptureSceneOnlineData;
import com.meishe.sdkdemo.capturescene.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.capturescene.view.CircleBarView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.meishe.sdkdemo.capturescene.data.Constants.CAPTURE_SCENE_LOCAL;
import static com.meishe.sdkdemo.capturescene.data.Constants.RESOURCE_NEW_PATH;

/**
 * Created by CaoZhiChao on 2019/1/3 19:51
 */
public class CaptureSceneAdapter extends RecyclerView.Adapter<CaptureSceneAdapter.CaptureSceneViewHolder> {
    private final String TAG = "CaptureSceneAdapter";

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int position) {
        int previousPosition = selectPosition;
        selectPosition = position;
        notifyItemChanged(previousPosition);
        notifyItemChanged(selectPosition);

    }

    private int selectPosition = -1;
    List<Integer> downloadingProgressList = new ArrayList<>();

    public synchronized void setmProgress(int mProgress, int position) {
        downloadingProgressList.set(position, mProgress);
        notifyItemChanged(position, "刷新");
    }

    public void setDataList(List<CaptureSceneOnlineData.CaptureSceneDetails> dataList) {
        this.dataList = dataList;
        for (int i = 0; i < dataList.size(); i++) {
            downloadingProgressList.add(0);
        }
        notifyDataSetChanged();
    }

    public void setDataList(int position, CaptureSceneOnlineData.CaptureSceneDetails dataList, boolean refresh) {
        this.dataList.set(position, dataList);
        if (refresh) {
            notifyItemChanged(position);
        }
    }

    private List<CaptureSceneOnlineData.CaptureSceneDetails> dataList;
    private Context mContext;
    private OnItemClickListener itemClickListener;

    public CaptureSceneAdapter(Context context, List<CaptureSceneOnlineData.CaptureSceneDetails> list, OnItemClickListener onItemClickListener) {
        this.dataList = list;
        this.mContext = context;
        itemClickListener = onItemClickListener;
    }

    @Override
    public CaptureSceneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CaptureSceneViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_cs, parent,
                false));
    }


    @Override
    public void onBindViewHolder(CaptureSceneViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String imageUrl = dataList.get(position).getCoverUrl();
        if (selectPosition == position) {
            holder.imageView_cover.setVisibility(View.VISIBLE);
        } else {
            holder.imageView_cover.setVisibility(View.GONE);
        }
        if (imageUrl.contains(RESOURCE_NEW_PATH)) {
            Glide.with(mContext).load(imageUrl).into(holder.imageView);
        } else {
            File file = new File(imageUrl);
            if (file.exists()) {
                Glide.with(mContext).load(file).into(holder.imageView);
            } else {
                Glide.with(mContext).load(R.drawable.capturescene_default).into(holder.imageView);
            }
        }
        if (dataList.get(position).getType() == CAPTURE_SCENE_LOCAL) {
            holder.circleBarView.setVisibility(View.GONE);
        } else {
            holder.circleBarView.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousPosition = selectPosition;
                selectPosition = position;
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectPosition);
                v.setTag(dataList.get(position));
                itemClickListener.onClick(v, position);
            }
        });
    }

    @Override
    public void onBindViewHolder(CaptureSceneViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            //下载进度更新
            holder.circleBarView.setProgress(downloadingProgressList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CaptureSceneViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView imageView_cover;
        CircleBarView circleBarView;

        CaptureSceneViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.item_cs_image);
            imageView_cover = (ImageView) view.findViewById(R.id.item_cs_cover);
            circleBarView = (CircleBarView) view.findViewById(R.id.item_cs_download);
        }
    }
}
