package com.meishe.sdkdemo.edit.clipEdit.correctionColor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.R;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.edit.clipEdit.SingleClipFragment;
import com.meishe.sdkdemo.edit.data.BackupData;
import com.meishe.sdkdemo.edit.interfaces.OnTitleBarClickListener;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.AppManager;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.OnSeekBarChangeListenerAbs;
import com.meishe.sdkdemo.utils.TimelineUtil;
import com.meishe.sdkdemo.utils.dataInfo.ClipInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.util.ArrayList;
import java.util.List;


/**
 * CorrectionColorActivity class 校色页面
 *
 * @author czl
 * @date 2018-07-15
 */
public class CorrectionColorActivity extends BaseActivity {
    private CustomTitleBar mTitleBar;
    private RelativeLayout mBottomLayout;
    private SeekBar mColorSeekBar;
    private RecyclerView mColorTypeRv;
    private ColorTypeAdapter mColorTypeAdapter;
    private ImageView mCorrectionColorFinish;
    private SingleClipFragment mClipFragment;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private NvsVideoFx mColorVideoFx, mVignetteVideoFx, mSharpenVideoFx, mCurrentVideoFx;
    private ImageView mColorResetImageView;
    private ArrayList<ClipInfo> mClipArrayList;
    private int mCurClipIndex = 0;
    private String mCurrentColorType;
    private static final float floatZero = 0.000001f;

