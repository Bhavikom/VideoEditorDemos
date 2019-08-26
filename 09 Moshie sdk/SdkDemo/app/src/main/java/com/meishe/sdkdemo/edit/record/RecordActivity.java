package com.meishe.sdkdemo.edit.record;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsAudioClip;
import com.meicam.sdk.NvsAudioFx;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.MSApplication;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.edit.VideoFragment;
import com.meishe.sdkdemo.edit.interfaces.OnTitleBarClickListener;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.edit.view.VerticalSeekBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.PathUtils;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.TimeFormatUtil;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.dataInfo.RecordAudioInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ms on 2018/8/8 0012.
 */

public class RecordActivity extends BaseActivity {
    private final String TAG = "RecordActivity";
    private final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 100;
    private final int SEEK_TYPE_NULL = 0;
    private final int SEEK_TYPE_DRAG = 1;
    private final int SEEK_TYPE_PLAY = 2;
    private VideoFragment m_videoFragment;
    private CustomTitleBar m_titleBar;
    private RelativeLayout m_bottomLayout;
    private NvsStreamingContext m_streamingContext;
    private NvsTimeline m_timeLine;
    private SqLayout m_sequenceLayout;
    private SqView m_sequenceView;
    private RelativeLayout m_playBtnLayout, m_zoomInBtn, m_zoomOutBtn;
    private Button m_recordFxBtn, m_playBtn, m_recordBeginBtn, m_recordStopBtn, m_recordDelBtn, m_fxOkBtn, m_okBtn;
    private TextView m_playCurTime;
    private RelativeLayout m_recordFxLayout;
    private RecyclerView m_recordFxRv;
    private VerticalSeekBar m_recordVolumeSeekBar;
    private int m_seekTimeline = SEEK_TYPE_PLAY;
    private RecordFxAdapter m_recordFxAdapter;
    private List<RecordFxListItem> mDataList = new ArrayList<>();

    // 录音相关
    private long m_minRecordDuration = 1000000;
    private boolean m_isRecording = false, m_isSelectFx = false;
    private long m_curRecordInPoint, m_curPositon;
    private RecordManager m_recordManager;
    private RecordAudioInfo m_recordAudioInfo;
    private Map<Long, RecordAudioInfo> m_audioInfoList = new HashMap<>();
    private NvsAudioTrack m_recordAudioTrack;
    private NotificationManager m_notificationManager;
    private boolean m_haveRecordPermission = false;

    @Override
    protected int initRootView() {
        m_streamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_record;
    }

    @Override
    protected void initViews() {
        m_titleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        m_bottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        m_sequenceLayout = (SqLayout) findViewById(R.id.sq_view);
        m_playBtnLayout = (RelativeLayout) findViewById(R.id.play_btn_layout);
        m_zoomInBtn = (RelativeLayout) findViewById(R.id.zoom_in_btn);
        m_zoomOutBtn = (RelativeLayout) findViewById(R.id.zoom_out_btn);
        m_recordFxBtn = (Button) findViewById(R.id.recordFxButton);
        m_playBtn = (Button) findViewById(R.id.play_btn);
        m_playCurTime = (TextView) findViewById(R.id.play_cur_time);
        m_recordBeginBtn = (Button) findViewById(R.id.record_begin_btn);
        m_recordStopBtn = (Button) findViewById(R.id.record_stop_btn);
        m_recordDelBtn = (Button) findViewById(R.id.record_del_btn);
        m_recordFxLayout = (RelativeLayout) findViewById(R.id.record_fx_layout);
        m_fxOkBtn = (Button) findViewById(R.id.fx_ok_btn);
        m_recordFxRv = (RecyclerView) findViewById(R.id.record_fx_rv);
        m_okBtn = (Button) findViewById(R.id.ok_btn);
        m_recordVolumeSeekBar = (VerticalSeekBar) findViewById(R.id.record_volume_seekBar);
        m_recordVolumeSeekBar.setMaxProgress(100);
        m_recordVolumeSeekBar.setProgress((int) (1.0f / 3 * 100));
        m_recordVolumeSeekBar.setThumbSizeDp(13, 13);
        m_recordVolumeSeekBar.setmInnerProgressWidthDp(3);
        m_recordVolumeSeekBar.setUnSelectColor(Color.parseColor("#979797"));
        m_recordVolumeSeekBar.setSelectColor(Color.parseColor("#4A90E2"));
    }

