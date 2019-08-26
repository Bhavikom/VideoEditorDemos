package com.meishe.sdkdemo.capturescene;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsCaptureVideoFx;
import com.meicam.sdk.NvsColor;
import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsVideoStreamInfo;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BasePermissionActivity;
import com.meishe.sdkdemo.capturescene.adapter.CaptureSceneAdapter;
import com.meishe.sdkdemo.capturescene.data.CaptureSceneOnlineData;
import com.meishe.sdkdemo.capturescene.data.Constants;
import com.meishe.sdkdemo.capturescene.httputils.NetWorkUtil;
import com.meishe.sdkdemo.capturescene.httputils.OkHttpClientManager;
import com.meishe.sdkdemo.capturescene.httputils.ResultCallback;
import com.meishe.sdkdemo.capturescene.httputils.download.DownLoadResultCallBack;
import com.meishe.sdkdemo.capturescene.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.capturescene.view.CircleBarView;
import com.meishe.sdkdemo.selectmedia.view.CustomPopWindow;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.MediaScannerUtil;
import com.meishe.sdkdemo.utils.ParameterSettingValues;
import com.meishe.sdkdemo.utils.PathNameUtil;
import com.meishe.sdkdemo.utils.PathUtils;
import com.meishe.sdkdemo.utils.PopWindowUtil;
import com.meishe.sdkdemo.utils.SpUtil;
import com.meishe.sdkdemo.utils.TimeFormatUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.dataInfo.ClipInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;
import com.meishe.sdkdemo.utils.permission.PermissionsActivity;
import com.meishe.sdkdemo.utils.permission.PermissionsChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

import static com.meishe.sdkdemo.MainActivity.REQUEST_CAMERA_PERMISSION_CODE;
import static com.meishe.sdkdemo.base.BaseConstants.CAPTURESCENE_DIALOG_SP_KEY;
import static com.meishe.sdkdemo.capturescene.data.Constants.CAPTURE_SCENE_LOCAL;
import static com.meishe.sdkdemo.capturescene.data.Constants.CAPTURE_SCENE_ONLINE;
import static com.meishe.sdkdemo.capturescene.data.Constants.RESOURCE_NEW_PATH;

