// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.skins;

//import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
//import org.lasque.tusdk.core.seles.sources.SelesPicture;
import java.util.List;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSurfaceBlurFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorMixedFilter;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSurfaceBlurFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorMixedFilter;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;

public class TuSDKSkinWhiteningFilter extends SelesFilterGroup implements SelesParameters.FilterFacePositionInterface, SelesParameters.FilterParameterInterface, SelesParameters.FilterTexturesInterface
{
    private float a;
    private float b;
    private float c;
    private float d;
    private float e;
    private float f;
    private TuSDKColorMixedFilter g;
    private TuSDKSurfaceBlurFilter h;
    private SelesFilter i;
    private _TuSDKSkinWhiteningFilter j;
    private _TuSDKGPUFaceBeautyFilter k;
    private boolean l;
    
    public TuSDKSkinWhiteningFilter() {
        this.f = 0.0f;
        this.addFilter(this.g = new TuSDKColorMixedFilter());
        (this.h = new TuSDKSurfaceBlurFilter()).setScale(0.5f);
        this.addFilter(this.h);
        (this.i = new SelesFilter()).setScale(0.5f);
        this.addFilter(this.i);
        this.addFilter(this.j = new _TuSDKSkinWhiteningFilter());
        this.addFilter(this.k = new _TuSDKGPUFaceBeautyFilter());
        this.j.addTarget(this.g, 0);
        this.h.addTarget(this.j, 1);
        this.i.addTarget(this.j, 2);
        this.g.addTarget(this.k, 0);
        this.setInitialFilters(this.h, this.i, this.j);
        this.setTerminalFilter(this.k);
        this.setSmoothing(0.3f);
        this.setWhitening(0.3f);
        this.setSkinColor(5000.0f);
        this.setEyeEnlargeSize(1.045f);
        this.setChinSize(0.048f);
        this.setRetouchSize(1.0f);
        this.h.setBlurSize(this.h.getMaxBlursize());
        this.h.setThresholdLevel(this.h.getMaxThresholdLevel());
        this.j.setLightLevel(0.4f);
        this.j.setDetailLevel(0.2f);
    }
    
    public float getSmoothing() {
        return this.a;
    }
    
    public void setSmoothing(final float a) {
        this.a = a;
        this.j.setIntensity(1.0f - a);
    }
    
    public float getWhitening() {
        return this.b;
    }
    
    public void setWhitening(final float b) {
        this.b = b;
        this.g.setMixed(this.b);
    }
    
    public float getSkinColor() {
        return this.c;
    }
    
    public void setSkinColor(final float n) {
        this.c = n;
        this.j.setTemperature(n);
    }
    
    public void setEyeEnlargeSize(final float n) {
        this.d = n;
        this.k.setEyeEnlargeSize(n);
    }
    
    public float getEyeEnlargeSize() {
        return this.d;
    }
    
    public void setChinSize(final float n) {
        this.e = n;
        this.k.setChinSize(n);
    }
    
    public float getChinSize() {
        return this.e;
    }
    
    public float getRetouchSize() {
        return this.f;
    }
    
    public void setRetouchSize(final float n) {
        final SelesParameters parameter = this.getParameter();
        this.setSmoothing(this.f = n);
        this.setWhitening(n);
        parameter.setFilterArg("retouchSize", n);
        parameter.setFilterArg("smoothing", n);
        parameter.setFilterArg("whitening", n);
        final float eyeEnlargeSize = 0.20000005f * n + 1.0f;
        this.setEyeEnlargeSize(eyeEnlargeSize);
        parameter.setFilterArg("eyeSize", (eyeEnlargeSize - 1.0f) / 0.2f);
        final float chinSize = 0.1f * n;
        this.setChinSize(chinSize);
        parameter.setFilterArg("chinSize", chinSize / 0.1f);
    }
    
    @Override
    public void updateFaceFeatures(final FaceAligment[] array, final float n) {
        PointF[] marks = null;
        if (array != null && array.length > 0) {
            marks = array[0].getMarks();
        }
        if (this.k != null && marks != null) {
            final PointF pointF = marks[0];
            final PointF pointF2 = marks[1];
            final PointF pointF3 = marks[3];
            final PointF pointF4 = marks[4];
            this.k.setFacePositions(pointF, pointF2, new PointF((pointF3.x + pointF4.x) / 2.0f, (pointF3.y + pointF4.y) / 2.0f));
        }
    }
    
    public void resetPosition() {
        if (this.k != null) {
            this.k.resetPosition();
        }
    }
    
    @Override
    public void appendTextures(final List<SelesPicture> list) {
        if (list == null) {
            return;
        }
        int n = 1;
        for (final SelesPicture selesPicture : list) {
            selesPicture.processImage();
            selesPicture.addTarget(this.g, n);
            ++n;
        }
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("retouchSize", this.getRetouchSize(), 0.0f, 1.0f);
        initParams.appendFloatArg("smoothing", this.getSmoothing(), 0.0f, 1.0f);
        initParams.appendFloatArg("whitening", this.getWhitening());
        initParams.appendFloatArg("skinColor", this.getSkinColor(), 4000.0f, 6000.0f);
        initParams.appendFloatArg("eyeSize", this.getEyeEnlargeSize(), 1.0f, 1.2f);
        initParams.appendFloatArg("chinSize", this.getChinSize(), 0.0f, 0.1f);
        return initParams;
    }
    
