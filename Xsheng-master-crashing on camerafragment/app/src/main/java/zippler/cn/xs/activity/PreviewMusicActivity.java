package zippler.cn.xs.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import VideoHandle.EpDraw;
import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;
import zippler.cn.xs.R;
import zippler.cn.xs.adapter.RecyclerMusicAdapter;
import zippler.cn.xs.listener.OnPageChangedListener;
import zippler.cn.xs.util.FFmpegEditor;
import zippler.cn.xs.util.FileUtil;
import zippler.cn.xs.util.PagingScrollHelper;

/**
 * add bgm there
 */
public class PreviewMusicActivity extends BaseActivity {

    private ImageView back;
    private TextView nextStep;
    private VideoView video;
    private ImageView playBtn;
    private ImageView loading;
    private RelativeLayout guideLayout;
    private RecyclerView musics;


    private ArrayList<String> data;
    private int currentPosition = 0;
    private String out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_music);

        initViews();
        registerListeners();
        initDatas();
        initRecyclerView();
    }

    private void initViews(){
        back = findViewById(R.id.back_preview_m);
        nextStep = findViewById(R.id.next_step_after_preview_m);
        video = findViewById(R.id.video_m);
        playBtn = findViewById(R.id.play_btn_m);
        guideLayout = findViewById(R.id.guide_layout);
        musics = findViewById(R.id.musics);
        loading = findViewById(R.id.c_loading_logo);

        if (isFirstIn()){
            guideLayout.setVisibility(View.GONE);
        }

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                video.start();
            }
        });
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video.start();
            }
        });
    }

    private void registerListeners(){
        back.setOnClickListener(this);
        nextStep.setOnClickListener(this);
        video.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        guideLayout.setOnClickListener(this);
    }

    private void removeListeners(){
        nextStep.setOnClickListener(null);
        video.setOnClickListener(null);
        playBtn.setOnClickListener(null);
        guideLayout.setOnClickListener(null);
    }

    private void initDatas(){
        //get intent
        data = getIntent().getStringArrayListExtra("videoPaths");
        Log.d(TAG, "initDatas: data size = "+data.size());
    }

    private void initRecyclerView(){
        LinearLayoutManager linerLayoutManager = new LinearLayoutManager(this);
        linerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        musics.setLayoutManager(linerLayoutManager);

        PagingScrollHelper helper = new PagingScrollHelper();//set scrolled horizontal
        helper.setUpRecycleView(musics);
        //set page changed listener
        helper.setOnPageChangedListener(new OnPageChangedListener() {
            @Override
            public void onChanged(int position) {
                Log.d(TAG, "onChanged: page changed!!!!  "+position);
                 //change video here
                if (position<data.size()-1&&position>=0){
                    video.setVideoPath(data.get(position));
                    currentPosition = position;
                }
            }
        });

        RecyclerMusicAdapter adapter = new RecyclerMusicAdapter(this,data);
        musics.setAdapter(adapter);

        if (guideLayout.getVisibility()==View.GONE){
           //play the first one
            if (data.size()>0){
                video.setVideoPath(data.get(0));
            }
        }
    }

    private void putSharedData(){
        SharedPreferences sp = getSharedPreferences("firstIn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("isFirstIn", "false");
        editor.commit();
    }

    private String getSharedData(){
        SharedPreferences sp = getSharedPreferences("firstIn", Context.MODE_PRIVATE);
        return sp.getString("isFirstIn","true");
    }

    private boolean isFirstIn(){
        if (getSharedData().equals("false")){
            return false;
        }else{
            putSharedData();
            return false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.guide_layout:
                guideLayout.setVisibility(View.GONE);
                //play the video
                if (data.size()>0){
                    video.setVideoPath(data.get(0));
                }
                break;
            case R.id.next_step_after_preview_m:
                gotoDeploy();
                break;
            default:
                break;
        }
    }

    private void addLogo(String in,OnEditorListener listener){
        String logo = FileUtil.getCamera2Path()+"logo.png";//should be loaded from internet.

        EpDraw epDraw = new EpDraw(logo,30,1630,100,100,false);
        EpVideo epVideo = new EpVideo(in);
        epVideo.addDraw(epDraw);

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        out = FileUtil.getCamera2Path()+"deploy"+File.separator+"py_logo_"+timeStamp+".mp4";


        EpEditor.OutputOption outputOption = new EpEditor.OutputOption(out);
        outputOption.frameRate = 60;
        outputOption.bitRate = 5*1024*1024;
        EpEditor.exec(epVideo, outputOption,listener);
    }

    private void addText(String in,OnEditorListener listener){
        String ttf = FileUtil.getCamera2Path()+"heart.ttf";
        EpVideo epVideo = new EpVideo(in);
        epVideo.addText(850,1780,88,"white",ttf,"形声");

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        out = FileUtil.getCamera2Path()+"deploy"+File.separator+"py_logo_"+timeStamp+".mp4";

        FFmpegEditor.OutputOption outputOption = new FFmpegEditor.OutputOption(out);
        outputOption.frameRate = 60;
        outputOption.bitRate = 5*1024*1024;
        FFmpegEditor.exec(epVideo, outputOption,listener);
    }

    private void gotoDeploy(){
        video.pause();

        Intent intent = new Intent(this,DeployActivity.class);

        //move to deploy
        final String result;
        String deploy = FileUtil.move2Folders(data.get(currentPosition),FileUtil.getCamera2Path()+"deploy"+ File.separator);
        if (deploy!=null&&!deploy.equals("")){
            result = deploy;
        }else{
            Log.e(TAG, "gotoDeploy: error in move files.." );
            result = data.get(currentPosition);
        }

        video.pause();
        loading.setVisibility(View.VISIBLE);

        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.loading);
        operatingAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        loading.startAnimation(operatingAnim);

        removeListeners();

        intent.putExtra("videoPath",result);
        startActivity(intent);
        finish();//not to back...because of this video maybe deleted.

        /*addText(result, new OnEditorListener() {
            @Override
            public void onSuccess() {
                //delete the old file
                File file = new File(result);
                if (file.delete()){
                    Log.d(TAG, "addText: delete successful");
                }
                intent.putExtra("videoPath",out);
                startActivity(intent);
                finish();//not to back...because of this video maybe deleted.
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "onFailure: add text failed" );
            }

            @Override
            public void onProgress(float v) {

            }
        });*/

/*
//      may be add text is more useful...
        addLogo(result, new OnEditorListener() {
            @Override
            public void onSuccess() {
                intent.putExtra("videoPath",out);
                startActivity(intent);
                finish();//not to back...because of this video maybe deleted.
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "onFailure: add logo failed" );
            }

            @Override
            public void onProgress(float v) {
            }
        });
*/

    }




    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //pause the video
        if (video!=null){
            video.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //restart
        if (video!=null){
            video.start();
        }
    }

}
