package com.meishe.sdkdemo.edit.animatesticker.customsticker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsVideoResolution;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.download.AssetDownloadActivity;
import com.meishe.sdkdemo.edit.adapter.SpaceItemDecoration;
import com.meishe.sdkdemo.edit.clipEdit.SingleClipFragment;
import com.meishe.sdkdemo.edit.data.ParseJsonFile;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.UUID;

public class CustomAnimateStickerEffectActivity extends BaseActivity {
    private static final String TAG = "CustomASEffectActivity";
    private static final int CUSTOMSTICKERREQUESTLIST = 109;
    private static final int CUSTOMSTICKERPLAYBACKEOF = 1001;

    private static final long seekTimelineTamp = 1000000;
    
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private RelativeLayout mSpaceLayout;
    private RelativeLayout mDownloadMoreButton;
    private ImageView mDowanloadImage;
    private TextView mDowanloadMoreText;
    private RecyclerView mCustomStickerEffectList;
    private ImageView mCustomStickerEffectFinish;
    private ImageView mCustomStickerIamge;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private SingleClipFragment mClipFragment;
    private String mImageSrcFilePath;
    private int makeRatio = NvAsset.AspectRatio_16v9;
    private CustomStickerRecycleViewAdaper mCustomStickerRecyclerAdapter;
    private NvAssetManager mAssetManager;
    private ArrayList<NvAsset> mCustomStickerAssetList;//自定义贴纸列表
    private int mCustomAssetType = NvAsset.ASSET_CUSTOM_ANIMATED_STICKER;
    private String mSelectCustomStickerUuid = "";
    private NvsTimelineAnimatedSticker mCurrentAddCustomSticker = null;
    private CustomAnimateStickerEffectActivity.CustomStickerEffectHandler m_handler = new CustomAnimateStickerEffectActivity.CustomStickerEffectHandler(this);
    static class CustomStickerEffectHandler extends Handler
    {
        WeakReference<CustomAnimateStickerEffectActivity> mWeakReference;
        public CustomStickerEffectHandler(CustomAnimateStickerEffectActivity activity)
        {
            mWeakReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg)
        {
            final CustomAnimateStickerEffectActivity activity = mWeakReference.get();
            if(activity != null)
            {
                switch (msg.what) {
                    case CUSTOMSTICKERPLAYBACKEOF:
                       activity.seekTimeline();
                        break;
                    default:
                        break;
                }
            }
        }
    }
    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_custom_animate_sticker_effect;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout)findViewById(R.id.bottomLayout);
        mSpaceLayout = (RelativeLayout)findViewById(R.id.spaceLayout);
        mCustomStickerIamge = (ImageView)findViewById(R.id.customStickerIamge);
        mDownloadMoreButton = (RelativeLayout)findViewById(R.id.download_more_btn);
        mDowanloadImage = (ImageView)findViewById(R.id.dowanloadImage);
        mDowanloadMoreText = (TextView)findViewById(R.id.dowanloadMoreText);
        mCustomStickerEffectList = (RecyclerView)findViewById(R.id.customStickerEffectList);
        mCustomStickerEffectFinish = (ImageView)findViewById(R.id.customStickerEffectFinish);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.customStickerselecteffect);
    }

    @Override
    protected void initData() {
        if(!initAssetData())
            return;
        setCustomStickerImageImage(mImageSrcFilePath);
        initCustomStickerEffectDataList();
        initCustomEffectRecyclerList();
        initVideoFragment();
    }

    @Override
    protected void initListener() {
        mDownloadMoreButton.setOnClickListener(this);
        mCustomStickerEffectFinish.setOnClickListener(this);
        mDowanloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDownloadMoreButton.callOnClick();
            }
        });
        mDowanloadMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDownloadMoreButton.callOnClick();
            }
        });
        mClipFragment.setVideoFragmentCallBack(new SingleClipFragment.VideoFragmentListener() {
            @Override
            public void playBackEOF(NvsTimeline timeline) {
                m_handler.sendEmptyMessage(CUSTOMSTICKERPLAYBACKEOF);
            }

            @Override
            public void playStopped(NvsTimeline timeline) {

            }

            @Override
            public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {

            }

            @Override
            public void streamingEngineStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.download_more_btn:
                mStreamingContext.stop();
                mDownloadMoreButton.setClickable(false);
                Bundle bundle = new Bundle();
                bundle.putInt("titleResId",R.string.moreCustomStickerEffect);
                bundle.putInt("assetType",NvAsset.ASSET_CUSTOM_ANIMATED_STICKER);
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), AssetDownloadActivity.class, bundle,CUSTOMSTICKERREQUESTLIST);
                break;
            case R.id.customStickerEffectFinish:
                mCustomStickerEffectFinish.setClickable(false);
                if(!mSelectCustomStickerUuid.isEmpty()){
                    //添加数据到自定义贴纸数据列表
                    NvAssetManager.NvCustomStickerInfo customStickerInfo = new NvAssetManager.NvCustomStickerInfo();
                    customStickerInfo.uuid = UUID.randomUUID().toString();//自定义贴纸的Uuid值
                    customStickerInfo.imagePath = mImageSrcFilePath;
                    customStickerInfo.templateUuid = mSelectCustomStickerUuid;
                    customStickerInfo.order = mAssetManager.getUsableCustomStickerAssets().size();
                    mAssetManager.appendCustomStickerInfoData(customStickerInfo);
                    //存储自定义贴纸
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            NvAssetManager.sharedInstance().setCustomStickerInfoToSharedPreferences();
                        }
                    }).start();
                }
                AppManager.getInstance().finishActivity();
                AppManager.getInstance().finishActivity();
                AppManager.getInstance().finishActivity();
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
            case CUSTOMSTICKERREQUESTLIST:
                initCustomStickerEffectDataList();
                mCustomStickerRecyclerAdapter.setAssetList(mCustomStickerAssetList);
                int selectPos = getSelectedPos();
                mCustomStickerRecyclerAdapter.setSelectedPos(selectPos);
                mCustomStickerRecyclerAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDownloadMoreButton.setClickable(true);
        mCustomStickerEffectFinish.setClickable(true);
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

    private boolean setCustomStickerImageImage( String imageFilePath){
        if(imageFilePath.isEmpty())
            return false;
        Glide.with(CustomAnimateStickerEffectActivity.this).load(imageFilePath).into(mCustomStickerIamge);
        setCustomStickerImageSize(imageFilePath);
        return true;
    }
    private void setCustomStickerImageSize(String imagePath){
        int statusHeight = ScreenUtils.getStatusBarHeight(this);//状态栏高度
        int screenWidth = ScreenUtils.getScreenWidth(this);//屏宽
        int screenHeight = ScreenUtils.getScreenHeight(this);//屏高
        int titleHeight = mTitleBar.getLayoutParams().height;
        int bottomHeight = mBottomLayout.getLayoutParams().height;

        NvsAVFileInfo avFileInfo = mStreamingContext.getAVFileInfo(imagePath);
        if(avFileInfo != null) {
            ViewGroup.LayoutParams layoutParams = mCustomStickerIamge.getLayoutParams();
            NvsSize videoSize = avFileInfo.getVideoStreamDimension(0);
            float newRatio = videoSize.width / (videoSize.height * 1.0f);
            int offset = 200;
            if(newRatio >= 1.0f){
                int newWidth = screenWidth - offset;
                layoutParams.width = newWidth;
                layoutParams.height = (int)Math.floor(newWidth / newRatio + 0.5D);
            }else {
                int newHeight = screenHeight - statusHeight - titleHeight - bottomHeight - offset;
                layoutParams.width = (int)Math.floor(newHeight * newRatio + 0.5D);
                layoutParams.height = newHeight;
            }
            mCustomStickerIamge.setLayoutParams(layoutParams);
        }
    }
    private int getSelectedPos(){
        int selectPos = -1;
        if(mSelectCustomStickerUuid.isEmpty())
            return selectPos;
        for (int index = 0;index < mCustomStickerAssetList.size();++index){
            if (mCustomStickerAssetList.get(index).uuid.equals(mSelectCustomStickerUuid)){
                selectPos = index;
                break;
            }
        }
        return selectPos;
    }
    private boolean initAssetData(){
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            mImageSrcFilePath = bundle.getString("imageDstFilePath");
        }
        if(mImageSrcFilePath.isEmpty())
            return false;

        NvsVideoResolution videoResolution = getVideoResolution();
        if(videoResolution != null) {
            String videoImagePath = "assets:/custom.png";
            mTimeline = TimelineUtil.createSingleClipTimelineExt(videoResolution,videoImagePath);
        }
        if(mTimeline == null)
            return false;
        mCustomStickerAssetList = new ArrayList<>();
        mAssetManager = NvAssetManager.sharedInstance();
        //查找自定义贴纸
        mAssetManager.searchLocalAssets(mCustomAssetType);
        String bundlePath = "customsticker";
        mAssetManager.searchReservedAssets(mCustomAssetType,bundlePath);
        return true;
    }
    //获取下载到手机缓存路径下的素材，包括assets路径下自带的素材
    private ArrayList<NvAsset> getCustomAssetsDataList(){
        return mAssetManager.getUsableAssets(mCustomAssetType,NvAsset.AspectRatio_All,0);
    }

    private void initCustomStickerEffectDataList(){
        mCustomStickerAssetList.clear();
        ArrayList<NvAsset> useableAsset = getCustomAssetsDataList();
        String jsonBundlePath =  "customsticker/info.json";
        ArrayList<ParseJsonFile.FxJsonFileInfo.JsonFileInfo> infoLists = ParseJsonFile.readBundleFxJsonFile(this,jsonBundlePath);
        if(infoLists != null) {
            for (ParseJsonFile.FxJsonFileInfo.JsonFileInfo jsonFileInfo : infoLists) {
                for (NvAsset asset : useableAsset) {
                    if(asset == null)
                        continue;
                    if(TextUtils.isEmpty(asset.uuid))
                        continue;

                    //assets路径下的贴纸模板包
                    if(asset.isReserved && asset.uuid.equals(jsonFileInfo.getFxPackageId())){
                        asset.name = jsonFileInfo.getName();
                        asset.aspectRatio = Integer.parseInt(jsonFileInfo.getFitRatio());
                        StringBuilder coverPath = new StringBuilder("file:///android_asset/customsticker/");
                        coverPath.append(jsonFileInfo.getImageName());
                        asset.coverUrl = coverPath.toString();
                    }
                }
            }
        }
        mCustomStickerAssetList = useableAsset;
    }

    private NvsVideoResolution getVideoResolution(){
        NvsAVFileInfo avFileInfo = mStreamingContext .getAVFileInfo(mImageSrcFilePath);
        if(avFileInfo != null) {
            NvsVideoResolution videoResolution = new NvsVideoResolution();
            NvsSize videoSize = avFileInfo.getVideoStreamDimension(0);
            if(videoSize.width > videoSize.height){
                videoResolution.imageWidth = 1280;
                videoResolution.imageHeight = 720;
                makeRatio = NvAsset.AspectRatio_16v9;
            }else if(videoSize.width == videoSize.height){
                videoResolution.imageWidth = 720;
                videoResolution.imageHeight = 720;
                makeRatio = NvAsset.AspectRatio_1v1;
            }else {
                videoResolution.imageWidth = 720;
                videoResolution.imageHeight = 1280;
                makeRatio = NvAsset.AspectRatio_9v16;
            }
            return videoResolution;
        }
        return null;
    }
    private void initVideoFragment() {
        mClipFragment = new SingleClipFragment();
        mClipFragment.setFragmentLoadFinisedListener(new SingleClipFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                seekTimeline();
            }
        });
        mClipFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight",mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight",mBottomLayout.getLayoutParams().height);
        bundle.putBoolean("playBarVisible",false);
        bundle.putInt("ratio", makeRatio);
        mClipFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.spaceLayout, mClipFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mClipFragment);
    }

    private void initCustomEffectRecyclerList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(CustomAnimateStickerEffectActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mCustomStickerEffectList.setLayoutManager(layoutManager);
        mCustomStickerRecyclerAdapter = new CustomStickerRecycleViewAdaper(CustomAnimateStickerEffectActivity.this);
        mCustomStickerRecyclerAdapter.setAssetList(mCustomStickerAssetList);
        mCustomStickerEffectList.setAdapter(mCustomStickerRecyclerAdapter);
        mCustomStickerEffectList.addItemDecoration(new SpaceItemDecoration(14,26));
        mCustomStickerRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if(pos < 0 || pos >= mCustomStickerAssetList.size())
                    return;
                if(mCustomStickerIamge.getVisibility() != View.GONE)
                    mCustomStickerIamge.setVisibility(View.GONE);
                if(mSpaceLayout.getVisibility() != View.VISIBLE)
                    mSpaceLayout.setVisibility(View.VISIBLE);
                int selectPos = getSelectedPos();
                if(selectPos >= 0 && selectPos == pos){
                    playVideo();
                    return;
                }
                if(mCurrentAddCustomSticker != null){
                    mTimeline.removeAnimatedSticker(mCurrentAddCustomSticker);
                    mCurrentAddCustomSticker = null;
                }
                mSelectCustomStickerUuid = mCustomStickerAssetList.get(pos).uuid;
                mCurrentAddCustomSticker = mTimeline.addCustomAnimatedSticker(0,mTimeline.getDuration(),mSelectCustomStickerUuid,mImageSrcFilePath);
                playVideo();
            }
        });
    }

    private void seekTimeline(){
        mClipFragment.seekTimeline(seekTimelineTamp, 0);
    }
    private void playVideo(){
        mClipFragment.playVideo(0,mTimeline.getDuration());
    }
}
