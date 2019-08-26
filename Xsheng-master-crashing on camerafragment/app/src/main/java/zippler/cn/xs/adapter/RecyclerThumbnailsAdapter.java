package zippler.cn.xs.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zippler.cn.xs.R;
import zippler.cn.xs.holder.ThumbnailsViewHolder;

/**
 * Created by Zipple on 2018/5/8.
 * get thumbnails
 */
public class RecyclerThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsViewHolder> {
    private static String TAG = "RecyclerThumbnailsAdapter";
    private List<String> thumbs;

    public RecyclerThumbnailsAdapter(List<String> thumbs) {
        setThumbs(thumbs);
    }

    @NonNull
    @Override
    public ThumbnailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_thumbnails_item,parent,false);
        ThumbnailsViewHolder holder = new ThumbnailsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailsViewHolder holder, int position) {
         Log.d(TAG, "onBindViewHolder: set bitmap");
         String path = thumbs.get(position);
         if (path!=null){
             holder.getItem().setImageURI(Uri.parse(path));
         }else{
             Log.d(TAG, "onBindViewHolder: null bitmap");
         }
    }

    @Override
    public int getItemCount() {
        return thumbs.size();
    }

    public List<String> getThumbs() {
        return thumbs;
    }

    public void setThumbs(List<String> thumbs) {
        this.thumbs = thumbs;
    }
}
