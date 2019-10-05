package com.example.commonDemo.noFree;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lansoeditor.demo.R;
import com.lansosdk.NoFree.LSOExtractFrame;
import com.lansosdk.videoeditor.MediaInfo;
import com.lansosdk.NoFree.onExtractFrameCompletedListener;
import com.lansosdk.NoFree.onExtractFrameProgressListener;

import java.nio.IntBuffer;

/**
 * 快速获取视频的每一帧, 注意: 在运行此类的时候, 请不要同时运行MediaPlayer或我们的DrawPad类,
 * 因为在高通410,610等低端处理器中, 他们是共用同一个硬件资源,会冲突;但各种旗舰机 不会出现类似问题.
 */
public class ExtractFrameDemoActivity extends Activity {

    private static final String TAG = "ExtractFrameDemoActivity";
    static int bmtcnt = 0;
    String videoPath = null;
    ProgressDialog mProgressDialog;
    int videoDuration;
    boolean isRuned = false;
    MediaInfo mInfo;
    TextView tvProgressHint;
    TextView tvHint;
    private boolean isExecuting = false;
    private LSOExtractFrame extractFrame;
    private long startTime;
    private int frameCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        videoPath = getIntent().getStringExtra("videopath");
        setContentView(R.layout.extract_video_frame_layout);

        initUI();
    }

    /**
     * 从这里开始演示.
     */
    private void startExtract() {
        if (isExecuting)
            return;

        isExecuting = true;

        mInfo = new MediaInfo(videoPath);
        if (mInfo.prepare() == false || mInfo.isHaveVideo() == false) {
            return;
        }
        Log.i("LSTODO", "info si "+mInfo);
        /**
         * 初始化.
         */
        extractFrame = new LSOExtractFrame(ExtractFrameDemoActivity.this, videoPath);


        //演示缩小一倍
        if (mInfo.vWidth * mInfo.vHeight > 960 * 540) {
            extractFrame.setBitmapWH(mInfo.vWidth / 2, mInfo.vHeight / 2);
        }
        /**
         * 设置处理完成监听.
         */
        extractFrame.setOnExtractCompletedListener(new onExtractFrameCompletedListener() {

            @Override
            public void onCompleted() {

                long timeOut = System.currentTimeMillis() - startTime; // 单位毫秒.
                float leftF = ((float) timeOut / 1000);
                float b = (float) (Math.round(leftF * 10)) / 10; // 保留一位小数.

                if (mInfo != null && extractFrame !=null) {
                    String str = "解码结束:\n" + "解码的视频总帧数:" + frameCount
                            + "\n" + "解码耗时:" + b + "(秒)" + "\n" + "\n"
                            + "\n" + "视频宽高:" + mInfo.vWidth + " x "
                            + mInfo.vHeight + "\n" + "解码后图片缩放宽高:"
                            + extractFrame.getBitmapWidth() + " x "
                            + extractFrame.getBitmapHeight() + "\n";

                    Log.i("TIME", "解码结束::" + str);
                    tvProgressHint.setText("Completed" + str);
                    isExecuting = false;
                    frameCount = 0;
                }

            }
        });
        /**
         * 设置处理进度监听.
         */
        extractFrame.setOnExtractProgressListener(new onExtractFrameProgressListener() {

            @Override
            public void onExtractBitmap(Bitmap bmp, long ptsUS) {
                frameCount++;
                String hint = " 当前是第" + frameCount + "帧" + "\n"
                        + "当前帧的时间戳是:" + String.valueOf(ptsUS) + "微秒";

                tvProgressHint.setText(hint);

                // savePng(bmp); //测试使用.
                // if(bmp!=null && bmp.isRecycled()){
                    if(bmp!=null){
                        bmp.recycle();
                    }

                // bmp=null;
                // }
                // if(ptsUS>15*1000*1000){ // 你可以在指定的时间段停止.
                // extractFrame.stop(); //这里演示在15秒的时候停止.
                // }
            }
        });
        frameCount = 0;
        /**
         * 开始执行. 或者你可以从指定地方开始解码.
         * extractFrame.start(10*1000*1000);则从视频的10秒处开始提取.
         */
        extractFrame.start();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (extractFrame != null) {
            extractFrame.release();
            extractFrame = null;
        }
    }

    private void initUI() {
        tvHint = (TextView) findViewById(R.id.id_extract_frame_hint);
        tvHint.setText(R.string.extract_video_frame_hint);
        tvProgressHint = (TextView) findViewById(R.id.id_extract_frame_progress_hint);

        findViewById(R.id.id_extract_frame_btn).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startExtract();
                    }
                });
        findViewById(R.id.id_extract_frame_btn2).setVisibility(View.GONE);
        /**
         * 一下是测试,读取指定视频位置的图片, 读取速度比上面慢一些, 如果您频繁的读取, 建议直接一次性读取完毕,放到sd卡里,然后用的时候,
         * 从sd卡中读取.
         */
        // findViewById(R.id.id_extract_frame_btn2).setOnClickListener(new
        // OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // if(extractFrame!=null){
        // seekCount++;
        // extractFrame.seekPause(seekCount*1000*1000);
        // }
        // }
        // });
    }
}