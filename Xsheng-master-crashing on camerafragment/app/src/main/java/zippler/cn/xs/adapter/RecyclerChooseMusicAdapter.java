package zippler.cn.xs.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import zippler.cn.xs.R;
import zippler.cn.xs.component.LinearProgressBar;
import zippler.cn.xs.entity.Music;
import zippler.cn.xs.holder.MusicViewHolder;

/**
 * Created by Zipple on 2018/5/6.
 * inject data by using adapter view holder
 */
public class RecyclerChooseMusicAdapter extends RecyclerView.Adapter<MusicViewHolder> {

    private List<Music> musicList;
    private Context context;


    private String TAG=this.getClass().getSimpleName();
    public RecyclerChooseMusicAdapter(Context context,List<Music> musicList) {
        this.musicList = musicList;
        this.context = context;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_choose_music_item,parent,false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.getMusicName().setText(music.getName());
        holder.getMusicLength().setText(music.getLength());

        final MediaPlayer player = MediaPlayer.create(this.getContext(), Uri.parse(music.getLocalStorageUrl()));
        final ImageView playBtn = holder.getPlay();
        final int duration = music.getDuration();//ms
        final LinearProgressBar progressBar = holder.getMusicProgress();//progress bar.
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int pos = (int)(((float)player.getCurrentPosition()/duration)*100);
                Log.d(TAG, "run: duration:"+duration);
                Log.d(TAG, "run: currentPosition:"+player.getCurrentPosition());
                Log.d(TAG, System.currentTimeMillis()+" ms run: set progress :"+pos);
                progressBar.setProgress(pos);
                handler.postDelayed(this,500);
            }
        };
        holder.getPlay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying()){
                    playBtn.setImageResource(R.mipmap.play);
                    player.pause();
                    handler.removeCallbacks(runnable);
                }else{
                    playBtn.setImageResource(R.mipmap.pause_dark);
                    player.start();
                    handler.postDelayed(runnable,500);
                }
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
