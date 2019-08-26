package com.meishe.sdkdemo.edit.music;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.utils.dataInfo.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ms on 2018/7/15 0015.
 */

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private Context mContext;
    private OnItemClickListener mClickListener;
    private List<MusicInfo> mMusicDataList = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(int position, MusicInfo audioInfo);
    }

    public MusicListAdapter(Context context, List<MusicInfo> dataList) {
        mContext = context;
        mMusicDataList = dataList;
    }

    public void updateData(List<MusicInfo> dataList) {
        mMusicDataList = dataList;
        notifyDataSetChanged();
    }

    public void clearPlayState() {
        if(mMusicDataList == null) {
            return;
        }
        for(MusicInfo audioInfo: mMusicDataList) {
            if(audioInfo == null) {
                continue;
            }
            audioInfo.setPlay(false);
            audioInfo.setPrepare(false);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_music_name;
        private TextView item_music_author;
        private ImageButton item_music_play_btn;
        public ViewHolder(View view) {
            super(view);
            item_music_name = (TextView) view.findViewById(R.id.music_name);
            item_music_author = (TextView) view.findViewById(R.id.music_author);
            item_music_play_btn = (ImageButton) view.findViewById(R.id.music_play_btn);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(position >= mMusicDataList.size()) {
            return;
        }
        final MusicInfo audioInfo = mMusicDataList.get(position);
        if(audioInfo == null) {
            return;
        }
        holder.item_music_name.setText(audioInfo.getTitle());

        holder.item_music_author.setText(audioInfo.getArtist());

        if(audioInfo.isPlay()) {
            holder.item_music_play_btn.setBackground(ContextCompat.getDrawable(mContext, R.drawable.music_pause));
        } else {
            holder.item_music_play_btn.setBackground(ContextCompat.getDrawable(mContext, R.drawable.music_play));
        }

        holder.item_music_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener != null) {
                    mClickListener.onItemClick(position, audioInfo);
                }

                if(audioInfo.isPlay()) {
                    audioInfo.setPlay(false);
                } else {
                    for(int i = 0; i < mMusicDataList.size(); ++i) {
                        if(i == position) {
                            mMusicDataList.get(i).setPlay(true);
                        } else {
                            mMusicDataList.get(i).setPlay(false);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMusicDataList.size();
    }
}
