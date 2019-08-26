package zippler.cn.xs.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import zippler.cn.xs.R;

/**
 * Created by ice on 2018/5/17.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivPic;
    public ImageView ivLike;
    public TextView tvName,tvComment,tvTime,tvLikeCount;
    public CommentViewHolder(View itemView) {
        super(itemView);

        ivPic = itemView.findViewById(R.id.image);
        ivLike = itemView.findViewById(R.id.image_like);
        tvName = itemView.findViewById(R.id.name);
        tvComment = itemView.findViewById(R.id.comment);
        tvTime = itemView.findViewById(R.id.time);
        tvLikeCount = itemView.findViewById(R.id.like_count);

    }


}
