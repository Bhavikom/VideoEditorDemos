package zippler.cn.xs.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import zippler.cn.xs.R;

public class PreviewFullVideoActivity extends BaseActivity {
    private String path;
    private VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_full_video);

        path = getIntent().getStringExtra("videoPath");
        video = findViewById(R.id.video_f);

        video.setVideoPath(path);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "onPrepared: ready to delete image view");
                video.start();
            }
        });

        video.setOnClickListener(this);

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video.start();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.video:
                finish();
                break;
        }
    }
}
