package zippler.cn.xs.listener;

import android.util.Log;

import VideoHandle.OnEditorListener;

/**
 * Created by Zipple on 2018/5/13.
 */

public class CombinedMusicEditorListener implements OnEditorListener {
    private static final String TAG="listener";

    private boolean isFinished = false;
    private boolean isFailed = false;

    @Override
    public void onSuccess() {
        Log.d(TAG, "onSuccess: combined music successfully");
        isFinished = true;
    }

    @Override
    public void onFailure() {
        Log.e(TAG, "onFailure: error in combined music");
        isFailed = true;
    }

    @Override
    public void onProgress(float v) {
        Log.i(TAG, "onProgress: combined music progress : "+v);
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public void setFailed(boolean failed) {
        isFailed = failed;
    }
}
