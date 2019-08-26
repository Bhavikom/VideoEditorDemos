package com.meishe.sdkdemo.makecover;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.download.AssetDownloadActivity;
import com.meishe.sdkdemo.edit.data.FilterItem;
import com.meishe.sdkdemo.edit.interfaces.OnTitleBarClickListener;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.AssetFxUtil;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.PathUtils;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.ToastUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;
import com.meishe.sdkdemo.utils.dataInfo.ClipInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;
import com.meishe.sdkdemo.utils.dataInfo.VideoClipFxInfo;
import com.meishe.sdkdemo.view.FilterView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ms on 2018/9/5 0030.
 */

public class MakeCoverActivity extends BaseActivity {

    private static final String TAG = "MakeCoverActivity";
    private static final int REQUEST_FILTER_LIST_CODE = 1020;
    private int mSelectedPos = 0;
    private float mIntensityValue = 1.0f;
    private ArrayList<FilterItem> mFilterItemArrayList;
    private int mAssetType = NvAsset.ASSET_FILTER;
    private NvAssetManager mAssetManager;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private VideoClipFxInfo mVideoClipFxInfo;

    private MakeCoverFragment mVideoFragment;
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private FilterView mFilterView;
    private Bitmap mCoverBitmap;
    private Button mResetImageBtn;

    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_make_cover;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mResetImageBtn = (Button) findViewById(R.id.reset_image_btn);
        mFilterView = (FilterView) findViewById(R.id.filterView);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.makingCover);
        mTitleBar.setTextRightVisible(View.VISIBLE);
        mTitleBar.setTextRight(R.string.compile);
    }

    @Override
    protected void initData() {
        if (!init()) {
            return;
        }
        initFilterDataList();
        initVideoFragment();
        initFilterView();
    }

    @Override
    protected void initListener() {
        mResetImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVideoFragment != null) {
                    mVideoFragment.resetAppearance();
                }
            }
        });
        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                removeTimeline();
                AppManager.getInstance().finishActivity();
            }

            @Override
            public void OnCenterTextClick() {
            }

            @Override
            public void OnRightTextClick() {
                takeImageBitmap(mStreamingContext.getTimelineCurrentPosition(mTimeline));
                compileCoverImage();
            }
        });

        mVideoFragment.setVideoFragmentCallBack(new MakeCoverFragment.VideoFragmentListener() {
            @Override
            public void playBackEOF(NvsTimeline timeline) {
            }

            @Override
            public void playStopped(NvsTimeline timeline) {
            }

            @Override
            public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {
            }

            @Override
            public void streamingEngineStateChanged(int i) {
            }

            @Override
            public void onFirstVideoFramePresented() {
            }

            @Override
            public void aVPlayStopedByButton() {
            }

            @Override
            public void aVPlayStartedByButton() {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case REQUEST_FILTER_LIST_CODE:
                initFilterDataList();
                mFilterView.setFilterArrayList(mFilterItemArrayList);
                mSelectedPos = AssetFxUtil.getSelectedFilterPos(mFilterItemArrayList, mVideoClipFxInfo);
                mFilterView.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        removeTimeline();
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void removeTimeline() {
        TimelineUtil.removeTimeline(mTimeline);
        mTimeline = null;
    }

    private boolean init() {
        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        if (videoClipArray == null || videoClipArray.size() == 0)
            return false;
        ClipInfo clipInfo = videoClipArray.get(0);
        mTimeline = TimelineUtil.createSingleClipTimeline(clipInfo, false);
        if (mTimeline == null)
            return false;
        NvsVideoClip clip = mTimeline.getVideoTrackByIndex(0).getClipByIndex(0);
        if (clip.getVideoType() == NvsVideoClip.VIDEO_CLIP_TYPE_IMAGE) { // 图片
            clip.changeTrimOutPoint(10 * Constants.NS_TIME_BASE, true);
        } else if (clip.getVideoType() == NvsVideoClip.VIDEO_CLIP_TYPE_AV) {// 视频
        }
        NvsVideoFx builtinFx = clip.appendBuiltinFx("Transform 2D");
        if (builtinFx == null)
            Logger.e(TAG, "Transform 2D append failed ");
        mFilterItemArrayList = new ArrayList<>();
        mVideoClipFxInfo = TimelineData.instance().getVideoClipFxData();
        if (mVideoClipFxInfo == null)
            mVideoClipFxInfo = new VideoClipFxInfo();
        mAssetManager = NvAssetManager.sharedInstance();
        mAssetManager.searchLocalAssets(mAssetType);
        String bundlePath = "filter";
        mAssetManager.searchReservedAssets(mAssetType, bundlePath);
        return true;
    }

    private void initFilterDataList() {
        mFilterItemArrayList = AssetFxUtil.getFilterData(this,
                getLocalData(),
                null,
                true,
                true);
    }

    private void initFilterView() {
        mFilterView.setFilterArrayList(mFilterItemArrayList);
        mFilterView.initFilterRecyclerView(this);
        mSelectedPos = AssetFxUtil.getSelectedFilterPos(mFilterItemArrayList, mVideoClipFxInfo);
        mFilterView.setSelectedPos(mSelectedPos);
        mFilterView.setIntensityLayoutVisible(mSelectedPos <= 0 ? View.INVISIBLE : View.VISIBLE);
        mFilterView.setIntensityTextVisible(View.VISIBLE);
        mFilterView.setIntensitySeekBarMaxValue(100);
        float intensity = mVideoClipFxInfo.getFxIntensity();
        mFilterView.setIntensitySeekBarProgress((int) (intensity * 100));
        mFilterView.setFilterFxListBackgroud("#00000000");
        mFilterView.setFilterListener(new FilterView.OnFilterListener() {
            @Override
            public void onItmeClick(View v, int position) {
                int count = mFilterItemArrayList.size();
                if (position < 0 || position >= count)
                    return;
                if (mSelectedPos == position)
                    return;
                if (position == 0) {
                    mFilterView.setIntensityLayoutVisible(View.INVISIBLE);
                    mVideoClipFxInfo.setFxMode(VideoClipFxInfo.FXMODE_BUILTIN);
                    mVideoClipFxInfo.setFxId(null);
                } else {
                    mFilterView.setIntensityLayoutVisible(View.VISIBLE);
                    FilterItem filterItem = mFilterItemArrayList.get(position);
                    int filterMode = filterItem.getFilterMode();
                    if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                        String filterName = filterItem.getFilterName();
                        mVideoClipFxInfo.setIsCartoon(filterItem.getIsCartoon());
                        mVideoClipFxInfo.setStrokenOnly(filterItem.getStrokenOnly());
                        mVideoClipFxInfo.setGrayScale(filterItem.getGrayScale());
                        mVideoClipFxInfo.setFxMode(VideoClipFxInfo.FXMODE_BUILTIN);
                        mVideoClipFxInfo.setFxId(filterName);
                    } else {
                        String packageId = filterItem.getPackageId();
                        mVideoClipFxInfo.setFxMode(VideoClipFxInfo.FXMODE_PACKAGE);
                        mVideoClipFxInfo.setFxId(packageId);
                    }
                    mFilterView.setIntensitySeekBarProgress(100);
                    mVideoClipFxInfo.setFxIntensity(1.0f);
                }
                mSelectedPos = position;
                TimelineUtil.buildTimelineFilter(mTimeline, mVideoClipFxInfo);
                mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), 0);
                mVideoFragment.playVideo(mStreamingContext.getTimelineCurrentPosition(mTimeline), 0);
            }

            @Override
            public void onMoreFilter() {
                Bundle bundle = new Bundle();
                bundle.putInt("titleResId", R.string.moreFilter);
                bundle.putInt("assetType", NvAsset.ASSET_FILTER);
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), AssetDownloadActivity.class, bundle, REQUEST_FILTER_LIST_CODE);
                mFilterView.setMoreFilterClickable(false);
            }

            @Override
            public void onIntensity(int value) {
                mIntensityValue = value / (float) 100;
                mVideoClipFxInfo.setFxIntensity(mIntensityValue);
                updateFxIntensity(mIntensityValue);
            }
        });

        mFilterView.setSeekBarTouchListener(new FilterView.OnSeekBarTouchListener() {
            @Override
            public void onStartTrackingTouch() {
            }

            @Override
            public void onStopTrackingTouch() {
            }
        });
    }

    private ArrayList<NvAsset> getLocalData() {
        return mAssetManager.getUsableAssets(mAssetType, NvAsset.AspectRatio_All, 0);
    }

    private void initVideoFragment() {
        mVideoFragment = new MakeCoverFragment();
        // 手动选择进度条结束后
        mVideoFragment.setSelectCoverListener(new MakeCoverFragment.OnSeekbarTraceStopedListener() {
            @Override
            public void onCoverPositionSelected() {
            }
        });

        mVideoFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight", mBottomLayout.getLayoutParams().height);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        bundle.putBoolean("playBarVisible", true);
        mVideoFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.video_layout, mVideoFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mVideoFragment);
    }

    private void updateFxIntensity(float value) {
        if (mTimeline == null)
            return;

        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        if (videoTrack == null)
            return;

        for (int i = 0; i < videoTrack.getClipCount(); i++) {
            NvsVideoClip videoClip = videoTrack.getClipByIndex(i);
            if (videoClip == null)
                continue;

            int fxCount = videoClip.getFxCount();
            for (int j = 0; j < fxCount; j++) {
                NvsVideoFx fx = videoClip.getFxByIndex(j);
                if (fx == null)
                    continue;

                String name = fx.getBuiltinVideoFxName();
                if (name == null) {
                    continue;
                }
                if (name.equals(Constants.FX_COLOR_PROPERTY) || name.equals(Constants.FX_VIGNETTE) ||
                        name.equals(Constants.FX_SHARPEN) || name.equals(Constants.FX_TRANSFORM_2D)) {
                    continue;
                }
                fx.setFilterIntensity(value);
            }
        }
        if (mVideoFragment.getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
            mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
        }
    }

    private void takeImageBitmap(long us) {
        if (mStreamingContext == null || mTimeline == null) {
            return;
        }
        NvsRational rational = new NvsRational(1, 1);
        if (mCoverBitmap != null && !mCoverBitmap.isRecycled()) {
            mCoverBitmap.recycle();
        }
        mCoverBitmap = mStreamingContext.grabImageFromTimeline(mTimeline, us, rational);
    }

    /**
     * 导出封面图片
     */
    private void compileCoverImage() {
        Bitmap bitmap = mCoverBitmap;
        if (bitmap == null) {
            ToastUtil.showToast(this, "保存失败！");
            return;
        }
        String cover_path = PathUtils.getCoverImagePath();
        if (cover_path == null || cover_path.isEmpty()) {
            return;
        }
        Log.e("===>", "cover_path: " + cover_path);
        boolean ret = Util.saveBitmapToSD(bitmap, cover_path);
        if (ret) {
            ToastUtil.showToastCenter(this.getApplicationContext(), "保存成功！\n保存路径：" + cover_path);
        } else {
            ToastUtil.showToast(this, "保存失败！");
            File file = new File(cover_path);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFilterView.setMoreFilterClickable(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCoverBitmap != null && !mCoverBitmap.isRecycled()) {
            mCoverBitmap.recycle();
            mCoverBitmap = null;
        }
        if (mTimeline != null) {
            mTimeline = null;
        }
    }
}