    @Override
    protected void initTitle() {
        m_titleBar.setTextCenter(R.string.dub);
        m_titleBar.setBackImageVisible(View.GONE);
    }

    @Override
    protected void initData() {
        initVideoFragment();
        updateSequenceView();
        getRecordFxList();
        m_recordManager = RecordManager.getInstance();
        updatePlaytimeText(0);

        ArrayList<RecordAudioInfo> recordAudioInfos = TimelineData.instance().getRecordAudioData();
        if(recordAudioInfos != null) {
            for(int i = 0; i < recordAudioInfos.size(); ++i) {
                RecordAudioInfo audioInfo = recordAudioInfos.get(i);
                if(audioInfo == null) {
                    continue;
                }
                m_sequenceLayout.addRecordView(audioInfo.getInPoint(), audioInfo.getOutPoint());
                m_audioInfoList.put(audioInfo.getInPoint(), audioInfo);

                // 如果首帧就有录音，那么要显示设置录音的选项(因为刚刚进入到页面画面停在首帧)
                if(audioInfo.getInPoint() == 0) {
                    haveRecordArea(0);
                }
            }
        }

        // 静音权限
        silenceRequest();

        // 录音权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)) {
                m_haveRecordPermission = true;
            } else {
                requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION_CODE);
            }
        } else {
            m_haveRecordPermission = true;
        }
    }

    @Override
    protected void initListener() {
        m_titleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                stopRecording();
                removeTimeline();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
            }

            @Override
            public void OnCenterTextClick() {

            }

            @Override
            public void OnRightTextClick() {

            }
        });

        m_zoomInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_seekTimeline = SEEK_TYPE_NULL;
                m_sequenceView.zoomInSequence();
                m_sequenceLayout.reLayoutAllViews();
            }
        });

        m_zoomOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_seekTimeline = SEEK_TYPE_NULL;
                m_sequenceView.zoomOutSequence();
                m_sequenceLayout.reLayoutAllViews();
            }
        });

        m_playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo();
            }
        });

        m_videoFragment.setVideoFragmentCallBack(new VideoFragment.VideoFragmentListener() {
            @Override
            public void playBackEOF(NvsTimeline timeline) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if(m_isRecording) {
                            m_recordManager.release();
                        }
                        if(m_isSelectFx) {
                            m_sequenceView.scrollTo(m_curPositon);
                        } else {
                            m_sequenceView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                        }
                        // 如果从0开始就有录音，则选中
                        for(int i = 0; i < m_recordAudioTrack.getClipCount(); ++i) {
                            NvsAudioClip audioClip = m_recordAudioTrack.getClipByIndex(i);
                            if(audioClip == null) {
                                continue;
                            }
                            if(audioClip.getInPoint() == 0) {
                                haveRecordArea(0);
                                break;
                            }
                        }
                    }
                });
            }

            @Override
            public void playStopped(NvsTimeline timeline) {
            }

            @Override
            public void playbackTimelinePosition(NvsTimeline timeline, long stamp) {
                updatePlaytimeText(stamp);
                if(m_sequenceView != null){
                    int x = Math.round((stamp / (float) m_timeLine.getDuration() * m_sequenceView.getSequenceWidth()));
                    m_sequenceView.smoothScrollTo(x, 0);
                }
                if(m_isRecording) {
                    m_sequenceLayout.updateRecordView(m_curRecordInPoint, stamp);
                    if(stamp - m_curRecordInPoint >= m_minRecordDuration) {
                        m_recordStopBtn.setEnabled(true);
                    }
                }
            }

            @Override
            public void streamingEngineStateChanged(int state) {
                if(NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK == state){
                    m_seekTimeline = SEEK_TYPE_PLAY;
                    m_playBtn.setBackgroundResource(R.mipmap.icon_edit_pause);
                }else{
                    m_seekTimeline = SEEK_TYPE_NULL;
                    m_playBtn.setBackgroundResource(R.mipmap.icon_edit_play);
                }
            }
        });

        m_sequenceLayout.setHorizontalScrollListener(new SqLayout.HorizontalScrollListener() {
            @Override
            public void horizontalScrollStoped() {

            }

            @Override
            public void horizontalScrollChanged(long inPoint, boolean isDraging, long cur_audio_inpoint) {
                if(isDraging) {
                    m_seekTimeline = SEEK_TYPE_DRAG;
                }
                if(m_seekTimeline == SEEK_TYPE_DRAG || m_seekTimeline == SEEK_TYPE_PLAY) {
                    if(!m_isRecording) {
                        if(cur_audio_inpoint != -1) {
                            haveRecordArea(cur_audio_inpoint);
                        } else {
                            m_recordBeginBtn.setVisibility(View.VISIBLE);
                            m_recordDelBtn.setVisibility(View.GONE);
                            m_recordStopBtn.setVisibility(View.GONE);
                            m_recordFxBtn.setVisibility(View.GONE);
                            m_recordVolumeSeekBar.setVisibility(View.GONE);
                        }
                    }
                }
                if(m_seekTimeline == SEEK_TYPE_DRAG) {
                    m_videoFragment.seekTimeline(inPoint, 0);
                    updatePlaytimeText(inPoint);
                }
            }
        });

        m_recordBeginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!silenceRequest()) {
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(RecordActivity.this, android.Manifest.permission.RECORD_AUDIO)) {
                        m_haveRecordPermission = true;
                    } else {
                        requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION_CODE);
                        return;
                    }
                } else {
                    m_haveRecordPermission = true;
                }
                if(m_haveRecordPermission) {
                    m_recordManager.prepareAudio(PathUtils.getAudioRecordFilePath());
                }
            }
        });

        m_recordStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!silenceRequest()) {
                    return;
                }
                m_recordManager.release();
            }
        });

        m_recordDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_recordBeginBtn.setVisibility(View.VISIBLE);
                m_recordStopBtn.setVisibility(View.GONE);
                m_recordDelBtn.setVisibility(View.GONE);
                m_recordFxBtn.setVisibility(View.GONE);
                m_recordVolumeSeekBar.setVisibility(View.GONE);
                deleteAudioClip(m_curRecordInPoint);
            }
        });

        m_recordFxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_curPositon = m_streamingContext.getTimelineCurrentPosition(m_timeLine);
                m_recordFxLayout.setVisibility(View.VISIBLE);
                m_bottomLayout.setVisibility(View.GONE);
                if(m_recordAudioTrack != null) {
                    for(int i = 0; i < m_recordAudioTrack.getClipCount(); ++i) {
                        NvsAudioClip audioClip = m_recordAudioTrack.getClipByIndex(i);
                        if(audioClip == null) {
                            continue;
                        }
                        if(audioClip.getInPoint() == m_curRecordInPoint) {
                            NvsAudioFx fx  = audioClip.getFxByIndex(0);
                            if(fx == null || fx.getBuiltinAudioFxName() == null || fx.getBuiltinAudioFxName().isEmpty()) {
                                m_recordFxAdapter.setItemSelectedByFxID(Constants.NO_FX);
                            } else {
                                m_recordFxAdapter.setItemSelectedByFxID(fx.getBuiltinAudioFxName());
                            }
                            break;
                        }
                    }
                }
            }
        });

        m_fxOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_isSelectFx = false;
                m_recordFxLayout.setVisibility(View.GONE);
                m_bottomLayout.setVisibility(View.VISIBLE);
                m_sequenceView.scrollTo(m_curPositon);
            }
        });

        m_recordManager.setOnRecordStart(new RecordManager.OnRecordStartListener() {
            @Override
            public void onRecordStart(Long id, String filePath) {
                doOnRecordStart(id, filePath);
            }

            @Override
            public void onRecordEnd() {
                doOnRecordEnd();
            }
        });

        m_okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
                TimelineData.instance().setRecordAudioData(new ArrayList<>(m_audioInfoList.values()));
                removeTimeline();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                AppManager.getInstance().finishActivity();
            }
        });

        m_recordVolumeSeekBar.setOnSlideChangeListener(new VerticalSeekBar.SlideChangeListener() {
            @Override
            public void onStart(VerticalSeekBar slideView, int progress) {

            }

            @Override
            public void onProgress(VerticalSeekBar slideView, int progress) {
                float value = (float) progress / 100 * 3;
                Log.e("===>", "record volume: " + value);
                setAudioClipVolume(m_curRecordInPoint, value);
            }

            @Override
            public void onStop(VerticalSeekBar slideView, int progress) {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        stopRecording();
        removeTimeline();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        AppManager.getInstance().finishActivity();
    }

    // 静音权限
    private boolean silenceRequest() {
        m_notificationManager = (NotificationManager) MSApplication.getmContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(m_notificationManager != null && !m_notificationManager.isNotificationPolicyAccessGranted()) {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MSApplication.getmContext().startActivity(intent);
                return false;
            }
        }
        return true;
    }

    private void stopRecording() {
        if(m_isRecording) {
            m_recordManager.release();
        }
    }

    private void haveRecordArea(long cur_audio_inpoint) {
        m_curRecordInPoint = cur_audio_inpoint;
        m_recordDelBtn.setVisibility(View.VISIBLE);
        m_recordFxBtn.setVisibility(View.VISIBLE);
        m_recordBeginBtn.setVisibility(View.GONE);
        m_recordStopBtn.setVisibility(View.GONE);
        float volume = getAudioClipVolume(m_curRecordInPoint);
        if(volume != -1) {
            m_recordVolumeSeekBar.setVisibility(View.VISIBLE);
            m_recordVolumeSeekBar.setProgress((int) (volume / 3 * 100));
        } else {
            m_recordVolumeSeekBar.setVisibility(View.GONE);
        }
    }

    private void setRecordState(boolean is_recording) {
        if(is_recording) {
            m_recordBeginBtn.setVisibility(View.GONE);
            m_recordDelBtn.setVisibility(View.GONE);
            m_recordFxBtn.setVisibility(View.GONE);
            m_recordStopBtn.setVisibility(View.VISIBLE);
            m_recordStopBtn.setEnabled(false);
        } else {
            m_recordBeginBtn.setVisibility(View.VISIBLE);
            m_recordStopBtn.setVisibility(View.GONE);
            m_recordDelBtn.setVisibility(View.GONE);
            m_recordFxBtn.setVisibility(View.GONE);
        }
        // 禁掉界面的其他操作
        m_playBtn.setEnabled(!is_recording);
        m_recordFxBtn.setEnabled(!is_recording);
        m_zoomInBtn.setEnabled(!is_recording);
        m_zoomOutBtn.setEnabled(!is_recording);
        m_sequenceLayout.setTouchEnabled(is_recording);
        m_videoFragment.setRecording(is_recording);
    }

    private void removeTimeline(){
        TimelineUtil.removeTimeline(m_timeLine);
        m_timeLine = null;
    }

    private void playVideo(){
        if(m_videoFragment.getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK){
            long startTime = m_streamingContext.getTimelineCurrentPosition(m_timeLine);
            long endTime = m_timeLine.getDuration();
            m_videoFragment.playVideo(startTime,endTime);
        }else{
            m_videoFragment.stopEngine();
        }
    }

    private void getRecordFxList() {
        if (m_recordFxAdapter == null) {
            if (m_streamingContext == null) {
                return;
            }
            RecordFxListItem item_none = new RecordFxListItem();
            item_none.fxID = Constants.NO_FX;
            item_none.fxName = "无";
            item_none.image_drawable = ContextCompat.getDrawable(this, R.drawable.record_none_fx);
            mDataList.add(item_none);

            List<RecordFxListItem> listFxAsset = Util.listRecordFxFromJson(this);
            if (listFxAsset != null) {
                mDataList.addAll(listFxAsset);
            }

            //国际化
            String[] effectList = getResources().getStringArray(R.array.effect_list);
            for (int i = 0; i < effectList.length; i++) {
                mDataList.get(i).fxName = effectList[i];
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            m_recordFxAdapter = new RecordFxAdapter(this, mDataList);
            m_recordFxRv.setLayoutManager(linearLayoutManager);
            m_recordFxRv.setAdapter(m_recordFxAdapter);
            m_recordFxAdapter.setItemSelectedByFxID(Constants.NO_FX);

            m_recordFxAdapter.setOnItemClickListener(new RecordFxAdapter.OnItemClickListener() {
                @Override
                public void fxSelected(int pos, RecordFxListItem audioFxListItem) {
                    if (audioFxListItem == null || audioFxListItem.fxID == null || audioFxListItem.fxID.isEmpty()) {
                        return;
                    }
                    RecordAudioInfo cur_recordAudioInfo= m_audioInfoList.get(m_curRecordInPoint);
                    if(cur_recordAudioInfo == null) {
                        return;
                    }
                    // 找到AudioClip
                    NvsAudioClip nvsAudioClip = null;
                    if(m_recordAudioTrack == null) {
                        return;
                    }
                    for(int i = 0; i < m_recordAudioTrack.getClipCount(); ++i) {
                        nvsAudioClip = m_recordAudioTrack.getClipByIndex(i);
                        if(nvsAudioClip != null && nvsAudioClip.getInPoint() == cur_recordAudioInfo.getInPoint()) {
                            break;
                        }
                    }
                    if(nvsAudioClip == null) {
                        return;
                    }
                    nvsAudioClip.removeFx(0);
                    cur_recordAudioInfo.setFxID(audioFxListItem.fxID);
                    if(!audioFxListItem.fxID.equals(Constants.NO_FX)) {
                        nvsAudioClip.appendFx(audioFxListItem.fxID);
                    }
                    m_isSelectFx = true;
                    m_videoFragment.playVideo(cur_recordAudioInfo.getInPoint(), cur_recordAudioInfo.getOutPoint());
                }
            });
        }
    }

    private void initVideoFragment() {
        m_timeLine = TimelineUtil.createTimeline();
        if(m_timeLine == null)
            return;
        m_recordAudioTrack = m_timeLine.getAudioTrackByIndex(1);
        m_videoFragment = new VideoFragment();
        m_videoFragment.setAutoPlay(false);
        m_videoFragment.setFragmentLoadFinisedListener(new VideoFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                m_videoFragment.seekTimeline(m_streamingContext.getTimelineCurrentPosition(m_timeLine),0);
            }
        });
        m_videoFragment.setTimeline(m_timeLine);
        Bundle m_bundle = new Bundle();
        m_bundle.putInt("titleHeight",m_titleBar.getLayoutParams().height);
        m_bundle.putInt("bottomHeight",m_bottomLayout.getLayoutParams().height);
        m_bundle.putBoolean("playBarVisible",false);
        m_bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        m_videoFragment.setArguments(m_bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.video_layout, m_videoFragment)
                .commit();
        getFragmentManager().beginTransaction().show(m_videoFragment);
    }

    private void updateSequenceView() {
        if (m_timeLine == null)
            return;
        m_sequenceView = m_sequenceLayout.getSqView();
        final ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> infoDescArray = new ArrayList<>();
        NvsVideoTrack videoTrack = m_timeLine.getVideoTrackByIndex(0);
        if (videoTrack == null)
            return;
        for (int i = 0; i < videoTrack.getClipCount(); i++) {
            NvsVideoClip clip = videoTrack.getClipByIndex(i);
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
        double duration = (double) m_timeLine.getDuration();
        int halfScreenWidth = ScreenUtils.getScreenWidth(this) / 2;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) m_playBtnLayout.getLayoutParams();
        int playBtnTotalWidth = layoutParams.width + layoutParams.leftMargin + layoutParams.rightMargin;
        int sequenceLeftPadding = halfScreenWidth - playBtnTotalWidth;
        m_sequenceView.setStartPadding(sequenceLeftPadding);
        m_sequenceView.setEndPadding(halfScreenWidth);
        m_sequenceLayout.initData((long) duration, infoDescArray);
    }

    private void updatePlaytimeText(long playTime){
        long totalDuaration = m_timeLine.getDuration();
        String totalStr = TimeFormatUtil.formatUsToString1(totalDuaration);
        String playTimeStr = TimeFormatUtil.formatUsToString1(playTime);
        String tmpStr = playTimeStr + "/" + totalStr;
        m_playCurTime.setText(tmpStr);
    }

    public void doOnRecordStart(Long id, String filePath) {
        // 静音
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(audioManager != null){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            audioManager.getStreamVolume(AudioManager.STREAM_RING);
            Log.e("===>", "已被静音");
        }

        m_isRecording = true;
        m_curRecordInPoint = m_streamingContext.getTimelineCurrentPosition(m_timeLine);
        m_videoFragment.playVideoButtonCilck(m_curRecordInPoint, m_timeLine.getDuration());
        m_sequenceLayout.addRecordView(m_curRecordInPoint, 0);
        setRecordState(true);

        if(m_recordAudioInfo == null) {
            m_recordAudioInfo = new RecordAudioInfo();
        }
        m_recordAudioInfo.clear();
        m_recordAudioInfo.setId(id);
        m_recordAudioInfo.setPath(filePath);
        m_recordAudioInfo.setInPoint(m_curRecordInPoint);
    }

    public void doOnRecordEnd() {
        // 取消静音
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(audioManager != null){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            audioManager.getStreamVolume(AudioManager.STREAM_RING);
            Log.e("===>", "取消静音");
        }

        m_videoFragment.stopEngine();
        m_isRecording = false;
        setRecordState(false);

        if(m_recordAudioInfo != null) {
            long cur_point = m_streamingContext.getTimelineCurrentPosition(m_timeLine);
            m_recordAudioInfo.setOutPoint(cur_point);
        }
        addAudioClip(m_recordAudioInfo);
    }

    private void addAudioClip(RecordAudioInfo recordInfo) {
        if (recordInfo == null || m_recordAudioTrack == null) {
            return;
        }
        m_audioInfoList.put(recordInfo.getInPoint(), recordInfo.clone());

        // 清除掉重合区域的录音
        List<NvsAudioClip> remove_list = new ArrayList<>();
        for(int i = 0; i < m_recordAudioTrack.getClipCount(); ++i) {
            NvsAudioClip audioClip = m_recordAudioTrack.getClipByIndex(i);
            if(audioClip == null) {
                continue;
            }
            Log.e("===>", "clip in: " + audioClip.getInPoint() + " rr in: " + recordInfo.getInPoint() + " rr out: " + recordInfo.getOutPoint());

            // 全覆盖则删除，部分覆盖则改trimIn
            if(audioClip.getInPoint() < recordInfo.getOutPoint() && audioClip.getInPoint() >=  recordInfo.getInPoint()
                    && audioClip.getOutPoint() <= recordInfo.getOutPoint()) {
                remove_list.add(audioClip);
                Log.e("===>", "remove: " + audioClip.getFilePath() + " " + audioClip.getInPoint());
            } else if(audioClip.getInPoint() < recordInfo.getOutPoint() && audioClip.getInPoint() >=  recordInfo.getInPoint()
                    && audioClip.getOutPoint() > recordInfo.getOutPoint()){
                Log.e("===>", "change trimIn: " + audioClip.getFilePath() + " " + audioClip.getInPoint());

                // 处理数据
                RecordAudioInfo recordAudioInfo = deleteAudioInfoData(audioClip.getInPoint());
                recordAudioInfo.setInPoint(recordInfo.getOutPoint());
                recordAudioInfo.setTrimIn(recordInfo.getOutPoint() - audioClip.getInPoint());
                m_audioInfoList.put(recordAudioInfo.getInPoint(), recordAudioInfo);

                // 选中这个录音
                haveRecordArea(recordInfo.getOutPoint());
            }
        }
        // 删除数据数据
        if(remove_list.size() > 0) {
            for (int i = 0; i < remove_list.size(); ++i) {
                deleteAudioClip(remove_list.get(i).getInPoint());
            }
        }

        // 重新设置UI
        m_sequenceLayout.clearAllAreas();
        Set<Map.Entry<Long, RecordAudioInfo>> set = m_audioInfoList.entrySet();
        Iterator<Map.Entry<Long, RecordAudioInfo>> iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry<Long, RecordAudioInfo> entry = iterator.next();
            RecordAudioInfo audioInfo = entry.getValue();
            if(audioInfo == null) {
                continue;
            }
            m_sequenceLayout.addRecordView(audioInfo.getInPoint(), audioInfo.getOutPoint());
        }

        // 添加音频到视频上
        m_recordAudioTrack.addClip(recordInfo.getPath(), recordInfo.getInPoint(), 0, recordInfo.getOutPoint() - recordInfo.getInPoint());
    }

    private void deleteAudioClip(long del_point) {
        if(m_recordAudioTrack == null) {
            return;
        }
        // UI层删除
        m_sequenceLayout.deleteRecordView(del_point);

        // 数据层删除
        deleteAudioInfoData(del_point);

        // 音轨上删除
        for(int i = 0; i < m_recordAudioTrack.getClipCount(); ++i) {
            NvsAudioClip audioClip = m_recordAudioTrack.getClipByIndex(i);
            if(audioClip == null) {
                continue;
            }
            if(audioClip.getInPoint() == del_point) {
                m_recordAudioTrack.removeClip(i, true);
                break;
            }
        }
    }

    private RecordAudioInfo deleteAudioInfoData(long del_point) {
        RecordAudioInfo recordAudioInfo = null;
        Set<Map.Entry<Long, RecordAudioInfo>> set = m_audioInfoList.entrySet();
        Iterator<Map.Entry<Long, RecordAudioInfo>> iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry<Long, RecordAudioInfo> entry = iterator.next();
            if(del_point == entry.getKey()) {
                recordAudioInfo = entry.getValue().clone();
                iterator.remove();
                break;
            }
        }
        return recordAudioInfo;
    }

    private void setAudioClipVolume(long inPoint, float volume) {
        if(m_recordAudioTrack != null) {
            RecordAudioInfo cur_recordAudioInfo= m_audioInfoList.get(inPoint);
            for(int i = 0; i < m_recordAudioTrack.getClipCount(); ++i) {
                NvsAudioClip audioClip = m_recordAudioTrack.getClipByIndex(i);
                if(audioClip == null) {
                    continue;
                }
                if(audioClip.getInPoint() == inPoint && cur_recordAudioInfo != null) {
                    audioClip.setVolumeGain(volume, volume);
                    cur_recordAudioInfo.setVolume(volume);
                    break;
                }
            }
        }
    }

    private float getAudioClipVolume(long inPoint) {
        if(m_recordAudioTrack != null) {
            for(int i = 0; i < m_recordAudioTrack.getClipCount(); ++i) {
                NvsAudioClip audioClip = m_recordAudioTrack.getClipByIndex(i);
                if(audioClip == null) {
                    continue;
                }
                if(audioClip.getInPoint() == inPoint) {
                    return audioClip.getVolumeGain().leftVolume;
                }
            }
        }
        return -1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_RECORD_AUDIO_PERMISSION_CODE) {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)) {
                m_haveRecordPermission = true;
            }
        }
    }
}
