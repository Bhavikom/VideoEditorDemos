package com.meishe.sdkdemo.boomrang.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsStreamingContext;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.capturescene.NvsStreamingContextUtil;
import com.meishe.sdkdemo.utils.ParameterSettingValues;
import com.meishe.sdkdemo.utils.permission.PermissionsActivity;
import com.meishe.sdkdemo.utils.permission.PermissionsChecker;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

//import static com.meicam.sdk.NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_DONT_CAPTURE_AUDIO;
import static com.meicam.sdk.NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_DONT_CAPTURE_AUDIO;
import static com.meishe.sdkdemo.MainActivity.REQUEST_CAMERA_PERMISSION_CODE;
import static com.meishe.sdkdemo.utils.permission.PermissionDialog.noPermissionDialog;
import static com.meishe.sdkdemo.utils.permission.PermissionsActivity.EXTRA_PERMISSIONS;

/**
 * Created by CaoZhiChao on 2018/12/17 11:17
 */
public class BaseRecordFragment extends Fragment implements
        NvsStreamingContext.CaptureDeviceCallback, NvsStreamingContext.CaptureRecordingDurationCallback, NvsStreamingContext.CaptureRecordingStartedCallback {
    private final String TAG = "BaseRecordFragment";
    private View rootView;
    private NvsLiveWindow mLiveWindow;
    private NvsStreamingContext mStreamingContext;
    private int mCurrentDeviceIndex = 0;
    private List<String> mAllRequestPermission = new ArrayList<>();
    private BaseRecordInterface baseRecordInterface;
    private View autoFocusView;
    private Animation mFocusAnimation;
    private boolean openAutoFocusAndExposure = false;
    private boolean mSupportAutoFocus = false;
    private boolean mSupportAutoExposure = false;
    private boolean mSupportFlash = false;
    private NvsStreamingContext.CaptureDeviceCapability mCapability = null;

    public void opeanAutoFocusAndExposure(boolean open, View view, Animation animation) {
        this.openAutoFocusAndExposure = open;
        if (view != null && animation != null) {
            this.autoFocusView = view;
            this.mFocusAnimation = animation;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseRecordInterface) {
            baseRecordInterface = (BaseRecordInterface) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_base_record, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStreamingContext = NvsStreamingContextUtil.getInstance().getmStreamingContext();
        initListener();
        initCapture();
//        updateSettingsWithCapability(mCurrentDeviceIndex);
    }

    private void updateSettingsWithCapability(int deviceIndex) {
        //获取采集设备能力描述对象，设置自动聚焦，曝光补偿，缩放
        mCapability = mStreamingContext.getCaptureDeviceCapability(deviceIndex);
        if (null == mCapability) {
            return;
        }
        mSupportFlash = mCapability.supportFlash;
        mSupportAutoFocus = mCapability.supportAutoFocus;
        mSupportAutoExposure = mCapability.supportExposureCompensation;
        Log.e(TAG, "updateSettingsWithCapability: " + mSupportFlash + "   " + mSupportAutoFocus + "   " + mSupportAutoExposure);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        mLiveWindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (openAutoFocusAndExposure && mSupportAutoFocus && autoFocusView != null && mFocusAnimation != null) {
                    float rectHalfWidth = autoFocusView.getWidth() / 2;
                    if (event.getX() - rectHalfWidth >= 0 && event.getX() + rectHalfWidth <= mLiveWindow.getWidth()
                            && event.getY() - rectHalfWidth >= 0 && event.getY() + rectHalfWidth <= mLiveWindow.getHeight()) {
                        autoFocusView.setX(event.getX() - rectHalfWidth);
                        autoFocusView.setY(event.getY() - rectHalfWidth);
                        RectF rectFrame = new RectF();
                        rectFrame.set(autoFocusView.getX(), autoFocusView.getY(),
                                autoFocusView.getX() + autoFocusView.getWidth(),
                                autoFocusView.getY() + autoFocusView.getHeight());
                        //启动自动聚焦
                        autoFocusView.startAnimation(mFocusAnimation);
                        initAutoFocusAndExposure(rectFrame);
                    }
                }
                return false;
            }
        });
    }

    private void initAutoFocusAndExposure(RectF rectF) {
        if (openAutoFocusAndExposure) {
            mStreamingContext.startAutoFocus(new RectF(rectF));
        }
        if (mSupportAutoExposure) {
            //启动自动曝光补偿
            mStreamingContext.setAutoExposureRect(rectF);
        }
    }

    private void initCapture() {
        if (null == mStreamingContext) {
            return;
        }
        mStreamingContext.setCaptureDeviceCallback(this);
        mStreamingContext.setCaptureRecordingDurationCallback(this);
        mStreamingContext.setCaptureRecordingStartedCallback(this);
        if (mStreamingContext.getCaptureDeviceCount() == 0) {
            return;
        }
        if (!mStreamingContext.connectCapturePreviewWithLiveWindow(mLiveWindow)) {
            Log.e(TAG, "Failed to connect capture preview with liveWindow!");
            return;
        }
        checkNeedPermission();
    }

    private boolean checkNeedPermission() {
        PermissionsChecker mPermissionsChecker = new PermissionsChecker(getContext());
        mAllRequestPermission.add(Manifest.permission.CAMERA);
        mAllRequestPermission = mPermissionsChecker.checkPermission(mAllRequestPermission);
        return mAllRequestPermission.isEmpty();
    }

    private int getCodeInPermission(String permission) {
        int code = 0;
        if (permission.equals(Manifest.permission.CAMERA)) {
            code = REQUEST_CAMERA_PERMISSION_CODE;
        }
        return code;
    }

    private void startCapturePreview(boolean deviceChanged) {
        int captureResolutionGrade = ParameterSettingValues.instance().getCaptureResolutionGrade();
        if ((deviceChanged || NvsStreamingContextUtil.getInstance().getEngineState() != com.meicam.sdk.NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTUREPREVIEW)) {
            if (!NvsStreamingContextUtil.getInstance().getmStreamingContext().startCapturePreview(mCurrentDeviceIndex,
                    captureResolutionGrade,
                    com.meicam.sdk.NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_GRAB_CAPTURED_VIDEO_FRAME |
                            com.meicam.sdk.NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_DONT_USE_SYSTEM_RECORDER
                            | STREAMING_ENGINE_CAPTURE_FLAG_DONT_CAPTURE_AUDIO, null)) {
                Log.e(TAG, "Failed to start capture preview!");
            }
        }
    }

    public void changeCurrentDeviceIndex() {
        this.mCurrentDeviceIndex = mCurrentDeviceIndex == 0 ? 1 : 0;
        startCapturePreview(true);
    }

    public void setRecordVideoBitrateMultiplier(float value) {
        mStreamingContext.setRecordVideoBitrateMultiplier(value);
    }

    public boolean startRecord(String path, int i, Hashtable<String, Object> config) {
        if (NvsStreamingContextUtil.getInstance().getEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
            return mStreamingContext.startRecording(path, i, config);
        }
        return false;
    }

    public boolean startRecord(String path) {
        if (!isRecording()) {
            return mStreamingContext.startRecording(path);
        }
        return false;
    }

    public void stopRecord() {
        if (isRecording()) {
            mStreamingContext.stopRecording();
        }
    }

    public boolean isRecording() {
        return NvsStreamingContextUtil.getInstance().getEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING;
    }

    public void changeFlash() {
        if (mSupportFlash) {
            mStreamingContext.toggleFlash(!isFlashOn());
        }
    }

    public boolean isFlashOn() {
        return mStreamingContext.isFlashOn();
    }

    private void initView(View rootView) {
        mLiveWindow = (NvsLiveWindow) rootView.findViewById(R.id.liveWindow);
    }

    @Override
    public void onCaptureDeviceCapsReady(int i) {
        updateSettingsWithCapability(i);

    }

    @Override
    public void onCaptureDevicePreviewResolutionReady(int i) {

    }

    @Override
    public void onCaptureDevicePreviewStarted(int i) {
        baseRecordInterface.onCaptureDevicePreviewStarted(i);
    }

    @Override
    public void onCaptureDeviceError(int i, int i1) {

    }

    @Override
    public void onCaptureDeviceStopped(int i) {

    }

    @Override
    public void onCaptureDeviceAutoFocusComplete(int i, boolean b) {

    }

    @Override
    public void onCaptureRecordingFinished(int i) {
        baseRecordInterface.onCaptureRecordingFinished(i);
    }

    @Override
    public void onCaptureRecordingError(int i) {
        baseRecordInterface.onCaptureRecordingError(i);
    }

    @Override
    public void onCaptureRecordingDuration(int i, long l) {
        baseRecordInterface.onCaptureRecordingDuration(i, l);
    }

    @Override
    public void onCaptureRecordingStarted(int i) {
        baseRecordInterface.onCaptureRecordingStarted(i);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mStreamingContext != null) {
            mStreamingContext.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (permissionResult == PermissionsActivity.PERMISSIONS_No_PROMPT) {
            noPermissionDialog(getContext());
        } else {
            if (mAllRequestPermission.isEmpty()) {
                startCapturePreview(false);
            } else if (permissionResult == PermissionsActivity.PERMISSIONS_GRANTED){
                startCapturePreview(false);
            }else {
                int code = getCodeInPermission(mAllRequestPermission.get(0));
                startPermissionsActivity(code, mAllRequestPermission.get(0));
            }
        }
    }

    private void startPermissionsActivity(int code, String... permission) {
        Intent intent = new Intent(getActivity(), PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permission);
        startActivityForResult(intent, code);
    }

    private int permissionResult = -1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionResult = resultCode;
    }

    public boolean ismSupportFlash() {
        return mSupportFlash;
    }
}
