package com.example.commonDemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lansoeditor.demo.R;
import com.lansosdk.videoeditor.CopyFileFromAssets;
import com.lansosdk.videoeditor.LanSongFileUtil;
import com.lansosdk.videoeditor.VideoEditor;
import com.lansosdk.videoeditor.VideoLayout;
import com.lansosdk.videoeditor.onVideoEditorProgressListener;
import com.lansosdk.videoplayer.VPlayer;
import com.lansosdk.videoplayer.VideoPlayer;
import com.lansosdk.videoplayer.VideoPlayer.OnPlayerPreparedListener;

/**
 * 因手机端性能有限, 如果加上缩放, 裁剪, 会处理的很慢
 * 建议1: 如果用户选择本地视频, 则在选择的时候, 就裁剪和缩放好;
 * 建议2: 如果录制视频, 则录制时就选择好录制尺寸;
 * 从而避免不必要的耗时;
 *
 */
public class VideoLayoutDemoActivity extends Activity {

    private static final boolean VERBOSE = true;
    private static final String TAG = "VideoLayoutDemoActivity";

    private MediaPlayer mediaPlayer = null;
    private VPlayer vplayer1 = null;
    private VPlayer vplayer2 = null;
    private VPlayer vplayer3 = null;
    private VPlayer vplayer4 = null;

    private TextureView texture1;
    private TextureView texture2;
    private TextureView texture3;
    private TextureView texture4;


    private LinearLayout  linearLayout;

    private String video1;
    private String video2;
    private String video3;
    private String video4;


    private String dstPath;
    private Button btnNext;
    VideoLayout layout=new VideoLayout();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_video_layout);


        btnNext=(Button)findViewById(R.id.id_videolayout_nextbtn);

        texture1 = (TextureView) findViewById(R.id.id_test_texture1);
        texture2 = (TextureView) findViewById(R.id.id_test_texture2);
        texture3 = (TextureView) findViewById(R.id.id_test_texture3);
        texture4 = (TextureView) findViewById(R.id.id_test_texture4);

        linearLayout=(LinearLayout)findViewById(R.id.id_morevideo_linearlayout);


        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();

        params.height = (int) (params.width*16.0f /9.0f); //改变为9:16的画面;
        linearLayout.setLayoutParams(params);
        linearLayout.invalidate();// 刷新一下.

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(isRunning==false){
                        releaseVPlayer();
                        new SubAsyncTask().execute();
                    }
            }
        });
        texture1.setSurfaceTextureListener(new SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
                                                    int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                video1= CopyFileFromAssets.copyAssets(getApplicationContext(),"dd1.mp4");
                startVPlayer1(new Surface(surface),video1);
            }
        });
        texture2.setSurfaceTextureListener(new SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
                                                    int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                video2= CopyFileFromAssets.copyAssets(getApplicationContext(),"dd2.mp4");
                startVPlayer2(new Surface(surface),video2);
            }
        });
        texture3.setSurfaceTextureListener(new SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
                                                    int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                video3= CopyFileFromAssets.copyAssets(getApplicationContext(),"dd3.mp4");
                startVPlayer3(new Surface(surface),video3);
            }
        });
        texture4.setSurfaceTextureListener(new SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
                                                    int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                video4= CopyFileFromAssets.copyAssets(getApplicationContext(),"dd4.mp4");
                startVPlayer4(new Surface(surface),video4);
            }
        });

        layout.setOnProgessListener(new onVideoEditorProgressListener() {
            @Override
            public void onProgress(VideoEditor v, int percent) {
                if(mProgressDialog!=null){
                    mProgressDialog.setMessage("当前进度"+ percent+ " %");
                }
            }
        });
    }

    private void startVPlayer1(final Surface surface,String video) {
        vplayer1 = new VPlayer(this);
        vplayer1.setVideoPath(video);
        vplayer1.setOnPreparedListener(new OnPlayerPreparedListener() {

            @Override
            public void onPrepared(VideoPlayer mp) {
                vplayer1.setSurface(surface);
                vplayer1.start();
            }
        });
        vplayer1.prepareAsync();
    }
    private void startVPlayer2(final Surface surface,String video) {
        vplayer2 = new VPlayer(this);
        vplayer2.setVideoPath(video);
        vplayer2.setOnPreparedListener(new OnPlayerPreparedListener() {

            @Override
            public void onPrepared(VideoPlayer mp) {
                vplayer2.setSurface(surface);
                vplayer2.start();
            }
        });
        vplayer2.prepareAsync();
    }
    private void startVPlayer3(final Surface surface,String video) {
        vplayer3 = new VPlayer(this);
        vplayer3.setVideoPath(video);
        vplayer3.setOnPreparedListener(new OnPlayerPreparedListener() {

            @Override
            public void onPrepared(VideoPlayer mp) {
                vplayer3.setSurface(surface);
                vplayer3.start();
            }
        });
        vplayer3.prepareAsync();
    }
    private void startVPlayer4(final Surface surface,String video) {
        vplayer4 = new VPlayer(this);
        vplayer4.setVideoPath(video);
        vplayer4.setOnPreparedListener(new OnPlayerPreparedListener() {

            @Override
            public void onPrepared(VideoPlayer mp) {
                vplayer4.setSurface(surface);
                vplayer4.start();
            }
        });
        vplayer4.prepareAsync();
    }


    @Override
    protected void onPause() {
        super.onPause();
        releaseVPlayer();
    }
    private  void releaseVPlayer()
    {
        if (vplayer1 != null) {
            vplayer1.stop();
            vplayer1.release();
            vplayer1 = null;
        }
        if (vplayer2 != null) {
            vplayer2.stop();
            vplayer2.release();
            vplayer2 = null;
        }
        if (vplayer3 != null) {
            vplayer3.stop();
            vplayer3.release();
            vplayer3 = null;
        }
        if (vplayer4 != null) {
            vplayer4.stop();
            vplayer4.release();
            vplayer4 = null;
        }
    }

//--------------------开始后台执行;
ProgressDialog  mProgressDialog;
    private boolean isRunning=false;
    public class SubAsyncTask extends AsyncTask<Object, Object, Boolean> {
    @Override
    protected void onPreExecute() {
        showProgressDialog();
        isRunning=true;
        releaseVPlayer();
        super.onPreExecute();
    }
    @Override
    protected synchronized Boolean doInBackground(Object... params) {
        dstPath=layout.executeLayout4Video(540,960,
                video1,0,0,
                video2,270,0,
                video3,0,480,
                video4,270,480);
        return null;
    }
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        calcelProgressDialog();

        isRunning=false;

        if(LanSongFileUtil.fileExist(dstPath)){
            showHintDialog();
        }
    }
}
    private void showProgressDialog()
    {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("正在处理中...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    private void calcelProgressDialog()
    {
        if( mProgressDialog!=null){
            mProgressDialog.cancel();
            mProgressDialog=null;
        }
    }

    private void showHintDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("视频已经生成.开始预览")
                .setPositiveButton("预览", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(VideoLayoutDemoActivity.this,VideoPlayerActivity.class);
                        intent.putExtra("videopath", dstPath);
                        startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
