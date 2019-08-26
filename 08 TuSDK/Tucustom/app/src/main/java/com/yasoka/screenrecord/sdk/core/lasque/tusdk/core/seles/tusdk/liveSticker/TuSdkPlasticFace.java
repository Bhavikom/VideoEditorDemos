// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker;

import java.util.Collection;
import android.graphics.PointF;
import java.util.Iterator;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.SelesFramebufferCache;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebufferCache;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesPointDrawFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.ArrayList;
//import org.lasque.tusdk.core.seles.SelesContext;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.List;
//import org.lasque.tusdk.core.seles.filters.SelesPointDrawFilter;
//import org.lasque.tusdk.core.face.FaceAligment;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSdkPlasticFace extends SelesFilter implements SelesParameters.FilterFacePositionInterface, SelesParameters.FilterParameterInterface
{
    private final FloatBuffer d;
    private final FloatBuffer e;
    private final IntBuffer f;
    private final int[] g;
    private boolean h;
    private float[] i;
    private float[] j;
    private int[] k;
    FaceAligment[] a;
    private final Object l;
    int[] b;
    private float m;
    private float n;
    private float o;
    private float p;
    private float q;
    private float r;
    private float s;
    private float t;
    private SelesPointDrawFilter u;
    private boolean v;
    List<TuSdkPlasticFaceInfo> c;
    
    public TuSdkPlasticFace() {
        this.g = new int[] { 0, 1, 2, 1, 2, 3 };
        this.h = false;
        this.l = new Object();
        this.m = 1.0f;
        this.n = 0.0f;
        this.o = 1.0f;
        this.p = 1.0f;
        this.q = 0.0f;
        this.r = 0.0f;
        this.s = 0.0f;
        this.t = 0.0f;
        this.v = false;
        this.d = ByteBuffer.allocateDirect(2416).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.e = ByteBuffer.allocateDirect(2416).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.f = ByteBuffer.allocateDirect(7248).order(ByteOrder.nativeOrder()).asIntBuffer();
        if (this.v) {
            this.addTarget(this.u = new SelesPointDrawFilter(), 0);
        }
        this.c = new ArrayList<TuSdkPlasticFaceInfo>();
    }
    
    @Override
    public void removeAllTargets() {
        super.removeAllTargets();
        if (this.u != null) {
            this.addTarget(this.u, 0);
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.checkGLError("TuSdkPlasticFace onInitOnGLThread");
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
        if (this.mUsingNextFrameForImageCapture) {
            this.mOutputFramebuffer.lock();
        }
        this.setUniformsForProgramAtIndex(0);
        GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
        GLES20.glClear(16384);
        this.inputFramebufferBindTexture();
        this.checkGLError("TuSdkPlasticFace inputFramebufferBindTexture");
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, (Buffer)this.d);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)this.e);
        GLES20.glDrawElements(4, this.f.limit(), 5125, (Buffer)this.f);
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
        this.inputFramebufferUnlock();
        this.cacaptureImageBuffer();
    }
    
    private void a(final FloatBuffer src, final FloatBuffer src2) {
        final boolean h = this.h;
        final FaceAligment[] a = this.a;
        if (!h || a == null) {
            this.d.clear();
            src.position(0);
            this.d.put(src).position(0);
            this.e.clear();
            src2.position(0);
            this.e.put(src2).position(0);
            this.f.clear();
            this.f.put(this.g).position(0).limit(6);
            return;
        }
        this.c.clear();
        final FaceAligment[] array = a;
        for (int length = array.length, i = 0; i < length; ++i) {
            final TuSdkPlasticFaceInfo tuSdkPlasticFaceInfo = new TuSdkPlasticFaceInfo(array[i]);
            if (tuSdkPlasticFaceInfo.isEmpty()) {
                TLog.w("plastic face is empty !!!", new Object[0]);
            }
            else {
                this.c.add(tuSdkPlasticFaceInfo);
            }
        }
        if (this.c.isEmpty()) {
            this.b = null;
            this.h = false;
            TLog.w("may be not data", new Object[0]);
            return;
        }
        this.a(this.c);
        for (final TuSdkPlasticFaceInfo tuSdkPlasticFaceInfo2 : this.c) {
            tuSdkPlasticFaceInfo2.calcChin(this.n);
            tuSdkPlasticFaceInfo2.calcEyeEnlarge(this.m);
            tuSdkPlasticFaceInfo2.calcEyeDis(this.r);
            tuSdkPlasticFaceInfo2.calcEyeAngle(this.s);
            tuSdkPlasticFaceInfo2.calcNose(this.o);
            tuSdkPlasticFaceInfo2.calcMouth(this.p);
            tuSdkPlasticFaceInfo2.calcArchEyebrow(this.q);
            tuSdkPlasticFaceInfo2.calcJaw(this.t);
        }
        if (!this.b(this.c)) {
            this.h = false;
            return;
        }
        this.a();
    }
    
    private void a() {
        synchronized (this.l) {
            this.d.clear();
            this.d.put(this.i).position(0).limit(this.i.length);
            this.e.clear();
            this.e.put(this.j).position(0).limit(this.j.length);
            this.f.clear();
            this.f.put(this.k).position(0).limit(this.k.length);
        }
    }
    
    private void a(final float m) {
        this.m = m;
    }
    
    private float b() {
        return this.m;
    }
    
    private void b(final float r) {
        this.r = r;
    }
    
    private float c() {
        return this.r;
    }
    
    private void c(final float s) {
        this.s = s;
    }
    
    private float d() {
        return this.s;
    }
    
    private void d(final float n) {
        this.n = n;
    }
    
    private float e() {
        return this.n;
    }
    
    private void e(final float t) {
        this.t = t;
    }
    
    private float f() {
        return this.t;
    }
    
    private float g() {
        return this.o;
    }
    
    private void f(final float o) {
        this.o = o;
    }
    
    private float h() {
        return this.p;
    }
    
    private void g(final float p) {
        this.p = p;
    }
    
    public float getArchEyebrow() {
        return this.q;
    }
    
    public void setArchEyebrow(final float q) {
        this.q = q;
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("eyeSize", this.b(), 1.0f, 1.3f);
        initParams.appendFloatArg("chinSize", this.e(), 0.0f, 0.1f);
        initParams.appendFloatArg("noseSize", this.g(), 1.0f, 0.8f);
        initParams.appendFloatArg("mouthWidth", this.h(), 0.9f, 1.1f);
        initParams.appendFloatArg("archEyebrow", this.getArchEyebrow(), 0.3f, -0.3f);
        initParams.appendFloatArg("eyeDis", this.c(), -0.05f, 0.05f);
        initParams.appendFloatArg("eyeAngle", this.d(), -5.0f, 5.0f);
        initParams.appendFloatArg("jawSize", this.f(), 0.06f, -0.06f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("eyeSize")) {
            this.a(filterArg.getValue());
        }
        else if (filterArg.equalsKey("chinSize")) {
            this.d(filterArg.getValue());
        }
        else if (filterArg.equalsKey("noseSize")) {
            this.f(filterArg.getValue());
        }
        else if (filterArg.equalsKey("mouthWidth")) {
            this.g(filterArg.getValue());
        }
        else if (filterArg.equalsKey("archEyebrow")) {
            this.setArchEyebrow(filterArg.getValue());
        }
        else if (filterArg.equalsKey("eyeDis")) {
            this.b(filterArg.getValue());
        }
        else if (filterArg.equalsKey("eyeAngle")) {
            this.c(filterArg.getValue());
        }
        else if (filterArg.equalsKey("jawSize")) {
            this.e(filterArg.getValue());
        }
    }
    
    @Override
    public void updateFaceFeatures(final FaceAligment[] a, final float n) {
        if (a == null || a.length < 1) {
            this.b = null;
            this.h = false;
            this.a = null;
            return;
        }
        synchronized (this.l) {
            this.a = a;
        }
        this.h = true;
    }
    
    private boolean a(final List<TuSdkPlasticFaceInfo> list) {
        final ArrayList<PointF> list2 = new ArrayList<PointF>(list.size() * 107 + 4);
        list2.add(new PointF(0.0f, 0.0f));
        list2.add(new PointF(1.0f, 0.0f));
        list2.add(new PointF(1.0f, 1.0f));
        list2.add(new PointF(0.0f, 1.0f));
        final Iterator<TuSdkPlasticFaceInfo> iterator = list.iterator();
        while (iterator.hasNext()) {
            list2.addAll(iterator.next().getPoints());
        }
        final float[] j = new float[list2.size() * 2];
        int n = 0;
        for (final PointF pointF : list2) {
            j[n++] = pointF.x;
            j[n++] = pointF.y;
        }
        this.j = j;
        return true;
    }
    
    private boolean b(final List<TuSdkPlasticFaceInfo> list) {
        final ArrayList<PointF> list2 = new ArrayList<PointF>(list.size() * 107 + 4);
        list2.add(new PointF(0.0f, 0.0f));
        list2.add(new PointF(1.0f, 0.0f));
        list2.add(new PointF(1.0f, 1.0f));
        list2.add(new PointF(0.0f, 1.0f));
        final ArrayList<Integer> list3 = new ArrayList<Integer>();
        final int[] array = { 0, 1, 2, 0, 3, 2 };
        for (final TuSdkPlasticFaceInfo tuSdkPlasticFaceInfo : list) {
            final List<PointF> points = tuSdkPlasticFaceInfo.getPoints();
            final int size = list2.size();
            list2.addAll(points);
            final int[] fillFace = tuSdkPlasticFaceInfo.fillFace();
            for (int i = 0; i < fillFace.length; ++i) {
                list3.add(fillFace[i] + size);
            }
        }
        final float[] j = new float[list2.size() * 2];
        int n = 0;
        for (final PointF pointF : list2) {
            j[n++] = pointF.x * 2.0f - 1.0f;
            j[n++] = pointF.y * 2.0f - 1.0f;
        }
        this.i = j;
        this.b = new int[array.length + list3.size()];
        if (this.b == null || this.b.length == 0) {
            return false;
        }
        for (int k = 0; k < array.length; ++k) {
            this.b[k] = array[k];
        }
        for (int l = 0; l < list3.size(); ++l) {
            this.b[array.length + l] = (int)list3.get(l);
        }
        this.k = this.b;
        if (this.v) {
            this.c(list2);
        }
        return true;
    }
    
    private void c(final List<PointF> list) {
        if (this.u == null) {
            return;
        }
        final FaceAligment[] array = { new FaceAligment(list.toArray(new PointF[list.size()])) };
        this.u.updateElemIndex(this.k, this.i);
        this.u.updateFaceFeatures(array, 0.0f);
    }
}
