package com.meishe.sdkdemo.douvideo;

import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BasePermissionActivity;
import com.meishe.sdkdemo.douvideo.bean.RecordClip;
import com.meishe.sdkdemo.douvideo.bean.RecordClipsInfo;
import com.meishe.sdkdemo.edit.clipEdit.SingleClipFragment;

import com.meishe.sdkdemo.edit.interfaces.OnTitleBarClickListener;
import com.meishe.sdkdemo.edit.timelineEditor.NvsTimelineEditor;
import com.meishe.sdkdemo.edit.timelineEditor.NvsTimelineTimeSpan;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.interfaces.TipsButtonClickListener;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.TimeFormatUtil;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.dataInfo.ClipInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DouVideoTrimActivity extends BasePermissionActivity {
    private static final String TAG = "DouVideoTrimActivity";
    private static final int VIDEOPLAYTOEOF = 1010;
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private TextView mTrimDurationVal;
    private ImageView mRotateClip;
    private NvsTimelineEditor mTimelineEditor;
    private ImageView mTrimFinish;
    private ImageView mVideoPlayButton;

    private long mTrimInPoint = 0;
    private long mTrimOutPoint = 0;
    private SingleClipFragment mClipFragment;
    NvsTimelineTimeSpan mDouyinTimeSpan;
    private NvsTimeline mTimeline;
    private ClipInfo mClipInfo = new ClipInfo();
    private NvsVideoClip mVideoClip;
    private String mVideoFilePath;
    private StringBuilder mTrimDurationText = new StringBuilder();
    private DouyinTrimHandler mTrimHandler = new DouyinTrimHandler(this);

    @Override
    protected List<String> initPermissions() {
        return Util.getAllPermissionsList();
    }

    @Override
    protected void hasPermission() {

    }

    @Override
    protected void nonePermission() {

    }

    @Override
    protected void noPromptPermission() {

    }

    static class DouyinTrimHandler extends Handler
    {
        WeakReference<DouVideoTrimActivity> mWeakReference;
        public DouyinTrimHandler(DouVideoTrimActivity activity)
        {
            mWeakReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg)
        {
            final DouVideoTrimActivity activity = mWeakReference.get();
            if(activity != null)
            {
                switch (msg.what) {
                    case VIDEOPLAYTOEOF:
                        activity.seekTimeline(activity.mTrimInPoint);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected int initRootView() {
        return R.layout.activity_dou_video_trim;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout)findViewById(R.id.bottomLayout);
        mTrimDurationVal = (TextView)findViewById(R.id.trimDurationVal);
        mRotateClip = (ImageView)findViewById(R.id.rotateClip);
        mTimelineEditor = (NvsTimelineEditor)findViewById(R.id.timelineEditor);
        mTrimFinish = (ImageView)findViewById(R.id.trimFinish);
        mVideoPlayButton = (ImageView)findViewById(R.id.videoPlayButton);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.trim);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            mVideoFilePath = bundle.getString("videoFilePath");
            mClipInfo.setFilePath(mVideoFilePath);
        }

        mTimeline = TimelineUtil.createSingleClipTimeline(mClipInfo,false);
        if(mTimeline == null)
            return;
        updateClipInfo();
        initVideoFragment();
        initDouyinSequence();
        loadVideoClipFailTips();
    }

    @Override
    protected void initListener() {
        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                removeTimeline();
            }

            @Override
            public void OnCenterTextClick() {

            }

            @Override
            public void OnRightTextClick() {

            }
        });
        mTrimFinish.setOnClickListener(this);
        if(mDouyinTimeSpan != null){
            mDouyinTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimInChangeListener() {
                @Override
                public void onChange(long timeStamp, boolean isDragEnd) {
                    mTrimInPoint = timeStamp;
                    long totalDuration = mTrimOutPoint - mTrimInPoint;
                    setTrimDurationText(totalDuration);
                    seekTimeline(timeStamp);
                    mClipFragment.setVideoTrimIn(timeStamp);
                }
            });
            mDouyinTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimOutChangeListener() {
                @Override
                public void onChange(long timeStamp, boolean isDragEnd) {
                    mTrimOutPoint = timeStamp;
                    long totalDuration = mTrimOutPoint - mTrimInPoint;
                    setTrimDurationText(totalDuration);
                    seekTimeline(timeStamp);
                    mClipFragment.setVideoTrimOut(timeStamp);
                }
            });
        }
        if(mClipFragment != null){
            mClipFragment.setVideoFragmentCallBack(new SingleClipFragment.VideoFragmentListener() {
                @Override
                public void playBackEOF(NvsTimeline timeline) {
                    mTrimHandler.sendEmptyMessage(VIDEOPLAYTOEOF);
                }

                @Override
                public void playStopped(NvsTimeline timeline) {

                }

                @Override
                public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {

                }

                @Override
                public void streamingEngineStateChanged(int state) {
                    if(state == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK){
                        mVideoPlayButton.setVisibility(View.GONE);
                    }else{
                        mVideoPlayButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        mVideoPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClipFragment.playVideo(mTrimInPoint,mTrimOutPoint);
            }
        });
        mRotateClip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.trimFinish:
                if(mClipFragment.getCurrentEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK){
                    mClipFragment.stopEngine();
                }
                mTrimFinish.setClickable(false);
                RecordClipsInfo recordClipsInfo = new RecordClipsInfo();
                RecordClip clip = new RecordClip(mVideoFilePath,mTrimInPoint,mTrimOutPoint,1.0f,0);
                clip.setCaptureVideo(false);
                clip.setRotateAngle(mVideoClip.getExtraVideoRotation());
                recordClipsInfo.addClip(clip);
                recordClipsInfo.setMusicInfo(null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("recordInfo", recordClipsInfo);
                AppManager.getInstance().jumpActivity(DouVideoTrimActivity.this, DouVideoEditActivity.class, bundle);
                break;
            case R.id.rotateClip:
                rotateClip();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!hasAllPermission()){
            AppManager.getInstance().finishActivity();//没有权限，关闭回到首页
        }else {
            mTrimFinish.setClickable(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mClipFragment != null)
                        mClipFragment.connectTimelineWithLiveWindow();
                }
            }, 100);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeTimeline();
        AppManager.getInstance().finishActivity();
    }

    private void loadVideoClipFailTips(){
        Logger.e(TAG,"duration = =>" + mTimeline.getDuration() + "mVideoFilePath == >" + mVideoFilePath);
        long duration = mTimeline.getDuration();
        if(duration > 0)
            return;

        String[] versionName = getResources().getStringArray(R.array.clip_load_failed_tips);
        Util.showDialog(DouVideoTrimActivity.this, versionName[0], versionName[1], new TipsButtonClickListener() {
            @Override
            public void onTipsButtoClick(View view) {
                Logger.e(TAG,"==》showDialog");
                removeTimeline();
                AppManager.getInstance().finishActivity();
            }
        });
    }
    private void removeTimeline(){
        TimelineUtil.removeTimeline(mTimeline);
        mTimeline = null;
        mTrimHandler.removeCallbacksAndMessages(null);
    }
    private void updateClipInfo(){
        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        if(videoTrack == null)
            return;
        mVideoClip = videoTrack.getClipByIndex(0);
        if(mVideoClip == null)
            return;
        long trimIn = mClipInfo.getTrimIn();
        if(trimIn < 0)
            mClipInfo.changeTrimIn(mVideoClip.getTrimIn());
        long trimOut = mClipInfo.getTrimOut();
        if(trimOut < 0)
            mClipInfo.changeTrimOut(mVideoClip.getTrimOut());
    }

    private void initDouyinSequence(){
        long duration = mTimeline.getDuration();
        ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> sequenceDescsArray = new ArrayList<>();
        NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc sequenceDescs = new NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
        sequenceDescs.mediaFilePath = mClipInfo.getFilePath();
        sequenceDescs.trimIn = 0;
        sequenceDescs.trimOut = duration;
        sequenceDescs.inPoint = 0;
        sequenceDescs.outPoint = duration;
        sequenceDescs.stillImageHint = false;
        sequenceDescsArray.add(sequenceDescs);
        int sequenceLeftPadding = ScreenUtils.dip2px(this,13);
        mTimelineEditor.setSequencLeftPadding(sequenceLeftPadding);
        mTimelineEditor.setSequencRightPadding(sequenceLeftPadding);
        mTimelineEditor.setTimeSpanLeftPadding(sequenceLeftPadding);
        mTimelineEditor.initTimelineEditor(sequenceDescsArray,duration);
        mTimelineEditor.setTimeSpanType("NvsTimelineTimeSpan");

        mTrimInPoint = mClipInfo.getTrimIn();
        mTrimOutPoint = mClipInfo.getTrimOut();
        long maxDuration = 15 * Constants.NS_TIME_BASE;
        if(mTrimOutPoint > maxDuration)
            mTrimOutPoint = maxDuration;
        mDouyinTimeSpan = mTimelineEditor.addDouyinTimeSpan(mTrimInPoint,mTrimOutPoint);
        setTrimDurationText(mTrimOutPoint - mTrimInPoint);
    }
    private void setTrimDurationText(long duration){
        mTrimDurationText.setLength(0);
        mTrimDurationText.append(getResources().getString(R.string.trim_duration));
        mTrimDurationText.append(" ");
        mTrimDurationText.append(TimeFormatUtil.formatUsToString1(duration));
        mTrimDurationVal.setText(mTrimDurationText.toString());
    }

    private void initVideoFragment() {
        mClipFragment = new SingleClipFragment();
        mClipFragment.setFragmentLoadFinisedListener(new SingleClipFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mVideoPlayButton.bringToFront();
                seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
            }
        });
        mClipFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight",mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight",mBottomLayout.getLayoutParams().height);
        bundle.putBoolean("playBarVisible",false);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        mClipFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.spaceLayout, mClipFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mClipFragment);
    }
    private void seekTimeline(long stamp){
        mClipFragment.seekTimeline(stamp,0);
    }
    private void rotateClip(){
        if(mVideoClip == null)
            return;
        int rotateAngle = mVideoClip.getExtraVideoRotation();
        switch (rotateAngle){
            case NvsVideoClip.ClIP_EXTRAVIDEOROTATION_0:
                mVideoClip.setExtraVideoRotation(NvsVideoClip.ClIP_EXTRAVIDEOROTATION_90);
                break;
            case NvsVideoClip.ClIP_EXTRAVIDEOROTATION_90:
                mVideoClip.setExtraVideoRotation(NvsVideoClip.ClIP_EXTRAVIDEOROTATION_180);
                break;case NvsVideoClip.ClIP_EXTRAVIDEOROTATION_180:
                mVideoClip.setExtraVideoRotation(NvsVideoClip.ClIP_EXTRAVIDEOROTATION_270);
                break;
            case NvsVideoClip.ClIP_EXTRAVIDEOROTATION_270:
                mVideoClip.setExtraVideoRotation(NvsVideoClip.ClIP_EXTRAVIDEOROTATION_0);
                break;
        }
        seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
    }
}
