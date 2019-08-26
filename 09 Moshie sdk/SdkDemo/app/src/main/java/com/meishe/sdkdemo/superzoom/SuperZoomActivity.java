package com.meishe.sdkdemo.superzoom;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meicam.effect.sdk.NvsEffectSdkContext;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsVideoResolution;
import com.meishe.effect.EGLHelper;
import com.meishe.effect.NvSuperZoom;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BasePermissionActivity;
import com.meishe.sdkdemo.boomrang.view.RoundProgressView;
import com.meishe.sdkdemo.interfaces.TipsButtonClickListener;
import com.meishe.sdkdemo.superzoom.fxview.CenterHorizontalView;
import com.meishe.sdkdemo.superzoom.fxview.CenterHorizontalViewAdapter;
import com.meishe.sdkdemo.superzoom.helper.ShaderHelper;
import com.meishe.sdkdemo.superzoom.processor.CameraProxy;
import com.meishe.sdkdemo.superzoom.processor.MediaEncoder;
import com.meishe.sdkdemo.superzoom.processor.MediaMuxerWrapper;
import com.meishe.sdkdemo.superzoom.processor.MediaVideoEncoder;
import com.meishe.sdkdemo.superzoom.zoomutils.Accelerometer;
import com.meishe.sdkdemo.superzoom.zoomutils.RawResourceReader;
import com.meishe.sdkdemo.superzoom.zoomutils.STMobileDetected;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.PathUtils;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;
import com.sensetime.stmobile.model.STHumanAction;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.meishe.sdkdemo.superzoom.processor.MediaMuxerWrapper.MSG_FINISH_RECORDING;
import static com.meishe.sdkdemo.utils.Constants.ASSET_DOWNLOAD_INPROGRESS;
import static com.meishe.sdkdemo.utils.Constants.ASSET_LIST_REQUEST_SUCCESS;


/**
 * SuperZoomActivity class
 *
 * @author mlj
 * @date 2019-01-11
 */
public class SuperZoomActivity extends BasePermissionActivity implements SurfaceTexture.OnFrameAvailableListener, GLSurfaceView.Renderer {

    private static final String TAG = "SuperZoomActivity";

    private static final int REQUEST_CAMERA_PERMISSION_CODE = 0;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 2;
    private static final int START_RECORDING_CODE = 1800;

    private static float[] m_squareCoords = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f};
    private static short[] m_drawOrder = {0, 1, 2, 0, 2, 3};
    private float[] textureCoords = {
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f};

    private RoundProgressView mButtonRecord;
    private ImageView mButtonSwitchFacing;
    private AppCompatButton mCloseBtn;
    private GLSurfaceView mGLView;
    private CenterHorizontalView mSuperZoomFxView;
    private LinearLayout mToolListLl;
    private ImageView mButtonFlash;

    /**
     * 默认前置摄像头
     */
    private int mCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private Accelerometer mAccelerometer = null;
    private CameraProxy mCameraProxy;
    private boolean mPermissionGranted;
    private boolean mIsPreviewing = false;
    private int mOrientation;
    private int mWidth, mHeight;
    private int mShaderProgram;
    private FloatBuffer mVertexBuffer;
    private ShortBuffer mDrawListBuffer;
    private float[] mVideoTextureTransform = new float[16];
    private SurfaceTexture mCameraPreviewTexture;
    private HandlerThread mSurfaceAvailableThread;
    private Handler mSurfaceAvailableHandler;
    private boolean mDetectFace = true;
    private boolean mFlipHorizontal = false;
    private String mZoomFx = null;
    private MediaVideoEncoder mVideoEncoder;
    private boolean mNeedResetEglContext = false;
    private boolean mIsRecording = false;
    private Object mGLThreadSyncObject = new Object();
    private boolean mFrameAvailable = false;
    int mTextureHandle;
    int mTextureCoordinateHandle;
    int mPositionHandle;
    int mTextureTransformHandle;
    private int[] mCameraPreviewTextures = new int[1];
    private FloatBuffer textureBuffer;
    private int mDisplayTex = 0;
    int mConvertProgramID = -1;
    private int[] mFrameBuffers = null;
    boolean mFlashToggle = false;
    private ArrayList<NvAsset> list = new ArrayList<>();
    private float mAnchorX = 0;
    private float mAnchorY = 0;

    STMobileDetected mStMobileDetected;
    private NvsEffectSdkContext mEffectSdkContext;
    private NvsVideoResolution mCurrentVideoResolution;
    private NvSuperZoom mNvSuperZoom;
    private CenterHorizontalViewAdapter mCenterHorizontalViewAdapter;
    private String[] fxNames = {
            "dramatization",
            "spring",
            "cartoon",
            "daily",
            "dogpacks",
            "fire",
            "horror",
            "love",
            "no",
            "rhythm",
//            "rotate",
            "shake",
            "tragedy",
            "tv",
            "wasted"
    };

    EGLContext mEglContext;
    EGLDisplay mEglDisplay;

    private ObjectAnimator animator;
    private NvAssetManager mAssetManager;
    private NvAsset mZoomFxData;

    private SuperZoomHandler mHandler = new SuperZoomHandler(this);
    private boolean isClick;
    private long lastClickTime;


    class SuperZoomHandler extends Handler {
        WeakReference<SuperZoomActivity> mWeakReference;

        public SuperZoomHandler(SuperZoomActivity superZoomContext) {
            mWeakReference = new WeakReference<>(superZoomContext);
        }

        @Override
        public void handleMessage(Message msg) {
            final SuperZoomActivity activity = mWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case START_RECORDING_CODE:
                        startCapturePreview();
                        break;
                    case MSG_FINISH_RECORDING:
                        mGLView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                // 理解为特效停止
                                mNvSuperZoom.stop();
                            }
                        });
                        // 理解为录制停止
                        stopRecording();
                        Intent intent = new Intent(SuperZoomActivity.this, SuperZoomPreviewActivity.class);
                        intent.putExtra("video_path", mMuxer.getFilePath());
                        intent.putExtra("zoomFx", mZoomFx);
                        startActivity(intent);
                        clearObjectAnimation();
                        mButtonRecord.setProgress(0);
                        break;
                    case ASSET_LIST_REQUEST_SUCCESS:
                        // 得到可用资源
                        ArrayList<NvAsset> localData = getLocalData(NvAsset.ASSET_SUPER_ZOOM);
                        // 遍历修改list中的数据的状态
                        for (NvAsset nvAsset : localData) {
                            for (int index = 0, length = fxNames.length; index < length; index++) {
                                if (TextUtils.equals(list.get(index).uuid, nvAsset.uuid)) {
                                    list.get(index).localDirPath = nvAsset.localDirPath;
                                    continue;
                                }
                            }
                        }
                        // 更新adapter
                        if (mCenterHorizontalViewAdapter == null) {
                            mCenterHorizontalViewAdapter = new CenterHorizontalViewAdapter(SuperZoomActivity.this, list, 1);
                        } else {
                            mCenterHorizontalViewAdapter.notifyDataSetChanged();
                        }
                        break;
                    case ASSET_DOWNLOAD_INPROGRESS:
                        if (mCenterHorizontalViewAdapter != null) {
                            if (!isClick) {
                                mCenterHorizontalViewAdapter.notifyDataSetChanged();
                            }
                        }
                        break;
                    default:
                }
            }
        }
    }

    private void setRecordButtonEnable(boolean enable) {
        mButtonRecord.setEnabled(enable);
        mButtonRecord.setClickable(enable);
    }

    @Override
    protected int initRootView() {
        return R.layout.activity_super_zoom;
    }

    @Override
    protected void initViews() {
        mEffectSdkContext = NvsEffectSdkContext.getInstance();
        if (mEffectSdkContext == null) {
            String effectSdkLicensePath = "assets:/meishesdk.lic";
            mEffectSdkContext = NvsEffectSdkContext.init(getApplicationContext(), effectSdkLicensePath, 0);
        }
        mStMobileDetected = new STMobileDetected();
        mStMobileDetected.initSTMobileDetected(SuperZoomActivity.this);
        initUI();
        mNvSuperZoom = new NvSuperZoom(SuperZoomActivity.this);
    }

    @Override
    protected void initTitle() {
    }

    @Override
    protected List<String> initPermissions() {
        return Util.getAllPermissionsList();
    }

    @Override
    protected void hasPermission() {
    }

    @Override
    protected void nonePermission() {
        Log.d(TAG, "initCapture failed,above 6.0 device may has no access to camera");
        // 拒绝后
        noPermissionDialog();
    }

    @Override
    protected void noPromptPermission() {
        // 拒绝了权限
        Logger.e(TAG, "initCapture failed,above 6.0 device has no access from user");
        noPermissionDialog();
    }

    private void setViewsVisibility(int visibility) {
        mCloseBtn.setVisibility(visibility);
        mToolListLl.setVisibility(visibility);
        mSuperZoomFxView.setVisibility(visibility);
    }

    private void initUI() {
        searchAssetData();
        mToolListLl = (LinearLayout) this.findViewById(R.id.tool_list_ll);
        mCloseBtn = (AppCompatButton) this.findViewById(R.id.close);
        mCloseBtn.setVisibility(View.VISIBLE);
        mButtonRecord = (RoundProgressView) findViewById(R.id.buttonRecord);
        mButtonRecord.setProgress(0);
        mButtonSwitchFacing = (ImageView) findViewById(R.id.buttonSwitchFacing);
        mButtonFlash = (ImageView) findViewById(R.id.buttonFlash);
        mGLView = (GLSurfaceView) findViewById(R.id.GLView);

        // 设置使用OPENGL ES2.0
        mGLView.setEGLContextClientVersion(2);
        mGLView.setRenderer(this);
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mAccelerometer = new Accelerometer(getApplicationContext());

        // 特效选择
        mSuperZoomFxView = (CenterHorizontalView) findViewById(R.id.super_zoom_fx_view);
        mSuperZoomFxView.setHasFixedSize(true);
        mSuperZoomFxView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        if (mCenterHorizontalViewAdapter == null) {
            mCenterHorizontalViewAdapter = new CenterHorizontalViewAdapter(this, list, 1);
        } else {
            mCenterHorizontalViewAdapter.notifyDataSetChanged();
        }
        // 解决下载刷新和点击冲突问题
        mCenterHorizontalViewAdapter.setItemTouchListener(new CenterHorizontalViewAdapter.ItemTouchCallBack() {
            @Override
            public void itemTouched(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isClick = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        isClick = false;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        isClick = false;
                        break;
                    default:
                        break;
                }
            }
        });
        // 点击监听
        mCenterHorizontalViewAdapter.setItemClickListener(new CenterHorizontalViewAdapter.ItemClickCallBack() {
            @Override
            public void itemclicked(CenterHorizontalViewAdapter.FxViewHolder holder, int position) {
                // 点击事件 如果没有下载 则下载
                boolean needDownload = TextUtils.isEmpty(mCenterHorizontalViewAdapter.getData().get(position).localDirPath) && TextUtils.isEmpty(mCenterHorizontalViewAdapter.getData().get(position).bundledLocalDirPath);
                if (needDownload) {
                    if (mCenterHorizontalViewAdapter.getData().get(position).downloadStatus == NvAsset.DownloadStatusInProgress) {
                        return;
                    }
                    mAssetManager.downloadAsset(NvAsset.ASSET_SUPER_ZOOM, fxNames[position]);
                    mCenterHorizontalViewAdapter.getData().get(position).downloadStatus = NvAsset.DownloadStatusInProgress;
                } else {
                    // 不需要下载则直接跳转
                    mSuperZoomFxView.moveToPosition(position);
                }
            }
        });
        mSuperZoomFxView.setAdapter(mCenterHorizontalViewAdapter);
        // 滑动停止后选择
        mSuperZoomFxView.setOnSelectedPositionChangedListener(new CenterHorizontalView.OnSelectedPositionChangedListener() {
            @Override
            public void selectedPositionChanged(int pos) {
                if (mCenterHorizontalViewAdapter.getData().size() > 0) {
                    int i = pos % mCenterHorizontalViewAdapter.getData().size();
                    mZoomFx = mCenterHorizontalViewAdapter.getData().get(pos).uuid;
                    mZoomFxData = mCenterHorizontalViewAdapter.getData().get(pos);
                    mDetectFace = true;
                    mAnchorX = 0;
                    mAnchorY = 0;
                }
            }
        });
    }

    @Override
    protected void initData() {
        mCameraProxy = new CameraProxy(SuperZoomActivity.this);
        mPermissionGranted = false;
        configCameraId();
        checkPermission();
    }

    /**
     * 判断是否有前置摄像头
     */
    private void configCameraId() {
        if (mCameraProxy.getNumberOfCameras() <= 1) {
            // 后置
            mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
            ifCanSwitch();
            setFlashButtonEnable(true);
        } else {
            // 前置
            mCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
            setFlashButtonEnable(false);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {
        mAssetManager.setManagerlistener(new NvAssetManager.NvAssetManagerListener() {
            @Override
            public void onRemoteAssetsChanged(boolean hasNext) {
                Message updateMessage = mHandler.obtainMessage();
                updateMessage.what = ASSET_LIST_REQUEST_SUCCESS;
                mHandler.sendMessage(updateMessage);
            }

            @Override
            public void onGetRemoteAssetsFailed() {

            }

            @Override
            public void onDownloadAssetProgress(String uuid, int progress) {
                // 下载进度
                for (int index = 0, length = fxNames.length; index < length; index++) {
                    if (TextUtils.equals(list.get(index).uuid, uuid)) {
                        list.get(index).downloadProgress = progress;
                        if (mHandler != null) {
                            Message updateMessage = mHandler.obtainMessage();
                            updateMessage.what = ASSET_DOWNLOAD_INPROGRESS;
                            mHandler.sendMessageDelayed(updateMessage, 300);
                        }
                        return;
                    }
                }
            }

            @Override
            public void onDonwloadAssetFailed(String uuid) {
                for (int index = 0, length = fxNames.length; index < length; index++) {
                    if (TextUtils.equals(list.get(index).uuid, uuid)) {
                        list.get(index).downloadStatus = NvAsset.DownloadStatusFailed;
                        Message updateMessage = mHandler.obtainMessage();
                        updateMessage.what = ASSET_DOWNLOAD_INPROGRESS;
                        updateMessage.arg1 = index;
                        mHandler.sendMessage(updateMessage);
                        return;
                    }
                }
            }

            @Override
            public void onDonwloadAssetSuccess(String uuid) {
                // 下载完后重新更新
                ArrayList<NvAsset> localData = getLocalData(NvAsset.ASSET_SUPER_ZOOM);
                for (NvAsset nvAsset : localData) {
                    for (int index = 0, length = fxNames.length; index < length; index++) {
                        if (TextUtils.equals(list.get(index).uuid, nvAsset.uuid)) {
                            list.get(index).localDirPath = nvAsset.localDirPath;
                            list.get(index).downloadStatus = NvAsset.DownloadStatusDecompressing;
                            continue;
                        }
                    }
                }
                Message updateMessage = mHandler.obtainMessage();
                updateMessage.what = ASSET_DOWNLOAD_INPROGRESS;
                mHandler.sendMessage(updateMessage);
            }

            @Override
            public void onFinishAssetPackageInstallation(String uuid) {
                // 解压完成
                ArrayList<NvAsset> localData = getLocalData(NvAsset.ASSET_SUPER_ZOOM);
                for (NvAsset nvAsset : localData) {
                    for (int index = 0, length = fxNames.length; index < length; index++) {
                        if (TextUtils.equals(list.get(index).uuid, nvAsset.uuid)) {
                            list.get(index).localDirPath = nvAsset.localDirPath;
                            list.get(index).downloadStatus = NvAsset.DownloadStatusFinished;
                            continue;
                        }
                    }
                }
                Message updateMessage = mHandler.obtainMessage();
                updateMessage.what = ASSET_DOWNLOAD_INPROGRESS;
                mHandler.sendMessage(updateMessage);
            }

            @Override
            public void onFinishAssetPackageUpgrading(String uuid) {

            }
        });
        mButtonFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonFlash.isEnabled()) {
                    if (mCameraID == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        return;
                    }
                    mFlashToggle = !mFlashToggle;
                    mCameraProxy.toggleFlash(mFlashToggle);
                    mButtonFlash.setBackground(null);
                    if (mFlashToggle) {
                        mButtonFlash.setBackgroundResource(R.mipmap.icon_flash_on);
                    } else {
                        mButtonFlash.setBackgroundResource(R.mipmap.icon_flash_off);
                    }
                }
            }
        });

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishActivity(SuperZoomActivity.class);
            }
        });
        // 点击录制
        mButtonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 未录制状态下
                if (!mIsRecording) {
                    // 判断有无素材
                    for (int index = 0, length = fxNames.length; index < length; index++) {
                        if (TextUtils.equals(list.get(index).uuid, mZoomFx)) {
                            if (TextUtils.isEmpty(list.get(index).localDirPath) && TextUtils.isEmpty(list.get(index).bundledLocalDirPath)) {
                                //无资源
                                String[] versionName = getResources().getStringArray(R.array.super_zoom_resouce_tips);
                                Util.showDialog(SuperZoomActivity.this, versionName[0], versionName[1]);
                                return;
                            }
                        }
                    }
                }
                long currentTimeMillis = System.currentTimeMillis();
                if (mIsRecording) {
                    if ((currentTimeMillis - lastClickTime) > 1300 && (currentTimeMillis - lastClickTime) < 4000) {
                        // 禁止按钮点击
                        setRecordButtonEnable(false);
                        // 结束录制
                        mGLView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                mNvSuperZoom.stop();
                            }
                        });
                        stopRecording();
                        return;
                    } else {
                        return;
                    }
                }
                // 人脸检测
                mDetectFace = false;
                // 开始特效
                if (mZoomFxData != null && !TextUtils.isEmpty(mZoomFxData.localDirPath)) {
                    mNvSuperZoom.setAssetsExternalPath(PathUtils.getAssetDownloadPath(NvAsset.ASSET_SUPER_ZOOM));
                } else if (mZoomFxData != null && !TextUtils.isEmpty(mZoomFxData.bundledLocalDirPath)) {
                    mNvSuperZoom.setAssetsExternalPath(null);
                }
                mNvSuperZoom.start(mZoomFx, mCurrentVideoResolution.imageWidth, mCurrentVideoResolution.imageHeight, mAnchorX, mAnchorY);
                //开始动画
                startRecordProgress();
                // 理解为开始录制
                startRecording();
                lastClickTime = currentTimeMillis;
            }
        });
        // 未录制状态点击视图
        mGLView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mIsRecording) {
                    mDetectFace = false;
                    mAnchorX = event.getX() / mGLView.getWidth() - 0.5f;
                    mAnchorY = -(event.getY() / mGLView.getHeight() - 0.5f);
                }
                return true;
            }
        });
        // 切换摄像头
        mButtonSwitchFacing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCameraProxy.getNumberOfCameras() <= 1) {
                    return;
                }
                if (mCameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    // 闪光灯标示不可用
                    setFlashButtonEnable(false);
                    mFlashToggle = false;
                } else if (mCameraID == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    // 闪光灯标示可用
                    setFlashButtonEnable(true);
                }
                switchCamera();
            }
        });
    }

    private void setFlashButtonEnable(boolean enable) {
        mButtonFlash.setBackground(null);
        if (enable) {
            mButtonFlash.setAlpha(1.0f);
            mButtonFlash.setBackgroundResource(R.mipmap.icon_flash_off);
            mButtonFlash.setClickable(true);
            mButtonFlash.setEnabled(true);
        } else {
            mButtonFlash.setAlpha(0.5f);
            mButtonFlash.setBackgroundResource(R.mipmap.icon_flash_off);
            mButtonFlash.setClickable(false);
            mButtonFlash.setEnabled(false);
        }
    }

    private NvAsset createSuperZoomEffectItem(String fxName) {
        NvAsset item = new NvAsset();
        if (TextUtils.equals("dramatization", fxName) || TextUtils.equals("spring", fxName)) {
            item.bundledLocalDirPath = "file:///android_asset/meicam";
        }
        item.uuid = fxName;
        return item;
    }

    private void startRecordProgress() {
        if (animator == null) {
            animator = ObjectAnimator.ofInt(mButtonRecord, "progress", 0, 100);
            animator.setDuration(4080);
            animator.start();
        }
    }

    private void clearObjectAnimation() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }

    /**
     * 初始化推镜特效列表
     */
    private void searchAssetData() {
        //  搜索本地数据数据 检索数据后 判断是不是本地存在  结束后刷新adapter
        //  点击下载后 直接更新adapter 随后存储数据（等待下一次app启动的时候，进行本地搜索）
        for (String fxName : fxNames) {
            list.add(createSuperZoomEffectItem(fxName));
        }
        // 搜索本地存储文件 更新list中记录下载状态
        mAssetManager = NvAssetManager.sharedInstance();
       /* String bundlePath = "meicam";
        // 搜索预装
        mAssetManager.searchReservedAssets(NvAsset.ASSET_SUPER_ZOOM, bundlePath);*/
        // 搜索本地（已经下载） 只搜索一次
        mAssetManager.searchLocalAssets(NvAsset.ASSET_SUPER_ZOOM);
        // 得到可用资源
        ArrayList<NvAsset> localData = getLocalData(NvAsset.ASSET_SUPER_ZOOM);
        // 遍历修改list中的数据的状态
        for (NvAsset nvAsset : localData) {
            for (int index = 0, length = fxNames.length; index < length; index++) {
                if (TextUtils.equals(list.get(index).uuid, nvAsset.uuid)) {
                    list.get(index).localDirPath = nvAsset.localDirPath;
                    continue;
                }
            }
        }
        mAssetManager.downloadRemoteAssetsInfo(NvAsset.ASSET_SUPER_ZOOM, NvAsset.AspectRatio_All, 0, 0, 20);
    }

    private ArrayList<NvAsset> getLocalData(int assetType) {
        return mAssetManager.getUsableAssets(assetType, NvAsset.AspectRatio_All, 0);
    }

    /**
     * 选择摄像头
     */
    private boolean setupCamera() {
        if (mCameraProxy.getCamera() == null) {
            if (mCameraProxy.getNumberOfCameras() == 1) {
                mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
            }
        }
        mCameraProxy.stopPreview();
        if (!mCameraProxy.openCamera(mCameraID)) {
            Log.d(TAG, "no camera permission , can't open camera");
            return false;
        }
        boolean isHuaweiP6 = checkMobileModel();
        if (isHuaweiP6) {
            mCameraProxy.setPreviewSize(640, 480);
        } else {
            mCameraProxy.setPreviewSize(1280, 720);
        }
        mOrientation = Accelerometer.getDisplayOrientation(this, mCameraProxy.getCameraId());
        return true;
    }

    private boolean checkMobileModel() {
        String model = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        if (model == null || manufacturer == null) {
            return false;
        }
        boolean isHuaweiP6 = false;
        model = model.toUpperCase();
        manufacturer = manufacturer.toUpperCase();
        if ("HUAWEI P6-C00".equals(model) && "HUAWEI".equals(manufacturer)) {
            isHuaweiP6 = true;
        }
        return isHuaweiP6;
    }

    /**
     * 切换摄像头
     */
    public void switchCamera() {
        if (mCameraProxy.cameraOpenFailed()) {
            return;
        }
        synchronized (mGLThreadSyncObject) {
            mIsPreviewing = false;
        }
        mCameraID = 1 - mCameraID;
        startCapturePreview();
    }

    private void checkPermission() {
        // 6.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)) {
                if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)) {
                    if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        mPermissionGranted = true;
                        setCaptureEnabled(true);
                        startCapturePreview();
                    } else {
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
                    }
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION_CODE);
                }
            } else {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION_CODE);
            }
        } else {
            // 6.0以下
            mPermissionGranted = true;
            startCapturePreview();
        }
    }

    /**
     * 无权限界面不可用
     */
    private void setCaptureEnabled(boolean enabled) {
        mButtonRecord.setEnabled(enabled);
        mButtonSwitchFacing.setEnabled(enabled);
        mButtonFlash.setEnabled(enabled);
    }

    /**
     * 设置切换摄像头是否可用
     */
    private void ifCanSwitch() {
        if (mCameraProxy.getNumberOfCameras() > 1) {
            mButtonSwitchFacing.setClickable(true);
            mButtonSwitchFacing.setEnabled(true);
            mButtonSwitchFacing.setAlpha(1.0F);
        } else {
            mButtonSwitchFacing.setClickable(false);
            mButtonSwitchFacing.setEnabled(false);
            mButtonSwitchFacing.setAlpha(0.5F);
        }
    }

    /**
     * 开始预览
     */
    private boolean startCapturePreview() {
        if (!mPermissionGranted || mIsPreviewing || mCameraPreviewTexture == null) {
            return false;
        }
        if (setupCamera()) {
            // 有权限(根据是否打开摄像头同时判断是否有摄像头权限)
            setCaptureEnabled(true);
            Log.d(TAG, "below 6.0 devices has access");
        } else {
            // 无权限
            setCaptureEnabled(false);
            noPermissionDialog();
            Log.d(TAG, "below 6.0 devices has no access");
            return false;
        }
        mCameraProxy.startPreview(mCameraPreviewTexture, mPreviewCallback);
        Camera.Size size = mCameraProxy.getPreviewSize();
        mCurrentVideoResolution = new NvsVideoResolution();
        if (mOrientation == 90 || mOrientation == 270) {
            mCurrentVideoResolution.imageWidth = size.height;
            mCurrentVideoResolution.imageHeight = size.width;
        } else {
            mCurrentVideoResolution.imageWidth = size.width;
            mCurrentVideoResolution.imageHeight = size.height;
        }
        mCurrentVideoResolution.imagePAR = new NvsRational(1, 1);
        mFlipHorizontal = mCameraProxy.isFlipHorizontal();
        synchronized (mGLThreadSyncObject) {
            mIsPreviewing = true;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void createEGLContext() {
        // 获取显示设备
        mEglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEglDisplay == EGL14.EGL_NO_DISPLAY) {
            Log.e(TAG, "eglGetDisplay failed");
            return;
        }
        // 初始化EGL
        final int[] version = new int[2];
        if (!EGL14.eglInitialize(mEglDisplay, version, 0, version, 1)) {
            mEglDisplay = null;
            Log.e(TAG, "eglInitialize failed");
            return;
        }
        // 选择配置
        android.opengl.EGLConfig mEglConfig = getConfig(false, true, mEglDisplay);
        if (mEglConfig == null) {
            Log.e(TAG, "chooseConfig failed");
            return;
        }

        // 创建上下文
        EGLContext currentContext = EGL14.eglGetCurrentContext();
        final int[] attribList = {
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL14.EGL_NONE
        };
        mEglContext = EGL14.eglCreateContext(mEglDisplay, mEglConfig, currentContext, attribList, 0);
        if (mEglContext == EGL14.EGL_NO_CONTEXT) {
            Log.e(TAG, "eglCreateContext");
        }
        // 关联EGLContext和渲染表面
//        EGLSurface currentSurface = EGL14.eglGetCurrentSurface(EGL14.EGL_DRAW);
//        if (!EGL14.eglMakeCurrent(mEglDisplay, currentSurface, currentSurface, mEglContext)) {
//            Log.e(TAG, "eglMakeCurrent failed");
//        }
    }

    private void createShaderAndProgram() {
        final String vertexShader = RawResourceReader.readTextFileFromRawResource(this, R.raw.vetext_sharder);
        final String fragmentShader = RawResourceReader.readTextFileFromRawResource(this, R.raw.fragment_sharder);

        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        mShaderProgram = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, new String[]{"texture", "vPosition", "vTexCoordinate", "textureTransform"});

        GLES20.glUseProgram(mShaderProgram);

        mTextureHandle = GLES20.glGetUniformLocation(mShaderProgram, "texture");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mShaderProgram, "vTexCoordinate");
        mPositionHandle = GLES20.glGetAttribLocation(mShaderProgram, "vPosition");
        mTextureTransformHandle = GLES20.glGetUniformLocation(mShaderProgram, "textureTransform");
    }

    private void createVertexBuffer() {
        // Draw list buffer
        mDrawListBuffer = ByteBuffer.allocateDirect(m_drawOrder.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        mDrawListBuffer.put(m_drawOrder);
        mDrawListBuffer.position(0);

        // Initialize the texture holder
        mVertexBuffer = ByteBuffer.allocateDirect(m_squareCoords.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.put(m_squareCoords);
        mVertexBuffer.position(0);
    }

    private void createAndBindTexture() {
        mCameraPreviewTextures = new int[1];
        textureBuffer = ByteBuffer.allocateDirect(textureCoords.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        textureBuffer.put(textureCoords);
        textureBuffer.position(0);

        // Generate the actual texture
        GLES20.glGenTextures(mCameraPreviewTextures.length, mCameraPreviewTextures, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        EGLHelper.checkGlError("Texture generate");
        mCameraPreviewTexture = new SurfaceTexture(mCameraPreviewTextures[0]);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mCameraPreviewTextures[0]);

        //创建摄像机需要的Preview Texture
        if (mSurfaceAvailableThread == null) {
            mSurfaceAvailableThread = new HandlerThread("ProcessImageThread");
            mSurfaceAvailableThread.start();
            mSurfaceAvailableHandler = new Handler(mSurfaceAvailableThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                }
            };
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCameraPreviewTexture.setOnFrameAvailableListener(this, mSurfaceAvailableHandler);
        } else {
            mCameraPreviewTexture.setOnFrameAvailableListener(this);
        }
        if (mHandler != null) {
            mHandler.sendEmptyMessage(START_RECORDING_CODE);
        }
    }

    private void drawTextureOES(int displayTex, int texWidht, int texHeight) {
        // Draw texture
        GLES20.glUseProgram(mShaderProgram);

        float ar = (float) texWidht / texHeight;
        float disar = (float) mWidth / mHeight;
        float cropWidth = 1.0f;
        float cropHeight = (float) 1.0;
        if (ar > disar) {
            cropHeight = 1.0f;
            cropWidth = ar / disar;
        } else {
            cropWidth = 1.0f;
            cropHeight = disar / ar;
        }
        mVertexBuffer.put(0, -cropWidth);
        mVertexBuffer.put(1, cropHeight);
        mVertexBuffer.put(2, -cropWidth);
        mVertexBuffer.put(3, -cropHeight);
        mVertexBuffer.put(4, cropWidth);
        mVertexBuffer.put(5, -cropHeight);
        mVertexBuffer.put(6, cropWidth);
        mVertexBuffer.put(7, cropHeight);
        mVertexBuffer.position(0);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);

        EGLHelper.checkGlError("glEnableVertexAttribArray");
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, displayTex);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(mTextureHandle, 0);

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 4, GLES20.GL_FLOAT, false, 0, textureBuffer);
        GLES20.glUniformMatrix4fv(mTextureTransformHandle, 1, false, mVideoTextureTransform, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, m_drawOrder.length, GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        GLES20.glUseProgram(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                noPermissionDialog();
                return;
            }
            switch (requestCode) {
                case REQUEST_CAMERA_PERMISSION_CODE:
                    if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)) {
                        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            mPermissionGranted = true;
                            startCapturePreview();
                        } else {
                            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
                        }
                    } else {
                        requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION_CODE);
                    }
                    break;
                case REQUEST_RECORD_AUDIO_PERMISSION_CODE:
                    if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        mPermissionGranted = true;
                        startCapturePreview();
                    } else {
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
                    }
                    break;
                case REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE:
                    mPermissionGranted = true;
                    startCapturePreview();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this) {
            mFrameAvailable = true;
        }
        mGLView.requestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        createEGLContext();
        createShaderAndProgram();
