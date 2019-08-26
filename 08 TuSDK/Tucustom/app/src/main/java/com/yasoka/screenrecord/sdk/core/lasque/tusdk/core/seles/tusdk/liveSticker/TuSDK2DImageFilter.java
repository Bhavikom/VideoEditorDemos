// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker;

import android.opengl.Matrix;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
import android.graphics.PointF;
//import org.lasque.tusdk.core.struct.TuSdkSizeF;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesContext;
import android.graphics.RectF;
//import org.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSizeF;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;

import java.util.List;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDK2DImageFilter extends SelesFilter
{
    public static final String TUSDK_MAP_2D_VERTEX_SHADER = "attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;uniform mat4 uMVPMatrix;uniform mat4 uTexMatrix;void main(){    gl_Position = uMVPMatrix * position;\n    textureCoordinate = (uTexMatrix * inputTextureCoordinate).xy;}";
    protected static final float[] stickerVertices;
    private final FloatBuffer a;
    private final FloatBuffer b;
    private int c;
    private int d;
    private final float[] e;
    private final float[] f;
    private List<TuSdkImage2DSticker> g;
    private RectF h;
    private float i;
    protected float mDeviceRadian;
    
    public TuSDK2DImageFilter() {
        this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;uniform mat4 uMVPMatrix;uniform mat4 uTexMatrix;void main(){    gl_Position = uMVPMatrix * position;\n    textureCoordinate = (uTexMatrix * inputTextureCoordinate).xy;}", "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
    }
    
    public TuSDK2DImageFilter(final String s) {
        this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;uniform mat4 uMVPMatrix;uniform mat4 uTexMatrix;void main(){    gl_Position = uMVPMatrix * position;\n    textureCoordinate = (uTexMatrix * inputTextureCoordinate).xy;}", s);
    }
    
    public TuSDK2DImageFilter(final String s, final String s2) {
        super(s, s2);
        this.e = new float[16];
        this.f = new float[16];
        this.mDeviceRadian = 0.0f;
        this.a = SelesFilter.buildBuffer(TuSDK2DImageFilter.stickerVertices);
        this.b = SelesFilter.buildBuffer(TuSDK2DImageFilter.noRotationTextureCoordinates);
    }
    
    public void setDisplayRect(final RectF h, final float i) {
        this.h = h;
        this.i = i;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.c = this.mFilterProgram.uniformIndex("uTexMatrix");
        this.d = this.mFilterProgram.uniformIndex("uMVPMatrix");
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        if (this.mFirstInputFramebuffer == null) {
            return;
        }
        this.b.clear();
        this.b.put(SelesFilter.textureCoordinates(this.mInputRotation)).position(0);
        this.renderToTexture(this.a, this.b);
        this.informTargetsAboutNewFrame(n);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (this.isPreventRendering()) {
            this.inputFramebufferUnlock();
            return;
        }
        SelesContext.setActiveShaderProgram(this.mFilterProgram);
        (this.mOutputFramebuffer = this.mFirstInputFramebuffer).activateFramebuffer();
        if (this.mUsingNextFrameForImageCapture) {
            this.mOutputFramebuffer.lock();
        }
        this.checkGLError(this.getClass().getSimpleName() + " activateFramebuffer");
        this.setUniformsForProgramAtIndex(0);
        if (this.getStickerCount() > 0) {
            GLES20.glEnable(3042);
            this.a(floatBuffer, floatBuffer2);
            GLES20.glDisable(3042);
        }
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
        this.cacaptureImageBuffer();
    }
    
    private void a(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        int n = 0;
        while (n < this.getStickerCount() && n < this.g.size()) {
            final TuSdkImage2DSticker tuSdkImage2DSticker = this.g.get(n);
            if (tuSdkImage2DSticker == null) {
                continue;
            }
            GLES20.glBlendFunc(1, 771);
            this.a(tuSdkImage2DSticker, floatBuffer, floatBuffer2, -1);
            ++n;
        }
    }
    
    private void a(final TuSdkImage2DSticker tuSdkImage2DSticker, final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2, final int n) {
        if (tuSdkImage2DSticker == null || !tuSdkImage2DSticker.isEnabled()) {
            return;
        }
        final TuSdkSize sizeOfFBO = this.sizeOfFBO();
        final float[] src = { -0.5f, -0.5f, 0.0f, 1.0f, 0.5f, -0.5f, 0.0f, 1.0f, -0.5f, 0.5f, 0.0f, 1.0f, 0.5f, 0.5f, 0.0f, 1.0f };
        this.a(tuSdkImage2DSticker, sizeOfFBO);
        floatBuffer.clear();
        floatBuffer.put(src).position(0);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, tuSdkImage2DSticker.getCurrentTextureId());
        GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
        GLES20.glUniformMatrix4fv(this.d, 1, false, this.f, 0);
        GLES20.glUniformMatrix4fv(this.c, 1, false, this.e, 0);
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 4, 5126, false, 0, (Buffer)floatBuffer);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)floatBuffer2);
        GLES20.glDrawArrays(5, 0, 4);
    }
    
    public void updateStickers(final List<TuSdkImage2DSticker> g) {
        this.g = g;
    }
    
    protected int getStickerCount() {
        if (this.g == null) {
            return 0;
        }
        return this.g.size();
    }
    
    private void a(final TuSdkImage2DSticker tuSdkImage2DSticker, final TuSdkSize tuSdkSize) {
        RectF h = this.h;
        final TuSdkSizeF create = TuSdkSizeF.create((float)tuSdkImage2DSticker.getCurrentSticker().getWidth(), (float)tuSdkImage2DSticker.getCurrentSticker().getHeight());
        final PointF pointF = new PointF(0.0f, 0.0f);
        final TuSdkSizeF create2 = TuSdkSizeF.create((float)tuSdkSize.width, (float)tuSdkSize.height);
        if (h == null || h.isEmpty()) {
            if (this.i > 0.0f) {
                final TuSdkSize create3 = TuSdkSize.create(tuSdkSize);
                create3.width = (int)(tuSdkSize.height * this.i);
                final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(create3, new Rect(0, 0, tuSdkSize.width, tuSdkSize.height));
                h = new RectF(rectWithAspectRatioInsideRect.left / (float)tuSdkSize.width, rectWithAspectRatioInsideRect.top / (float)tuSdkSize.height, rectWithAspectRatioInsideRect.right / (float)tuSdkSize.width, rectWithAspectRatioInsideRect.bottom / (float)tuSdkSize.height);
            }
            else {
                h = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
            }
        }
        if (this.i > 0.0f) {
            if (tuSdkSize.width / (float)tuSdkSize.height > this.i) {
                create2.width = tuSdkSize.height * this.i;
                create2.height = (float)tuSdkSize.height;
            }
            else {
                create2.width = (float)tuSdkSize.width;
                create2.height = tuSdkSize.width / this.i;
            }
        }
        else {
            create2.width = tuSdkSize.width * (h.left + h.right);
            create2.height = tuSdkSize.height * (h.top + h.bottom);
        }
        final float a = create2.width / tuSdkImage2DSticker.getDesignScreenSize().width;
        final float b = create2.height / tuSdkImage2DSticker.getDesignScreenSize().height;
        if (a != 1.0f && b != 1.0f) {
            final float min = Math.min(a, b);
            create.width *= min;
            create.height *= min;
        }
        final float n = tuSdkImage2DSticker.getCurrentSticker().getOffsetX() / tuSdkImage2DSticker.getDesignScreenSize().width;
        final float n2 = tuSdkImage2DSticker.getCurrentSticker().getOffsetY() / tuSdkImage2DSticker.getDesignScreenSize().height;
        final float n3 = create2.width * n;
        final float n4 = create2.height * n2;
        pointF.x = create.width / 2.0f + n3;
        pointF.y = create.height / 2.0f + n4;
        this.a(tuSdkSize, create, pointF, tuSdkImage2DSticker.getCurrentSticker().getRotation());
    }
    
    private void a(final TuSdkSize tuSdkSize, final TuSdkSizeF tuSdkSizeF, final PointF pointF, final float n) {
        final float[] array = new float[16];
        Matrix.setIdentityM(array, 0);
        final float[] array2 = new float[16];
        Matrix.setIdentityM(array2, 0);
        Matrix.setIdentityM(this.e, 0);
        Matrix.orthoM(array, 0, 0.0f, (float)tuSdkSize.width, 0.0f, (float)tuSdkSize.height, -1.0f, 1.0f);
        Matrix.translateM(array2, 0, pointF.x, pointF.y, 0.0f);
        if (n != 0.0f) {
            Matrix.rotateM(array2, 0, n, 0.0f, 0.0f, 1.0f);
        }
        Matrix.scaleM(array2, 0, tuSdkSizeF.width, tuSdkSizeF.height, 1.0f);
        Matrix.multiplyMM(this.f, 0, array, 0, array2, 0);
    }
    
    static {
        stickerVertices = new float[] { -0.5f, -0.5f, 0.0f, 1.0f, 0.5f, -0.5f, 0.0f, 1.0f, -0.5f, 0.5f, 0.0f, 1.0f, 0.5f, 0.5f, 0.0f, 1.0f };
    }
}
