package org.lasque.tusdk.core.listener;

import android.annotation.SuppressLint;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import org.lasque.tusdk.core.utils.ColorUtils;

public class TuSdkTouchColorChangeListener
  implements View.OnTouchListener
{
  private TouchColorType a;
  private int b;
  
  public static TuSdkTouchColorChangeListener bindTouchDark(View paramView)
  {
    return bindTouch(paramView, TouchColorType.DARK);
  }
  
  public static TuSdkTouchColorChangeListener bindTouchLight(View paramView)
  {
    return bindTouch(paramView, TouchColorType.LIGHT);
  }
  
  public static TuSdkTouchColorChangeListener bindTouch(View paramView, TouchColorType paramTouchColorType)
  {
    if ((paramView == null) || (paramTouchColorType == null)) {
      return null;
    }
    TuSdkTouchColorChangeListener localTuSdkTouchColorChangeListener = new TuSdkTouchColorChangeListener(paramTouchColorType);
    paramView.setOnTouchListener(new TuSdkTouchColorChangeListener(paramTouchColorType));
    return localTuSdkTouchColorChangeListener;
  }
  
  public static TuSdkTouchColorChangeListener viewTouchDarkListener()
  {
    return new TuSdkTouchColorChangeListener(TouchColorType.DARK);
  }
  
  public static TuSdkTouchColorChangeListener viewTouchLightListener()
  {
    return new TuSdkTouchColorChangeListener(TouchColorType.LIGHT);
  }
  
  public static void setDark(Drawable paramDrawable)
  {
    changeFilter(paramDrawable, TouchColorType.DARK);
  }
  
  public static void setLight(Drawable paramDrawable)
  {
    changeFilter(paramDrawable, TouchColorType.LIGHT);
  }
  
  public static void clearColorType(Drawable paramDrawable)
  {
    changeFilter(paramDrawable, null);
  }
  
  public static void changeFilter(Drawable paramDrawable, TouchColorType paramTouchColorType)
  {
    if (paramDrawable == null) {
      return;
    }
    paramDrawable.clearColorFilter();
    if (paramTouchColorType != null) {
      paramDrawable.setColorFilter(new ColorMatrixColorFilter(paramTouchColorType.getFilter()));
    }
  }
  
  public static int changeColorFilter(ColorDrawable paramColorDrawable, TouchColorType paramTouchColorType, int paramInt)
  {
    if (Build.VERSION.SDK_INT < 11) {
      return paramInt;
    }
    ColorDrawable localColorDrawable = paramColorDrawable;
    if (paramTouchColorType != null)
    {
      paramInt = ColorUtils.getColor(localColorDrawable);
      ColorUtils.setColorFilter(localColorDrawable, paramTouchColorType.getFilter());
    }
    else
    {
      ColorUtils.setColor(localColorDrawable, paramInt);
    }
    return paramInt;
  }
  
  public TuSdkTouchColorChangeListener(TouchColorType paramTouchColorType)
  {
    this.a = paramTouchColorType;
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    if ((!paramView.isEnabled()) || (paramView.isSelected())) {
      return false;
    }
    switch (paramMotionEvent.getAction())
    {
    case 0: 
      a(paramView, this.a);
      break;
    case 1: 
    case 3: 
    case 4: 
      a(paramView, null);
      break;
    }
    return false;
  }
  
  private void a(View paramView, TouchColorType paramTouchColorType)
  {
    if ((paramView instanceof ImageView))
    {
      localObject = (ImageView)paramView;
      ((ImageView)localObject).clearColorFilter();
      if (paramTouchColorType != null) {
        ((ImageView)localObject).setColorFilter(new ColorMatrixColorFilter(paramTouchColorType.getFilter()));
      }
      return;
    }
    Object localObject = paramView.getBackground();
    if (localObject == null) {
      return;
    }
    if ((localObject instanceof ColorDrawable)) {
      this.b = changeColorFilter((ColorDrawable)localObject, paramTouchColorType, this.b);
    } else {
      changeFilter((Drawable)localObject, paramTouchColorType);
    }
  }
  
  public void enabledChanged(View paramView, boolean paramBoolean)
  {
    if (paramBoolean) {
      a(paramView, null);
    } else {
      a(paramView, this.a);
    }
  }
  
  public void selectedChanged(View paramView, boolean paramBoolean)
  {
    if (paramBoolean) {
      a(paramView, this.a);
    } else {
      a(paramView, null);
    }
  }
  
  public static enum TouchColorType
  {
    private float[] a;
    
    private TouchColorType(float[] paramArrayOfFloat)
    {
      this.a = paramArrayOfFloat;
    }
    
    public float[] getFilter()
    {
      return this.a;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\listener\TuSdkTouchColorChangeListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */