// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.imageToVideo;

import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
import android.content.Context;
//import org.lasque.tusdk.core.utils.TLog;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

public class TuSdkImageComposeItem extends TuSdkComposeItem
{
    private Bitmap a;
    private long b;
    private long c;
    private long d;
    
    public TuSdkImageComposeItem() {
        this.b = 2000000L;
        this.mComposeType = TuSdkComposeType.IMAGE;
    }
    
    public void setImageBitmap(final Bitmap a) {
        if (a == null || a.isRecycled()) {
            TLog.w("bitmap is null or recycled !!!", "TuSdkImageComposeItem");
            return;
        }
        this.a = a;
    }
    
    public void setImageResource(final Context context, @IdRes final int n) {
        this.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), n));
    }
    
    public void setImagePath(final String s) {
        this.setImageBitmap(BitmapFactory.decodeFile(s));
    }
    
    public void setDurationUs(final long n) {
        if (n <= 0L) {
            TLog.w("%s set durationUs is invalid !!!  %s", "TuSdkImageComposeItem", n);
            return;
        }
        this.b = n;
    }
    
    public void setDuration(final float n) {
        this.setDurationUs((long)(n * 1000000.0f));
    }
    
    public long getDurationUs() {
        return this.b;
    }
    
    public Bitmap getImageBitmap() {
        return this.a;
    }
    
    public void alignTimeRange(final long c) {
        this.c = c;
        this.d = this.c + this.b;
    }
    
    public boolean isContainTimeRange(final long n) {
        return n >= this.c && n < this.d;
    }
    
    public long getStartTimeUs() {
        return this.c;
    }
    
    public long getEndTimeUs() {
        return this.d;
    }
}
