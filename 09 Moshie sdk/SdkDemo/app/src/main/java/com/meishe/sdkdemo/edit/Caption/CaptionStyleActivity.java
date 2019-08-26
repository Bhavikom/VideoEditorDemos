package com.meishe.sdkdemo.edit.Caption;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsColor;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineCaption;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.download.AssetDownloadActivity;
import com.meishe.sdkdemo.edit.VideoFragment;
import com.meishe.sdkdemo.edit.data.AssetItem;
import com.meishe.sdkdemo.edit.data.BackupData;
import com.meishe.sdkdemo.edit.data.CaptionColorInfo;
import com.meishe.sdkdemo.edit.data.ParseJsonFile;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.edit.view.CustomViewPager;
import com.meishe.sdkdemo.edit.view.InputDialog;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.ColorUtil;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.ToastUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;
import com.meishe.sdkdemo.utils.dataInfo.CaptionInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.meishe.sdkdemo.utils.Constants.ASSET_DOWNLOAD_FAILED;
import static com.meishe.sdkdemo.utils.Constants.ASSET_DOWNLOAD_INPROGRESS;
import static com.meishe.sdkdemo.utils.Constants.ASSET_DOWNLOAD_START_TIMER;
import static com.meishe.sdkdemo.utils.Constants.ASSET_DOWNLOAD_SUCCESS;
import static com.meishe.sdkdemo.utils.Constants.ASSET_LIST_REQUEST_FAILED;
import static com.meishe.sdkdemo.utils.Constants.ASSET_LIST_REQUEST_SUCCESS;
import static com.meishe.sdkdemo.utils.Constants.CaptionColors;


public class CaptionStyleActivity extends BaseActivity {
    private static final String TAG = "CaptionStyleActivity";
    private static final int CAPTIONSTYLEREQUESTLIST = 103;
    private static final int VIDEOPLAYTOEOF = 105;

    private static final int CAPTION_ALIGNLEFT = 0;
    private static final int CAPTION_ALIGNHORIZCENTER = 1;
    private static final int CAPTION_ALIGNRIGHT = 2;
    private static final int CAPTION_ALIGNTOP = 3;
    private static final int CAPTION_ALIGNVERTCENTER = 4;
    private static final int CAPTION_ALIGNBOTTOM = 5;
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;

    private TabLayout mCaptionStyleTab;
    private CustomViewPager mViewPager;
    private ImageView mCaptionAssetFinish;
    private VideoFragment mVideoFragment;
    private ArrayList<AssetItem> mTotalCaptionStyleList;//总的字幕样式列表
    private ArrayList<Fragment> mAssetFragmentsArray;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;

    private NvAssetManager mAssetManager;
    private int mCaptionStyleType = NvAsset.ASSET_CAPTION_STYLE;
    private int mFontType = NvAsset.ASSET_FONT;

    private int mSelectedStylePos = 0;
    private int mSelectedColorPos = -1;
    private int mSelectedOutlinePos = 0;
    private int mSelectedFontPos = 0;
    NvsTimelineCaption mCurAddCaption = null;
    private int mAlignType = -1;
    private CaptionStyleFragment mCaptionStyleFragment;
    private CaptionColorFragment mCaptionColorFragment;
    private CaptionOutlineFragment mCaptionOutlineFragment;
    private CaptionFontFragment mCaptionFontFragment;
    private CaptionSizeFragment mCaptionSizeFragment;
    private CaptionPositionFragment mCaptionPositionFragment;
    private ArrayList<CaptionColorInfo> mCaptionColorList;
    private ArrayList<CaptionColorInfo> mCaptionOutlineColorList;
    private ArrayList<AssetItem> mCaptionFontList;
    private int mCaptionColorOpacityValue = 100;
    private int mCaptionOutlineWidthValue = 8;
    private int mCaptionOutlineOpacityValue = 100;
    private int mCaptionSizeValue = 72;
    ArrayList<CaptionInfo> mCaptionDataListClone;
    private int mCurCaptionZVal = 0;

    private boolean bIsStyleUuidApplyToAll = false;
    private boolean bIsCaptionColorApplyToAll = false;
    private boolean bIsOutlineApplyToAll = false;
    private boolean bIsFontApplyToAll = false;
    private boolean bIsSizeApplyToAll = false;
    private boolean bIsPositionApplyToAll = false;

