package com.example.commonDemo;

public class DemoInfo {

	public final int mHintId;
	public final int mTextId;
	public final boolean isOutVideo;
	public final boolean isOutAudio;
	
	public DemoInfo(int hintId,int textId,boolean outvideo,boolean outaudio)
	{
		mHintId=hintId;
		mTextId=textId;
		isOutVideo=outvideo;
		isOutAudio=outaudio;
	}
}
