package zippler.cn.xs.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import zippler.cn.xs.R;
import zippler.cn.xs.activity.PreviewFullVideoActivity;
import zippler.cn.xs.util.ImageFileUtil;

/**
 * Created by Zipple on 2018/5/14.
 */
public class UserFavVidPreAdapter extends RecyclerView.Adapter<UserFavVidPreAdapter.VideoViewHolder> {
    private Context context;
    private List<String> videos;

    public UserFavVidPreAdapter(){

    }

    public UserFavVidPreAdapter(Context context, List<String> videoList) {
        this.context = context;
        this.videos = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_video_thub_item,parent,false);
        return new UserFavVidPreAdapter.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        final String path = videos.get(position);
        ImageFileUtil.setFirstFrame(holder.getImageView(),context, Uri.parse(videos.get(position)));
        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PreviewFullVideoActivity.class);
                intent.putExtra("videoPath",path);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        VideoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.my_video_img);
        }
        ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}
