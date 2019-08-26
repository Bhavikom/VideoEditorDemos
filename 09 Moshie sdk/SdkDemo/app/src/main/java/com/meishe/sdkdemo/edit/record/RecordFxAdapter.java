package com.meishe.sdkdemo.edit.record;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 滤镜列表的adapter
 */
public class RecordFxAdapter extends RecyclerView.Adapter<RecordFxAdapter.ViewHolder> {
    private Context mContext;
    private List<RecordFxListItem> mDataList = new ArrayList<>();
    private OnItemClickListener mListener;

    public RecordFxAdapter(Context context, List<RecordFxListItem> list) {
        mContext = context;
        mDataList = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setItemSelectedByFxID(String fx_id) {
        if(fx_id == null || fx_id.isEmpty()) {
            return;
        }
        for(RecordFxListItem fxListItem: mDataList) {
            if(fxListItem == null) {
                continue;
            }
            if(fx_id.equals(fxListItem.fxID)) {
                fxListItem.selected = true;
            } else {
                fxListItem.selected = false;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.record_fx_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (mDataList == null || position >= mDataList.size() || position < 0)
            return;

        final RecordFxListItem recordFxListItem = mDataList.get(position);
        if (recordFxListItem == null)
            return;

        holder.fx_name.setText(recordFxListItem.fxName);
        if(recordFxListItem.image_drawable != null) {
            holder.fx_image.setBackground(recordFxListItem.image_drawable);
        }

        if (recordFxListItem.selected) {
            holder.itemView.setSelected(true);
            holder.fx_name.setTextColor(mContext.getResources().getColor(R.color.fx_select));
            holder.fx_select.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setSelected(false);
            holder.fx_name.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.fx_select.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDataList.get(position).selected) {
                    mDataList.get(position).selected = true;
                    for (int i = 0; i < mDataList.size(); ++i) {
                        if (i != position) {
                            mDataList.get(i).selected = false;
                        }
                    }
                }
                notifyDataSetChanged();

                if (mListener != null) {
                    mListener.fxSelected(position, mDataList.get(position));
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fx_name;
        public ImageView fx_image;
        public RoundImageView fx_select;

        public ViewHolder(View itemView) {
            super(itemView);
            fx_name = (TextView) itemView.findViewById(R.id.record_fx_name);
            fx_image = (ImageView) itemView.findViewById(R.id.record_fx_image);
            fx_select = (RoundImageView) itemView.findViewById(R.id.record_fx_select);
        }
    }

    public interface OnItemClickListener {
        void fxSelected(int pos, RecordFxListItem RecordFxListItem);
    }
}
