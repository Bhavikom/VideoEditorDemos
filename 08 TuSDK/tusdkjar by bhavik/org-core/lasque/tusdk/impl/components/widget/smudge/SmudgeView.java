package org.lasque.tusdk.impl.components.widget.smudge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
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
import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
import org.lasque.tusdk.modules.view.widget.smudge.BrushLocalPackage;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize.SizeType;

public class SmudgeView
  extends TuSdkRelativeLayout
{
  private SmudgeViewDelegate a;
  private SmudgeActionDelegate b;
  protected SimpleProcessor mSmudgeProcessor;
  private ImageView c;
  private ImageView d;
  private ImageView e;
  private PointF f;
  private PointF g;
  private BrushData h;
  private BrushSize.SizeType i;
  private int j = 5;
  @SuppressLint({"ClickableViewAccessibility"})
  protected View.OnTouchListener mOnTouchListener = new View.OnTouchListener()
  {
    public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
    {
      int i = SmudgeView.this.getBrushSizePixel();
      if (i <= 0) {
        return false;
      }
      if ((SmudgeView.a(SmudgeView.this) == null) || (SmudgeView.this.getProcessorInstance() == null) || (paramAnonymousMotionEvent.getPointerCount() > 1)) {
        return false;
      }
      if (SmudgeView.b(SmudgeView.this) == null) {
        SmudgeView.a(SmudgeView.this, new PointF(0.0F, 0.0F));
      }
      if (SmudgeView.c(SmudgeView.this) != null)
      {
        SmudgeView.b(SmudgeView.this).x = SmudgeView.c(SmudgeView.this).x;
        SmudgeView.b(SmudgeView.this).y = SmudgeView.c(SmudgeView.this).y;
      }
      else
      {
        SmudgeView.b(SmudgeView.this, new PointF(0.0F, 0.0F));
      }
      Matrix localMatrix = new Matrix();
      SmudgeView.a(SmudgeView.this).getImageMatrix().invert(localMatrix);
      float[] arrayOfFloat = { paramAnonymousMotionEvent.getX(), paramAnonymousMotionEvent.getY() };
      PointF localPointF = new PointF(arrayOfFloat[0], arrayOfFloat[1]);
      localMatrix.mapPoints(arrayOfFloat);
      SmudgeView.c(SmudgeView.this).x = arrayOfFloat[0];
      SmudgeView.c(SmudgeView.this).y = arrayOfFloat[1];
      SimpleProcessor localSimpleProcessor = SmudgeView.this.getProcessorInstance();
      switch (paramAnonymousMotionEvent.getAction())
      {
      case 0: 
        localSimpleProcessor.touchBegan();
        break;
      case 1: 
        localSimpleProcessor.saveCurrentAsHistory();
        SmudgeView.this.sendSmudgeActionChangeNotify();
        SmudgeView.this.onSmudgeEnd();
        break;
      case 2: 
        SmudgeView.c(SmudgeView.this, localPointF);
        localSimpleProcessor.drawBetweenPoints(SmudgeView.b(SmudgeView.this), SmudgeView.c(SmudgeView.this));
        Bitmap localBitmap = localSimpleProcessor.getCanvasImage();
        SmudgeView.d(SmudgeView.this).setImageBitmap(localBitmap);
        SmudgeView.this.onSmudgeChanged(SmudgeView.c(SmudgeView.this), localPointF, localBitmap.getWidth(), localBitmap.getHeight());
      }
      return true;
    }
  };
  
  public SmudgeViewDelegate getDelegate()
  {
    return this.a;
  }
  
  public void setDelegate(SmudgeViewDelegate paramSmudgeViewDelegate)
  {
    this.a = paramSmudgeViewDelegate;
  }
  
  public SmudgeActionDelegate getActionDelegate()
  {
    return this.b;
  }
  
  public void setActionDelegate(SmudgeActionDelegate paramSmudgeActionDelegate)
  {
    this.b = paramSmudgeActionDelegate;
  }
  
  public SmudgeView(Context paramContext)
  {
    super(paramContext);
  }
  
  public SmudgeView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public SmudgeView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected SimpleProcessor getProcessorInstance()
  {
    if (!SdkValid.shared.smudgeEnabled()) {
      return null;
    }
    if (this.mSmudgeProcessor == null) {
      this.mSmudgeProcessor = new SmudgeProcessor();
    }
    return this.mSmudgeProcessor;
  }
  
  public void setBrush(BrushData paramBrushData)
  {
    this.h = paramBrushData;
    updateBrushSettings();
  }
  
  public void setBrushSize(BrushSize.SizeType paramSizeType)
  {
    this.i = paramSizeType;
    updateBrushSettings();
    a();
  }
  
  public BrushSize.SizeType getBrushSize()
  {
    return this.i;
  }
  
  public int getBrushSizePixel()
  {
    BrushSize.SizeType localSizeType = getBrushSize();
    int k = 24;
    if (localSizeType == BrushSize.SizeType.MediumBrush)
    {
      k = 48;
    }
    else if (localSizeType == BrushSize.SizeType.LargeBrush)
    {
      k = 72;
    }
    else if (localSizeType == BrushSize.SizeType.CustomizeBrush)
    {
      float f1 = BrushSize.SizeType.CustomizeBrush.getCustomizeBrushValue();
      k = (int)(f1 * 72.0F);
    }
    return k;
  }
  
  public int getMaxUndoCount()
  {
    return this.j;
  }
  
  public void setMaxUndoCount(int paramInt)
  {
    this.j = paramInt;
  }
  
  protected void updateBrushSettings()
  {
    if (getProcessorInstance() == null) {
      return;
    }
    if (this.h == null) {
      this.h = BrushLocalPackage.shared().getEeaserBrush();
    }
    if (BrushLocalPackage.shared().loadBrushData(this.h))
    {
      getProcessorInstance().setBrush(this.h);
      getProcessorInstance().setBrushSize(this.i);
    }
  }
  
  public void loadView()
  {
    super.loadView();
    this.c = new ImageView(getContext());
    addView(this.c, new RelativeLayout.LayoutParams(-1, -1));
    this.d = new ImageView(getContext());
    this.d.setOnTouchListener(this.mOnTouchListener);
    addView(this.d, new RelativeLayout.LayoutParams(-1, -1));
    this.e = new ImageView(getContext());
    this.e.setVisibility(4);
    this.e.setBackgroundColor(16777215);
    addView(this.e, new RelativeLayout.LayoutParams(24, 24));
    a();
  }
  
  private void a()
  {
    int k = getBrushSizePixel();
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)this.e.getLayoutParams();
    localLayoutParams.width = k;
    localLayoutParams.height = k;
    localLayoutParams.leftMargin = 0;
    localLayoutParams.topMargin = 0;
    this.e.setLayoutParams(localLayoutParams);
    if (k > 0) {
      this.e.setImageBitmap(BitmapHelper.createOvalImage(k, k, -1));
    }
  }
  
  private void a(PointF paramPointF)
  {
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)this.e.getLayoutParams();
    localLayoutParams.setMargins((int)paramPointF.x - localLayoutParams.width / 2, (int)paramPointF.y - localLayoutParams.height / 2, 0, 0);
    this.e.setLayoutParams(localLayoutParams);
    this.e.setVisibility(0);
  }
  
  protected void onSmudgeChanged(PointF paramPointF1, PointF paramPointF2, int paramInt1, int paramInt2)
  {
    if (getActionDelegate() != null) {
      getActionDelegate().onSmudgeChanged(paramPointF1, paramPointF2, paramInt1, paramInt2);
    }
  }
  
  protected void onSmudgeEnd()
  {
    if (getActionDelegate() != null) {
      getActionDelegate().onSmudgeEnd();
    }
    this.e.setVisibility(4);
  }
  
  public void undo()
  {
    SimpleProcessor localSimpleProcessor = getProcessorInstance();
    if (localSimpleProcessor == null) {
      return;
    }
    if (localSimpleProcessor.getUndoCount() > 0) {
      this.d.setImageBitmap(localSimpleProcessor.getUndoData());
    }
    sendSmudgeActionChangeNotify();
  }
  
  public void redo()
  {
    SimpleProcessor localSimpleProcessor = getProcessorInstance();
    if (localSimpleProcessor == null) {
      return;
    }
    if (localSimpleProcessor.getRedoCount() > 0) {
      this.d.setImageBitmap(localSimpleProcessor.getRedoData());
    }
    sendSmudgeActionChangeNotify();
  }
  
  public void showOriginalImage(boolean paramBoolean)
  {
    if (getProcessorInstance() != null) {
      this.d.setVisibility(paramBoolean ? 4 : 0);
    }
  }
  
  protected int getRedoCount()
  {
    if (getProcessorInstance() != null) {
      return getProcessorInstance().getRedoCount();
    }
    return 0;
  }
  
  protected int getUndoCount()
  {
    if (getProcessorInstance() != null) {
      return getProcessorInstance().getUndoCount();
    }
    return 0;
  }
  
  protected void sendSmudgeActionChangeNotify()
  {
    if (getDelegate() != null) {
      getDelegate().onRefreshStepStatesWithHistories(getUndoCount(), getRedoCount());
    }
  }
  
  public void setImageBitmap(Bitmap paramBitmap)
  {
    if ((getClass() == SmudgeView.class) && (!SdkValid.shared.smudgeEnabled()))
    {
      TLog.e("You are not allowed to use the smudge feature, please see http://tusdk.com", new Object[0]);
      return;
    }
    if (paramBitmap == null) {
      return;
    }
    Bitmap localBitmap = scaleToFill(paramBitmap, getWidth(), getHeight());
    this.mSmudgeProcessor = getProcessorInstance();
    this.mSmudgeProcessor.init(paramBitmap, localBitmap, getWidth());
    this.mSmudgeProcessor.setMaxUndoCount(getMaxUndoCount());
    this.d.setImageBitmap(this.mSmudgeProcessor.getCanvasImage());
    this.c.setImageBitmap(localBitmap);
    updateBrushSettings();
  }
  
  public Bitmap getSmudgeBitmap()
  {
    if (this.mSmudgeProcessor != null) {
      return this.mSmudgeProcessor.getCanvasImage();
    }
    return null;
  }
  
  public Bitmap getOriginalBitmap()
  {
    if (this.mSmudgeProcessor != null) {
      return this.mSmudgeProcessor.getOriginalImage();
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
  
  public static abstract interface SmudgeActionDelegate
  {
    public abstract void onSmudgeChanged(PointF paramPointF1, PointF paramPointF2, int paramInt1, int paramInt2);
    
    public abstract void onSmudgeEnd();
  }
  
  public static abstract interface SmudgeViewDelegate
  {
    public abstract void onRefreshStepStatesWithHistories(int paramInt1, int paramInt2);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\smudge\SmudgeView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */