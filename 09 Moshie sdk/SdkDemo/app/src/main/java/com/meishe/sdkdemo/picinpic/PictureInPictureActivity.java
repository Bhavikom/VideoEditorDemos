package com.meishe.sdkdemo.picinpic;

import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;

import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BasePermissionActivity;
import com.meishe.sdkdemo.edit.CompileVideoFragment;
import com.meishe.sdkdemo.edit.adapter.SpaceItemDecoration;
import com.meishe.sdkdemo.edit.clipEdit.trim.TrimActivity;
import com.meishe.sdkdemo.edit.data.BackupData;
import com.meishe.sdkdemo.edit.interfaces.OnTitleBarClickListener;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.edit.watermark.SingleClickActivity;
import com.meishe.sdkdemo.picinpic.data.PicInPicDrawRect;
import com.meishe.sdkdemo.picinpic.data.PicInPicTemplateAsset;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.PathUtils;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PictureInPictureActivity extends BasePermissionActivity {
    private static final String TAG = "PicInPicActivity";
    private static final int PICINPICPREVIEW_REQUESTCODE = 1001;
    private static final int PICINPICREPLACE_REQUESTCODE = 1002;
    private static final int PICINPICTRIMCLIP_REQUESTCODE = 1003;
    private static final float defaultVolumeValue = 1.0f;
    private static final float muteVolumeValue = 0.0f;
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private NvsLiveWindow mLiveWindow;
    private PicInPicDrawRect mPIPDrawRect;
    private LinearLayout mPipToolBar;
    private ImageView mReplaceImage;
    private ImageView mZoomInImage;
    private ImageView mZoomOutImage;
    private ImageView mRotateImage;
    private ImageView mTrimImage;
    private ImageView mUpVideoVolumeButton;
    private ImageView mDownVideoVolumeButton;
    private LinearLayout mTemplateLayout;
    private LinearLayout mPreviewLayout;
    private RelativeLayout mTemplateListLayout;
    private RecyclerView mPicInPicTemplateList;
    private ImageView mTemplateFinish;
    private RelativeLayout mCompilePage;

    public static NvsTimeline mTimeline;
    private NvsVideoTrack mVideoTrackFir;
    private NvsVideoTrack mVideoTrackSec;
    private ArrayList<PicInPicTemplateAsset> mPiPTemplateAssetsArray;
    private CompileVideoFragment mCompileVideoFragment;
    private int mClipVideoIndex = 0;
    private int mVideoFxTemplateIndex = 0;
    private double mFirScaleValue = 1.0D,mSecScaleValue = 1.0D,mFirRotateAngle = 0,mSecRotateAngle = 0;
    private double mUpTransX = 0,mUpTransY = 0,mDownTransX = 0,mDownTransY = 0;

    private boolean mUpClipIsMute = false;
    private boolean mDownClipIsMute = false;
    int mLiveWindowWidth;
    int mLiveWidowHeight;
    private PointF mPrevPoint = new PointF();
    private RectF mShapeRectF = new RectF();
    private boolean mSelClickPosArea = false;
    private ArrayList<String> mFilePathList;
    private long mFirTrimInPoint = 0,mFirTrimOutPoint = 0,mSecTrimInPoint = 0,mSecTrimOutPoint = 0;

//    private ScaleGestureDetector mScaleGestureDetector;

    private static final Gson m_gson = new Gson();
    /**
     * Json转Java对象
     */
    private static <T> T fromJson(String json, Class<T> clz) {
        return m_gson.fromJson(json, clz);
    }

    @Override
    protected int initRootView() {
        return R.layout.activity_picture_in_picture;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout)findViewById(R.id.bottom_layout);
        mLiveWindow = (NvsLiveWindow)findViewById(R.id.liveWindow);
        mPIPDrawRect = (PicInPicDrawRect)findViewById(R.id.picInPicDrawRect);
        mPipToolBar = (LinearLayout)findViewById(R.id.pipToolBar);
        mReplaceImage = (ImageView) findViewById(R.id.replaceImage);
        mZoomInImage = (ImageView) findViewById(R.id.zoomInImage);
        mZoomOutImage = (ImageView) findViewById(R.id.zoomOutImage);
        mRotateImage = (ImageView) findViewById(R.id.rotateImage);
        mTrimImage = (ImageView) findViewById(R.id.trimImage);
        mUpVideoVolumeButton = (ImageView)findViewById(R.id.upVideoVolumeButton);
        mDownVideoVolumeButton = (ImageView)findViewById(R.id.downVideoVolumeButton);
        mTemplateLayout = (LinearLayout)findViewById(R.id.templateLayout);
        mPreviewLayout = (LinearLayout)findViewById(R.id.previewLayout);
        mTemplateListLayout = (RelativeLayout)findViewById(R.id.templateListLayout);
        mPicInPicTemplateList = (RecyclerView)findViewById(R.id.picInPicTemplateList);
        mTemplateFinish = (ImageView)findViewById(R.id.templateFinish);
        mCompilePage = (RelativeLayout) findViewById(R.id.compilePage);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.picInPicEdit);
        mTitleBar.setTextRight(R.string.compile);
        mTitleBar.setTextRightVisible(View.VISIBLE);
    }

    @Override
    protected void initData() {
        if(!createTimeline())
            return;
        setLiveWindowRatio(NvAsset.AspectRatio_9v16);

        initDataVar();
        parseJsonFileInfo();
        initPicInPicTemplateRecycerViewList();
        initCompileVideoFragment();
        initTrackClip();
    }

    @Override
    protected void initListener() {
        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                removeTimeline();
            }

            @Override
            public void OnCenterTextClick() {

            }

            @Override
            public void OnRightTextClick() {
                mCompilePage.setVisibility(View.VISIBLE);
                mCompileVideoFragment.compileVideo();
            }
        });

        if(mCompileVideoFragment != null){
            mCompileVideoFragment.setCompileVideoListener(new CompileVideoFragment.OnCompileVideoListener() {
                @Override
                public void compileProgress(NvsTimeline timeline, int progress) {

                }

                @Override
                public void compileFinished(NvsTimeline timeline) {
                    mCompilePage.setVisibility(View.GONE);
                }

                @Override
                public void compileFailed(NvsTimeline timeline) {
                    mCompilePage.setVisibility(View.GONE);
                }

                @Override
                public void compileCompleted(NvsTimeline nvsTimeline, boolean isCanceled) {
                    mCompilePage.setVisibility(View.GONE);
                }

                @Override
                public void compileVideoCancel() {
                    mCompilePage.setVisibility(View.GONE);
                }
            });
        }

        mLiveWindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RectF firstRectData = getDrawRectFData(mVideoFxTemplateIndex,0);
                RectF secondRectData = getDrawRectFData(mVideoFxTemplateIndex,1);
                boolean isFirContain = firstRectData.contains(event.getX(), event.getY());
                boolean isSecContain = secondRectData.contains(event.getX(), event.getY());
                if(mVideoFxTemplateIndex == 2){
                    if(isSecContain){
                        mClipVideoIndex = 1;
                        mShapeRectF = secondRectData;
                    }else {
                        if(isFirContain){
                            mClipVideoIndex = 0;
                            mShapeRectF = firstRectData;
                        }
                    }
                }else {
                    if(isFirContain){
                        mClipVideoIndex = 0;
                        mShapeRectF = firstRectData;
                    }else {
                        if(isSecContain){
                            mClipVideoIndex = 1;
                            mShapeRectF = secondRectData;
                        }
                    }
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPrevPoint.set(event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float transX = event.getX() - mPrevPoint.x;
                        float transY = mPrevPoint.y - event.getY();
                        transFormVideo(transX,transY);
                        mPrevPoint.set(event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        selectEditDrawRect(mShapeRectF);
                        break;
                }
                return true;
            }
        });
        mPIPDrawRect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                mScaleGestureDetector.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        mPrevPoint.set(event.getX(), event.getY());
                        mSelClickPosArea = mShapeRectF.contains(event.getX(),event.getY());
                        if(mVideoFxTemplateIndex == 2){
                            mSelClickPosArea = false;
                        }
                        break;
                    }
                    case MotionEvent.ACTION_MOVE:
                        if(!mSelClickPosArea)
                            break;
                        float transX = event.getX() - mPrevPoint.x;
                        float transY = mPrevPoint.y - event.getY();
                        transFormVideo(transX,transY);
                        mPrevPoint.set(event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_UP: {
                        mSelClickPosArea = false;
                        break;
                    }
                }
                return mSelClickPosArea;
            }
        });
