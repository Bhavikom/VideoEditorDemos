package com.example.commonDemo.advanceSuggest;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import com.lansoeditor.demo.R;
import com.lansosdk.videoeditor.CopyFileFromAssets;
import com.lansosdk.videoplayer.TextureRenderView;

import java.io.IOException;

import static com.lansosdk.videoeditor.CopyFileFromAssets.copyAssets;

public class PlayAdvanceDemoActivity extends Activity{
    MediaPlayer mediaPlayer;
    private int screenWidth,screenHeight;
    public static final int AE_DEMO = 101;
    public static final int SEEK_DEMO = 102;
    public static final int LANTIANBAIYUN = 103;
    public static final int HUANNIAN_QINGCHUN = 104;
    public static final int DOUYIN_EFFECT = 105;



    TextureRenderView textureView;
    String video;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.ae_play_aedemofile_layout);

        int inputType = getIntent().getIntExtra("PlayType", AE_DEMO);


        if (inputType == AE_DEMO) {
            video= copyAssets(getApplicationContext(),"aeDemo.mp4");
        }else if(inputType==SEEK_DEMO){
            video= copyAssets(getApplicationContext(),"seek_exact.mp4");
        }else if(inputType==LANTIANBAIYUN) {
            video= copyAssets(getApplicationContext(),"lantianbaiyun.mp4");
        }else if(inputType == HUANNIAN_QINGCHUN) {
            video= copyAssets(getApplicationContext(),"huainian.mp4");
        }else if(inputType==DOUYIN_EFFECT) {
            video= copyAssets(getApplicationContext(),"douyin_effect.mp4");
        }else{
            video= copyAssets(getApplicationContext(),"aeDemo.mp4");
        }



        textureView=(TextureRenderView)findViewById(R.id.id_aedemo_textureview);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                play(new Surface(surfaceTexture));
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
    }
    private void play(Surface surface)  {

        if(video==null)
            return ;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();


        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(PlayAdvanceDemoActivity.this, "视频播放完毕!", Toast.LENGTH_SHORT).show();
            }
        });

        try {
            mediaPlayer.setDataSource(video);
            mediaPlayer.setSurface(surface);
            mediaPlayer.prepare();

            //因为是竖屏.宽度小于高度.
            if(screenWidth>mediaPlayer.getVideoWidth()){
                textureView.setDispalyRatio(TextureRenderView.AR_ASPECT_WRAP_CONTENT);

            }else{  //大于屏幕的宽度
                textureView.setDispalyRatio(TextureRenderView.AR_ASPECT_FIT_PARENT);
            }
            textureView.setVideoSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
            textureView.requestLayout();

            mediaPlayer.start();
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
}
