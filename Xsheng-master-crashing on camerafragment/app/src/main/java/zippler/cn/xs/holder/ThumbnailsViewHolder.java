package zippler.cn.xs.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import zippler.cn.xs.R;

/**
 * Created by Zipple on 2018/5/8.
 */
public class ThumbnailsViewHolder extends RecyclerView.ViewHolder {
    private ImageView item;
    public ThumbnailsViewHolder(View itemView) {
        super(itemView);
        item = itemView.findViewById(R.id.thumbnail_item);
    }

    public ImageView getItem() {
        return item;
    }

    public void setItem(ImageView item) {
        this.item = item;
    }
}
