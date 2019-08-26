package org.lasque.tusdk.api.image;

import android.media.MediaFormat;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.lasque.tusdk.api.engine.TuSdkFilterEngine;
import org.lasque.tusdk.api.engine.TuSdkFilterEngineImpl;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFormat;
import org.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkComposeItem;
import org.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkImageComposeItem;
import org.lasque.tusdk.core.media.codec.suit.imageToVideo.TuSdkMediaVideoComposer;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoQuality;
import org.lasque.tusdk.core.media.suit.TuSdkMediaSuit;
import org.lasque.tusdk.core.seles.tusdk.TuSDKMediaTransitionWrap.TuSDKMediaTransitionType;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.AlbumHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData.TuSdkMediaEffectDataType;
import org.lasque.tusdk.video.editor.TuSdkMediaTransitionEffectData;

public class TuSDKMediaMovieCompositionComposer
{
  private static String a;
  private float b = 2.0F;
  private TuSdkMediaVideoComposer c;
  private List<ImageSqlInfo> d;
  private LinkedList<TuSdkComposeItem> e;
  private TuSdkMediaProgress f;
  private MediaFormat g;
  private MediaFormat h;
  private TuSdkFilterEngine i;
  private boolean j = false;
  private String k;
  private boolean l = true;
  private List<TuSdkMediaEffectData> m;
  
  public TuSDKMediaMovieCompositionComposer()
  {
    this(a);
  }
  
  public TuSDKMediaMovieCompositionComposer(String paramString)
  {
    a = paramString;
    this.e = new LinkedList();
    this.g = TuSdkMediaFormat.buildSafeVideoEncodecFormat(TuSdkSize.create(1080, 1920), TuSdkVideoQuality.LIVE_HIGH1, 2130708361);
    this.h = TuSdkMediaFormat.buildSafeAudioEncodecFormat(44100, 1, 96000, 2);
    this.m = new LinkedList();
  }
  
  public void setImageSource(List<ImageSqlInfo> paramList)
  {
    this.d = paramList;
  }
  
