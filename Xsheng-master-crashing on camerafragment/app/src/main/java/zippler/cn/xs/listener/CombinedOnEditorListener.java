package zippler.cn.xs.listener;

import android.util.Log;

import java.io.File;
import java.util.List;

import VideoHandle.OnEditorListener;

/**
 * Created by Zipple on 2018/5/11.
 */

public class CombinedOnEditorListener implements OnEditorListener {
    private static final String TAG = "OnEditorListener";

    private List<String> paths;
    private boolean isFinished;

    public CombinedOnEditorListener(List<String> old, boolean flag) {
        this.isFinished = flag;
        Log.d(TAG, "CombinedOnEditorListener: inject listeners");
        paths = old;
    }


    @Override
    public void onSuccess() {
        Log.d(TAG, "onSuccess: combined success");
        //remove the two videos
        File file;
        for (String path : paths) {
            file = new File(path);
            if (file.exists()) {
                if (file.delete()) {
                    Log.d(TAG, "gotoPreview: delete old videos :" + path.substring(path.lastIndexOf("/")));
                }
            }
        }
        isFinished = true;
    }

    @Override
    public void onFailure() {
        Log.e(TAG, "onFailure: error in combined videos");
    }

    @Override
    public void onProgress(float progress) {
        Log.d(TAG, "onProgress: combined progress : " + progress);
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
