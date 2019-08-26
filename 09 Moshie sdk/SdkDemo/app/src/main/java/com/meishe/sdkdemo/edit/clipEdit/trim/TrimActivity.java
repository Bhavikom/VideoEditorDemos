package com.meishe.sdkdemo.edit.clipEdit.trim;

import android.content.Intent;
import android.os.Bundle;
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
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.edit.clipEdit.SingleClipFragment;
import com.meishe.sdkdemo.edit.data.BackupData;
import com.meishe.sdkdemo.edit.timelineEditor.NvsTimelineEditor;
import com.meishe.sdkdemo.edit.timelineEditor.NvsTimelineTimeSpanExt;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.TimeFormatUtil;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.dataInfo.ClipInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.util.ArrayList;

public class TrimActivity extends BaseActivity {
    private static final String TAG = "NvsTimelineTimeSpanExt";
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private TextView mTrimDurationVal;
    private NvsTimelineEditor mTimelineEditor;
    private ImageView mTrimFinish;
    private long mTrimInPoint = 0;
    private long mTrimOutPoint = 0;
    private SingleClipFragment mClipFragment;
    NvsTimelineTimeSpanExt mTimlineTimeSpanExt;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    ArrayList<ClipInfo> mClipArrayList;
    private ClipInfo mClipInfo = new ClipInfo();
    private StringBuilder mTrimDurationText = new StringBuilder();
    /*
    * 值是EditActivity表示即从EditActivity页面进入裁剪页面;
    * 值是PictureInPictureActivity，即从画中画页面进入裁剪页面
    */
    private String mFromActivity = "EditActivity";
    private int mCurClipIndex = 0;

    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        mStreamingContext.stop();
        return R.layout.activity_trim;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout)findViewById(R.id.bottomLayout);
        mTrimDurationVal = (TextView)findViewById(R.id.trimDurationVal);
        mTimelineEditor = (NvsTimelineEditor)findViewById(R.id.timelineEditor);
        mTrimFinish = (ImageView)findViewById(R.id.trimFinish);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.trim);
        mTitleBar.setBackImageVisible(View.GONE);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            mFromActivity = bundle.getString("fromActivity","EditActivity");
        if(fromPicInPic()) {//画中画页面
            String filePath = bundle.getString("videoFilePath");
            long trimIn = bundle.getLong("trimInPoint");
            long trimOut = bundle.getLong("trimOutPoint");
            mClipInfo.setFilePath(filePath);
            mClipInfo.changeTrimIn(trimIn);
            mClipInfo.changeTrimOut(trimOut);
        } else {//片段编辑页面EditActivity
            mClipArrayList =  BackupData.instance().cloneClipInfoData();
            mCurClipIndex = BackupData.instance().getClipIndex();
            if(mCurClipIndex < 0 || mCurClipIndex >= mClipArrayList.size())
                return;
            mClipInfo = mClipArrayList.get(mCurClipIndex);
        }
        mTimeline = TimelineUtil.createSingleClipTimeline(mClipInfo,false);
        if(mTimeline == null)
            return;
        updateClipInfo();
        initVideoFragment();
        initMultiSequence();
    }

    @Override
    protected void initListener() {
        mTrimFinish.setOnClickListener(this);
        if(mTimlineTimeSpanExt != null){
            mTimlineTimeSpanExt.setOnChangeListener(new NvsTimelineTimeSpanExt.OnTrimInChangeListener() {
                @Override
                public void onChange(long timeStamp, boolean isDragEnd) {
                    mTrimInPoint = timeStamp;
                    long totalDuration = mTrimOutPoint - mTrimInPoint;
                    setTrimDurationText(totalDuration);
                    if(!fromPicInPic()){
                        float speed = mClipInfo.getSpeed();
                        speed = speed <= 0 ? 1.0f : speed;
                        long newTrimIn = (long)(timeStamp * speed);
                        mClipArrayList.get(mCurClipIndex).changeTrimIn(newTrimIn);
                    }
                    mClipFragment.updateCurPlayTime(timeStamp);
                    mClipFragment.setVideoTrimIn(timeStamp);
                    seekTimeline(timeStamp);
                }
            });
            mTimlineTimeSpanExt.setOnChangeListener(new NvsTimelineTimeSpanExt.OnTrimOutChangeListener() {
                @Override
                public void onChange(long timeStamp, boolean isDragEnd) {
                    mTrimOutPoint = timeStamp;
                    long totalDuration = mTrimOutPoint - mTrimInPoint;
                    setTrimDurationText(totalDuration);
                    if(!fromPicInPic()){
                        float speed = mClipInfo.getSpeed();
                        speed = speed <= 0 ? 1.0f : speed;
                        long newTrimOut = (long)(timeStamp * speed);
                        mClipArrayList.get(mCurClipIndex).changeTrimOut(newTrimOut);
                    }
                    mClipFragment.setVideoTrimOut(timeStamp);
                    seekTimeline(timeStamp);
                }
            });
        }

        mClipFragment.setVideoFragmentCallBack(new SingleClipFragment.VideoFragmentListener() {
            @Override
            public void playBackEOF(NvsTimeline timeline) {}

            @Override
            public void playStopped(NvsTimeline timeline) {}

            @Override
            public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {}

            @Override
            public void streamingEngineStateChanged(int state) {}
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.trimFinish:
                if(!fromPicInPic()) {
                    BackupData.instance().setClipInfoData(mClipArrayList);
                }
                removeTimeline();
                setResultIntent();
                AppManager.getInstance().finishActivity();
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        removeTimeline();
        if(fromPicInPic()){
            setResultIntent();
        }
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void removeTimeline(){
        mClipFragment.stopEngine();
        TimelineUtil.removeTimeline(mTimeline);
        mTimeline = null;
    }
    private void updateClipInfo(){
        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        if(videoTrack == null)
            return;
        NvsVideoClip videoClip = videoTrack.getClipByIndex(0);
        if(videoClip == null)
            return;
        long trimIn = mClipInfo.getTrimIn();
        if(trimIn < 0)
            mClipInfo.changeTrimIn(videoClip.getTrimIn());
        long trimOut = mClipInfo.getTrimOut();
        if(trimOut < 0)
            mClipInfo.changeTrimOut(videoClip.getTrimOut());
    }

    private void initMultiSequence(){
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
        double pixelPerMicrosecond = getPixelMicrosecond(duration);
        mTimelineEditor.setPixelPerMicrosecond(pixelPerMicrosecond);
        int sequenceLeftPadding = ScreenUtils.dip2px(this,13);
        mTimelineEditor.setSequencLeftPadding(sequenceLeftPadding);
        mTimelineEditor.setSequencRightPadding(sequenceLeftPadding);
        mTimelineEditor.setTimeSpanLeftPadding(sequenceLeftPadding);
        mTimelineEditor.initTimelineEditor(sequenceDescsArray,duration);
        mTimelineEditor.getMultiThumbnailSequenceView().getLayoutParams().height = ScreenUtils.dip2px(this,64);
        //warning: 使用addTimeSpanExt之前必须设置setTimeSpanType()
        mTimelineEditor.setTimeSpanType("NvsTimelineTimeSpanExt");
        float speed = mClipInfo.getSpeed();
        speed = speed <= 0 ? 1.0f : speed;
        mTrimInPoint = (long)(mClipInfo.getTrimIn() / speed);
        mTrimOutPoint = (long)(mClipInfo.getTrimOut() / speed);
        mTimlineTimeSpanExt = mTimelineEditor.addTimeSpanExt(mTrimInPoint,mTrimOutPoint);
        setTrimDurationText(mTrimOutPoint - mTrimInPoint);
    }
    private void setTrimDurationText(long duration){
        mTrimDurationText.setLength(0);
        mTrimDurationText.append(getResources().getString(R.string.trim_duration));
        mTrimDurationText.append(" ");
        mTrimDurationText.append(TimeFormatUtil.formatUsToString1(duration));
        mTrimDurationVal.setText(mTrimDurationText.toString());
    }

    private double getPixelMicrosecond(long duration){
        int width = ScreenUtils.getScreenWidth(TrimActivity.this);
        int leftPadding = ScreenUtils.dip2px(this,13);
        int sequenceWidth = width - 2 * leftPadding;
        double pixelMicrosecond = sequenceWidth / (double)duration;
        return pixelMicrosecond;
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
        mClipFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.spaceLayout, mClipFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mClipFragment);
    }
    private void seekTimeline(long stamp){
        mClipFragment.seekTimeline(stamp,0);
    }
    private void setResultIntent(){
        Intent intent = new Intent();
        if(fromPicInPic()){
            intent.putExtra("trimInPoint",mTrimInPoint);
            intent.putExtra("trimOutPoint",mTrimOutPoint);
        }
        setResult(RESULT_OK, intent);
    }
    private boolean fromPicInPic(){
        String picInPic = "PictureInPictureActivity";
        return mFromActivity.equals(picInPic);
    }
}
