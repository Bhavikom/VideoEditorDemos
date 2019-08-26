package com.meishe.sdkdemo.particle;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.edit.CompileVideoFragment;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.Util;

/**
 * Created by ms on 2018/9/21 0021.
 */

public class ParticlePreviewActivity extends BaseActivity {
    private final String TAG = "ParticlePreviewActivity";
    private Context mContext;
    private LiveWindow mLiveWindow;
    private Button mCloseBtn;
    private ImageView mCompileBtn;
    private SeekBar mPlaySeekBar;
    private ImageButton mPlayBtn;
    private RelativeLayout mCompilePage;
    private NvsTimeline mTimeline;
    private NvsStreamingContext mStreamingContext;
    private long mPlayStartFlag = -1; // 播放开始标识
    private CompileVideoFragment mCompileVideoFragment;

    @Override
    protected int initRootView() {
        mContext = this;
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_particle_preview;
    }
    @Override
    protected void initViews() {
        mLiveWindow = (LiveWindow) findViewById(R.id.liveWindow);
        mCloseBtn = (Button) findViewById(R.id.closeBtn);
        mCompileBtn = (ImageView) findViewById(R.id.compileBtn);
        mPlaySeekBar = (SeekBar) findViewById(R.id.playSeekBar);
        mPlayBtn = (ImageButton) findViewById(R.id.playBtn);
        mCompilePage = (RelativeLayout) findViewById(R.id.compilePage);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initData() {
        initTimeline();
        initCompileVideoFragment();
    }

    @Override
    protected void initListener() {
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });

        mCompileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCompilePage.setVisibility(View.VISIBLE);
                mCompileVideoFragment.compileVideo();
            }
        });

        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int state = mStreamingContext.getStreamingEngineState();
                if (state == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    mStreamingContext.stop();
                    mPlayStartFlag = -1;
                } else {
                    mPlayStartFlag = mStreamingContext.getTimelineCurrentPosition(mTimeline);
                    mStreamingContext.playbackTimeline(mTimeline, mPlayStartFlag, -1,
                            NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
                }
            }
        });

        mLiveWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayBtn.getVisibility() != View.VISIBLE) {
                    mPlayStartFlag = mStreamingContext.getTimelineCurrentPosition(mTimeline);
                    mPlayBtn.setVisibility(View.VISIBLE);
                    return;
                }
                mPlayBtn.performClick();
            }
        });

        mCompilePage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mCompileVideoFragment.setCompileVideoListener(new CompileVideoFragment.OnCompileVideoListener() {
            @Override
            public void compileProgress(NvsTimeline timeline, int progress) {

            }

            @Override
            public void compileFinished(NvsTimeline timeline) {
                mCompilePage.setVisibility(View.GONE);
            }

            @Override
            public void compileFailed(NvsTimeline timeline) {
                mCompilePage.setVisibility(View.GONE);
            }

            @Override
            public void compileCompleted(NvsTimeline nvsTimeline, boolean isCanceled) {
                mCompilePage.setVisibility(View.GONE);
            }

            @Override
            public void compileVideoCancel() {
                mCompilePage.setVisibility(View.GONE);
            }
        });

        mPlaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if(fromUser) {
                    seekTimeline(i, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mStreamingContext.setPlaybackCallback(new NvsStreamingContext.PlaybackCallback() {
            @Override
            public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {
            }

            @Override
            public void onPlaybackStopped(NvsTimeline nvsTimeline) {
            }

            @Override
            public void onPlaybackEOF(NvsTimeline nvsTimeline) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        seekTimeline(0 ,0);
                        mPlaySeekBar.setProgress(0);
                    }
                });
            }
        });

        mStreamingContext.setPlaybackCallback2(new NvsStreamingContext.PlaybackCallback2() {
            @Override
            public void onPlaybackTimelinePosition(NvsTimeline nvsTimeline, long cur_position) {
                // 播放按钮消失
                if (mPlayStartFlag != -1) {
                    if (cur_position - mPlayStartFlag >= 3000000)
                        mPlayBtn.setVisibility(View.GONE);
                }
                mPlaySeekBar.setProgress((int) cur_position);
            }
        });

        mStreamingContext.setStreamingEngineCallback(new NvsStreamingContext.StreamingEngineCallback() {
            @Override
            public void onStreamingEngineStateChanged(int i) {
                if (i == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    mPlayBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.particle_pause));
                } else {
                    mPlayBtn.setVisibility(View.VISIBLE);
                    mPlayBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.particle_play));
                }
            }
            @Override
            public void onFirstVideoFramePresented(NvsTimeline nvsTimeline) {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    private void initTimeline() {
        mTimeline = TimelineUtil.createTimeline();
        if (mStreamingContext == null || mTimeline == null || mLiveWindow == null)
            return;
        mPlaySeekBar.setMax((int) mTimeline.getDuration());

        mLiveWindow.post(new Runnable() {
            @Override
            public void run() {
                int width = mLiveWindow.getWidth();
                int height = mLiveWindow.getHeight();
                NvsSize liveWindowSize = Util.getLiveWindowSize(TimelineUtil.getTimelineSize(mTimeline), new NvsSize(width, height), false);
                ViewGroup.LayoutParams params = mLiveWindow.getLayoutParams();
                params.width = liveWindowSize.width;
                params.height = liveWindowSize.height;
                mLiveWindow.setLayoutParams(params);
            }
        });

        mStreamingContext.connectTimelineWithLiveWindow(mTimeline, mLiveWindow);
        seekTimeline(0, 0);
    }

    private void finishActivity(){
        AppManager.getInstance().finishActivity();
        if(mStreamingContext.getStreamingEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK)
            mStreamingContext.stop();
        removeTimeline();
    }
    private void removeTimeline() {
        if(mStreamingContext != null && mTimeline != null){
            TimelineUtil.removeTimeline(mTimeline);
            mTimeline = null;
        }
    }

    private void seekTimeline(long timestamp, int seekShowMode) {
        /* seekTimeline
         * param1: 当前时间线
         * param2: 时间戳 取值范围为  [0, timeLine.getDuration()) (左闭右开区间)
         * param3: 图像预览模式
         * param4: 引擎定位的特殊标志
         * */
        mStreamingContext.seekTimeline(mTimeline, timestamp, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, seekShowMode);
    }

    private void initCompileVideoFragment() {
        mCompileVideoFragment = new CompileVideoFragment();
        mCompileVideoFragment.setTimeline(mTimeline);
        getFragmentManager().beginTransaction()
                .add(R.id.compilePage, mCompileVideoFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mCompileVideoFragment);
    }
}
