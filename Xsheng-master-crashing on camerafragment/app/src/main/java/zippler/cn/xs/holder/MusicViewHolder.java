package zippler.cn.xs.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import zippler.cn.xs.R;
import zippler.cn.xs.component.LinearProgressBar;

/**
 * Created by Zipple on 2018/5/6.
 * initial recycler music item
 */
public class MusicViewHolder extends RecyclerView.ViewHolder {
    private ImageView musicAvatar;
    private TextView musicName;
    private TextView musicAuthor;
    private TextView musicLength;
    private ImageView play;
    private LinearProgressBar musicProgress;

    public MusicViewHolder(View itemView) {
        super(itemView);
        musicAvatar = itemView.findViewById(R.id.music_avatar);
        musicName = itemView.findViewById(R.id.music_name);
        musicAuthor = itemView.findViewById(R.id.music_author);
        musicLength = itemView.findViewById(R.id.music_length);
        play = itemView.findViewById(R.id.music_play);
        musicProgress= itemView.findViewById(R.id.music_progress);
    }

    public ImageView getMusicAvatar() {
        return musicAvatar;
    }

    public void setMusicAvatar(ImageView musicAvatar) {
        this.musicAvatar = musicAvatar;
    }

    public TextView getMusicName() {
        return musicName;
    }

    public void setMusicName(TextView musicName) {
        this.musicName = musicName;
    }

    public TextView getMusicAuthor() {
        return musicAuthor;
    }

    public void setMusicAuthor(TextView musicAuthor) {
        this.musicAuthor = musicAuthor;
    }

    public TextView getMusicLength() {
        return musicLength;
    }

    public void setMusicLength(TextView musicLength) {
        this.musicLength = musicLength;
    }

    public ImageView getPlay() {
        return play;
    }

    public void setPlay(ImageView play) {
        this.play = play;
    }

    public LinearProgressBar getMusicProgress() {
        return musicProgress;
    }

    public void setMusicPogress(LinearProgressBar musicProgress) {
        this.musicProgress = musicProgress;
    }
}
