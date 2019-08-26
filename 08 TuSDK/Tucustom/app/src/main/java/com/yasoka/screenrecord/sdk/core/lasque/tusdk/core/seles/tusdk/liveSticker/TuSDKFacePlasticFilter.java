// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker;

//import org.lasque.tusdk.core.utils.TLog;
import android.graphics.RectF;
import android.graphics.PointF;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesPointDrawFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.filters.SelesPointDrawFilter;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKFacePlasticFilter extends SelesFilter implements SelesParameters.FilterParameterInterface
{
    private int a;
    private int b;
    private int c;
    private int d;
    private float e;
    private float f;
    private float g;
    protected FaceAligment[] mFaces;
    protected float mDeviceAngle;
    private SelesPointDrawFilter h;
    private boolean i;
    
    public TuSDKFacePlasticFilter() {
        super("-sfbf3");
        this.e = 1.05f;
        this.f = 0.048f;
        this.g = 1.7777778f;
        this.mDeviceAngle = 0.0f;
        this.i = false;
        if (this.i) {
            this.addTarget(this.h = new SelesPointDrawFilter(), 0);
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.a = this.mFilterProgram.uniformIndex("eyePower");
        this.b = this.mFilterProgram.uniformIndex("chinPower");
        this.c = this.mFilterProgram.uniformIndex("screenRatio");
        this.d = this.mFilterProgram.uniformIndex("faceInfo");
        this.setEyeEnlargeSize(this.e);
        this.setChinSize(this.f);
        this.setScreenRatio(this.g);
        this.updateFaceFeatures(this.mFaces, this.mDeviceAngle);
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        this.checkGLError(this.getClass().getSimpleName());
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    }
    
    @Override
    public void setInputSize(final TuSdkSize tuSdkSize, final int n) {
        super.setInputSize(tuSdkSize, n);
        final float screenRatio = tuSdkSize.height / (float)tuSdkSize.width;
        if (screenRatio != this.g) {
            this.setScreenRatio(screenRatio);
        }
    }
    
    private RectF a(final PointF pointF, final PointF pointF2, final PointF pointF3, final float n) {
        final PointF pointF4 = new PointF(pointF.x, pointF.y);
        final PointF pointF5 = new PointF(pointF2.x, pointF2.y);
        final PointF pointF6 = new PointF(pointF3.x, pointF3.y);
        pointF4.y *= n;
        pointF5.y *= n;
        pointF6.y *= n;
        final float n2 = pointF4.x - pointF5.x;
        final float n3 = pointF4.y - pointF5.y;
        final float n4 = (float)(Math.pow(pointF4.x, 2.0) - Math.pow(pointF5.x, 2.0) + Math.pow(pointF4.y, 2.0) - Math.pow(pointF5.y, 2.0)) / 2.0f;
        final float n5 = pointF6.x - pointF5.x;
        final float n6 = pointF6.y - pointF5.y;
        final float n7 = (float)(Math.pow(pointF6.x, 2.0) - Math.pow(pointF5.x, 2.0) + Math.pow(pointF6.y, 2.0) - Math.pow(pointF5.y, 2.0)) / 2.0f;
        final float n8 = n2 * n6 - n5 * n3;
        float x;
        float y;
        if (n8 == 0.0f) {
            x = pointF4.x;
            y = pointF4.y;
        }
        else {
            x = (n4 * n6 - n7 * n3) / n8;
            y = (n2 * n7 - n5 * n4) / n8;
        }
        final float n9 = (float)Math.sqrt((x - pointF4.x) * (x - pointF4.x) + (y - pointF4.y) * (y - pointF4.y));
        return new RectF(x, y / n, n9, n9);
    }
    
    public void updateFaceFeatures(final FaceAligment[] mFaces, final float mDeviceAngle) {
        this.mFaces = mFaces;
        this.mDeviceAngle = mDeviceAngle;
        PointF[] marks = null;
        if (mFaces != null && mFaces.length > 0) {
            marks = mFaces[0].getMarks();
        }
        this.a(marks);
    }
    
    private void a(final PointF[] array) {
        final float[] array2 = new float[24];
        for (int i = 0; i < 24; ++i) {
            array2[i] = 0.0f;
        }
        if (array == null || array.length == 0) {
            this.setFloatArray(array2, this.d, this.mFilterProgram);
            return;
        }
        final PointF pointF = new PointF(array[0].x, array[0].y);
        final PointF pointF2 = new PointF(array[4].x, array[4].y);
        final PointF pointF3 = new PointF(array[8].x, array[8].y);
        final PointF pointF4 = new PointF(array[12].x, array[12].y);
        final PointF pointF5 = new PointF(array[16].x, array[16].y);
        final RectF a = this.a(pointF, pointF2, pointF3, this.getScreenRatio());
        final RectF a2 = this.a(pointF3, pointF4, pointF5, this.getScreenRatio());
        final PointF a3 = this.a(new int[] { 37, 38, 40, 41 }, array);
        final PointF a4 = this.a(new int[] { 43, 44, 46, 47 }, array);
        array2[0] = a3.x;
        array2[1] = a3.y;
        array2[2] = a4.x;
        array2[3] = a4.y;
        array2[4] = pointF.x;
        array2[5] = pointF.y;
        array2[6] = pointF3.x;
        array2[7] = pointF3.y;
        array2[8] = pointF5.x;
        array2[9] = pointF5.y;
        array2[10] = a.left;
        array2[11] = a.top;
        array2[12] = a.right;
        array2[13] = a2.left;
        array2[14] = a2.top;
        array2[15] = a2.right;
        final PointF pointF6 = new PointF();
        pointF6.x = array[36].x - (a3.x - array[36].x) * 2.5f;
        pointF6.y = array[36].y - (a3.y - array[36].y) * 2.5f;
        final PointF pointF7 = new PointF();
        pointF7.x = (array[37].x + array[38].x) * 0.5f;
        pointF7.y = (array[37].y + array[38].y) * 0.5f;
        pointF7.x -= (a3.x - pointF7.x) * 7.0f;
        pointF7.y -= (a3.y - pointF7.y) * 7.0f;
        array2[16] = pointF6.x;
        array2[17] = pointF6.y;
        array2[18] = pointF7.x;
        array2[19] = pointF7.y;
        final PointF pointF8 = new PointF();
        pointF8.x = array[45].x - (a4.x - array[45].x) * 2.5f;
        pointF8.y = array[45].y - (a4.y - array[45].y) * 2.5f;
        final PointF pointF9 = new PointF();
        pointF9.x = (array[43].x + array[44].x) * 0.5f;
        pointF9.y = (array[43].y + array[44].y) * 0.5f;
        pointF9.x -= (a4.x - pointF9.x) * 7.0f;
        pointF9.y -= (a4.y - pointF9.y) * 7.0f;
        array2[20] = pointF8.x;
        array2[21] = pointF8.y;
        array2[22] = pointF9.x;
        array2[23] = pointF9.y;
        this.setFloatArray(array2, this.d, this.mFilterProgram);
        this.a(array2);
    }
    
    private void a(final float[] array) {
        if (this.h == null) {
            return;
        }
        final PointF[] array2 = { new PointF(array[0], array[1]), new PointF(array[2], array[3]), new PointF(array[4], array[5]), new PointF(array[6], array[7]), new PointF(array[8], array[9]), new PointF(array[10], array[11]), new PointF(array[13], array[14]), new PointF(array[16], array[17]), new PointF(array[18], array[19]), new PointF(array[20], array[21]), new PointF(array[22], array[23]) };
        TLog.d("faceLeftCicleRadius: %f, faceRightCicleRadius: %f", array[16], array[19]);
        this.h.updateFaceFeatures(new FaceAligment[] { new FaceAligment(array2) }, 0.0f);
    }
    
    private PointF a(final int[] array, final PointF[] array2) {
        final PointF pointF = new PointF(0.0f, 0.0f);
        final int length = array.length;
        for (int i = 0; i < length; ++i) {
            final PointF pointF2 = array2[array[i]];
            final PointF pointF3 = pointF;
            pointF3.x += pointF2.x;
            final PointF pointF4 = pointF;
            pointF4.y += pointF2.y;
        }
        pointF.x /= length;
        pointF.y /= length;
        return pointF;
    }
    
    public float getScreenRatio() {
        return this.g;
    }
    
    public void setScreenRatio(final float g) {
        this.setFloat(this.g = g, this.c, this.mFilterProgram);
    }
    
    public void setEyeEnlargeSize(final float e) {
        this.e = e;
        this.setFloat((this.e == 0.0f) ? 0.0f : (1.0f - 1.0f / this.e), this.a, this.mFilterProgram);
    }
    
    public float getEyeEnlargeSize() {
        return this.e;
    }
    
    public void setChinSize(final float f) {
        this.f = f;
        this.setFloat(this.f * 2.0f, this.b, this.mFilterProgram);
    }
    
    public float getChinSize() {
        return this.f;
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("eyeSize", this.getEyeEnlargeSize(), 1.0f, 1.36f);
        initParams.appendFloatArg("chinSize", this.getChinSize(), 0.0f, 0.2f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("eyeSize")) {
            this.setEyeEnlargeSize(filterArg.getValue());
        }
        else if (filterArg.equalsKey("chinSize")) {
            this.setChinSize(filterArg.getValue());
        }
    }
}
