// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesContext;
import android.graphics.PointF;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import android.util.AttributeSet;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;

public class SelesVRLeftRightView extends SelesBaseView
{
    private _VRLeftRightSurfacePusher a;
    
    public SelesVRLeftRightView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public SelesVRLeftRightView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public SelesVRLeftRightView(final Context context) {
        super(context);
    }
    
    @Override
    protected void initView(final Context context, final AttributeSet set) {
        super.initView(context, set);
    }
    
    @Override
    protected SelesSurfacePusher buildWindowDisplay() {
        if (this.a == null) {
            this.a = new _VRLeftRightSurfacePusher();
        }
        return this.a;
    }
    
    @Override
    protected SelesVerticeCoordinateBuilder buildVerticeCoordinateBuilder() {
        return null;
    }
    
    public void setRadius(final float radius) {
        if (this.a == null) {
            return;
        }
        this.a.setRadius(radius);
    }
    
    public void setScale(final float scale) {
        if (this.a == null) {
            return;
        }
        this.a.setScale(scale);
    }
    
    public void setCenter(final PointF center) {
        if (this.a == null) {
            return;
        }
        this.a.setCenter(center);
    }
    
    private class _VRLeftRightSurfacePusher extends SelesSurfacePusher
    {
        private int b;
        private int c;
        private int d;
        private float e;
        private float f;
        private PointF g;
        
        public _VRLeftRightSurfacePusher() {
            super("precision highp float;varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;uniform highp float aspectRatio;uniform highp vec4 aspheric;uniform highp vec4 aspectRegion;highp vec2 handleCorpCoord(highp vec2 coord, highp vec4 region){\t\tvec2 hCoord = vec2(coord);\t\tif(hCoord.y > 0.5){hCoord.y = hCoord.y - 0.5;}\t\thighp vec2 rCoord = (hCoord - region.xy) / region.zw;\t\trCoord.y = rCoord.y * 2.0;     return rCoord;}void main(){     vec2 cord = handleCorpCoord(textureCoordinate, aspectRegion);     highp vec2 textureCoordinateToUse = vec2(cord.x, (cord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));     highp float dist = distance(aspheric.xy, textureCoordinateToUse);     textureCoordinateToUse = cord;     if (dist < aspheric.z)     {        textureCoordinateToUse -= aspheric.xy;        highp float percent = 1.0 + ((0.5 - dist) / 0.5) * aspheric.w;        textureCoordinateToUse = textureCoordinateToUse * percent;        textureCoordinateToUse += aspheric.xy;\t\t   if(textureCoordinateToUse != clamp(textureCoordinateToUse, 0.0, 1.0)){        \t\tgl_FragColor = vec4(0.0);        }else{        \t\tgl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse);\t\t   }     }     else     {       gl_FragColor = vec4(0.0);     }}");
            this.e = 1.0f;
            this.g = new PointF(0.5f, 0.5f);
        }
        
        @Override
        protected void onInitOnGLThread() {
            super.onInitOnGLThread();
            this.b = this.mDisplayProgram.uniformIndex("aspectRatio");
            this.c = this.mDisplayProgram.uniformIndex("aspheric");
            this.d = this.mDisplayProgram.uniformIndex("aspectRegion");
            this.g = new PointF(0.5f, 0.5f);
            this.e = 1.0f;
            this.f = -0.12f;
            this.a();
            this.recalculateViewGeometry();
        }
        
        protected void recalculateViewGeometry() {
            SelesContext.setActiveShaderProgram(this.mDisplayProgram);
            GLES20.glUniform1f(this.b, this.mInputImageSize.height / (float)this.mInputImageSize.width);
            final TuSdkSize copy = SelesVRLeftRightView.this.mSizeInPixels.copy();
            final TuSdkSize create = TuSdkSize.create(copy.width, copy.height / 2);
            final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(this.mInputImageSize, new Rect(0, 0, create.width, create.height));
            GLES20.glUniform4fv(this.d, 1, FloatBuffer.wrap(new float[] { rectWithAspectRatioInsideRect.left / (float)create.width, rectWithAspectRatioInsideRect.top / (float)create.height, rectWithAspectRatioInsideRect.width() / (float)create.width, rectWithAspectRatioInsideRect.height() / (float)create.height }));
        }
        
        public void setRadius(final float e) {
            this.e = e;
            this.a();
        }
        
        public void setScale(final float f) {
            this.f = f;
            this.a();
        }
        
        public void setCenter(final PointF g) {
            if (g == null) {
                return;
            }
            this.g = g;
            this.a();
        }
        
        private void a() {
            this.runOnDraw(new Runnable() {
                @Override
                public void run() {
                    SelesContext.setActiveShaderProgram(_VRLeftRightSurfacePusher.this.mDisplayProgram);
                    GLES20.glUniform4fv(_VRLeftRightSurfacePusher.this.c, 1, FloatBuffer.wrap(new float[] { _VRLeftRightSurfacePusher.this.g.x, _VRLeftRightSurfacePusher.this.g.y, _VRLeftRightSurfacePusher.this.e, _VRLeftRightSurfacePusher.this.f }));
                }
            });
        }
    }
}
