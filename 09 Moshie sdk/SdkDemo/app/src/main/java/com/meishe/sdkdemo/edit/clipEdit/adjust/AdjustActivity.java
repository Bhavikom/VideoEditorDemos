package com.meishe.sdkdemo.edit.clipEdit.adjust;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsPanAndScan;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.edit.clipEdit.SingleClipFragment;
import com.meishe.sdkdemo.edit.data.BackupData;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.dataInfo.ClipInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.util.ArrayList;

public class AdjustActivity extends BaseActivity {
    private final String TAG = "AdjustActivity";
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private RelativeLayout mHorizLayout;
    private RelativeLayout mVerticLayout;
    private RelativeLayout mRotateLayout;
    private RelativeLayout mResetLayout;
    private ImageView mAdjustFinish;
    private SingleClipFragment mClipFragment;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    NvsVideoFx mVideoFx;
    NvsVideoClip mVideoClip;
    ArrayList<ClipInfo> mClipArrayList;
    private int mCurClipIndex = 0;

    int mScaleX = 1;
    int mScaleY = 1;
    int mRotateAngle = 0;

    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_adjust;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout)findViewById(R.id.bottomLayout);
        mHorizLayout = (RelativeLayout)findViewById(R.id.horizLayout);
        mVerticLayout = (RelativeLayout)findViewById(R.id.verticLayout);
        mRotateLayout = (RelativeLayout)findViewById(R.id.rotateLayout);
        mResetLayout = (RelativeLayout)findViewById(R.id.resetLayout);
        mAdjustFinish = (ImageView)findViewById(R.id.adjustFinish);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.adjust);
        mTitleBar.setBackImageVisible(View.GONE);
    }

    @Override
    protected void initData() {
        mClipArrayList =  BackupData.instance().cloneClipInfoData();
        mCurClipIndex = BackupData.instance().getClipIndex();
        if(mCurClipIndex < 0 || mCurClipIndex >= mClipArrayList.size())
            return;
        ClipInfo clipInfo = mClipArrayList.get(mCurClipIndex);
        int scaleX = clipInfo.getScaleX();
        int scaleY = clipInfo.getScaleY();
        if(scaleX >= -1)
            mScaleX = scaleX;
        if (scaleY >= -1)
            mScaleY = scaleY;
        mRotateAngle = clipInfo.getRotateAngle();
        mTimeline = TimelineUtil.createSingleClipTimeline(clipInfo,true);
        if(mTimeline == null)
            return;

        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        if(videoTrack == null)
            return;
        mVideoClip = videoTrack.getClipByIndex(0);
        if(mVideoClip == null)
            return;
        initVideoFragment();
        adjustClip();
}

    @Override
    protected void initListener() {
        mHorizLayout.setOnClickListener(this);
        mVerticLayout.setOnClickListener(this);
        mRotateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rotateAngle = mVideoClip.getExtraVideoRotation();
                switch (rotateAngle){
                    case NvsVideoClip.ClIP_EXTRAVIDEOROTATION_0:
                        mRotateAngle = NvsVideoClip.ClIP_EXTRAVIDEOROTATION_90;
                        break;
                    case NvsVideoClip.ClIP_EXTRAVIDEOROTATION_90:
                        mRotateAngle = NvsVideoClip.ClIP_EXTRAVIDEOROTATION_180;
                        break;
                    case NvsVideoClip.ClIP_EXTRAVIDEOROTATION_180:
                        mRotateAngle = NvsVideoClip.ClIP_EXTRAVIDEOROTATION_270;
                        break;
                    case NvsVideoClip.ClIP_EXTRAVIDEOROTATION_270:
                        mRotateAngle = NvsVideoClip.ClIP_EXTRAVIDEOROTATION_0;
                        break;
                    default:
                        break;
                }
                rotateClip();
                mClipArrayList.get(mCurClipIndex).setRotateAngle(mRotateAngle);
                if(mClipFragment.getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
                }
            }
        });
        mClipFragment.setOnScaleGesture(new SingleClipFragment.OnScaleGesture() {
            @Override
            public void onHorizScale(float scale) {
                NvsPanAndScan panAndScan = mVideoClip.getPanAndScan();
                float scan = (scale - 1.0f) * 2 + panAndScan.scan;
                Logger.e(TAG,"length = " + scan);
                if(scan > 1.0f)
                    scan = 1.0f;
                if(scan < 0.0f)
                    scan = 0.0f;
                mVideoClip.setPanAndScan(panAndScan.pan,scan);
                seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
            }

            @Override
            public void onVerticalTrans(float tranVal) {
                NvsPanAndScan panAndScan = mVideoClip.getPanAndScan();
                Logger.e(TAG,"tranVal = " + tranVal);
                float pan = tranVal + panAndScan.pan;
                Logger.e(TAG,"pan = " + pan);
                if(pan > 1.0f)
                    pan = 1.0f;
                if(pan < -1.0f)
                    pan = -1.0f;
                mVideoClip.setPanAndScan(pan,panAndScan.scan);
                seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
            }
        });
        mResetLayout.setOnClickListener(this);
        mAdjustFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.horizLayout:  //水平
                mScaleX = mScaleX > 0 ? -1 : 1;
                mVideoFx.setFloatVal(Constants.FX_TRANSFORM_2D_SCALE_X,mScaleX);
                mClipArrayList.get(mCurClipIndex).setScaleX(mScaleX);
                break;
            case R.id.verticLayout: //垂直
                mScaleY = mScaleY > 0 ? -1 : 1;
                mVideoFx.setFloatVal(Constants.FX_TRANSFORM_2D_SCALE_Y,mScaleY);
                mClipArrayList.get(mCurClipIndex).setScaleY(mScaleY);
                break;
            case R.id.resetLayout://复位
                mScaleX = 1;
                mScaleY = 1;
                mRotateAngle = 0;
                rotateClip();
                mVideoClip.setPanAndScan(0.0f,0.0f);//
                mVideoFx.setFloatVal(Constants.FX_TRANSFORM_2D_SCALE_X,mScaleX);
                mVideoFx.setFloatVal(Constants.FX_TRANSFORM_2D_SCALE_Y,mScaleY);
                mClipArrayList.get(mCurClipIndex).setScaleX(mScaleX);
                mClipArrayList.get(mCurClipIndex).setScaleY(mScaleY);
                mClipArrayList.get(mCurClipIndex).setRotateAngle(mRotateAngle);
                break;
            case R.id.adjustFinish:
                NvsPanAndScan panAndScan = mVideoClip.getPanAndScan();
                mClipArrayList.get(mCurClipIndex).setPan(panAndScan.pan);
                mClipArrayList.get(mCurClipIndex).setScan(panAndScan.scan);
                BackupData.instance().setClipInfoData(mClipArrayList);
                removeTimeline();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                AppManager.getInstance().finishActivity();
                break;
            default:
                break;
        }
        if(mClipFragment.getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK)
            seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
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

    private void rotateClip(){
        mVideoClip.setExtraVideoRotation(mRotateAngle);
    }
    private void adjustClip(){
        rotateClip();
        int fxCount = mVideoClip.getFxCount();
        for(int index = 0;index < fxCount;++index){
            NvsVideoFx videoFx = mVideoClip.getFxByIndex(index);
            if(videoFx == null)
                continue;
            if(videoFx.getBuiltinVideoFxName().compareTo(Constants.FX_TRANSFORM_2D) == 0){
                mVideoFx = videoFx;
                break;
            }
        }
        if(mVideoFx == null)
            mVideoFx = mVideoClip.appendBuiltinFx(Constants.FX_TRANSFORM_2D);
        if(mVideoFx == null)
            return;
        if(mScaleX >= -1)
            mVideoFx.setFloatVal(Constants.FX_TRANSFORM_2D_SCALE_X,mScaleX);
        if(mScaleY >= -1)
            mVideoFx.setFloatVal(Constants.FX_TRANSFORM_2D_SCALE_Y,mScaleY);
    }
    private void initVideoFragment() {
        mClipFragment = new SingleClipFragment();
        mClipFragment.setFragmentLoadFinisedListener(new SingleClipFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
            }
        });
        mClipFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight",mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight",mBottomLayout.getLayoutParams().height);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        bundle.putBoolean("isAddOnTouchEvent", true);
        mClipFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.spaceLayout, mClipFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mClipFragment);
    }
    private void seekTimeline(long timeStamp){
        mClipFragment.seekTimeline(timeStamp,0);
    }
}
