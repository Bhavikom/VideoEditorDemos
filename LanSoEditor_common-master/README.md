## 因为太多来自QQ增加好友的干扰,大量的时间被浪费, 此SDK一周后不再免费(20190723),请理解; 

### 预告
 - 从20190720开始把专业版里的录像和视频图层,图片图层下方到基本版本. 以满足大部分客户的需求. 
 - 视频的常见处理将极速. 
### 主要特点:
  -  增加 H264硬件编码器 和 H264硬件解码器,  处理速度极大提升;
  -  增加40多个常见方法,编写各种辅助处理类.
  -  SDK已运行二年左右,商用APP400+.
  -  ffmpeg版本是2.8; 

### 使用:
```
-  1, 一条命令即可完成,阻塞执行:  
	       VideoEditor editor=VideoEditor();
   举例1: 视频增加水印:
              "处理后的视频"=editor.executeOverLayVideoFrame("视频路径","增加的图片路径","x坐标","y坐标");
   举例2: 裁剪视频时长:
               dstResult=editor.executeCutVideo("视频路径","开始时间S","结束时间S");
   举例3: 画面裁剪:
               dstResult=editor.executeCropVideoFrame("视频路径","x坐标","y坐标","裁剪宽度","裁剪高度");
               
-  2, 我们封装好的功能有
		 替换背景音乐, 声音混合, 多段视频的拼接, 不同视频源的拼接, 视频画面拼接, 读取视频帧, 获取所有帧, 图片转视频, 
               倒序,加减速,镜像,音频混合,音频转码,画面填充,缩放,压缩,镜像,增加文字,视频转码,图片视频转GIF等;
               
-  3, 如果列举的功能不能满足您的需求, 您可以根据ffmpeg的命令自行扩展,代码中有说明;  
```
### 辅助代码:    
```
    1, VideoEditor其他公共方法:
    	   1. setonProgressListener();  //executeXXX 正在执行的百分比进度;
    	   2 setEncoderBitrate();  //给executeXXX 指定码率;
    	   3 VideoEditor.isForceSoftWareEncoder=true; //强制executeXXX 在编码时,采用软编码器;
    	   4 cancel(); //取消正在执行的executeXXX;
    	   5,当返回null, 我们提供了log采集, 可通过getErrorLog()得到错误信息;大部分是因为视频参数错误引起的.
    	   
    2, 写了MediaInfo辅助类, 用来很快的获取视频的基本信息,以方便你实际参数的参考, 使用如下:
            MediaInfo info=new MediaInfo("要获取视频的路径");
             if(info.prepare()){
               	; 如果返回true,得到视频宽度,高度,码率,帧率,时长,编码器,总帧数,是否有B帧,旋转角度, 音频采样率,音频通道数, 音频码率,是否有音频,视频,视频是否旋转 等参数;
             }
	     也可以先用MediaInfo判断当前视频的各种信息;
    3, 文件创建,删除类 LanSongFileUtil.java 方便你在编辑时的各种文件创建,删除, 判断等操作.
    4, 请不要混淆我们的SDK， 具体如下：
		-keep public class com.lansosdk.videoeditor.** {
			<fields>;
			<methods>;
		}。
		-keep public class com.lansosdk.videoplayer.** {
			<fields>;
			<methods>;
		}。
``` 
### 常见问题解决：
```
   方法1： 80%的问题是因为文件传输错误，可用MediaInfo.checkFile("您的文件绝对路径")； 来查看打印信息， 根据打印信息的提示，排除问题。
   方法2： 查看打印信息, 如返回 xxx pixel -1之类, 说明找不到对应的节码器; 如其他信息, 查看对应的关键字, 大部分是因为命令错误,或没有覆盖之前的文件等等;
   
```
### 集成步骤
-  直接导入lansongsdk 这个module后; 在代码开始的时候,增加SDK初始化代码:
```
	LanSoEditor.initSDK(getApplicationContext())；
	在您工程的settting.gradle中增加: ,'LanSongSDK' ; 并在app.gradle中 增加: implementation project(':LanSongSDK')
	
```
- 混淆代码
```
-keep public class com.lansosdk.videoeditor.** {
<fields>;
<methods>;
}。
-keep public class com.lansosdk.videoplayer.** {
<fields>;
<methods>;
}。
-keep class com.lansosdk.box.**{*;}
我们的jar文件， 已经在sdk提供给您的时候， 做过混淆操作，请勿再次混淆。
```


# 增值服务包括:
- com.lansosdk.videoEditor.NoFree文件夹中的功能.
- 底层硬件加速编码器;
- 极速视频压缩功能;
- 提供为期一年的 技术支持和功能定制(一年内可定制基于此版本的其它功能.) 
- 定制的功能永久使用,我们负责维护, 速度优化. 
-  费用13000元(SDK永久使用.).

# IOS基本版下载地址:
-  https://github.com/LanSoSdk/LanSongEditor_IOS_common
# 专业版架构介绍
![架构示意图](https://github.com/LanSoSdk/LanSoEditor_advance/blob/master/SDK%E6%9E%B6%E6%9E%84%E5%9B%BE%E7%89%87.png)
-  专业版SDK,图层架构,可以做各种视频特效, 并支持AE模板,微商小视频,抖音趣拍等效果: 
-  android: 
  https://github.com/LanSoSdk/LanSoEditor_advance
- ios：
    https://github.com/LanSoSdk/LanSongEditor_IOS
-   欢迎您的评估使用.

### 联系方式:
   -  网址:www.lansongtech.com
   -   网址上列出了我们专业版 的优劣,  适用人群, SDK主打功能, 图层 种类等 .  希望可以满足您的各种想法.
   
## 此SDK里举例的一些功能有：
1. 获取信息
2. 提取音频/视频
3. 合并音频视频
4. 增加背景音
5. 裁剪视频
6. 裁剪音频
7. 拼接视频
8. 拼接音频
9. 裁剪画面
10. 拼接画面
11. 提取视频图片
12. 图片转视频
13. 增加水印
14. 删除水印
15. 指定时间段增加图片
16. 增加文字
17. 缩放视频
18. 压缩视频
19. 旋转视频
20. 倒序
21. 加减速
22. 调整视频帧率
23. 镜像视频
24. 视频转码
25. 视频转Gif
26. MP4附带文字
27. 获取MP4文字
28. 延迟音频
29. M4a转mp3
30. mp3转M4A
31. 音频转单通道wav,一些语音识别的特定格式
32. 音频解码为wav
33. pcm混合
34. 辅助功能,设置处理进度监听, 取消执行, 设置码率, 强制使用软解码器, 强制硬编码器等.
        
