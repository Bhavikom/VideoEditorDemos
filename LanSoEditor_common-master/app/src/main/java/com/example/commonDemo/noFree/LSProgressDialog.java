package com.example.commonDemo.noFree;

import android.app.Activity;
import android.app.ProgressDialog;

public class LSProgressDialog {

    ProgressDialog mProgressDialog;
    public void show(Activity acty) {
        release();
        mProgressDialog = new ProgressDialog(acty);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("正在处理中...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void setProgress(int percent){
        if (mProgressDialog != null) {
            mProgressDialog.setMessage("正在处理中..."
                    + String.valueOf(percent) + "%");
        }
    }
    public void release() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
            mProgressDialog = null;
        }
    }
}
