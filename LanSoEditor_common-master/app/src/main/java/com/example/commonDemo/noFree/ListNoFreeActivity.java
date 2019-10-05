package com.example.commonDemo.noFree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.commonDemo.DemoUtil;
import com.lansoeditor.demo.R;
import com.lansosdk.NoFree.AudioPadExecute;
import com.lansosdk.box.AudioLayer;
import com.lansosdk.videoeditor.MediaInfo;

public class ListNoFreeActivity extends Activity implements View.OnClickListener {

    String videoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_no_free_layout);

        videoPath = getIntent().getStringExtra("videopath");

        findViewById(R.id.id_nofree_list_compress).setOnClickListener(this);
        findViewById(R.id.id_nofree_list_extractspeed).setOnClickListener(this);
        findViewById(R.id.id_nofree_list_extract20).setOnClickListener(this);
        findViewById(R.id.id_nofree_list_extract60).setOnClickListener(this);
        findViewById(R.id.id_nofree_list_audiopad).setOnClickListener(this);
        findViewById(R.id.id_nofree_list_segmentrecord).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.id_nofree_list_compress:
                Intent intent = new Intent(ListNoFreeActivity.this, VideoCompressActivity.class);
                startActivity(intent);
                break;
            case R.id.id_nofree_list_extractspeed:
                Intent intent1 = new Intent(ListNoFreeActivity.this, ExtractFrameDemoActivity.class);
                intent1.putExtra("videopath", videoPath);
                startActivity(intent1);
                break;
            case R.id.id_nofree_list_extract20:
                Intent intent2 = new Intent(ListNoFreeActivity.this, DisplayFramesActivity.class);
                intent2.putExtra("TYPE", DisplayFramesActivity.FRAME_TYPE_60);
                intent2.putExtra("videopath", videoPath);
                startActivity(intent2);
                break;
            case R.id.id_nofree_list_extract60:
                Intent intent3 = new Intent(ListNoFreeActivity.this, DisplayFramesActivity.class);
                intent3.putExtra("TYPE", DisplayFramesActivity.FRAME_TYPE_60);
                intent3.putExtra("videopath", videoPath);
                startActivity(intent3);
                break;
            case R.id.id_nofree_list_audiopad:
                DemoUtil.showHintDialog(ListNoFreeActivity.this,"暂时无界面:详细代码举例在AudioPadExecute.java文件中");
                break;
            case R.id.id_nofree_list_segmentrecord:
                DemoUtil.showHintDialog(ListNoFreeActivity.this,"后期增加");
                break;
            default:
                break;
        }
    }
    private void testFile(){
        AudioPadExecute execute = new AudioPadExecute(getApplicationContext(), "/sdcard/d1.mp4");
        execute.setOnAudioPadCompletedListener(new AudioPadExecute.onAudioPadExecuteCompletedListener() {
            @Override
            public void onCompleted(String path) {
                MediaInfo.checkFile(path);
            }
        });
        //增加一个音频
        AudioLayer audioLayer = execute.addAudioLayer("/sdcard/Rear_Left.wav", 0, 0, -1);
        if (audioLayer != null) {
            audioLayer.setLooping(true);
        }
        //增加另一个音频
        execute.addAudioLayer("/sdcard/hongdou10s.mp3", true,0.5f);

        //主音频禁止;
        AudioLayer audioLayer2 = execute.getMainAudioLayer();
        if (audioLayer2 != null) {
            audioLayer2.setMute(true);
        }
        execute.start();
    }
}
