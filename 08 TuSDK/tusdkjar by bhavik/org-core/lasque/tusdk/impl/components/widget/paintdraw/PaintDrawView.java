package org.lasque.tusdk.impl.components.widget.paintdraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize.SizeType;

public class PaintDrawView
  extends TuSdkRelativeLayout
{
  private PaintDrawViewDelagate a;
  private PaintDrawActionDelegate b;
  protected PaintDrawProcessor mPaintDrawProcessor;
  private ImageView c;
  private ImageView d;
  private ImageView e;
  private PointF f;
  private PointF g;
  private BrushSize.SizeType h = BrushSize.SizeType.MediumBrush;
  private float i = 3.0F;
  private int j = 0;
  private Paint.Join k = Paint.Join.ROUND;
  private Paint.Cap l = Paint.Cap.ROUND;
  private float m = 10.0F;
  private int n = 5;
  protected View.OnTouchListener mOnTouchListener = new View.OnTouchListener()
  {
    public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
    {
      int i = PaintDrawView.this.getBrushSizePixel();
      if (i <= 0) {
        return false;
      }
      if ((PaintDrawView.a(PaintDrawView.this) == null) || (PaintDrawView.this.getProcessorInstance() == null) || (paramAnonymousMotionEvent.getPointerCount() > 1)) {
        return false;
      }
      if (PaintDrawView.b(PaintDrawView.this) == null) {
        PaintDrawView.a(PaintDrawView.this, new PointF(0.0F, 0.0F));
      }
      if (PaintDrawView.c(PaintDrawView.this) != null)
      {
        PaintDrawView.b(PaintDrawView.this).x = PaintDrawView.c(PaintDrawView.this).x;
        PaintDrawView.b(PaintDrawView.this).y = PaintDrawView.c(PaintDrawView.this).y;
      }
      else
      {
        PaintDrawView.b(PaintDrawView.this, new PointF(0.0F, 0.0F));
      }
      Matrix localMatrix = new Matrix();
      PaintDrawView.a(PaintDrawView.this).getImageMatrix().invert(localMatrix);
      float[] arrayOfFloat = { paramAnonymousMotionEvent.getX(), paramAnonymousMotionEvent.getY() };
      PointF localPointF = new PointF(arrayOfFloat[0], arrayOfFloat[1]);
      localMatrix.mapPoints(arrayOfFloat);
      PaintDrawView.c(PaintDrawView.this).x = arrayOfFloat[0];
      PaintDrawView.c(PaintDrawView.this).y = arrayOfFloat[1];
      PaintDrawProcessor localPaintDrawProcessor = PaintDrawView.this.getProcessorInstance();
      switch (paramAnonymousMotionEvent.getAction())
      {
      case 0: 
        localPaintDrawProcessor.touchBegan(PaintDrawView.c(PaintDrawView.this));
        break;
      case 1: 
        localPaintDrawProcessor.saveCurrentAsHistory();
        PaintDrawView.this.sendPaintDrawActionChangeNotify();
        PaintDrawView.this.onPaintDrawEnd();
        localPaintDrawProcessor.touchEnd();
        break;
      case 2: 
        PaintDrawView.c(PaintDrawView.this, localPointF);
        localPaintDrawProcessor.pathMove(PaintDrawView.c(PaintDrawView.this));
        Bitmap localBitmap = localPaintDrawProcessor.getCanvasImage();
        PaintDrawView.d(PaintDrawView.this).setImageBitmap(localBitmap);
        PaintDrawView.this.onPaintDrawChanged(PaintDrawView.c(PaintDrawView.this), localPointF, localBitmap.getWidth(), localBitmap.getHeight());
      }
      return true;
    }
  };
  
  public PaintDrawViewDelagate getDelegate()
  {
    return this.a;
  }
  
  public void setDelegate(PaintDrawViewDelagate paramPaintDrawViewDelagate)
  {
    this.a = paramPaintDrawViewDelagate;
  }
  
  public PaintDrawActionDelegate getActionDelegate()
  {
    return this.b;
  }
  
  public void setActionDelegate(PaintDrawActionDelegate paramPaintDrawActionDelegate)
  {
    this.b = paramPaintDrawActionDelegate;
  }
  
  public PaintDrawView(Context paramContext)
  {
    super(paramContext);
  }
  
  public PaintDrawView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public PaintDrawView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected PaintDrawProcessor getProcessorInstance()
  {
    if (!SdkValid.shared.paintEnabled())
    {
      TLog.e("You are not allowed to use the paint feature, please see http://tusdk.com", new Object[0]);
      return null;
    }
    if (this.mPaintDrawProcessor == null) {
      this.mPaintDrawProcessor = new PaintDrawProcessor();
    }
    return this.mPaintDrawProcessor;
  }
  
  public float getMinDistance()
  {
    return this.m;
  }
  
  public void setMinDistance(float paramFloat)
  {
    this.m = paramFloat;
    updateBrushSettings();
  }
  
  public int getPaintColor()
  {
    return this.j;
  }
  
  public void setPaintColor(int paramInt)
  {
    this.j = paramInt;
    updateBrushSettings();
  }
  
  public float getBrushScale()
  {
    return this.i;
  }
  
  public void setBrushScale(float paramFloat)
  {
    this.i = paramFloat;
    updateBrushSettings();
  }
  
  public void setBrushSize(BrushSize.SizeType paramSizeType)
  {
    this.h = paramSizeType;
    updateBrushSettings();
  }
  
  public BrushSize.SizeType getBrushSize()
  {
    return this.h;
  }
  
  public int getMaxUndoCount()
  {
    return this.n;
  }
  
  public void setMaxUndoCount(int paramInt)
  {
    this.n = paramInt;
  }
  
  public int getBrushSizePixel()
  {
    BrushSize.SizeType localSizeType = getBrushSize();
    int i1 = 24;
    if (localSizeType == BrushSize.SizeType.MediumBrush)
    {
      i1 = 48;
    }
    else if (localSizeType == BrushSize.SizeType.LargeBrush)
    {
      i1 = 72;
    }
    else if (localSizeType == BrushSize.SizeType.CustomizeBrush)
    {
      float f1 = BrushSize.SizeType.CustomizeBrush.getCustomizeBrushValue();
      i1 = (int)(f1 * 72.0F);
    }
    return i1;
  }
  
  protected void updateBrushSettings()
  {
    if (getProcessorInstance() == null) {
      return;
    }
    getProcessorInstance().setBrushSize(this.h);
    getProcessorInstance().setPaintCap(this.l);
    getProcessorInstance().setPaintJoin(this.k);
    getProcessorInstance().setPaintColor(this.j);
    getProcessorInstance().setBrushScale(this.i);
    getProcessorInstance().setMinDistance(this.m);
  }
  
  public void loadView()
  {
    super.loadView();
    this.c = new ImageView(getContext());
    addView(this.c, new RecyclerView.LayoutParams(-1, -1));
    this.d = new ImageView(getContext());
    this.d.setOnTouchListener(this.mOnTouchListener);
    addView(this.d, new RecyclerView.LayoutParams(-1, -1));
    this.e = new ImageView(getContext());
    this.e.setVisibility(4);
    this.e.setBackgroundColor(16777215);
    addView(this.e, new RecyclerView.LayoutParams(24, 24));
    a();
  }
  
  private void a()
  {
    int i1 = getBrushSizePixel();
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)this.e.getLayoutParams();
    localLayoutParams.width = i1;
    localLayoutParams.height = i1;
    localLayoutParams.leftMargin = 0;
    localLayoutParams.topMargin = 0;
    this.e.setLayoutParams(localLayoutParams);
    if (i1 > 0) {
      this.e.setImageBitmap(BitmapHelper.createOvalImage(i1, i1, -1));
    }
  }
  
  protected void sendPaintDrawActionChangeNotify()
  {
    if (getDelegate() != null) {
      getDelegate().onRefreshStepStatesWithHistories(getUndoCount(), getRedoCount());
    }
  }
  
  protected int getUndoCount()
  {
    if (getProcessorInstance() != null) {
      return getProcessorInstance().getUndoCount();
    }
    return 0;
  }
  
  protected int getRedoCount()
  {
    if (getProcessorInstance() != null) {
      return getProcessorInstance().getRedoCount();
    }
    return 0;
  }
  
  protected void onPaintDrawEnd()
  {
    if (getActionDelegate() != null) {
      getActionDelegate().onPaintDrawEnd();
    }
    this.e.setVisibility(4);
  }
  
  private void a(PointF paramPointF)
  {
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)this.e.getLayoutParams();
    localLayoutParams.setMargins((int)paramPointF.x - localLayoutParams.width / 2, (int)paramPointF.y - localLayoutParams.height / 2, 0, 0);
    this.e.setLayoutParams(localLayoutParams);
    this.e.setVisibility(0);
  }
  
  protected void onPaintDrawChanged(PointF paramPointF1, PointF paramPointF2, int paramInt1, int paramInt2)
  {
    if (getActionDelegate() != null) {
      getActionDelegate().onPaintDrawChanged(paramPointF1, paramPointF2, paramInt1, paramInt2);
    }
  }
  
  public void undo()
  {
    PaintDrawProcessor localPaintDrawProcessor = getProcessorInstance();
    if (localPaintDrawProcessor == null) {
      return;
    }
    if (localPaintDrawProcessor.getUndoCount() > 0) {
      this.d.setImageBitmap(localPaintDrawProcessor.getUndoData());
    }
    sendPaintDrawActionChangeNotify();
  }
  
  public void redo()
  {
    PaintDrawProcessor localPaintDrawProcessor = getProcessorInstance();
    if (localPaintDrawProcessor == null) {
      return;
    }
    if (localPaintDrawProcessor.getRedoCount() > 0) {
      this.d.setImageBitmap(localPaintDrawProcessor.getRedoData());
    }
    sendPaintDrawActionChangeNotify();
  }
  
  public void showOriginalImage(boolean paramBoolean)
  {
    if (getProcessorInstance() != null) {
      this.d.setVisibility(paramBoolean ? 4 : 0);
    }
  }
  
  public void setImageBitmap(Bitmap paramBitmap)
  {
    if ((getClass() == PaintDrawView.class) && (!SdkValid.shared.paintEnabled()))
    {
      TLog.e("You are not allowed to use the paint feature, please see http://tusdk.com", new Object[0]);
      return;
    }
    if (paramBitmap == null) {
      return;
    }
    Bitmap localBitmap = scaleToFill(paramBitmap, getWidth(), getHeight());
    this.mPaintDrawProcessor = getProcessorInstance();
    this.mPaintDrawProcessor.init(paramBitmap, localBitmap, getWidth());
    this.mPaintDrawProcessor.setMaxUndoCount(getMaxUndoCount());
    this.d.setImageBitmap(this.mPaintDrawProcessor.getCanvasImage());
    this.c.setImageBitmap(localBitmap);
    updateBrushSettings();
  }
  
  public Bitmap getPrintDrawBitmap()
  {
    if (this.mPaintDrawProcessor != null) {
      return this.mPaintDrawProcessor.getCanvasImage();
    }
    return null;
  }
  
  public Bitmap getOriginalBitmap()
  {
    if (this.mPaintDrawProcessor != null) {
      return this.mPaintDrawProcessor.getOriginalImage();
    }
    return null;
  }
  
  protected Bitmap scaleToFill(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    float f1 = paramInt2 / paramBitmap.getHeight();
    float f2 = paramInt1 / paramBitmap.getWidth();
    float f3 = f1 > f2 ? f2 : f1;
    return Bitmap.createScaledBitmap(paramBitmap, (int)(paramBitmap.getWidth() * f3), (int)(paramBitmap.getHeight() * f3), true);
  }
  
  public Bitmap getCanvasImage(Bitmap paramBitmap, boolean paramBoolean)
  {
    if (getProcessorInstance() != null) {
      return getProcessorInstance().getSmudgeImage(paramBitmap, paramBoolean);
    }
    return null;
  }
  
  public void destroy()
  {
    if (getProcessorInstance() != null) {
      getProcessorInstance().destroy();
    }
  }
  
  public static abstract interface PaintDrawActionDelegate
  {
    public abstract void onPaintDrawChanged(PointF paramPointF1, PointF paramPointF2, int paramInt1, int paramInt2);
    
    public abstract void onPaintDrawEnd();
  }
  
  public static abstract interface PaintDrawViewDelagate
  {
    public abstract void onRefreshStepStatesWithHistories(int paramInt1, int paramInt2);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\paintdraw\PaintDrawView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */