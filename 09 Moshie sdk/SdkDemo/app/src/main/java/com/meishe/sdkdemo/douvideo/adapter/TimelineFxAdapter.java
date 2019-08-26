package com.meishe.sdkdemo.douvideo.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.douvideo.bean.TimelineFxResourceObj;
import java.util.List;

public class TimelineFxAdapter extends RecyclerView.Adapter<TimelineFxAdapter.ViewHolder> implements View.OnClickListener {
    private Context m_context;
    private List<TimelineFxResourceObj> m_data_list;
    private OnItemClickListener m_on_item_click_listener = null;

    public TimelineFxAdapter(Context context, List<TimelineFxResourceObj> list) {
        m_context= context;
        m_data_list = list;
    }

    public List<TimelineFxResourceObj> getData(){
        return m_data_list;
    }

    public void resetData(){
        for(int i = 0; i < m_data_list.size(); i++){
            TimelineFxResourceObj item = m_data_list.get(i);
            item.isShowWaitProgressBar = false;
            item.isSelected = false;
        }
    }

    @Override
    public TimelineFxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.douyin_time_line_fx_list_item, parent, false);
        v.setOnClickListener(this);
        TimelineFxAdapter.ViewHolder viewHolder = new TimelineFxAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position >= m_data_list.size())
            return;

        TimelineFxResourceObj dataItem = m_data_list.get(position);

        holder.mTextView.setText(m_data_list.get(position).imageName);
        holder.itemView.setTag(position);
        holder.mImageView.setImageResource(m_data_list.get(position).image);

        if (dataItem.isSelected) {
            GradientDrawable background = (GradientDrawable) holder.mMaskView.getBackground();
            background.setColor(ContextCompat.getColor(m_context, m_data_list.get(position).color));
            holder.mTextView.setTextColor(m_context.getResources().getColor(R.color.ff4a90e2));
        } else {
            GradientDrawable background = (GradientDrawable) holder.mMaskView.getBackground();
            background.setColor(ContextCompat.getColor(m_context, R.color.ms_gray));
            holder.mTextView.setTextColor(m_context.getResources().getColor(R.color.unsel_white));
        }

        if(dataItem.isShowWaitProgressBar){
            holder.mWaitProgressBar.setVisibility(View.VISIBLE);
            holder.mImageView.setVisibility(View.INVISIBLE);
        }else{
            holder.mWaitProgressBar.setVisibility(View.INVISIBLE);
            holder.mImageView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return m_data_list == null ? 0 : m_data_list.size();
    }

    @Override
    public void onClick(View view) {
        if (m_on_item_click_listener != null) {
            m_on_item_click_listener.onItemClick(view, (int) view.getTag());

        }
        notifyDataSetChanged();
    }

    public void setSelect(TimelineFxResourceObj item){
        for(int i = 0; i < m_data_list.size(); i++){
            TimelineFxResourceObj dataItem = m_data_list.get(i);
            if(item.equals(dataItem)){
                dataItem.isSelected = true;
            }else{
                dataItem.isSelected = false;
            }
        }
        notifyDataSetChanged();
    }

    public void showBusy(TimelineFxResourceObj item){
        for(int i = 0; i < m_data_list.size(); i++){
            TimelineFxResourceObj dataItem = m_data_list.get(i);
            if(item.equals(dataItem)){
                dataItem.isShowWaitProgressBar = true;
            }else{
                dataItem.isShowWaitProgressBar = false;
            }
        }
        notifyDataSetChanged();
    }

    public void hideBusy() {
        for (int i = 0; i < m_data_list.size(); i++) {
            TimelineFxResourceObj dataItem = m_data_list.get(i);
            dataItem.isShowWaitProgressBar = false;
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.m_on_item_click_listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        ProgressBar mWaitProgressBar;
        View mMaskView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
            mTextView = (TextView) itemView.findViewById(R.id.text_view);
            mWaitProgressBar = (ProgressBar) itemView.findViewById(R.id.wait_progress_bar);
            mMaskView = (View) itemView.findViewById(R.id.maskView);
        }
    }
}
