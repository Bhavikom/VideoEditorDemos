// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener;

import android.content.Context;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import android.view.OrientationEventListener;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;

public class TuSdkOrientationEventListener extends OrientationEventListener
{
    private int a;
    private TuSdkOrientationDegreeDelegate b;
    private TuSdkOrientationDelegate c;
    private InterfaceOrientation d;
    private boolean e;
    
    public int getDeviceAngle() {
        return this.a;
    }
    
    public TuSdkOrientationEventListener(final Context context) {
        super(context);
    }
    
    public TuSdkOrientationEventListener(final Context context, final int n) {
        super(context, n);
    }
    
    public void onOrientationChanged(final int a) {
        this.a = a;
        if (this.b != null) {
            this.b.onOrientationDegreeChanged(a);
        }
        this.a(a);
    }
    
    private void a(final int n) {
        InterfaceOrientation orien;
        final InterfaceOrientation interfaceOrientation = orien = this.getOrien();
        for (final InterfaceOrientation interfaceOrientation2 : InterfaceOrientation.values()) {
            if (interfaceOrientation2.isMatch(n)) {
                orien = interfaceOrientation2;
                break;
            }
        }
        this.d = orien;
        if ((this.e || orien != interfaceOrientation) && this.c != null) {
            this.c.onOrientationChanged(orien);
            this.e = false;
        }
    }
    
    public InterfaceOrientation getOrien() {
        if (this.d == null) {
            this.d = InterfaceOrientation.Portrait;
        }
        return this.d;
    }
    
    public void setDelegate(final TuSdkOrientationDelegate c, final TuSdkOrientationDegreeDelegate b) {
        this.c = c;
        this.b = b;
    }
    
    public void enable() {
        this.e = true;
        super.enable();
    }
    
    public interface TuSdkOrientationDelegate
    {
        void onOrientationChanged(final InterfaceOrientation p0);
    }
    
    public interface TuSdkOrientationDegreeDelegate
    {
        void onOrientationDegreeChanged(final int p0);
    }
}
