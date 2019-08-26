package com.meishe.sdkdemo.edit.watermark;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseFragmentPagerAdapter;
import com.meishe.sdkdemo.edit.EditBaseActivity;
import com.meishe.sdkdemo.edit.VideoFragment;
import com.meishe.sdkdemo.edit.clipEdit.EditActivity;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.PathUtils;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.util.ArrayList;
import java.util.List;

import static com.meishe.sdkdemo.edit.watermark.WaterMarkConstant.ASSETS_WATERMARK_PICTURE_PATH;
import static com.meishe.sdkdemo.edit.watermark.WaterMarkConstant.DEFAULT_WATERMARK_PICTURE;
import static com.meishe.sdkdemo.edit.watermark.WaterMarkConstant.WATERMARKTYPE_DYNAMICS;
import static com.meishe.sdkdemo.edit.watermark.WaterMarkConstant.WATERMARKTYPE_STATIC;
import static com.meishe.sdkdemo.utils.MediaConstant.SINGLE_PICTURE_PATH;

public class WaterMarkActivity extends EditBaseActivity implements OnItemClickListener {
    public static final String WATER_CLEAN = "waterClean";
    private final String TAG = "WaterMarkActivity";
    private CustomTitleBar m_titleBar;
    private Point liveWindowPoint;
    private String mPicturePath;
    private int pictureType;
    private int waterMarkType;

    private String mWaterMarkPicture;
    private TabLayout tlSelectWater;
    private ViewPager vpSelectWater;
    private List<Fragment> fragmentLists = new ArrayList<>();
    private List<String> fragmentTabTitles = new ArrayList<>();
    private NvsTimelineVideoFx story;
    private float mTransX = 0;
    private float mTransY = 0;
    private float mScale = 1;
    private int mPictureW;
    private int mPictureH;
    private boolean backFromSelect = false;

    @Override
    protected int initRootView() {
        return R.layout.activity_water_mark;
    }

    @Override
    protected void initViews() {
        m_titleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        findViewById(R.id.water_btn_ok).setOnClickListener(this);
        tlSelectWater = (TabLayout) findViewById(R.id.tl_select_water);
        vpSelectWater = (ViewPager) findViewById(R.id.vp_select_water);
    }

    @Override
    protected void initTitle() {
        m_titleBar.setTextCenter(R.string.watermark);
        m_titleBar.setBackImageVisible(View.GONE);
    }

