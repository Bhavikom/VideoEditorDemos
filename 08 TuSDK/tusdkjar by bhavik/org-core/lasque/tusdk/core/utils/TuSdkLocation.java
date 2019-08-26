package org.lasque.tusdk.core.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import java.util.List;

public class TuSdkLocation
{
  private static TuSdkLocation a;
  private Context b;
  private Criteria c;
  private Location d;
  private LocationManager e;
  private TuSdkLocationListener f;
  private TuSdkLocationDelegate g;
  private Handler h;
  private Runnable i;
  private boolean j;
  private boolean k;
  
  public static void init(Context paramContext)
  {
    if ((a == null) && (paramContext != null)) {
      a = new TuSdkLocation(paramContext);
    }
  }
  
  public static void setDelegate(TuSdkLocationDelegate paramTuSdkLocationDelegate)
  {
    if (a == null) {
      return;
    }
    a.g = paramTuSdkLocationDelegate;
    if (a.g != null) {
      a.c();
    }
  }
  
  public static Location getLastLocation()
  {
    if (a == null) {
      return null;
    }
    a.c();
    return a.d;
  }
  
  public static boolean canGps()
  {
    if (a == null) {
      return false;
    }
    return a.k;
  }
  
  public TuSdkLocation(Context paramContext)
  {
    this.b = paramContext;
    this.c = a();
    this.e = ((LocationManager)ContextUtils.getSystemService(this.b, "location"));
    this.f = new TuSdkLocationListener(null);
    this.h = new Handler(paramContext.getMainLooper());
    this.i = new Runnable()
    {
      public void run()
      {
        TuSdkLocation.this.cancelGPS();
      }
    };
    c();
  }
  
  private Criteria a()
  {
    Criteria localCriteria = new Criteria();
    localCriteria.setAccuracy(1);
    localCriteria.setAltitudeRequired(false);
    localCriteria.setBearingRequired(false);
    localCriteria.setCostAllowed(true);
    localCriteria.setPowerRequirement(1);
    return localCriteria;
  }
  
  private void b()
  {
    if (this.g == null) {
      return;
    }
    this.g.onLocationReceived(this.d);
  }
  
  private void c()
  {
    if (!e()) {
      return;
    }
    this.h.post(new Runnable()
    {
      public void run()
      {
        TuSdkLocation.a(TuSdkLocation.this);
      }
    });
  }
  
  private void d()
  {
    if (!e()) {
      return;
    }
    String str = this.e.getBestProvider(this.c, true);
    if ((this.k = str != null ? 1 : 0) == 0) {
      return;
    }
    if (this.e.getAllProviders() == null) {
      return;
    }
    if ((!this.e.getAllProviders().contains("network")) || (!this.e.isProviderEnabled("network"))) {
      return;
    }
    try
    {
      this.h.removeCallbacks(this.i);
      this.j = true;
      this.f.setProvider(str);
      this.d = this.e.getLastKnownLocation(str);
      this.e.requestSingleUpdate(str, this.f, null);
      this.h.postDelayed(this.i, 20000L);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  private boolean e()
  {
    if ((this.e == null) || (this.j)) {
      return false;
    }
    if (this.d == null) {
      return true;
    }
    if (Math.abs(TuSdkDate.create().diffOfMillis(this.d.getTime())) < 300000L)
    {
      b();
      return false;
    }
    return true;
  }
  
  public void locationChangedCallback(Location paramLocation, TuSdkLocationListener paramTuSdkLocationListener)
  {
    this.d = paramLocation;
    b();
    this.e.removeUpdates(paramTuSdkLocationListener);
    this.j = false;
  }
  
  public void cancelGPS()
  {
    if ((this.j) && (this.f != null))
    {
      this.e.removeUpdates(this.f);
      this.j = false;
    }
  }
  
  private class TuSdkLocationListener
    implements LocationListener
  {
    private String b;
    
    private TuSdkLocationListener() {}
    
    public void setProvider(String paramString)
    {
      this.b = paramString;
    }
    
    public void onLocationChanged(Location paramLocation)
    {
      TuSdkLocation.this.locationChangedCallback(paramLocation, this);
    }
    
    public void onStatusChanged(String paramString, int paramInt, Bundle paramBundle) {}
    
    public void onProviderEnabled(String paramString) {}
    
    public void onProviderDisabled(String paramString) {}
  }
  
  public static abstract interface TuSdkLocationDelegate
  {
    public abstract void onLocationReceived(Location paramLocation);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\TuSdkLocation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */