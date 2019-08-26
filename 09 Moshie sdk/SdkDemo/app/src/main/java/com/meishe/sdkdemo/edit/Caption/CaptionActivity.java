package com.meishe.sdkdemo.edit.Caption;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineCaption;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.edit.VideoFragment;
import com.meishe.sdkdemo.edit.data.BackupData;
import com.meishe.sdkdemo.edit.timelineEditor.NvsTimelineEditor;
import com.meishe.sdkdemo.edit.timelineEditor.NvsTimelineTimeSpan;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.edit.view.InputDialog;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.TimeFormatUtil;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.dataInfo.CaptionInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CaptionActivity extends BaseActivity {
    private static final String TAG = "CaptionActivity";
    private static final int VIDEOPLAYTOEOF = 105;
    private static final int REQUESTCAPTIONSTYLE = 103;
    private CustomTitleBar mTitleBar;
    private TextView mPlayCurTime;
    private RelativeLayout mZoomInBtn;
    private RelativeLayout mZoomOutBtn;
    private Button mCaptionStyleButton;
    private NvsTimelineEditor mTimelineEditor;
    private Button mPlayBtn;
    private Button mAddCaptionBtn;
    private Button mOkBtn;
    private VideoFragment mVideoFragment;
    private RelativeLayout mBottomRelativeLayout;
    private RelativeLayout mPlayBtnLayout;
    private NvsMultiThumbnailSequenceView mMultiSequenceView;

    private NvsTimeline mTimeline;
    private boolean mIsSeekTimeline = true;
    private NvsTimelineCaption mCurCaption;
    private NvsStreamingContext mStreamingContext;
    private List<CaptionTimeSpanInfo> mTimeSpanInfoList = new ArrayList<>();
    private CaptionActivity.CaptionHandler m_handler = new CaptionActivity.CaptionHandler(this);
    private ArrayList<CaptionInfo> mCaptionDataListClone;
    private boolean mIsInnerDrawRect = false;
    private StringBuilder mShowCurrentDuration = new StringBuilder();

    static class CaptionHandler extends Handler {
        WeakReference<CaptionActivity> mWeakReference;

        public CaptionHandler(CaptionActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CaptionActivity activity = mWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case VIDEOPLAYTOEOF:
                        activity.resetView();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void resetView() {
        updatePlaytimeText(0);
        seekTimeline(0);
        mMultiSequenceView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
    }

    private void seekTimeline(long timeStamp) {
        mVideoFragment.seekTimeline(timeStamp, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
    }

    private void selectCaptionAndTimeSpan() {
        selectCaption();
        mVideoFragment.setCurCaption(mCurCaption);
        mVideoFragment.updateCaptionCoordinate(mCurCaption);
        mVideoFragment.changeCaptionRectVisible();
        if (mCurCaption != null) {
            int alignVal = mCurCaption.getTextAlignment();
            mVideoFragment.setAlignIndex(alignVal);
        }
        if (mCurCaption != null) {
            selectTimeSpan();
        } else {
            mTimelineEditor.unSelectAllTimeSpan();
        }
    }

    @Override
    protected int initRootView() {
        return R.layout.activity_caption;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mPlayCurTime = (TextView) findViewById(R.id.play_cur_time);
        mZoomInBtn = (RelativeLayout) findViewById(R.id.zoom_in_btn);
        mZoomOutBtn = (RelativeLayout) findViewById(R.id.zoom_out_btn);
        mCaptionStyleButton = (Button) findViewById(R.id.captionStyleButton);
        mTimelineEditor = (NvsTimelineEditor) findViewById(R.id.caption_timeline_editor);
        mPlayBtn = (Button) findViewById(R.id.play_btn);
        mAddCaptionBtn = (Button) findViewById(R.id.add_caption_btn);
        mOkBtn = (Button) findViewById(R.id.ok_btn);
        mBottomRelativeLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mPlayBtnLayout = (RelativeLayout) findViewById(R.id.play_btn_layout);
        mMultiSequenceView = mTimelineEditor.getMultiThumbnailSequenceView();
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.caption);
        mTitleBar.setBackImageVisible(View.GONE);
    }

    @Override
    protected void initData() {
        mStreamingContext = NvsStreamingContext.getInstance();
        mTimeline = TimelineUtil.createTimeline();
        if (mTimeline == null)
            return;
        mCaptionDataListClone = TimelineData.instance().cloneCaptionData();
        initVideoFragment();
        updatePlaytimeText(0);
        initMultiSequence();
        addAllTimeSpan();
        selectCaption();
        selectTimeSpan();
    }

    @Override
    protected void initListener() {
        mZoomInBtn.setOnClickListener(this);
        mZoomOutBtn.setOnClickListener(this);
        mCaptionStyleButton.setOnClickListener(this);
        mAddCaptionBtn.setOnClickListener(this);
        mOkBtn.setOnClickListener(this);
        mPlayBtn.setOnClickListener(this);

        mTimelineEditor.setOnScrollListener(new NvsTimelineEditor.OnScrollChangeListener() {
            @Override
            public void onScrollX(long timeStamp) {
                if (!mIsSeekTimeline)
                    return;
                if (mTimeline != null) {
                    updatePlaytimeText(timeStamp);
                    selectCaptionAndTimeSpan();
                    seekTimeline(timeStamp);
                }
            }
        });


        mMultiSequenceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mIsSeekTimeline = true;
                return false;
            }
        });

        if (mVideoFragment != null) {
            mVideoFragment.setVideoFragmentCallBack(new VideoFragment.VideoFragmentListener() {
                @Override
                public void playBackEOF(NvsTimeline timeline) {
                    m_handler.sendEmptyMessage(VIDEOPLAYTOEOF);
                }

                @Override
                public void playStopped(NvsTimeline timeline) {
                    selectCaptionAndTimeSpan();
                }

                @Override
                public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {
                    updatePlaytimeText(stamp);
                    mVideoFragment.setDrawRectVisible(View.GONE);
                    mTimelineEditor.unSelectAllTimeSpan();
                    selectCaption();
                    if (mMultiSequenceView != null) {
                        int x = Math.round((stamp / (float) mTimeline.getDuration() * mTimelineEditor.getSequenceWidth()));
                        mMultiSequenceView.smoothScrollTo(x, 0);
                    }
                }

                @Override
                public void streamingEngineStateChanged(int state) {
                    if (NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK == state) {
                        mIsSeekTimeline = false;
                        mPlayBtn.setBackgroundResource(R.mipmap.icon_edit_pause);
                    } else {
                        mPlayBtn.setBackgroundResource(R.mipmap.icon_edit_play);
                        mIsSeekTimeline = true;
                    }
                }
            });
            mVideoFragment.setAssetEditListener(new VideoFragment.AssetEditListener() {
                @Override
                public void onAssetDelete() {
                    deleteCurCaptionTimeSpan();
                    int zVal = (int) mCurCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if (index >= 0) {
                        mCaptionDataListClone.remove(index);
                    }
                    mTimeline.removeCaption(mCurCaption);
                    mCurCaption = null;
                    selectCaptionAndTimeSpan();
                    seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
                }

                @Override
                public void onAssetSelected(PointF curPoint) {
                    //判断若没有选中当前字幕框则选中，选中则不处理
                    mIsInnerDrawRect = mVideoFragment.curPointIsInnerDrawRect((int) curPoint.x, (int) curPoint.y);
                    if (!mIsInnerDrawRect) {
                        mVideoFragment.selectCaptionByHandClick(curPoint);
                        mCurCaption = mVideoFragment.getCurCaption();
                        selectTimeSpan();
                        if (mCurCaption != null) {
                            int alignVal = mCurCaption.getTextAlignment();
                            mVideoFragment.setAlignIndex(alignVal);
                        }
                    }
                }

                @Override
                public void onAssetTranstion() {
                    if (mCurCaption == null)
                        return;
                    PointF pointF = mCurCaption.getCaptionTranslation();
                    //Log.e(TAG,"pointF.x = " + pointF.x + "pointF.y =" + pointF.y);
                    int zVal = (int) mCurCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if (index >= 0) {
                        mCaptionDataListClone.get(index).setUsedTranslationFlag(CaptionInfo.ATTRIBUTE_USED_FLAG);
                        mCaptionDataListClone.get(index).setTranslation(pointF);
                    }
                }

                @Override
                public void onAssetScale() {
                    if (mCurCaption == null)
                        return;
                    int zVal = (int) mCurCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if (index >= 0) {
                        mCaptionDataListClone.get(index).setUsedScaleRotationFlag(CaptionInfo.ATTRIBUTE_USED_FLAG);
                        mCaptionDataListClone.get(index).setScaleFactorX(mCurCaption.getScaleX());
                        mCaptionDataListClone.get(index).setScaleFactorY(mCurCaption.getScaleY());
                        mCaptionDataListClone.get(index).setAnchor(mCurCaption.getAnchorPoint());
                        mCaptionDataListClone.get(index).setRotation(mCurCaption.getRotationZ());
                        mCaptionDataListClone.get(index).setCaptionSize(mCurCaption.getFontSize());
                        PointF pointF = mCurCaption.getCaptionTranslation();
                        //Log.e(TAG,"pointF.x = " + pointF.x + "pointF.y =" + pointF.y);
                        mCaptionDataListClone.get(index).setTranslation(pointF);
                    }
                }

                @Override
                public void onAssetAlign(int alignVal) {
                    int zVal = (int) mCurCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if (index >= 0)
                        mCaptionDataListClone.get(index).setAlignVal(alignVal);
                }

                @Override
                public void onAssetHorizFlip(boolean isHorizFlip) {

                }
            });
            mVideoFragment.setCaptionTextEditListener(new VideoFragment.VideoCaptionTextEditListener() {
                @Override
                public void onCaptionTextEdit() {
                    if (!mIsInnerDrawRect)
                        return;
                    InputDialog inputDialog = new InputDialog(CaptionActivity.this, R.style.dialog, new InputDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean ok) {
                            if (ok) {
                                InputDialog d = (InputDialog) dialog;
                                String userInputText = d.getUserInputText();
                                mCurCaption.setText(userInputText);
                                seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
                                mVideoFragment.updateCaptionCoordinate(mCurCaption);
                                mVideoFragment.changeCaptionRectVisible();
                                int zVal = (int) mCurCaption.getZValue();
                                int index = getCaptionIndex(zVal);
                                if (index >= 0) {
                                    mCaptionDataListClone.get(index).setText(userInputText);
                                }

                            }
                        }
                    });
                    if(mCurCaption != null) {
                        inputDialog.setUserInputText(mCurCaption.getText());
                    }
                    inputDialog.show();
                    mIsInnerDrawRect = false;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zoom_in_btn:
                mIsSeekTimeline = false;
                mTimelineEditor.ZoomInSequence();
                break;

            case R.id.zoom_out_btn:
                mIsSeekTimeline = false;
                mTimelineEditor.ZoomOutSequence();
                break;
            case R.id.captionStyleButton:
                mCaptionStyleButton.setClickable(false);
                BackupData.instance().setCaptionData(mCaptionDataListClone);
                BackupData.instance().setCaptionZVal((int) mCurCaption.getZValue());
                BackupData.instance().setCurSeekTimelinePos(mStreamingContext.getTimelineCurrentPosition(mTimeline));
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), CaptionStyleActivity.class, null, REQUESTCAPTIONSTYLE);
                break;
            case R.id.add_caption_btn: {
                new InputDialog(this, R.style.dialog, new InputDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean ok) {
                        if (ok) {
                            InputDialog d = (InputDialog) dialog;
                            String userInputText = d.getUserInputText();
                            addCaption(userInputText);
                        }
                    }
                }).show();

                break;
            }
            case R.id.ok_btn:
                mStreamingContext.stop();
                removeTimeline();
                TimelineData.instance().setCaptionData(mCaptionDataListClone);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                AppManager.getInstance().finishActivity();
                break;

            case R.id.play_btn:
                playVideo();
                break;
        }
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case REQUESTCAPTIONSTYLE:
                mCaptionDataListClone = BackupData.instance().getCaptionData();
                mCurCaption = null;
                TimelineUtil.setCaption(mTimeline, mCaptionDataListClone);
                mTimelineEditor.deleteAllTimeSpan();
                mTimeSpanInfoList.clear();
                addAllTimeSpan();
                long curSeekPos = BackupData.instance().getCurSeekTimelinePos();
                seekTimeline(curSeekPos);
                boolean isSelectCurCaption = data.getBooleanExtra("isSelectCurCaption", true);
                if (!isSelectCurCaption) {
                    selectCaptionAndTimeSpan();
                } else {
                    int curZVal = BackupData.instance().getCaptionZVal();
                    selectCatpionByZVal(curZVal);
                    mVideoFragment.setCurCaption(mCurCaption);
                    mVideoFragment.updateCaptionCoordinate(mCurCaption);
                    mVideoFragment.changeCaptionRectVisible();
                    if (mCurCaption != null) {
                        int alignVal = mCurCaption.getTextAlignment();
                        mVideoFragment.setAlignIndex(alignVal);
                    }
                    selectTimeSpan();
                }
                break;
            default:
                break;
        }
    }

    private void selectCatpionByZVal(int curZVal) {
        if (mTimeline != null) {
            long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
            List<NvsTimelineCaption> captionList = mTimeline.getCaptionsByTimelinePosition(curPos);
            int captionCount = captionList.size();
            if (captionCount > 0) {
                for (int i = 0; i < captionCount; i++) {
                    int zVal = (int) captionList.get(i).getZValue();
                    if (curZVal == zVal) {
                        mCurCaption = captionList.get(i);
                        break;
                    }
                }
                if (mCurCaption != null) {
                    mCaptionStyleButton.setVisibility(View.VISIBLE);
                }
            } else {
                mCurCaption = null;
                mCaptionStyleButton.setVisibility(View.GONE);
            }
        }
    }

    private void playVideo() {
        if (mVideoFragment.getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
            long startTime = mStreamingContext.getTimelineCurrentPosition(mTimeline);
            long endTime = mTimeline.getDuration();
            mVideoFragment.playVideo(startTime, endTime);
        } else {
            mVideoFragment.stopEngine();
        }
    }


    private void updatePlaytimeText(long playTime) {
        if (mTimeline != null) {
            long totalDuaration = mTimeline.getDuration();
            String strTotalDuration = TimeFormatUtil.formatUsToString1(totalDuaration);
            String strCurrentDuration = TimeFormatUtil.formatUsToString1(playTime);
            mShowCurrentDuration.setLength(0);
            mShowCurrentDuration.append(strCurrentDuration);
            mShowCurrentDuration.append("/");
            mShowCurrentDuration.append(strTotalDuration);
            mPlayCurTime.setText(mShowCurrentDuration.toString());
        }
    }

    private float getCurCaptionZVal() {
        float zVal = 0.0f;
        NvsTimelineCaption caption = mTimeline.getFirstCaption();
        while (caption != null) {
            float tmpZVal = caption.getZValue();
            if (tmpZVal > zVal)
                zVal = tmpZVal;
            caption = mTimeline.getNextCaption(caption);
        }
        zVal += 1.0;
        return zVal;
    }

    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mVideoFragment.setCurCaption(mCurCaption);
                mOkBtn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVideoFragment.updateCaptionCoordinate(mCurCaption);
                        mVideoFragment.changeCaptionRectVisible();
                        seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
                    }
                }, 100);
            }
        });
        //设置字幕模式
        mVideoFragment.setEditMode(Constants.EDIT_MODE_CAPTION);
        mVideoFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight", mBottomRelativeLayout.getLayoutParams().height);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        bundle.putBoolean("playBarVisible", false);
        mVideoFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.video_layout, mVideoFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mVideoFragment);
    }


    private void initMultiSequence() {
        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        if (videoTrack == null)
            return;
        int clipCount = videoTrack.getClipCount();
        ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> sequenceDescsArray = new ArrayList<>();
        for (int index = 0; index < clipCount; ++index) {
            NvsVideoClip videoClip = videoTrack.getClipByIndex(index);
            if (videoClip == null)
                continue;

            NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc sequenceDescs = new NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
            sequenceDescs.mediaFilePath = videoClip.getFilePath();
            sequenceDescs.trimIn = videoClip.getTrimIn();
            sequenceDescs.trimOut = videoClip.getTrimOut();
            sequenceDescs.inPoint = videoClip.getInPoint();
            sequenceDescs.outPoint = videoClip.getOutPoint();
            sequenceDescs.stillImageHint = false;
            sequenceDescsArray.add(sequenceDescs);
        }

        long duration = mTimeline.getDuration();
        int halfScreenWidth = ScreenUtils.getScreenWidth(this) / 2;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mPlayBtnLayout.getLayoutParams();
        int playBtnTotalWidth = layoutParams.width + layoutParams.leftMargin + layoutParams.rightMargin;
        int sequenceLeftPadding = halfScreenWidth - playBtnTotalWidth;
        mTimelineEditor.setSequencLeftPadding(sequenceLeftPadding);
        mTimelineEditor.setSequencRightPadding(halfScreenWidth);
        mTimelineEditor.setTimeSpanLeftPadding(sequenceLeftPadding);
        mTimelineEditor.initTimelineEditor(sequenceDescsArray, duration);
    }

    //添加字幕
    private void addCaption(String caption) {
        long inPoint = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        long captionDuration = 4 * Constants.NS_TIME_BASE;
        long outPoint = inPoint + captionDuration;
        long duration = mTimeline.getDuration();

        if (outPoint > duration) {
            captionDuration = duration - inPoint;
            if (captionDuration <= Constants.NS_TIME_BASE) {
                captionDuration = Constants.NS_TIME_BASE;
                inPoint = duration - captionDuration;
                if (duration <= Constants.NS_TIME_BASE) {
                    captionDuration = duration;
                    inPoint = 0;
                }
            }
            outPoint = duration;
        }

        mCurCaption = mTimeline.addCaption(caption, inPoint, captionDuration, null);
        if (mCurCaption == null) {
            Log.e(TAG, "addCaption: " + " 添加字幕失败！");
            return;
        }
        float zVal = getCurCaptionZVal();
        mCurCaption.setZValue(zVal);
        NvsTimelineTimeSpan timeSpan = addTimeSpan(inPoint, outPoint);
        if (timeSpan == null) {
            Log.e(TAG, "addCaption: " + " 添加TimeSpan失败!");
            return;
        }
        mTimeSpanInfoList.add(new CaptionTimeSpanInfo(mCurCaption, timeSpan));
        mCaptionStyleButton.setVisibility(View.VISIBLE);
        mVideoFragment.setCurCaption(mCurCaption);
        mVideoFragment.updateCaptionCoordinate(mCurCaption);
        int alignVal = mCurCaption.getTextAlignment();
        mVideoFragment.setAlignIndex(alignVal);
        mVideoFragment.changeCaptionRectVisible();
        seekTimeline(inPoint);
        selectTimeSpan();//选择timeSpan
        CaptionInfo captionInfo = Util.saveCaptionData(mCurCaption);
        if (captionInfo != null)
            mCaptionDataListClone.add(captionInfo);
    }

    private NvsTimelineTimeSpan addTimeSpan(long inPoint, long outPoint) {
        //warning: 使用addTimeSpanExt之前必须设置setTimeSpanType()
        mTimelineEditor.setTimeSpanType("NvsTimelineTimeSpan");
        NvsTimelineTimeSpan timelineTimeSpan = mTimelineEditor.addTimeSpan(inPoint, outPoint);
        if (timelineTimeSpan == null) {
            Log.e(TAG, "addTimeSpan: " + " 添加TimeSpan失败!");
            return null;
        }
        timelineTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimInChangeListener() {
            @Override
            public void onChange(long timeStamp, boolean isDragEnd) {
                seekTimeline(timeStamp);
                updatePlaytimeText(timeStamp);
                mVideoFragment.changeCaptionRectVisible();
                if (isDragEnd && mCurCaption != null) {
                    Logger.e(TAG, "TrimInChange1212->" + timeStamp);
                    mCurCaption.changeInPoint(timeStamp);
                    int zVal = (int) mCurCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if (index >= 0)
                        mCaptionDataListClone.get(index).setInPoint(timeStamp);

                    seekMultiThumbnailSequenceView();
                }
            }
        });
        timelineTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimOutChangeListener() {
            @Override
            public void onChange(long timeStamp, boolean isDragEnd) {
                //outPoint是开区间，seekTimeline时，需要往前平移一帧即0.04秒，转换成微秒即40000微秒
                seekTimeline(timeStamp - 40000);
                updatePlaytimeText(timeStamp);
                mVideoFragment.changeCaptionRectVisible();
                if (isDragEnd && mCurCaption != null) {
                    Logger.e(TAG, "TrimInChange5454->" + timeStamp);
                    mCurCaption.changeOutPoint(timeStamp);
                    int zVal = (int) mCurCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if (index >= 0) {
                        mCaptionDataListClone.get(index).setOutPoint(timeStamp);
                    }

                    seekMultiThumbnailSequenceView();
                }
            }
        });

        return timelineTimeSpan;
    }

    private void seekMultiThumbnailSequenceView() {
        if (mMultiSequenceView != null) {
            long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
            long duration = mTimeline.getDuration();
            mMultiSequenceView.scrollTo(Math.round(((float) curPos) / (float) duration * mTimelineEditor.getSequenceWidth()), 0);
        }
    }

    private void addAllTimeSpan() {
        NvsTimelineCaption caption = mTimeline.getFirstCaption();
        while (caption != null) {
            int capCategory = caption.getCategory();
            int roleTheme = caption.getRoleInTheme();
            Logger.e(TAG, "capCategoryCp = " + capCategory);
            // capCategory值为0是默认字幕即未使用字幕样式的字幕，
            // 值为1表示是用户自定义种类即使用字幕样式的字幕，值为2是主题字幕
            if (capCategory == NvsTimelineCaption.THEME_CATEGORY
                    && roleTheme != NvsTimelineCaption.ROLE_IN_THEME_GENERAL) {//主题字幕不作编辑处理
                caption = mTimeline.getNextCaption(caption);
                continue;
            }
            long inPoint = caption.getInPoint();
            long outPoint = caption.getOutPoint();
            NvsTimelineTimeSpan timeSpan = addTimeSpan(inPoint, outPoint);
            if (timeSpan != null) {
                CaptionTimeSpanInfo timeSpanInfo = new CaptionTimeSpanInfo(caption, timeSpan);
                mTimeSpanInfoList.add(timeSpanInfo);
            }
            caption = mTimeline.getNextCaption(caption);
        }
    }

    private void selectCaption() {
        long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        List<NvsTimelineCaption> captionList = mTimeline.getCaptionsByTimelinePosition(curPos);
        Logger.e(TAG, "captionList => " + captionList.size());
        int captionCount = captionList.size();
        if (captionCount > 0) {
            float zVal = captionList.get(0).getZValue();
            int index = 0;
            for (int i = 0; i < captionCount; i++) {
                float tmpZVal = captionList.get(i).getZValue();
                if (tmpZVal > zVal) {
                    zVal = tmpZVal;
                    index = i;
                }
            }
            mCurCaption = captionList.get(index);
            if (mCurCaption.getCategory() == NvsTimelineCaption.THEME_CATEGORY
                    && mCurCaption.getRoleInTheme() != NvsTimelineCaption.ROLE_IN_THEME_GENERAL) {
                mCurCaption = null;
                mCaptionStyleButton.setVisibility(View.GONE);
            } else {
                mCaptionStyleButton.setVisibility(View.VISIBLE);
            }
        } else {
            mCurCaption = null;
            mCaptionStyleButton.setVisibility(View.GONE);
        }
    }

    private void selectTimeSpan() {
        for (int i = 0; i < mTimeSpanInfoList.size(); i++) {
            if (mCurCaption != null &&
                    mTimeSpanInfoList.get(i).mCaption == mCurCaption) {
                mTimelineEditor.selectTimeSpan(mTimeSpanInfoList.get(i).mTimeSpan);
                break;
            }
        }
    }

    private void deleteCurCaptionTimeSpan() {
        for (int i = 0; i < mTimeSpanInfoList.size(); i++) {
            if (mTimeSpanInfoList.get(i).mCaption == mCurCaption) {
                mTimelineEditor.deleteSelectedTimeSpan(mTimeSpanInfoList.get(i).mTimeSpan);
                mTimeSpanInfoList.remove(i);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        mStreamingContext.stop();
        removeTimeline();
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void removeTimeline() {
        TimelineUtil.removeTimeline(mTimeline);
        mTimeline = null;
        m_handler.removeCallbacksAndMessages(null);
    }

    private class CaptionTimeSpanInfo {
        public NvsTimelineCaption mCaption;
        public NvsTimelineTimeSpan mTimeSpan;

        public CaptionTimeSpanInfo(NvsTimelineCaption caption, NvsTimelineTimeSpan timeSpan) {
            this.mCaption = caption;
            this.mTimeSpan = timeSpan;
        }
    }

    private int getCaptionIndex(int curZValue) {
        int index = -1;
        int count = mCaptionDataListClone.size();
        for (int i = 0; i < count; ++i) {
            int zVal = mCaptionDataListClone.get(i).getCaptionZVal();
            if (curZValue == zVal) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCaptionStyleButton.setClickable(true);
    }
}
