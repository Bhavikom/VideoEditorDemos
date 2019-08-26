package com.meishe.sdkdemo.makecover;

import android.app.Fragment;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.asset.NvAsset;


/**
 * 制作封面Fragment
 */

public class MakeCoverFragment extends Fragment {

    private static final String TAG = "MakeCoverFragment";

    private static final int RESETPLATBACKSTATE = 100;
    private boolean mPlayBarVisibleState = true;
    private int mMakeRatio = NvAsset.AspectRatio_16v9;
    private PointF mPrePoint = new PointF();
    private PointF midPoint = new PointF();
    private float oriDis = 1f;
    private float tempDis;

    enum MODE {NONE, DRAG, ZOOM}

    private int mode = MODE.NONE.ordinal();
    private double mScaleValue = 1.0D, mRotateAngle = 0;
    private double mTransX = 0, mTransY = 0;

    private NvsLiveWindow mLiveWindow;
    private LinearLayout mPlayBarLayout;
    private RelativeLayout mPlayButtonLayout;
    private ImageView mPlayImage;
    private TextView mCurrentPlayTime;
    private SeekBar mPlaySeekBar;
    private TextView mTotalDuration;
    private RelativeLayout mPlayerLayout;

    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;

    private VideoFragmentListener mVideoFragmentCallBack;
    private OnSeekbarTraceStopedListener mOnSeekbarTraceStopedListener;

    public interface VideoFragmentListener {
        void playBackEOF(NvsTimeline timeline);

        void playStopped(NvsTimeline timeline);

        void playbackTimelinePosition(NvsTimeline timeline, long stamp);

        void streamingEngineStateChanged(int state);

        void onFirstVideoFramePresented();

        void aVPlayStopedByButton();

        void aVPlayStartedByButton();
    }

    public interface OnSeekbarTraceStopedListener {
        void onCoverPositionSelected();
    }

    public void resetAppearance() {
        mScaleValue = 1.0D;
        mRotateAngle = 0;
        mTransX = 0;
        mTransY = 0;
        updateTransform2DFx(mScaleValue, mRotateAngle, mTransX, mTransY);
        seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
    }

