package zippler.cn.xs.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zippler.cn.xs.R;
import zippler.cn.xs.entity.Fans;

/**
 * Created by Zipple on 2018/5/14.
 */
public class UserFansAdapter extends RecyclerView.Adapter<UserFansAdapter.FansHolder> {

    private List<Fans> data ;
    public UserFansAdapter(List<Fans> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public FansHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_user_fans_item,parent,false);
        return new FansHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FansHolder holder, int position) {
        ImageView avatar = holder.getAvatar();
        TextView username = holder.getUsername();
        TextView description = holder.getDescription();

        Fans fans = data.get(position);

        //change avatar url if the data is loaded from internet.
        avatar.setImageResource(Integer.parseInt(fans.getAvatar()));
        username.setText(fans.getUsername());
        description.setText(fans.getIntroduction());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FansHolder extends RecyclerView.ViewHolder{
        private ImageView avatar;
        private TextView username;
        private TextView description;
        public FansHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar_circle_image_view);
            username = (TextView) itemView.findViewById(R.id.username_text_view);
            description = (TextView) itemView.findViewById(R.id.desc_text_view);
        }
        public ImageView getAvatar() {
            return avatar;
        }

        public void setAvatar(ImageView avatar) {
            this.avatar = avatar;
        }

        public TextView getUsername() {
            return username;
        }

        public void setUsername(TextView username) {
            this.username = username;
        }

        public TextView getDescription() {
            return description;
        }

        public void setDescription(TextView description) {
            this.description = description;
        }
    }
}
