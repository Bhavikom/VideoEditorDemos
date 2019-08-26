// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters;

import java.util.ArrayList;
import android.graphics.PointF;

import java.util.List;
import java.util.Arrays;
import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.face.FaceAligment;

public class SelesPointDrawFilter extends SelesFilter
{
    public static final String SELES_POINT_DRAW_VERTEX_SHADER = "attribute vec4 position;uniform mat4 uMVPMatrix;void main(){    gl_Position = position;    gl_PointSize = 5.0;}";
    public static final String SELES_POINT_DRAW_FRAGMENT_SHADER = "precision highp float;uniform vec4 uColor;void main(){     gl_FragColor = uColor;}";
    private FaceAligment[] b;
    private float c;
    private IntBuffer d;
    private FloatBuffer e;
    private FloatBuffer f;
    private int g;
    private int h;
    private final float[] i;
    private float[] j;
    float[] a;
    
    public SelesPointDrawFilter() {
        this("attribute vec4 position;uniform mat4 uMVPMatrix;void main(){    gl_Position = position;    gl_PointSize = 5.0;}", "precision highp float;uniform vec4 uColor;void main(){     gl_FragColor = uColor;}");
        this.d = ByteBuffer.allocateDirect(14448).order(ByteOrder.nativeOrder()).asIntBuffer();
        this.e = ByteBuffer.allocateDirect(4816).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }
    
    public SelesPointDrawFilter(final String s, final String s2) {
        super(s, s2);
        this.c = 0.0f;
        this.i = new float[16];
        this.a = new float[] { 1.0f, 0.0f, 0.0f, 1.0f };
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.g = this.mFilterProgram.uniformIndex("uMVPMatrix");
        this.h = this.mFilterProgram.uniformIndex("uColor");
        this.setVec4(new float[] { 0.0f, 1.0f, 0.0f, 1.0f }, this.h, this.mFilterProgram);
    }
    
    public void setColor(final float[] a) {
        if (a == null) {
            return;
        }
        this.a = a;
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        if (this.mFirstInputFramebuffer == null) {
            return;
        }
        this.a();
        this.renderToTexture(this.f, null);
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
        this.setUniformsForProgramAtIndex(0);
        if (this.b() > 0) {
            this.setVec4(new float[] { 0.0f, 1.0f, 0.0f, 1.0f }, this.h, this.mFilterProgram);
            GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, (Buffer)floatBuffer);
            GLES20.glDrawArrays(0, 0, this.j.length / 2);
            this.setVec4(this.a, this.h, this.mFilterProgram);
            GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 2, 5126, false, 0, (Buffer)this.e);
            GLES20.glDrawElements(1, this.d.limit(), 5125, (Buffer)this.d);
        }
        this.cacaptureImageBuffer();
    }
    
    private void a() {
        if (this.b() < 1) {
            return;
        }
        final List<FaceAligment> list = Arrays.asList(this.b);
        final int n = this.a(list) * 2;
        if (this.j == null || n != this.j.length) {
            this.j = new float[n];
            this.f = SelesFilter.buildBuffer(this.j);
        }
        int n2 = 0;
        for (final FaceAligment faceAligment : list) {
            if (faceAligment.getOrginMarks() == null) {
                continue;
            }
            for (final PointF pointF : faceAligment.getOrginMarks()) {
                this.j[n2++] = pointF.x * 2.0f - 1.0f;
                this.j[n2++] = pointF.y * 2.0f - 1.0f;
            }
        }
        this.f.clear();
        this.f.put(this.j).position(0);
    }
    
    private int a(final List<FaceAligment> list) {
        if (list == null || list.size() < 1) {
            return 0;
        }
        int n = 0;
        for (final FaceAligment faceAligment : list) {
            if (faceAligment.getOrginMarks() == null) {
                continue;
            }
            n += faceAligment.getOrginMarks().length;
        }
        return n;
    }
    
    public void updateFaceFeatures(final FaceAligment[] b, final float n) {
        this.b = b;
        this.c = (float)(n * 3.141592653589793 / 180.0);
    }
    
    public void updateElemIndex(final int[] array, final float[] src) {
        this.d.clear();
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < array.length; ++i) {
            if (i % 3 == 0) {
                list.add(array[i]);
                list.add(array[i + 1]);
            }
            else if (i % 3 == 1) {
                list.add(array[i]);
                list.add(array[i + 1]);
            }
            else if (i % 3 == 2) {
                list.add(array[i]);
                list.add(array[i - 2]);
            }
        }
        final int[] src2 = new int[list.size()];
        for (int j = 0; j < list.size(); ++j) {
            src2[j] = (int)list.get(j);
        }
        this.d.put(src2).position(0).limit(src2.length);
        this.e.clear();
        this.e.put(src).position(0).limit(src.length);
    }
    
    private int b() {
        if (this.b == null) {
            return 0;
        }
        return this.b.length;
    }
}