    private Handler m_handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RESETPLATBACKSTATE:
                    updateSeekBarProgress(0);
                    updateCurPlayTime(0);
                    seekTimeline(0, 0);
                    break;
            }
            return false;
        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_single_clip, container, false);
        findViewByIds(rootView);
        initUiListener();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureLivewindow();
        initLiveWindowListener();
        updateTotalDuration();
    }

    private void initLiveWindowListener() {
        mPlaySeekBar.setMax((int) mTimeline.getDuration());
        updateCurPlayTime(0);
        updateSeekBarProgress(0);
        seekTimeline(0);
        mStreamingContext.setPlaybackCallback(new NvsStreamingContext.PlaybackCallback() {
            @Override
            public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {
            }

            @Override
            public void onPlaybackStopped(NvsTimeline nvsTimeline) {
                if (mVideoFragmentCallBack != null) {
                    mVideoFragmentCallBack.playStopped(nvsTimeline);
                }
            }

            @Override
            public void onPlaybackEOF(NvsTimeline nvsTimeline) {
                if (mPlayBarVisibleState) {
                    m_handler.sendEmptyMessage(RESETPLATBACKSTATE);
                }
                if (mVideoFragmentCallBack != null) {
                    mVideoFragmentCallBack.playBackEOF(nvsTimeline);
                }
            }
        });

        mStreamingContext.setPlaybackCallback2(new NvsStreamingContext.PlaybackCallback2() {
            @Override
            public void onPlaybackTimelinePosition(NvsTimeline nvsTimeline, long l) {
                updateCurPlayTime(l);
                updateSeekBarProgress((int) l);
            }
        });

        mStreamingContext.setStreamingEngineCallback(new NvsStreamingContext.StreamingEngineCallback() {
            @Override
            public void onStreamingEngineStateChanged(int i) {
                if (i == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    mPlayImage.setBackgroundResource(R.mipmap.icon_edit_pause);
                } else {
                    mPlayImage.setBackgroundResource(R.mipmap.icon_edit_play);
                }
                mVideoFragmentCallBack.streamingEngineStateChanged(i);
            }

            @Override
            public void onFirstVideoFramePresented(NvsTimeline nvsTimeline) {
                mVideoFragmentCallBack.onFirstVideoFramePresented();
            }
        });

        mLiveWindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    // 单指
                    case MotionEvent.ACTION_DOWN:
                        mPrePoint.set(event.getX(), event.getY());
                        mode = MODE.DRAG.ordinal();
                        Log.d(TAG, "ACTION_DOWN: " + mPrePoint.toString());
                        break;
                    // 双指
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oriDis = distance(event);
                        if (oriDis > 15f) {
                            midPoint = middle(event);
                            mode = MODE.ZOOM.ordinal();
                            tempDis = oriDis;
                        }
                        Log.d(TAG, "ACTION_POINTER_DOWN: " + oriDis + ":" + midPoint);
                        break;
                    // 手指放开
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = MODE.NONE.ordinal();
                        break;
                    // 单指滑动事件
                    case MotionEvent.ACTION_MOVE:
                        if (mode == MODE.DRAG.ordinal()) {
                            // 是一个手指拖动
                            float transX = event.getX() - mPrePoint.x;
                            float transY = mPrePoint.y - event.getY();
                            mTransX += transX;
                            mTransY += transY;
                            updateTransform2DFx(mScaleValue, mRotateAngle, mTransX, mTransY);
                            mPrePoint.set(event.getX(), event.getY());
                            Log.d(TAG, "ACTION_MOVE_SINGLE: " + mTransX + ":" + mTransY);
                        } else if (mode == MODE.ZOOM.ordinal()) {
                            // 两个手指滑动
                            float newDist = distance(event);
                            if ((newDist - tempDis) > 2f) {
                                mScaleValue = mScaleValue * 1.1;
                                updateTransform2DFx(mScaleValue, mRotateAngle, mTransX, mTransY);
                            } else if ((tempDis - newDist) >= 2f) {
                                mScaleValue = mScaleValue * 0.9;
                                updateTransform2DFx(mScaleValue, mRotateAngle, mTransX, mTransY);
                            }
                            tempDis = newDist;
                            Log.d(TAG, "ACTION_MOVE_DOUBLE: " + newDist + ":" + mScaleValue);
                        }
                        seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
                        break;
                }
                return true;
            }
        });
    }

    void seekTimeline(long timestamp) {
        if (mStreamingContext != null) {
            mStreamingContext.seekTimeline(mTimeline, timestamp, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);
        }
    }

    private void configureLivewindow() {
        Bundle bundle = getArguments();
        int titleHeight = 0, bottomHeight = 0;
        if (bundle != null) {
            mMakeRatio = bundle.getInt("ratio", NvAsset.AspectRatio_16v9);
            titleHeight = bundle.getInt("titleHeight");
            bottomHeight = bundle.getInt("bottomHeight");
            mPlayBarVisibleState = bundle.getBoolean("playBarVisible", true);
            mPlayBarLayout.setVisibility(mPlayBarVisibleState ? View.VISIBLE : View.GONE);
        }
        mStreamingContext = NvsStreamingContext.getInstance();
        if (null == mTimeline) {
            return;
        }
        setLiveWindowRatio(mMakeRatio, titleHeight, bottomHeight);
        if (mStreamingContext == null || mTimeline == null || mLiveWindow == null)
            return;
        mStreamingContext.connectTimelineWithLiveWindow(mTimeline, mLiveWindow);
    }

    public void setTimeline(NvsTimeline timeline) {
        this.mTimeline = timeline;
    }

    public void playVideo(long startTime, long endTime) {
        mStreamingContext.playbackTimeline(mTimeline, startTime, endTime, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
    }

    public int getCurrentEngineState() {
        return mStreamingContext.getStreamingEngineState();
    }

    public void seekTimeline(long timestamp, int seekShowMode) {
        mStreamingContext.seekTimeline(mTimeline, timestamp, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, seekShowMode);
    }

    public void updateCurPlayTime(long time) {
        mCurrentPlayTime.setText(formatTimeStrWithUs(time));
    }

    public void updateSeekBarProgress(int progress) {
        mPlaySeekBar.setProgress(progress);
    }

    public void updateTotalDuration() {
        mTotalDuration.setText(formatTimeStrWithUs(mTimeline.getDuration()));
        mPlaySeekBar.setMax((int) mTimeline.getDuration());
    }

    public void stopEngine() {
        if (mStreamingContext != null) {
            mStreamingContext.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        m_handler.removeCallbacksAndMessages(null);
    }

    private void initUiListener() {
        mPlayButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前引擎状态是否是播放状态
                if (getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    // 播放视频
                    long startTime = mStreamingContext.getTimelineCurrentPosition(mTimeline);
                    long endTime = mTimeline.getDuration();
                    if (mVideoFragmentCallBack != null) {
                        mVideoFragmentCallBack.aVPlayStartedByButton();
                    }
                    playVideo(startTime, endTime);
                } else {
                    stopEngine();
                    if (mVideoFragmentCallBack != null) {
                        mVideoFragmentCallBack.aVPlayStopedByButton();
                    }
                }
            }
        });

        mPlaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekTimeline(progress, 0);
                    updateCurPlayTime(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mOnSeekbarTraceStopedListener != null) {
                    mOnSeekbarTraceStopedListener.onCoverPositionSelected();
                }
            }
        });
    }

    private void findViewByIds(View rootView) {
        mLiveWindow = (NvsLiveWindow) rootView.findViewById(R.id.liveWindow);
        mPlayBarLayout = (LinearLayout) rootView.findViewById(R.id.playBarLayout);
        mPlayerLayout = (RelativeLayout) rootView.findViewById(R.id.player_layout);
        mPlayButtonLayout = (RelativeLayout) rootView.findViewById(R.id.playLayout);
        mPlayImage = (ImageView) rootView.findViewById(R.id.playImage);
        mCurrentPlayTime = (TextView) rootView.findViewById(R.id.currentPlaytime);
        mPlaySeekBar = (SeekBar) rootView.findViewById(R.id.play_seekBar);
        mTotalDuration = (TextView) rootView.findViewById(R.id.totalDuration);
    }

    private void setLiveWindowRatio(int ratio, int titleHeight, int bottomHeight) {
        ViewGroup.LayoutParams layoutParams = mPlayerLayout.getLayoutParams();
        int statusHeight = ScreenUtils.getStatusBarHeight(getActivity());//状态栏高度
        int screenWidth = ScreenUtils.getScreenWidth(getActivity());//屏宽
        int screenHeight = ScreenUtils.getScreenHeight(getActivity());//屏高
        int newHeight = screenHeight - titleHeight - bottomHeight - statusHeight;
        switch (ratio) {
            case NvAsset.AspectRatio_16v9: // 16:9
                layoutParams.width = screenWidth;
                layoutParams.height = (int) (screenWidth * 9.0 / 16);
                break;
            case NvAsset.AspectRatio_1v1: //1:1
                layoutParams.width = screenWidth;
                layoutParams.height = screenWidth;
                if (newHeight < screenWidth) {
                    layoutParams.width = newHeight;
                    layoutParams.height = newHeight;
                }
                break;
            case NvAsset.AspectRatio_9v16: //9:16
                layoutParams.width = (int) (newHeight * 9.0 / 16);
                layoutParams.height = newHeight;
                break;
            case NvAsset.AspectRatio_3v4: // 3:4
                layoutParams.width = (int) (newHeight * 3.0 / 4);
                layoutParams.height = newHeight;
                break;
            case NvAsset.AspectRatio_4v3: //4:3
                layoutParams.width = screenWidth;
                layoutParams.height = (int) (screenWidth * 3.0 / 4);
                break;
            default: // 16:9
                layoutParams.width = screenWidth;
                layoutParams.height = (int) (screenWidth * 9.0 / 16);
                break;
        }
        mPlayerLayout.setLayoutParams(layoutParams);
        mLiveWindow.setFillMode(NvsLiveWindow.FILLMODE_PRESERVEASPECTFIT);
    }

    public void setVideoFragmentCallBack(VideoFragmentListener videoFragmentCallBack) {
        this.mVideoFragmentCallBack = videoFragmentCallBack;
    }

    public void setSelectCoverListener(OnSeekbarTraceStopedListener onSeekbarTraceStopedListener) {
        this.mOnSeekbarTraceStopedListener = onSeekbarTraceStopedListener;
    }

    private void updateTransform2DFx(double scaleValue, double rotateAngle, double transX, double transY) {
        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        int clipCount = videoTrack.getClipCount();
        for (int clipIindex = 0; clipIindex < clipCount; ++clipIindex) {
            NvsVideoClip videoClip = videoTrack.getClipByIndex(clipIindex);
            int fxCount = videoClip.getFxCount();
            for (int fxIndex = 0; fxIndex < fxCount; ++fxIndex) {
                NvsVideoFx videoFx = videoClip.getFxByIndex(fxIndex);
                String videoFxName = "Transform 2D";
                if (videoFx.getVideoFxType() == NvsVideoFx.VIDEOFX_TYPE_BUILTIN
                        && videoFx.getBuiltinVideoFxName().equals(videoFxName)) {
                    Log.d(TAG, "updateTransform2DFx: " + "apply:updateTransform2DFx");
                    //缩放
                    videoFx.setFloatVal("Scale X", scaleValue);
                    videoFx.setFloatVal("Scale Y", scaleValue);
                    //旋转
                    videoFx.setFloatVal("Rotation", rotateAngle);
                    //片段偏移
                    videoFx.setFloatVal("Trans X", transX);
                    videoFx.setFloatVal("Trans Y", transY);
                    break;
                }
            }
        }
    }

    private String formatTimeStrWithUs(long us) {
        int second = (int) (us / 1000000.0 + 0.5);
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        return hh > 0 ? String.format("%02d:%02d:%02d", hh, mm, ss) : String.format("%02d:%02d", mm, ss);
    }

    // 计算两个触摸点之间的距离
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // 计算两个触摸点的中点
    private PointF middle(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        return new PointF(x / 2, y / 2);
    }

}
