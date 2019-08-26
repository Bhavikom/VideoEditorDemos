package com.meishe.sdkdemo.selectmedia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.base.BaseFragmentPagerAdapter;
import com.meishe.sdkdemo.edit.VideoEditActivity;
import com.meishe.sdkdemo.edit.data.BackupData;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.picinpic.PictureInPictureActivity;
import com.meishe.sdkdemo.selectmedia.adapter.AgendaSimpleSectionAdapter;
import com.meishe.sdkdemo.selectmedia.bean.MediaData;
import com.meishe.sdkdemo.selectmedia.fragment.MediaFragment;
import com.meishe.sdkdemo.selectmedia.interfaces.OnTotalNumChangeForActivity;
import com.meishe.sdkdemo.selectmedia.view.CustomPopWindow;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.MediaConstant;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.dataInfo.ClipInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.meishe.sdkdemo.utils.Constants.POINT16V9;
import static com.meishe.sdkdemo.utils.Constants.POINT1V1;
import static com.meishe.sdkdemo.utils.Constants.POINT3V4;
import static com.meishe.sdkdemo.utils.Constants.POINT4V3;
import static com.meishe.sdkdemo.utils.Constants.POINT9V16;
import static com.meishe.sdkdemo.utils.MediaConstant.KEY_CLICK_TYPE;
import static com.meishe.sdkdemo.utils.MediaConstant.LIMIT_COUNT;

public class SelectMediaActivity extends BaseActivity implements OnTotalNumChangeForActivity, CustomPopWindow.OnViewClickListener {
    private String TAG = getClass().getName();
    private CustomTitleBar mTitleBar;
    private TabLayout tlSelectMedia;
    private ViewPager vpSelectMedia;
    private List<Fragment> fragmentLists = new ArrayList<>();
    private List<String> fragmentTabTitles = new ArrayList<>();
    private BaseFragmentPagerAdapter fragmentPagerAdapter;
    private int visitMethod = Constants.FROMMAINACTIVITYTOVISIT;
    private List<MediaData> mMediaDataList = new ArrayList<>();
    private Integer[] fragmentTotalNumber = {0,0,0};
    private int nowFragmentPosition = 0;
    private TextView meidaTVOfStart;
    private int mLimiteMediaCount = -1;

