package com.meishe.sdkdemo.capturescene;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.edit.CompileVideoFragment;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.ToastUtil;

/**
 * Created by CaoZhiChao on 2018/11/14 10:11
 */
public class PreviewFragment extends Fragment {
    private final String TAG = "PreviewFragment";
    private final String FragmentTag = "CompileVideoFragment";
    View rootView;
    //    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private NvsLiveWindow mLiveWindow;
    private ImageView previewImageStop;
    private ImageView previewImageBack;
    private ImageView previewImageCreate;
    private SeekBar previewSeekBar;
    String compilePath;
    private CompileVideoFragment mCompileVideoFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_preview, container, false);
//        mStreamingContext = NvsStreamingContext.getInstance();
        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTimeline = TimelineUtil.createTimeline();
        if (mTimeline == null) {
            return;
        }
        previewSeekBar.setMax((int) mTimeline.getDuration());
        connectTimelineWithLiveWindow();
        initCompileVideoFragment();
    }

    private void initCompileVideoFragment() {
        mCompileVideoFragment = new CompileVideoFragment();
        mCompileVideoFragment.setTimeline(mTimeline);
        mCompileVideoFragment.setCompileVideoListener(new CompileVideoFragment.OnCompileVideoListener() {
            @Override
            public void compileProgress(NvsTimeline timeline, int progress) {
            }

            @Override
            public void compileFinished(NvsTimeline timeline) {
                hideFragment();
            }

            @Override
            public void compileFailed(NvsTimeline timeline) {
                hideFragment();
            }

            @Override
            public void compileCompleted(NvsTimeline nvsTimeline, boolean isCanceled) {
                hideFragment();
            }

            @Override
            public void compileVideoCancel() {
                hideFragment();
            }
        });
        getFragmentManager().beginTransaction().add(R.id.compilePage, mCompileVideoFragment,FragmentTag).commit();
        hideFragment();
    }

    private void showFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(FragmentTag);
        if (fragment == null){
            fragmentManager.beginTransaction().add(R.id.compilePage, mCompileVideoFragment,FragmentTag).commit();
        }else {
            fragmentManager.beginTransaction().show(fragment).commit();
        }
    }

    private void hideFragment() {
        getFragmentManager().beginTransaction().hide(mCompileVideoFragment).commit();
    }

    //连接时间线跟liveWindow
    public void connectTimelineWithLiveWindow() {
        NvsStreamingContext mStreamingContext = NvsStreamingContextUtil.getInstance().getmStreamingContext();
        if (mStreamingContext == null || mTimeline == null || mLiveWindow == null)
            return;
        mStreamingContext.setPlaybackCallback(new NvsStreamingContext.PlaybackCallback() {
            @Override
            public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {

            }

            @Override
            public void onPlaybackStopped(NvsTimeline nvsTimeline) {
            }

            @Override
            public void onPlaybackEOF(NvsTimeline nvsTimeline) {
                seekTimeline(0, 0);
                changeProgress(0);
            }
        });

        mStreamingContext.setPlaybackCallback2(new NvsStreamingContext.PlaybackCallback2() {
            @Override
            public void onPlaybackTimelinePosition(NvsTimeline nvsTimeline, long cur_position) {
                changeProgress(cur_position);
            }
        });

        mStreamingContext.setStreamingEngineCallback(new NvsStreamingContext.StreamingEngineCallback() {
            @Override
            public void onStreamingEngineStateChanged(int i) {
                previewImageStop.setVisibility(isPlaying(i) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onFirstVideoFramePresented(NvsTimeline nvsTimeline) {

            }
        });
//        mStreamingContext.setCompileCallback(new NvsStreamingContext.CompileCallback() {
//            @Override
//            public void onCompileProgress(NvsTimeline nvsTimeline, int i) {
//                showFragment();
//            }
//
//            @Override
//            public void onCompileFinished(NvsTimeline nvsTimeline) {
//                mStreamingContext.setCompileConfigurations(null);
//                //加入到媒体库
//                MediaScannerUtil.scanFile(compilePath, "video/mp4");
//            }
//
//            @Override
//            public void onCompileFailed(NvsTimeline nvsTimeline) {
//                Util.showDialog(getActivity(), "生成失败", "请检查手机存储空间");
//            }
//        });
        mStreamingContext.setCompileCallback2(new NvsStreamingContext.CompileCallback2() {
            @Override
            public void onCompileCompleted(NvsTimeline nvsTimeline, boolean isCanceled) {
                if (!isCanceled) {
                    ToastUtil.showToast(getActivity(), "生成成功！\n 保存路径: " + compilePath);
                }
            }
        });
        mStreamingContext.connectTimelineWithLiveWindow(mTimeline, mLiveWindow);
        start(0, -1);
    }

    private void changeProgress(long cur_position) {
        previewSeekBar.setProgress((int) cur_position);
    }

    private void initView(View rootView) {
        mLiveWindow = (NvsLiveWindow) rootView.findViewById(R.id.liveWindow);
        mLiveWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLiveWindowClick();
            }
        });
        previewImageStop = (ImageView) rootView.findViewById(R.id.preview_image_stop);
        previewImageStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NvsStreamingContextUtil.getInstance().startNow(mTimeline);
            }
        });
        previewImageBack = (ImageView) rootView.findViewById(R.id.preview_image_back);
        previewImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        previewImageCreate = (ImageView) rootView.findViewById(R.id.preview_image_create);
        previewImageCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment();
                mCompileVideoFragment.compileVideo();
            }
        });
        previewSeekBar = (SeekBar) rootView.findViewById(R.id.preview_seekBar);
        previewSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekTimeline(progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void onLiveWindowClick() {
        int state = NvsStreamingContextUtil.getInstance().getEngineState();
        if (isPlaying(state)) {
            stop();
        } else {
            NvsStreamingContextUtil.getInstance().startNow(mTimeline);
        }
    }

    private void start(long startTime, long endTime) {
        NvsStreamingContextUtil.getInstance().start(mTimeline, startTime, endTime);
    }

    private void stop() {
        NvsStreamingContextUtil.getInstance().stop();
    }

    private boolean isPlaying(int state) {
        return state == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK;
    }

    public void seekTimeline(long timestamp, int seekShowMode) {
        NvsStreamingContextUtil.getInstance().seekTimeline(mTimeline, timestamp, seekShowMode);
    }
}
