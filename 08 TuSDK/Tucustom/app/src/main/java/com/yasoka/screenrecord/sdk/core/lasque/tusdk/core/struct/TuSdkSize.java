// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct;

import android.graphics.Point;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.utils.hardware.CameraHelper;
//import org.lasque.tusdk.core.utils.ContextUtils;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.io.Serializable;

public class TuSdkSize implements Serializable
{
    public int width;
    public int height;
    
    public static TuSdkSize create(final Bitmap bitmap) {
        if (bitmap != null) {
            return new TuSdkSize(bitmap.getWidth(), bitmap.getHeight());
        }
        return null;
    }
    
    public static TuSdkSize create(final TuSdkSize tuSdkSize) {
        if (tuSdkSize != null) {
            return new TuSdkSize(tuSdkSize.width, tuSdkSize.height);
        }
        return null;
    }
    
    public static TuSdkSize create(final Rect rect) {
        if (rect != null) {
            return new TuSdkSize(rect.width(), rect.height());
        }
        return null;
    }
    
    public static TuSdkSize create(final int n) {
        return create(n, n);
    }
    
    public static TuSdkSize create(final int n, final int n2) {
        return new TuSdkSize(n, n2);
    }
    
    public static TuSdkSize createDP(final Context context, final int n, final int n2) {
        return create(ContextUtils.dip2px(context, (float)n), ContextUtils.dip2px(context, (float)n2));
    }
    
    public TuSdkSize() {
    }
    
    public TuSdkSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
    
    public void set(final TuSdkSize tuSdkSize) {
        if (tuSdkSize == null) {
            return;
        }
        this.set(tuSdkSize.width, tuSdkSize.height);
    }
    
    public void set(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
    
    public int getRatio() {
        return CameraHelper.getSizeRatio(this.width, this.height);
    }
    
    public float getRatioFloat() {
        return this.width / (float)this.height;
    }
    
    public TuSdkSize maxSize() {
        final int maxSide = this.maxSide();
        return new TuSdkSize(maxSide, maxSide);
    }
    
    public int maxSide() {
        return Math.max(this.width, this.height);
    }
    
    public TuSdkSize minSize() {
        final int minSide = this.minSide();
        return new TuSdkSize(minSide, minSide);
    }
    
    public int minSide() {
        return Math.min(this.width, this.height);
    }
    
    public boolean isSize() {
        return this.minSide() > 0;
    }
    
    public float maxMinRatio() {
        return this.maxSide() / (float)this.minSide();
    }
    
    public float minMaxRatio() {
        return this.minSide() / (float)this.maxSide();
    }
    
    public float diagonal() {
        return (float)Math.sqrt(this.width * this.width + this.height * this.height);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof TuSdkSize)) {
            return false;
        }
        final TuSdkSize tuSdkSize = (TuSdkSize)o;
        return this.width == tuSdkSize.width && this.height == tuSdkSize.height;
    }
    
    @Override
    public int hashCode() {
        return this.width * 32713 + this.height;
    }
    
    @Override
    public String toString() {
        return String.format("{width: %s, height:%s };", this.width, this.height);
    }
    
    public TuSdkSize copy() {
        return new TuSdkSize(this.width, this.height);
    }
    
    public TuSdkSize dp2Pix() {
        if (TuSdkContext.ins() == null || TuSdkContext.ins().getContext() == null) {
            return null;
        }
        final Context context = TuSdkContext.ins().getContext();
        final TuSdkSize tuSdkSize = new TuSdkSize();
        tuSdkSize.width = ContextUtils.dip2px(context, (float)this.width);
        tuSdkSize.height = ContextUtils.dip2px(context, (float)this.height);
        return tuSdkSize;
    }
    
    public final float limitScale() {
        final TuSdkSize limitSize = this.limitSize();
        if (limitSize.maxSide() >= this.maxSide()) {
            return 1.0f;
        }
        return limitSize.maxSide() / (float)this.maxSide();
    }
    
    public final TuSdkSize limitSize() {
        return this.a(TuSdkGPU.getMaxTextureOptimizedSize());
    }
    
    private TuSdkSize a(int min) {
        if (SdkValid.shared.maxImageSide() > 0) {
            min = Math.min(Math.max(min, 0), SdkValid.shared.maxImageSide());
        }
        if (min <= 0 || !this.isSize() || min >= this.maxSide()) {
            return this.evenSize();
        }
        final TuSdkSize create = create(this);
        if (this.width > this.height) {
            create.width = min;
            create.height = (int)Math.floor(min / (float)this.width * this.height);
        }
        else {
            create.height = min;
            create.width = (int)Math.floor(min / (float)this.height * this.width);
        }
        return create.evenSize();
    }
    
    public TuSdkSize evenSize() {
        final TuSdkSize create = create(this);
        if (create.width % 2 != 0) {
            final TuSdkSize tuSdkSize = create;
            --tuSdkSize.width;
        }
        if (create.height % 2 != 0) {
            final TuSdkSize tuSdkSize2 = create;
            --tuSdkSize2.height;
        }
        return create;
    }
    
    public TuSdkSize transforOrientation(final ImageOrientation imageOrientation) {
        final TuSdkSize create = create(this);
        if (imageOrientation == null || !imageOrientation.isTransposed()) {
            return create;
        }
        create.width = this.height;
        create.height = this.width;
        return create;
    }
    
    public Point center() {
        final Point point = new Point();
        point.x = (int)(this.width * 0.5f);
        point.y = (int)(this.height * 0.5f);
        return point;
    }
    
    public TuSdkSize scale(final float n) {
        if (n == 0.0f || n == 1.0f) {
            return this;
        }
        return new TuSdkSize((int)(this.width * n), (int)(this.height * n)).evenSize();
    }
}
