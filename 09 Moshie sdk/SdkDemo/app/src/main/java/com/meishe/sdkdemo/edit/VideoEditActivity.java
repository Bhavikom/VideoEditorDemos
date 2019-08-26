package com.meishe.sdkdemo.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.edit.Caption.CaptionActivity;
import com.meishe.sdkdemo.edit.adapter.AssetRecyclerViewAdapter;
import com.meishe.sdkdemo.edit.adapter.SpaceItemDecoration;
import com.meishe.sdkdemo.edit.animatesticker.AnimateStickerActivity;
import com.meishe.sdkdemo.edit.clipEdit.EditActivity;
import com.meishe.sdkdemo.edit.compoundcaption.CompoundCaptionActivity;
import com.meishe.sdkdemo.edit.data.AssetInfoDescription;
import com.meishe.sdkdemo.edit.data.BackupData;
import com.meishe.sdkdemo.edit.data.BitmapData;
import com.meishe.sdkdemo.edit.filter.FilterActivity;
import com.meishe.sdkdemo.edit.interfaces.OnItemClickListener;
import com.meishe.sdkdemo.edit.interfaces.OnTitleBarClickListener;
import com.meishe.sdkdemo.edit.music.MusicActivity;
import com.meishe.sdkdemo.edit.record.RecordActivity;
import com.meishe.sdkdemo.edit.theme.ThemeActivity;
import com.meishe.sdkdemo.edit.transition.TransitionActivity;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.edit.watermark.WaterMarkActivity;
import com.meishe.sdkdemo.edit.watermark.WaterMarkUtil;
import com.meishe.sdkdemo.interfaces.TipsButtonClickListener;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.dataInfo.CaptionInfo;
import com.meishe.sdkdemo.utils.dataInfo.CompoundCaptionInfo;
import com.meishe.sdkdemo.utils.dataInfo.MusicInfo;
import com.meishe.sdkdemo.utils.dataInfo.RecordAudioInfo;
import com.meishe.sdkdemo.utils.dataInfo.StickerInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;
import com.meishe.sdkdemo.utils.dataInfo.TransitionInfo;
import com.meishe.sdkdemo.utils.dataInfo.VideoClipFxInfo;

import java.util.ArrayList;
import java.util.List;

import static com.meishe.sdkdemo.utils.Constants.VIDEOVOLUME_MAXSEEKBAR_VALUE;
import static com.meishe.sdkdemo.utils.Constants.VIDEOVOLUME_MAXVOLUMEVALUE;

/**
 * VideoEditActivity class
 *
 * @author czl
 * @date 2018-05-28
 */
public class VideoEditActivity extends BaseActivity {
    private static final String TAG = "VideoEditActivity";
    public static final int REQUESTRESULT_THEME = 1001;
    public static final int REQUESTRESULT_EDIT = 1002;
    public static final int REQUESTRESULT_FILTER = 1003;
    public static final int REQUESTRESULT_STICKER = 1004;
    public static final int REQUESTRESULT_CAPTION = 1005;
    public static final int REQUESTRESULT_TRANSITION = 1006;
    public static final int REQUESTRESULT_MUSIC = 1007;
    public static final int REQUESTRESULT_RECORD = 1008;
    public static final int REQUESTRESULT_WATERMARK = 1009;
    public static final int REQUESTRESULT_COMPOUND_CAPTION = 1010;

    private CustomTitleBar mTitleBar;

    private RelativeLayout mBottomLayout;
    private RecyclerView mAssetRecycleView;
    private AssetRecyclerViewAdapter mAssetRecycleAdapter;
    private ArrayList<AssetInfoDescription> mArrayAssetInfo;
    private LinearLayout mVolumeUpLayout;
    private SeekBar mVideoVoiceSeekBar;
    private SeekBar mMusicVoiceSeekBar;
    private SeekBar mDubbingSeekBarSeekBar;
    private TextView mVideoVoiceSeekBarValue;
    private TextView mMusicVoiceSeekBarValue;
    private TextView mDubbingSeekBarSeekBarValue;
    private ImageView mSetVoiceFinish;
    private RelativeLayout mCompilePage;

    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private NvsVideoTrack mVideoTrack;
    private NvsAudioTrack mMusicTrack;
    private NvsAudioTrack mRecordAudioTrack;
    private VideoFragment mVideoFragment;
    private CompileVideoFragment mCompileVideoFragment;
    private boolean m_waitFlag = false;

