package com.meishe.sdkdemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meicam.sdk.NvsStreamingContext;
import com.meishe.sdkdemo.base.BaseFragmentPagerAdapter;
import com.meishe.sdkdemo.base.BasePermissionActivity;
import com.meishe.sdkdemo.boomrang.BoomRangActivity;
import com.meishe.sdkdemo.capture.CaptureActivity;
import com.meishe.sdkdemo.capturescene.CaptureSceneActivity;
import com.meishe.sdkdemo.douvideo.DouVideoCaptureActivity;
import com.meishe.sdkdemo.edit.data.BackupData;
import com.meishe.sdkdemo.edit.watermark.SingleClickActivity;
import com.meishe.sdkdemo.feedback.FeedBackActivity;
import com.meishe.sdkdemo.main.MainViewPagerFragment;
import com.meishe.sdkdemo.main.MainViewPagerFragmentData;
import com.meishe.sdkdemo.main.OnItemClickListener;
import com.meishe.sdkdemo.musicLyrics.MultiVideoSelectActivity;
import com.meishe.sdkdemo.particle.ParticleCaptureActivity;
import com.meishe.sdkdemo.selectmedia.SelectMediaActivity;
import com.meishe.sdkdemo.superzoom.SuperZoomActivity;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.FileUtils;
import com.meishe.sdkdemo.utils.Logger;
import com.meishe.sdkdemo.utils.ParameterSettingValues;
import com.meishe.sdkdemo.utils.ScreenUtils;
import com.meishe.sdkdemo.utils.SpUtil;
import com.meishe.sdkdemo.utils.ToastUtil;
import com.meishe.sdkdemo.utils.Util;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meishe.sdkdemo.utils.Constants.HUMAN_AI_TYPE_MS;
import static com.meishe.sdkdemo.utils.Constants.HUMAN_AI_TYPE_NONE;


/**
 * MainActivity class
 * 主页面
 *
 * @author gexinyu
 * @date 2018-05-24
 */
public class MainActivity extends BasePermissionActivity implements OnItemClickListener {
    private static final String TAG = "MainActivity";
    public static final int REQUEST_CAMERA_PERMISSION_CODE = 200;
    public static final int INIT_ARSCENE_COMPLETE_CODE = 201;
    public static final int INIT_ARSCENE_FAILURE_CODE = 202;
    private ImageView mIvSetting;
    private ImageView mIvFeedBack;
    private RelativeLayout layoutVideoCapture;
    private RelativeLayout layoutVideoEdit;
    private ViewPager mainViewPager;
    private RadioGroup radioGroup;
    private TextView mainVersionNumber;
    private int spanCount = 4;
    private View clickedView = null;

    private boolean arSceneFinished = false;// 人脸初始化完成的标识
    private boolean initARSceneing = true;// 记录人脸模块正在初始化
    private boolean isClickRepeat = false;// 防止页面重复点击标识

    /**
     * SDK普通版
     */
    private int mCanUseARFaceType = HUMAN_AI_TYPE_NONE;

    private HandlerThread mHandlerThread;

    private MainActivityHandler mHandler = new MainActivityHandler(this);

    class MainActivityHandler extends Handler {
        WeakReference<MainActivity> mWeakReference;

        public MainActivityHandler(MainActivity mainActivityContext) {
            mWeakReference = new WeakReference<>(mainActivityContext);
        }

