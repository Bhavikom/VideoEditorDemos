// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend;

import android.os.Build;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.HardwareHelper;

import java.util.HashMap;
//import org.lasque.tusdk.core.utils.hardware.HardwareHelper;
import java.util.Map;

public class TuSdkMediaTimeSlicePatch
{
    private static String[] a;
    private static final Map<String, String> b;
    private static String c;
    private boolean d;
    private boolean e;
    
    public TuSdkMediaTimeSlicePatch() {
        this.d = false;
        this.e = false;
    }
    
    private boolean a() {
        if (TuSdkMediaTimeSlicePatch.c == null || TuSdkMediaTimeSlicePatch.c.isEmpty()) {
            return false;
        }
        final String[] a = TuSdkMediaTimeSlicePatch.a;
        for (int length = a.length, i = 0; i < length; ++i) {
            if (TuSdkMediaTimeSlicePatch.c.startsWith(a[i])) {
                return true;
            }
        }
        return false;
    }
    
    public boolean overview(final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity, final long n, final long n2) {
        if (tuSdkMediaTimeSliceEntity == null) {
            return false;
        }
        if (!this.a() || this.b()) {
            return tuSdkMediaTimeSliceEntity.overview(n2) > 0;
        }
        if (n < n2 && !tuSdkMediaTimeSliceEntity.isReverse() && !this.e) {
            this.d = true;
        }
        return this.d || tuSdkMediaTimeSliceEntity.overview(n2) > 0;
    }
    
    public void switchSliced() {
        if (this.d) {
            this.d = false;
            this.e = true;
        }
    }
    
    public boolean isReturnFrame(final long n, final long n2) {
        if (this.e && this.a()) {
            if (n > n2) {
                this.d = false;
                this.e = false;
            }
            return true;
        }
        return false;
    }
    
    private boolean b() {
        boolean matchDeviceModelAndManuFacturer = false;
        for (final Map.Entry<String, String> entry : TuSdkMediaTimeSlicePatch.b.entrySet()) {
            matchDeviceModelAndManuFacturer = HardwareHelper.isMatchDeviceModelAndManuFacturer(entry.getKey(), entry.getValue());
            if (matchDeviceModelAndManuFacturer) {
                break;
            }
        }
        return matchDeviceModelAndManuFacturer;
    }
    
    static {
        TuSdkMediaTimeSlicePatch.a = new String[] { "mt" };
        (b = new HashMap<String, String>()).put("MP1605", "Meitu");
        TuSdkMediaTimeSlicePatch.c = Build.HARDWARE;
    }
}
