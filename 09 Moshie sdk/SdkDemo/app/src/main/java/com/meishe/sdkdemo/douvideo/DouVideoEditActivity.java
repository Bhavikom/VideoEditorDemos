package com.meishe.sdkdemo.douvideo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsAudioClip;
import com.meicam.sdk.NvsAudioResolution;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoStreamInfo;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BasePermissionActivity;
import com.meishe.sdkdemo.douvideo.adapter.FilterFxAdapter;
import com.meishe.sdkdemo.douvideo.adapter.TimelineFxAdapter;
import com.meishe.sdkdemo.douvideo.bean.FilterFxInfo;
import com.meishe.sdkdemo.douvideo.bean.FilterJsonFileInfo;
import com.meishe.sdkdemo.douvideo.bean.RecordClip;
import com.meishe.sdkdemo.douvideo.bean.RecordClipsInfo;
import com.meishe.sdkdemo.douvideo.bean.TimelineFxResourceObj;
import com.meishe.sdkdemo.edit.CompileVideoFragment;
import com.meishe.sdkdemo.edit.adapter.SpaceItemDecoration;
import com.meishe.sdkdemo.edit.data.AssetItem;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.PathUtils;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.ToastUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;
import com.meishe.sdkdemo.utils.dataInfo.MusicInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.meishe.sdkdemo.utils.Constants.ASSET_DOWNLOAD_FAILED;
import static com.meishe.sdkdemo.utils.Constants.ASSET_DOWNLOAD_INPROGRESS;
import static com.meishe.sdkdemo.utils.Constants.ASSET_DOWNLOAD_START_TIMER;
import static com.meishe.sdkdemo.utils.Constants.ASSET_DOWNLOAD_SUCCESS;
import static com.meishe.sdkdemo.utils.Constants.ASSET_LIST_REQUEST_FAILED;
import static com.meishe.sdkdemo.utils.Constants.ASSET_LIST_REQUEST_SUCCESS;
import static com.meishe.sdkdemo.utils.Constants.FilterColors;

public class DouVideoEditActivity extends BasePermissionActivity implements NvsStreamingContext.StreamingEngineCallback,
        NvsStreamingContext.PlaybackCallback, NvsStreamingContext.PlaybackCallback2 , View.OnClickListener{
    private static final String TAG = "DouVideoEditActivity";

    public static final int TIMELINE_FX_MODE_NONE = 0;
    public static final int TIMELINE_FX_MODE_REVERSE = 1;
    public static final int TIMELINE_FX_MODE_REPEAT = 2;
    public static final int TIMELINE_FX_MODE_SLOW = 3;

    public static final long MAXTIME = 60000000L*60*10;
    private static final long MINTIME = 4000000;

    private static final int TIME_BASE = 1000000;
    private static final int MESSAGE_REPLAY_TIMELINE = 1;
    public static final int MESSAGE_ALL_VIDEO_CONVERT_FINISHED = 2;

    private NvsLiveWindow mLiveWindow;
    private ImageButton mClostBtn;
    private Button mPlayBtn;
    private NvsMultiThumbnailSequenceView mSequenceView;
    private FxSeekView mFxSeekView;
    private Button mUndoBtn;
    private RecyclerView mFilterFxRecyclerView;
    private FilterFxAdapter mFilterFxAdapter;
    private RecyclerView mTimelineFxRecyclerView;
    private TimelineFxAdapter mTimelineFxAdapter;
    private Button mTimelineFxSelectBtn;
    private Button mFilterFxSelectBtn;
    private TextView mFxTipTxt;
    private AlertDialog mQuitDialog;
    private View mFilterFxLayer;
    private AlphaAnimation mTipTxtShowAni;
    private RelativeLayout mCompilePage;
    private TextView mCompileBtn;
    private CompileVideoFragment mCompileVideoFragment;

    //素材管理对象
    private NvAssetManager mAssetManager;
    private int mFilterType = NvAsset.ASSET_FILTER;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int mFilterClickPos = -1;

    // 录制信息
    RecordClipsInfo mRecordInfo;
    // 转码
    private ConvertFiles mConvertFiles;
    // 是否在等待转码完成
    private boolean mIsWatiCoverting = false;
    // 播放进度值
    private float mProgress;
    // 反复特效位置
    private float mRepeatPosValue = 0.5f;
    // 慢动作特效位置
    private float mSlowPosValue = 0.5f;
    // 反复或者慢动作特效的位置
    private float mTimelinePosValue = -1;
    // 当前选中的时间特效
    private int mTimeFxMode = TIMELINE_FX_MODE_NONE;
    private int mAutoDoTimelineFx = TIMELINE_FX_MODE_NONE;

    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private NvsVideoTrack mVideoTrack;
    private NvsAudioTrack mMusicTrack;

    private EditHandler mHandler;

    // 进入页面之后是否有特效的修改
    private boolean mEffectChanged = false;
    // 记录操作历史，用于撤销操作
    private ArrayList<ArrayList<FilterFxInfo>> mFilterHistoryList;
    // 当前时间线上的滤镜特效信息
    private ArrayList<FilterFxInfo> mCurFilterInfoList;
    // 当前逆向时间线上的滤镜信息
    private ArrayList<FilterFxInfo> mCurReverseFilterInfoList;
    // 当前正在添加的滤镜特效信息
    private FilterFxInfo mCurFilterFxInfo = new FilterFxInfo();


    private ArrayList<AssetItem> mFilterDataInfoList = new ArrayList<>();//滤镜特效数据信息

    // 是否是长按事件
    private boolean isLongPressedEvent = false;
    // 当前是不是时间线特效
    private boolean isTimelineFx = false;
    // 视频时长
    private long mTimelineDuration;

    private static final Gson m_gson = new Gson();
    /**
     * Json转Java对象
     */
    private static <T> T fromJson(String json, Class<T> clz) {
        return m_gson.fromJson(json, clz);
    }

    private DouyinEditHandler m_handler = new DouyinEditHandler(this);
    static class DouyinEditHandler extends Handler
    {
        WeakReference<DouVideoEditActivity> mWeakReference;
        public DouyinEditHandler(DouVideoEditActivity activity)
        {
            mWeakReference= new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg)
        {
            final DouVideoEditActivity activity = mWeakReference.get();
            if(activity != null)
            {
                switch (msg.what) {
                    case ASSET_LIST_REQUEST_SUCCESS:
                        activity.updateFilterDataList();
                        break;
                    case ASSET_LIST_REQUEST_FAILED:
                        activity.updateFilterDataList();
                        activity.filterListRequestFail();
                        break;
                    case ASSET_DOWNLOAD_START_TIMER:
                        activity.startProgressTimer();
                        String progressUuid = (String) msg.obj;
                        activity.filterItemCopy(progressUuid);
                        break;
                    case ASSET_DOWNLOAD_SUCCESS:
                        String successUuid = (String) msg.obj;
                        activity.filterItemCopy(successUuid);
                        activity.updateFilterItem();
                        break;
                    case ASSET_DOWNLOAD_FAILED:
                        activity.filterDownloadFail();
                        activity.updateFilterItem();
                        break;
                    case ASSET_DOWNLOAD_INPROGRESS:
                        activity.updateFilterDownloadProgress();
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
        return R.layout.activity_dou_video_edit;
    }

    @Override
    protected void initViews() {
        mLiveWindow = (NvsLiveWindow) findViewById(R.id.live_window);
        mClostBtn = (ImageButton) findViewById(R.id.close_btn);
        mPlayBtn = (Button) findViewById(R.id.play_button);
        mSequenceView = (NvsMultiThumbnailSequenceView) findViewById(R.id.sequence_view);
        mUndoBtn = (Button) findViewById(R.id.undo_btn);
        mFilterFxRecyclerView = (RecyclerView) findViewById(R.id.filter_fx_recycler_view);
        mTimelineFxRecyclerView = (RecyclerView) findViewById(R.id.time_fx_recycler_view);
        mTimelineFxSelectBtn = (Button) findViewById(R.id.timeline_fx_select_btn);
        mFilterFxSelectBtn = (Button) findViewById(R.id.filter_fx_select_btn);
        mFxTipTxt = (TextView) findViewById(R.id.effect_tip_txt);
        mFxSeekView = (FxSeekView) findViewById(R.id.fx_seek_view);
        mCompilePage = (RelativeLayout) findViewById(R.id.compile_page);
        mCompileBtn = (TextView) findViewById(R.id.compile_btn);
        mCompileBtn.getPaint().setFakeBoldText(true);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mRecordInfo = (RecordClipsInfo) bundle.get("recordInfo");
        mHandler = new EditHandler(this);
        if(hasAllPermission()){
            mConvertFiles = new ConvertFiles(mRecordInfo, PathUtils.getDouYinConvertDir(), mHandler, MESSAGE_ALL_VIDEO_CONVERT_FINISHED, this);
            mConvertFiles.sendConvertFileMsg();
        }


        mFilterHistoryList = new ArrayList<>();
        mCurFilterInfoList = new ArrayList<>();
        mCurReverseFilterInfoList = new ArrayList<>();
        mTimeline = initTimeline(mRecordInfo.getClipList());
        if(mTimeline == null){
            return;
        }

        mTimelineDuration = mTimeline.getDuration();
        mVideoTrack = mTimeline.getVideoTrackByIndex(0);
        removeAllTransition(mVideoTrack);
        mMusicTrack = mTimeline.getAudioTrackByIndex(0);
        addMusic(mRecordInfo.getMusicInfo());
        mStreamingContext.connectTimelineWithLiveWindow(mTimeline, mLiveWindow);
        seekTimeline(mTimeline, 0);

        ViewTreeObserver observer = mLiveWindow.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = mLiveWindow.getWidth();
                int height = mLiveWindow.getHeight();
                NvsSize liveWindowSize = Util.getLiveWindowSize(TimelineUtil.getTimelineSize(mTimeline), new NvsSize(width, height), false);
                ViewGroup.LayoutParams params = mLiveWindow.getLayoutParams();
                params.width = liveWindowSize.width;
                params.height = liveWindowSize.height;
                mLiveWindow.setLayoutParams(params);
                mLiveWindow.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mTipTxtShowAni = new AlphaAnimation(0.0f, 1.0f);
        mTipTxtShowAni.setDuration(200);
        mTipTxtShowAni.setFillAfter(true);

        updateSequenceView();

        searchLocalFilterData();//查询本地滤镜数据，请求网络滤镜数据

        initFilterFxRecyclerView();
        initTimeLineFxRecyclerView();
        initFxSeekView();
        initCompileVideoFragment();

        mFxSeekView.setFilterMode();
        isTimelineFx = false;
    }

    @Override
    protected void initListener() {
        mStreamingContext.setStreamingEngineCallback(this);
        mStreamingContext.setPlaybackCallback2(this);
        mStreamingContext.setPlaybackCallback(this);
        mLiveWindow.setOnClickListener(this);
        mClostBtn.setOnClickListener(this);
        mPlayBtn.setOnClickListener(this);
        mUndoBtn.setOnClickListener(this);
        mFilterFxSelectBtn.setOnClickListener(this);
        mTimelineFxSelectBtn.setOnClickListener(this);
        mCompileBtn.setOnClickListener(this);
        mCompilePage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        if(mCompileVideoFragment != null){
            mCompileVideoFragment.setCompileVideoListener(new CompileVideoFragment.OnCompileVideoListener() {
                @Override
                public void compileProgress(NvsTimeline timeline, int progress) {
                }

                @Override
                public void compileFinished(NvsTimeline timeline) {
                    hideCompilePage();
                }

                @Override
                public void compileFailed(NvsTimeline timeline) {
                    hideCompilePage();
                }

                @Override
                public void compileCompleted(NvsTimeline nvsTimeline, boolean isCanceled) {
                    hideCompilePage();
                }

                @Override
                public void compileVideoCancel() {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mConvertFiles){
            mConvertFiles.onConvertDestory();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!hasAllPermission()){
            clear();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.close_btn:{
                closeActivity();
                break;
            }

            case R.id.play_button:
            case R.id.live_window:{
                if (getStreamingEngineState() != mStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                    startPlay(mTimeline, getCurPlayPos());
                } else {
                    stopPlay();
                }
                break;
            }

            case R.id.undo_btn:{
                removeAllFilterFx(mTimeline);
                long seekPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);

                if(isReverseMode()){
                    if(!mCurReverseFilterInfoList.isEmpty()){
                        int filterInfoCount = mCurReverseFilterInfoList.size();
                        if(filterInfoCount > 1){
                            seekPos = mCurReverseFilterInfoList.get(mCurReverseFilterInfoList.size() - 2).getOutPoint();
                        }else {
                            seekPos = mCurReverseFilterInfoList.get(mCurReverseFilterInfoList.size() - 1).getInPoint();
                        }
                    }

                }else{
                    if(!mCurFilterInfoList.isEmpty()){
                        int filterInfoCount = mCurFilterInfoList.size();
                        if(filterInfoCount > 1){
                            seekPos = mCurFilterInfoList.get(mCurFilterInfoList.size() - 2).getOutPoint();
                        }else {
                            seekPos = mCurFilterInfoList.get(mCurFilterInfoList.size() - 1).getInPoint();
                        }
                    }
                }

                int size = mFilterHistoryList.size();
                if(size < 2){
                    mFilterHistoryList.clear();
                }else{
                    mCurFilterInfoList = mFilterHistoryList.get(size-2);
                    mFilterHistoryList.remove(size-1);
                    mCurReverseFilterInfoList = reverseFilterFx(mCurFilterInfoList, mTimeline.getDuration());
                }

                seekTimeline(mTimeline, seekPos);
                if (mFilterHistoryList.isEmpty()) {
                    mUndoBtn.setVisibility(View.INVISIBLE);
                    mCurFilterInfoList.clear();
                    mCurReverseFilterInfoList.clear();
                }else{
                    if(isReverseMode()){
                        readdFilterFx(mCurReverseFilterInfoList, mTimeline);
                    }else{
                        readdFilterFx(mCurFilterInfoList, mTimeline);
                    }
                }
                mFxSeekView.endAddingFilter(mCurFilterInfoList, mTimeline.getDuration());
                break;
            }

            case R.id.timeline_fx_select_btn:{
                stopPlay();
                mFilterFxRecyclerView.setVisibility(View.INVISIBLE);
                mTimelineFxRecyclerView.setVisibility(View.VISIBLE);
                mTimelineFxSelectBtn.setTextColor(getResources().getColor(R.color.ff4a90e2));
                mFilterFxSelectBtn.setTextColor(Color.WHITE);
                mFxTipTxt.setText(getResources().getText(R.string.timeline_fx_tip));
                mFxSeekView.setFxMode(mTimeFxMode);
                isTimelineFx = true;
                mUndoBtn.setVisibility(View.INVISIBLE);
                break;
            }

            case R.id.filter_fx_select_btn:{
                stopPlay();
                mFilterFxRecyclerView.setVisibility(View.VISIBLE);
                mTimelineFxRecyclerView.setVisibility(View.INVISIBLE);
                mFilterFxSelectBtn.setTextColor(getResources().getColor(R.color.ff4a90e2));
                mTimelineFxSelectBtn.setTextColor(Color.WHITE);
                mFxTipTxt.setText(getResources().getText(R.string.filter_fx_tip));
                if (!mFilterHistoryList.isEmpty()) {
                    mUndoBtn.setVisibility(View.VISIBLE);
                }
                mFxSeekView.setFilterMode();
                isTimelineFx = false;
                if(!mRecordInfo.getIsConvert() && (mAutoDoTimelineFx == TIMELINE_FX_MODE_REPEAT || mAutoDoTimelineFx == TIMELINE_FX_MODE_REVERSE)){
                    mAutoDoTimelineFx = TIMELINE_FX_MODE_NONE;
                    mIsWatiCoverting = false;
                    mTimelineFxAdapter.hideBusy();
                    mTimelineFxAdapter.setSelect(mTimelineFxAdapter.getData().get(TIMELINE_FX_MODE_NONE));
                }
                break;
            }

            case R.id.compile_btn:{
                showCompilePage();
                stopPlay();
                mCompileVideoFragment.compileVideo();
                break;
            }

            default:{
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        closeActivity();
        //super.onBackPressed();
    }


    @Override
    public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {

    }

    @Override
    public void onPlaybackStopped(NvsTimeline nvsTimeline) {

    }

    @Override
    public void onPlaybackEOF(NvsTimeline nvsTimeline) {
        mHandler.sendEmptyMessage(MESSAGE_REPLAY_TIMELINE);
    }

    @Override
    public void onPlaybackTimelinePosition(NvsTimeline nvsTimeline, long curPos) {
        long duration = mTimeline.getDuration();
        if(mTimeFxMode == TIMELINE_FX_MODE_REVERSE) {
            mProgress = (duration - curPos)* 100.f / duration;
        } else {
            mProgress = 100.f * curPos / duration;
        }
        mFxSeekView.setFirstValue(mProgress);
    }

    @Override
    public void onStreamingEngineStateChanged(int state) {
        if (state == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
            mPlayBtn.setVisibility(View.INVISIBLE);
        } else {
            mPlayBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFirstVideoFramePresented(NvsTimeline nvsTimeline) {

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

    private void updateFilterDataList(){
        ArrayList<AssetItem> bundleFilterData = new ArrayList<>();
        for (int i = 0;i < mFilterDataInfoList.size();++i){
            bundleFilterData.add(mFilterDataInfoList.get(i));
        }
        ArrayList<NvAsset>  filterDataList = getFilterAssetsDataList();
        for (int index = 0;index < filterDataList.size();++index){
            NvAsset asset = filterDataList.get(index);
            if(asset == null)
                continue;
            if (TextUtils.isEmpty(asset.uuid))
                continue;
            boolean isDeleteSameUuid = false;
            for (int jj = 0;jj < bundleFilterData.size();++jj){
                String bundleUuid = bundleFilterData.get(jj).getAsset().uuid;
                if (asset.uuid.equals(bundleUuid)){
                    isDeleteSameUuid = true;
                    break;
                }
            }
            if(isDeleteSameUuid)
                continue;
            AssetItem localFilterInfo = new AssetItem();
            localFilterInfo.setAsset(asset);
            localFilterInfo.setAssetMode(AssetItem.ASSET_LOCAL);
            int colorCount = FilterColors.length;
            if(index <= colorCount - 1){
                localFilterInfo.setFilterColor(FilterColors[index]);
            }else {
                int ii = index % colorCount;
                localFilterInfo.setFilterColor(FilterColors[ii]);
            }

            mFilterDataInfoList.add(localFilterInfo);
        }
        if(mFilterFxAdapter != null){
            mFilterFxAdapter.setAssetDataList(mFilterDataInfoList);
            mFilterFxAdapter.notifyDataSetChanged();
        }
    }

    private void filterListRequestFail(){
        ToastUtil.showToast(this,this.getResources().getString(R.string.check_network));
    }

    private void filterDownloadFail(){
        ToastUtil.showToast(this,this.getResources().getString(R.string.download_failed));
    }

    private void updateFilterItem(){
        mFilterFxAdapter.notifyDataSetChanged();
    }
    private void updateFilterDownloadProgress(){
        boolean isDownloadState = false;
        for (int i = 0; i < mFilterDataInfoList.size();++i){
            NvAsset asset = mFilterDataInfoList.get(i).getAsset();
            if(asset == null)
                continue;
            if(asset.downloadStatus == NvAsset.DownloadStatusInProgress
                    || asset.downloadStatus == NvAsset.DownloadStatusPending){
                isDownloadState = true;
            }
        }
        if(isDownloadState){//是下载状态，通知更新数据
            updateFilterItem();
        }
    }

    private void filterItemCopy(String uuid){
        NvAsset curAsset = null;
        ArrayList<NvAsset> usableAsset = getFilterAssetsDataList();
        for (int index = 0;index < usableAsset.size();++index){
            curAsset = usableAsset.get(index);
            if(curAsset == null)
                continue;
            if(!TextUtils.isEmpty(curAsset.uuid) && uuid.equals(curAsset.uuid)){
                break;
            }
        }

        for (int i = 0; i < mFilterDataInfoList.size();++i){
            NvAsset asset = mFilterDataInfoList.get(i).getAsset();
            if(asset == null)
                continue;
            if(curAsset != null &&!TextUtils.isEmpty(asset.uuid) && asset.uuid.equals(uuid)){
                mFilterDataInfoList.get(i).getAsset().copyAsset(curAsset);
            }
        }
    }

    //获取滤镜数据列表
    private ArrayList<NvAsset> getFilterAssetsDataList(){
        return mAssetManager.getRemoteAssetsWithPage(mFilterType, NvAsset.AspectRatio_All, NvAsset.NV_CATEGORY_ID_DOUYINFILTER,0,60);
    }

    private void searchLocalFilterData(){
        mAssetManager = NvAssetManager.sharedInstance();
        mAssetManager.searchLocalAssets(mFilterType);//查找字体文件
        //解析Bundle json滤镜数据信息
        parseJsonFileInfo();
        assetDataRequest();
    }

    private void parseJsonFileInfo(){
        String jsonFilePath = "douvideoeditfilter/fxinfo.json";
        readFilterJsonFileInfo(jsonFilePath,true);
        mFxSeekView.setFilterDataInfoList(mFilterDataInfoList);
    }
    private void readFilterJsonFileInfo(String jsonFilePath,boolean isBundle){
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
            StringBuilder packageIdBuilder = new StringBuilder();
            String bundleFilterDir = "assets:/douvideoeditfilter/";
            StringBuilder videoFxPackagePath = new StringBuilder();
            FilterJsonFileInfo resultInfo = fromJson(strbuilder.toString(), FilterJsonFileInfo.class);
            ArrayList<FilterJsonFileInfo.JsonFileInfo> jsonFileInfoLists = resultInfo.getFxInfoList();
            int count = jsonFileInfoLists.size();
            for (int index = 0;index < count;++index){
                AssetItem assetItem = new AssetItem();
                NvAsset asset = new NvAsset();
                FilterJsonFileInfo.JsonFileInfo jsonFileInfo = jsonFileInfoLists.get(index);
                asset.name = jsonFileInfo.getName();
                StringBuilder videoFxCoverPath = new StringBuilder("file:///android_asset/douvideoeditfilter/");
                videoFxCoverPath.append(jsonFileInfo.getImageName());
                asset.coverUrl = videoFxCoverPath.toString();
                assetItem.setAsset(asset);
                assetItem.setFilterColor(jsonFileInfo.getColor());

                if(jsonFileInfo.getType().equals("package")){
                    packageIdBuilder.setLength(0);
                    videoFxPackagePath.setLength(0);
                    videoFxPackagePath.append(bundleFilterDir);
                    videoFxPackagePath.append(jsonFileInfo.getFxFileName());
                    boolean isSuccess = installAssetPackage(videoFxPackagePath.toString(),packageIdBuilder);
                    assetItem.getAsset().uuid = packageIdBuilder.toString();
                    assetItem.getAsset().bundledLocalDirPath = videoFxPackagePath.toString();
                    assetItem.setAssetMode(AssetItem.ASSET_LOCAL);
                    if(isSuccess){
                        mFilterDataInfoList.add(assetItem);
                    }
                }else {
                    assetItem.getAsset().uuid = jsonFileInfo.getFxFileName();
                    assetItem.getAsset().bundledLocalDirPath = jsonFileInfo.getFxFileName();
                    assetItem.setAssetMode(AssetItem.ASSET_BUILTIN);
                    mFilterDataInfoList.add(assetItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assetDataRequest(){
        //网络请求
        mAssetManager.downloadRemoteAssetsInfo(mFilterType, NvAsset.AspectRatio_All, NvAsset.NV_CATEGORY_ID_DOUYINFILTER,0, 30);
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

    private boolean installAssetPackage(String filterPackageFilePath,StringBuilder packageId){
        int error = mStreamingContext.getAssetPackageManager().installAssetPackage(filterPackageFilePath, null, NvsAssetPackageManager.ASSET_PACKAGE_TYPE_VIDEOFX, true, packageId);
        if (error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR
                || error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
            if(error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED){
                mStreamingContext.getAssetPackageManager().upgradeAssetPackage(filterPackageFilePath, null, NvsAssetPackageManager.ASSET_PACKAGE_TYPE_VIDEOFX, true, packageId);
            }
            return true;
        }else {
            Logger.e(TAG,"Douyin installAssetPackage Failed = " + packageId.toString());
        }
        return false;
    }

    private void initCompileVideoFragment() {
        mCompileVideoFragment = new CompileVideoFragment();
        mCompileVideoFragment.setTimeline(mTimeline);
        getFragmentManager().beginTransaction()
                .add(R.id.compile_page, mCompileVideoFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mCompileVideoFragment);
    }

    private void showCompilePage(){
        mCompilePage.setVisibility(View.VISIBLE);
    }

    private void hideCompilePage(){
        mCompilePage.setVisibility(View.GONE);
    }

    private NvsTimeline initTimeline(ArrayList<RecordClip> clips){
        NvsVideoResolution videoEditRes = Util.getVideoEditResolution(NvAsset.AspectRatio_9v16);
        videoEditRes.imagePAR = new NvsRational(1, 1);
        NvsRational videoFps = new NvsRational(25, 1);

        NvsAudioResolution audioEditRes = new NvsAudioResolution();
        audioEditRes.sampleRate = 44100;
        audioEditRes.channelCount = 2;
        NvsTimeline timeline = mStreamingContext.createTimeline(videoEditRes, videoFps, audioEditRes);
        if(timeline == null){
            Log.e(TAG, "create timeline failed!");
            return null;
        }

        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if(videoTrack == null){
            Log.e(TAG, "append video track failed!");
            return null;
        }

        NvsAudioTrack audioTrack = timeline.appendAudioTrack();
        if(audioTrack == null){
            Log.e(TAG, "append audio track failed!");
            return null;
        }

        int size = clips.size();
        for(int i = 0; i < size; i++){
            RecordClip recordClip = clips.get(i);
            NvsVideoClip videoClip = appendClip(videoTrack,recordClip,false);
            if(videoClip != null)
                localVideoTrim(videoClip,recordClip);
        }
        Logger.e(TAG,"TimeLine Duratoin Normal -->" + timeline.getDuration());
        return timeline;
     }

    private void seekTimeline(NvsTimeline timeline, long timestamp) {
        if(timeline == null)
            return;
        mStreamingContext.seekTimeline(timeline,timestamp, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER | NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_ANIMATED_STICKER_POSTER);
        long duration = timeline.getDuration();
        if(mTimeFxMode == TIMELINE_FX_MODE_REVERSE) {
            mProgress = (duration - timestamp)* 100.f / duration;
        } else {
            mProgress = 100.f * timestamp / duration;
        }
        mFxSeekView.setFirstValue(mProgress);
    }

    private void startPlay(NvsTimeline timeline, long startTime) {
        mStreamingContext.playbackTimeline(timeline, startTime, -1, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
    }
    private void stopPlay(){
        if(getStreamingEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK){
            mStreamingContext.stop();
        }
    }

    private long getPlayPosition() {
        if(mTimeFxMode == TIMELINE_FX_MODE_SLOW || mTimeFxMode == TIMELINE_FX_MODE_REPEAT) {
            long time = (long) (mTimeline.getDuration() * mTimelinePosValue);
            time = time - TIME_BASE;
            if (time < 0)
                time = 0;
            Log.d(TAG, "getPlayPosition: " + time + " mCurTimeline: " + mTimeline.getDuration());
            return time;
        } else {
            return 0;
        }
    }

    private long getCurPlayPos(){
        return mStreamingContext.getTimelineCurrentPosition(mTimeline);
    }

    private int getStreamingEngineState(){
        return mStreamingContext.getStreamingEngineState();
    }

    private void removeAllFilterFx(NvsTimeline timeline){
        NvsTimelineVideoFx nextFx = timeline.removeTimelineVideoFx(timeline.getFirstTimelineVideoFx());
        while (nextFx != null) {
            nextFx = timeline.removeTimelineVideoFx(nextFx);
        }
    }

    private void readdFilterFx(ArrayList<FilterFxInfo> filterInfos, NvsTimeline timeline){
        if (filterInfos == null) {
            return;
        }

        for (int i=0; i<filterInfos.size(); i++) {
            FilterFxInfo filterInfo = filterInfos.get(i);
            long inpoint = filterInfo.getInPoint();
            long duration = filterInfo.getOutPoint() - filterInfo.getInPoint();

            if (filterInfo.getName().equalsIgnoreCase("Video Echo"))
                timeline.addBuiltinTimelineVideoFx(inpoint, duration, filterInfo.getName());
            else {
                timeline.addPackagedTimelineVideoFx(inpoint, duration, filterInfo.getName());
            }
        }
    }

    private void addFilterFxHistory() {
        if (mFilterHistoryList == null) {
            return;
        }
        ArrayList<FilterFxInfo> filterInfos = new ArrayList<FilterFxInfo>();
        for (int i=0; i< mCurFilterInfoList.size(); i++) {
            FilterFxInfo info = mCurFilterInfoList.get(i);
            FilterFxInfo newInfo = new FilterFxInfo(info.getName(), info.getInPoint(), info.getOutPoint());
            filterInfos.add(newInfo);
        }
        mFilterHistoryList.add(filterInfos);
        mUndoBtn.setVisibility(View.VISIBLE);
    }

    private ArrayList<FilterFxInfo> reverseFilterFx(ArrayList<FilterFxInfo> filterInfos, long duration){
        ArrayList<FilterFxInfo> tmpInfos = new ArrayList<FilterFxInfo>();
        for(int i = 0; i < filterInfos.size(); i++){
            FilterFxInfo info = filterInfos.get(i);
            FilterFxInfo tmp = new FilterFxInfo();
            tmp.setName(info.getName());
            tmp.setInPoint(duration - info.getOutPoint());
            tmp.setOutPoint(duration - info.getInPoint());
            tmpInfos.add(tmp);
        }
        return tmpInfos;
    }


    private boolean isReverseMode(){
        return mTimeFxMode == TIMELINE_FX_MODE_REVERSE;
    }

    private void updateSequenceView() {
        final ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> infoDescArray = new ArrayList<>();

        for (int i = 0; i < mVideoTrack.getClipCount(); i++) {
            NvsVideoClip clip = mVideoTrack.getClipByIndex(i);
            if (clip == null)
                continue;

            NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc infoDesc = new NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
            infoDesc.mediaFilePath = clip.getFilePath();
            infoDesc.trimIn = clip.getTrimIn();
            infoDesc.trimOut = clip.getTrimOut();
            infoDesc.inPoint = clip.getInPoint();
            infoDesc.outPoint = clip.getOutPoint();
            infoDesc.stillImageHint = false;
            infoDescArray.add(infoDesc);
        }

        int screenWidth = ScreenUtils.getScreenWidth(this) - ScreenUtils.dip2px(this, 20);
        double duration = (double)mTimeline.getDuration();
        double pixelPerMicrosecond = screenWidth / duration;
        mSequenceView.setPixelPerMicrosecond(pixelPerMicrosecond);
        mSequenceView.setThumbnailSequenceDescArray(infoDescArray);
    }

    private void initFxSeekView() {
        mProgress = 0;
        if(mTimelinePosValue < 0)
            mTimelinePosValue =  0.5f;

        mFxSeekView.setFirstValue(mProgress);
        mFxSeekView.setFxMode(mTimeFxMode);

        if(mTimeFxMode == TIMELINE_FX_MODE_REPEAT) {
            mTimelineFxAdapter.setSelect(mTimelineFxAdapter.getData().get(1));
            mFxSeekView.setSecondValue(mTimelinePosValue * 100);
            mRepeatPosValue = mTimelinePosValue;
        } else if(mTimeFxMode == TIMELINE_FX_MODE_SLOW) {
            mTimelineFxAdapter.setSelect(mTimelineFxAdapter.getData().get(2));
            mFxSeekView.setSecondValue(mTimelinePosValue * 100);
            mSlowPosValue = mTimelinePosValue;
        } else if(mTimeFxMode == TIMELINE_FX_MODE_REVERSE) {
            mTimelineFxAdapter.setSelect(mTimelineFxAdapter.getData().get(3));
        }

        long time = getPlayPosition();
        seekTimeline(mTimeline,time);

        mFxSeekView.setOndataChanged(new FxSeekView.OnDataChangedListener() {
            @Override
            public void onFirstDataChange(float var) {
                long time = (long) (mTimeline.getDuration() * var / 100);
                if(mTimeFxMode == TIMELINE_FX_MODE_REVERSE){
                    seekTimeline(mTimeline, mTimeline.getDuration() - time);
                } else {
                    seekTimeline(mTimeline, time);
                }
            }

            @Override
            public void onSecondDataChange(float var) {
//                Log.e(TAG, "test : " + var);
                mTimelinePosValue = var / 100;
//                Log.e(TAG, "test : " + mTimelinePosValue);
                if(mTimelinePosValue < 0) {
                    mTimelinePosValue = 0;
                }

                if(mTimeFxMode == TIMELINE_FX_MODE_SLOW){
                    mSlowPosValue = mTimelinePosValue;
                } else if(mTimeFxMode == TIMELINE_FX_MODE_REPEAT){
                    mRepeatPosValue = mTimelinePosValue;
                }
                float point = mTimelinePosValue*mTimeline.getDuration();
//                Log.e(TAG, "test : " + mTimelinePosValue + " float");
                stopPlay();
                rebuildTimeline(mTimeline, mVideoTrack, mRecordInfo,
                        false, mTimeFxMode, point);
                long time = (long) (mTimeline.getDuration() * var / 100);
                time = time - TIME_BASE;
                if(time < 0)
                    time = 0;
                startPlay(mTimeline, time);
            }

        });
    }

    private ArrayList<FilterFxInfo>  recalculateFilterFxList(ArrayList<FilterFxInfo> filterFxInfos, FilterFxInfo newFxInfo) {
        if(filterFxInfos == null){
            Log.d(TAG, "recalculateFilterFxList: " + "当前滤镜列表为空");
            return null;
        }

        long inPoint = newFxInfo.getInPoint();
        long outPoint = newFxInfo.getOutPoint();
        ArrayList<FilterFxInfo> tmpList = new ArrayList<FilterFxInfo>();

        for (int i=0; i<filterFxInfos.size(); i++) {
            FilterFxInfo info = filterFxInfos.get(i);
            long tmpInPoint = info.getInPoint();
            long tmpOutPoint = info.getOutPoint();

            if (tmpInPoint < inPoint) {
                if (tmpOutPoint <= inPoint) {
                    FilterFxInfo newInfo = new FilterFxInfo(info.getName(), tmpInPoint, tmpOutPoint);
                    tmpList.add(newInfo);
                } else {
                    FilterFxInfo newInfo = new FilterFxInfo(info.getName(), tmpInPoint, inPoint);
                    tmpList.add(newInfo);
                    if (tmpOutPoint > outPoint) {
                        newInfo = new FilterFxInfo(info.getName(), outPoint, tmpOutPoint);
                        tmpList.add(newInfo);
                    }
                }
            } else if (tmpInPoint < outPoint) {
                if (tmpOutPoint <= outPoint) {
                    // do nothing
                } else {
                    FilterFxInfo newInfo = new FilterFxInfo(info.getName(), outPoint, tmpOutPoint);
                    tmpList.add(newInfo);
                }
            } else {
                FilterFxInfo newInfo = new FilterFxInfo(info.getName(), tmpInPoint, tmpOutPoint);
                tmpList.add(newInfo);
            }
        }

        return tmpList;
    }

    private void initFilterFxRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mFilterFxRecyclerView.setLayoutManager(layoutManager);
        mFilterFxAdapter = new FilterFxAdapter(DouVideoEditActivity.this);
        mFilterFxAdapter.setAssetDataList(mFilterDataInfoList);
        mFilterFxRecyclerView.setAdapter(mFilterFxAdapter);
        int space = ScreenUtils.dip2px(this, 13);
        mFilterFxRecyclerView.addItemDecoration(new SpaceItemDecoration(space, 0));

        mFilterFxAdapter.setOnItemLongPressListener(new FilterFxAdapter.OnItemLongPressListener() {
            @Override
            public void onItemLongPress(View view, int pos) {
                if (mTimeline.getDuration() - mStreamingContext.getTimelineCurrentPosition(mTimeline) < 0.1*TIME_BASE) {
                    return;
                }

                mEffectChanged = true;
                setControllerEnabled(false);
                mFxSeekView.setFirstValue(mProgress);

                mFilterFxLayer = view.findViewById(R.id.layer);
                mFilterFxLayer.setVisibility(View.VISIBLE);
                AssetItem assetItem = mFilterDataInfoList.get(pos);
                startFilterFx(assetItem);
            }
        });

        mFilterFxAdapter.setFilterItemClickListener(new FilterFxAdapter.OnFilterItemClickListener() {
            @Override
            public void onItemDownload(View view, int position) {
                int count = mFilterDataInfoList.size();
                if(position <= 0 || position >= count)
                    return;
                if(mFilterClickPos == position)
                    return;//重复点击，不作处理防止素材多次下载
                mFilterClickPos = position;
                Logger.e(TAG,"onItemDownload" + position);
                mAssetManager.downloadAsset(mFilterType,mFilterDataInfoList.get(position).getAsset().uuid);
            }
        });

        mFilterFxRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                // 完成滤镜特效
                switch (e.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (mFilterFxLayer != null) {
                            mFilterFxLayer.setVisibility(View.GONE);
                        }
                        endFilterFx();
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        if (mFilterFxLayer != null) {
                            mFilterFxLayer.setVisibility(View.GONE);
                        }
                        endFilterFx();
                        break;

                    default:
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    private void startFilterFx(AssetItem assetItem) {
        if(assetItem == null)
            return;
        NvAsset asset = assetItem.getAsset();
        if(asset == null)
            return;
        isLongPressedEvent = true;
        removeAllFilterFx(mTimeline);

        long inPoint = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        long duration = mTimeline.getDuration() - inPoint;
        mCurFilterFxInfo = new FilterFxInfo();
        mCurFilterFxInfo.setInPoint(inPoint);
        if (assetItem.getAssetMode() == AssetItem.ASSET_BUILTIN) {//内建数据类型
            NvsTimelineVideoFx fx = mTimeline.addBuiltinTimelineVideoFx(inPoint, duration, asset.uuid);
            if(fx != null){
                mCurFilterFxInfo.setName(fx.getBuiltinTimelineVideoFxName());
                mCurFilterFxInfo.setAddResult(true);
            }else{
                mCurFilterFxInfo.setAddResult(false);
                Log.e(TAG, "startFilterFx: 添加内建滤镜特效失败! 内建特效名: " + asset.uuid);
                return;
            }
        } else {
            NvsTimelineVideoFx fx = mTimeline.addPackagedTimelineVideoFx(inPoint, duration, asset.uuid);
            if(fx != null){
                mCurFilterFxInfo.setName(fx.getTimelineVideoFxPackageId());
                mCurFilterFxInfo.setAddResult(true);
            }else {
                mCurFilterFxInfo.setAddResult(false);
                Log.e(TAG, "startFilterFx: 添加包裹滤镜特效失败! 包ID: " + asset.uuid);
                return;
            }
        }

        mFxSeekView.addingFilter(asset.uuid);

        int state = mStreamingContext.getStreamingEngineState();
        if(state != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK)//非播放状态，播放视频
            startPlay(mTimeline, getCurPlayPos());
    }

    private void endFilterFx() {
        if(!isLongPressedEvent){
            return;
        }
        stopPlay();
        isLongPressedEvent = false;
        setControllerEnabled(true);

        // 本次滤镜特效添加失败
        if(!mCurFilterFxInfo.getAddResult()){
            return;
        }
        long outPoint = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        if (mTimeline.getDuration()- outPoint < 0.05*TIME_BASE) {
            outPoint = mTimeline.getDuration();
        }
        mCurFilterFxInfo.setOutPoint(outPoint);

        long duration = mTimelineDuration;
        FilterFxInfo reverseInfo = new FilterFxInfo();
        reverseInfo.setName(mCurFilterFxInfo.getName());
        reverseInfo.setInPoint(duration - mCurFilterFxInfo.getOutPoint());
        reverseInfo.setOutPoint(duration - mCurFilterFxInfo.getInPoint());
        if(isReverseMode()){ // 当前是倒放
            mCurReverseFilterInfoList = recalculateFilterFxList(mCurReverseFilterInfoList, mCurFilterFxInfo);
            mCurReverseFilterInfoList.add(mCurFilterFxInfo);

            mCurFilterInfoList = recalculateFilterFxList(mCurFilterInfoList, reverseInfo);
            mCurFilterInfoList.add(reverseInfo);

        }else{ // 当前是正放
            mCurFilterInfoList = recalculateFilterFxList(mCurFilterInfoList, mCurFilterFxInfo);
            mCurFilterInfoList.add(mCurFilterFxInfo);

            mCurReverseFilterInfoList = recalculateFilterFxList(mCurReverseFilterInfoList, reverseInfo);
            mCurReverseFilterInfoList.add(reverseInfo);
        }

        resetFilterFx(mTimeline);
        seekTimeline(mTimeline, getCurPlayPos());
        // 更新FxSeekView
        mFxSeekView.endAddingFilter(mCurFilterInfoList, mTimeline.getDuration());

        // 记录操作
        addFilterFxHistory();
    }

    private void setControllerEnabled(boolean enabled) {
        mLiveWindow.setEnabled(enabled);
        mPlayBtn.setEnabled(enabled);
        mFxSeekView.setEnabled(enabled);
        mClostBtn.setEnabled(enabled);
        mTimelineFxRecyclerView.setEnabled(enabled);
    }

    private void initTimeLineFxRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mTimelineFxRecyclerView.setLayoutManager(layoutManager);
        mTimelineFxAdapter = new TimelineFxAdapter(this, getTimeLineFxData());
        mTimelineFxRecyclerView.setAdapter(mTimelineFxAdapter);
        mTimelineFxAdapter.setSelect(mTimelineFxAdapter.getData().get(0));
        int count = mTimelineFxAdapter.getItemCount();
        int itemsWidth = count * ScreenUtils.dip2px(this, 49);
        int marginWith = ScreenUtils.dip2px(this, 13)* 2;
        int space = (ScreenUtils.getScreenWidth(this) - itemsWidth - marginWith)  / 3;
        mTimelineFxRecyclerView.addItemDecoration(new SpaceItemDecoration(space, 0));

        mTimelineFxAdapter.setOnItemClickListener(new TimelineFxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {

                mEffectChanged = true;
                setControllerEnabled(false);
                TimelineFxResourceObj dataItem = mTimelineFxAdapter.getData().get(pos);
                mTimelineFxAdapter.resetData();
                mTimelineFxAdapter.setSelect(dataItem);
                // 使用时间线特效
                switch (pos) {
                    case TIMELINE_FX_MODE_NONE:
                        mFxSeekView.setFxMode(pos);
                        mProgress = 0;
                        mFxSeekView.setFirstValue(mProgress);
                        mTimeFxMode = pos;
                        rebuildTimeline(mTimeline, mVideoTrack, mRecordInfo, false, mTimeFxMode, mTimelinePosValue*mTimeline.getDuration());
                        startPlay(mTimeline,getPlayPosition());
                        break;

                    case TIMELINE_FX_MODE_REVERSE:

                        if(!mRecordInfo.getIsConvert()){
                            mTimelineFxAdapter.showBusy(dataItem);
                            mIsWatiCoverting = true;
                            mAutoDoTimelineFx = TIMELINE_FX_MODE_REVERSE;
                        }else{
                            mTimeFxMode = pos;
                            mIsWatiCoverting = false;
                            rebuildTimeline(mTimeline, mVideoTrack, mRecordInfo, false, mTimeFxMode, mTimelinePosValue*mTimeline.getDuration());
                            mFxSeekView.setFxMode(pos);
                            mProgress = 100;
                            mFxSeekView.setFirstValue(mProgress);
                            startPlay(mTimeline,getPlayPosition());
                        }

                        break;

                    case TIMELINE_FX_MODE_REPEAT:

                        if(!mRecordInfo.getIsConvert()){
                            mTimelineFxAdapter.showBusy(dataItem);
                            mIsWatiCoverting = true;
                            mAutoDoTimelineFx = TIMELINE_FX_MODE_REPEAT;
                        }else{
                            mIsWatiCoverting = false;
                            mTimeFxMode = pos;
                            mFxSeekView.setFxMode(pos);
                            mTimelinePosValue = mRepeatPosValue;
                            mFxSeekView.setSecondValue(mTimelinePosValue * 100);
                            mProgress = 0;
                            mFxSeekView.setFirstValue(mProgress);
                            rebuildTimeline(mTimeline, mVideoTrack, mRecordInfo, false, mTimeFxMode, mTimelinePosValue*mTimeline.getDuration());
                            startPlay(mTimeline,getPlayPosition());
                        }
                        break;

                    case TIMELINE_FX_MODE_SLOW:
                        mFxSeekView.setFxMode(pos);
                        mTimelinePosValue = mSlowPosValue;
                        mFxSeekView.setSecondValue(mTimelinePosValue * 100);
                        mProgress = 0;
                        mFxSeekView.setFirstValue(mProgress);
                        mTimeFxMode = pos;
                        rebuildTimeline(mTimeline, mVideoTrack, mRecordInfo, false, mTimeFxMode, mTimelinePosValue*mTimeline.getDuration());
                        startPlay(mTimeline,getPlayPosition());
                        break;

                    default:
                        break;
                }
                setControllerEnabled(true);
            }
        });
    }

    public void rebuildTimeline(NvsTimeline timeline,
                                       NvsVideoTrack videoTrack,
                                       RecordClipsInfo clipsInfo,
                                       boolean needRotate,
                                       int fxMode,
                                       float fxPosition) {
        if (timeline == null || videoTrack == null)
            return;

        rebuildTimelineStructure(videoTrack, clipsInfo, fxMode, (long)fxPosition, needRotate);
        resetFilterFx(timeline);
    }

    private void resetFilterFx(NvsTimeline timeline){
        removeAllFilterFx(timeline);
        if(mTimeFxMode == TIMELINE_FX_MODE_REVERSE){
            readdFilterFx(mCurReverseFilterInfoList, timeline);
        }else {
            readdFilterFx(mCurFilterInfoList, timeline);
        }
    }

    private void rebuildTimelineStructure(NvsVideoTrack videoTrack,
                                                 RecordClipsInfo clipsInfo,
                                                 int fxMode,
                                                 long position,
                                                 boolean needRotate) {
        if (videoTrack == null)
            return;

//        Log.e(TAG, "test : " + position + " long");

        videoTrack.removeAllClips();

        // video
        if(fxMode == TIMELINE_FX_MODE_REVERSE) {
            ArrayList<RecordClip> videoClipArray = clipsInfo.getReverseClipList();
            for (int i = videoClipArray.size() - 1;i >= 0;i--) {
                RecordClip clipInfo = videoClipArray.get(i);
                NvsVideoClip videoClip = appendClip(videoTrack,clipInfo,needRotate);
                if(videoClip != null){
                    //videoClip.setSoftWareDecoding(true);
                    if(!clipInfo.getIsConvertSuccess()){
                        //转码不成功，使用源视频是本地导入素材，则需要进行裁剪
                        localVideoTrim(videoClip,clipInfo);
                    }
                }
            }
            Logger.e(TAG,"TimeLine Duratoin REVERSE -->" + mTimeline.getDuration());

        } else {
            ArrayList<RecordClip> videoClipArray = clipsInfo.getClipList();
            for (int i = 0;i < videoClipArray.size();i++) {
                RecordClip clipInfo = videoClipArray.get(i);
                NvsVideoClip videoClip = appendClip(videoTrack,clipInfo,needRotate);
                if(videoClip != null)
                    localVideoTrim(videoClip,clipInfo);
            }
            Logger.e(TAG,"TimeLine Duratoin Normal timelineFx -->" + mTimeline.getDuration());
        }

        if(position >= 0) {
            if (fxMode == TIMELINE_FX_MODE_REPEAT) {
                doRepeatTimeline(position, videoTrack);
            } else if (fxMode == TIMELINE_FX_MODE_SLOW) {
                doSlowMotionTimeline(position, videoTrack);
            }
        }

        if(videoTrack != null && videoTrack.getDuration() > MAXTIME) {
            videoTrack.removeRange(MAXTIME, videoTrack.getDuration(), false);
        }

        removeAllTransition(videoTrack);
    }

    private NvsVideoClip appendClip(NvsVideoTrack videoTrack,RecordClip clipInfo,boolean needRotate){
        if(videoTrack == null)
            return null;
        String filePath = clipInfo.getFilePath();
        NvsVideoClip videoClip = videoTrack.appendClip(filePath);
        if (videoClip == null){
            Log.e(TAG, "clip append failed! path: " + clipInfo.getFilePath());
            return videoClip;
        }

//        if (needRotate) {
//            int rotation = getRotation(NvsStreamingContext.getInstance().getAVFileInfo(filePath));
//            videoClip.setExtraVideoRotation(rotation);
//        }

        int rotateAngle = clipInfo.getRotateAngle();
        if(rotateAngle > 0)
            videoClip.setExtraVideoRotation(rotateAngle);

        float speed = clipInfo.getSpeed();
        videoClip.changeSpeed(speed);

        videoClip.setVolumeGain(0, 0);
        return videoClip;
    }

    private void localVideoTrim(NvsVideoClip videoClip,RecordClip clipInfo){
        if(videoClip == null)
            return;
        if(clipInfo == null)
            return;
        if(!clipInfo.isCaptureVideo()){//手机本地导入视频素材
            videoClip.changeTrimInPoint(clipInfo.getTrimIn(),true);
            videoClip.changeTrimOutPoint(clipInfo.getTrimOut(),true);
        }
    }


    public int getRotation(NvsAVFileInfo fileInfo) {
        int rotation = 0;
        if(fileInfo != null) {
            int videoStreamCount = fileInfo.getVideoStreamCount();
            if(videoStreamCount > 0) {
                rotation = fileInfo.getVideoStreamRotation(0);
                if(rotation == NvsVideoStreamInfo.VIDEO_ROTATION_90) {
                    rotation = NvsVideoStreamInfo.VIDEO_ROTATION_270;
                } else if(rotation == NvsVideoStreamInfo.VIDEO_ROTATION_180){
                    rotation = NvsVideoStreamInfo.VIDEO_ROTATION_180;
                } else if(rotation == NvsVideoStreamInfo.VIDEO_ROTATION_270) {
                    rotation = NvsVideoStreamInfo.VIDEO_ROTATION_90;
                }
            }
        }

        return rotation;
    }

    public void removeAllTransition(NvsVideoTrack videoTrack) {
        for(int i = 0;i<videoTrack.getClipCount();i++) {
            videoTrack.setBuiltinTransition(i,null);
        }
    }
    public boolean doSlowMotionTimeline(long point, NvsVideoTrack videoTrack) {
        if(videoTrack == null)
            return false;

        long duration = videoTrack.getDuration();
        if(duration < TIME_BASE)
            return false;

        if(duration < (point + TIME_BASE))
            point = duration - TIME_BASE;

        if(!splitClip(point, videoTrack))
            return false;

        if(!splitClip(point + TIME_BASE, videoTrack))
            return false;

        long prePoint = 0;
        long preduration = TIME_BASE;
        long afterPoint = point + TIME_BASE;
        long afterduration = TIME_BASE;
        if(point <  TIME_BASE){
            preduration = point;
            afterduration += TIME_BASE - preduration;
            if((afterPoint + afterduration) > duration)
                afterduration = duration - afterPoint;
        }else{
            if((afterPoint + afterduration) > duration)
                afterduration = duration - afterPoint;

            preduration += (TIME_BASE - afterduration);
            if(preduration > point){
                preduration = point;
            }
            prePoint = point - preduration;
        }

        if(point > preduration){
            splitClip(prePoint, videoTrack);
        }

        if((afterPoint + afterduration) < duration){
            splitClip(afterPoint + afterduration, videoTrack);
        }
        ArrayList<NvsVideoClip> clipArray = getClipRange(point, TIME_BASE, videoTrack);
        if(clipArray.size() < 1)
            return false;

        ArrayList<NvsVideoClip> preClipArray = getClipRange(prePoint, preduration, videoTrack);
        ArrayList<NvsVideoClip> afterClipArray = getClipRange(afterPoint, afterduration, videoTrack);

        for(int i = 0; i < clipArray.size(); i++){
            NvsVideoClip orgClip = clipArray.get(i);
            orgClip.changeSpeed(orgClip.getSpeed() / 2.0f, true);
        }

        for(int i = 0; i < preClipArray.size(); i++){
            NvsVideoClip orgClip = preClipArray.get(i);
            orgClip.changeSpeed(orgClip.getSpeed() * 2.0f, true);
        }

        for(int i = 0; i < afterClipArray.size(); i++){
            NvsVideoClip orgClip = afterClipArray.get(i);
            orgClip.changeSpeed(orgClip.getSpeed() * 2.0f, true);
        }

        setPanAndScan(videoTrack);
        muteVideoTrack(videoTrack);
        return true;
    }

    public boolean doRepeatTimeline(long point, NvsVideoTrack videoTrack) {
        if(videoTrack == null)
            return false;

        long duration = videoTrack.getDuration();
        long repeatDuration = TIME_BASE / 2;
        if(duration < repeatDuration)
            return false;

        if(duration < (point + repeatDuration))
            point = duration - repeatDuration;


//        NvsVideoClip splitClip = videoTrack.getClipByTimelinePosition(point);
//        if(splitClip == null)
//            return false;

        // 在point点(开始重复的点)处切割clip
        if(!splitClip(point, videoTrack))
            return false;
        // 在point(重复结束的点)处切割clip
        if(!splitClip(point+repeatDuration, videoTrack))
            return false;

        // 得到point点之后的所有clip
        ArrayList<NvsVideoClip> clipArray = getClipRange(point, repeatDuration, videoTrack);
        if(clipArray.size() < 1)
            return false;

        //
        ArrayList<Long> durationArray = new ArrayList<>();
        long segmentDuration = repeatDuration;
        for(int i = 0;i<clipArray.size();i++) {
            NvsVideoClip orgClip = clipArray.get(i);
            long newTrimOut = orgClip.getTrimIn() + segmentDuration;
            if(newTrimOut > orgClip.getTrimOut()) {
                newTrimOut = orgClip.getTrimOut();
            }

            long clipDuration = (newTrimOut - orgClip.getTrimIn());
            segmentDuration = segmentDuration - (newTrimOut - orgClip.getTrimIn());
            durationArray.add(clipDuration);
        }


        NvsVideoClip lastClip = clipArray.get(clipArray.size() - 1);
        int clipIndex = lastClip.getIndex() + 1;
        if(clipIndex >= videoTrack.getClipCount())
            clipIndex = videoTrack.getClipCount() - 1;

        for(int i = 0;i < 4;i++) {
            boolean bReverse = false;
            int start = 0;
            int end = clipArray.size() - 1;
            int step = 1;
            if((i % 2) == 0){
                bReverse = true;
                start = clipArray.size() - 1;
                end = 0;
                step = -1;
            }

            for(int n = start; bReverse ? n >= end : n <= end; n+= step){
                NvsVideoClip orgClip = clipArray.get(n);

                long clipDuration = durationArray.get(n);
                String orgFilePath = orgClip.getFilePath();
                long trimIn = orgClip.getTrimIn();
                long trimOut = orgClip.getTrimIn() + clipDuration;
                Logger.e(TAG,"Timeline normal trimIn--> " +trimIn + "trimOut -->" + trimOut);
                RecordClip recordClip = mRecordInfo.getClipByPath(orgFilePath);
                Logger.e(TAG,"Timeline normal recordClip.getTrimIn()--> " + recordClip.getTrimIn());
                long orgDur = recordClip.getDuration();
                if(bReverse && recordClip.getIsConvertSuccess()){
                    orgFilePath = PathUtils.getDouYinConvertDir() + File.separator + PathUtils.getFileName(orgFilePath);
                    RecordClip reverseClip = mRecordInfo.getReverseClipByPath(orgFilePath);
                    long dur = orgDur;
                    if(reverseClip != null){
                        dur = reverseClip.getDuration();
                    }
                    long relativeTrimIn = orgClip.getTrimIn() - recordClip.getTrimIn();//将原始视频传入的裁剪入点减掉
                    Logger.e(TAG,"Timeline Reverse relativeTrimIn--> " +relativeTrimIn);
                    trimIn = orgDur - clipDuration - (relativeTrimIn);

                    if(trimIn < 0){
                        trimIn = 0;
                    }
                    trimOut = orgDur - relativeTrimIn;
                    if(trimOut > dur){
                        trimOut = dur;
                    }
                    Logger.e(TAG,"Timeline Reverse trimIn--> " +trimIn + "trimOut -->" + trimOut);
                    if(trimIn > trimOut){
                        long tmp = trimIn;
                        trimIn = trimOut;
                        trimOut = tmp;
                    }
                }
                NvsVideoClip clip = videoTrack.insertClip(orgFilePath, trimIn, trimOut, clipIndex);
                if(clip == null) {
//                    Log.d("test", "doRepeatTimeline: clip == null");
                    continue;
                }
                int rotation = orgClip.getExtraVideoRotation();
                if(rotation != NvsVideoStreamInfo.VIDEO_ROTATION_0) {
                    clip.setExtraVideoRotation(rotation);
                }

                double speed = orgClip.getSpeed();
                if(speed <= 1){
                    speed = speed * 1.5;
                }
                clip.changeSpeed(speed,true);
//                if(bReverse) {
//                    clip.setPlayInReverse(true);
//                }
                clipIndex++;
            }
        }

        setPanAndScan(videoTrack);
        videoTrack.removeRange(duration, videoTrack.getDuration(), false);
        muteVideoTrack(videoTrack);
        //先注掉片段软件解码
//        for(int i = 0; i < videoTrack.getClipCount();i++) {
//            NvsVideoClip clip = videoTrack.getClipByIndex(i);
//            if(clip != null) {
//                long clipduration = clip.getOutPoint() - clip.getInPoint();
//                if(clipduration < 500*1000) {
//                    int nextclipIndex = i + 1;
//                    Logger.e(TAG,"nextclipIndex = " + nextclipIndex);
//                    if(nextclipIndex < videoTrack.getClipCount()) {
//                        NvsVideoClip nextclip = videoTrack.getClipByIndex(nextclipIndex);
//                        nextclip.setSoftWareDecoding(true);
//                    }
//                }
//            }
//        }

        return true;
    }

    public boolean muteVideoTrack(NvsVideoTrack videoTrack) {
        if(videoTrack == null)
            return false;

        for(int i = 0; i < videoTrack.getClipCount();i++) {
            NvsVideoClip clip = videoTrack.getClipByIndex(i);
            if(clip != null)
                clip.setVolumeGain(0, 0);
        }

        return true;
    }

    public boolean setPanAndScan(NvsVideoTrack videoTrack) {
        if(videoTrack == null)
            return false;

        for(int i = 0; i < videoTrack.getClipCount();i++) {
            NvsVideoClip clip = videoTrack.getClipByIndex(i);
            if(clip != null)
                clip.setPanAndScan(0, 1);
        }

        return true;
    }

    public ArrayList<NvsVideoClip> getClipRange(long point, long duration, NvsVideoTrack videoTrack) {
        ArrayList<NvsVideoClip> clipArray = new ArrayList<>();
        long ptIndex = point;
        while (ptIndex < (point + duration)) {
            NvsVideoClip clip = videoTrack.getClipByTimelinePosition(ptIndex);
            if(clip == null)
                break;

            ptIndex = clip.getOutPoint();
            clipArray.add(clip);
        }
        return clipArray;
    }

    public boolean splitClip(long point, NvsVideoTrack videoTrack) {
        if(videoTrack == null)
            return false;

        NvsVideoClip splitClip = videoTrack.getClipByTimelinePosition(point);
        if(splitClip == null)
            return false;

        if(splitClip.getInPoint() != point) {
            boolean flag = videoTrack.splitClip(splitClip.getIndex(), point);
            if(!flag)
                return false;
        }

        return true;
    }

    private void addMusic(MusicInfo musicInfo) {
        if(musicInfo == null){
            return;
        }
        mMusicTrack.removeAllClips();
        while (mMusicTrack.getDuration() < mVideoTrack.getDuration()) {
            long trimIn = musicInfo.getTrimIn();
            long trimOut = musicInfo.getTrimOut();

            NvsAudioClip audioClip = mMusicTrack.appendClip(musicInfo.getFilePath(), trimIn, trimOut);
            if(audioClip == null) {
                Log.e(TAG, "添加音频文件失败");
                return;
            }
        }
    }

    private void clear(){
        if(!mRecordInfo.getIsConvert()){
            if(mConvertFiles != null)
                mConvertFiles.sendCancelConvertMsg();
        }
        if (mQuitDialog != null && mQuitDialog.isShowing()) {
            mQuitDialog.dismiss();
        }
        if(mStreamingContext != null && mTimeline != null){
            mStreamingContext.stop();
            TimelineUtil.removeTimeline(mTimeline);
            mTimeline = null;
            mStreamingContext.setStreamingEngineCallback(null);
            mStreamingContext.setPlaybackCallback2(null);
            mStreamingContext.setPlaybackCallback(null);
            mLiveWindow.setOnClickListener(null);
            mStreamingContext.clearCachedResources(false);
            mHandler.removeCallbacksAndMessages(null);
            stopProgressTimer();
            AppManager.getInstance().finishActivity();
        }
    }

    public void closeActivity() {
        if(!mEffectChanged) {
            clear();
        } else {
            String[] returnTips = getResources().getStringArray(R.array.douyin_return_tips);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(returnTips[0]).setPositiveButton(returnTips[1], new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clear();
                }
            }).setNegativeButton(returnTips[2], new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setCancelable(false);
            mQuitDialog = builder.create();
            mQuitDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (null != mQuitDialog && mQuitDialog.isShowing()) {
                            mQuitDialog.dismiss();
                        }
                    }
                    return false;
                }
            });
            mQuitDialog.show();
        }
    }

    private List<TimelineFxResourceObj> getTimeLineFxData() {
        List<TimelineFxResourceObj> list = new ArrayList<TimelineFxResourceObj>();

        TimelineFxResourceObj none = new TimelineFxResourceObj();
        none.image = R.mipmap.timeline_fx_no;
        none.color = R.color.ms994a90e2;
        none.imageName = getResources().getString(R.string.timeline_fx_none);
        list.add(none);

        TimelineFxResourceObj back_in_time = new TimelineFxResourceObj();
        back_in_time.image = R.mipmap.timeline_fx_reverse;
        back_in_time.color = R.color.ms994a90e2;
        back_in_time.imageName = getResources().getString(R.string.timeline_fx_reverse);
        list.add(back_in_time);

        TimelineFxResourceObj repeate = new TimelineFxResourceObj();
        repeate.image = R.mipmap.timeline_fx_repeat;
        repeate.color = R.color.ms994a90e2;
        repeate.imageName = getResources().getString(R.string.timeline_fx_repeat);
        list.add(repeate);

        TimelineFxResourceObj slow_motion = new TimelineFxResourceObj();
        slow_motion.image = R.mipmap.timeline_fx_slow;
        slow_motion.color = R.color.ms994a90e2;
        slow_motion.imageName = getResources().getString(R.string.timeline_fx_slow_motion);
        list.add(slow_motion);

        return list;
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


    static class EditHandler extends Handler
    {
        WeakReference<DouVideoEditActivity> mWeakReference;
        public EditHandler(DouVideoEditActivity activity)
        {
            mWeakReference= new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg)
        {
            final DouVideoEditActivity activity = mWeakReference.get();
            if(activity!=null)
            {
                switch (msg.what) {

                    case MESSAGE_REPLAY_TIMELINE: {
                        if(activity.mTimeline == null)
                            return;
                        if (activity.mFxSeekView.isAddingFilter()) {
                            activity.seekTimeline(activity.mTimeline, activity.mTimeline.getDuration());
                            break;
                        }
                        if (activity.mTimeFxMode == TIMELINE_FX_MODE_REVERSE) {
                            activity.mProgress = 100;
                        } else {
                            activity.mProgress = 0;
                        }
                        activity.mFxSeekView.setFirstValue(activity.mProgress);
                        activity.startPlay(activity.mTimeline, 0);
                        break;
                    }

                    case MESSAGE_ALL_VIDEO_CONVERT_FINISHED:{
                        if(activity.mTimeline == null)
                            return;
                        activity.mRecordInfo.setIsConvert(true);
                        if(activity.mIsWatiCoverting && activity.isTimelineFx ){
                            if(activity.mAutoDoTimelineFx == TIMELINE_FX_MODE_REVERSE){
                                activity.mTimeFxMode = TIMELINE_FX_MODE_REVERSE;
                                activity.rebuildTimeline(activity.mTimeline,activity. mVideoTrack,activity. mRecordInfo, false, activity.mTimeFxMode, activity.mTimelinePosValue*activity.mTimeline.getDuration());
                                activity.mProgress = 100;
                                activity.mFxSeekView.setFxMode(activity.mTimeFxMode);
                                activity.mTimelineFxAdapter.hideBusy();
                                activity.mTimelineFxAdapter.setSelect(activity.mTimelineFxAdapter.getData().get(TIMELINE_FX_MODE_REVERSE));
                            }else if(activity.mAutoDoTimelineFx == TIMELINE_FX_MODE_REPEAT){
                                activity.mTimeFxMode = TIMELINE_FX_MODE_REPEAT;
                                activity.mTimelinePosValue = activity.mRepeatPosValue;
                                activity.rebuildTimeline(activity.mTimeline, activity.mVideoTrack, activity.mRecordInfo, false, activity.mTimeFxMode, activity.mTimelinePosValue*activity.mTimeline.getDuration());
                                activity.mFxSeekView.setFxMode(activity.mTimeFxMode);
                                activity.mFxSeekView.setSecondValue(activity.mTimelinePosValue * 100);
                                activity.mProgress = 0;
                                activity.mFxSeekView.setFirstValue(activity.mProgress);
                                activity.mTimelineFxAdapter.hideBusy();
                                activity.mTimelineFxAdapter.setSelect(activity.mTimelineFxAdapter.getData().get(TIMELINE_FX_MODE_REPEAT));
                            }
                            activity.startPlay(activity.mTimeline,activity.getPlayPosition());
                        }
                        activity.mIsWatiCoverting = false;
                        activity.mAutoDoTimelineFx = TIMELINE_FX_MODE_NONE;
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }
}
