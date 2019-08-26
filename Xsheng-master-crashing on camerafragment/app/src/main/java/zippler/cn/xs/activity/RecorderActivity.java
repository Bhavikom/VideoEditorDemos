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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import VideoHandle.EpVideo;
import zippler.cn.xs.R;
import zippler.cn.xs.handler.RecordTimerRunnable;
import zippler.cn.xs.listener.CombinedOnEditorListener;
import zippler.cn.xs.util.FFmpegEditor;
import zippler.cn.xs.util.FileUtil;

public class RecorderActivity extends BaseActivity implements TextureView.SurfaceTextureListener{

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

    private SurfaceTexture surface;

    //about record videos
    private Camera camera;
    private Camera.Parameters parameters;
    private MediaPlayer music;
    private MediaRecorder mediaRecorder;
    private String savedVideoPath;
    private List<String> old = new ArrayList<>();//old path
    private int backCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
    private int frontCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private boolean isLightOn;
    private boolean isBackCameraOn ;
    private boolean isRecordOn = false;
    private boolean isFirstRecord = false;

    private float oldDist =1f;
    private static final int DURATION = 15000 ;//set max video duration

    //listener
    private VideoCaptureDurationListener durationListener ;
    private CombinedOnEditorListener editorListener;

    //timer
    private Handler handler;
    private RecordTimerRunnable runnable;
    private int time = 15;//15s
    private int progress = -1;
    private boolean isCombined = false;//combined video progress

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);


        initViews();
        registerListeners();

        handler = new Handler();
    }

    private void initViews(){
        preview = findViewById(R.id.preview);
//        back = findViewById(R.id.back);
        changeMode = findViewById(R.id.change_mode);
        exposure = findViewById(R.id.exposure);
        reverse = findViewById(R.id.camera_id);
        recordBtn = findViewById(R.id.record_btn);
        nextStep = findViewById(R.id.next_step);
        record_circle_progress = findViewById(R.id.record_progress);
        record_line_progress = findViewById(R.id.record_line_progress);
        pauseBtn = findViewById(R.id.pause_btn);
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
            case R.id.change_mode:
                //change record type.
                Intent intent = new Intent(this,MusicChooseActivity.class);
                startActivity(intent);
                break;
            case R.id.exposure:
                switchFlash();
                break;
            case R.id.camera_id:
                switchCamera();
                break;
            case R.id.record_btn:
                nextStep.setOnClickListener(this);
                nextStep.setBackgroundResource(R.drawable.pink_background);
                startRecord();
                break;
            case R.id.next_step:
                if (isRecordOn){ //oom
                    stop();
                }
                gotoPreview();
                break;
            case R.id.pause_btn:
                //waiting for combine video
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
            gotoPreview();
        }
    }

    /**
     * started the timer
     */
    private void startTimer(){
        Log.d(TAG, "startTimer: ");
        runnable = new RecordTimerRunnable(progress,time);
        runnable.setHandler(handler);
        runnable.setProgressBar(record_line_progress);
        handler.postDelayed(runnable,1000);
    }

    /**
     * stop the timer
     */
    private void pauseTimer(){
        progress = runnable.getProgress();//get current progress
        time = runnable.getTime();//get current remained time;
        handler.removeCallbacks(runnable);
        runnable = null;
        Log.d(TAG, "pauseTimer: pause timer");
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

        camera.lock();
        mediaRecorder.stop();
        mediaRecorder.release();
    }

    private void gotoPreview(){
        final String combinedPath;
        Log.d(TAG, "stop: jump to preview");
        Intent intent = new Intent(this,PreviewActivity.class);
        old.add(savedVideoPath);
        if (old.size()>1){
            combinedPath = combinedVideos(old);
            Log.d(TAG, "gotoPreview: waiting for combined videos");
            //load a loading dialog
            final ProgressDialog dialog = ProgressDialog.show(this,"合成中...","请稍后...",true);
            final Handler timerHandler = new Handler();
            timerHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isCombined = editorListener.isFinished();
                    if (isCombined){
                        dialog.dismiss();
                        Intent intent = new Intent(RecorderActivity.this,PreviewActivity.class);
                        intent.putExtra("videoPath",combinedPath);
                        startActivity(intent);
                    }else{
                        timerHandler.postDelayed(this,500);
                    }
                }
            },500);

        }else{
            intent.putExtra("videoPath",savedVideoPath);
            startActivity(intent);
        }
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
        editorListener = new CombinedOnEditorListener(old,isCombined);
        FFmpegEditor.mergeByLc(root, epVideos, new FFmpegEditor.OutputOption(outFile), editorListener);
        return outFile;
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

}
