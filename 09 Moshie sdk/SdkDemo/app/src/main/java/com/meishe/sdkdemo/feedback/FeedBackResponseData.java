package com.meishe.sdkdemo.feedback;

/**
 * Created by CaoZhiChao on 2018/11/29 11:10
 * errString
 */
public class FeedBackResponseData {
    private int errNo;

    //这个不关注。现在后台返回信息无用
//    private String errString;
    public FeedBackResponseData() {
    }

    public FeedBackResponseData(int errNo) {
        this.errNo = errNo;
    }

    public int getErrNo() {
        return errNo;
    }

    public void setErrNo(int errNo) {
        this.errNo = errNo;
    }
}
