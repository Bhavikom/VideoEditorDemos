package com.meishe.sdkdemo;

import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.meicam.sdk.NvsStreamingContext;
import com.meishe.sdkdemo.base.BaseActivity;
import com.meishe.sdkdemo.edit.interfaces.OnTitleBarClickListener;
import com.meishe.sdkdemo.edit.view.CustomTitleBar;
import com.meishe.sdkdemo.utils.Constants;
import com.meishe.sdkdemo.utils.ParameterSettingValues;
import com.meishe.sdkdemo.utils.SpUtil;
import com.meishe.sdkdemo.utils.ToastUtil;
import com.meishe.sdkdemo.utils.Util;

/**
 * Created by admin on 2018-5-25.
 */

public class ParameterSettingActivity extends BaseActivity {
    private String TAG = "ParameterSettingActivity";
    public static final int CompileVideoRes_2160 = 2160;
    public static final int CompileVideoRes_1080 = 1080;
    public static final int CompileVideoRes_720 = 720;
    public static final int CompileVideoRes_540 = 540;

    private CustomTitleBar mTitleBar;
    private RadioGroup mCapture_ratio_sex;
    private RadioButton mCapcture_ratio_1080;
    private RadioButton mCapcture_ratio_720;
    private RadioGroup mOut_ratio_sex;
    private RadioButton mOut_ratio_4k;
    private RadioButton mOut_ratio_1080;
    private RadioButton mOut_ratio_720;
    private RadioButton mOut_ratio_540;
    private EditText mOutput_bitrate_editText;
    private Switch mEncoder_support;
    private Switch mBackgroud_blur;
    private int mCaptureResolutionGrade;
    private int mCompileVidoeRes;
    private double mCompileBitrate;
    private boolean mDisableDeviceEncoderSupport = false;
    private boolean mIsUseBackgroundBlur = false;
    private SpUtil mSp;

    private TextView mSDKVersion;
    @Override
    protected int initRootView() {
        return R.layout.activity_parameter_setting;
    }

