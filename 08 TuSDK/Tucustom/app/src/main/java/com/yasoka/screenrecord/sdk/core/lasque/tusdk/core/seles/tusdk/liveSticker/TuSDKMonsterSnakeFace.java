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
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesPointDrawFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.calc.PointCalc;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
//import org.lasque.tusdk.core.seles.filters.SelesPointDrawFilter;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.List;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKMonsterSnakeFace extends SelesFilter implements SelesParameters.FilterFacePositionInterface
{
    public static final int TuSDKMonsterSnakeFaceType = 1;
    public static final int TuSDKMonsterBigFaceType = 2;
    private static final int[] g;
    boolean a;
    FaceAligment[] b;
    List<MonsterSnakeFaceInfo> c;
    FloatBuffer d;
    FloatBuffer e;
    IntBuffer f;
    private int h;
    private int i;
    private float[] j;
    private float[] k;
    private int[] l;
    private int m;
    private SelesPointDrawFilter n;
    private SelesPointDrawFilter o;
    private boolean p;
    
    public TuSDKMonsterSnakeFace() {
        this.h = 4;
        this.i = 2;
        this.j = new float[] { -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f };
        this.k = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f };
        this.l = new int[] { 0, 1, 2, 1, 2, 3 };
        this.m = 2;
        this.p = false;
        this.a();
        this.c = new ArrayList<MonsterSnakeFaceInfo>();
        this.d = ByteBuffer.allocateDirect((this.h + 220) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.e = ByteBuffer.allocateDirect((this.h + 220) * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.f = ByteBuffer.allocateDirect((this.i + 300) * 4 * 3).order(ByteOrder.nativeOrder()).asIntBuffer();
        if (this.p) {
            this.n = new SelesPointDrawFilter();
            this.o = new SelesPointDrawFilter();
            this.addTarget(this.n, 0);
            this.addTarget(this.o, 0);
        }
    }
    
    @Override
    public void removeAllTargets() {
        super.removeAllTargets();
        if (this.p) {
            if (this.n != null) {
                this.addTarget(this.n, 0);
            }
            if (this.o != null) {
                this.addTarget(this.o, 0);
            }
        }
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
        this.checkGLError(this.getClass().getSimpleName() + " bindFramebuffer");
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, (Buffer)this.d);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)this.e);
        GLES20.glDrawElements(4, this.f.limit(), 5125, (Buffer)this.f);
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
        this.inputFramebufferUnlock();
        this.cacaptureImageBuffer();
    }

    private void a(FloatBuffer var1, FloatBuffer var2) {
        boolean var3 = this.a;
        FaceAligment[] var4 = this.b;
        this.d.clear();
        this.e.clear();
        this.f.clear();
        if (var3 && var4 != null) {
            this.c.clear();
            FaceAligment[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                FaceAligment var8 = var5[var7];
                TuSDKMonsterSnakeFace.MonsterSnakeFaceInfo var9 = new TuSDKMonsterSnakeFace.MonsterSnakeFaceInfo(var8);
                if (var9 != null) {
                    this.c.add(var9);
                }
            }

            if (this.c.size() == 0) {
                this.a = false;
            } else {
                float[] var19 = new float[this.j.length + 44 * this.c.size()];
                float[] var20 = new float[this.k.length + 44 * this.c.size()];
                int[] var21 = new int[this.l.length + g.length * this.c.size()];
                int var22 = 0;
                int var23 = 0;
                int var10 = 0;

                int var11;
                for(var11 = 0; var11 < this.j.length; ++var11) {
                    var19[var22++] = this.j[var11];
                }

                for(var11 = 0; var11 < this.k.length; ++var11) {
                    var20[var23++] = this.k[var11];
                }

                int[] var24 = new int[]{0, 1, 2, 0, 3, 2};

                int var12;
                for(var12 = 0; var12 < var24.length; ++var12) {
                    var21[var10++] = var24[var12];
                }

                for(var12 = 0; var12 < this.c.size(); ++var12) {
                    float[] var13 = ((TuSDKMonsterSnakeFace.MonsterSnakeFaceInfo)this.c.get(var12)).c();

                    for(int var14 = 0; var14 < var13.length; ++var14) {
                        var20[var23++] = var13[var14];
                    }

                    float[] var15;
                    int var16;
                    Iterator var17;
                    PointF var18;
                    List var25;
                    if (this.p) {
                        var25 = ((TuSDKMonsterSnakeFace.MonsterSnakeFaceInfo)this.c.get(var12)).a();
                        var15 = new float[var25.size() * 2];
                        var16 = 0;

                        for(var17 = var25.iterator(); var17.hasNext(); var15[var16++] = var18.y * 2.0F - 1.0F) {
                            var18 = (PointF)var17.next();
                            var15[var16++] = var18.x * 2.0F - 1.0F;
                        }

                        this.a(var25, g, var15);
                    }

                    switch(this.m) {
                        case 2:
                            ((TuSDKMonsterSnakeFace.MonsterSnakeFaceInfo)this.c.get(var12)).e();
                            break;
                        default:
                            ((TuSDKMonsterSnakeFace.MonsterSnakeFaceInfo)this.c.get(var12)).d();
                    }

                    if (this.p) {
                        var25 = ((TuSDKMonsterSnakeFace.MonsterSnakeFaceInfo)this.c.get(var12)).a();
                        var15 = new float[var25.size() * 2];
                        var16 = 0;

                        for(var17 = var25.iterator(); var17.hasNext(); var15[var16++] = var18.y * 2.0F - 1.0F) {
                            var18 = (PointF)var17.next();
                            var15[var16++] = var18.x * 2.0F - 1.0F;
                        }

                        this.b(var25, g, var15);
                    }

                    float[] var26 = ((TuSDKMonsterSnakeFace.MonsterSnakeFaceInfo)this.c.get(var12)).b();

                    int var27;
                    for(var27 = 0; var27 < var26.length; ++var27) {
                        var19[var22++] = var26[var27];
                    }

                    var27 = this.h + var12 * 22;

                    for(var16 = 0; var16 < g.length; ++var16) {
                        var21[var10++] = g[var16] + var27;
                    }
                }

                this.d.put(var19).position(0).limit(var19.length);
                this.e.put(var20).position(0).limit(var20.length);
                this.f.put(var21).position(0).limit(var21.length);
            }
        } else {
            var1.position(0);
            this.d.put(var1).position(0);
            var2.position(0);
            this.e.put(var2).position(0);
            this.f.put(this.l).position(0).limit(this.l.length);
        }
    }
    
    private void a(final List<PointF> list, final int[] array, final float[] array2) {
        if (this.n == null) {
            return;
        }
        final FaceAligment[] array3 = { new FaceAligment(list.toArray(new PointF[list.size()])) };
        this.n.setColor(new float[] { 1.0f, 0.0f, 0.0f, 1.0f });
        this.n.updateElemIndex(array, array2);
        this.n.updateFaceFeatures(array3, 0.0f);
    }
    
    private void b(final List<PointF> list, final int[] array, final float[] array2) {
        if (this.o == null) {
            return;
        }
        final FaceAligment[] array3 = { new FaceAligment(list.toArray(new PointF[list.size()])) };
        this.o.setColor(new float[] { 0.0f, 1.0f, 0.0f, 1.0f });
        this.o.updateElemIndex(array, array2);
        this.o.updateFaceFeatures(array3, 0.0f);
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
        g = new int[] { 0, 1, 12, 0, 9, 12, 1, 2, 13, 1, 12, 13, 2, 3, 14, 2, 13, 14, 3, 4, 15, 3, 14, 15, 4, 5, 17, 4, 15, 16, 4, 16, 17, 5, 6, 18, 5, 17, 18, 6, 7, 19, 6, 18, 19, 7, 8, 20, 7, 19, 20, 8, 10, 20, 9, 11, 21, 9, 12, 21, 10, 11, 21, 10, 20, 21, 12, 13, 21, 13, 14, 21, 14, 15, 21, 15, 16, 21, 16, 17, 21, 17, 18, 21, 18, 19, 21, 19, 20, 21 };
    }
    
    private class MonsterSnakeFaceInfo
    {
        PointF a;
        PointF[] b;
        PointF[] c;
        
        private MonsterSnakeFaceInfo(final FaceAligment faceAligment) {
            this.b = new PointF[12];
            this.c = new PointF[9];
            if (faceAligment != null) {
                this.a(faceAligment.getOrginMarks());
            }
        }
        
        private List<PointF> a() {
            final ArrayList<PointF> list = new ArrayList<PointF>();
            for (int i = 0; i < 12; ++i) {
                list.add(new PointF(this.b[i].x, this.b[i].y));
            }
            for (int j = 0; j < 9; ++j) {
                list.add(new PointF(this.c[j].x, this.c[j].y));
            }
            list.add(new PointF(this.a.x, this.a.y));
            list.add(new PointF(this.a.x, this.a.y));
            return list;
        }
        
        private float[] b() {
            final float[] array = new float[44];
            int n = 0;
            for (int i = 0; i < 12; ++i) {
                array[n++] = this.b[i].x * 2.0f - 1.0f;
                array[n++] = this.b[i].y * 2.0f - 1.0f;
            }
            for (int j = 0; j < 9; ++j) {
                array[n++] = this.c[j].x * 2.0f - 1.0f;
                array[n++] = this.c[j].y * 2.0f - 1.0f;
            }
            array[n++] = this.a.x * 2.0f - 1.0f;
            array[n++] = this.a.y * 2.0f - 1.0f;
            return array;
        }
        
        private float[] c() {
            final float[] array = new float[44];
            int n = 0;
            for (int i = 0; i < 12; ++i) {
                array[n++] = this.b[i].x;
                array[n++] = this.b[i].y;
            }
            for (int j = 0; j < 9; ++j) {
                array[n++] = this.c[j].x;
                array[n++] = this.c[j].y;
            }
            array[n++] = this.a.x;
            array[n++] = this.a.y;
            return array;
        }
        
        private void d() {
            final float n = 0.2f;
            this.c[0] = PointCalc.pointOfPercentage(this.c[0], this.a, n * 0.25f);
            this.c[1] = PointCalc.pointOfPercentage(this.c[1], this.a, n * 0.75f);
            this.c[2] = PointCalc.pointOfPercentage(this.c[2], this.a, n * 1.0f);
            this.c[3] = PointCalc.pointOfPercentage(this.c[3], this.a, n * 0.75f);
            this.c[4] = PointCalc.pointOfPercentage(this.c[4], this.a, n * 0.4f);
            this.c[5] = PointCalc.pointOfPercentage(this.c[5], this.a, n * 0.75f);
            this.c[6] = PointCalc.pointOfPercentage(this.c[6], this.a, n * 1.0f);
            this.c[7] = PointCalc.pointOfPercentage(this.c[7], this.a, n * 0.75f);
            this.c[8] = PointCalc.pointOfPercentage(this.c[8], this.a, n * 0.25f);
        }
        
        private void e() {
            final float n = -0.2f;
            this.c[0] = PointCalc.pointOfPercentage(this.c[0], this.a, n * 0.25f);
            this.c[1] = PointCalc.pointOfPercentage(this.c[1], this.a, n * 0.75f);
            this.c[2] = PointCalc.pointOfPercentage(this.c[2], this.a, n * 1.0f);
            this.c[3] = PointCalc.pointOfPercentage(this.c[3], this.a, n * 0.75f);
            this.c[4] = PointCalc.pointOfPercentage(this.c[4], this.a, n * 0.4f);
            this.c[5] = PointCalc.pointOfPercentage(this.c[5], this.a, n * 0.75f);
            this.c[6] = PointCalc.pointOfPercentage(this.c[6], this.a, n * 1.0f);
            this.c[7] = PointCalc.pointOfPercentage(this.c[7], this.a, n * 0.75f);
            this.c[8] = PointCalc.pointOfPercentage(this.c[8], this.a, n * 0.25f);
        }
        
        private void a(final PointF[] array) {
            if (array == null || array.length < 3) {
                return;
            }
            final int[] array2 = { 0, 4, 8, 12, 16, 20, 24, 28, 32 };
            this.a = new PointF(array[46].x, array[46].y);
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
        }
    }
}