    @Override
    protected void initData() {
        initVideoFragment(R.id.water_videoLayout);

        String[] tabList = getResources().getStringArray(R.array.select_water);
        for (int i = 0; i < tabList.length; i++) {
            WaterMarkSelectFragment mediaFragment = new WaterMarkSelectFragment(this, this);
            Bundle bundle = new Bundle();
            bundle.putInt(WaterMarkConstant.WATERMARKTYPE_TYPE, WaterMarkConstant.WATERMARKTYPES[i]);
            mediaFragment.setArguments(bundle);
            fragmentLists.add(mediaFragment);
            fragmentTabTitles.add(tabList[i]);
        }
        //测试提交
        BaseFragmentPagerAdapter fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentLists, fragmentTabTitles);
        vpSelectWater.setAdapter(fragmentPagerAdapter);
        tlSelectWater.setupWithViewPager(vpSelectWater);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();

    }

    int playState;

    @Override
    protected void initListener() {
        videoFragment.setAssetEditListener(new VideoFragment.AssetEditListener() {
            @Override
            public void onAssetDelete() {
                clearActivityData();
                if (waterMarkType == WATERMARKTYPE_DYNAMICS) {
                    TimelineUtil.checkAndDeleteExitFX(nvsTimeline);
                }
                refreshLiveWindowFrame();
                videoFragment.setPicturePath(null);
                videoFragment.setDrawRectVisible(View.GONE);
            }

            @Override
            public void onAssetSelected(PointF curPoint) {

            }

            @Override
            public void onAssetTranstion() {

            }

            @Override
            public void onAssetScale() {

            }

            @Override
            public void onAssetAlign(int alignVal) {

            }

            @Override
            public void onAssetHorizFlip(boolean isHorizFlip) {

            }
        });
        videoFragment.setVideoFragmentCallBack(new VideoFragment.VideoFragmentListener() {
            @Override
            public void playBackEOF(NvsTimeline timeline) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void playStopped(NvsTimeline timeline) {
            }

            @Override
            public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {
            }

            @Override
            public void streamingEngineStateChanged(int state) {
                playState = state;
                if (waterMarkType == WaterMarkConstant.WATERMARKTYPE_DYNAMICS) {
                    if (state == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                        videoFragment.setDrawRectVisible(View.GONE);
                    } else {
                        videoFragment.setDrawRectVisible(View.VISIBLE);
                    }
                }
            }
        });
        videoFragment.setWaterMarkChangeListener(new VideoFragment.WaterMarkChangeListener() {
            @Override
            public void onDrag(List<PointF> newPointList) {
                if (story != null && waterMarkType == WATERMARKTYPE_DYNAMICS) {
                    float centerX = newPointList.get(3).x - (newPointList.get(3).x - newPointList.get(0).x) / 2;
                    float centerY = newPointList.get(1).y - (newPointList.get(1).y - newPointList.get(0).y) / 2;
                    int sceneWidth = videoFragment.getLiveWindowSize().x;
                    int sceneHeight = videoFragment.getLiveWindowSize().y;
                    float transX = centerX - sceneWidth / 2;
                    float transY = centerY - sceneHeight / 2;
                    story.setFloatVal("Sticker TransX", transX);
                    story.setFloatVal("Sticker TransY", -transY);
                    mTransX = transX;
                    mTransY = transY;
                    refreshLiveWindowFrame();
                }
            }

            @Override
            public void onScaleAndRotate(List<PointF> nowPointToRect) {
                if (story != null && waterMarkType == WATERMARKTYPE_DYNAMICS) {
                    int normalWidth = (int) Math.abs(nowPointToRect.get(0).x - nowPointToRect.get(3).x);
                    int normalHeight = (int) Math.abs(nowPointToRect.get(0).y - nowPointToRect.get(1).y);
                    float scale1 = (float) normalWidth / mPictureH;
                    float scale2 = (float) normalHeight / mPictureH;
                    float scale = (scale1 + scale2) / 2;
                    mScale = scale;
                    story.setFloatVal("Sticker Scale", scale);
                    refreshLiveWindowFrame();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            WaterMarkSelectFragment waterMarkSelectFragment = (WaterMarkSelectFragment) fragmentLists.get(0);
            Gson gson = new Gson();
            WaterMarkCacheData waterMarkCacheData = new WaterMarkCacheData(waterMarkSelectFragment.getCacheList());
            String str = gson.toJson(waterMarkCacheData);
            FileUtil.writeWaterMarkCacheFile(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !backFromSelect) {
            backFromSelect = false;
            liveWindowPoint = videoFragment.getLiveWindowSize();
            WaterMarkData waterMarkData = TimelineData.instance().getWaterMarkData();
            if (waterMarkData != null) {
                setActivityData(waterMarkData.getWaterMarkItemData().getItemWaterMarkType(), waterMarkData.getPicPath(), waterMarkData.getWaterMarkItemData().getWaterMarkpath());
                liveWindowPoint = waterMarkData.getPointOfLiveWindow();
                if (waterMarkType == WATERMARKTYPE_STATIC) {
                    setWaterMarkOnTheFirst(mWaterMarkPicture);
                } else if (waterMarkType == WATERMARKTYPE_DYNAMICS) {
                    mTransX = waterMarkData.getTransX();
                    mTransY = waterMarkData.getTransY();
                    mScale = waterMarkData.getScale();
                    setDynamicWaterMarkOnTheFirst(mPicturePath, mWaterMarkPicture, true);
                }
                videoFragment.setDrawRect(waterMarkData.getPointFInLiveWindow());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.water_btn_ok:
                if (mPicturePath != null) {
                    List<PointF> nowPointToRect = videoFragment.getDrawRect();
                    List<PointF> newPointToRect = viewPointToCanonical(nowPointToRect);
                    List<PointF> pointAtFirst = videoFragment.getPointFListToFirstAddWaterMark();
                    List<PointF> newPointAtFirst = viewPointToCanonical(pointAtFirst);
                    int excursionX = (int) (newPointAtFirst.get(3).x - newPointToRect.get(3).x);
                    int excursionY = (int) (newPointAtFirst.get(3).y - newPointToRect.get(3).y);
                    int picWidthOfCanonical = (int) Math.abs(newPointToRect.get(0).x - newPointToRect.get(3).x);
                    int picHeightOfCanonical = (int) Math.abs(newPointToRect.get(0).y - newPointToRect.get(1).y);
                    if (waterMarkType == WATERMARKTYPE_DYNAMICS) {
                        picWidthOfCanonical = (int) Math.abs(nowPointToRect.get(0).x - nowPointToRect.get(3).x);
                        picHeightOfCanonical = (int) Math.abs(nowPointToRect.get(0).y - nowPointToRect.get(1).y);
                    }

                    WaterMarkData waterMarkData = new WaterMarkData(nowPointToRect, excursionX, excursionY, picWidthOfCanonical,
                            picHeightOfCanonical, mPicturePath, liveWindowPoint, new WaterMarkItemData(pictureType, waterMarkType, mWaterMarkPicture, mPicturePath));
                    waterMarkData.setScale(mScale);
                    waterMarkData.setTransX(mTransX);
                    waterMarkData.setTransY(mTransY);
                    TimelineData.instance().setWaterMarkData(waterMarkData);

                    Intent intent = new Intent();
                    intent.putExtra(WATER_CLEAN, false);
                    setResult(RESULT_OK, intent);
                } else {
                    TimelineData.instance().cleanWaterMarkData();
                    Intent intent = new Intent();
                    intent.putExtra(WATER_CLEAN, true);
                    setResult(RESULT_OK, intent);
                }
                finishActivity();
                break;
            default:
                break;
        }
    }

    private List<PointF> viewPointToCanonical(List<PointF> nowPointToRect) {
        List<PointF> newPointFS = new ArrayList<>();
        for (PointF pointF : nowPointToRect) {
            PointF newPointToCanonical = videoFragment.getLiveWindow().mapViewToCanonical(pointF);
            newPointFS.add(newPointToCanonical);
        }
        return newPointFS;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            TimelineUtil.checkAndDeleteExitFX(nvsTimeline);
            setActivityData(WaterMarkConstant.WATERMARKTYPE_STATIC, data.getStringExtra(SINGLE_PICTURE_PATH), data.getStringExtra(SINGLE_PICTURE_PATH));
            setWaterMarkOnTheFirst(mWaterMarkPicture);
            refreshLiveWindowFrame();
            WaterMarkSelectFragment waterMarkSelectFragment = (WaterMarkSelectFragment) fragmentLists.get(0);
            waterMarkSelectFragment.addDataAndRefresh(mPicturePath);
        }
    }

    private void setWaterMarkOnTheFirst(String picturePath) {
        videoFragment.setEditMode(Constants.EDIT_MODE_WATERMARK);
        videoFragment.setPicturePath(picturePath);
        videoFragment.firstSetWaterMarkPosition(liveWindowPoint.x, liveWindowPoint.y, picturePath);
        videoFragment.setDrawRectVisible(View.VISIBLE);
    }

    private void setDynamicWaterMarkOnTheFirst(String mPicturePath, String waterMarkPicture, boolean isFromWindowFocusChanged) {
        setWaterMarkOnTheFirst(null);
        videoFragment.setDrawRectVisible(View.GONE);
        videoFragment.playVideo(0, -1);
        String dir = ASSETS_WATERMARK_PICTURE_PATH;
        if (mPicturePath.contains(DEFAULT_WATERMARK_PICTURE)) {
            dir = PathUtils.getWatermarkCafDirectoryDir();
        }
        int sceneWidth = videoFragment.getLiveWindowSize().x;
        int sceneHeight = videoFragment.getLiveWindowSize().y;

        List<PointF> nowPointToRect = videoFragment.getDrawRect();
        float normalX = nowPointToRect.get(0).x;
        float normalY = nowPointToRect.get(0).y;
        float transX = normalX - sceneWidth / 2 + mPictureW / 2;
        float transY = normalY - sceneHeight / 2 + mPictureH / 2;
        if (isFromWindowFocusChanged) {
            story = WaterMarkUtil.setDynamicWaterMark(nvsTimeline, sceneWidth, sceneHeight, waterMarkPicture, mPictureW, mPictureH, dir, mTransX, mTransY, mScale);
        } else {
            mTransX = transX;
            mTransY = transY;
            story = WaterMarkUtil.setDynamicWaterMark(nvsTimeline, sceneWidth, sceneHeight, waterMarkPicture, mPictureW, mPictureH, dir, transX, transY, 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(RecyclerView.ViewHolder holder, int position, int pictureType, String picturePath, int waterMarkType, String waterMarkPicture) {
        if (position == 0 && waterMarkType == WaterMarkConstant.WATERMARKTYPE_STATIC) {
            backFromSelect = true;
            AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), SingleClickActivity.class, new Bundle(), EditActivity.CLIPTRIM_REQUESTCODE);
        } else {
            TimelineUtil.checkAndDeleteExitFX(nvsTimeline);
            this.pictureType = pictureType;
            setActivityData(waterMarkType, picturePath, waterMarkPicture);
            if (waterMarkType == WaterMarkConstant.WATERMARKTYPE_STATIC) {
                setWaterMarkOnTheFirst(waterMarkPicture);
                WaterMarkSelectFragment selectFragment = (WaterMarkSelectFragment) fragmentLists.get(1);
                if (selectFragment != null && selectFragment.getNowPosition()!= -1) {
                    selectFragment.refreshNowPosition();
                }
            } else {
                WaterMarkSelectFragment selectFragment = (WaterMarkSelectFragment) fragmentLists.get(0);
                if (selectFragment != null&& selectFragment.getNowPosition()!= -1) {
                    selectFragment.refreshNowPosition();
                }
                setDynamicWaterMarkOnTheFirst(picturePath, waterMarkPicture, false);
            }
        }
        refreshLiveWindowFrame();
    }

    private void refreshLiveWindowFrame() {
        if (playState != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
            videoFragment.seekTimeline(nvsStreamingContext.getTimelineCurrentPosition(nvsTimeline), 0);
        }
    }

    private void clearActivityData() {
        mWaterMarkPicture = null;
        mPicturePath = null;
    }

    private void setActivityData(int type, String picturePath, String mWaterMarkPath) {
        mPicturePath = picturePath;
        mWaterMarkPicture = mWaterMarkPath;
        waterMarkType = type;
        Point point;
        if (waterMarkType == WaterMarkConstant.WATERMARKTYPE_STATIC) {
            point = videoFragment.getPictureSize(picturePath);
        } else {
            point = videoFragment.getPictureSize(mWaterMarkPath);
        }
        mPictureW = point.x;
        mPictureH = point.y;
    }
}
