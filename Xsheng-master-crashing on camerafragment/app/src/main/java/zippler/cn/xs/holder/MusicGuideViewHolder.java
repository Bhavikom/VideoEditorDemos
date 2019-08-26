package zippler.cn.xs.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import zippler.cn.xs.R;

/**
 * Created by Zipple on 2018/5/6.
 * initial recycler music item
 */
public class MusicGuideViewHolder extends RecyclerView.ViewHolder {
    private ImageView musicAvatar;
    private TextView musicName;
    private TextView musicLength;
    private ImageView play;

    public MusicGuideViewHolder(View itemView) {
        super(itemView);
        musicAvatar = itemView.findViewById(R.id.music_avatar_c);
        musicName = itemView.findViewById(R.id.music_name_c);
        musicLength = itemView.findViewById(R.id.music_length_c);
        play = itemView.findViewById(R.id.music_play_c);
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

}
