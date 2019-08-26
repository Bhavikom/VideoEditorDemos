package com.meishe.sdkdemo.edit.clipEdit.photo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.edit.clipEdit.SingleClipFragment;
import com.meishe.sdkdemo.edit.data.BackupData;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.dataInfo.ClipInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.util.ArrayList;

public class DurationActivity extends BaseActivity {
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private SeekBar mDurationSeekBar;
    private TextView mDurationSeekBarValue;
    private ImageView mDurationFinish;
    private SingleClipFragment mClipFragment;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    ArrayList<ClipInfo> mClipArrayList;
    private int mCurClipIndex = 0;
    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_duration;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout)findViewById(R.id.bottomLayout);
        mDurationSeekBar = (SeekBar)findViewById(R.id.durationSeekBar);
        mDurationSeekBar.setMax(10);
        mDurationSeekBarValue = (TextView)findViewById(R.id.durationSeekBarValue);
        mDurationFinish = (ImageView)findViewById(R.id.durationFinish);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.duration);
        mTitleBar.setBackImageVisible(View.GONE);
    }

    @Override
    protected void initData() {
        mClipArrayList =  BackupData.instance().cloneClipInfoData();
        mCurClipIndex = BackupData.instance().getClipIndex();
        if(mCurClipIndex < 0 || mCurClipIndex >= mClipArrayList.size())
            return;
        ClipInfo clipInfo = mClipArrayList.get(mCurClipIndex);
        if(clipInfo == null)
            return;
        mTimeline = TimelineUtil.createSingleClipTimeline(clipInfo,true);
        if(mTimeline == null)
            return;
        initVideoFragment();
        long trimOut = mTimeline.getDuration();
        int progress = (int)(trimOut / Constants.NS_TIME_BASE);
        updateDurationSeekBar(progress);
    }

    @Override
    protected void initListener() {
        mDurationFinish.setOnClickListener(this);
        mDurationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    if(progress < 1)
                        progress = 1;
                    updateDurationSeekBar(progress);
                    changeClipTrimOut(progress);
                    mClipFragment.updateTotalDuration();
                    long trimOut = progress * Constants.NS_TIME_BASE;
                    mClipArrayList.get(mCurClipIndex).changeTrimOut(trimOut);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.durationFinish:
                BackupData.instance().setClipInfoData(mClipArrayList);
                removeTimeline();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                AppManager.getInstance().finishActivity();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        removeTimeline();
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void removeTimeline(){
        TimelineUtil.removeTimeline(mTimeline);
        mTimeline = null;
    }

    private void changeClipTrimOut(int progress){
        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        if(videoTrack == null)
            return;
        NvsVideoClip videoClip = videoTrack.getClipByIndex(0);
        if(videoClip == null)
            return;
        long trimOut = progress * Constants.NS_TIME_BASE;
        videoClip.changeTrimOutPoint(trimOut,true);
    }
    private void updateDurationSeekBar(int progress){
        mDurationSeekBar.setProgress(progress);
        mDurationSeekBarValue.setText(String.valueOf(progress));
    }

    private void initVideoFragment() {
        mClipFragment = new SingleClipFragment();
        mClipFragment.setFragmentLoadFinisedListener(new SingleClipFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mClipFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
            }
        });
        mClipFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight",mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight",mBottomLayout.getLayoutParams().height);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        mClipFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.spaceLayout, mClipFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mClipFragment);
    }
}
