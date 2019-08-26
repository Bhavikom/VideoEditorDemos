package com.meishe.sdkdemo.capturescene;

import android.os.Environment;
import android.util.Log;

import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meishe.sdkdemo.MSApplication;
import com.meishe.sdkdemo.utils.ParameterSettingValues;

import java.io.File;
import java.util.Hashtable;

/**
 * Created by CaoZhiChao on 2018/11/14 14:10
 */
public class NvsStreamingContextUtil {
    private String TAG = "NvsStreamingContextUtil";
    private static final NvsStreamingContextUtil ourInstance = new NvsStreamingContextUtil();

    public NvsStreamingContext getmStreamingContext() {
        return mStreamingContext;
    }

    private NvsStreamingContext mStreamingContext;

    public static NvsStreamingContextUtil getInstance() {
        return ourInstance;
    }

    private NvsStreamingContextUtil() {
        mStreamingContext = NvsStreamingContext.getInstance();
        if (mStreamingContext == null) {
            String licensePath = "assets:/meishesdk.lic";
            NvsStreamingContext.init(MSApplication.getmContext(), licensePath, NvsStreamingContext.STREAMING_CONTEXT_FLAG_SUPPORT_4K_EDIT);
        }
    }

    //预览
    public void seekTimeline(NvsTimeline mTimeline, long timestamp, int seekShowMode) {
        /* seekTimeline
         * param1: 当前时间线
         * param2: 时间戳 取值范围为  [0, timeLine.getDuration()) (左闭右开区间)
         * param3: 图像预览模式
         * param4: 引擎定位的特殊标志
         * */
        assert mStreamingContext != null;
        mStreamingContext.seekTimeline(mTimeline, timestamp, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, seekShowMode);
    }

    public void startNow(NvsTimeline mTimeline) {
        assert mStreamingContext != null;
        start(mTimeline, mStreamingContext.getTimelineCurrentPosition(mTimeline), -1);
    }

    public int getEngineState() {
        assert mStreamingContext != null;
        return mStreamingContext.getStreamingEngineState();
    }

    public void start(NvsTimeline mTimeline, long startTime, long endTime) {
        assert mStreamingContext != null;
        mStreamingContext.playbackTimeline(mTimeline, startTime, endTime, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
    }

    public void stop() {
        assert mStreamingContext != null;
        mStreamingContext.stop();
    }

    public boolean startRecording(String path) {
        return mStreamingContext.startRecording(path);
    }

    public void stopRecording() {
        mStreamingContext.stopRecording();
    }

    //生成视频
    public void compileVideo(NvsTimeline mTimeline, String compilePath) {
        NvsStreamingContextUtil.getInstance().stop();
        double bitrate = ParameterSettingValues.instance().getCompileBitrate();
        if (bitrate != 0) {
            Hashtable<String, Object> config = new Hashtable<>();
            config.put(NvsStreamingContext.COMPILE_BITRATE, bitrate * 1000000);
            mStreamingContext.setCompileConfigurations(config);
        }
        int encoderFlag = 0;
        if (ParameterSettingValues.instance().disableDeviceEncorder()) {
            encoderFlag = NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_DISABLE_HARDWARE_ENCODER;
        }
        mStreamingContext.setCustomCompileVideoHeight(mTimeline.getVideoRes().imageHeight);
        mStreamingContext.compileTimeline(mTimeline, 0, mTimeline.getDuration(), compilePath, NvsStreamingContext.COMPILE_VIDEO_RESOLUTION_GRADE_CUSTOM, NvsStreamingContext.COMPILE_BITRATE_GRADE_HIGH, encoderFlag);
    }

    public String getVideoStoragePath() {
        File compileDir = new File(Environment.getExternalStorageDirectory(), "NvStreamingSdk" + File.separator + "Compile");
        if (!compileDir.exists() && !compileDir.mkdirs()) {
            Log.d(TAG, "Failed to make Compile directory");
            return null;
        }
        String compilePath = compileDir.toString();
        long currentMilis = System.currentTimeMillis();
        String videoName = "meicam_" + String.valueOf(currentMilis) + ".mp4";
        compilePath += "/";
        compilePath += videoName;
        return compilePath;
    }

    public void destory() {
        if (mStreamingContext != null) {
            mStreamingContext.removeAllCaptureVideoFx();
            mStreamingContext.removeCurrentCaptureScene();
            mStreamingContext.stop();
            mStreamingContext = null;
        }
    }

    public boolean isCaptureDeviceBackFacing(int captureDeviceIndex) {
        return mStreamingContext.isCaptureDeviceBackFacing(captureDeviceIndex);
    }
}
