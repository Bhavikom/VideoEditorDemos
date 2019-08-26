package com.meishe.sdkdemo.edit.transition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.download.AssetDownloadActivity;
import com.meishe.sdkdemo.edit.data.FilterItem;
import com.meishe.sdkdemo.edit.VideoFragment;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;
import com.meishe.sdkdemo.utils.dataInfo.TransitionInfo;

import java.util.ArrayList;
import java.util.List;

import static com.meishe.sdkdemo.utils.TimelineUtil.TIME_BASE;

public class TransitionActivity extends BaseActivity {
    private static final String TAG = "TransitionActivity";
    private static final int TRANSITIONREQUESTLIST = 105;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private NvsVideoTrack mVideoTrack;
    private VideoFragment mVideoFragment;
    private CustomTitleBar mTitleBar;
    private LinearLayout mBottomLayout;

    private RecyclerView mThumbRecyclerView;
    private RecyclerView mTransitionRecyclerView;
    private RelativeLayout mFinishLayout;
    private RelativeLayout mMoreDownload;
    private ImageView mDowanloadImage;
    private TextView mDowanloadMoreText;
    private TransitionAdapter mTransitionAdapter;
    private ThumbAdapter mThumbAdapter;

    private List<String> mFileData = new ArrayList<>();
    private ArrayList<FilterItem> mFilterList = new ArrayList<>();
    private TransitionInfo mTransitionInfo;

    private int mAssetType = NvAsset.ASSET_VIDEO_TRANSITION;
    private NvAssetManager mAssetManager;

    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_transition;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        mThumbRecyclerView = (RecyclerView) findViewById(R.id.thumbRecyclerView);
        mTransitionRecyclerView = (RecyclerView) findViewById(R.id.transitionRecyclerView);
        mFinishLayout = (RelativeLayout) findViewById(R.id.finishLayout);
        mMoreDownload = (RelativeLayout) findViewById(R.id.download_more_btn);
        mDowanloadImage = (ImageView) findViewById(R.id.dowanloadImage);
        mDowanloadMoreText = (TextView) findViewById(R.id.dowanloadMoreText);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.transition);
        mTitleBar.setBackImageVisible(View.GONE);
    }

    @Override
    protected void initData() {
        mTransitionInfo = TimelineData.instance().cloneTransitionData();
        if (mTransitionInfo == null)
            mTransitionInfo = new TransitionInfo();

        mTimeline = TimelineUtil.createTimeline();
        if (mTimeline != null) {
            mVideoTrack = mTimeline.getVideoTrackByIndex(0);
        }
        mAssetManager = NvAssetManager.sharedInstance();
        mAssetManager.searchLocalAssets(mAssetType);
        String bundlePath = "transition";
        mAssetManager.searchReservedAssets(mAssetType, bundlePath);

        initVideoFragment();
        initTransitionDataList();
        initThumbRecyclerView();
        initTransitionRecyclerView();
    }

    @Override
    protected void initListener() {
        mDowanloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreDownload.callOnClick();
            }
        });
        mDowanloadMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreDownload.callOnClick();
            }
        });

        mFinishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimelineData.instance().setTransitionData(mTransitionInfo);
                removeTimeline();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                AppManager.getInstance().finishActivity();
            }
        });
        mMoreDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.download_more_btn:
                mMoreDownload.setClickable(false);
                Bundle bundle = new Bundle();
                bundle.putInt("titleResId", R.string.moreTransition);
                bundle.putInt("assetType", NvAsset.ASSET_VIDEO_TRANSITION);
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), AssetDownloadActivity.class, bundle, TRANSITIONREQUESTLIST);
                break;
            default:
                break;
        }
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case TRANSITIONREQUESTLIST:
                initTransitionDataList();
                mTransitionAdapter.setFilterList(mFilterList);
                mTransitionAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
            }
        });
        mVideoFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight", mBottomLayout.getLayoutParams().height);
        bundle.putBoolean("playBarVisible", true);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        mVideoFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.video_layout, mVideoFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mVideoFragment);
    }

    private void initThumbRecyclerView() {
        if (mVideoTrack == null) {
            return;
        }

        for (int i = 0; i < mVideoTrack.getClipCount(); i++) {
            mFileData.add(mVideoTrack.getClipByIndex(i).getFilePath());
        }

        int selectedFilterPos = getSelectedFilterPos();
        FilterItem filterItem = null;
        if (selectedFilterPos >= 0) {
            filterItem = mFilterList.get(selectedFilterPos);
        }

        mThumbAdapter = new ThumbAdapter(this, mFileData, filterItem);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mThumbRecyclerView.setLayoutManager(linearLayoutManager);
        mThumbRecyclerView.setAdapter(mThumbAdapter);
        mThumbAdapter.setOnItemClickListener(new ThumbAdapter.OnItemClickListener() {
            @Override
            public void onThumbItemClick(View view, int position) {
                if (mVideoTrack == null)
                    return;

                int videoCount = mVideoTrack.getClipCount();
                if (videoCount < position)
                    return;

                NvsVideoClip clip = mVideoTrack.getClipByIndex(position);
                if (clip == null)
                    return;

                long inPoint = clip.getInPoint();
                mVideoFragment.playVideo(inPoint, -1);
            }

            @Override
            public void onTransitionItemClick(View view, int position) {
                if (mVideoTrack == null)
                    return;

                playTransition(position);
            }
        });
    }

    private void initTransitionDataList() {
        int[] resImages = {
                R.mipmap.fade, R.mipmap.turning, R.mipmap.swap, R.mipmap.stretch_in,
                R.mipmap.page_curl, R.mipmap.lens_flare, R.mipmap.star, R.mipmap.dip_to_black,
                R.mipmap.dip_to_white, R.mipmap.push_to_right, R.mipmap.push_to_left, R.mipmap.upper_left_into
        };

        mFilterList.clear();
        FilterItem filterItem = new FilterItem();
        filterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
        filterItem.setFilterName("无");
        filterItem.setImageId(R.mipmap.no);
        mFilterList.add(filterItem);

        List<String> builtinTransitionList = mStreamingContext.getAllBuiltinVideoTransitionNames();
        for (int i = 0; i < builtinTransitionList.size(); i++) {
            String transitionName = builtinTransitionList.get(i);
            FilterItem newFilterItem = new FilterItem();
            newFilterItem.setFilterName(transitionName);
            if (i < resImages.length) {
                int imageId = resImages[i];
                newFilterItem.setImageId(imageId);
            }
            newFilterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
            mFilterList.add(newFilterItem);
        }

        ArrayList<NvAsset> transitionList = getLocalData();
        String bundlePath = "transition/info.txt";
        Util.getBundleFilterInfo(this, transitionList, bundlePath);

        int ratio = TimelineData.instance().getMakeRatio();
        for (NvAsset asset : transitionList) {
            if ((ratio & asset.aspectRatio) == 0)
                continue;

            FilterItem newFilterItem = new FilterItem();
            if (asset.isReserved) {
                newFilterItem.setFilterMode(FilterItem.FILTERMODE_BUNDLE);
                String coverPath = "transition/";
                coverPath += asset.uuid;
                coverPath += ".png";
                asset.coverUrl = coverPath;
            } else {
                newFilterItem.setFilterMode(FilterItem.FILTERMODE_PACKAGE);
            }
            newFilterItem.setFilterName(asset.name);
            newFilterItem.setPackageId(asset.uuid);
            newFilterItem.setImageUrl(asset.coverUrl);
            mFilterList.add(newFilterItem);
        }

        int position = getSelectedFilterPos();
        if (mTransitionAdapter != null) {
            mTransitionAdapter.setSelectPos(position);
        }
    }

    private int getSelectedFilterPos() {
        if (mFilterList == null || mFilterList.size() == 0)
            return -1;

        if (mTransitionInfo != null) {
            String transitionName = mTransitionInfo.getTransitionId();
            if (TextUtils.isEmpty(transitionName))
                return 0;

            for (int i = 0; i < mFilterList.size(); i++) {
                FilterItem newFilterItem = mFilterList.get(i);
                if (newFilterItem == null)
                    continue;

                int filterMode = newFilterItem.getFilterMode();
                String filterName;
                if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                    filterName = newFilterItem.getFilterName();
                } else {
                    filterName = newFilterItem.getFilterId();
                }

                if (transitionName.equals(filterName)) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void initTransitionRecyclerView() {
        mTransitionAdapter = new TransitionAdapter(this);
        mTransitionAdapter.setFilterList(mFilterList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTransitionRecyclerView.setLayoutManager(linearLayoutManager);
        mTransitionRecyclerView.setAdapter(mTransitionAdapter);

        int selectedFilterPos = getSelectedFilterPos();
        mTransitionAdapter.setSelectPos(selectedFilterPos);

        mTransitionAdapter.setOnItemClickListener(new TransitionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < 0 || position >= mFilterList.size())
                    return;
                if (position == 0) {
                    mTransitionInfo.setTransitionMode(TransitionInfo.TRANSITIONMODE_BUILTIN);
                    mTransitionInfo.setTransitionId(null);
                } else {
                    FilterItem filterItem = mFilterList.get(position);
                    int filterMode = filterItem.getFilterMode();
                    if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                        String filterName = filterItem.getFilterName();
                        mTransitionInfo.setTransitionMode(TransitionInfo.TRANSITIONMODE_BUILTIN);
                        mTransitionInfo.setTransitionId(filterName);
                    } else if (filterMode == FilterItem.FILTERMODE_BUNDLE) {
                        String packageId = filterItem.getPackageId();
                        mTransitionInfo.setTransitionMode(TransitionInfo.TRANSITIONMODE_PACKAGE);
                        mTransitionInfo.setTransitionId(packageId);
                    } else {
                        String packageId = filterItem.getPackageId();
                        mTransitionInfo.setTransitionMode(TransitionInfo.TRANSITIONMODE_PACKAGE);
                        mTransitionInfo.setTransitionId(packageId);
                    }
                }

                TimelineUtil.setTransition(mTimeline, mTransitionInfo);
                if (mThumbAdapter != null) {
                    playTransition(mThumbAdapter.getSelectPos());
                }
            }

            @Override
            public void onResetTransition(FilterItem filterItem) {
                mThumbAdapter.setTransitionFilterItem(filterItem);
                mThumbAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSameItemClick() {
                if (mThumbAdapter != null) {
                    playTransition(mThumbAdapter.getSelectPos());
                }
            }
        });
    }

    private ArrayList<NvAsset> getBundleData() {
        return mAssetManager.getReservedAssets(mAssetType, NvAsset.AspectRatio_All, 0);
    }

    private ArrayList<NvAsset> getLocalData() {
        return mAssetManager.getUsableAssets(mAssetType, NvAsset.AspectRatio_All, 0);
    }

    @Override
    public void onBackPressed() {
        removeTimeline();
        super.onBackPressed();
        AppManager.getInstance().finishActivity();
    }

    private void removeTimeline() {
        TimelineUtil.removeTimeline(mTimeline);
        mTimeline = null;
    }

    private void playTransition(int index) {
        int videoCount = mVideoTrack.getClipCount();
        if (videoCount < index + 1)
            return;

        NvsVideoClip clip = mVideoTrack.getClipByIndex(index);
        if (clip == null)
            return;

        long inPointBefore = clip.getInPoint();
        long outPointBefore = clip.getOutPoint();

        NvsVideoClip nextClip = mVideoTrack.getClipByIndex(index + 1);
        if (nextClip == null)
            return;

        long inPointNext = nextClip.getInPoint();
        long outPointNext = nextClip.getOutPoint();
        outPointBefore -= TIME_BASE;
        inPointNext += TIME_BASE;
        if (outPointBefore < inPointBefore) {
            outPointBefore = inPointBefore;
        }
        if (inPointNext > outPointNext) {
            inPointNext = outPointNext;
        }

        mVideoFragment.playVideo(outPointBefore, inPointNext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMoreDownload.setClickable(true);
    }
}
