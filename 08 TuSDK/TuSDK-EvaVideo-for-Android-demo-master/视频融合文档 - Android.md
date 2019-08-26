# 视频融合文档 - Android

## 库资源与依赖

###Jar包

* TuSdkCore.jar
* TuSdkEva.jar

###SO库

* libtusdk-library.so
* libtusdk-eva.so

### 资源依赖

* TuSDK.bundle文件 (权限认证在这里，需要开通对应的[权限]([https://tutucloud.com](https://tutucloud.com/))，导出拥有使用权限的包)



 将jar包与jni包导入到项目中，TuSDK.bundle文件放入项目中的`asset`文件夹内



## 初始化

* Application

  `Application`继承`TuSdkApplication` 在`onCreate()`方法内

  ```java
     // 设置输出状态，建议在接入阶段开启该选项，以便定位问题。
      this.setEnableLog(true);
   	TuSdkEva.register();
     /**
     *  初始化SDK，应用密钥是您的应用在 TuSDK 的唯一标识符。每个应用的包名(Bundle Identifier)、密钥、资源包(滤镜、贴纸等)三者需要匹配，否则将会报错。
      *
      *  @param appkey 应用秘钥 (请前往 http://tusdk.com 申请秘钥)
      */
      TuSdk.setResourcePackageClazz(org.lsque.tusdkevademo.R.class);
      this.initPreLoader(this.getApplicationContext(), "您申请的APPKEY");
  ```

### 使用

#### 模板加载与播放

#####加载

1. 资源的加载可以初始化播放器，设置资源的路径

```java
//初始化播放器 
mEvaPlayer = new TuSdkEvaPlayerImpl();
/*
 设置资源的路径
 如果在asset下，则直接设置文件名 假如我们有个资源文件夹为 tueva 则设置RES 为tueva 
 如果在SDK内  则设置绝对路径,例如 /storage/emulated/0/tueva
 */
mEvaPlayer.setResource(RES);

```

2. 设置显示的视图

```java
//SelesView是eva播放所依赖的视图
mEvaPlayer.setDisplayView(selesView)

```

`SelesView`可以在xml中进行使用

```xml
    <org.lasque.tusdk.core.seles.output.SelesView
        android:id="@+id/selesView"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </org.lasque.tusdk.core.seles.output.SelesView>
```

3.调用加载的方法

```java
//加载模板
mEvaPlayer.load();
```

##### 播放

关于播放器控制，有以下几个方法

```java
  /** 播放 **/
  void play();

  /**
  * 跳转到指定的进度
  *
  * @param progress 0f~1f
  **/
  void seek(float progress);

  /** 暂停 **/
  void pause();

  /** 销毁 **/
  void release();


```



播放器进度回调

```java
/****************    进度监听    ****************/
 public interface TuSdkEvaPlayerProgressListener {
     /**
      * 播放回调
      *
      * @param progress      播放进度 (0 ~ 1)
      * @param currentTimeNN 当前播放的时间 (纳秒)
      * @param durationNN    总时间 (纳秒)
      */
     void onProgress(float progress, long currentTimeNN, long durationNN);
 }
```

#### 资源替换（重点）

##### 可替换的资源

1. 文字资源
2. 图片资源
3. 视频资源 
4. 音频资源

具体的资源类型可以通过每个`Entity`的`getAssetType()`来获取,目前的枚举有

```java
  public enum TuSdkEvaAssetType {
        //只替换图片
        EvaOnlyImage,

        //只替换视频
        EvaOnlyVideo,

        //图片和视频
        EvaVideoImage,

        //文字
        EvaText,

        //音频
        EvaAudio;

}
```





可以通过`TuSdkEvaAssetManager`来获取，当我们初始化播放器的时候，就默认持有了一个资源管理器,我们可以通过`TuSdkEvaPlayer.getAssetManager()`来获取资源管理器的实例，通过资源管理器，我们可以获取到可替换的资源队列

```java
     /**
     * 获取图片资源替换列表
     *
     * @return 可替换的图片列表
     **/
    public TuSdkEvaEntityQueue<TuSdkEvaImageEntity> getReplaceImageList();

    /**
     * 获取视频资源替换列表
     *
     * @return 可替换的视频列表
     **/
    public TuSdkEvaEntityQueue<TuSdkEvaVideoEntity> getReplaceVideoList();

    /**
     * 获取可替换的文字列表
     *
     * @return 可替换的文字列表
     **/
    public TuSdkEvaEntityQueue<TuSdkEvaTextEntity> getReplaceTextList();

    /**
     * 获取可替换的音频列表
     *
     * @return 可替换的音频列表
     **/
    public TuSdkEvaEntityQueue<TuSdkEvaAudioEntity> getReplaceAudioList();



```



##### 替换图片

我们通过`getReplaceImageList()`获取到可替换文件的队列，可以选则队列中的一个`TuSdkEvaImageEntity`实例

```java
/** 设置图片裁剪区间 (可不设置) **/
mImageEntity.setCropRectF(new RectF(0f,0f, 1f, 1f));
/** 设置替换图片的路径 (如果在asset文件则直接输入在asset中的路径) **/
mImageEntity.setReplaceImagePath(imagePath)
```

##### 替换视频/图片

同替换图片一样，需要用`getReplaceVideoList()` 获取需要替换的`TuSdkEvaVideoEntity`实例

```java
//如果是EvaOnlyVideo类型，那么只可以替换为视频资源
mVideoEntity.setVideoPath(videoPath);
//如果是EvaVideoImage类型，那么可以同时替换为图片资源，
mVideoEntity.setImagePath(videoPath);
```

#####替换音频

同以上一样，需要用`getReplaceAudioList()`获取需要替换的`TuSdkEvaAudioEntity`实例

```java
//替换音频资源
mAudioEntity.setAudioPath(audioPath);
```

#####替换文字

同以上一样，需要用`getReplaceTextList()`获取需要替换的`TuSdkEvaTextEntity`实例

```java
//替换文字
mAudioEntity.setReplaceText(text)
```



#### 视频导出

替换资源，预览没问题后，如果想要导出为视频，可以用`TuSdkEvaSaver`这个API去实现

* 保存器创建

```java
  TuSdkEvaSaver saver = new TuSdkEvaSaverImpl();
```

* 设置视频的输出格式

```java
 saver.setOutputVideoFormat(videoFormat);
```

* 设置音频的输出格式

```java
 saver.setOutputAudioFormat(audioFormat);
```

* 设置资源管理器

```java
  saver.setAssetManager(assetManager);
```

* 保存

```java
 saver.run(TuSdkMediaProgress)
```



* 进度监听

```java
/** 媒体处理进度接口 */
public interface TuSdkMediaProgress {
    /**
     * 执行进度 [主线程]
     *
     * @param progress        进度百分比 0-1
     * @param mediaDataSource 当前处理的视频媒体源
     * @param index           当前处理的视频索引
     * @param total           总共需要处理的文件数
     */
    void onProgress(float progress, TuSdkMediaDataSource mediaDataSource, int index, int total);

    /***
     * 完成转码 [主线程]
     * @param e 如果成功则为Null
     * @param outputFile 输出文件路径
     * @param total 处理文件总数
     */
    void onCompleted(@Nullable Exception e, TuSdkMediaDataSource outputFile, int total);
}
```

### 其他

- 使用前需要先获取权限，拿到与bundle ID 一致的授权Key和TuSDK.bundle资源
- Demo中的资源包不可用于商用，有版权约束
- 其他更详细的API请看SDK中接口文件的API