//      createVertexBuffer();
//      createAndBindTexture();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                startCapturePreview();
//            }
//        });
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
        GLES20.glViewport(0, 0, width, height);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onDrawFrame(GL10 gl) {
        drawFrameToGLView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void drawFrameToGLView() {
        //计算当前的时间戳
        synchronized (this) {
            if (mFrameAvailable) {
                mCameraPreviewTexture.updateTexImage();
                mCameraPreviewTexture.getTransformMatrix(mVideoTextureTransform);
                mFrameAvailable = false;
            }
        }
        boolean isInPreview = false;
        synchronized (mGLThreadSyncObject) {
            isInPreview = mIsPreviewing;
        }

        int texWidth = mWidth;
        int texHeight = mHeight;
        mDisplayTex = mCameraPreviewTextures[0];

        if (isInPreview && mNvSuperZoom != null) {
            texWidth = mCurrentVideoResolution.imageWidth;
            texHeight = mCurrentVideoResolution.imageHeight;
            if (mIsRecording) {
                long timestamp = mCameraPreviewTexture.getTimestamp();
                mDisplayTex = mNvSuperZoom.render(mCameraPreviewTextures[0], true, mCameraProxy.getOrientation(), mFlipHorizontal);
                if (mZoomFx != null) {
                    if (mNvSuperZoom.renderEnded()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stopRecording();
                            }
                        });
                    }
                }
                GLES20.glFinish();
                synchronized (this) {
                    if (mVideoEncoder != null) {
                        if (mNeedResetEglContext) {
                            mVideoEncoder.setEglContext(mEglContext, mDisplayTex);
                            mNeedResetEglContext = false;
                        }
                        mVideoEncoder.frameAvailableSoon(timestamp);
                    }
                }
            }
        }
        GLES20.glViewport(0, 0, mWidth, mHeight);
        if (mDisplayTex == mCameraPreviewTextures[0]) {
            this.drawTextureOES(mDisplayTex, texWidth, texHeight);
        } else {
            if (mNvSuperZoom != null) {
                drawTexture(mDisplayTex, texWidth, texHeight, mWidth, mHeight);
            }
        }
        EGLHelper.checkGlError("drawTexture");
    }

    private int mDrawTextureProgramID = -1;
    private FloatBuffer mDrawTextureBuffer;
    private FloatBuffer mDrawGLCubeBuffer;

    private static final int EGL_RECORDABLE_ANDROID = 0x3142;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private android.opengl.EGLConfig getConfig(final boolean withDepthBuffer,
                                               final boolean isRecordable, EGLDisplay mEglDisplay) {
        final int[] attribList = {
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_ALPHA_SIZE, 8,
                // EGL14.EGL_STENCIL_SIZE, 8,
                EGL14.EGL_NONE, EGL14.EGL_NONE,
                /*EGL_RECORDABLE_ANDROID, 1,
                this flag need to recording of MediaCodec*/
                EGL14.EGL_NONE, EGL14.EGL_NONE,
                //	withDepthBuffer ? EGL14.EGL_DEPTH_SIZE : EGL14.EGL_NONE,
                EGL14.EGL_NONE, EGL14.EGL_NONE,
                // withDepthBuffer ? 16 : 0,
                EGL14.EGL_NONE
        };
        int offset = 10;
        if (false) {
            attribList[offset++] = EGL14.EGL_STENCIL_SIZE;
            attribList[offset++] = 8;
        }
        if (withDepthBuffer) {
            attribList[offset++] = EGL14.EGL_DEPTH_SIZE;
            attribList[offset++] = 16;
        }
        if (isRecordable && (Build.VERSION.SDK_INT >= 18)) {
            attribList[offset++] = EGL_RECORDABLE_ANDROID;
            attribList[offset++] = 1;
        }
        for (int i = attribList.length - 1; i >= offset; i--) {
            attribList[i] = EGL14.EGL_NONE;
        }
        final android.opengl.EGLConfig[] configs = new android.opengl.EGLConfig[1];
        final int[] numConfigs = new int[1];
        if (!EGL14.eglChooseConfig(mEglDisplay, attribList, 0, configs, 0, configs.length, numConfigs, 0)) {
            // XXX it will be better to fallback to RGB565
            Log.w(TAG, "unable to find RGBA8888 / " + " EGLConfig");
            return null;
        }
        return configs[0];
    }

    /**
     * 输入结果到GLSurfaceView，必须在opengl环境中运行
     */
    public void drawTexture(int displayTex, int texWidth, int texHeight, int displayWidth,
                            int displayHeight) {
        // Draw texture
        if (mDrawTextureProgramID < 0) {
            mDrawTextureProgramID = EGLHelper.loadProgramForTexture();
            mDrawGLCubeBuffer = ByteBuffer.allocateDirect(EGLHelper.CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            mDrawGLCubeBuffer.put(EGLHelper.CUBE).position(0);
            mDrawTextureBuffer = ByteBuffer.allocateDirect(EGLHelper.TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            mDrawTextureBuffer.clear();
            mDrawTextureBuffer.put(EGLHelper.TEXTURE_NO_ROTATION).position(0);
        }
        float ar = (float) texWidth / texHeight;
        float disar = (float) displayWidth / displayHeight;
        float cropWidth = 1.0f;
        float cropHeight = (float) 1.0;
        if (ar > disar) {
            cropHeight = 1.0f;
            cropWidth = ar / disar;
        } else {
            cropWidth = 1.0f;
            cropHeight = disar / ar;
        }
        mDrawGLCubeBuffer.put(0, -cropWidth);
        mDrawGLCubeBuffer.put(1, cropHeight);
        mDrawGLCubeBuffer.put(2, cropWidth);
        mDrawGLCubeBuffer.put(3, cropHeight);
        mDrawGLCubeBuffer.put(4, -cropWidth);
        mDrawGLCubeBuffer.put(5, -cropHeight);
        mDrawGLCubeBuffer.put(6, cropWidth);
        mDrawGLCubeBuffer.put(7, -cropHeight);
        mDrawGLCubeBuffer.position(0);
        GLES20.glUseProgram(mDrawTextureProgramID);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, displayTex);

        mDrawGLCubeBuffer.position(0);
        int glAttribPosition = GLES20.glGetAttribLocation(mDrawTextureProgramID, "position");
        GLES20.glVertexAttribPointer(glAttribPosition, 2, GLES20.GL_FLOAT, false, 0, mDrawGLCubeBuffer);
        GLES20.glEnableVertexAttribArray(glAttribPosition);

        mDrawTextureBuffer.position(0);
        int glAttribTextureCoordinate = GLES20.glGetAttribLocation(mDrawTextureProgramID, "inputTextureCoordinate");
        GLES20.glVertexAttribPointer(glAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0, mDrawTextureBuffer);
        GLES20.glEnableVertexAttribArray(glAttribTextureCoordinate);

        int textUniform = GLES20.glGetUniformLocation(mDrawTextureProgramID, "inputImageTexture");
        GLES20.glUniform1i(textUniform, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(glAttribPosition);
        GLES20.glDisableVertexAttribArray(glAttribTextureCoordinate);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glUseProgram(0);
    }

    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(final byte[] data, Camera camera) {
            if (mDetectFace) {
                int cameraOrientation = mCameraProxy.getOrientation();
                int bufferWidth = mCurrentVideoResolution.imageWidth;
                int bufferHeight = mCurrentVideoResolution.imageHeight;
                if (cameraOrientation == 90 || cameraOrientation == 270) {
                    bufferWidth = mCurrentVideoResolution.imageHeight;
                    bufferHeight = mCurrentVideoResolution.imageWidth;
                }
                STHumanAction humanAction = mStMobileDetected.stMobileDetected(data, bufferWidth, bufferHeight, cameraOrientation, mFlipHorizontal);
                Rect rect = mStMobileDetected.getFaceRect(humanAction);
                if (rect != null) {
                    float centerX = (rect.right + rect.left) / 2;
                    float centerY = (rect.bottom + rect.top) / 2;
                    mAnchorX = centerX - 0.5f;
                    mAnchorY = -centerY + 0.5f;
                }
            }
        }
    };

    private final MediaEncoder.MediaEncoderListener mMediaEncoderListener = new MediaEncoder.MediaEncoderListener() {
        @Override
        public void onPrepared(final MediaEncoder encoder) {
            if (encoder instanceof MediaVideoEncoder) {
                setVideoEncoder((MediaVideoEncoder) encoder);
            }
        }

        @Override
        public void onStopped(final MediaEncoder encoder) {
            if (encoder instanceof MediaVideoEncoder) {
                setVideoEncoder(null);
            }
        }
    };

    private MediaMuxerWrapper mMuxer;

    private void startRecording() {
        setViewsVisibility(View.INVISIBLE);
        mNeedResetEglContext = true;
        try {
            mMuxer = new MediaMuxerWrapper(".mp4");
            mMuxer.mHandler = mHandler;
            new MediaVideoEncoder(mMuxer, mMediaEncoderListener, mCurrentVideoResolution.imageWidth, mCurrentVideoResolution.imageHeight);
            mMuxer.prepare();
            mMuxer.startRecording();
        } catch (final IOException e) {
            Log.e(TAG, "startCapture:", e);
        }
        mIsRecording = true;
    }

    private void stopRecording() {
        if (!mIsRecording) {
            return;
        }
        mIsRecording = false;
        if (mMuxer != null) {
            mMuxer.stopRecording();
        }
        setViewsVisibility(View.VISIBLE);
    }

    public void setVideoEncoder(final MediaVideoEncoder encoder) {
        mGLView.queueEvent(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                synchronized (this) {
                    if (encoder != null && mDisplayTex > 0) {
                        encoder.setEglContext(mEglContext, mDisplayTex);
                    }
                    mVideoEncoder = encoder;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNvSuperZoom = new NvSuperZoom(SuperZoomActivity.this);
        mGLView.onResume();
        mGLView.queueEvent(new Runnable() {
            @Override
            public void run() {
                createVertexBuffer();
                createAndBindTexture();
            }
        });
        clearObjectAnimation();
        setRecordButtonEnable(true);
        mAccelerometer.start();
    }

    @Override
    protected void onPause() {
        lastClickTime = 0;
        if (mIsRecording) {
            mGLView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    // 理解为特效停止
                    mNvSuperZoom.stop();
                }
            });
            stopRecording();
        }
        mIsRecording = false;
        mFlashToggle = false;
        if (mCameraProxy != null) {
            mCameraProxy.releaseCamera();
        }
        //停止引擎
        synchronized (mGLThreadSyncObject) {
            mIsPreviewing = false;
        }
        final CountDownLatch count = new CountDownLatch(1);
        mGLView.queueEvent(new Runnable() {
            @Override
            public void run() {
                count.countDown();
            }
        });

        try {
            count.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mAccelerometer != null) {
            mAccelerometer.stop();
        }
        // 修正摄像头状态
        if (mCameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mButtonFlash.setBackgroundResource(R.mipmap.icon_flash_off);
        }
        if (mNvSuperZoom != null) {
            mGLView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    if (mDrawTextureProgramID > 0) {
                        GLES20.glDeleteProgram(mDrawTextureProgramID);
                    }
                    mDrawTextureProgramID = -1;
                    mNvSuperZoom.releaseResources();
                }
            });
        }
        mGLView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        clearObjectAnimation();
    }

    @Override
    protected void onDestroy() {
        mAccelerometer = null;
        if (mStMobileDetected != null) {
            mStMobileDetected.closeDetected();
            mStMobileDetected = null;
        }
        mGLView.queueEvent(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                if (mFrameBuffers != null) {
                    GLES20.glDeleteFramebuffers(1, mFrameBuffers, 0);
                    mFrameBuffers = null;
                }
                if (mConvertProgramID > 0) {
                    GLES20.glDeleteProgram(mConvertProgramID);
                }
                mConvertProgramID = -1;
                if (mDrawTextureProgramID > 0) {
                    GLES20.glDeleteProgram(mDrawTextureProgramID);
                }
                mDrawTextureProgramID = -1;
                if (mNvSuperZoom != null) {
                    mNvSuperZoom.releaseResources();
                    mNvSuperZoom = null;
                }
                if (mEglContext != null) {
                    EGL14.eglDestroyContext(mEglDisplay, mEglContext);
                    mEglDisplay = null;
                    mEglContext = null;
                }
            }
        });
        if (mEffectSdkContext != null) {
            NvsEffectSdkContext.close();
            mEffectSdkContext = null;
        }
        if (mCameraProxy != null) {
            mCameraProxy.releaseCamera();
            mCameraProxy = null;
        }
        if (mHandler != null) {
            mHandler.removeMessages(ASSET_DOWNLOAD_INPROGRESS);
            mHandler = null;
        }
        cancelDownloadTask();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mGLView.queueEvent(new Runnable() {
            @Override
            public void run() {
                mNvSuperZoom.stop();
            }
        });
        if (mMuxer != null && mMuxer.mHandler != null) {
            mMuxer.mHandler = null;
        }
        super.onBackPressed();
    }

    public void cancelDownloadTask() {
        ArrayList<String> pendingAssetsToDownloads = mAssetManager.getPendingAssetsToDownload();
        for (String uuid : pendingAssetsToDownloads) {
            mAssetManager.cancelAssetDownload(uuid);
        }
    }

    private void noPermissionDialog() {
        String[] permissionsTips = getResources().getStringArray(R.array.permissions_tips);
        Util.showDialog(SuperZoomActivity.this, permissionsTips[0], permissionsTips[1], new TipsButtonClickListener() {
            @Override
            public void onTipsButtoClick(View view) {
                AppManager.getInstance().finishActivity();
            }
        });
    }
}