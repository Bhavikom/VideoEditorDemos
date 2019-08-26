package zippler.cn.xs.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Jni.VideoUitls;
import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import zippler.cn.xs.R;
import zippler.cn.xs.component.BarWavesView;
import zippler.cn.xs.component.MediaController;
import zippler.cn.xs.entity.Music;
import zippler.cn.xs.listener.CombinedOnEditorListener;
import zippler.cn.xs.util.ColorUtil;
import zippler.cn.xs.util.FFmpegEditor;
import zippler.cn.xs.util.FileUtil;

public class GuideRecorderActivity extends BaseActivity implements TextureView.SurfaceTextureListener{

    //views
    private TextureView preview;
    //    private ImageView back;
    private TextView changeMode;
    private ImageView exposure;
    private ImageView reverse;
    private ImageView recordBtn;
    private TextView nextStep;
    private ProgressBar record_circle_progress;
    private ProgressBar record_line_progress;
    private ImageView pauseBtn;
    private BarWavesView barWavesView;
    private MediaController controller;


    private SurfaceTexture surface;

    //about record videos
    private Camera camera;
    private Camera.Parameters parameters;
    private MediaPlayer music;
    private MediaRecorder mediaRecorder;
    private String savedVideoPath;
    private List<String> old = new ArrayList<>();//old path
    private List<String> outFiles = new ArrayList<>();
    private int backCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
    private int frontCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private boolean isLightOn;
    private boolean isBackCameraOn ;
    private boolean isRecordOn = false;
    private boolean isFirstRecord = false;

    private float oldDist =1f;
    private static final int DURATION = 20000 ;//set max video duration

    //listener
    private VideoCaptureDurationListener durationListener ;
    private CombinedOnEditorListener editorListener;

    //timer
    private Handler handler;
    private Runnable runnable;
    private double time = 20;//15s
    private int progress = -1;
    private boolean isCombined = false;//combined video progress

    private String musicPath;