    @Override
    protected void initViews() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
        mCapture_ratio_sex = (RadioGroup)findViewById(R.id.capture_ratio_sex);
        mCapcture_ratio_1080 = (RadioButton)findViewById(R.id.capture_ratio_1080);
        mCapcture_ratio_720 = (RadioButton)findViewById(R.id.capture_ratio_720);
        mOut_ratio_sex = (RadioGroup)findViewById(R.id.out_ratio_sex);
        mOut_ratio_4k = (RadioButton)findViewById(R.id.output_ratio_4k);
        mOut_ratio_1080 = (RadioButton)findViewById(R.id.output_ratio_1080);
        mOut_ratio_720 = (RadioButton)findViewById(R.id.output_ratio_720);
        mOut_ratio_540 = (RadioButton)findViewById(R.id.output_ratio_540);
        mOutput_bitrate_editText  = (EditText)findViewById(R.id.output_bitrate_editText);
        mEncoder_support = (Switch)findViewById(R.id.encoder_support);
        mBackgroud_blur = (Switch)findViewById(R.id.backgroud_blur);
        mSDKVersion = (TextView)findViewById(R.id.sdkVersion);
    }

    @Override
    protected void initTitle() {
        mTitleBar.setTextCenter(R.string.setting);
    }

    @Override
    public void initData() {
        ParameterSettingValues parameterValues = ParameterSettingValues.instance();
        mCaptureResolutionGrade = parameterValues.getCaptureResolutionGrade();
        mCompileVidoeRes = parameterValues.getCompileVideoRes();
        mCompileBitrate = parameterValues.getCompileBitrate();
        mIsUseBackgroundBlur = parameterValues.isUseBackgroudBlur();
        mDisableDeviceEncoderSupport = parameterValues.disableDeviceEncorder();
        if (mCompileBitrate > 0)
            mOutput_bitrate_editText.setText(String.valueOf(mCompileBitrate));
        switch (mCaptureResolutionGrade){
            case NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_SUPER_HIGH:
                mCapcture_ratio_1080.setChecked(true);
                mCapcture_ratio_720.setChecked(false);
                break;
            case NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH:
                mCapcture_ratio_1080.setChecked(false);
                mCapcture_ratio_720.setChecked(true);
                break;
            default:
                mCapcture_ratio_1080.setChecked(false);
                mCapcture_ratio_720.setChecked(true);
                break;
        }

        switch (mCompileVidoeRes){
            case CompileVideoRes_2160:
                mOut_ratio_4k.setChecked(true);
                break;
            case CompileVideoRes_1080:
                mOut_ratio_1080.setChecked(true);
                break;
            case CompileVideoRes_720:
                mOut_ratio_720.setChecked(true);
                break;
            case CompileVideoRes_540:
                mOut_ratio_540.setChecked(true);
                break;
            default:
                mOut_ratio_720.setChecked(true);
                break;
        }

        setEditTextHint(mCompileVidoeRes);
        if (mIsUseBackgroundBlur){
            mBackgroud_blur.setChecked(true);
        }else {
            mBackgroud_blur.setChecked(false);
        }

        if (mDisableDeviceEncoderSupport){
            mEncoder_support.setChecked(true);
        }else {
            mEncoder_support.setChecked(false);
        }
        NvsStreamingContext.SdkVersion sdkVersion = mStreamingContext.getSdkVersion();
        StringBuilder stringBuilder = new StringBuilder("Meishe SDK ");
        stringBuilder.append(sdkVersion.majorVersion);
        stringBuilder.append(".");
        stringBuilder.append(sdkVersion.minorVersion);
        stringBuilder.append(".");
        stringBuilder.append(sdkVersion.revisionNumber);
        mSDKVersion.setText(stringBuilder.toString());
    }

    @Override
    protected void initListener() {
        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                setParameterSettingValues();
            }

            @Override
            public void OnCenterTextClick() {

            }

            @Override
            public void OnRightTextClick() {
            }
        });

        mCapture_ratio_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.capture_ratio_1080:
                        // 拍摄1080P
                        mCaptureResolutionGrade = NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_SUPER_HIGH;
                        break;
                    case R.id.capture_ratio_720:
                        // 拍摄720
                        mCaptureResolutionGrade = NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH;
                        break;
                }
            }
        });

        mOut_ratio_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.output_ratio_4k:
                        // 输出4K
                        mCompileVidoeRes = CompileVideoRes_2160;
                        break;
                    case R.id.output_ratio_1080:
                        // 输出1080
                        mCompileVidoeRes = CompileVideoRes_1080;
                        break;
                    case R.id.output_ratio_720:
                        // 输出720
                        mCompileVidoeRes = CompileVideoRes_720;
                        break;
                    case R.id.output_ratio_540:
                        // 输出480
                        mCompileVidoeRes = CompileVideoRes_540;
                        break;
                }
                setEditTextHint(mCompileVidoeRes);
            }
        });

        mOutput_bitrate_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                double value = Util.convertToDouble(text, 0);
                if(value < 0 || value > 200) {
                    String inputError = getResources().getString(R.string.input_error);
                    ToastUtil.showToast(ParameterSettingActivity.this,inputError);
                    return;
                }
                mCompileBitrate = Util.convertToDouble(text, 0);
            }
        });

        mEncoder_support.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDisableDeviceEncoderSupport = isChecked;
            }
        });
        mBackgroud_blur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsUseBackgroundBlur  = isChecked;
            }
        });
    }

    private void setEditTextHint(int compileResolutionGrade){
        String strHint;
        String[] settingSuggest = getResources().getStringArray(R.array.settingSuggest);
        switch (compileResolutionGrade) {
            case CompileVideoRes_2160:
                strHint = settingSuggest[0];
                break;
            case CompileVideoRes_1080:
                strHint = settingSuggest[1];
                break;
            case CompileVideoRes_720:
                strHint = settingSuggest[2];
                break;
            case CompileVideoRes_540:
                strHint = settingSuggest[3];
                break;
            default:
                strHint = settingSuggest[2];
                break;
        }
        mOutput_bitrate_editText.setHint(strHint);
    }
    @Override
    public void onBackPressed() {
        setParameterSettingValues();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

    }

    private void setParameterSettingValues() {
        ParameterSettingValues values = ParameterSettingValues.instance();
        mSp = SpUtil.getInstance(getApplicationContext());
        values.setCaptureResolutionGrade(mCaptureResolutionGrade);
        values.setCompileVideoRes(mCompileVidoeRes);
        values.setCompileBitrate(mCompileBitrate);
        values.setUseBackgroudBlur(mIsUseBackgroundBlur);
        values.setDisableDeviceEncorder(mDisableDeviceEncoderSupport);
        mSp.setObjectToShare(getApplicationContext(),values,Constants.KEY_PARAMTER);
    }
}
