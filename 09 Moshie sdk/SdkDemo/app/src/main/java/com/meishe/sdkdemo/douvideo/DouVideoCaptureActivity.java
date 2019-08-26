package com.meishe.sdkdemo.douvideo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsCaptureVideoFx;
import com.meicam.sdk.NvsFxDescription;
import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BasePermissionActivity;
import com.meishe.sdkdemo.douvideo.bean.RecordClip;
import com.meishe.sdkdemo.douvideo.bean.RecordClipsInfo;
import com.meishe.sdkdemo.douvideo.view.CountDownDurationAdjustView;
import com.meishe.sdkdemo.douvideo.view.TimeDownView;
import com.meishe.sdkdemo.download.AssetDownloadActivity;
import com.meishe.sdkdemo.edit.data.FilterItem;
import com.meishe.sdkdemo.edit.music.SelectMusicActivity;
import com.meishe.sdkdemo.edit.watermark.SingleClickActivity;
import com.meishe.sdkdemo.interfaces.TipsButtonClickListener;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.AssetFxUtil;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.ParameterSettingValues;
import com.meishe.sdkdemo.utils.PathUtils;
import com.meishe.sdkdemo.utils.ToastUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;
import com.meishe.sdkdemo.utils.dataInfo.MusicInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;
import com.meishe.sdkdemo.utils.dataInfo.VideoClipFxInfo;
import com.meishe.sdkdemo.view.FaceUPropView;
import com.meishe.sdkdemo.view.FilterView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.meishe.sdkdemo.utils.Constants.HUMAN_AI_TYPE_MS;
import static com.meishe.sdkdemo.utils.Constants.HUMAN_AI_TYPE_NONE;

