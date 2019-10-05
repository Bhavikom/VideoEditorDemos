package com.example.commonDemo.advanceSuggest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lansoeditor.demo.R;

public class ListAdvanceDemoActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.advance_demo_list);
        findViewById(R.id.id_advance_list_aedemo).setOnClickListener(this);
        findViewById(R.id.id_advance_list_lantianbaiyun).setOnClickListener(this);
        findViewById(R.id.id_advance_list_huainianchuqing).setOnClickListener(this);
        findViewById(R.id.id_advance_list_douyin_effect).setOnClickListener(this);
        findViewById(R.id.id_advance_list_seek_exact).setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_advance_list_aedemo:
                Intent intent = new Intent(ListAdvanceDemoActivity.this, AERecordFileHintActivity.class);
                startActivity(intent);
                break;
            case R.id.id_advance_list_lantianbaiyun:
                startPlayDemo(PlayAdvanceDemoActivity.LANTIANBAIYUN);
                break;
            case R.id.id_advance_list_douyin_effect:
                startPlayDemo(PlayAdvanceDemoActivity.DOUYIN_EFFECT);

                break;
            case R.id.id_advance_list_huainianchuqing:
                startPlayDemo(PlayAdvanceDemoActivity.HUANNIAN_QINGCHUN);

                break;
            case R.id.id_advance_list_seek_exact:
                startPlayDemo(PlayAdvanceDemoActivity.SEEK_DEMO);
                break;
        }
    }
    private  void startPlayDemo(int type){
        Intent intent = new Intent(ListAdvanceDemoActivity.this, PlayAdvanceDemoActivity.class);
        intent.putExtra("PlayType", type);
        startActivity(intent);
    }
}