    private boolean isCaptionStyleItemClick = false;
    boolean m_waitFlag = false;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private int mFontCurClickPos = 0;
    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_caption_style;
    }

    private CaptionStyleActivity.CaptionStyleHandler m_handler = new CaptionStyleActivity.CaptionStyleHandler(this);
    static class CaptionStyleHandler extends Handler
    {
        WeakReference<CaptionStyleActivity> mWeakReference;
        public CaptionStyleHandler(CaptionStyleActivity activity)
        {
            mWeakReference= new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg)
        {
            final CaptionStyleActivity activity = mWeakReference.get();
            if(activity != null)
            {
                switch (msg.what) {
                    case VIDEOPLAYTOEOF:
                        activity.updateCaption();
                        break;
                    case ASSET_LIST_REQUEST_SUCCESS:
                        activity.updateFontList();
                        break;
                    case ASSET_LIST_REQUEST_FAILED:
                        activity.fontListRequestFail();
                        break;
                    case ASSET_DOWNLOAD_START_TIMER:
                        activity.startProgressTimer();
                        String progressUuid = (String) msg.obj;
                        activity.fontItemCopy(progressUuid);
                        break;
                    case ASSET_DOWNLOAD_SUCCESS:
                        String successUuid = (String) msg.obj;
                        activity.fontItemCopy(successUuid);
                        activity.applyLastSelFont(successUuid);
                        activity.updateFontItem();
                        break;
                    case ASSET_DOWNLOAD_FAILED:
                        activity.fontDownloadFail();
                        activity.updateFontItem();
                        break;
                    case ASSET_DOWNLOAD_INPROGRESS:
                        activity.updateFontDownloadProgress();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar)findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout)findViewById(R.id.bottom_layout);
        mCaptionStyleTab = (TabLayout)findViewById(R.id.captionStyleTab);
        mViewPager = (CustomViewPager)findViewById(R.id.viewPager);
        mViewPager.setPagingEnabled(false);
        mCaptionAssetFinish = (ImageView)findViewById(R.id.captionAssetFinish);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setBackImageVisible(View.GONE);
        mTitleBar.setTextCenter(R.string.caption);
    }

    @Override
    protected void initData() {
        initAssetData();
        initVideoFragment();
        initTabLayout();
    }

    @Override
    protected void initListener() {
        mCaptionAssetFinish.setOnClickListener(this);
        if(mVideoFragment != null){
            mVideoFragment.setCaptionTextEditListener(new VideoFragment.VideoCaptionTextEditListener() {
                @Override
                public void onCaptionTextEdit() {
                    //字幕编辑
                    InputDialog inputDialog = new InputDialog(CaptionStyleActivity.this, R.style.dialog, new InputDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean ok) {
                            if (ok) {
                                InputDialog d = (InputDialog) dialog;
                                String userInputText = d.getUserInputText();
                                mCurAddCaption.setText(userInputText);
                                updateCaption();
                                int index = getCaptionIndex(mCurCaptionZVal);
                                if(index >= 0){
                                    mCaptionDataListClone.get(index).setText(userInputText);
                                }
                            }
                        }
                    });
                    if(mCurAddCaption != null) {
                        inputDialog.setUserInputText(mCurAddCaption.getText());
                    }
                    inputDialog.show();
                }
            });
            mVideoFragment.setAssetEditListener(new VideoFragment.AssetEditListener() {
                @Override
                public void onAssetDelete() {
                    mTimeline.removeCaption(mCurAddCaption);
                    mCurAddCaption = null;
                    mVideoFragment.setCurCaption(mCurAddCaption);
                    mVideoFragment.changeCaptionRectVisible();
                    int index = getCaptionIndex(mCurCaptionZVal);
                    if(index >= 0){
                        mCaptionDataListClone.remove(index);
                        BackupData.instance().setCaptionData(mCaptionDataListClone);
                        removeTimeline();
                        Intent intent = new Intent();
                        intent.putExtra("isSelectCurCaption",false);
                        setResult(RESULT_OK, intent);
                        AppManager.getInstance().finishActivity();
                    }
                }

                @Override
                public void onAssetSelected(PointF curPoint) {
                }

                @Override
                public void onAssetTranstion() {
                    if(mCurAddCaption == null)
                        return;
                    PointF captionTranslation = mCurAddCaption.getCaptionTranslation();
                    //Log.e(TAG,"captionTranslation.x = " + captionTranslation.x + "pointF.y =" + captionTranslation.y);
                    int zVal = (int) mCurAddCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if(index >= 0) {
                        mCaptionDataListClone.get(index).setUsedTranslationFlag(CaptionInfo.ATTRIBUTE_USED_FLAG);
                        mCaptionDataListClone.get(index).setTranslation(captionTranslation);
                    }
                }

                @Override
                public void onAssetScale() {
                    int zVal = (int) mCurAddCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if(index >= 0){
                        mCaptionDataListClone.get(index).setUsedScaleRotationFlag(CaptionInfo.ATTRIBUTE_USED_FLAG);
                        mCaptionDataListClone.get(index).setScaleFactorX(mCurAddCaption.getScaleX());
                        mCaptionDataListClone.get(index).setScaleFactorY(mCurAddCaption.getScaleY());
                        mCaptionDataListClone.get(index).setAnchor(mCurAddCaption.getAnchorPoint());
                        mCaptionDataListClone.get(index).setRotation(mCurAddCaption.getRotationZ());
                        mCaptionDataListClone.get(index).setCaptionSize(mCurAddCaption.getFontSize());
                        mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
                    }
                }

                @Override
                public void onAssetAlign(int alignVal) {
                    int zVal = (int) mCurAddCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if(index >= 0)
                        mCaptionDataListClone.get(index).setAlignVal(alignVal);
                }

                @Override
                public void onAssetHorizFlip(boolean isHorizFlip) {

                }
            });
            mVideoFragment.setVideoFragmentCallBack(new VideoFragment.VideoFragmentListener() {
                @Override
                public void playBackEOF(NvsTimeline timeline) {
                    m_handler.sendEmptyMessage(VIDEOPLAYTOEOF);
                }

                @Override
                public void playStopped(NvsTimeline timeline) {
                    if(isCaptionStyleItemClick)
                        return;
                    updateDrawRect();
                }

                @Override
                public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {
                    mVideoFragment.setDrawRectVisible(View.GONE);
                }

                @Override
                public void streamingEngineStateChanged(int state) {

                }
            });
            mVideoFragment.setLiveWindowClickListener(new VideoFragment.OnLiveWindowClickListener() {
                @Override
                public void onLiveWindowClick() {
                    isCaptionStyleItemClick = false;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.captionAssetFinish:
                applyToAllCaption();
                BackupData.instance().setCaptionData(mCaptionDataListClone);
                removeTimeline();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                intent.putExtra("isSelectCurCaption",true);
                AppManager.getInstance().finishActivity();
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

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case CAPTIONSTYLEREQUESTLIST:
                initCaptionStyleList();
                mCaptionStyleFragment.setAssetInfolist(mTotalCaptionStyleList);
                mSelectedStylePos = getCaptionStyleSelectedIndex();
                mCaptionStyleFragment.setSelectedPos(mSelectedStylePos);
                mCaptionStyleFragment.notifyDataSetChanged();
                updateCaption();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();
        //存储素材数据线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                NvAssetManager.sharedInstance().setAssetInfoToSharedPreferences(mFontType);
            }
        }).start();
    }

    private void applyLastSelFont(String uuid){
        String curClickUuid = mCaptionFontList.get(mFontCurClickPos).getAsset().uuid;
        if(!TextUtils.isEmpty(curClickUuid) && curClickUuid.equals(uuid)){
            String fontPath = mCaptionFontList.get(mFontCurClickPos).getAsset().localDirPath;
            applyCaptionFont(fontPath);
            mCaptionFontFragment.setSelectedPos(mFontCurClickPos);
            mSelectedFontPos = mFontCurClickPos;
        }
    }

    private void startProgressTimer() {
        if(mTimer == null)
            mTimer = new Timer();
        if(mTimerTask == null){
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    m_handler.sendEmptyMessage(ASSET_DOWNLOAD_INPROGRESS);
                }
            };
            mTimer.schedule(mTimerTask, 0, 50);
        }
    }

    private void stopProgressTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void updateFontList(){
        initCaptionFontList();
        mSelectedFontPos = getCaptionFontSelectedIndex();
        mCaptionFontFragment.setFontInfolist(mCaptionFontList);
        mCaptionFontFragment.setSelectedPos(mSelectedFontPos);
        mCaptionFontFragment.notifyDataSetChanged();
    }

    private void updateFontDownloadProgress(){
        boolean isDownloadState = false;
        for (int i = 0; i < mCaptionFontList.size();++i){
            NvAsset asset = mCaptionFontList.get(i).getAsset();
            if(asset == null)
                continue;
            if(asset.downloadStatus == NvAsset.DownloadStatusInProgress
                    || asset.downloadStatus == NvAsset.DownloadStatusPending){
                isDownloadState = true;
            }
        }
        if(isDownloadState){//是下载状态，通知更新数据
            updateFontItem();
        }
    }

    private void updateFontItem(){
        mCaptionFontFragment.notifyDataSetChanged();
    }

    private void fontItemCopy(String uuid){
        NvAsset curAsset = null;
        ArrayList<NvAsset> usableAsset = getFontAssetsDataList();
        for (int index = 0;index < usableAsset.size();++index){
            NvAsset asset = usableAsset.get(index);
            if(asset == null)
                continue;
           if(!TextUtils.isEmpty(asset.uuid) && uuid.equals(asset.uuid)){
               curAsset = asset;
               break;
           }
        }

        for (int i = 0; i < mCaptionFontList.size();++i){
            NvAsset asset = mCaptionFontList.get(i).getAsset();
            if(asset == null)
                continue;
            if(curAsset != null &&!TextUtils.isEmpty(asset.uuid) && asset.uuid.equals(uuid)){
                mCaptionFontList.get(i).getAsset().copyAsset(curAsset);
            }
        }
    }

    private void fontListRequestFail(){
        ToastUtil.showToast(this,this.getResources().getString(R.string.check_network));
    }

    private void fontDownloadFail(){
        ToastUtil.showToast(this,this.getResources().getString(R.string.download_failed));
    }
    private void applyToAllCaption(){
        int index = getCaptionIndex(mCurCaptionZVal);
        if(index < 0)
            return;
        int count = mCaptionDataListClone.size();
        CaptionInfo curCaptionInfo = mCaptionDataListClone.get(index);
        for (int i = 0; i < count;++i){
            if(i == index)
                continue;
            if(bIsStyleUuidApplyToAll){
                mCaptionDataListClone.get(i).setCaptionStyleUuid(curCaptionInfo.getCaptionStyleUuid());
            }
            if(bIsCaptionColorApplyToAll){
                mCaptionDataListClone.get(i).setCaptionColor(curCaptionInfo.getCaptionColor());
                mCaptionDataListClone.get(i).setCaptionColorAlpha(curCaptionInfo.getCaptionColorAlpha());
            }
            if(bIsOutlineApplyToAll){
                mCaptionDataListClone.get(i).setHasOutline(curCaptionInfo.isHasOutline());
                mCaptionDataListClone.get(i).setOutlineColor(curCaptionInfo.getOutlineColor());
                mCaptionDataListClone.get(i).setOutlineColorAlpha(curCaptionInfo.getOutlineColorAlpha());
                mCaptionDataListClone.get(i).setOutlineWidth(curCaptionInfo.getOutlineWidth());
            }
            if(bIsFontApplyToAll){
                mCaptionDataListClone.get(i).setCaptionFont(curCaptionInfo.getCaptionFont());
                mCaptionDataListClone.get(i).setBold(curCaptionInfo.isBold());
                mCaptionDataListClone.get(i).setItalic(curCaptionInfo.isItalic());
                mCaptionDataListClone.get(i).setShadow(curCaptionInfo.isShadow());
            }
            if(bIsSizeApplyToAll){
                mCaptionDataListClone.get(i).setCaptionSize(curCaptionInfo.getCaptionSize());
            }
        }
        if(bIsPositionApplyToAll)
            updateCaptionPosition();
    }
    private void updateCaptionPosition(){
        NvsTimelineCaption caption = mTimeline.getFirstCaption();
        while (caption != null){
            if( caption.getCategory() == NvsTimelineCaption.THEME_CATEGORY
                    && caption.getRoleInTheme() != NvsTimelineCaption.ROLE_IN_THEME_GENERAL){//主题字幕不作处理
                caption = mTimeline.getNextCaption(caption);
                continue;
            }
            int zVal = (int)caption.getZValue();
            if(mCurCaptionZVal == zVal){
                caption = mTimeline.getNextCaption(caption);
                continue;
            }
            List<PointF> list = caption.getBoundingRectangleVertices();
            if(list == null || list.size() < 4){
                caption = mTimeline.getNextCaption(caption);
                continue;
            }

            int index = getCaptionIndex(zVal);
            switch (mAlignType){
                case CAPTION_ALIGNLEFT://左对齐
                    Collections.sort(list, new Util.PointXComparator());
                    float xLeftOffset = -(mTimeline.getVideoRes().imageWidth/2 + list.get(0).x);
                    caption.translateCaption(new PointF(xLeftOffset,0));
                    break;
                case CAPTION_ALIGNHORIZCENTER://水平居中
                    Collections.sort(list, new Util.PointXComparator());
                    float xHorizCenterOffset = -((list.get(3).x - list.get(0).x)/2 + list.get(0).x);
                    caption.translateCaption(new PointF(xHorizCenterOffset,0));
                    break;
                case CAPTION_ALIGNRIGHT://右对齐
                    Collections.sort(list, new Util.PointXComparator());
                    float xRightOffset = mTimeline.getVideoRes().imageWidth/2 - list.get(3).x;
                    caption.translateCaption(new PointF(xRightOffset,0));
                    break;
                case CAPTION_ALIGNTOP://上对齐
                    Collections.sort(list, new Util.PointYComparator());
                    float yTopdis = list.get(3).y - list.get(0).y;
                    float yTopOffset = mTimeline.getVideoRes().imageHeight/2 - list.get(0).y - yTopdis;
                    caption.translateCaption(new PointF(0,yTopOffset));
                    break;
                case CAPTION_ALIGNVERTCENTER://垂直居中
                    Collections.sort(list, new Util.PointYComparator());
                    float yVertCenterOffset = -((list.get(3).y - list.get(0).y)/2 + list.get(0).y);
                    caption.translateCaption(new PointF(0,yVertCenterOffset));
                    break;
                case CAPTION_ALIGNBOTTOM://底部对齐
                    Collections.sort(list, new Util.PointYComparator());
                    float yBottomdis = list.get(3).y - list.get(0).y;
                    float yBottomOffset = -(mTimeline.getVideoRes().imageHeight/2 + list.get(3).y - yBottomdis);
                    caption.translateCaption(new PointF(0,yBottomOffset));
                    break;
                default:
                    break;
            }
            if(index >= 0)
                mCaptionDataListClone.get(index).setTranslation(caption.getCaptionTranslation());
            caption = mTimeline.getNextCaption(caption);
        }
    }
    private void removeTimeline(){
        TimelineUtil.removeTimeline(mTimeline);
        mCurAddCaption = null;
        mTimeline = null;
        m_handler.removeCallbacksAndMessages(null);
        stopProgressTimer();
    }

    private void initTabLayout(){
        String[] assetName = getResources().getStringArray(R.array.captionEdit);
        for (int i = 0; i < assetName.length; i++) {
            mCaptionStyleTab.addTab(mCaptionStyleTab.newTab().setText(assetName[i]));
        }
        initCaptionTabFragment();
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
        mCaptionStyleTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //当前选中的tab的位置，切换到相应的fragment
                int nowPosition = tab.getPosition();
                mViewPager.setCurrentItem(nowPosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void initCaptionTabFragment(){
        mCaptionStyleFragment = initCaptionStyleFragment();
        mAssetFragmentsArray.add(mCaptionStyleFragment);
        mCaptionColorFragment = initCaptionColorFragment();
        mAssetFragmentsArray.add(mCaptionColorFragment);
        mCaptionOutlineFragment = initCaptionOutlineFragment();
        mAssetFragmentsArray.add(mCaptionOutlineFragment);
        mCaptionFontFragment = initCaptionFontFragment();
        mAssetFragmentsArray.add(mCaptionFontFragment);
        mCaptionSizeFragment = initCaptionSizeFragment();
        //mAssetFragmentsArray.add(mCaptionSizeFragment);
        mCaptionPositionFragment = initCaptionPositionFragment();
        mAssetFragmentsArray.add(mCaptionPositionFragment);
    }
    private void initAssetData(){
        mTimeline = TimelineUtil.createTimeline();
        if(mTimeline == null)
            return;
        mCurCaptionZVal = BackupData.instance().getCaptionZVal();
        mCaptionDataListClone = BackupData.instance().cloneCaptionData();
        TimelineUtil.setCaption(mTimeline,mCaptionDataListClone);

        mTotalCaptionStyleList = new ArrayList<>();
        mAssetFragmentsArray = new ArrayList<>();
        mCaptionColorList = new ArrayList<>();
        mCaptionOutlineColorList = new ArrayList<>();
        mCaptionFontList = new ArrayList<>();
        //
        mAssetManager = NvAssetManager.sharedInstance();

        mAssetManager.searchLocalAssets(mCaptionStyleType);
        String bundlePath = "captionstyle";
        mAssetManager.searchReservedAssets(mCaptionStyleType,bundlePath);
        mAssetManager.searchLocalAssets(mFontType);//查找字体文件
        assetDataRequest();

        initCaptionStyleList();
        initCaptionColorList();
        initCaptionOutlineColorList();
    }
    private void initCaptionStyleList(){
        mTotalCaptionStyleList.clear();
        ArrayList<NvAsset> usableAsset = getAssetsDataList(mCaptionStyleType);
        String jsonBundlePath =  "captionstyle/info.json";
        ArrayList<ParseJsonFile.FxJsonFileInfo.JsonFileInfo> infoLists = ParseJsonFile.readBundleFxJsonFile(this,jsonBundlePath);
        if(infoLists != null) {
            for (ParseJsonFile.FxJsonFileInfo.JsonFileInfo jsonFileInfo : infoLists) {
                for (NvAsset asset : usableAsset) {
                    if(asset == null)
                        continue;
                    if(TextUtils.isEmpty(asset.uuid))
                        continue;

                    //assets路径下的字幕样式包
                    if(asset.isReserved && asset.uuid.equals(jsonFileInfo.getFxPackageId())){
                        asset.name = jsonFileInfo.getName();
                        asset.aspectRatio = Integer.parseInt(jsonFileInfo.getFitRatio());
                        StringBuilder coverPath = new StringBuilder("file:///android_asset/captionstyle/");
                        coverPath.append(jsonFileInfo.getImageName());
                        asset.coverUrl = coverPath.toString();
                    }
                }
            }
        }

        int ratio = TimelineData.instance().getMakeRatio();
        for (NvAsset asset : usableAsset) {
            if (asset == null)
                continue;
            if (TextUtils.isEmpty(asset.uuid))
                continue;
            if((ratio & asset.aspectRatio) == 0)
                continue;//制作比例不适配，不加载
            AssetItem assetItem = new AssetItem();
            assetItem.setAsset(asset);
            assetItem.setAssetMode(AssetItem.ASSET_LOCAL);
            mTotalCaptionStyleList.add(assetItem);
        }
        AssetItem assetItem = new AssetItem();
        NvAsset asset = new NvAsset();
        asset.name = "无";
        assetItem.setImageRes(R.mipmap.captionstyle_no);
        assetItem.setAssetMode(AssetItem.ASSET_NONE);
        assetItem.setAsset(asset);
        mTotalCaptionStyleList.add(0,assetItem);
    }

    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                seekTimeline(BackupData.instance().getCurSeekTimelinePos());
                selectCaption();
                mCaptionAssetFinish.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVideoFragment.setCurCaption(mCurAddCaption);
                        mVideoFragment.updateCaptionCoordinate(mCurAddCaption);
                        mVideoFragment.changeCaptionRectVisible();
                        if(mCurAddCaption != null){
                            int alignVal = mCurAddCaption.getTextAlignment();
                            mVideoFragment.setAlignIndex(alignVal);
                        }
                    }
                }, 100);
            }
        });
        mVideoFragment.setTimeline(mTimeline);
        //设置贴纸模式
        mVideoFragment.setEditMode(Constants.EDIT_MODE_CAPTION);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight",mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight",mBottomLayout.getLayoutParams().height);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        bundle.putBoolean("playBarVisible",false);
        mVideoFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.spaceLayout, mVideoFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mVideoFragment);
    }

    //获取下载到手机路径下的素材，包括assets路径下自带的素材
    private ArrayList<NvAsset> getAssetsDataList(int assetType){
        return mAssetManager.getUsableAssets(assetType,NvAsset.AspectRatio_All,0);
    }

    //获取字体数据列表
    private ArrayList<NvAsset> getFontAssetsDataList(){
        return mAssetManager.getRemoteAssetsWithPage(mFontType, NvAsset.AspectRatio_All, 0,0,10);
    }
    private void assetDataRequest(){
        //网络请求
        mAssetManager.downloadRemoteAssetsInfo(mFontType, NvAsset.AspectRatio_All, 0,0, 10);
        mAssetManager.setManagerlistener(new NvAssetManager.NvAssetManagerListener() {
            @Override
            public void onRemoteAssetsChanged(boolean hasNext) {
                m_handler.sendEmptyMessage(ASSET_LIST_REQUEST_SUCCESS);
            }

            @Override
            public void onGetRemoteAssetsFailed() {
                m_handler.sendEmptyMessage(ASSET_LIST_REQUEST_FAILED);
            }

            @Override
            public void onDownloadAssetProgress(String uuid, int progress) {
                sendHandleMsg(uuid,ASSET_DOWNLOAD_START_TIMER);
            }

            @Override
            public void onDonwloadAssetFailed(String uuid) {
                sendHandleMsg(uuid,ASSET_DOWNLOAD_FAILED);
            }

            @Override
            public void onDonwloadAssetSuccess(String uuid) {
                sendHandleMsg(uuid,ASSET_DOWNLOAD_SUCCESS);
            }

            @Override
            public void onFinishAssetPackageInstallation(String uuid) {

            }

            @Override
            public void onFinishAssetPackageUpgrading(String uuid) {

            }
        });
    }

    private void sendHandleMsg(String uuid,int what){
        Message sendMsg = m_handler.obtainMessage();
        if(sendMsg == null)
            sendMsg = new Message();
        sendMsg.what = what;
        sendMsg.obj = uuid;
        m_handler.sendMessage(sendMsg);
    }

    private void applyCaptionFont(String fontPath){
        if(mCurAddCaption != null){
            mCurAddCaption.setFontByFilePath(fontPath);
            int index = getCaptionIndex(mCurCaptionZVal);
            if(index >= 0)
                mCaptionDataListClone.get(index).setCaptionFont(fontPath);
            updateCaption();
        }
    }

    private void initCaptionColorList(){
        for(int index = 0;index < CaptionColors.length;++index){
            mCaptionColorList.add(new CaptionColorInfo(CaptionColors[index],false));
        }
    }
    private void initCaptionOutlineColorList(){
        for(int index = 0;index < CaptionColors.length;++index){
            mCaptionOutlineColorList.add(new CaptionColorInfo(CaptionColors[index],false));
        }
        mCaptionOutlineColorList.add(0,new CaptionColorInfo("",false));
    }
    private void initCaptionFontList(){
        mCaptionFontList.clear();
        AssetItem noneFontInfo = new AssetItem();
        noneFontInfo.setAsset(new NvAsset());
        noneFontInfo.setImageRes(R.mipmap.captionstyle_no);
        noneFontInfo.setAssetMode(AssetItem.ASSET_NONE);
        mCaptionFontList.add(noneFontInfo);

        ArrayList<NvAsset> usableAsset = getFontAssetsDataList();
        for (int index = 0;index < usableAsset.size();++index){
            AssetItem localFontInfo = new AssetItem();
            localFontInfo.setAsset(usableAsset.get(index));
            localFontInfo.setAssetMode(AssetItem.ASSET_LOCAL);
            mCaptionFontList.add(localFontInfo);
        }
    }
    
    private CaptionStyleFragment initCaptionStyleFragment(){
        CaptionStyleFragment captionStyleFragment = new CaptionStyleFragment();
        captionStyleFragment.setAssetInfolist(mTotalCaptionStyleList);
        captionStyleFragment.setCaptionStyleListener(new CaptionStyleFragment.OnCaptionStyleListener() {
            @Override
            public void onFragmentLoadFinished(){
                mCaptionStyleFragment.applyToAllCaption(bIsStyleUuidApplyToAll);
                mSelectedStylePos = getCaptionStyleSelectedIndex();
                mCaptionStyleFragment.setSelectedPos(mSelectedStylePos);
                mCaptionStyleFragment.notifyDataSetChanged();
            }
            @Override
            public void OnDownloadCaptionStyle() {
                if(m_waitFlag)
                    return;
                mVideoFragment.stopEngine();
                Bundle bundle = new Bundle();
                bundle.putInt("titleResId",R.string.moreCaptionStyle);
                bundle.putInt("assetType",NvAsset.ASSET_CAPTION_STYLE);
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), AssetDownloadActivity.class, bundle,CAPTIONSTYLEREQUESTLIST);
                m_waitFlag = true;
            }

            @Override
            public void onItemClick(int pos) {
                if(pos < 0 || pos >= mTotalCaptionStyleList.size())
                    return;
                if(mCurAddCaption == null)
                    return;
                isCaptionStyleItemClick = true;
                long startTime = mCurAddCaption.getInPoint();
                long endTime = mCurAddCaption.getOutPoint();
                mVideoFragment.setDrawRectVisible(View.GONE);
                if(mSelectedStylePos == pos){
                    mVideoFragment.playVideo(startTime,endTime);
                    return;
                }
                NvAsset asset = mTotalCaptionStyleList.get(pos).getAsset();
                if(asset == null)
                    return;
                mSelectedStylePos = pos;
                //设置字幕样式
                mCurAddCaption.applyCaptionStyle(asset.uuid);
                mVideoFragment.playVideo(startTime,endTime);
                float captionSize = mCurAddCaption.getFontSize();
                float scaleX = mCurAddCaption.getScaleX();
                float scaleY = mCurAddCaption.getScaleY();
                PointF pointF = mCurAddCaption.getCaptionTranslation();
                float rotateAngle = mCurAddCaption.getRotationZ();
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0){
                    mCaptionDataListClone.get(index).setCaptionStyleUuid(asset.uuid);
                    mCaptionDataListClone.get(index).setTranslation(pointF);
                    mCaptionDataListClone.get(index).setCaptionSize(captionSize);
                    mCaptionDataListClone.get(index).setScaleFactorX(scaleX);
                    mCaptionDataListClone.get(index).setScaleFactorY(scaleY);
                    mCaptionDataListClone.get(index).setRotation(rotateAngle);
                }
            }

            @Override
            public void onIsApplyToAll(boolean isApplyToAll) {
                bIsStyleUuidApplyToAll = isApplyToAll;
            }
        });
        return captionStyleFragment;
    }

    private CaptionColorFragment initCaptionColorFragment(){
        CaptionColorFragment captionColorFragment = new CaptionColorFragment();
        captionColorFragment.setCaptionColorInfolist(mCaptionColorList);
        captionColorFragment.setCaptionColorListener(new CaptionColorFragment.OnCaptionColorListener() {
            @Override
            public void onFragmentLoadFinished(){
                mCaptionColorFragment.applyToAllCaption(bIsCaptionColorApplyToAll);
                mSelectedColorPos = getCaptionColorSelectedIndex();
                if(mSelectedColorPos >= 0) {
                    mCaptionColorList.get(mSelectedColorPos).mSelected = true;
                    mCaptionColorFragment.setCaptionColorInfolist(mCaptionColorList);
                    mCaptionColorFragment.notifyDataSetChanged();
                }
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0) {
                    mCaptionColorOpacityValue = mCaptionDataListClone.get(index).getCaptionColorAlpha();
                    mCaptionColorFragment.updateCaptionOpacityValue(mCaptionColorOpacityValue);
                }
            }
            @Override
            public void onCaptionColor(int pos) {
                if(pos < 0 || pos > mCaptionColorList.size())
                    return;
                if(mCurAddCaption == null)
                    return;
                if(mSelectedColorPos == pos)
                    return;
                if(mSelectedColorPos >= 0)
                    mCaptionColorList.get(mSelectedColorPos).mSelected = false;
                mCaptionColorList.get(pos).mSelected = true;
                mCaptionColorFragment.notifyDataSetChanged();
                mSelectedColorPos = pos;
                mCaptionColorOpacityValue = 100;
                mCaptionColorFragment.updateCaptionOpacityValue(mCaptionColorOpacityValue);
                // 设置字体颜色
                NvsColor color = ColorUtil.colorStringtoNvsColor(mCaptionColorList.get(pos).mColorValue);
                mCurAddCaption.setTextColor(color);
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0) {
                    mCaptionDataListClone.get(index).setUsedColorFlag(CaptionInfo.ATTRIBUTE_USED_FLAG);
                    mCaptionDataListClone.get(index).setCaptionColor(mCaptionColorList.get(pos).mColorValue);
                }
                updateCaption();
            }

            @Override
            public void onCaptionOpacity(int progress) {
                if(mCurAddCaption == null)
                    return;

                // 设置字体的不透明度
                NvsColor curColor = mCurAddCaption.getTextColor();
                curColor.a = progress / 100.0f;
                String strColor = ColorUtil.nvsColorToHexString(curColor);
                mCurAddCaption.setTextColor(curColor);
                mCaptionColorOpacityValue = progress;
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0){
                    mCaptionDataListClone.get(index).setUsedColorFlag(CaptionInfo.ATTRIBUTE_USED_FLAG);
                    mCaptionDataListClone.get(index).setCaptionColor(strColor);
                    mCaptionDataListClone.get(index).setCaptionColorAlpha(progress);
                }

                updateCaption();
            }

            @Override
            public void onIsApplyToAll(boolean isApplyToAll) {
                bIsCaptionColorApplyToAll = isApplyToAll;
            }
        });
        return captionColorFragment;
    }

    private CaptionOutlineFragment initCaptionOutlineFragment(){
        CaptionOutlineFragment captionOutlineFragment = new CaptionOutlineFragment();
        captionOutlineFragment.setCaptionOutlineInfolist(mCaptionOutlineColorList);
        captionOutlineFragment.setCaptionOutlineListener(new CaptionOutlineFragment.OnCaptionOutlineListener() {
            @Override
            public void onFragmentLoadFinished(){
                mCaptionOutlineFragment.applyToAllCaption(bIsOutlineApplyToAll);
                mSelectedOutlinePos = getOutlineColorSelectedIndex();
                mCaptionOutlineColorList.get(mSelectedOutlinePos).mSelected = true;
                mCaptionOutlineFragment.setCaptionOutlineInfolist(mCaptionOutlineColorList);
                mCaptionOutlineFragment.notifyDataSetChanged();
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0) {
                    boolean isDrawOutline = mCaptionDataListClone.get(index).isHasOutline();
                    if (isDrawOutline){
                        mCaptionOutlineWidthValue = (int) mCaptionDataListClone.get(index).getOutlineWidth();
                        mCaptionOutlineOpacityValue = mCaptionDataListClone.get(index).getOutlineColorAlpha();
                    }
                    mCaptionOutlineFragment.updateCaptionOutlineWidthValue(mCaptionOutlineWidthValue);
                    mCaptionOutlineFragment.updateCaptionOutlineOpacityValue(mCaptionOutlineOpacityValue);
                }
            }
            @Override
            public void onCaptionOutlineColor(int pos) {
                if(pos < 0 || pos > mCaptionOutlineColorList.size())
                    return;
                if(mCurAddCaption == null)
                    return;
                if(mSelectedOutlinePos == pos)
                    return;
                mCaptionOutlineColorList.get(mSelectedOutlinePos).mSelected = false;
                mCaptionOutlineColorList.get(pos).mSelected = true;
                mCaptionOutlineFragment.notifyDataSetChanged();
                mSelectedOutlinePos = pos;

                mCaptionOutlineOpacityValue = 100;
                int index = getCaptionIndex(mCurCaptionZVal);
                // 设置字体颜色
                if(pos == 0){
                    mCurAddCaption.setDrawOutline(false);
                    mCaptionOutlineWidthValue = 0;
                    if(index >= 0) {
                        mCaptionDataListClone.get(index).setUsedOutlineFlag(CaptionInfo.ATTRIBUTE_USED_FLAG);
                        mCaptionDataListClone.get(index).setHasOutline(false);
                    }

                }else {
                    mCaptionOutlineWidthValue = 8;
                    mCurAddCaption.setDrawOutline(true);//字幕描边
                    // 设置描边颜色
                    NvsColor color = ColorUtil.colorStringtoNvsColor(mCaptionOutlineColorList.get(pos).mColorValue);
                    mCurAddCaption.setOutlineColor(color);
                    mCurAddCaption.setOutlineWidth(mCaptionOutlineWidthValue); //字幕描边宽度
                    if(index >= 0){
                        mCaptionDataListClone.get(index).setUsedOutlineFlag(CaptionInfo.ATTRIBUTE_USED_FLAG);
                        mCaptionDataListClone.get(index).setHasOutline(true);
                        mCaptionDataListClone.get(index).setOutlineColor(mCaptionOutlineColorList.get(pos).mColorValue);
                        mCaptionDataListClone.get(index).setOutlineWidth(mCaptionOutlineWidthValue);
                        mCaptionDataListClone.get(index).setOutlineColorAlpha(mCaptionOutlineOpacityValue);
                    }
                }
                mCaptionOutlineFragment.updateCaptionOutlineWidthValue(mCaptionOutlineWidthValue);
                mCaptionOutlineFragment.updateCaptionOutlineOpacityValue(mCaptionOutlineOpacityValue);
                updateCaption();
            }

            @Override
            public void onCaptionOutlineWidth(int width) {
                if(mCurAddCaption == null)
                    return;
                if(mSelectedOutlinePos == 0)
                    return;
                mCurAddCaption.setOutlineWidth(width); //字幕描边宽度
                mCaptionOutlineWidthValue = width;
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0)
                    mCaptionDataListClone.get(index).setOutlineWidth(mCaptionOutlineWidthValue);
                updateCaption();
            }

            @Override
            public void onCaptionOutlineOpacity(int opacity) {
                if(mCurAddCaption == null)
                    return;
                if(mSelectedOutlinePos == 0)
                    return;
                // 设置描边的不透明度
                NvsColor curColor = mCurAddCaption.getOutlineColor();
                curColor.a = opacity / 100.0f;
                mCurAddCaption.setOutlineColor(curColor);
                mCaptionOutlineOpacityValue = opacity;
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0)
                    mCaptionDataListClone.get(index).setOutlineColorAlpha(opacity);
                updateCaption();
            }

            @Override
            public void onIsApplyToAll(boolean isApplyToAll) {
                bIsOutlineApplyToAll = isApplyToAll;
            }
        });
        return captionOutlineFragment;
    }
    private CaptionFontFragment initCaptionFontFragment(){
        CaptionFontFragment captionFontFragment = new CaptionFontFragment();
        captionFontFragment.setCaptionFontListener(new CaptionFontFragment.OnCaptionFontListener() {
            @Override
            public void onFragmentLoadFinished(){
                updateFontList();
                mCaptionFontFragment.applyToAllCaption(bIsFontApplyToAll);
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0){
                    mCaptionFontFragment.updateBoldButton(mCaptionDataListClone.get(index).isBold());
                    mCaptionFontFragment.updateItalicButton(mCaptionDataListClone.get(index).isItalic());
                    mCaptionFontFragment.updateShadowButton(mCaptionDataListClone.get(index).isShadow());
                }
            }
            @Override
            public void onItemClick(int pos) {
                if(pos < 0 || pos >= mCaptionFontList.size())
                    return;
                mSelectedFontPos = pos;
                NvAsset asset = mCaptionFontList.get(pos).getAsset();
                if(asset == null)
                    return;
                applyCaptionFont(asset.localDirPath);
            }

            @Override
            public void onBold() {
                if(mCurAddCaption == null)
                    return;
                boolean isBold = mCurAddCaption.getBold();
                isBold = !isBold;
                mCurAddCaption.setBold(isBold); //加粗
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0) {
                    mCaptionDataListClone.get(index).setUsedIsItalicFlag(CaptionInfo.ATTRIBUTE_USED_FLAG);
                    mCaptionDataListClone.get(index).setBold(isBold);
                }
                updateCaption();
            }

            @Override
            public void onItalic() {
                if(mCurAddCaption == null)
                    return;
                boolean isItalic = mCurAddCaption.getItalic();
                isItalic = !isItalic;
                mCurAddCaption.setItalic(isItalic); // 斜体
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0) {
                    mCaptionDataListClone.get(index).setUsedIsItalicFlag(CaptionInfo.ATTRIBUTE_USED_FLAG);
                    mCaptionDataListClone.get(index).setItalic(isItalic);
                }
                updateCaption();
            }

            @Override
            public void onShadow() {
                if(mCurAddCaption == null)
                    return;
                boolean isShadow = mCurAddCaption.getDrawShadow();
                isShadow = !isShadow;
                if(isShadow){
                    PointF point = new PointF(7, -7);
                    NvsColor shadowColor = new NvsColor(0, 0, 0, 0.5f);
                    mCurAddCaption.setShadowOffset(point);  //字幕阴影偏移量
                    mCurAddCaption.setShadowColor(shadowColor); // 字幕阴影颜色
                }
                mCurAddCaption.setDrawShadow(isShadow);

                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0) {
                    mCaptionDataListClone.get(index).setUsedShadowFlag(CaptionInfo.ATTRIBUTE_USED_FLAG);
                    mCaptionDataListClone.get(index).setShadow(isShadow);
                }
                updateCaption();
            }

            @Override
            public void onIsApplyToAll(boolean isApplyToAll) {
                bIsFontApplyToAll = isApplyToAll;
            }

            @Override
            public void onFontDownload(int pos) {
                int count = mCaptionFontList.size();
                if(pos <= 0 || pos >= count)
                    return;
                if (mFontCurClickPos == pos)
                    return;////重复点击，不作处理防止素材多次下载
                mFontCurClickPos = pos;
                mAssetManager.downloadAsset(mFontType,mCaptionFontList.get(pos).getAsset().uuid);
            }
        });
        return captionFontFragment;
    }
    private CaptionSizeFragment initCaptionSizeFragment(){
        CaptionSizeFragment captionSizeFragment = new CaptionSizeFragment();
        captionSizeFragment.setCaptionSizeListener(new CaptionSizeFragment.OnCaptionSizeListener() {
            @Override
            public void onFragmentLoadFinished(){
                mCaptionSizeFragment.applyToAllCaption(bIsSizeApplyToAll);
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0){
                    int captionSizeVal = (int)mCaptionDataListClone.get(index).getCaptionSize();
                    if(captionSizeVal >= 0)
                        mCaptionSizeValue = captionSizeVal;
                    mCaptionSizeFragment.updateCaptionSizeValue(mCaptionSizeValue);
                }
            }
            @Override
            public void OnCaptionSize(int size) {
                if(mCurAddCaption == null)
                    return;
                mCurAddCaption.setFontSize(size);
                mCaptionSizeValue = size;
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0)
                    mCaptionDataListClone.get(index).setCaptionSize(size);
                updateCaption();
            }

            @Override
            public void onIsApplyToAll(boolean isApplyToAll) {
                bIsSizeApplyToAll = isApplyToAll;
            }
        });
        return captionSizeFragment;
    }

    private CaptionPositionFragment initCaptionPositionFragment(){
        CaptionPositionFragment captionPositionFragment = new CaptionPositionFragment();
        captionPositionFragment.setCaptionPostionListener(new CaptionPositionFragment.OnCaptionPositionListener() {
            @Override
            public void onFragmentLoadFinished(){
                mCaptionPositionFragment.applyToAllCaption(bIsPositionApplyToAll);
            }
            @Override
            public void OnAlignLeft() {
                if(mCurAddCaption == null)
                    return;

                List<PointF> list = mCurAddCaption.getBoundingRectangleVertices();
                if(list == null || list.size() < 4)
                    return;
                Collections.sort(list, new Util.PointXComparator());

                float xOffset = -(mTimeline.getVideoRes().imageWidth/2 + list.get(0).x);
                mCurAddCaption.translateCaption(new PointF(xOffset,0));
                mAlignType = CAPTION_ALIGNLEFT;
                updateCaption();
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0)
                    mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
            }

            @Override
            public void OnAlignCenterHorizontal() {
                if(mCurAddCaption == null)
                    return;

                List<PointF> list = mCurAddCaption.getBoundingRectangleVertices();
                if(list == null || list.size() < 4)
                    return;
                Collections.sort(list, new Util.PointXComparator());

                float xOffset = -((list.get(3).x - list.get(0).x)/2 + list.get(0).x);
                mCurAddCaption.translateCaption(new PointF(xOffset,0));
                updateCaption();
                mAlignType = CAPTION_ALIGNHORIZCENTER;
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0)
                    mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
            }

            @Override
            public void OnAlignRight() {
                if(mCurAddCaption == null)
                    return;

                List<PointF> list = mCurAddCaption.getBoundingRectangleVertices();
                if(list == null || list.size() < 4)
                    return;
                Collections.sort(list, new Util.PointXComparator());

                float xOffset = mTimeline.getVideoRes().imageWidth/2 - list.get(3).x;
                mCurAddCaption.translateCaption(new PointF(xOffset,0));
                updateCaption();
                mAlignType = CAPTION_ALIGNRIGHT;
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0)
                    mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
            }

            @Override
            public void OnAlignTop() {
                if(mCurAddCaption == null)
                    return;

                List<PointF> list = mCurAddCaption.getBoundingRectangleVertices();
                if(list == null || list.size() < 4)
                    return;
                Collections.sort(list, new Util.PointYComparator());
                float y_dis = list.get(3).y - list.get(0).y;

                float yOffset = mTimeline.getVideoRes().imageHeight/2 - list.get(0).y - y_dis;
                mCurAddCaption.translateCaption(new PointF(0,yOffset));
                updateCaption();
                mAlignType = CAPTION_ALIGNTOP;
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0)
                    mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
            }

            @Override
            public void OnAlignCenterVertical() {
                if(mCurAddCaption == null)
                    return;
                List<PointF> list = mCurAddCaption.getBoundingRectangleVertices();
                if(list == null || list.size() < 4)
                    return;
                Collections.sort(list, new Util.PointYComparator());

                float yOffset = -((list.get(3).y - list.get(0).y)/2 + list.get(0).y);
                mCurAddCaption.translateCaption(new PointF(0,yOffset));
                updateCaption();
                mAlignType = CAPTION_ALIGNVERTCENTER;
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0)
                    mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
            }

            @Override
            public void OnAlignBottom() {
                if(mCurAddCaption == null)
                    return;

                List<PointF> list = mCurAddCaption.getBoundingRectangleVertices();
                if(list == null || list.size() < 4)
                    return;
                Collections.sort(list, new Util.PointYComparator());
                float y_dis = list.get(3).y - list.get(0).y;

                float yOffset = -(mTimeline.getVideoRes().imageHeight/2 + list.get(3).y - y_dis);
                mCurAddCaption.translateCaption(new PointF(0,yOffset));
                updateCaption();
                mAlignType = CAPTION_ALIGNBOTTOM;
                int index = getCaptionIndex(mCurCaptionZVal);
                if(index >= 0)
                    mCaptionDataListClone.get(index).setTranslation(mCurAddCaption.getCaptionTranslation());
            }
            @Override
            public void onIsApplyToAll(boolean isApplyToAll) {
                bIsPositionApplyToAll = isApplyToAll;
            }
        });
        return captionPositionFragment;
    }

    private int getCaptionStyleSelectedIndex(){
        int selectIndex = 0;
        if(mCurAddCaption != null){
            String uuid = mCurAddCaption.getCaptionStylePackageId();
            for (int index = 0;index < mTotalCaptionStyleList.size();++index){
                NvAsset asset = mTotalCaptionStyleList.get(index).getAsset();
                if(asset == null)
                    continue;
                if (asset.uuid.compareTo(uuid) == 0){
                    selectIndex = index;
                    break;
                }
            }
        }

        return selectIndex;
    }

    private int getCaptionColorSelectedIndex(){
        int selectedPos = -1;
        int captionIndex = getCaptionIndex(mCurCaptionZVal);
        if(captionIndex >= 0){
            String captionColor = mCaptionDataListClone.get(captionIndex).getCaptionColor();
            for (int i = 0;i < mCaptionColorList.size();++i){
                if(mCaptionColorList.get(i).mColorValue.compareTo(captionColor) == 0){
                    selectedPos = i;
                    break;
                }
            }
        }
        return selectedPos;
    }
    private int getOutlineColorSelectedIndex(){
        int selectedPos = 0;
        int captionIndex = getCaptionIndex(mCurCaptionZVal);
        if(captionIndex >= 0){
            String outlineColor = mCaptionDataListClone.get(captionIndex).getOutlineColor();
            for (int i = 0;i < mCaptionOutlineColorList.size();++i){
                if(mCaptionOutlineColorList.get(i).mColorValue.compareTo(outlineColor) == 0){
                    selectedPos = i;
                    break;
                }
            }
        }
        return selectedPos;
    }

    private int getCaptionFontSelectedIndex(){
        int selectedPos = 0;
        int captionIndex = getCaptionIndex(mCurCaptionZVal);
        if(captionIndex >= 0){
            String captionFont = mCaptionDataListClone.get(captionIndex).getCaptionFont();
            for (int i = 0;i < mCaptionFontList.size();++i){
                NvAsset asset = mCaptionFontList.get(i).getAsset();
                if(asset == null)
                    continue;
                if(TextUtils.isEmpty(asset.localDirPath))
                    continue;
                if(asset.localDirPath.compareTo(captionFont) == 0){
                    selectedPos = i;
                    break;
                }
            }
        }
        return selectedPos;
    }

    private void updateCaption(){
        seekTimeline(mCurAddCaption.getInPoint());
        updateDrawRect();
    }

    private void updateDrawRect(){
        if(mCurAddCaption != null){
            int alignVal = mCurAddCaption.getTextAlignment();
            mVideoFragment.setAlignIndex(alignVal);
        }
        mVideoFragment.updateCaptionCoordinate(mCurAddCaption);
        mVideoFragment.changeCaptionRectVisible();
    }

    private void seekTimeline(long timestamp){
        mVideoFragment.seekTimeline(timestamp,NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
    }
    private int getCaptionIndex(int curZValue){
        int index = -1;
        int count = mCaptionDataListClone.size();
        for (int i = 0;i < count;++i){
            int zVal = mCaptionDataListClone.get(i).getCaptionZVal();
            if(curZValue == zVal){
                index = i;
                break;
            }
        }
        return index;
    }

    private void selectCaption(){
        long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        List<NvsTimelineCaption> captionList = mTimeline.getCaptionsByTimelinePosition(curPos);
        int captionCount = captionList.size();
        for(int index = 0; index < captionCount; index++){
            int tmpZVal = (int)captionList.get(index).getZValue();
            if(mCurCaptionZVal ==  tmpZVal){
                mCurAddCaption = captionList.get(index);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_waitFlag = false;
    }
}
