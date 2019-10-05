package com.example.commonDemo;

import android.content.Context;

import com.lansosdk.videoeditor.CopyFileFromAssets;
import com.lansosdk.videoeditor.LanSongFileUtil;
import com.lansosdk.videoeditor.MediaInfo;
import com.lansosdk.videoeditor.VideoEditor;

/**
 * 
 * 注意: 此代码仅作为视频处理的演示使用, 不属于sdk的一部分. 
 * 
 */
public class DemoFunctions {
	
	public final static String TAG="DemoFunctions";
	/**
	 * 演示音频和视频合成, 也可以认为是音频替换.
	 * 
	 * 音视频合成:\n把一个纯音频文件和纯视频文件合并成一个mp4格式的多媒体文件, 如果源视频之前有音频,会首先删除音频部分. \n\n
	 */
	public static String demoAVMerge(Context ctx, VideoEditor editor, String srcVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			String audio= CopyFileFromAssets.copyAssets(ctx,"aac20s.aac");  //举例;
//			 return LanSongMergeAV.mergeAVDirectly(audio,srcVideo,false);  //也可以这样;但为了显示进度,采用下面形式

	  		if(info.isHaveAudio()){
				String video2=editor.executeGetVideoTrack(srcVideo);  //拿到视频轨道
				String ret=editor.executeVideoMergeAudio(video2,audio);  //和音频合并;
				LanSongFileUtil.deleteFile(video2);
				MediaInfo.checkFile(ret);
				return  ret;
	  		}else{
				return editor.executeVideoMergeAudio(srcVideo,audio);  //和音频合并;
			}
		}
		return null;
	}
	/**
	 * 演示 视频截取
	 * 
	 * 视频剪切:\n剪切视频的长度,可以指定开始位置,指定结束位置.\n这里演示截取视频的前20秒或时长的一半,形成新的视频文件.
	 */
	public static String demoVideoCut(VideoEditor editor, String srcVideo)
	{
			MediaInfo info=new MediaInfo(srcVideo);
	    	if(info.prepare())
	    	{
	    		if(info.vDuration>20)
	    		 	return	editor.executeCutVideo(srcVideo,0,20);
				else
					return	editor.executeCutVideo(srcVideo,0,info.vDuration/2);
	    	}
	    	return null;
	}
	/**
	 * 演示音频截取
	 * 
	 * 音频剪切:\n剪切音频的长度,可以指定开始位置,指定结束位置.\n这里演示截取音频的前20秒或时长的一半,形成新的音频文件.
	 */
	public static String demoAudioCut(Context ctx, VideoEditor editor)
	{
			String srcAudio= CopyFileFromAssets.copyAssets(ctx,"honor30s2.m4a");
			MediaInfo info=new MediaInfo(srcAudio);
	    	if(info.prepare() && info.aCodecName!=null)
	    	{
	    		if(info.aDuration>15)
	    			return 	editor.executeCutAudio(srcAudio,0,15);
				else
					return 	editor.executeCutAudio(srcAudio,0,info.aDuration/2);
	    	}else{
	    		return null;
	    	}
	}
	/**
	 * 视频拼接 , 
	 * 
	 * 为了方便演示,需要您的视频大于20秒(或用默认视频).先把原视频从(0到1/3处)和(2/3到结束)截取成两段,这样就有了两个独立的视频, 然后把这两段拼接在一起,来演示视频的拼接, 
	 * 您实际可任意的组合,注意尽量视频的宽高比等参数一致,不然合成是可以,但有些播放器无法播放.
	 */
	public static String demoVideoConcat(VideoEditor editor, String srcVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
    	if(info.prepare() && info.isHaveVideo())
    	{
    		String seg1=editor.executeCutVideo(srcVideo, 0,info.vDuration/3);
    		String seg2=editor.executeCutVideo(srcVideo, info.vDuration*2/3,info.vDuration);
    		
    		
			String ret=editor.executeConcatMP4(new String[]{seg1,seg2});

    		 LanSongFileUtil.deleteFile(seg2);
    		 LanSongFileUtil.deleteFile(seg1);
    		 return  ret;
    	}
    	return null;
	}
	/**
	 *  演示视频压缩, 硬件实现 
	 *  
	 *  视频压缩转码:\n调整视频的码率,让视频文件大小变小一些,方便传输.注意:如调整的小太多,则会导致画面下降.这里演示把码率)降低为70%\n
	 */
	public static String demoVideoCompress(VideoEditor editor, String srcVideo)
	{
		return editor.executeVideoCompress(srcVideo, 0.7f);
	}
	/**
	 * 演示视频画面裁剪
	 * 
	 * 视频画面裁剪:裁剪视频的宽度和高度\n 这里演示从左上角裁剪视频高度和宽度为原来的一半保存为新的视频.\n
	 */
	public static String demoFrameCrop(VideoEditor editor, String srcVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
	    	int w= VideoEditor.make16Closest(info.getWidth()/2);
	    	int h= VideoEditor.make16Closest(info.getHeight()/2);
    		return editor.executeCropVideoFrame(srcVideo, w,h, 0, 0);
		}else{
			return null;
		}
	}
	/**
	 * 视频画面缩放[软件缩放], 
	 * 
	 * 视频缩放:缩小视频的宽度和高度\n 这里演示把宽度和高度都缩小一半.\n 注意:这里是采用软缩放的形式来做,流程是:硬解码-->软件缩放-->硬编码
	 * 
	 */
	public static String  demoVideoScale(VideoEditor editor, String srcVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			int width= VideoEditor.make16Closest(info.getWidth()/2);
			int height= VideoEditor.make16Closest(info.getHeight()/2);
			return editor.executeScaleVideoFrame(srcVideo, width,height);
		}else{
			return null;
		}
	}
	/**
	 *  演示 叠加图片(水印)
	 *  
	 *  视频上增加图片:
	 */
	public static String demoAddPicture(Context ctx, VideoEditor editor, String srcVideo)
	{
			MediaInfo info=new MediaInfo(srcVideo);
			if(info.prepare())
			{
				String imagePath= CopyFileFromAssets.copyAssets(ctx, "watermark.png");
				return editor.executeOverLayVideoFrame(srcVideo, imagePath, 0, 0);
			}else{
				return null;
			}
	}
	/**
	 * 演示获得图片,保存到/sdcard/lansongBox下面.
	 * 我们的高级版本有ExtractVideoFrameDemoActivity, 更快速获取所有视频帧, 并返回bitmap类型的数据.
	 * 
	 * 视频提取图片:\n把视频中的画面转换为图片, 可以全部指定图片,也可以每秒钟提取一帧,可以设置开始和结束时间
	 */
	public static int demoGetAllFrames(VideoEditor editor, String srcVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
    	if(info.prepare())
    	{
    		return editor.executeGetAllFrames(srcVideo,info.vCodecName, "img");
    	}else{
    		return -1;
    	}
	}
	/**
	 *  演示把一张图片转换为视频
	 */
	public static String demoOnePicture2Video(Context ctx, VideoEditor editor)
	{

		String imagePath= CopyFileFromAssets.copyAssets(ctx, "threeword.png");
		return editor.executePicture2Video(imagePath,5);
	}

	
	/**
	 * 演示 先剪切, 再画面裁剪,再图片叠加. 
	 */
	public static String demoVideoCropOverlay(Context ctx, VideoEditor editor, String srcVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			String imagePath= CopyFileFromAssets.copyAssets(ctx, "ic_launcher.png");
			 int cropW=240;
	    	 int cropH=240;
	    	return  editor.executeCropOverlay(srcVideo, imagePath, 20, 20, cropW, cropH, 0, 0);
		}else{
			return null;
		}
	}
	/**
	 * 两个音频在混合时,第二个延时一定时间.
	 */
	public static String demoAudioDelayMix(Context ctx, VideoEditor editor)
	{
		String audiostr1= CopyFileFromAssets.copyAssets(ctx,"aac20s.aac");
		String audiostr2= CopyFileFromAssets.copyAssets(ctx,"honor30s2.m4a");
		
		return editor.executeAudioDelayMix(audiostr1, audiostr2, 3000, 3000);
	}
	/**
	 * 两个音频在混合时调整音量
	 */
	public static String demoAudioVolumeMix(Context ctx, VideoEditor editor)
	{
		String audiostr1= CopyFileFromAssets.copyAssets(ctx,"aac20s.aac");
		String audiostr2= CopyFileFromAssets.copyAssets(ctx,"honor30s2.m4a");
		
		return editor.executeAudioVolumeMix(audiostr1,audiostr2, 0.5f, 4);
	}
	/**
	 * 视频增加边框,
	 * 如果视频的宽高是16的倍数, 则扩16个字节,
	 * 如果不是16倍数,则填充为16的倍数;
	 */
	public static String demoPaddingVideo(VideoEditor editor, String srcVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			//先把数据 16字节对齐;
			int width= VideoEditor.make16Next(info.getWidth());
			int height= VideoEditor.make16Next(info.getHeight());



			if(width==info.getWidth() && height== info.getHeight()){
				return editor.executePadVideo(srcVideo, width+16, height+16, 8, 8);
			}else{  //不是16的倍数
				int diffW=width - info.getWidth();
				int diffH=height- info.getHeight();
				if(diffW<0) diffW=0;
				if(diffH<0)diffH=0;
				return  editor.executePadVideo(srcVideo,width,height,diffW/2,diffH/2);
			}
		}else{
			return null;
		}
	}
	/**
	 * 获取一张图片
	 */
	public static String demoGetOneFrame(VideoEditor editor, String srcVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			return editor.executeGetOneFrame(srcVideo,info.vDuration/2);
		}else{
			return null;
		}
	}
}
