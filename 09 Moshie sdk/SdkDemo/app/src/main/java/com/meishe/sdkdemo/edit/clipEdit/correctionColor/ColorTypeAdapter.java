package com.meishe.sdkdemo.edit.clipEdit.correctionColor;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.meishe.sdkdemo.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyj on 2018/10/11 0011.
 */

public class ColorTypeAdapter extends RecyclerView.Adapter<ColorTypeAdapter.ViewHolder> {
    private Context mContext;
    private OnItemClickListener mClickListener;
    private List<ColorTypeItem> mColorTypeList = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(View view, ColorTypeItem colorTypeItem);
    }

    public ColorTypeAdapter(Context context, List<ColorTypeItem> colorTypeList) {
        mContext = context;
        this.mColorTypeList = colorTypeList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_name;
        public ViewHolder(View view) {
            super(view);
            item_name = (TextView) view.findViewById(R.id.name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color_type, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ColorTypeItem colorTypeItem = mColorTypeList.get(position);
        if(colorTypeItem == null) {
            return;
        }
        holder.item_name.setText(colorTypeItem.getColorAtrubuteText());

        if(colorTypeItem.isSelected()) {
            holder.item_name.setTextColor(ContextCompat.getColor(mContext, R.color.ms994a90e2));
        } else {
            holder.item_name.setTextColor(ContextCompat.getColor(mContext, R.color.ccffffff));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < mColorTypeList.size(); ++i) {
                    if(i == position) {
                        mColorTypeList.get(i).setSelected(true);
                    } else {
                        mColorTypeList.get(i).setSelected(false);
                    }
                }
                notifyDataSetChanged();

                if(mClickListener != null) {
                    mClickListener.onItemClick(view, colorTypeItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mColorTypeList.size();
    }
}
