package com.meishe.sdkdemo.capture;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsCaptureVideoFx;
import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsVideoFrameRetriever;
import com.meicam.sdk.NvsVideoStreamInfo;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BasePermissionActivity;
import com.meishe.sdkdemo.download.AssetDownloadActivity;
import com.meishe.sdkdemo.edit.VideoEditActivity;
import com.meishe.sdkdemo.edit.adapter.SpaceItemDecoration;
import com.meishe.sdkdemo.edit.data.FilterItem;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.AssetFxUtil;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.MediaScannerUtil;
import com.meishe.sdkdemo.utils.OnSeekBarChangeListenerAbs;
import com.meishe.sdkdemo.utils.ParameterSettingValues;
import com.meishe.sdkdemo.utils.PathUtils;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.TimeFormatUtil;
import com.meishe.sdkdemo.utils.ToastUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;
import com.meishe.sdkdemo.utils.dataInfo.ClipInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;
import com.meishe.sdkdemo.utils.dataInfo.VideoClipFxInfo;
import com.meishe.sdkdemo.utils.permission.PermissionDialog;
import com.meishe.sdkdemo.view.FaceUPropView;
import com.meishe.sdkdemo.view.FilterView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.meishe.sdkdemo.utils.Constants.HUMAN_AI_TYPE_MS;
import static com.meishe.sdkdemo.utils.Constants.HUMAN_AI_TYPE_NONE;

/**
 * CaptureActivity class
 *
 * @author zd
 * @date 2018-06-05
 */
public class CaptureActivity extends BasePermissionActivity implements NvsStreamingContext.CaptureDeviceCallback, NvsStreamingContext.CaptureRecordingDurationCallback, NvsStreamingContext.CaptureRecordingStartedCallback {

    private static final String TAG = CaptureActivity.class.getSimpleName();

    private static final int REQUEST_FILTER_LIST_CODE = 110;
    private static final int ARFACE_LIST_REQUES_CODE = 111;

    private NvsLiveWindow mLiveWindow;
    private NvsStreamingContext mStreamingContext;
    private Button mCloseButton;
    private LinearLayout mFunctionButtonLayout;
    private LinearLayout mSwitchFacingLayout;
    private LinearLayout mFlashLayout;
    private ImageView mFlashButton;
    private LinearLayout mZoomLayout;
    private LinearLayout mExposureLayout;
    private LinearLayout mBeautyLayout;
    private LinearLayout mFilterLayout;
    private LinearLayout mFuLayout;
    private RelativeLayout mStartLayout;
    private ImageView mStartRecordingImage;
    private TextView mStartText;
    private ImageView mDelete;
    private ImageView mNext;
    private TextView mRecordTime;
    private TextView mSeekTitle;
    private TextView mSeekProgress;
    private ImageView mImageAutoFocusRect;
    private RelativeLayout mBottomLayout;
    private LinearLayout mAdjustColorLayout, mSharpenLayout;
    private Switch mAdjustColorSwitch, mSharpenSwitch;

    /**
     * 拍照or视频
     */
    private RelativeLayout mSelectLayout, mPictureLayout;
    private LinearLayout mRecordTypeLayout;
    private View mTypeRightView;
    private Button mTypePictureBtn, mTypeVideoBtn, mPictureCancel, mPictureOk;
    private int mRecordType = Constants.RECORD_TYPE_VIDEO;
    private ImageView mPictureImage;
    private Bitmap mPictureBitmap;

    /**
     * 录制
     */
    private ArrayList<Long> mRecordTimeList = new ArrayList<>();
    private ArrayList<String> mRecordFileList = new ArrayList<>();
    private long mEachRecodingVideoTime = 0, mEachRecodingImageTime = 4000000;
    private long mAllRecordingTime = 0;
    private String mCurRecordVideoPath;
    private NvAssetManager mAssetManager;
    private int mCurrentDeviceIndex;
    private boolean mIsSwitchingCamera;
    NvsStreamingContext.CaptureDeviceCapability mCapability = null;
    private AlphaAnimation mFocusAnimation;

    /**
     * 变焦以及曝光dialog
     */
    private AlertDialog mCaptureZoomAndExposeDialog;
    private View mZoomView;
    private SeekBar mZoomSeekbar;
    private SeekBar mExposeSeekbar;
    private int mZoomValue;
    private int mMinExpose;
    private int mCaptureType;
    private boolean m_supportAutoFocus;

    /**
     * 美颜Dialog
     */
    private AlertDialog mCaptureBeautyDialog;
    private View mBeautyView;
    private Button mBeautyTabButton;
    private Button mShapeTabButton;
    private RelativeLayout mBeautySelectRelativeLayout;
    private RelativeLayout mShapeSelectRelativeLayout;
    private SeekBar mBeautySeekBar;
    private SeekBar mShapeSeekBar;

    /**
     * 美颜
     */
    private Switch mBeauty_switch;
    private TextView mBeauty_switch_text;
    private Boolean mIsBeautyType = true;
    private RecyclerView mBeautyRecyclerView;
    private BeautyShapeAdapter mBeautyAdapter;
    private String mCurBeautyId;
    private NvsCaptureVideoFx mBeautyFx;
    private boolean mBeautySwitchIsOpend;
    private double mDefaultBeautyIntensity = 1.0;
    private NvsCaptureVideoFx mArSceneFaceEffect;

    /**
     * 美型id 检索数组
     */
    private String[] mShapeIdArray = {
            "Face Size Warp Degree",
            "Face Length Warp Degree",
            "Face Width Warp Degree",
            "Nose Width Warp Degree"
    };
    List<String> mShapeIdList = new ArrayList<>(Arrays.asList(mShapeIdArray));

    /**
     * ArScene 是否初始化完成
     */
    private boolean initArScene;

    /**
     * 美型
     */
    private Switch mBeauty_shape_switch;
    private TextView mBeauty_shape_switch_text;
    private LinearLayout mBeautyShapeResetLayout;
    private ImageView mBeautyShapeResetIcon;
    private TextView mBeautyShapeResetTxt;
    private RecyclerView mShapeRecyclerView;
    private BeautyShapeAdapter mShapeAdapter;
    private boolean mShapeSwitchIsOpen;

    /**
     * 滤镜
     */
    private AlertDialog mFilterDialog;
    private FilterView mFilterView;
    private NvsCaptureVideoFx mCurCaptureVideoFx;
    private ArrayList<FilterItem> mFilterDataArrayList = new ArrayList<>();
    private int mFilterSelPos;
    private VideoClipFxInfo mVideoClipFxInfo = new VideoClipFxInfo();