    @Override
    protected int initRootView() {
        return R.layout.activity_select_media;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        tlSelectMedia = (TabLayout) findViewById(R.id.tl_select_media);
        vpSelectMedia = (ViewPager) findViewById(R.id.vp_select_media);
        meidaTVOfStart = (TextView) findViewById(R.id.media_tv_startEdit);
        meidaTVOfStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visitMethod == Constants.FROMMAINACTIVITYTOVISIT) {
                    new CustomPopWindow.PopupWindowBuilder(SelectMediaActivity.this)
                            .setView(R.layout.pop_select_makesize)
//                            .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
//                            .setBgDarkAlpha(alphaOnPop) // 控制亮度
                            .setViewClickListener(SelectMediaActivity.this)
                            .create()
                            .showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
                } else if (visitMethod == Constants.FROMCLIPEDITACTIVITYTOVISIT) {
                    ArrayList<ClipInfo> clipInfos = getClipInfoList();
                    BackupData.instance().setAddClipInfoList(clipInfos);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    AppManager.getInstance().finishActivity();
                }else if(visitMethod == Constants.FROMPICINPICACTIVITYTOVISIT){
                    ArrayList<String> picInpicArray = getPicInPicVideoList();
                    if(picInpicArray.size() <= 1){
                        String[] selectVideoTips = getResources().getStringArray(R.array.select_video_tips);
                        Util.showDialog(SelectMediaActivity.this, selectVideoTips[0], selectVideoTips[1]);
                        return;
                    }
                    BackupData.instance().setPicInPicVideoArray(picInpicArray);
                    AppManager.getInstance().jumpActivity(SelectMediaActivity.this, PictureInPictureActivity.class, null);
                    AppManager.getInstance().finishActivity();
                }
            }
        });

    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.selectMedia);
    }

    public void setTitleText(int count) {
        if (count > 0) {
            String txt = getResources().getString(R.string.setSelectMedia);
            @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String txtWidthCount = String.format(txt, count);
            mTitleBar.setTextCenter(txtWidthCount);
        } else {
            mTitleBar.setTextCenter(R.string.selectMedia);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void initData() {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                visitMethod = bundle.getInt("visitMethod", Constants.FROMMAINACTIVITYTOVISIT);
                mLimiteMediaCount = bundle.getInt("limitMediaCount",-1);
            }
        }
        if(visitMethod == Constants.FROMPICINPICACTIVITYTOVISIT){
            meidaTVOfStart.setVisibility(View.VISIBLE);
            meidaTVOfStart.setText(R.string.select_two_video);
            meidaTVOfStart.setTextColor(Color.parseColor("#ffa3a3a3"));
        }
        String[] tabList = getResources().getStringArray(R.array.select_media);
        checkDataCountAndTypeCount(tabList, MediaConstant.MEDIATYPECOUNT);
        fragmentLists = getSupportFragmentManager().getFragments();
        if (fragmentLists == null || fragmentLists.size() == 0) {
            fragmentLists = new ArrayList<>();
            for (int i = 0; i < tabList.length; i++) {
                MediaFragment mediaFragment = new MediaFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(MediaConstant.MEDIA_TYPE, MediaConstant.MEDIATYPECOUNT[i]);
                bundle.putInt(LIMIT_COUNT, mLimiteMediaCount);
                bundle.putInt(KEY_CLICK_TYPE, MediaConstant.TYPE_ITEMCLICK_MULTIPLE);
                mediaFragment.setArguments(bundle);
                fragmentLists.add(mediaFragment);
            }
        }
        for (int i = 0; i < tabList.length; i++) {
            fragmentTabTitles.add(tabList[i]);
        }

        //禁止预加载
        vpSelectMedia.setOffscreenPageLimit(3);
        //测试提交
        fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentLists, fragmentTabTitles);
        vpSelectMedia.setAdapter(fragmentPagerAdapter);
        vpSelectMedia.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                nowFragmentPosition = position;
                for (int i = 0; i < fragmentLists.size(); i++) {
                    MediaFragment mediaFragment = (MediaFragment) fragmentLists.get(i);
                    List<Integer> list = Arrays.asList(fragmentTotalNumber);
                    if (!list.isEmpty()){
                        mediaFragment.setTotalSize(Collections.max(list));
                    }
                }
                notifyFragmentDataSetChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tlSelectMedia.setupWithViewPager(vpSelectMedia);
    }

    /**
     * 校验一次数据，使得item标注的数据统一
     *
     * @param position 碎片对应位置0.1.2
     */
    private void notifyFragmentDataSetChanged(int position) {
        MediaFragment fragment = (MediaFragment) fragmentLists.get(position);
        List<MediaData> currentFragmentList = checkoutSelectList(fragment);
        fragment.getAdapter().setSelectList(currentFragmentList);
        setTitleText(fragment.getAdapter().getSelectList().size());
        Logger.e(TAG, "onPageSelected: " + fragment.getAdapter().getSelectList().size());
    }

    private List<MediaData> checkoutSelectList(MediaFragment fragment) {
        List<MediaData> currentFragmentList = fragment.getAdapter().getSelectList();
        List<MediaData> totalSelectList = getMediaDataList();
        for (MediaData mediaData : currentFragmentList) {
            for (MediaData data : totalSelectList) {
                if (data.getPath().equals(mediaData.getPath()) && data.isState() == mediaData.isState()) {
                    mediaData.setPosition(data.getPosition());
                }
            }
        }
        return currentFragmentList;
    }

    private void checkDataCountAndTypeCount(String[] tabList, int[] mediaTypeCount) {
        if (tabList.length != mediaTypeCount.length) {
            return;
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断如果同意的情况下就去 吧权限请求设置给当前fragment的
        for (int i = 0; i < fragmentLists.size(); i++) {
            fragmentLists.get(i).onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onTotalNumChangeForActivity(List selectList, Object tag) {
        int index = (int) tag;
        fragmentTotalNumber[index] = selectList.size();
        if (visitMethod == Constants.FROMPICINPICACTIVITYTOVISIT) {//画中画页面
            List<MediaData> mediaDataList = getMediaDataList();
            int count = mediaDataList.size();
            if (count == 1) {
                meidaTVOfStart.setText(R.string.select_the_second_video);
            } else if (count == 2) {
                meidaTVOfStart.setText(R.string.startMaking);
            } else {
                meidaTVOfStart.setText(R.string.select_two_video);
            }
            meidaTVOfStart.setTextColor(count == mLimiteMediaCount ? Color.parseColor("#ff4a90e2") : Color.parseColor("#ffa3a3a3"));
        } else {
            meidaTVOfStart.setVisibility((Collections.max(Arrays.asList(fragmentTotalNumber))) > 0 ? View.VISIBLE : View.GONE);
        }

        Logger.e("onTotalNumChangeForActivity", "对应的碎片：  " + index+"    个数："+selectList.size());
        for (int i = 0; i < fragmentLists.size(); i++) {
            if (i != index) {
                Logger.e("2222", "要刷新的碎片：  " + i);
                MediaFragment fragment = (MediaFragment) fragmentLists.get(i);
                fragment.refreshSelect(selectList, index);
            }
        }

        if (index == nowFragmentPosition){
            setTitleText(selectList.size());
        }
    }


    public List<MediaData> getMediaDataList() {
        if (mMediaDataList == null) {
            return new ArrayList<>();
        }
        MediaFragment fragment = (MediaFragment) fragmentLists.get(0);
        if (fragment != null) {
            AgendaSimpleSectionAdapter adapter = fragment.getAdapter();
            if (adapter != null) {
                return adapter.getSelectList();
            }
        }
        return new ArrayList<>();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.e(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
//        setTotal(0);
        nowFragmentPosition = 0;
        super.onDestroy();
        Logger.e(TAG, "onDestroy");
    }

    @Override
    public void onViewClick(CustomPopWindow popWindow, View view) {
        switch (view.getId()) {
            case R.id.button16v9:
                selectCreateRatio(POINT16V9);
                break;
            case R.id.button1v1:
                selectCreateRatio(POINT1V1);
                break;
            case R.id.button9v16:
                selectCreateRatio(POINT9V16);
                break;
            case R.id.button3v4:
                selectCreateRatio(POINT3V4);
                break;
            case R.id.button4v3:
                selectCreateRatio(POINT4V3);
                break;
            default:
                break;
        }
    }

    private void selectCreateRatio(int makeRatio) {
        ArrayList<ClipInfo> pathList = getClipInfoList();
        TimelineData.instance().setVideoResolution(Util.getVideoEditResolution(makeRatio));
        TimelineData.instance().setClipInfoData(pathList);
        TimelineData.instance().setMakeRatio(makeRatio);
        AppManager.getInstance().jumpActivity(SelectMediaActivity.this, VideoEditActivity.class, null);
        AppManager.getInstance().finishActivity();
    }

    private ArrayList<ClipInfo> getClipInfoList() {
        ArrayList<ClipInfo> pathList = new ArrayList<>();
        for (MediaData mediaData : getMediaDataList()) {
            ClipInfo clipInfo = new ClipInfo();
            clipInfo.setFilePath(mediaData.getPath());
            pathList.add(clipInfo);
        }
        return pathList;
    }

    private ArrayList<String> getPicInPicVideoList() {
        ArrayList<String> pathList = new ArrayList<>();
        for (MediaData mediaData : getMediaDataList()) {
            pathList.add(mediaData.getPath());
        }
        return pathList;
    }
}