    int[] videoEditImageId = {
            R.mipmap.icon_edit_theme,
            R.mipmap.icon_edit_edit,
            R.mipmap.icon_edit_filter,
            R.mipmap.icon_edit_sticker,
            R.mipmap.icon_edit_caption,
            R.mipmap.icon_compound_caption,
            R.mipmap.icon_watermark,
            R.mipmap.icon_edit_transition,
            R.mipmap.icon_edit_music,
            R.mipmap.icon_edit_voice
    };

    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_video_edit;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mAssetRecycleView = (RecyclerView) findViewById(R.id.assetRecycleList);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        mVolumeUpLayout = (LinearLayout) findViewById(R.id.volumeUpLayout);
        mVideoVoiceSeekBar = (SeekBar) findViewById(R.id.videoVoiceSeekBar);
        mMusicVoiceSeekBar = (SeekBar) findViewById(R.id.musicVoiceSeekBar);
        mDubbingSeekBarSeekBar = (SeekBar) findViewById(R.id.dubbingSeekBar);
        mVideoVoiceSeekBarValue = (TextView) findViewById(R.id.videoVoiceSeekBarValue);
        mMusicVoiceSeekBarValue = (TextView) findViewById(R.id.musicVoiceSeekBarValue);
        mDubbingSeekBarSeekBarValue = (TextView) findViewById(R.id.dubbingSeekBarValue);
        mSetVoiceFinish = (ImageView) findViewById(R.id.finish);
        mCompilePage = (RelativeLayout) findViewById(R.id.compilePage);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.videoEdit);
        mTitleBar.setTextRight(R.string.compile);
        mTitleBar.setTextRightVisible(View.VISIBLE);
    }

    @Override
    protected void initData() {
        mTimeline = TimelineUtil.createTimeline();
        if (mTimeline == null) {
            return;
        }
        mVideoTrack = mTimeline.getVideoTrackByIndex(0);
        if (mVideoTrack == null) {
            return;
        }

        initVideoFragment();
        initCompileVideoFragment();
        initAssetInfo();
        initAssetRecycleAdapter();
        initVoiceSeekBar();
        loadVideoClipFailTips();
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_waitFlag = false;
        if (mTimeline != null) {
            mMusicTrack = mTimeline.getAudioTrackByIndex(0);
            mRecordAudioTrack = mTimeline.getAudioTrackByIndex(1);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeTimeline();
        clearData();
        AppManager.getInstance().finishActivity();
    }

    private void loadVideoClipFailTips() {
        //导入视频无效，提示
        if (mTimeline == null || (mTimeline != null && mTimeline.getDuration() <= 0)) {
            String[] versionName = getResources().getStringArray(R.array.clip_load_failed_tips);
            Util.showDialog(VideoEditActivity.this, versionName[0], versionName[1], new TipsButtonClickListener() {
                @Override
                public void onTipsButtoClick(View view) {
                    removeTimeline();
                    AppManager.getInstance().finishActivity();
                }
            });
        }
    }

    /**
     * 清空数据
     */
    private void clearData() {
        TimelineData.instance().clear();
        BackupData.instance().clear();
        BitmapData.instance().clear();
    }

    private void removeTimeline() {
        TimelineUtil.removeTimeline(mTimeline);
        mTimeline = null;
    }

    private void initVoiceSeekBar() {
        mVideoVoiceSeekBar.setMax(VIDEOVOLUME_MAXSEEKBAR_VALUE);
        mMusicVoiceSeekBar.setMax(VIDEOVOLUME_MAXSEEKBAR_VALUE);
        mDubbingSeekBarSeekBar.setMax(VIDEOVOLUME_MAXSEEKBAR_VALUE);
        if (mVideoTrack == null) {
            return;
        }
        int volumeVal = (int) Math.floor(mVideoTrack.getVolumeGain().leftVolume / VIDEOVOLUME_MAXVOLUMEVALUE * VIDEOVOLUME_MAXSEEKBAR_VALUE + 0.5D);
        updateVideoVoiceSeekBar(volumeVal);
        updateMusicVoiceSeekBar(volumeVal);
        updateDubbingVoiceSeekBar(volumeVal);
    }

    private void updateVideoVoiceSeekBar(int volumeVal) {
        mVideoVoiceSeekBar.setProgress(volumeVal);
        mVideoVoiceSeekBarValue.setText(String.valueOf(volumeVal));
    }

    private void updateMusicVoiceSeekBar(int volumeVal) {
        mMusicVoiceSeekBar.setProgress(volumeVal);
        mMusicVoiceSeekBarValue.setText(String.valueOf(volumeVal));
    }

    private void updateDubbingVoiceSeekBar(int volumeVal) {
        mDubbingSeekBarSeekBar.setProgress(volumeVal);
        mDubbingSeekBarSeekBarValue.setText(String.valueOf(volumeVal));
    }

    private void setVideoVoice(int voiceVal) {
        if (mVideoTrack == null) {
            return;
        }
        updateVideoVoiceSeekBar(voiceVal);
        float volumeVal = voiceVal * VIDEOVOLUME_MAXVOLUMEVALUE / VIDEOVOLUME_MAXSEEKBAR_VALUE;
        mVideoTrack.setVolumeGain(volumeVal, volumeVal);
        TimelineData.instance().setOriginVideoVolume(volumeVal);
    }

    private void setMusicVoice(int voiceVal) {
        if (mMusicTrack == null) {
            return;
        }
        updateMusicVoiceSeekBar(voiceVal);
        float volumeVal = voiceVal * VIDEOVOLUME_MAXVOLUMEVALUE / VIDEOVOLUME_MAXSEEKBAR_VALUE;
        mMusicTrack.setVolumeGain(volumeVal, volumeVal);
        TimelineData.instance().setMusicVolume(volumeVal);
    }

    private void setDubbingVoice(int voiceVal) {
        if (mRecordAudioTrack == null) {
            return;
        }
        updateDubbingVoiceSeekBar(voiceVal);
        float volumeVal = voiceVal * VIDEOVOLUME_MAXVOLUMEVALUE / VIDEOVOLUME_MAXSEEKBAR_VALUE;
        mRecordAudioTrack.setVolumeGain(volumeVal, volumeVal);
        TimelineData.instance().setRecordVolume(volumeVal);
    }

    private void initVideoFragment() {
        mVideoFragment = new VideoFragment();
        mVideoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mVideoFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), 0);
            }
        });
        mVideoFragment.setTimeline(mTimeline);
        mVideoFragment.setAutoPlay(true);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight", mBottomLayout.getLayoutParams().height);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        bundle.putBoolean("playBarVisible", true);
        bundle.putBoolean("voiceButtonVisible", true);
        mVideoFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.video_layout, mVideoFragment).commit();
        getFragmentManager().beginTransaction().show(mVideoFragment);
    }

    private void initCompileVideoFragment() {
        mCompileVideoFragment = new CompileVideoFragment();
        mCompileVideoFragment.setTimeline(mTimeline);
        getFragmentManager().beginTransaction().add(R.id.compilePage, mCompileVideoFragment).commit();
        getFragmentManager().beginTransaction().show(mCompileVideoFragment);
    }

    private void initAssetInfo() {
        mArrayAssetInfo = new ArrayList<>();
        String[] assetName = getResources().getStringArray(R.array.videoEdit);
        for (int i = 0; i < assetName.length; i++) {
            mArrayAssetInfo.add(new AssetInfoDescription(assetName[i], videoEditImageId[i]));
        }
    }

    private void initAssetRecycleAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(VideoEditActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mAssetRecycleView.setLayoutManager(layoutManager);
        mAssetRecycleAdapter = new AssetRecyclerViewAdapter(VideoEditActivity.this);
        mAssetRecycleAdapter.updateData(mArrayAssetInfo);
        mAssetRecycleView.setAdapter(mAssetRecycleAdapter);
        mAssetRecycleView.addItemDecoration(new SpaceItemDecoration(8, 8));
        mAssetRecycleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (m_waitFlag) {
                    return;
                }
                mStreamingContext.stop();
                String tag = (String) view.getTag();
                if (tag.equals(getStringResourse(R.string.theme))) {
                    onItemClickToActivity(ThemeActivity.class, VideoEditActivity.REQUESTRESULT_THEME);
                } else if (tag.equals(getStringResourse(R.string.edit))) {
                    onItemClickToActivity(EditActivity.class, VideoEditActivity.REQUESTRESULT_EDIT);
                } else if (tag.equals(getStringResourse(R.string.filter))) {
                    onItemClickToActivity(FilterActivity.class, VideoEditActivity.REQUESTRESULT_FILTER);
                } else if (tag.equals(getStringResourse(R.string.animatedSticker))) {
                    onItemClickToActivity(AnimateStickerActivity.class, VideoEditActivity.REQUESTRESULT_STICKER);
                } else if (tag.equals(getStringResourse(R.string.caption))) {
                    onItemClickToActivity(CaptionActivity.class, VideoEditActivity.REQUESTRESULT_CAPTION);
                } else if (tag.equals(getStringResourse(R.string.comcaption))) {
                    onItemClickToActivity(CompoundCaptionActivity.class, VideoEditActivity.REQUESTRESULT_COMPOUND_CAPTION);
                } else if (tag.equals(getStringResourse(R.string.watermark))) {
                    onItemClickToActivity(WaterMarkActivity.class, VideoEditActivity.REQUESTRESULT_WATERMARK);
                } else if (tag.equals(getStringResourse(R.string.transition))) {
                    if (mTimeline != null) {
                        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
                        if (videoTrack != null) {
                            int clipCount = videoTrack.getClipCount();
                            if (clipCount <= 1) {
                                String[] transitionTipsInfo = getResources().getStringArray(R.array.transition_tips);
                                Util.showDialog(VideoEditActivity.this, transitionTipsInfo[0], transitionTipsInfo[1]);
                                return;
                            }
                        }
                    }
                    onItemClickToActivity(TransitionActivity.class, VideoEditActivity.REQUESTRESULT_TRANSITION);
                } else if (tag.equals(getStringResourse(R.string.music))) {
                    onItemClickToActivity(MusicActivity.class, VideoEditActivity.REQUESTRESULT_MUSIC);
                } else if (tag.equals(getStringResourse(R.string.dub))) {
                    onItemClickToActivity(RecordActivity.class, VideoEditActivity.REQUESTRESULT_RECORD);
                } else {
                    String[] tipsInfo = getResources().getStringArray(R.array.edit_function_tips);
                    Util.showDialog(VideoEditActivity.this, tipsInfo[0], tipsInfo[1], tipsInfo[2]);
                }
            }
        });
    }

    private void onItemClickToActivity(Class<? extends Activity> cls, int requstcode) {
        m_waitFlag = true;
        AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(), cls, null, requstcode);
    }

    private String getStringResourse(int id) {
        return getApplicationContext().getResources().getString(id);
    }

    @Override
    protected void initListener() {
        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                removeTimeline();
                clearData();
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
        mVideoVoiceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setVideoVoice(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mMusicVoiceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setMusicVoice(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mDubbingSeekBarSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setDubbingVoice(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSetVoiceFinish.setOnClickListener(this);
        mCompilePage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        if (mCompileVideoFragment != null) {
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

        if (mVideoFragment != null) {
            mVideoFragment.setVideoVolumeListener(new VideoFragment.VideoVolumeListener() {
                @Override
                public void onVideoVolume() {
                    mVolumeUpLayout.setVisibility(View.VISIBLE);
                }
            });
        }

        mVolumeUpLayout.setOnTouchListener(new View.OnTouchListener() {
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
            case R.id.finish:
                mVolumeUpLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case REQUESTRESULT_THEME:
                String themeId = TimelineData.instance().getThemeData();
                TimelineUtil.applyTheme(mTimeline, themeId);
                //重新添加字幕，防止某些主题会删除字幕
                updateCaption();
                mVideoFragment.playVideoButtonCilck();
                break;
            case REQUESTRESULT_EDIT:
                TimelineUtil.reBuildVideoTrack(mTimeline);
                break;
            case REQUESTRESULT_FILTER:
                VideoClipFxInfo videoClipFxInfo = TimelineData.instance().getVideoClipFxData();
                TimelineUtil.buildTimelineFilter(mTimeline, videoClipFxInfo);
                break;
            case REQUESTRESULT_STICKER:
                ArrayList<StickerInfo> stickerArray = TimelineData.instance().getStickerData();
                TimelineUtil.setSticker(mTimeline, stickerArray);
                break;
            case REQUESTRESULT_CAPTION:
                updateCaption();
                break;
            case REQUESTRESULT_COMPOUND_CAPTION:
                updateCompoundCaption();
                break;
            case REQUESTRESULT_TRANSITION:
                TransitionInfo transitionInfo = TimelineData.instance().getTransitionData();
                TimelineUtil.setTransition(mTimeline, transitionInfo);
                break;
            case REQUESTRESULT_MUSIC:
                List<MusicInfo> musicInfos = TimelineData.instance().getMusicData();
                TimelineUtil.buildTimelineMusic(mTimeline, musicInfos);
                break;
            case REQUESTRESULT_RECORD:
                Logger.e(TAG, "录音界面");
                ArrayList<RecordAudioInfo> audioInfos = TimelineData.instance().getRecordAudioData();
                TimelineUtil.buildTimelineRecordAudio(mTimeline, audioInfos);
                break;
            case REQUESTRESULT_WATERMARK:
                Logger.e(TAG, "水印界面");
                TimelineUtil.checkAndDeleteExitFX(mTimeline);
                boolean cleanWaterMark = data.getBooleanExtra(WaterMarkActivity.WATER_CLEAN, true);
                if (cleanWaterMark) {
                    mTimeline.deleteWatermark();
                } else {
                    WaterMarkUtil.setWaterMark(mTimeline, TimelineData.instance().getWaterMarkData());
                }
                break;
            default:
                break;
        }
        mVideoFragment.updateTotalDuarationText();
    }

    private void updateCaption() {
        ArrayList<CaptionInfo> captionArray = TimelineData.instance().getCaptionData();
        TimelineUtil.setCaption(mTimeline, captionArray);
    }

    private void updateCompoundCaption(){
        ArrayList<CompoundCaptionInfo> captionArray = TimelineData.instance().getCompoundCaptionArray();
        TimelineUtil.setCompoundCaption(mTimeline, captionArray);
    }
}


