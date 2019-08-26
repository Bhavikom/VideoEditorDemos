package com.meishe.sdkdemo.edit.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.edit.interfaces.OnTitleBarClickListener;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.TimeFormatUtil;
import com.meishe.sdkdemo.utils.dataInfo.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ms on 2018/7/13 0013.
 */

public class SelectMusicActivity extends BaseActivity{
    private TextView mMyMusicBtn, mLocalMusicBtn;
    private View mMyMusicSelectView, mLocalMusicSelectView;
    private CustomViewPager mViewPager;
    private boolean mRightSelected = false;
    private LocalMusicFragment mLocalMusicFragment;
    private MyMusicFragment mMyMusicFragment;
    private List<Fragment> mFragmentList;
    private String[] mFragmentTitle;
    private CustomTitleBar mTitleBar;
    private AudioUtil mAudioUtil;
    private MusicInfo mPlayMusic;
    private RelativeLayout mHaveSelectLayout, mCutViewLayout;
    private TextView mSelectMusicName, mSelectMusicTime;
    private CutMusicView mCutMusicView;
    private Button mUseBtn, mNoMusicBtn;
    private int mFromWhatPage = Constants.SELECT_MUSIC_FROM_EDIT;
    private int mDouyinMinDuration = 15000000;
    private RelativeLayout mTabLayout;

    @Override
    protected int initRootView() {
        return R.layout.activity_select_music;
    }