  public void setVideoFormat(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat == null) {
      return;
    }
    this.g = paramMediaFormat;
  }
  
  public void setMediaProgress(TuSdkMediaProgress paramTuSdkMediaProgress)
  {
    this.f = paramTuSdkMediaProgress;
  }
  
  public void startExport()
  {
    if (c())
    {
      if (this.c != null)
      {
        this.c.stop();
        this.c = null;
      }
      this.c = TuSdkMediaSuit.imageToVideo(a(this.d), a, this.g, this.h, this.f, this.l, a());
    }
    else
    {
      TLog.e("start composer fail", new Object[0]);
    }
  }
  
  private TuSdkSurfaceRender a()
  {
    new TuSdkSurfaceRender()
    {
      public void onSurfaceCreated()
      {
        if (TuSDKMediaMovieCompositionComposer.a(TuSDKMediaMovieCompositionComposer.this) == null)
        {
          TuSDKMediaMovieCompositionComposer.a(TuSDKMediaMovieCompositionComposer.this, new TuSdkFilterEngineImpl(false, true));
          TuSDKMediaMovieCompositionComposer.a(TuSDKMediaMovieCompositionComposer.this).onSurfaceCreated();
        }
        else
        {
          TuSDKMediaMovieCompositionComposer.a(TuSDKMediaMovieCompositionComposer.this).onSurfaceCreated();
        }
        TuSDKMediaMovieCompositionComposer.a(TuSDKMediaMovieCompositionComposer.this).removeAllMediaEffects();
        TuSDKMediaMovieCompositionComposer.b(TuSDKMediaMovieCompositionComposer.this);
      }
      
      public void onSurfaceChanged(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        TuSDKMediaMovieCompositionComposer.a(TuSDKMediaMovieCompositionComposer.this).onSurfaceChanged(paramAnonymousInt1, paramAnonymousInt2);
      }
      
      public void onSurfaceDestory() {}
      
      public int onDrawFrame(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, long paramAnonymousLong)
      {
        return TuSDKMediaMovieCompositionComposer.a(TuSDKMediaMovieCompositionComposer.this).processFrame(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3, paramAnonymousLong);
      }
      
      public void onDrawFrameCompleted() {}
    };
  }
  
  public void cancelExport()
  {
    if (this.c != null) {
      this.c.stop();
    }
  }
  
  private void b()
  {
    Iterator localIterator = this.m.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
      this.i.addMediaEffectData(localTuSdkMediaEffectData);
    }
  }
  
  public boolean addMediaEffect(TuSdkMediaTransitionEffectData paramTuSdkMediaTransitionEffectData)
  {
    if ((paramTuSdkMediaTransitionEffectData.getEffectCode().equals(TuSDKMediaTransitionWrap.TuSDKMediaTransitionType.TuSDKMediaTransitionTypePullInBottom)) || (paramTuSdkMediaTransitionEffectData.getEffectCode().equals(TuSDKMediaTransitionWrap.TuSDKMediaTransitionType.TuSDKMediaTransitionTypePullInLeft)) || (paramTuSdkMediaTransitionEffectData.getEffectCode().equals(TuSDKMediaTransitionWrap.TuSDKMediaTransitionType.TuSDKMediaTransitionTypePullInRight)) || (paramTuSdkMediaTransitionEffectData.getEffectCode().equals(TuSDKMediaTransitionWrap.TuSDKMediaTransitionType.TuSDKMediaTransitionTypePullInTop))) {
      return this.m.add(paramTuSdkMediaTransitionEffectData);
    }
    return false;
  }
  
  public void removeMediaEffect(TuSdkMediaTransitionEffectData paramTuSdkMediaTransitionEffectData)
  {
    this.m.remove(paramTuSdkMediaTransitionEffectData);
  }
  
  public void removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    Iterator localIterator = this.m.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
      if (localTuSdkMediaEffectData.getMediaEffectType().equals(paramTuSdkMediaEffectDataType)) {
        this.m.remove(localTuSdkMediaEffectData);
      }
    }
  }
  
  public List<TuSdkMediaEffectData> mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.m.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaEffectData localTuSdkMediaEffectData = (TuSdkMediaEffectData)localIterator.next();
      if (localTuSdkMediaEffectData.getMediaEffectType().equals(paramTuSdkMediaEffectDataType)) {
        localArrayList.add(localTuSdkMediaEffectData);
      }
    }
    return Collections.unmodifiableList(localArrayList);
  }
  
  public List<TuSdkMediaEffectData> getAllMediaEffects()
  {
    return Collections.unmodifiableList(this.m);
  }
  
  private boolean c()
  {
    try
    {
      if ((this.d == null) || (this.e == null) || (this.d.isEmpty())) {
        return false;
      }
      if (this.f == null) {
        return false;
      }
      if ((this.j) || (StringHelper.isBlank(a)))
      {
        if (StringHelper.isBlank(this.k)) {
          a = AlbumHelper.getAlbumVideoFile().getPath();
        } else {
          a = AlbumHelper.getAlbumVideoFile(this.k).getPath();
        }
        a = getOutputTempFilePath();
      }
      else
      {
        a = getOutputTempFilePath();
      }
    }
    catch (NullPointerException localNullPointerException)
    {
      a = getOutputTempFilePath();
    }
    return true;
  }
  
  private LinkedList<TuSdkComposeItem> a(List<ImageSqlInfo> paramList)
  {
    if ((this.e != null) && (!this.e.isEmpty())) {
      this.e.clear();
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      ImageSqlInfo localImageSqlInfo = (ImageSqlInfo)localIterator.next();
      TuSdkImageComposeItem localTuSdkImageComposeItem = new TuSdkImageComposeItem();
      localTuSdkImageComposeItem.setDuration(this.b);
      localTuSdkImageComposeItem.setImagePath(localImageSqlInfo.path);
      this.e.add(localTuSdkImageComposeItem);
    }
    return this.e;
  }
  
  public void saveToAlbum(boolean paramBoolean)
  {
    this.j = paramBoolean;
  }
  
  public boolean isSaveToAlbum()
  {
    return this.j;
  }
  
  public void setOutpuFilePath(String paramString)
  {
    a = paramString;
  }
  
  protected String getOutputTempFilePath()
  {
    return TuSdk.getAppTempPath().getPath() + "/LSQ_" + System.currentTimeMillis() + ".mp4";
  }
  
  public void setIsAllKeyFrame(boolean paramBoolean)
  {
    this.l = paramBoolean;
  }
  
  public void setDuration(float paramFloat)
  {
    this.b = paramFloat;
  }
  
  public float getDuration()
  {
    return this.b;
  }
  
  public TuSdkSize getRecommendOutputSize()
  {
    if ((this.d == null) || (this.d.isEmpty())) {
      return new TuSdkSize(0, 0);
    }
    TuSdkSize localTuSdkSize = ((ImageSqlInfo)this.d.get(0)).size;
    Iterator localIterator = this.d.iterator();
    while (localIterator.hasNext())
    {
      ImageSqlInfo localImageSqlInfo = (ImageSqlInfo)localIterator.next();
      if (localImageSqlInfo.size.minSide() < localTuSdkSize.minSide()) {
        localTuSdkSize = localImageSqlInfo.size;
      }
    }
    if (localTuSdkSize.minSide() > 1080) {
      return new TuSdkSize(localTuSdkSize.width / 2, localTuSdkSize.height / 2);
    }
    return localTuSdkSize;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\image\TuSDKMediaMovieCompositionComposer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */