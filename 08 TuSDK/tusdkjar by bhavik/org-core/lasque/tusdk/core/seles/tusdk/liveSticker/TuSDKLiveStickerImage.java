package org.lasque.tusdk.core.seles.tusdk.liveSticker;

import android.graphics.Bitmap;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.sticker.LiveStickerLoader;
import org.lasque.tusdk.core.sticker.StickerPositionInfo;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData.StickerType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;

public class TuSDKLiveStickerImage
{
  private LiveStickerLoader a;
  private boolean b;
  private int c;
  private int d;
  private int e;
  private boolean f = true;
  private long g;
  private StickerData h;
  private boolean i;
  private boolean j;
  private final List<TuSDKStickerAnimationItem> k = new ArrayList();
  
  public TuSDKLiveStickerImage(LiveStickerLoader paramLiveStickerLoader)
  {
    this.a = paramLiveStickerLoader;
  }
  
  public boolean isActived()
  {
    return this.i;
  }
  
  public boolean isEnabled()
  {
    return this.j;
  }
  
  public void updateSticker(StickerData paramStickerData)
  {
    if (paramStickerData.getType() != StickerData.StickerType.TypeDynamic) {
      return;
    }
    StickerPositionInfo localStickerPositionInfo = paramStickerData.positionInfo;
    this.h = paramStickerData;
    this.e = (localStickerPositionInfo != null ? localStickerPositionInfo.frameInterval : 0);
    if (this.e <= 0) {
      this.e = 100;
    }
    reset();
    this.i = true;
    c();
  }
  
  public StickerData getSticker()
  {
    return this.h;
  }
  
  public void removeSticker()
  {
    if (this.h != null)
    {
      Bitmap localBitmap = this.h.getImage();
      this.h.setImage(null);
      if ((localBitmap != null) && (!localBitmap.isRecycled())) {
        localBitmap.recycle();
      }
    }
    this.h = null;
    reset();
  }
  
  public int getCurrentTextureID()
  {
    if (!this.i) {
      return 0;
    }
    if (this.f) {
      this.d = a(System.currentTimeMillis());
    }
    if (this.d >= this.k.size()) {
      return 0;
    }
    return ((TuSDKStickerAnimationItem)this.k.get(this.d)).textureID;
  }
  
  public TuSdkSize getTextureSize()
  {
    if ((!this.i) || (this.k == null) || (this.k.size() == 0)) {
      return null;
    }
    return ((TuSDKStickerAnimationItem)this.k.get(0)).imageSize;
  }
  
  public void setCurrentFrameIndex(int paramInt)
  {
    if (paramInt < 0) {
      return;
    }
    this.d = paramInt;
  }
  
  public int getCurrentFrameIndex()
  {
    return this.d;
  }
  
  public void seekStickerToFrameTime(long paramLong)
  {
    if (paramLong < 0L) {
      return;
    }
    this.d = a(paramLong);
  }
  
  public void setBenchmarkTime(long paramLong)
  {
    this.g = paramLong;
  }
  
  public void setEnableAutoplayMode(boolean paramBoolean)
  {
    if (paramBoolean == this.f) {
      return;
    }
    this.f = paramBoolean;
    b();
  }
  
