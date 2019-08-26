package com.meishe.sdkdemo.edit.music;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsAudioClip;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.edit.VideoFragment;
import com.meishe.sdkdemo.edit.interfaces.OnTitleBarClickListener;
import com.meishe.sdkdemo.edit.record.SqView;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.edit.view.VerticalSeekBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.TimeFormatUtil;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.dataInfo.MusicInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ms on 2018/7/12 0012.
 */

public class MusicActivity extends BaseActivity{
    private final String TAG = "MusicActivity";
    private final int SEEK_TYPE_NULL = 0;
    private final int SEEK_TYPE_DRAG = 1;
    private final int SEEK_TYPE_PLAY = 2;
    private final int FADE_DURATION = 1000000;
    private VideoFragment m_videoFragment;
    private CustomTitleBar m_titleBar;
    private RelativeLayout m_bottomLayout, m_multiMusicLayout, m_playBtnLayout, m_zoomInBtn, m_zoomOutBtn, m_backBtn;
    private LinearLayout m_navLayout;
    private TextView m_playCurTime;
    private ImageButton m_musicSingleBtn, m_musicMultiBtn;
    private ImageView m_multiOkBtn;
    private Button m_playBtn, m_addMusicBtn, m_delMusicBtn, m_fadeBtn;
    private MusicSqLayout m_sequenceLayout;
    private SqView m_sequenceView;
    private VerticalSeekBar m_musicVolumeSeekBar;
    private NvsStreamingContext m_streamingContext;
    private NvsTimeline m_timeLine;
    private NvsAudioTrack m_musicTrack;
    private int m_seekTimeline = SEEK_TYPE_PLAY;
    private long m_curInPoint;
    private List<MusicInfo> musicInfosClone = new ArrayList<>();
    private Drawable m_fadeSelectDrawable, m_fadeUnSelectDrawable;

    @Override
    protected int initRootView() {
        m_streamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_music;
    }

    @Override
    protected void initViews() {
        m_titleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        m_bottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        m_musicSingleBtn = (ImageButton) findViewById(R.id.music_single_btn);
        m_musicMultiBtn = (ImageButton) findViewById(R.id.music_multi_btn);
        m_multiMusicLayout = (RelativeLayout) findViewById(R.id.multi_music_layout);
        m_navLayout = (LinearLayout) findViewById(R.id.nav_layout);
        m_multiOkBtn = (ImageView) findViewById(R.id.ok_btn);
        m_sequenceLayout = (MusicSqLayout) findViewById(R.id.sq_view);
        m_playBtnLayout = (RelativeLayout) findViewById(R.id.play_btn_layout);
        m_zoomInBtn = (RelativeLayout) findViewById(R.id.zoom_in_btn);
        m_zoomOutBtn = (RelativeLayout) findViewById(R.id.zoom_out_btn);
        m_playCurTime = (TextView) findViewById(R.id.play_cur_time);
        m_playBtn = (Button) findViewById(R.id.play_btn);
        m_addMusicBtn = (Button) findViewById(R.id.music_add_btn);
        m_delMusicBtn = (Button) findViewById(R.id.music_del_btn);
        m_musicVolumeSeekBar = (VerticalSeekBar) findViewById(R.id.music_volume_seekBar);
        m_fadeBtn = (Button) findViewById(R.id.fade_btn);
        m_backBtn = (RelativeLayout) findViewById(R.id.back_btn);
        m_fadeSelectDrawable = getResources().getDrawable(R.drawable.fadein_select);
        m_fadeUnSelectDrawable = getResources().getDrawable(R.drawable.fadein_unselect);
        m_fadeSelectDrawable.setBounds(0, 0, m_fadeSelectDrawable.getMinimumWidth(), m_fadeSelectDrawable.getMinimumHeight());
        m_fadeUnSelectDrawable.setBounds(0, 0, m_fadeUnSelectDrawable.getMinimumWidth(), m_fadeUnSelectDrawable.getMinimumHeight());
    }

    @Override
    protected void initTitle() {
        m_titleBar.setTextCenter(R.string.music);
    }