    /**
     * 道具-默认普通版，不带人脸功能
     */
    private int mCanUseARFaceType = HUMAN_AI_TYPE_NONE;
    private AlertDialog mFaceUPropDialog;
    private FaceUPropView mFaceUPropView;
    private ArrayList<FilterItem> mPropDataArrayList = new ArrayList<>();
    private int mFaceUPropSelPos;
    private String mArSceneId = "";
    private TextView mbeautywhiteningtext;
    private ImageView mbeautywhiteningA;
    private ImageView mbeautywhiteningB;
    private LinearLayout mBeautyWhiteningAll;

    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_capture;
    }

    @Override
    protected void initViews() {
        // 页面主要布局
        mBottomLayout = (RelativeLayout) findViewById(R.id.capture_bottom_rl);
        mFunctionButtonLayout = (LinearLayout) findViewById(R.id.functionButtonLayout);
        mRecordTypeLayout = (LinearLayout) findViewById(R.id.record_type_layout);

        /*变焦以及曝光dialog*/
        mZoomView = LayoutInflater.from(this).inflate(R.layout.zoom_view, null);
        mZoomSeekbar = (SeekBar) mZoomView.findViewById(R.id.zoom_seekbar);
        mExposeSeekbar = (SeekBar) mZoomView.findViewById(R.id.expose_seekbar);
        mSeekTitle = (TextView) mZoomView.findViewById(R.id.seekTitle);
        mSeekProgress = (TextView) mZoomView.findViewById(R.id.seekProgress);

        /* A 美颜、美型*/
        mBeautyView = LayoutInflater.from(this).inflate(R.layout.beauty_view, null);
        mBeautyTabButton = (Button) mBeautyView.findViewById(R.id.beauty_tab_btn);
        mShapeTabButton = (Button) mBeautyView.findViewById(R.id.shape_tab_btn);
        mBeautySelectRelativeLayout = (RelativeLayout) mBeautyView.findViewById(R.id.beauty_select_rl);
        mShapeSelectRelativeLayout = (RelativeLayout) mBeautyView.findViewById(R.id.shape_select_rl);
        mBeautySeekBar = (SeekBar) mBeautyView.findViewById(R.id.beauty_sb);
        mShapeSeekBar = (SeekBar) mBeautyView.findViewById(R.id.shape_sb);
        


        /* A1 美颜*/
        mBeauty_switch = (Switch) mBeautyView.findViewById(R.id.beauty_switch);
        mBeauty_switch_text = (TextView) mBeautyView.findViewById(R.id.beauty_switch_text);
        mBeautyRecyclerView = (RecyclerView) mBeautyView.findViewById(R.id.beauty_list_rv);
        //美白模式
        mbeautywhiteningtext = (TextView)mBeautyView.findViewById(R.id.beauty_whitening_text);
        mbeautywhiteningA = (ImageView)mBeautyView.findViewById(R.id.beauty_whiteningA);
        mBeautyWhiteningAll = (LinearLayout)mBeautyView.findViewById(R.id.beauty_whitening_all);
        mBeautyWhiteningAll.setVisibility(View.GONE);
        mbeautywhiteningA.setVisibility(View.VISIBLE);
        mbeautywhiteningB = (ImageView)mBeautyView.findViewById(R.id.beauty_whiteningB);
        mbeautywhiteningB.setVisibility(View.GONE);
        mbeautywhiteningtext.setEnabled(false);

        // 较色
        mAdjustColorLayout = (LinearLayout) mBeautyView.findViewById(R.id.adjust_color_layout);
        mAdjustColorSwitch = (Switch) mBeautyView.findViewById(R.id.adjust_color_switch);

        // 锐化
        mSharpenLayout = (LinearLayout) mBeautyView.findViewById(R.id.sharpen_layout);
        mSharpenSwitch = (Switch) mBeautyView.findViewById(R.id.sharpen_switch);


        /* A2 美型*/
        mBeauty_shape_switch = (Switch) mBeautyView.findViewById(R.id.beauty_shape_switch);
        mBeauty_shape_switch_text = (TextView) mBeautyView.findViewById(R.id.beauty_shape_switch_text);
        mBeautyShapeResetLayout = (LinearLayout) mBeautyView.findViewById(R.id.beauty_shape_reset_layout);
        mBeautyShapeResetIcon = (ImageView) mBeautyView.findViewById(R.id.beauty_shape_reset_icon);
        mBeautyShapeResetTxt = (TextView) mBeautyView.findViewById(R.id.beauty_shape_reset_txt);
        mShapeRecyclerView = (RecyclerView) mBeautyView.findViewById(R.id.beauty_shape_item_list);

        mRecordTime = (TextView) findViewById(R.id.recordTime);
        mImageAutoFocusRect = (ImageView) findViewById(R.id.imageAutoFocusRect);
        mDelete = (ImageView) findViewById(R.id.delete);
        mNext = (ImageView) findViewById(R.id.next);
        mStartLayout = (RelativeLayout) findViewById(R.id.startLayout);
        mStartRecordingImage = (ImageView) findViewById(R.id.startRecordingImage);
        mStartText = (TextView) findViewById(R.id.startText);
        mLiveWindow = (NvsLiveWindow) findViewById(R.id.liveWindow);
        mCloseButton = (Button) findViewById(R.id.closeButton);
        mSwitchFacingLayout = (LinearLayout) findViewById(R.id.switchFacingLayout);
        mFlashLayout = (LinearLayout) findViewById(R.id.flashLayout);
        mFlashButton = (ImageView) findViewById(R.id.flashButton);
        mZoomLayout = (LinearLayout) findViewById(R.id.zoomLayout);
        mExposureLayout = (LinearLayout) findViewById(R.id.exposureLayout);
        mBeautyLayout = (LinearLayout) findViewById(R.id.beautyLayout);
        mFilterLayout = (LinearLayout) findViewById(R.id.filterLayout);
        mFuLayout = (LinearLayout) findViewById(R.id.fuLayout);

        /*拍照or视频*/
        mTypeRightView = findViewById(R.id.rightView);
        mTypePictureBtn = (Button) findViewById(R.id.type_picture_btn);
        mTypeVideoBtn = (Button) findViewById(R.id.type_video_btn);
        mSelectLayout = (RelativeLayout) findViewById(R.id.select_layout);
        mPictureLayout = (RelativeLayout) findViewById(R.id.picture_layout);
        mPictureCancel = (Button) findViewById(R.id.picture_cancel);
        mPictureOk = (Button) findViewById(R.id.picture_ok);
        mPictureImage = (ImageView) findViewById(R.id.picture_image);

        mCaptureZoomAndExposeDialog = new AlertDialog.Builder(this).create();
        mCaptureZoomAndExposeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                closeCaptureDialogView(mCaptureZoomAndExposeDialog);
            }
        });

        mCaptureBeautyDialog = new AlertDialog.Builder(this).create();
        mCaptureBeautyDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                closeCaptureDialogView(mCaptureBeautyDialog);
            }
        });
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

    /**
     * 滤镜数据初始化
     */
    private void initFilterList() {
        mFilterDataArrayList.clear();
        mFilterDataArrayList = AssetFxUtil.getFilterData(this,
                getLocalData(NvAsset.ASSET_FILTER),
                null,
                true,
                false);
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
        // 先设置滤镜数据
        mFilterView.setFilterArrayList(mFilterDataArrayList);
        mFilterView.initFilterRecyclerView(this);
        mFilterView.setIntensityLayoutVisible(View.INVISIBLE);
        mFilterView.setIntensityTextVisible(View.GONE);
        mFilterView.setFilterListener(new FilterView.OnFilterListener() {
            @Override
            public void onItmeClick(View v, int position) {
                int count = mFilterDataArrayList.size();
                if (position < 0 || position >= count) {
                    return;
                }
                if (mFilterSelPos == position) {
                    return;
                }
                mFilterSelPos = position;
                removeAllFilterFx();
                mFilterView.setIntensitySeekBarMaxValue(100);
                mFilterView.setIntensitySeekBarProgress(100);
                if (position == 0) {
                    mFilterView.setIntensityLayoutVisible(View.INVISIBLE);
                    mVideoClipFxInfo.setFxMode(VideoClipFxInfo.FXMODE_BUILTIN);
                    mVideoClipFxInfo.setFxId(null);
                    return;
                }
                mFilterView.setIntensityLayoutVisible(View.VISIBLE);
                FilterItem filterItem = mFilterDataArrayList.get(position);
                int filterMode = filterItem.getFilterMode();
                if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                    String filterName = filterItem.getFilterName();

                    if (!TextUtils.isEmpty(filterName) && filterItem.getIsCartoon()) {
                        // 删除美颜美颜开关关闭,若添加美颜则删除当前美颜
                        mBeauty_switch.setChecked(false);
                        // 删除美型效果
                        mBeauty_shape_switch.setChecked(false);
                        mCurCaptureVideoFx = mStreamingContext.appendBuiltinCaptureVideoFx("Cartoon");
                        mCurCaptureVideoFx.setBooleanVal("Stroke Only", filterItem.getStrokenOnly());
                        mCurCaptureVideoFx.setBooleanVal("Grayscale", filterItem.getGrayScale());
                    } else if (!TextUtils.isEmpty(filterName)) {
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
                // 拍摄进入下载，不作比例适配
                TimelineData.instance().setMakeRatio(NvAsset.AspectRatio_NoFitRatio);
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

    /**
     * 道具-数据
     */
    private void initFacUPropDataList() {
        mPropDataArrayList.clear();
        ArrayList<NvAsset> faceULocalDataList = getLocalData(NvAsset.ASSET_ARSCENE_FACE);
        mPropDataArrayList = AssetFxUtil.getFaceUDataList(faceULocalDataList, null);
    }

    /**
     * 道具-Dialog
     */
    private void initFacUPropDialog() {
        mFaceUPropDialog = new AlertDialog.Builder(this).create();
        mFaceUPropDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                closeCaptureDialogView(mFaceUPropDialog);
            }
        });
        mFaceUPropView = new FaceUPropView(this);
        mFaceUPropView.setPropDataArrayList(mPropDataArrayList);
        mFaceUPropView.initPropRecyclerView(this);
        mFaceUPropView.setFaceUPropListener(new FaceUPropView.OnFaceUPropListener() {
            @Override
            public void onItmeClick(View v, int position) {
                int count = mPropDataArrayList.size();
                if (position < 0 || position >= count) {
                    return;
                }
                if (mFaceUPropSelPos == position) {
                    return;
                }
                mFaceUPropSelPos = position;
                mArSceneId = mPropDataArrayList.get(position).getPackageId();
                if (mArSceneFaceEffect == null) {
                    return;
                }
                FilterItem item = mPropDataArrayList.get(position);
                String sceneId = item.getPackageId();
                mArSceneFaceEffect.setStringVal("Scene Id", sceneId);
            }

            @Override
            public void onMoreFaceUProp() {
                TimelineData.instance().setMakeRatio(NvAsset.AspectRatio_NoFitRatio);
                mFaceUPropView.setMoreFaceUPropClickable(false);
                Bundle bundle = new Bundle();
                bundle.putInt("titleResId", R.string.moreFaceU);
                bundle.putInt("assetType", NvAsset.ASSET_ARSCENE_FACE);
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), AssetDownloadActivity.class, bundle, ARFACE_LIST_REQUES_CODE);
            }
        });
    }

    private void initBeautyRecyclerView() {
        mBeautyAdapter = new BeautyShapeAdapter(this, new CaptureDataHelper().getBeautyDataListByType(CaptureActivity.this, mCanUseARFaceType));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mBeautyRecyclerView.setLayoutManager(layoutManager);
        mBeautyRecyclerView.setAdapter(mBeautyAdapter);
        mBeautyAdapter.setEnable(false);
        mBeautyAdapter.setOnItemClickListener(new BeautyShapeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (position < BeautyShapeAdapter.POS_BEAUTY_ADJUSTCOLOR_3) {
                    mBeautySeekBar.setVisibility(View.VISIBLE);

                    // 美颜程度
                    double level = 0.0;
                    mCurBeautyId = mBeautyAdapter.getSelectItem().beautyShapeId;
                    if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                        level = mArSceneFaceEffect.getFloatVal(mCurBeautyId);
                        mBeautySeekBar.setProgress((int) (level * 100));
                    } else {
                        // 其他
                        level = mBeautyFx.getFloatVal(mCurBeautyId);
                        mBeautySeekBar.setProgress((int) (level * 100));
                    }
                } else if(position == BeautyShapeAdapter.POS_BEAUTY_ADJUSTCOLOR_3) {
                    // 判断基础滤镜
                    if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                        if (mArSceneFaceEffect.getBooleanVal("Default Beauty Enabled")) {
                            mBeautySeekBar.setVisibility(View.VISIBLE);
                            mDefaultBeautyIntensity = mArSceneFaceEffect.getFloatVal("Default Intensity");
                            mBeautySeekBar.setProgress((int) (mDefaultBeautyIntensity * 100));
                        } else {
                            mBeautySeekBar.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (mBeautyFx.getBooleanVal("Default Beauty Enabled")) {
                            mBeautySeekBar.setVisibility(View.VISIBLE);
                            mDefaultBeautyIntensity = mBeautyFx.getFloatVal("Default Intensity");
                            mBeautySeekBar.setProgress((int) (mDefaultBeautyIntensity * 100));
                        } else {
                            mBeautySeekBar.setVisibility(View.INVISIBLE);
                        }
                    }

                } else {
                    mBeautySeekBar.setVisibility(View.INVISIBLE);
                }

                mBeautyWhiteningAll.setVisibility(View.GONE);
                mAdjustColorLayout.setVisibility(View.GONE);
                mSharpenLayout.setVisibility(View.GONE);
                if (position == BeautyShapeAdapter.POS_BEAUTY_WHITING_1) {
                    mBeautyWhiteningAll.setVisibility(View.VISIBLE);
                    mBeautyAdapter.setWittenName(1,getResources().getString(R.string.whitening_A));
                } else if (position == BeautyShapeAdapter.POS_BEAUTY_ADJUSTCOLOR_3) {
                    mAdjustColorLayout.setVisibility(View.VISIBLE);
                } else if (position == BeautyShapeAdapter.POS_BEAUTY_SHARPEN_4) {
                    mSharpenLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initShapeRecyclerView() {
        mShapeAdapter = new BeautyShapeAdapter(this, new CaptureDataHelper().getShapeDataList(CaptureActivity.this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mShapeRecyclerView.setLayoutManager(linearLayoutManager);
        mShapeRecyclerView.setAdapter(mShapeAdapter);
        int space = ScreenUtils.dip2px(this, 8);
        mShapeRecyclerView.addItemDecoration(new SpaceItemDecoration(space, 0));
        mShapeAdapter.setOnItemClickListener(new BeautyShapeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < 0 || position >= mShapeAdapter.getItemCount()) {
                    return;
                }
                mShapeSeekBar.setVisibility(View.VISIBLE);
                // 美型程度
                double level = 0.0;
                if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                    String beautyShapeId = mShapeAdapter.getSelectItem().beautyShapeId;
                    level = mArSceneFaceEffect.getFloatVal(beautyShapeId);
                    if (mShapeIdList.contains(beautyShapeId)) {
                        mShapeSeekBar.setProgress((int) (100 - level * 100));
                    } else {
                        // 美型特效值的范围[-1,1]
                        mShapeSeekBar.setProgress((int) (level * 100 + 100));
                    }
                } else {
                    // 其他
                }
            }
        });
    }

    private void shapeLayoutEnabled(Boolean isEnabled) {
        mBeautyShapeResetLayout.setEnabled(isEnabled);
        mBeautyShapeResetLayout.setClickable(isEnabled);
        mShapeAdapter.setEnable(isEnabled);
        if (isEnabled) {
            mBeautyShapeResetIcon.setAlpha(1f);
            mBeautyShapeResetTxt.setTextColor(Color.WHITE);
        } else {
            mBeautyShapeResetIcon.setAlpha(0.5f);
            mBeautyShapeResetTxt.setTextColor(getResources().getColor(R.color.ms_disable_color));
        }
    }

    /**
     * 美颜、美型初始值
     */
    private void initBeautyData() {
        if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
            // ARScene可用
            mArSceneFaceEffect = mStreamingContext.appendBuiltinCaptureVideoFx("AR Scene");
            if (mArSceneFaceEffect != null) {
                // 美型美颜数据初始化
                resetBeautyShapeDefaultValue();
            }
        } else {
            // 其他特效
        }
    }

    private void updateTypeRightView() {
        mTypeRightView.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = mTypeRightView.getLayoutParams();
                layoutParams.width = mTypePictureBtn.getWidth();
                mTypeRightView.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initData() {
        updateTypeRightView();
        initCaptureData();
        initCapture();
        searchAssetData();
        initBeautyData();
        // 滤镜初始化
        initFilterList();
        initFilterDialog();
        // 人脸道具初始化
        initFacUPropDataList();
        initFacUPropDialog();
        // 美型初始化
        initShapeRecyclerView();
        // 美颜初始化
        initBeautyRecyclerView();
        mBeautyTabButton.setSelected(true);
        shapeLayoutEnabled(false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {
        initBeautyClickListener();
        mLiveWindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float rectHalfWidth = mImageAutoFocusRect.getWidth() / 2;
                if (event.getX() - rectHalfWidth >= 0 && event.getX() + rectHalfWidth <= mLiveWindow.getWidth()
                        && event.getY() - rectHalfWidth >= 0 && event.getY() + rectHalfWidth <= mLiveWindow.getHeight()) {
                    mImageAutoFocusRect.setX(event.getX() - rectHalfWidth);
                    mImageAutoFocusRect.setY(event.getY() - rectHalfWidth);
                    RectF rectFrame = new RectF();
                    rectFrame.set(mImageAutoFocusRect.getX(), mImageAutoFocusRect.getY(),
                            mImageAutoFocusRect.getX() + mImageAutoFocusRect.getWidth(),
                            mImageAutoFocusRect.getY() + mImageAutoFocusRect.getHeight());
                    //启动自动聚焦
                    mImageAutoFocusRect.startAnimation(mFocusAnimation);
                    if (m_supportAutoFocus) {
                        mStreamingContext.startAutoFocus(new RectF(rectFrame));
                    }
                }
                return false;
            }
        });
        /*变焦调节*/
        mZoomSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAbs() {
            private boolean startTracking = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (startTracking) {
                    if (mCaptureType == Constants.CAPTURE_TYPE_ZOOM) {
                        // 设置缩放比例
                        mStreamingContext.setZoom(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                super.onStartTrackingTouch(seekBar);
                startTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                startTracking = false;
            }
        });
        /*曝光补偿调节*/
        mExposeSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAbs() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mCaptureType == Constants.CAPTURE_TYPE_EXPOSE) {
                    // 设置曝光补偿
                    mStreamingContext.setExposureCompensation(progress + mMinExpose);
                    mSeekProgress.setText(progress + mMinExpose + "");
                }
            }
        });
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*切换摄像头*/
        mSwitchFacingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsSwitchingCamera) {
                    return;
                }
                if (mCurrentDeviceIndex == 0) {
                    mCurrentDeviceIndex = 1;
                    mFlashLayout.setEnabled(false);
                    mFlashButton.setImageResource(R.mipmap.icon_flash_off);
                    mFlashButton.setImageAlpha(128);
                } else {
                    mCurrentDeviceIndex = 0;
                    mFlashLayout.setEnabled(true);
                    mFlashButton.setImageResource(R.mipmap.icon_flash_off);
                    mFlashButton.setImageAlpha(255);
                }

                mIsSwitchingCamera = true;
                startCapturePreview(true);
            }
        });
        /*闪光灯*/
        mFlashLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStreamingContext.isFlashOn()) {
                    mStreamingContext.toggleFlash(false);
                    mFlashButton.setImageResource(R.mipmap.icon_flash_off);
                    mFlashButton.setImageAlpha(255);
                } else {
                    mStreamingContext.toggleFlash(true);
                    mFlashButton.setImageResource(R.mipmap.icon_flash_on);
                    mFlashButton.setImageAlpha(255);
                }
            }
        });
        /*变焦*/
        mZoomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCaptureType = Constants.CAPTURE_TYPE_ZOOM;
                mSeekTitle.setText(R.string.picture_zoom);
                mSeekProgress.setVisibility(View.INVISIBLE);
                mZoomSeekbar.setVisibility(View.VISIBLE);
                mExposeSeekbar.setVisibility(View.INVISIBLE);
                showCaptureDialogView(mCaptureZoomAndExposeDialog, mZoomView);
            }
        });
        /*曝光补偿*/
        mExposureLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                mCaptureType = Constants.CAPTURE_TYPE_EXPOSE;
                mSeekTitle.setText(R.string.exposure_compensation);
                mSeekProgress.setVisibility(View.VISIBLE);
                mSeekProgress.setText(mExposeSeekbar.getProgress() + mMinExpose + "");
                mZoomSeekbar.setVisibility(View.INVISIBLE);
                mExposeSeekbar.setVisibility(View.VISIBLE);
                showCaptureDialogView(mCaptureZoomAndExposeDialog, mZoomView);
            }
        });
        /*美颜*/
        mBeautyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCaptureDialogView(mCaptureBeautyDialog, mBeautyView);
            }
        });

        mBeautyShapeResetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShapeSeekBar.setVisibility(View.INVISIBLE);
                resetBeautyShapeDefaultValue();
                mShapeSeekBar.setProgress(100);
                mShapeAdapter.setSelectPos(Integer.MAX_VALUE);
            }
        });

        /*滤镜*/
        mFilterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCaptureDialogView(mFilterDialog, mFilterView);
            }
        });
        /*道具*/
        mFuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 只有美摄道具才可以使用
                if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                    if (initArScene) {
                        showCaptureDialogView(mFaceUPropDialog, mFaceUPropView);
                    } else {
                        // 授权过期
                        String[] versionName = getResources().getStringArray(R.array.sdk_expire_tips);
                        Util.showDialog(CaptureActivity.this, versionName[0], versionName[1]);
                    }
                } else {
                    String[] versionName = getResources().getStringArray(R.array.sdk_version_tips);
                    Util.showDialog(CaptureActivity.this, versionName[0], versionName[1]);
                }
            }
        });
        /*开始录制*/
        mStartRecordingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 当前在录制状态，可停止视频录制
                if (getCurrentEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
                    stopRecording();
                } else {
                    mCurRecordVideoPath = PathUtils.getRecordVideoPath();
                    if (mCurRecordVideoPath == null) {
                        return;
                    }
                    mStartRecordingImage.setEnabled(false);
                    // 拍视频or拍照片
                    if (mRecordType == Constants.RECORD_TYPE_VIDEO) {
                        mStartRecordingImage.setBackgroundResource(R.mipmap.particle_capture_recording);
                        mEachRecodingVideoTime = 0;
                        //当前未在视频录制状态，则启动视频录制。此处使用带特效的录制方式
                        if (!mStreamingContext.startRecording(mCurRecordVideoPath)) {
                            return;
                        }
                        isInRecording(false);
                        mRecordFileList.add(mCurRecordVideoPath);
                    } else if (mRecordType == Constants.RECORD_TYPE_PICTURE) {
                        mStreamingContext.startRecording(mCurRecordVideoPath);
                        isInRecording(false);
                    }
                }
            }
        });
        /*删除视频*/
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecordTimeList.size() != 0 && mRecordFileList.size() != 0) {
                    mAllRecordingTime -= mRecordTimeList.get(mRecordTimeList.size() - 1);
                    mRecordTimeList.remove(mRecordTimeList.size() - 1);
                    PathUtils.deleteFile(mRecordFileList.get(mRecordFileList.size() - 1));
                    mRecordFileList.remove(mRecordFileList.size() - 1);
                    mRecordTime.setText(TimeFormatUtil.formatUsToString2(mAllRecordingTime));

                    if (mRecordTimeList.size() == 0) {
                        mStartText.setVisibility(View.GONE);
                        mDelete.setVisibility(View.GONE);
                        mNext.setVisibility(View.GONE);
                        mRecordTime.setVisibility(View.INVISIBLE);
                    } else {
                        mStartText.setText(mRecordTimeList.size() + "");
                        mRecordTime.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
        /*下一步，进入编辑*/
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*将拍摄的视频传到下一个页面mRecordFileList*/
                ArrayList<ClipInfo> pathList = new ArrayList<>();
                for (int i = 0; i < mRecordFileList.size(); i++) {
                    ClipInfo clipInfo = new ClipInfo();
                    clipInfo.setFilePath(mRecordFileList.get(i));
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
                mNext.setClickable(false);

                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.START_ACTIVITY_FROM_CAPTURE, true);
                AppManager.getInstance().jumpActivity(CaptureActivity.this, VideoEditActivity.class, bundle);
            }
        });

        mTypePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRecordType(true);
            }
        });

        mTypeRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRecordType(false);
            }
        });

        mPictureCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurRecordVideoPath != null) {
                    File file = new File(mCurRecordVideoPath);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                showPictureLayout(false);
                isInRecording(true);
                if (mRecordTimeList.isEmpty()) {
                    mDelete.setVisibility(View.INVISIBLE);
                    mNext.setVisibility(View.INVISIBLE);
                    mStartText.setVisibility(View.INVISIBLE);
                }
            }
        });

        mPictureOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 拍照片
                if (mRecordType == Constants.RECORD_TYPE_PICTURE) {
                    mAllRecordingTime += mEachRecodingImageTime;
                    mRecordTimeList.add(mEachRecodingImageTime);
                    mRecordTime.setText(TimeFormatUtil.formatUsToString2(mAllRecordingTime));
                    mStartText.setText(mRecordTimeList.size() + "");
                    isInRecording(true);
                }
                String jpgPath = PathUtils.getRecordPicturePath();
                boolean save_ret = Util.saveBitmapToSD(mPictureBitmap, jpgPath);
                if (save_ret) {
                    mRecordFileList.add(jpgPath);
                }
                if (mCurRecordVideoPath != null) {
                    File file = new File(mCurRecordVideoPath);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                showPictureLayout(false);
            }
        });
    }

    private void adjustColorOrBeauty() {
        // 判断基础滤镜
        if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
            if (mArSceneFaceEffect.getBooleanVal("Default Beauty Enabled")) {
                // 基础滤镜在最前
                if (mBeautyAdapter.getSelectPos() == BeautyShapeAdapter.POS_BEAUTY_ADJUSTCOLOR_3) {
                    mBeautySeekBar.setVisibility(View.VISIBLE);
                    mDefaultBeautyIntensity = mArSceneFaceEffect.getFloatVal("Default Intensity");
                    mBeautySeekBar.setProgress((int) (mDefaultBeautyIntensity * 100));
                }
            }
        } else {
            if (mBeautyFx.getBooleanVal("Default Beauty Enabled")) {
                // 基础滤镜在最前
                if (mBeautyAdapter.getSelectPos() == BeautyShapeAdapter.POS_BEAUTY_ADJUSTCOLOR_3) {
                    mBeautySeekBar.setVisibility(View.VISIBLE);
                    mDefaultBeautyIntensity = mBeautyFx.getFloatVal("Default Intensity");
                    mBeautySeekBar.setProgress((int) (mDefaultBeautyIntensity * 100));
                }
            }
        }
        // 判断美颜特效
        if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
            if (mBeautyAdapter.getSelectPos() >= 0 && mBeautyAdapter.getSelectPos() <= mBeautyAdapter.getItemCount()) {
                // 不是基础滤镜在最前
                if (mBeautyAdapter.getSelectPos() < BeautyShapeAdapter.POS_BEAUTY_ADJUSTCOLOR_3) {
                    mBeautySeekBar.setVisibility(View.VISIBLE);
                    double tempLevel = mArSceneFaceEffect.getFloatVal(mBeautyAdapter.getSelectItem().beautyShapeId);
                    mBeautySeekBar.setProgress((int) (tempLevel * 100));
                }
            }
        } else {
            if (mBeautyAdapter.getSelectPos() >= 0 && mBeautyAdapter.getSelectPos() <= mBeautyAdapter.getItemCount()) {
                // 不是基础滤镜在最前
                if (mBeautyAdapter.getSelectPos() < BeautyShapeAdapter.POS_BEAUTY_ADJUSTCOLOR_3) {
                    mBeautySeekBar.setVisibility(View.VISIBLE);
                    double tempLevel = mBeautyFx.getFloatVal(mBeautyAdapter.getSelectItem().beautyShapeId);
                    mBeautySeekBar.setProgress((int) (tempLevel * 100));
                }
            }
        }
    }

    /**
     * 美颜dialog 动作监听
     */
    private void initBeautyClickListener() {
        /*美颜tab*/
        mBeautyTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 控制点一次
                if (!mIsBeautyType) {
                    // 隐藏
                    mBeautySeekBar.setVisibility(View.INVISIBLE);
                    mShapeSeekBar.setVisibility(View.INVISIBLE);
                    if (mBeautySwitchIsOpend) {
                        adjustColorOrBeauty();
                    }
                    mIsBeautyType = true;
                    mBeautyTabButton.setSelected(true);
                    mShapeTabButton.setSelected(false);
                    mBeautySelectRelativeLayout.setVisibility(View.VISIBLE);
                    mShapeSelectRelativeLayout.setVisibility(View.GONE);
                }
            }
        });
        /*美型tab*/
        mShapeTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBeautyType) {
                    // 先隐藏
                    mBeautySeekBar.setVisibility(View.INVISIBLE);
                    mShapeSeekBar.setVisibility(View.INVISIBLE);
                    if (mShapeSwitchIsOpen && mShapeAdapter.getSelectPos() >= 0 && mShapeAdapter.getSelectPos() <= mShapeAdapter.getItemCount()) {
                        if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                            double tempLevel = mArSceneFaceEffect.getFloatVal(mShapeAdapter.getSelectItem().beautyShapeId);
                            if (mShapeIdList.contains(mShapeAdapter.getSelectItem().beautyShapeId)) {
                                mShapeSeekBar.setProgress((int) (100 - tempLevel * 100));
                            } else {
                                // 美型特效值的范围[-1,1]
                                mShapeSeekBar.setProgress((int) (tempLevel * 100 + 100));
                            }
                        }
                        mShapeSeekBar.setVisibility(View.VISIBLE);
                    }
                    mIsBeautyType = false;
                    mBeautyTabButton.setSelected(false);
                    mShapeTabButton.setSelected(true);
                    mBeautySelectRelativeLayout.setVisibility(View.GONE);
                    mShapeSelectRelativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        //美白模式
        mBeautyWhiteningAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronized(this){
                    if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                        if (mArSceneFaceEffect.getBooleanVal("Default Sharpen Enabled")) {
                            mArSceneFaceEffect.setBooleanVal("Default Sharpen Enabled", false);
                            mArSceneFaceEffect.setStringVal("Default Beauty Lut File", "assets:/capture/preset.mslut");
                            mArSceneFaceEffect.setStringVal("Whitening Lut File", "assets:/capture/filter.png");
                            mArSceneFaceEffect.setBooleanVal("Whitening Lut Enabled", true);
                            ToastUtil.showToastCenterNoBg(getApplicationContext(), getResources().getString(R.string.whiteningB));
                            mBeautyAdapter.setWittenName(1,getResources().getString(R.string.whitening_B));
                            mbeautywhiteningA.setVisibility(View.GONE);
                            mbeautywhiteningB.setVisibility(View.VISIBLE);
                        } else {
                            mArSceneFaceEffect.setBooleanVal("Default Sharpen Enabled", true);
                            mArSceneFaceEffect.setStringVal("Default Beauty Lut File", "");
                            mArSceneFaceEffect.setStringVal("Whitening Lut File", "");
                            mArSceneFaceEffect.setBooleanVal("Whitening Lut Enabled", false);
                            mbeautywhiteningA.setVisibility(View.VISIBLE);
                            mbeautywhiteningB.setVisibility(View.GONE);
                            ToastUtil.showToastCenterNoBg(getApplicationContext(), getResources().getString(R.string.whiteningA));
                            mBeautyAdapter.setWittenName(1,getResources().getString(R.string.whitening_A));
                        }
                    } else {
                        if (mBeautyFx.getBooleanVal("Default Sharpen Enabled")) {
                            mBeautyFx.setBooleanVal("Default Sharpen Enabled", false);
                            mBeautyFx.setStringVal("Default Beauty Lut File", "assets:/capture/preset.mslut");
                            mBeautyFx.setStringVal("Whitening Lut File", "assets:/capture/filter.png");
                            mBeautyFx.setBooleanVal("Whitening Lut Enabled", true);
                            mbeautywhiteningA.setVisibility(View.GONE);
                            mbeautywhiteningB.setVisibility(View.VISIBLE);
                            ToastUtil.showToastCenterNoBg(getApplicationContext(), getResources().getString(R.string.whiteningB));
                            mBeautyAdapter.setWittenName(1,getResources().getString(R.string.whitening_B));

                        } else {
                            mBeautyFx.setBooleanVal("Default Sharpen Enabled", true);
                            mBeautyFx.setStringVal("Default Beauty Lut File", "");
                            mBeautyFx.setStringVal("Whitening Lut File", "");
                            mBeautyFx.setBooleanVal("Whitening Lut Enabled", false);
                            ToastUtil.showToastCenterNoBg(getApplicationContext(), getResources().getString(R.string.whiteningA));
                            mBeautyAdapter.setWittenName(1,getResources().getString(R.string.whitening_A));
                            mbeautywhiteningA.setVisibility(View.VISIBLE);
                            mbeautywhiteningB.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        mSharpenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                synchronized (this) {
                    if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                        if (mArSceneFaceEffect.getBooleanVal("Default Sharpen Enabled")) {
                            mArSceneFaceEffect.setBooleanVal("Default Sharpen Enabled", false);
                            ToastUtil.showToastCenterNoBg(getApplicationContext(), getResources().getString(R.string.sharpen_close));
                        } else {
                            mArSceneFaceEffect.setBooleanVal("Default Sharpen Enabled", true);
                            ToastUtil.showToastCenterNoBg(getApplicationContext(), getResources().getString(R.string.sharpen_open));
                        }
                    } else {
                        if (mBeautyFx.getBooleanVal("Default Sharpen Enabled")) {
                            mBeautyFx.setBooleanVal("Default Sharpen Enabled", false);
                            ToastUtil.showToastCenterNoBg(getApplicationContext(), getResources().getString(R.string.sharpen_close));
                        } else {
                            mBeautyFx.setBooleanVal("Default Sharpen Enabled", true);
                            ToastUtil.showToastCenterNoBg(getApplicationContext(), getResources().getString(R.string.sharpen_open));
                        }
                    }
                }
            }
        });

        mAdjustColorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                    // ARScene 基础美颜开启 且在最前面
                    if (mArSceneFaceEffect.getBooleanVal("Default Beauty Enabled")) {
                        mArSceneFaceEffect.setBooleanVal("Default Beauty Enabled", false);
                        ToastUtil.showToastCenterNoBg(getApplicationContext(), getResources().getString(R.string.default_beauty_close));
                        mBeautySeekBar.setVisibility(View.INVISIBLE);
                    } else {
                        mBeautySeekBar.setVisibility(View.VISIBLE);
                        mArSceneFaceEffect.setBooleanVal("Default Beauty Enabled", true);
                        ToastUtil.showToastCenterNoBg(getApplicationContext(), getResources().getString(R.string.default_beauty_open));
                        double tempLevel = mArSceneFaceEffect.getFloatVal("Default Intensity");
                        mBeautySeekBar.setProgress((int) (tempLevel * 100));
                    }
                } else {
                    // 基本功能美颜
                    if (mBeautyFx.getBooleanVal("Default Beauty Enabled")) {
                        mBeautyFx.setBooleanVal("Default Beauty Enabled", false);
                        ToastUtil.showToastCenterNoBg(getApplicationContext(), getResources().getString(R.string.default_beauty_close));
                        mBeautySeekBar.setVisibility(View.INVISIBLE);
                    } else {
                        mBeautySeekBar.setVisibility(View.VISIBLE);
                        mBeautyFx.setBooleanVal("Default Beauty Enabled", true);
                        ToastUtil.showToastCenterNoBg(getApplicationContext(), getResources().getString(R.string.default_beauty_open));
                        double tempLevel = mBeautyFx.getFloatVal("Default Intensity");
                        mBeautySeekBar.setProgress((int) (tempLevel * 100));
                    }
                }
            }
        });

        /*美颜*/
        mBeauty_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBeautySwitchIsOpend = isChecked;
                if (isChecked) {
                    mBeautyWhiteningAll.setEnabled(true);

                    if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                        mArSceneFaceEffect.setBooleanVal("Beauty Effect", true);
                        // 基础滤镜强度
                        mDefaultBeautyIntensity = mArSceneFaceEffect.getFloatVal("Default Intensity");
                    } else {
                        // 内建美颜效果
                        mBeautyFx = mStreamingContext.appendBeautyCaptureVideoFx();
                        mBeautyFx.setBooleanVal("Default Sharpen Enabled", false);
                        // 基础滤镜强度
                        mDefaultBeautyIntensity = mBeautyFx.getFloatVal("Default Intensity");
                    }
