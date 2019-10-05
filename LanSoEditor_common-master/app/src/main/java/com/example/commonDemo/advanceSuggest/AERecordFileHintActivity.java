package com.example.commonDemo.advanceSuggest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lansoeditor.demo.R;


public class AERecordFileHintActivity  extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ae_recordfile_hint_layout);

        findViewById(R.id.id_ae_recordfile_startbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlay();
            }
        });
    }
    private  void startPlay(){
        Intent intent = new Intent(AERecordFileHintActivity.this, PlayAdvanceDemoActivity.class);
        intent.putExtra("PlayType", PlayAdvanceDemoActivity.AE_DEMO);
        startActivity(intent);
    }
}
