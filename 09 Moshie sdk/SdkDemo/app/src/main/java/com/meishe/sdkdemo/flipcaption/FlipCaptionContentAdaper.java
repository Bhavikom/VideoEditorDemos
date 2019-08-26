package com.meishe.sdkdemo.flipcaption;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.edit.view.RoundColorView;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.ToastUtil;
import com.meishe.sdkdemo.utils.Util;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2018/5/25.
 */

public class FlipCaptionContentAdaper extends RecyclerView.Adapter<FlipCaptionContentAdaper.ViewHolder>  {
    private ArrayList<FlipCaptionDataInfo> mCaptionDataInfoList = new ArrayList<>();
    private Context mContext;
    private FlipCaptionContentListener mFlipCaptionListener = null;
    int mClickEditPos = -1;
    public interface FlipCaptionContentListener{
        void onCaptionModifyFinished(String newCaptionContent,int position);
        void onClickEditCaption(int position);
        void onIsSelectedCaption(int position);
    }
    public void setFlipCaptionListener(FlipCaptionContentListener flipCaptionListener) {
        mFlipCaptionListener = flipCaptionListener;
    }

    public FlipCaptionContentAdaper(Context context) {
        mContext = context;
    }

    public void setCaptionDataInfoList(ArrayList<FlipCaptionDataInfo> captionDataInfoList) {
        this.mCaptionDataInfoList = captionDataInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.flip_caption_content_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FlipCaptionDataInfo dataInfo = mCaptionDataInfoList.get(position);
        String captionText = dataInfo.getCaptionText();
        String newCaptionText = captionText.substring(captionText.indexOf("]") + 1);
        holder.mCaptionContent.setText(newCaptionText);
        holder.mCaptionContent.setTextColor(Color.parseColor(dataInfo.getCaptionColor()));
        boolean isSelectedItem = dataInfo.isSelectItem();//选中
        if(!isSelectedItem){
            holder.mEditCaptionButton.setVisibility(View.INVISIBLE);
            holder.mCaptionSelItem.setBackgroundResource(R.drawable.shape_flip_caption_circle);
        }else {
            holder.mEditCaptionButton.setVisibility(View.VISIBLE);
            holder.mCaptionSelItem.setBackgroundResource(R.drawable.shape_flip_caption_circle_selected);
        }

        if(isSelectedItem && mClickEditPos == position){
            holder.mCaptionContentRect.setVisibility(View.GONE);
            holder.mCaptionInputRect.setVisibility(View.VISIBLE);
            holder.mUserInputText.setText(holder.mCaptionContent.getText());
            showInputMethod(holder);
        }else {
            holder.mCaptionContentRect.setVisibility(View.VISIBLE);
            holder.mCaptionInputRect.setVisibility(View.GONE);
        }

        holder.mCaptionSelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = mCaptionDataInfoList.size();
                if(position < 0 || position >= count)
                    return;
                if(mFlipCaptionListener != null){
                    mFlipCaptionListener.onIsSelectedCaption(position);
                }
            }
        });

        holder.mCaptionContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mCaptionSelItem.callOnClick();
            }
        });

        holder.mCaptionUnSelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = mCaptionDataInfoList.size();
                if(position < 0 || position >= count)
                    return;
                mClickEditPos = -1;
                if(mFlipCaptionListener != null){
                    mFlipCaptionListener.onIsSelectedCaption(position);
                }
            }
        });
        holder.mEditCaptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = mCaptionDataInfoList.size();
                if(position < 0 || position >= count)
                    return;
                if(mFlipCaptionListener != null){
                    mFlipCaptionListener.onClickEditCaption(position);
                }
                if(mClickEditPos >= 0)
                    notifyItemChanged(mClickEditPos);
                mClickEditPos = position;
                notifyItemChanged(mClickEditPos);
            }
        });

        holder.mEditCaptionFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = mCaptionDataInfoList.size();
                if(position < 0 || position >= count)
                    return;
                mClickEditPos = -1;
                String inputText = holder.mUserInputText.getText().toString();
                //隐藏键盘
                InputMethodManager inputManager = (InputMethodManager)holder.mCaptionSelItem.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(holder.mCaptionSelItem.getWindowToken(), 0);
                if(mFlipCaptionListener != null){
                    mFlipCaptionListener.onCaptionModifyFinished(inputText,position);
                }
            }
        });

        //重新布局
        ViewGroup.LayoutParams contentLayoutParams = holder.mCaptionContent.getLayoutParams();
        contentLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        holder.mCaptionContent.setLayoutParams(contentLayoutParams);
        ViewGroup.LayoutParams inputLayoutParams = holder.mUserInputText.getLayoutParams();
        inputLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        holder.mUserInputText.setLayoutParams(inputLayoutParams);
    }

    private void showInputMethod(final ViewHolder holder){
        //弹出系统键盘
        holder.mUserInputText.postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.e("mUserInputText","mUserInputText");
                holder.mCaptionInputRect.requestFocus();//获取焦点
                holder.mUserInputText.setSelection(holder.mCaptionContent.getText().length());//将光标设置到最后
                InputMethodManager inputManager = (InputMethodManager)holder.mUserInputText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(holder.mUserInputText, InputMethodManager.SHOW_FORCED);
            }
        },100);
    }

    @Override
    public int getItemCount() {
        return mCaptionDataInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mCaptionSelItem;
        LinearLayout mCaptionContentRect;
        TextView mCaptionContent;
        ImageView mEditCaptionButton;
        View mCaptionUnSelItem;
        LinearLayout mCaptionInputRect;
        EditText mUserInputText;
        ImageView mEditCaptionFinish;
        public ViewHolder(View itemView) {
            super(itemView);
            mCaptionSelItem = itemView.findViewById(R.id.captionSelItem);
            mCaptionContentRect = (LinearLayout)itemView.findViewById(R.id.captionContentRect);
            mCaptionContent = (TextView)itemView.findViewById(R.id.captionContent);
            mEditCaptionButton = (ImageView)itemView.findViewById(R.id.editCaptionButton);
            mCaptionUnSelItem = itemView.findViewById(R.id.captionUnSelItem);
            mCaptionInputRect = (LinearLayout)itemView.findViewById(R.id.captionInputRect);
            mUserInputText = (EditText)itemView.findViewById(R.id.user_input);
            mEditCaptionFinish = (ImageView)itemView.findViewById(R.id.editCaptionFinish);
        }
    }
}
