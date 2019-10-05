package com.example.commonDemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.lansoeditor.demo.R;
import com.lansosdk.videoeditor.MediaInfo;

public class MediaInfoActivity extends Activity{

	TextView  tvInfo;
	String videoPath=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.mediainfo_layout);
		tvInfo=(TextView)findViewById(R.id.id_mediainfo_tv);
		
		 videoPath=getIntent().getStringExtra("videopath");
		 
		 tvInfo.setText(getMediaInfo());
	}
	
	private String getMediaInfo()
	{
		String retStr="";
		MediaInfo info=new MediaInfo(videoPath);
	
		if(info.prepare())
		{
			retStr+="文件路径: " 	+	info.filePath	+	" \n";
			retStr+="文件名字: " 	+	info.fileName	+	" \n";
			retStr+="文件后缀: " 	+	info.fileSuffix	+	" \n";
			
			if(info.vCodecHeight>0  && info.vCodecWidth>0)
			{
				retStr+="\n视频信息----------------------\n";
				
				retStr+="宽度: "	+	info.vWidth		+	" \n";
				retStr+="高度: "	+	info.vHeight	+	" \n";
				retStr+="编码宽度: "	+	info.vCodecWidth	+	" \n";
				retStr+="编码高度: "	+	info.vCodecHeight	+	" \n";
				retStr+="总时长: "	+	info.vDuration	+"(秒)"+	" \n";
				
				retStr+="比特率(码率): "	+	info.vBitRate	+ " bit/s" +	" \n";
				retStr+="帧率: "	+	info.vFrameRate	+	 " frame/s" +	" \n";
				retStr+="像素格式: "	+	info.vPixelFmt	+	" \n";
				retStr+="旋转角度: "	+	info.vRotateAngle	+	" \n";
				retStr+="总帧数: "	+	info.vTotalFrames	+	" \n";
				retStr+="是否有B帧: "	+	info.vHasBFrame	+	" \n";
				retStr+="可采用的解码器: ";
			
				if(info.vCodecName.equals("lansoh264_dec"))
					retStr+="h264,";
				
				retStr+=info.vCodecName	+	" \n";
				
			}
			if(info.aBitRate>0)
			{
				retStr+="\n音频信息----------------------\n";
				retStr+="时长: " + info.aDuration + "(秒)" + "\n";
				retStr+="采样率: " + info.aSampleRate	+ "\n";
				retStr+="通道数: " + info.aChannels + "\n";
				retStr+="总帧数: " + info.aTotalFrames + "\n";
				retStr+="比特率(码率): " + info.aBitRate + "\n";
				retStr+="最大比特率(最大码率): " + info.aMaxBitRate + "\n";
				retStr+="可采用的解码器: " + info.aCodecName + "\n";
				
			}
		}else{
			retStr="无法获取文件的音视频信息,请检查您的文件是否是音视频文件.";
		}
		return retStr;
	}
	
	
	
}
