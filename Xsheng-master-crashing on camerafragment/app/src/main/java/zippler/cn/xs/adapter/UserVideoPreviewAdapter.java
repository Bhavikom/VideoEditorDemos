package zippler.cn.xs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import zippler.cn.xs.R;
import zippler.cn.xs.activity.PreviewFullVideoActivity;

/**
 * Created by Zipple on 2018/5/14.
 */
public class UserVideoPreviewAdapter extends RecyclerView.Adapter<UserVideoPreviewAdapter.VideoViewHolder> {
    private Context context;
    private List<String> videos;
    public UserVideoPreviewAdapter(){}

    public UserVideoPreviewAdapter(Context context, List<String> pathList){
        this.context = context;
        this.videos = pathList;
    }

    @NonNull
    @Override
    public UserVideoPreviewAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_video_thub_item,parent,false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserVideoPreviewAdapter.VideoViewHolder holder, final int position) {
        final String path = videos.get(position);
//        ImageFileUtil.setFirstFrame(holder.getImageView(),videos.get(position));
        Glide.with(context).load(new File(path)).thumbnail(1.0f).into(holder.getImageView());
        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PreviewFullVideoActivity.class);
                intent.putExtra("videoPath",path);
                context.startActivity(intent);
            }
        });
        //delete operations..
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
