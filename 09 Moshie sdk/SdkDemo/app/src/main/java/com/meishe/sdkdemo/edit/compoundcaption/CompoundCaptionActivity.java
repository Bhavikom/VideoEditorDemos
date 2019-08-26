package com.meishe.sdkdemo.edit.compoundcaption;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineCompoundCaption;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.download.AssetDownloadActivity;
import com.meishe.sdkdemo.edit.VideoFragment;
import com.meishe.sdkdemo.edit.adapter.SpaceItemDecoration;
import com.meishe.sdkdemo.edit.data.AssetItem;
import com.meishe.sdkdemo.edit.data.BackupData;
import com.meishe.sdkdemo.edit.data.ParseJsonFile;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.edit.timelineEditor.NvsTimelineEditor;
import com.meishe.sdkdemo.edit.timelineEditor.NvsTimelineTimeSpan;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.PathNameUtil;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.TimeFormatUtil;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.asset.NvAsset;
import com.meishe.sdkdemo.utils.asset.NvAssetManager;
import com.meishe.sdkdemo.utils.dataInfo.CompoundCaptionInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CompoundCaptionActivity extends BaseActivity {
    private static final String TAG = "CaptionActivity";
    private static final int VIDEOPLAYTOEOF = 105;
    private static final int REQUEST_CAPTION_STYLE = 103;
    private static final int COMPOINDCAPTIONREQUESTLIST = 104;
    public static final String select_caption_index = "select_caption_index";
    public static final String select_caption_text = "select_caption_text";
    private CustomTitleBar mTitleBar;
    private TextView mPlayCurTime;
    private RelativeLayout mZoomInBtn;
    private RelativeLayout mZoomOutBtn;
    private Button mModifyButton;
    private NvsTimelineEditor mTimelineEditor;
    private Button mPlayBtn;
    private ImageView mAddCaptionBtn;
    private ImageView mOkBtn;
    private VideoFragment mVideoFragment;
    private RelativeLayout mBottomRelativeLayout;
    private RelativeLayout mPlayBtnLayout;
    private NvsMultiThumbnailSequenceView mMultiSequenceView;

    private RelativeLayout mCompCaptionAssetLayout;
    private ImageView mComCaptionAssetDownload;
    private RecyclerView mComCaptionRecycler;
    private ImageView mComCaptionAssetFinish;
    private CompoundCaptionAdaper mCompoundCaptionAdaper;

    private NvsTimeline mTimeline;
    private boolean mIsSeekTimeline = true;
    private NvsTimelineCompoundCaption mCurCaption;
    private NvsTimelineCompoundCaption mAddComCaption;
    private NvsStreamingContext mStreamingContext;
    private List<CaptionTimeSpanInfo> mTimeSpanInfoList = new ArrayList<>();
    private CompoundCaptionActivity.CaptionHandler m_handler = new CompoundCaptionActivity.CaptionHandler(this);
    private ArrayList<CompoundCaptionInfo> mCaptionDataListClone;
    private boolean mIsInnerDrawRect = false;
    private StringBuilder mShowCurrentDuration = new StringBuilder();
    private ArrayList<AssetItem> mComCaptionStyleList = new ArrayList<>();
    private int mCurSelectedPos = -1;
    private boolean isNewCaptionUuidItemClick = false;
    private long mInPoint;
    private long mCaptionDuration;
    private int mCurCaptionZVal;
    private NvAssetManager mAssetManager;
    private int mCaptionStyleType = NvAsset.ASSET_COMPOUND_CAPTION;

    static class CaptionHandler extends Handler {
        WeakReference<CompoundCaptionActivity> mWeakReference;

        public CaptionHandler(CompoundCaptionActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CompoundCaptionActivity activity = mWeakReference.get();
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

    private void resetView() {
        updatePlaytimeText(mCompCaptionAssetLayout.getVisibility() == View.VISIBLE ? mInPoint : 0);
        mMultiSequenceView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        seekTimeline(mCompCaptionAssetLayout.getVisibility() == View.VISIBLE ? mInPoint : 0);
        selectCaptionAndTimeSpan();
    }

    private void seekTimeline(long timeStamp) {
        mVideoFragment.seekTimeline(timeStamp, NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
    }

    @Override
    protected int initRootView() {
        return R.layout.activity_compound_caption;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mPlayCurTime = (TextView) findViewById(R.id.play_cur_time);
        mZoomInBtn = (RelativeLayout) findViewById(R.id.zoom_in_btn);
        mZoomOutBtn = (RelativeLayout) findViewById(R.id.zoom_out_btn);
        mModifyButton = (Button) findViewById(R.id.modifyButton);
        mTimelineEditor = (NvsTimelineEditor) findViewById(R.id.caption_timeline_editor);
        mPlayBtn = (Button) findViewById(R.id.play_btn);
        mAddCaptionBtn = (ImageView) findViewById(R.id.add_caption_btn);
        mOkBtn = (ImageView) findViewById(R.id.ok_btn);
        mBottomRelativeLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mPlayBtnLayout = (RelativeLayout) findViewById(R.id.play_btn_layout);
        mMultiSequenceView = mTimelineEditor.getMultiThumbnailSequenceView();

        //
        mCompCaptionAssetLayout = (RelativeLayout) findViewById(R.id.compCaptionAsset_layout);
        mComCaptionAssetDownload = (ImageView) findViewById(R.id.comCaptionAssetDownload);
        mComCaptionRecycler = (RecyclerView) findViewById(R.id.comCaptionRecycler);
        mComCaptionAssetFinish = (ImageView) findViewById(R.id.comCaptionAssetFinish);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.compoundcaption);
        mTitleBar.setBackImageVisible(View.GONE);
    }

    @Override
    protected void initData() {
        mStreamingContext = NvsStreamingContext.getInstance();
        mTimeline = TimelineUtil.createTimeline();
        if (mTimeline == null)
            return;
        mCaptionDataListClone = TimelineData.instance().cloneCompoundCaptionData();
        initAssetData();
        initVideoFragment();
        updatePlaytimeText(0);
        initMultiSequence();
        addAllTimeSpan();
        selectCaption();
        selectTimeSpan();
        initCompoundCaptionStyleList();
        initCompoundCaptionRecycleAdapter();
        initCaptionFontInfoList();
    }

    private void initAssetData() {
        mAssetManager = NvAssetManager.sharedInstance();
        mAssetManager.searchLocalAssets(mCaptionStyleType);
        String bundlePath = "compoundcaption";
        mAssetManager.searchReservedAssets(mCaptionStyleType, bundlePath);
    }

    private void initCaptionFontInfoList() {
        String fontJsonPath = "font/info.json";
        String fontJsonText = ParseJsonFile.readAssetJsonFile(this, fontJsonPath);
        if (TextUtils.isEmpty(fontJsonText)) {
            return;
        }
        ArrayList<FontInfo> fontInfoList = ParseJsonFile.fromJson(fontJsonText, new TypeToken<List<FontInfo>>() {
        }.getType());
        if (fontInfoList == null) {
            return;
        }
        int fontCount = fontInfoList.size();
        for (int idx = 0; idx < fontCount; idx++) {
            FontInfo fontInfo = fontInfoList.get(idx);
            if (fontInfo == null) {
                continue;
            }
            String fontAssetPath = "assets:/font/" + fontInfo.getFontFileName();
            mStreamingContext.registerFontByFilePath(fontAssetPath);
        }
    }

    @Override
    protected void initListener() {
        mZoomInBtn.setOnClickListener(this);
        mZoomOutBtn.setOnClickListener(this);
        mModifyButton.setOnClickListener(this);
        mAddCaptionBtn.setOnClickListener(this);
        mOkBtn.setOnClickListener(this);
        mPlayBtn.setOnClickListener(this);
        mComCaptionAssetDownload.setOnClickListener(this);
        mComCaptionAssetFinish.setOnClickListener(this);
        mTimelineEditor.setOnScrollListener(new NvsTimelineEditor.OnScrollChangeListener() {
            @Override
            public void onScrollX(long timeStamp) {
                if (!mIsSeekTimeline)
                    return;
                if (mTimeline != null) {
                    updatePlaytimeText(timeStamp);
                    selectCaptionAndTimeSpan();
                    seekTimeline(timeStamp);
                }
            }
        });

        mVideoFragment.setLiveWindowClickListener(new VideoFragment.OnLiveWindowClickListener() {
            @Override
            public void onLiveWindowClick() {
                isNewCaptionUuidItemClick = false;
            }
        });
        mMultiSequenceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mIsSeekTimeline = true;
                return false;
            }
        });

        if (mVideoFragment != null) {
            mVideoFragment.setVideoFragmentCallBack(new VideoFragment.VideoFragmentListener() {
                @Override
                public void playBackEOF(NvsTimeline timeline) {
                    m_handler.sendEmptyMessage(VIDEOPLAYTOEOF);
                }

                @Override
                public void playStopped(NvsTimeline timeline) {
                    if (isNewCaptionUuidItemClick)
                        return;
                    selectCaptionAndTimeSpan();
                }

                @Override
                public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {
                    updatePlaytimeText(stamp);
                    mVideoFragment.setDrawRectVisible(View.GONE);
                    mTimelineEditor.unSelectAllTimeSpan();
                    selectCaption();
                    multiThumbnailSequenceViewSmooth(stamp);
                }

                @Override
                public void streamingEngineStateChanged(int state) {
                    if (NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK == state) {
                        mIsSeekTimeline = false;
                        mPlayBtn.setBackgroundResource(R.mipmap.icon_edit_pause);
                    } else {
                        mPlayBtn.setBackgroundResource(R.mipmap.icon_edit_play);
                        mIsSeekTimeline = true;
                    }
                }
            });
            mVideoFragment.setAssetEditListener(new VideoFragment.AssetEditListener() {
                @Override
                public void onAssetDelete() {
                    removeCaption(mCurCaption);
                    mCurCaption = null;
                    mAddCaptionBtn = null;
                    selectCaptionAndTimeSpan();
                    seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
                    //取消所有Tab页贴纸选中的状态
                    notifyDataSetChanged(-1);
                }

                @Override
                public void onAssetSelected(PointF curPoint) {
                    if (mCompCaptionAssetLayout.getVisibility() == View.VISIBLE)
                        return;//组合字幕素材列表显示，则不允许在liveWindow 来回切换选择组合字幕，只选择当前添加的组合字幕
                    //判断若没有选中当前字幕框则选中，选中则不处理
                    mIsInnerDrawRect = mVideoFragment.curPointIsInnerDrawRect((int) curPoint.x, (int) curPoint.y);
                    if (!mIsInnerDrawRect) {
                        mVideoFragment.selectCompoundCaptionByHandClick(curPoint);
                        mCurCaption = mVideoFragment.getCurrCompoundCaption();
                        selectTimeSpan();
                    }
                }

                @Override
                public void onAssetTranstion() {
                    if (mCurCaption == null)
                        return;
                    PointF pointF = mCurCaption.getCaptionTranslation();
                    int zVal = (int) mCurCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if (index >= 0) {
                        mCaptionDataListClone.get(index).setTranslation(pointF);
                    }
                }

                @Override
                public void onAssetScale() {
                    if (mCurCaption == null)
                        return;
                    int zVal = (int) mCurCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if (index >= 0) {
                        mCaptionDataListClone.get(index).setScaleFactorX(mCurCaption.getScaleX());
                        mCaptionDataListClone.get(index).setScaleFactorY(mCurCaption.getScaleY());
                        mCaptionDataListClone.get(index).setAnchor(mCurCaption.getAnchorPoint());
                        mCaptionDataListClone.get(index).setRotation(mCurCaption.getRotationZ());
                        PointF pointF = mCurCaption.getCaptionTranslation();
                        mCaptionDataListClone.get(index).setTranslation(pointF);
                    }
                }

                @Override
                public void onAssetAlign(int alignVal) {
                }

                @Override
                public void onAssetHorizFlip(boolean isHorizFlip) {

                }
            });
            mVideoFragment.setCompoundCaptionListener(new VideoFragment.OnCompoundCaptionListener() {
                @Override
                public void onCaptionIndex(int captionIndex) {
                    if (mCompCaptionAssetLayout.getVisibility() == View.VISIBLE) {
                        return;
                    }
                    if (mCurCaption == null) {
                        return;
                    }
                    if (!mIsInnerDrawRect) {
                        return;
                    }
                    int captionCount = mCurCaption.getCaptionCount();
                    if (captionIndex < 0 || captionIndex >= captionCount) {
                        return;
                    }
                    mIsInnerDrawRect = false;
                    int zVal = (int) mCurCaption.getZValue();
                    BackupData.instance().setCaptionZVal(zVal);
                    BackupData.instance().setCompoundCaptionList(mCaptionDataListClone);
                    BackupData.instance().setCurSeekTimelinePos(mStreamingContext.getTimelineCurrentPosition(mTimeline));
                    String captionText = mCurCaption.getText(captionIndex);
                    Bundle bundle = new Bundle();
                    bundle.putInt(select_caption_index, captionIndex);
                    bundle.putString(select_caption_text, captionText);
                    AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), CompoundCaptionStyleActivity.class, bundle, REQUEST_CAPTION_STYLE);
                }
            });
        }

        mCompCaptionAssetLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zoom_in_btn:
                mIsSeekTimeline = false;
                mTimelineEditor.ZoomInSequence();
                break;

            case R.id.zoom_out_btn:
                mIsSeekTimeline = false;
                mTimelineEditor.ZoomOutSequence();
                break;
            case R.id.modifyButton:
                mModifyButton.setClickable(false);
                int zVal = (int) mCurCaption.getZValue();
                int index = getCaptionIndex(zVal);
                if (index < 0) {
                    return;
                }
                CompoundCaptionInfo captionInfo = mCaptionDataListClone.get(index);
                if (captionInfo == null) {
                    return;
                }
                String captionPackageUuid = captionInfo.getCaptionStyleUuid();
                int selectPos = getCaptionSelectPos(captionPackageUuid);
                if (selectPos >= 0) {
                    notifyDataSetChanged(selectPos);
                    mCompCaptionAssetLayout.setVisibility(View.VISIBLE);
                    mAddComCaption = mCurCaption;
                    mInPoint = mCurCaption.getInPoint();
                    mCaptionDuration = mCurCaption.getOutPoint() - mInPoint;
                }
                break;
            case R.id.add_caption_btn:
                mVideoFragment.stopEngine();
                mInPoint = mStreamingContext.getTimelineCurrentPosition(mTimeline);
                mCaptionDuration = 4 * Constants.NS_TIME_BASE;
                long duration = mTimeline.getDuration();
                long outPoint = mInPoint + mCaptionDuration;
                if (outPoint > duration) {
                    mCaptionDuration = duration - mInPoint;
                    if (mCaptionDuration <= Constants.NS_TIME_BASE) {
                        mCaptionDuration = Constants.NS_TIME_BASE;
                        mInPoint = duration - mCaptionDuration;
                        if (duration <= Constants.NS_TIME_BASE) {
                            mCaptionDuration = duration;
                            mInPoint = 0;
                        }
                    }
                }
                if (mCurCaption != null) {
                    mCurCaptionZVal = (int) mCurCaption.getZValue();
                }
                mVideoFragment.setDrawRectVisible(View.GONE);
                mCompCaptionAssetLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.ok_btn:
                mStreamingContext.stop();
                removeTimeline();
                TimelineData.instance().setCompoundCaptionArray(mCaptionDataListClone);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                AppManager.getInstance().finishActivity();
                break;

            case R.id.play_btn:
                playVideo();
                break;
            case R.id.comCaptionAssetDownload:
                mVideoFragment.stopEngine();
                Bundle bundle = new Bundle();
                bundle.putInt("titleResId", R.string.moreCompoundCaptionStyle);
                bundle.putInt("assetType", NvAsset.ASSET_COMPOUND_CAPTION);
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), AssetDownloadActivity.class, bundle, COMPOINDCAPTIONREQUESTLIST);
                break;
            case R.id.comCaptionAssetFinish:
                multiThumbnailSequenceViewSmooth(mInPoint);
                mCompCaptionAssetLayout.setVisibility(View.GONE);
                seekTimeline(mInPoint);
                if (mAddComCaption != null) {
                    selectCaptionAndTimeSpan();
                } else {
                    selectCaptionAndTimeSpanByZVal();
                }
                mAddComCaption = null;//添加组合字幕对象置空，否则再次进入组合字幕列表会造成误删
                mCurCaptionZVal = 0;
                mCurSelectedPos = -1;
                //取消列表字幕的选中状态
                notifyDataSetChanged(mCurSelectedPos);
                mModifyButton.setClickable(true);
                isNewCaptionUuidItemClick = false;
                break;
        }
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case REQUEST_CAPTION_STYLE:
                mCaptionDataListClone = BackupData.instance().getCompoundCaptionList();
                TimelineUtil.setCompoundCaption(mTimeline, mCaptionDataListClone);
                mTimelineEditor.deleteAllTimeSpan();
                mTimeSpanInfoList.clear();
                mCurCaption = null;
                addAllTimeSpan();
                long curSeekPos = BackupData.instance().getCurSeekTimelinePos();
                seekTimeline(curSeekPos);
                mCurCaptionZVal = BackupData.instance().getCaptionZVal();
                selectCaptionAndTimeSpanByZVal();
                break;
            case COMPOINDCAPTIONREQUESTLIST:
                initCompoundCaptionStyleList();
                mCompoundCaptionAdaper.setAssetList(mComCaptionStyleList);
                mCompoundCaptionAdaper.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void notifyDataSetChanged(int selectPos) {
        if (mCompoundCaptionAdaper != null) {
            mCompoundCaptionAdaper.setSelectedPos(selectPos);
            mCompoundCaptionAdaper.notifyDataSetChanged();
        }
    }

    private int getCaptionSelectPos(String captionUuid) {
        if (TextUtils.isEmpty(captionUuid)) {
            return -1;
        }
        int capStyleCount = mComCaptionStyleList.size();
        for (int idx = 0; idx < capStyleCount; idx++) {
            AssetItem assetItem = mComCaptionStyleList.get(idx);
            if (assetItem == null) {
                continue;
            }
            NvAsset asset = assetItem.getAsset();
            if (asset == null) {
                continue;
            }
            if (!TextUtils.isEmpty(asset.uuid) && asset.uuid.equals(captionUuid)) {
                return idx;
            }
        }
        return -1;
    }

    private void selectCaptionAndTimeSpan() {
        selectCaption();
        updateComCaptionBoundingRect();
        if (mCurCaption != null) {
            selectTimeSpan();
        } else {
            mTimelineEditor.unSelectAllTimeSpan();
        }
    }

    private void selectCaptionAndTimeSpanByZVal() {
        selectCatpionByZVal(mCurCaptionZVal);
        updateComCaptionBoundingRect();
        if (mCurCaption != null) {
            selectTimeSpan();
        } else {
            mTimelineEditor.unSelectAllTimeSpan();
        }
    }

    private void updateComCaptionBoundingRect() {
        mVideoFragment.setCurCompoundCaption(mCurCaption);
        mVideoFragment.updateCompoundCaptionCoordinate(mCurCaption);
        if (mAddComCaption == null
                && mCompCaptionAssetLayout.getVisibility() == View.VISIBLE) {
            mVideoFragment.setDrawRectVisible(View.GONE);
        } else {
            mVideoFragment.changeCompoundCaptionRectVisible();
        }
    }

    private void multiThumbnailSequenceViewSmooth(long stamp) {
        if (mMultiSequenceView != null) {
            int x = Math.round((stamp / (float) mTimeline.getDuration() * mTimelineEditor.getSequenceWidth()));
            mMultiSequenceView.smoothScrollTo(x, 0);
        }
    }

    private void selectCatpionByZVal(int curZVal) {
        if (mTimeline == null) {
            return;
        }
        long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        List<NvsTimelineCompoundCaption> captionList = mTimeline.getCompoundCaptionsByTimelinePosition(curPos);
        int captionCount = captionList.size();
        if (captionCount > 0) {
            for (int i = 0; i < captionCount; i++) {
                int zVal = (int) captionList.get(i).getZValue();
                if (curZVal == zVal) {
                    mCurCaption = captionList.get(i);
                    break;
                }
            }
            if (mCurCaption != null) {
                mModifyButton.setVisibility(View.VISIBLE);
            }
        } else {
            mCurCaption = null;
            mModifyButton.setVisibility(View.GONE);
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

    private void updatePlaytimeText(long playTime) {
        if (mTimeline != null) {
            long totalDuaration = mTimeline.getDuration();
            String strTotalDuration = TimeFormatUtil.formatUsToString1(totalDuaration);
            String strCurrentDuration = TimeFormatUtil.formatUsToString1(playTime);
            mShowCurrentDuration.setLength(0);
            mShowCurrentDuration.append(strCurrentDuration);
            mShowCurrentDuration.append("/");
            mShowCurrentDuration.append(strTotalDuration);
            mPlayCurTime.setText(mShowCurrentDuration.toString());
        }
    }

    private float getCurCaptionZVal() {
        float zVal = 0.0f;
        NvsTimelineCompoundCaption caption = mTimeline.getFirstCompoundCaption();
        while (caption != null) {
            float tmpZVal = caption.getZValue();
            if (tmpZVal > zVal)
                zVal = tmpZVal;
            caption = mTimeline.getNextCaption(caption);
        }
        zVal += 1.0;
        return zVal;
    }

    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mOkBtn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline));
                        selectCaptionAndTimeSpan();
                    }
                }, 100);
            }
        });

        //设置组合字幕模式
        mVideoFragment.setEditMode(Constants.EDIT_MODE_COMPOUND_CAPTION);
        mVideoFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight", mBottomRelativeLayout.getLayoutParams().height);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        bundle.putBoolean("playBarVisible", false);
        mVideoFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.video_layout, mVideoFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mVideoFragment);
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
        int halfScreenWidth = ScreenUtils.getScreenWidth(this) / 2;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mPlayBtnLayout.getLayoutParams();
        int playBtnTotalWidth = layoutParams.width + layoutParams.leftMargin + layoutParams.rightMargin;
        int sequenceLeftPadding = halfScreenWidth - playBtnTotalWidth;
        mTimelineEditor.setSequencLeftPadding(sequenceLeftPadding);
        mTimelineEditor.setSequencRightPadding(halfScreenWidth);
        mTimelineEditor.setTimeSpanLeftPadding(sequenceLeftPadding);
        mTimelineEditor.initTimelineEditor(sequenceDescsArray, duration);
    }

    //获取下载到手机路径下的素材，包括assets路径下自带的素材
    private ArrayList<NvAsset> getAssetsDataList(int assetType) {
        return mAssetManager.getUsableAssets(assetType, NvAsset.AspectRatio_All, 0);
    }

    private void initCompoundCaptionStyleList() {
        mComCaptionStyleList.clear();
        ArrayList<NvAsset> usableAsset = getAssetsDataList(mCaptionStyleType);
        String jsonBundlePath = "compoundcaption/info.json";
        ArrayList<ParseJsonFile.FxJsonFileInfo.JsonFileInfo> infoLists = ParseJsonFile.readBundleFxJsonFile(this, jsonBundlePath);
        if (infoLists != null) {
            for (ParseJsonFile.FxJsonFileInfo.JsonFileInfo jsonFileInfo : infoLists) {
                for (NvAsset asset : usableAsset) {
                    if (asset == null || TextUtils.isEmpty(asset.uuid)) {
                        continue;
                    }
                    //assets路径下的字幕样式包
                    if (asset.isReserved && asset.uuid.equals(jsonFileInfo.getFxPackageId())) {
                        asset.name = jsonFileInfo.getName();
                        asset.aspectRatio = Integer.parseInt(jsonFileInfo.getFitRatio());
                        StringBuilder coverPath = new StringBuilder("file:///android_asset/compoundcaption/");
                        coverPath.append(jsonFileInfo.getImageName());
                        asset.coverUrl = coverPath.toString();
                        asset.fxFileName = jsonFileInfo.getFxFileName();
                    }
                }
            }
        }
        int ratio = TimelineData.instance().getMakeRatio();
        for (NvAsset asset : usableAsset) {
            if (asset == null || TextUtils.isEmpty(asset.uuid) || (ratio & asset.aspectRatio) == 0) {
                //制作比例不适配，不加载
                continue;
            }
            if (asset.fxFileName == null) {
                asset.fxFileName = PathNameUtil.getPathNameWithSuffix(asset.localDirPath);
            }
            String assetPackageFilePath = asset.localDirPath;
            if (TextUtils.isEmpty(asset.localDirPath)) {
                assetPackageFilePath = "assets:/compoundcaption/" + asset.fxFileName;
            }
            StringBuilder packageUuid = new StringBuilder();
            boolean installSuccess = installCaptionPackage(assetPackageFilePath, packageUuid);
            if (!installSuccess) {
                continue;
            }
            asset.uuid = packageUuid.toString();
            AssetItem assetItem = new AssetItem();
            assetItem.setAsset(asset);
            assetItem.setAssetMode(AssetItem.ASSET_LOCAL);
            mComCaptionStyleList.add(assetItem);
        }
    }

    private boolean installCaptionPackage(String assetPackageFilePath, StringBuilder packageUuid) {
        int retResult = mStreamingContext.getAssetPackageManager().installAssetPackage(assetPackageFilePath, null,
                NvsAssetPackageManager.ASSET_PACKAGE_TYPE_COMPOUND_CAPTION, true, packageUuid);
        if (retResult != NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR && retResult != NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
            Logger.e(TAG, "failed to install package = " + assetPackageFilePath);
            return false;
        }
        return true;
    }

    private void initCompoundCaptionRecycleAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mComCaptionRecycler.setLayoutManager(layoutManager);
        mCompoundCaptionAdaper = new CompoundCaptionAdaper(this);
        mCompoundCaptionAdaper.setAssetList(mComCaptionStyleList);
        mComCaptionRecycler.setAdapter(mCompoundCaptionAdaper);
        mComCaptionRecycler.addItemDecoration(new SpaceItemDecoration(0, ScreenUtils.dip2px(this, 8)));
        mCompoundCaptionAdaper.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                int captiontStyleCount = mComCaptionStyleList.size();
                if (pos < 0 || pos >= captiontStyleCount) {
                    return;
                }
                if (mCurSelectedPos == pos) {
                    if (mVideoFragment.getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                        if (mAddComCaption != null) {
                            long startTime = mAddComCaption.getInPoint();
                            long endTime = mAddComCaption.getOutPoint();
                            mVideoFragment.playVideo(startTime, endTime);
                            mVideoFragment.setDrawRectVisible(View.GONE);
                            isNewCaptionUuidItemClick = false;
                        }
                    } else {
                        mVideoFragment.stopEngine();
                    }
                    return;
                }

                AssetItem assetItem = mComCaptionStyleList.get(pos);
                if (assetItem == null) {
                    return;
                }
                NvAsset asset = assetItem.getAsset();
                if (asset == null) {
                    return;
                }
                mCurSelectedPos = pos;
                String captionStyleUuid = asset.uuid;
                addCaption(captionStyleUuid);
            }
        });
    }

    //添加字幕
    private void addCaption(String captionUuid) {
        isNewCaptionUuidItemClick = true;
        if (mAddComCaption != null) {
            removeCaption(mAddComCaption);
            mAddComCaption = null;
        }
        mAddComCaption = mTimeline.addCompoundCaption(mInPoint, mCaptionDuration, captionUuid);
        if (mAddComCaption == null) {
            Logger.e(TAG, "addCaption: " + " 添加组合字幕失败！");
            return;
        }
        float zVal = getCurCaptionZVal();
        mAddComCaption.setZValue(zVal);
        long outPoint = mInPoint + mCaptionDuration;
        NvsTimelineTimeSpan timeSpan = addTimeSpan(mInPoint, outPoint);
        if (timeSpan == null) {
            Logger.e(TAG, "addCaption: " + " 添加TimeSpan失败!");
            return;
        }
        mTimeSpanInfoList.add(new CaptionTimeSpanInfo(mAddComCaption, timeSpan));
        mModifyButton.setVisibility(View.VISIBLE);
        CompoundCaptionInfo captionInfo = Util.saveCompoundCaptionData(mAddComCaption);
        if (captionInfo != null) {
            captionInfo.setCaptionStyleUuid(captionUuid);
            mCaptionDataListClone.add(captionInfo);
        }
        //播放视频
        mVideoFragment.playVideo(mInPoint, outPoint);
        mVideoFragment.setDrawRectVisible(View.GONE);
    }

    private void removeCaption(NvsTimelineCompoundCaption delCaption) {
        deleteCurCaptionTimeSpan(delCaption);
        int zVal = (int) delCaption.getZValue();
        int index = getCaptionIndex(zVal);
        if (index >= 0) {
            mCaptionDataListClone.remove(index);
        }
        mTimeline.removeCompoundCaption(delCaption);
    }

    private NvsTimelineTimeSpan addTimeSpan(long inPoint, long outPoint) {
        //warning: 使用addTimeSpanExt之前必须设置setTimeSpanType()
        mTimelineEditor.setTimeSpanType("NvsTimelineTimeSpan");
        NvsTimelineTimeSpan timelineTimeSpan = mTimelineEditor.addTimeSpan(inPoint, outPoint);
        if (timelineTimeSpan == null) {
            Log.e(TAG, "addTimeSpan: " + " 添加TimeSpan失败!");
            return null;
        }
        timelineTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimInChangeListener() {
            @Override
            public void onChange(long timeStamp, boolean isDragEnd) {
                seekTimeline(timeStamp);
                updatePlaytimeText(timeStamp);
                mVideoFragment.changeCaptionRectVisible();
                if (isDragEnd && mCurCaption != null) {
                    Logger.e(TAG, "TrimInChange1212->" + timeStamp);
                    mCurCaption.changeInPoint(timeStamp);
                    int zVal = (int) mCurCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if (index >= 0)
                        mCaptionDataListClone.get(index).setInPoint(timeStamp);

                    seekMultiThumbnailSequenceView();
                }
            }
        });
        timelineTimeSpan.setOnChangeListener(new NvsTimelineTimeSpan.OnTrimOutChangeListener() {
            @Override
            public void onChange(long timeStamp, boolean isDragEnd) {
                //outPoint是开区间，seekTimeline时，需要往前平移一帧即0.04秒，转换成微秒即40000微秒
                seekTimeline(timeStamp - 40000);
                updatePlaytimeText(timeStamp);
                mVideoFragment.changeCaptionRectVisible();
                if (isDragEnd && mCurCaption != null) {
                    Logger.e(TAG, "TrimInChange5454->" + timeStamp);
                    mCurCaption.changeOutPoint(timeStamp);
                    int zVal = (int) mCurCaption.getZValue();
                    int index = getCaptionIndex(zVal);
                    if (index >= 0) {
                        mCaptionDataListClone.get(index).setOutPoint(timeStamp);
                    }

                    seekMultiThumbnailSequenceView();
                }
            }
        });

        return timelineTimeSpan;
    }

    private void seekMultiThumbnailSequenceView() {
        if (mMultiSequenceView != null) {
            long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
            long duration = mTimeline.getDuration();
            mMultiSequenceView.scrollTo(Math.round(((float) curPos) / (float) duration * mTimelineEditor.getSequenceWidth()), 0);
        }
    }

    private void addAllTimeSpan() {
        NvsTimelineCompoundCaption caption = mTimeline.getFirstCompoundCaption();
        while (caption != null) {
            long inPoint = caption.getInPoint();
            long outPoint = caption.getOutPoint();
            NvsTimelineTimeSpan timeSpan = addTimeSpan(inPoint, outPoint);
            if (timeSpan != null) {
                CaptionTimeSpanInfo timeSpanInfo = new CaptionTimeSpanInfo(caption, timeSpan);
                mTimeSpanInfoList.add(timeSpanInfo);
            }
            caption = mTimeline.getNextCaption(caption);
        }
    }

    private void selectCaption() {
        long curPos = mStreamingContext.getTimelineCurrentPosition(mTimeline);
        List<NvsTimelineCompoundCaption> captionList = mTimeline.getCompoundCaptionsByTimelinePosition(curPos);
        Logger.e(TAG, "captionList => " + captionList.size());
        int captionCount = captionList.size();
        if (captionCount > 0) {
            float zVal = captionList.get(0).getZValue();
            int index = 0;
            for (int i = 0; i < captionCount; i++) {
                float tmpZVal = captionList.get(i).getZValue();
                if (tmpZVal > zVal) {
                    zVal = tmpZVal;
                    index = i;
                }
            }
            mCurCaption = captionList.get(index);
            mModifyButton.setVisibility(View.VISIBLE);
        } else {
            mCurCaption = null;
            mModifyButton.setVisibility(View.GONE);
        }
    }

    private void selectTimeSpan() {
        for (int i = 0; i < mTimeSpanInfoList.size(); i++) {
            if (mCurCaption != null &&
                    mTimeSpanInfoList.get(i).mCaption == mCurCaption) {
                mTimelineEditor.selectTimeSpan(mTimeSpanInfoList.get(i).mTimeSpan);
                break;
            }
        }
    }

    private void deleteCurCaptionTimeSpan(NvsTimelineCompoundCaption delCaption) {
        for (int i = 0; i < mTimeSpanInfoList.size(); i++) {
            if (mTimeSpanInfoList.get(i).mCaption == delCaption) {
                mTimelineEditor.deleteSelectedTimeSpan(mTimeSpanInfoList.get(i).mTimeSpan);
                mTimeSpanInfoList.remove(i);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        mStreamingContext.stop();
        removeTimeline();
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void removeTimeline() {
        TimelineUtil.removeTimeline(mTimeline);
        mTimeline = null;
        m_handler.removeCallbacksAndMessages(null);
    }

    private class CaptionTimeSpanInfo {
        public NvsTimelineCompoundCaption mCaption;
        public NvsTimelineTimeSpan mTimeSpan;

        public CaptionTimeSpanInfo(NvsTimelineCompoundCaption caption, NvsTimelineTimeSpan timeSpan) {
            this.mCaption = caption;
            this.mTimeSpan = timeSpan;
        }
    }

    private int getCaptionIndex(int curZValue) {
        int index = -1;
        int count = mCaptionDataListClone.size();
        for (int i = 0; i < count; ++i) {
            int zVal = mCaptionDataListClone.get(i).getCaptionZVal();
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
        mModifyButton.setClickable(true);
    }
}