public class CaptureSceneActivity extends BasePermissionActivity implements NvsAssetPackageManager.AssetPackageManagerCallback, CustomPopWindow.OnViewClickListener,
        NvsStreamingContext.CaptureDeviceCallback, NvsStreamingContext.CaptureRecordingDurationCallback, NvsStreamingContext.CaptureRecordingStartedCallback {
    private String TAG = "CaptureSceneActivity";
    private String FILENAME_SUFFIX = "capturescene";
    private final String NAME = "Master Keyer";
    private ImageView switchText;
    private ImageView flashText;
    private ImageView backgroundText;
    private NvsLiveWindow mLiveWindow;
    private ImageView closeButton_cs;
    private int mCurrentDeviceIndex = 0;
    private PermissionsChecker mPermissionsChecker;
    private boolean mPermissionGranted;
    private List<String> mAllRequestPermission = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinkedList<CaptureSceneOnlineData.CaptureSceneDetails> captureSceneDetails = new LinkedList<>();
    /**
     * 记录下载地址的数组，维护下载的完整性。
     */
    private Map<String, String> downloadingURL = new HashMap<>();
    private CaptureSceneAdapter captureSceneAdapter;
    private LinearLayout csLayoutBackground;
    private android.widget.RelativeLayout captureSceneRecordLayout;
    private android.widget.CheckBox captureSceneRecord;
    private ImageView captureSceneRecordDelete;
    private ImageView captureSceneRecordSure;
    private TextView captureSceneRecordTime;
    private RecordData currentRecordData;
    private long currentRecordLength = 0;
    private List<RecordData> listOfRecordData = new ArrayList<>();
    private LinearLayout captureSceneControl;
    private TextView csTextReset;

    @Override
    protected int initRootView() {
        return R.layout.activity_capture_scene;
    }

    @Override
    protected void initViews() {
        initView();
        mLiveWindow = (NvsLiveWindow) findViewById(R.id.liveWindow);
        switchText = (ImageView) findViewById(R.id.captureScene_switch);
        flashText = (ImageView) findViewById(R.id.captureScene_flash);
        backgroundText = (ImageView) findViewById(R.id.captureScene_background);
        closeButton_cs = (ImageView) findViewById(R.id.closeButton_cs);
        recyclerView = (RecyclerView) findViewById(R.id.cs_recycleView);
        csLayoutBackground = (LinearLayout) findViewById(R.id.cs_layout_background);
        csTextReset = (TextView) findViewById(R.id.cs_text_reset);
    }

    @Override
    protected void initTitle() {
    }


    @Override
    protected void initData() {
        if (hasAllPermission()) {
            getMessageFormAssetsFile();
            captureSceneAdapter = new CaptureSceneAdapter(getBaseContext(), captureSceneDetails, new OnItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    CaptureSceneOnlineData.CaptureSceneDetails captureSceneDetails = (CaptureSceneOnlineData.CaptureSceneDetails) view.getTag();
                    if (captureSceneDetails.getPackageUrl().contains(RESOURCE_NEW_PATH) &&
                            !downloadingURL.containsKey(captureSceneDetails.getPackageUrl())) {
                        CircleBarView circleBarView = (CircleBarView) view.findViewById(R.id.item_cs_download);
                        downloadImage(captureSceneDetails.getCoverUrl(), position);
                        downloadPackage(captureSceneDetails.getPackageUrl(), circleBarView, position);
                    } else {
                        setCaptureSceneByPath(captureSceneDetails.getId(), captureSceneDetails.getPackageUrl());
                    }
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(captureSceneAdapter);
        }
        if (NetWorkUtil.isNetworkConnected(this)) {
            //有权限，则删除本地拍摄的视频文件
            OkHttpClientManager.getAsyn(Constants.CAPTURE_SCENE_PATH, new ResultCallback<CaptureSceneOnlineData>() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(CaptureSceneOnlineData response) {
                    List<CaptureSceneOnlineData.CaptureSceneDetails> captureSceneDetailsList = response.getList();
                    int oldSize = captureSceneDetails.size();
                    for (CaptureSceneOnlineData.CaptureSceneDetails sceneDetail : captureSceneDetailsList) {
                        sceneDetail.setType(CAPTURE_SCENE_ONLINE);
                        sceneDetail.setCoverUrl(sceneDetail.getCoverUrl().replace(Constants.RESOURCE_OLD_PATH, RESOURCE_NEW_PATH));
                        sceneDetail.setPackageUrl(sceneDetail.getPackageUrl().replaceAll(Constants.RESOURCE_OLD_PATH, RESOURCE_NEW_PATH));
                        if (!captureSceneDetails.contains(sceneDetail)) {
                            captureSceneDetails.add(sceneDetail);
                        }
                    }
                    if (oldSize != captureSceneDetails.size()) {
                        captureSceneAdapter.setDataList(captureSceneDetails);
                    }
                }
            });
        }
    }

    private void downloadImage(String coverUrl, final int position) {
        final String path = PathUtils.getCaptureSceneLocalFilePath();
        OkHttpClientManager.downloadAsyn(coverUrl, path, new DownLoadResultCallBack<String>() {
            @Override
            public void onProgress(long now, long total, int progress) {

            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                CaptureSceneOnlineData.CaptureSceneDetails data = captureSceneDetails.get(position);
                data.setCoverUrl(response);
                captureSceneAdapter.setDataList(position, data, false);
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }

    private void downloadPackage(final String packageUrl, final CircleBarView circleBarView, final int position) {
        final String path = PathUtils.getCaptureSceneLocalFilePath();
        downloadingURL.put(packageUrl, path + File.separator + PathNameUtil.getPathNameWithSuffix(packageUrl));
        OkHttpClientManager.downloadAsyn(packageUrl, path, new DownLoadResultCallBack<String>() {
            @Override
            public void onProgress(long now, long total, int progress) {
                captureSceneAdapter.setmProgress(progress, position);
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Logger.e(TAG, "downloadPackageOnError: " + e.toString());
                downloadingURL.remove(packageUrl);
                deleteFiles(path);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                circleBarView.setVisibility(View.GONE);
                downloadingURL.remove(packageUrl);
                CaptureSceneOnlineData.CaptureSceneDetails data = captureSceneDetails.get(position);
                data.setPackageUrl(response);
                data.setType(CAPTURE_SCENE_LOCAL);
                captureSceneAdapter.setDataList(position, data, true);
                if (captureSceneAdapter.getSelectPosition() == position) {
                    setCaptureSceneByPath(PathNameUtil.getPathNameNoSuffix(response), response);
                }
            }
        });
    }

    private void deleteFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {
        setClick(switchText, flashText, backgroundText, closeButton_cs, captureSceneRecordDelete, captureSceneRecordSure, csTextReset);
        initCapture();
        captureSceneRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isRecording()) {
                    stopRecording();
                } else {
                    captureSceneRecord.setBackground(getResources().getDrawable(R.drawable.capturescene_record_button));
                    String filePath = PathUtils.getCaptureSceneRecordVideoPath();
                    if (filePath == null) {
                        return;
                    }
                    currentRecordData = new RecordData(0, filePath);
                    //当前未在视频录制状态，则启动视频录制。此处使用带特效的录制方式
                    if (!mStreamingContext.startRecording(currentRecordData.getPath())) {
                        return;
                    }
                    listOfRecordData.add(currentRecordData);
                }
            }
        });
        mLiveWindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isContainCaptureVideoFxByName(NAME)) {
                    appendBuiltinCaptureVideoFx();
                }
                if (csLayoutBackground.getVisibility() == View.VISIBLE) {
                    setLayoutVisibility();
                    return false;
                }
                if (getCurrentEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
                    return false;
                }

                NvsColor sampledColor = getColorFromLiveWindow(event);
                // 将吸取下来的背景画面颜色值设置给抠像特技
                NvsCaptureVideoFx keyerFx = mStreamingContext.getCaptureVideoFxByIndex(0);
                if (keyerFx == null) {
                    return false;
                }
                keyerFx.setColorVal("Key Color", sampledColor);
                return true;
            }
        });
    }

    private boolean isContainCaptureVideoFxByName(String name) {
        int count = mStreamingContext.getCaptureVideoFxCount();
        for (int i = 0; i < count; i++) {
            NvsCaptureVideoFx nvsCaptureVideoFx = mStreamingContext.getCaptureVideoFxByIndex(i);
            if (nvsCaptureVideoFx.getBuiltinCaptureVideoFxName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private NvsColor getColorFromLiveWindow(MotionEvent event) {
        //从NvsLiveWindow控件的点击位置吸取背景画面的颜色值
        int sampleWidth = 20;
        int sampleHeight = 20;
        RectF sampleRect = new RectF();
        sampleRect.left = (int) (event.getX() - sampleWidth / 2);
        if (sampleRect.left < 0) {
            sampleRect.left = 0;
        } else if (sampleRect.left + sampleWidth > mLiveWindow.getWidth()) {
            sampleRect.left = mLiveWindow.getWidth() - sampleWidth;
        }

        sampleRect.top = (int) (event.getY() - sampleHeight / 2);
        if (sampleRect.top < 0) {
            sampleRect.top = 0;
        } else if (sampleRect.top + sampleHeight > mLiveWindow.getHeight()) {
            sampleRect.top = mLiveWindow.getHeight() - sampleHeight;
        }
        sampleRect.right = sampleRect.left + sampleWidth;
        sampleRect.bottom = sampleRect.top + sampleHeight;
        return mStreamingContext.sampleColorFromCapturedVideoFrame(sampleRect);
    }

    private boolean isRecording() {
        return getCurrentEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING;
    }

    private void stopRecording() {
        mStreamingContext.stopRecording();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !needShowDialog && !SpUtil.getInstance(this).getBoolean(CAPTURESCENE_DIALOG_SP_KEY, false)) {
            PopWindowUtil.getInstance().show(CaptureSceneActivity.this, R.layout.pop_tips_capturescene, CaptureSceneActivity.this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.captureScene_switch:
                mCurrentDeviceIndex = mCurrentDeviceIndex == 0 ? 1 : 0;
                if (mStreamingContext.isCaptureDeviceBackFacing(mCurrentDeviceIndex)) {
                    flashText.setEnabled(true);
                    flashText.setAlpha(1f);
                } else {
                    flashText.setEnabled(false);
                    flashText.setAlpha(0.5f);
                    if (mStreamingContext.isFlashOn()) {
                        changeFlash();
                    }
                }
                startCapturePreview(true);
                break;
            case R.id.closeButton_cs:
                AppManager.getInstance().finishActivity();
                break;
            case R.id.captureScene_flash:
                changeFlash();
                break;
            case R.id.captureScene_background:
                setLayoutVisibility();
                break;
            case R.id.captureScene_record_delete:
                if (!listOfRecordData.isEmpty()) {
                    listOfRecordData.remove(listOfRecordData.size() - 1);
                }
                onTotalLengthChange();
                break;
            case R.id.captureScene_record_sure:
                jumpToPreview();
                break;
            case R.id.cs_text_reset:
                clearCaptureScene();
                break;
            default:
                break;
        }
    }

    private void changeFlash() {
        mStreamingContext.toggleFlash(!mStreamingContext.isFlashOn());
        flashText.setBackground(mStreamingContext.isFlashOn() ? ContextCompat.getDrawable(getBaseContext(), R.mipmap.icon_flash_on)
                : ContextCompat.getDrawable(getBaseContext(), R.mipmap.icon_flash_off));
    }

    private void clearCaptureScene() {
        mStreamingContext.removeCurrentCaptureScene();
        mStreamingContext.removeAllCaptureVideoFx();
        captureSceneAdapter.setSelectPosition(-1);
    }

    private void setLayoutVisibility() {
        csLayoutBackground.setVisibility(csLayoutBackground.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        captureSceneRecordLayout.setVisibility(csLayoutBackground.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    private void jumpToPreview() {
        ArrayList<ClipInfo> pathList = new ArrayList<>();
        for (int i = 0; i < listOfRecordData.size(); i++) {
            ClipInfo clipInfo = new ClipInfo();
            clipInfo.setFilePath(listOfRecordData.get(i).getPath());
            pathList.add(clipInfo);
        }
        NvsAVFileInfo avFileInfo = mStreamingContext.getAVFileInfo(pathList.get(0).getFilePath());
        if (avFileInfo == null) {
            return;
        }
        TimelineData.instance().clear();//数据清空
        NvsSize size = avFileInfo.getVideoStreamDimension(0);
        int rotation = avFileInfo.getVideoStreamRotation(0);
        if (rotation == NvsVideoStreamInfo.VIDEO_ROTATION_90
                || rotation == NvsVideoStreamInfo.VIDEO_ROTATION_270) {
            int tmp = size.width;
            size.width = size.height;
            size.height = tmp;
        }
        int makeRatio = size.width > size.height ? NvAsset.AspectRatio_16v9 : NvAsset.AspectRatio_9v16;
        TimelineData.instance().setVideoResolution(Util.getVideoEditResolution(makeRatio));
        TimelineData.instance().setMakeRatio(makeRatio);
        TimelineData.instance().setClipInfoData(pathList);
        Bundle bundle = new Bundle();
        AppManager.getInstance().jumpActivity(CaptureSceneActivity.this, PreviewActivity.class, bundle);
    }

    private void initCapture() {
        if (null == mStreamingContext) {
            return;
        }
        mStreamingContext.getAssetPackageManager().setCallbackInterface(this);
        mStreamingContext.setCaptureDeviceCallback(this);
        mStreamingContext.setCaptureRecordingDurationCallback(this);
        mStreamingContext.setCaptureRecordingStartedCallback(this);
        if (mStreamingContext.getCaptureDeviceCount() == 0) {
            return;
        }
        if (!mStreamingContext.connectCapturePreviewWithLiveWindow(mLiveWindow)) {
            Logger.e(TAG, "Failed to connect capture preview with livewindow!");
            return;
        }
        if (mStreamingContext.getCaptureDeviceCount() > 1) {
            switchText.setEnabled(true);
        } else {
            switchText.setEnabled(false);
        }
        mPermissionsChecker = new PermissionsChecker(this);
        mAllRequestPermission.add(Manifest.permission.CAMERA);
        mAllRequestPermission = mPermissionsChecker.checkPermission(mAllRequestPermission);
        if (mAllRequestPermission.isEmpty()) {
            mPermissionGranted = true;
            startCapturePreview(false);
        } else {
            int code = getCodeInPermission(mAllRequestPermission.get(0));
            startPermissionsActivity(code, mAllRequestPermission.get(0));
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        appendBuiltinCaptureVideoFx();
    }

    private void appendBuiltinCaptureVideoFx() {
        NvsCaptureVideoFx keyerFx = mStreamingContext.appendBuiltinCaptureVideoFx(NAME);
        if (keyerFx != null) {
            // 开启溢色去除
            keyerFx.setBooleanVal("Spill Removal", true);
            // 将溢色去除强度设置为最低
            keyerFx.setFloatVal("Spill Removal Intensity", 0);
            //设置收缩边界强度
            keyerFx.setFloatVal("Shrink Intensity", 0.4);
        }
    }

    private void startPermissionsActivity(int code, String... permission) {
        PermissionsActivity.startActivityForResult(this, code, permission);
    }

    /**
     * 获取activity需要的权限列表
     *
     * @return 权限列表
     */
    @Override
    protected List<String> initPermissions() {
        List<String> list = new ArrayList<>();
        list.add(Manifest.permission.CAMERA);
        list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return list;
    }

    /**
     * 获取权限后
     */
    @Override
    protected void hasPermission() {

    }

    /**
     * 没有允许权限
     */
    @Override
    protected void nonePermission() {

    }

    /**
     * 用户选择了不再提示
     */
    @Override
    protected void noPromptPermission() {

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
        if (mPermissionGranted && (deviceChanged || getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTUREPREVIEW)) {
            if (!mStreamingContext.startCapturePreview(mCurrentDeviceIndex,
                    captureResolutionGrade,
                    NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_GRAB_CAPTURED_VIDEO_FRAME |
                            NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_DONT_USE_SYSTEM_RECORDER, null)) {
                Logger.e(TAG, "Failed to start capture preview!");
            }
        }
    }

    private int getCurrentEngineState() {
        return mStreamingContext.getStreamingEngineState();
    }

    private void setClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    private void getMessageFormAssetsFile() {
        String path = PathUtils.getCaptureSceneLocalFilePath();
        if (!path.isEmpty()) {
            File file = new File(path);
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isFile()) {
                    String suffix = PathNameUtil.getPathSuffix(file2.getAbsolutePath());
                    if (suffix.equals(FILENAME_SUFFIX)) {
                        CaptureSceneOnlineData.CaptureSceneDetails captureSceneDetails = new CaptureSceneOnlineData.CaptureSceneDetails();
                        String pathName = file2.getAbsolutePath();
                        captureSceneDetails.setPackageUrl(pathName);
                        captureSceneDetails.setId(PathNameUtil.getPathNameNoSuffix(pathName));
                        String imagePath = PathNameUtil.getOutOfPathSuffix(pathName) + "png";
                        File imageFile = new File(imagePath);
                        if (!imageFile.exists()) {
                            imagePath = "";
                        }
                        captureSceneDetails.setCoverUrl(imagePath);
                        captureSceneDetails.setType(CAPTURE_SCENE_LOCAL);
                        this.captureSceneDetails.add(captureSceneDetails);
                    }
                }
            }
        }
    }

    private void setCaptureSceneByPath(String sceneId, String scenePackageFilePath) {
        // 首先检查改拍摄场景的资源包是否已经安装
        NvsAssetPackageManager assetPackageManager = mStreamingContext.getAssetPackageManager();
        int packageStatus = assetPackageManager.getAssetPackageStatus(sceneId, NvsAssetPackageManager.ASSET_PACKAGE_TYPE_CAPTURESCENE);
        if (packageStatus == NvsAssetPackageManager.ASSET_PACKAGE_STATUS_NOTINSTALLED) {
            // 该拍摄场景的资源包尚未安装，现在安装该资源包，由于拍摄场景的资源包尺寸较大
            // 为了不freeze UI，我们采用异步安装模式
            assetPackageManager.installAssetPackage(scenePackageFilePath, null, NvsAssetPackageManager.ASSET_PACKAGE_TYPE_CAPTURESCENE, false, null);
        } else {
            // 该拍摄场景的资源包已经安装，应用其效果
            mStreamingContext.applyCaptureScene(sceneId);
        }
    }

    @Override
    public void onFinishAssetPackageInstallation(String assetPackageId, String s1, int i, int error) {
        Logger.e(TAG, "onFinishAssetPackageInstallation: " + error + "     " + s1);
        if (error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR) {
            mStreamingContext.applyCaptureScene(assetPackageId);
        }
    }

    @Override
    public void onFinishAssetPackageUpgrading(String s, String s1, int i, int i1) {

    }

    @Override
    protected void onDestroy() {
        Logger.e(TAG, "onDestroy: ");
        if (mStreamingContext != null) {
            mStreamingContext.removeAllCaptureVideoFx();
            mStreamingContext.removeCurrentCaptureScene();
            mStreamingContext.stop();
            mStreamingContext = null;
        }
        for (String url : downloadingURL.keySet()) {
            Logger.e(TAG, "onDestroy下载地址: " + url);
            OkHttpClientManager.cancelTag(url);
        }
        for (String filePath : downloadingURL.values()) {
            Logger.e(TAG, "onDestroy文件地址: " + filePath);
            Logger.e(TAG, "onDestroy文件地址: " + PathNameUtil.getOutOfPathSuffix(filePath) + "png");
            deleteFiles(filePath);
            deleteFiles(PathNameUtil.getOutOfPathSuffix(filePath) + "png");
        }
        downloadingURL.clear();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mStreamingContext != null) {
            mStreamingContext.stop();
        }
        // 修正摄像头状态
        flashText.setBackground(ContextCompat.getDrawable(getBaseContext(), R.mipmap.icon_flash_off));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPermissionsChecker == null) {
            mPermissionsChecker = new PermissionsChecker(this);
        }
        startCapturePreview(false);
    }

    private boolean needShowDialog = false;

    @Override
    public void onViewClick(CustomPopWindow popWindow, View view) {
        if (view.getId() == R.id.pop_tips_tv_noMore) {
            SpUtil.getInstance(this).putBoolean(CAPTURESCENE_DIALOG_SP_KEY, true);
        } else if (view.getId() == R.id.pop_tips_tv_iKnow) {
            needShowDialog = true;
        }
    }

    private void initView() {
        captureSceneControl = (LinearLayout) findViewById(R.id.captureScene_control);
        captureSceneRecordLayout = (RelativeLayout) findViewById(R.id.captureScene_record_layout);
        captureSceneRecord = (CheckBox) findViewById(R.id.captureScene_record);
        captureSceneRecordDelete = (ImageView) findViewById(R.id.captureScene_record_delete);
        captureSceneRecordSure = (ImageView) findViewById(R.id.captureScene_record_sure);
        captureSceneRecordTime = (TextView) findViewById(R.id.captureScene_record_time);
    }

    @Override
    public void onCaptureDeviceCapsReady(int i) {

    }

    @Override
    public void onCaptureDevicePreviewResolutionReady(int i) {

    }

    @Override
    public void onCaptureDevicePreviewStarted(int i) {

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
        captureSceneControl.setVisibility(View.VISIBLE);
        captureSceneRecord.setChecked(true);
        currentRecordData.setLength(currentRecordLength);
        onTotalLengthChange();
        // 保存到媒体库
        if (listOfRecordData != null && !listOfRecordData.isEmpty()) {
            for (RecordData recordData : listOfRecordData) {
                if (recordData == null) {
                    continue;
                }
                if (recordData.getPath().endsWith(".mp4")) {
                    MediaScannerUtil.scanFile(recordData.getPath(), "video/mp4");
                } else if (recordData.getPath().endsWith(".jpg")) {
                    MediaScannerUtil.scanFile(recordData.getPath(), "image/jpg");
                }
            }
        }
    }

    @Override
    public void onCaptureRecordingError(int i) {
        captureSceneRecord.setChecked(true);
    }

    @Override
    public void onCaptureRecordingDuration(int i, long l) {
        currentRecordLength = l;
        captureSceneRecord.setEnabled(l / 1000 > 3000);
        captureSceneRecordTime.setText(TimeFormatUtil.formatUsToString2(l + getTotalRecordLength()));
        captureSceneRecordTime.setTextColor(getResources().getColor(R.color.cs_textColor_recording));

    }

    @Override
    public void onCaptureRecordingStarted(int i) {
        captureSceneControl.setVisibility(View.GONE);
        captureSceneRecord.setText("");
    }

    private long getTotalRecordLength() {
        if (listOfRecordData.isEmpty()) {
            return 0;
        } else {
            long total = 0;
            for (RecordData listOfRecordDatum : listOfRecordData) {
                total += listOfRecordDatum.getLength();
            }
            return total;
        }
    }

    private void onTotalLengthChange() {
        long length = getTotalRecordLength();
        captureSceneRecordDelete.setVisibility(length == 0 ? View.GONE : View.VISIBLE);
        captureSceneRecordSure.setVisibility(length == 0 ? View.GONE : View.VISIBLE);
        captureSceneRecordTime.setText(length == 0 ? "" : TimeFormatUtil.formatUsToString2(getTotalRecordLength()));
        captureSceneRecordTime.setTextColor(getResources().getColor(R.color.white));
        captureSceneRecord.setBackground(length == 0 ? getResources().getDrawable(R.drawable.capturescene_normal) : getResources().getDrawable(R.drawable.capturescene_record_button));
        captureSceneRecord.setText(length == 0 ? "" : String.valueOf(listOfRecordData.size()));
    }
}
