package com.meishe.sdkdemo.edit.clipEdit;

import android.app.Fragment;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.asset.NvAsset;

import static com.meishe.sdkdemo.utils.Constants.HANDCLICK_DURATION;
import static com.meishe.sdkdemo.utils.Constants.HANDMOVE_DISTANCE;

/**
 * Created by czl on 2018/5/29 0029.
 * 单片段编辑SingleClipFragment，封装liveWindow,供多个页面使用，避免代码重复
 */

public class SingleClipFragment extends Fragment {
    private final static String TAG = "SingleClipFragment";
    private static final int RESETPLATBACKSTATE = 100;
    private final long BASE_VALUE = 100000;
    private NvsLiveWindow mLiveWindow;
    private LinearLayout mPlayBarLayout;
    private RelativeLayout mPlayButton;
    private ImageView mPlayImage;
    private TextView mCurrentPlayTime;
    private SeekBar mPlaySeekBar;
    private TextView mTotalDuration;
    private RelativeLayout mVoiceButton;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private OnFragmentLoadFinisedListener mFragmentLoadFinisedListener;
    private VideoFragmentListener mVideoFragmentCallBack;
    private boolean mPlayBarVisibleState = true;

    private PointF mPrevPoint = new PointF();
    private PointF mCurPoint = new PointF();
    private ScaleGestureDetector mScaleGestureDetector;
    private ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scale = scaleGestureDetector.getScaleFactor();
            if (onScaleGesture != null) {
                onScaleGesture.onHorizScale(scale);
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

        }
    };

    private OnScaleGesture onScaleGesture;
    private int mMakeRatio = NvAsset.AspectRatio_16v9;
    private boolean mIsAddOnTouchEvent = false;
    private long mPrevMillionSecond = 0;
    private double mClickMoveDistance = 0.0D;

    private long mVideoTrimIn = 0;
    private long mVideoTrimOut = 0;

    public interface OnScaleGesture {
        void onHorizScale(float scale);

        void onVerticalTrans(float tranVal);
    }

    //Fragment加载完成回调
    public interface OnFragmentLoadFinisedListener {
        void onLoadFinished();
    }

    //视频播放相关回调
    public interface VideoFragmentListener {
        //video play
        void playBackEOF(NvsTimeline timeline);

        void playStopped(NvsTimeline timeline);

        void playbackTimelinePosition(NvsTimeline timeline, long stamp);

        void streamingEngineStateChanged(int state);
    }

    private Handler m_handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case RESETPLATBACKSTATE:
                    updateCurPlayTime(mVideoTrimIn);
                    seekTimeline(mVideoTrimIn, 0);
                    break;
            }
            return false;
        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_single_clip, container, false);

        mLiveWindow = (NvsLiveWindow) rootView.findViewById(R.id.liveWindow);
        mPlayBarLayout = (LinearLayout) rootView.findViewById(R.id.playBarLayout);
        mPlayButton = (RelativeLayout) rootView.findViewById(R.id.playLayout);
        mPlayImage = (ImageView) rootView.findViewById(R.id.playImage);
        mCurrentPlayTime = (TextView) rootView.findViewById(R.id.currentPlaytime);
        mPlaySeekBar = (SeekBar) rootView.findViewById(R.id.play_seekBar);
        mTotalDuration = (TextView) rootView.findViewById(R.id.totalDuration);
        mVoiceButton = (RelativeLayout) rootView.findViewById(R.id.voiceLayout);
        mScaleGestureDetector = new ScaleGestureDetector(getActivity(), mScaleGestureListener);
        controllerOperation();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated");
        initData();

        if (mIsAddOnTouchEvent) {
            mLiveWindow.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mScaleGestureDetector.onTouchEvent(event);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            mPrevMillionSecond = System.currentTimeMillis();
                            mPrevPoint.set(event.getX(), event.getY());
                            mCurPoint.set(event.getX(), event.getY());
                            break;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            mCurPoint.set(event.getX(), event.getY());
                            mClickMoveDistance = Math.sqrt(Math.pow(mCurPoint.x - mPrevPoint.x, 2) + Math.pow(mCurPoint.y - mPrevPoint.y, 2));
                            Logger.e(TAG, "mClickMoveDistance = " + mClickMoveDistance);
                            float maxValue = 400.0f;
                            if (mMakeRatio == NvAsset.AspectRatio_16v9
                                    || mMakeRatio == NvAsset.AspectRatio_4v3) {
                                maxValue = mLiveWindow.getLayoutParams().height / 2;
                            } else if (mMakeRatio == NvAsset.AspectRatio_1v1
                                    || mMakeRatio == NvAsset.AspectRatio_9v16
                                    || mMakeRatio == NvAsset.AspectRatio_3v4) {
                                maxValue = mLiveWindow.getLayoutParams().width / 2;
                            }
                            if (mCurPoint.y - mPrevPoint.y < 0) {
                                mClickMoveDistance = -mClickMoveDistance;
                            }
                            float transValue = (float) mClickMoveDistance / maxValue;
                            long moveTime_up = System.currentTimeMillis() - mPrevMillionSecond;
                            if (onScaleGesture != null
                                    && moveTime_up > HANDCLICK_DURATION) {
                                onScaleGesture.onVerticalTrans(transValue);
                            }
                            mPrevPoint.set(mCurPoint.x, mCurPoint.y);
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            long moveTime_up = System.currentTimeMillis() - mPrevMillionSecond;
                            if (mClickMoveDistance <= HANDMOVE_DISTANCE
                                    && moveTime_up <= HANDCLICK_DURATION) {
                                mPlayButton.callOnClick();
                            }
                            mClickMoveDistance = 0.0D;
                            break;
                    }
                    return false;
                }
            });
        }

        mPlayBarLayout.setVisibility(mPlayBarVisibleState ? View.VISIBLE : View.GONE);
        if (mFragmentLoadFinisedListener != null) {
            mFragmentLoadFinisedListener.onLoadFinished();
        }
    }

    public void setOnScaleGesture(OnScaleGesture onScaleGesture) {
        this.onScaleGesture = onScaleGesture;
    }

    public void setFragmentLoadFinisedListener(SingleClipFragment.OnFragmentLoadFinisedListener fragmentLoadFinisedListener) {
        this.mFragmentLoadFinisedListener = fragmentLoadFinisedListener;
    }

    public void setVideoFragmentCallBack(VideoFragmentListener videoFragmentCallBack) {
        this.mVideoFragmentCallBack = videoFragmentCallBack;
    }

    private void initData() {
        updateLivewindow();
        updateTotalDuration();
    }


    private void updateLivewindow() {
        Bundle bundle = getArguments();
        int titleHeight = 0, bottomHeight = 0;
        if (bundle != null) {
            mMakeRatio = bundle.getInt("ratio", NvAsset.AspectRatio_16v9);
            titleHeight = bundle.getInt("titleHeight");
            bottomHeight = bundle.getInt("bottomHeight");
            mPlayBarVisibleState = bundle.getBoolean("playBarVisible", true);
            mIsAddOnTouchEvent = bundle.getBoolean("isAddOnTouchEvent", false);
        }
        mStreamingContext = NvsStreamingContext.getInstance();
        if (null == mTimeline) {
            Log.e(TAG, "mTimeline is null!");
            return;
        }

        setLiveWindowRatio(mMakeRatio, titleHeight, bottomHeight);
        connectTimelineWithLiveWindow();
    }

    //设置Timeline
    public void setTimeline(NvsTimeline timeline) {
        this.mTimeline = timeline;
    }

    public void setVideoTrimIn(long videoTrimIn) {
        this.mVideoTrimIn = videoTrimIn;
    }

    public void setVideoTrimOut(long videoTrimOut) {
        this.mVideoTrimOut = videoTrimOut;
    }

    public void playVideo(long startTime, long endTime) {
        // 播放视频
        mStreamingContext.playbackTimeline(mTimeline, startTime, endTime, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
    }

    //连接Timeline跟liveWindow
    public void connectTimelineWithLiveWindow() {
        if (mStreamingContext == null || mTimeline == null || mLiveWindow == null)
            return;
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
                if (mVideoFragmentCallBack != null) {
                    mVideoFragmentCallBack.playbackTimelinePosition(nvsTimeline, l);
                }
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
                if (mVideoFragmentCallBack != null) {
                    mVideoFragmentCallBack.streamingEngineStateChanged(i);
                }
            }

            @Override
            public void onFirstVideoFramePresented(NvsTimeline nvsTimeline) {

            }
        });

        mLiveWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsAddOnTouchEvent)
                    return;
                mPlayButton.callOnClick();
            }
        });
        mStreamingContext.connectTimelineWithLiveWindow(mTimeline, mLiveWindow);
        mPlaySeekBar.setMax((int) (mTimeline.getDuration() / BASE_VALUE));
        updateCurPlayTime(0);
    }

    // 获取当前引擎状态
    public int getCurrentEngineState() {
        return mStreamingContext.getStreamingEngineState();
    }

    //预览
    public void seekTimeline(long timestamp, int seekShowMode) {
        /* seekTimeline
         * param1: 当前时间线
         * param2: 时间戳 取值范围为  [0, timeLine.getDuration()) (左闭右开区间)
         * param3: 图像预览模式
         * param4: 引擎定位的特殊标志
         * */
        mStreamingContext.seekTimeline(mTimeline, timestamp, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, seekShowMode);
    }

    public void updateCurPlayTime(long time) {
        mCurrentPlayTime.setText(formatTimeStrWithUs(time));
        mPlaySeekBar.setProgress((int) (time / BASE_VALUE));
    }

    public void updateTotalDuration() {
        if (mTimeline == null)
            return;
        mTotalDuration.setText(formatTimeStrWithUs(mTimeline.getDuration()));
        mPlaySeekBar.setMax((int) (mTimeline.getDuration() / BASE_VALUE));
    }

    //停止引擎
    public void stopEngine() {
        if (mStreamingContext != null) {
            mStreamingContext.stop();//停止播放
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");

        m_handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG, "onHiddenChanged: " + hidden);
    }

    private void setLiveWindowRatio(int ratio, int titleHeight, int bottomHeight) {
        ViewGroup.LayoutParams layoutParams = mLiveWindow.getLayoutParams();
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
        mLiveWindow.setLayoutParams(layoutParams);
        mLiveWindow.setFillMode(NvsLiveWindow.FILLMODE_PRESERVEASPECTFIT);
    }

    //formate time
    private String formatTimeStrWithUs(long us) {
        int second = (int) (us / 1000000.0);
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        return hh > 0 ? String.format("%02d:%02d:%02d", hh, mm, ss) : String.format("%02d:%02d", mm, ss);
    }

    private void controllerOperation() {
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前引擎状态是否是播放状态
                if (getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    // 播放视频
                    if (mVideoTrimOut > mVideoTrimIn && (mVideoTrimIn >= 0)) {
                        playVideo(mVideoTrimIn, mVideoTrimOut);
                    } else {
                        long startTime = mStreamingContext.getTimelineCurrentPosition(mTimeline);
                        long endTime = mTimeline.getDuration();
                        playVideo(startTime, endTime);
                    }
                } else {
                    stopEngine();
                }
            }
        });

        mPlaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekTimeline(progress * BASE_VALUE, 0);
                    updateCurPlayTime(progress * BASE_VALUE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
