package com.meishe.sdkdemo.edit.clipEdit.volume;

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
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.dataInfo.ClipInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.util.ArrayList;

import static com.meishe.sdkdemo.utils.Constants.VIDEOVOLUME_MAXSEEKBAR_VALUE;
import static com.meishe.sdkdemo.utils.Constants.VIDEOVOLUME_MAXVOLUMEVALUE;

public class VolumeActivity extends BaseActivity{
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private SeekBar mVolumeSeekBar;
    private TextView mVolumeSeekBarValue;
    private ImageView mVolumeFinish;
    private SingleClipFragment mClipFragment;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    ArrayList<ClipInfo> mClipArrayList;
    private int mCurClipIndex = 0;
    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_volume;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout)findViewById(R.id.bottomLayout);
        mVolumeSeekBar = (SeekBar)findViewById(R.id.volumeSeekBar);
        mVolumeSeekBar.setMax(VIDEOVOLUME_MAXSEEKBAR_VALUE);
        mVolumeSeekBarValue = (TextView)findViewById(R.id.volumeSeekBarValue);
        mVolumeFinish = (ImageView)findViewById(R.id.volumeFinish);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.volume);
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
        int value = (int)Math.floor(clipInfo.getVolume() * VIDEOVOLUME_MAXSEEKBAR_VALUE / VIDEOVOLUME_MAXVOLUMEVALUE + 0.5D);
        updateVolumeSeekBarValue(value);
    }

    @Override
    protected void initListener() {
        mVolumeFinish.setOnClickListener(this);
        mVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    updateClipVolume(progress);
                    updateVolumeSeekBarValue(progress);
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
            switch (id){
                case R.id.volumeFinish:
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

    private void updateVolumeSeekBarValue(int volumeValue){
        mVolumeSeekBar.setProgress(volumeValue);
        mVolumeSeekBarValue.setText(String.valueOf(volumeValue));
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
    private void updateClipVolume(int volume){
        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        if(videoTrack == null)
            return;
        NvsVideoClip videoClip = videoTrack.getClipByIndex(0);
        if(videoClip == null)
            return;
        float volumeGain = VIDEOVOLUME_MAXVOLUMEVALUE * volume / VIDEOVOLUME_MAXSEEKBAR_VALUE;
        videoClip.setVolumeGain(volumeGain,volumeGain);
        //存储数据
        mClipArrayList.get(mCurClipIndex).setVolume(volumeGain);
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