    @Override
    protected void initViews() {
        mTabLayout = (RelativeLayout)findViewById(R.id.tab_layout);
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mMyMusicBtn = (TextView) findViewById(R.id.my_music_text);
        mLocalMusicBtn = (TextView) findViewById(R.id.local_music_text);
        mMyMusicSelectView = findViewById(R.id.my_music_select_view);
        mLocalMusicSelectView = findViewById(R.id.local_music_select_view);
        mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        mHaveSelectLayout = (RelativeLayout) findViewById(R.id.have_select_layout);
        mSelectMusicName = (TextView) findViewById(R.id.select_music_name);
        mSelectMusicTime = (TextView) findViewById(R.id.select_music_time);
        mCutMusicView = (CutMusicView) findViewById(R.id.select_music_cut_view);
        mUseBtn = (Button) findViewById(R.id.select_music_use_btn);
        mNoMusicBtn = (Button) findViewById(R.id.no_music_btn);
        mNoMusicBtn.getPaint().setFakeBoldText(true);
        mCutViewLayout = (RelativeLayout) findViewById(R.id.select_music_cut_layout);

        initViewPager();
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.select_music);
    }

    @Override
    protected void initData() {
        // bundle
        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                mFromWhatPage = bundle.getInt(Constants.SELECT_MUSIC_FROM, Constants.SELECT_MUSIC_FROM_EDIT);
            }
        }
        mFragmentTitle = getResources().getStringArray(R.array.music_fragment_title);
        mAudioUtil = new AudioUtil(this);

        if(mFromWhatPage == Constants.SELECT_MUSIC_FROM_MUSICLYRICS){
            mViewPager.setCurrentItem(1);
            mTabLayout.setVisibility(View.GONE);
            mNoMusicBtn.setVisibility(View.GONE);
            mTitleBar.setBackImageVisible(View.VISIBLE);
            mViewPager.setScanScroll(false);
        }

        mAudioUtil.getMedias(Constants.MEDIA_TYPE_AUDIO, new AudioUtil.LocalMediaLoadListener() {
            @Override
            public void loadComplete(List<MusicInfo> medias) {
                if(mLocalMusicFragment != null) {
                    mLocalMusicFragment.loadAudioData(medias);
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<MusicInfo> audioInfoList = mAudioUtil.listMusicFilesFromAssets(SelectMusicActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mMyMusicFragment != null) {
                            mMyMusicFragment.loadAudioData(audioInfoList);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    protected void initListener() {
        mLocalMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocalMusicBtn.setTextColor(ContextCompat.getColor(SelectMusicActivity.this, R.color.ms_blue));
                mMyMusicBtn.setTextColor(ContextCompat.getColor(SelectMusicActivity.this, R.color.white));
                if (mRightSelected) {
                    mRightSelected = false;
                    rightToLeft();
                    mViewPager.setCurrentItem(0);
                }
            }
        });

        mMyMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyMusicBtn.setTextColor(ContextCompat.getColor(SelectMusicActivity.this, R.color.ms_blue));
                mLocalMusicBtn.setTextColor(ContextCompat.getColor(SelectMusicActivity.this, R.color.white));
                if (!mRightSelected) {
                    mRightSelected = true;
                    leftToRight();
                    mViewPager.setCurrentItem(1);
                }
            }
        });

        mUseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMusicActivity();
            }
        });

        mCutMusicView.setOnSeekBarChangedListener(new CutMusicView.OnSeekBarChanged() {
            @Override
            public void onLeftValueChange(long var) {
                if(mPlayMusic != null) {
                    mSelectMusicTime.setText(TimeFormatUtil.formatUsToString2(var) + "/" +
                            TimeFormatUtil.formatUsToString2(mPlayMusic.getTrimOut()));
                }
            }

            @Override
            public void onRightValueChange(long var) {
                if(mPlayMusic != null) {
                    mSelectMusicTime.setText(TimeFormatUtil.formatUsToString2(mPlayMusic.getTrimIn()) + "/" +
                            TimeFormatUtil.formatUsToString2(var));
                }
            }

            @Override
            public void onCenterTouched(long left, long right) {

            }

            @Override
            public void onUpTouched(boolean touch_left, long left, long right) {
                if(mPlayMusic != null) {
                    mPlayMusic.setTrimIn(left);
                    mPlayMusic.setTrimOut(right);
                }
                if(touch_left) {
                    if (mPlayMusic != null && mPlayMusic.isPlay()) {
                        AudioPlayer.getInstance(SelectMusicActivity.this).seekPosition(left);
                    }
                    mCutMusicView.setIndicator(left);
                }
            }
        });

        AudioPlayer.getInstance(this).setPlayListener(new AudioPlayer.OnPlayListener() {
            @Override
            public void onMusicPlay() {

            }

            @Override
            public void onMusicStop() {

            }

            @Override
            public void onGetCurrentPos(int curPos) {
                mCutMusicView.setIndicator(curPos);

                mSelectMusicTime.setText(TimeFormatUtil.formatUsToString2(curPos) + "/" +
                        TimeFormatUtil.formatUsToString2(mPlayMusic.getTrimOut()));
            }
        });

        mNoMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayMusic = null;
                closeMusicActivity();
            }
        });
        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                AudioPlayer.getInstance(getApplicationContext()).destroyPlayer();
            }

            @Override
            public void OnCenterTextClick() {

            }

            @Override
            public void OnRightTextClick() {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    private void rightToLeft() {
        TranslateAnimation ani = new TranslateAnimation(mMyMusicSelectView.getX(), mLocalMusicSelectView.getX(),
                mMyMusicSelectView.getY(), mLocalMusicSelectView.getY());
        ani.setDuration(300);
        ani.setFillAfter(true);
        mLocalMusicSelectView.startAnimation(ani);
    }

    private void leftToRight() {
        TranslateAnimation ani = new TranslateAnimation(mLocalMusicSelectView.getX(), mMyMusicSelectView.getX(),
                mLocalMusicSelectView.getY(), mMyMusicSelectView.getY());
        ani.setDuration(300);
        ani.setFillAfter(true);
        mLocalMusicSelectView.startAnimation(ani);
    }

    private void initViewPager() {
        mFragmentList = new ArrayList<>();
        mLocalMusicFragment = new LocalMusicFragment();
        mMyMusicFragment = new MyMusicFragment();
        mFragmentList.add(mLocalMusicFragment);
        mFragmentList.add(mMyMusicFragment);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    mLocalMusicBtn.performClick();
                } else if(position == 1) {
                    mMyMusicBtn.performClick();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FragmentPagerAdapter fragment_adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentTitle[position];
            }
        };
        mViewPager.setAdapter(fragment_adapter);
        mViewPager.setCurrentItem(0);
    }

    public void playMusic(MusicInfo musicInfo, boolean is_local) {
        if (musicInfo == null)
            return;
        if(is_local) {
            mMyMusicFragment.clearPlayState();
        } else {
            mLocalMusicFragment.clearPlayState();
        }
        mPlayMusic = musicInfo;
        if (mPlayMusic.isPlay()) {
            // 已经选择音乐，处理UI
            mHaveSelectLayout.setVisibility(View.GONE);
            AudioPlayer.getInstance(this).stopPlay();
        } else {
            // 已经选择音乐，处理UI
            mHaveSelectLayout.setVisibility(View.VISIBLE);
            mCutViewLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSelectMusicName.setText(mPlayMusic.getTitle());
                    String duration_text = TimeFormatUtil.formatUsToString2(mPlayMusic.getDuration());
                    if(mFromWhatPage == Constants.SELECT_MUSIC_FROM_DOUYIN) {
                        mSelectMusicTime.setText("00:00/" + duration_text);
                        mCutMusicView.setRightHandleVisiable(false);
                        mCutMusicView.setMinDuration(mDouyinMinDuration);
                    } else {
                        mSelectMusicTime.setText(mPlayMusic.getArtist());
                        mCutMusicView.setRightHandleVisiable(true);
                        mCutMusicView.setMinDuration(Constants.MUSIC_MIN_DURATION);
                    }
                    mCutMusicView.setCutLayoutWidth(mCutViewLayout.getWidth());
                    mCutMusicView.setCanTouchCenterMove(false);
                    mCutMusicView.setMaxDuration(mPlayMusic.getDuration());
                    mCutMusicView.setInPoint(0);
                    mCutMusicView.setOutPoint(mPlayMusic.getDuration());
                    mCutMusicView.reLayout();
                }
            }, 100);

            mPlayMusic.setTrimIn(0);
            mPlayMusic.setTrimOut(mPlayMusic.getDuration());
            AudioPlayer.getInstance(this).setCurrentMusic(mPlayMusic, true);
            AudioPlayer.getInstance(this).startPlay();
        }
    }
    private void closeMusicActivity(){
        Intent intent = new Intent();
        if (mPlayMusic != null) {
            if(mFromWhatPage == Constants.SELECT_MUSIC_FROM_DOUYIN) {
                if(mPlayMusic.getDuration() - mPlayMusic.getTrimIn() <= mDouyinMinDuration) {
                    mPlayMusic.setTrimOut(mPlayMusic.getDuration() - mPlayMusic.getTrimIn());
                } else {
                    mPlayMusic.setTrimOut(mPlayMusic.getTrimIn() + mDouyinMinDuration);
                }
            }
        }
        intent.putExtra("select_music", mPlayMusic);
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AudioPlayer.getInstance(getApplicationContext()).stopPlay();
    }

    private void finishActivity(){
        AudioPlayer.getInstance(getApplicationContext()).destroyPlayer();
        AppManager.getInstance().finishActivity();
    }
}
