// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Looper;
import android.location.LocationListener;
import android.os.Handler;
import android.location.LocationManager;
import android.location.Location;
import android.location.Criteria;
import android.content.Context;

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

    public static void init(final Context context) {
        if (TuSdkLocation.a == null && context != null) {
            TuSdkLocation.a = new TuSdkLocation(context);
        }
    }

    public static void setDelegate(final TuSdkLocationDelegate g) {
        if (TuSdkLocation.a == null) {
            return;
        }
        TuSdkLocation.a.g = g;
        if (TuSdkLocation.a.g != null) {
            TuSdkLocation.a.c();
        }
    }

    public static Location getLastLocation() {
        if (TuSdkLocation.a == null) {
            return null;
        }
        TuSdkLocation.a.c();
        return TuSdkLocation.a.d;
    }

    public static boolean canGps() {
        return TuSdkLocation.a != null && TuSdkLocation.a.k;
    }

    public TuSdkLocation(final Context b) {
        this.b = b;
        this.c = this.a();
        this.e = ContextUtils.getSystemService(this.b, "location");
        this.f = new TuSdkLocationListener();
        this.h = new Handler(b.getMainLooper());
        this.i = new Runnable() {
            @Override
            public void run() {
                TuSdkLocation.this.cancelGPS();
            }
        };
        this.c();
    }

    private Criteria a() {
        final Criteria criteria = new Criteria();
        criteria.setAccuracy(1);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(1);
        return criteria;
    }

    private void b() {
        if (this.g == null) {
            return;
        }
        this.g.onLocationReceived(this.d);
    }

    private void c() {
        if (!this.e()) {
            return;
        }
        this.h.post((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSdkLocation.this.d();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void d() {
        if (!this.e()) {
            return;
        }
        final String bestProvider = this.e.getBestProvider(this.c, true);
        if (!(this.k = (bestProvider != null))) {
            return;
        }
        if (this.e.getAllProviders() == null) {
            return;
        }
        if (!this.e.getAllProviders().contains("network") || !this.e.isProviderEnabled("network")) {
            return;
        }
        try {
            this.h.removeCallbacks(this.i);
            this.j = true;
            this.f.setProvider(bestProvider);
            this.d = this.e.getLastKnownLocation(bestProvider);
            this.e.requestSingleUpdate(bestProvider, (LocationListener)this.f, (Looper)null);
            this.h.postDelayed(this.i, 20000L);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private boolean e() {
        if (this.e == null || this.j) {
            return false;
        }
        if (this.d == null) {
            return true;
        }
        if (Math.abs(TuSdkDate.create().diffOfMillis(this.d.getTime())) < 300000L) {
            this.b();
            return false;
        }
        return true;
    }
    
    public void locationChangedCallback(final Location d, final TuSdkLocationListener tuSdkLocationListener) {
        this.d = d;
        this.b();
        this.e.removeUpdates((LocationListener)tuSdkLocationListener);
        this.j = false;
    }
    
    public void cancelGPS() {
        if (this.j && this.f != null) {
            this.e.removeUpdates((LocationListener)this.f);
            this.j = false;
        }
    }
    
    private class TuSdkLocationListener implements LocationListener
    {
        private String b;
        
        public void setProvider(final String b) {
            this.b = b;
        }
        
        public void onLocationChanged(final Location location) {
            TuSdkLocation.this.locationChangedCallback(location, this);
        }
        
        public void onStatusChanged(final String s, final int n, final Bundle bundle) {
        }
        
        public void onProviderEnabled(final String s) {
        }
        
        public void onProviderDisabled(final String s) {
        }
    }
    
    public interface TuSdkLocationDelegate
    {
        void onLocationReceived(final Location p0);
    }
}
