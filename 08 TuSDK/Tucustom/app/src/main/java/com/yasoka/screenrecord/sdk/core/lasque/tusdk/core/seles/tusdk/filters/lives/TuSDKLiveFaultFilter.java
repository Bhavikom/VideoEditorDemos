// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives;

//import org.lasque.tusdk.core.utils.RectHelper;
import java.util.Iterator;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import android.graphics.RectF;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.SelesParameters;
import java.nio.Buffer;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKLiveFaultFilter extends SelesFilter
{
    private TuSDKLiveSignalVertexBuild d;
    private int e;
    private float f;
    private float g;
    private float h;
    private float[] i;
    private float j;
    int a;
    long b;
    long c;
    private int k;
    
    public TuSDKLiveFaultFilter() {
        super("-slive14f");
        this.h = 1.0f;
        this.i = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
        this.a = 0;
        this.b = -50L;
        this.c = -1L;
        this.d = new TuSDKLiveSignalVertexBuild();
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.e = this.mFilterProgram.uniformIndex("flutter");
        this.setFlutter(this.i);
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
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
        this.checkGLError(this.getClass().getSimpleName() + " activateFramebuffer");
        this.setUniformsForProgramAtIndex(0);
        this.a();
        GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
        GLES20.glClear(16384);
        this.inputFramebufferBindTexture();
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, this.d.getPositionSize(), 5126, false, 0, (Buffer)this.d.getPositions());
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, this.d.getTextureCoordinateSize(), 5126, false, 0, (Buffer)this.d.getTextureCoordinates());
        this.checkGLError(this.getClass().getSimpleName() + " bindFramebuffer");
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        GLES20.glDrawArrays(4, 0, this.d.getDrawTotal());
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
        GLES20.glDisable(3042);
        this.inputFramebufferUnlock();
        this.cacaptureImageBuffer();
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
        this.makeAnimationWithTime(n);
        super.informTargetsAboutNewFrame(n);
    }
    
    private void a() {
        this.d.setBarTotal((int)this.getBarTotal());
        this.d.setBlockTotal(0);
        this.d.setTextureSize(this.mInputTextureSize);
        this.d.setRotation(this.mInputRotation);
        this.d.setType((int)this.getType());
        this.d.calculate();
    }
    
    public float[] getFlutter() {
        return this.i;
    }
    
    public void setFlutter(final float[] i) {
        this.setVec4(this.i = i, this.e, this.mFilterProgram);
    }
    
    public void setFlutterR(final float n) {
        final float[] flutter = this.getFlutter();
        flutter[0] = n;
        this.setFlutter(flutter);
    }
    
    public void setFlutterG(final float n) {
        final float[] flutter = this.getFlutter();
        flutter[1] = n;
        this.setFlutter(flutter);
    }
    
    public void setFlutterB(final float n) {
        final float[] flutter = this.getFlutter();
        flutter[2] = n;
        this.setFlutter(flutter);
    }
    
    public void setFlutterMixed(final float n) {
        final float[] flutter = this.getFlutter();
        flutter[3] = n;
        this.setFlutter(flutter);
    }
    
    public float getType() {
        return this.j;
    }
    
    public void setType(final float j) {
        this.j = j;
    }
    
    public float getBarTotal() {
        return this.f;
    }
    
    public void setBarTotal(final float f) {
        this.f = f;
    }
    
    public float getBlockTotal() {
        return this.g;
    }
    
    public void setBlockTotal(final float g) {
        this.g = g;
    }
    
    public float getAnimation() {
        return this.h;
    }
    
    public void setAnimation(final float h) {
        this.h = h;
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("flutterR", this.getFlutter()[0], -1.0f, 1.0f);
        initParams.appendFloatArg("flutterG", this.getFlutter()[1], -1.0f, 1.0f);
        initParams.appendFloatArg("flutterB", this.getFlutter()[2], -1.0f, 1.0f);
        initParams.appendFloatArg("flutterMixed", this.getFlutter()[3], 0.0f, 1.0f);
        initParams.appendFloatArg("Type", this.getType(), 0.0f, 10.0f);
        initParams.appendFloatArg("barTotal", this.getBarTotal(), 0.0f, 10.0f);
        initParams.appendFloatArg("blockTotal", this.getBlockTotal(), 0.0f, 100.0f);
        initParams.appendFloatArg("animation", this.getAnimation(), 0.0f, 1.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("flutterR")) {
            this.setFlutterR(filterArg.getValue());
        }
        else if (filterArg.equalsKey("flutterG")) {
            this.setFlutterG(filterArg.getValue());
        }
        else if (filterArg.equalsKey("flutterB")) {
            this.setFlutterB(filterArg.getValue());
        }
        else if (filterArg.equalsKey("flutterMixed")) {
            this.setFlutterMixed(filterArg.getValue());
        }
        else if (filterArg.equalsKey("Type")) {
            this.setType(filterArg.getValue());
        }
        else if (filterArg.equalsKey("barTotal")) {
            this.setBarTotal(filterArg.getValue());
        }
        else if (filterArg.equalsKey("blockTotal")) {
            this.setBlockTotal(filterArg.getValue());
        }
        else if (filterArg.equalsKey("animation")) {
            this.setAnimation(filterArg.getValue());
        }
    }
    
    public void makeAnimationWithTime(final long n) {
        if (this.getAnimation() < 0.5) {
            return;
        }
        final long n2 = n % 1000000000L;
        final long[] array = { 0L, 50000000L, 100000000L, 150000000L, 200000000L, 250000000L, 300000000L, 340000000L, 380000000L, 420000000L };
        final float[] array2 = { 0.0f, 0.1f, 0.3f, 0.3f, 0.2f, 0.2f, 0.3f };
        final float[] array3 = { 0.0f, 0.1f, 0.2f, 0.2f, 0.3f, 0.3f, 0.4f };
        final float[] array4 = { 0.5f, 0.52f, 0.52f };
        final float[] array5 = { 0.51f, 0.53f, 0.54f };
        final float[] array6 = { 0.49f, 0.5f, 0.49f };
        if (n2 > array[array.length - 1]) {
            this.getParameter().setFilterArg("Type", 0.0f);
            this.getParameter().setFilterArg("barTotal", 0.0f);
            this.getParameter().setFilterArg("flutterR", 0.5f);
            this.getParameter().setFilterArg("flutterG", 0.5f);
            this.getParameter().setFilterArg("flutterB", 0.5f);
            this.getParameter().setFilterArg("flutterMixed", 0.0f);
        }
        for (int i = 0; i < 6; ++i) {
            if (array[i] < n2 && array[i + 1] >= n2) {
                this.getParameter().setFilterArg("Type", array3[i + 1]);
                this.getParameter().setFilterArg("barTotal", array2[i + 1]);
            }
        }
        for (int j = 6; j < array.length - 1; ++j) {
            if (array[j] < n2 && array[j + 1] >= n2) {
                this.getParameter().setFilterArg("Type", 0.0f);
                this.getParameter().setFilterArg("barTotal", 0.0f);
                this.getParameter().setFilterArg("flutterR", array4[j - 6]);
                this.getParameter().setFilterArg("flutterG", array5[j - 6] + ((this.k == j) ? 0.01f : 0.0f));
                this.getParameter().setFilterArg("flutterB", array6[j - 6]);
                this.getParameter().setFilterArg("flutterMixed", 1.0f);
                this.k = j;
            }
        }
        this.submitParameter();
    }
    
    private class TuSDKLiveSignalVertexBuild
    {
        public final int VERTEX_POSITION_SIZE = 2;
        public final int VERTEX_TEXTURECOORDINATE_SIZE = 2;
        public final int VERTEX_ELEMENT_POINTS = 6;
        private FloatBuffer b;
        private FloatBuffer c;
        private boolean d;
        private int e;
        private int f;
        private int g;
        private TuSdkSize h;
        private int i;
        private ImageOrientation j;
        
        public int getDrawTotal() {
            return 6 * this.e;
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
        
        public int getPositionSize() {
            return 2;
        }
        
        public int getTextureCoordinateSize() {
            return 2;
        }
        
        public int getElementPoints() {
            return 6;
        }
        
        public int getElementTotal() {
            return this.e;
        }
        
        public void setType(final int i) {
            this.i = i;
        }
        
        public void setBarTotal(final int f) {
            if (this.f == f) {
                return;
            }
            this.d = true;
            this.f = f;
        }
        
        public void setBlockTotal(final int g) {
            if (this.g == g) {
                return;
            }
            this.d = true;
            this.g = g;
        }
        
        public void setTextureSize(final TuSdkSize h) {
            if (h == null || h.equals(this.h)) {
                return;
            }
            this.d = true;
            this.h = h;
        }
        
        public void setRotation(final ImageOrientation j) {
            if (j == null || j == this.j) {
                return;
            }
            this.d = true;
            this.j = j;
        }
        
        public TuSDKLiveSignalVertexBuild() {
            this.e = 1;
            this.j = ImageOrientation.Up;
            this.a();
        }
        
        public void calculate() {
            if (this.d) {
                this.a();
            }
        }
        
        private int a(final ArrayList<RectF> list, final ArrayList<RectF> list2) {
            if (this.f <= 0 || this.h == null || !this.h.isSize()) {
                return 0;
            }
            final float[][] array = { { 0.0f, 0.8f, 1.0f, 0.7f } };
            final float[][] array2 = { { 0.0f, 0.98f, 1.0f, 0.88f }, { 0.0f, 0.65f, 0.5f, 0.55f }, { 0.5f, 0.65f, 0.75f, 0.2f } };
            final float[][] array3 = { { 0.5f, 0.95f, 1.0f, 0.85f }, { 0.0f, 0.06f, 1.0f, 0.0f } };
            final float[][] array4 = { { 0.0f, 1.0f, 0.1f, 0.2f }, { 0.5f, 0.7f, 1.0f, 0.6f }, { 0.15f, 0.2f, 1.0f, 0.15f } };
            float[][] array5 = null;
            switch (this.i) {
                case 0: {
                    array5 = new float[0][];
                    break;
                }
                case 1: {
                    array5 = array;
                    break;
                }
                case 2: {
                    array5 = array2;
                    break;
                }
                case 3: {
                    array5 = array3;
                    break;
                }
                case 4: {
                    array5 = array4;
                    break;
                }
                default: {
                    array5 = new float[0][];
                    break;
                }
            }
            int n = 0;
            for (int i = 0; i < array5.length; ++i) {
                list.add(new RectF(array5[i][0], array5[i][1], array5[i][2], array5[i][3]));
                list2.add(new RectF(array5[i][0], 1.0f - array5[i][3], array5[i][2], 1.0f - array5[i][1]));
                ++n;
            }
            return n;
        }
        
        private void a() {
            this.d = false;
            this.e = this.f + this.g + 1;
            this.b = ByteBuffer.allocateDirect(this.getDrawTotal() * this.getPositionSize() * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            this.c = ByteBuffer.allocateDirect(this.getDrawTotal() * this.getTextureCoordinateSize() * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            final float[] src = { -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f };
            final float[] textureCoordinates = SelesFilter.textureCoordinates(this.j);
            final float[] src2 = { textureCoordinates[0], textureCoordinates[1], textureCoordinates[2], textureCoordinates[3], textureCoordinates[4], textureCoordinates[5], textureCoordinates[2], textureCoordinates[3], textureCoordinates[4], textureCoordinates[5], textureCoordinates[6], textureCoordinates[7] };
            this.b.put(src);
            this.c.put(src2);
            final ArrayList<RectF> list = new ArrayList<RectF>(this.f + this.g);
            final ArrayList<RectF> list2 = new ArrayList<RectF>(this.f + this.g);
            if (this.a(list, list2) > 0) {
                final Iterator<RectF> iterator = list.iterator();
                while (iterator.hasNext()) {
                    this.a(this.b, iterator.next());
                }
                final Iterator<RectF> iterator2 = list2.iterator();
                while (iterator2.hasNext()) {
                    this.b(this.c, iterator2.next());
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
            final float[] textureCoordinates = RectHelper.textureCoordinates(this.j, rectF);
            floatBuffer.put(new float[] { textureCoordinates[0] * 0.8f, textureCoordinates[1] * 0.8f, textureCoordinates[2] * 0.8f, textureCoordinates[3] * 0.8f, textureCoordinates[4] * 0.8f, textureCoordinates[5] * 0.8f, textureCoordinates[2] * 0.8f, textureCoordinates[3] * 0.8f, textureCoordinates[4] * 0.8f, textureCoordinates[5] * 0.8f, textureCoordinates[6] * 0.8f, textureCoordinates[7] * 0.8f });
        }
    }
}
