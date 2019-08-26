package com.meishe.sdkdemo.edit.music;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.dataInfo.MusicInfo;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ms on 2018/7/15
 * note: MediaPlayer功能封装，使用meidiaPlayer时时间单位是毫秒
 */

public class AudioPlayer {
    private final  String TAG = "AudioPlayer";
    private final PlayHandler m_handler = new PlayHandler(this);
    private static final int UPDATE_TIME = 0;
    private MediaPlayer mMediaPlayer;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private MusicInfo mCurrentMusic;
    private OnPlayListener mListener;
    private Context mContext;
    private static AudioPlayer mMusicPlayer;


    public void setPlayListener(OnPlayListener listener) {
        mListener = listener;
    }

    private AudioPlayer(Context context) {
        mContext = context;
        mCurrentMusic = null;
    }

    public static AudioPlayer getInstance(Context context){
        if (mMusicPlayer == null) {
            synchronized (AudioPlayer.class) {
                if (mMusicPlayer == null) {
                    mMusicPlayer = new AudioPlayer(context);
                }
            }
        }
        return mMusicPlayer;
    }

    public void destroyPlayer() {
        if(mMediaPlayer == null)
            return;
        stopMusicTimer();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        m_handler.removeCallbacksAndMessages(null);
    }

    public void startPlay() {
        stopMusicTimer();
        if(mCurrentMusic == null || mMediaPlayer == null)
            return;
        if(mCurrentMusic.isPrepare()) {
            try {
                mMediaPlayer.start();
                startMusicTimer();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "start Exception");
            }
        }
        if(mListener != null)
            mListener.onMusicPlay();
    }

    public void stopPlay() {
        if(mMediaPlayer != null) {
            mMediaPlayer.pause();
            stopMusicTimer();
            if(mListener != null)
                mListener.onMusicStop();
        }
    }

    public void setCurrentMusic(MusicInfo audioInfo, boolean autoPlay) {
        if(audioInfo == null)
            return;
        mCurrentMusic = audioInfo;
        mCurrentMusic.setPrepare(false);
        resetMediaPlayer(autoPlay);
    }

    public void seekPosition(long time) {
        time = time / 1000;
        if (time < mMediaPlayer.getDuration() && time >= 0)
            mMediaPlayer.seekTo((int) time);
    }

    public long getCurMusicPos(){
        return mMediaPlayer.getCurrentPosition();
    }

    // 重置MediaPlayer
    private void resetMediaPlayer(final boolean autoPlay) {
        stopMusicTimer();
        if (mCurrentMusic == null) {
            if(mMediaPlayer == null)
                return;
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "stop & release: null");
            }
            mMediaPlayer = null;
            return;
        }

        if(mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if(mCurrentMusic != null) {
                        int trimIn = (int) mCurrentMusic.getTrimIn() / 1000;
                        if(trimIn > 0)
                            mMediaPlayer.seekTo(trimIn);

                        if(mCurrentMusic.isPrepare()) {
                            if (!Util.isBackground(mContext))
                                startPlay();
                        }
                    }
                }
            });

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    if(mCurrentMusic != null) {
                        mCurrentMusic.setPrepare(true);
                        mMediaPlayer.seekTo((int) mCurrentMusic.getTrimIn() / 1000);
                    }
                    if(!Util.isBackground(mContext) && autoPlay)
                        startPlay();
                }
            });

            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    Toast.makeText(mContext, mContext.getString(R.string.play_error), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
        try {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "stop & release: null");
        }
        try {
            String url = "";
            if(mCurrentMusic.isHttpMusic())
                url = mCurrentMusic.getFileUrl();
            else
                url = mCurrentMusic.getFilePath();
            if(url != null) {
                if(mCurrentMusic.isAsset()) {
                    AssetFileDescriptor musicfd = mContext.getAssets().openFd(mCurrentMusic.getAssetPath());
                    mMediaPlayer.setDataSource(musicfd.getFileDescriptor(), musicfd.getStartOffset(), musicfd.getLength());
                } else {
                    mMediaPlayer.setDataSource(url);
                }
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 开启刷新进度定时器
    private void startMusicTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    m_handler.sendEmptyMessage(UPDATE_TIME);
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
    private void sendCurrentPos(int pos) {
        if(mListener != null)
            mListener.onGetCurrentPos(pos * 1000);
    }

    static class PlayHandler extends Handler
    {
        WeakReference<AudioPlayer> mWeakReference;
        public PlayHandler(AudioPlayer player)
        {
            mWeakReference= new WeakReference<>(player);
        }
        @Override
        public void handleMessage(Message msg)
        {
            final AudioPlayer player = mWeakReference.get();
            if(player!=null)
            {
                switch (msg.what) {
                    case UPDATE_TIME:
                        if(player.mMediaPlayer == null)
                            return;
                        int curPos = player.mMediaPlayer.getCurrentPosition();
                        Log.e("===>", "ccc: " + curPos + " trimIn: " + player.mCurrentMusic.getTrimIn() + " trimOut: " + player.mCurrentMusic.getTrimOut());

                        if (curPos >= player.mCurrentMusic.getTrimOut() / 1000) {
                            Log.e("===>", "seekTo");
                            player.mMediaPlayer.seekTo((int) (player.mCurrentMusic.getTrimIn() / 1000));
                            player.startPlay();
                        }

                        player.sendCurrentPos(curPos);
                        break;
                }
            }
        }
    }

    public interface OnPlayListener {
        void onMusicPlay();
        void onMusicStop();
        void onGetCurrentPos(int curPos);
    }
}
