package org.lasque.tusdk.impl.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import java.io.IOException;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.utils.image.TuSdkGifDrawable;
import org.lasque.tusdk.core.utils.image.TuSdkGifDrawable.TuGifAnimationListener;
import org.lasque.tusdk.modules.components.ComponentActType;

public class TuGifView
  extends ImageView
  implements Drawable.Callback, TuSdkGifDrawable.TuGifAnimationListener
{
  private TuSdkGifDrawable a;
  private boolean b = true;
  private TuGifViewDelegate c;
  
  public TuGifView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuGifView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    a(paramAttributeSet);
  }
  
  public TuGifView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    a(paramAttributeSet);
  }
  
  private void a(AttributeSet paramAttributeSet)
  {
    if ((paramAttributeSet == null) && (isInEditMode())) {
      return;
    }
    String str1 = "http://schemas.android.com/apk/res/android";
    int i = paramAttributeSet.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
    if (i > 0)
    {
      String str2 = getResources().getResourceTypeName(i);
      if (("drawable".equals(str2)) || ("raw".equals(str2))) {
        setImageResource(i);
      }
    }
  }
  
  public void setImageURI(Uri paramUri)
  {
    if (a(paramUri)) {
      super.setImageURI(paramUri);
    }
  }
  
  private boolean a(Uri paramUri)
  {
    if (paramUri != null) {
      try
      {
        a();
        this.a = new TuSdkGifDrawable(getContext().getContentResolver(), paramUri);
        a(this.a);
        return true;
      }
      catch (IOException localIOException) {}
    }
    return false;
  }
  
  public void setImageResource(int paramInt)
  {
    if (!a(paramInt)) {
      super.setImageResource(paramInt);
    }
    if ((this.a != null) && (isAutoPlay())) {
      start();
    }
  }
  
  private boolean a(int paramInt)
  {
    Resources localResources = getResources();
    if (localResources != null) {
      try
      {
        a();
        this.a = new TuSdkGifDrawable(localResources, paramInt);
        a(this.a);
        return true;
      }
      catch (IOException localIOException) {}
    }
    return false;
  }
  
  private void a()
  {
    if ((this.a != null) && (!this.a.isRecycled()))
    {
      this.a.setCallback(null);
      this.a.removeAnimationListener(this);
      this.a.recycle();
      this.a = null;
    }
  }
  
  private void a(TuSdkGifDrawable paramTuSdkGifDrawable)
  {
    setImageDrawable(paramTuSdkGifDrawable);
    paramTuSdkGifDrawable.addAnimationListener(this);
    StatisticsManger.appendComponent(ComponentActType.gifViewer);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    if (this.a != null) {
      this.a.draw(paramCanvas);
    }
  }
  
  public void invalidateDrawable(Drawable paramDrawable)
  {
    invalidate();
  }
  
  public void scheduleDrawable(Drawable paramDrawable, Runnable paramRunnable, long paramLong)
  {
    postDelayed(paramRunnable, paramLong);
  }
  
  public void unscheduleDrawable(Drawable paramDrawable, Runnable paramRunnable)
  {
    removeCallbacks(paramRunnable);
  }
  
  public boolean isAutoPlay()
  {
    return this.b;
  }
  
  public void setAutoPlay(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  public boolean isRunning()
  {
    if (this.a != null) {
      return this.a.isRunning();
    }
    return false;
  }
  
  public void start()
  {
    if (this.a != null)
    {
      if (this.a.getCallback() == null) {
        this.a.setCallback(this);
      }
      this.a.start();
    }
  }
  
  public void pause()
  {
    if (this.a != null) {
      this.a.pause();
    }
  }
  
  public void onGifAnimationCompleted(int paramInt)
  {
    if (getDelegate() != null) {
      getDelegate().onGifAnimationComplete(paramInt);
    }
  }
  
  public TuGifViewDelegate getDelegate()
  {
    return this.c;
  }
  
  public void setDelegate(TuGifViewDelegate paramTuGifViewDelegate)
  {
    this.c = paramTuGifViewDelegate;
  }
  
  public void reset()
  {
    if (this.a != null) {
      this.a.reset();
    }
  }
  
  public void dispose()
  {
    a();
  }
  
  public static abstract interface TuGifViewDelegate
  {
    public abstract void onGifAnimationComplete(int paramInt);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\TuGifView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */