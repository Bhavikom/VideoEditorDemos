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

public class TuSDKMonsterSquareFace extends SelesFilter implements SelesParameters.FilterFacePositionInterface
{
    private static final int[] l;
    boolean a;
    FaceAligment[] b;
    List<MonsterSquareFaceInfo> c;
    FloatBuffer d;
    FloatBuffer e;
    IntBuffer f;
    float[] g;
    float[] h;
    int[] i;
    int j;
    int k;
    
    public TuSDKMonsterSquareFace() {
        this.g = new float[] { -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f };
        this.h = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        this.i = new int[] { 0, 1, 2, 1, 2, 3 };
        this.j = 4;
        this.k = 2;
        this.a();
        this.c = new ArrayList<MonsterSquareFaceInfo>();
        this.d = ByteBuffer.allocateDirect((this.j + 70) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.e = ByteBuffer.allocateDirect((this.j + 70) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.f = ByteBuffer.allocateDirect((this.k + 70) * 4 * 3).order(ByteOrder.nativeOrder()).asIntBuffer();
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
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, (Buffer)this.d);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)this.e);
        this.checkGLError(this.getClass().getSimpleName() + " bindFramebuffer");
        GLES20.glDrawElements(4, this.f.limit(), 5125, (Buffer)this.f);
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
        final FaceAligment[] array = b;
        for (int length = array.length, i = 0; i < length; ++i) {
            final MonsterSquareFaceInfo monsterSquareFaceInfo = new MonsterSquareFaceInfo(array[i]);
            if (monsterSquareFaceInfo != null) {
                this.c.add(monsterSquareFaceInfo);
            }
        }
        if (this.c.size() == 0) {
            this.a = false;
            return;
        }
        final float[] src3 = new float[this.g.length + 14 * this.c.size()];
        final float[] src4 = new float[this.h.length + 14 * this.c.size()];
        final int[] src5 = new int[this.i.length + TuSDKMonsterSquareFace.l.length * this.c.size()];
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        for (int j = 0; j < this.g.length; ++j) {
            src3[n++] = this.g[j];
        }
        for (int k = 0; k < this.h.length; ++k) {
            src4[n2++] = this.h[k];
        }
        final int[] array2 = { 0, 1, 2, 0, 3, 2 };
        for (int l = 0; l < array2.length; ++l) {
            src5[n3++] = array2[l];
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
            final int n7 = this.j + n4 * 7;
            for (int n8 = 0; n8 < TuSDKMonsterSquareFace.l.length; ++n8) {
                src5[n3++] = TuSDKMonsterSquareFace.l[n8] + n7;
            }
        }
        this.d.put(src3).position(0).limit(src3.length);
        this.e.put(src4).position(0).limit(src4.length);
        this.f.put(src5).position(0).limit(src5.length);
    }
    
    private void a() {
    }
    
    @Override
    public void updateFaceFeatures(final FaceAligment[] b, final float n) {
        if (b == null || b.length < 1) {
            this.a = false;
            this.b = null;
            return;
        }
        this.b = b;
        this.a = true;
    }
    
    static {
        l = new int[] { 0, 2, 3, 0, 3, 4, 1, 4, 5, 1, 5, 6, 2, 3, 4, 2, 4, 6, 4, 5, 6 };
    }
    
    private class MonsterSquareFaceInfo
    {
        PointF a;
        PointF[] b;
        PointF[] c;
        PointF d;
        PointF e;
        
        private MonsterSquareFaceInfo(final FaceAligment faceAligment) {
            this.b = new PointF[2];
            this.c = new PointF[5];
            if (faceAligment != null) {
                this.a(faceAligment.getOrginMarks());
            }
        }
        
        private float[] a() {
            final float[] array = new float[14];
            int n = 0;
            for (int i = 0; i < 2; ++i) {
                array[n++] = this.b[i].x * 2.0f - 1.0f;
                array[n++] = this.b[i].y * 2.0f - 1.0f;
            }
            for (int j = 0; j < 5; ++j) {
                array[n++] = this.c[j].x * 2.0f - 1.0f;
                array[n++] = this.c[j].y * 2.0f - 1.0f;
            }
            return array;
        }
        
        private float[] b() {
            final float[] array = new float[14];
            int n = 0;
            for (int i = 0; i < 2; ++i) {
                array[n++] = this.b[i].x;
                array[n++] = this.b[i].y;
            }
            for (int j = 0; j < 5; ++j) {
                array[n++] = this.c[j].x;
                array[n++] = this.c[j].y;
            }
            return array;
        }
        
        private void c() {
            this.c[1].x = this.d.x;
            this.c[1].y = this.d.y;
            this.c[3].x = this.e.x;
            this.c[3].y = this.e.y;
        }
        
        private void a(final PointF[] array) {
            if (array == null || array.length < 3) {
                return;
            }
            this.a = new PointF(array[46].x, array[46].y);
            this.c[0] = new PointF(array[0].x, array[0].y);
            this.c[1] = new PointF(array[8].x, array[8].y);
            this.c[2] = new PointF(array[16].x, array[16].y);
            this.c[3] = new PointF(array[24].x, array[24].y);
            this.c[4] = new PointF(array[32].x, array[32].y);
            final float abs = Math.abs(PointCalc.distance(this.c[0], this.c[4]));
            final float abs2 = Math.abs(PointCalc.distance(this.c[1], this.c[3]));
            final float n = -(abs - abs2) / abs2;
            final PointF crossPoint = PointCalc.crossPoint(array[16], array[43], this.c[1], this.c[3]);
            this.d = PointCalc.pointOfPercentage(this.c[1], crossPoint, n);
            this.e = PointCalc.pointOfPercentage(this.c[3], crossPoint, n);
            this.d = PointCalc.pointOfPercentage(this.d, crossPoint, 0.05f);
            this.e = PointCalc.pointOfPercentage(this.e, crossPoint, 0.05f);
            this.b[0] = PointCalc.pointOfPercentage(this.d, this.a, -0.2f);
            this.b[1] = PointCalc.pointOfPercentage(this.e, this.a, -0.2f);
        }
    }
}
