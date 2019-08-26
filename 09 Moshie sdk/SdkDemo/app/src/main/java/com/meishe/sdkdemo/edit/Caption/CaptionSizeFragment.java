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
import android.widget.SeekBar;
import android.widget.TextView;

import com.meishe.sdkdemo.R;

public class CaptionSizeFragment extends Fragment {
    private SeekBar mCaptonSizeSeekBar;
    private TextView mSeekBarValue;
    private LinearLayout mApplyToAll;
    private ImageView mApplyToAllImage;
    private TextView mApplyToAllText;
    private boolean mIsApplyToAll = false;
    private OnCaptionSizeListener mCaptionSizeListener;
    public interface OnCaptionSizeListener{
        void onFragmentLoadFinished();
        void OnCaptionSize(int size);
        void onIsApplyToAll(boolean isApplyToAll);
    }
    public void setCaptionSizeListener(OnCaptionSizeListener captionSizeListener) {
        this.mCaptionSizeListener = captionSizeListener;
    }
    public void updateCaptionSizeValue(int progress){
        mSeekBarValue.setText(String.valueOf(progress));
        mCaptonSizeSeekBar.setProgress(progress);
    }

    public void applyToAllCaption(boolean isApplyToAll){
        mApplyToAllImage.setImageResource(isApplyToAll ? R.mipmap.applytoall : R.mipmap.unapplytoall);
        mApplyToAllText.setTextColor(isApplyToAll ? Color.parseColor("#ff4a90e2") : Color.parseColor("#ff909293"));
        mIsApplyToAll = isApplyToAll;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootParent = inflater.inflate(R.layout.caption_size_fragment, container, false);
        mCaptonSizeSeekBar = (SeekBar)rootParent.findViewById(R.id.captonSizeSeekBar);
        mCaptonSizeSeekBar.setMax(500);
        mSeekBarValue = (TextView) rootParent.findViewById(R.id.seekBarValue);
        mApplyToAll = (LinearLayout)rootParent.findViewById(R.id.applyToAll);
        mApplyToAllImage = (ImageView)rootParent.findViewById(R.id.applyToAllImage);
        mApplyToAllText = (TextView)rootParent.findViewById(R.id.applyToAllText);
        return rootParent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCaptionSizeSeekBar();
        if(mCaptionSizeListener != null){
            mCaptionSizeListener.onFragmentLoadFinished();
        }
    }

    private void initCaptionSizeSeekBar() {
        mCaptonSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    updateCaptionSizeValue(progress);
                    if(mCaptionSizeListener != null){
                        mCaptionSizeListener.OnCaptionSize(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mApplyToAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsApplyToAll = !mIsApplyToAll;
                applyToAllCaption(mIsApplyToAll);
                if(mCaptionSizeListener != null){
                    mCaptionSizeListener.onIsApplyToAll(mIsApplyToAll);
                }
            }
        });

    }
}