//                    mBeautySeekBar.setVisibility(View.VISIBLE);
//                    mBeautySeekBar.setProgress((int) (mDefaultBeautyIntensity * 100));
                    boolean ret = removeFilterFxByName("Cartoon");
                    if (ret) {
                        mFilterView.setSelectedPos(0);
                        mFilterView.notifyDataSetChanged();
                    }
                    mBeauty_switch_text.setText(R.string.beauty_close);
                    mBeautyAdapter.setWittenName(1,getResources().getString(R.string.whitening));

                } else {
                    // 重置索引位置
                    mBeautyAdapter.setSelectPos(Integer.MAX_VALUE);
                    mBeautyWhiteningAll.setEnabled(false);
                    mAdjustColorLayout.setVisibility(View.INVISIBLE);
                    mSharpenLayout.setVisibility(View.INVISIBLE);

                    mBeautySeekBar.setVisibility(View.INVISIBLE);
                    mBeauty_switch_text.setText(R.string.beauty_open);
                    mBeautyAdapter.setWittenName(1,getResources().getString(R.string.whitening));
                    mBeautyWhiteningAll.setVisibility(View.GONE);
                    if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                        if (mArSceneFaceEffect != null) {
                            mArSceneFaceEffect.setFloatVal("Beauty Strength", 0.5);
                            mArSceneFaceEffect.setFloatVal("Beauty Whitening", 0);
                            mArSceneFaceEffect.setFloatVal("Beauty Reddening", 0);
                            mArSceneFaceEffect.setFloatVal("Default Intensity", 1.0);
                            mArSceneFaceEffect.setBooleanVal("Default Sharpen Enabled", false);
                            mArSceneFaceEffect.setBooleanVal("Beauty Effect", false);
                        }
                    } else {
                        mBeautyFx.setFloatVal("Default Intensity", 1.0);
                        mBeautyFx.setBooleanVal("Default Sharpen Enabled", false);
                        removeFilterFxByName("Beauty");
                        mBeautyFx = null;
                    }
                }
                mBeautyAdapter.setEnable(isChecked);
                mBeauty_switch.setChecked(isChecked);
            }
        });
        /*美型开关*/
        mBeauty_shape_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mShapeSwitchIsOpen = isChecked;
                if (mCanUseARFaceType != HUMAN_AI_TYPE_MS) {
                    // 美型不可用
                    mBeauty_shape_switch.setChecked(false);
                    String[] versionName = getResources().getStringArray(R.array.sdk_version_tips);
                    Util.showDialog(CaptureActivity.this, versionName[0], versionName[1]);
                } else {
                    // 美型可用
                    if (mCanUseARFaceType == HUMAN_AI_TYPE_MS && initArScene) {
                        // 美摄功能
                        if (isChecked) {
                            // 美型功能打开
                            mArSceneFaceEffect.setBooleanVal("Beauty Shape", true);
                            mBeauty_shape_switch_text.setText(R.string.beauty_shape_close);
                            boolean ret = removeFilterFxByName("Cartoon");
                            if (ret) {
                                mFilterView.setSelectedPos(0);
                                mFilterView.notifyDataSetChanged();
                            }
                        } else {
                            // 美型功能关闭;
                            resetBeautyShapeDefaultValue();
                            mArSceneFaceEffect.setBooleanVal("Beauty Shape", false);
                            mBeauty_shape_switch_text.setText(R.string.beauty_shape_open);
                            mShapeAdapter.setSelectPos(Integer.MAX_VALUE);
                            mShapeSeekBar.setVisibility(View.INVISIBLE);
                        }
                        mBeauty_shape_switch.setChecked(isChecked);
                        shapeLayoutEnabled(isChecked);
                    } else {
                        // 授权过期
                        String[] versionName = getResources().getStringArray(R.array.sdk_expire_tips);
                        Util.showDialog(CaptureActivity.this, versionName[0], versionName[1]);
                        mBeauty_shape_switch.setChecked(false);
                    }
                }
            }
        });
        /*ShapeSeekBar调整*/
        mShapeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAbs() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mShapeAdapter.getSelectPos() >= 0 && mShapeAdapter.getSelectPos() <= mShapeAdapter.getItemCount()) {
                    // 两种情况
                    if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                        // 美摄美型
                        if (mArSceneFaceEffect == null) {
                            return;
                        }
                        boolean containsShapeId = mShapeIdList.contains(mShapeAdapter.getSelectItem().beautyShapeId);
                        mArSceneFaceEffect.setFloatVal(mShapeAdapter.getSelectItem().beautyShapeId, containsShapeId ? ((float) (100 - progress) / 100) : ((float) (progress - 100) / 100));
                    } else {
                        // 其他美型
                    }
                }
            }
        });
        /*BeautySeekbar调整*/
        mBeautySeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAbs() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
                    if (mArSceneFaceEffect.getBooleanVal("Default Beauty Enabled") && mBeautyAdapter.getSelectPos() == BeautyShapeAdapter.POS_BEAUTY_ADJUSTCOLOR_3) {
                        // 基础滤镜
                        mDefaultBeautyIntensity = progress * 1.0 / 100;
                        mArSceneFaceEffect.setFloatVal("Default Intensity", mDefaultBeautyIntensity);
                        // 美颜
                    } else if (mBeautyAdapter.getSelectPos() >= 0 && mBeautyAdapter.getSelectPos() < BeautyShapeAdapter.POS_BEAUTY_ADJUSTCOLOR_3) {
                        double tempLevel = progress * 1.0 / 100;
                        mArSceneFaceEffect.setFloatVal(mBeautyAdapter.getSelectItem().beautyShapeId, tempLevel);
                    }

                } else {
                    if (mBeautyFx.getBooleanVal("Default Beauty Enabled") && mBeautyAdapter.getSelectPos() == BeautyShapeAdapter.POS_BEAUTY_ADJUSTCOLOR_3) {
                        // 基础滤镜
                        mDefaultBeautyIntensity = progress * 1.0 / 100;
                        mBeautyFx.setFloatVal("Default Intensity", mDefaultBeautyIntensity);
                        // 美颜
                    } else if (mBeautyAdapter.getSelectPos() >= 0 && mBeautyAdapter.getSelectPos() < BeautyShapeAdapter.POS_BEAUTY_ADJUSTCOLOR_3) {
                        double tempLevel = progress * 1.0 / 100;
                        mBeautyFx.setFloatVal(mBeautyAdapter.getSelectItem().beautyShapeId, tempLevel);
                    }
                }
            }
        });
    }

    /**
     * 重置、默认初始值
     */
    private void resetBeautyShapeDefaultValue() {

        if (mCanUseARFaceType == HUMAN_AI_TYPE_MS) {
            // 美摄美型
            mArSceneFaceEffect.setFloatVal("Face Size Warp Degree", 0f);
            mArSceneFaceEffect.setFloatVal("Eye Size Warp Degree", 0f);
            mArSceneFaceEffect.setFloatVal("Chin Length Warp Degree", 0f);
            mArSceneFaceEffect.setFloatVal("Face Length Warp Degree", 0f);
            mArSceneFaceEffect.setFloatVal("Face Width Warp Degree", 0f);
            mArSceneFaceEffect.setFloatVal("Forehead Height Warp Degree", 0f);
            mArSceneFaceEffect.setFloatVal("Nose Width Warp Degree", 0f);
            mArSceneFaceEffect.setFloatVal("Nose Length Warp Degree", 0f);
            mArSceneFaceEffect.setFloatVal("Eye Corner Stretch Degree", 0f);
            mArSceneFaceEffect.setFloatVal("Mouth Size Warp Degree", 0f);
            mArSceneFaceEffect.setFloatVal("Mouth Corner Lift Degree", 0f);
        } else {
            // 其他美型 或 HUMAN_AI_TYPE_NONE
            return;
        }
    }

    private void stopRecording() {
        mStreamingContext.stopRecording();
        mStartRecordingImage.setBackgroundResource(R.mipmap.capture_recording_stop);

        // 拍视频
        if (mRecordType == Constants.RECORD_TYPE_VIDEO) {
            mAllRecordingTime += mEachRecodingVideoTime;
            mRecordTimeList.add(mEachRecodingVideoTime);
            mStartText.setText(mRecordTimeList.size() + "");
            isInRecording(true);
        }
    }

    private void removeAllFilterFx() {
        List<Integer> remove_list = new ArrayList<>();
        for (int i = 0; i < mStreamingContext.getCaptureVideoFxCount(); i++) {
            NvsCaptureVideoFx fx = mStreamingContext.getCaptureVideoFxByIndex(i);
            if (fx == null) {
                continue;
            }
            String name = fx.getBuiltinCaptureVideoFxName();
            if (name != null && !"Beauty".equals(name) && !"Face Effect".equals(name) && !"AR Scene".equals(name)) {
                remove_list.add(i);
                Log.e("===>", "fx name: " + name);
            }
        }
        if (!remove_list.isEmpty()) {
            for (int i = 0; i < remove_list.size(); i++) {
                mStreamingContext.removeCaptureVideoFx(remove_list.get(i));
            }
        }
    }

    private boolean removeFilterFxByName(String name) {
        for (int i = 0; i < mStreamingContext.getCaptureVideoFxCount(); i++) {
            NvsCaptureVideoFx fx = mStreamingContext.getCaptureVideoFxByIndex(i);
            if (fx.getDescription().getName().equals(name)) {
                mStreamingContext.removeCaptureVideoFx(i);
                return true;
            }
        }
        return false;
    }

    /**
     * 显示窗口
     */
    private void showCaptureDialogView(AlertDialog dialog, View view) {
        TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        // 动画时间500毫秒
        translate.setDuration(200);
        // 动画出来控件可以点击
        translate.setFillAfter(false);
        mStartLayout.startAnimation(translate);
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
        isShowCaptureButton(false);
    }

    /**
     * 关闭窗口
     */
    private void closeCaptureDialogView(AlertDialog dialog) {
        dialog.dismiss();
        TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        // 动画时间300毫秒
        translate.setDuration(300);
        // 动画出来控件可以点击
        translate.setFillAfter(false);
        mStartLayout.setAnimation(translate);
        isShowCaptureButton(true);
    }

    private void initCaptureData() {
        mStreamingContext.removeAllCaptureVideoFx();
        mFocusAnimation = new AlphaAnimation(1.0f, 0.0f);
        mFocusAnimation.setDuration(1000);
        mFocusAnimation.setFillAfter(true);
        mCanUseARFaceType = NvsStreamingContext.hasARModule();
    }

    private void initCapture() {
        if (null == mStreamingContext) {
            return;
        }
        // 给Streaming Context设置采集设备回调接口
        setStreamingCallback(false);
        if (mStreamingContext.getCaptureDeviceCount() == 0) {
            return;
        }

        // 将采集预览输出连接到LiveWindow控件
        if (!mStreamingContext.connectCapturePreviewWithLiveWindow(mLiveWindow)) {
            Log.e(TAG, "Failed to connect capture preview with livewindow!");
            return;
        }

        mCurrentDeviceIndex = 0;
        // 采集设备数量判定
        if (mStreamingContext.getCaptureDeviceCount() > 1) {
            mSwitchFacingLayout.setEnabled(true);
        } else {
            mSwitchFacingLayout.setEnabled(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else {
            try {
                startCapturePreview(false);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "startCapturePreviewException: initCapture failed,under 6.0 device may has no access to camera");
                // 拒绝后，所有按钮禁止点击
                PermissionDialog.noPermissionDialog(CaptureActivity.this);
                setCaptureViewEnable(false);
            }
        }
        setCaptureViewEnable(true);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                initArScene = bundle.getBoolean("initArScene");
            }
        }
    }

    private boolean startCapturePreview(boolean deviceChanged) {
        // 判断当前引擎状态是否为采集预览状态
        int captureResolutionGrade = ParameterSettingValues.instance().getCaptureResolutionGrade();
        if (deviceChanged || getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTUREPREVIEW) {
            m_supportAutoFocus = false;
            if (!mStreamingContext.startCapturePreview(mCurrentDeviceIndex, captureResolutionGrade,
                    NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_DONT_USE_SYSTEM_RECORDER |
                            NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_CAPTURE_BUDDY_HOST_VIDEO_FRAME |
                            NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_STRICT_PREVIEW_VIDEO_SIZE, null)) {
                Log.e(TAG, "Failed to start capture preview!");
                return false;
            }
        }
        return true;
    }


    /**
     * 获取当前引擎状态
     */
    private int getCurrentEngineState() {
        return mStreamingContext.getStreamingEngineState();
    }

    private void updateSettingsWithCapability(int deviceIndex) {
        // 获取采集设备能力描述对象，设置自动聚焦，曝光补偿，缩放
        mCapability = mStreamingContext.getCaptureDeviceCapability(deviceIndex);
        if (null == mCapability) {
            return;
        }

        // 是否支持闪光灯
        if (mCapability.supportFlash) {
            mFlashLayout.setEnabled(true);
        }

        m_supportAutoFocus = mCapability.supportAutoFocus;

        // 是否支持缩放
        if (mCapability.supportZoom) {
            mZoomValue = mCapability.maxZoom;
            mZoomSeekbar.setMax(mZoomValue);
            mZoomSeekbar.setProgress(mStreamingContext.getZoom());
            mZoomSeekbar.setEnabled(true);
        } else {
            Log.e(TAG, "该设备不支持缩放");
        }

        // 是否支持曝光补偿
        if (mCapability.supportExposureCompensation) {
            mMinExpose = mCapability.minExposureCompensation;
            mExposeSeekbar.setMax(mCapability.maxExposureCompensation - mMinExpose);
            mExposeSeekbar.setProgress(mStreamingContext.getExposureCompensation() - mMinExpose);
            mExposeSeekbar.setEnabled(true);
        }
    }

    private void isInRecording(boolean isInRecording) {
        int show;
        if (isInRecording) {
            show = View.VISIBLE;
            mRecordTime.setTextColor(0xffffffff);
        } else {
            mRecordTime.setTextColor(0xffD0021B);
            show = View.INVISIBLE;
        }
        mCloseButton.setVisibility(show);
        mFunctionButtonLayout.setVisibility(show);
        mDelete.setVisibility(show);
        mNext.setVisibility(show);
        mStartText.setVisibility(show);
        mSelectLayout.setVisibility(show);
        if (mRecordTimeList.isEmpty()) {
            mRecordTime.setVisibility(View.INVISIBLE);
        } else {
            mRecordTime.setVisibility(View.VISIBLE);
        }
    }

    private void isShowCaptureButton(boolean isShow) {
        int show;
        if (isShow) {
            show = View.VISIBLE;
        } else {
            show = View.INVISIBLE;
        }
        if (show == View.VISIBLE) {
            mCloseButton.requestLayout();
            mFunctionButtonLayout.requestLayout();
            mStartLayout.requestLayout();
            mRecordTime.requestLayout();
        }
        mCloseButton.setVisibility(show);
        mFunctionButtonLayout.setVisibility(show);
        mStartLayout.setVisibility(show);
        mRecordTime.setVisibility(show);
    }

    private ArrayList<NvAsset> getLocalData(int assetType) {
        return mAssetManager.getUsableAssets(assetType, NvAsset.AspectRatio_All, 0);
    }


    @Override
    public void onClick(View view) {
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
        mIsSwitchingCamera = false;
    }

    @Override
    public void onCaptureDeviceError(int i, int i1) {
        Log.e(TAG, "onCaptureDeviceError: initCapture failed,under 6.0 device may has no access to camera");
        // 没有权限之后，所有按钮禁止点击
        PermissionDialog.noPermissionDialog(CaptureActivity.this);
        setCaptureViewEnable(false);
    }

    @Override
    public void onCaptureDeviceStopped(int i) {

    }

    @Override
    public void onCaptureDeviceAutoFocusComplete(int i, boolean b) {

    }

    @Override
    public void onCaptureRecordingFinished(int i) {
        // 保存到媒体库
        if (mRecordFileList != null && !mRecordFileList.isEmpty()) {
            for (String path : mRecordFileList) {
                if (path == null) {
                    continue;
                }
                if (path.endsWith(".mp4")) {
                    MediaScannerUtil.scanFile(path, "video/mp4");
                } else if (path.endsWith(".jpg")) {
                    MediaScannerUtil.scanFile(path, "image/jpg");
                }
            }
        }
    }

    @Override
    public void onCaptureRecordingError(int i) {

    }

    @Override
    public void onCaptureRecordingDuration(int i, long l) {
        // 拍视频or拍照片
        if (mRecordType == Constants.RECORD_TYPE_VIDEO) {
            if (l >= Constants.MIN_RECORD_DURATION) {
                mStartRecordingImage.setEnabled(true);
            }
            mEachRecodingVideoTime = l;
            mRecordTime.setVisibility(View.VISIBLE);
            mRecordTime.setText(TimeFormatUtil.formatUsToString2(mAllRecordingTime + mEachRecodingVideoTime));
        } else if (mRecordType == Constants.RECORD_TYPE_PICTURE) {
            if (l > 40000) {
                stopRecording();
                takePhoto(l);
            }
        }
    }

    @Override
    public void onCaptureRecordingStarted(int i) {

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
                    // 页面返回 特效位置确定
                    mFaceUPropSelPos = AssetFxUtil.getSelectedFaceUPropPos(mPropDataArrayList, mArSceneId);
                    mFaceUPropView.setSelectedPos(mFaceUPropSelPos);
                    mFaceUPropView.notifyDataSetChanged();
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

    @Override
    protected void hasPermission() {
        //初始化拍摄
        startCapturePreview(false);
    }

    @Override
    protected void nonePermission() {
        Log.d(TAG, "initCapture failed,above 6.0 device may has no access to camera");
        // 拒绝后，所有按钮禁止点击
        setCaptureViewEnable(false);
        PermissionDialog.noPermissionDialog(CaptureActivity.this);
    }

    @Override
    protected void noPromptPermission() {
        // 拒绝了权限
        Logger.e(TAG, "initCapture failed,above 6.0 device has no access from user");
        setCaptureViewEnable(false);
        PermissionDialog.noPermissionDialog(CaptureActivity.this);
    }

    @Override
    protected void onDestroy() {
        destroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNext.setClickable(true);
        mFilterView.setMoreFilterClickable(true);
        mFaceUPropView.setMoreFaceUPropClickable(true);
        startCapturePreview(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getCurrentEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
            stopRecording();
        }
        // 修正摄像头状态
        mFlashButton.setImageResource(R.mipmap.icon_flash_off);
    }

    private void destroy() {
        if (mStreamingContext != null) {
            mStreamingContext.removeAllCaptureVideoFx();
            mStreamingContext.stop();
            setStreamingCallback(true);
            mStreamingContext = null;
        }
        mRecordTimeList.clear();
        mRecordFileList.clear();
        mFilterDataArrayList.clear();
        mPropDataArrayList.clear();
    }

    private void setStreamingCallback(boolean isDestroyCallback) {
        mStreamingContext.setCaptureDeviceCallback(isDestroyCallback ? null : this);
        mStreamingContext.setCaptureRecordingDurationCallback(isDestroyCallback ? null : this);
        mStreamingContext.setCaptureRecordingStartedCallback(isDestroyCallback ? null : this);
    }

    private void takePhoto(long time) {
        if (mCurRecordVideoPath != null) {
            NvsVideoFrameRetriever videoFrameRetriever = mStreamingContext.createVideoFrameRetriever(mCurRecordVideoPath);
            if (videoFrameRetriever != null) {
                mPictureBitmap = videoFrameRetriever.getFrameAtTimeWithCustomVideoFrameHeight(time, ScreenUtils.getScreenHeight(this));
                Log.e("===>", "screen: " + ScreenUtils.getScreenWidth(this) + " " + ScreenUtils.getScreenHeight(this));
                Log.e("===>", "picture: " + mPictureBitmap.getWidth() + " " + mPictureBitmap.getHeight());
                if (mPictureBitmap != null) {
                    mPictureImage.setImageBitmap(mPictureBitmap);
                    showPictureLayout(true);
                }
            }
        }
    }

    private void selectRecordType(boolean left_to_right) {
        TranslateAnimation ani;
        if (left_to_right) {
            if (mRecordType == Constants.RECORD_TYPE_PICTURE) {
                return;
            }
            ani = new TranslateAnimation(mTypePictureBtn.getX(), mTypeVideoBtn.getX(), 0, 0);
            mTypePictureBtn.setTextColor(ContextCompat.getColor(CaptureActivity.this, R.color.ms_red));
            mTypeVideoBtn.setTextColor(ContextCompat.getColor(CaptureActivity.this, R.color.white));
            mRecordType = Constants.RECORD_TYPE_PICTURE;
        } else {
            ani = new TranslateAnimation(mTypeVideoBtn.getX(), mTypePictureBtn.getX(), 0, 0);
            mTypePictureBtn.setTextColor(ContextCompat.getColor(CaptureActivity.this, R.color.white));
            mTypeVideoBtn.setTextColor(ContextCompat.getColor(CaptureActivity.this, R.color.ms_red));
            mRecordType = Constants.RECORD_TYPE_VIDEO;
        }
        ani.setDuration(300);
        ani.setFillAfter(true);
        mRecordTypeLayout.startAnimation(ani);
    }

    private void showPictureLayout(boolean show) {
        TranslateAnimation topTranslate;
        if (show) {
            mPictureLayout.setVisibility(View.INVISIBLE);
            topTranslate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            topTranslate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mPictureLayout.clearAnimation();
                    mCloseButton.setVisibility(View.GONE);
                    mPictureLayout.setVisibility(View.VISIBLE);
                    mPictureLayout.setClickable(true);
                    mPictureLayout.setFocusable(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {
            mStartRecordingImage.setEnabled(true);
            mCloseButton.setVisibility(View.VISIBLE);
            topTranslate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

            topTranslate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mPictureLayout.clearAnimation();
                    mPictureLayout.setVisibility(View.GONE);
                    mPictureLayout.setClickable(false);
                    mPictureLayout.setFocusable(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        topTranslate.setDuration(300);
        topTranslate.setFillAfter(true);
        mPictureLayout.setAnimation(topTranslate);
    }

    public void setCaptureViewEnable(boolean enable) {
        mBottomLayout.setEnabled(enable);
        mBottomLayout.setClickable(enable);
        mFunctionButtonLayout.setEnabled(enable);
        mFunctionButtonLayout.setClickable(enable);
        mRecordTypeLayout.setEnabled(enable);
        mRecordTypeLayout.setClickable(enable);
    }
}
