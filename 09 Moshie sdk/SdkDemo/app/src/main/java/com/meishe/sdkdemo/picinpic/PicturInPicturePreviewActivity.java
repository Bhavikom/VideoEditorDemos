package com.meishe.sdkdemo.picinpic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.base.BasePermissionActivity;
import com.meishe.sdkdemo.edit.CompileVideoFragment;
import com.meishe.sdkdemo.edit.clipEdit.SingleClipFragment;
import com.meishe.sdkdemo.edit.interfaces.OnTitleBarClickListener;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.asset.NvAsset;

import java.util.List;

public class PicturInPicturePreviewActivity extends BasePermissionActivity {
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private RelativeLayout mCompilePage;
    private CompileVideoFragment mCompileVideoFragment;
    private SingleClipFragment mClipFragment;
    private NvsTimeline mTimeline;
    @Override
    protected int initRootView() {
        return R.layout.activity_pictur_in_picture_preview;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout)findViewById(R.id.bottom_layout);
        mCompilePage = (RelativeLayout) findViewById(R.id.compilePage);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.picInPicEdit);
        mTitleBar.setTextRight(R.string.compile);
        mTitleBar.setTextRightVisible(View.VISIBLE);
    }

    @Override
    protected void initData() {
        mTimeline = PictureInPictureActivity.mTimeline;
        if(mTimeline == null)
            return;
        initVideoFragment();
        initCompileVideoFragment();
    }

    @Override
    protected void initListener() {
        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                mTimeline = null;
            }

            @Override
            public void OnCenterTextClick() {

            }

            @Override
            public void OnRightTextClick() {
                if(mClipFragment.getCurrentEngineState() == NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK)
                    mClipFragment.stopEngine();
                mCompilePage.setVisibility(View.VISIBLE);
                mCompileVideoFragment.compileVideo();
            }
        });
        if(mCompileVideoFragment != null){
            mCompileVideoFragment.setCompileVideoListener(new CompileVideoFragment.OnCompileVideoListener() {
                @Override
                public void compileProgress(NvsTimeline timeline, int progress) {

                }

                @Override
                public void compileFinished(NvsTimeline timeline) {
                    mCompilePage.setVisibility(View.GONE);
                }

                @Override
                public void compileFailed(NvsTimeline timeline) {
                    mCompilePage.setVisibility(View.GONE);
                }

                @Override
                public void compileCompleted(NvsTimeline nvsTimeline, boolean isCanceled) {
                   mCompilePage.setVisibility(View.GONE);
                }

                @Override
                public void compileVideoCancel() {
                    mCompilePage.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!hasAllPermission()) {
            AppManager.getInstance().finishActivity();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        mTimeline = null;
        super.onBackPressed();
        AppManager.getInstance().finishActivity();
    }

    private void initVideoFragment() {
        mClipFragment = new SingleClipFragment();
        mClipFragment.setFragmentLoadFinisedListener(new SingleClipFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mClipFragment.playVideo(0,mTimeline.getDuration());
            }
        });
        mClipFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight",mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight",mBottomLayout.getLayoutParams().height);
        bundle.putInt("ratio", NvAsset.AspectRatio_9v16);
        mClipFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.spaceLayout, mClipFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mClipFragment);
    }
    private void seekTimeline(long timeStamp){
        mClipFragment.seekTimeline(timeStamp,0);
    }
    private void initCompileVideoFragment() {
        mCompileVideoFragment = new CompileVideoFragment();
        mCompileVideoFragment.setTimeline(mTimeline);
        getFragmentManager().beginTransaction()
                .add(R.id.compilePage, mCompileVideoFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mCompileVideoFragment);
    }

    @Override
    protected List<String> initPermissions() {
        return Util.getAllPermissionsList();
    }

    @Override
    protected void hasPermission() {

    }

    @Override
    protected void nonePermission() {

    }

    @Override
    protected void noPromptPermission() {

    }
}
