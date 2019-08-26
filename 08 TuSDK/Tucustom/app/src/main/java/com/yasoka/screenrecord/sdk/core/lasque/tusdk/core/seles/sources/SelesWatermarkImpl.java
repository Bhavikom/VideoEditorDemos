// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.RectF;
import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.output.SelesSurfacePusher;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesSurfacePusher;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class SelesWatermarkImpl extends SelesFilter implements SelesWatermark
{
    private final FloatBuffer a;
    private final FloatBuffer b;
    private boolean c;
    private TuSdkWaterMarkOption.WaterMarkPosition d;
    private float e;
    private float f;
    private boolean g;
    
    @Override
    public void setWaterPostion(final TuSdkWaterMarkOption.WaterMarkPosition d) {
        if (d == null) {
            return;
        }
        this.d = d;
        this.g = true;
    }
    
    @Override
    public void setScale(final float n) {
        if (n <= 0.0f || n > 1.0f) {
            TLog.w("%s setScale need 0 < scaleWithWidth <= 1, %f", "SelesWatermarkImpl", n);
            return;
        }
        this.e = n;
        this.g = true;
    }
    
    @Override
    public void setPadding(final float n) {
        if (n > 0.0f || n > 1.0f) {
            TLog.w("%s setPadding need 0 <= paddingWithWidth < 1, %f", "SelesWatermarkImpl", n);
            return;
        }
        this.f = n;
        this.g = true;
    }
    
    public SelesWatermarkImpl(final boolean c) {
        this.a = SelesFilter.buildBuffer(SelesWatermarkImpl.imageVertices);
        this.c = false;
        this.d = TuSdkWaterMarkOption.WaterMarkPosition.TopRight;
        this.e = 0.12f;
        this.f = 0.02f;
        this.g = false;
        this.useNextFrameForImageCapture();
        this.c = c;
        this.b = SelesFilter.buildBuffer(this.c ? SelesSurfacePusher.noRotationTextureCoordinates : SelesFilter.noRotationTextureCoordinates);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.checkGLError("SelesWatermarkImpl onInitOnGLThread");
    }
    
    @Override
    public void setImage(final Bitmap bitmap, final boolean b) {
        if (bitmap == null || bitmap.isRecycled()) {
            TLog.w("%s setImage is Null or Recycled, %s", "SelesWatermarkImpl", bitmap);
            return;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                final TuSdkSize sizeThatFitsWithinATexture = SelesContext.sizeThatFitsWithinATexture(TuSdkSize.create(bitmap));
                if (sizeThatFitsWithinATexture == null || !sizeThatFitsWithinATexture.isSize()) {
                    TLog.w("%s setImage is too small, %s", "SelesWatermarkImpl", bitmap);
                    return;
                }
                SelesWatermarkImpl.this.removeOutputFramebuffer();
                SelesWatermarkImpl.this.mFirstInputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE, sizeThatFitsWithinATexture);
                SelesWatermarkImpl.this.mFirstInputFramebuffer.bindTexture(bitmap, SelesWatermarkImpl.this.isShouldSmoothlyScaleOutput(), b);
                SelesWatermarkImpl.this.g = true;
            }
        });
    }
    
    @Override
    public void removeOutputFramebuffer() {
        if (this.mFirstInputFramebuffer != null) {
            this.mFirstInputFramebuffer.enableReferenceCounting();
            this.mFirstInputFramebuffer.unlock();
            this.mFirstInputFramebuffer = null;
        }
        super.removeOutputFramebuffer();
    }
    
    @Override
    protected void onDestroy() {
        this.removeOutputFramebuffer();
        super.onDestroy();
    }
    
    @Override
    public void setInputRotation(final ImageOrientation imageOrientation, final int n) {
        if (imageOrientation == null || this.mInputRotation == imageOrientation) {
            return;
        }
        this.mInputRotation = ImageOrientation.getValue(this.mInputRotation.getDegree(), false);
        this.b.clear();
        if (this.c) {
            this.b.put(SelesSurfacePusher.textureCoordinates(this.mInputRotation)).position(0);
        }
        else {
            this.b.put(SelesFilter.textureCoordinates(this.mInputRotation)).position(0);
        }
        this.g = true;
    }
    
    @Override
    public void setInputSize(final TuSdkSize mInputTextureSize, final int n) {
        if (mInputTextureSize == null || this.mInputTextureSize.equals(mInputTextureSize)) {
            return;
        }
        this.mInputTextureSize = mInputTextureSize;
        this.g = true;
    }
    
    @Override
    public void drawInGLThread(final long n, final TuSdkSize tuSdkSize, final ImageOrientation imageOrientation) {
        this.setInputSize(tuSdkSize, 0);
        this.setInputRotation(imageOrientation, 0);
        this.newFrameReady(n, 0);
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        if (!this.isEnabled()) {
            return;
        }
        this.renderToTexture(this.a, this.b);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (this.mFirstInputFramebuffer == null) {
            return;
        }
        this.a();
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(772, 771);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, (this.mFirstInputFramebuffer == null) ? 0 : this.mFirstInputFramebuffer.getTexture());
        GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
        this.checkGLError("SelesWatermarkImpl");
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, (Buffer)floatBuffer);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)floatBuffer2);
        GLES20.glDrawArrays(5, 0, 4);
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
        GLES20.glDisable(3042);
    }
    
    private void a() {
        if (!this.g || this.mFirstInputFramebuffer == null) {
            return;
        }
        this.g = false;
        final TuSdkSize size = this.mFirstInputFramebuffer.getSize();
        final float ratioFloat = size.getRatioFloat();
        final float diagonal = size.diagonal();
        final TuSdkSize transforOrientation = this.mInputTextureSize.transforOrientation(this.mInputRotation);
        final float diagonal2 = this.mInputTextureSize.diagonal();
        if (diagonal == 0.0f || diagonal2 == 0.0f) {
            return;
        }
        final float n = diagonal2 * this.e / transforOrientation.width;
        final float n2 = n / ratioFloat * transforOrientation.getRatioFloat();
        final float n3 = diagonal2 * this.f / transforOrientation.width;
        final float n4 = diagonal2 * this.f / transforOrientation.height;
        final RectF rectF = new RectF();
        switch (this.d.ordinal()) {
            case 1: {
                rectF.set(n3, n4, n + n3, n2 + n4);
                break;
            }
            case 2: {
                rectF.set(1.0f - n - n3, n4, 1.0f - n3, n2 + n4);
                break;
            }
            case 3: {
                rectF.set(n3, 1.0f - n2 - n4, n + n3, 1.0f - n4);
                break;
            }
            case 4: {
                rectF.set(1.0f - n - n3, 1.0f - n2 - n4, 1.0f - n3, 1.0f - n4);
                break;
            }
            case 5: {
                rectF.set(0.5f - n * 0.5f, 0.5f - n2 * 0.5f, 0.5f + n * 0.5f, 0.5f + n2 * 0.5f);
                break;
            }
        }
        float[] src;
        if (this.c) {
            src = RectHelper.displayVertices(this.mInputRotation, rectF);
        }
        else {
            src = RectHelper.textureVertices(this.mInputRotation, rectF);
        }
        this.a.put(src).position(0);
    }
}
