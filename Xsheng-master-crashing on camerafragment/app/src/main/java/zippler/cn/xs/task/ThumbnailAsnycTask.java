package zippler.cn.xs.task;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

import java.util.HashMap;

import static android.os.Build.VERSION;

/**
 * Created by Zipple on 2018/5/22.
 */

public class ThumbnailAsnycTask extends AsyncTask<String,Integer,Bitmap> {

    public OnFinishedListener listener;

    @Override
    protected Bitmap doInBackground(String... strings) {
        return createVideoThumbnail(strings[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        listener.onFinished(bitmap);
    }

    private Bitmap createVideoThumbnail(String url) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        return bitmap;
    }

    public OnFinishedListener getListener() {
        return listener;
    }

    public void setListener(OnFinishedListener listener) {
        this.listener = listener;
    }

    public abstract static class OnFinishedListener {
        public abstract void onFinished(Bitmap bitmap);
    }
}