    @Override
    protected int initRootView() {
        mStreamingContext = NvsStreamingContext.getInstance();
        return R.layout.activity_correction_color;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        mColorSeekBar = (SeekBar) findViewById(R.id.colorSeekBar);
        mColorResetImageView = (ImageView) findViewById(R.id.colorResetImageView);
        mCorrectionColorFinish = (ImageView) findViewById(R.id.correctionColorFinish);
        mColorTypeRv = (RecyclerView) findViewById(R.id.colorTypeRv);
        mColorSeekBar.setMax(100);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.correctionColor);
        mTitleBar.setBackImageVisible(View.GONE);
    }

    @Override
    protected void initData() {
        mClipArrayList = BackupData.instance().cloneClipInfoData();
        mCurClipIndex = BackupData.instance().getClipIndex();
        mCurClipIndex = BackupData.instance().getClipIndex();
        if (mCurClipIndex < 0 || mCurClipIndex >= mClipArrayList.size()) {
            return;
        }
        ClipInfo clipInfo = mClipArrayList.get(mCurClipIndex);
        if (clipInfo == null) {
            return;
        }
        mTimeline = TimelineUtil.createSingleClipTimeline(clipInfo, true);
        if (mTimeline == null) {
            return;
        }

        int brightnessVal = getCurProgress(clipInfo.getBrightnessVal());
        mColorSeekBar.setProgress(brightnessVal);

        updateClipColorVal(clipInfo);
        initVideoFragment();
        initColorTypeRv();
    }

    private void initColorTypeRv() {
        List<ColorTypeItem> colorTypeItems = new ArrayList<>();
        ColorTypeItem colorTypeItem1 = new ColorTypeItem();
        colorTypeItem1.setFxName(Constants.FX_COLOR_PROPERTY);
        colorTypeItem1.setColorTypeName(Constants.FX_COLOR_PROPERTY_BRIGHTNESS);
        colorTypeItem1.setColorAtrubuteText(getResources().getString(R.string.brightness));
        colorTypeItems.add(colorTypeItem1);
        ColorTypeItem colorTypeItem2 = new ColorTypeItem();
        colorTypeItem2.setFxName(Constants.FX_COLOR_PROPERTY);
        colorTypeItem2.setColorTypeName(Constants.FX_COLOR_PROPERTY_CONTRAST);
        colorTypeItem2.setColorAtrubuteText(getResources().getString(R.string.contrast));
        colorTypeItems.add(colorTypeItem2);
        ColorTypeItem colorTypeItem3 = new ColorTypeItem();
        colorTypeItem3.setFxName(Constants.FX_COLOR_PROPERTY);
        colorTypeItem3.setColorTypeName(Constants.FX_COLOR_PROPERTY_SATURATION);
        colorTypeItem3.setColorAtrubuteText(getResources().getString(R.string.saturation));
        colorTypeItems.add(colorTypeItem3);
        ColorTypeItem colorTypeItem4 = new ColorTypeItem();
        colorTypeItem4.setFxName(Constants.FX_VIGNETTE);
        colorTypeItem4.setColorTypeName(Constants.FX_VIGNETTE_DEGREE);
        colorTypeItem4.setColorAtrubuteText(getResources().getString(R.string.degree));
        colorTypeItems.add(colorTypeItem4);
        ColorTypeItem colorTypeItem5 = new ColorTypeItem();
        colorTypeItem5.setFxName(Constants.FX_SHARPEN);
        colorTypeItem5.setColorTypeName(Constants.FX_SHARPEN_AMOUNT);
        colorTypeItem5.setColorAtrubuteText(getResources().getString(R.string.sharpness));
        colorTypeItems.add(colorTypeItem5);

        // 当前选中
        colorTypeItem1.setSelected(true);
        mCurrentVideoFx = mColorVideoFx;
        mCurrentColorType = Constants.FX_COLOR_PROPERTY_BRIGHTNESS;

        mColorTypeAdapter = new ColorTypeAdapter(this, colorTypeItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mColorTypeRv.setLayoutManager(linearLayoutManager);
        mColorTypeRv.setAdapter(mColorTypeAdapter);

        mColorTypeAdapter.setOnItemClickListener(new ColorTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ColorTypeItem colorTypeItem) {
                if (colorTypeItem == null || colorTypeItem.getColorTypeName() == null || colorTypeItem.getFxName() == null) {
                    return;
                }
                // 校色参数
                mCurrentColorType = colorTypeItem.getColorTypeName();
                if (colorTypeItem.getFxName().equals(Constants.FX_COLOR_PROPERTY)) {
                    mCurrentVideoFx = mColorVideoFx;
                    mColorSeekBar.setProgress(getCurProgress((float) mCurrentVideoFx.getFloatVal(mCurrentColorType)));
                } else if (colorTypeItem.getFxName().equals(Constants.FX_VIGNETTE)) {
                    mCurrentVideoFx = mVignetteVideoFx;
                    mColorSeekBar.setProgress((int) (mCurrentVideoFx.getFloatVal(mCurrentColorType) * 100));
                } else if (colorTypeItem.getFxName().equals(Constants.FX_SHARPEN)) {
                    mCurrentVideoFx = mSharpenVideoFx;
                    mColorSeekBar.setProgress((int) (mCurrentVideoFx.getFloatVal(mCurrentColorType) * 100));
                }
            }
        });
    }

    private float getFloatColorVal(int progress) {
        return progress < 50 ? progress / 50.f : progress * 9 / 50.0f - 8;
    }

    private float getFloatColorVal2(int progress) {
        return progress / 100.0f;
    }

    private int getCurProgress(float colVal) {
        int curProgress = 50;
        if (colVal < 0) {
            return curProgress;
        }
        for (int progress = 0; progress < 100; ++progress) {
            float result = progress < 50 ? progress / 50.0f : progress * 9 / 50.0f - 8;

            result = result - colVal;
            if (result >= -floatZero && result <= floatZero) {
                curProgress = progress;
                break;
            }
        }
        return curProgress;
    }

    private void updateClipColorVal(ClipInfo clipInfo) {
        NvsVideoTrack videoTrack = mTimeline.getVideoTrackByIndex(0);
        if (videoTrack == null) {
            return;
        }
        NvsVideoClip videoClip = videoTrack.getClipByIndex(0);
        if (videoClip == null) {
            return;
        }
        int fxCount = videoClip.getFxCount();
        for (int index = 0; index < fxCount; ++index) {
            NvsVideoFx videoFx = videoClip.getFxByIndex(index);
            if (videoFx == null) {
                continue;
            }

            String fxName = videoFx.getBuiltinVideoFxName();
            if (fxName == null || TextUtils.isEmpty(fxName)) {
                continue;
            }
            if (fxName.equals(Constants.FX_COLOR_PROPERTY)) {
                mColorVideoFx = videoFx;
            } else if (fxName.equals(Constants.FX_VIGNETTE)) {
                mVignetteVideoFx = videoFx;
            } else if (fxName.equals(Constants.FX_SHARPEN)) {
                mSharpenVideoFx = videoFx;
            }
        }
        if (mColorVideoFx == null) {
            mColorVideoFx = videoClip.appendBuiltinFx(Constants.FX_COLOR_PROPERTY);
        }
        if (mVignetteVideoFx == null) {
            mVignetteVideoFx = videoClip.appendBuiltinFx(Constants.FX_VIGNETTE);
        }
        if (mSharpenVideoFx == null) {
            mSharpenVideoFx = videoClip.appendBuiltinFx(Constants.FX_SHARPEN);
        }
        if (mColorVideoFx == null || mSharpenVideoFx == null || mVignetteVideoFx == null) {
            return;
        }

        float brightVal = clipInfo.getBrightnessVal();
        float contrastVal = clipInfo.getContrastVal();
        float saturationVal = clipInfo.getSaturationVal();
        float vignette = clipInfo.getVignetteVal();
        float sharpen = clipInfo.getSharpenVal();
        if (brightVal >= 0 || contrastVal >= 0 || saturationVal >= 0) {
            if (brightVal >= 0) {
                mColorVideoFx.setFloatVal(Constants.FX_COLOR_PROPERTY_BRIGHTNESS, brightVal);
            }
            if (contrastVal >= 0) {
                mColorVideoFx.setFloatVal(Constants.FX_COLOR_PROPERTY_CONTRAST, contrastVal);
            }
            if (saturationVal >= 0) {
                mColorVideoFx.setFloatVal(Constants.FX_COLOR_PROPERTY_SATURATION, saturationVal);
            }
        }
        if (vignette >= 0) {
            mVignetteVideoFx.setFloatVal(Constants.FX_VIGNETTE_DEGREE, vignette);
        }
        if (sharpen >= 0) {
            mSharpenVideoFx.setFloatVal(Constants.FX_SHARPEN_AMOUNT, sharpen);
        }
    }

    @Override
    protected void initListener() {
        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                removeTimeline();
            }

            @Override
            public void OnCenterTextClick() {
            }

            @Override
            public void OnRightTextClick() {
            }
        });
        mCorrectionColorFinish.setOnClickListener(this);
        mColorSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAbs() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (mCurrentVideoFx != null && mCurrentColorType != null) {
                        float colorVal = 0;
                        if (mCurrentColorType.equals(Constants.FX_COLOR_PROPERTY_BRIGHTNESS)) {
                            colorVal = getFloatColorVal(progress);
                            mCurrentVideoFx.setFloatVal(Constants.FX_COLOR_PROPERTY_BRIGHTNESS, colorVal);
                            mClipArrayList.get(mCurClipIndex).setBrightnessVal(colorVal);
                        } else if (mCurrentColorType.equals(Constants.FX_COLOR_PROPERTY_CONTRAST)) {
                            colorVal = getFloatColorVal(progress);
                            mCurrentVideoFx.setFloatVal(Constants.FX_COLOR_PROPERTY_CONTRAST, colorVal);
                            mClipArrayList.get(mCurClipIndex).setContrastVal(colorVal);
                        } else if (mCurrentColorType.equals(Constants.FX_COLOR_PROPERTY_SATURATION)) {
                            colorVal = getFloatColorVal(progress);
                            mCurrentVideoFx.setFloatVal(Constants.FX_COLOR_PROPERTY_SATURATION, colorVal);
                            mClipArrayList.get(mCurClipIndex).setSaturationVal(colorVal);
                        } else if (mCurrentColorType.equals(Constants.FX_VIGNETTE_DEGREE)) {
                            colorVal = getFloatColorVal2(progress);
                            mCurrentVideoFx.setFloatVal(Constants.FX_VIGNETTE_DEGREE, colorVal);
                            mClipArrayList.get(mCurClipIndex).setVignetteVal(colorVal);
                        } else if (mCurrentColorType.equals(Constants.FX_SHARPEN_AMOUNT)) {
                            colorVal = getFloatColorVal2(progress);
                            mCurrentVideoFx.setFloatVal(Constants.FX_SHARPEN_AMOUNT, colorVal);
                            mClipArrayList.get(mCurClipIndex).setSharpenVal(colorVal);
                        }
                        if (mClipFragment.getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                            mClipFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                        }
                    }
                }
            }
        });
        // 重置校色参数
        mColorResetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentVideoFx != null && mCurrentColorType != null) {
                    float colorVal = 0;
                    if (mCurrentColorType.equals(Constants.FX_COLOR_PROPERTY_BRIGHTNESS)) {
                        colorVal = getFloatColorVal(50);
                        mCurrentVideoFx.setFloatVal(Constants.FX_COLOR_PROPERTY_BRIGHTNESS, colorVal);
                        mColorSeekBar.setProgress(50);
                        mClipArrayList.get(mCurClipIndex).setBrightnessVal(colorVal);
                    } else if (mCurrentColorType.equals(Constants.FX_COLOR_PROPERTY_CONTRAST)) {
                        colorVal = getFloatColorVal(50);
                        mCurrentVideoFx.setFloatVal(Constants.FX_COLOR_PROPERTY_CONTRAST, colorVal);
                        mColorSeekBar.setProgress(50);
                        mClipArrayList.get(mCurClipIndex).setContrastVal(colorVal);
                    } else if (mCurrentColorType.equals(Constants.FX_COLOR_PROPERTY_SATURATION)) {
                        colorVal = getFloatColorVal(50);
                        mCurrentVideoFx.setFloatVal(Constants.FX_COLOR_PROPERTY_SATURATION, colorVal);
                        mColorSeekBar.setProgress(50);
                        mClipArrayList.get(mCurClipIndex).setSaturationVal(colorVal);
                    } else if (mCurrentColorType.equals(Constants.FX_VIGNETTE_DEGREE)) {
                        colorVal = getFloatColorVal2(0);
                        mCurrentVideoFx.setFloatVal(Constants.FX_VIGNETTE_DEGREE, colorVal);
                        mColorSeekBar.setProgress(0);
                        mClipArrayList.get(mCurClipIndex).setVignetteVal(colorVal);
                    } else if (mCurrentColorType.equals(Constants.FX_SHARPEN_AMOUNT)) {
                        colorVal = getFloatColorVal2(0);
                        mCurrentVideoFx.setFloatVal(Constants.FX_SHARPEN_AMOUNT, colorVal);
                        mColorSeekBar.setProgress(0);
                        mClipArrayList.get(mCurClipIndex).setSharpenVal(colorVal);
                    }
                    if (mClipFragment.getCurrentEngineState() != NvsStreamingContext.STREAMING_ENGINE_STATE_PLAYBACK) {
                        mClipFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.correctionColorFinish:
                BackupData.instance().setClipInfoData(mClipArrayList);
                removeTimeline();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                AppManager.getInstance().finishActivity();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        removeTimeline();
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void removeTimeline() {
        TimelineUtil.removeTimeline(mTimeline);
        mTimeline = null;
    }

    private void initVideoFragment() {
        mClipFragment = new SingleClipFragment();
        mClipFragment.setFragmentLoadFinisedListener(new SingleClipFragment.OnFragmentLoadFinisedListener() {
            @Override
            public void onLoadFinished() {
                mClipFragment.seekTimeline(mStreamingContext.getTimelineCurrentPosition(mTimeline), NvsStreamingContext.STREAMING_ENGINE_SEEK_FLAG_SHOW_CAPTION_POSTER);
            }
        });
        mClipFragment.setTimeline(mTimeline);
        Bundle bundle = new Bundle();
        bundle.putInt("titleHeight", mTitleBar.getLayoutParams().height);
        bundle.putInt("bottomHeight", mBottomLayout.getLayoutParams().height);
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        mClipFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.spaceLayout, mClipFragment)
                .commit();
        getFragmentManager().beginTransaction().show(mClipFragment);
    }
}
