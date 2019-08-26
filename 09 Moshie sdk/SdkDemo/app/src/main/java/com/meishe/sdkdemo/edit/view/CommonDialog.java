package com.meishe.sdkdemo.edit.view;

/**
 * Created by ms on 2018/2/6.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meishe.sdkdemo.R;

public class CommonDialog extends Dialog {
    private Context mContext;
    private TextView mTitleTxt;
    private TextView mFirstTipsTxt;
    private TextView mSecondTipsTxt;
    private Button mOkBtn, mCandelBtn;
    private RelativeLayout mOkBtnLayout, mCancelBtnLayout;
    private int mBtnCount = 1;
    private OnBtnClickListener mClickListener;
    private OnCreateListener mCreateListener;

    public CommonDialog(Context context, int btn_count) {
        super(context, R.style.dialog);
        mContext = context;
        mBtnCount = btn_count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog);
        setCanceledOnTouchOutside(false);
        initView();
        if(mCreateListener != null) {
            mCreateListener.OnCreated();
        }
    }

    private void initView() {
        mTitleTxt = (TextView) findViewById(R.id.dialog_title);
        mFirstTipsTxt = (TextView) findViewById(R.id.dialog_first_tip);
        mSecondTipsTxt = (TextView) findViewById(R.id.dialog_second_tip);
        mOkBtn = (Button) findViewById(R.id.ok_btn);
        mCandelBtn = (Button) findViewById(R.id.cancel_btn);
        mCancelBtnLayout = (RelativeLayout) findViewById(R.id.cancel_btn_layout);
        mOkBtnLayout = (RelativeLayout) findViewById(R.id.ok_btn_layout);

        if (mBtnCount == 1) {
            mOkBtnLayout.setVisibility(View.VISIBLE);
            mCancelBtnLayout.setVisibility(View.GONE);
        } else {
            mCancelBtnLayout.setVisibility(View.VISIBLE);
            mOkBtnLayout.setVisibility(View.VISIBLE);
        }

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener != null) {
                    mClickListener.OnOkBtnClicked(view);
                }
            }
        });

        mCandelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener != null) {
                    mClickListener.OnCancelBtnClicked(view);
                }
            }
        });
    }

    public void setTitleTxt(String title) {
        mTitleTxt.setText(title);
    }

    public void setFirstTipsTxt(String tips) {
        mFirstTipsTxt.setText(tips);
    }

    public void setSecondTipsTxt(String tips) {
        mSecondTipsTxt.setVisibility(View.VISIBLE);
        mSecondTipsTxt.setText(tips);
    }

    public void setBtnText(String cancel_text, String ok_text) {
        mCandelBtn.setText(cancel_text);
        mOkBtn.setText(ok_text);
    }

    public void setOnBtnClickListener(OnBtnClickListener listener) {
        mClickListener = listener;
    }

    public interface OnBtnClickListener {
        void OnOkBtnClicked(View view);

        void OnCancelBtnClicked(View view);
    }
    public void setOnCreateListener(OnCreateListener listener) {
        mCreateListener = listener;
    }

    public interface OnCreateListener {
        void OnCreated();
    }
}

