package com.meishe.sdkdemo.feedback;

/**
 * Created by CaoZhiChao on 2018/11/29 10:25
 */
public class FeedBackData {
    private String content;
    private String contact;
    private String sdkVersion;
    private String deviceModel;

    public FeedBackData() {
    }

    public FeedBackData(String content, String contact, String sdkVersion, String deviceModel) {
        this.content = content;
        this.contact = contact;
        this.sdkVersion = sdkVersion;
        this.deviceModel = deviceModel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }
}
