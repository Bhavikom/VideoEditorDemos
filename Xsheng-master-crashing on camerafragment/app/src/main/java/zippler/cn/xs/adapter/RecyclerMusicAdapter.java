package zippler.cn.xs.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import zippler.cn.xs.R;

/**
 * Created by Zipple on 2018/5/12.
 * set music in recycler view which origin from background internet.
 */
public class RecyclerMusicAdapter extends RecyclerView.Adapter<RecyclerMusicAdapter.MusicHolder> {

    private ArrayList<String> data;
    private String[] titles = {"大钢琴","尼龙弦吉他","小提琴","原生贝斯"};
    private Context c;

    public RecyclerMusicAdapter(Context c , ArrayList<String> data) {
        this.data = data;
        this.data.add("没有啦，再换换？");
        this.c = c;
    }

    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item,parent,false);
        //set some listener here.
        return new MusicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
        if (position==data.size()-1){
            holder.getTextView().setText(data.get(position));
            holder.getRelativeLayout().setBackgroundColor(c.getResources().getColor(R.color.colorBlack2));
        }else{
            holder.getTextView().setText(titles[position]);
        }
        holder.getTextView().setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MusicHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private RelativeLayout relativeLayout;
        MusicHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.music_text);
            relativeLayout = itemView.findViewById(R.id.bgm_bg);
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public RelativeLayout getRelativeLayout() {
            return relativeLayout;
        }

        public void setRelativeLayout(RelativeLayout relativeLayout) {
            this.relativeLayout = relativeLayout;
        }
    }
}
