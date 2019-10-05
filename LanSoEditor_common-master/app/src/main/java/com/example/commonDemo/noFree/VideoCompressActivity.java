package com.example.commonDemo.noFree;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.commonDemo.DemoUtil;
import com.example.commonDemo.VideoPlayerActivity;
import com.lansoeditor.demo.R;
import com.lansosdk.videoeditor.MediaInfo;
import com.lansosdk.videoeditor.LanSongFileUtil;
import com.lansosdk.NoFree.LSOVideoScale;
import com.lansosdk.NoFree.onVideoCompressCompletedListener;
import com.lansosdk.NoFree.onVideoCompressProgressListener;

public class VideoCompressActivity extends Activity{
    private  String videoPath = null;

    private String dstPath;
    private TextView tvBefore;
    private TextView tvAfter;
    private Button btnPreview;
    private static final String TAG="LSOVideoScale";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_compress_layout);

        initView();
    }
    private void selectVideo(){
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 66);
        tvBefore.setText("");
        tvAfter.setText("");
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 66 && resultCode == RESULT_OK && null != data) {
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedVideo,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            videoPath = cursor.getString(columnIndex);
            tvBefore.setText(""+LanSongFileUtil.getFileSize(videoPath));

            cursor.close();
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }

    LSOVideoScale videoCompress;
    LSProgressDialog progressDialog;
    /**
     * 开始压缩
     */
    private void startCompress(){
        if(!LSOVideoScale.isNeedCompress(videoPath)){
            DemoUtil.showHintDialog(VideoCompressActivity.this,"文件大小不超过3M, 没必要压缩");
        }else{
            progressDialog=new LSProgressDialog();
            progressDialog.show(VideoCompressActivity.this);

            videoCompress=new LSOVideoScale(getApplication(),videoPath);
            videoCompress.setOnVideoCompressCompletedListener(new onVideoCompressCompletedListener() {
                @Override
                public void onCompleted(String video) {
                    Log.i("compress","压缩完毕");
                    progressDialog.release();
                    dstPath=video;

                    Log.i(TAG,"之前的视频是:");
                    MediaInfo.checkFile(videoPath);

                    MediaInfo.checkFile(video);

                    float size=LanSongFileUtil.getFileSize(dstPath);
                    tvAfter.setText(""+size);
                    btnPreview.setEnabled(true);
                }
            });
            videoCompress.setOnVideoCompressProgressListener(new onVideoCompressProgressListener() {
                @Override
                public void onProgress(int percent) {
                    progressDialog.setProgress(percent);
                }
            });
            if(!videoCompress.start()){
                DemoUtil.showHintDialog(VideoCompressActivity.this,"开始压缩失败");
                progressDialog.release();
                progressDialog=null;
            }
        }
    }
    private void startPreview(){
        if(LanSongFileUtil.fileExist(dstPath)){
            Intent intent = new Intent(VideoCompressActivity.this, VideoPlayerActivity.class);
            intent.putExtra("videopath", dstPath);
            startActivity(intent);
        }
    }
    private void initView(){
        findViewById(R.id.id_videocompress_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideo();
            }
        });

        findViewById(R.id.id_videocompress_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCompress();
            }
        });

        btnPreview=(Button)findViewById(R.id.id_videocompress_preview);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPreview();
            }
        });
        btnPreview.setEnabled(false);
        tvBefore=(TextView)findViewById(R.id.id_videocompress_tv_before);
        tvAfter=(TextView)findViewById(R.id.id_videocompress_tv_after);
    }
}
