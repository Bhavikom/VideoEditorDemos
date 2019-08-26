package zippler.cn.xs.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Random;

import zippler.cn.xs.R;
import zippler.cn.xs.entity.Comment;
import zippler.cn.xs.holder.CommentViewHolder;

/**
 * Created by ice on 2018/5/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(View.inflate(context, R.layout.item_comment,null));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvName.setText(comment.getName());
        holder.ivPic.setImageResource(comment.getPic());
        holder.tvTime.setText(comment.getTime());
        holder.tvLikeCount.setText(new Random().nextInt(3000)+"");
        holder.tvComment.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
