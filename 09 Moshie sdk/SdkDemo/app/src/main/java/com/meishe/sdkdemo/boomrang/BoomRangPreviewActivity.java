package com.meishe.sdkdemo.boomrang;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.effect.NvBoomErang;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.MediaScannerUtil;
import com.meishe.sdkdemo.utils.PathUtils;
import com.meishe.sdkdemo.utils.threadpools.AllThreadPools;

import java.util.concurrent.TimeUnit;

import static com.meishe.sdkdemo.utils.VideoCompileUtil.addLogoWaterMark;
import static com.meishe.sdkdemo.utils.VideoCompileUtil.removeLogoSticker;

/**
 * Created by ms on 2018/11/2.
 */

public class BoomRangPreviewActivity extends BaseActivity {
    private final String TAG = "BoomRangPreviewActivity";
    private LiveWindow mLiveWindow; // 用于预览视频的控件
    private ImageView boomRangPreviewCloseButton;
    private ImageView boomRangPreviewCreate;
    private LinearLayout mCompilePage;
    private LinearLayout boomRang_preview_ing;
    private TextView boomRang_preview_completed;
    private NvsTimeline mTimeline;
    private String mRecordVideoPath;
    private String mCompileVideoPath;

    @Override
    protected int initRootView() {
        return R.layout.activity_preview_boomrang;
    }

    @Override
    protected void initViews() {
        mLiveWindow = (LiveWindow) findViewById(R.id.liveWindow);
        boomRangPreviewCloseButton = (ImageView) findViewById(R.id.boomRang_preview_closeButton);
        boomRangPreviewCreate = (ImageView) findViewById(R.id.boomRang_preview_create);
        mCompilePage = (LinearLayout) findViewById(R.id.compilePage);
        boomRang_preview_ing = (LinearLayout) findViewById(R.id.boomRang_preview_ing);
        boomRang_preview_completed = (TextView) findViewById(R.id.boomRang_preview_completed);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mRecordVideoPath = intent.getStringExtra("video_path");
            if (mRecordVideoPath == null || mRecordVideoPath.isEmpty()) {
                return;
            }
        }
        // 初始化时间线，构建视频特效
        mLiveWindow.init();
        initTimeline();
    }

    /**
     * 初始化监听
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {
        boomRangPreviewCloseButton.setOnClickListener(this);
        boomRangPreviewCreate.setOnClickListener(this);
        // 美摄sdk导出视频的回调
        mStreamingContext.setCompileCallback(new NvsStreamingContext.CompileCallback() {
            @Override
            public void onCompileProgress(NvsTimeline nvsTimeline, int i) {

            }

            @Override
            public void onCompileFinished(NvsTimeline nvsTimeline) {
                setCover(1);
                AllThreadPools.scheduleThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setCover(2);
                            }
                        });
                    }
                }, 1, TimeUnit.SECONDS);
                MediaScannerUtil.scanFile(mCompileVideoPath, "video/mp4");
            }

            @Override
            public void onCompileFailed(NvsTimeline nvsTimeline) {
                removeLogo();
            }
        });

        mCompilePage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    /**
     * 初始化时间线
     */
    private void initTimeline() {
        mTimeline = NvBoomErang.createTimeline(mRecordVideoPath);
        if (mTimeline == null) {
            Log.e(TAG, "initTimeline： timeline buildFx failed");
            return;
        }
        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        videoTrack.setVolumeGain(0, 0);
        mLiveWindow.playVideo(mTimeline);
        Log.e(TAG, "initTimeline： timeline duration: " + mTimeline.getDuration());
    }

//    NvsTimelineAnimatedSticker logo = null;

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boomRang_preview_closeButton:
                AppManager.getInstance().finishActivity();
                break;
            case R.id.boomRang_preview_create:
                setCover(0);
                mCompileVideoPath = PathUtils.getBoomrangRecordingDirectory("_compile");
                int compile_height = 1280;
                NvsAVFileInfo fileInfo = mStreamingContext.getAVFileInfo(mRecordVideoPath);
                if (fileInfo != null) {
                    NvsSize size = fileInfo.getVideoStreamDimension(0);
                    if (size != null) {
                        compile_height = size.height;
                    }
                }
                mStreamingContext.setCustomCompileVideoHeight(compile_height);

//                logo = addLogoWaterMark(getBaseContext(), mStreamingContext, mTimeline);
                mStreamingContext.compileTimeline(mTimeline, 0, mTimeline.getDuration(), mCompileVideoPath,
                        NvsStreamingContext.COMPILE_VIDEO_RESOLUTION_GRADE_CUSTOM,
                        NvsStreamingContext.COMPILE_BITRATE_GRADE_MEDIUM, 0);
                break;
            default:

                break;
        }
    }

    /**
     * 控制生成中蒙层的显示
     *
     * @param state 0-开始显示，生成中 1-生成完成 2-消失
     */
    private void setCover(int state) {
        if (state == 0) {
            mCompilePage.setVisibility(View.VISIBLE);
            boomRang_preview_ing.setVisibility(View.VISIBLE);
            boomRang_preview_completed.setVisibility(View.GONE);
        } else if (state == 1) {
            boomRang_preview_ing.setVisibility(View.GONE);
            boomRang_preview_completed.setVisibility(View.VISIBLE);
        } else if (state == 2) {
            mCompilePage.setVisibility(View.GONE);
            removeLogo();
        }
    }

    private void removeLogo() {
//        assert logo != null;
//        removeLogoSticker(logo, mTimeline);
        mLiveWindow.playVideo(mTimeline);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTimeline != null &&
                mStreamingContext.getStreamingEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
            mLiveWindow.playVideo(mTimeline);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mStreamingContext.stop();
    }
}

