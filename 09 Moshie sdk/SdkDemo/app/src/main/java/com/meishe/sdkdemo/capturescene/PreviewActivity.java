package com.meishe.sdkdemo.capturescene;

import android.os.Bundle;
import android.view.View;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;

public class PreviewActivity extends BaseActivity {
    private final String TAG = getClass().getName();

    @Override
    protected int initRootView() {
        return R.layout.activity_preview;
    }

    @Override
    protected void initViews() {
        setFragment();
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    private void setFragment() {
        PreviewFragment previewFragment;
        previewFragment = (PreviewFragment) getFragmentManager().findFragmentByTag(TAG);
        if (previewFragment == null) {
            previewFragment = new PreviewFragment();
            Bundle bundle = getIntent().getExtras();
            previewFragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .add(R.id.rootView, previewFragment, TAG)
                    .commit();
            getFragmentManager().beginTransaction().show(previewFragment).commit();
        }
    }

    @Override
    public void onClick(View v) {

    }
}
