package zippler.cn.xs.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import zippler.cn.xs.R;
import zippler.cn.xs.component.CircleImageView;

/**
 * Created by Zipple on 2018/5/5.
 * Instantiate view
 */
public class VideoViewHolder extends RecyclerView.ViewHolder {
    private ImageView poster;
    private TextView name;
    private TextView time;
    private CircleImageView avatar;
    private ImageView play;
    private TextView length;
    private VideoView videoview;
    private ImageView comment;
    private ImageView redeploy;
    private ImageView love;
    private RelativeLayout videoRoot;
    private ImageView loading;

    public VideoViewHolder(View itemView) {
        super(itemView);
        poster = itemView.findViewById(R.id.poster);
        name = itemView.findViewById(R.id.video_name);
        time = itemView.findViewById(R.id.video_time);
        avatar = itemView.findViewById(R.id.avatar);
        length = itemView.findViewById(R.id.length);
        play = itemView.findViewById(R.id.play);
        videoview = itemView.findViewById(R.id.video_show);
        comment = itemView.findViewById(R.id.comment);
        redeploy = itemView.findViewById(R.id.redeploy);
        love = itemView.findViewById(R.id.love);
        videoRoot = itemView.findViewById(R.id.video_root);
        loading = itemView.findViewById(R.id.loading_movie);
    }

    public ImageView getPoster() {
        return poster;
    }

    public void setPoster(ImageView poster) {
        this.poster = poster;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    public CircleImageView getAvatar() {
        return avatar;
    }

    public void setAvatar(CircleImageView avatar) {
        this.avatar = avatar;
    }

    public TextView getLength() {
        return length;
    }

    public void setLength(TextView length) {
        this.length = length;
    }

    public ImageView getPlay() {
        return play;
    }

    public void setPlay(ImageView play) {
        this.play = play;
    }

    public VideoView getVideoview() {
        return videoview;
    }

    public void setVideoview(VideoView videoview) {
        this.videoview = videoview;
    }

    public ImageView getComment() {
        return comment;
    }

    public void setComment(ImageView comment) {
        this.comment = comment;
    }

    public ImageView getRedeploy() {
        return redeploy;
    }

    public void setRedeploy(ImageView redeploy) {
        this.redeploy = redeploy;
    }

    public ImageView getLove() {
        return love;
    }

    public void setLove(ImageView love) {
        this.love = love;
    }

    public RelativeLayout getVideoRoot() {
        return videoRoot;
    }

    public void setVideoRoot(RelativeLayout videoRoot) {
        this.videoRoot = videoRoot;
    }

    public ImageView getLoading() {
        return loading;
    }

    public void setLoading(ImageView loading) {
        this.loading = loading;
    }
}
