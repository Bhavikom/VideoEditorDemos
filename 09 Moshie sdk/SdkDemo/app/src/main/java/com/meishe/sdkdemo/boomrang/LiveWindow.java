package com.meishe.sdkdemo.boomrang;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;

public class LiveWindow extends NvsLiveWindow {
    private final String TAG = "LiveWindow";
    private NvsStreamingContext m_streamingContext;
    private boolean m_isRecycle = true;

    public LiveWindow(Context context) {
        super(context);
    }

    public LiveWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
        m_streamingContext = NvsStreamingContext.getInstance();
        if(m_streamingContext == null) {
            Log.e(TAG, "init: nvsStreamingContext is null");
            return;
        }
        // 美摄sdk预览视频的状态回调
        if(m_isRecycle) {
            m_streamingContext.setPlaybackCallback(new NvsStreamingContext.PlaybackCallback() {
                @Override
                public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {
                }

                @Override
                public void onPlaybackStopped(NvsTimeline nvsTimeline) {
                }

                @Override
                public void onPlaybackEOF(final NvsTimeline nvsTimeline) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            m_streamingContext.playbackTimeline(nvsTimeline, 0, -1,
                                    NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
                        }
                    });
                }
            });
        }
    }


    /**
     * 播放视频
     * @param nvsTimeline 要播放预览的时间线
     */
    public void playVideo(NvsTimeline nvsTimeline) {
        if(m_streamingContext == null) {
            Log.e(TAG, "playVideo: nvsStreamingContext is null");
            return;
        }
        if(nvsTimeline == null) {
            Log.e(TAG, "playVideo: nvsTimeline is null");
            return;
        }

        if (nvsTimeline.getVideoRes().imageWidth >= nvsTimeline.getVideoRes().imageHeight) {
            this.setFillMode(NvsLiveWindow.FILLMODE_PRESERVEASPECTFIT);
        }
        m_streamingContext.connectTimelineWithLiveWindow(nvsTimeline, this);
        m_streamingContext.playbackTimeline(nvsTimeline, m_streamingContext.getTimelineCurrentPosition(nvsTimeline), -1,
                NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
    }

    /**
     * seek时间线到指定时刻
     * @param timestamp 时间戳（单位微秒）
     */
    private void seekTimeline(NvsTimeline nvsTimeline, long timestamp) {
        if(m_streamingContext == null) {
            Log.e(TAG, "seekTimeline: nvsStreamingContext is null");
            return;
        }
        if(nvsTimeline == null) {
            Log.e(TAG, "seekTimeline: nvsTimeline is null");
            return;
        }
        m_streamingContext.seekTimeline(nvsTimeline, timestamp, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);
    }
}