public class DouVideoCaptureActivity extends BasePermissionActivity implements NvsStreamingContext.CaptureDeviceCallback,
        NvsStreamingContext.CaptureRecordingDurationCallback, NvsStreamingContext.CaptureRecordingStartedCallback,
        NvsStreamingContext.StreamingEngineCallback {
    private static final String TAG = "DouVideoCaptureActivity";
    private Context mContext;

    private static final int REQUEST_FILTER_LIST_CODE = 110;
    private static final int ARFACE_LIST_REQUES_CODE = 111;
    private static final int MUSIC_DATA_REQUES_CODE = 112;
    private static final int REQUEST_CODE_DOUYIN_EDIT = 113;// douyin

    private static final int SPEED_TXT_SELECT_COLOR = Color.parseColor("#ff4d4f51");
    private static final int SPEED_TXT_NORMAL_COLOR = Color.parseColor("#ffeaebeb");

    private static final String beautyFxName = "Beauty";
    private static final String BEAUTIFY_STRENGTH = "Strength";

    private static final String arSceneFxName = "AR Scene";
    private static final String arSceneBeauty = "Beauty Effect";//美颜
    private static final String arSceneBeautyShape = "Beauty Shape";//美型

    private static final String BEAUTIFY_ARSCENE_STRENGTH = "Beauty Strength";//人脸美颜磨皮
    private static final String BEAUTIFY_SHAPE_EYE_ENLARGING = "Eye Size Warp Degree";//大眼
    private static final String BEAUTIFY_SHAPE_CHECK_THINNING = "Face Size Warp Degree";//瘦脸
    private static final int SEEKBAR_BEAUTYSHAPE_MAXVALUE = 200;

    public static final String INTENT_KEY_STRENGTH = "BeautyStrength";
    public static final String INTENT_KEY_CHEEK = "CheekThinning";
    public static final String INTENT_KEY_EYE = "EyeEnlarging";
    private int mIntentStrength = -1;
    private int mIntentCheekThinning = -1;
    private int mIntentEyeEnlarging = -1;


    private static final int MESSAGE_RECORD_FINISH = 2;
    private static final int MESSAGE_RECORD_STOPRECORDING = 3;
    // 片段最小录制时长 1s
    private final int CLIP_MIN_RECORD_DURATION = 1 * 1000 * 1000;
    // 最小录制总时长
    private final int MIN_RECORD_DURATION = 4 * 1000 * 1000;
    // 最大录制总时长 15s
    private final int MAX_RECORD_DURATION = 15 * 1000 * 1000;

    private NvAssetManager mAssetManager;
    // 界面
    private RecordProgress mRecordProgress;
    private ImageButton mCloseBtn;
    private RelativeLayout mTopLayout;
    private LinearLayout mSelectMusic;
    private ImageView mMusicIcon;
    private TextView mMusicName;
    private LinearLayout mSwitchCameraBtn;
    private LinearLayout mBeautyBtn;
    private LinearLayout mFilterBtn;
    private LinearLayout mCountdownBtn;
    private LinearLayout mFaceUBtn;

    private ImageView mRecordBtn;
    private LinearLayout mOperationLayout;
    private ImageView mDeleteBtn;
    private ImageView mNextBtn;
    private RelativeLayout mSpeedItemLayout;
    private Button mSuperSlowSpeedBtn;
    private Button mSlowSpeedBtn;
    private Button mStandardSpeedBtn;
    private Button mFastSpeedBtn;
    private Button mSuperFastSpeedBtn;
    private LinearLayout mBottomOperationLayout;
    private LinearLayout mPhotoAlbum;//相册

    //美化
    private NvsCaptureVideoFx mBeautyVideoFx;//美颜对象
    private AlertDialog mBeautifyDialog;
    private View mBeautifyView;
    private SeekBar mStrengthSeekBar;
    private SeekBar mCheekThinningSeekBar;
    private SeekBar mEyeEnlargingSeekBar;
    private TextView mStrengthValue;
    private TextView mCheekThinningValue;
    private TextView mEyeEnlargingValue;

    //滤镜
    private AlertDialog mFilterDialog;
    private FilterView mFilterView;
    private NvsCaptureVideoFx mCurCaptureVideoFx;
    private ArrayList<FilterItem> mFilterDataArrayList = new ArrayList<>();
    private int mFilterSelPos = 0;
    private VideoClipFxInfo mVideoClipFxInfo = new VideoClipFxInfo();

    //倒计时拍摄
    private AlertDialog mCountDownDialog;
    private View mCountDownView;
    private CountDownDurationAdjustView mCountDownCaptureView;
    private Button mStartCountDownCapture;
    /**
     * 倒计时文本控件
     */
    private TimeDownView mCountDownTextView;
    private long mCurCountDownCapDuration = 0;//倒计时分段拍摄视频的时长

    //道具
    private int mCanUseARFaceType = HUMAN_AI_TYPE_NONE;//人脸特效是否可用的标识，默认普通版，不带人脸功能
    private NvsCaptureVideoFx mARFaceU;
    private String mArSceneId = "";
    private AlertDialog mFaceUPropDialog;
    private FaceUPropView mFaceUPropView;
    private ArrayList<FilterItem> mPropDataArrayList = new ArrayList<>();
    private int mFaceUPropSelPos = 0;
    private boolean mInitArScene;

    // 音乐
    private MusicInfo mCurMusicInfo;
    private MusicPlayer mMusicPlayer;
    private boolean mIsUsedMusic = false;//抖视频默认不使用音乐
    private long mMusicStartPos = 0;
    private long mMusicCurPlayPos = 0;

    private boolean mLocalAssetsLoadForOnce = true;//在所有权限允许之后，对本地素材查询及加载操作进行一次的标志位，防止不必要的重复加载和查询。

    private NvsLiveWindow mLivewindow;
    private int mCurrentDeviceIndex;

    // 录制
    private String mCurRecordVideoPath;
    private RecordClipsInfo mRecordClipsInfo;
    private long mCurVideoDuration = 0; // 累计录制视频的时长，包括正在录制的视频的时长
    private float mCurSpeed = 1f;

    private RecordHandler mHandler;
    private boolean misRecordFinished = false;

    boolean mSupportAutoFocus = false;
    boolean mSupportAutoExposure = false;

    // 聚焦
    private ImageView mImageAutoFocusRect;
    // 聚焦动画
    AlphaAnimation mFocusAnimation;

    @Override
    protected int initRootView() {
        return R.layout.activity_dou_video_capture;
    }

    @Override
    protected void initViews() {
        mContext = this;
        mRecordProgress = (RecordProgress) findViewById(R.id.record_progress);
        mTopLayout = (RelativeLayout) findViewById(R.id.topLayout);
        mCloseBtn = (ImageButton) findViewById(R.id.close_btn);
        mSelectMusic = (LinearLayout) findViewById(R.id.selectMusic);
        mMusicName = (TextView) findViewById(R.id.musicName);
        mMusicIcon = (ImageView) findViewById(R.id.musicIcon);

        mSwitchCameraBtn = (LinearLayout) findViewById(R.id.switch_camera_layout);
        mBeautyBtn = (LinearLayout) findViewById(R.id.beauty_layout);
        mFilterBtn = (LinearLayout) findViewById(R.id.filter_layout);
        mCountdownBtn = (LinearLayout) findViewById(R.id.countdown_layout);
        mFaceUBtn = (LinearLayout) findViewById(R.id.faceU_layout);

        mLivewindow = (NvsLiveWindow) findViewById(R.id.livewidow);
        mRecordBtn = (ImageView) findViewById(R.id.record_btn);
        mOperationLayout = (LinearLayout) findViewById(R.id.operation_layout);
        mDeleteBtn = (ImageView) findViewById(R.id.delete);
        mNextBtn = (ImageView) findViewById(R.id.next);
        mSpeedItemLayout = (RelativeLayout) findViewById(R.id.speed_item_layout);
        mSuperSlowSpeedBtn = (Button) findViewById(R.id.super_slow_speed_btn);
        mSlowSpeedBtn = (Button) findViewById(R.id.slow_speed_btn);
        mStandardSpeedBtn = (Button) findViewById(R.id.standard_speed_btn);
        mFastSpeedBtn = (Button) findViewById(R.id.fast_speed_btn);
        mSuperFastSpeedBtn = (Button) findViewById(R.id.super_fast_speed_btn);
        mBottomOperationLayout = (LinearLayout) findViewById(R.id.bottom_operation_layout);

        LayoutInflater inflater = LayoutInflater.from(this);
        /*美化*/
        mBeautifyView = inflater.inflate(R.layout.douyin_beauty_view, null);
        mStrengthSeekBar = (SeekBar) mBeautifyView.findViewById(R.id.strengthSeekBar);
        mCheekThinningSeekBar = (SeekBar) mBeautifyView.findViewById(R.id.cheekThinningSeekBar);
        mEyeEnlargingSeekBar = (SeekBar) mBeautifyView.findViewById(R.id.eyeEnlargingSeekBar);
        mStrengthValue = (TextView) mBeautifyView.findViewById(R.id.strengthValue);
        mCheekThinningValue = (TextView) mBeautifyView.findViewById(R.id.cheekThinningValue);
        mEyeEnlargingValue = (TextView) mBeautifyView.findViewById(R.id.eyeEnlargingValue);

        //倒计时拍摄
        mCountDownView = inflater.inflate(R.layout.douyin_countdown_view, null);
        mCountDownCaptureView = (CountDownDurationAdjustView) mCountDownView.findViewById(R.id.countDownCaptureView);
        mStartCountDownCapture = (Button) mCountDownView.findViewById(R.id.startCountDownCapture);
        mCountDownTextView = (TimeDownView) findViewById(R.id.countDownTextView);

        // 聚焦
        mImageAutoFocusRect = (ImageView) findViewById(R.id.imageAutoFocusRect);
        //相册
        mPhotoAlbum = (LinearLayout) findViewById(R.id.photoAlbum);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                mInitArScene = bundle.getBoolean("initArScene");
                mIntentStrength = bundle.getInt(INTENT_KEY_STRENGTH, -1);
                mIntentCheekThinning = bundle.getInt(INTENT_KEY_CHEEK, -1);
                mIntentEyeEnlarging = bundle.getInt(INTENT_KEY_EYE, -1);
            }
        }
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initData() {
        initDouyinData();
        //美化
        initBeautifyDialog();
        initBeautifyParam();
        //滤镜
        initFilterDialog();
        //倒计时
        initCountDownDialog();
        //道具
        initFacUPropDialog();
    }

    @Override
    protected void initListener() {
        mCloseBtn.setOnClickListener(this);
        mSelectMusic.setOnClickListener(this);
        mSwitchCameraBtn.setOnClickListener(this);
        mFilterBtn.setOnClickListener(this);
        mBeautyBtn.setOnClickListener(this);
        mCountdownBtn.setOnClickListener(this);
        mFaceUBtn.setOnClickListener(this);

        mRecordBtn.setOnClickListener(this);
        mSuperSlowSpeedBtn.setOnClickListener(this);
        mSlowSpeedBtn.setOnClickListener(this);
        mStandardSpeedBtn.setOnClickListener(this);
        mFastSpeedBtn.setOnClickListener(this);
        mSuperFastSpeedBtn.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mPhotoAlbum.setOnClickListener(this);

        mStrengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    strengthSeekBarChange(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mCheekThinningSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mInitArScene) {
                    cheekThinningSeekBarChange(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mEyeEnlargingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mInitArScene) {
                    eyeEnlargingSeekBarChange(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mCountDownCaptureView.setNewDurationChangeListener(new CountDownDurationAdjustView.OnCaptureDurationChangeListener() {
            @Override
            public void onNewDurationChange(long newDuration, boolean isDragEnd) {
                mCurCountDownCapDuration = newDuration;
                Logger.e(TAG, "mCurCountDownCapDuration = " + mCurCountDownCapDuration);
            }
        });

        mStartCountDownCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCaptureDialogView(mCountDownDialog);
                mCountDownTextView.setVisibility(View.VISIBLE);
                mCountDownTextView.downSecond(2);//3秒
            }
        });
        mCountDownTextView.setOnTimeDownListener(new TimeDownView.DownTimeWatcher() {
            @Override
            public void onTime(int num) {

            }

            @Override
            public void onLastTime(int num) {

            }

            @Override
            public void onLastTimeFinish(int num) {
                mCountDownTextView.setVisibility(View.GONE);
                mCountDownTextView.closeDefaultAnimate();
                startRecording();
            }
        });

        mMusicPlayer.setPlayListener(new MusicPlayer.OnPlayListener() {
            @Override
            public void onMusicPlay() {

            }

            @Override
            public void onMusicStop() {

            }

            @Override
            public void onGetCurrentPos(long curPos) {
                mMusicCurPlayPos = curPos;
                Log.d("1234: ", "onGetCurrentPos: " + curPos);
            }
        });

        // 聚焦
        mLivewindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float rectHalfWidth = mImageAutoFocusRect.getWidth() / 2;
                if (event.getX() - rectHalfWidth >= 0 && event.getX() + rectHalfWidth <= mLivewindow.getWidth()
                        && event.getY() - rectHalfWidth >= 0 && event.getY() + rectHalfWidth <= mLivewindow.getHeight()) {
                    mImageAutoFocusRect.setX(event.getX() - rectHalfWidth);
                    mImageAutoFocusRect.setY(event.getY() - rectHalfWidth);
                    RectF rectFrame = new RectF();
                    rectFrame.set(mImageAutoFocusRect.getX(), mImageAutoFocusRect.getY(),
                            mImageAutoFocusRect.getX() + mImageAutoFocusRect.getWidth(),
                            mImageAutoFocusRect.getY() + mImageAutoFocusRect.getHeight());
                    if (mSupportAutoFocus) {
                        //启动自动聚焦
                        mStreamingContext.startAutoFocus(rectFrame);
                    }
                    if (mSupportAutoExposure) {
                        //启动自动曝光补偿
                        mStreamingContext.setAutoExposureRect(rectFrame);
                    }

                    if (mSupportAutoFocus || mSupportAutoExposure) {
                        mImageAutoFocusRect.startAnimation(mFocusAnimation);
                    }
                }
                return false;
            }
        });
    }

    private void strengthSeekBarChange(int progress) {
        double strengthValue = progress / (float) 100;
        if (mCanUseARFaceType == HUMAN_AI_TYPE_MS && mInitArScene) {
            if (mARFaceU == null) {
                return;
            }
            boolean isEnabledBeauty = mARFaceU.getBooleanVal(arSceneBeauty);
            if (isEnabledBeauty) {
                mARFaceU.setFloatVal(BEAUTIFY_ARSCENE_STRENGTH, strengthValue);
            }
        } else {
            if (mBeautyVideoFx == null) {
                return;
            }
            mBeautyVideoFx.setFloatVal(BEAUTIFY_STRENGTH, strengthValue);
        }
        updateStrengthSeekBar(progress);
    }

    private void cheekThinningSeekBarChange(int progress) {
        double levelValue = getCheekThinVal(progress);
        boolean isEnabledBeautyShape = mARFaceU.getBooleanVal(arSceneBeautyShape);
        if (isEnabledBeautyShape) {
            mARFaceU.setFloatVal(BEAUTIFY_SHAPE_CHECK_THINNING, levelValue);
        }
        updateCheekThinningSeekBar(progress);
    }

    private void eyeEnlargingSeekBarChange(int progress) {
        double levelValue = getEyeEnlargeVal(progress);
        boolean isEnabledBeautyShape = mARFaceU.getBooleanVal(arSceneBeautyShape);
        if (isEnabledBeautyShape) {
            mARFaceU.setFloatVal(BEAUTIFY_SHAPE_EYE_ENLARGING, levelValue);
        }
        updateEyeEnlargingSeekBar(progress);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isCaptureRecording()) {
            stopRecording();
        }
        mStreamingContext.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasAllPermission()) {
            //checkPermissions();//缺少权限时,则请求权限
            AppManager.getInstance().finishActivity();//返回首页
        } else {
            if (mLocalAssetsLoadForOnce) {
                initAssetData();
                mLocalAssetsLoadForOnce = false;//加载之后标志位置为false.
            }
            initCapture();
            startCapturePreview(false);
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initCapture();
                    startCapturePreview(false);
                }
            },100);*/
        }
        setSelectMusicEnable(true);
        mFilterView.setMoreFilterClickable(true);
        mFaceUPropView.setMoreFaceUPropClickable(true);
        mPhotoAlbum.setClickable(true);
        mNextBtn.setClickable(true);
        mNextBtn.setEnabled(true);
        mNextBtn.setAlpha(1f);
        mRecordBtn.setEnabled(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mStreamingContext != null) {
            mStreamingContext.removeAllCaptureVideoFx();
            mStreamingContext.stop();
        }
        mMusicPlayer.stopPlay();
        mMusicPlayer.setPlayListener(null);
        mMusicPlayer.destroyPlayer();
        if (hasAllPermission())//有权限，则删除本地拍摄的视频文件
            deleteDouYinFile();

        setStreamingCallback(true);
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.close_btn: {
                AppManager.getInstance().finishActivity();
                break;
            }
            case R.id.selectMusic:
                setSelectMusicEnable(false);
                Bundle musicBundle = new Bundle();
                musicBundle.putInt(Constants.SELECT_MUSIC_FROM, Constants.SELECT_MUSIC_FROM_DOUYIN);
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(),
                        SelectMusicActivity.class,
                        musicBundle,
                        MUSIC_DATA_REQUES_CODE
                );
                break;
            case R.id.switch_camera_layout: {
                mCurrentDeviceIndex = mCurrentDeviceIndex == 0 ? 1 : 0;
                startCapturePreview(true);
                break;
            }
            case R.id.beauty_layout: {
                showCaptureDialogView(mBeautifyDialog, mBeautifyView);
                break;
            }
            case R.id.filter_layout: {
                showCaptureDialogView(mFilterDialog, mFilterView);
                break;
            }

            case R.id.countdown_layout: {
                showCaptureDialogView(mCountDownDialog, mCountDownView);
                initCountDownDuraAdjustView();
                break;
            }

            case R.id.faceU_layout: {
                if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                    if (mInitArScene) {
                        showCaptureDialogView(mFaceUPropDialog, mFaceUPropView);
                    } else {
                        // 人脸初始化失败
                        String[] versionName = getResources().getStringArray(R.array.sdk_expire_tips);
                        Util.showDialog(DouVideoCaptureActivity.this, versionName[0], versionName[1]);
                    }
                } else {
                    String[] versionName = getResources().getStringArray(R.array.sdk_version_tips);
                    Util.showDialog(DouVideoCaptureActivity.this, versionName[0], versionName[1]);
                }
                break;
            }
            case R.id.record_btn: {
                mCurCountDownCapDuration = 0;
                // 当前在录制状态，可停止视频录制
                if (isCaptureRecording()) {
                    stopRecording();
                } else {
                    startRecording();
                }
                break;
            }
            case R.id.super_fast_speed_btn:
            case R.id.fast_speed_btn:
            case R.id.standard_speed_btn:
            case R.id.slow_speed_btn:
            case R.id.super_slow_speed_btn: {
                selectSpeedItem(id);
                break;
            }
            case R.id.delete: {
                int size = mRecordClipsInfo.getClipList().size();
                if (size > 0) {
                    RecordClip clip = mRecordClipsInfo.removeLastClip();
                    mRecordProgress.updateRecordClipsInfo(mRecordClipsInfo);
                    mMusicPlayer.seekPosition(clip.getMusicStartPos());
                    misRecordFinished = false;
                }
                size = mRecordClipsInfo.getClipList().size();
                if (size <= 0) {
                    mDeleteBtn.setVisibility(View.INVISIBLE);
                    setSelectMusicEnable(true);
                    mMusicIcon.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.douvideo_music_selected));
                    mMusicName.setTextColor(ContextCompat.getColor(mContext, R.color.dy_text_after_music_seleeted));
                }
                mPhotoAlbum.setVisibility(size <= 0 ? View.VISIBLE : View.GONE);

                mCurVideoDuration = mRecordClipsInfo.getClipsDurationBySpeed();
                if (mCurVideoDuration < MAX_RECORD_DURATION)
                    setCountDownButtonEnable(true);
                Log.e(TAG, "onClick: " + mCurVideoDuration);
                if (mCurVideoDuration < MIN_RECORD_DURATION) {
                    mNextBtn.setVisibility(View.INVISIBLE);
                }

                mPhotoAlbum.setVisibility(mCurVideoDuration <= 0 ? View.VISIBLE : View.GONE);
                break;
            }

            case R.id.next: {
                jumpToEditActivity();
                break;
            }

            case R.id.photoAlbum: {
                mPhotoAlbum.setClickable(false);
                Bundle photoAlbumBundle = new Bundle();
                photoAlbumBundle.putInt(Constants.SELECT_MEDIA_FROM, Constants.SELECT_VIDEO_FROM_DOUYINCAPTURE);
                AppManager.getInstance().jumpActivity(this, SingleClickActivity.class, photoAlbumBundle);
                break;
            }
            default:
                break;
        }
    }

    private double getCheekThinVal(int progress) {
        return (100 - progress) / 100.0f;
    }

    private int getCheekThinProgressVal(double shapeVal) {
        return (int) (100 - 100 * shapeVal);
    }

    private double getEyeEnlargeVal(int progress) {
        return (progress - 100) / 100.0f;
    }

    private int getEyeEnlargeProgressVal(double shapeVal) {
        return (int) (100 * shapeVal + 100);
    }

    private void updateMusicPlayer(long seekPos) {
        if (mIsUsedMusic) {
            mMusicPlayer.setSpeed(1 / mCurSpeed);
            mMusicPlayer.resetExoPlayer();
            mMusicPlayer.seekPosition(seekPos);
        }
    }

    private void setSelectMusicEnable(boolean enable) {
        mSelectMusic.setEnabled(enable);
    }

    private void setStreamingCallback(boolean isDestroy) {
        mStreamingContext.setCaptureDeviceCallback(isDestroy ? null : this);
        mStreamingContext.setCaptureRecordingDurationCallback(isDestroy ? null : this);
        mStreamingContext.setCaptureRecordingStartedCallback(isDestroy ? null : this);
        mStreamingContext.setStreamingEngineCallback(isDestroy ? null : this);
    }

    private void initDouyinData() {
        mCanUseARFaceType = NvsStreamingContext.hasARModule();
        mHandler = new RecordHandler(this);
        mMusicPlayer = new MusicPlayer(getApplicationContext());
        mRecordClipsInfo = new RecordClipsInfo();
        // 聚焦动画
        mFocusAnimation = new AlphaAnimation(1.0f, 0.0f);
        mFocusAnimation.setDuration(1000);
        mFocusAnimation.setFillAfter(true);

        mStreamingContext.removeAllCaptureVideoFx();//移除所有采集特效
        //采集设备数量判定
        if (mStreamingContext.getCaptureDeviceCount() > 1) {
            mSwitchCameraBtn.setEnabled(true);
            mCurrentDeviceIndex = 1;
        } else {
            mSwitchCameraBtn.setEnabled(false);
            mCurrentDeviceIndex = 0;
        }
    }

    //初始化素材数据
    private void initAssetData() {
        searchAssetData();
        initFilterList();
        mFilterView.setFilterArrayList(mFilterDataArrayList);
        mFilterView.notifyDataSetChanged();
        initFacUPropDataList();
        mFaceUPropView.setPropDataArrayList(mPropDataArrayList);
        mFaceUPropView.notifyDataSetChanged();
    }

    private void searchAssetData() {
        mAssetManager = NvAssetManager.sharedInstance();
        String bundlePath = "filter";
        mAssetManager.searchReservedAssets(NvAsset.ASSET_FILTER, bundlePath);
        mAssetManager.searchLocalAssets(NvAsset.ASSET_FILTER);
        bundlePath = "arface";
        mAssetManager.searchReservedAssets(NvAsset.ASSET_ARSCENE_FACE, bundlePath);
        mAssetManager.searchLocalAssets(NvAsset.ASSET_ARSCENE_FACE);
    }

    private void initCountDownDuraAdjustView() {
        if (mCurVideoDuration >= MAX_RECORD_DURATION)
            mCurVideoDuration = MAX_RECORD_DURATION;
        mCountDownCaptureView.setMaxCaptureDuration(MAX_RECORD_DURATION);
        mCountDownCaptureView.setCurCaptureDuaration(mCurVideoDuration);
        mCountDownCaptureView.resetHandleViewPos();
        long newMaxCaptureDuration = MAX_RECORD_DURATION - mCurVideoDuration;
        if (newMaxCaptureDuration <= 0)
            newMaxCaptureDuration = 0;
        mCountDownCaptureView.setNewCaptureDuration(newMaxCaptureDuration);
    }

    //滤镜数据初始化
    private void initFilterList() {
        mFilterDataArrayList.clear();
        mFilterDataArrayList = AssetFxUtil.getFilterData(this,
                getLocalData(NvAsset.ASSET_FILTER),
                null,
                false,
                false);
    }

    private void initFacUPropDataList() {
        mPropDataArrayList.clear();
        //先初始化道具数据数据
        ArrayList<NvAsset> faceULocalDataList = getLocalData(NvAsset.ASSET_ARSCENE_FACE);
        mPropDataArrayList = AssetFxUtil.getFaceUDataList(faceULocalDataList, null);
    }

    //美化对话框初始化
    private void initBeautifyDialog() {
        mBeautifyDialog = new AlertDialog.Builder(this).create();
        mBeautifyDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                closeCaptureDialogView(mBeautifyDialog);
            }
        });
    }

    private void initFilterDialog() {
        mFilterDialog = new AlertDialog.Builder(this).create();
        mFilterDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                closeCaptureDialogView(mFilterDialog);
            }
        });

        mFilterView = new FilterView(this);
        //先设置滤镜数据
        mFilterView.setFilterArrayList(mFilterDataArrayList);
        mFilterView.initFilterRecyclerView(this);
        mFilterView.setIntensityLayoutVisible(View.INVISIBLE);
        mFilterView.setFilterListener(new FilterView.OnFilterListener() {
            @Override
            public void onItmeClick(View v, int position) {
                int count = mFilterDataArrayList.size();
                if (position < 0 || position >= count)
                    return;
                if (mFilterSelPos == position)
                    return;
                mFilterSelPos = position;
                removeAllFilterFx();
                mFilterView.setIntensitySeekBarMaxValue(100);
                mFilterView.setIntensitySeekBarProgress(100);
                if (position == 0) {
                    mVideoClipFxInfo.setFxMode(VideoClipFxInfo.FXMODE_BUILTIN);
                    mVideoClipFxInfo.setFxId(null);
                    return;
                }

                FilterItem filterItem = mFilterDataArrayList.get(position);
                int filterMode = filterItem.getFilterMode();
                if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                    String filterName = filterItem.getFilterName();
                    if (!TextUtils.isEmpty(filterName)) {//内建滤镜
                        mCurCaptureVideoFx = mStreamingContext.appendBuiltinCaptureVideoFx(filterName);
                    }
                    mVideoClipFxInfo.setFxMode(VideoClipFxInfo.FXMODE_BUILTIN);
                    mVideoClipFxInfo.setFxId(filterName);
                } else {
                    String filterPackageId = filterItem.getPackageId();
                    if (!TextUtils.isEmpty(filterPackageId)) {
                        mCurCaptureVideoFx = mStreamingContext.appendPackagedCaptureVideoFx(filterPackageId);
                    }
                    mVideoClipFxInfo.setFxMode(VideoClipFxInfo.FXMODE_PACKAGE);
                    mVideoClipFxInfo.setFxId(filterPackageId);
                }

                mCurCaptureVideoFx.setFilterIntensity(1.0f);
            }

            @Override
            public void onMoreFilter() {
                TimelineData.instance().setMakeRatio(NvAsset.AspectRatio_NoFitRatio);//拍摄进入下载，不作比例适配
                Bundle bundle = new Bundle();
                bundle.putInt("titleResId", R.string.moreFilter);
                bundle.putInt("assetType", NvAsset.ASSET_FILTER);
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), AssetDownloadActivity.class, bundle, REQUEST_FILTER_LIST_CODE);
                mFilterView.setMoreFilterClickable(false);
            }

            @Override
            public void onIntensity(int value) {
                if (mCurCaptureVideoFx != null) {
                    float intensity = value / (float) 100;
                    mCurCaptureVideoFx.setFilterIntensity(intensity);
                }
            }
        });
    }

    //美化对话框初始化
    private void initCountDownDialog() {
        mCountDownDialog = new AlertDialog.Builder(this).create();
        mCountDownDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                closeCaptureDialogView(mCountDownDialog);
            }
        });
    }

    //初始化美化参数
    private void initBeautifyParam() {
        if (mCanUseARFaceType == HUMAN_AI_TYPE_MS && mInitArScene) {//人脸特效可用
            mARFaceU = mStreamingContext.appendBuiltinCaptureVideoFx(arSceneFxName);
            if (mARFaceU != null) {
                mARFaceU.setBooleanVal(arSceneBeauty, true);//打开人脸里面的美颜
                mARFaceU.setBooleanVal(arSceneBeautyShape, true);////打开人脸里面的美型
                mCheekThinningSeekBar.setMax(SEEKBAR_BEAUTYSHAPE_MAXVALUE);
                if (mIntentCheekThinning == -1) {
                    int cheekThinning = getCheekThinProgressVal(mARFaceU.getFloatVal(BEAUTIFY_SHAPE_EYE_ENLARGING));
                    updateCheekThinningSeekBar(cheekThinning);
                } else {
                    cheekThinningSeekBarChange(mIntentCheekThinning);
                }
                mEyeEnlargingSeekBar.setMax(SEEKBAR_BEAUTYSHAPE_MAXVALUE);
                if (mIntentEyeEnlarging == -1) {
                    int eyeEnlarge = getEyeEnlargeProgressVal(mARFaceU.getFloatVal(BEAUTIFY_SHAPE_EYE_ENLARGING));
                    updateEyeEnlargingSeekBar(eyeEnlarge);
                } else {
                    eyeEnlargingSeekBarChange(mIntentEyeEnlarging);
                }
                if (mIntentStrength == -1) {
                    int stength = (int) (mARFaceU.getFloatVal(BEAUTIFY_ARSCENE_STRENGTH) * 100);
                    updateStrengthSeekBar(stength);
                } else {
                    updateStrengthSeekBar(mIntentStrength);
                }
            }
        } else {
            //添加美颜
            mBeautyVideoFx = mStreamingContext.appendBeautyCaptureVideoFx();
            NvsFxDescription fxDescription = mStreamingContext.getVideoFxDescription(beautyFxName);
            List<NvsFxDescription.ParamInfoObject> paramInfo = fxDescription.getAllParamsInfo();
            for (NvsFxDescription.ParamInfoObject param : paramInfo) {
                String paramName = param.getString("paramName");
                if (paramName.equals(BEAUTIFY_STRENGTH)) {//获取磨皮默认参数
                    double maxValue = param.getFloat("floatMaxVal");
                    double defVal = param.getFloat("floatDefVal");
                    mStrengthSeekBar.setMax((int) (maxValue * 100));
                    if (mIntentStrength == -1) {
                        updateStrengthSeekBar((int) (defVal * 100));
                    } else {
                        updateStrengthSeekBar(mIntentStrength);
                    }
                    break;
                }
            }
            mCheekThinningSeekBar.setEnabled(false);
            mCheekThinningSeekBar.setClickable(false);
            mEyeEnlargingSeekBar.setEnabled(false);
            mEyeEnlargingSeekBar.setClickable(false);
            updateCheekThinningSeekBar(0);
            updateEyeEnlargingSeekBar(0);
        }
    }

    //道具
    private void initFacUPropDialog() {
        mFaceUPropDialog = new AlertDialog.Builder(this).create();
        mFaceUPropDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                closeCaptureDialogView(mFaceUPropDialog);
            }
        });
        mFaceUPropView = new FaceUPropView(this);

        //先设置道具数据
        mFaceUPropView.setPropDataArrayList(mPropDataArrayList);
        mFaceUPropView.initPropRecyclerView(this);
        mFaceUPropView.setFaceUPropListener(new FaceUPropView.OnFaceUPropListener() {
            @Override
            public void onItmeClick(View v, int position) {
                int count = mPropDataArrayList.size();
                if (position < 0 || position >= count)
                    return;
                if (mARFaceU == null)
                    return;
                if (mFaceUPropSelPos == position)
                    return;

                mFaceUPropSelPos = position;
                mArSceneId = mPropDataArrayList.get(position).getPackageId();
                Log.e("===>", "mArSceneId: " + mArSceneId);
                mARFaceU.setStringVal("Scene Id", mArSceneId);
            }

            @Override
            public void onMoreFaceUProp() {
                TimelineData.instance().setMakeRatio(NvAsset.AspectRatio_NoFitRatio);//拍摄进入下载，不作比例适配
                mFaceUPropView.setMoreFaceUPropClickable(false);
                Bundle bundle = new Bundle();
                bundle.putInt("titleResId", R.string.moreFaceU);
                bundle.putInt("assetType", NvAsset.ASSET_ARSCENE_FACE);
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), AssetDownloadActivity.class, bundle, ARFACE_LIST_REQUES_CODE);
            }
        });
    }

    //磨皮
    private void updateStrengthSeekBar(int value) {
        updateBeautyValue(mStrengthSeekBar, mStrengthValue, value);
    }

    //瘦脸
    private void updateCheekThinningSeekBar(int value) {
        mCheekThinningSeekBar.setProgress(value);
        mCheekThinningValue.setText(String.valueOf(value - 100));
    }

    //大眼
    private void updateEyeEnlargingSeekBar(int value) {
        mEyeEnlargingSeekBar.setProgress(value);
        mEyeEnlargingValue.setText(String.valueOf(value - 100));
    }

    private void updateBeautyValue(SeekBar seekBar, TextView textView, int value) {
        seekBar.setProgress(value);
        textView.setText(String.valueOf(value));
    }

    private void jumpToEditActivity() {
        mNextBtn.setEnabled(false);
        mNextBtn.setAlpha(0.5f);
        mRecordClipsInfo.setMusicInfo(mCurMusicInfo);

        Bundle bundle = new Bundle();
        bundle.putSerializable("recordInfo", mRecordClipsInfo);
        AppManager.getInstance().jumpActivityForResult(this, DouVideoEditActivity.class, bundle, REQUEST_CODE_DOUYIN_EDIT);
    }

    private boolean isCaptureRecording() {
        return getCurrentEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING;
    }

    private void startRecording() {
        if (mCurVideoDuration >= MAX_RECORD_DURATION) {
            if(Util.isFastClick()){
                return;
            }
            String tipsName = getResources().getString(R.string.douyin_recorder_maxduration_tips);
            ToastUtil.showToastCenter(DouVideoCaptureActivity.this, tipsName);
            return;
        }
        long minCountDownDuration = (long) Math.floor(CLIP_MIN_RECORD_DURATION * 0.45 + 0.5D);
        if (mCurCountDownCapDuration > 0 && mCurCountDownCapDuration <= minCountDownDuration) {//倒计时录制时长不能低于0.5秒
            String[] tipsName = getResources().getStringArray(R.array.douyin_recorder_minduration_tips);
            Util.showDialog(DouVideoCaptureActivity.this, tipsName[0], tipsName[1]);
            Logger.e(TAG, "startRecording = " + mCurCountDownCapDuration);
            return;
        }
        // 剩余时长不足一个0.5秒，不进行录制
        /*if ((MAX_RECORD_DURATION - mRecordClipsInfo.getClipsDurationBySpeed() <= CLIP_MIN_RECORD_DURATION / 2)
                || misRecordFinished) {
            return;
        }*/

        mCurRecordVideoPath = PathUtils.getDouYinRecordVideoPath();
        if (mCurRecordVideoPath == null)
            return;
        mRecordBtn.setEnabled(false);

        //当前未在视频录制状态，则启动视频录制。此处使用带特效的录制方式
        if (!mStreamingContext.startRecording(mCurRecordVideoPath)) {
            return;
        }
        mMusicIcon.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.douvideo_music_use));
        mMusicName.setTextColor(ContextCompat.getColor(mContext, R.color.dy_yellow));
        isInRecording(true);
    }

    private ArrayList<NvAsset> getLocalData(int assetType) {
        return mAssetManager.getUsableAssets(assetType, NvAsset.AspectRatio_All, 0);
    }

    private void deleteDouYinFile() {
        String douYinDir = PathUtils.getDouYinRecordDir();
        String douYinConvertDir = PathUtils.getDouYinConvertDir();
        PathUtils.deleteFile(douYinDir);
        PathUtils.deleteFile(douYinConvertDir);
    }

    private void removeAllFilterFx() {
        List<Integer> remove_list = new ArrayList<>();
        for (int i = 0; i < mStreamingContext.getCaptureVideoFxCount(); i++) {
            NvsCaptureVideoFx fx = mStreamingContext.getCaptureVideoFxByIndex(i);
            if (fx == null)
                continue;

            String name = fx.getBuiltinCaptureVideoFxName();
            if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                if (name != null && !name.equals(arSceneFxName)) {
                    remove_list.add(i);
                }
            } else {
                if (name != null && !name.equals(beautyFxName)) {
                    remove_list.add(i);
                }
            }
        }
        if (!remove_list.isEmpty()) {
            for (int i = 0; i < remove_list.size(); i++) {
                mStreamingContext.removeCaptureVideoFx(remove_list.get(i));
            }
        }
    }

    private void selectSpeedItem(int id) {
        mSuperSlowSpeedBtn.setBackgroundResource(R.drawable.douvideo_speed_left_shape);
        mSlowSpeedBtn.setBackgroundResource(R.color.douyin_speed_color);
        mStandardSpeedBtn.setBackgroundResource(R.color.douyin_speed_color);
        mFastSpeedBtn.setBackgroundResource(R.color.douyin_speed_color);
        mSuperFastSpeedBtn.setBackgroundResource(R.drawable.douvideo_speed_right_shape);
        mSuperSlowSpeedBtn.setTextColor(SPEED_TXT_NORMAL_COLOR);
        mSlowSpeedBtn.setTextColor(SPEED_TXT_NORMAL_COLOR);
        mStandardSpeedBtn.setTextColor(SPEED_TXT_NORMAL_COLOR);
        mFastSpeedBtn.setTextColor(SPEED_TXT_NORMAL_COLOR);
        mSuperFastSpeedBtn.setTextColor(SPEED_TXT_NORMAL_COLOR);

        switch (id) {
            case R.id.super_fast_speed_btn: {
                mSuperFastSpeedBtn.setBackgroundResource(R.drawable.douvideo_speed_select_shape);
                mSuperFastSpeedBtn.setTextColor(SPEED_TXT_SELECT_COLOR);
                mCurSpeed = 2.0f;
                break;
            }

            case R.id.fast_speed_btn: {
                mFastSpeedBtn.setBackgroundResource(R.drawable.douvideo_speed_select_shape);
                mFastSpeedBtn.setTextColor(SPEED_TXT_SELECT_COLOR);
                mCurSpeed = 1.5f;
                break;
            }

            case R.id.standard_speed_btn: {
                mStandardSpeedBtn.setBackgroundResource(R.drawable.douvideo_speed_select_shape);
                mStandardSpeedBtn.setTextColor(SPEED_TXT_SELECT_COLOR);
                mCurSpeed = 1f;
                break;
            }

            case R.id.slow_speed_btn: {
                mSlowSpeedBtn.setBackgroundResource(R.drawable.douvideo_speed_select_shape);
                mSlowSpeedBtn.setTextColor(SPEED_TXT_SELECT_COLOR);
                mCurSpeed = 0.75f;
                break;
            }

            case R.id.super_slow_speed_btn: {
                mSuperSlowSpeedBtn.setBackgroundResource(R.drawable.douvideo_speed_select_shape);
                mSuperSlowSpeedBtn.setTextColor(SPEED_TXT_SELECT_COLOR);
                mCurSpeed = 0.5f;
                break;
            }
            default:
                break;
        }
        updateMusicPlayer(mMusicCurPlayPos);
    }

    private boolean startCapturePreview(boolean deviceChanged) {
        int captureResolutionGrade = ParameterSettingValues.instance().getCaptureResolutionGrade();
        if (deviceChanged || getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTUREPREVIEW) {
            if (!mStreamingContext.startCapturePreview(mCurrentDeviceIndex,
                    captureResolutionGrade, NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_DONT_USE_SYSTEM_RECORDER |
                            NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_CAPTURE_BUDDY_HOST_VIDEO_FRAME
                            | NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_STRICT_PREVIEW_VIDEO_SIZE, null)) {
                Log.e(TAG, "Failed to start capture preview!");
                return false;
            }
        }

        return true;
    }

    private int getCurrentEngineState() {
        return mStreamingContext.getStreamingEngineState();
    }

    private void initCapture() {
        if (null == mStreamingContext) {
            return;
        }
        //给Streaming Context设置采集设备回调接口
        setStreamingCallback(false);
        if (mStreamingContext.getCaptureDeviceCount() == 0) {
            Log.e(TAG, "没有采集设备！");
            return;
        }
        // 将采集预览输出连接到LiveWindow控件
        if (!mStreamingContext.connectCapturePreviewWithLiveWindow(mLivewindow)) {
            Log.e(TAG, "Failed to connect capture preview with livewindow!");
        }
    }

    private void stopRecording() {
        if (mStreamingContext.getStreamingEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_STOPPED) {
            mStreamingContext.stopRecording();
        }
    }

    private void isInRecording(boolean isInRecording) {
        int show;
        if (isInRecording) {
            show = View.INVISIBLE;
            mNextBtn.setVisibility(show);
            mPhotoAlbum.setVisibility(show);
            setSelectMusicEnable(false);
        } else {
            show = View.VISIBLE;
            if (mRecordClipsInfo.getClipsDurationBySpeed() > MIN_RECORD_DURATION) {
                mNextBtn.setVisibility(show);
            }
        }
        mTopLayout.setVisibility(show);
        mOperationLayout.setVisibility(show);
        mDeleteBtn.setVisibility(show);
        mSpeedItemLayout.setVisibility(show);
    }

    // 显示窗口
    private void showCaptureDialogView(AlertDialog dialog, View view) {
        TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

        translate.setDuration(200);//动画时间500毫秒
        translate.setFillAfter(false);//动画出来控件可以点击
        mBottomOperationLayout.startAnimation(translate);
        dialog.show();
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        params.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorTranslucent));
        dialog.getWindow().setWindowAnimations(R.style.fx_dlg_style);
        isShowCaptureButton(View.INVISIBLE);

    }

    // 关闭窗口
    private void closeCaptureDialogView(AlertDialog dialog) {
        dialog.dismiss();
        TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        translate.setDuration(300);//动画时间300毫秒
        translate.setFillAfter(false);//动画出来控件可以点击
        mBottomOperationLayout.setAnimation(translate);
        isShowCaptureButton(View.VISIBLE);
    }

    private void isShowCaptureButton(int show) {
        mTopLayout.requestLayout();
        mOperationLayout.requestLayout();
        mBottomOperationLayout.requestLayout();
        mTopLayout.setVisibility(show);
        mOperationLayout.setVisibility(show);
        mBottomOperationLayout.setVisibility(show);
    }

    private void updateSettingsWithCapability(int deviceIndex) {
        //获取采集设备能力描述对象，设置自动聚焦，曝光补偿，缩放
        NvsStreamingContext.CaptureDeviceCapability capability = mStreamingContext.getCaptureDeviceCapability(deviceIndex);
        if (null == capability) {
            return;
        }

        mSupportAutoFocus = capability.supportAutoFocus;
        mSupportAutoExposure = capability.supportAutoExposure;
    }

    private void setCountDownButtonEnable(boolean enable) {
        mCountdownBtn.setEnabled(enable);
        mCountdownBtn.setAlpha(enable ? 1.0f : 0.3f);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_FILTER_LIST_CODE) {
                    initFilterList();
                    mFilterView.setFilterArrayList(mFilterDataArrayList);
                    mFilterSelPos = AssetFxUtil.getSelectedFilterPos(mFilterDataArrayList, mVideoClipFxInfo);
                    mFilterView.setSelectedPos(mFilterSelPos);
                    mFilterView.notifyDataSetChanged();
                } else if (requestCode == ARFACE_LIST_REQUES_CODE) {
                    initFacUPropDataList();
                    mFaceUPropView.setPropDataArrayList(mPropDataArrayList);
                    mFaceUPropSelPos = AssetFxUtil.getSelectedFaceUPropPos(mPropDataArrayList, mArSceneId);
                    mFaceUPropView.setSelectedPos(mFaceUPropSelPos);
                    mFaceUPropView.notifyDataSetChanged();
                } else if (requestCode == MUSIC_DATA_REQUES_CODE) {
                    // 处理选择的音乐
                    mCurMusicInfo = (MusicInfo) data.getSerializableExtra("select_music");
                    if (mCurMusicInfo != null) {
                        mIsUsedMusic = true;//使用音乐
                        mMusicName.setText(mCurMusicInfo.getTitle());
                        Log.e(TAG, "onActivityResult: music trimin: " + mCurMusicInfo.getTrimIn() +
                                " music trimout: " + mCurMusicInfo.getTrimOut() +
                                " music duration: " + mCurMusicInfo.getDuration());
                        mMusicPlayer.setCurrentMusic(mCurMusicInfo);
                        updateMusicPlayer(mCurMusicInfo.getTrimIn());

                        mMusicIcon.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.douvideo_music_selected));
                        mMusicName.setTextColor(ContextCompat.getColor(mContext, R.color.dy_text_after_music_seleeted));
                    } else {
                        mIsUsedMusic = false;
                        mMusicName.setText(R.string.select_music);

                        mMusicIcon.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.douvideo_music));
                        mMusicName.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    }
                } else if (requestCode == REQUEST_CODE_DOUYIN_EDIT) {
                    //TODO
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected List<String> initPermissions() {
        return Util.getAllPermissionsList();
    }

    //有权限
    @Override
    protected void hasPermission() {
        Logger.e(TAG, "hasPermission: 所有权限都有了");
        mLocalAssetsLoadForOnce = true;
    }

    //没有权限
    @Override
    protected void nonePermission() {
        Logger.e(TAG, "nonePermission: 没有允许权限");
        String[] permissionsTips = getResources().getStringArray(R.array.permissions_tips);
        Util.showDialog(DouVideoCaptureActivity.this, permissionsTips[0], permissionsTips[1], new TipsButtonClickListener() {
            @Override
            public void onTipsButtoClick(View view) {
                AppManager.getInstance().finishActivity();
            }
        });
    }

    //有权限，用户选择了不再提示
    @Override
    protected void noPromptPermission() {
        Logger.e(TAG, "noPromptPermission: 用户选择了不再提示");
    }

    @Override
    public void onCaptureDeviceCapsReady(int captureDeviceIndex) {
        if (captureDeviceIndex != mCurrentDeviceIndex) {
            return;
        }
        updateSettingsWithCapability(captureDeviceIndex);
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
        Log.d(TAG, "onCaptureDeviceStopped: ");
    }

    @Override
    public void onCaptureDeviceAutoFocusComplete(int i, boolean b) {

    }

    @Override
    public void onCaptureRecordingFinished(int i) {
        mRecordBtn.setEnabled(true);
        if (mIsUsedMusic) {
            mMusicPlayer.stopPlay();
            mCurMusicInfo.setPlay(false);
        }
        long clipDur = 0;
        NvsAVFileInfo info = mStreamingContext.getAVFileInfo(mCurRecordVideoPath);
        if (info != null) {
            clipDur = info.getDuration();
        }
        Log.d(TAG, "onCaptureRecordingFinished: clip duration:" + clipDur);
        RecordClip clip = new RecordClip(mCurRecordVideoPath, 0, clipDur, mCurSpeed, mMusicStartPos);
        clip.setCaptureVideo(true);
        mRecordClipsInfo.addClip(clip);
        mRecordProgress.updateRecordClipsInfo(mRecordClipsInfo);
        mRecordProgress.setCurVideoDuration(mRecordClipsInfo.getClipsDurationBySpeed());
       /* if (clipDur > CLIP_MIN_RECORD_DURATION / 2) {
            mRecordClipsInfo.addClip(clip);
            mRecordProgress.updateRecordClipsInfo(mRecordClipsInfo);
            mRecordProgress.setCurVideoDuration(mRecordClipsInfo.getClipsDurationBySpeed());
        } else {
            if (MAX_RECORD_DURATION - mRecordClipsInfo.getClipsDurationBySpeed() <= CLIP_MIN_RECORD_DURATION / 2)
                misRecordFinished = true;//剩余时长小于
            mRecordProgress.setCurVideoDuration(mRecordClipsInfo.getClipsDurationBySpeed());
        }*/

        isInRecording(false);
        Log.d(TAG, "onCaptureRecordingFinished: all duration:" + mRecordClipsInfo.getClipsDurationBySpeed());

        if (mRecordClipsInfo.getClipsDurationBySpeed() >= MAX_RECORD_DURATION) {
            setCountDownButtonEnable(false);
            mHandler.sendEmptyMessage(MESSAGE_RECORD_FINISH);
        }

    }

    @Override
    public void onCaptureRecordingError(int i) {

    }

    @Override
    public void onCaptureRecordingDuration(int i, long duration) {
        if (duration >= CLIP_MIN_RECORD_DURATION) {
            mRecordBtn.setEnabled(true);
        }
        long hasRecordedDuration = (long) (duration * 1f / mCurSpeed);//已拍摄视频的时长
        mCurVideoDuration = mRecordClipsInfo.getClipsDurationBySpeed() + hasRecordedDuration;
        mRecordProgress.setCurVideoDuration(mCurVideoDuration);
        if (mCurVideoDuration >= MAX_RECORD_DURATION) {
            mHandler.sendEmptyMessage(MESSAGE_RECORD_STOPRECORDING);
        } else {
            if (mCurCountDownCapDuration > 0
                    && hasRecordedDuration >= mCurCountDownCapDuration) {
                //倒计时分段拍摄控制
                mHandler.sendEmptyMessage(MESSAGE_RECORD_STOPRECORDING);
                mCurCountDownCapDuration = 0;
            }
        }
    }

    @Override
    public void onCaptureRecordingStarted(int i) {
        if (mIsUsedMusic) {
            mMusicStartPos = mMusicPlayer.getCurMusicPos() * 1000;
            mMusicPlayer.startPlay();
            mCurMusicInfo.setPlay(true);
        }
    }

    @Override
    public void onStreamingEngineStateChanged(int i) {
        mRecordBtn.setBackgroundResource(i == NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING ?
                R.drawable.capturescene_stoprecord :
                R.mipmap.douvideo_record);
    }

    @Override
    public void onFirstVideoFramePresented(NvsTimeline nvsTimeline) {

    }

    private static class RecordHandler extends Handler {
        private WeakReference<DouVideoCaptureActivity> mWeekReference;

        public RecordHandler(DouVideoCaptureActivity activity) {
            mWeekReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DouVideoCaptureActivity activity = mWeekReference.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {
                case MESSAGE_RECORD_FINISH: {
                    activity.jumpToEditActivity();
                    break;
                }
                case MESSAGE_RECORD_STOPRECORDING: {
                    activity.stopRecording();
                    break;
                }

                default: {
                    Log.e(TAG, "handleMessage: 没有处理的消息 id: " + msg.what);
                    break;
                }
            }
        }
    }
}