        @Override
        public void handleMessage(Message msg) {
            final MainActivity activity = mWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case INIT_ARSCENE_COMPLETE_CODE:
                        // 初始化ARScene 完成
                        arSceneFinished = true;
                        initARSceneing = false;
                        break;
                    case INIT_ARSCENE_FAILURE_CODE:
                        // 初始化ARScene 失败
                        arSceneFinished = false;
                        initARSceneing = false;
                        break;
                    default:
                        break;

                }
            }
        }
    }

    @Override
    protected int initRootView() {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return R.layout.activity_main;
        }
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initViews() {
        mIvSetting = (ImageView) findViewById(R.id.iv_main_setting);
        mIvFeedBack = (ImageView) findViewById(R.id.iv_main_feedback);
        layoutVideoCapture = (RelativeLayout) findViewById(R.id.layout_video_capture);
        layoutVideoEdit = (RelativeLayout) findViewById(R.id.layout_video_edit);
        mainViewPager = (ViewPager) findViewById(R.id.main_viewPager);
        radioGroup = (RadioGroup) findViewById(R.id.main_radioGroup);
        mainVersionNumber = (TextView) findViewById(R.id.main_versionNumber);
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int alwaysFinish = Settings.Global.getInt(getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0);
            if (alwaysFinish == 1) {
                Dialog dialog = null;
                dialog = new AlertDialog.Builder(this)
                        .setMessage(R.string.no_back_activity_message)
                        .setNegativeButton(R.string.no_back_activity_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                MainActivity.this.finish();
                            }
                        }).setPositiveButton(R.string.no_back_activity_setting, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                                startActivity(intent);
                            }
                        }).create();
                dialog.show();
            }
        }
        ParameterSettingValues parameterValues = (ParameterSettingValues) SpUtil.getObjectFromShare(getApplicationContext(), Constants.KEY_PARAMTER);
        // 本地没有存储设置的参数，设置默认值
        if (parameterValues != null) {
            ParameterSettingValues.setParameterValues(parameterValues);
        }
        initFragmentAndView();
        NvsStreamingContext.SdkVersion sdkVersion = NvsStreamingContext.getInstance().getSdkVersion();
        String sdkVersionNum = String.valueOf(sdkVersion.majorVersion) + "." + String.valueOf(sdkVersion.minorVersion) + "." + String.valueOf(sdkVersion.revisionNumber);
        mainVersionNumber.setText(String.format(getResources().getString(R.string.versionNumber), sdkVersionNum));
    }

    private void initFragmentAndView() {
        Map<Integer, List<String>> map = subListByItemCount();
        List<Fragment> mFragmentList = getSupportFragmentManager().getFragments();
        if (mFragmentList == null || mFragmentList.size() == 0) {
            mFragmentList = new ArrayList<>();
            for (int i = 0; i < map.size(); i++) {
                List<String> nameList = map.get(i);
                MainViewPagerFragment mediaFragment = new MainViewPagerFragment();
                Bundle bundle = new Bundle();
                ArrayList<MainViewPagerFragmentData> list = initFragmentDataById(nameList, i + 1);
                bundle.putParcelableArrayList("list", list);
                bundle.putInt("span", spanCount);
                mediaFragment.setArguments(bundle);
                mFragmentList.add(mediaFragment);
            }
        }
        for (int i = 0; i < map.size(); i++) {
            addRadioButton(i);
        }
        BaseFragmentPagerAdapter fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList, null);
        mainViewPager.setAdapter(fragmentPagerAdapter);
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setRadioButtonState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private Map<Integer, List<String>> subListByItemCount() {
        String[] fragmentItems = getResources().getStringArray(R.array.main_fragment_item);
        Map<Integer, List<String>> map = new HashMap<>();
        List<String> list = Arrays.asList(fragmentItems);
        int count = list.size() / spanCount + 1;
        for (int i = 0; i < count; i++) {
            int endTime = list.size() < (i + 1) * spanCount ? list.size() : (i + 1) * spanCount;
            int startTime = i == 0 ? i : i * spanCount;
            List<String> childList = list.subList(startTime, endTime);
            map.put(i, childList);
        }
        return map;
    }

    private void addRadioButton(int i) {
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ScreenUtils.dip2px(this, 5), ScreenUtils.dip2px(this, 5));
        lp.setMargins(0, 0, ScreenUtils.dip2px(this, 5), 0);
        radioGroup.addView(initRadioButton(i), lp);
    }

    private RadioButton initRadioButton(int i) {
        RadioButton radioButton = new RadioButton(this);
        radioButton.setId(getResources().getIdentifier("main_radioButton" + i, "id", getPackageName()));
        radioButton.setBackground(getResources().getDrawable(R.drawable.activity_main_checkbox_background));
        radioButton.setButtonDrawable(null);
        radioButton.setChecked(i == 0);
        return radioButton;
    }

    private ArrayList<MainViewPagerFragmentData> initFragmentDataById(List<String> names, int fragmentCount) {
        String[] fragmentItemsBackGround = getResources().getStringArray(R.array.main_fragment_background);
        List<String> listBackground = Arrays.asList(fragmentItemsBackGround);

        String[] fragmentItemsImage = getResources().getStringArray(R.array.main_fragment_image);
        List<String> listImage = Arrays.asList(fragmentItemsImage);
        ArrayList<MainViewPagerFragmentData> list1 = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            int backGroundId = getResources().getIdentifier(listBackground.get((fragmentCount - 1) * 4 + i), "drawable", getPackageName());
            int imageId = getResources().getIdentifier(listImage.get((fragmentCount - 1) * 4 + i), "drawable", getPackageName());
            if (backGroundId != 0 && imageId != 0) {
                list1.add(new MainViewPagerFragmentData(backGroundId, names.get(i), imageId));
            }
        }
        return list1;
    }

    private void setRadioButtonState(int position) {
        RadioButton radioButton = (RadioButton) findViewById(getResources().getIdentifier("main_radioButton" + position, "id", getPackageName()));
        radioButton.setChecked(true);
    }

    @Override
    protected void initListener() {
        mIvSetting.setOnClickListener(this);
        mIvFeedBack.setOnClickListener(this);
        layoutVideoCapture.setOnClickListener(this);
        layoutVideoEdit.setOnClickListener(this);
        checkPermissions();
        if (hasAllPermission()) {
            initARSceneEffect();//初始化人脸Model
        }
    }

    @Override
    public void onClick(View view) {
        if (isClickRepeat) {
            return;
        }
        isClickRepeat = true;
        switch (view.getId()) {
            // 设置
            case R.id.iv_main_setting:
                AppManager.getInstance().jumpActivity(this, ParameterSettingActivity.class, null);
                return;
            // 反馈
            case R.id.iv_main_feedback:
                AppManager.getInstance().jumpActivity(this, FeedBackActivity.class, null);
                return;
            default:
                break;
        }
        // 没有权限，则请求权限
        if (!hasAllPermission()) {
            clickedView = view;
            checkPermissions();
        } else {
            doClick(view);
        }
    }

    private void initARSceneEffect() {
        mCanUseARFaceType = NvsStreamingContext.hasARModule();
        // 初始化AR Scene，全局只需一次
        if (mCanUseARFaceType == HUMAN_AI_TYPE_MS && !arSceneFinished) {
            if (mHandlerThread == null) {
                mHandlerThread = new HandlerThread("handlerThread");
                mHandlerThread.start();
            }
            Handler initHandler = new Handler(mHandlerThread.getLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    boolean copySuccess = FileUtils.copyFileIfNeed(MainActivity.this, "tt_face.model", "facemode");
                    Logger.e(TAG, "copySuccess-->" + copySuccess);
                    File rootDir = getApplicationContext().getExternalFilesDir(null);
                    String destModelDir = rootDir + "/facemode/tt_face.model";
                    boolean initSuccess = NvsStreamingContext.initHumanDetection(MSApplication.getmContext(),
                            destModelDir, "assets:/facemode/tt_face.license",
                            NvsStreamingContext.HUMAN_DETECTION_FEATURE_FACE_LANDMARK | NvsStreamingContext.HUMAN_DETECTION_FEATURE_FACE_ACTION);
                    String fakefacePath = "assets:/facemode/fakeface.dat";
                    boolean fakefaceSuccess = NvsStreamingContext.setupHumanDetectionData(NvsStreamingContext.HUMAN_DETECTION_DATA_TYPE_FAKE_FACE, fakefacePath);
                    Logger.e(TAG, "fakefaceSuccess-->" + fakefaceSuccess);
                    if (initSuccess) {
                        mHandler.sendEmptyMessage(INIT_ARSCENE_COMPLETE_CODE);
                    } else {
                        mHandler.sendEmptyMessage(INIT_ARSCENE_FAILURE_CODE);
                    }
                    return false;
                }
            });
            initHandler.sendEmptyMessage(1);
        } else {
            initARSceneing = false;
        }
    }

    private void doClick(View view) {
        if (view == null)
            return;
        switch (view.getId()) {
            case R.id.iv_main_setting://设置
                AppManager.getInstance().jumpActivity(this, ParameterSettingActivity.class, null);
                break;

            case R.id.layout_video_capture://拍摄
                if (!initARSceneing) {
                    Bundle captureBundle = new Bundle();
                    captureBundle.putBoolean("initArScene", arSceneFinished);
                    AppManager.getInstance().jumpActivity(this, CaptureActivity.class, captureBundle);
                } else {
                    isClickRepeat = false;
                    ToastUtil.showToast(MainActivity.this, R.string.initArsence);
                }
                break;

            case R.id.layout_video_edit://编辑
                Bundle editBundle = new Bundle();
                editBundle.putInt("visitMethod", Constants.FROMMAINACTIVITYTOVISIT);
                editBundle.putInt("limitMediaCount", -1);//-1表示无限可选择素材
                AppManager.getInstance().jumpActivity(this, SelectMediaActivity.class, editBundle);
                break;
            default:
                String tag = (String) view.getTag();
                if (tag.equals(getResources().getString(R.string.douYinEffects))) {
                    if (!initARSceneing) {
                        Bundle douyinBundle = new Bundle();
                        douyinBundle.putBoolean("initArScene", arSceneFinished);
                        douyinBundle.putInt(DouVideoCaptureActivity.INTENT_KEY_STRENGTH, 75);
                        if (arSceneFinished) {
                            douyinBundle.putInt(DouVideoCaptureActivity.INTENT_KEY_CHEEK, 150);
                            douyinBundle.putInt(DouVideoCaptureActivity.INTENT_KEY_EYE, 150);
                        }
                        AppManager.getInstance().jumpActivity(this, DouVideoCaptureActivity.class, douyinBundle);
                    } else {
                        isClickRepeat = false;
                        ToastUtil.showToast(MainActivity.this, R.string.initArsence);
                    }
                } else if (tag.equals(getResources().getString(R.string.particleEffects))) {
                    AppManager.getInstance().jumpActivity(this, ParticleCaptureActivity.class, null);
                } else if (tag.equals(getResources().getString(R.string.captureScene))) {
                    AppManager.getInstance().jumpActivity(this, CaptureSceneActivity.class, null);
                } else if (tag.equals(getResources().getString(R.string.picInPic))) {
                    Bundle pipBundle = new Bundle();
                    pipBundle.putInt("visitMethod", Constants.FROMPICINPICACTIVITYTOVISIT);
                    pipBundle.putInt("limitMediaCount", 2); //2表示选择两个素材
                    AppManager.getInstance().jumpActivity(this, SelectMediaActivity.class, pipBundle);
                } else if (tag.equals(getResources().getString(R.string.makingCover))) {
                    Bundle makeCoverBundle = new Bundle();
                    makeCoverBundle.putInt(Constants.SELECT_MEDIA_FROM, Constants.SELECT_IMAGE_FROM_MAKE_COVER);
                    AppManager.getInstance().jumpActivity(this, SingleClickActivity.class, makeCoverBundle);
                } else if (tag.equals(getResources().getString(R.string.flipSubtitles))) {
                    Bundle flipBundle = new Bundle();
                    flipBundle.putInt(Constants.SELECT_MEDIA_FROM, Constants.SELECT_VIDEO_FROM_FLIP_CAPTION);
                    flipBundle.putInt("limitMediaCount", -1); //-1表示无限可选择素材
                    AppManager.getInstance().jumpActivity(this, MultiVideoSelectActivity.class, flipBundle);
                } else if (tag.equals(getResources().getString(R.string.musicLyrics))) {
                    Bundle musicBundle = new Bundle();
                    musicBundle.putInt(Constants.SELECT_MEDIA_FROM, Constants.SELECT_VIDEO_FROM_MUSIC_LYRICS);
                    musicBundle.putInt("limitMediaCount", -1); //-1表示无限可选择素材
                    AppManager.getInstance().jumpActivity(this, MultiVideoSelectActivity.class, musicBundle);
                } else if (tag.equals(getResources().getString(R.string.boomRang))) {
                    AppManager.getInstance().jumpActivity(this, BoomRangActivity.class);
                } else if (tag.equals(getResources().getString(R.string.pushMirrorFilm))) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        AppManager.getInstance().jumpActivity(this, SuperZoomActivity.class);
                    } else {
                        String[] tipsInfo = getResources().getStringArray(R.array.edit_function_tips);
                        Util.showDialog(MainActivity.this, tipsInfo[0], getString(R.string.versionBelowTip));
                    }
                } else {
                    String[] tipsInfo = getResources().getStringArray(R.array.edit_function_tips);
                    Util.showDialog(MainActivity.this, tipsInfo[0], tipsInfo[1], tipsInfo[2]);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
        if (hasAllPermission()) {
            Util.clearRecordAudioData();
        }
        // 退出清理
        if (mStreamingContext != null) {
            if (mCanUseARFaceType == HUMAN_AI_TYPE_MS)
                NvsStreamingContext.closeHumanDetection();
            NvsStreamingContext.close();
            mStreamingContext = null;
            TimelineData.instance().clear();
            BackupData.instance().clear();
            // 获取当前进程的id
            int pid = android.os.Process.myPid();
            // 这个方法只能用于自杀操作
            android.os.Process.killProcess(pid);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isClickRepeat = false;
    }

    /**
     * 获取activity需要的权限列表
     *
     * @return 权限列表
     */
    @Override
    protected List<String> initPermissions() {
        return Util.getAllPermissionsList();
    }

    /**
     * 获取权限后
     */
    @Override
    protected void hasPermission() {
        Log.e(TAG, "hasPermission: 所有权限都有了");
        initARSceneEffect();//初始化人脸Model
        doClick(clickedView);
    }

    /**
     * 没有允许权限
     */
    @Override
    protected void nonePermission() {
        Log.e(TAG, "hasPermission: 没有允许权限");
    }

    /**
     * 用户选择了不再提示
     */
    @Override
    protected void noPromptPermission() {
        Log.e(TAG, "hasPermission: 用户选择了不再提示");
        startAppSettings();
    }

    // 启动应用的设置
    public void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MobclickAgent.onKillProcess(this);
    }
}