    private String combinedPath;
    private String outputFile;//attach music

    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_record);
        musicPath = getIntent().getStringExtra("music");
        Log.e(TAG, "onCreate: music path = "+musicPath);

        initViews();
        registerListeners();

        handler = new Handler();
    }

    private void initViews(){
        preview = findViewById(R.id.preview_c);
        changeMode = findViewById(R.id.change_mode_c);
        exposure = findViewById(R.id.exposure_c);
        reverse = findViewById(R.id.camera_id_c);
        recordBtn = findViewById(R.id.record_btn_c);
        nextStep = findViewById(R.id.next_step_c);
        record_circle_progress = findViewById(R.id.record_progress_c);
        record_line_progress = findViewById(R.id.record_line_progress_c);
        record_line_progress.setMax(30);

        pauseBtn = findViewById(R.id.pause_btn_c);

        barWavesView = findViewById(R.id.music_guide);
        barWavesView.setBarColor(ColorUtil.getRandomColor()); // ColorUtils.getRandomColor() 获得一个随机颜色

        /*int[][] cs = new int[barWavesView.getWaveNumber()][2];
        for (int i = 0; i < cs.length; i++) {
            // 控件允许给每一条波浪条单独设置颜色，这两个颜色将以纵向渐变的形式被绘制
            cs[i][0] = ColorUtil.getRandomColor();
            cs[i][1] = ColorUtil.getRandomColor();
        }
        barWavesView.setWaveColor(cs);*/
        Log.e(TAG, "initViews: 初始化音频可视化");
        Music music = new Music();
        music.setLocalStorageUrl(musicPath);//set music path;
        controller = new MediaController(this,music);
        controller.setupVisualizer(barWavesView.getWaveNumber(), 50, new MediaController.onFftDataCaptureListener() {
            @Override
            public void onFftCapture(float[] fft) {
                barWavesView.setWaveHeight(fft);
            }
        });
        controller.setVisualizerEnable(true);
    }

    private void registerListeners(){
        preview.setSurfaceTextureListener(this);
        changeMode.setOnClickListener(this);
        exposure.setOnClickListener(this);
        reverse.setOnClickListener(this);
        recordBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);

        addTouchListeners();
    }


    private void addTouchListeners() {

        preview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount()==1){
                    touchFocus(event);
                }else {
                    changeZoom(event);
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_mode_c:
                //change record type.
                if (isRecordOn){ //oom
                    stop();
                }
                Intent intent = new Intent(this,MusicChooseActivity.class);
                startActivity(intent);
                break;
            case R.id.exposure_c:
                switchFlash();
                break;
            case R.id.camera_id_c:
                switchCamera();
                break;
            case R.id.record_btn_c:
                nextStep.setOnClickListener(this);
                nextStep.setBackgroundResource(R.drawable.pink_background);
                startRecord();
                break;
            case R.id.next_step_c:
                if (isRecordOn){ //oom
                    stop();
                }
                pauseTimer();
                gotoPreview();
                break;
            case R.id.pause_btn_c:
                //waiting for combine video
                //pause and
                pause();
                pause();
                break;
            default:
                Log.d(TAG, "onClick: default clicked");
                break;
        }
    }

    /**
     *  about life cycle
     *  open camera and release resource.
     */

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();//finished this activity! there is no back page
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.releaseMediaPlayer();
    }

    /**
     * About when to open camera
     */

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.surface = surface;
        openCamera(backCamera);
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        this.surface = surface;
        openCamera(backCamera);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        //release resources
        camera.stopPreview();
        camera.release();
        if (mediaRecorder!=null){
            mediaRecorder.release();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    /**
     * about how to use camera
     */



    /**
     * open camera
     * @param position which camera
     */
    private void openCamera(int position) {
        isBackCameraOn = (position == Camera.CameraInfo.CAMERA_FACING_BACK);

        camera = Camera.open(position);
        if (parameters==null){
            parameters = camera.getParameters();
            isLightOn = false;
            //set auto focused , There should be a clicking focus feature here
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            parameters.setPreviewSize(1920,1080);//choose the right size of phone.
        }


        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);//but the front camera is mirror
        try {
            camera.setPreviewTexture(surface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        Log.d(TAG, "openCamera: start preview");
    }

    private void startRecord(){
        isFirstRecord = true;
        if (!isRecordOn){
            if (progress!=-1&&progress>0&&progress<15){
                Log.d(TAG, "pause: continueRecord camera");
                pauseBtn.setImageResource(R.mipmap.pause);
                record_circle_progress.setVisibility(View.VISIBLE);
                continueRecord();
                continueTimer();
            }else{
                record_circle_progress.setVisibility(View.VISIBLE);
                record_line_progress.setVisibility(View.VISIBLE);
                recordBtn.setImageResource(R.mipmap.stop);
                recordBtn.setImageResource(R.mipmap.record);
                startTimer();//begin
                record();
            }
        }else{
            stop();
            pauseTimer();
            gotoPreview();
        }
    }

    /**
     * started the timer
     */
    private void startTimer(){
        Log.d(TAG, "startTimer: ");
//        runnable = new RecordTimerRunnable(progress,time);
//        runnable.setHandler(handler);
//        runnable.setProgressBar(record_line_progress);
        runnable = new Runnable() {
            @Override
            public void run() {
                time =time-0.5;
                if (time>0){
                    progress++;
                    record_line_progress.setProgress(progress);
                    if (time==10.5){
                        controller.stop();//pause
                    }
                    handler.postDelayed(this,500);
                }else{
                    //stop
                    handler.removeCallbacks(this);
                }
            }
        };
        handler.postDelayed(runnable,0);
    }

    /**
     * stop the timer
     */
    private void pauseTimer(){
        if (runnable!=null){
//            progress = runnable.getProgress();//get current progress
//            time = runnable.getTime();//get current remained time;
            handler.removeCallbacks(runnable);
            runnable = null;
            Log.d(TAG, "pauseTimer: pause timer");
        }
    }

    /**
     * continue the timer
     */
    private void continueTimer(){
        //if the timing is over.
        startTimer();
    }

    private void prepare(){
        mediaRecorder = new MediaRecorder();
        camera.lock();
        camera.unlock();

        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

        mediaRecorder.setVideoSize(1920, 1080);
        mediaRecorder.setVideoEncodingBitRate(5*1024*1024);
        mediaRecorder.setVideoFrameRate(60);

        if (isBackCameraOn){
            mediaRecorder.setOrientationHint(90);
        }else{
            mediaRecorder.setOrientationHint(270);
        }

        //set record lengths here.
        mediaRecorder.setMaxDuration(DURATION);
        durationListener = new VideoCaptureDurationListener();
        mediaRecorder.setOnInfoListener(durationListener);
    }

    private void record() {
        prepare();
        playMusic(R.raw.di);
        controller.play();
        isRecordOn = true;//fixed camera unlock failed
        reverse.setVisibility(View.GONE);//maybe invisible



        //save video
        String root = FileUtil.getCamera2Path();
        FileUtil.createSavePath(root);
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        savedVideoPath = root + "形声_" + timeStamp + ".mp4";
        mediaRecorder.setOutputFile(savedVideoPath);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();

    }


    /**
     * stop record
     */
    private void stop() {
        Log.d(TAG, "stop: stop record videos");
        isRecordOn = false;
        record_circle_progress.setVisibility(View.INVISIBLE);
        playMusic(R.raw.po);
        recordBtn.setImageResource(R.mipmap.record);
        reverse.setVisibility(View.VISIBLE);

        controller.stop();

        camera.lock();
        mediaRecorder.stop();
        mediaRecorder.release();
    }

    private void gotoPreview(){
        Log.d(TAG, "stop: jump to preview");
//        Intent intent = new Intent(this,PreviewActivity.class);
        old.add(savedVideoPath);
        //然后调用视频变速方法，最后合成
        String rootPath = FileUtil.getCamera2Path()+"videoCache/";
        editorListener = new CombinedOnEditorListener(old,isCombined);
        //音乐在第10秒的时候停止  视频继续录制  点击继续播放音乐 直到完成录制
        double videoDuration = VideoUitls.getDuration(old.get(0))/1000000.0;
        float speed = (float) (videoDuration/10.0);
        tts(old, rootPath, speed);//计算变速倍速;

        //load a loading dialog
        dialog =  ProgressDialog.show(this,"合成中...","请稍后...",true);
        final Handler timerHandler = new Handler();

        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isCombined = editorListener.isFinished();
                if (isCombined){

                    Log.d(TAG, "gotoPreview: waiting for combined videos");
                    Log.d(TAG, "tts: 再添加背景音乐");
                    attachBgm(combinedPath);

                   /* dialog.dismiss();
                    Intent intent = new Intent(GuideRecorderActivity.this,PreviewActivity.class);
                    intent.putExtra("videoPath",combinedPath);
                    startActivity(intent);
                    controller.releaseMediaPlayer();*/
                }else{
                    timerHandler.postDelayed(this,500);
                }
            }
        },500);
    }

    /**
     * pause for record video
     */
    private void pause(){
        if (isFirstRecord){
            if(isRecordOn){
                Log.d(TAG, "pause: camera");
                pauseBtn.setImageResource(R.mipmap.play_white);
                record_circle_progress.setVisibility(View.INVISIBLE);
                pauseTimer();
                stop();
            }else{
                Log.d(TAG, "pause: continueRecord camera");
                pauseBtn.setImageResource(R.mipmap.pause);
                record_circle_progress.setVisibility(View.VISIBLE);
                continueRecord();
                continueTimer();
            }
        }
    }

    /**
     * combined the old video and the new video .
     */
    private void continueRecord(){
        old.add(savedVideoPath) ;
        Log.d(TAG, "continueRecord: begin to record again");
        record();
    }

    /**
     * upload the videos to xserver.
     */
    private void upload2tts(String userId, List<File>files, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30,TimeUnit.SECONDS).build();
        MultipartBody.Builder requestBodyBuilder =new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        int i = 0;
        for (File temp:files) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("video/mp4"), temp);

            requestBodyBuilder.addFormDataPart("file"+i, temp.getName(), fileBody)
                    .addFormDataPart("userId", userId);
            i++;
        }
        RequestBody requestBody = requestBodyBuilder.build();
        Request request = new Request.Builder()
                .url("http://127.79.237.105/xserver/upload")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * waiting for this videos
     * @param paths the old videos
     * @return the combined video path
     */
    private String combinedVideos(List<String> paths){
        String root = FileUtil.getCamera2Path();
        FileUtil.createSavePath(root);
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String outFile = root + "合成_" + timeStamp + ".mp4";
        ArrayList<EpVideo> epVideos = new ArrayList<>();
        epVideos.clear();
        for (String temp :paths) {

            Log.d(TAG, "combinedVideos: current = "+temp);
            epVideos.add(new EpVideo(temp));
        }
//        FFmpegEditor.mergeByLc(root, epVideos, new FFmpegEditor.OutputOption(outFile), editorListener);
        FFmpegEditor.merge(epVideos, new FFmpegEditor.OutputOption(outFile), editorListener);
        return outFile;
    }


    private void tts(final List<String> filePath, final String rootPath, float speed){
        Log.d(TAG, "tts: 视频压缩开始，压缩倍速："+speed);
        if(filePath==null || filePath.size()==0){
            Log.d(TAG, "tts: 当前已经完成所有视频的压缩过程");
            if (outFiles.size()>1){
                Log.d(TAG, "tts: 输出视频数量："+outFiles.size());
                Log.d(TAG, "tts: 准备视频片段合成");
                combinedPath = combinedVideos(outFiles);//这里进行压缩合成...并且跳转到下一个activity 要重写
            }else{
                Log.d(TAG, "tts: 当前无多个视频片段，无需合成，直接跳转预览");
                Intent intent = new Intent(GuideRecorderActivity.this,PreviewActivity.class);
                intent.putExtra("videoPath",outFiles.get(outFiles.size()-1));
                startActivity(intent);
                dialog.dismiss();
                controller.releaseMediaPlayer();
            }
            return;
        }
        int size = filePath.size();
        String videoPath = filePath.remove(0);
        Log.d(TAG, "tts: filePath size = "+size +"  current file is "+videoPath);
        //还是使用时间戳
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        final String outfile = rootPath+"ys_"+timeStamp+".mp4";
        Log.d(TAG, "tts: out file:"+outfile);

        EpEditor.changePTS(videoPath, outfile, speed, EpEditor.PTS.ALL, new OnEditorListener() {
            @Override
            public void onSuccess() {
                outFiles.add(outfile);
                Log.d(TAG, "onSuccess: 继续压缩");
                tts(filePath,rootPath,1.0f);//只变速第一个
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "onFailure: 视频压缩错误！" );
                tts(filePath,rootPath,1.0f);
            }

            @Override
            public void onProgress(float progress) {

            }
        });
    }

    private  void attachBgm(String videoIn){
        String audioin = FileUtil.getCamera2Path()+"duang.mp3";
        String cache = "videoCache"+File.separator;
        FileUtil.createSavePath(FileUtil.getCamera2Path()+cache);
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        outputFile= FileUtil.getCamera2Path()+cache+"ys_"+timeStamp+".mp4";
        FFmpegEditor.music(videoIn, audioin, outputFile, 0, 1.0f, new OnEditorListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: 音频合成完成");
                Intent intent = new Intent(GuideRecorderActivity.this,PreviewActivity.class);
                intent.putExtra("videoPath",outputFile);
                startActivity(intent);
                dialog.dismiss();
                controller.releaseMediaPlayer();
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onProgress(float v) {

            }
        });
    }


    private void switchCamera(){
        releaseCamera();
        if (isBackCameraOn){
            openCamera(frontCamera);
        }else{
            openCamera(backCamera);
        }
    }

    private void releaseCamera(){
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private void switchFlash(){
        if (parameters!=null){
            if (!isLightOn){
                exposure.setImageResource(R.mipmap.exposure);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                isLightOn = true;
                camera.setParameters(parameters);
            }else{
                exposure.setImageResource(R.mipmap.flash_off);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                isLightOn = false;
                camera.setParameters(parameters);
            }
        }
    }

    /**
     * scaling
     * @param event touch event
     */
    private void changeZoom(MotionEvent event){
        Log.d(TAG, "changeZoom: scaling");
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = getFingerSpacing(event);
                break;
            case MotionEvent.ACTION_MOVE:
                float newDist = getFingerSpacing(event);
                if (newDist >1.1 * oldDist) {
                    handleZoom(true);
                } else if (newDist < oldDist) {
                    handleZoom(false);
                }
                oldDist = newDist;
                break;
        }
    }

    private void handleZoom(boolean isZoomIn) {
        Camera.Parameters params = camera.getParameters();
        if (params.isZoomSupported()) {
            int maxZoom = params.getMaxZoom();
            int zoom = params.getZoom();
            if (isZoomIn && zoom < maxZoom) {
                zoom++;
            } else if (zoom > 0) {
                zoom--;
            }
            params.setZoom(zoom);
            //there should be set the corresponding clarity
            camera.setParameters(params);
        } else {
            Log.i(TAG, "zoom not supported");
        }
    }

    private void touchFocus(MotionEvent event){

        Log.d(TAG, "touchFocus: 点击，对焦区域");

        Camera.Parameters params = camera.getParameters();
        Camera.Size previewSize = params.getPreviewSize();
        Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f, previewSize);

        camera.cancelAutoFocus();
        if (params.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(focusRect, 800));
            params.setFocusAreas(focusAreas);
        } else {
            Log.i(TAG, "focus areas not supported");
        }

        Rect meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f, previewSize);
        if (params.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<>();
            meteringAreas.add(new Camera.Area(meteringRect, 800));
            params.setMeteringAreas(meteringAreas);
        } else {
            Log.i(TAG, "metering areas not supported");
        }

        final String currentFocusMode = params.getFocusMode();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
        camera.setParameters(params);

        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                Camera.Parameters params = camera.getParameters();
                params.setFocusMode(currentFocusMode);
                camera.setParameters(params);
            }
        });
    }


    /**
     * listener
     */

    private class VideoCaptureDurationListener implements MediaRecorder.OnInfoListener{
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            if(what==MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
                Log.d("DurationListener", "Maximum Duration Reached");
                stop();
                pauseTimer();
                gotoPreview();
            }
        }
    }


    /**
     * about utils function
     */

    private void playMusic(int musicId) {
        music = MediaPlayer.create(this, musicId);
        music.start();
        music.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                music.release();
            }
        });
    }


    private Rect calculateTapArea(float x, float y, float coefficient, Camera.Size previewSize) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / previewSize.width - 1000);
        int centerY = (int) (y / previewSize.height - 1000);

        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);

        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);

        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private  int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }


    private  float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private abstract class OnTtsSuccessListner{
        public abstract boolean isFinished();
    }

}
