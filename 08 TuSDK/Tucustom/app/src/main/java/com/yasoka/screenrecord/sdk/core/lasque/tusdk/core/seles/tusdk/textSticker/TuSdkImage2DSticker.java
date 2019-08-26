// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.textSticker;

import android.opengl.GLUtils;
import android.opengl.GLES20;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSdkImage2DSticker
{
    private Image2DStickerData a;
    private boolean b;
    private int c;
    private TuSdkSize d;
    private TuSdkSize e;
    
    public boolean isEnabled() {
        return this.b;
    }
    
    public void setEnabled(final boolean b) {
        this.b = b;
    }
    
    public int getCurrentTextureId() {
        if (this.a == null || this.a.getBitmap() == null) {
            TLog.e("Bitmap is null !!!", new Object[0]);
            return -1;
        }
        if (this.c <= 0) {
            this.a(this.a.getBitmap());
        }
        return this.c;
    }
    
    public void reset() {
        this.c = -1;
    }
    
    private void a(final Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        final TuSdkSize create = TuSdkSize.create(bitmap);
        if (create.minSide() <= 0) {
            TLog.e("Passed image must not be empty - it should be at least 1px tall and wide", new Object[0]);
            return;
        }
        final int[] array = { 0 };
        GLES20.glGenTextures(1, array, 0);
        final int c = array[0];
        GLES20.glBindTexture(3553, c);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        GLES20.glTexParameterf(3553, 10241, 9729.0f);
        GLES20.glTexParameterf(3553, 10240, 9729.0f);
        GLES20.glTexParameterf(3553, 10242, 33071.0f);
        GLES20.glTexParameterf(3553, 10243, 33071.0f);
        GLES20.glBindTexture(3553, 0);
        this.d = create;
        this.c = c;
    }
    
    public TuSdkSize getCurrentSize() {
        if (this.d == null) {
            this.a(this.a.getBitmap());
        }
        return this.d;
    }
    
    public Image2DStickerData getCurrentSticker() {
        return this.a;
    }
    
    public void setCurrentSticker(final Image2DStickerData a) {
        this.a = a;
    }
    
    public TuSdkSize getDesignScreenSize() {
        return this.e;
    }
    
    public void setDesignScreenSize(final TuSdkSize e) {
        this.e = e;
    }
}
