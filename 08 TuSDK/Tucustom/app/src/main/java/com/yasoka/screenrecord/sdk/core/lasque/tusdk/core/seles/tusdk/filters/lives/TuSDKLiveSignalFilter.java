// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives;

//import org.lasque.tusdk.core.utils.RectHelper;
import java.util.Iterator;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.Random;
import android.graphics.RectF;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.SelesParameters;
import java.nio.Buffer;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.seles.SelesContext;
import java.nio.FloatBuffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;

public class TuSDKLiveSignalFilter extends SelesTwoInputFilter
{
    private int f;
    private int g;
    private int h;
    private int i;
    private TuSDKLiveSignalVertexBuild j;
    private PointF k;
    private float[] l;
    private float[] m;
    private float n;
    private float o;
    private float p;
    private float q;
    private float r;
    private float s;
    private float t;
    int a;
    long b;
    long c;
    int d;
    int e;
    
    public TuSDKLiveSignalFilter() {
        super("-slive06v", "-slive06f");
        this.k = new PointF(0.0f, 0.0f);
        this.l = new float[] { 0.1f, 0.2f, 1.0f, 0.5f };
        this.m = new float[] { 0.6f, 0.4f, 0.7f, 1.0f };
        this.s = 25.0f;
        this.t = 1.0f;
        this.disableSecondFrameCheck();
        this.j = new TuSDKLiveSignalVertexBuild();
    }
    
