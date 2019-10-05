package com.example.commonDemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lansoeditor.demo.R;
import com.lansosdk.NoFree.LSOVideoScale;
import com.lansosdk.videoeditor.LanSongFileUtil;
import com.lansosdk.videoeditor.MediaInfo;
import com.lansosdk.videoeditor.VideoEditor;
import com.lansosdk.videoeditor.onVideoEditorEncodeChangedListener;
import com.lansosdk.videoeditor.onVideoEditorProgressListener;

import java.io.IOException;

/**
 * 杭州蓝松科技, 专业的视频开发团队.
 * <p>
 * 基本版本中视频编辑演示.
 * 此代码不属于sdk的一部分， 仅作为演示使用。
 */
public class AVEditorDemoActivity extends Activity implements OnClickListener {

    private final static boolean VERBOSE = false;
    private final static String TAG = "AVSplitDemoActivity";


    String srcVideo = null;

    VideoEditor mEditor = null;
    ;
    ProgressDialog mProgressDialog;

    private String dstVideo = null;
    private String dstAudio = null;
    private boolean isRunning = false;
    private int demoID = 0;
    private int textID = 0;
    private boolean isOutVideo;
    private boolean isOutAudio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_cmd_layout);

        TextView tvText = (TextView) findViewById(R.id.id_test_cmd_demo_hint);

        findViewById(R.id.id_test_cmd_btn).setOnClickListener(this);
        findViewById(R.id.id_test_cmdvideo_play_btn).setOnClickListener(this);
        findViewById(R.id.id_test_cmdaudio_play_btn).setOnClickListener(this);

        srcVideo = getIntent().getStringExtra("videopath1");

        demoID = getIntent().getIntExtra("demoID", 0);
        isOutVideo = getIntent().getBooleanExtra("outvideo", false);
        isOutAudio = getIntent().getBooleanExtra("outaudio", false);
        textID = getIntent().getIntExtra("textID", 0);
        if (demoID != 0) {
            setTitle(demoID);
        }
        if (textID != 0) {
            tvText.setText(textID);
        }

        if (isOutVideo == false) {
            findViewById(R.id.id_test_cmdvideo_play_btn).setVisibility(View.GONE);
        }
        if (isOutAudio == false) {
            findViewById(R.id.id_test_cmdaudio_play_btn).setVisibility(View.GONE);
        }
        /**
         * 创建VideoEditor对象, 并设置进度监听
         */
        mEditor = new VideoEditor();
        mEditor.setOnProgessListener(new onVideoEditorProgressListener() {

            @Override
            public void onProgress(VideoEditor v, int percent) {
                if (mProgressDialog != null) {
                    mProgressDialog.setMessage("正在处理中..." + String.valueOf(percent) + "%");
                }
            }
        });
        mEditor.setOnEncodeChangedListener(new onVideoEditorEncodeChangedListener() {
            @Override
            public void onChanged(VideoEditor v, boolean isSoftencoder) {
                Toast.makeText(getApplicationContext(), "切换为软编码...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isRunning) {
            Log.e(TAG, "VideoEditor is running...cannot back!!! ");
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LanSongFileUtil.deleteFile(dstAudio);
        dstAudio = null;
        LanSongFileUtil.deleteFile(dstVideo);
        dstVideo = null;
        if (audioPlayer != null) {
            audioPlayer.stop();
            audioPlayer.release();
            audioPlayer = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_test_cmd_btn:
                if(demoID==R.string.demo_id_videocompress && !LSOVideoScale.isNeedCompress(srcVideo)){
                    DemoUtil.showHintDialog(AVEditorDemoActivity.this, "当前视频没有压缩的空间.");
                }else if (!isRunning) {
                        new SubAsyncTask().execute();  //开始VideoEditor方法的处理==============>
                }
                break;
            case R.id.id_test_cmdvideo_play_btn:
                playDstVideo();
                break;
            case R.id.id_test_cmdaudio_play_btn:
                playDstAudio();
                break;
            default:
                break;
        }
    }


    /**
     * 异步执行
     */
    public class SubAsyncTask extends AsyncTask<Object, Object, Boolean> {
        @Override
        protected void onPreExecute() {
            showProgressDialog();
            isRunning = true;
            super.onPreExecute();
        }

        @Override
        protected synchronized Boolean doInBackground(Object... params) {
            startRunDemoFunction();  //========>执行演示
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            calcelProgressDialog();

            isRunning = false;

            if (LanSongFileUtil.fileExist(dstVideo))
                findViewById(R.id.id_test_cmdvideo_play_btn).setEnabled(true);
        }
    }

    private int startRunDemoFunction() {
        LanSongFileUtil.deleteFile(dstAudio);
        LanSongFileUtil.deleteFile(dstVideo);
        int ret = -1;
        switch (demoID) {
            case R.string.demo_id_avmerge:  //合成音频/替换音频
                dstVideo = DemoFunctions.demoAVMerge(AVEditorDemoActivity.this, mEditor, srcVideo);
                break;
            case R.string.demo_id_cutaudio:  //剪切音频
                dstAudio = DemoFunctions.demoAudioCut(AVEditorDemoActivity.this, mEditor);
                break;
            case R.string.demo_id_cutvideo: //剪切视频
                dstVideo = DemoFunctions.demoVideoCut(mEditor, srcVideo);
                break;
            case R.string.demo_id_avsplit: //音视频分离.
                dstAudio = mEditor.executeGetAudioTrack(srcVideo);
                dstVideo = mEditor.executeGetVideoTrack(srcVideo);
                break;
            case R.string.demo_id_concatvideo:  //视频拼接
                dstVideo = DemoFunctions.demoVideoConcat(mEditor, srcVideo);
                break;
            case R.string.demo_id_videocompress:
                if(LSOVideoScale.isNeedCompress(srcVideo)){
                    dstVideo = DemoFunctions.demoVideoCompress(mEditor, srcVideo);
                }else{

                }

                break;
            case R.string.demo_id_videocrop:
                dstVideo = DemoFunctions.demoFrameCrop(mEditor, srcVideo);
                break;
            case R.string.demo_id_videoscale_soft:
                dstVideo = DemoFunctions.demoVideoScale(mEditor, srcVideo);
                break;
            case R.string.demo_id_videowatermark:
                dstVideo = DemoFunctions.demoAddPicture(AVEditorDemoActivity.this, mEditor, srcVideo);
                break;
            case R.string.demo_id_videocropwatermark:
                dstVideo = DemoFunctions.demoVideoCropOverlay(AVEditorDemoActivity.this, mEditor, srcVideo);
                break;
            case R.string.demo_id_videoclockwise90:
                dstVideo = mEditor.executeVideoRotate90Clockwise(srcVideo);
                break;
            case R.string.demo_id_videocounterClockwise90:
                dstVideo = mEditor.executeVideoRotate90CounterClockwise(srcVideo);
                break;
            case R.string.demo_id_videoaddanglemeta:
                dstVideo = mEditor.executeSetVideoMetaAngle(srcVideo, 270);
                break;
            case R.string.demo_id_audiodelaymix:
                dstAudio = DemoFunctions.demoAudioDelayMix(AVEditorDemoActivity.this, mEditor);
                break;
            case R.string.demo_id_audiovolumemix:
                dstAudio = DemoFunctions.demoAudioVolumeMix(AVEditorDemoActivity.this, mEditor);
                break;
            case R.string.demo_id_videopad:
                dstVideo = DemoFunctions.demoPaddingVideo(mEditor, srcVideo);
                break;
            case R.string.demo_id_videoadjustspeed:
                dstVideo = mEditor.executeAdjustVideoSpeed(srcVideo, 0.5f); //这里用0.5放慢一倍;
                break;
            case R.string.demo_id_videomirrorh:
                dstVideo = mEditor.executeVideoMirrorH(srcVideo);
                break;
            case R.string.demo_id_videomirrorv:
                dstVideo = mEditor.executeVideoMirrorV(srcVideo);
                break;
            case R.string.demo_id_videorotateh:
                dstVideo = mEditor.executeVideoRotateHorizontally(srcVideo);
                break;
            case R.string.demo_id_videorotatev:
                dstVideo = mEditor.executeVideoRotateVertically(srcVideo);
                break;
            case R.string.demo_id_videoreverse:
                dstVideo = mEditor.executeVideoReverse(srcVideo);
                break;
            case R.string.demo_id_avreverse:
                dstVideo = mEditor.executeAVReverse(srcVideo);
                break;
            default:
                break;
        }
        return ret;
    }

    private void playDstVideo() {
        if (LanSongFileUtil.fileExist(dstVideo)) {
            Intent intent = new Intent(AVEditorDemoActivity.this, VideoPlayerActivity.class);
            intent.putExtra("videopath", dstVideo);
            startActivity(intent);
        } else {
            Toast.makeText(AVEditorDemoActivity.this, "文件不存在,请看打印信息", Toast.LENGTH_SHORT).show();
        }
    }

    MediaPlayer audioPlayer = null;

    private void playDstAudio() {
        if (MediaInfo.isSupport(dstAudio) && audioPlayer == null) {
            audioPlayer = new MediaPlayer();
            try {
                audioPlayer.setDataSource(dstAudio);
                audioPlayer.prepare();
                audioPlayer.start();

                audioPlayer.setOnCompletionListener(new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        audioPlayer.release();
                        audioPlayer = null;
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(AVEditorDemoActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("正在处理中...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void calcelProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
            mProgressDialog = null;
        }
    }


}

