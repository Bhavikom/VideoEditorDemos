package zippler.cn.xs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zippler.cn.xs.R;
import zippler.cn.xs.activity.GuideRecorderActivity;
import zippler.cn.xs.entity.Music;
import zippler.cn.xs.holder.MusicGuideViewHolder;

/**
 * Created by Zipple on 2018/5/6.
 * inject data by using adapter view holder
 */
public class MusicChooseAdapter extends RecyclerView.Adapter<MusicGuideViewHolder> {

    private List<Music> musicList;
    private Context context;


    private String TAG=this.getClass().getSimpleName();
    public MusicChooseAdapter(Context context, List<Music> musicList) {
        this.musicList = musicList;
        this.context = context;
    }

    @NonNull
    @Override
    public MusicGuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_music_guide_item,parent,false);
        return new MusicGuideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MusicGuideViewHolder holder, final int position) {
        Music music = musicList.get(position);
        holder.getMusicName().setText(music.getName());
        holder.getMusicLength().setText(music.getLength());
        holder.getPlay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GuideRecorderActivity.class);
                intent.putExtra("music",musicList.get(holder.getAdapterPosition()).getLocalStorageUrl());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return musicList.size();
    }


    public List<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
