package com.meishe.sdkdemo.boomrang;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.boomrang.fragment.BaseRecordFragment;
import com.meishe.sdkdemo.boomrang.fragment.BaseRecordInterface;
import com.meishe.sdkdemo.boomrang.view.RoundProgressView;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.MediaScannerUtil;
import com.meishe.sdkdemo.utils.PathUtils;

import java.io.LineNumberReader;
import java.util.Hashtable;

public class BoomRangActivity extends BaseActivity implements BaseRecordInterface {
    private final String TAG = getClass().getName();
    BaseRecordFragment previewFragment;
    Button boomRangCloseButton;
    LinearLayout boomRangControl;
    ImageView boomRangSwitch;
    ImageView boomRangFlash;
    ImageView boomRangAnimateImage;
    RoundProgressView boomRangRecordBtn;
    String recordFilePath;

    private boolean mIsSwitchingCamera = false;
    ObjectAnimator animator;

    @Override
    protected int initRootView() {
        return R.layout.activity_boom_rang;
    }

    @Override
    protected void initViews() {
        boomRangCloseButton = (Button) findViewById(R.id.boomRang_closeButton);
        boomRangControl = (LinearLayout) findViewById(R.id.boomRang_control);
        boomRangSwitch = (ImageView) findViewById(R.id.boomRang_switch);
        boomRangFlash = (ImageView) findViewById(R.id.boomRang_flash);
        boomRangRecordBtn = (RoundProgressView) findViewById(R.id.boomRang_recordBtn);
        boomRangAnimateImage = (ImageView) findViewById(R.id.boomRang_animateImage);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initData() {
        setFragment();
        previewFragment.opeanAutoFocusAndExposure(true, null, null);
    }

    @Override
    protected void initListener() {
        boomRangCloseButton.setOnClickListener(this);
        boomRangSwitch.setOnClickListener(this);
        boomRangFlash.setOnClickListener(this);
        boomRangRecordBtn.setOnClickListener(this);
    }

    private void setFragment() {
        previewFragment = (BaseRecordFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        if (previewFragment == null) {
            previewFragment = new BaseRecordFragment();
            Bundle bundle = getIntent().getExtras();
            previewFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.boomRang_rootView, previewFragment, TAG)
                    .commit();
            getSupportFragmentManager().beginTransaction().show(previewFragment).commit();
        }
    }

    @Override
    public void onCaptureRecordingStarted(int i) {

    }

    @Override
    public void onCaptureRecordingDuration(int i, long l) {
        if (l >= 1000000) {
            Log.e(TAG, "record end");
            stopRecord();
        }
    }

    @Override
    public void onCaptureRecordingFinished(int i) {
        MediaScannerUtil.scanFile(recordFilePath, "video/mp4");
        Intent intent = new Intent(this, BoomRangPreviewActivity.class);
        intent.putExtra("video_path", recordFilePath);
        startActivity(intent);
        boomRangRecordBtn.setProgress(0);
    }

    @Override
    public void onCaptureRecordingError(int i) {
        Logger.e(TAG, "RecordingError:   now capture device " + i + "   please check you memory");
    }

    @Override
    public void onCaptureDevicePreviewStarted(int i) {
        mIsSwitchingCamera = false;
        boolean ismSupportFlash = previewFragment.ismSupportFlash();
        Log.e(TAG, "onCaptureDevicePreviewStarted: " +ismSupportFlash );
        boomRangFlash.setAlpha(ismSupportFlash ? 1f : 0.5f);
        boomRangFlash.setEnabled(ismSupportFlash);
    }

    private void stopRecord() {
        layoutChangeOnRecordStateChange(false);
        if (previewFragment.isRecording()) {
            stopRecordAnimation();
            boomRangAnimateImage.setVisibility(View.GONE);
            boomRangRecordBtn.setEnabled(true);
        }
        previewFragment.stopRecord();
    }

    private void stopRecordAnimation() {
        boomRangAnimateImage.clearAnimation();
        clearObjectAnimation();
    }

    private void clearObjectAnimation() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boomRang_closeButton:
                finish();
                break;
            case R.id.boomRang_switch:
                if (mIsSwitchingCamera) {
                    return;
                }
                previewFragment.changeCurrentDeviceIndex();
                mIsSwitchingCamera = true;
                if (previewFragment.isFlashOn()){
                    changeFlash();
                }
                break;
            case R.id.boomRang_flash:
                previewFragment.changeFlash();
                changeFlash();
                break;
            case R.id.boomRang_recordBtn:
                startRecord();
                break;
            default:

                break;
        }
    }

    private void changeFlash() {
        boomRangFlash.setBackground(mStreamingContext.isFlashOn()? ContextCompat.getDrawable(getBaseContext(),R.mipmap.icon_flash_on)
                :ContextCompat.getDrawable(getBaseContext(),R.mipmap.icon_flash_off));
    }

    private void startRecord() {
        layoutChangeOnRecordStateChange(true);
        previewFragment.setRecordVideoBitrateMultiplier(1.5f);
        Hashtable<String, Object> config = new Hashtable<>();
        config.put(com.meicam.sdk.NvsStreamingContext.RECORD_GOP_SIZE, 5);
        recordFilePath = PathUtils.getBoomrangRecordingDirectory("_record");
        if (!previewFragment.startRecord(recordFilePath, 0, config)) {
            return;
        }
        boomRangRecordBtn.setEnabled(false);
        startRecordAnimation();
    }

    private void layoutChangeOnRecordStateChange(boolean isStart){
        boomRangCloseButton.setVisibility(isStart?View.GONE:View.VISIBLE);
        boomRangControl.setVisibility(isStart?View.GONE:View.VISIBLE);
    }

    private void startRecordAnimation() {
        startTwinkle();
        startRecordProgress();
    }

    private void startTwinkle() {
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation1.setDuration(20);
        alphaAnimation1.setRepeatCount(Animation.INFINITE);
        alphaAnimation1.setRepeatMode(Animation.REVERSE);
        boomRangAnimateImage.startAnimation(alphaAnimation1);
        boomRangAnimateImage.setVisibility(View.VISIBLE);
    }

    private void startRecordProgress() {
        if (animator == null) {
            animator = ObjectAnimator.ofInt(boomRangRecordBtn, "progress", 100);
            animator.setDuration(1000);
            animator.start();
        }
    }
}

