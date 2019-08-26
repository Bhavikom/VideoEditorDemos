package com.meishe.sdkdemo.edit.animatesticker;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.download.AssetDownloadActivity;
import com.meishe.sdkdemo.edit.VideoFragment;
import com.meishe.sdkdemo.edit.timelineEditor.NvsTimelineEditor;
import com.meishe.sdkdemo.edit.timelineEditor.NvsTimelineTimeSpan;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.edit.view.CustomViewPager;
import com.meishe.sdkdemo.edit.watermark.SingleClickActivity;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.TimeFormatUtil;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;
import com.meishe.sdkdemo.utils.dataInfo.StickerInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AnimateStickerActivity extends BaseActivity {
    private static final String TAG = "AnimateStickerActivity";
    private static final int ANIMATESTICKERREQUESTLIST = 104;
    private static final int VIDEOPLAYTOEOF = 105;
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private RelativeLayout mZoomOutButton;
    private RelativeLayout mZoomInButton;
    private TextView mCurrentPlaytime;
    private ImageView mVideoPlay;
    private NvsTimelineEditor mTimelineEditor;
    private ImageView mAddAnimateStickerButton;
    private ImageView mStickerFinish;

    //素材列表变量
    private RelativeLayout mAnimateStickerAssetLayout;
    private ImageView mMoreDownload;
    private TabLayout mAnimateStickerTypeTab;
    private CustomViewPager mViewPager;
    private ImageView mStickerAssetFinish;

    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private VideoFragment mVideoFragment;
    private NvsMultiThumbnailSequenceView mMultiThumbnailSequenceView;
    private boolean mIsSeekTimeline = true;
    private AnimateStickerActivity.AnimateStickerHandler m_handler = new AnimateStickerActivity.AnimateStickerHandler(this);
    private List<AnimateStickerActivity.AnimateStickerTimeSpanInfo> mTimeSpanInfoList = new ArrayList<AnimateStickerActivity.AnimateStickerTimeSpanInfo>();
    private NvsTimelineAnimatedSticker mCurSelectAnimatedSticker;
    ArrayList<StickerInfo> mStickerDataListClone;

    //
    private ArrayList<String> mStickerAssetTypeList;
    private ArrayList<NvAsset> mTotalStickerAssetList;//总的贴纸列表
    private ArrayList<NvAssetManager.NvCustomStickerInfo> mCustomStickerAssetList;//自定义贴纸列表
    private ArrayList<AnimateStickerListFragment> mAssetFragmentsArray = new ArrayList<>();
    private AnimateStickerListFragment mStickerListFragment;
    private AnimateStickerListFragment mCustomStickerListFragment;
    private NvAssetManager mAssetManager;
    private int mAssetType = NvAsset.ASSET_ANIMATED_STICKER;
    private long mInPoint = 0;
    private long mStickerDuration = 0;
    private int mCurTabPage = 0;// 记录当前tab页
    private int mPrevTabPage = 0;//记录上次操作的Tab

    //新添加贴纸对象
    private NvsTimelineAnimatedSticker mAddAnimateSticker = null;
    private int mCurSelectedPos = -1;
    private boolean isNewStickerUuidItemClick = false;
    private String mSelectUuid = "";
    private int mCurAnimateStickerZVal = 0;
    private StringBuilder mShowCurrentDuration = new StringBuilder();

    static class AnimateStickerHandler extends Handler {
        WeakReference<AnimateStickerActivity> mWeakReference;

        public AnimateStickerHandler(AnimateStickerActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final AnimateStickerActivity activity = mWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case VIDEOPLAYTOEOF:
                        activity.resetView();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    //贴纸与timeSpan类，存储添加的贴纸跟TimeSpan
    private class AnimateStickerTimeSpanInfo {
        public NvsTimelineAnimatedSticker mAnimateSticker;
        public NvsTimelineTimeSpan mTimeSpan;

        public AnimateStickerTimeSpanInfo(NvsTimelineAnimatedSticker sticker, NvsTimelineTimeSpan timeSpan) {
            this.mAnimateSticker = sticker;
            this.mTimeSpan = timeSpan;
        }
    }

    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_animate_sticker;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mZoomOutButton = (RelativeLayout) findViewById(R.id.zoomOut);
        mZoomInButton = (RelativeLayout) findViewById(R.id.zoomIn);
        mCurrentPlaytime = (TextView) findViewById(R.id.currentPlaytime);
        mVideoPlay = (ImageView) findViewById(R.id.videoPlay);
        mTimelineEditor = (NvsTimelineEditor) findViewById(R.id.timelineEditor);
        mAddAnimateStickerButton = (ImageView) findViewById(R.id.addAnimateStickerButton);
        mStickerFinish = (ImageView) findViewById(R.id.stickerFinish);
        mMultiThumbnailSequenceView = mTimelineEditor.getMultiThumbnailSequenceView();

        //素材列表变量
        mAnimateStickerAssetLayout = (RelativeLayout) findViewById(R.id.animateStickerAsset_layout);
        mMoreDownload = (ImageView) findViewById(R.id.moreDownload);
        mAnimateStickerTypeTab = (TabLayout) findViewById(R.id.animateStickerTypeTab);
        mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
        mViewPager.setPagingEnabled(false);
        mStickerAssetFinish = (ImageView) findViewById(R.id.stickerAssetFinish);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.animatedSticker);
        mTitleBar.setBackImageVisible(View.GONE);
    }

    @Override
    protected void initData() {
        if (!initAssetData())
            return;
        setPlaytimeText(0);
        initMultiSequence();
        //添加所有贴纸
        addAllTimeSpan();
        initVideoFragment();

        //初始化素材列表
        initAnimateStickerDataList();
        initCustomAssetsDataList();
        initTabLayout();
        gifToCafStickerTemplateinstall();//gif转caf图需要这个贴纸模板
    }

    @Override
    protected void initListener() {
        mZoomOutButton.setOnClickListener(this);
        mZoomInButton.setOnClickListener(this);
        mVideoPlay.setOnClickListener(this);
        mAddAnimateStickerButton.setOnClickListener(this);
        mStickerFinish.setOnClickListener(this);
        mMoreDownload.setOnClickListener(this);
        mStickerAssetFinish.setOnClickListener(this);
        mTimelineEditor.setOnScrollListener(new NvsTimelineEditor.OnScrollChangeListener() {
            @Override
            public void onScrollX(long timeStamp) {
                if (!mIsSeekTimeline)
                    return;
                if (mTimeline != null) {
                    seekTimeline(mAnimateStickerAssetLayout.getVisibility() == View.VISIBLE ? mInPoint : timeStamp);
                    setPlaytimeText(timeStamp);
                    selectAnimateStickerAndTimeSpan();
                }
            }
        });
        mMultiThumbnailSequenceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mIsSeekTimeline = true;
                return false;
            }
        });

        mVideoFragment.setVideoFragmentCallBack(new VideoFragment.VideoFragmentListener() {
            @Override
            public void playBackEOF(NvsTimeline timeline) {
                m_handler.sendEmptyMessage(VIDEOPLAYTOEOF);
            }

            @Override
            public void playStopped(NvsTimeline timeline) {
                if (isNewStickerUuidItemClick)
                    return;
                selectAnimateStickerAndTimeSpan();
            }

            @Override
            public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {
                mVideoFragment.setDrawRectVisible(View.GONE);
                if (mAnimateStickerAssetLayout.getVisibility() != View.VISIBLE) {
                    setPlaytimeText(stamp);
                    mTimelineEditor.unSelectAllTimeSpan();
                    multiThumbnailSequenceViewSmooth(stamp);
                }
            }

            @Override
            public void streamingEngineStateChanged(int state) {
                if (state == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    mVideoPlay.setBackgroundResource(R.mipmap.icon_edit_pause);
                    mIsSeekTimeline = false;
                    mAssetFragmentsArray.get(mCurTabPage).setIsStickerInPlay(true);
                } else {
                    mVideoPlay.setBackgroundResource(R.mipmap.icon_edit_play);
                    mIsSeekTimeline = true;
                    int tapCount = mAssetFragmentsArray.size();
                    for (int index = 0; index < tapCount; ++index) {
                        mAssetFragmentsArray.get(index).setIsStickerInPlay(false);
                    }
                }
                if (mAnimateStickerAssetLayout.getVisibility() == View.VISIBLE) {
                    int tapCount = mAssetFragmentsArray.size();
                    for (int index = 0; index < tapCount; ++index) {
                        mAssetFragmentsArray.get(index).notifyDataSetChanged();
                    }
                }
            }
        });
        mVideoFragment.setAssetEditListener(new VideoFragment.AssetEditListener() {
            @Override
            public void onAssetDelete() {
                int zVal = (int) mCurSelectAnimatedSticker.getZValue();
                int stickerIndex = getAnimateStickerIndex(zVal);
                if (stickerIndex >= 0) {
                    mStickerDataListClone.remove(stickerIndex);
                }
                mAddAnimateSticker = null;
                deleteAnimateSticker();

                //取消所有Tab页贴纸选中的状态
                mCurSelectedPos = -1;
                int tabCount = mAssetFragmentsArray.size();
                for (int index = 0; index < tabCount; ++index) {
                    mAssetFragmentsArray.get(index).setSelectedPos(mCurSelectedPos);
                    mAssetFragmentsArray.get(index).notifyDataSetChanged();
                }
            }

            @Override
            public void onAssetSelected(PointF curPoint) {
                if (mAnimateStickerAssetLayout.getVisibility() == View.VISIBLE)
                    return;//贴纸素材列表显示不允许在liveWindow 来回切换选择贴纸，只选择当前添加的贴纸
                mVideoFragment.selectAnimateStickerByHandClick(curPoint);
                mCurSelectAnimatedSticker = mVideoFragment.getCurAnimateSticker();
                mVideoFragment.updateAnimateStickerCoordinate(mCurSelectAnimatedSticker);
                updateStickerMuteVisible();
                mVideoFragment.changeStickerRectVisible();
                selectTimeSpan();
            }

            @Override
            public void onAssetTranstion() {
                if (mCurSelectAnimatedSticker == null)
                    return;
                int zVal = (int) mCurSelectAnimatedSticker.getZValue();
                int index = getAnimateStickerIndex(zVal);
                if (index >= 0)
                    mStickerDataListClone.get(index).setTranslation(mCurSelectAnimatedSticker.getTranslation());
            }

            @Override
            public void onAssetScale() {
                if (mCurSelectAnimatedSticker == null)
                    return;
                int zVal = (int) mCurSelectAnimatedSticker.getZValue();
                int index = getAnimateStickerIndex(zVal);
                if (index >= 0) {
                    mStickerDataListClone.get(index).setTranslation(mCurSelectAnimatedSticker.getTranslation());
                    ;
                    mStickerDataListClone.get(index).setScaleFactor(mCurSelectAnimatedSticker.getScale());
                    mStickerDataListClone.get(index).setRotation(mCurSelectAnimatedSticker.getRotationZ());
                }
            }

            @Override
            public void onAssetAlign(int alignVal) {

            }

            @Override
            public void onAssetHorizFlip(boolean isHorizFlip) {
                if (mCurSelectAnimatedSticker == null)
                    return;
                int zVal = (int) mCurSelectAnimatedSticker.getZValue();
                int index = getAnimateStickerIndex(zVal);
                if (index >= 0) {
                    mStickerDataListClone.get(index).setHorizFlip(mCurSelectAnimatedSticker.getHorizontalFlip());
                }
            }
        });
        mVideoFragment.setLiveWindowClickListener(new VideoFragment.OnLiveWindowClickListener() {
            @Override
            public void onLiveWindowClick() {
                isNewStickerUuidItemClick = false;
            }
        });

        mVideoFragment.setStickerMuteListener(new VideoFragment.OnStickerMuteListener() {
            @Override
            public void onStickerMute() {
                if (mCurSelectAnimatedSticker == null)
                    return;
                float volumeGain = mCurSelectAnimatedSticker.getVolumeGain().leftVolume;
                int zVal = (int) mCurSelectAnimatedSticker.getZValue();
                int index = getAnimateStickerIndex(zVal);
                if (index >= 0) {
                    mStickerDataListClone.get(index).setVolumeGain(volumeGain);
                }
            }
        });

        mAnimateStickerAssetLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.zoomIn:
                mIsSeekTimeline = false;
                mTimelineEditor.ZoomInSequence();
                break;
            case R.id.zoomOut:
                mIsSeekTimeline = false;
                mTimelineEditor.ZoomOutSequence();
                break;
            case R.id.videoPlay:
                playVideo();
                break;
            case R.id.addAnimateStickerButton:
                mVideoFragment.stopEngine();
                mInPoint = mStreamingContext.getTimelineCurrentPosition(mTimeline);
                mStickerDuration = 4 * Constants.NS_TIME_BASE;
                long duration = mTimeline.getDuration();
                long outPoint = mInPoint + mStickerDuration;
                if (outPoint > duration) {
                    mStickerDuration = duration - mInPoint;
                    if (mStickerDuration <= Constants.NS_TIME_BASE) {
                        mStickerDuration = Constants.NS_TIME_BASE;
                        mInPoint = duration - mStickerDuration;
                        if (duration <= Constants.NS_TIME_BASE) {
                            mStickerDuration = duration;
                            mInPoint = 0;
                        }
                    }
                }
                if (mCurSelectAnimatedSticker != null)
                    mCurAnimateStickerZVal = (int) mCurSelectAnimatedSticker.getZValue();
                mAnimateStickerAssetLayout.setVisibility(View.VISIBLE);
                mVideoFragment.setDrawRectVisible(View.GONE);
                break;
            case R.id.stickerFinish:
                TimelineData.instance().setStickerData(mStickerDataListClone);
                mVideoFragment.stopEngine();
                removeTimeline();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                AppManager.getInstance().finishActivity();
                break;
            case R.id.moreDownload:
                mStreamingContext.stop();
                mMoreDownload.setClickable(false);
                Bundle bundle = new Bundle();
                bundle.putInt("titleResId", R.string.moreAnimatedSticker);
                bundle.putInt("assetType", NvAsset.ASSET_ANIMATED_STICKER);
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), AssetDownloadActivity.class, bundle, ANIMATESTICKERREQUESTLIST);
                break;
            case R.id.stickerAssetFinish:
                multiThumbnailSequenceViewSmooth(mInPoint);
                mAnimateStickerAssetLayout.setVisibility(View.GONE);
                seekTimeline(mInPoint);
                if (mAddAnimateSticker != null) {
                    selectAnimateStickerAndTimeSpan();
                } else {
                    selectAnimateStickerAndTimeSpanByZVal();
                }
                mAddAnimateSticker = null;//添加贴纸对象置空，否则再次进入贴纸列表会造成误删
                mCurAnimateStickerZVal = 0;
                isNewStickerUuidItemClick = false;
                mSelectUuid = "";
                //取消当前Tab页贴纸选中的状态
                mCurSelectedPos = -1;
                mAssetFragmentsArray.get(mCurTabPage).setSelectedPos(mCurSelectedPos);
                mAssetFragmentsArray.get(mCurTabPage).notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void playVideo() {
        if (mVideoFragment.getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
            long startTime = mStreamingContext.getTimelineCurrentPosition(mTimeline);
            long endTime = mTimeline.getDuration();
            mVideoFragment.playVideo(startTime, endTime);
        } else {
            mVideoFragment.stopEngine();
        }
    }

    @Override
    public void onBackPressed() {
        mVideoFragment.stopEngine();
        removeTimeline();
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void removeTimeline() {
        TimelineUtil.removeTimeline(mTimeline);
        mTimeline = null;
        m_handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case ANIMATESTICKERREQUESTLIST:
                initAnimateStickerDataList();
                mAssetFragmentsArray.get(0).setAssetInfolist(mTotalStickerAssetList);
                mCurSelectedPos = getSelectedPos();
                mAssetFragmentsArray.get(0).setSelectedPos(mCurSelectedPos);
                mAssetFragmentsArray.get(0).notifyDataSetChanged();
                updateStickerBoundingRect();
                break;
            default:
                break;
        }
    }

    private void multiThumbnailSequenceViewSmooth(long stamp) {
        if (mMultiThumbnailSequenceView != null) {
            int x = Math.round((stamp / (float) mTimeline.getDuration() * mTimelineEditor.getSequenceWidth()));
            mMultiThumbnailSequenceView.smoothScrollTo(x, 0);
        }
    }

    private void selectAnimateStickerAndTimeSpanByZVal() {
        selectAnimateStickerByZVal();
        updateStickerBoundingRect();
        if (mCurSelectAnimatedSticker != null) {
            selectTimeSpan();
        } else {
            mTimelineEditor.unSelectAllTimeSpan();
        }
    }

    private void selectAnimateStickerByZVal() {
        long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        List<NvsTimelineAnimatedSticker> animateStickerList = mTimeline.getAnimatedStickersByTimelinePosition(curPos);
        int stickerCount = animateStickerList.size();
        if (stickerCount > 0) {
            int index = -1;
            for (int i = 0; i < animateStickerList.size(); i++) {
                int tmpZVal = (int) animateStickerList.get(i).getZValue();
                if (tmpZVal == mCurAnimateStickerZVal) {
                    index = i;
                    break;
                }
            }
            mCurSelectAnimatedSticker = index >= 0 ? animateStickerList.get(index) : null;
        } else {
            mCurSelectAnimatedSticker = null;
        }
    }

    private void updateStickerBoundingRect() {
        mVideoFragment.setCurAnimateSticker(mCurSelectAnimatedSticker);
        mVideoFragment.updateAnimateStickerCoordinate(mCurSelectAnimatedSticker);
        updateStickerMuteVisible();
        if (mAddAnimateSticker == null
                && mAnimateStickerAssetLayout.getVisibility() == View.VISIBLE) {
            mVideoFragment.setDrawRectVisible(View.GONE);
        } else {
            mVideoFragment.changeStickerRectVisible();
        }
    }

    private int getSelectedPos() {
        int selectPos = -1;
        if (mSelectUuid.isEmpty())
            return selectPos;
        for (int index = 0; index < mTotalStickerAssetList.size(); ++index) {
            if (mTotalStickerAssetList.get(index).uuid.equals(mSelectUuid)) {
                selectPos = index;
                break;
            }
        }
        return selectPos;
    }

    private int getCustomStickerSelectedPos() {
        int selectPos = -1;
        if (mSelectUuid.isEmpty())
            return selectPos;
        for (int index = 0; index < mCustomStickerAssetList.size(); ++index) {
            if (mCustomStickerAssetList.get(index).uuid.equals(mSelectUuid)) {
                selectPos = index;
                break;
            }
        }
        return selectPos;
    }

    private boolean initAssetData() {
        mTimeline = TimelineUtil.createTimeline();
        if (mTimeline == null)
            return false;
        mStickerDataListClone = TimelineData.instance().cloneStickerData();
        mStickerAssetTypeList = new ArrayList<>();
        mTotalStickerAssetList = new ArrayList<>();
        mCustomStickerAssetList = new ArrayList<>();
        mAssetManager = NvAssetManager.sharedInstance();
        mAssetManager.searchLocalAssets(mAssetType);
        String bundlePath = "sticker";
        mAssetManager.searchReservedAssets(mAssetType, bundlePath);

        mAssetManager.searchLocalAssets(NvAsset.ASSET_CUSTOM_ANIMATED_STICKER);
        String bundlePath2 = "customsticker";
        mAssetManager.searchReservedAssets(NvAsset.ASSET_CUSTOM_ANIMATED_STICKER, bundlePath2);//查询自定义贴纸特效包
        mAssetManager.initCustomStickerInfoFromSharedPreferences();//查询自定义的贴纸
        return true;
    }

    private void initAnimateStickerDataList() {
        mTotalStickerAssetList.clear();
        ArrayList<NvAsset> userableAsset = getAssetsDataList();
        if (userableAsset != null && userableAsset.size() > 0) {
            for (NvAsset asset : userableAsset) {
                if (asset.isReserved()) {
                    String coverPath = "file:///android_asset/sticker/";
                    coverPath += asset.uuid;
                    coverPath += ".png";
                    asset.coverUrl = coverPath;//加载assets/sticker文件夹下的图片
                }
            }
            mTotalStickerAssetList = userableAsset;
        }
    }

    //获取下载到手机缓存路径下的素材，包括assets路径下自带的素材
    private ArrayList<NvAsset> getAssetsDataList() {
        return mAssetManager.getUsableAssets(mAssetType, NvAsset.AspectRatio_All, 0);
    }

    private void initCustomAssetsDataList() {
        //自定义贴纸
        mCustomStickerAssetList.clear();
        mCustomStickerAssetList = mAssetManager.getUsableCustomStickerAssets();
    }

    private void initTabLayout() {
        String[] tabList = getResources().getStringArray(R.array.animatedSticker_type);
        mStickerAssetTypeList.add(tabList[0]);
        mStickerAssetTypeList.add(tabList[1]);
        for (int index = 0; index < mStickerAssetTypeList.size(); index++) {
            mAnimateStickerTypeTab.addTab(mAnimateStickerTypeTab.newTab().setText(mStickerAssetTypeList.get(index)));
        }
        initAnimateStickerFragment();
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mAssetFragmentsArray.get(position);
            }

            @Override
            public int getCount() {
                return mAssetFragmentsArray.size();
            }
        });

        //mAnimateStickerTypeTab 添加tab切换的监听事件
        mAnimateStickerTypeTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //当前选中的tab的位置，切换到相应的fragment
                mCurTabPage = tab.getPosition();
                mViewPager.setCurrentItem(mCurTabPage);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void gifToCafStickerTemplateinstall() {
        String stickerTemplatePath = "assets:/E14FEE65-71A0-4717-9D66-3397B6C11223.5.animatedsticker";
        StringBuilder packageId = new StringBuilder();
        mStreamingContext.getAssetPackageManager().installAssetPackage(stickerTemplatePath, null, NvsAssetPackageManager.ASSET_PACKAGE_TYPE_ANIMATEDSTICKER, true, packageId);
    }

    private void initAnimateStickerFragment() {
        mStickerListFragment = new AnimateStickerListFragment();
        mStickerListFragment.setAnimateStickerClickerListener(new AnimateStickerListFragment.AnimateStickerClickerListener() {
            @Override
            public void onFragmentLoadFinish() {
                mStickerListFragment.setCustomStickerButtonVisible(View.GONE);
                mCustomStickerListFragment.setIsCutomStickerAsset(false);
                mStickerListFragment.setAssetInfolist(mTotalStickerAssetList);
            }

            @Override
            public void onItemClick(View view, int pos) {
                if (pos < 0 || pos >= mTotalStickerAssetList.size())
                    return;
                applyAnimateSticker(pos);
            }

            @Override
            public void onAddCustomSticker() {

            }
        });

        mAssetFragmentsArray.add(mStickerListFragment);
        mCustomStickerListFragment = new AnimateStickerListFragment();
        mCustomStickerListFragment.setAnimateStickerClickerListener(new AnimateStickerListFragment.AnimateStickerClickerListener() {
            @Override
            public void onFragmentLoadFinish() {
                mCustomStickerListFragment.setCustomStickerButtonVisible(View.VISIBLE);
                mCustomStickerListFragment.setIsCutomStickerAsset(true);
                mCustomStickerListFragment.setCustomStickerAssetInfolist(mCustomStickerAssetList);
            }

            @Override
            public void onItemClick(View view, int pos) {
                if (pos < 0 || pos >= mCustomStickerAssetList.size())
                    return;
                applyCustomAnimateSticker(pos);
            }

            @Override
            public void onAddCustomSticker() {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.SELECT_MEDIA_FROM, Constants.SELECT_IMAGE_FROM_CUSTOM_STICKER);
                AppManager.getInstance().jumpActivity(AppManager.getInstance().currentActivity(), SingleClickActivity.class, bundle);
            }
        });

        mAssetFragmentsArray.add(mCustomStickerListFragment);
    }

    private void applyAnimateSticker(int pos) {
        if (mAddAnimateSticker != null
                && mPrevTabPage == mCurTabPage
                && mCurSelectedPos == pos) {
            isNewStickerUuidItemClick = false;
            if (mVideoFragment.getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                long endTime = mInPoint + mStickerDuration;
                mVideoFragment.playVideo(mInPoint, endTime);
                mVideoFragment.setDrawRectVisible(View.GONE);
            } else {
                mVideoFragment.stopEngine();
            }
            return;
        }

        removeAddAniamteSticker();
        //添加贴纸
        float zStickerVal = getCurAnimateStickerZVal();
        mAddAnimateSticker = mTimeline.addAnimatedSticker(mInPoint, mStickerDuration,
                mTotalStickerAssetList.get(pos).uuid);
        if (mAddAnimateSticker == null)
            return;
        mAddAnimateSticker.setZValue(zStickerVal);
        //取消其他页贴纸选中
        mCurSelectedPos = pos;
        mSelectUuid = mTotalStickerAssetList.get(pos).uuid;
        addTimeSpanAndPlayVideo(false, "");
    }

    private void applyCustomAnimateSticker(final int pos) {
        if (mAddAnimateSticker != null
                && mPrevTabPage == mCurTabPage
                && mCurSelectedPos == pos) {
            isNewStickerUuidItemClick = false;
            if (mVideoFragment.getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                long endTime = mInPoint + mStickerDuration;
                mVideoFragment.playVideo(mInPoint, endTime);
                mVideoFragment.setDrawRectVisible(View.GONE);
            } else {
                mVideoFragment.stopEngine();
            }
            return;
        }

        removeAddAniamteSticker();

        //添加自定义贴纸
        String imageSrcFilePath = mCustomStickerAssetList.get(pos).imagePath;
        int lastPointPos = imageSrcFilePath.lastIndexOf(".");
        String fileSuffixName = imageSrcFilePath.substring(lastPointPos).toLowerCase();
        if (fileSuffixName.equals(".gif")) {//gif
            String targetCafPath = mCustomStickerAssetList.get(pos).targetImagePath;
            File targetCafFile = new File(targetCafPath);
            if (targetCafFile.exists()) {//检测目标caf文件是否存在
                addCustomAnimateSticker(pos, targetCafPath);
            }
        } else {//image
            addCustomAnimateSticker(pos, mCustomStickerAssetList.get(pos).imagePath);
        }
    }

    private void addCustomAnimateSticker(int pos, String imageFilePath) {
        float zStickerVal = getCurAnimateStickerZVal();
        mAddAnimateSticker = mTimeline.addCustomAnimatedSticker(mInPoint, mStickerDuration,
                mCustomStickerAssetList.get(pos).templateUuid, imageFilePath);
        if (mAddAnimateSticker == null)
            return;

        mAddAnimateSticker.setZValue(zStickerVal);
        mCurSelectedPos = pos;
        mSelectUuid = mCustomStickerAssetList.get(pos).uuid;
        addTimeSpanAndPlayVideo(true, imageFilePath);
    }

    private void removeAddAniamteSticker() {
        if (mAddAnimateSticker != null) {
            int zVal = (int) mAddAnimateSticker.getZValue();
            int index = getAnimateStickerIndex(zVal);
            if (index >= 0)
                mStickerDataListClone.remove(index);
            deleteCurStickerTimeSpan(mAddAnimateSticker);
            mTimeline.removeAnimatedSticker(mAddAnimateSticker);
            mAddAnimateSticker = null;
            mVideoFragment.setCurAnimateSticker(mAddAnimateSticker);
            mVideoFragment.changeStickerRectVisible();
        }
    }

    private StickerInfo saveStickerInfo() {
        StickerInfo stickerInfo = new StickerInfo();
        stickerInfo.setInPoint(mAddAnimateSticker.getInPoint());
        stickerInfo.setOutPoint(mAddAnimateSticker.getOutPoint());
        stickerInfo.setHorizFlip(mAddAnimateSticker.getHorizontalFlip());
        stickerInfo.setTranslation(mAddAnimateSticker.getTranslation());
        String packagedId = mAddAnimateSticker.getAnimatedStickerPackageId();
        stickerInfo.setId(packagedId);
        int zVal = (int) mAddAnimateSticker.getZValue();
        stickerInfo.setAnimateStickerZVal(zVal);
        return stickerInfo;
    }

    private void addTimeSpanAndPlayVideo(boolean isCustomSticker, String imageFilePath) {
        if (mAddAnimateSticker == null)
            return;
        if (mPrevTabPage != mCurTabPage) {
            mAssetFragmentsArray.get(mPrevTabPage).setSelectedPos(-1);
            mAssetFragmentsArray.get(mPrevTabPage).notifyDataSetChanged();
        }
        isNewStickerUuidItemClick = true;
        mPrevTabPage = mCurTabPage;
        long endTime = mInPoint + mStickerDuration;
        //添加timeSpan
        NvsTimelineTimeSpan timeSpan = addTimeSpan(mInPoint, endTime);
        if (timeSpan != null) {
            AnimateStickerActivity.AnimateStickerTimeSpanInfo timeSpanInfo = new AnimateStickerActivity.AnimateStickerTimeSpanInfo(mAddAnimateSticker, timeSpan);
            mTimeSpanInfoList.add(timeSpanInfo);
        }

        //保存数据
        StickerInfo stickerInfo = saveStickerInfo();
        stickerInfo.setCustomSticker(isCustomSticker);
        stickerInfo.setCustomImagePath(imageFilePath);
        mStickerDataListClone.add(stickerInfo);
        mVideoFragment.setDrawRectVisible(View.GONE);
        //播放视频
        mVideoFragment.playVideo(mInPoint, endTime);
    }

    private float getCurAnimateStickerZVal() {
        float zVal = 0.0f;
        NvsTimelineAnimatedSticker animatedSticker = mTimeline.getFirstAnimatedSticker();
        while (animatedSticker != null) {
            float tmpZVal = animatedSticker.getZValue();
            if (tmpZVal > zVal)
                zVal = tmpZVal;
            animatedSticker = mTimeline.getNextAnimatedSticker(animatedSticker);
        }
        zVal += 1.0;
        return zVal;
    }

    private void selectAnimateStickerAndTimeSpan() {
        selectAnimateSticker();
        updateStickerBoundingRect();
        if (mCurSelectAnimatedSticker != null) {
            selectTimeSpan();
        } else {
            mTimelineEditor.unSelectAllTimeSpan();
        }
    }

    private void deleteAnimateSticker() {
        deleteCurStickerTimeSpan(mCurSelectAnimatedSticker);
        mTimeline.removeAnimatedSticker(mCurSelectAnimatedSticker);
        mCurSelectAnimatedSticker = null;
        selectAnimateStickerAndTimeSpan();
        seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
    }

    private void deleteCurStickerTimeSpan(NvsTimelineAnimatedSticker animateSticker) {
        for (int i = 0; i < mTimeSpanInfoList.size(); i++) {
            if (animateSticker != null
                    && mTimeSpanInfoList.get(i).mAnimateSticker == animateSticker) {
                mTimelineEditor.deleteSelectedTimeSpan(mTimeSpanInfoList.get(i).mTimeSpan);
                mTimeSpanInfoList.remove(i);
                break;
            }
        }
    }

    private NvsTimelineTimeSpan addTimeSpan(long inPoint, long outPoint) {
        //warning: 使用addTimeSpanExt之前必须设置setTimeSpanType()
        mTimelineEditor.setTimeSpanType("NvsTimelineTimeSpan");
        final NvsTimelineTimeSpan timelineTimeSpan = mTimelineEditor.addTimeSpan(inPoint, outPoint);
        if (timelineTimeSpan == null) {
            Log.e(TAG, "addTimeSpan: " + " 添加TimeSpan失败!");
            return null;
        }
        timelineTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimInChangeListener() {
            @Override
            public void onChange(long timeStamp, boolean isDragEnd) {
                seekTimeline(timeStamp);
                mVideoFragment.changeStickerRectVisible();
                setPlaytimeText(timeStamp);
                if (isDragEnd && mCurSelectAnimatedSticker != null) {
                    mCurSelectAnimatedSticker.changeInPoint(timeStamp);
                    int zVal = (int) mCurSelectAnimatedSticker.getZValue();
                    int index = getAnimateStickerIndex(zVal);
                    if (index >= 0) {
                        mStickerDataListClone.get(index).setInPoint(mCurSelectAnimatedSticker.getInPoint());
                    }

                    seekMultiThumbnailSequenceView();
                }
            }
        });
        timelineTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimOutChangeListener() {
            @Override
            public void onChange(long timeStamp, boolean isDragEnd) {
                //outPoint是开区间，seekTimeline时，需要往前平移一帧即0.04秒，转换成微秒即40000微秒
                seekTimeline(timeStamp - 40000);
                setPlaytimeText(timeStamp);
                mVideoFragment.changeStickerRectVisible();
                if (isDragEnd && mCurSelectAnimatedSticker != null) {
                    mCurSelectAnimatedSticker.changeOutPoint(timeStamp);
                    int zVal = (int) mCurSelectAnimatedSticker.getZValue();
                    int index = getAnimateStickerIndex(zVal);
                    if (index >= 0) {
                        mStickerDataListClone.get(index).setOutPoint(timeStamp);
                    }

                    seekMultiThumbnailSequenceView();
                }
            }
        });

        return timelineTimeSpan;
    }

    private void seekMultiThumbnailSequenceView() {
        if (mMultiThumbnailSequenceView != null) {
            long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
            long duration = mTimeline.getDuration();
            mMultiThumbnailSequenceView.scrollTo(Math.round(((float) curPos) / (float) duration * mTimelineEditor.getSequenceWidth()), 0);
        }
    }

    private void seekTimeline(long timestamp) {
        mVideoFragment.seekTimeline(timestamp, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
    }

    private void addAllTimeSpan() {
        NvsTimelineAnimatedSticker animatedSticker = mTimeline.getFirstAnimatedSticker();
        while (animatedSticker != null) {
            long inPoint = animatedSticker.getInPoint();
            long outPoint = animatedSticker.getOutPoint();
            NvsTimelineTimeSpan timeSpan = addTimeSpan(inPoint, outPoint);
            if (timeSpan != null) {
                AnimateStickerActivity.AnimateStickerTimeSpanInfo timeSpanInfo = new AnimateStickerActivity.AnimateStickerTimeSpanInfo(animatedSticker, timeSpan);
                mTimeSpanInfoList.add(timeSpanInfo);
            }
            animatedSticker = mTimeline.getNextAnimatedSticker(animatedSticker);
        }
    }

    private void selectAnimateSticker() {
        long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        List<NvsTimelineAnimatedSticker> animateStickerList = mTimeline.getAnimatedStickersByTimelinePosition(curPos);
        Logger.e(TAG, "animateStickerListCount-->" + animateStickerList.size());
        int stickerCount = animateStickerList.size();
        if (stickerCount > 0) {
            float zVal = animateStickerList.get(0).getZValue();
            int index = 0;
            for (int i = 0; i < animateStickerList.size(); i++) {
                float tmpZVal = animateStickerList.get(i).getZValue();
                if (tmpZVal > zVal) {
                    zVal = tmpZVal;
                    index = i;
                }
            }
            mCurSelectAnimatedSticker = animateStickerList.get(index);
        } else {
            mCurSelectAnimatedSticker = null;
        }
    }

    private void selectTimeSpan() {
        for (int i = 0; i < mTimeSpanInfoList.size(); i++) {
            if (mTimeSpanInfoList.get(i).mAnimateSticker == mCurSelectAnimatedSticker) {
                mTimelineEditor.selectTimeSpan(mTimeSpanInfoList.get(i).mTimeSpan);
                break;
            }
        }
    }

    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mVideoFragment.setCurAnimateSticker(mCurSelectAnimatedSticker);
                mStickerFinish.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
                        selectAnimateStickerAndTimeSpan();
                    }
                }, 100);
            }
        });
        mVideoFragment.setTimeline(mTimeline);
        //设置贴纸模式
        mVideoFragment.setEditMode(Constants.EDIT_MODE_STICKER);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight", mBottomLayout.getLayoutParams().height);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        bundle.putBoolean("playBarVisible", false);
        mVideoFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.spaceLayout, mVideoFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mVideoFragment);
    }

    private void updateStickerMuteVisible() {
        if (mCurSelectAnimatedSticker != null) {
            boolean hasAudio = mCurSelectAnimatedSticker.hasAudio();
            mVideoFragment.setMuteVisible(hasAudio);
            if (hasAudio) {
                float leftVolume = (int) mCurSelectAnimatedSticker.getVolumeGain().leftVolume;
                mVideoFragment.setStickerMuteIndex(leftVolume > 0 ? 0 : 1);
            }
        }
    }

    private void initMultiSequence() {
        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        if (videoTrack == null)
            return;
        int clipCount = videoTrack.getClipCount();
        ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> sequenceDescsArray = new ArrayList<>();
        for (int index = 0; index < clipCount; ++index) {
            NvsVideoClip videoClip = videoTrack.getClipByIndex(index);
            if (videoClip == null)
                continue;

            NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc sequenceDescs = new NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
            sequenceDescs.mediaFilePath = videoClip.getFilePath();
            sequenceDescs.trimIn = videoClip.getTrimIn();
            sequenceDescs.trimOut = videoClip.getTrimOut();
            sequenceDescs.inPoint = videoClip.getInPoint();
            sequenceDescs.outPoint = videoClip.getOutPoint();
            sequenceDescs.stillImageHint = false;
            sequenceDescsArray.add(sequenceDescs);
        }
        long duration = mTimeline.getDuration();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mVideoPlay.getLayoutParams();
        int playBtnTotalWidth = layoutParams.width + layoutParams.leftMargin + layoutParams.rightMargin;
        int halfScreenWidth = ScreenUtils.getScreenWidth(this) / 2;
        int sequenceLeftPadding = halfScreenWidth - playBtnTotalWidth;
        mTimelineEditor.setSequencLeftPadding(sequenceLeftPadding);
        mTimelineEditor.setSequencRightPadding(halfScreenWidth);
        mTimelineEditor.setTimeSpanLeftPadding(sequenceLeftPadding);
        mTimelineEditor.initTimelineEditor(sequenceDescsArray, duration);
    }

    private void setPlaytimeText(long playTime) {
        long totalDuaration = mTimeline.getDuration();
        String strTotalDuration = TimeFormatUtil.formatUsToString1(totalDuaration);
        String strCurrentDuration = TimeFormatUtil.formatUsToString1(playTime);
        mShowCurrentDuration.setLength(0);
        mShowCurrentDuration.append(strCurrentDuration);
        mShowCurrentDuration.append("/");
        mShowCurrentDuration.append(strTotalDuration);
        mCurrentPlaytime.setText(mShowCurrentDuration.toString());
    }

    private void resetView() {
        setPlaytimeText(0);
        mVideoPlay.setBackgroundResource(R.mipmap.icon_edit_play);
        mMultiThumbnailSequenceView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        seekTimeline(mAnimateStickerAssetLayout.getVisibility() == View.VISIBLE ? mInPoint : 0);
        selectAnimateStickerAndTimeSpan();
    }

    private int getAnimateStickerIndex(int curZValue) {
        int index = -1;
        int count = mStickerDataListClone.size();
        for (int i = 0; i < count; ++i) {
            int zVal = mStickerDataListClone.get(i).getAnimateStickerZVal();
            if (curZValue == zVal) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMoreDownload.setClickable(true);
        initCustomAssetsDataList();
        mAssetFragmentsArray.get(1).setCustomStickerAssetInfolist(mCustomStickerAssetList);
        mCurSelectedPos = getCustomStickerSelectedPos();
        mAssetFragmentsArray.get(1).setSelectedPos(mCurSelectedPos);
        mAssetFragmentsArray.get(1).notifyDataSetChanged();
        updateStickerBoundingRect();
    }
}
