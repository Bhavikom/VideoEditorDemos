package org.lasque.tusdk.core.listener;

import android.content.Context;
import android.view.OrientationEventListener;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;

public class TuSdkOrientationEventListener
  extends OrientationEventListener
{
  private int a;
  private TuSdkOrientationDegreeDelegate b;
  private TuSdkOrientationDelegate c;
  private InterfaceOrientation d;
  private boolean e;
  
  public int getDeviceAngle()
  {
    return this.a;
  }
  
  public TuSdkOrientationEventListener(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuSdkOrientationEventListener(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
  }
  
  public void onOrientationChanged(int paramInt)
  {
    this.a = paramInt;
    if (this.b != null) {
      this.b.onOrientationDegreeChanged(paramInt);
    }
    a(paramInt);
  }
  
  private void a(int paramInt)
  {
    InterfaceOrientation localInterfaceOrientation1 = getOrien();
    Object localObject = localInterfaceOrientation1;
    for (InterfaceOrientation localInterfaceOrientation2 : InterfaceOrientation.values()) {
      if (localInterfaceOrientation2.isMatch(paramInt))
      {
        localObject = localInterfaceOrientation2;
        break;
      }
    }
    this.d = ((InterfaceOrientation)localObject);
    if (((this.e) || (localObject != localInterfaceOrientation1)) && (this.c != null))
    {
      this.c.onOrientationChanged((InterfaceOrientation)localObject);
      this.e = false;
    }
  }
  
  public InterfaceOrientation getOrien()
  {
    if (this.d == null) {
      this.d = InterfaceOrientation.Portrait;
    }
    return this.d;
  }
  
  public void setDelegate(TuSdkOrientationDelegate paramTuSdkOrientationDelegate, TuSdkOrientationDegreeDelegate paramTuSdkOrientationDegreeDelegate)
  {
    this.c = paramTuSdkOrientationDelegate;
    this.b = paramTuSdkOrientationDegreeDelegate;
  }
  
  public void enable()
  {
    this.e = true;
    super.enable();
  }
  
  public static abstract interface TuSdkOrientationDelegate
  {
    public abstract void onOrientationChanged(InterfaceOrientation paramInterfaceOrientation);
  }
  
  public static abstract interface TuSdkOrientationDegreeDelegate
  {
    public abstract void onOrientationDegreeChanged(int paramInt);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\listener\TuSdkOrientationEventListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */