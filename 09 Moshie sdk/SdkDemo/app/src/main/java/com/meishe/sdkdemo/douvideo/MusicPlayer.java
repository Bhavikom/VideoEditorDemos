package com.meishe.sdkdemo.douvideo;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.meishe.sdkdemo.utils.dataInfo.MusicInfo;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/*变速播放音乐，时间单位是毫秒*/

public class MusicPlayer {
    private final  String TAG = getClass().getSimpleName();
    private static final int UPDATE_TIME = 0;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private MusicInfo mCurrentMusic;
    private OnPlayListener mListener;
    private Context mContext;
    private SimpleExoPlayer mExoPlayer;
    private float mSpeed;
    private PlayHandler mPlayHandler;

    public MusicPlayer(Context context) {
        mContext = context;
        mCurrentMusic = null;
        mPlayHandler = new PlayHandler(this);
        mSpeed = 1;

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory factory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(factory);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
    }

    public void setPlayListener(OnPlayListener listener) {
        mListener = listener;
    }

    public void destroyPlayer() {
        if(mExoPlayer == null)
            return;
        stopMusicTimer();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.release();
            mExoPlayer = null;
        }
        mPlayHandler.removeCallbacksAndMessages(null);
    }

    public void startPlay() {
        stopMusicTimer();
        if(mCurrentMusic == null || mExoPlayer == null)
            return;

        mExoPlayer.setPlayWhenReady(true);
        startMusicTimer();

        if(mListener != null) {
            mListener.onMusicPlay();
        }
    }

    public void stopPlay() {
        if(mExoPlayer != null) {
            stopMusicTimer();
            mPlayHandler.removeCallbacksAndMessages(null);
            mExoPlayer.setPlayWhenReady(false);
            if(mListener != null)
                mListener.onMusicStop();
        }
    }

    public boolean isPlaying(){
        return mExoPlayer.getPlayWhenReady();
    }

    public void setCurrentMusic(MusicInfo audioInfo) {
        if(audioInfo == null)
            return;
        mCurrentMusic = audioInfo;
        mCurrentMusic.setPrepare(false);
        resetExoPlayer();
    }

    public void setSpeed(float speed){
        mSpeed = speed;
    }

    public float getSpeed(){
        return mSpeed;
    }

    public void seekPosition(long time) {
        mExoPlayer.seekTo(time);
    }

    public long getCurMusicPos(){
        return mExoPlayer.getCurrentPosition();
    }

    // 重置ExoPlayer,换音乐
    public void resetExoPlayer() {
        stopMusicTimer();
        try {
            if(mCurrentMusic == null)
                return;
            String url = mCurrentMusic.getExoPlayerPath();

            if(url == null)
                return;
            DefaultBandwidthMeter defaultBandwidthMeter=new DefaultBandwidthMeter();
            DataSource.Factory factory = new DefaultDataSourceFactory(mContext, com.google.android.exoplayer2.util.Util.getUserAgent(mContext,"SDKDemo"),defaultBandwidthMeter);
            Uri uri= Uri.parse(url);

            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(uri,factory,extractorsFactory,null,null);
            mExoPlayer.prepare(mediaSource);
            long seekTime = mCurrentMusic.getTrimIn();
            if(seekTime < 0)
                seekTime = 0;
            mExoPlayer.seekTo(seekTime);

            PlaybackParameters playbackParameters = new PlaybackParameters(mSpeed, 1.0f);
            mExoPlayer.setPlaybackParameters(playbackParameters);
            mExoPlayer.setPlayWhenReady(false);

        }catch (Exception e){
            e.fillInStackTrace();
        }

    }

    // 开启刷新进度定时器
    private void startMusicTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mExoPlayer != null && mExoPlayer.getPlayWhenReady()) {
                    mPlayHandler.sendEmptyMessage(UPDATE_TIME);
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 100);
    }

    // 停止刷新进度定时器
    private void stopMusicTimer() {
        if(mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if(mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    // 回调进度值
    private void sendCurrentPos(long pos) {
        if(mListener != null)
            mListener.onGetCurrentPos(pos);
    }

    static class PlayHandler extends Handler
    {
        WeakReference<MusicPlayer> mWeakReference;
        public PlayHandler(MusicPlayer player)
        {
            mWeakReference= new WeakReference<>(player);
        }
        @Override
        public void handleMessage(Message msg)
        {
            final MusicPlayer player = mWeakReference.get();
            if(player!=null)
            {
                switch (msg.what) {
                    case UPDATE_TIME:
                        if(player.mExoPlayer == null)
                            return;
                        long curPos = player.getCurMusicPos();

//                        Log.d("===>", "ccc: " + curPos + " trimIn: " + player.mCurrentMusic.getTrimIn() + " trimOut: " + player.mCurrentMusic.getTrimOut());
//                        Log.d("1234: ", "curPos: " + curPos);

                        if (curPos >= player.mCurrentMusic.getTrimOut() / 1000) {
//                            Log.d("===>", "seekTo");
                            player.mExoPlayer.seekTo((int) (player.mCurrentMusic.getTrimIn() / 1000));
                            player.startPlay();
                        }

                        long musicDur = player.mCurrentMusic.getDuration();
                        if(curPos*1000 <= musicDur){
                            player.sendCurrentPos(curPos);
                        }

                        break;
                }
            }
        }
    }

    public interface OnPlayListener {
        void onMusicPlay();
        void onMusicStop();
        void onGetCurrentPos(long curPos);
    }
}