    @Override
    protected void initData() {
        initVideoFragment();
        updateSequenceView();

        updatePlaytimeText(0);
        List<MusicInfo> musicInfos = TimelineData.instance().getMusicData();
        if(musicInfos != null) {
            for(int i = 0; i < musicInfos.size(); ++i) {
                MusicInfo musicInfo = musicInfos.get(i);
                if(musicInfo == null) {
                    continue;
                }
                m_sequenceLayout.addRecordView(musicInfo.getInPoint(), musicInfo.getOutPoint());
                musicInfosClone.add(musicInfo);

                // 如果首帧就有音乐，那么要显示设置音乐的选项(因为刚刚进入到页面画面停在首帧)
                if(musicInfo.getInPoint() == 0) {
                    haveRecordArea(0);
                    m_sequenceLayout.selectAreaByInPoint(0);
                }
            }
        }
    }

    @Override
    protected void initListener() {
        m_titleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
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

        m_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_navLayout.setVisibility(View.VISIBLE);
                m_multiMusicLayout.setVisibility(View.GONE);
                m_backBtn.setVisibility(View.GONE);
                m_videoFragment.setPlaySeekVisiable(true);
            }
        });

        m_musicSingleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle musicBundle = new Bundle();
                musicBundle.putInt(Constants.SELECT_MUSIC_FROM, Constants.SELECT_MUSIC_FROM_EDIT);
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(),
                        SelectMusicActivity.class,
                        musicBundle,
                        Constants.ACTIVITY_START_CODE_MUSIC_SINGLE);
            }
        });

        m_musicMultiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_navLayout.setVisibility(View.GONE);
                m_multiMusicLayout.setVisibility(View.VISIBLE);
                m_backBtn.setVisibility(View.VISIBLE);
                m_videoFragment.setPlaySeekVisiable(false);
            }
        });

        m_multiOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_navLayout.setVisibility(View.VISIBLE);
                m_multiMusicLayout.setVisibility(View.GONE);
                m_backBtn.setVisibility(View.GONE);
                m_videoFragment.setPlaySeekVisiable(true);
            }
        });

        m_playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo();
            }
        });

        m_zoomInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_seekTimeline = SEEK_TYPE_NULL;
                m_sequenceView.zoomInSequence();
                m_sequenceLayout.reLayoutAllViews();
                m_sequenceLayout.selectAreaByInPoint(m_streamingContext.getTimelineCurrentPosition(m_timeLine));
            }
        });

        m_zoomOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_seekTimeline = SEEK_TYPE_NULL;
                m_sequenceView.zoomOutSequence();
                m_sequenceLayout.reLayoutAllViews();
                m_sequenceLayout.selectAreaByInPoint(m_streamingContext.getTimelineCurrentPosition(m_timeLine));
            }
        });

        m_addMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle musicBundle = new Bundle();
                musicBundle.putInt(Constants.SELECT_MUSIC_FROM, Constants.SELECT_MUSIC_FROM_EDIT);
                AppManager.getInstance().jumpActivityForResult(AppManager.getInstance().currentActivity(),
                        SelectMusicActivity.class,
                        musicBundle,
                        Constants.ACTIVITY_START_CODE_MUSIC_MULTI);
            }
        });

        m_delMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOneMusic(m_curInPoint);
            }
        });

        m_fadeBtn.setTag(0);
        m_fadeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((int) m_fadeBtn.getTag() == 0) {
                    m_fadeBtn.setTag(1);
                    m_fadeBtn.setCompoundDrawables(m_fadeSelectDrawable,null,null,null);
                    m_fadeBtn.setTextColor(ContextCompat.getColor(MusicActivity.this,R.color.ms_blue));
                    setFadeInOut(true);
                } else {
                    m_fadeBtn.setTag(0);
                    m_fadeBtn.setCompoundDrawables(m_fadeUnSelectDrawable,null,null,null);
                    m_fadeBtn.setTextColor(ContextCompat.getColor(MusicActivity.this,R.color.white));
                    setFadeInOut(false);
                }
            }
        });


        m_musicVolumeSeekBar.setOnSlideChangeListener(new VerticalSeekBar.SlideChangeListener() {
            @Override
            public void onStart(VerticalSeekBar slideView, int progress) {

            }

            @Override
            public void onProgress(VerticalSeekBar slideView, int progress) {
                float value = (float) progress / 100 * 3;
                Log.e("===>", "music volume: " + value);
                NvsAudioClip audioClip = findAudioClipByInPoint(m_curInPoint);
                MusicInfo cur_musicInfo = findMusicInfoByInPoint(m_curInPoint);
                if(audioClip != null && cur_musicInfo != null) {
                    audioClip.setVolumeGain(value, value);
                    cur_musicInfo.setVolume(value);
                }
            }

            @Override
            public void onStop(VerticalSeekBar slideView, int progress) {

            }
        });

        m_videoFragment.setVideoFragmentCallBack(new VideoFragment.VideoFragmentListener() {
            @Override
            public void playBackEOF(NvsTimeline timeline) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        m_sequenceView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                        haveRecordArea(0);
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

        m_sequenceLayout.setHorizontalScrollListener(new MusicSqLayout.HorizontalScrollListener() {
            @Override
            public void horizontalScrollStoped() {

            }

            @Override
            public void horizontalScrollChanged(long inPoint, boolean isDraging, long cur_audio_inpoint) {
                Log.e("===>", "cur_audio_inpoint: " + cur_audio_inpoint);
                if(isDraging) {
                    m_seekTimeline = SEEK_TYPE_DRAG;
                }
                if(m_seekTimeline == SEEK_TYPE_DRAG || m_seekTimeline == SEEK_TYPE_PLAY) {
                    haveRecordArea(cur_audio_inpoint);
                }
                if(m_seekTimeline == SEEK_TYPE_DRAG) {
                    m_videoFragment.seekTimeline(inPoint, 0);
                    updatePlaytimeText(inPoint);
                }
            }
        });

        m_sequenceLayout.setOnSeekValueListener(new MusicSqLayout.OnSeekValueListener() {
            @Override
            public void onLeftValueChange(long var) {
                changeInPoint(var);
            }

            @Override
            public void onRightValueChange(long var) {
                changeOutPoint(var);
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
        removeTimeline();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        AppManager.getInstance().finishActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.ACTIVITY_START_CODE_MUSIC_SINGLE) {

                // 处理从单段音乐选择的音乐
                MusicInfo musicInfo = (MusicInfo) data.getSerializableExtra("select_music");
                if(m_timeLine == null) {
                    return;
                }
                musicInfosClone.clear();
                m_curInPoint = 0;
                if(musicInfo != null) {
                    addOneMusic(musicInfo, true);
                } else {
                    TimelineUtil.buildTimelineMusic(m_timeLine, musicInfosClone);
                    m_sequenceLayout.clearAllAreas();
                }
                m_videoFragment.playVideo(m_curInPoint, m_timeLine.getDuration());

            } else if(requestCode == Constants.ACTIVITY_START_CODE_MUSIC_MULTI) {

                // 处理从多段音乐选择的音乐
                MusicInfo musicInfo = (MusicInfo) data.getSerializableExtra("select_music");
                if(m_timeLine == null || m_streamingContext == null) {
                    return;
                }
                m_curInPoint = m_streamingContext.getTimelineCurrentPosition(m_timeLine);
                if(musicInfo != null) {
                    addOneMusic(musicInfo, false);
                } else {
                    musicInfosClone.clear();
                    TimelineUtil.buildTimelineMusic(m_timeLine, musicInfosClone);
                    m_sequenceLayout.clearAllAreas();
                }
                m_videoFragment.playVideo(m_curInPoint, m_timeLine.getDuration());
            }
        }
    }

    private void removeTimeline(){
        // 保存音乐数据
        TimelineData.instance().setMusicList(musicInfosClone);
        TimelineUtil.removeTimeline(m_timeLine);
        m_timeLine = null;
    }

    private void initVideoFragment() {
        m_timeLine = TimelineUtil.createTimeline();
        if(m_timeLine == null)
            return;
        m_musicTrack = m_timeLine.getAudioTrackByIndex(0);
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
        m_bundle.putBoolean("playBarVisible",true);
        m_bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        m_videoFragment.setArguments(m_bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.video_layout, m_videoFragment)
                .commit();
        getFragmentManager().beginTransaction().show(m_videoFragment);
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

    private MusicInfo findMusicInfoByInPoint(long inPoint) {
        for(MusicInfo musicInfo: musicInfosClone) {
            if(musicInfo == null) {
                continue;
            }
            if(musicInfo.getInPoint() == inPoint) {
                return musicInfo;
            }
        }
        return null;
    }

    private NvsAudioClip findAudioClipByInPoint(long inPoint) {
        if(m_musicTrack != null) {
            for(int i = 0; i < m_musicTrack.getClipCount(); ++i) {
                NvsAudioClip audioClip = m_musicTrack.getClipByIndex(i);
                if(audioClip == null) {
                    continue;
                }
                if(audioClip.getInPoint() == inPoint) {
                    return audioClip;
                }
            }
        }
        return null;
    }

    private NvsAudioClip findAudioClipByCurPosition(long curPos) {
        if(m_musicTrack != null) {
            for(int i = 0; i < m_musicTrack.getClipCount(); ++i) {
                NvsAudioClip audioClip = m_musicTrack.getClipByIndex(i);
                if(audioClip == null) {
                    continue;
                }
                Log.e("===>", "find clip in: " + audioClip.getInPoint() + " out: " + audioClip.getOutPoint());
                if(curPos >= audioClip.getInPoint() && curPos < audioClip.getOutPoint()) {
                    return audioClip;
                }
            }
        }
        return null;
    }

    private long findNextInPointByInPoint(long curInPoint) {
        if(m_musicTrack != null) {
            List<Long> next_point = new ArrayList<>();
            for(int i = 0; i < m_musicTrack.getClipCount(); ++i) {
                NvsAudioClip audioClip = m_musicTrack.getClipByIndex(i);
                if(audioClip == null) {
                    continue;
                }
                if(audioClip.getInPoint() > curInPoint && audioClip.getAttachment(Constants.MUSIC_EXTRA_AUDIOCLIP) == null) {
                    next_point.add(audioClip.getInPoint());
                }
            }
            if(next_point.size() > 0) {
                // 排序
                Collections.sort(next_point);

                return next_point.get(0);
            }
        }
        return -1;
    }

    private long findLastOutPointByInPoint(long curInPoint) {
        if(m_musicTrack != null) {
            List<Long> last_point = new ArrayList<>();
            for(int i = 0; i < m_musicTrack.getClipCount(); ++i) {
                NvsAudioClip audioClip = m_musicTrack.getClipByIndex(i);
                if(audioClip == null) {
                    continue;
                }
                if(audioClip.getInPoint() < curInPoint) {
                    last_point.add(audioClip.getOutPoint());
                }
            }
            if(last_point.size() > 0) {
                // 排序
                Collections.sort(last_point);

                return last_point.get(last_point.size() - 1);
            }
        }
        return -1;
    }

    private void haveRecordArea(long cur_audio_inpoint) {
        if(cur_audio_inpoint == -1) {
            m_delMusicBtn.setVisibility(View.GONE);
            m_musicVolumeSeekBar.setVisibility(View.GONE);
            m_fadeBtn.setVisibility(View.GONE);

            long cur_pos = m_streamingContext.getTimelineCurrentPosition(m_timeLine);
            long next_inpoint = findNextInPointByInPoint(cur_pos);
            if(next_inpoint != -1 && Math.abs(cur_pos - next_inpoint) < Constants.MUSIC_MIN_DURATION) { // 距离下一个clip不到1s
                m_addMusicBtn.setVisibility(View.GONE);
            } else {
                if(Math.abs(cur_pos - m_timeLine.getDuration()) < Constants.MUSIC_MIN_DURATION) { // 视频末尾不到1s
                    m_addMusicBtn.setVisibility(View.GONE);
                } else {
                    m_addMusicBtn.setVisibility(View.VISIBLE);
                }
            }
            return;
        }
        NvsAudioClip audioClip = findAudioClipByCurPosition(cur_audio_inpoint);
        if(audioClip != null) {
            m_curInPoint = audioClip.getInPoint();
            m_delMusicBtn.setVisibility(View.VISIBLE);
            m_fadeBtn.setVisibility(View.VISIBLE);
            m_addMusicBtn.setVisibility(View.GONE);

            float volume = audioClip.getVolumeGain().leftVolume;
            m_musicVolumeSeekBar.setVisibility(View.VISIBLE);
            m_musicVolumeSeekBar.setProgress((int) (volume / 3 * 100));

            long fade_duration = audioClip.getFadeInDuration();
            m_fadeBtn.setTag((fade_duration > 0) ? 1 : 0);
            if((int) m_fadeBtn.getTag() == 1) {
                m_fadeBtn.setCompoundDrawables(m_fadeSelectDrawable,null,null,null);
                m_fadeBtn.setTextColor(ContextCompat.getColor(MusicActivity.this,R.color.ms_blue));
            } else {
                m_fadeBtn.setCompoundDrawables(m_fadeUnSelectDrawable,null,null,null);
                m_fadeBtn.setTextColor(ContextCompat.getColor(MusicActivity.this,R.color.white));
            }

        } else {
            m_addMusicBtn.setVisibility(View.VISIBLE);
            m_delMusicBtn.setVisibility(View.GONE);
            m_musicVolumeSeekBar.setVisibility(View.GONE);
            m_fadeBtn.setVisibility(View.GONE);
        }
        Log.e("===>", "m_curInPoint: " + m_curInPoint);
    }

    private void addOneMusic(MusicInfo musicInfo, boolean single) {
        if (musicInfo == null || m_musicTrack == null) {
            return;
        }
        // 预先处理数据
        musicInfo.setFadeDuration(FADE_DURATION);
        musicInfo.setInPoint(m_curInPoint);
        long duration_left = m_timeLine.getDuration() - m_curInPoint;
        if(musicInfo.getTrimOut() - musicInfo.getTrimIn() > duration_left) {
            musicInfo.setTrimOut(musicInfo.getTrimIn() + duration_left);
        }
        long outPoint = m_curInPoint + (musicInfo.getTrimOut() - musicInfo.getTrimIn());
        if(outPoint > m_timeLine.getDuration()) {
            outPoint = m_timeLine.getDuration();
        }
        musicInfo.setOutPoint(outPoint);

        // 如果是单段音乐
        if(single) {
            musicInfo.setOriginalInPoint(musicInfo.getInPoint());
            musicInfo.setOriginalOutPoint(musicInfo.getOutPoint());
            musicInfo.setOriginalTrimIn(musicInfo.getTrimIn());
            musicInfo.setOriginalTrimOut(musicInfo.getTrimOut());

            long cur_music_duration = musicInfo.getOriginalOutPoint() - musicInfo.getOriginalInPoint();
            long extra_duration = m_timeLine.getDuration() - musicInfo.getOriginalOutPoint();
            int extra_music = (int) (extra_duration / cur_music_duration);
            long extra_music_left = extra_duration % cur_music_duration;
            musicInfo.setExtraMusic(extra_music);
            musicInfo.setExtraMusicLeft(extra_music_left);
            musicInfo.setOutPoint(m_timeLine.getDuration());

        } else {
            // 处理数据（如果重合，则处理当前音乐的trimOut）
            for(int i = 0; i < m_musicTrack.getClipCount(); ++i) {
                NvsAudioClip audioClip = m_musicTrack.getClipByIndex(i);
                if(audioClip == null) {
                    continue;
                }
                Log.e("===>", "clip in: " + audioClip.getInPoint() + " rr in: " + musicInfo.getInPoint() + " rr out: " + musicInfo.getOutPoint());

                // 覆盖则改trimIn
                if(audioClip.getInPoint() < musicInfo.getOutPoint() && audioClip.getInPoint() >=  musicInfo.getInPoint()) {
                    Log.e("===>", "change trimIn: " + audioClip.getFilePath() + " " + audioClip.getInPoint());

                    musicInfo.setTrimOut(audioClip.getInPoint() - musicInfo.getInPoint() + musicInfo.getTrimIn());
                    musicInfo.setOutPoint(audioClip.getInPoint());
                }
            }
            musicInfo.setOriginalInPoint(musicInfo.getInPoint());
            musicInfo.setOriginalOutPoint(musicInfo.getOutPoint());
            musicInfo.setOriginalTrimIn(musicInfo.getTrimIn());
            musicInfo.setOriginalTrimOut(musicInfo.getTrimOut());
        }
        musicInfosClone.add(musicInfo);

        // 添加音乐
        addAudioClip();

        // 把已经应用的主题中的音乐去掉
        if(m_musicTrack.getClipCount() > 0) {
            String pre_theme_id = TimelineData.instance().getThemeData();
            if (pre_theme_id != null && !pre_theme_id.isEmpty()) {
                m_timeLine.setThemeMusicVolumeGain(0, 0);
            }
        }
    }

    private void deleteOneMusic(long del_point) {
        if(m_musicTrack == null) {
            return;
        }
        // UI层删除
        m_sequenceLayout.deleteRecordView(del_point);

        // 数据层删除
        MusicInfo musicInfo = findMusicInfoByInPoint(del_point);
        if(musicInfo != null) {
            musicInfosClone.remove(musicInfo);
        }

        // 音轨上删除
        NvsAudioClip del_audio_clip = findAudioClipByInPoint(del_point);
        if(del_audio_clip != null && del_audio_clip.getFilePath() != null) {
            String filePath = del_audio_clip.getFilePath();

            int clipCount = m_musicTrack.getClipCount();
            for(int i = 0; i < clipCount; ++i) {
                if(i >= m_musicTrack.getClipCount()) {
                    continue;
                }
                NvsAudioClip audioClip = m_musicTrack.getClipByIndex(i);
                if(audioClip == null) {
                    continue;
                }
                if(audioClip.getInPoint() == del_point) {
                    m_musicTrack.removeClip(i, true);
                    --i;
                    continue;
                }
                Object object = audioClip.getAttachment(Constants.MUSIC_EXTRA_AUDIOCLIP);
                if(audioClip.getFilePath().equals(filePath) && object != null && (long)object == del_point) {
                    m_musicTrack.removeClip(i, true);
                    --i;
                }
            }
        }
        haveRecordArea(m_curInPoint);
    }

    private void changeInPoint(long inPointChanged) {
        NvsAudioClip musicClip = findAudioClipByInPoint(m_curInPoint);
        MusicInfo cur_musicInfo = findMusicInfoByInPoint(m_curInPoint);
        long last_out_point = findLastOutPointByInPoint(m_curInPoint);
        if(musicClip != null && cur_musicInfo != null) {

            // 处理数据（如果重合，则处理当前音乐的inPoint）
            long newInPoint = inPointChanged;
            if(last_out_point != -1) {
                if(inPointChanged <= last_out_point) {
                    newInPoint = last_out_point;
                }
            }
            long now_all_duration = cur_musicInfo.getOutPoint() - newInPoint;
            long cur_music_duration = cur_musicInfo.getOriginalOutPoint() - cur_musicInfo.getOriginalInPoint();
            long extra_duration = now_all_duration - cur_music_duration;
            int extra_music = (int) (extra_duration / cur_music_duration);
            long extra_music_left = extra_duration % cur_music_duration;
            cur_musicInfo.setExtraMusic(extra_music);
            cur_musicInfo.setExtraMusicLeft(extra_music_left);
            cur_musicInfo.setInPoint(newInPoint);
            cur_musicInfo.setOriginalInPoint(newInPoint);
            cur_musicInfo.setOriginalOutPoint(newInPoint + cur_music_duration);
            cur_musicInfo.setTrimIn(cur_musicInfo.getOriginalTrimIn());
            if(cur_musicInfo.getOutPoint() - cur_musicInfo.getInPoint() < cur_music_duration) {
                cur_musicInfo.setTrimOut(cur_musicInfo.getTrimIn() + (cur_musicInfo.getOutPoint() - cur_musicInfo.getInPoint()));
            } else {
                cur_musicInfo.setTrimOut(cur_musicInfo.getOriginalTrimOut());
            }

            // 添加音乐
            addAudioClip();
        }
    }

    private void changeOutPoint(long outPointChanged) {
        NvsAudioClip musicClip = findAudioClipByInPoint(m_curInPoint);
        MusicInfo cur_musicInfo = findMusicInfoByInPoint(m_curInPoint);
        long first_in_point = findNextInPointByInPoint(m_curInPoint);

        if(musicClip != null && cur_musicInfo != null) {

            if(outPointChanged <= cur_musicInfo.getOriginalOutPoint()) {
                long newTrimOut, newOutPoint;
                if(first_in_point != -1 && first_in_point <= cur_musicInfo.getOriginalOutPoint() && outPointChanged >= first_in_point) {
                    newTrimOut = cur_musicInfo.getTrimIn() + (first_in_point - cur_musicInfo.getInPoint());
                    newOutPoint = first_in_point;
                } else {
                    newTrimOut = cur_musicInfo.getTrimIn() + (outPointChanged - cur_musicInfo.getInPoint());
                    newOutPoint = outPointChanged;
                }
                cur_musicInfo.setTrimOut(newTrimOut);
                cur_musicInfo.setOutPoint(newOutPoint);
                cur_musicInfo.setExtraMusic(0);
                cur_musicInfo.setExtraMusicLeft(0);

            } else {
                // 处理数据（如果重合，则处理当前音乐的trimOut）
                long newOutPoint = outPointChanged;
                if(first_in_point != -1) {
                    if(outPointChanged >= first_in_point) {
                        newOutPoint = first_in_point;
                    }
                }
                long cur_music_duration = cur_musicInfo.getOriginalOutPoint() - cur_musicInfo.getOriginalInPoint();
                long extra_duration = newOutPoint - cur_musicInfo.getOriginalOutPoint();
                int extra_music = (int) (extra_duration / cur_music_duration);
                long extra_music_left = extra_duration % cur_music_duration;
                cur_musicInfo.setExtraMusic(extra_music);
                cur_musicInfo.setExtraMusicLeft(extra_music_left);
                cur_musicInfo.setOutPoint(newOutPoint);
                if(cur_musicInfo.getOutPoint() - cur_musicInfo.getInPoint() < cur_music_duration) {
                    cur_musicInfo.setTrimOut(cur_musicInfo.getTrimIn() + (cur_musicInfo.getOutPoint() - cur_musicInfo.getInPoint()));
                } else {
                    cur_musicInfo.setTrimOut(cur_musicInfo.getOriginalTrimOut());
                }
            }
            // 添加音乐
            addAudioClip();
        }
    }

    private void addAudioClip() {
        m_musicTrack.removeAllClips();
        List<MusicInfo> have_add_music = new ArrayList<>();
        for(MusicInfo oneMusic: musicInfosClone) {
            if(oneMusic == null) {
                continue;
            }
            NvsAudioClip audioClip = m_musicTrack.addClip(oneMusic.getFilePath(), oneMusic.getInPoint(), oneMusic.getTrimIn(), oneMusic.getTrimOut());
            if(oneMusic.getExtraMusic() > 0) {
                for(int i = 0; i < oneMusic.getExtraMusic(); ++i) {
                    NvsAudioClip extra_clip = m_musicTrack.addClip(oneMusic.getFilePath(),
                            oneMusic.getOriginalOutPoint() + i * (oneMusic.getOriginalOutPoint() - oneMusic.getOriginalInPoint()),
                            oneMusic.getOriginalTrimIn(), oneMusic.getOriginalTrimOut());
                    if(extra_clip != null) {
                        extra_clip.setAttachment(Constants.MUSIC_EXTRA_AUDIOCLIP, oneMusic.getInPoint());
                        if(i == oneMusic.getExtraMusic() - 1 && oneMusic.getExtraMusicLeft() <= 0) {
                            extra_clip.setAttachment(Constants.MUSIC_EXTRA_LAST_AUDIOCLIP, oneMusic.getInPoint());
                            extra_clip.setFadeOutDuration(oneMusic.getFadeDuration());
                        }
                    }
                }
            }

            if(oneMusic.getExtraMusicLeft() > 0) {
                NvsAudioClip extra_clip = m_musicTrack.addClip(oneMusic.getFilePath(),
                        oneMusic.getOriginalOutPoint() + oneMusic.getExtraMusic() * (oneMusic.getOriginalOutPoint() - oneMusic.getOriginalInPoint()),
                        oneMusic.getOriginalTrimIn(),
                        oneMusic.getOriginalTrimIn() + oneMusic.getExtraMusicLeft());
                if(extra_clip != null) {
                    extra_clip.setAttachment(Constants.MUSIC_EXTRA_AUDIOCLIP, oneMusic.getInPoint());
                    extra_clip.setAttachment(Constants.MUSIC_EXTRA_LAST_AUDIOCLIP, oneMusic.getInPoint());
                    extra_clip.setFadeOutDuration(oneMusic.getFadeDuration());
                }
            }
            if(audioClip != null) {
                audioClip.setFadeInDuration(oneMusic.getFadeDuration());
                if(oneMusic.getExtraMusic() <= 0 && oneMusic.getExtraMusicLeft() <= 0) {
                    audioClip.setFadeOutDuration(oneMusic.getFadeDuration());
                }
                have_add_music.add(oneMusic);
            }
        }
        // 防止有添加不成功的
        musicInfosClone.clear();
        musicInfosClone.addAll(have_add_music);

        // 重新设置UI
        m_sequenceLayout.clearAllAreas();
        for(MusicInfo oneMusic: musicInfosClone) {
            if(oneMusic == null) {
                continue;
            }
            m_sequenceLayout.addRecordView(oneMusic.getInPoint(), oneMusic.getOutPoint());
        }
        long cur_position = m_streamingContext.getTimelineCurrentPosition(m_timeLine);
        haveRecordArea(cur_position);
        m_sequenceLayout.selectAreaByInPoint(cur_position);
    }

    private void setFadeInOut(boolean have_fade) {
        long fade_duration = 0;
        if(have_fade) {
            fade_duration = FADE_DURATION;
        }
        MusicInfo cur_musicInfo = findMusicInfoByInPoint(m_curInPoint);
        if(m_musicTrack != null && cur_musicInfo != null) {
            for(int i = 0; i < m_musicTrack.getClipCount(); ++i) {
                NvsAudioClip audioClip = m_musicTrack.getClipByIndex(i);
                if(audioClip == null) {
                    continue;
                }
                if(audioClip.getInPoint() == m_curInPoint) {
                    cur_musicInfo.setFadeDuration(fade_duration);
                    audioClip.setFadeInDuration(fade_duration);
                    if(cur_musicInfo.getExtraMusic() > 0 || cur_musicInfo.getExtraMusicLeft() > 0) {
                        for(int j = 0; j < m_musicTrack.getClipCount(); ++j) {
                            NvsAudioClip extraAudioClip = m_musicTrack.getClipByIndex(j);
                            if(extraAudioClip == null) {
                                continue;
                            }
                            if(extraAudioClip.getAttachment(Constants.MUSIC_EXTRA_LAST_AUDIOCLIP) != null) {
                                extraAudioClip.setFadeOutDuration(fade_duration);
                                break;
                            }
                        }
                    } else {
                        audioClip.setFadeOutDuration(fade_duration);
                    }
                    break;
                }
            }
        }
    }
}
