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

public class TuSDKMonsterFace extends SelesFilter implements SelesParameters.FilterFacePositionInterface
{
    public static final int TuSDKMonsterFaceThickLipsType = 1;
    public static final int TuSDKMonsterPapayaFaceType = 2;
    public static final int TuSDKMonsterSmallEyesType = 3;
    private static int[] l;
    boolean a;
    FaceAligment[] b;
    List<MonsterFaceInfo> c;
    FloatBuffer d;
    FloatBuffer e;
    IntBuffer f;
    private int m;
    float[] g;
    float[] h;
    int[] i;
    int j;
    int k;
    
    public TuSDKMonsterFace() {
        this.m = 1;
        this.g = new float[] { -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f };
        this.h = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        this.i = new int[] { 0, 1, 2, 1, 2, 3 };
        this.j = 4;
        this.k = 2;
        this.c = new ArrayList<MonsterFaceInfo>();
        this.d = ByteBuffer.allocateDirect((this.j + 610) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.e = ByteBuffer.allocateDirect((this.j + 610) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.f = ByteBuffer.allocateDirect((this.k + 1080) * 4 * 3).order(ByteOrder.nativeOrder()).asIntBuffer();
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    public void setMonsterFaceType(final int m) {
        this.m = m;
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
            final MonsterFaceInfo monsterFaceInfo = new MonsterFaceInfo(array[i]);
            if (monsterFaceInfo != null) {
                this.c.add(monsterFaceInfo);
            }
        }
        if (this.c.size() == 0) {
            this.a = false;
            return;
        }
        final float[] src3 = new float[this.g.length + 122 * this.c.size()];
        final float[] src4 = new float[this.h.length + 122 * this.c.size()];
        final int[] src5 = new int[this.i.length + TuSDKMonsterFace.l.length * this.c.size()];
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
            final MonsterFaceInfo monsterFaceInfo2 = this.c.get(n4);
            final float[] a2 = monsterFaceInfo2.a(false);
            for (int n5 = 0; n5 < a2.length; ++n5) {
                src4[n2++] = a2[n5];
            }
            switch (this.m) {
                case 2: {
                    monsterFaceInfo2.c();
                    break;
                }
                case 3: {
                    monsterFaceInfo2.a();
                    break;
                }
                default: {
                    monsterFaceInfo2.b();
                    break;
                }
            }
            monsterFaceInfo2.a();
            final float[] a3 = monsterFaceInfo2.a(true);
            for (int n6 = 0; n6 < a3.length; ++n6) {
                src3[n++] = a3[n6];
            }
            final int n7 = this.j + n4 * 61;
            for (int n8 = 0; n8 < TuSDKMonsterFace.l.length; ++n8) {
                src5[n3++] = TuSDKMonsterFace.l[n8] + n7;
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
            return;
        }
        this.b = b;
        this.a = true;
    }
    
    static {
        TuSDKMonsterFace.l = new int[] { 0, 1, 12, 0, 9, 12, 1, 2, 13, 1, 12, 13, 2, 3, 14, 2, 13, 14, 3, 4, 16, 3, 14, 15, 3, 15, 16, 4, 5, 16, 5, 6, 18, 5, 16, 17, 5, 17, 18, 6, 7, 19, 6, 18, 19, 7, 8, 20, 7, 19, 20, 8, 10, 20, 9, 11, 24, 9, 12, 21, 9, 21, 22, 9, 22, 24, 10, 11, 31, 10, 20, 28, 10, 28, 29, 10, 29, 31, 11, 24, 26, 11, 26, 35, 11, 31, 33, 11, 33, 35, 12, 13, 38, 12, 21, 36, 12, 36, 38, 13, 14, 47, 13, 37, 38, 13, 37, 45, 13, 45, 47, 14, 15, 47, 15, 16, 55, 15, 47, 55, 16, 17, 57, 16, 55, 56, 16, 56, 57, 17, 18, 48, 17, 48, 57, 18, 19, 48, 19, 46, 48, 19, 20, 43, 19, 41, 43, 19, 41, 46, 20, 28, 40, 20, 40, 43, 21, 22, 23, 21, 23, 36, 22, 23, 25, 22, 24, 25, 23, 25, 36, 24, 25, 26, 25, 26, 27, 25, 27, 36, 26, 27, 35, 27, 35, 36, 28, 29, 30, 28, 30, 40, 29, 30, 32, 29, 31, 32, 30, 32, 40, 31, 32, 33, 32, 33, 34, 32, 34, 40, 33, 34, 35, 34, 35, 40, 35, 36, 39, 35, 39, 45, 35, 40, 42, 35, 42, 46, 35, 44, 45, 35, 44, 46, 36, 37, 38, 36, 37, 39, 37, 39, 45, 40, 41, 42, 40, 41, 43, 41, 42, 46, 44, 45, 49, 44, 46, 51, 44, 49, 50, 44, 50, 51, 45, 47, 49, 46, 48, 51, 47, 49, 52, 47, 52, 58, 47, 55, 58, 48, 51, 54, 48, 54, 60, 48, 57, 60, 49, 50, 52, 50, 51, 54, 50, 52, 53, 50, 53, 54, 52, 53, 58, 53, 54, 60, 53, 58, 59, 53, 59, 60, 55, 56, 58, 56, 57, 60, 56, 58, 59, 56, 59, 60 };
    }
    
    private class MonsterFaceInfo
    {
        PointF a;
        PointF b;
        PointF c;
        PointF d;
        PointF e;
        PointF f;
        PointF g;
        PointF h;
        PointF[] i;
        PointF[] j;
        PointF[] k;
        PointF[] l;
        PointF[] m;
        PointF[] n;
        PointF[] o;
        PointF[] p;
        
        private MonsterFaceInfo(final FaceAligment faceAligment) {
            this.i = new PointF[12];
            this.j = new PointF[9];
            this.k = new PointF[7];
            this.l = new PointF[7];
            this.m = new PointF[4];
            this.n = new PointF[4];
            this.o = new PointF[3];
            this.p = new PointF[14];
            if (faceAligment != null) {
                this.a(faceAligment.getOrginMarks());
            }
        }
        
        private float[] a(final boolean b) {
            final float[] array = new float[122];
            int n = 0;
            for (int i = 0; i < 12; ++i) {
                if (b) {
                    array[n++] = this.i[i].x * 2.0f - 1.0f;
                    array[n++] = this.i[i].y * 2.0f - 1.0f;
                }
                else {
                    array[n++] = this.i[i].x;
                    array[n++] = this.i[i].y;
                }
            }
            for (int j = 0; j < 9; ++j) {
                if (b) {
                    array[n++] = this.j[j].x * 2.0f - 1.0f;
                    array[n++] = this.j[j].y * 2.0f - 1.0f;
                }
                else {
                    array[n++] = this.j[j].x;
                    array[n++] = this.j[j].y;
                }
            }
            for (int k = 0; k < 7; ++k) {
                if (b) {
                    array[n++] = this.k[k].x * 2.0f - 1.0f;
                    array[n++] = this.k[k].y * 2.0f - 1.0f;
                }
                else {
                    array[n++] = this.k[k].x;
                    array[n++] = this.k[k].y;
                }
            }
            for (int l = 0; l < 7; ++l) {
                if (b) {
                    array[n++] = this.l[l].x * 2.0f - 1.0f;
                    array[n++] = this.l[l].y * 2.0f - 1.0f;
                }
                else {
                    array[n++] = this.l[l].x;
                    array[n++] = this.l[l].y;
                }
            }
            if (b) {
                array[n++] = this.b.x * 2.0f - 1.0f;
                array[n++] = this.b.y * 2.0f - 1.0f;
            }
            else {
                array[n++] = this.b.x;
                array[n++] = this.b.y;
            }
            for (int n2 = 0; n2 < 4; ++n2) {
                if (b) {
                    array[n++] = this.m[n2].x * 2.0f - 1.0f;
                    array[n++] = this.m[n2].y * 2.0f - 1.0f;
                }
                else {
                    array[n++] = this.m[n2].x;
                    array[n++] = this.m[n2].y;
                }
            }
            for (int n3 = 0; n3 < 4; ++n3) {
                if (b) {
                    array[n++] = this.n[n3].x * 2.0f - 1.0f;
                    array[n++] = this.n[n3].y * 2.0f - 1.0f;
                }
                else {
                    array[n++] = this.n[n3].x;
                    array[n++] = this.n[n3].y;
                }
            }
            for (int n4 = 0; n4 < 3; ++n4) {
                if (b) {
                    array[n++] = this.o[n4].x * 2.0f - 1.0f;
                    array[n++] = this.o[n4].y * 2.0f - 1.0f;
                }
                else {
                    array[n++] = this.o[n4].x;
                    array[n++] = this.o[n4].y;
                }
            }
            for (int n5 = 0; n5 < 14; ++n5) {
                if (b) {
                    array[n++] = this.p[n5].x * 2.0f - 1.0f;
                    array[n++] = this.p[n5].y * 2.0f - 1.0f;
                }
                else {
                    array[n++] = this.p[n5].x;
                    array[n++] = this.p[n5].y;
                }
            }
            return array;
        }
        
        private void a() {
            final float n = 0.55f;
            this.m[0] = PointCalc.increasePercentage(this.c, this.m[0], -n);
            this.m[1] = PointCalc.increasePercentage(this.c, this.m[1], -n);
            this.n[0] = PointCalc.increasePercentage(this.d, this.n[0], -n);
            this.n[1] = PointCalc.increasePercentage(this.d, this.n[1], -n);
        }
        
        private void b() {
            final float n = 0.55f;
            this.m[0] = PointCalc.increasePercentage(this.c, this.m[0], -n);
            this.m[1] = PointCalc.increasePercentage(this.c, this.m[1], -n);
            this.n[0] = PointCalc.increasePercentage(this.d, this.n[0], -n);
            this.n[1] = PointCalc.increasePercentage(this.d, this.n[1], -n);
            this.p[0] = PointCalc.increasePercentage(this.h, this.p[0], n);
            this.p[1] = PointCalc.increasePercentage(this.h, this.p[1], n);
            this.p[2] = PointCalc.increasePercentage(this.h, this.p[2], n * 1.3f);
            this.p[3] = PointCalc.increasePercentage(this.h, this.p[3], n * 1.3f);
            this.p[4] = PointCalc.increasePercentage(this.h, this.p[4], n * 1.3f);
            this.p[5] = PointCalc.increasePercentage(this.h, this.p[5], n * 1.3f);
            this.p[6] = PointCalc.increasePercentage(this.h, this.p[6], n * 1.3f);
            this.p[7] = PointCalc.increasePercentage(this.h, this.p[7], n * 1.3f);
            this.p[8] = PointCalc.increasePercentage(this.h, this.p[8], n * 1.3f);
            this.p[9] = PointCalc.increasePercentage(this.h, this.p[9], n * 1.3f);
            this.p[10] = PointCalc.increasePercentage(this.h, this.p[10], n * 1.3f);
            this.p[11] = PointCalc.increasePercentage(this.h, this.p[11], n * 1.3f);
            this.p[12] = PointCalc.increasePercentage(this.h, this.p[12], n * 1.3f);
            this.p[13] = PointCalc.increasePercentage(this.h, this.p[13], n * 1.3f);
            this.o[0] = PointCalc.increasePercentage(this.b, this.o[0], -n * 0.2f);
            this.o[1] = PointCalc.increasePercentage(this.j[1], this.o[1], -n * 0.2f);
            this.o[2] = PointCalc.increasePercentage(this.j[7], this.o[2], -n * 0.2f);
            final float n2 = n * 0.3f;
            this.j[0] = PointCalc.increasePercentage(this.a, this.j[0], n2 * 0.1f);
            this.j[1] = PointCalc.increasePercentage(this.a, this.j[1], n2 * 0.5f);
            this.j[2] = PointCalc.increasePercentage(this.a, this.j[2], n2 * 1.0f);
            this.j[3] = PointCalc.increasePercentage(this.a, this.j[3], n2 * 1.0f);
            this.j[4] = PointCalc.increasePercentage(this.a, this.j[4], n2 * 1.0f);
            this.j[5] = PointCalc.increasePercentage(this.a, this.j[5], n2 * 1.0f);
            this.j[6] = PointCalc.increasePercentage(this.a, this.j[6], n2 * 1.0f);
            this.j[7] = PointCalc.increasePercentage(this.a, this.j[7], n2 * 0.5f);
            this.j[8] = PointCalc.increasePercentage(this.a, this.j[8], n2 * 0.1f);
        }
        
        private void c() {
            final float n = 0.29f;
            this.m[0] = PointCalc.increasePercentage(this.c, this.m[0], n * 1.5f);
            this.m[1] = PointCalc.increasePercentage(this.c, this.m[1], n * 1.5f);
            this.m[2] = PointCalc.increasePercentage(this.c, this.m[2], n * 0.5f);
            this.m[3] = PointCalc.increasePercentage(this.c, this.m[3], n * 0.5f);
            this.n[0] = PointCalc.increasePercentage(this.d, this.n[0], n * 1.5f);
            this.n[1] = PointCalc.increasePercentage(this.d, this.n[1], n * 1.5f);
            this.n[2] = PointCalc.increasePercentage(this.d, this.n[2], n * 0.5f);
            this.n[3] = PointCalc.increasePercentage(this.d, this.n[3], n * 0.5f);
            this.p[0] = PointCalc.increasePercentage(this.a, this.p[0], n);
            this.p[1] = PointCalc.increasePercentage(this.a, this.p[1], n);
            this.p[2] = PointCalc.increasePercentage(this.a, this.p[2], n);
            this.p[3] = PointCalc.increasePercentage(this.a, this.p[3], n);
            this.p[4] = PointCalc.increasePercentage(this.a, this.p[4], n);
            this.p[5] = PointCalc.increasePercentage(this.a, this.p[5], n);
            this.p[6] = PointCalc.increasePercentage(this.a, this.p[6], n);
            this.p[7] = PointCalc.increasePercentage(this.a, this.p[7], n);
            this.p[8] = PointCalc.increasePercentage(this.a, this.p[8], n);
            this.p[9] = PointCalc.increasePercentage(this.a, this.p[9], n);
            this.p[10] = PointCalc.increasePercentage(this.a, this.p[10], n);
            this.p[11] = PointCalc.increasePercentage(this.a, this.p[11], n);
            this.p[12] = PointCalc.increasePercentage(this.a, this.p[12], n);
            this.p[13] = PointCalc.increasePercentage(this.a, this.p[13], n);
            this.o[0] = PointCalc.increasePercentage(this.a, this.o[0], n * 1.5f);
            this.o[1] = PointCalc.increasePercentage(this.a, this.o[1], n * 1.5f);
            this.o[2] = PointCalc.increasePercentage(this.a, this.o[2], n * 1.5f);
        }
        
        private void a(final PointF[] array) {
            if (array == null || array.length < 3) {
                return;
            }
            this.a = new PointF(array[46].x, array[46].y);
            final int[] array2 = { 72, 73, 52, 55 };
            final int[] array3 = { 75, 76, 58, 61 };
            this.c = PointCalc.crossPoint(array[52], array[55], array[72], array[73]);
            this.d = PointCalc.crossPoint(array[58], array[61], array[75], array[76]);
            for (int i = 0; i < 4; ++i) {
                this.m[i] = new PointF(array[array2[i]].x, array[array2[i]].y);
            }
            for (int j = 0; j < 4; ++j) {
                this.n[j] = new PointF(array[array3[j]].x, array[array3[j]].y);
            }
            final int[] array4 = { 49, 82, 83 };
            this.g = new PointF(array[45].x, array[45].y);
            for (int k = 0; k < 3; ++k) {
                this.o[k] = new PointF(array[array4[k]].x, array[array4[k]].y);
            }
            final int[] array5 = { 84, 90, 86, 87, 88, 97, 98, 99, 94, 93, 92, 103, 102, 101 };
            this.h = PointCalc.crossPoint(array[84], array[90], array[87], array[93]);
            for (int l = 0; l < 14; ++l) {
                this.p[l] = new PointF(array[array5[l]].x, array[array5[l]].y);
            }
            final int[] array6 = { 33, 34, 64, 35, 65, 37, 67 };
            final int[] array7 = { 42, 41, 71, 40, 70, 38, 68 };
            this.b = PointCalc.crossPoint(array[37], array[79], array[38], array[78]);
            this.e = PointCalc.crossPoint(array[33], array[37], array[35], array[65]);
            this.f = PointCalc.crossPoint(array[38], array[42], array[40], array[70]);
            for (int n = 0; n < 7; ++n) {
                this.k[n] = new PointF(array[array6[n]].x, array[array6[n]].y);
            }
            for (int n2 = 0; n2 < 7; ++n2) {
                this.l[n2] = new PointF(array[array7[n2]].x, array[array7[n2]].y);
            }
            final int[] array8 = { 0, 4, 8, 12, 16, 20, 24, 28, 32 };
            for (int n3 = 0; n3 < 9; ++n3) {
                this.j[n3] = new PointF(array[array8[n3]].x, array[array8[n3]].y);
            }
            this.j[3] = PointCalc.increasePercentage(this.a, this.j[3], 0.05f);
            this.j[4] = PointCalc.increasePercentage(this.a, this.j[4], 0.1f);
            this.j[5] = PointCalc.increasePercentage(this.a, this.j[5], 0.05f);
            for (int n4 = 0; n4 < 9; ++n4) {
                this.i[n4] = new PointF(array[array8[n4]].x, array[array8[n4]].y);
            }
            this.i[9] = array[34];
            this.i[10] = array[41];
            this.i[11] = PointCalc.center(array[35], array[40]);
            PointCalc.scalePoint(this.i, this.a, 0.4f);
        }
    }
}
