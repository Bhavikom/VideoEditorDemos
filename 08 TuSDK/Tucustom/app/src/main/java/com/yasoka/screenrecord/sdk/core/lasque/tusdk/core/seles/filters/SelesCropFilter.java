// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters;

import android.graphics.Rect;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.RectHelper;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.nio.FloatBuffer;

public class SelesCropFilter extends SelesFilter
{
    private FloatBuffer a;
    private RectF b;
    private TuSdkSize c;
    private boolean d;
    
    public SelesCropFilter() {
        this.b = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        this.c = new TuSdkSize(0, 0);
        this.d = false;
        this.a = SelesFilter.buildBuffer(SelesCropFilter.noRotationTextureCoordinates);
    }
    
    public void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    public void setOutputSize(final TuSdkSize c) {
        if (c.isSize() && !c.equals(this.c)) {
            this.c = c;
        }
    }
    
    public TuSdkSize getOutputSize() {
        if (this.c.isSize()) {
            return this.c;
        }
        return this.mInputTextureSize;
    }
    
    public void setEnableHorizontallyFlip(final boolean d) {
        this.d = d;
        this.a();
    }
    
    public boolean isEnableHorizontallyFlip() {
        return this.d;
    }
    
    public void setCropRegion(final RectF b) {
        if (this.b.left == b.left && this.b.right == b.right && this.b.top == b.top && this.b.bottom == b.bottom) {
            return;
        }
        this.b = b;
        this.a();
    }
    
    protected void updateCropRegion(final TuSdkSize tuSdkSize) {
        final RectF cropRegion = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        if (!tuSdkSize.isSize() || !this.c.isSize()) {
            return;
        }
        final TuSdkSize computerOutSize = RectHelper.computerOutSize(tuSdkSize, this.c.getRatioFloat(), true);
        cropRegion.right = computerOutSize.width / (float)tuSdkSize.width;
        cropRegion.bottom = computerOutSize.height / (float)tuSdkSize.height;
        this.setCropRegion(cropRegion);
    }
    
    private void a() {
        final float n = 0.5f - this.b.right / 2.0f;
        final float n2 = 0.5f - this.b.bottom / 2.0f;
        final float n3 = 0.5f + this.b.right / 2.0f;
        final float n4 = 0.5f + this.b.bottom / 2.0f;
        final float[] src = { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };
        ImageOrientation imageOrientation = this.mInputRotation;
        if (this.isEnableHorizontallyFlip() && imageOrientation == ImageOrientation.Up) {
            imageOrientation = ImageOrientation.UpMirrored;
        }
        switch (imageOrientation.ordinal()) {
            case 1: {
                src[0] = n3;
                src[1] = n2;
                src[2] = n3;
                src[3] = n4;
                src[4] = n;
                src[5] = n2;
                src[6] = n;
                src[7] = n4;
                break;
            }
            case 2: {
                src[0] = n;
                src[1] = n4;
                src[2] = n;
                src[3] = n2;
                src[4] = n3;
                src[5] = n4;
                src[6] = n3;
                src[7] = n2;
                break;
            }
            case 3: {
                src[0] = n;
                src[1] = n4;
                src[2] = n3;
                src[3] = n4;
                src[4] = n;
                src[5] = n2;
                src[6] = n3;
                src[7] = n2;
                break;
            }
            case 4: {
                src[0] = n3;
                src[1] = n2;
                src[2] = n;
                src[3] = n2;
                src[4] = n3;
                src[5] = n4;
                src[6] = n;
                src[7] = n4;
                break;
            }
            case 5: {
                src[0] = n;
                src[1] = n2;
                src[2] = n;
                src[3] = n4;
                src[4] = n3;
                src[5] = n2;
                src[6] = n3;
                src[7] = n4;
                break;
            }
            case 6: {
                src[0] = n3;
                src[1] = n4;
                src[2] = n3;
                src[3] = n2;
                src[4] = n;
                src[5] = n4;
                src[6] = n;
                src[7] = n2;
                break;
            }
            case 7: {
                src[0] = n3;
                src[1] = n4;
                src[2] = n;
                src[3] = n4;
                src[4] = n3;
                src[5] = n2;
                src[6] = n;
                src[7] = n2;
                break;
            }
            default: {
                src[0] = n;
                src[1] = n2;
                src[2] = n3;
                src[3] = n2;
                src[4] = n;
                src[5] = n4;
                src[6] = n3;
                src[7] = n4;
                break;
            }
        }
        this.a.clear();
        this.a.put(src).position(0);
    }
    
    @Override
    public void setInputRotation(final ImageOrientation imageOrientation, final int n) {
        super.setInputRotation(imageOrientation, n);
        this.a();
    }
    
    @Override
    public void setInputSize(TuSdkSize create, final int n) {
        if (this.isPreventRendering()) {
            return;
        }
        if (this.mOverrideInputSize) {
            if (this.mForcedMaximumSize == null || this.mForcedMaximumSize.minSide() < 1) {
                this.setupFilterForSize(this.sizeOfFBO());
                return;
            }
            create = TuSdkSize.create(RectHelper.makeRectWithAspectRatioInsideRect(create, new Rect(0, 0, this.mForcedMaximumSize.width, this.mForcedMaximumSize.height)));
        }
        final TuSdkSize rotatedSize = this.rotatedSize(create, n);
        if (rotatedSize.isSize() && !rotatedSize.equals(this.mInputTextureSize)) {
            this.mInputTextureSize = rotatedSize;
        }
        this.updateCropRegion(this.mInputTextureSize);
        final TuSdkSize mInputTextureSize = new TuSdkSize();
        mInputTextureSize.width = (int)(this.mInputTextureSize.width * this.b.width());
        mInputTextureSize.height = (int)(this.mInputTextureSize.height * this.b.height());
        if (mInputTextureSize.isSize() && !this.mInputTextureSize.equals(mInputTextureSize)) {
            this.mInputTextureSize = mInputTextureSize;
        }
        this.setupFilterForSize(this.sizeOfFBO());
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, this.a);
        this.checkGLError(this.getClass().getSimpleName());
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    }
    
    @Override
    public TuSdkSize maximumOutputSize() {
        return this.c;
    }
    
    public void setUsingNextFrameForImageCapture(final boolean mUsingNextFrameForImageCapture) {
        this.mUsingNextFrameForImageCapture = mUsingNextFrameForImageCapture;
    }
}