//        mScaleGestureDetector = new ScaleGestureDetector(PictureInPictureActivity.this, new ScaleGestureDetector.OnScaleGestureListener() {
//            @Override
//            public boolean onScale(ScaleGestureDetector detector) {
//                float scale = detector.getScaleFactor();
//                Logger.e(TAG,"scale = " + scale);
//                return true;
//            }
//
//            @Override
//            public boolean onScaleBegin(ScaleGestureDetector detector) {
//                return true;
//            }
//
//            @Override
//            public void onScaleEnd(ScaleGestureDetector detector) {
//
//            }
//        });
        mPipToolBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mTemplateListLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mReplaceImage.setOnClickListener(this);
        mZoomInImage.setOnClickListener(this);
        mZoomOutImage.setOnClickListener(this);
        mRotateImage.setOnClickListener(this);
        mTrimImage.setOnClickListener(this);
        mUpVideoVolumeButton.setOnClickListener(this);
        mDownVideoVolumeButton.setOnClickListener(this);
        mTemplateLayout.setOnClickListener(this);
        mPreviewLayout.setOnClickListener(this);
        mTemplateFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.replaceImage:
                mReplaceImage.setClickable(false);
                Bundle pipBundle = new Bundle();
                pipBundle.putInt(Constants.SELECT_MEDIA_FROM, Constants.FROMPICINPICACTIVITYTOVISIT);
                pipBundle.putInt("picInPicVideoIndex",mClipVideoIndex);//picInPicVideoIndex表示待更改素材索引值
                AppManager.getInstance().jumpActivityForResult(PictureInPictureActivity.this, SingleClickActivity.class, pipBundle,PICINPICREPLACE_REQUESTCODE);
                break;
            case R.id.zoomInImage:
                double inScaleFactor = 1.25;//放大视频，放大因子是当前视频的1.25倍
                zoomInAndZoomOut(inScaleFactor);
                break;
            case R.id.zoomOutImage:
                double outScaleFactor = 0.8;//缩小视频，缩小因子是当前视频的0.8倍
                zoomInAndZoomOut(outScaleFactor);
                break;
            case R.id.rotateImage:
                if(mClipVideoIndex == 0){
                    if(mFirRotateAngle <= -360)
                        mFirRotateAngle = 0;
                    mFirRotateAngle += -90;
                    updateFirTrackTransform2DFx();
                }else {
                    if(mSecRotateAngle <= -360)
                        mSecRotateAngle = 0;
                    mSecRotateAngle += -90;
                    updateSecTrackTransform2DFx();
                }
                seekTimeline(0);
                break;
            case R.id.trimImage:
                mTrimImage.setClickable(false);
                Bundle bundle = new Bundle();
                bundle.putString("fromActivity","PictureInPictureActivity");
                bundle.putString("videoFilePath",mFilePathList.get(mClipVideoIndex));
                if(mClipVideoIndex == 0){
                    bundle.putLong("trimInPoint",mFirTrimInPoint);
                    bundle.putLong("trimOutPoint",mFirTrimOutPoint);
                }else{
                    bundle.putLong("trimInPoint",mSecTrimInPoint);
                    bundle.putLong("trimOutPoint",mSecTrimOutPoint);
                }
                AppManager.getInstance().jumpActivityForResult(PictureInPictureActivity.this, TrimActivity.class, bundle,PICINPICTRIMCLIP_REQUESTCODE);
                break;
            case R.id.upVideoVolumeButton:
                mUpClipIsMute = !mUpClipIsMute;
                if(mUpClipIsMute){
                    mVideoTrackFir.setVolumeGain(muteVolumeValue,muteVolumeValue);
                    mUpVideoVolumeButton.setImageResource(R.mipmap.pip_mute);
                }else {
                    mVideoTrackFir.setVolumeGain(defaultVolumeValue,defaultVolumeValue);
                    mUpVideoVolumeButton.setImageResource(R.mipmap.pip_volume);
                }
                break;
            case R.id.downVideoVolumeButton:
                mDownClipIsMute = !mDownClipIsMute;
                if(mDownClipIsMute){
                    mVideoTrackSec.setVolumeGain(muteVolumeValue,muteVolumeValue);
                    mDownVideoVolumeButton.setImageResource(R.mipmap.pip_mute);
                }else {
                    mVideoTrackSec.setVolumeGain(defaultVolumeValue,defaultVolumeValue);
                    mDownVideoVolumeButton.setImageResource(R.mipmap.pip_volume);
                }
                break;
            case R.id.templateLayout:
                mTemplateListLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.previewLayout:
                mPreviewLayout.setClickable(false);
                AppManager.getInstance().jumpActivityForResult(PictureInPictureActivity.this, PicturInPicturePreviewActivity.class, null,PICINPICPREVIEW_REQUESTCODE);
                break;
            case R.id.templateFinish:
                mTemplateListLayout.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeTimeline();
        AppManager.getInstance().finishActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case PICINPICPREVIEW_REQUESTCODE:
                mStreamingContext.connectTimelineWithLiveWindow(mTimeline,mLiveWindow);
                mCompileVideoFragment.initCompileCallBack();
                seekTimeline(0);
                break;
            case PICINPICREPLACE_REQUESTCODE:
                if(mClipVideoIndex == 0){
                    mVideoTrackFir.removeAllClips();
                    addVideoClip(mVideoTrackFir,mFilePathList.get(0),0,-1,null);
                    //获取裁剪点值
                    setTrimPointValue();
                    mVideoTrackFir.setVolumeGain(defaultVolumeValue,defaultVolumeValue);
                    mUpVideoVolumeButton.setImageResource(R.mipmap.pip_volume);
                }else {
                    mVideoTrackSec.removeAllClips();
                    addVideoClip(mVideoTrackSec,mFilePathList.get(1),0,-1,null);
                    //获取裁剪点值
                    setTrimPointValue();
                    mVideoTrackSec.setVolumeGain(defaultVolumeValue,defaultVolumeValue);
                    mDownVideoVolumeButton.setImageResource(R.mipmap.pip_volume);
                }
                updateTrackClip();
                break;
            case PICINPICTRIMCLIP_REQUESTCODE:
                if(mClipVideoIndex == 0){
                    mFirTrimInPoint = data.getLongExtra("trimInPoint",0);
                    mFirTrimOutPoint = data.getLongExtra("trimOutPoint",0);
                    changeVideoClipTrimPoint(mVideoTrackFir,mFirTrimInPoint,mFirTrimOutPoint);
                }else {
                    mSecTrimInPoint = data.getLongExtra("trimInPoint",0);
                    mSecTrimOutPoint = data.getLongExtra("trimOutPoint",0);
                    changeVideoClipTrimPoint(mVideoTrackSec,mSecTrimInPoint,mSecTrimOutPoint);
                }
                updateTrackClip();
                break;
        }
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

    }

    @Override
    protected void noPromptPermission() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!hasAllPermission()){
            AppManager.getInstance().finishActivity();
        }else {
            mReplaceImage.setClickable(true);
            mTrimImage.setClickable(true);
            mPreviewLayout.setClickable(true);
        }
    }

    private void transFormVideo(float transX, float transY){
        if(mClipVideoIndex == 0){
            mUpTransX += transX;
            mUpTransY += transY;
            updateFirTrackTransform2DFx();
        }else {
            mDownTransX += transX;
            mDownTransY += transY;
            updateSecTrackTransform2DFx();
        }
        seekTimeline(0);
    }
    private void resetVari(){
        mFirScaleValue = mSecScaleValue = 1.0D;
        mFirRotateAngle = mSecRotateAngle = 0;
        mUpClipIsMute = mDownClipIsMute = false;
        mUpTransX = mUpTransY = 0;
        mDownTransX = mDownTransY = 0;
    }
    private void resetTrackVolume(){
        mVideoTrackFir.setVolumeGain(defaultVolumeValue,defaultVolumeValue);
        mUpVideoVolumeButton.setImageResource(R.mipmap.pip_volume);
        mVideoTrackSec.setVolumeGain(defaultVolumeValue,defaultVolumeValue);
        mDownVideoVolumeButton.setImageResource(R.mipmap.pip_volume);
    }
    private void zoomInAndZoomOut(double scaleFactor){
        if(mClipVideoIndex == 0){
            mFirScaleValue = scaleFactor * mFirScaleValue;
            updateFirTrackTransform2DFx();
        }else {
            mSecScaleValue = scaleFactor * mSecScaleValue;
            updateSecTrackTransform2DFx();
        }
        seekTimeline(0);
    }
    private boolean createTimeline(){
        int makeRatio = NvAsset.AspectRatio_9v16;
        NvsVideoResolution videoEditRes = Util.getVideoEditResolution(makeRatio);
        //存储数据
        TimelineData.instance().setVideoResolution(videoEditRes);
        TimelineData.instance().setMakeRatio(makeRatio);
        //创建时间线
        mTimeline = TimelineUtil.newTimeline(videoEditRes);
        if(mTimeline == null){
            Logger.e(TAG, "PicInPic failed to create timeline");
            return false;
        }
        mVideoTrackFir = mTimeline.appendVideoTrack();
        mVideoTrackSec = mTimeline.appendVideoTrack();
        return true;
    }

    private void initDataVar(){
        mPiPTemplateAssetsArray = new ArrayList<>();
        mFilePathList = BackupData.instance().getPicInPicVideoArray();
    }

    private void initCompileVideoFragment() {
        mCompileVideoFragment = new CompileVideoFragment();
        mCompileVideoFragment.setTimeline(mTimeline);
        getFragmentManager().beginTransaction()
                .add(R.id.compilePage, mCompileVideoFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mCompileVideoFragment);
    }

    private void initTrackClip(){
        //添加视频片段
        if(mFilePathList == null || mFilePathList.size() == 0)
            return;

        addVideoClip(mVideoTrackFir, mFilePathList.get(0),
                0, -1,null);
        addVideoClip(mVideoTrackSec, mFilePathList.get(1),
                0, -1,null);

        //获取裁剪点值
        setTrimPointValue();
        //添加剩余视频片段
        addRemainVideoClip();
        applyVideoFxTemplate(mVideoFxTemplateIndex);
        mShapeRectF = getDrawRectFData(mVideoFxTemplateIndex,mClipVideoIndex);
        selectEditDrawRect(mShapeRectF);
        seekTimeline(0);
    }

    private void updateTrackClip(){
        int firClipCount = mVideoTrackFir.getClipCount();
        //移除轨道上多余的片段
        for (int i = firClipCount - 1; i > 0; --i) {
            mVideoTrackFir.removeClip(i,false);
        }
        int secClipCount = mVideoTrackSec.getClipCount();
        //移除轨道上多余的片段
        for (int i = secClipCount - 1; i > 0; --i) {
            mVideoTrackSec.removeClip(i,false);
        }

        //添加剩余视频片段
        addRemainVideoClip();

        applyVideoFxTemplate(mVideoFxTemplateIndex);
        mShapeRectF = getDrawRectFData(mVideoFxTemplateIndex,mClipVideoIndex);
        selectEditDrawRect(mShapeRectF);
        seekTimeline(0);
    }

    //计算把某一轨道剩余视频片段补齐
    private void addRemainVideoClip(){
        long firDuration = mVideoTrackFir.getDuration();
        long secDuration = mVideoTrackSec.getDuration();
        if(firDuration == 0 || secDuration == 0)
            return;//有一个轨道片段添加不成功
        long result = Math.abs(firDuration - secDuration);
        if(result == 0)
            return;//两个片段时长相同
        long minValue = Math.min(firDuration,secDuration);
        long needAddClipCount =  result / minValue;//需要添加的片段数量
        long remainedClipDuration = result % minValue;//需要添加的片段时长的一部分长度值
        if(firDuration > secDuration){
            NvsVideoClip secondTrackClip = mVideoTrackSec.getClipByIndex(0);
            if(secondTrackClip == null)
                return;
            String secondClipFilePath = secondTrackClip.getFilePath();
            NvsVideoFx transformFx = getTransform2DFx(secondTrackClip);
            while (needAddClipCount > 0){
                addVideoClip(mVideoTrackSec,secondClipFilePath,mSecTrimInPoint,mSecTrimOutPoint,transformFx);
                --needAddClipCount;
            }
            if(remainedClipDuration > 0){
                addVideoClip(mVideoTrackSec,secondClipFilePath,mSecTrimInPoint,mSecTrimInPoint + remainedClipDuration,transformFx);
            }
        }else {
            NvsVideoClip firstTrackClip = mVideoTrackFir.getClipByIndex(0);
            if(firstTrackClip == null)
                return;
            NvsVideoFx transformFx = getTransform2DFx(firstTrackClip);
            String firstClipFilePath = firstTrackClip.getFilePath();
            while (needAddClipCount > 0){
                addVideoClip(mVideoTrackFir,firstClipFilePath,mFirTrimInPoint,mFirTrimOutPoint,transformFx);
                --needAddClipCount;
            }
            if(remainedClipDuration > 0){
                addVideoClip(mVideoTrackFir,firstClipFilePath,mFirTrimInPoint,mFirTrimInPoint + remainedClipDuration,transformFx);
            }
        }
    }

    private void changeVideoClipTrimPoint(NvsVideoTrack videoTrack,long trimIn,long trimOut){
        if(videoTrack == null)
            return;
        NvsVideoClip videoClip = videoTrack.getClipByIndex(0);
        if(videoClip != null && trimIn >= 0 && trimOut > trimIn){
            videoClip.changeTrimInPoint(trimIn,true);
            videoClip.changeTrimOutPoint(trimOut,true);
        }
    }
    private NvsVideoFx getTransform2DFx(NvsVideoClip videoClip) {
        int fxCount = videoClip.getFxCount();
        for (int fxIndex = 0; fxIndex < fxCount; ++fxIndex) {
            NvsVideoFx videoFx = videoClip.getFxByIndex(fxIndex);
            String videoFxName = "Transform 2D";
            if (videoFx.getVideoFxType() == NvsVideoFx.VIDEOFX_TYPE_BUILTIN
                    && videoFx.getBuiltinVideoFxName().equals(videoFxName)) {
                return videoFx;
            }
        }
        return null;
    }
    private void setTrimPointValue(){
        NvsVideoClip firClip = mVideoTrackFir.getClipByIndex(0);
        if(firClip != null){
            mFirTrimInPoint = firClip.getTrimIn();
            mFirTrimOutPoint = firClip.getTrimOut();
        }
        NvsVideoClip secClip = mVideoTrackSec.getClipByIndex(0);
        if(secClip != null){
            mSecTrimInPoint = secClip.getTrimIn();
            mSecTrimOutPoint = secClip.getTrimOut();
        }
    }

    private void addVideoClip(NvsVideoTrack videoTrack,
                              String clipPath, long trimIn,
                              long trimOut,NvsVideoFx transformFx){
        NvsVideoClip videoClip;
        if(trimIn >= 0 && trimOut > 0 && trimOut > trimIn){
            videoClip = videoTrack.appendClip(clipPath,trimIn,trimOut);
        }else {
            videoClip = videoTrack.appendClip(clipPath);
        }
        if(videoClip == null){
            Logger.e(TAG, "PicInPic failed to append clip");
            return;
        }

        int videoType = getVideoType(clipPath);
        if(videoType == NvsAVFileInfo.AV_FILE_TYPE_IMAGE){
            videoClip.setImageMotionAnimationEnabled(false);
        }
        NvsVideoFx builtinFx = videoClip.appendBuiltinFx("Transform 2D");
        if(builtinFx != null && transformFx != null){
            double scaleXValue = transformFx.getFloatVal("Scale X");
            double scaleYValue = transformFx.getFloatVal("Scale Y");
            double rotateAngle = transformFx.getFloatVal("Rotation");
            double transX = transformFx.getFloatVal("Trans X");
            double transY = transformFx.getFloatVal("Trans Y");

            builtinFx.setFloatVal("Scale X",scaleXValue);
            builtinFx.setFloatVal("Scale Y",scaleYValue);
            //旋转
            builtinFx.setFloatVal("Rotation",rotateAngle);
            //片段偏移
            builtinFx.setFloatVal("Trans X",transX);
            builtinFx.setFloatVal("Trans Y",transY);
        }
    }
    private int getVideoType(String clipPath){
        NvsAVFileInfo avFileInfo = mStreamingContext.getAVFileInfo(clipPath);
        if(avFileInfo == null)
            return -1;
        return avFileInfo.getAVFileType();
    }


    private void parseJsonFileInfo(){
        String jsonFileName = "pipFileInfo.json";
        String jsonFilePath = "picinpic/" + jsonFileName;
        readPicInPicJsonFileInfo(jsonFilePath,true);
        String localJsonFilePath = PathUtils.getPicInPicDirPath();
        localJsonFilePath += "/";
        localJsonFilePath += jsonFileName;
        File file = new File(localJsonFilePath);
        if(file.exists()){
            readPicInPicJsonFileInfo(localJsonFilePath,false);
        }
    }

    private void readPicInPicJsonFileInfo(String jsonFilePath ,boolean isBundle){
        try {
            InputStream inputStream;
            if(isBundle){
                inputStream = this.getAssets().open(jsonFilePath);
            }else {
                inputStream = new FileInputStream(jsonFilePath);
            }
            if(inputStream == null)
                return;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String infoStrLine;
            StringBuilder strbuilder = new StringBuilder();
            while ((infoStrLine = bufferedReader.readLine()) != null) {
                strbuilder.append(infoStrLine);
            }
            bufferedReader.close();
            PicInPicTemplateAsset.PicInPicJsonFileInfo resultInfo = fromJson(strbuilder.toString(), PicInPicTemplateAsset.PicInPicJsonFileInfo.class);
            ArrayList<PicInPicTemplateAsset.PicInPicJsonFileInfoList> jsonFileInfoLists = resultInfo.getPipInfoList();
            int count = jsonFileInfoLists.size();
            for (int index = 0;index < count;++index){
                PicInPicTemplateAsset inPicTemplateAsset = new PicInPicTemplateAsset();
                PicInPicTemplateAsset.PicInPicJsonFileInfoList jsonFileInfo = jsonFileInfoLists.get(index);
                inPicTemplateAsset.setTemplateName(jsonFileInfo.getName());
                inPicTemplateAsset.setBundle(jsonFileInfo.isBundle());
                StringBuilder fileDirPath = new StringBuilder();
                if(isBundle){
                    inPicTemplateAsset.setTemplateCover("file:///android_asset/picinpic/" + jsonFileInfo.getCoverImageName());
                    fileDirPath.append("assets:/picinpic/");
                    fileDirPath.append(jsonFileInfo.getFileDirName());
                    fileDirPath.append("/");
                }else {
                    int lastIndexOf = jsonFilePath.lastIndexOf("/");
                    String jsonFileDirPath = jsonFilePath.substring(0,lastIndexOf + 1);
                    inPicTemplateAsset.setTemplateCover(jsonFileDirPath + jsonFileInfo.getCoverImageName());
                    fileDirPath.append(jsonFileDirPath);
                    fileDirPath.append(jsonFileInfo.getFileDirName());
                    fileDirPath.append("/");
                }
                ArrayList<String> packageIDList = getPackageIdList(fileDirPath.toString(),jsonFileInfo);
                if(packageIDList.size() == 2){
                    inPicTemplateAsset.setTempatePackageID(packageIDList);
                    mPiPTemplateAssetsArray.add(inPicTemplateAsset);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private ArrayList<String> getPackageIdList(String fileDir,PicInPicTemplateAsset.PicInPicJsonFileInfoList jsonFileInfo){
        ArrayList<String> videoFxPackageIDList = new ArrayList<>();
        StringBuilder packageId = new StringBuilder();

        String videoFxPackagePath = fileDir + jsonFileInfo.getPipPackageName1();
        boolean ret = installAssetPackage(videoFxPackagePath,packageId);
        if(ret){
            videoFxPackageIDList.add(packageId.toString());
        }
        videoFxPackagePath = fileDir + jsonFileInfo.getPipPackageName2();
        ret = installAssetPackage(videoFxPackagePath,packageId);
        if(ret){
            videoFxPackageIDList.add(packageId.toString());
        }
        return videoFxPackageIDList;
    }
    private boolean installAssetPackage(String videoFxPackagePath,StringBuilder packageId){
        int error = mStreamingContext.getAssetPackageManager().installAssetPackage(videoFxPackagePath, null, NvsAssetPackageManager.ASSET_PACKAGE_TYPE_VIDEOFX, true, packageId);
        if (error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR
                || error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
            if(error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED){
                mStreamingContext.getAssetPackageManager().upgradeAssetPackage(videoFxPackagePath, null, NvsAssetPackageManager.ASSET_PACKAGE_TYPE_VIDEOFX, true, packageId);
            }
            return true;
        }else {
            Logger.e(TAG,"PicInPic installAssetPackage Failed = " + packageId.toString());
        }
        return false;
    }
    private void initPicInPicTemplateRecycerViewList(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(PictureInPictureActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mPicInPicTemplateList.setLayoutManager(layoutManager);
        PictureInPictureTemplateAdapter inPictureTemplateAdapter = new PictureInPictureTemplateAdapter(PictureInPictureActivity.this);
        inPictureTemplateAdapter.setPicInPicDataList(mPiPTemplateAssetsArray);
        mPicInPicTemplateList.setAdapter(inPictureTemplateAdapter);
        mPicInPicTemplateList.addItemDecoration(new SpaceItemDecoration(14,14));
        inPictureTemplateAdapter.setOnItemClickListener(new PictureInPictureTemplateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                resetVari();
                updateFirTrackTransform2DFx();
                updateSecTrackTransform2DFx();
                resetTrackVolume();
                applyVideoFxTemplate(position);
                seekTimeline(0);
                mShapeRectF = getDrawRectFData(mVideoFxTemplateIndex,mClipVideoIndex);
                selectEditDrawRect(mShapeRectF);
            }
        });
    }

    private void applyVideoFxTemplate(int index){
        int firstClipCount = mVideoTrackFir.getClipCount();
        for (int firstIndex = 0;firstIndex < firstClipCount;++firstIndex){
            NvsVideoClip firstClip = mVideoTrackFir.getClipByIndex(firstIndex);
            removeVideoFx(firstClip,NvsVideoFx.VIDEOFX_TYPE_PACKAGE);
            NvsVideoFx secondVideoFx = firstClip.appendPackagedFx(mPiPTemplateAssetsArray.get(index).getTempatePackageID().get(0));
            if(secondVideoFx == null)
                Logger.e(TAG,"secondVideoFx append failed ");
        }

        int secClipCount = mVideoTrackSec.getClipCount();
        for (int secIndex = 0;secIndex < secClipCount;++secIndex){
            NvsVideoClip secondClip = mVideoTrackSec.getClipByIndex(secIndex);
            removeVideoFx(secondClip,NvsVideoFx.VIDEOFX_TYPE_PACKAGE);
            NvsVideoFx secondVideoFx = secondClip.appendPackagedFx(mPiPTemplateAssetsArray.get(index).getTempatePackageID().get(1));
            if(secondVideoFx == null)
                Logger.e(TAG,"secondVideoFx append failed ");
        }
        mVideoFxTemplateIndex = index;
    }

    private void removeVideoFx( NvsVideoClip videoClip,int videoFxType){
        if(videoClip == null)
            return;
        int fxCount = videoClip.getFxCount();
        for (int index = 0;index < fxCount;++index){
            NvsVideoFx videoFx = videoClip.getFxByIndex(index);
            if(videoFx.getVideoFxType() == videoFxType){
                videoClip.removeFx(index);
                break;
            }
        }
    }

    private void updateFirTrackTransform2DFx(){
        updateTransform2DFx(mVideoTrackFir,mFirScaleValue,mFirRotateAngle,mUpTransX,mUpTransY);
    }
    private void updateSecTrackTransform2DFx(){
        updateTransform2DFx(mVideoTrackSec,mSecScaleValue,mSecRotateAngle,mDownTransX,mDownTransY);
    }
    private void updateTransform2DFx(NvsVideoTrack videoTrack,double scaleValue,double rotateAngle,double transX,double transY){
        int clipCount = videoTrack.getClipCount();
        for (int clipIindex = 0;clipIindex < clipCount;++clipIindex){
            NvsVideoClip videoClip = videoTrack.getClipByIndex(clipIindex);
            int fxCount = videoClip.getFxCount();
            for (int fxIndex = 0;fxIndex < fxCount;++fxIndex) {
                NvsVideoFx videoFx = videoClip.getFxByIndex(fxIndex);
                String videoFxName = "Transform 2D";
                if(videoFx.getVideoFxType() == NvsVideoFx.VIDEOFX_TYPE_BUILTIN
                        && videoFx.getBuiltinVideoFxName().equals(videoFxName)){
                    //Logger.e(TAG,"scaleValue = " + scaleValue);
                    //缩放
                    videoFx.setFloatVal("Scale X",scaleValue);
                    videoFx.setFloatVal("Scale Y",scaleValue);
                    //旋转
                    videoFx.setFloatVal("Rotation",rotateAngle);
                    //片段偏移
                    videoFx.setFloatVal("Trans X",transX);
                    videoFx.setFloatVal("Trans Y",transY);
                    break;
                }
            }
        }
    }

    private void setLiveWindowRatio(int ratio) {
        RelativeLayout.LayoutParams liveWindowParams = (RelativeLayout.LayoutParams)mLiveWindow.getLayoutParams();
        int statusHeight = ScreenUtils.getStatusBarHeight(this);//状态栏高度
        int navBarHeight = ScreenUtils.getNavigationBarHeight(this);//获取虚拟按键的高度
        int screenWidth = ScreenUtils.getScreenWidth(this);//屏宽
        int screenHeight = ScreenUtils.getScreenHeight(this);//屏高
        int titleHeight = mTitleBar.getLayoutParams().height;
        int bottomHeight = mBottomLayout.getLayoutParams().height;
        int newHeight = screenHeight - titleHeight - bottomHeight - statusHeight - navBarHeight;
        switch (ratio) {
            case NvAsset.AspectRatio_16v9: // 16:9
                liveWindowParams.width = screenWidth;
                liveWindowParams.height = (int) (screenWidth * 9.0 / 16);
                break;
            case NvAsset.AspectRatio_1v1: //1:1
                liveWindowParams.width = screenWidth;
                liveWindowParams.height = screenWidth;
                if (newHeight < screenWidth) {
                    liveWindowParams.width = newHeight;
                    liveWindowParams.height = newHeight;
                }
                break;
            case NvAsset.AspectRatio_9v16: //9:16
                liveWindowParams.width = (int) (newHeight * 9.0 / 16);
                liveWindowParams.height = newHeight;
                break;
            case NvAsset.AspectRatio_3v4: // 3:4
                liveWindowParams.width = (int) (newHeight * 3.0 / 4);
                liveWindowParams.height = newHeight;
                break;
            case NvAsset.AspectRatio_4v3: //4:3
                liveWindowParams.width = screenWidth;
                liveWindowParams.height = (int) (screenWidth * 3.0 / 4);
                break;
            default: // 16:9
                liveWindowParams.width = screenWidth;
                liveWindowParams.height = (int) (screenWidth * 9.0 / 16);
                break;
        }
        mLiveWindow.setLayoutParams(liveWindowParams);
        mLiveWindow.setFillMode(NvsLiveWindow.FILLMODE_PRESERVEASPECTFIT);
        mStreamingContext.connectTimelineWithLiveWindow(mTimeline,mLiveWindow);
        mLiveWindowWidth = liveWindowParams.width;
        mLiveWidowHeight = liveWindowParams.height;
    }

    private void removeTimeline() {
        if(mStreamingContext != null){
            mStreamingContext.removeTimeline(mTimeline);
            mTimeline = null;
        }
    }

    private void seekTimeline(long timestamp){
        if(mStreamingContext != null){
            mStreamingContext.seekTimeline(mTimeline, timestamp, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, 0);
        }
    }


    private void selectEditDrawRect(RectF shapeRectF){
        mPIPDrawRect.setDrawRect(shapeRectF);
        updatePipToolBar(shapeRectF,mVideoFxTemplateIndex,mClipVideoIndex);
        int videoType = getVideoType(mFilePathList.get(mClipVideoIndex));
        if(videoType == NvsAVFileInfo.AV_FILE_TYPE_IMAGE){//片段是图片，不作裁剪
            mTrimImage.setVisibility(View.GONE);
        }else {
            mTrimImage.setVisibility(View.VISIBLE);
        }
        updateVolumeButton(mLiveWindowWidth,mLiveWidowHeight,mVideoFxTemplateIndex);
    }

    private void updatePipToolBar(RectF shapeRectF,int videoFxTemplateIndex,int videoIndex){
        RelativeLayout.LayoutParams toolBarParams = (RelativeLayout.LayoutParams)mPipToolBar.getLayoutParams();
        if(toolBarParams == null)
            return;
        int toolBarHeight = toolBarParams.height;
        toolBarParams.alignWithParent = true;
        switch (videoFxTemplateIndex) {
            case 0://上下
                toolBarParams.leftMargin = 0;
                toolBarParams.topMargin = videoIndex == 0 ? (int)shapeRectF.bottom : (int)shapeRectF.top - toolBarHeight;
                break;
            case 1://左右
                toolBarParams.leftMargin = videoIndex == 0 ? 0 : (int)shapeRectF.left;
                toolBarParams.topMargin = (int)shapeRectF.bottom - toolBarHeight;
                break;
            case 2://全屏左上
                toolBarParams.leftMargin = 0;
                toolBarParams.topMargin = videoIndex == 0 ? (int)shapeRectF.bottom - toolBarHeight : (int)shapeRectF.bottom;
                break;
            case 3://左上右下
                toolBarParams.leftMargin = videoIndex == 0 ? 0 : (int)shapeRectF.left;
                toolBarParams.topMargin = videoIndex == 0 ? (int)shapeRectF.bottom : (int)shapeRectF.top - toolBarHeight;
                break;
            default:
                toolBarParams.leftMargin = 0;
                toolBarParams.topMargin = videoIndex == 0 ? (int)shapeRectF.bottom : (int)shapeRectF.top - toolBarHeight;
                break;
        }
        mPipToolBar.setLayoutParams(toolBarParams);
    }
    private void updateVolumeButton(int liveWinowWidth,int liveWindowHeight,int videoFxTemplateIndex){
        RelativeLayout.LayoutParams upVolumeButtonParams = (RelativeLayout.LayoutParams)mUpVideoVolumeButton.getLayoutParams();
        if(upVolumeButtonParams == null)
            return;
        RelativeLayout.LayoutParams downVolumeButtonParams = (RelativeLayout.LayoutParams)mDownVideoVolumeButton.getLayoutParams();
        if(downVolumeButtonParams == null)
            return;
        int volumeLeftMargin = ScreenUtils.dip2px(PictureInPictureActivity.this,11);
        int volumeTopMargin = ScreenUtils.dip2px(PictureInPictureActivity.this,15);
        switch (videoFxTemplateIndex) {
            case 0://上下
                upVolumeButtonParams.leftMargin = volumeLeftMargin;
                upVolumeButtonParams.topMargin = volumeTopMargin;
                downVolumeButtonParams.leftMargin = volumeLeftMargin;
                downVolumeButtonParams.topMargin = liveWindowHeight / 2 + volumeTopMargin;
                break;
            case 1://左右
                upVolumeButtonParams.leftMargin = volumeLeftMargin;
                upVolumeButtonParams.topMargin = volumeTopMargin;
                downVolumeButtonParams.leftMargin = liveWinowWidth / 2 + volumeLeftMargin;
                downVolumeButtonParams.topMargin = volumeTopMargin;
                break;
            case 2://全屏左上
                upVolumeButtonParams.leftMargin = liveWinowWidth / 2 + volumeLeftMargin;
                upVolumeButtonParams.topMargin = volumeTopMargin;
                downVolumeButtonParams.leftMargin = volumeLeftMargin;
                downVolumeButtonParams.topMargin = volumeTopMargin;
                break;
            case 3://左上右下
                upVolumeButtonParams.leftMargin = volumeLeftMargin;
                upVolumeButtonParams.topMargin = volumeTopMargin;
                downVolumeButtonParams.leftMargin = liveWinowWidth / 2 + volumeLeftMargin;
                downVolumeButtonParams.topMargin = liveWindowHeight / 2 + volumeTopMargin;
                break;
            default:
                upVolumeButtonParams.leftMargin = volumeLeftMargin;
                upVolumeButtonParams.topMargin = volumeTopMargin;
                downVolumeButtonParams.leftMargin = volumeLeftMargin;
                downVolumeButtonParams.topMargin = liveWindowHeight / 2 + volumeTopMargin;
                break;
        }
        mUpVideoVolumeButton.setLayoutParams(upVolumeButtonParams);
        mDownVideoVolumeButton.setLayoutParams(downVolumeButtonParams);
    }

    private RectF getDrawRectFData(int videoFxTemplateIndex,int videoIndex){
        float left,right,top,bottom;
        int offset = 50;
        switch (videoFxTemplateIndex){
            case 0://上下
                left = 0;
                right = mLiveWindowWidth;
                top = videoIndex == 0 ? 0 : mLiveWidowHeight / 2;
                bottom = videoIndex == 0 ? mLiveWidowHeight / 2 : mLiveWidowHeight;
                break;
            case 1://左右
                left = videoIndex == 0 ? 0 : mLiveWindowWidth / 2;
                right = videoIndex == 0 ? mLiveWindowWidth / 2 : mLiveWindowWidth;
                top = 0;
                bottom = mLiveWidowHeight;
                break;
            case 2://全屏左上
                left = 0;
                right = videoIndex == 0 ? mLiveWindowWidth : mLiveWindowWidth / 3;
                top = 0;
                bottom = videoIndex == 0 ? mLiveWidowHeight : mLiveWidowHeight / 3;
                break;
            case 3://左上右下
                left = videoIndex == 0 ? 0 : mLiveWindowWidth / 2 - offset;
                right = videoIndex == 0 ? mLiveWindowWidth / 2 + offset : mLiveWindowWidth;
                top = videoIndex == 0 ? 0 : mLiveWidowHeight / 2;
                bottom = videoIndex == 0 ? mLiveWidowHeight / 2 : mLiveWidowHeight;
                break;
            default:
                left = 0;
                right = mLiveWindowWidth;
                top = videoIndex == 0 ? 0 : mLiveWidowHeight / 2;
                bottom = videoIndex == 0 ? mLiveWidowHeight / 2 : mLiveWidowHeight;
                break;
        }
        RectF rectF = new RectF();
        rectF.set(left,top,right,bottom);
        return rectF;
    }
}
