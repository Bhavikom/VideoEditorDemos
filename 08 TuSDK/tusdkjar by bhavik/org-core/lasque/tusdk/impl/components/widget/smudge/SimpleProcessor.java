package org.lasque.tusdk.impl.components.widget.smudge;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData.BrushType;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize.SizeType;

public class SimpleProcessor
{
  protected Bitmap originalImage;
  protected Bitmap originalSnap;
  protected Bitmap currentSnap;
  protected Canvas smudgeCanvas;
  protected Bitmap brushSnap;
  protected BrushData mCurrentBrush;
  protected float mBrushScale;
  private int a;
  private boolean b = false;
  private PointF c = new PointF(0.0F, 0.0F);
  private List<SmudgeLog> d;
  private List<SmudgeLog> e;
  private int f = 5;
  private Handler g = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      super.handleMessage(paramAnonymousMessage);
      if ((paramAnonymousMessage.what == 1) && (paramAnonymousMessage.obj != null))
      {
        String str = (String)paramAnonymousMessage.obj;
        SimpleProcessor.a(SimpleProcessor.this, str);
      }
    }
  };
  private ExecutorService h;
  private boolean i = false;
  private boolean j = true;
  
  protected void init(Bitmap paramBitmap1, Bitmap paramBitmap2, int paramInt)
  {
    this.originalImage = paramBitmap1;
    this.originalSnap = paramBitmap2;
    int k = paramBitmap2.getWidth();
    int m = paramBitmap2.getHeight();
    this.currentSnap = Bitmap.createBitmap(k, m, Bitmap.Config.ARGB_8888);
    this.smudgeCanvas = new Canvas(this.currentSnap);
    this.a = paramInt;
  }
  
  protected int getImageWidth()
  {
    if (this.originalSnap != null) {
      return this.originalSnap.getWidth();
    }
    return 0;
  }
  
  protected int getImageHeight()
  {
    if (this.originalSnap != null) {
      return this.originalSnap.getHeight();
    }
    return 0;
  }
  
  protected void setBrush(BrushData paramBrushData)
  {
    if (paramBrushData == null) {
      return;
    }
    this.mCurrentBrush = paramBrushData;
    BitmapHelper.recycled(this.brushSnap);
    if (this.mCurrentBrush.getType() != BrushData.BrushType.TypeMosaic) {
      this.brushSnap = this.mCurrentBrush.getImage();
    } else {
      this.brushSnap = this.mCurrentBrush.getImage().copy(Bitmap.Config.ARGB_8888, true);
    }
  }
  
  protected BrushData getBrush()
  {
    return this.mCurrentBrush;
  }
  
  protected void setBrushSize(BrushSize.SizeType paramSizeType)
  {
    if (paramSizeType == null) {
      return;
    }
    double d1 = BrushSize.getBrushValue(paramSizeType);
    this.mBrushScale = ((int)(d1 * this.a));
  }
  
  protected int getMaxUndoCount()
  {
    return this.f;
  }
  
  protected void setMaxUndoCount(int paramInt)
  {
    this.f = paramInt;
  }
  
  protected int getRedoCount()
  {
    if (this.e != null) {
      return this.e.size();
    }
    return 0;
  }
  
  protected int getUndoCount()
  {
    if (this.d != null) {
      return this.d.size();
    }
    return 0;
  }
  
  protected void saveCurrentAsHistory()
  {
    if (this.currentSnap == null) {
      return;
    }
    Bitmap localBitmap = this.currentSnap.copy(Bitmap.Config.ARGB_8888, true);
    if (this.d == null) {
      this.d = new ArrayList();
    }
    SmudgeLog localSmudgeLog = new SmudgeLog(localBitmap);
    this.d.add(localSmudgeLog);
    b();
    a();
  }
  
  private void a()
  {
    if ((!this.j) || (this.i)) {
      return;
    }
    SmudgeLog localSmudgeLog1 = null;
    for (int k = 0; k < this.d.size(); k++) {
      if (!((SmudgeLog)this.d.get(k)).hasCached())
      {
        localSmudgeLog1 = (SmudgeLog)this.d.get(k);
        break;
      }
    }
    if (localSmudgeLog1 == null) {
      return;
    }
    this.i = true;
    final SmudgeLog localSmudgeLog2 = localSmudgeLog1;
    if (this.h == null) {
      this.h = Executors.newFixedThreadPool(1);
    }
    this.h.execute(new Runnable()
    {
      public void run()
      {
        SimpleProcessor.a(SimpleProcessor.this, localSmudgeLog2);
      }
    });
  }
  
  private void a(SmudgeLog paramSmudgeLog)
  {
    File localFile = new File(TuSdk.getAppTempPath(), paramSmudgeLog.getFileName());
    BitmapHelper.saveBitmapAsPNG(localFile, paramSmudgeLog.getBitmap(), 100);
    Message localMessage = new Message();
    localMessage.what = 1;
    localMessage.obj = paramSmudgeLog.getName();
    this.g.sendMessage(localMessage);
  }
  
  private void a(String paramString)
  {
    this.i = false;
    if ((this.d != null) && (this.d.size() > 0))
    {
      Iterator localIterator = this.d.iterator();
      while (localIterator.hasNext())
      {
        SmudgeLog localSmudgeLog = (SmudgeLog)localIterator.next();
        if (localSmudgeLog.getName().equals(paramString))
        {
          localSmudgeLog.markAsCached();
          break;
        }
      }
      a();
    }
  }
  
  private void b()
  {
    if (this.d.size() > getMaxUndoCount())
    {
      SmudgeLog localSmudgeLog = (SmudgeLog)this.d.get(0);
      this.d.remove(0);
      localSmudgeLog.destroy();
    }
    a(this.e);
  }
  
  protected Bitmap getRedoData()
  {
    if ((this.e == null) || (this.e.size() == 0)) {
      return null;
    }
    int k = this.e.size() - 1;
    SmudgeLog localSmudgeLog = (SmudgeLog)this.e.get(k);
    Bitmap localBitmap = localSmudgeLog.getBitmap();
    this.e.remove(k);
    BitmapHelper.recycled(this.currentSnap);
    this.currentSnap = localBitmap.copy(Bitmap.Config.ARGB_8888, true);
    c();
    this.d.add(localSmudgeLog);
    return getCanvasImage();
  }
  
  protected Bitmap getUndoData()
  {
    if ((this.d == null) || (this.d.size() <= 0)) {
      return null;
    }
    int k = this.d.size() - 1;
    SmudgeLog localSmudgeLog = (SmudgeLog)this.d.get(k);
    this.d.remove(k);
    Bitmap localBitmap = null;
    if (this.d.size() == 0) {
      localBitmap = Bitmap.createBitmap(getImageWidth(), getImageHeight(), Bitmap.Config.ARGB_8888);
    } else {
      localBitmap = ((SmudgeLog)this.d.get(this.d.size() - 1)).getBitmap();
    }
    this.currentSnap = localBitmap.copy(Bitmap.Config.ARGB_8888, true);
    c();
    if (this.e == null) {
      this.e = new ArrayList();
    }
    this.e.add(localSmudgeLog);
    return getCanvasImage();
  }
  
  private void c()
  {
    this.smudgeCanvas.setBitmap(this.currentSnap);
  }
  
  protected Bitmap getCanvasImage()
  {
    return this.currentSnap;
  }
  
  protected Bitmap getOriginalImage()
  {
    return this.originalSnap;
  }
  
  protected void touchBegan()
  {
    this.b = false;
  }
  
  protected final void drawBetweenPoints(PointF paramPointF1, PointF paramPointF2)
  {
    float f1 = paramPointF2.x - paramPointF1.x;
    float f2 = paramPointF2.y - paramPointF1.y;
    float f3 = (float)Math.pow(f1 * f1 + f2 * f2, 0.5D);
    float f4;
    float f5;
    if (this.b)
    {
      f4 = this.c.x;
      f5 = this.c.y;
    }
    else
    {
      f4 = paramPointF1.x;
      f5 = paramPointF1.y;
      this.b = true;
    }
    float f6 = this.mBrushScale / this.brushSnap.getWidth();
    BrushData.BrushType localBrushType = this.mCurrentBrush.getType();
    if ((localBrushType == BrushData.BrushType.TypeMosaic) || (localBrushType == BrushData.BrushType.TypeEraser)) {
      f6 = (float)(f6 * 0.5D);
    }
    float f7 = getMaxTemplateDistance(this.mBrushScale);
    int k = (int)(f3 / f7 + 1.0F);
    float f8 = paramPointF2.x;
    float f9 = paramPointF2.y;
    float f10 = paramPointF1.x;
    float f11 = paramPointF1.y;
    for (int m = 1; m <= k; m++)
    {
      float f12 = m / k;
      float f13 = f12 * (1.0F - f12);
      float f14 = f10 + (f8 - f10) * f12 + f13 * (f10 - f4);
      float f15 = f11 + (f9 - f11) * f12 + f13 * (f11 - f5);
      float f16 = (float)(Math.atan2(f14 - f10, f15 - f11) * 180.0D / 3.141592653589793D);
      f10 = f14;
      f11 = f15;
      drawAtPoint(f14, f15, f3, f6, -1.0F * f16);
    }
    this.c.x = paramPointF1.x;
    this.c.y = paramPointF1.y;
  }
  
  protected float getMaxTemplateDistance(float paramFloat)
  {
    return paramFloat * 0.8F;
  }
  
  protected void drawAtPoint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {}
  
  protected Bitmap getSmudgeImage(Bitmap paramBitmap, boolean paramBoolean)
  {
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramBitmap);
    Bitmap localBitmap = Bitmap.createBitmap(localTuSdkSize.width, localTuSdkSize.height, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, null);
    Matrix localMatrix = new Matrix();
    localMatrix.postScale(paramBitmap.getWidth() / this.currentSnap.getWidth(), paramBitmap.getHeight() / this.currentSnap.getHeight());
    localCanvas.drawBitmap(this.currentSnap, localMatrix, null);
    if (paramBoolean) {
      BitmapHelper.recycled(paramBitmap);
    }
    return localBitmap;
  }
  
  protected void destroy()
  {
    BitmapHelper.recycled(this.originalSnap);
    BitmapHelper.recycled(this.currentSnap);
    BitmapHelper.recycled(this.brushSnap);
    a(this.d);
    a(this.e);
    if (this.h != null)
    {
      this.h.shutdown();
      this.h = null;
    }
  }
  
  private void a(List<SmudgeLog> paramList)
  {
    if ((paramList == null) || (paramList.size() <= 0)) {
      return;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      SmudgeLog localSmudgeLog = (SmudgeLog)localIterator.next();
      localSmudgeLog.destroy();
    }
    paramList.clear();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\smudge\SimpleProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */