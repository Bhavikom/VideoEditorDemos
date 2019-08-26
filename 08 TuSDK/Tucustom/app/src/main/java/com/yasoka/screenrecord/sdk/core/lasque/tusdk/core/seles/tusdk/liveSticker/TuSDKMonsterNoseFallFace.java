// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker;

//import org.lasque.tusdk.core.utils.calc.PointCalc;
import android.graphics.PointF;
//import org.lasque.tusdk.core.seles.SelesFramebufferCache;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebufferCache;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.calc.PointCalc;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.util.List;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKMonsterNoseFallFace extends SelesFilter implements SelesParameters.FilterFacePositionInterface
{
    private final int l = 10;
    private final int m = 12;
    private final int n = 9;
    private final int o = 4;
    private final int p = 25;
    private final int q = 37;
    private final int[] r;
    boolean a;
    FaceAligment[] b;
    List<MonsterNoseFallFaceInfo> c;
    FloatBuffer d;
    FloatBuffer e;
    IntBuffer f;
    float[] g;
    float[] h;
    int[] i;
    int j;
    int k;
    
    public TuSDKMonsterNoseFallFace() {
        this.r = new int[] { 0, 1, 12, 0, 9, 12, 1, 2, 13, 1, 12, 13, 2, 3, 14, 2, 13, 14, 3, 4, 15, 3, 14, 15, 4, 5, 17, 4, 15, 16, 4, 16, 17, 5, 6, 18, 5, 17, 18, 6, 7, 19, 6, 18, 19, 7, 8, 20, 7, 19, 20, 8, 10, 20, 9, 11, 21, 9, 12, 21, 10, 11, 21, 10, 20, 21, 12, 13, 21, 9, 11, 21, 13, 21, 23, 13, 14, 23, 14, 22, 23, 14, 15, 22, 15, 16, 22, 16, 17, 22, 17, 18, 22, 18, 19, 24, 18, 22, 24, 19, 20, 21, 19, 21, 24, 21, 23, 24, 22, 23, 24 };
        this.g = new float[] { -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f };
        this.h = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        this.i = new int[] { 0, 1, 2, 1, 2, 3 };
        this.j = 4;
        this.k = 2;
        this.c = new ArrayList<MonsterNoseFallFaceInfo>();
        this.d = ByteBuffer.allocateDirect((this.j + 250) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.e = ByteBuffer.allocateDirect((this.j + 250) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.f = ByteBuffer.allocateDirect((this.k + 370) * 4 * 3).order(ByteOrder.nativeOrder()).asIntBuffer();
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (this.isPreventRendering()) {
            this.inputFramebufferUnlock();
            return;
        }
        this.a(floatBuffer, floatBuffer2);
        SelesContext.setActiveShaderProgram(this.mFilterProgram);
        final TuSdkSize sizeOfFBO = this.sizeOfFBO();
        final SelesFramebufferCache sharedFramebufferCache = SelesContext.sharedFramebufferCache();
        if (sharedFramebufferCache == null) {
            return;
        }
        (this.mOutputFramebuffer = sharedFramebufferCache.fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, sizeOfFBO, this.getOutputTextureOptions())).activateFramebuffer();
        this.checkGLError(this.getClass().getSimpleName() + " activateFramebuffer");
        if (this.mUsingNextFrameForImageCapture) {
            this.mOutputFramebuffer.lock();
        }
        this.setUniformsForProgramAtIndex(0);
        GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
        GLES20.glClear(16384);
        this.inputFramebufferBindTexture();
        this.checkGLError(this.getClass().getSimpleName() + " bindFramebuffer");
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, (Buffer)this.d);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)this.e);
        GLES20.glDrawElements(4, this.f.limit(), 5125, (Buffer)this.f);
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
        this.inputFramebufferUnlock();
        this.cacaptureImageBuffer();
    }
    
    private void a(final FloatBuffer src, final FloatBuffer src2) {
        final boolean a = this.a;
        final FaceAligment[] b = this.b;
        this.d.clear();
        this.e.clear();
        this.f.clear();
        if (!a || b == null) {
            src.position(0);
            this.d.put(src).position(0);
            src2.position(0);
            this.e.put(src2).position(0);
            this.f.put(this.i).position(0).limit(this.i.length);
            return;
        }
        this.c.clear();
        final FaceAligment[] b2 = this.b;
        for (int length = b2.length, i = 0; i < length; ++i) {
            final MonsterNoseFallFaceInfo monsterNoseFallFaceInfo = new MonsterNoseFallFaceInfo(b2[i]);
            if (monsterNoseFallFaceInfo != null) {
                this.c.add(monsterNoseFallFaceInfo);
            }
        }
        if (this.c.size() == 0) {
            this.a = false;
            return;
        }
        final float[] src3 = new float[this.g.length + 50 * this.c.size()];
        final float[] src4 = new float[this.h.length + 50 * this.c.size()];
        final int[] src5 = new int[this.i.length + this.r.length * this.c.size()];
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        for (int j = 0; j < this.g.length; ++j) {
            src3[n++] = this.g[j];
        }
        for (int k = 0; k < this.h.length; ++k) {
            src4[n2++] = this.h[k];
        }
        final int[] array = { 0, 1, 2, 0, 3, 2 };
        for (int l = 0; l < array.length; ++l) {
            src5[n3++] = array[l];
        }
        for (int n4 = 0; n4 < this.c.size(); ++n4) {
            final float[] a2 = this.c.get(n4).b();
            for (int n5 = 0; n5 < a2.length; ++n5) {
                src4[n2++] = a2[n5];
            }
            this.c.get(n4).c();
            final float[] c = this.c.get(n4).a();
            for (int n6 = 0; n6 < c.length; ++n6) {
                src3[n++] = c[n6];
            }
            final int n7 = this.j + n4 * 25;
            for (int n8 = 0; n8 < this.r.length; ++n8) {
                src5[n3++] = this.r[n8] + n7;
            }
        }
        this.d.put(src3).position(0).limit(src3.length);
        this.e.put(src4).position(0).limit(src4.length);
        this.f.put(src5).position(0).limit(src5.length);
    }
    
    @Override
    public void updateFaceFeatures(final FaceAligment[] b, final float n) {
        if (b == null || b.length < 1) {
            this.a = false;
            this.b = null;
            this.c.clear();
            return;
        }
        this.b = b;
        this.a = true;
    }
    
    private class MonsterNoseFallFaceInfo
    {
        PointF a;
        PointF[] b;
        PointF[] c;
        PointF[] d;
        PointF e;
        
        private MonsterNoseFallFaceInfo(final FaceAligment faceAligment) {
            this.b = new PointF[12];
            this.c = new PointF[9];
            this.d = new PointF[4];
            if (faceAligment != null) {
                this.a(faceAligment.getOrginMarks());
            }
        }
        
        private void a(final PointF[] array) {
            if (array == null || array.length < 3) {
                return;
            }
            this.a = new PointF(array[46].x, array[46].y);
            final int[] array2 = { 0, 4, 8, 12, 16, 20, 24, 28, 32 };
            for (int i = 0; i < 9; ++i) {
                this.c[i] = new PointF(array[array2[i]].x, array[array2[i]].y);
            }
            for (int j = 0; j < 9; ++j) {
                this.b[j] = new PointF(array[array2[j]].x, array[array2[j]].y);
            }
            this.b[9] = new PointF(array[34].x, array[34].y);
            this.b[10] = new PointF(array[41].x, array[41].y);
            this.b[11] = PointCalc.center(array[35], array[40]);
            PointCalc.scalePoint(this.b, this.a, 0.5f);
            final int[] array3 = { 45, 49, 82, 83 };
            for (int k = 0; k < 4; ++k) {
                this.d[k] = new PointF(array[array3[k]].x, array[array3[k]].y);
            }
            this.e = PointCalc.crossPoint(this.d[0], this.d[1], this.d[2], this.d[3]);
        }
        
        private float[] a() {
            final float[] array = new float[50];
            int n = 0;
            for (int i = 0; i < 12; ++i) {
                array[n++] = this.b[i].x * 2.0f - 1.0f;
                array[n++] = this.b[i].y * 2.0f - 1.0f;
            }
            for (int j = 0; j < 9; ++j) {
                array[n++] = this.c[j].x * 2.0f - 1.0f;
                array[n++] = this.c[j].y * 2.0f - 1.0f;
            }
            for (int k = 0; k < 4; ++k) {
                array[n++] = this.d[k].x * 2.0f - 1.0f;
                array[n++] = this.d[k].y * 2.0f - 1.0f;
            }
            return array;
        }
        
        private float[] b() {
            final float[] array = new float[50];
            int n = 0;
            for (int i = 0; i < 12; ++i) {
                array[n++] = this.b[i].x;
                array[n++] = this.b[i].y;
            }
            for (int j = 0; j < 9; ++j) {
                array[n++] = this.c[j].x;
                array[n++] = this.c[j].y;
            }
            for (int k = 0; k < 4; ++k) {
                array[n++] = this.d[k].x;
                array[n++] = this.d[k].y;
            }
            return array;
        }
        
        private void c() {
            this.d[0] = PointCalc.pointOfPercentage(this.d[0], this.e, -0.5f);
            this.d[1] = PointCalc.pointOfPercentage(this.d[1], this.d[0], -0.5f);
            this.d[2] = PointCalc.pointOfPercentage(this.d[2], this.d[0], -0.5f);
            this.d[3] = PointCalc.pointOfPercentage(this.d[3], this.d[0], -0.5f);
            final Float value = -0.2f;
            this.c[0] = PointCalc.pointOfPercentage(this.c[0], this.a, value * 0.25f);
            this.c[1] = PointCalc.pointOfPercentage(this.c[1], this.a, value * 0.75f);
            this.c[2] = PointCalc.pointOfPercentage(this.c[2], this.a, value * 1.0f);
            this.c[3] = PointCalc.pointOfPercentage(this.c[3], this.a, value * 0.75f);
            this.c[4] = PointCalc.pointOfPercentage(this.c[4], this.a, value * 0.4f);
            this.c[5] = PointCalc.pointOfPercentage(this.c[5], this.a, value * 0.75f);
            this.c[6] = PointCalc.pointOfPercentage(this.c[6], this.a, value * 1.0f);
            this.c[7] = PointCalc.pointOfPercentage(this.c[7], this.a, value * 0.75f);
            this.c[8] = PointCalc.pointOfPercentage(this.c[8], this.a, value * 0.25f);
        }
    }
}