    @Override
    public void setParameter(final SelesParameters parameter) {
        this.l = true;
        super.setParameter(parameter);
        this.l = false;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("smoothing")) {
            this.setSmoothing(filterArg.getValue());
        }
        else if (filterArg.equalsKey("whitening")) {
            this.setWhitening(filterArg.getValue());
        }
        else if (filterArg.equalsKey("skinColor")) {
            this.setSkinColor(filterArg.getValue());
        }
        else if (filterArg.equalsKey("eyeSize")) {
            this.setEyeEnlargeSize(filterArg.getValue());
        }
        else if (filterArg.equalsKey("chinSize")) {
            this.setChinSize(filterArg.getValue());
        }
        else if (filterArg.equalsKey("retouchSize") && !this.l) {
            this.setRetouchSize(filterArg.getValue());
        }
    }
    
    private static class _TuSDKGPUFaceBeautyFilter extends SelesFilter
    {
        private int a;
        private int b;
        private int c;
        private int d;
        private int e;
        private float f;
        private float g;
        private PointF h;
        private PointF i;
        private PointF j;
        
        public _TuSDKGPUFaceBeautyFilter() {
            super("-sfbf1");
            this.f = 1.05f;
            this.g = 0.048f;
            this.h = new PointF(0.0f, 0.0f);
            this.i = new PointF(0.0f, 0.0f);
            this.j = new PointF(0.0f, 0.0f);
        }
        
        @Override
        protected void onInitOnGLThread() {
            super.onInitOnGLThread();
            this.a = this.mFilterProgram.uniformIndex("eyePower");
            this.b = this.mFilterProgram.uniformIndex("chinPower");
            this.c = this.mFilterProgram.uniformIndex("leftEyeCoordinate");
            this.d = this.mFilterProgram.uniformIndex("rightEyeCoordinate");
            this.e = this.mFilterProgram.uniformIndex("mouthCoordinate");
            this.setFacePositions(this.h, this.i, this.j);
            this.setEyeEnlargeSize(this.f);
            this.setChinSize(this.g);
        }
        
        public void setFacePositions(final PointF h, final PointF i, final PointF j) {
            this.h = h;
            this.i = i;
            this.j = j;
            this.setPoint(h, this.c, this.mFilterProgram);
            this.setPoint(i, this.d, this.mFilterProgram);
            this.setPoint(j, this.e, this.mFilterProgram);
        }
        
        public void resetPosition() {
            this.setFacePositions(new PointF(0.0f, 0.0f), new PointF(0.0f, 0.0f), new PointF(0.0f, 0.0f));
        }
        
        public void setEyeEnlargeSize(final float f) {
            this.setFloat(this.f = f, this.a, this.mFilterProgram);
        }
        
        public void setChinSize(final float g) {
            this.setFloat(this.g = g, this.b, this.mFilterProgram);
        }
    }
    
    private static class _TuSDKSkinWhiteningFilter extends SelesThreeInputFilter
    {
        private int a;
        private int b;
        private int c;
        private int d;
        private int e;
        private float f;
        private float g;
        private float h;
        private float i;
        
        public _TuSDKSkinWhiteningFilter() {
            super("-sscf7");
            this.f = 1.0f;
            this.g = 5000.0f;
            this.h = 0.22f;
            this.i = 0.7f;
        }
        
        @Override
        protected void onInitOnGLThread() {
            super.onInitOnGLThread();
            this.a = this.mFilterProgram.uniformIndex("intensity");
            this.b = this.mFilterProgram.uniformIndex("temperature");
            this.c = this.mFilterProgram.uniformIndex("enableSkinColorDetection");
            this.d = this.mFilterProgram.uniformIndex("lightLevel");
            this.e = this.mFilterProgram.uniformIndex("detailLevel");
            this.setIntensity(this.f);
            this.setTemperature(this.g);
            this.setEnableSkinColorDetection(0.0f);
            this.setLightLevel(this.h);
            this.setDetailLevel(this.i);
        }
        
        public void setIntensity(final float f) {
            this.setFloat(this.f = f, this.a, this.mFilterProgram);
        }
        
        public void setTemperature(final float g) {
            this.g = g;
            this.setFloat((g < 5000.0f) ? ((float)(4.0E-4 * (g - 5000.0))) : ((float)(6.0E-5 * (g - 5000.0))), this.b, this.mFilterProgram);
        }
        
        public void setEnableSkinColorDetection(final float n) {
            this.setFloat(n, this.c, this.mFilterProgram);
        }
        
        public void setLightLevel(final float h) {
            this.setFloat(this.h = h, this.d, this.mFilterProgram);
        }
        
        public void setDetailLevel(final float i) {
            this.setFloat(this.i = i, this.e, this.mFilterProgram);
        }
    }
}
