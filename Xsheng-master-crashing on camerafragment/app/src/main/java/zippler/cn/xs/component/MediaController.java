package zippler.cn.xs.component;


import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.util.Log;

import java.util.Arrays;

import zippler.cn.xs.entity.Music;

import static android.media.audiofx.Visualizer.getMaxCaptureRate;

/**
 * Created by Zipple on 2018/5/21.
 */

public class MediaController implements PlayController {
    private static final String TAG="MediaController";
    private Context mContext;

    private volatile int mCurrentSong;

    private volatile boolean mPlayState = false; //true为正在播放

    private  Music song;

    private final MediaPlayer mPlayer;
    private Visualizer mVisualizer;
    private boolean mVisualizerEnable = false;

    public MediaController(Context context,Music music) {
        this.mContext = context.getApplicationContext();
        song = music;
        if (song==null) {
            throw new IllegalStateException("没有歌曲");
        }

        mPlayer = MediaPlayer.create(context, Uri.parse(music.getLocalStorageUrl()));
//        mPlayer.start();
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(TAG, "onError: something wrong in playing music" );
                return true;
            }
        });
    }

    public interface onFftDataCaptureListener {
        void onFftCapture(float[] fft);
    }

    /**
     * 设置频谱回调
     *
     * @param size 传回的数组大小
     * @param max  整体频率的大小，该值越小，传回数组的平均值越大，在 50 时效果较好。
     * @param l    回调
     */
    public void setupVisualizer(final int size, final int max, final onFftDataCaptureListener l) {
        // 频率分之一是时间  赫兹=1/秒
        mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]); //0为128；1为1024
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {

            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                //快速傅里叶变换有关的数据

            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                //波形数据

                byte[] model = new byte[fft.length / 2 + 1];
                model[0] = (byte) Math.abs(fft[1]);
                int j = 1;

                for (int i = 2; i < size * 2; ) {

                    model[j] = (byte) Math.hypot(fft[i], fft[i + 1]);
                    i += 2;
                    j++;
                }

                float[] data = new float[size];
                if (max != 0) {
                    for (int i = 0; i < size; i++) {
                        data[i] = (float) model[i] / max;
                        data[i] = data[i] < 0 ? 0 : data[i];
                    }
                } else {
                    Arrays.fill(data, 0);
                }

                l.onFftCapture(data);

            } // getMaxCaptureRate() -> 20000 最快
        }, getMaxCaptureRate() / 8, false, true);

        mVisualizer.setEnabled(false); //这个设置必须在参数设置之后，表示开始采样
    }

    public void setVisualizerEnable(boolean visualizerEnable) {
        this.mVisualizerEnable = visualizerEnable;
        if (mPlayer.isPlaying()) {
            mVisualizer.setEnabled(mVisualizerEnable);
        }
    }

    public void releaseMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
        }

        if (mVisualizer != null) {
            mVisualizer.release();
        }
    }

    @Override
    public synchronized boolean play() {
        if (mPlayer.isPlaying())
            return false;
        else {
            mPlayer.start();
            if (mVisualizerEnable) {
                mVisualizer.setEnabled(true);
            }
            mPlayState = true;
            return true;
        }
    }

    public boolean playing() {
        return mPlayer.isPlaying();
    }

    @Override
    public synchronized boolean stop() {
        if (!mPlayer.isPlaying())
            return false;
        else {
            mPlayer.pause();
            if (mVisualizerEnable) {
                mVisualizer.setEnabled(false);
            }
            mPlayState = false;
            return true;
        }
    }

    @Override
    public void seekTo(int to) {
        mPlayer.seekTo(to);
    }

    @Override
    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mPlayer.getDuration();
    }
}