    public TuSDKLiveSignalFilter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null) {
            float n = 0.0f;
            float n2 = 0.0f;
            float n3 = 0.1f;
            float n4 = 0.2f;
            float n5 = 0.6f;
            float n6 = 0.4f;
            float n7 = 0.7f;
            if (filterOption.args.containsKey("splitX")) {
                final float float1 = Float.parseFloat(filterOption.args.get("splitX"));
                if (float1 > 0.0f) {
                    n = float1;
                }
            }
            if (filterOption.args.containsKey("splitY")) {
                final float float2 = Float.parseFloat(filterOption.args.get("splitY"));
                if (float2 > 0.0f) {
                    n2 = float2;
                }
            }
            this.setSplit(new PointF(n, n2));
            if (filterOption.args.containsKey("flutterX")) {
                final float float3 = Float.parseFloat(filterOption.args.get("flutterX"));
                if (float3 > 0.0f) {
                    n3 = float3;
                }
            }
            if (filterOption.args.containsKey("flutterY")) {
                final float float4 = Float.parseFloat(filterOption.args.get("flutterY"));
                if (float4 > 0.0f) {
                    n4 = float4;
                }
            }
            this.setFlutter(new float[] { n3, n4, 1.0f, 0.5f });
            if (filterOption.args.containsKey("partialRed")) {
                final float float5 = Float.parseFloat(filterOption.args.get("partialRed"));
                if (float5 > 0.0f) {
                    n5 = float5;
                }
            }
            if (filterOption.args.containsKey("partialGreen")) {
                final float float6 = Float.parseFloat(filterOption.args.get("partialGreen"));
                if (float6 > 0.0f) {
                    n6 = float6;
                }
            }
            if (filterOption.args.containsKey("partialBlue")) {
                final float float7 = Float.parseFloat(filterOption.args.get("partialBlue"));
                if (float7 > 0.0f) {
                    n7 = float7;
                }
            }
            this.setPartialColor(new float[] { n5, n6, n7, 1.0f });
            if (filterOption.args.containsKey("division")) {
                final float float8 = Float.parseFloat(filterOption.args.get("division"));
                if (float8 > 0.0f) {
                    this.setDivision(float8);
                }
            }
            if (filterOption.args.containsKey("animation")) {
                final float float9 = Float.parseFloat(filterOption.args.get("animation"));
                if (float9 > 0.0f) {
                    this.setAnimation(float9);
                }
            }
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        GLES20.glEnableVertexAttribArray(this.f = this.mFilterProgram.attributeIndex("inputTextureType"));
        this.g = this.mFilterProgram.uniformIndex("split");
        this.h = this.mFilterProgram.uniformIndex("flutter");
        this.i = this.mFilterProgram.uniformIndex("partialColor");
        this.setSplit(this.k);
        this.setFlutter(this.l);
        this.setPartialColor(this.m);
    }
    
    @Override
    protected void initializeAttributes() {
        super.initializeAttributes();
        this.mFilterProgram.addAttribute("inputTextureType");
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (this.isPreventRendering()) {
            this.inputFramebufferUnlock();
            return;
        }
        SelesContext.setActiveShaderProgram(this.mFilterProgram);
        (this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, this.sizeOfFBO(), this.getOutputTextureOptions())).activateFramebuffer();
        if (this.mUsingNextFrameForImageCapture) {
            this.mOutputFramebuffer.lock();
        }
        this.setUniformsForProgramAtIndex(0);
        this.a();
        GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
        GLES20.glClear(16384);
        this.inputFramebufferBindTexture();
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, this.j.getPositionSize(), 5126, false, 0, (Buffer)this.j.getPositions());
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, this.j.getTextureCoordinateSize(), 5126, false, 0, (Buffer)this.j.getTextureCoordinates());
        GLES20.glVertexAttribPointer(this.mFilterSecondTextureCoordinateAttribute, this.j.getTextureCoordinateSize2(), 5126, false, 0, (Buffer)this.j.getTextureCoordinates2());
        GLES20.glVertexAttribPointer(this.f, this.j.getParamsSize(), 5126, false, 0, (Buffer)this.j.getParams());
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        GLES20.glDrawArrays(4, 0, this.j.getDrawTotal());
        GLES20.glDisable(3042);
        this.inputFramebufferUnlock();
        this.cacaptureImageBuffer();
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
        this.makeAnimotionWithTime(System.currentTimeMillis());
        super.informTargetsAboutNewFrame(n);
    }
    
    private void a() {
        this.j.setMainType(this.getMainType());
        this.j.setBarType(this.getBarType());
        this.j.setBarTotal((int)Math.floor(this.getBarTotal()));
        this.j.setBlockType(this.getBlockType());
        this.j.setBlockTotal((int)Math.floor(this.getBlockTotal()));
        this.j.setDivision((int)Math.floor(this.getDivision()));
        this.j.setTextureSize(this.mInputTextureSize);
        this.j.setRotation(this.mInputRotation);
        this.j.setRotation2(this.mInputRotation2);
        this.j.calculate();
    }
    
    public PointF getSplit() {
        return this.k;
    }
    
    public void setSplit(final PointF k) {
        this.setPoint(this.k = k, this.g, this.mFilterProgram);
    }
    
    public void setSplitX(final float x) {
        final PointF split = this.getSplit();
        split.x = x;
        this.setSplit(split);
    }
    
    public void setSplitY(final float y) {
        final PointF split = this.getSplit();
        split.y = y;
        this.setSplit(split);
    }
    
    public float[] getFlutter() {
        return this.l;
    }
    
    public void setFlutter(final float[] l) {
        this.setVec4(this.l = l, this.h, this.mFilterProgram);
    }
    
    public void setFlutterX(final float n) {
        final float[] flutter = this.getFlutter();
        flutter[0] = n;
        this.setFlutter(flutter);
    }
    
    public void setFlutterY(final float n) {
        final float[] flutter = this.getFlutter();
        flutter[1] = n;
        this.setFlutter(flutter);
    }
    
    public void setFlutterStrength(final float n) {
        final float[] flutter = this.getFlutter();
        flutter[2] = n;
        this.setFlutter(flutter);
    }
    
    public void setFlutterMixed(final float n) {
        final float[] flutter = this.getFlutter();
        flutter[3] = n;
        this.setFlutter(flutter);
    }
    
    public float[] getPartialColor() {
        return this.m;
    }
    
    public void setPartialColor(final float[] m) {
        this.setVec4(this.m = m, this.i, this.mFilterProgram);
    }
    
    public void setPartialRed(final float n) {
        final float[] partialColor = this.getPartialColor();
        partialColor[0] = n;
        this.setPartialColor(partialColor);
    }
    
    public void setPartialGreen(final float n) {
        final float[] partialColor = this.getPartialColor();
        partialColor[1] = n;
        this.setPartialColor(partialColor);
    }
    
    public void setPartialBlue(final float n) {
        final float[] partialColor = this.getPartialColor();
        partialColor[2] = n;
        this.setPartialColor(partialColor);
    }
    
    public void setPartialInvers(final float n) {
        final float[] partialColor = this.getPartialColor();
        partialColor[3] = n;
        this.setPartialColor(partialColor);
    }
    
    public float getMainType() {
        return this.n;
    }
    
    public void setMainType(final float n) {
        this.n = n;
    }
    
    public float getBarType() {
        return this.o;
    }
    
    public void setBarType(final float o) {
        this.o = o;
    }
    
    public float getBarTotal() {
        return this.p;
    }
    
    public void setBarTotal(final float p) {
        this.p = p;
    }
    
    public float getBlockType() {
        return this.q;
    }
    
    public void setBlockType(final float q) {
        this.q = q;
    }
    
    public float getBlockTotal() {
        return this.r;
    }
    
    public void setBlockTotal(final float r) {
        this.r = r;
    }
    
    public float getDivision() {
        return this.s;
    }
    
    public void setDivision(final float s) {
        this.s = s;
    }
    
    public float getAnimation() {
        return this.t;
    }
    
    public void setAnimation(final float t) {
        this.t = t;
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("splitX", this.getSplit().x, -1.0f, 1.0f);
        initParams.appendFloatArg("splitY", this.getSplit().y, -1.0f, 1.0f);
        initParams.appendFloatArg("flutterX", this.getFlutter()[0], -1.0f, 1.0f);
        initParams.appendFloatArg("flutterY", this.getFlutter()[1], -1.0f, 1.0f);
        initParams.appendFloatArg("flutterStrength", this.getFlutter()[2], 0.0f, 1.0f);
        initParams.appendFloatArg("flutterMixed", this.getFlutter()[3], 0.0f, 1.0f);
        initParams.appendFloatArg("partialRed", this.getPartialColor()[0], 0.0f, 2.0f);
        initParams.appendFloatArg("partialGreen", this.getPartialColor()[1], 0.0f, 2.0f);
        initParams.appendFloatArg("partialBlue", this.getPartialColor()[2], 0.0f, 2.0f);
        initParams.appendFloatArg("partialInvers", this.getPartialColor()[3], 0.0f, 1.0f);
        initParams.appendFloatArg("mainType", this.getMainType(), 0.0f, 1.0f);
        initParams.appendFloatArg("barType", this.getBarType(), 0.0f, 1.0f);
        initParams.appendFloatArg("barTotal", this.getBarTotal(), 0.0f, 10.0f);
        initParams.appendFloatArg("blockType", this.getBlockType(), 0.0f, 1.0f);
        initParams.appendFloatArg("blockTotal", this.getBlockTotal(), 0.0f, 150.0f);
        initParams.appendFloatArg("division", this.getDivision(), 0.0f, 40.0f);
        initParams.appendFloatArg("animation", this.getAnimation(), 0.0f, 1.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("splitX")) {
            this.setSplitX(filterArg.getValue());
        }
        else if (filterArg.equalsKey("splitY")) {
            this.setSplitY(filterArg.getValue());
        }
        else if (filterArg.equalsKey("flutterX")) {
            this.setFlutterX(filterArg.getValue());
        }
        else if (filterArg.equalsKey("flutterY")) {
            this.setFlutterY(filterArg.getValue());
        }
        else if (filterArg.equalsKey("flutterStrength")) {
            this.setFlutterStrength(filterArg.getValue());
        }
        else if (filterArg.equalsKey("flutterMixed")) {
            this.setFlutterMixed(filterArg.getValue());
        }
        else if (filterArg.equalsKey("partialRed")) {
            this.setPartialRed(filterArg.getValue());
        }
        else if (filterArg.equalsKey("partialGreen")) {
            this.setPartialGreen(filterArg.getValue());
        }
        else if (filterArg.equalsKey("partialBlue")) {
            this.setPartialBlue(filterArg.getValue());
        }
        else if (filterArg.equalsKey("partialInvers")) {
            this.setPartialInvers(filterArg.getValue());
        }
        else if (filterArg.equalsKey("mainType")) {
            this.setMainType(filterArg.getValue());
        }
        else if (filterArg.equalsKey("barType")) {
            this.setBarType(filterArg.getValue());
        }
        else if (filterArg.equalsKey("barTotal")) {
            this.setBarTotal(filterArg.getValue());
        }
        else if (filterArg.equalsKey("blockType")) {
            this.setBlockType(filterArg.getValue());
        }
        else if (filterArg.equalsKey("blockTotal")) {
            this.setBlockTotal(filterArg.getValue());
        }
        else if (filterArg.equalsKey("division")) {
            this.setDivision(filterArg.getValue());
        }
        else if (filterArg.equalsKey("animation")) {
            this.setAnimation(filterArg.getValue());
        }
    }
    
    public void makeAnimotionWithTime(final long n) {
        if (this.getAnimation() < 0.5) {
            return;
        }
        long n2 = n / 50L;
        if (this.c == -1L) {
            this.c = n2;
        }
        else {
            n2 -= this.c;
        }
        if (n2 == this.b) {
            return;
        }
        this.b = n2;
        final int[] array = { 0, 2, 4, 44, 60, 110, 112, 114, 164, 166, 168, 208, 224 };
        final float[] array2 = { 0.15f, 0.0f, 0.0f, 0.35f, 0.0f, 0.0f, 0.15f, 0.0f, 0.15f, 0.0f, 0.0f, 0.0f };
        final float[] array3 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.15f, 0.0f, 0.0f, 0.25f, 0.0f, 0.0f, 0.4f, 0.0f };
        final float[] array4 = { 0.0f, 0.0f, -0.005f, 0.0f, 0.004f, 0.0f, 0.0f, 0.004f, 0.0f, 0.0f, 0.004f, 0.0f };
        final float[] array5 = { 0.0f, 0.0f, 0.2f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
        final float[] array6 = { 0.0f, 0.4f, 0.12f, 0.0f, 0.4f, 0.4f, 0.0f, 0.4f, 0.0f, 0.4f, 0.4f, 0.0f };
        final float[] array7 = { 0.0f, 0.5f, 0.68f, 0.0f, 0.6f, 0.68f, 0.0f, 0.5f, 0.0f, 0.68f, 0.68f, 0.0f };
        final float[] array8 = { 0.0f, 0.0f, 0.002f, 0.0f, 0.0023f, 0.0f, 0.0f, 0.0023f, 0.0f, 0.0f, 0.0023f, 0.0f };
        final float[] array9 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.4f, 0.0f, 0.0f, 0.0f, 0.4f, 0.0f, 0.0f };
        final float[] array10 = { 0.0f, 0.0f, 0.0f, 0.01f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.01f };
        final float[] array11 = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
        if (this.d == 0 || this.d >= array[array.length - 1] - 1) {
            this.d = 0;
            this.a = 0;
            this.b = -1L;
            this.getParameter().setFilterArg("splitX", 0.5f);
            this.getParameter().setFilterArg("splitX", 0.5f);
            this.getParameter().setFilterArg("splitY", 0.5f);
            this.getParameter().setFilterArg("partialRed", 0.0f);
            this.getParameter().setFilterArg("partialGreen", 0.0f);
            this.getParameter().setFilterArg("partialBlue", 0.0f);
            this.getParameter().setFilterArg("partialInvers", 0.0f);
            this.getParameter().setFilterArg("flutterX", 0.3f);
            this.getParameter().setFilterArg("flutterY", 0.3f);
            this.getParameter().setFilterArg("flutterMixed", 0.6f);
        }
        ++this.d;
        if (this.d >= (long)array[this.a + 1] || this.a == 0) {
            this.e = 0;
            if (this.d != 1) {
                ++this.a;
            }
            this.getParameter().setFilterArg("mainType", array2[this.a]);
            this.getParameter().setFilterArg("barType", array3[this.a]);
            this.getParameter().setFilterArg("blockType", array6[this.a]);
            this.getParameter().setFilterArg("division", array7[this.a]);
            this.getParameter().setFilterArg("barTotal", array5[this.a]);
            this.getParameter().setFilterArg("blockTotal", array9[this.a]);
            this.getParameter().setFilterArg("flutterStrength", array11[this.a]);
        }
        else {
            ++this.e;
            this.getParameter().setFilterArg("barTotal", array4[this.a] * this.e + array5[this.a]);
            this.getParameter().setFilterArg("blockTotal", array8[this.a] * this.e + array9[this.a]);
            this.getParameter().setFilterArg("flutterStrength", array10[this.a] * this.e + array11[this.a]);
        }
        this.submitParameter();
    }
    
    private class TuSDKLiveSignalVertexBuild
    {
        public final int VERTEX_POSITION_SIZE = 2;
        public final int VERTEX_TEXTURECOORDINATE_SIZE = 2;
        public final int VERTEX_TEXTURECOORDINATE_SIZE2 = 2;
        public final int VERTEX_PARAMS_SIZE = 1;
        public final int VERTEX_ELEMENT_POINTS = 6;
        private FloatBuffer b;
        private FloatBuffer c;
        private FloatBuffer d;
        private FloatBuffer e;
        private boolean f;
        private boolean g;
        private int h;
        private float i;
        private float j;
        private int k;
        private float l;
        private int m;
        private int n;
        private TuSdkSize o;
        private ImageOrientation p;
        private ImageOrientation q;
        
        public int getDrawTotal() {
            return 6 * this.h;
        }
        
        public FloatBuffer getPositions() {
            if (this.b != null) {
                this.b.position(0);
            }
            return this.b;
        }
        
        public FloatBuffer getTextureCoordinates() {
            if (this.c != null) {
                this.c.position(0);
            }
            return this.c;
        }
        
        public FloatBuffer getTextureCoordinates2() {
            if (this.d != null) {
                this.d.position(0);
            }
            return this.d;
        }
        
        public FloatBuffer getParams() {
            if (this.e != null) {
                this.e.position(0);
            }
            return this.e;
        }
        
        public int getPositionSize() {
            return 2;
        }
        
        public int getTextureCoordinateSize() {
            return 2;
        }
        
        public int getTextureCoordinateSize2() {
            return 2;
        }
        
        public int getParamsSize() {
            return 1;
        }
        
        public int getElementPoints() {
            return 6;
        }
        
        public int getElementTotal() {
            return this.h;
        }
        
        public void setMainType(final float i) {
            if (this.i == i) {
                return;
            }
            this.g = true;
            this.i = i;
        }
        
        public void setBarType(final float j) {
            if (this.j == j) {
                return;
            }
            this.g = true;
            this.j = j;
        }
        
        public void setBarTotal(final int k) {
            if (this.k == k) {
                return;
            }
            this.f = true;
            this.k = k;
        }
        
        public void setBlockType(final float l) {
            if (this.l == l) {
                return;
            }
            this.g = true;
            this.l = l;
        }
        
        public void setBlockTotal(final int m) {
            if (this.m == m) {
                return;
            }
            this.f = true;
            this.m = m;
        }
        
        public void setDivision(int n) {
            if (n < 1) {
                n = 1;
            }
            if (this.n == n) {
                return;
            }
            this.f = true;
            this.n = n;
        }
        
        public void setTextureSize(final TuSdkSize o) {
            if (o == null || o.equals(this.o)) {
                return;
            }
            this.f = true;
            this.o = o;
        }
        
        public void setRotation(final ImageOrientation p) {
            if (p == null || p == this.p) {
                return;
            }
            this.f = true;
            this.p = p;
        }
        
        public void setRotation2(final ImageOrientation q) {
            if (q == null || q == this.q) {
                return;
            }
            this.f = true;
            this.q = q;
        }
        
        public TuSDKLiveSignalVertexBuild() {
            this.h = 1;
            this.p = ImageOrientation.Up;
            this.q = ImageOrientation.Up;
            this.a();
        }
        
        public void calculate() {
            if (this.f) {
                this.a();
            }
            else if (this.g) {
                this.b();
            }
        }
        
        private int a(final ArrayList<RectF> list, final ArrayList<RectF> list2) {
            if (this.n < 1 || this.o == null || !this.o.isSize()) {
                return 0;
            }
            final float n = this.o.minSide() / (float)this.n;
            final int n2 = (int)Math.floor(this.o.width / n);
            final int n3 = (int)Math.floor(this.o.height / n);
            final float n4 = n / this.o.width;
            final float n5 = n / this.o.height;
            int n6 = 0;
            final Random random = new Random();
            for (int i = 0; i < this.k; ++i) {
                final float n7 = random.nextInt(n3) * n5;
                list.add(new RectF(0.0f, n7, 1.0f, n7 + n5));
                final float n8 = random.nextInt(n3) * n5;
                list2.add(new RectF(0.0f, n8, 1.0f, n8 + n5));
                ++n6;
            }
            for (int j = 0; j < this.m; ++j) {
                final float n9 = random.nextInt(n2) * n4;
                final float n10 = random.nextInt(n3) * n5;
                list.add(new RectF(n9, n10, n9 + n4, n10 + n5));
                final float n11 = random.nextInt(n2) * n4;
                final float n12 = random.nextInt(n3) * n5;
                list2.add(new RectF(n11, n12, n11 + n4, n12 + n5));
                ++n6;
            }
            return n6;
        }
        
        private void a() {
            this.f = false;
            this.h = this.k + this.m + 1;
            this.b = ByteBuffer.allocateDirect(this.getDrawTotal() * this.getPositionSize() * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            this.c = ByteBuffer.allocateDirect(this.getDrawTotal() * this.getTextureCoordinateSize() * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            this.d = ByteBuffer.allocateDirect(this.getDrawTotal() * this.getTextureCoordinateSize2() * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            final float[] src = { -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f };
            final float[] textureCoordinates = SelesFilter.textureCoordinates(this.p);
            final float[] src2 = { textureCoordinates[0], textureCoordinates[1], textureCoordinates[2], textureCoordinates[3], textureCoordinates[4], textureCoordinates[5], textureCoordinates[2], textureCoordinates[3], textureCoordinates[4], textureCoordinates[5], textureCoordinates[6], textureCoordinates[7] };
            final float[] textureCoordinates2 = SelesFilter.textureCoordinates(this.q);
            final float[] array = { textureCoordinates2[0], textureCoordinates2[1], textureCoordinates2[2], textureCoordinates2[3], textureCoordinates2[4], textureCoordinates2[5], textureCoordinates2[2], textureCoordinates2[3], textureCoordinates2[4], textureCoordinates2[5], textureCoordinates2[6], textureCoordinates2[7] };
            this.b.put(src);
            this.c.put(src2);
            this.d.put(array);
            this.b();
            final ArrayList<RectF> list = new ArrayList<RectF>(this.k + this.m);
            final ArrayList<RectF> list2 = new ArrayList<RectF>(this.k + this.m);
            if (this.a(list, list2) > 0) {
                final Iterator<RectF> iterator = list.iterator();
                while (iterator.hasNext()) {
                    this.a(this.b, iterator.next());
                }
                for (final RectF rectF : list2) {
                    this.d.put(array);
                    this.b(this.c, rectF);
                }
            }
        }
        
        private void b() {
            this.g = false;
            (this.e = ByteBuffer.allocateDirect(this.getDrawTotal() * this.getParamsSize() * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()).put(new float[] { this.i, this.i, this.i, this.i, this.i, this.i });
            if (this.k > 0) {
                final float[] src = { this.j, this.j, this.j, this.j, this.j, this.j };
                for (int i = 0; i < this.k; ++i) {
                    this.e.put(src);
                }
            }
            if (this.m > 0) {
                final float[] src2 = { this.l, this.l, this.l, this.l, this.l, this.l };
                for (int j = 0; j < this.m; ++j) {
                    this.e.put(src2);
                }
            }
        }
        
        private void a(final FloatBuffer floatBuffer, final RectF rectF) {
            final float[] src = new float[12];
            src[0] = rectF.left * 2.0f - 1.0f;
            src[1] = 1.0f - rectF.bottom * 2.0f;
            src[2] = rectF.right * 2.0f - 1.0f;
            src[3] = src[1];
            src[4] = src[0];
            src[5] = 1.0f - rectF.top * 2.0f;
            src[6] = src[2];
            src[7] = src[3];
            src[8] = src[4];
            src[9] = src[5];
            src[10] = src[2];
            src[11] = src[5];
            floatBuffer.put(src);
        }
        
        private void b(final FloatBuffer floatBuffer, final RectF rectF) {
            final float[] textureCoordinates = RectHelper.textureCoordinates(this.p, rectF);
            floatBuffer.put(new float[] { textureCoordinates[0], textureCoordinates[1], textureCoordinates[2], textureCoordinates[3], textureCoordinates[4], textureCoordinates[5], textureCoordinates[2], textureCoordinates[3], textureCoordinates[4], textureCoordinates[5], textureCoordinates[6], textureCoordinates[7] });
        }
    }
}
