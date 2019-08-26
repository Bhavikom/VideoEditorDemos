package com.meishe.sdkdemo.edit.Caption;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meishe.sdkdemo.R;

public class CaptionPositionFragment extends Fragment {
    private ImageView mAlignLeft;
    private ImageView mAlignCenterHorizontal;
    private ImageView mAlignRight;
    private ImageView mAlignTop;
    private ImageView mAlignCenterVertical;
    private ImageView mAlignBottom;
    private LinearLayout mApplyToAll;
    private ImageView mApplyToAllImage;
    private TextView mApplyToAllText;
    private boolean mIsApplyToAll = false;
    private OnCaptionPositionListener mCaptionPositionListener;
    public interface OnCaptionPositionListener{
        void onFragmentLoadFinished();
        void OnAlignLeft();
        void OnAlignCenterHorizontal();
        void OnAlignRight();
        void OnAlignTop();
        void OnAlignCenterVertical();
        void OnAlignBottom();
        void onIsApplyToAll(boolean isApplyToAll);
    }
    public void setCaptionPostionListener(OnCaptionPositionListener captionPositionListener) {
        this.mCaptionPositionListener = captionPositionListener;
    }

    public void applyToAllCaption(boolean isApplyToAll){
        if(isApplyToAll){
            mApplyToAllImage.setImageResource(R.mipmap.applytoall);
            mApplyToAllText.setTextColor(Color.parseColor("#ff4a90e2"));
        }else {
            mApplyToAllImage.setImageResource(R.mipmap.unapplytoall);
            mApplyToAllText.setTextColor(Color.parseColor("#ff909293"));
        }
        mIsApplyToAll = isApplyToAll;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootParent = inflater.inflate(R.layout.caption_position_fragment, container, false);
        mAlignLeft = (ImageView) rootParent.findViewById(R.id.alignLeft);
        mAlignCenterHorizontal = (ImageView) rootParent.findViewById(R.id.alignCenterHorizontal);
        mAlignRight = (ImageView) rootParent.findViewById(R.id.alignRight);
        mAlignTop = (ImageView) rootParent.findViewById(R.id.alignTop);
        mAlignCenterVertical = (ImageView) rootParent.findViewById(R.id.alignCenterVertical);
        mAlignBottom = (ImageView) rootParent.findViewById(R.id.alignBottom);

        mApplyToAll = (LinearLayout)rootParent.findViewById(R.id.applyToAll);
        mApplyToAllImage = (ImageView)rootParent.findViewById(R.id.applyToAllImage);
        mApplyToAllText = (TextView)rootParent.findViewById(R.id.applyToAllText);
        return rootParent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAssetRecycleAdapter();

        if(mCaptionPositionListener != null){
            mCaptionPositionListener.onFragmentLoadFinished();
        }
    }

    private void initAssetRecycleAdapter() {
        mAlignLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCaptionPositionListener != null){
                    mCaptionPositionListener.OnAlignLeft();
                }
            }
        });
        mAlignCenterHorizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCaptionPositionListener != null){
                    mCaptionPositionListener.OnAlignCenterHorizontal();
                }
            }
        });
        mAlignRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCaptionPositionListener != null){
                    mCaptionPositionListener.OnAlignRight();
                }
            }
        });
        mAlignTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCaptionPositionListener != null){
                    mCaptionPositionListener.OnAlignTop();
                }
            }
        });
        mAlignCenterVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCaptionPositionListener != null){
                    mCaptionPositionListener.OnAlignCenterVertical();
                }
            }
        });
        mAlignBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCaptionPositionListener != null){
                    mCaptionPositionListener.OnAlignBottom();
                }
            }
        });
        mApplyToAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsApplyToAll = !mIsApplyToAll;
                applyToAllCaption(mIsApplyToAll);
                if(mCaptionPositionListener != null){
                    mCaptionPositionListener.onIsApplyToAll(mIsApplyToAll);
                }
            }
        });
    }
}