  public void reset()
  {
    b();
    this.c = 0;
    if (this.k.size() > 0)
    {
      this.j = false;
      ArrayList localArrayList = new ArrayList(this.k);
      this.k.clear();
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        TuSDKStickerAnimationItem localTuSDKStickerAnimationItem = (TuSDKStickerAnimationItem)localIterator.next();
        localTuSDKStickerAnimationItem.destory();
      }
    }
    if (!this.b) {
      this.i = false;
    }
    this.b = false;
  }
  
  protected void finalize()
  {
    reset();
    super.finalize();
  }
  
  private void a()
  {
    if ((!this.f) || (this.d > 0) || (this.g > 0L)) {
      return;
    }
    this.d = 0;
    setBenchmarkTime(System.currentTimeMillis());
  }
  
  private void b()
  {
    setBenchmarkTime(0L);
    this.d = 0;
  }
  
  private int a(long paramLong)
  {
    if ((paramLong < 0L) || (this.g <= 0L) || (paramLong < this.g) || (this.e == 0) || (this.k.size() == 0)) {
      return 0;
    }
    float f1 = (float)(paramLong - this.g);
    float f2 = this.e;
    int m = (int)Math.floor(f1 / f2);
    if (m > this.k.size() - 1) {
      if ((this.h.positionInfo.loopStartIndex > 0) && (this.h.positionInfo.loopStartIndex < this.k.size()))
      {
        int n = this.h.positionInfo.loopStartIndex;
        m = (m - n) % (this.k.size() - n) + n;
      }
      else
      {
        m %= this.k.size();
      }
    }
    return m;
  }
  
  private void c()
  {
    this.b = true;
    StickerPositionInfo localStickerPositionInfo = this.h.positionInfo;
    if ((localStickerPositionInfo != null) && (localStickerPositionInfo.hasAnimationSupported())) {
      nextTextureLoadTask();
    } else {
      a(this.h.stickerImageName);
    }
  }
  
  protected void nextTextureLoadTask()
  {
    StickerPositionInfo localStickerPositionInfo = this.h.positionInfo;
    String str = (String)localStickerPositionInfo.resourceList.get(this.c);
    a(str);
  }
  
  private void a(final String paramString)
  {
    if (this.a == null) {
      return;
    }
    this.a.loadImage(new Runnable()
    {
      public void run()
      {
        final Bitmap localBitmap;
        if (TuSDKLiveStickerImage.a(TuSDKLiveStickerImage.this).getImage() != null)
        {
          localBitmap = TuSDKLiveStickerImage.a(TuSDKLiveStickerImage.this).getImage();
        }
        else if (paramString.toLowerCase().endsWith(".png"))
        {
          StickerGroup localStickerGroup = StickerLocalPackage.shared().getStickerGroup(TuSDKLiveStickerImage.a(TuSDKLiveStickerImage.this).groupId);
          String str = TuSdk.getAppTempPath() + File.separator + localStickerGroup.file.substring(0, localStickerGroup.file.lastIndexOf("."));
          str = str + File.separator + TuSDKLiveStickerImage.a(TuSDKLiveStickerImage.this).stickerId + File.separator + paramString;
          localBitmap = BitmapHelper.getBitmap(new File(str));
        }
        else
        {
          localBitmap = StickerLocalPackage.shared().loadSmartStickerItem(TuSDKLiveStickerImage.a(TuSDKLiveStickerImage.this), paramString);
        }
        TuSDKLiveStickerImage.this.runOnGLContext(new Runnable()
        {
          public void run()
          {
            TuSDKLiveStickerImage.a(TuSDKLiveStickerImage.this, localBitmap);
          }
        });
      }
    });
  }
  
  private void a(SelesFramebuffer paramSelesFramebuffer)
  {
    if (!this.b)
    {
      paramSelesFramebuffer.destroy();
      this.i = false;
      return;
    }
    this.k.add(new TuSDKStickerAnimationItem(paramSelesFramebuffer));
    StickerPositionInfo localStickerPositionInfo = this.h.positionInfo;
    if ((localStickerPositionInfo != null) && (localStickerPositionInfo.hasAnimationSupported()))
    {
      this.c += 1;
      int m = localStickerPositionInfo.resourceList.size();
      if (this.c >= m)
      {
        d();
      }
      else
      {
        int n = Math.min(5, m);
        if ((!this.j) && (this.c > n))
        {
          this.j = true;
          a();
        }
        nextTextureLoadTask();
      }
    }
    else
    {
      d();
    }
  }
  
  private void d()
  {
    this.j = true;
    this.b = false;
    this.c = 0;
    StickerPositionInfo localStickerPositionInfo = this.h.positionInfo;
    if ((localStickerPositionInfo != null) && (localStickerPositionInfo.hasAnimationSupported())) {
      a();
    }
  }
  
  private void a(Bitmap paramBitmap)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled())) {
      return;
    }
    boolean bool = false;
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramBitmap);
    if (localTuSdkSize.minSide() <= 0)
    {
      TLog.e("Passed image must not be empty - it should be at least 1px tall and wide", new Object[0]);
      return;
    }
    if (bool) {
      localTuSdkSize = SelesContext.sizeThatFitsWithinATexture(localTuSdkSize.copy());
    }
    final SelesFramebuffer localSelesFramebuffer = SelesContext.sharedFramebufferCache().fetchTexture(localTuSdkSize, false);
    localSelesFramebuffer.bindTexture(paramBitmap, bool, true);
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSDKLiveStickerImage.a(TuSDKLiveStickerImage.this, localSelesFramebuffer);
      }
    });
  }
  
  protected void runOnGLContext(Runnable paramRunnable)
  {
    if ((paramRunnable == null) || (this.a == null)) {
      return;
    }
    this.a.uploadTexture(paramRunnable);
  }
  
  public final class TuSDKStickerAnimationItem
  {
    public TuSdkSize imageSize;
    public int textureID;
    private SelesFramebuffer b;
    
    public TuSDKStickerAnimationItem(SelesFramebuffer paramSelesFramebuffer)
    {
      this.textureID = paramSelesFramebuffer.getTexture();
      this.imageSize = paramSelesFramebuffer.getSize();
      this.b = paramSelesFramebuffer;
    }
    
    public void destory()
    {
      if (this.b != null)
      {
        this.b.destroy();
        this.b = null;
      }
    }
    
    protected void finalize()
    {
      destory();
      super.finalize();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\liveSticker\TuSDKLiveStickerImage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */