package com.meishe.sdkdemo.edit.filter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.download.AssetDownloadActivity;
import com.meishe.sdkdemo.edit.VideoFragment;
import com.meishe.sdkdemo.edit.data.FilterItem;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.AssetFxUtil;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;
import com.meishe.sdkdemo.utils.dataInfo.CaptionInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;
import com.meishe.sdkdemo.utils.dataInfo.VideoClipFxInfo;
import com.meishe.sdkdemo.view.FilterView;

import java.util.ArrayList;

/**
 * Created by yyj on 2018/5/30 0030.
 */

public class FilterActivity extends BaseActivity{
    private static final String TAG = "FilterActivity";
    private static final int REQUEST_FILTER_LIST_CODE = 102;
    private VideoFragment mVideoFragment;
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;

    private FilterView mFilterView;
    private ImageView mFilterAssetFinish;

    private ArrayList<FilterItem> mFilterItemArrayList;
    private int mAssetType = NvAsset.ASSET_FILTER;
    private NvAssetManager mAssetManager;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private VideoClipFxInfo mVideoClipFxInfo;
    private int mSelectedPos = 0;

    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_filter;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mFilterView = (FilterView)findViewById(R.id.filterView);
        mFilterAssetFinish = (ImageView)findViewById(R.id.filterAssetFinish);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.filter);
        mTitleBar.setBackImageVisible(View.GONE);
    }

    @Override
    protected void initData() {
        init();
        initFilterDataList();
        initVideoFragment();
        initFilterView();
    }

    @Override
    protected void initListener() {
        mFilterAssetFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.filterAssetFinish:
                TimelineData.instance().setVideoClipFxData(mVideoClipFxInfo);//保存数据
                removeTimeline();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                quitActivity();
                break;
        }
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
                mSelectedPos = AssetFxUtil.getSelectedFilterPos(mFilterItemArrayList,mVideoClipFxInfo);
                mFilterView.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        removeTimeline();
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void removeTimeline(){
        TimelineUtil.removeTimeline(mTimeline);
        mTimeline = null;
    }

    private void quitActivity(){
        AppManager.getInstance().finishActivity();
    }

    private void init(){
        mTimeline = TimelineUtil.createTimeline();
        if(mTimeline == null)
            return;

        TimelineUtil.applyTheme(mTimeline, null);
        //移除主题，则需要删除字幕，然后重新添加，防止带片头主题删掉字幕
        ArrayList<CaptionInfo> captionArray = TimelineData.instance().getCaptionData();
        TimelineUtil.setCaption(mTimeline,captionArray);

        mFilterItemArrayList = new ArrayList<>();
        mVideoClipFxInfo = TimelineData.instance().getVideoClipFxData();
        if(mVideoClipFxInfo == null)
            mVideoClipFxInfo = new VideoClipFxInfo();
        mAssetManager = getNvAssetManager();
        mAssetManager.searchLocalAssets(mAssetType);
        String bundlePath = "filter";
        mAssetManager.searchReservedAssets(mAssetType,bundlePath);
    }
    private void initFilterDataList(){
        mFilterItemArrayList = AssetFxUtil.getFilterData(this,
                getLocalData(),
                null,
                true,
                true);
    }

    private ArrayList<NvAsset> getBundleData(){
        return mAssetManager.getReservedAssets(mAssetType,NvAsset.AspectRatio_All,0);
    }

    private ArrayList<NvAsset> getLocalData(){
        return mAssetManager.getUsableAssets(mAssetType,NvAsset.AspectRatio_All,0);
    }

    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline),0);
            }
        });
        mVideoFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight",mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight",mBottomLayout.getLayoutParams().height);
        bundle.putBoolean("playBarVisible",true);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        mVideoFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.video_layout, mVideoFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mVideoFragment);
    }

    private void initFilterView(){
        mFilterView.setFilterArrayList(mFilterItemArrayList);
        mFilterView.initFilterRecyclerView(this);
        mSelectedPos = AssetFxUtil.getSelectedFilterPos(mFilterItemArrayList,mVideoClipFxInfo);
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
                if(position < 0 || position >= count)
                    return;
                if(mSelectedPos == position){
                    mVideoFragment.playVideoButtonCilck();
                    return;
                }

                mSelectedPos = position;
                mFilterView.setIntensitySeekBarProgress(100);
                if(position == 0) {
                    mFilterView.setIntensityLayoutVisible(View.INVISIBLE);
                    mVideoClipFxInfo.setFxMode(VideoClipFxInfo.FXMODE_BUILTIN);
                    mVideoClipFxInfo.setFxId(null);
                }else {
                    mFilterView.setIntensityLayoutVisible(View.VISIBLE);
                    FilterItem filterItem = mFilterItemArrayList.get(position);
                    int filterMode = filterItem.getFilterMode();
                    if(filterMode == FilterItem.FILTERMODE_BUILTIN) {
                        String filterName = filterItem.getFilterName();
                        mVideoClipFxInfo.setFxMode(VideoClipFxInfo.FXMODE_BUILTIN);
                        mVideoClipFxInfo.setFxId(filterName);
                        mVideoClipFxInfo.setIsCartoon(filterItem.getIsCartoon());
                        mVideoClipFxInfo.setGrayScale(filterItem.getGrayScale());
                        mVideoClipFxInfo.setStrokenOnly(filterItem.getStrokenOnly());
                    } else {
                        String packageId = filterItem.getPackageId();
                        mVideoClipFxInfo.setFxMode(VideoClipFxInfo.FXMODE_PACKAGE);
                        mVideoClipFxInfo.setFxId(packageId);
                    }
                    mVideoClipFxInfo.setFxIntensity(1.0f);
                }
                TimelineUtil.buildTimelineFilter(mTimeline, mVideoClipFxInfo);
                if(mStreamingContext.getStreamingEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    mVideoFragment.playVideoButtonCilck();
                }
            }

            @Override
            public void onMoreFilter() {
                Bundle bundle = new Bundle();
                bundle.putInt("titleResId",R.string.moreFilter);
                bundle.putInt("assetType",NvAsset.ASSET_FILTER);
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), AssetDownloadActivity.class, bundle,REQUEST_FILTER_LIST_CODE);
                mFilterView.setMoreFilterClickable(false);
            }

            @Override
            public void onIntensity(int value) {
                float intensity = value / (float) 100;
                mVideoClipFxInfo.setFxIntensity(intensity);
                updateFxIntensity(intensity);
                if(mStreamingContext.getStreamingEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline),0);
                }
            }
        });
    }

    private void updateFxIntensity(float value) {
        if(mTimeline == null)
            return;

        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        if(videoTrack == null)
            return;

        for(int i = 0;i < videoTrack.getClipCount();i++) {
            NvsVideoClip videoClip = videoTrack.getClipByIndex(i);
            if(videoClip == null)
                continue;

            int fxCount = videoClip.getFxCount();
            for(int j=0;j<fxCount;j++) {
                NvsVideoFx fx = videoClip.getFxByIndex(j);
                if(fx == null)
                    continue;

                String name = fx.getBuiltinVideoFxName();
                if(name == null) {
                    continue;
                }
                if(name.equals(Constants.FX_COLOR_PROPERTY) || name.equals(Constants.FX_VIGNETTE) ||
                        name.equals(Constants.FX_SHARPEN)|| name.equals(Constants.FX_TRANSFORM_2D)) {
                    continue;
                }
                fx.setFilterIntensity(value);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFilterView.setMoreFilterClickable(true);
    }
}
